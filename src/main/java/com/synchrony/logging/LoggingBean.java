/*
 * Copyright (c) 2023.
 * <p>Author: Srujana Kalluru </p>
 */

package com.synchrony.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Builder
@AllArgsConstructor
public class LoggingBean {
    private ApiType apiType;
    private String className;
    private String method;
    private Object[] arguments;
    private String[] parameters;
    private Long durationMs;
    private String detailMessage;
    private String stackTrace;
    private Object returnValue;

    /**
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-11s", apiType)).append("=\t{");
        sb.append("className =\"").append(String.format("%-30s", className)).append("\"")
                .append(" | method =\"").append(String.format("%-20s", method)).append("\"");
        appendIfNotNull(sb, " | \tdurationMs =", String.format("%-5s", durationMs));
        appendIfNotNull(sb, " | \tparameters =", String.format("%-15s", Arrays.toString(parameters)));
        appendIfNotNull(sb, " | \targuments =", String.format("%-15s", Arrays.toString(arguments)));
        appendIfNotNull(sb, " | \tstacktrace =", StringUtils.trimWhitespace(stackTrace));
        appendIfNotNull(sb, " | \tdetailMessage =", detailMessage);
        appendIfNotNull(sb, " | \treturnValue =", returnValue);

        sb.append("}");
        return sb.toString();
    }

    private void appendIfNotNull(StringBuilder sb, String label, Object... values) {
        for (Object value : values) {
            if (value != null) {
                sb.append(label).append("\"").append(value).append("\"");
            }
        }
    }


    @Getter
    public enum ApiType {
        EXTERNAL, CONTROLLER, SERVICE, REPOSITORY, ERROR, CACHE, KAFKA
    }
}
