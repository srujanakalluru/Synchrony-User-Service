package com.synchrony.logging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggingBeanTest {

    @Test
    void testToString() {
        LoggingBean.ApiType apiType = LoggingBean.ApiType.EXTERNAL;
        String className = "TestClass";
        String method = "testMethod";
        Object[] arguments = {1, "arg"};
        String[] parameters = {"param1", "param2"};
        Long durationMs = 100L;
        String detailMessage = "Test Detail Message";
        String stackTrace = "Test Stack Trace";
        Object returnValue = "Test Return Value";

        LoggingBean loggingBean = LoggingBean.builder()
                .apiType(apiType)
                .className(className)
                .method(method)
                .arguments(arguments)
                .parameters(parameters)
                .durationMs(durationMs)
                .detailMessage(detailMessage)
                .stackTrace(stackTrace)
                .returnValue(returnValue)
                .build();

        String result = loggingBean.toString();

        assertNotNull(result);
        assertTrue(result.contains("EXTERNAL"));
        assertTrue(result.contains("className =\"TestClass"));
        assertTrue(result.contains("method =\"testMethod"));
        assertTrue(result.contains("durationMs =\"100"));
        assertTrue(result.contains("parameters =\"[param1, param2]"));
        assertTrue(result.contains("arguments =\"[1, arg]"));
        assertTrue(result.contains("stacktrace =\"Test Stack Trace"));
        assertTrue(result.contains("detailMessage =\"Test Detail Message"));
        assertTrue(result.contains("returnValue =\"Test Return Value"));
    }
}
