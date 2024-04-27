/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 */
package net.dev.important.modules.module.modules.movement;

import java.awt.Color;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MoveEvent;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.Render2DEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.event.WorldEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.gui.font.Fonts;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.PacketUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Info(name="BowJump", spacedName="Bow Jump", description="Allows you to jump further with auto bow shoot.", category=Category.MOVEMENT, cnName="\u5f13\u7bad\u8fdc\u773a")
public class BowJump
extends Module {
    private final FloatValue boostValue = new FloatValue("Boost", 4.25f, 0.0f, 10.0f, "x");
    private final FloatValue heightValue = new FloatValue("Height", 0.42f, 0.0f, 10.0f, "m");
    private final FloatValue timerValue = new FloatValue("Timer", 1.0f, 0.1f, 10.0f, "x");
    private final IntegerValue delayBeforeLaunch = new IntegerValue("DelayBeforeArrowLaunch", 1, 1, 20, " tick");
    private final BoolValue autoDisable = new BoolValue("AutoDisable", true);
    private final BoolValue renderValue = new BoolValue("RenderStatus", true);
    private int bowState = 0;
    private long lastPlayerTick = 0L;
    private int lastSlot = -1;

    @Override
    public void onEnable() {
        if (BowJump.mc.field_71439_g == null) {
            return;
        }
        this.bowState = 0;
        this.lastPlayerTick = -1L;
        this.lastSlot = BowJump.mc.field_71439_g.field_71071_by.field_70461_c;
        MovementUtils.strafe(0.0f);
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (BowJump.mc.field_71439_g.field_70122_E && this.bowState < 3) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange c09 = (C09PacketHeldItemChange)event.getPacket();
            this.lastSlot = c09.func_149614_c();
            event.cancelEvent();
        }
        if (event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer c03 = (C03PacketPlayer)event.getPacket();
            if (this.bowState < 3) {
                c03.func_149469_a(false);
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        BowJump.mc.field_71428_T.field_74278_d = 1.0f;
        boolean forceDisable = false;
        switch (this.bowState) {
            case 0: {
                int slot = this.getBowSlot();
                if (slot < 0 || !BowJump.mc.field_71439_g.field_71071_by.func_146028_b(Items.field_151032_g)) {
                    Client.hud.addNotification(new Notification("No arrows or bow found in your inventory!", Notification.Type.ERROR));
                    forceDisable = true;
                    this.bowState = 5;
                    break;
                }
                if (this.lastPlayerTick != -1L) break;
                ItemStack stack = BowJump.mc.field_71439_g.field_71069_bz.func_75139_a(slot + 36).func_75211_c();
                if (this.lastSlot != slot) {
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C09PacketHeldItemChange(slot));
                }
                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, BowJump.mc.field_71439_g.field_71069_bz.func_75139_a(slot + 36).func_75211_c(), 0.0f, 0.0f, 0.0f));
                this.lastPlayerTick = BowJump.mc.field_71439_g.field_70173_aa;
                this.bowState = 1;
                break;
            }
            case 1: {
                int reSlot = this.getBowSlot();
                if ((long)BowJump.mc.field_71439_g.field_70173_aa - this.lastPlayerTick <= (long)((Integer)this.delayBeforeLaunch.get()).intValue()) break;
                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C05PacketPlayerLook(BowJump.mc.field_71439_g.field_70177_z, -90.0f, BowJump.mc.field_71439_g.field_70122_E));
                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
                if (this.lastSlot != reSlot) {
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C09PacketHeldItemChange(this.lastSlot));
                }
                this.bowState = 2;
                break;
            }
            case 2: {
                if (BowJump.mc.field_71439_g.field_70737_aN <= 0) break;
                this.bowState = 3;
                break;
            }
            case 3: {
                MovementUtils.strafe(((Float)this.boostValue.get()).floatValue());
                BowJump.mc.field_71439_g.field_70181_x = ((Float)this.heightValue.get()).floatValue();
                this.bowState = 4;
                this.lastPlayerTick = BowJump.mc.field_71439_g.field_70173_aa;
                break;
            }
            case 4: {
                BowJump.mc.field_71428_T.field_74278_d = ((Float)this.timerValue.get()).floatValue();
                if (!BowJump.mc.field_71439_g.field_70122_E || (long)BowJump.mc.field_71439_g.field_70173_aa - this.lastPlayerTick < 1L) break;
                this.bowState = 5;
            }
        }
        if (this.bowState < 3) {
            BowJump.mc.field_71439_g.field_71158_b.field_78900_b = 0.0f;
            BowJump.mc.field_71439_g.field_71158_b.field_78902_a = 0.0f;
        }
        if (this.bowState == 5 && (((Boolean)this.autoDisable.get()).booleanValue() || forceDisable)) {
            this.setState(false);
        }
    }

    @EventTarget
    public void onWorld(WorldEvent event) {
        this.setState(false);
    }

    @Override
    public void onDisable() {
        BowJump.mc.field_71428_T.field_74278_d = 1.0f;
        BowJump.mc.field_71439_g.field_71102_ce = 0.02f;
    }

    private int getBowSlot() {
        for (int i = 36; i < 45; ++i) {
            ItemStack stack = BowJump.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (stack == null || !(stack.func_77973_b() instanceof ItemBow)) continue;
            return i - 36;
        }
        return -1;
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (!((Boolean)this.renderValue.get()).booleanValue()) {
            return;
        }
        ScaledResolution scaledRes = new ScaledResolution(mc);
        float width = (float)this.bowState / 5.0f * 60.0f;
        Fonts.font40.drawCenteredString(this.getBowStatus(), (float)scaledRes.func_78326_a() / 2.0f, (float)scaledRes.func_78328_b() / 2.0f + 14.0f, -1, true);
        RenderUtils.drawRect((float)scaledRes.func_78326_a() / 2.0f - 31.0f, (float)scaledRes.func_78328_b() / 2.0f + 25.0f, (float)scaledRes.func_78326_a() / 2.0f + 31.0f, (float)scaledRes.func_78328_b() / 2.0f + 29.0f, -1610612736);
        RenderUtils.drawRect((float)scaledRes.func_78326_a() / 2.0f - 30.0f, (float)scaledRes.func_78328_b() / 2.0f + 26.0f, (float)scaledRes.func_78326_a() / 2.0f - 30.0f + width, (float)scaledRes.func_78328_b() / 2.0f + 28.0f, this.getStatusColor());
    }

    public String getBowStatus() {
        switch (this.bowState) {
            case 0: {
                return "Idle...";
            }
            case 1: {
                return "Preparing...";
            }
            case 2: {
                return "Waiting for damage...";
            }
            case 3: 
            case 4: {
                return "Boost!";
            }
        }
        return "Task completed.";
    }

    public Color getStatusColor() {
        switch (this.bowState) {
            case 0: {
                return new Color(21, 21, 21);
            }
            case 1: {
                return new Color(48, 48, 48);
            }
            case 2: {
                return Color.yellow;
            }
            case 3: 
            case 4: {
                return Color.green;
            }
        }
        return new Color(0, 111, 255);
    }
}

