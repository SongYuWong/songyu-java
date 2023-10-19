package com.songyu.components.springboot.mvc.advice;

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
        log.error("ERROR : {}", t.toString());
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
        log.error("API Exception : {}", apiException.toString());
        apiException.printStackTrace();
        return Response.buildWithSummary(ResponseStatus.SERVER_ERR, apiException.getMessage());
    }

}
