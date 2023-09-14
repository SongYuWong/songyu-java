package com.songyu.commonutils;

import com.songyu.commonutils.exception.FileReadException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <p>
 * 通用压缩工具类
 * </p>
 *
 * @author songYu
 * @since 2023/9/13 10:54
 */
public class CommonZipUtils {

    /**
     * 从 zip 压缩包中读取文件字节数组
     *
     * @param zipFile  zip 文件
     * @param zipEntry 要读取的压缩实体
     * @return 字节数组
     * @throws FileReadException 文件读取异常
     */
    public static byte[] readZipEntry(ZipFile zipFile, ZipEntry zipEntry) throws FileReadException {
        int readLen;
        byte[] buffer = new byte[1024];
        try (InputStream inputStream = zipFile.getInputStream(zipEntry);
             ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {
            while ((readLen = inputStream.read(buffer)) != -1) {
                arrayOutputStream.write(buffer, 0, readLen);
            }
            arrayOutputStream.flush();
            return arrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new FileReadException(e);
        }
    }

    /**
     * 打开 zip 压缩包进行操作
     *
     * @param path     压缩包路径
     * @param openToDo 压缩包打开的操作
     * @throws FileReadException 文件读取异常
     */
    public static void openZipFile(String path, Consumer<ZipFile> openToDo) throws FileReadException {
        try (ZipFile zipFile = new ZipFile(path)) {
            openToDo.accept(zipFile);
        } catch (IOException e) {
            throw new FileReadException(e);
        }
    }

}
