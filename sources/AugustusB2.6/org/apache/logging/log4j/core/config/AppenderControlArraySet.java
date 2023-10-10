// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import java.util.HashMap;
import org.apache.logging.log4j.core.Appender;
import java.util.Map;
import java.util.Objects;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive
public class AppenderControlArraySet
{
    private static final AtomicReferenceFieldUpdater<AppenderControlArraySet, AppenderControl[]> appenderArrayUpdater;
    private volatile AppenderControl[] appenderArray;
    
    public AppenderControlArraySet() {
        this.appenderArray = AppenderControl.EMPTY_ARRAY;
    }
    
    public boolean add(final AppenderControl control) {
        boolean success;
        do {
            final AppenderControl[] appenderArray;
            final AppenderControl[] original = appenderArray = this.appenderArray;
            for (final AppenderControl existing : appenderArray) {
                if (existing.equals(control)) {
                    return false;
                }
            }
            final AppenderControl[] copy = Arrays.copyOf(original, original.length + 1);
            copy[copy.length - 1] = control;
            success = AppenderControlArraySet.appenderArrayUpdater.compareAndSet(this, original, copy);
        } while (!success);
        return true;
    }
    
    public AppenderControl remove(final String name) {
        boolean success;
        do {
            success = true;
            final AppenderControl[] original = this.appenderArray;
            int i = 0;
            while (i < original.length) {
                final AppenderControl appenderControl = original[i];
                if (Objects.equals(name, appenderControl.getAppenderName())) {
                    final AppenderControl[] copy = this.removeElementAt(i, original);
                    if (AppenderControlArraySet.appenderArrayUpdater.compareAndSet(this, original, copy)) {
                        return appenderControl;
                    }
                    success = false;
                    break;
                }
                else {
                    ++i;
                }
            }
        } while (!success);
        return null;
    }
    
    private AppenderControl[] removeElementAt(final int i, final AppenderControl[] array) {
        final AppenderControl[] result = Arrays.copyOf(array, array.length - 1);
        System.arraycopy(array, i + 1, result, i, result.length - i);
        return result;
    }
    
    public Map<String, Appender> asMap() {
        final Map<String, Appender> result = new HashMap<String, Appender>();
        for (final AppenderControl appenderControl : this.appenderArray) {
            result.put(appenderControl.getAppenderName(), appenderControl.getAppender());
        }
        return result;
    }
    
    public AppenderControl[] clear() {
        return AppenderControlArraySet.appenderArrayUpdater.getAndSet(this, AppenderControl.EMPTY_ARRAY);
    }
    
    public boolean isEmpty() {
        return this.appenderArray.length == 0;
    }
    
    public AppenderControl[] get() {
        return this.appenderArray;
    }
    
    @Override
    public String toString() {
        return "AppenderControlArraySet [appenderArray=" + Arrays.toString(this.appenderArray) + "]";
    }
    
    static {
        appenderArrayUpdater = AtomicReferenceFieldUpdater.newUpdater(AppenderControlArraySet.class, AppenderControl[].class, "appenderArray");
    }
}
