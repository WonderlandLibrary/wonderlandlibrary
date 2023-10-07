/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3f
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  org.lwjgl.input.Mouse
 */
package me.report.liquidware.modules.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.vecmath.Vector3f;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

@ModuleInfo(name="LookTP", description="LookTP", category=ModuleCategory.MOVEMENT)
public class LookTP
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"old"}, "old");
    private final BoolValue resetAfterTp = new BoolValue("NoSpam", true);
    private final ListValue buttonValue = new ListValue("Button", new String[]{"Left", "Right", "Middle"}, "Middle");
    private int delay;
    private BlockPos endPos;
    private MovingObjectPosition objectPosition;

    @Override
    public void onDisable() {
        this.delay = 0;
        this.endPos = null;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (LookTP.mc.field_71462_r == null && Mouse.isButtonDown((int)Arrays.asList(this.buttonValue.getValues()).indexOf(this.buttonValue.get())) && this.delay <= 0) {
            this.endPos = this.objectPosition.func_178782_a();
            if (BlockUtils.getBlock(this.endPos).func_149688_o() == Material.field_151579_a) {
                this.endPos = null;
                return;
            }
            new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
            LiquidBounce.hud.addNotification(new Notification("Getting ready! Teleporting to " + this.endPos.func_177958_n() + ", " + this.endPos.func_177956_o() + ", " + this.endPos.func_177952_p(), Notification.Type.WARNING));
            this.delay = 6;
        }
        if (this.delay > 0) {
            --this.delay;
        }
        if (this.endPos != null && LookTP.mc.field_71439_g.func_70093_af()) {
            if (!LookTP.mc.field_71439_g.field_70122_E) {
                double endX = (double)this.endPos.func_177958_n() + 0.5;
                double endY = (double)this.endPos.func_177956_o() + 1.3;
                double endZ = (double)this.endPos.func_177952_p() + 0.5;
                if ("old".equalsIgnoreCase((String)this.modeValue.get())) {
                    for (Vector3f vector3f : this.vanillaTeleportPositions(endX, endY, endZ, 0.001)) {
                        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition((double)vector3f.getX(), (double)vector3f.getY(), (double)vector3f.getZ(), false));
                    }
                }
                if (((Boolean)this.resetAfterTp.get()).booleanValue()) {
                    this.endPos = null;
                }
                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                LiquidBounce.hud.addNotification(new Notification("Successfully Teleported!", Notification.Type.SUCCESS));
            } else {
                LookTP.mc.field_71439_g.func_70664_aZ();
            }
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        this.objectPosition = LookTP.mc.field_71439_g.func_174822_a(1000.0, event.getPartialTicks());
        if (this.objectPosition.func_178782_a() == null) {
            return;
        }
        int x = this.objectPosition.func_178782_a().func_177958_n();
        int y = this.objectPosition.func_178782_a().func_177956_o();
        int z = this.objectPosition.func_178782_a().func_177952_p();
        if (BlockUtils.getBlock(this.objectPosition.func_178782_a()).func_149688_o() != Material.field_151579_a) {
            RenderManager renderManager = mc.func_175598_ae();
        }
    }

    private List<Vector3f> vanillaTeleportPositions(double tpX, double tpY, double tpZ, double speed) {
        double d;
        ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
        double posX = tpX - LookTP.mc.field_71439_g.field_70165_t;
        double posZ = tpZ - LookTP.mc.field_71439_g.field_70161_v;
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / Math.PI - 90.0);
        double tmpY = LookTP.mc.field_71439_g.field_70163_u;
        double steps = 1.0;
        for (d = speed; d < this.getDistance(LookTP.mc.field_71439_g.field_70165_t, LookTP.mc.field_71439_g.field_70163_u, LookTP.mc.field_71439_g.field_70161_v, tpX, tpY, tpZ); d += speed) {
            steps += 1.0;
        }
        for (d = speed; d < this.getDistance(LookTP.mc.field_71439_g.field_70165_t, LookTP.mc.field_71439_g.field_70163_u, LookTP.mc.field_71439_g.field_70161_v, tpX, tpY, tpZ); d += speed) {
            double tmpX = LookTP.mc.field_71439_g.field_70165_t - Math.sin(Math.toRadians(yaw)) * d;
            double tmpZ = LookTP.mc.field_71439_g.field_70161_v + Math.cos(Math.toRadians(yaw)) * d;
            positions.add(new Vector3f((float)tmpX, (float)(tmpY -= (LookTP.mc.field_71439_g.field_70163_u - tpY) / steps), (float)tmpZ));
        }
        positions.add(new Vector3f((float)tpX, (float)tpY, (float)tpZ));
        return positions;
    }

    private double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d1 = y1 - y2;
        double d2 = z1 - z2;
        return MathHelper.func_76133_a((double)(d0 * d0 + d1 * d1 + d2 * d2));
    }
}

