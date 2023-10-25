/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.HoverAction;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.util.ObjectUtil;
import java.util.Objects;

public class HoverEvent
implements Cloneable {
    private HoverAction action;
    private Message value;

    public HoverEvent(HoverAction action, Message value) {
        this.action = action;
        this.value = value;
    }

    public HoverAction getAction() {
        return this.action;
    }

    public Message getValue() {
        return this.value;
    }

    public HoverEvent clone() {
        return new HoverEvent(this.action, this.value.clone());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HoverEvent)) {
            return false;
        }
        HoverEvent that = (HoverEvent)o;
        return this.action == that.action && Objects.equals(this.value, that.value);
    }

    public int hashCode() {
        return ObjectUtil.hashCode(new Object[]{this.action, this.value});
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

