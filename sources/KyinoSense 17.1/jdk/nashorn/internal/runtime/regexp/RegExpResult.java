/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp;

public final class RegExpResult {
    final Object[] groups;
    final int index;
    final String input;

    public RegExpResult(String input, int index, Object[] groups2) {
        this.input = input;
        this.index = index;
        this.groups = groups2;
    }

    public Object[] getGroups() {
        return this.groups;
    }

    public String getInput() {
        return this.input;
    }

    public int getIndex() {
        return this.index;
    }

    public int length() {
        return ((String)this.groups[0]).length();
    }

    public Object getGroup(int groupIndex) {
        return groupIndex >= 0 && groupIndex < this.groups.length ? this.groups[groupIndex] : "";
    }

    public Object getLastParen() {
        return this.groups.length > 1 ? this.groups[this.groups.length - 1] : "";
    }
}

