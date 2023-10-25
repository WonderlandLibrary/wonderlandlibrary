/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.PrivateSecurityManagerStackTraceUtil;

public class StackLocator {
    private static final StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static final StackWalker stackWalker = StackWalker.getInstance();
    private static final StackLocator INSTANCE = new StackLocator();

    public static StackLocator getInstance() {
        return INSTANCE;
    }

    private StackLocator() {
    }

    public Class<?> getCallerClass(String fqcn) {
        return this.getCallerClass(fqcn, "");
    }

    public Class<?> getCallerClass(String fqcn, String pkg) {
        return walker.walk(s -> s.dropWhile(f -> !f.getClassName().equals(fqcn)).dropWhile(f -> f.getClassName().equals(fqcn)).dropWhile(f -> !f.getClassName().startsWith(pkg)).findFirst()).map(StackWalker.StackFrame::getDeclaringClass).orElse(null);
    }

    public Class<?> getCallerClass(Class<?> anchor) {
        return walker.walk(s -> s.dropWhile(f -> !f.getDeclaringClass().equals(anchor)).dropWhile(f -> f.getDeclaringClass().equals(anchor)).findFirst()).map(StackWalker.StackFrame::getDeclaringClass).orElse(null);
    }

    public Class<?> getCallerClass(int depth) {
        return walker.walk(s -> s.skip(depth).findFirst()).map(StackWalker.StackFrame::getDeclaringClass).orElse(null);
    }

    public Stack<Class<?>> getCurrentStackTrace() {
        if (PrivateSecurityManagerStackTraceUtil.isEnabled()) {
            return PrivateSecurityManagerStackTraceUtil.getCurrentStackTrace();
        }
        Stack stack = new Stack();
        List classes = walker.walk(s -> s.map(f -> f.getDeclaringClass()).collect(Collectors.toList()));
        stack.addAll(classes);
        return stack;
    }

    public StackTraceElement calcLocation(String fqcnOfLogger) {
        return stackWalker.walk(s -> s.dropWhile(f -> !f.getClassName().equals(fqcnOfLogger)).dropWhile(f -> f.getClassName().equals(fqcnOfLogger)).findFirst()).map(StackWalker.StackFrame::toStackTraceElement).orElse(null);
    }

    public StackTraceElement getStackTraceElement(int depth) {
        return stackWalker.walk(s -> s.skip(depth).findFirst()).map(StackWalker.StackFrame::toStackTraceElement).orElse(null);
    }
}

