/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive
public class AppenderControlArraySet {
    private final AtomicReference<AppenderControl[]> appenderArray = new AtomicReference<AppenderControl[]>(new AppenderControl[0]);

    public boolean add(AppenderControl control) {
        AppenderControl[] copy;
        AppenderControl[] original;
        boolean success;
        do {
            for (AppenderControl existing : original = this.appenderArray.get()) {
                if (!existing.equals(control)) continue;
                return false;
            }
            copy = Arrays.copyOf(original, original.length + 1);
            copy[copy.length - 1] = control;
        } while (!(success = this.appenderArray.compareAndSet(original, copy)));
        return true;
    }

    public AppenderControl remove(String name) {
        boolean success;
        block0: do {
            success = true;
            AppenderControl[] original = this.appenderArray.get();
            for (int i = 0; i < original.length; ++i) {
                AppenderControl appenderControl = original[i];
                if (!Objects.equals(name, appenderControl.getAppenderName())) continue;
                AppenderControl[] copy = this.removeElementAt(i, original);
                if (this.appenderArray.compareAndSet(original, copy)) {
                    return appenderControl;
                }
                success = false;
                continue block0;
            }
        } while (!success);
        return null;
    }

    private AppenderControl[] removeElementAt(int i, AppenderControl[] array) {
        AppenderControl[] result = Arrays.copyOf(array, array.length - 1);
        System.arraycopy(array, i + 1, result, i, result.length - i);
        return result;
    }

    public Map<String, Appender> asMap() {
        HashMap<String, Appender> result = new HashMap<String, Appender>();
        for (AppenderControl appenderControl : this.appenderArray.get()) {
            result.put(appenderControl.getAppenderName(), appenderControl.getAppender());
        }
        return result;
    }

    public AppenderControl[] clear() {
        return this.appenderArray.getAndSet(new AppenderControl[0]);
    }

    public boolean isEmpty() {
        return this.appenderArray.get().length == 0;
    }

    public AppenderControl[] get() {
        return this.appenderArray.get();
    }

    public String toString() {
        return "AppenderControlArraySet [appenderArray=" + this.appenderArray + "]";
    }
}

