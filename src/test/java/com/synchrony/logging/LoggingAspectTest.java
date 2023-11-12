package com.synchrony.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {
    @InjectMocks
    private LoggingAspect loggingAspect;

    @Mock
    private CodeSignature codeSignature;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Test
    void logAroundController() throws Throwable {
        setUpCommonMockBehavior();
        Object result = loggingAspect.logAroundController(proceedingJoinPoint);
        verifyCommonMockBehavior(result);
    }

    @Test
    void logAroundService() throws Throwable {
        setUpCommonMockBehavior();
        Object result = loggingAspect.logAroundService(proceedingJoinPoint);
        verifyCommonMockBehavior(result);
    }

    @Test
    void logAroundRepository() throws Throwable {
        setUpCommonMockBehavior();
        Object result = loggingAspect.logAroundRepository(proceedingJoinPoint);
        verifyCommonMockBehavior(result);
    }

    @Test
    void logAroundCache() throws Throwable {
        setUpCommonMockBehavior();
        Object result = loggingAspect.logAroundCache(proceedingJoinPoint);
        verifyCommonMockBehavior(result);
    }

    @Test
    void logAroundBean() throws Throwable {
        setUpCommonMockBehavior();
        Object result = loggingAspect.logAroundBean(proceedingJoinPoint, LoggingBean.ApiType.EXTERNAL);
        verifyCommonMockBehavior(result);
    }

    @Test
    void logAfterThrowingExceptionCall() {
        JoinPoint joinPoint = mock(JoinPoint.class);
        when(joinPoint.getTarget()).thenReturn(new Object());
        when(joinPoint.getSignature()).thenReturn(codeSignature);
        Throwable ex = new RuntimeException("Test exception");
        assertDoesNotThrow(()->loggingAspect.logAfterThrowingExceptionCall(joinPoint, ex));
    }

    private void setUpCommonMockBehavior() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenReturn("result");
        when(proceedingJoinPoint.getTarget()).thenReturn(new Object());
        when(proceedingJoinPoint.getSignature()).thenReturn(codeSignature);
        when(codeSignature.getParameterNames()).thenReturn(null);
    }

    private void verifyCommonMockBehavior(Object result) throws Throwable {
        verify(proceedingJoinPoint, times(1)).proceed();
        Assert.isTrue(result.equals("result"), "Result should be 'result'");
    }
}
