/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.text.StringsKt
 *  org.lwjgl.input.Keyboard
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
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import org.lwjgl.input.Keyboard;

public final class BindCommand
extends Command {
    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length > 2) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(stringArray[1]);
            if (module == null) {
                this.chat("Module \u00a7a\u00a7l" + stringArray[1] + "\u00a73 not found.");
                return;
            }
            String string = stringArray[2];
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            int n = Keyboard.getKeyIndex((String)string2.toUpperCase());
            module.setKeyBind(n);
            this.chat("Bound module \u00a7a\u00a7l" + module.getName() + "\u00a73 to key \u00a7a\u00a7l" + Keyboard.getKeyName((int)n) + "\u00a73.");
            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Bind", "Bound " + module.getName() + " to " + Keyboard.getKeyName((int)n), NotifyType.INFO, 1500, 500));
            this.playEdit();
            return;
        }
        this.chatSyntax(new String[]{"<module> <key>", "<module> none"});
    }

    public BindCommand() {
        super("bind", new String[0]);
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

