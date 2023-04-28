package com.xingyun.lotteryadmin.config;

import com.alibaba.fastjson2.JSON;
import com.xingyun.lotteryadmin.common.AdminSession;
import com.xingyun.lotteryadmin.response.SysUserInfo;
import com.xingyun.mysterycommon.annotation.Unauthorized;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.base.ResultCode;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
public class LoginInterceptor implements AsyncHandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger("baseLog");

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        ApiOperation apiOperation = handlerMethod.getMethodAnnotation(ApiOperation.class);
        if (apiOperation == null) {
            return true;
        }
        // 从请求头部获取 登录 token
        String token = getToken(request);
        Unauthorized unauthorized = handlerMethod.getMethodAnnotation(Unauthorized.class);
        if (unauthorized != null) {
            return true;
        }

        if (StringUtils.isBlank(token)) {

            logout(response);
            return false;

        } else {

            String sysUserInfoString = redisTemplate.opsForValue().get("ADMIN_TOKEN" + token);

            if (StringUtils.isBlank(sysUserInfoString)) {
                //退出登录
                logout(response);
                return false;
            }

            SysUserInfo sysUserInfo = JSON.parseObject(sysUserInfoString, SysUserInfo.class);
            // 放入ThreadLocal 中
            AdminSession.setContext(sysUserInfo);
            return true;
        }

    }

    private void blockRequest(HttpServletResponse response) throws IOException {
        //gson.toJson(new DataResponse(ResponseCodeEnum.LOGIN_OUT))
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(JSON.toJSONString(R.failed("Operate frequently")));
    }

    private void logout(HttpServletResponse response) throws IOException {
        //gson.toJson(new DataResponse(ResponseCodeEnum.LOGIN_OUT))
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(JSON.toJSONString(R.failed(ResultCode.UN_LOGIN)));
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null || token.length() == 0) {
            token = request.getParameter("token");
        }
        return token;
    }

}
