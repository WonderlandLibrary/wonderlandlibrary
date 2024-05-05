/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.util.BlockPos
 */
package me.report.liquidware.modules.movement;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

@ModuleInfo(name="PeralFly", description="Allows you to jump higher.", category=ModuleCategory.MOVEMENT)
public class PeralFly
extends Module {
    boolean damaged = false;
    int i = 0;
    final Fly Fly = (Fly)LiquidBounce.moduleManager.getModule(Fly.class);

    @Override
    public void onDisable() {
        this.Fly.setState(false);
    }

    public int getEnderPearlSlot() {
        this.i = 36;
        while (this.i < 45) {
            ItemStack stack = PeralFly.mc.field_71439_g.field_71069_bz.func_75139_a(this.i).func_75211_c();
            if (stack != null && stack.func_77973_b() instanceof ItemEnderPearl) {
                return this.i - 36;
            }
            ++this.i;
        }
        return -1;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        int enderPearlSlot = this.getEnderPearlSlot();
        if (!this.damaged) {
            if (enderPearlSlot == -1) {
                this.setState(false);
                return;
            }
            if (PeralFly.mc.field_71439_g.field_71071_by.field_70461_c != enderPearlSlot) {
                PeralFly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C09PacketHeldItemChange(enderPearlSlot));
            }
            PeralFly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(PeralFly.mc.field_71439_g.field_70177_z, 90.0f, PeralFly.mc.field_71439_g.field_70122_E));
            PeralFly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, PeralFly.mc.field_71439_g.field_71069_bz.func_75139_a(enderPearlSlot + 36).func_75211_c(), 0.0f, 0.0f, 0.0f));
            this.damaged = true;
        }
        if (this.damaged && PeralFly.mc.field_71439_g.field_70737_aN > 0) {
            this.Fly.setState(true);
        }
    }

    @Override
    public void onEnable() {
        this.damaged = false;
    }
}

