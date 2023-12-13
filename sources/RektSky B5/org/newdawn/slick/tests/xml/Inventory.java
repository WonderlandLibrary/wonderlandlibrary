/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests.xml;

import java.util.ArrayList;
import org.newdawn.slick.tests.xml.Item;

public class Inventory {
    private ArrayList items = new ArrayList();

    private void add(Item item) {
        this.items.add(item);
    }

    public void dump(String prefix) {
        System.out.println(prefix + "Inventory");
        for (int i2 = 0; i2 < this.items.size(); ++i2) {
            ((Item)this.items.get(i2)).dump(prefix + "\t");
        }
    }
}

