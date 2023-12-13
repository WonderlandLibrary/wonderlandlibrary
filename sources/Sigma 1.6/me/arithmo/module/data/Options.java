/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.data;

public class Options {
    public String name;
    public String selected;
    public String[] options;

    public Options(String name, String selected, String[] options) {
        this.name = name;
        this.selected = selected;
        this.options = options;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelected() {
        return this.selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String[] getOptions() {
        return this.options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}

