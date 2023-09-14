package com.songyu.commonutils.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.IOException;

import static com.songyu.commonutils.CommonFileUtils.deleteFile;

/**
 * <p>
 * 文件云数据异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/12 22:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileMetaException extends CommonUtilException {

    /**
     * 存在问题的文件
     */
    private final File file;

    public FileMetaException(File file) {
        this.file = file;
    }

    public FileMetaException(String message, File file) {
        super(message);
        this.file = file;
    }

    public FileMetaException(String message, Throwable cause, File file) {
        super(message, cause);
        this.file = file;
    }

    public FileMetaException(Throwable cause, File file) {
        super(cause);
        this.file = file;
    }

    public FileMetaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, File file) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.file = file;
    }

    /**
     * 重新删除创建对应的文件为文件夹
     *
     * @return 新建的文件夹
     * @throws FileCreateException 文件夹创建失败异常
     */
    public File reMkdirs() throws FileCreateException {
        try {
            deleteFile(file);
        } catch (FileDeleteException | MaxFileLevelException | SourceNullException ex) {
            throw new FileCreateException("原有非文件夹文件删除失败", ex);
        }
        if (!file.mkdirs()) {
            throw new FileCreateException("新文件夹创建失败");
        }
        return file;
    }

    /**
     * 重新创建对应的文件夹成文件
     *
     * @return 新建的文件
     * @throws FileCreateException 文件创建失败异常
     */
    public File reCreateFile() throws FileCreateException {
        try {
            deleteFile(file);
        } catch (FileDeleteException | MaxFileLevelException | SourceNullException ex) {
            throw new FileCreateException("原有文件夹文件删除失败", ex);
        }
        try {
            if (!file.createNewFile()) {
                throw new FileCreateException("新文件创建失败");
            }
        } catch (IOException e) {
            throw new FileCreateException("新文件创建失败");
        }
        return file;
    }
}
