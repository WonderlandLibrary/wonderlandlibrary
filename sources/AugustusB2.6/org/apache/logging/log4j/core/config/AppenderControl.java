// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import java.util.Objects;
import org.apache.logging.log4j.core.filter.Filterable;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.filter.AbstractFilterable;

public class AppenderControl extends AbstractFilterable
{
    static final AppenderControl[] EMPTY_ARRAY;
    private final ThreadLocal<AppenderControl> recursive;
    private final Appender appender;
    private final Level level;
    private final int intLevel;
    private final String appenderName;
    
    public AppenderControl(final Appender appender, final Level level, final Filter filter) {
        super(filter);
        this.recursive = new ThreadLocal<AppenderControl>();
        this.appender = appender;
        this.appenderName = appender.getName();
        this.level = level;
        this.intLevel = ((level == null) ? Level.ALL.intLevel() : level.intLevel());
        this.start();
    }
    
    public String getAppenderName() {
        return this.appenderName;
    }
    
    public Appender getAppender() {
        return this.appender;
    }
    
    public void callAppender(final LogEvent event) {
        if (this.shouldSkip(event)) {
            return;
        }
        this.callAppenderPreventRecursion(event);
    }
    
    private boolean shouldSkip(final LogEvent event) {
        return this.isFilteredByAppenderControl(event) || this.isFilteredByLevel(event) || this.isRecursiveCall();
    }
    
    @PerformanceSensitive
    private boolean isFilteredByAppenderControl(final LogEvent event) {
        final Filter filter = this.getFilter();
        return filter != null && Filter.Result.DENY == filter.filter(event);
    }
    
    @PerformanceSensitive
    private boolean isFilteredByLevel(final LogEvent event) {
        return this.level != null && this.intLevel < event.getLevel().intLevel();
    }
    
    @PerformanceSensitive
    private boolean isRecursiveCall() {
        if (this.recursive.get() != null) {
            this.appenderErrorHandlerMessage("Recursive call to appender ");
            return true;
        }
        return false;
    }
    
    private String appenderErrorHandlerMessage(final String prefix) {
        final String result = this.createErrorMsg(prefix);
        this.appender.getHandler().error(result);
        return result;
    }
    
    private void callAppenderPreventRecursion(final LogEvent event) {
        try {
            this.recursive.set(this);
            this.callAppender0(event);
        }
        finally {
            this.recursive.set(null);
        }
    }
    
    private void callAppender0(final LogEvent event) {
        this.ensureAppenderStarted();
        if (!this.isFilteredByAppender(event)) {
            this.tryCallAppender(event);
        }
    }
    
    private void ensureAppenderStarted() {
        if (!this.appender.isStarted()) {
            this.handleError("Attempted to append to non-started appender ");
        }
    }
    
    private void handleError(final String prefix) {
        final String msg = this.appenderErrorHandlerMessage(prefix);
        if (!this.appender.ignoreExceptions()) {
            throw new AppenderLoggingException(msg);
        }
    }
    
    private String createErrorMsg(final String prefix) {
        return prefix + this.appender.getName();
    }
    
    private boolean isFilteredByAppender(final LogEvent event) {
        return this.appender instanceof Filterable && ((Filterable)this.appender).isFiltered(event);
    }
    
    private void tryCallAppender(final LogEvent event) {
        try {
            this.appender.append(event);
        }
        catch (RuntimeException error) {
            this.handleAppenderError(event, error);
        }
        catch (Exception error2) {
            this.handleAppenderError(event, new AppenderLoggingException(error2));
        }
    }
    
    private void handleAppenderError(final LogEvent event, final RuntimeException ex) {
        this.appender.getHandler().error(this.createErrorMsg("An exception occurred processing Appender "), event, ex);
        if (!this.appender.ignoreExceptions()) {
            throw ex;
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AppenderControl)) {
            return false;
        }
        final AppenderControl other = (AppenderControl)obj;
        return Objects.equals(this.appenderName, other.appenderName);
    }
    
    @Override
    public int hashCode() {
        return this.appenderName.hashCode();
    }
    
    @Override
    public String toString() {
        return super.toString() + "[appender=" + this.appender + ", appenderName=" + this.appenderName + ", level=" + this.level + ", intLevel=" + this.intLevel + ", recursive=" + this.recursive + ", filter=" + this.getFilter() + "]";
    }
    
    static {
        EMPTY_ARRAY = new AppenderControl[0];
    }
}
