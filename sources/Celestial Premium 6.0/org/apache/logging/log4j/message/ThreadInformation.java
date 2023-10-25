/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

interface ThreadInformation {
    public void printThreadInfo(StringBuilder var1);

    public void printStack(StringBuilder var1, StackTraceElement[] var2);
}

