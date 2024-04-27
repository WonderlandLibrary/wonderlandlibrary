/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.MathHelper
 */
package net.dev.important.modules.module.modules.movement;

import net.dev.important.event.BlockBBEvent;
import net.dev.important.event.EventState;
import net.dev.important.event.EventTarget;
import net.dev.important.event.JumpEvent;
import net.dev.important.event.MotionEvent;
import net.dev.important.event.MoveEvent;
import net.dev.important.event.PacketEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.value.FloatValue;
import net.dev.important.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

@Info(name="WallClimb", spacedName="Wall Climb", description="Allows you to climb up walls like a spider.", category=Category.MOVEMENT, cnName="\u722c\u5899")
public class WallClimb
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Simple", "CheckerClimb", "Clip", "AAC3.3.12", "AACGlide", "Verus"}, "Simple");
    private final ListValue clipMode = new ListValue("ClipMode", new String[]{"Jump", "Fast"}, "Fast", () -> ((String)this.modeValue.get()).equalsIgnoreCase("clip"));
    private final FloatValue checkerClimbMotionValue = new FloatValue("CheckerClimbMotion", 0.0f, 0.0f, 1.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("checkerclimb"));
    private final FloatValue verusClimbSpeed = new FloatValue("VerusClimbSpeed", 0.0f, 0.0f, 1.0f, () -> ((String)this.modeValue.get()).equalsIgnoreCase("verus"));
    private boolean glitch;
    private boolean canClimb;
    private int waited;

    @Override
    public void onEnable() {
        this.glitch = false;
        this.canClimb = false;
        this.waited = 0;
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (!WallClimb.mc.field_71439_g.field_70123_F || WallClimb.mc.field_71439_g.func_70617_f_() || WallClimb.mc.field_71439_g.func_70090_H() || WallClimb.mc.field_71439_g.func_180799_ab()) {
            return;
        }
        if ("simple".equalsIgnoreCase((String)this.modeValue.get())) {
            event.setY(0.2);
            WallClimb.mc.field_71439_g.field_70181_x = 0.0;
        }
    }

    @EventTarget
    public void onJump(JumpEvent event) {
        if (((String)this.modeValue.get()).equalsIgnoreCase("verus") && this.canClimb) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public void onUpdate(MotionEvent event) {
        if (event.getEventState() != EventState.POST) {
            return;
        }
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "clip": {
                if (WallClimb.mc.field_71439_g.field_70181_x < 0.0) {
                    this.glitch = true;
                }
                if (!WallClimb.mc.field_71439_g.field_70123_F) break;
                switch (((String)this.clipMode.get()).toLowerCase()) {
                    case "jump": {
                        if (!WallClimb.mc.field_71439_g.field_70122_E) break;
                        WallClimb.mc.field_71439_g.func_70664_aZ();
                        break;
                    }
                    case "fast": {
                        if (WallClimb.mc.field_71439_g.field_70122_E) {
                            WallClimb.mc.field_71439_g.field_70181_x = 0.42;
                            break;
                        }
                        if (!(WallClimb.mc.field_71439_g.field_70181_x < 0.0)) break;
                        WallClimb.mc.field_71439_g.field_70181_x = -0.3;
                    }
                }
                break;
            }
            case "checkerclimb": {
                boolean isInsideBlock = BlockUtils.collideBlockIntersects(WallClimb.mc.field_71439_g.func_174813_aQ(), block -> !(block instanceof BlockAir));
                float motion = ((Float)this.checkerClimbMotionValue.get()).floatValue();
                if (!isInsideBlock || motion == 0.0f) break;
                WallClimb.mc.field_71439_g.field_70181_x = motion;
                break;
            }
            case "aac3.3.12": {
                if (WallClimb.mc.field_71439_g.field_70123_F && !WallClimb.mc.field_71439_g.func_70617_f_()) {
                    ++this.waited;
                    if (this.waited == 1) {
                        WallClimb.mc.field_71439_g.field_70181_x = 0.43;
                    }
                    if (this.waited == 12) {
                        WallClimb.mc.field_71439_g.field_70181_x = 0.43;
                    }
                    if (this.waited == 23) {
                        WallClimb.mc.field_71439_g.field_70181_x = 0.43;
                    }
                    if (this.waited == 29) {
                        WallClimb.mc.field_71439_g.func_70107_b(WallClimb.mc.field_71439_g.field_70165_t, WallClimb.mc.field_71439_g.field_70163_u + 0.5, WallClimb.mc.field_71439_g.field_70161_v);
                    }
                    if (this.waited < 30) break;
                    this.waited = 0;
                    break;
                }
                if (!WallClimb.mc.field_71439_g.field_70122_E) break;
                this.waited = 0;
                break;
            }
            case "aacglide": {
                if (!WallClimb.mc.field_71439_g.field_70123_F || WallClimb.mc.field_71439_g.func_70617_f_()) {
                    return;
                }
                WallClimb.mc.field_71439_g.field_70181_x = -0.189;
                break;
            }
            case "verus": {
                if (!WallClimb.mc.field_71439_g.field_70123_F || WallClimb.mc.field_71439_g.func_70090_H() || WallClimb.mc.field_71439_g.func_180799_ab() || WallClimb.mc.field_71439_g.func_70617_f_() || WallClimb.mc.field_71439_g.field_70134_J || WallClimb.mc.field_71439_g.func_70617_f_()) {
                    this.canClimb = false;
                    break;
                }
                this.canClimb = true;
                WallClimb.mc.field_71439_g.field_70181_x = ((Float)this.verusClimbSpeed.get()).floatValue();
                WallClimb.mc.field_71439_g.field_70122_E = true;
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            if (this.glitch) {
                float yaw = (float)MovementUtils.getDirection();
                packetPlayer.field_149479_a -= (double)MathHelper.func_76126_a((float)yaw) * 1.0E-8;
                packetPlayer.field_149478_c += (double)MathHelper.func_76134_b((float)yaw) * 1.0E-8;
                this.glitch = false;
            }
            if (this.canClimb) {
                packetPlayer.field_149474_g = true;
            }
        }
    }

    @EventTarget
    public void onBlockBB(BlockBBEvent event) {
        if (WallClimb.mc.field_71439_g == null) {
            return;
        }
        String mode = (String)this.modeValue.get();
        switch (mode.toLowerCase()) {
            case "checkerclimb": {
                if (!((double)event.getY() > WallClimb.mc.field_71439_g.field_70163_u)) break;
                event.setBoundingBox(null);
                break;
            }
            case "clip": {
                if (event.getBlock() == null || WallClimb.mc.field_71439_g == null || !(event.getBlock() instanceof BlockAir) || !((double)event.getY() < WallClimb.mc.field_71439_g.field_70163_u) || !WallClimb.mc.field_71439_g.field_70123_F || WallClimb.mc.field_71439_g.func_70617_f_() || WallClimb.mc.field_71439_g.func_70090_H() || WallClimb.mc.field_71439_g.func_180799_ab()) break;
                event.setBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).func_72317_d(WallClimb.mc.field_71439_g.field_70165_t, (double)((int)WallClimb.mc.field_71439_g.field_70163_u - 1), WallClimb.mc.field_71439_g.field_70161_v));
            }
        }
    }
}

