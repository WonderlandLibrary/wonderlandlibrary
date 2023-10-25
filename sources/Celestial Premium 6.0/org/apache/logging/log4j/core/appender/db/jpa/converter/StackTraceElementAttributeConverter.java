/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.persistence.AttributeConverter
 *  javax.persistence.Converter
 */
package org.apache.logging.log4j.core.appender.db.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.logging.log4j.core.helpers.Strings;

@Converter(autoApply=false)
public class StackTraceElementAttributeConverter
implements AttributeConverter<StackTraceElement, String> {
    private static final int UNKNOWN_SOURCE = -1;
    private static final int NATIVE_METHOD = -2;

    public String convertToDatabaseColumn(StackTraceElement element) {
        if (element == null) {
            return null;
        }
        return element.toString();
    }

    public StackTraceElement convertToEntityAttribute(String s) {
        if (Strings.isEmpty(s)) {
            return null;
        }
        return StackTraceElementAttributeConverter.convertString(s);
    }

    static StackTraceElement convertString(String s) {
        int open = s.indexOf("(");
        String classMethod = s.substring(0, open);
        String className = classMethod.substring(0, classMethod.lastIndexOf("."));
        String methodName = classMethod.substring(classMethod.lastIndexOf(".") + 1);
        String parenthesisContents = s.substring(open + 1, s.indexOf(")"));
        String fileName = null;
        int lineNumber = -1;
        if ("Native Method".equals(parenthesisContents)) {
            lineNumber = -2;
        } else if (!"Unknown Source".equals(parenthesisContents)) {
            int colon = parenthesisContents.indexOf(":");
            if (colon > -1) {
                fileName = parenthesisContents.substring(0, colon);
                try {
                    lineNumber = Integer.parseInt(parenthesisContents.substring(colon + 1));
                }
                catch (NumberFormatException ignore) {}
            } else {
                fileName = parenthesisContents.substring(0);
            }
        }
        return new StackTraceElement(className, methodName, fileName, lineNumber);
    }
}

