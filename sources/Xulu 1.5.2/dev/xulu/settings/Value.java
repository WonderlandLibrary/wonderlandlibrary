// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.settings;

import com.elementars.eclient.command.Command;
import java.util.Iterator;
import com.elementars.eclient.Xulu;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.ArrayList;
import com.elementars.eclient.module.Module;

public class Value<T>
{
    private String name;
    private Module parent;
    private Mode mode;
    private T value;
    private ArrayList<T> options;
    private T min;
    private T max;
    private Consumer<OnChangedValue<T>> changeTask;
    private Predicate<T> visibleCheck;
    private Predicate<T> filter;
    private String filterError;
    
    public Value(final String name, final Module parent, final T value, final ArrayList<T> options) {
        this.changeTask = null;
        this.visibleCheck = null;
        this.filter = null;
        this.filterError = null;
        this.name = name;
        this.parent = parent;
        this.value = value;
        this.options = options;
        this.mode = Mode.MODE;
    }
    
    public Value(final String name, final Module parent, final T value, final T[] options) {
        this.changeTask = null;
        this.visibleCheck = null;
        this.filter = null;
        this.filterError = null;
        this.name = name;
        this.parent = parent;
        this.value = value;
        this.options = new ArrayList<T>((Collection<? extends T>)Arrays.asList(options));
        this.mode = Mode.MODE;
        if (value instanceof Enum) {
            this.mode = Mode.ENUM;
        }
    }
    
    public Value(final String name, final Module parent, final T value) {
        this.changeTask = null;
        this.visibleCheck = null;
        this.filter = null;
        this.filterError = null;
        this.name = name;
        this.parent = parent;
        this.value = value;
        this.mode = Mode.UNKNOWN;
        if (value instanceof Boolean) {
            this.mode = Mode.TOGGLE;
        }
        else if (value instanceof Bind) {
            this.mode = Mode.BIND;
        }
        else if (value instanceof TextBox) {
            this.mode = Mode.TEXT;
        }
    }
    
    public Value(final String name, final Module parent, final T value, final T min, final T max) {
        this.changeTask = null;
        this.visibleCheck = null;
        this.filter = null;
        this.filterError = null;
        this.name = name;
        this.parent = parent;
        this.value = value;
        this.min = min;
        this.max = max;
        this.mode = Mode.NUMBER;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Module getParentMod() {
        return this.parent;
    }
    
    public ArrayList<T> getOptions() {
        return this.options;
    }
    
    public String getCorrectString(final String stringIn) {
        if (this.value instanceof String) {
            for (final String s : this.options) {
                if (s.equalsIgnoreCase(stringIn)) {
                    return s;
                }
            }
            return null;
        }
        if (this.mode == Mode.ENUM) {
            for (final T s2 : this.options) {
                if (s2.toString().equalsIgnoreCase(stringIn)) {
                    return Xulu.getTitle(s2.toString());
                }
            }
            return null;
        }
        return null;
    }
    
    public T getCorrectOption(final String stringIn) {
        if (this.mode == Mode.ENUM) {
            for (final T s : this.options) {
                if (s.toString().equalsIgnoreCase(stringIn)) {
                    return s;
                }
            }
            return null;
        }
        return null;
    }
    
    public void setEnumValue(final String value) {
        for (final Enum e : (Enum[])((Enum)this.value).getClass().getEnumConstants()) {
            if (e.name().equalsIgnoreCase(value)) {
                final T old = this.value;
                this.value = (T)e;
                if (this.changeTask != null) {
                    this.changeTask.accept(new OnChangedValue<T>(old, (T)e));
                }
            }
        }
    }
    
    public T getMax() {
        return this.max;
    }
    
    public T getMin() {
        return this.min;
    }
    
    public T getValue() {
        return this.value;
    }
    
    public void setValue(final T value) {
        if (this.value != value) {
            if (this.filter != null && !this.filter.test(value)) {
                if (this.filterError != null) {
                    Command.sendChatMessage("&c" + this.filterError);
                }
                return;
            }
            final T old = this.value;
            this.value = value;
            if (this.changeTask != null) {
                this.changeTask.accept(new OnChangedValue<T>(old, value));
            }
        }
    }
    
    public boolean isMode() {
        return this.mode == Mode.MODE;
    }
    
    public boolean isToggle() {
        return this.mode == Mode.TOGGLE;
    }
    
    public boolean isNumber() {
        return this.mode == Mode.NUMBER;
    }
    
    public boolean isEnum() {
        return this.mode == Mode.ENUM;
    }
    
    public boolean isBind() {
        return this.mode == Mode.BIND;
    }
    
    public boolean isText() {
        return this.mode == Mode.TEXT;
    }
    
    public Value<T> onChanged(final Consumer<OnChangedValue<T>> run) {
        this.changeTask = run;
        return this;
    }
    
    public Value<T> visibleWhen(final Predicate<T> predicate) {
        this.visibleCheck = predicate;
        return this;
    }
    
    public Value<T> newValueFilter(final Predicate<T> predicate) {
        this.filter = predicate;
        return this;
    }
    
    public Value<T> withFilterError(final String s) {
        this.filterError = s;
        return this;
    }
    
    public boolean isVisible() {
        return this.visibleCheck == null || this.visibleCheck.test(this.value);
    }
    
    enum Mode
    {
        UNKNOWN, 
        MODE, 
        ENUM, 
        TOGGLE, 
        NUMBER, 
        BIND, 
        TEXT;
    }
}
