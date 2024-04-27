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
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\b\n\n\u0000\n\n\n\b\n \n\b\u000020B¢J02\f\b00H¢\bJ!\t\b00\n2\f\b00H¢¨\f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/EnchantCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "Pride"})
public final class EnchantCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length > 2) {
            int n;
            int n2;
            IItemStack item;
            if (MinecraftInstance.mc.getPlayerController().isNotCreative()) {
                this.chat("§c§lError: §3You need to be in creative mode.");
                return;
            }
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            IItemStack iItemStack = item = iEntityPlayerSP != null ? iEntityPlayerSP.getHeldItem() : null;
            if ((iItemStack != null ? iItemStack.getItem() : null) == null) {
                this.chat("§c§lError: §3You need to hold an item.");
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
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketCreativeInventoryAction(36 + iEntityPlayerSP2.getInventory().getCurrentItem(), item));
            this.chat(enchantment.getTranslatedName(level) + " added to " + item.getDisplayName() + '.');
            return;
        }
        this.chatSyntax("enchant <type> [level]");
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
                Iterable $this$map$iv = MinecraftInstance.functions.getEnchantments();
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

    public EnchantCommand() {
        super("enchant", new String[0]);
    }
}
