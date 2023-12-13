/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.combat;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import markgg.Client;
import markgg.events.Event;
import markgg.events.listeners.EventMotion;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.settings.BooleanSetting;
import markgg.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class KillAura
extends Module {
    public static List<EntityLivingBase> targets;
    public float[] facing;
    public NumberSetting range = new NumberSetting("Range", 3.5, 1.0, 6.0, 0.1);
    public NumberSetting aps = new NumberSetting("APS", 10.0, 1.0, 20.0, 0.5);
    public BooleanSetting noSwing = new BooleanSetting("No Swing", false);
    public BooleanSetting disableOnDeath = new BooleanSetting("Disable on death", false);
    public BooleanSetting block = new BooleanSetting("Block", false);
    public long lastMS = System.currentTimeMillis();
    public boolean hasstopped = true;

    public KillAura() {
        super("KillAura", 0, Module.Category.COMBAT);
        this.addSettings(this.range, this.aps, this.noSwing, this.block, this.disableOnDeath);
    }

    @Override
    public void onDisable() {
        if (this.mc.currentScreen == null) {
            this.mc.gameSettings.keyBindUseItem.pressed = false;
            this.hasstopped = true;
        }
    }

    public static void click() throws AWTException {
        Robot bot = new Robot();
        bot.mousePress(2048);
        bot.mouseRelease(2048);
    }

    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (System.currentTimeMillis() - this.lastMS > time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public void setTime(long Time) {
        this.lastMS = Time;
    }

    public long getTime() {
        return System.currentTimeMillis() - this.lastMS;
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion) {
            EventMotion event = (EventMotion)e;
            targets = Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
            targets = targets.stream().filter(entity -> (double)entity.getDistanceToEntity(this.mc.thePlayer) < this.range.getValue() && entity != this.mc.thePlayer).collect(Collectors.toList());
            targets.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(this.mc.thePlayer)));
            if (targets.isEmpty()) {
                if (!this.hasstopped) {
                    this.mc.gameSettings.keyBindUseItem.pressed = false;
                }
                this.hasstopped = true;
            }
            if (!targets.isEmpty()) {
                this.hasstopped = false;
                EntityLivingBase entity2 = targets.get(0);
                AxisAlignedBB bb = entity2.getEntityBoundingBox();
                if (e.isPre()) {
                    double posx = this.mc.thePlayer.posX;
                    double posy = this.mc.thePlayer.posY;
                    double posz = this.mc.thePlayer.posZ;
                    double eyeheight = this.mc.thePlayer.getEyeHeight();
                    Vec3 eyes = new Vec3(posx, posy + eyeheight, posz);
                    Vec3 vector = new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.8 * Math.random(), bb.minY + (bb.maxY - bb.minY) * 1.0 * Math.random(), bb.minZ + (bb.maxZ - bb.minZ) * 0.8 * Math.random());
                    double var1 = vector.xCoord - eyes.xCoord;
                    double var2 = vector.yCoord - eyes.yCoord;
                    double var3 = vector.zCoord - eyes.zCoord;
                    double var4 = Math.sqrt(var1 * var1 + var3 * var3);
                    float yaw = (float)Math.toDegrees(Math.atan2(var3, var1)) - 90.0f;
                    float pitch = (float)(-Math.toDegrees(Math.atan2(var2, var4)));
                    this.facing = new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
                    event.setYaw(this.facing[0]);
                    event.setPitch(this.facing[1]);
                }
                if (e.isPost()) {
                    if (Client.isModuleToggled("AntiBot")) {
                        if (entity2.isInvisible()) {
                            return;
                        }
                        if (!entity2.onGround && entity2.motionY == 0.0 || entity2.isAirBorne && entity2.motionY == 0.0) {
                            return;
                        }
                        if (entity2.getDisplayName().getFormattedText() == "") {
                            return;
                        }
                    }
                    if (entity2.isDead) {
                        this.mc.gameSettings.keyBindUseItem.pressed = false;
                        return;
                    }
                    if (this.hasTimeElapsed((long)(1000.0 / this.aps.getValue()), true)) {
                        if (this.noSwing.isEnabled()) {
                            this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                        } else {
                            this.mc.thePlayer.swingItem();
                        }
                        this.mc.getNetHandler().addToSendQueue(new C02PacketUseEntity((Entity)entity2, C02PacketUseEntity.Action.ATTACK));
                        if (this.block.isEnabled() && this.mc.thePlayer.inventory.getCurrentItem() != null) {
                            this.mc.gameSettings.keyBindUseItem.pressed = true;
                            this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem());
                        }
                    }
                }
            }
        }
        if (e instanceof EventUpdate && this.disableOnDeath.isEnabled() && this.mc.thePlayer.getHealth() == 0.0f) {
            this.toggle();
        }
    }
}

