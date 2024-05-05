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
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class EnchantCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length > 2) {
            int n;
            int n2;
            IItemStack item;
            if (MinecraftInstance.mc.getPlayerController().isNotCreative()) {
                this.chat("\u00a7c\u00a7lError: \u00a73You need to be in creative mode.");
                return;
            }
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            IItemStack iItemStack = item = iEntityPlayerSP != null ? iEntityPlayerSP.getHeldItem() : null;
            if ((iItemStack != null ? iItemStack.getItem() : null) == null) {
                this.chat("\u00a7c\u00a7lError: \u00a73You need to hold an item.");
                return;
            }
            try {
                String string = args[1];
                boolean bl = false;
                n2 = Integer.parseInt(string);
            }
            catch (NumberFormatException e) {
                IEnchantment enchantment = MinecraftInstance.functions.getEnchantmentByLocation(args[1]);
                if (enchantment == null) {
                    this.chat("There is no enchantment with the name '" + args[1] + '\'');
                    return;
                }
                n2 = enchantment.getEffectId();
            }
            int enchantID = n2;
            IEnchantment enchantment = MinecraftInstance.functions.getEnchantmentById(enchantID);
            if (enchantment == null) {
                this.chat("There is no enchantment with the ID '" + enchantID + '\'');
                return;
            }
            try {
                String string = args[2];
                boolean bl = false;
                n = Integer.parseInt(string);
            }
            catch (NumberFormatException e) {
                this.chatSyntaxError();
                return;
            }
            int level = n;
            item.addEnchantment(enchantment, level);
            this.chat(enchantment.getTranslatedName(level) + " added to " + item.getDisplayName() + '.');
            return;
        }
        this.chatSyntax("enchant <type> [level]");
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public List<String> tabComplete(String[] args) {
        String[] stringArray = args;
        boolean bl = false;
        if (stringArray.length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                String it;
                Iterable $this$mapTo$iv$iv;
                Iterable $this$map$iv = MinecraftInstance.functions.getEnchantments();
                boolean $i$f$map = false;
                Iterable iterable = $this$map$iv;
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    IResourceLocation iResourceLocation = (IResourceLocation)item$iv$iv;
                    Collection collection = destination$iv$iv;
                    boolean bl2 = false;
                    String string = it.getResourcePath();
                    boolean bl3 = false;
                    String string2 = string;
                    if (string2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string3 = string2.toLowerCase();
                    collection.add(string3);
                }
                Iterable $this$filter$iv = (List)destination$iv$iv;
                boolean $i$f$filter = false;
                $this$mapTo$iv$iv = $this$filter$iv;
                destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    it = (String)element$iv$iv;
                    boolean bl4 = false;
                    if (!StringsKt.startsWith((String)it, (String)args[0], (boolean)true)) continue;
                    destination$iv$iv.add(element$iv$iv);
                }
                return (List)destination$iv$iv;
            }
        }
        return CollectionsKt.emptyList();
    }

    public EnchantCommand() {
        super("enchant", new String[0]);
    }
}

