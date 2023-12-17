// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.event.EventTarget;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import ru.tuskevich.modules.impl.HUD.Hud;
import org.lwjgl.opengl.GL11;
import ru.tuskevich.modules.impl.COMBAT.KillAura;
import ru.tuskevich.event.events.impl.EventRender;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "TargetStrafe", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class TargetStrafe extends Module
{
    private SliderSetting distance;
    private SliderSetting speedValue;
    private BooleanSetting renderCircle;
    private boolean switchDir;
    
    public TargetStrafe() {
        this.distance = new SliderSetting("Distance", 2.0f, 0.1f, 6.0f, 0.45f);
        this.speedValue = new SliderSetting("Speed", 0.23f, 0.1f, 2.0f, 0.01f);
        this.renderCircle = new BooleanSetting("Render Circle", true);
        this.switchDir = true;
        this.add(this.distance, this.speedValue, this.renderCircle);
    }
    
    @EventTarget
    public void onRenderCircle(final EventRender render) {
        if (KillAura.target != null && this.renderCircle.get()) {
            final EntityLivingBase entity = KillAura.target;
            final double calcX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * TargetStrafe.mc.getRenderPartialTicks() - TargetStrafe.mc.getRenderManager().renderPosX;
            final double calcY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * TargetStrafe.mc.getRenderPartialTicks() - TargetStrafe.mc.getRenderManager().renderPosY;
            final double calcZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * TargetStrafe.mc.getRenderPartialTicks() - TargetStrafe.mc.getRenderManager().renderPosZ;
            final float radius = this.distance.getFloatValue();
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (int i = 0; i <= 360; ++i) {
                final int rainbow = Hud.getColor(i).getRGB();
                GlStateManager.color((rainbow >> 16 & 0xFF) / 255.0f, (rainbow >> 8 & 0xFF) / 255.0f, (rainbow & 0xFF) / 255.0f);
                GL11.glVertex3d(calcX + radius * Math.cos(Math.toRadians(i)), calcY, calcZ + radius * Math.sin(Math.toRadians(i)));
            }
            GL11.glEnd();
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glEnable(2929);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
            GlStateManager.resetColor();
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (KillAura.target != null) {
            final Minecraft mc = TargetStrafe.mc;
            if (Minecraft.player.ticksExisted >= 20) {
                final EntityLivingBase entity = KillAura.target;
                final double speed = this.speedValue.getFloatValue();
                final Minecraft mc2 = TargetStrafe.mc;
                if (Minecraft.player.onGround) {
                    final Minecraft mc3 = TargetStrafe.mc;
                    Minecraft.player.jump();
                }
                final Minecraft mc4 = TargetStrafe.mc;
                final double y = Minecraft.player.posZ - entity.posZ;
                final Minecraft mc5 = TargetStrafe.mc;
                final double atan2;
                double wrap = atan2 = Math.atan2(y, Minecraft.player.posX - entity.posX);
                double n2;
                if (this.switchDir) {
                    final double n = speed;
                    final Minecraft mc6 = TargetStrafe.mc;
                    n2 = n / Minecraft.player.getDistance(entity);
                }
                else {
                    final double n3 = speed;
                    final Minecraft mc7 = TargetStrafe.mc;
                    n2 = -(n3 / Minecraft.player.getDistance(entity));
                }
                wrap = atan2 + n2;
                double x = entity.posX + this.distance.getFloatValue() * Math.cos(wrap);
                double z = entity.posZ + this.distance.getFloatValue() * Math.sin(wrap);
                if (this.switchCheck(x, z)) {
                    this.switchDir = !this.switchDir;
                    final double n4 = wrap;
                    final double n5 = 2.0;
                    double n7;
                    if (this.switchDir) {
                        final double n6 = speed;
                        final Minecraft mc8 = TargetStrafe.mc;
                        n7 = n6 / Minecraft.player.getDistance(entity);
                    }
                    else {
                        final double n8 = speed;
                        final Minecraft mc9 = TargetStrafe.mc;
                        n7 = -(n8 / Minecraft.player.getDistance(entity));
                    }
                    wrap = n4 + n5 * n7;
                    x = entity.posX + this.distance.getFloatValue() * Math.cos(wrap);
                    z = entity.posZ + this.distance.getFloatValue() * Math.sin(wrap);
                }
                final Minecraft mc10 = TargetStrafe.mc;
                Minecraft.player.motionX = speed * -Math.sin(Math.toRadians(this.wrapDS(x, z)));
                final Minecraft mc11 = TargetStrafe.mc;
                Minecraft.player.motionZ = speed * Math.cos(Math.toRadians(this.wrapDS(x, z)));
            }
        }
    }
    
    public boolean switchCheck(final double x, final double z) {
        final Minecraft mc = TargetStrafe.mc;
        if (Minecraft.player.collidedHorizontally || TargetStrafe.mc.gameSettings.keyBindLeft.isPressed() || TargetStrafe.mc.gameSettings.keyBindRight.isPressed()) {
            return true;
        }
        final Minecraft mc2 = TargetStrafe.mc;
        int l = (int)(Minecraft.player.posY + 4.0);
        while (l >= 0) {
            final BlockPos playerPos = new BlockPos(x, l, z);
            if (!TargetStrafe.mc.world.getBlockState(playerPos).getBlock().equals(Blocks.LAVA)) {
                if (!TargetStrafe.mc.world.getBlockState(playerPos).getBlock().equals(Blocks.FIRE)) {
                    if (TargetStrafe.mc.world.isAirBlock(playerPos)) {
                        --l;
                        continue;
                    }
                    return false;
                }
            }
            return true;
        }
        return true;
    }
    
    private double wrapDS(final double x, final double z) {
        final Minecraft mc = TargetStrafe.mc;
        final double diffX = x - Minecraft.player.posX;
        final Minecraft mc2 = TargetStrafe.mc;
        final double diffZ = z - Minecraft.player.posZ;
        return Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0;
    }
}
