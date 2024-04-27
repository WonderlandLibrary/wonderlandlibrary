/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0DPacketCloseWindow
 *  net.minecraft.network.play.client.C16PacketClientStatus
 *  net.minecraft.network.play.client.C16PacketClientStatus$EnumState
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.InventoryUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="AutoSoup", spacedName="Auto Soup", description="Makes you automatically eat soup whenever your health is low.", category=Category.COMBAT, cnName="\u81ea\u52a8\u559d\u6c64")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lnet/dev/important/modules/module/modules/combat/AutoSoup;", "Lnet/dev/important/modules/module/Module;", "()V", "bowlValue", "Lnet/dev/important/value/ListValue;", "delayValue", "Lnet/dev/important/value/IntegerValue;", "healthValue", "Lnet/dev/important/value/FloatValue;", "openInventoryValue", "Lnet/dev/important/value/BoolValue;", "simulateInventoryValue", "tag", "", "getTag", "()Ljava/lang/String;", "timer", "Lnet/dev/important/utils/timer/MSTimer;", "onUpdate", "", "event", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class AutoSoup
extends Module {
    @NotNull
    private final FloatValue healthValue = new FloatValue("Health", 15.0f, 0.0f, 20.0f);
    @NotNull
    private final IntegerValue delayValue = new IntegerValue("Delay", 150, 0, 500, "ms");
    @NotNull
    private final BoolValue openInventoryValue = new BoolValue("OpenInv", false);
    @NotNull
    private final BoolValue simulateInventoryValue = new BoolValue("SimulateInventory", true);
    @NotNull
    private final ListValue bowlValue;
    @NotNull
    private final MSTimer timer;

    public AutoSoup() {
        String[] stringArray = new String[]{"Drop", "Move", "Stay"};
        this.bowlValue = new ListValue("Bowl", stringArray, "Drop");
        this.timer = new MSTimer();
    }

    @Override
    @NotNull
    public String getTag() {
        return String.valueOf(((Number)this.healthValue.get()).floatValue());
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        int soupInInventory;
        boolean openInventory;
        if (!this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            return;
        }
        int soupInHotbar = InventoryUtils.findItem(36, 45, Items.field_151009_A);
        if (MinecraftInstance.mc.field_71439_g.func_110143_aJ() <= ((Number)this.healthValue.get()).floatValue() && soupInHotbar != -1) {
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(soupInHotbar - 36));
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(MinecraftInstance.mc.field_71439_g.field_71069_bz.func_75139_a(soupInHotbar).func_75211_c()));
            if (StringsKt.equals((String)this.bowlValue.get(), "Drop", true)) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
            }
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c));
            this.timer.reset();
            return;
        }
        int bowlInHotbar = InventoryUtils.findItem(36, 45, Items.field_151054_z);
        if (StringsKt.equals((String)this.bowlValue.get(), "Move", true) && bowlInHotbar != -1) {
            if (((Boolean)this.openInventoryValue.get()).booleanValue() && !(MinecraftInstance.mc.field_71462_r instanceof GuiInventory)) {
                return;
            }
            boolean bowlMovable = false;
            int n = 9;
            while (n < 37) {
                int i;
                ItemStack itemStack;
                if ((itemStack = MinecraftInstance.mc.field_71439_g.field_71069_bz.func_75139_a(i = n++).func_75211_c()) == null) {
                    bowlMovable = true;
                    break;
                }
                if (!Intrinsics.areEqual(itemStack.func_77973_b(), Items.field_151054_z) || itemStack.field_77994_a >= 64) continue;
                bowlMovable = true;
                break;
            }
            if (bowlMovable) {
                boolean bl = openInventory = !(MinecraftInstance.mc.field_71462_r instanceof GuiInventory) && (Boolean)this.simulateInventoryValue.get() != false;
                if (openInventory) {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                }
                MinecraftInstance.mc.field_71442_b.func_78753_a(0, bowlInHotbar, 0, 1, (EntityPlayer)MinecraftInstance.mc.field_71439_g);
            }
        }
        if ((soupInInventory = InventoryUtils.findItem(9, 36, Items.field_151009_A)) != -1 && InventoryUtils.hasSpaceHotbar()) {
            if (((Boolean)this.openInventoryValue.get()).booleanValue() && !(MinecraftInstance.mc.field_71462_r instanceof GuiInventory)) {
                return;
            }
            boolean bl = openInventory = !(MinecraftInstance.mc.field_71462_r instanceof GuiInventory) && (Boolean)this.simulateInventoryValue.get() != false;
            if (openInventory) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            }
            MinecraftInstance.mc.field_71442_b.func_78753_a(0, soupInInventory, 0, 1, (EntityPlayer)MinecraftInstance.mc.field_71439_g);
            if (openInventory) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
            }
            this.timer.reset();
        }
    }
}

