// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.combat;

import net.minecraft.util.MathHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import Lavish.utils.misc.NetUtil;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.item.ItemSword;
import Lavish.utils.render.RenderUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Comparator;
import Lavish.utils.math.MathUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import java.util.List;
import Lavish.event.events.EventMotion;
import Lavish.event.Event;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import java.util.ArrayList;
import Lavish.modules.Category;
import net.minecraft.entity.EntityLivingBase;
import Lavish.utils.misc.Timer;
import Lavish.modules.Module;

public class Killaura extends Module
{
    public Timer timer;
    public static boolean blocking;
    private float[] serverAngles;
    public static EntityLivingBase target;
    public float yaw;
    public float pitch;
    
    public Killaura() {
        super("Killaura", 0, true, Category.Combat, "Makes you automatically attack people!");
        this.timer = new Timer();
        this.serverAngles = new float[2];
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Vanilla");
        options.add("None");
        Client.instance.setmgr.rSetting(new Setting("Rotation Mode", this, "Vanilla", options));
        Client.instance.setmgr.rSetting(new Setting("CPS", this, 12.0, 1.0, 20.0, false));
        Client.instance.setmgr.rSetting(new Setting("CPS Random", this, 1.0, 0.0, 5.0, false));
        Client.instance.setmgr.rSetting(new Setting("Reach", this, 4.0, 3.0, 10.0, false));
        Client.instance.setmgr.rSetting(new Setting("Autoblock", this, false));
        Client.instance.setmgr.rSetting(new Setting("Animate", this, false));
        Client.instance.setmgr.rSetting(new Setting("No Swing", this, false));
        Client.instance.setmgr.rSetting(new Setting("Lock View", this, false));
    }
    
