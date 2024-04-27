/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.EntityUtils;

public final class TargetCommand
extends Command {
    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length > 1) {
            if (StringsKt.equals((String)stringArray[1], (String)"players", (boolean)true)) {
                EntityUtils.targetPlayer = !EntityUtils.targetPlayer;
                this.chat("\u00a77Target player toggled " + (EntityUtils.targetPlayer ? "on" : "off") + '.');
                this.playEdit();
                return;
            }
            if (StringsKt.equals((String)stringArray[1], (String)"mobs", (boolean)true)) {
                EntityUtils.targetMobs = !EntityUtils.targetMobs;
                this.chat("\u00a77Target mobs toggled " + (EntityUtils.targetMobs ? "on" : "off") + '.');
                this.playEdit();
                return;
            }
            if (StringsKt.equals((String)stringArray[1], (String)"animals", (boolean)true)) {
                EntityUtils.targetAnimals = !EntityUtils.targetAnimals;
                this.chat("\u00a77Target animals toggled " + (EntityUtils.targetAnimals ? "on" : "off") + '.');
                this.playEdit();
                return;
            }
            if (StringsKt.equals((String)stringArray[1], (String)"invisible", (boolean)true)) {
                EntityUtils.targetInvisible = !EntityUtils.targetInvisible;
                this.chat("\u00a77Target Invisible toggled " + (EntityUtils.targetInvisible ? "on" : "off") + '.');
                this.playEdit();
                return;
            }
        }
        this.chatSyntax("target <players/mobs/animals/invisible>");
    }

    @Override
    public List tabComplete(String[] stringArray) {
        List list;
        Object object = stringArray;
        boolean bl = false;
        if (((String[])object).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (stringArray.length) {
            case 1: {
                object = CollectionsKt.listOf((Object[])new String[]{"players", "mobs", "animals", "invisible"});
                bl = false;
                Object object2 = object;
                Collection collection = new ArrayList();
                boolean bl2 = false;
                Iterator iterator2 = object2.iterator();
                while (iterator2.hasNext()) {
                    Object t = iterator2.next();
                    String string = (String)t;
                    boolean bl3 = false;
                    if (!StringsKt.startsWith((String)string, (String)stringArray[0], (boolean)true)) continue;
                    collection.add(t);
                }
                list = (List)collection;
                break;
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    public TargetCommand() {
        super("target", new String[0]);
    }
}

