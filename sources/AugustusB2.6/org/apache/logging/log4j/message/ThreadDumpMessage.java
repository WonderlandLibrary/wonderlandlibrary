// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import java.util.HashMap;
import java.io.Serializable;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.ServiceLoader;
import java.util.Map;
import org.apache.logging.log4j.util.StringBuilderFormattable;

@AsynchronouslyFormattable
public class ThreadDumpMessage implements Message, StringBuilderFormattable
{
    private static final long serialVersionUID = -1103400781608841088L;
    private static ThreadInfoFactory FACTORY;
    private volatile Map<ThreadInformation, StackTraceElement[]> threads;
    private final String title;
    private String formattedMessage;
    
    public ThreadDumpMessage(final String title) {
        this.title = ((title == null) ? "" : title);
        this.threads = getFactory().createThreadInfo();
    }
    
    private ThreadDumpMessage(final String formattedMsg, final String title) {
        this.formattedMessage = formattedMsg;
        this.title = ((title == null) ? "" : title);
    }
    
    private static ThreadInfoFactory getFactory() {
        if (ThreadDumpMessage.FACTORY == null) {
            ThreadDumpMessage.FACTORY = initFactory(ThreadDumpMessage.class.getClassLoader());
        }
        return ThreadDumpMessage.FACTORY;
    }
    
    private static ThreadInfoFactory initFactory(final ClassLoader classLoader) {
        final ServiceLoader<ThreadInfoFactory> serviceLoader = ServiceLoader.load(ThreadInfoFactory.class, classLoader);
        ThreadInfoFactory result = null;
        try {
            for (Iterator<ThreadInfoFactory> iterator = serviceLoader.iterator(); result == null && iterator.hasNext(); result = iterator.next()) {}
        }
        catch (ServiceConfigurationError | LinkageError | Exception serviceConfigurationError) {
            final Throwable t;
            final Throwable unavailable = t;
            StatusLogger.getLogger().info("ThreadDumpMessage uses BasicThreadInfoFactory: could not load extended ThreadInfoFactory: {}", unavailable.toString());
            result = null;
        }
        return (result == null) ? new BasicThreadInfoFactory() : result;
    }
    
    @Override
    public String toString() {
        return this.getFormattedMessage();
    }
    
    @Override
    public String getFormattedMessage() {
        if (this.formattedMessage != null) {
            return this.formattedMessage;
        }
        final StringBuilder sb = new StringBuilder(255);
        this.formatTo(sb);
        return sb.toString();
    }
    
    @Override
    public void formatTo(final StringBuilder sb) {
        sb.append(this.title);
        if (this.title.length() > 0) {
            sb.append('\n');
        }
        for (final Map.Entry<ThreadInformation, StackTraceElement[]> entry : this.threads.entrySet()) {
            final ThreadInformation info = entry.getKey();
            info.printThreadInfo(sb);
            info.printStack(sb, entry.getValue());
            sb.append('\n');
        }
    }
    
    @Override
    public String getFormat() {
        return (this.title == null) ? "" : this.title;
    }
    
    @Override
    public Object[] getParameters() {
        return null;
    }
    
    protected Object writeReplace() {
        return new ThreadDumpMessageProxy(this);
    }
    
    private void readObject(final ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
    
    @Override
    public Throwable getThrowable() {
        return null;
    }
    
    private static class ThreadDumpMessageProxy implements Serializable
    {
        private static final long serialVersionUID = -3476620450287648269L;
        private final String formattedMsg;
        private final String title;
        
        ThreadDumpMessageProxy(final ThreadDumpMessage msg) {
            this.formattedMsg = msg.getFormattedMessage();
            this.title = msg.title;
        }
        
        protected Object readResolve() {
            return new ThreadDumpMessage(this.formattedMsg, this.title, null);
        }
    }
    
    private static class BasicThreadInfoFactory implements ThreadInfoFactory
    {
        @Override
        public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
            final Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
            final Map<ThreadInformation, StackTraceElement[]> threads = new HashMap<ThreadInformation, StackTraceElement[]>(map.size());
            for (final Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
                threads.put(new BasicThreadInformation(entry.getKey()), entry.getValue());
            }
            return threads;
        }
    }
    
    public interface ThreadInfoFactory
    {
        Map<ThreadInformation, StackTraceElement[]> createThreadInfo();
    }
}
