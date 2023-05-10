package com.xingyun.mysteryapi.config;

import com.alibaba.fastjson2.JSON;
import com.xingyun.mysteryapi.common.LoginSession;
import com.xingyun.mysteryapi.component.LoginRes;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.base.ResultCode;
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

        // 从请求头部获取 登录 token
        String token = getToken(request);
        Permission permission = handlerMethod.getMethodAnnotation(Permission.class);

        if (permission != null) {
            if (StringUtils.isBlank(token)) {

                logout(response);
                return false;

            } else {

                String loginInfoStr = redisTemplate.opsForValue().get("USER_TOKEN" + token);

                if (StringUtils.isBlank(loginInfoStr)) {
                    //退出登录
                    logout(response);
                    return false;
                }

                LoginRes loginInfo = JSON.parseObject(loginInfoStr, LoginRes.class);
                // 放入ThreadLocal 中
                LoginSession.setContext(loginInfo);
                return true;
            }
        }


        if (StringUtils.isNotBlank(token)) {

            String loginInfoStr = redisTemplate.opsForValue().get("USER_TOKEN" + token);

            if (StringUtils.isBlank(loginInfoStr)) {

                return true;
            }

            LoginRes loginInfo = JSON.parseObject(loginInfoStr, LoginRes.class);
            // 放入ThreadLocal 中
            LoginSession.setContext(loginInfo);
            return true;
        }

        return true;

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
