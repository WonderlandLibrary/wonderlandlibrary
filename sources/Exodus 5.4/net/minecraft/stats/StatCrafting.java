/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.stats;

import net.minecraft.item.Item;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.stats.StatBase;
import net.minecraft.util.IChatComponent;

public class StatCrafting
extends StatBase {
    private final Item field_150960_a;

    public Item func_150959_a() {
        return this.field_150960_a;
    }

    public StatCrafting(String string, String string2, IChatComponent iChatComponent, Item item) {
        super(String.valueOf(string) + string2, iChatComponent);
        this.field_150960_a = item;
        int n = Item.getIdFromItem(item);
        if (n != 0) {
            IScoreObjectiveCriteria.INSTANCES.put(String.valueOf(string) + n, this.func_150952_k());
        }
    }
}

