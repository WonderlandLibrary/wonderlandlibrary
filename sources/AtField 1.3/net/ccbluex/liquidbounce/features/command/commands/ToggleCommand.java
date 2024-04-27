/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;

public final class ToggleCommand
extends Command {
    public ToggleCommand() {
        super("toggle", "t");
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length <= 1) {
            this.chatSyntax("toggle <module> [on/off]");
            return;
        }
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(stringArray[1]);
        if (module == null) {
            this.chat("Module '" + stringArray[1] + "' not found.");
            return;
        }
        if (stringArray.length > 2) {
            String string = stringArray[2];
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            if (!string3.equals("on") && !string3.equals("off")) {
                this.chatSyntax("toggle <module> [on/off]");
                return;
            }
            module.setState(string3.equals("on"));
        } else {
            module.toggle();
        }
        this.chat((module.getState() ? "Enabled" : "Disabled") + " module \u00a78" + module.getName() + "\u00a73.");
    }

    @Override
    public List tabComplete(String[] stringArray) {
        List list;
        Object object = stringArray;
        boolean bl = false;
        if (((String[])object).length == 0) {
            return CollectionsKt.emptyList();
        }
        object = stringArray[0];
        switch (stringArray.length) {
            case 1: {
                boolean bl2;
                Object object2;
                Iterable iterable = LiquidBounce.INSTANCE.getModuleManager().getModules();
                boolean bl3 = false;
                Iterable iterable2 = iterable;
                Collection collection = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)iterable, (int)10));
                boolean bl4 = false;
                for (Object t : iterable2) {
                    object2 = (Module)t;
                    Collection collection2 = collection;
                    bl2 = false;
                    String string = ((Module)object2).getName();
                    collection2.add(string);
                }
                iterable = (List)collection;
                bl3 = false;
                iterable2 = iterable;
                collection = new ArrayList();
                bl4 = false;
                for (Object t : iterable2) {
                    object2 = (String)t;
                    bl2 = false;
                    if (!StringsKt.startsWith((String)object2, (String)object, (boolean)true)) continue;
                    collection.add(t);
                }
                list = CollectionsKt.toList((Iterable)((List)collection));
                break;
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }
}

