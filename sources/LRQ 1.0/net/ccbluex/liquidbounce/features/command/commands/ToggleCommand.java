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
    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void execute(String[] args) {
        if (args.length <= 1) {
            this.chatSyntax("toggle <module> [on/off]");
            return;
        }
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(args[1]);
        if (module == null) {
            this.chat("Module '" + args[1] + "' not found.");
            return;
        }
        if (args.length > 2) {
            String string = args[2];
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String newState = string2.toLowerCase();
            if (!newState.equals("on") && !newState.equals("off")) {
                this.chatSyntax("toggle <module> [on/off]");
                return;
            }
            module.setState(newState.equals("on"));
        } else {
            module.toggle();
        }
        this.chat((module.getState() ? "Enabled" : "Disabled") + " module \u00a78" + module.getName() + "\u00a73.");
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public List<String> tabComplete(String[] args) {
        List list;
        String[] stringArray = args;
        boolean bl = false;
        if (stringArray.length == 0) {
            return CollectionsKt.emptyList();
        }
        String moduleName = args[0];
        switch (args.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                String it;
                Iterable $this$mapTo$iv$iv;
                Iterable $this$map$iv = LiquidBounce.INSTANCE.getModuleManager().getModules();
                boolean $i$f$map = false;
                Iterable iterable = $this$map$iv;
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    Module module = (Module)item$iv$iv;
                    Collection collection = destination$iv$iv;
                    boolean bl2 = false;
                    String string = ((Module)((Object)it)).getName();
                    collection.add(string);
                }
                Iterable $this$filter$iv = (List)destination$iv$iv;
                boolean $i$f$filter = false;
                $this$mapTo$iv$iv = $this$filter$iv;
                destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    it = (String)element$iv$iv;
                    boolean bl3 = false;
                    if (!StringsKt.startsWith((String)it, (String)moduleName, (boolean)true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                list = CollectionsKt.toList((Iterable)((List)destination$iv$iv));
                break;
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    public ToggleCommand() {
        super("toggle", "t");
    }
}

