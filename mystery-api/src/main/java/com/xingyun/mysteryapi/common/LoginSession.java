package com.xingyun.mysteryapi.common;

import com.xingyun.mysteryapi.component.LoginRes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class LoginSession {

    private static final String SECURITY_CONTEXT_ATTRIBUTES = "SECURITY_CONTEXT";

    public static void setContext(LoginRes context) {
        RequestContextHolder.currentRequestAttributes().setAttribute(
                SECURITY_CONTEXT_ATTRIBUTES,
                context,
                RequestAttributes.SCOPE_REQUEST);
    }

    public static LoginRes getMemberInfo() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Object attribute = requestAttributes.getAttribute(SECURITY_CONTEXT_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
        return (LoginRes) attribute;
        /*return (LoginInfo)RequestContextHolder.currentRequestAttributes()
                .getAttribute(SECURITY_CONTEXT_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);*/
    }

    public static String getWalletAddress() {
        LoginRes loginDto = getMemberInfo();
        if (loginDto == null) {
            return null;
        } else {
            return loginDto.getWalletAddress();
        }
    }

}
