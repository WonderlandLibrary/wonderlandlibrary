/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests.xml;

import java.util.ArrayList;
import me.kiras.aimwhere.libraries.slick.tests.xml.Item;

public class Inventory {
    private ArrayList items = new ArrayList();

    private void add(Item item) {
        this.items.add(item);
    }

    public void dump(String prefix) {
        System.out.println(prefix + "Inventory");
        for (int i = 0; i < this.items.size(); ++i) {
            ((Item)this.items.get(i)).dump(prefix + "\t");
        }
    }
}

