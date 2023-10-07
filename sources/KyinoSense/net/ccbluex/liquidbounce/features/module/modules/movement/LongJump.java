/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C00PacketKeepAlive
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.PosLookInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name="LongJump", spacedName="Long Jump", description="Allows you to jump further.", category=ModuleCategory.MOVEMENT)
public class LongJump
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"NCP", "Damage", "AACv1", "AACv2", "AACv3", "AAC4", "Blocksmc", "BlocksmcPacket", "Mineplex", "Mineplex2", "Mineplex3", "RedeskyMaki", "Redesky", "InfiniteRedesky", "MatrixFlag", "VerusDmg", "Pearl"}, "NCP");
    private final BoolValue autoJumpValue = new BoolValue("AutoJump", false);
    private final FloatValue ncpBoostValue = new FloatValue("NCP-Boost", 4.25f, 1.0f, 10.0f);
    private final FloatValue matrixBoostValue = new FloatValue("MatrixFlag-Boost", 1.95f, 0.0f, 10.0f);
    private final FloatValue matrixHeightValue = new FloatValue("MatrixFlag-Height", 5.0f, 0.0f, 10.0f);
    private final BoolValue matrixKeepAliveValue = new BoolValue("MatrixFlag-KeepAlive", true);
    private final BoolValue matrixJBAValue = new BoolValue("MatrixFlag-JumpBeforeActivation", true);
    private final BoolValue matrixJumpValue = new BoolValue("MatrixFlag-KeepJump", true);
    private final BoolValue redeskyTimerBoostValue = new BoolValue("Redesky-TimerBoost", false);
    private final BoolValue redeskyGlideAfterTicksValue = new BoolValue("Redesky-GlideAfterTicks", false);
    private final IntegerValue redeskyTickValue = new IntegerValue("Redesky-Ticks", 21, 1, 25);
    private final FloatValue redeskyYMultiplier = new FloatValue("Redesky-YMultiplier", 0.77f, 0.1f, 1.0f);
    private final FloatValue redeskyXZMultiplier = new FloatValue("Redesky-XZMultiplier", 0.9f, 0.1f, 1.0f);
    private final FloatValue redeskyTimerBoostStartValue = new FloatValue("Redesky-TimerBoostStart", 1.85f, 0.05f, 10.0f);
    private final FloatValue redeskyTimerBoostEndValue = new FloatValue("Redesky-TimerBoostEnd", 1.0f, 0.05f, 10.0f);
    private final IntegerValue redeskyTimerBoostSlowDownSpeedValue = new IntegerValue("Redesky-TimerBoosSlowDownSpeed", 2, 1, 10);
    private final FloatValue verusBoostValue = new FloatValue("VerusDmg-Boost", 4.25f, 0.0f, 10.0f);
    private final FloatValue verusHeightValue = new FloatValue("VerusDmg-Height", 0.42f, 0.0f, 10.0f);
    private final FloatValue verusTimerValue = new FloatValue("VerusDmg-Timer", 1.0f, 0.05f, 10.0f);
    private final FloatValue pearlBoostValue = new FloatValue("Pearl-Boost", 4.25f, 0.0f, 10.0f);
    private final FloatValue pearlHeightValue = new FloatValue("Pearl-Height", 0.42f, 0.0f, 10.0f);
    private final FloatValue pearlTimerValue = new FloatValue("Pearl-Timer", 1.0f, 0.05f, 10.0f);
    private final FloatValue damageBoostValue = new FloatValue("Damage-Boost", 4.25f, 0.0f, 10.0f);
    private final FloatValue damageHeightValue = new FloatValue("Damage-Height", 0.42f, 0.0f, 10.0f);
    private final FloatValue damageTimerValue = new FloatValue("Damage-Timer", 1.0f, 0.05f, 10.0f);
    private final BoolValue damageNoMoveValue = new BoolValue("Damage-NoMove", false);
    private final BoolValue damageARValue = new BoolValue("Damage-AutoReset", false);
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
    private double lastMotX;
    private double lastMotY;
    private double lastMotZ;
    private boolean flagged = false;
    private boolean hasFell = false;
    private MSTimer dmgTimer = new MSTimer();
    private PosLookInstance posLookInstance = new PosLookInstance();

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
        this.flagged = false;
        this.hasFell = false;
        this.pearlState = 0;
        this.verusJumpTimes = 0;
        this.dmgTimer.reset();
        this.posLookInstance.reset();
        double x = LongJump.mc.field_71439_g.field_70165_t;
        double y = LongJump.mc.field_71439_g.field_70163_u;
        double z = LongJump.mc.field_71439_g.field_70161_v;
        if (((String)this.modeValue.get()).equalsIgnoreCase("matrixflag")) {
            if (((Boolean)this.matrixJBAValue.get()).booleanValue()) {
                if (LongJump.mc.field_71439_g.field_70122_E) {
                    LongJump.mc.field_71439_g.func_70664_aZ();
                }
            } else {
                this.hasFell = true;
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (((String)this.modeValue.get()).equalsIgnoreCase("matrixflag")) {
            if (this.hasFell) {
                if (!this.flagged) {
                    if (((Boolean)this.matrixJumpValue.get()).booleanValue()) {
                        LongJump.mc.field_71439_g.func_70664_aZ();
                    }
                    MovementUtils.strafe(((Float)this.matrixBoostValue.get()).floatValue());
                    LongJump.mc.field_71439_g.field_70181_x = ((Float)this.matrixHeightValue.get()).floatValue();
                    if (((Boolean)this.matrixKeepAliveValue.get()).booleanValue()) {
                        mc.func_147114_u().func_147297_a((Packet)new C00PacketKeepAlive());
                    }
                }
            } else {
                LongJump.mc.field_71439_g.field_70159_w *= 0.2;
                LongJump.mc.field_71439_g.field_70179_y *= 0.2;
                if (LongJump.mc.field_71439_g.field_70143_R > 0.0f) {
                    this.hasFell = true;
                }
            }
            return;
        }
        if (((String)this.modeValue.get()).equalsIgnoreCase("verusdmg")) {
            if (LongJump.mc.field_71439_g.field_70737_aN > 0 && !this.verusDmged) {
                this.verusDmged = true;
                MovementUtils.strafe(((Float)this.verusBoostValue.get()).floatValue());
                LongJump.mc.field_71439_g.field_70181_x = ((Float)this.verusHeightValue.get()).floatValue();
            }
            if (this.verusDmged) {
                LongJump.mc.field_71428_T.field_74278_d = ((Float)this.verusTimerValue.get()).floatValue();
            } else {
                LongJump.mc.field_71439_g.field_71158_b.field_78900_b = 0.0f;
                LongJump.mc.field_71439_g.field_71158_b.field_78902_a = 0.0f;
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
                case "blocksmc": {
                    LongJump.mc.field_71439_g.field_70747_aH = 0.1f;
                    LongJump.mc.field_71439_g.field_70181_x += 0.0132;
                    LongJump.mc.field_71439_g.field_70747_aH = 0.09f;
                    LongJump.mc.field_71428_T.field_74278_d = 0.8f;
                    MovementUtils.strafe();
                    break;
                }
                case "blocksmcpacket": {
                    LongJump.mc.field_71439_g.field_70181_x += 0.01554;
                    MovementUtils.strafe(MovementUtils.getSpeed() * 1.114514f);
                    LongJump.mc.field_71428_T.field_74278_d = 0.917555f;
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
                case "aac4": {
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
                        LongJump.mc.field_71439_g.field_70181_x *= (double)((Float)this.redeskyYMultiplier.get()).floatValue();
                        LongJump.mc.field_71439_g.field_70159_w *= (double)((Float)this.redeskyXZMultiplier.get()).floatValue();
                        LongJump.mc.field_71439_g.field_70179_y *= (double)((Float)this.redeskyXZMultiplier.get()).floatValue();
                    } else {
                        if (((Boolean)this.redeskyGlideAfterTicksValue.get()).booleanValue()) {
                            LongJump.mc.field_71439_g.field_70181_x += (double)0.03f;
                        }
                        if (((Boolean)this.redeskyTimerBoostValue.get()).booleanValue() && this.currentTimer > ((Float)this.redeskyTimerBoostEndValue.get()).floatValue()) {
                            this.currentTimer = Math.max(0.08f, this.currentTimer - 0.05f * (float)((Integer)this.redeskyTimerBoostSlowDownSpeedValue.get()).intValue());
                        }
                    }
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
            if (mode.equalsIgnoreCase("verusdmg") && this.verusJumpTimes < 5) {
                packetPlayer.field_149474_g = false;
            }
        }
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            this.flagged = true;
            this.posLookInstance.set((S08PacketPlayerPosLook)event.getPacket());
            this.lastMotX = LongJump.mc.field_71439_g.field_70159_w;
            this.lastMotY = LongJump.mc.field_71439_g.field_70181_x;
            this.lastMotZ = LongJump.mc.field_71439_g.field_70179_y;
        }
        if (event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook && this.posLookInstance.equalFlag((C03PacketPlayer.C06PacketPlayerPosLook)event.getPacket())) {
            this.posLookInstance.reset();
            ClientUtils.displayChatMessage("\u00a7a\u00a7lLaunched!");
            LongJump.mc.field_71439_g.field_70159_w = this.lastMotX;
            LongJump.mc.field_71439_g.field_70181_x = this.lastMotY;
            LongJump.mc.field_71439_g.field_70179_y = this.lastMotZ;
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
                case "aac4": {
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

