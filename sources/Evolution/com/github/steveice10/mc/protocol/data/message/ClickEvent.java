/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.ClickAction;
import com.github.steveice10.mc.protocol.util.ObjectUtil;
import java.util.Objects;

public class ClickEvent
implements Cloneable {
    private ClickAction action;
    private String value;

    public ClickEvent(ClickAction action, String value) {
        this.action = action;
        this.value = value;
    }

    public ClickAction getAction() {
        return this.action;
    }

    public String getValue() {
        return this.value;
    }

    public ClickEvent clone() {
        return new ClickEvent(this.action, this.value);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClickEvent)) {
            return false;
        }
        ClickEvent that = (ClickEvent)o;
        return this.action == that.action && Objects.equals(this.value, that.value);
    }

    public int hashCode() {
        return ObjectUtil.hashCode(new Object[]{this.action, this.value});
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

