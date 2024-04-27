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
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public final class HideCommand
extends Command {
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

    public HideCommand() {
        super("hide", new String[0]);
    }

    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length > 1) {
            if (StringsKt.equals((String)stringArray[1], (String)"list", (boolean)true)) {
                this.chat("\u00a7c\u00a7lHidden");
                Iterable iterable = LiquidBounce.INSTANCE.getModuleManager().getModules();
                boolean bl = false;
                Iterable iterable2 = iterable;
                Collection collection2 = new ArrayList();
                boolean bl2 = false;
                Iterator iterator2 = iterable2.iterator();
                while (iterator2.hasNext()) {
                    Object t = iterator2.next();
                    Module module = (Module)t;
                    boolean bl3 = false;
                    if (!(!module.getArray())) continue;
                    collection2.add(t);
                }
                iterable = (List)collection2;
                bl = false;
                for (Collection collection2 : iterable) {
                    Module module = (Module)((Object)collection2);
                    boolean bl4 = false;
                    ClientUtils.displayChatMessage("\u00a76> \u00a7c" + module.getName());
                }
                return;
            }
            if (StringsKt.equals((String)stringArray[1], (String)"clear", (boolean)true)) {
                for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
                    module.setArray(true);
                }
                this.chat("Cleared hidden modules.");
                return;
            }
            if (StringsKt.equals((String)stringArray[1], (String)"reset", (boolean)true)) {
                for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
                    module.setArray(module.getClass().getAnnotation(ModuleInfo.class).array());
                }
                this.chat("Reset hidden modules.");
                return;
            }
            Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(stringArray[1]);
            if (module == null) {
                this.chat("Module \u00a7a\u00a7l" + stringArray[1] + "\u00a73 not found.");
                return;
            }
            module.setArray(!module.getArray());
            this.chat("Module \u00a7a\u00a7l" + module.getName() + "\u00a73 is now \u00a7a\u00a7l" + (module.getArray() ? "visible" : "invisible") + "\u00a73 on the array list.");
            this.playEdit();
            return;
        }
        this.chatSyntax("hide <module/list/clear/reset>");
    }
}

