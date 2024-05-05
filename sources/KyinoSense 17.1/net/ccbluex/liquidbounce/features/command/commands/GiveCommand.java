/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C10PacketCreativeInventoryAction
 *  net.minecraft.util.RegistryNamespaced
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Session
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\bJ!\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/GiveCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "KyinoClient"})
public final class GiveCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkParameterIsNotNull(args2, "args");
        EntityPlayerSP entityPlayerSP = GiveCommand.access$getMc$p$s1046033730().field_71439_g;
        if (entityPlayerSP == null) {
            return;
        }
        EntityPlayerSP thePlayer = entityPlayerSP;
        PlayerControllerMP playerControllerMP = GiveCommand.access$getMc$p$s1046033730().field_71442_b;
        Intrinsics.checkExpressionValueIsNotNull(playerControllerMP, "mc.playerController");
        if (playerControllerMP.func_78762_g()) {
            this.chat("\u00a7c\u00a7lError: \u00a73You need to be in creative mode.");
            return;
        }
        if (args2.length > 1) {
            int i;
            ItemStack itemStack = ItemUtils.createItem(StringUtils.toCompleteString(args2, 1));
            if (itemStack == null) {
                this.chatSyntaxError();
                return;
            }
            int emptySlot = -1;
            int n = 36;
            int n2 = 44;
            while (n <= n2) {
                Slot slot = thePlayer.field_71069_bz.func_75139_a(i);
                Intrinsics.checkExpressionValueIsNotNull(slot, "thePlayer.inventoryContainer.getSlot(i)");
                if (slot.func_75211_c() == null) {
                    emptySlot = i;
                    break;
                }
                ++i;
            }
            if (emptySlot == -1) {
                n2 = 44;
                for (i = 9; i <= n2; ++i) {
                    Slot slot = thePlayer.field_71069_bz.func_75139_a(i);
                    Intrinsics.checkExpressionValueIsNotNull(slot, "thePlayer.inventoryContainer.getSlot(i)");
                    if (slot.func_75211_c() != null) continue;
                    emptySlot = i;
                    break;
                }
            }
            if (emptySlot != -1) {
                Minecraft minecraft = GiveCommand.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C10PacketCreativeInventoryAction(emptySlot, itemStack));
                StringBuilder stringBuilder = new StringBuilder().append("\u00a77Given [\u00a78").append(itemStack.func_82833_r()).append("\u00a77] * \u00a78").append(itemStack.field_77994_a).append("\u00a77 to \u00a78");
                Session session = GiveCommand.access$getMc$p$s1046033730().field_71449_j;
                Intrinsics.checkExpressionValueIsNotNull(session, "mc.session");
                this.chat(stringBuilder.append(session.func_111285_a()).append("\u00a77.").toString());
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
    @NotNull
    public List<String> tabComplete(@NotNull String[] args2) {
        Intrinsics.checkParameterIsNotNull(args2, "args");
        String[] stringArray = args2;
        boolean bl = false;
        if (stringArray.length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args2.length) {
            case 1: {
                void $this$filterTo$iv$iv;
                String it;
                Iterable $this$mapTo$iv$iv;
                RegistryNamespaced registryNamespaced = Item.field_150901_e;
                Intrinsics.checkExpressionValueIsNotNull(registryNamespaced, "Item.itemRegistry");
                Set set = registryNamespaced.func_148742_b();
                Intrinsics.checkExpressionValueIsNotNull(set, "Item.itemRegistry.keys");
                Iterable $this$map$iv = set;
                boolean $i$f$map = false;
                Iterable iterable = $this$map$iv;
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    String string;
                    String string2;
                    ResourceLocation resourceLocation = (ResourceLocation)item$iv$iv;
                    Collection collection = destination$iv$iv;
                    boolean bl2 = false;
                    void v2 = it;
                    Intrinsics.checkExpressionValueIsNotNull(v2, "it");
                    Intrinsics.checkExpressionValueIsNotNull(v2.func_110623_a(), "it.resourcePath");
                    boolean bl3 = false;
                    String string3 = string2;
                    if (string3 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    Intrinsics.checkExpressionValueIsNotNull(string3.toLowerCase(), "(this as java.lang.String).toLowerCase()");
                    collection.add(string);
                }
                Iterable $this$filter$iv = (List)destination$iv$iv;
                boolean $i$f$filter = false;
                $this$mapTo$iv$iv = $this$filter$iv;
                destination$iv$iv = new ArrayList();
                boolean $i$f$filterTo = false;
                for (Object element$iv$iv : $this$filterTo$iv$iv) {
                    it = (String)element$iv$iv;
                    boolean bl4 = false;
                    if (!StringsKt.startsWith(it, args2[0], true)) continue;
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

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

