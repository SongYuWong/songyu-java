package com.songyu.components.springboot.mvc.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.songyu.components.api.Response;
import com.songyu.components.api.ResponseStatus;
import com.songyu.components.api.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * <p>
 * 接口异常统一处理
 * </p>
 *
 * @author songYu
 * @since 2023/3/24 16:05
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Throwable.class)
    public Response<?> onError(Throwable t) {
        t.printStackTrace();
        log.error("ERROR : {}", JSON.toJSONString(t));
        String message;
        if (isSQLException(t)) {
            message = "数据映射失败";
        } else {
            message = t.getMessage();
        }
        return Response.buildWithSummary(ResponseStatus.SERVER_ERR, message);
    }

    private boolean isSQLException(Throwable t) {
        if (t instanceof SQLException) {
            return true;
        } else {
            if (t.getCause() != null) {
                return isSQLException(t.getCause());
            } else {
                return false;
            }
        }
    }

    @ExceptionHandler(ApiException.class)
    public Response<?> onApiException(ApiException apiException) {
        apiException.printStackTrace();
        log.error("API Exception : {}", JSON.toJSONString(apiException.toString()));
        return Response.buildWithSummary(ResponseStatus.SERVER_ERR, apiException.getMessage());
    }

}
