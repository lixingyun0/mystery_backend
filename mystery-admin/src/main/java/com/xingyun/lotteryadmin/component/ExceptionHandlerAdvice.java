package com.xingyun.lotteryadmin.component;

import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.base.ResultCode;
import com.xingyun.mysterycommon.exception.BizException;
import com.xingyun.mysterycommon.exception.LockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    private Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);


    // Intercept error handling
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.error("URL:{} unified error handling", request.getRequestURI(), e);

        return R.failed(ResultCode.FAIL);
    }

    // Intercept error handling
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public R handleBusinessException(BizException ex) {
        String message = ex.getMessage();
        logger.error(message, ex);
        return R.failed(ex.getMessage());
    }


    @ExceptionHandler(LockException.class)
    @ResponseBody
    public R handleBusinessException(LockException ex) {
        String message = ex.getMessage();
        logger.error(message, ex);
        return R.failed(ResultCode.OPERATE_FREQUENTLY);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public R handleMysqlDuplicateException(HttpServletRequest request, HttpServletResponse response, DuplicateKeyException e) {
        //邮件/钉钉通知
        logger.error("URL:{} 唯一索引重复,请仔细检查业务代码", request.getRequestURI(), e);
        return R.failed(ResultCode.FAIL);
    }


    /**
     * @param e
     * @return
     * @RequestParam 参数缺失
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public R handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return R.failed(ResultCode.PARA_ERROR);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpMediaTypeException.class})
    @ResponseBody
    public R badRequest(ServletException ex) {
        return R.failed("请求方式错误");
    }

    /**
     * Catch method argument exceptions
     * validate校验不通过异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public R handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getMessage();
        logger.error(message, ex);
        return R.failed(ResultCode.PARA_ERROR);
    }


    /**
     * Catch @Valid @RequestBody bean exceptions
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getMessage();
        logger.error(message, ex);
        return R.failed(ResultCode.PARA_ERROR);
    }

    /**
     * Catch @Valid @RequestBody bean exceptions
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public R handleBindException(BindException ex) {
        String message = ex.getMessage();
        logger.error(message, ex);
        return R.failed(ResultCode.PARA_ERROR);
    }


}