    @Override
    public void onDisable() {
        Killaura.blocking = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            final int cps = (int)Client.instance.setmgr.getSettingByName("CPS").getValDouble();
            if (Client.instance.moduleManager.getModuleByName("Scaffold").isEnabled()) {
                return;
            }
            final EventMotion event = (EventMotion)e;
            List<Entity> targets = Killaura.mc.theWorld.loadedEntityList.stream().filter(Entity.class::isInstance).collect((Collector<? super Object, ?, List<Entity>>)Collectors.toList());
            targets = targets.stream().filter(entity -> entity.getDistanceToEntity(Killaura.mc.thePlayer) < Client.instance.setmgr.getSettingByName("Reach").getValDouble() - MathUtils.getRandomInRange(0.2, 0.6) && entity != Killaura.mc.thePlayer && !entity.isDead).collect((Collector<? super Object, ?, List<Entity>>)Collectors.toList());
            targets.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(Killaura.mc.thePlayer)));
            targets = targets.stream().filter(EntityPlayer.class::isInstance).collect((Collector<? super Object, ?, List<Entity>>)Collectors.toList());
            if (!targets.isEmpty()) {
                final Entity target = targets.get(0);
                if (Antibot.getInvalid().contains(target)) {
                    return;
                }
                if (this.timer.isDelayComplete((float)(1000 / cps))) {
                    Killaura.mc.thePlayer.swingItem();
                    Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                    this.timer.reset();
                }
                if (Client.instance.setmgr.getSettingByName("Rotation Mode").getValString().equalsIgnoreCase("Vanilla")) {
                    final float[] dstAngle = this.getRotationsToEnt(target, Killaura.mc.thePlayer);
                    final float[] srcAngle = { this.serverAngles[0], this.serverAngles[1] };
                    this.serverAngles = this.smoothAngle(dstAngle, srcAngle);
                    Killaura.mc.thePlayer.renderYawOffset = dstAngle[0] + MathUtils.getRandom(12);
                    Killaura.mc.thePlayer.rotationYawHead = dstAngle[0] + MathUtils.getRandom(12);
                    Killaura.mc.thePlayer.rotationYaw += 0.001f;
                    Killaura.mc.thePlayer.rotationPitch += 0.001f;
                    if (Client.instance.setmgr.getSettingByName("Lock View").getValBoolean()) {
                        Killaura.mc.thePlayer.rotationYaw = dstAngle[0] + MathUtils.getRandom(12);
                        Killaura.mc.thePlayer.rotationPitch = dstAngle[1] - 6.0f;
                    }
                    RenderUtils.setCustomPitch(dstAngle[1] - 6.0f);
                    event.setYaw(dstAngle[0] + MathUtils.getRandom(12));
                    event.setPitch(dstAngle[1] - 6.0f);
                }
                if (Client.instance.setmgr.getSettingByName("Rotation Mode").getValString().equalsIgnoreCase("None")) {
                    RenderUtils.setCustomPitch(Killaura.mc.thePlayer.rotationPitch);
                }
                if (Client.instance.setmgr.getSettingByName("Autoblock").getValBoolean() && Killaura.mc.thePlayer.inventory.getCurrentItem() != null && Killaura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                    Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT));
                    NetUtil.sendPacketNoEvents(new C08PacketPlayerBlockPlacement(Killaura.mc.thePlayer.inventory.getCurrentItem()));
                    Killaura.mc.thePlayer.setItemInUse(Killaura.mc.thePlayer.getCurrentEquippedItem(), 71999);
                }
                if (Client.instance.setmgr.getSettingByName("Animate").getValBoolean() && Killaura.mc.thePlayer.inventory.getCurrentItem() != null && Killaura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                    Killaura.blocking = true;
                }
            }
            else {
                if (Client.instance.setmgr.getSettingByName("Autoblock").getValBoolean()) {
                    Killaura.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(1, 1, 1), EnumFacing.DOWN));
                }
                RenderUtils.setCustomPitch(Killaura.mc.thePlayer.rotationPitch);
                Killaura.blocking = false;
            }
        }
    }
    
    private float[] getRotationsToEnt(final Entity ent, final EntityPlayerSP playerSP) {
        final double differenceX = ent.posX - playerSP.posX;
        final double differenceY = ent.posY + ent.height - (playerSP.posY + playerSP.height + MathUtils.getRandomInRange(0.3, 0.7));
        final double differenceZ = ent.posZ - playerSP.posZ;
        final float rotationYaw = (float)(Math.atan2(differenceZ, differenceX) * 180.0 / 3.141592653589793) - 90.0f;
        final float rotationPitch = (float)(Math.atan2(differenceY, playerSP.getDistanceToEntity(ent)) * 180.0 / 3.141592653589793);
        final float finishedYaw = playerSP.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - playerSP.rotationYaw);
        final float finishedPitch = playerSP.rotationPitch + MathHelper.wrapAngleTo180_float(rotationPitch - playerSP.rotationPitch);
        return new float[] { finishedYaw, -finishedPitch };
    }
    
    private float[] smoothAngle(final float[] dst, final float[] src) {
        float[] smoothedAngle = { src[0] - dst[0], src[1] - dst[1] };
        smoothedAngle = MathUtils.constrainAngle(smoothedAngle);
        smoothedAngle[0] = src[0] - smoothedAngle[0] / 8.0f * MathUtils.getRandomInRange(8, 12);
        smoothedAngle[1] = src[1] - smoothedAngle[1] / 8.0f * MathUtils.getRandomInRange(3, 8);
        return smoothedAngle;
    }
    
    public float[] getRotations(final Entity e) {
        final double deltaX = e.posX + (e.posX - e.lastTickPosX) - Killaura.mc.thePlayer.posX;
        final double deltaY = e.posY - 3.5 + e.getEyeHeight() - Killaura.mc.thePlayer.posY + Killaura.mc.thePlayer.getEyeHeight();
        final double deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - Killaura.mc.thePlayer.posZ;
        final double distance = Math.sqrt(Math.pow(deltaX, 2.0) + Math.pow(deltaZ, 2.0));
        float yaw = (float)Math.toDegrees(-Math.atan(deltaX / deltaZ));
        final float pitch = (float)(-Math.toDegrees(Math.atan(deltaY / distance)));
        if (deltaX < 0.0 && deltaZ < 0.0) {
            yaw = (float)(90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        }
        else if (deltaX > 0.0 && deltaZ < 0.0) {
            yaw = (float)(-90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        }
        return new float[] { yaw, pitch };
    }
    
    private boolean isHoldingSword() {
        return Killaura.mc.thePlayer != null && (Killaura.mc.thePlayer.getCurrentEquippedItem() != null && Killaura.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword);
    }
    
    public float[] rotations(final Entity e) {
        final double deltaX = e.boundingBox.minX + (e.boundingBox.maxX - e.boundingBox.minX + 0.1) - Killaura.mc.thePlayer.posX;
        final double deltaY = e.posY - 4.25 + e.getEyeHeight() - Killaura.mc.thePlayer.posY + Killaura.mc.thePlayer.getEyeHeight();
        final double deltaZ = e.boundingBox.minZ + (e.boundingBox.maxX - e.boundingBox.minX) - Killaura.mc.thePlayer.posZ;
        final double distance = Math.sqrt(Math.pow(deltaX, 2.0) + Math.pow(deltaZ, 2.0));
        float yaw = (float)Math.toDegrees(-Math.atan(deltaX / deltaZ));
        final float pitch = (float)(-Math.toDegrees(Math.atan(deltaY / distance)));
        final double v = Math.toDegrees(Math.atan(deltaZ / deltaX));
        if (deltaX < 0.0 && deltaZ < 0.0) {
            yaw = (float)(90.0 + v);
        }
        else if (deltaX > 0.0 && deltaZ < 0.0) {
            yaw = (float)(-90.0 + v);
        }
        return new float[] { yaw, pitch };
    }
}
