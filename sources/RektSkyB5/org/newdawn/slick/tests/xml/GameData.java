/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests.xml;

import java.util.ArrayList;
import org.newdawn.slick.tests.xml.Entity;

public class GameData {
    private ArrayList entities = new ArrayList();

    private void add(Entity entity) {
        this.entities.add(entity);
    }

    public void dump(String prefix) {
        System.out.println(prefix + "GameData");
        for (int i2 = 0; i2 < this.entities.size(); ++i2) {
            ((Entity)this.entities.get(i2)).dump(prefix + "\t");
        }
    }
}

