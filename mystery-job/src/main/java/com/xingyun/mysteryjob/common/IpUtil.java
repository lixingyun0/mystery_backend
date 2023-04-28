package com.xingyun.mysteryjob.common;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {

    public static String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");

        if (StringUtils.isNotBlank(ip) && !ip.equalsIgnoreCase("unknown")){
            return ip;
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !ip.equalsIgnoreCase("unknown")){
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !ip.equalsIgnoreCase("unknown")){
            return ip;
        }
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (StringUtils.isNotBlank(ip) && !ip.equalsIgnoreCase("unknown")){
            return ip;
        }
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (StringUtils.isNotBlank(ip) && !ip.equalsIgnoreCase("unknown")){
            return ip;
        }
        return request.getRemoteAddr();
    }

}
