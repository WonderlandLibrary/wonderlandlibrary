// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import org.apache.logging.log4j.ThreadContext;

public final class NDC
{
    private NDC() {
    }
    
    public static void clear() {
        ThreadContext.clearStack();
    }
    
    public static Stack cloneStack() {
        final Stack<String> stack = new Stack<String>();
        for (final String element : ThreadContext.cloneStack().asList()) {
            stack.push(element);
        }
        return stack;
    }
    
    public static void inherit(final Stack stack) {
        ThreadContext.setStack(stack);
    }
    
    public static String get() {
        return ThreadContext.peek();
    }
    
    public static int getDepth() {
        return ThreadContext.getDepth();
    }
    
    public static String pop() {
        return ThreadContext.pop();
    }
    
    public static String peek() {
        return ThreadContext.peek();
    }
    
    public static void push(final String message) {
        ThreadContext.push(message);
    }
    
    public static void remove() {
        ThreadContext.removeStack();
    }
    
    public static void setMaxDepth(final int maxDepth) {
        ThreadContext.trim(maxDepth);
    }
}
