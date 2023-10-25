/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.message.BasicThreadInformation;
import org.apache.logging.log4j.message.ExtendedThreadInformation;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ThreadInformation;

public class ThreadDumpMessage
implements Message {
    private static final long serialVersionUID = -1103400781608841088L;
    private static final ThreadInfoFactory FACTORY;
    private volatile Map<ThreadInformation, StackTraceElement[]> threads;
    private final String title;
    private String formattedMessage;

    public ThreadDumpMessage(String title) {
        this.title = title == null ? "" : title;
        this.threads = FACTORY.createThreadInfo();
    }

    private ThreadDumpMessage(String formattedMsg, String title) {
        this.formattedMessage = formattedMsg;
        this.title = title == null ? "" : title;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ThreadDumpMessage[");
        if (this.title.length() > 0) {
            sb.append("Title=\"").append(this.title).append("\"");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String getFormattedMessage() {
        if (this.formattedMessage != null) {
            return this.formattedMessage;
        }
        StringBuilder sb = new StringBuilder(this.title);
        if (this.title.length() > 0) {
            sb.append("\n");
        }
        for (Map.Entry<ThreadInformation, StackTraceElement[]> entry : this.threads.entrySet()) {
            ThreadInformation info = entry.getKey();
            info.printThreadInfo(sb);
            info.printStack(sb, entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getFormat() {
        return this.title == null ? "" : this.title;
    }

    @Override
    public Object[] getParameters() {
        return null;
    }

    protected Object writeReplace() {
        return new ThreadDumpMessageProxy(this);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }

    @Override
    public Throwable getThrowable() {
        return null;
    }

    static {
        Method[] methods = ThreadInfo.class.getMethods();
        boolean basic = true;
        for (Method method : methods) {
            if (!method.getName().equals("getLockInfo")) continue;
            basic = false;
            break;
        }
        FACTORY = basic ? new BasicThreadInfoFactory() : new ExtendedThreadInfoFactory();
    }

    private static class ExtendedThreadInfoFactory
    implements ThreadInfoFactory {
        private ExtendedThreadInfoFactory() {
        }

        @Override
        public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
            ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            ThreadInfo[] array = bean.dumpAllThreads(true, true);
            HashMap<ThreadInformation, StackTraceElement[]> threads = new HashMap<ThreadInformation, StackTraceElement[]>(array.length);
            for (ThreadInfo info : array) {
                threads.put(new ExtendedThreadInformation(info), info.getStackTrace());
            }
            return threads;
        }
    }

    private static class BasicThreadInfoFactory
    implements ThreadInfoFactory {
        private BasicThreadInfoFactory() {
        }

        @Override
        public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
            Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
            HashMap<ThreadInformation, StackTraceElement[]> threads = new HashMap<ThreadInformation, StackTraceElement[]>(map.size());
            for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
                threads.put(new BasicThreadInformation(entry.getKey()), entry.getValue());
            }
            return threads;
        }
    }

    private static interface ThreadInfoFactory {
        public Map<ThreadInformation, StackTraceElement[]> createThreadInfo();
    }

    private static class ThreadDumpMessageProxy
    implements Serializable {
        private static final long serialVersionUID = -3476620450287648269L;
        private final String formattedMsg;
        private final String title;

        public ThreadDumpMessageProxy(ThreadDumpMessage msg) {
            this.formattedMsg = msg.getFormattedMessage();
            this.title = msg.title;
        }

        protected Object readResolve() {
            return new ThreadDumpMessage(this.formattedMsg, this.title);
        }
    }
}

