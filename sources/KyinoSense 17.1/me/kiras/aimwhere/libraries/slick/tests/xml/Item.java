/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests.xml;

public class Item {
    protected String name;
    protected int condition;

    public void dump(String prefix) {
        System.out.println(prefix + "Item " + this.name + "," + this.condition);
    }
}

