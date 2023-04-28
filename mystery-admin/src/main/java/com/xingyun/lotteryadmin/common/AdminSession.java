package com.xingyun.lotteryadmin.common;

import com.xingyun.lotteryadmin.response.SysUserInfo;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class AdminSession {

    private static final String SECURITY_CONTEXT_ATTRIBUTES = "SECURITY_CONTEXT";

    public static void setContext(SysUserInfo context) {
        RequestContextHolder.currentRequestAttributes().setAttribute(
                SECURITY_CONTEXT_ATTRIBUTES,
                context,
                RequestAttributes.SCOPE_REQUEST);
    }

    public static SysUserInfo getUserInfo() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Object attribute = requestAttributes.getAttribute(SECURITY_CONTEXT_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
        return (SysUserInfo) attribute;
    }

    public static Long getUserId() {
        SysUserInfo sysUserInfo = getUserInfo();
        if (sysUserInfo == null) {
            return null;
        } else {
            return sysUserInfo.getUserId();
        }
    }

}
