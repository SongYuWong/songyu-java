package com.songyu.components.springboot.mvc.service;

import com.songyu.components.api.ApiDoc;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * <p>
 * 端点服务
 * </p>
 *
 * @author songYu
 * @since 2023/12/6 10:59
 */
@Service
public class EndpointsService {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    public Map<String, ApiInfo> getApiInfos() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        HashMap<String, ApiInfo> result = new HashMap<>();
        handlerMethods.forEach((k, v) -> {
            if (!k.getMethodsCondition().isEmpty()) {
                Method method = v.getMethod();
                ApiDoc annotation = method.getAnnotation(ApiDoc.class);
                if (annotation != null) {
                    String name = method.getName();
                    String uniqueKey;
                    for (String directPath : k.getDirectPaths()) {
                        uniqueKey = name.concat("%@%").concat(directPath);
                        if (result.containsKey(uniqueKey)) {
                            throw new RuntimeException("存在多个方法和请求路径处理组合：" + uniqueKey);
                        }
                        ApiInfo apiInfo = new ApiInfo();
                        apiInfo.setCode(name);
                        apiInfo.setName(annotation.name());
                        apiInfo.setDesc(annotation.desc());
                        apiInfo.setMethods(k.getMethodsCondition().getMethods().stream().map(RequestMethod::name).collect(Collectors.toSet()));
                        apiInfo.setUri(directPath);
                        apiInfo.setRequestType(annotation.requestType());
                        apiInfo.setResponseType(annotation.responseType());
                        result.put(uniqueKey, apiInfo);
                    }
                }
            }
        });
        return result;
    }

    /**
     * 接口信息
     */
    @Data
    public static class ApiInfo {

        /**
         * 编码
         */
        private String code;

        /**
         * 名称
         */
        private String name;

        /**
         * 描述
         */
        private String desc;

        /**
         * 请求方式
         */
        private Set<String> methods;

        /**
         * 资源定位符
         */
        private String uri;

        /**
         * 请求类型
         */
        private Class<?> requestType;

        /**
         * 响应类型
         */
        private Class<?> responseType;

        @Override
        public String toString() {
            return "insert into auth.api (api_code, api_uri, api_method, api_name, api_desc, response_type, request_type) values (" + "'" + code + "' ," +
                    "'" + uri + "' ," +
                    "'" + methodString() + "' ," +
                    "'" + name + "' ," +
                    "'" + desc + "' ," +
                    "'" + responseType.getName() + "' ," +
                    "'" + requestType.getName() + "');";
        }

        private String methodString(){
            if (this.methods.isEmpty()) {
                return "";
            }else {
                StringJoiner joiner = new StringJoiner(",");
                for (String method : this.methods) {
                    joiner.add(method);
                }
                return joiner.toString();
            }
        }

    }

}
