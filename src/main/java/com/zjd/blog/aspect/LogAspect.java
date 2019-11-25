package com.zjd.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    /*内部类，用于接收请求数据，其实用结构体更好，但是java没有*/
    private class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.zjd.blog.web.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        /*获取请求的信息*/
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        String url = request != null ? request.getRequestURL().toString() : null;
        String ip = request != null ? request.getRemoteAddr() : null;
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        /*输出到日志*/
        logger.info("Request : {}", new RequestLog(url, ip, classMethod, args));
    }

    @After("log()")
    public void doAfter() {
        logger.info("---------doAfter------------");
    }

    @AfterReturning(returning = "res", pointcut = "log()")
    public void doAfterReturn(Object res) {
        logger.info("Result : {}", res);
    }

}
