package com.songyu.components.springboot.exception;

import com.songyu.components.springboot.data.AbstractTable;

/**
 * <p>
 * 表信息不合法异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 11:26
 */
public abstract class IllegalInfoException extends Exception {

    /**
     * 表信息
     */
    protected final AbstractTable abstractTable;

    public IllegalInfoException(AbstractTable abstractTable) {
        this.abstractTable = abstractTable;
    }

    public IllegalInfoException(String message, AbstractTable abstractTable) {
        super(message);
        this.abstractTable = abstractTable;
    }

    public IllegalInfoException(String message, Throwable cause, AbstractTable abstractTable) {
        super(message, cause);
        this.abstractTable = abstractTable;
    }

    public IllegalInfoException(Throwable cause, AbstractTable abstractTable) {
        super(cause);
        this.abstractTable = abstractTable;
    }

    public IllegalInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, AbstractTable abstractTable) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.abstractTable = abstractTable;
    }

    public abstract String tableDesc();

    @Override
    public String toString() {
        return "IllegalInfoException{" +
                "table=" + tableDesc() +
                '}';
    }
}
