package com.synchrony.logging;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import static com.synchrony.logging.LoggingBean.ApiType.CACHE;
import static com.synchrony.logging.LoggingBean.ApiType.CONTROLLER;
import static com.synchrony.logging.LoggingBean.ApiType.ERROR;
import static com.synchrony.logging.LoggingBean.ApiType.EXTERNAL;
import static com.synchrony.logging.LoggingBean.ApiType.REPOSITORY;
import static com.synchrony.logging.LoggingBean.ApiType.SERVICE;


@Aspect
@Component
@Getter
@Setter
@Slf4j
public class LoggingAspect {
    /**
     * Pointcut for controller
     */
    @Pointcut("execution(* com.synchrony.controller..*(..))")
    public void controllerPointCut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for repository
     */
    @Pointcut("execution(* com.synchrony.repository..*(..))")
    public void repositoryPointCut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for service
     */
    @Pointcut("execution(* com.synchrony.service..*(..))")
    public void servicePointCut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for service
     */
    @Pointcut("execution(* com.synchrony.cache..*(..))")
    public void cachePointCut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for external client calls
     */
    @Pointcut("execution(* com.synchrony.client..*(..))")
    public void externalServicePointCut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * @param proceedingJoinPoint proceedingJoinPoint
     * @throws Throwable throwable
     */
    @Around("controllerPointCut()")
    public Object logAroundController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return logAroundBean(proceedingJoinPoint, CONTROLLER);
    }

    /**
     * @param proceedingJoinPoint proceedingJoinPoint
     * @throws Throwable throwable
     */
    @Around("servicePointCut()")
    public Object logAroundService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return logAroundBean(proceedingJoinPoint, SERVICE);
    }

    /**
     * @param proceedingJoinPoint proceedingJoinPoint
     * @throws Throwable throwable
     */
    @Around("cachePointCut()")
    public Object logAroundCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return logAroundBean(proceedingJoinPoint, CACHE);
    }

    /**
     * @param proceedingJoinPoint proceedingJoinPoint
     * @throws Throwable throwable
     */
    @Around("externalServicePointCut()")
    public Object logAroundExternalService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return logAroundBean(proceedingJoinPoint, EXTERNAL);
    }

    /**
     * @param proceedingJoinPoint proceedingJoinPoint
     * @throws Throwable throwable
     */
    @Around("repositoryPointCut()")
    public Object logAroundRepository(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return logAroundBean(proceedingJoinPoint, REPOSITORY);
    }

    /**
     * @param joinPoint joinPoint
     */
    @AfterThrowing(value = "controllerPointCut()", throwing = "ex")
    public void logAfterThrowingExceptionCall(JoinPoint joinPoint, Throwable ex) {
        logErrorBean(joinPoint, ex);
    }

    void logErrorBean(JoinPoint joinPoint, Throwable ex) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        String stackTraceMessage = null;
        if (null != ex.getMessage()) {
            stackTraceMessage = ex.getMessage();
        } else if (null != ex.getCause()) {
            stackTraceMessage = ex.getCause().getMessage();
        }

        LoggingBean bean = LoggingBean.builder()
                .apiType(ERROR)
                .className(joinPoint.getTarget().getClass().getSimpleName())
                .method(signature.getName())
                .parameters(signature.getParameterNames())
                .arguments(joinPoint.getArgs())
                .stackTrace(stackTraceMessage)
                .build();
        log.error(bean.toString());

    }

    /**
     * @param proceedingJoinPoint proceedingJoinPoint
     * @param apiType             apiType
     * @return Object object
     * @throws Throwable throwable
     */
    Object logAroundBean(ProceedingJoinPoint proceedingJoinPoint, LoggingBean.ApiType apiType)
            throws Throwable {
        return logAroundBean(proceedingJoinPoint, apiType, null);
    }

    /**
     * @param proceedingJoinPoint proceedingJoinPoint
     * @param apiType             apiType
     * @param detailMessage       detailMessage
     * @return Object
     * @throws Throwable throwable
     */
    public Object logAroundBean(
            ProceedingJoinPoint proceedingJoinPoint, LoggingBean.ApiType apiType, String detailMessage)
            throws Throwable {
        long startTime = System.currentTimeMillis();
        Object object = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();

        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature();

        LoggingBean bean =
                LoggingBean.builder()
                        .apiType(apiType)
                        .className(proceedingJoinPoint.getTarget().getClass().getSimpleName())
                        .method(signature.getName())
                        .parameters(signature.getParameterNames())
                        .arguments(proceedingJoinPoint.getArgs())
                        .returnValue(object)
                        .durationMs(endTime - startTime)
                        .detailMessage(detailMessage)
                        .build();
        log.info(bean.toString());
        return object;
    }
}
