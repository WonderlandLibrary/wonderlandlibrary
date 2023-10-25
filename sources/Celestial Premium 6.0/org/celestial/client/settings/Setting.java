/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.settings;

import java.util.function.Supplier;

public class Setting {
    protected String name;
    protected Supplier<Boolean> visible;

    public boolean isVisible() {
        return this.visible.get();
    }

    public void setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

