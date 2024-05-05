package net.minecraft.event;

import net.minecraft.event.ClickEvent;

public class ClickEvent {
    private final Action action;
    private final String value;

    public ClickEvent(Action theAction, String theValue) {
        this.action = theAction;
        this.value = theValue;
    }

    public Action getAction() {
        return this.action;
    }

    public String getValue() {
        return this.value;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            ClickEvent clickevent = (ClickEvent)p_equals_1_;
            if (this.action != clickevent.action) {
                return false;
            }
            return !(this.value != null ? !this.value.equals((Object)clickevent.value) : clickevent.value != null);
        }
        return false;
    }

    public String toString() {
        return "ClickEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
    }

    public int hashCode() {
        int i = this.action.hashCode();
        i = 31 * i + (this.value != null ? this.value.hashCode() : 0);
        return i;
    }
}
