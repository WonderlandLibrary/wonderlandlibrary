/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.text.StringsKt
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.lwjgl.input.Keyboard;

public final class BindsCommand
extends Command {
    public BindsCommand() {
        super("binds", new String[0]);
    }

    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length > 1 && StringsKt.equals((String)stringArray[1], (String)"clear", (boolean)true)) {
            for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
                module.setKeyBind(0);
            }
            this.chat("Removed all binds.");
            return;
        }
        this.chat("\u00a7c\u00a7lBinds");
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
            if (!(module.getKeyBind() != 0)) continue;
            collection2.add(t);
        }
        iterable = (List)collection2;
        bl = false;
        for (Collection collection2 : iterable) {
            Module module = (Module)((Object)collection2);
            boolean bl4 = false;
            ClientUtils.displayChatMessage("\u00a76> \u00a7c" + module.getName() + ": \u00a7a\u00a7l" + Keyboard.getKeyName((int)module.getKeyBind()));
        }
        this.chatSyntax("binds clear");
    }
}

