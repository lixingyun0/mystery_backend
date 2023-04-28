package com.xingyun.mysteryapi.config;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;

public class RequestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger("Request");

    private static final String X_WWW_FORM_URL_ENCODE = "application/x-www-form-urlencoded";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        doMyFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
        //filterChain.doFilter(request, response);

    }

    /**
     * 打印请求参数
     *
     * @param req
     * @param res
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    private void doMyFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {

        if (RequestMethod.OPTIONS.toString().equals(req.getMethod())) {
            res.setHeader("Access-Control-Allow-Origin", "*");
            res.setHeader("Access-Control-Allow-Methods", "*");
            res.setHeader("Access-Control-Allow-Headers", "*");
            return;
        }
        String requestURI = req.getRequestURI();

        if (StringUtils.isNotBlank(req.getContentType()) && req.getContentType().contains("multipart/form-data")) {
            logger.info("[{}] {} ", req.getMethod(), requestURI);
            chain.doFilter(req, res);
            return;
        }

        if (RequestMethod.GET.toString().equals(req.getMethod())) {
            //空请求太多 影响日志查看
            if (!"/".equals(requestURI)) {
                logger.info("[{}] [{}] {} ,g param is : {}", req.getMethod(), req.getContentType(), requestURI, JSONObject.toJSONString(req.getParameterMap()));
            }

            chain.doFilter(req, res);
            return;
        }
        if (req.getContentType().toLowerCase(Locale.ROOT).startsWith(X_WWW_FORM_URL_ENCODE)) {
            Enumeration<String> parameterNames = req.getParameterNames();
            StringBuffer sb = new StringBuffer();
            while (parameterNames.hasMoreElements()) {
                String element = parameterNames.nextElement();
                sb.append(element).append("=");
                String parameter = req.getParameter(element);
                sb.append(parameter).append("&");
            }
            logger.info("[{}] [{}] {} ,x param is : {}", req.getMethod(), req.getContentType(), requestURI, sb);
            chain.doFilter(req, res);
            return;
        }
        String body = HttpHelper.getBodyString(req);
        if (StringUtils.isNotBlank(body)) {
            logger.info("[{}] [{}] {} ,p param is : {}", req.getMethod(), req.getContentType(), requestURI, body);
            HttpServletRequest request = new BodyReaderHttpServletRequestWrapper(req, body);
            chain.doFilter(request, res);
            return;
        }
        chain.doFilter(req, res);

    }
}
