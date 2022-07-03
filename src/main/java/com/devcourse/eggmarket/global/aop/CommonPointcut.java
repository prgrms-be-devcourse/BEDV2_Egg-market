package com.devcourse.eggmarket.global.aop;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointcut {

    @Pointcut("execution(public * com.devcourse.eggmarket.domain..*Controller.*(..))")
    public void controllerPublicMethodPointcut() {
        // TODO Contoller Layer Pointcut
    }

    @Pointcut("execution(public * com.devcourse.eggmarket.domain..*Service.*(..))")
    public void servicePublicMethodPointcut() {
        // TODO Service Layer Pointcut
    }

    @Pointcut("execution(public * com.devcourse.eggmarket.domain..*Repository.*(..))")
    public void repositoryPublicMethodPointcut() {
        // TODO Repository Layer Pointcut
    }
}
