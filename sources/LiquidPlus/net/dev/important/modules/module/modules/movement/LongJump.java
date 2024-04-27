/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 */
package net.dev.important.modules.module.modules.movement;

import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.JumpEvent;
import net.dev.important.event.MoveEvent;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.PacketUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Info(name="LongJump", spacedName="Long Jump", description="Allows you to jump further.", category=Category.MOVEMENT, cnName="\u8fdc\u8df3")
public class LongJump
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"NCP", "Damage", "AACv1", "AACv2", "AACv3", "AACv4", "Mineplex", "Mineplex2", "Mineplex3", "RedeskyMaki", "Redesky", "InfiniteRedesky", "VerusDmg", "Pearl"}, "NCP");
    private final BoolValue autoJumpValue = new BoolValue("AutoJump", false);
    private final FloatValue ncpBoostValue = new FloatValue("NCPBoost", 4.25f, 1.0f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("ncp"));
    private final BoolValue redeskyTimerBoostValue = new BoolValue("Redesky-TimerBoost", false, () -> ((String)this.modeValue.get()).equalsIgnoreCase("redesky"));
    private final BoolValue redeskyGlideAfterTicksValue = new BoolValue("Redesky-GlideAfterTicks", false, () -> ((String)this.modeValue.get()).equalsIgnoreCase("redesky"));
    private final IntegerValue redeskyTickValue = new IntegerValue("Redesky-Ticks", 21, 1, 25, () -> ((String)this.modeValue.get()).equalsIgnoreCase("redesky"));
    private final FloatValue redeskyYMultiplier = new FloatValue("Redesky-YMultiplier", 0.77f, 0.1f, 1.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("redesky"));
    private final FloatValue redeskyXZMultiplier = new FloatValue("Redesky-XZMultiplier", 0.9f, 0.1f, 1.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("redesky"));
    private final FloatValue redeskyTimerBoostStartValue = new FloatValue("Redesky-TimerBoostStart", 1.85f, 0.05f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("redesky") && (Boolean)this.redeskyTimerBoostValue.get() != false);
    private final FloatValue redeskyTimerBoostEndValue = new FloatValue("Redesky-TimerBoostEnd", 1.0f, 0.05f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("redesky") && (Boolean)this.redeskyTimerBoostValue.get() != false);
    private final IntegerValue redeskyTimerBoostSlowDownSpeedValue = new IntegerValue("Redesky-TimerBoost-SlowDownSpeed", 2, 1, 10, () -> ((String)this.modeValue.get()).equalsIgnoreCase("redesky") && (Boolean)this.redeskyTimerBoostValue.get() != false);
    private final ListValue verusDmgModeValue = new ListValue("VerusDmg-DamageMode", new String[]{"Instant", "InstantC06", "Jump"}, "None", () -> ((String)this.modeValue.get()).equalsIgnoreCase("verusdmg"));
    private final FloatValue verusBoostValue = new FloatValue("VerusDmg-Boost", 4.25f, 0.0f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("verusdmg"));
    private final FloatValue verusHeightValue = new FloatValue("VerusDmg-Height", 0.42f, 0.0f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("verusdmg"));
    private final FloatValue verusTimerValue = new FloatValue("VerusDmg-Timer", 1.0f, 0.05f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("verusdmg"));
    private final FloatValue pearlBoostValue = new FloatValue("Pearl-Boost", 4.25f, 0.0f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("pearl"));
    private final FloatValue pearlHeightValue = new FloatValue("Pearl-Height", 0.42f, 0.0f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("pearl"));
    private final FloatValue pearlTimerValue = new FloatValue("Pearl-Timer", 1.0f, 0.05f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("pearl"));
    private final FloatValue damageBoostValue = new FloatValue("Damage-Boost", 4.25f, 0.0f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("damage"));
    private final FloatValue damageHeightValue = new FloatValue("Damage-Height", 0.42f, 0.0f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("damage"));
    private final FloatValue damageTimerValue = new FloatValue("Damage-Timer", 1.0f, 0.05f, 10.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("damage"));
    private final BoolValue damageNoMoveValue = new BoolValue("Damage-NoMove", false, () -> ((String)this.modeValue.get()).equalsIgnoreCase("damage"));
    private final BoolValue damageARValue = new BoolValue("Damage-AutoReset", false, () -> ((String)this.modeValue.get()).equalsIgnoreCase("damage"));
    private boolean jumped;
    private boolean canBoost;
    private boolean teleported;
    private boolean canMineplexBoost;
    private int ticks = 0;
    private float currentTimer = 1.0f;
    private boolean verusDmged;
    private boolean hpxDamage;
    private boolean damaged = false;
    private int verusJumpTimes = 0;
    private int pearlState = 0;
    private MSTimer dmgTimer = new MSTimer();

    @Override
    public void onEnable() {
        if (LongJump.mc.field_71439_g == null) {
            return;
        }
        if (((String)this.modeValue.get()).equalsIgnoreCase("redesky") && ((Boolean)this.redeskyTimerBoostValue.get()).booleanValue()) {
            this.currentTimer = ((Float)this.redeskyTimerBoostStartValue.get()).floatValue();
        }
        this.ticks = 0;
        this.verusDmged = false;
        this.hpxDamage = false;
        this.damaged = false;
        this.pearlState = 0;
        this.verusJumpTimes = 0;
        this.dmgTimer.reset();
        double x = LongJump.mc.field_71439_g.field_70165_t;
        double y = LongJump.mc.field_71439_g.field_70163_u;
        double z = LongJump.mc.field_71439_g.field_70161_v;
        if (((String)this.modeValue.get()).equalsIgnoreCase("verusdmg")) {
            if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Instant")) {
                if (LongJump.mc.field_71439_g.field_70122_E && LongJump.mc.field_71441_e.func_72945_a((Entity)LongJump.mc.field_71439_g, LongJump.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, 4.0, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(LongJump.mc.field_71439_g.field_70165_t, y + 4.0, LongJump.mc.field_71439_g.field_70161_v, false));
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(LongJump.mc.field_71439_g.field_70165_t, y, LongJump.mc.field_71439_g.field_70161_v, false));
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(LongJump.mc.field_71439_g.field_70165_t, y, LongJump.mc.field_71439_g.field_70161_v, true));
                    LongJump.mc.field_71439_g.field_70179_y = 0.0;
                    LongJump.mc.field_71439_g.field_70159_w = 0.0;
                }
            } else if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("InstantC06")) {
                if (LongJump.mc.field_71439_g.field_70122_E && LongJump.mc.field_71441_e.func_72945_a((Entity)LongJump.mc.field_71439_g, LongJump.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, 4.0, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(LongJump.mc.field_71439_g.field_70165_t, y + 4.0, LongJump.mc.field_71439_g.field_70161_v, LongJump.mc.field_71439_g.field_70177_z, LongJump.mc.field_71439_g.field_70125_A, false));
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(LongJump.mc.field_71439_g.field_70165_t, y, LongJump.mc.field_71439_g.field_70161_v, LongJump.mc.field_71439_g.field_70177_z, LongJump.mc.field_71439_g.field_70125_A, false));
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C06PacketPlayerPosLook(LongJump.mc.field_71439_g.field_70165_t, y, LongJump.mc.field_71439_g.field_70161_v, LongJump.mc.field_71439_g.field_70177_z, LongJump.mc.field_71439_g.field_70125_A, true));
                    LongJump.mc.field_71439_g.field_70179_y = 0.0;
                    LongJump.mc.field_71439_g.field_70159_w = 0.0;
                }
            } else if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Jump") && LongJump.mc.field_71439_g.field_70122_E) {
                LongJump.mc.field_71439_g.func_70664_aZ();
                this.verusJumpTimes = 1;
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (((String)this.modeValue.get()).equalsIgnoreCase("verusdmg")) {
            if (LongJump.mc.field_71439_g.field_70737_aN > 0 && !this.verusDmged) {
                this.verusDmged = true;
                MovementUtils.strafe(((Float)this.verusBoostValue.get()).floatValue());
                LongJump.mc.field_71439_g.field_70181_x = ((Float)this.verusHeightValue.get()).floatValue();
            }
            if (((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Jump") && this.verusJumpTimes < 5) {
                if (LongJump.mc.field_71439_g.field_70122_E) {
                    LongJump.mc.field_71439_g.func_70664_aZ();
                    ++this.verusJumpTimes;
                }
                return;
            }
            if (this.verusDmged) {
                LongJump.mc.field_71428_T.field_74278_d = ((Float)this.verusTimerValue.get()).floatValue();
            } else {
                LongJump.mc.field_71439_g.field_71158_b.field_78900_b = 0.0f;
                LongJump.mc.field_71439_g.field_71158_b.field_78902_a = 0.0f;
                if (!((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Jump")) {
                    LongJump.mc.field_71439_g.field_70181_x = 0.0;
                }
            }
            return;
        }
        if (((String)this.modeValue.get()).equalsIgnoreCase("damage")) {
            if (LongJump.mc.field_71439_g.field_70737_aN > 0 && !this.damaged) {
                this.damaged = true;
                MovementUtils.strafe(((Float)this.damageBoostValue.get()).floatValue());
                LongJump.mc.field_71439_g.field_70181_x = ((Float)this.damageHeightValue.get()).floatValue();
            }
            if (this.damaged) {
                LongJump.mc.field_71428_T.field_74278_d = ((Float)this.damageTimerValue.get()).floatValue();
                if (((Boolean)this.damageARValue.get()).booleanValue() && LongJump.mc.field_71439_g.field_70737_aN <= 0) {
                    this.damaged = false;
                }
            }
            return;
        }
        if (((String)this.modeValue.get()).equalsIgnoreCase("pearl")) {
            int enderPearlSlot = this.getPearlSlot();
            if (this.pearlState == 0) {
                if (enderPearlSlot == -1) {
                    Client.hud.addNotification(new Notification("You don't have any ender pearl!", Notification.Type.ERROR));
                    this.pearlState = -1;
                    this.setState(false);
                    return;
                }
                if (LongJump.mc.field_71439_g.field_71071_by.field_70461_c != enderPearlSlot) {
                    LongJump.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C09PacketHeldItemChange(enderPearlSlot));
                }
                LongJump.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(LongJump.mc.field_71439_g.field_70177_z, 90.0f, LongJump.mc.field_71439_g.field_70122_E));
                LongJump.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, LongJump.mc.field_71439_g.field_71069_bz.func_75139_a(enderPearlSlot + 36).func_75211_c(), 0.0f, 0.0f, 0.0f));
                if (enderPearlSlot != LongJump.mc.field_71439_g.field_71071_by.field_70461_c) {
                    LongJump.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C09PacketHeldItemChange(LongJump.mc.field_71439_g.field_71071_by.field_70461_c));
                }
                this.pearlState = 1;
            }
            if (this.pearlState == 1 && LongJump.mc.field_71439_g.field_70737_aN > 0) {
                this.pearlState = 2;
                MovementUtils.strafe(((Float)this.pearlBoostValue.get()).floatValue());
                LongJump.mc.field_71439_g.field_70181_x = ((Float)this.pearlHeightValue.get()).floatValue();
            }
            if (this.pearlState == 2) {
                LongJump.mc.field_71428_T.field_74278_d = ((Float)this.pearlTimerValue.get()).floatValue();
            }
            return;
        }
        if (this.jumped) {
            String mode = (String)this.modeValue.get();
            if (LongJump.mc.field_71439_g.field_70122_E || LongJump.mc.field_71439_g.field_71075_bZ.field_75100_b) {
                this.jumped = false;
                this.canMineplexBoost = false;
                if (mode.equalsIgnoreCase("NCP")) {
                    LongJump.mc.field_71439_g.field_70159_w = 0.0;
                    LongJump.mc.field_71439_g.field_70179_y = 0.0;
                }
                return;
            }
            switch (mode.toLowerCase()) {
                case "ncp": {
                    MovementUtils.strafe(MovementUtils.getSpeed() * (this.canBoost ? ((Float)this.ncpBoostValue.get()).floatValue() : 1.0f));
                    this.canBoost = false;
                    break;
                }
                case "aacv1": {
                    LongJump.mc.field_71439_g.field_70181_x += 0.05999;
                    MovementUtils.strafe(MovementUtils.getSpeed() * 1.08f);
                    break;
                }
                case "aacv2": 
                case "mineplex3": {
                    LongJump.mc.field_71439_g.field_70747_aH = 0.09f;
                    LongJump.mc.field_71439_g.field_70181_x += 0.01321;
                    LongJump.mc.field_71439_g.field_70747_aH = 0.08f;
                    MovementUtils.strafe();
                    break;
                }
                case "aacv3": {
                    EntityPlayerSP player = LongJump.mc.field_71439_g;
                    if (!(player.field_70143_R > 0.5f) || this.teleported) break;
                    double value = 3.0;
                    EnumFacing horizontalFacing = player.func_174811_aO();
                    double x = 0.0;
                    double z = 0.0;
                    switch (horizontalFacing) {
                        case NORTH: {
                            z = -value;
                            break;
                        }
                        case EAST: {
                            x = value;
                            break;
                        }
                        case SOUTH: {
                            z = value;
                            break;
                        }
                        case WEST: {
                            x = -value;
                        }
                    }
                    player.func_70107_b(player.field_70165_t + x, player.field_70163_u, player.field_70161_v + z);
                    this.teleported = true;
                    break;
                }
                case "mineplex": {
                    LongJump.mc.field_71439_g.field_70181_x += 0.01321;
                    LongJump.mc.field_71439_g.field_70747_aH = 0.08f;
                    MovementUtils.strafe();
                    break;
                }
                case "mineplex2": {
                    if (!this.canMineplexBoost) break;
                    LongJump.mc.field_71439_g.field_70747_aH = 0.1f;
                    if (LongJump.mc.field_71439_g.field_70143_R > 1.5f) {
                        LongJump.mc.field_71439_g.field_70747_aH = 0.0f;
                        LongJump.mc.field_71439_g.field_70181_x = -10.0;
                    }
                    MovementUtils.strafe();
                    break;
                }
                case "aacv4": {
                    LongJump.mc.field_71439_g.field_70747_aH = 0.05837456f;
                    LongJump.mc.field_71428_T.field_74278_d = 0.5f;
                    break;
                }
                case "redeskymaki": {
                    LongJump.mc.field_71439_g.field_70747_aH = 0.15f;
                    LongJump.mc.field_71439_g.field_70181_x += (double)0.05f;
                    break;
                }
                case "redesky": {
                    if (((Boolean)this.redeskyTimerBoostValue.get()).booleanValue()) {
                        LongJump.mc.field_71428_T.field_74278_d = this.currentTimer;
                    }
                    if (this.ticks < (Integer)this.redeskyTickValue.get()) {
                        LongJump.mc.field_71439_g.func_70664_aZ();
                    } else {
                        if (((Boolean)this.redeskyGlideAfterTicksValue.get()).booleanValue()) {
                            LongJump.mc.field_71439_g.field_70181_x += (double)0.03f;
                        }
                        if (((Boolean)this.redeskyTimerBoostValue.get()).booleanValue() && this.currentTimer > ((Float)this.redeskyTimerBoostEndValue.get()).floatValue()) {
                            this.currentTimer = Math.max(0.08f, this.currentTimer - 0.05f * (float)((Integer)this.redeskyTimerBoostSlowDownSpeedValue.get()).intValue());
                        }
                    }
                    LongJump.mc.field_71439_g.field_70181_x *= (double)((Float)this.redeskyYMultiplier.get()).floatValue();
                    LongJump.mc.field_71439_g.field_70159_w *= (double)((Float)this.redeskyXZMultiplier.get()).floatValue();
                    LongJump.mc.field_71439_g.field_70179_y *= (double)((Float)this.redeskyXZMultiplier.get()).floatValue();
                    ++this.ticks;
                    break;
                }
                case "infiniteredesky": {
                    if (LongJump.mc.field_71439_g.field_70143_R > 0.6f) {
                        LongJump.mc.field_71439_g.field_70181_x += (double)0.02f;
                    }
                    MovementUtils.strafe((float)Math.min(0.85, Math.max(0.25, (double)MovementUtils.getSpeed() * 1.05878)));
                }
            }
        }
        if (((Boolean)this.autoJumpValue.get()).booleanValue() && LongJump.mc.field_71439_g.field_70122_E && MovementUtils.isMoving()) {
            this.jumped = true;
            LongJump.mc.field_71439_g.func_70664_aZ();
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        String mode = (String)this.modeValue.get();
        if (mode.equalsIgnoreCase("mineplex3")) {
            if (LongJump.mc.field_71439_g.field_70143_R != 0.0f) {
                LongJump.mc.field_71439_g.field_70181_x += 0.037;
            }
        } else if (mode.equalsIgnoreCase("ncp") && !MovementUtils.isMoving() && this.jumped) {
            LongJump.mc.field_71439_g.field_70159_w = 0.0;
            LongJump.mc.field_71439_g.field_70179_y = 0.0;
            event.zeroXZ();
        }
        if (mode.equalsIgnoreCase("damage") && ((Boolean)this.damageNoMoveValue.get()).booleanValue() && !this.damaged || mode.equalsIgnoreCase("verusdmg") && !this.verusDmged) {
            event.zeroXZ();
        }
        if (mode.equalsIgnoreCase("pearl") && this.pearlState != 2) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        String mode = (String)this.modeValue.get();
        if (event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packetPlayer = (C03PacketPlayer)event.getPacket();
            if (mode.equalsIgnoreCase("verusdmg") && ((String)this.verusDmgModeValue.get()).equalsIgnoreCase("Jump") && this.verusJumpTimes < 5) {
                packetPlayer.field_149474_g = false;
            }
        }
    }

    @EventTarget(ignoreCondition=true)
    public void onJump(JumpEvent event) {
        this.jumped = true;
        this.canBoost = true;
        this.teleported = false;
        if (this.getState()) {
            switch (((String)this.modeValue.get()).toLowerCase()) {
                case "mineplex": {
                    event.setMotion(event.getMotion() * 4.08f);
                    break;
                }
                case "mineplex2": {
                    if (!LongJump.mc.field_71439_g.field_70123_F) break;
                    event.setMotion(2.31f);
                    this.canMineplexBoost = true;
                    LongJump.mc.field_71439_g.field_70122_E = false;
                    break;
                }
                case "aacv4": {
                    event.setMotion(event.getMotion() * 1.0799f);
                }
            }
        }
    }

    private int getPearlSlot() {
        for (int i = 36; i < 45; ++i) {
            ItemStack stack = LongJump.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (stack == null || !(stack.func_77973_b() instanceof ItemEnderPearl)) continue;
            return i - 36;
        }
        return -1;
    }

    @Override
    public void onDisable() {
        LongJump.mc.field_71428_T.field_74278_d = 1.0f;
        LongJump.mc.field_71439_g.field_71102_ce = 0.02f;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

