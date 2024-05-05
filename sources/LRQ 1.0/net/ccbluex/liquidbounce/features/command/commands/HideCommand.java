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
    /*
     * WARNING - void declaration
     */
    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            if (StringsKt.equals((String)args[1], (String)"list", (boolean)true)) {
                void $this$filterTo$iv$iv;
                this.chat("\u00a7c\u00a7lHidden");
                Iterable $this$filter$iv = LiquidBounce.INSTANCE.getModuleManager().getModules();
                boolean $i$f$filter = false;
                Iterable iterable = $this$filter$iv;
                Collection destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    Module it = (Module)element$iv$iv;
                    boolean bl = false;
                    if (!(!it.getArray())) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                Iterable $this$forEach$iv = (List)destination$iv$iv;
                boolean $i$f$forEach = false;
                for (Object element$iv : $this$forEach$iv) {
                    Module it = (Module)element$iv;
                    boolean bl = false;
                    ClientUtils.displayChatMessage("\u00a76> \u00a7c" + it.getName());
                }
                return;
            }
            if (StringsKt.equals((String)args[1], (String)"clear", (boolean)true)) {
                for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
                    module.setArray(true);
                }
                this.chat("Cleared hidden modules.");
                return;
            }
            if (StringsKt.equals((String)args[1], (String)"reset", (boolean)true)) {
                for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
                    module.setArray(module.getClass().getAnnotation(ModuleInfo.class).array());
                }
                this.chat("Reset hidden modules.");
                return;
            }
            Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(args[1]);
            if (module == null) {
                this.chat("Module \u00a7a\u00a7l" + args[1] + "\u00a73 not found.");
                return;
            }
            module.setArray(!module.getArray());
            this.chat("Module \u00a7a\u00a7l" + module.getName() + "\u00a73 is now \u00a7a\u00a7l" + (module.getArray() ? "visible" : "invisible") + "\u00a73 on the array list.");
            this.playEdit();
            return;
        }
        this.chatSyntax("hide <module/list/clear/reset>");
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

    public HideCommand() {
        super("hide", new String[0]);
    }
}

