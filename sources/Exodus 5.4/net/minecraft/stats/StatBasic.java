/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.stats;

import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.IChatComponent;

public class StatBasic
extends StatBase {
    public StatBasic(String string, IChatComponent iChatComponent, IStatType iStatType) {
        super(string, iChatComponent, iStatType);
    }

    @Override
    public StatBase registerStat() {
        super.registerStat();
        StatList.generalStats.add(this);
        return this;
    }

    public StatBasic(String string, IChatComponent iChatComponent) {
        super(string, iChatComponent);
    }
}

