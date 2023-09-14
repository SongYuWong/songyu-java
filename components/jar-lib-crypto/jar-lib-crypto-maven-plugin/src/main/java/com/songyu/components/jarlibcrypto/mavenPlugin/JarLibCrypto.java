package com.songyu.components.jarlibcrypto.mavenPlugin;

import cn.hutool.crypto.CipherMode;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.songyu.commonutils.*;
import com.songyu.commonutils.exception.*;
import com.songyu.components.jarlibcrypto.core.UIUtils;
import com.songyu.components.jarlibcrypto.core.JoseUtils;
import javassist.*;
import javassist.bytecode.*;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static com.songyu.commonutils.CommonFileUtils.deleteFile;
import static com.songyu.components.jarlibcrypto.core.JoseUtils.generateToken;

/**
 * <p>
 * jar lib 包加密插件
 * </p>
 *
 * @author songYu
 * @since 2023/9/5 14:22
 */
@Mojo(name = "JarLibCrypto", defaultPhase = LifecyclePhase.INSTALL)
public class JarLibCrypto extends AbstractMojo {

    private Log logger;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    private static final String TOP_PROJECT_GROUP_ID = "com.songyu";

    private static final String JAR = "jar";

    private static final String MAIN_CLASS_PREFIX = "Main-Class: ";

    private static final HashSet<String> CONFIG_TO_BE_DEL = new HashSet<String>() {{
        add("pom.xml");
        add("pom.properties");
    }};

    private File encryptedDir;

    private File encryptedTemp;

    private File clearedTemp;

    private File configTemp;

    private File outPutDir;

    private String password;

    private Long begin;

    private Long end;

    private RsaJsonWebKey rsaJsonWebKey;

    private ClassPool classPool;

    private Set<String> cryptoLibJarNames = new HashSet<>();

    private Set<String> denLibJarNames = new HashSet<>();

