package com.songyu.domains.auth.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.songyu.components.springboot.data.AbstractTable;
import com.songyu.components.springboot.exception.IllegalInfoException;

/**
 * <p>
 * 用户客户端信息不合法异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 11:26
 */
public class IllegalUserClientInfoException extends IllegalInfoException {

    public IllegalUserClientInfoException(AbstractTable abstractTable) {
        super(abstractTable);
    }

    public IllegalUserClientInfoException(String message, AbstractTable abstractTable) {
        super(message, abstractTable);
    }

    public IllegalUserClientInfoException(String message, Throwable cause, AbstractTable abstractTable) {
        super(message, cause, abstractTable);
    }

    public IllegalUserClientInfoException(Throwable cause, AbstractTable abstractTable) {
        super(cause, abstractTable);
    }

    public IllegalUserClientInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, AbstractTable abstractTable) {
        super(message, cause, enableSuppression, writableStackTrace, abstractTable);
    }

    @Override
    public String tableDesc() {
        return JSON.toJSONString(this, SerializerFeature.PrettyFormat);
    }
}
