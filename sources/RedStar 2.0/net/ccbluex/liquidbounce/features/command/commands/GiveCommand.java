package net.ccbluex.liquidbounce.features.command.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\b\n\n\u0000\n\n\n\b\n \n\b\u000020B¢J02\f\b00H¢\bJ!\t\b00\n2\f\b00H¢¨\f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/GiveCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "Pride"})
public final class GiveCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (MinecraftInstance.mc.getPlayerController().isNotCreative()) {
            this.chat("§c§lError: §3You need to be in creative mode.");
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
                this.chat("§7Given [§8" + itemStack.getDisplayName() + "§7] * §8" + itemStack.getStackSize() + "§7 to §8" + MinecraftInstance.mc.getSession().getUsername() + "§7.");
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
    public List<String> tabComplete(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
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
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    String string;
                    IResourceLocation iResourceLocation = (IResourceLocation)item$iv$iv;
                    Collection collection = destination$iv$iv;
                    boolean bl2 = false;
                    String string2 = it.getResourcePath();
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
                    if (!StringsKt.startsWith(it, args[0], true)) continue;
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
