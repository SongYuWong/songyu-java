package com.songyu.commonutils.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 最大文件操作层级异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/12 18:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaxFileLevelException extends CommonUtilException {

    private final int level;

    public MaxFileLevelException(int level) {
        super("最大文件操作层级异常");
        this.level = level;
    }

    public MaxFileLevelException(String message, int level) {
        super(message);
        this.level = level;
    }

    public MaxFileLevelException(String message, Throwable cause, int level) {
        super(message, cause);
        this.level = level;
    }

    public MaxFileLevelException(Throwable cause, int level) {
        super(cause);
        this.level = level;
    }

    public MaxFileLevelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int level) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.level = level;
    }
}
