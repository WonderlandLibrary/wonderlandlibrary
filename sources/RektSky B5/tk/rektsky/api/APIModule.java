/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.api;

import tk.rektsky.api.Category;
import tk.rektsky.event.Event;
import tk.rektsky.script.BoolListener;

public class APIModule {
    public String name;
    public Category category;
    public BoolListener l1 = null;
    public BoolListener l2 = null;

    public APIModule(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public void init(BoolListener l1, BoolListener l2) {
        this.l1 = l1;
        this.l2 = l2;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    @Deprecated
    public void onEvent(Event event) {
    }

    public void setEnabled(boolean newState) {
        this.l1.run(newState);
    }

    public void setRawEnabled(boolean newState) {
        this.l2.run(newState);
    }
}

