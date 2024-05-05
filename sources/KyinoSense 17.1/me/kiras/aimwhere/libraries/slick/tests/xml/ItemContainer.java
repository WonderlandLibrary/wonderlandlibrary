/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests.xml;

import java.util.ArrayList;
import me.kiras.aimwhere.libraries.slick.tests.xml.Item;

public class ItemContainer
extends Item {
    private ArrayList items = new ArrayList();

    private void add(Item item) {
        this.items.add(item);
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setCondition(int condition) {
        this.condition = condition;
    }

    @Override
    public void dump(String prefix) {
        System.out.println(prefix + "Item Container " + this.name + "," + this.condition);
        for (int i = 0; i < this.items.size(); ++i) {
            ((Item)this.items.get(i)).dump(prefix + "\t");
        }
    }
}