    @Override
    public void execute() {
        password = UIUtils.readPassword();
        begin = UIUtils.readTimeBegin();
        end = UIUtils.readTimeEnd(begin);
        rsaJsonWebKey = JoseUtils.generateJWK(project.getArtifactId().concat(project.getVersion()));
        logger = getLog();
        Build build = project.getBuild();
        outPutDir = new File(build.getDirectory().concat(File.separator).concat("jar-lib-crypto"));
        if (outPutDir.exists()) {
            boolean ignore1 = outPutDir.delete();
            boolean ignore2 = outPutDir.mkdir();
        }
        String mainJar = build.getDirectory()
                .concat(File.separator).concat(build.getFinalName()).concat(".").concat(project.getPackaging());
        logger.info("待加密的 jar : " + mainJar);
        listCryptoJarLibNames();
        for (String cryptoLibJarName : cryptoLibJarNames) {
            logger.info("待加密的 jar : " + cryptoLibJarName);
        }
        classPool = ClassPool.getDefault();
        loadAllClasses(mainJar);
        prepareTempDirs(build);
        encrypt(mainJar);
        cryptoLibJarNames.forEach(cryptoLibJarName -> {
            prepareTempDirs(build);
            encrypt(cryptoLibJarName, "lib");
        });
        try {
            deleteFile(clearedTemp);
            deleteFile(encryptedTemp);
            deleteFile(configTemp);
        } catch (FileDeleteException | MaxFileLevelException | SourceNullException e) {
            logger.error("删除临时文件失败" + e.getMessage());
        }
        File file = new File(outPutDir, "lib");
        Path target = file.toPath();
        denLibJarNames.forEach(path -> {
            try {
                Path source = Paths.get(path);
                Files.copy(source, target.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("第三方lib复制失败");
            }
        });
    }

    private void prepareTempDirs(Build build) {
        try {
            encryptedTemp = CommonFileUtils.reMkdirs(build.getDirectory(), ".ENCRYPTED_TEMP");
        } catch (FileCreateException | SourceNullException e) {
            logger.error("加密临时文件新建失败" + e.getMessage());
        } catch (FileDeleteException e) {
            logger.error("删除加密临时文件失败" + e.getMessage());
        }
        try {
            encryptedDir = CommonFileUtils.reMkdirs(encryptedTemp, "META-INF", ".C++");
        } catch (FileCreateException | SourceNullException e) {
            logger.error("类加密临时文件新建失败" + e.getMessage());
        } catch (FileDeleteException e) {
            logger.error("删除类加密临时文件失败" + e.getMessage());
        }
        try {
            clearedTemp = CommonFileUtils.reMkdirs(build.getDirectory(), ".CLEARED_TEMP");
        } catch (FileCreateException | SourceNullException e) {
            logger.error("清除方法实现临时文件新建失败" + e.getMessage());
        } catch (FileDeleteException e) {
            logger.error("删除清除方法实现临时文件失败" + e.getMessage());
        }
        try {
            configTemp = CommonFileUtils.reMkdirs(build.getDirectory(), ".CONFIG_TEMP");
        } catch (FileCreateException | SourceNullException e) {
            logger.error("配置临时文件新建失败" + e.getMessage());
        } catch (FileDeleteException e) {
            logger.error("删除配置临时文件失败" + e.getMessage());
        }
    }

    private void encrypt(String cryptoLibJarName, String... parentPaths) {
        logger.info("开始加密：" + cryptoLibJarName);
        try {
            CommonZipUtils.openZipFile(cryptoLibJarName, jarFile -> {
                Enumeration<? extends ZipEntry> entries = jarFile.entries();
                String mainClassName = parseMainClassName(jarFile);
                if (mainClassName != null) {
                    while (entries.hasMoreElements()) {
                        ZipEntry zipEntry = entries.nextElement();
                        if (!zipEntry.isDirectory()) {
                            if (zipEntry.getName().endsWith(".class")) {
                                String className = pathToClassName(zipEntry.getName());
                                if (!className.equals(mainClassName)) {
                                    clearMethodsBody(className, zipEntry.getName());
                                    encryptClassBytes(jarFile, zipEntry, className);
                                } else {
                                    logger.info("转移项目启动 main class：" + mainClassName);
                                    zipEntryToTemp(jarFile, zipEntry, clearedTemp);
                                }
                            } else {
                                zipEntryToTemp(jarFile, zipEntry, configTemp);
                            }
                        }
                    }
                } else {
                    while (entries.hasMoreElements()) {
                        ZipEntry zipEntry = entries.nextElement();
                        if (!zipEntry.isDirectory()) {
                            if (zipEntry.getName().endsWith(".class")) {
                                String className = pathToClassName(zipEntry.getName());
                                encryptClassBytes(jarFile, zipEntry, className);
                                clearMethodsBody(className, zipEntry.getName());
                            } else {
                                zipEntryToTemp(jarFile, zipEntry, configTemp);
                            }
                        }
                    }
                }
            });
        } catch (FileReadException e) {
            logger.error("读取 jar 文件失败" + e.getMessage());
        }
        File output;
        if (CommonArrayUtils.isNotEmpty(parentPaths)) {
            output = new File(outPutDir, parentPaths[0]);
            for (int i = 1; i < parentPaths.length; i++) {
                output = new File(output, parentPaths[i]);
            }
        } else {
            output = outPutDir;
        }
        tempJar(output, cryptoLibJarName);
    }

    private void zipEntryToTemp(ZipFile jarFile, ZipEntry zipEntry, File tempFile) {
        byte[] bytes;
        try {
            bytes = CommonZipUtils.readZipEntry(jarFile, zipEntry);
        } catch (FileReadException e) {
            logger.error("读取 jar 中的文件失败" + e.getMessage());
            throw new RuntimeException(e);
        }
        String[] split = zipEntry.getName().split("/");
        String fileName = split[split.length - 1];
        if (CONFIG_TO_BE_DEL.contains(fileName)) {
            return;
        }
        File file = compactPathWithTemp(split, tempFile);
        try {
            CommonFileUtils.writeFile(bytes, file, fileName);
        } catch (FileWriteException | FileCreateException e) {
            logger.error(fileName + " 输出到临时文件失败");
        }
    }

    private File compactPathWithTemp(String[] split, File tempFile) {
        String dirName = "";
        for (int i = 0; i < split.length - 1; i++) {
            dirName = dirName.concat("\\").concat(split[i]);
        }
        File file = new File(tempFile, dirName);
        boolean ignore = file.mkdirs();
        return file;
    }

    private void tempJar(File output, String jarPath) {
        if (output.exists()) {
            boolean ignore1 = output.delete();
        }
        boolean ignore2 = output.mkdirs();
        LinkedList<File> files = new LinkedList<>();
        try {
            CommonFileUtils.listAllLevelFiles(files, clearedTemp);
            CommonFileUtils.listAllLevelFiles(files, encryptedDir);
            CommonFileUtils.listAllLevelFiles(files, configTemp);
        } catch (SourceNullException e) {
            logger.error("扫描要打包的文件失败" + e.getMessage());
        }
        String[] jarPaths = jarPath.split("\\\\");
        File outfile = new File(output, jarPaths[jarPaths.length - 1]);
        try {
            boolean ignore3 = outfile.createNewFile();
        } catch (IOException e) {
            logger.error(e);
        }
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(outfile);
                ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        ) {
            for (File file : files) {
                String fileName = file.getAbsolutePath();
                if (fileName.equals(clearedTemp.getAbsolutePath()) || fileName.equals(encryptedTemp.getAbsolutePath())) {
                    continue;
                }
                if (fileName.contains(clearedTemp.getAbsolutePath())) {
                    fileName = fileName.substring(clearedTemp.getAbsolutePath().length() + 1);
                } else if (fileName.contains(encryptedTemp.getAbsolutePath())) {
                    fileName = fileName.substring(encryptedTemp.getAbsolutePath().length() + 1);
                } else if (fileName.contains(configTemp.getAbsolutePath())) {
                    if (fileName.equals(configTemp.getAbsolutePath())) {
                        continue;
                    }
                    fileName = fileName.substring(configTemp.getAbsolutePath().length() + 1);
                }
                fileName = fileName.replace(File.separator, "/");
                //目录，添加一个目录entry
                if (file.isDirectory()) {
                    ZipEntry ze = new ZipEntry(fileName + "/");
                    ze.setTime(System.currentTimeMillis());
                    zipOutputStream.putNextEntry(ze);
                    zipOutputStream.closeEntry();
                } else {
                    ZipEntry ze = new ZipEntry(fileName);
                    ze.setTime(System.currentTimeMillis());
                    zipOutputStream.putNextEntry(ze);
                    try (
                            FileInputStream fileInputStream = new FileInputStream(file);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
                    ) {
                        byte[] buffer = new byte[1024];
                        int readLength;
                        while ((readLength = fileInputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, readLength);
                        }
                        byteArrayOutputStream.flush();
                        zipOutputStream.write(byteArrayOutputStream.toByteArray());
                        zipOutputStream.closeEntry();
                    }
                }
            }
            logger.info("打包成功 << " + outfile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("打包失败");
        }
    }

    private void clearMethodsBody(String className, String fileName) {
        try {
            CtClass ctClass = classPool.get(className);
            CtMethod[] methods = ctClass.getMethods();
            for (CtMethod ctMethod : methods) {
                if (!ctMethod.getName().contains("<") && ctMethod.getLongName().startsWith(ctClass.getName())) {
                    CodeAttribute codeAttribute = ctMethod.getMethodInfo().getCodeAttribute();
                    //接口的ca就是null,方法体本来就是空的就是-79
                    if (codeAttribute != null && codeAttribute.getCodeLength() != 1 && codeAttribute.getCode()[0] != -79) {
                        if (ctClass.isFrozen()) {
                            logger.error(ctClass.getName() + " class is frozen");
                        }
                        CodeIterator iterator = codeAttribute.iterator();
                        Javac jv = new Javac(ctClass);
                        int numOfLocalVars = jv.recordParams(ctMethod.getParameterTypes(), Modifier.isStatic(ctMethod.getModifiers()));
                        jv.recordParamNames(codeAttribute, numOfLocalVars);
                        jv.recordLocalVariables(codeAttribute, 0);
                        jv.recordReturnType(Descriptor.getReturnType(ctMethod.getMethodInfo().getDescriptor(), ctClass.getClassPool()), false);
                        Bytecode b = jv.compileBody(ctMethod, null);
                        int stack = b.getMaxStack();
                        int locals = b.getMaxLocals();
                        if (stack > codeAttribute.getMaxStack()) {
                            codeAttribute.setMaxStack(stack);
                        }
                        if (locals > codeAttribute.getMaxLocals()) {
                            codeAttribute.setMaxLocals(locals);
                        }
                        int pos = iterator.insertEx(b.get());
                        iterator.insert(b.getExceptionTable(), pos);
                        ctMethod.getMethodInfo().rebuildStackMapIf6(ctClass.getClassPool(), ctClass.getClassFile2());
                    }
                }
            }
            byte[] bytes = ctClass.toBytecode();
            String[] split = fileName.split("/");
            File file = compactPathWithTemp(split, clearedTemp);
            try {
                CommonFileUtils.writeFile(bytes, file, split[split.length - 1]);
            } catch (FileWriteException | FileCreateException e) {
                logger.error(className + "方法清除文件保存失败：" + e.getMessage());
            }
            logger.info("方法清除处理成功：" + className);
        } catch (NotFoundException | CannotCompileException | IOException | BadBytecode | CompileError e) {
            logger.error(className + "方法清除处理失败：" + e.getMessage());
        }
    }

    private void encryptClassBytes(ZipFile jarFile, ZipEntry zipEntry, String className) {
        byte[] bytes;
        try {
            bytes = CommonZipUtils.readZipEntry(jarFile, zipEntry);
        } catch (FileReadException e) {
            logger.error("读取 jar 中的类文件失败" + e.getMessage());
            throw new RuntimeException(e);
        }
        byte[] key = parseKey(password, className, CommonRandomUtils.randomString(8));
        byte[] iv = parseIv(className);
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("z", key);
        payload.put("l", iv);
        String token = generateToken(rsaJsonWebKey.getPrivateKey(), rsaJsonWebKey.getKeyId(), className, end, begin, payload);
        try {
            CommonFileUtils.writeFile(Base64.getEncoder().encode(token.getBytes(StandardCharsets.UTF_8)), encryptedDir, String.valueOf(className.hashCode()).concat(".h"));
        } catch (FileWriteException | FileCreateException e) {
            logger.error("输出加密文件 token 失败");
        }
        byte[] sec = new AES(Mode.CBC, Padding.PKCS5Padding, key, iv).encrypt(bytes);
        try {
            CommonFileUtils.writeFile(Base64.getEncoder().encode(sec), encryptedDir, String.valueOf(className.hashCode()).concat(".c"));
        } catch (FileWriteException | FileCreateException e) {
            logger.error("输出加密文件失败");
        }
        logger.info("加密处理成功：" + className);
    }

    private byte[] parseKey(Object... o) {
        return CommonByteArrayUtils.parseBytes(32, o);
    }

    private byte[] parseIv(Object... o) {
        return CommonByteArrayUtils.parseBytes(16, o);
    }

    private String pathToClassName(String path) {
        return path.replaceAll("/", ".").replaceFirst(".class", "");
    }

    private String parseMainClassName(ZipFile jarFile) {
        ZipEntry entry = jarFile.getEntry("META-INF/MANIFEST.MF");
        if (entry != null) {
            try (InputStream inputStream = jarFile.getInputStream(entry);
                 InputStreamReader in = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(in)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.startsWith(MAIN_CLASS_PREFIX)) {
                        return line.split(MAIN_CLASS_PREFIX)[1];
                    }
                }
            } catch (IOException e) {
                logger.warn(String.format("%s 读取类清单文件失败", jarFile.getName()));
                return null;
            }
        }
        return null;
    }

    private void loadAllClasses(String mainJarName) {
        insertClassPath(classPool, mainJarName);
        File[] libs = new File(project.getBuild().getDirectory().concat(File.separator).concat("lib")).listFiles();
        if (libs != null) {
            for (File lib : libs) {
                insertClassPath(classPool, lib.getAbsolutePath());
            }
        }
    }

    private void insertClassPath(ClassPool classPool, String path) {
        try {
            classPool.insertClassPath(path);
        } catch (NotFoundException e) {
            logger.error(String.format("未找到 jar 包: %s", path));
            System.exit(1);
        }
    }

    private void listCryptoJarLibNames() {
        File[] libs = new File(project.getBuild().getDirectory().concat(File.separator).concat("lib")).listFiles();
        if (libs != null) {
            MavenProject topProject = getTopProject(project);
            DependencyManagement dependencyManagement = topProject.getDependencyManagement();
            List<Dependency> dependencies = dependencyManagement.getDependencies();
            HashSet<String> libFileNames = new HashSet<>();
            for (File lib : libs) {
                libFileNames.add(lib.getAbsolutePath());
            }
            dependencies.forEach(dependency -> {
                if (dependency.getGroupId().startsWith(TOP_PROJECT_GROUP_ID) && JAR.equals(dependency.getType())) {
                    String dependencyName = project.getBuild().getDirectory().concat(File.separator).concat("lib")
                            .concat(File.separator).concat(dependency.getArtifactId()).concat("-")
                            .concat(dependency.getVersion()).concat(".jar");
                    if (libFileNames.contains(dependencyName)) {
                        cryptoLibJarNames.add(dependencyName);
                    }
                }
            });
            libFileNames.removeAll(cryptoLibJarNames);
            denLibJarNames = libFileNames;
        } else {
            cryptoLibJarNames = Collections.emptySet();
        }
    }

    private MavenProject getTopProject(MavenProject project) {
        if (project.hasParent()) {
            return getTopProject(project.getParent());
        } else {
            return project;
        }
    }

}
