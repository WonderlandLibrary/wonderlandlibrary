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
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;

public final class GiveCommand
extends Command {
    @Override
    public void execute(String[] args) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (MinecraftInstance.mc.getPlayerController().isNotCreative()) {
            this.chat("\u00a7c\u00a7lError: \u00a73You need to be in creative mode.");
            return;
        }
        if (args.length > 1) {
            int i;
            IItemStack itemStack = ItemUtils.createItem(StringUtils.toCompleteString(args, 1));
            if (itemStack == null) {
                this.chatSyntaxError();
                return;
            }
            int emptySlot = -1;
            int n = 36;
            int n2 = 44;
            while (n <= n2) {
                if (thePlayer.getInventoryContainer().getSlot(i).getStack() == null) {
                    emptySlot = i;
                    break;
                }
                ++i;
            }
            if (emptySlot == -1) {
                n2 = 44;
                for (i = 9; i <= n2; ++i) {
                    if (thePlayer.getInventoryContainer().getSlot(i).getStack() != null) continue;
                    emptySlot = i;
                    break;
                }
            }
            if (emptySlot != -1) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCreativeInventoryAction(emptySlot, itemStack));
                this.chat("\u00a77Given [\u00a78" + itemStack.getDisplayName() + "\u00a77] * \u00a78" + itemStack.getStackSize() + "\u00a77 to \u00a78" + MinecraftInstance.mc.getSession().getUsername() + "\u00a77.");
            } else {
                this.chat("Your inventory is full.");
            }
            return;
        }
        this.chatSyntax("give <item> [amount] [data] [datatag]");
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
                Iterable $this$map$iv = MinecraftInstance.functions.getItemRegistryKeys();
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

    public GiveCommand() {
        super("give", "item", "i", "get");
    }
}

