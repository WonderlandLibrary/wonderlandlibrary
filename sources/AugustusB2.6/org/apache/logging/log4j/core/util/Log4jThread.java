// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.util.concurrent.atomic.AtomicLong;

public class Log4jThread extends Thread
{
    static final String PREFIX = "Log4j2-";
    private static final AtomicLong threadInitNumber;
    
    private static long nextThreadNum() {
        return Log4jThread.threadInitNumber.getAndIncrement();
    }
    
    private static String toThreadName(final Object name) {
        return "Log4j2-" + name;
    }
    
    public Log4jThread() {
        super(toThreadName(nextThreadNum()));
    }
    
    public Log4jThread(final Runnable target) {
        super(target, toThreadName(nextThreadNum()));
    }
    
    public Log4jThread(final Runnable target, final String name) {
        super(target, toThreadName(name));
    }
    
    public Log4jThread(final String name) {
        super(toThreadName(name));
    }
    
    public Log4jThread(final ThreadGroup group, final Runnable target) {
        super(group, target, toThreadName(nextThreadNum()));
    }
    
    public Log4jThread(final ThreadGroup group, final Runnable target, final String name) {
        super(group, target, toThreadName(name));
    }
    
    public Log4jThread(final ThreadGroup group, final Runnable target, final String name, final long stackSize) {
        super(group, target, toThreadName(name), stackSize);
    }
    
    public Log4jThread(final ThreadGroup group, final String name) {
        super(group, toThreadName(name));
    }
    
    static {
        threadInitNumber = new AtomicLong();
    }
}
