/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.util.concurrent.atomic.AtomicLong;

public class Log4jThread
extends Thread {
    static final String PREFIX = "Log4j2-";
    private static final AtomicLong threadInitNumber = new AtomicLong();

    private static long nextThreadNum() {
        return threadInitNumber.getAndIncrement();
    }

    private static String toThreadName(Object name) {
        return PREFIX + name;
    }

    public Log4jThread() {
        super(Log4jThread.toThreadName(Log4jThread.nextThreadNum()));
    }

    public Log4jThread(Runnable target) {
        super(target, Log4jThread.toThreadName(Log4jThread.nextThreadNum()));
    }

    public Log4jThread(Runnable target, String name) {
        super(target, Log4jThread.toThreadName(name));
    }

    public Log4jThread(String name) {
        super(Log4jThread.toThreadName(name));
    }

    public Log4jThread(ThreadGroup group, Runnable target) {
        super(group, target, Log4jThread.toThreadName(Log4jThread.nextThreadNum()));
    }

    public Log4jThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, Log4jThread.toThreadName(name));
    }

    public Log4jThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, Log4jThread.toThreadName(name), stackSize);
    }

    public Log4jThread(ThreadGroup group, String name) {
        super(group, Log4jThread.toThreadName(name));
    }
}

