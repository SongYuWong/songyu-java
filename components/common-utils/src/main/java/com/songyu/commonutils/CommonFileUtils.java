package com.songyu.commonutils;

import com.songyu.commonutils.exception.*;

import java.io.*;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * <p>
 * 通用文件工具类
 * </p>
 *
 * @author songYu
 * @since 2023/9/12 17:51
 */
public class CommonFileUtils {

    /**
     * 递归删除文件以及其子文件
     *
     * @param file  对应文件
     * @param level 文件层级
     */
    public static void deleteFile(File file, int level) throws FileDeleteException, MaxFileLevelException, SourceNullException {
        CommonParamCheckUtils.notNull(file, "要删除的文件不能为 null");
        if (level == 0) {
            throw new MaxFileLevelException("超出最大递归删除文件层级", level);
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (CommonArrayUtils.isNotEmpty(files)) {
                for (File f : files) {
                    deleteFile(f, --level);
                }
            }
        }
        if (!file.delete()) {
            throw new FileDeleteException("文件删除失败");
        }
    }

    /**
     * 递归删除文件以及其子文件
     *
     * @param file 对应文件
     */
    public static void deleteFile(File file) throws FileDeleteException, MaxFileLevelException, SourceNullException {
        deleteFile(file, 100);
    }

    /**
     * 扫描 java 文件包括文件夹并将结果保存到结果集容器中
     *
     * @param files 结果集容器
     * @param file  被扫描的 java 文件
     * @throws SourceNullException 操作资源不存在异常
     */
    public static void listAllLevelFiles(LinkedList<File> files, File file) throws SourceNullException {
        CommonParamCheckUtils.notNull(files, "扫描文件结果集容器不能为 null");
        CommonParamCheckUtils.notNull(file, "要扫描文件不能为 null");
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (CommonArrayUtils.isNotEmpty(listFiles)) {
                for (File listFile : listFiles) {
                    listAllLevelFiles(files, listFile);
                }
            }
        } else {
            files.add(file);
        }
    }


    /**
     * 扫描 java 文件
     *
     * @param file 被扫描的 java 文件
     * @return java 文件中扫描出来的文件对象包括文件夹
     */
    public static LinkedList<File> listAllLevelFiles(File file) {
        if (file == null) {
            return new LinkedList<>();
        }
        LinkedList<File> files = new LinkedList<>();
        try {
            listAllLevelFiles(files, file);
        } catch (SourceNullException e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    /**
     * 创建文件夹 java 文件对象
     *
     * @param paths 文件路径 索引越小层级越高
     * @return java 文件对象
     */
    public static File mkdirs(String... paths) throws FileCreateException, FileMetaException, SourceNullException {
        File file = pathsToFile(paths);
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new FileMetaException("文件已存在且不是文件夹", file);
            }
        } else {
            if (!file.mkdirs()) {
                throw new FileCreateException("文件夹创建异常");
            }
        }
        return file;
    }

    /**
     * 创建文件夹 java 文件对象
     *
     * @param paths 文件路径 索引越小层级越高
     * @return java 文件对象
     */
    public static File mkdirs(File parent, String... paths) throws FileCreateException, FileMetaException {
        for (String path : paths) {
            parent = new File(parent, path);
        }
        if (parent.exists()) {
            if (!parent.isDirectory()) {
                throw new FileMetaException("文件已存在且不是文件夹", parent);
            }
        } else {
            if (!parent.mkdirs()) {
                throw new FileCreateException("文件夹创建异常");
            }
        }
        return parent;
    }

    /**
     * 重新创建文件夹 java 文件对象
     * <p>文件存在将会删除文件重新创建文件夹</p>
     *
     * @param paths 文件路径 索引越小层级越高
     * @return java 文件对象
     */
    public static File reMkdirs(String... paths) throws FileCreateException, SourceNullException, FileDeleteException {
        File file = pathsToFile(paths);
        if (file.exists()) {
            try {
                deleteFile(file);
            } catch (MaxFileLevelException e) {
                throw new FileDeleteException("原有文件删除失败", e);
            }
            try {
                file = mkdirs(paths);
            } catch (FileMetaException e) {
                file = e.reMkdirs();
            }
        } else {
            if (!file.mkdirs()) {
                throw new FileCreateException("文件夹创建异常");
            }
        }
        return file;
    }

    /**
     * 重新创建文件夹 java 文件对象
     * <p>文件存在将会删除文件重新创建文件夹</p>
     *
     * @param paths 文件路径 索引越小层级越高
     * @return java 文件对象
     */
    public static File reMkdirs(File parent, String... paths) throws FileCreateException, SourceNullException, FileDeleteException {
        CommonParamCheckUtils.notNull(parent, "父级文件夹不存在");
        File result = new File(parent.toURI());
        for (String path : paths) {
            result = new File(parent, path);
        }
        if (result.exists()) {
            try {
                deleteFile(result);
            } catch (MaxFileLevelException e) {
                throw new FileDeleteException("原有文件删除失败", e);
            }
            try {
                result = mkdirs(parent, paths);
            } catch (FileMetaException e) {
                result = e.reMkdirs();
            }
        } else {
            if (!result.mkdirs()) {
                throw new FileCreateException("文件夹创建异常");
            }
        }
        return result;
    }

    /**
     * 创建文件 java 文件对象
     *
     * @param paths 文件路径 索引越小层级越高
     * @return java 文件对象
     */
    public static File createFile(String... paths) throws FileCreateException, FileMetaException, SourceNullException {
        return createNewFileIfNotExists(pathsToFile(paths));
    }

    /**
     * 路径数组转多层级文件
     *
     * @param paths 层级路径数组
     * @return 多层级文件
     * @throws SourceNullException 路径数组不能为空
     */
    public static File pathsToFile(String[] paths) throws SourceNullException {
        if (CommonArrayUtils.isEmpty(paths)) {
            throw new SourceNullException("文件的路径不能为空");
        }
        File file = new File(paths[0]);
        for (int i = 1; i < paths.length; i++) {
            file = new File(file, paths[i]);
        }
        return file;
    }

    /**
     * 创建文件夹 java 文件对象
     *
     * @param paths 文件路径 索引越小层级越高
     * @return java 文件对象
     */
    public static File createFile(File parent, String... paths) throws FileCreateException, FileMetaException, SourceNullException {
        CommonParamCheckUtils.notNull(parent, "父级文件夹不存在");
        File result = new File(parent.toURI());
        for (String path : paths) {
            result = new File(result, path);
        }
        return createNewFileIfNotExists(result);
    }

    /**
     * 如果文件不存在创建文件
     *
     * @param file 文件
     * @return 创建了的文件
     * @throws FileMetaException   文件元数据错误
     * @throws FileCreateException 文件创建失败
     */
    private static File createNewFileIfNotExists(File file) throws FileMetaException, FileCreateException, SourceNullException {
        CommonParamCheckUtils.notNull(file, "要创建的文件不能为 null");
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new FileMetaException("文件已存在且是文件夹", file);
            }
        } else {
            File parentFile = file.getParentFile();
            try {
                mkdirs(parentFile);
            } catch (FileCreateException | FileMetaException e) {
                throw new FileCreateException("文件的父级文件夹创建失败", e);
            }
            try {
                if (!file.createNewFile()) {
                    throw new FileCreateException("文件创建异常");
                }
            } catch (IOException e) {
                throw new FileCreateException("文件创建异常", e);
            }
        }
        return file;
    }

    /**
     * 重新创建文件夹 java 文件对象
     * <p>文件存在将会删除文件重新创建文件夹</p>
     *
     * @param paths 文件路径 索引越小层级越高
     * @return java 文件对象
     */
    public static File reCreateNewFile(String... paths) throws FileCreateException, SourceNullException, FileDeleteException {
        File file = pathsToFile(paths);
        if (file.exists()) {
            try {
                deleteFile(file);
            } catch (MaxFileLevelException e) {
                throw new FileDeleteException("原有文件删除失败", e);
            }
            try {
                file = createFile(paths);
            } catch (FileMetaException e) {
                file = e.reCreateFile();
            }
        } else {
            try {
                if (!file.createNewFile()) {
                    throw new FileCreateException("文件创建异常");
                }
            } catch (IOException e) {
                throw new FileCreateException("文件创建异常", e);
            }
        }
        return file;
    }

    /**
     * 重新创建文件夹 java 文件对象
     * <p>文件存在将会删除文件重新创建文件夹</p>
     *
     * @param paths 文件路径 索引越小层级越高
     * @return java 文件对象
     */
    public static File reCreateNewFile(File parent, String... paths) throws FileCreateException, SourceNullException, FileDeleteException {
        CommonParamCheckUtils.notNull(parent, "父级文件夹不能为 null");
        File result = new File(parent.toURI());
        for (String path : paths) {
            result = new File(parent, path);
        }
        if (result.exists()) {
            try {
                deleteFile(result);
            } catch (MaxFileLevelException e) {
                throw new FileDeleteException("原有文件删除失败", e);
            }
            try {
                result = createFile(parent, paths);
            } catch (FileMetaException e) {
                result = e.reCreateFile();
            }
        } else {
            try {
                if (!result.createNewFile()) {
                    throw new FileCreateException("文件创建异常");
                }
            } catch (IOException e) {
                throw new FileCreateException("文件创建异常", e);
            }
        }
        return result;
    }

    /**
     * 写入文件
     *
     * @param bytes 要写入的数据
     * @param paths 文件路径
     * @throws FileWriteException  文件写入异常
     * @throws FileCreateException 新建写入的文件异常
     */
    public static void writeFile(byte[] bytes, String... paths) throws FileWriteException, FileCreateException {
        File file;
        try {
            file = reCreateNewFile(paths);
        } catch (FileCreateException | SourceNullException | FileDeleteException e) {
            throw new FileCreateException("创建写入的文件失败", e);
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
        } catch (IOException e) {
            throw new FileWriteException();
        }
    }

    /**
     * 写入文件
     *
     * @param bytes  写入的数据
     * @param parent 父级文件夹
     * @param paths  文件路径
     * @throws FileWriteException  文件写入异常
     * @throws FileCreateException 新建写入的文件异常
     */
    public static void writeFile(byte[] bytes, File parent, String... paths) throws FileWriteException, FileCreateException {
        File file;
        try {
            file = reCreateNewFile(parent, paths);
        } catch (FileCreateException | SourceNullException | FileDeleteException e) {
            throw new FileCreateException("创建写入的文件失败", e);
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
        } catch (IOException e) {
            throw new FileWriteException();
        }
    }

    /**
     * 获取系统资源路径下的文件 resource 中的文件
     *
     * @param paths 层级文件名称
     * @return 文件对象
     */
    public static File getClassPathFile(String... paths) {
        if (paths == null || paths.length == 0) {
            return null;
        } else {
            String[] pathList = new String[paths.length + 1];
            pathList[0] = ClassLoader.getSystemResource(".").getPath();
            System.arraycopy(paths, 0, pathList, 1, paths.length);
            try {
                return pathsToFile(pathList);
            } catch (SourceNullException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取类资源路径下的文件输入流，并执行某些操作
     *
     * @param consumer 文件输入流操作
     * @param paths    相对类文件路径
     */
    public static void doWithClassPathFileInputStream(Consumer<FileInputStream> consumer, String... paths) {
        File classPathFile = getClassPathFile(paths);
        try (FileInputStream fileInputStream = new FileInputStream(classPathFile)) {
            consumer.accept(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取类资源路径下的文件文本内容
     *
     * @return 文本内容
     */
    public static String readClassPathFileContent(String... paths) {
        File classPathFile = getClassPathFile(paths);
        String content = "";
        try (FileInputStream fileInputStream = new FileInputStream(classPathFile);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                content = content.concat(temp);
            }
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
