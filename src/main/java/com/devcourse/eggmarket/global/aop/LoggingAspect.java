package com.devcourse.eggmarket.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Object loggingFormat(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        logger.info("[{}] Before method called. {}", layer, joinPoint.getSignature());
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        long endTime = System.nanoTime() - startTime;
        logger.info("[{}] After method called with result => {} as time taken {} ns", layer, result, endTime);
        return result;
}

    @Around("com.devcourse.eggmarket.global.aop.CommonPointcut.controllerPublicMethodPointcut()")
    public Object controllerLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return loggingFormat(joinPoint, "Controller");
    }

    @Around("com.devcourse.eggmarket.global.aop.CommonPointcut.servicePublicMethodPointcut()")
    public Object serviceLog(ProceedingJoinPoint joinPoint) throws Throwable {
            return loggingFormat(joinPoint, "Service");
        }

        @Around("com.devcourse.eggmarket.global.aop.CommonPointcut.repositoryPublicMethodPointcut()")
        public Object repositoryLog(ProceedingJoinPoint joinPoint) throws Throwable {
            return loggingFormat(joinPoint, "Repository");
    }
}
