/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagDouble
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C10PacketCreativeInventoryAction
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/HoloStandCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "KyinoClient"})
public final class HoloStandCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkParameterIsNotNull(args2, "args");
        if (args2.length > 4) {
            PlayerControllerMP playerControllerMP = HoloStandCommand.access$getMc$p$s1046033730().field_71442_b;
            Intrinsics.checkExpressionValueIsNotNull(playerControllerMP, "mc.playerController");
            if (playerControllerMP.func_78762_g()) {
                this.chat("\u00a7c\u00a7lError: \u00a73You need to be in creative mode.");
                return;
            }
            try {
                String string = args2[1];
                boolean bl = false;
                double x = Double.parseDouble(string);
                String string2 = args2[2];
                boolean bl2 = false;
                double y = Double.parseDouble(string2);
                String string3 = args2[3];
                boolean bl3 = false;
                double z = Double.parseDouble(string3);
                String message = StringUtils.toCompleteString(args2, 4);
                ItemStack itemStack = new ItemStack((Item)Items.field_179565_cj);
                NBTTagCompound base = new NBTTagCompound();
                NBTTagCompound entityTag = new NBTTagCompound();
                entityTag.func_74768_a("Invisible", 1);
                entityTag.func_74778_a("CustomName", message);
                entityTag.func_74768_a("CustomNameVisible", 1);
                entityTag.func_74768_a("NoGravity", 1);
                NBTTagList position = new NBTTagList();
                position.func_74742_a((NBTBase)new NBTTagDouble(x));
                position.func_74742_a((NBTBase)new NBTTagDouble(y));
                position.func_74742_a((NBTBase)new NBTTagDouble(z));
                entityTag.func_74782_a("Pos", (NBTBase)position);
                base.func_74782_a("EntityTag", (NBTBase)entityTag);
                itemStack.func_77982_d(base);
                itemStack.func_151001_c("\u00a7c\u00a7lHolo\u00a7eStand");
                Minecraft minecraft = HoloStandCommand.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C10PacketCreativeInventoryAction(36, itemStack));
                this.chat("The HoloStand was successfully added to your inventory.");
            }
            catch (NumberFormatException exception) {
                this.chatSyntaxError();
            }
            return;
        }
        this.chatSyntax("holostand <x> <y> <z> <message...>");
    }

    public HoloStandCommand() {
        super("holostand", new String[0]);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

