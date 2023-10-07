/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j;

class NameValue {
    String key;
    String value;

    public NameValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String toString() {
        return this.key + "=" + this.value;
    }
}

