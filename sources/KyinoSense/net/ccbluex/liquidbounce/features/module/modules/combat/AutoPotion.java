/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0DPacketCloseWindow
 *  net.minecraft.network.play.client.C16PacketClientStatus
 *  net.minecraft.network.play.client.C16PacketClientStatus$EnumState
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.InventoryUtil;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoPotion", description="Automatically throws healing potions.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\nH\u0002J\u0010\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\nH\u0002J\u0010\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\nH\u0002J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/AutoPotion;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "healthValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "openInventoryValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "pot", "", "regen", "selectValue", "simulateInventory", "throwTickValue", "throwTime", "throwing", "", "utility", "findPotion", "startSlot", "endSlot", "findSinglePotion", "slot", "isUsefulPotion", "id", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class AutoPotion
extends Module {
    private final FloatValue healthValue = new FloatValue("Health", 15.0f, 1.0f, 20.0f);
    private final IntegerValue delayValue = new IntegerValue("Delay", 700, 500, 1000);
    private final IntegerValue throwTickValue = new IntegerValue("ThrowTick", 10, 1, 10);
    private final IntegerValue selectValue = new IntegerValue("SelectSlot", -1, -1, 9);
    private final BoolValue openInventoryValue = new BoolValue("OpenInv", false);
    private final BoolValue simulateInventory = new BoolValue("SimulateInventory", true);
    private final BoolValue regen = new BoolValue("Regen", true);
    private final BoolValue utility = new BoolValue("Utility", true);
    private boolean throwing;
    private int throwTime;
    private int pot = -1;

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        int invPotion;
        int potion;
        boolean enableSelect;
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (!AutoPotion.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            return;
        }
        if (this.throwing) {
            int n = this.throwTime;
            this.throwTime = n + 1;
            RotationUtils.setTargetRotation(new Rotation(AutoPotion.access$getMc$p$s1046033730().field_71439_g.field_70177_z, 90.0f));
            if (this.throwTime == ((Number)this.throwTickValue.get()).intValue()) {
                Minecraft minecraft = AutoPotion.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(this.pot - 36));
                Minecraft minecraft2 = AutoPotion.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                NetHandlerPlayClient netHandlerPlayClient = minecraft2.func_147114_u();
                EntityPlayerSP entityPlayerSP = AutoPotion.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                netHandlerPlayClient.func_147297_a((Packet)new C08PacketPlayerBlockPlacement(entityPlayerSP.func_70694_bm()));
                Minecraft minecraft3 = AutoPotion.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
                minecraft3.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(AutoPotion.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c));
                this.pot = -1;
            }
            if (this.throwTime >= ((Number)this.throwTickValue.get()).intValue() * 2) {
                this.throwTime = 0;
                this.throwing = false;
            }
            return;
        }
        if (!InventoryUtil.INSTANCE.getINV_TIMER().hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            return;
        }
        boolean bl = enableSelect = ((Number)this.selectValue.get()).intValue() != -1;
        int n = enableSelect ? (this.findSinglePotion(36 + ((Number)this.selectValue.get()).intValue()) ? 36 + ((Number)this.selectValue.get()).intValue() : -1) : (potion = this.findPotion(36, 45));
        if (potion != -1) {
            RotationUtils.setTargetRotation(new Rotation(AutoPotion.access$getMc$p$s1046033730().field_71439_g.field_70177_z, 90.0f));
            this.pot = potion;
            this.throwing = true;
            InventoryUtil.INSTANCE.getINV_TIMER().reset();
            return;
        }
        if (((Boolean)this.openInventoryValue.get()).booleanValue() && !enableSelect && (invPotion = this.findPotion(9, 36)) != -1) {
            boolean openInventory;
            boolean bl2 = openInventory = !(AutoPotion.access$getMc$p$s1046033730().field_71462_r instanceof GuiInventory) && (Boolean)this.simulateInventory.get() != false;
            if (InventoryUtils.hasSpaceHotbar()) {
                if (openInventory) {
                    Minecraft minecraft = AutoPotion.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                }
                AutoPotion.access$getMc$p$s1046033730().field_71442_b.func_78753_a(0, invPotion, 0, 1, (EntityPlayer)AutoPotion.access$getMc$p$s1046033730().field_71439_g);
                if (openInventory) {
                    Minecraft minecraft = AutoPotion.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
                }
                return;
            }
            int n2 = 36;
            int n3 = 45;
            while (n2 < n3) {
                void i;
                Slot slot = AutoPotion.access$getMc$p$s1046033730().field_71439_g.field_71069_bz.func_75139_a((int)i);
                Intrinsics.checkExpressionValueIsNotNull(slot, "mc.thePlayer.inventoryContainer.getSlot(i)");
                ItemStack stack = slot.func_75211_c();
                if (stack != null && stack.func_77973_b() instanceof ItemPotion && ItemPotion.func_77831_g((int)stack.func_77952_i())) {
                    if (openInventory) {
                        Minecraft minecraft = AutoPotion.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    }
                    AutoPotion.access$getMc$p$s1046033730().field_71442_b.func_78753_a(0, invPotion, 0, 0, (EntityPlayer)AutoPotion.access$getMc$p$s1046033730().field_71439_g);
                    AutoPotion.access$getMc$p$s1046033730().field_71442_b.func_78753_a(0, (int)i, 0, 0, (EntityPlayer)AutoPotion.access$getMc$p$s1046033730().field_71439_g);
                    if (!openInventory) break;
                    Minecraft minecraft = AutoPotion.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
                    break;
                }
                ++i;
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    private final int findPotion(int startSlot, int endSlot) {
        int n = startSlot;
        int n2 = endSlot;
        while (n < n2) {
            void i;
            if (this.findSinglePotion((int)i)) {
                return (int)i;
            }
            ++i;
        }
        return -1;
    }

    private final boolean findSinglePotion(int slot) {
        block7: {
            ItemPotion itemPotion;
            ItemStack stack;
            block6: {
                PotionEffect potionEffect;
                Slot slot2 = AutoPotion.access$getMc$p$s1046033730().field_71439_g.field_71069_bz.func_75139_a(slot);
                Intrinsics.checkExpressionValueIsNotNull(slot2, "mc.thePlayer.inventoryContainer.getSlot(slot)");
                stack = slot2.func_75211_c();
                if (stack == null || !(stack.func_77973_b() instanceof ItemPotion) || !ItemPotion.func_77831_g((int)stack.func_77952_i())) {
                    return false;
                }
                Item item = stack.func_77973_b();
                if (item == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemPotion");
                }
                itemPotion = (ItemPotion)item;
                EntityPlayerSP entityPlayerSP = AutoPotion.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                if (!(entityPlayerSP.func_110143_aJ() < ((Number)this.healthValue.get()).floatValue()) || !((Boolean)this.regen.get()).booleanValue()) break block6;
                Iterator iterator2 = itemPotion.func_77832_l(stack).iterator();
                while (iterator2.hasNext()) {
                    PotionEffect potionEffect2 = potionEffect = (PotionEffect)iterator2.next();
                    Intrinsics.checkExpressionValueIsNotNull(potionEffect2, "potionEffect");
                    if (potionEffect2.func_76456_a() != Potion.field_76432_h.field_76415_H) continue;
                    return true;
                }
                if (AutoPotion.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76428_l)) break block7;
                iterator2 = itemPotion.func_77832_l(stack).iterator();
                while (iterator2.hasNext()) {
                    PotionEffect potionEffect3 = potionEffect = (PotionEffect)iterator2.next();
                    Intrinsics.checkExpressionValueIsNotNull(potionEffect3, "potionEffect");
                    if (potionEffect3.func_76456_a() != Potion.field_76428_l.field_76415_H) continue;
                    return true;
                }
                break block7;
            }
            if (((Boolean)this.utility.get()).booleanValue()) {
                Iterator iterator3 = itemPotion.func_77832_l(stack).iterator();
                while (iterator3.hasNext()) {
                    PotionEffect potionEffect;
                    PotionEffect potionEffect4 = potionEffect = (PotionEffect)iterator3.next();
                    Intrinsics.checkExpressionValueIsNotNull(potionEffect4, "potionEffect");
                    if (!this.isUsefulPotion(potionEffect4.func_76456_a())) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private final boolean isUsefulPotion(int id) {
        if (id == Potion.field_76428_l.field_76415_H || id == Potion.field_76432_h.field_76415_H || id == Potion.field_76436_u.field_76415_H || id == Potion.field_76440_q.field_76415_H || id == Potion.field_76433_i.field_76415_H || id == Potion.field_82731_v.field_76415_H || id == Potion.field_76419_f.field_76415_H || id == Potion.field_76421_d.field_76415_H) {
            return false;
        }
        return !AutoPotion.access$getMc$p$s1046033730().field_71439_g.func_82165_m(id);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

