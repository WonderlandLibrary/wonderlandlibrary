// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.MathHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.Iterator;
import vip.Resolute.util.render.RenderUtils;
import vip.Resolute.util.render.Colors;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import vip.Resolute.events.impl.EventRender3D;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import vip.Resolute.events.impl.EventPacket;
import net.minecraft.world.World;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import java.util.Collection;
import net.minecraft.util.Vec3;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.util.HashMap;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.BooleanSetting;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import vip.Resolute.modules.Module;

public class AimBot extends Module
{
    public int ticks;
    public int lookDelay;
    private EntityPlayer target;
    public int buffer;
    private Map playerPositions;
    public static boolean isFiring;
    public BooleanSetting silent;
    public NumberSetting delay;
    public BooleanSetting autoFire;
    public NumberSetting fov;
    public NumberSetting recoilSet;
    
    public AimBot() {
        super("AimBot", 0, "Automatically aims at targets", Category.COMBAT);
        this.buffer = 10;
        this.playerPositions = new HashMap();
        this.silent = new BooleanSetting("Silent", true);
        this.delay = new NumberSetting("Delay", 3.0, 0.1, 10.0, 0.1);
        this.autoFire = new BooleanSetting("Auto Fire", true);
        this.fov = new NumberSetting("FOV", 90.0, 5.0, 360.0, 5.0);
        this.recoilSet = new NumberSetting("Recoil", 1.5, 0.1, 3.0, 0.1);
        this.addSettings(this.silent, this.delay, this.autoFire, this.fov, this.recoilSet);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (AimBot.isFiring) {
            AimBot.isFiring = false;
        }
        if (e instanceof EventMotion && AimBot.mc.thePlayer.isEntityAlive()) {
            final EventMotion em = (EventMotion)e;
            if (em.isPre()) {
                double targetWeight = Double.NEGATIVE_INFINITY;
                this.target = null;
                for (final Object o : AimBot.mc.theWorld.getLoadedEntityList()) {
                    if (o instanceof EntityPlayer) {
                        final EntityPlayer p = (EntityPlayer)o;
                        if (p == AimBot.mc.thePlayer || isTeam(AimBot.mc.thePlayer, p) || !AimBot.mc.thePlayer.canEntityBeSeen(p) || !this.isVisibleFOV(AimBot.mc.thePlayer, p, (float)this.fov.getValue())) {
                            continue;
                        }
                        if (this.target == null) {
                            this.target = p;
                            targetWeight = this.getTargetWeight(p);
                        }
                        else {
                            if (this.getTargetWeight(p) <= targetWeight) {
                                continue;
                            }
                            this.target = p;
                            targetWeight = this.getTargetWeight(p);
                        }
                    }
                }
                for (final Object o : this.playerPositions.keySet().toArray()) {
                    final EntityPlayer player = (EntityPlayer)o;
                    if (!AimBot.mc.theWorld.playerEntities.contains(player) || !checkPing(player)) {
                        this.playerPositions.remove(player);
                    }
                }
                for (final Object o : AimBot.mc.theWorld.playerEntities) {
                    final EntityPlayer p = (EntityPlayer)o;
                    this.playerPositions.putIfAbsent(p, new ArrayList());
                    final List previousPositions = this.playerPositions.get(p);
                    previousPositions.add(new Vec3(p.posX, p.posY, p.posZ));
                    if (previousPositions.size() > this.buffer) {
                        int i = 0;
                        for (final Vec3 position : new ArrayList(previousPositions)) {
                            if (i < previousPositions.size() - this.buffer) {
                                previousPositions.remove(previousPositions.get(i));
                            }
                            ++i;
                        }
                    }
                }
                if (this.target != null) {
                    final EntityLivingBase simulated = (EntityLivingBase)this.predictPlayerMovement(this.target);
                    final float[] rotations = this.getRotationsToEnt(this.predict(this.target), AimBot.mc.thePlayer);
                    em.setYaw(rotations[0]);
                    em.setPitch(rotations[1]);
                    ++this.lookDelay;
                    if ((float)this.lookDelay >= this.delay.getValue()) {
                        AimBot.isFiring = true;
                        final boolean nospread = true;
                        if (nospread) {
                            AimBot.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(AimBot.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                        }
                        if (this.autoFire.isEnabled() && AimBot.mc.thePlayer.inventory.getCurrentItem() != null) {
                            AimBot.mc.playerController.sendUseItem(AimBot.mc.thePlayer, AimBot.mc.theWorld, AimBot.mc.thePlayer.inventory.getCurrentItem());
                        }
                        if (nospread) {
                            AimBot.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(AimBot.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        }
                        this.lookDelay = 0;
                    }
                }
                else {
                    --this.ticks;
                    if (this.ticks <= 0) {
                        this.ticks = 0;
                    }
                }
            }
        }
        else if (e instanceof EventPacket) {
            if (((EventPacket)e).getPacket() instanceof C08PacketPlayerBlockPlacement) {
                ++this.ticks;
            }
        }
        else if (e instanceof EventRender3D) {
            final EventRender3D er = (EventRender3D)e;
            if (this.target != null) {
                final double[] p2 = this.getPredPos(this.target);
                final double x = AimBot.mc.thePlayer.prevPosX + (AimBot.mc.thePlayer.posX - AimBot.mc.thePlayer.prevPosX) * er.getPartialTicks() - RenderManager.renderPosX;
                final double y = AimBot.mc.thePlayer.prevPosY + (AimBot.mc.thePlayer.posY - AimBot.mc.thePlayer.prevPosY) * er.getPartialTicks() - RenderManager.renderPosY;
                final double z = AimBot.mc.thePlayer.prevPosZ + (AimBot.mc.thePlayer.posZ - AimBot.mc.thePlayer.prevPosZ) * er.getPartialTicks() - RenderManager.renderPosZ;
                GlStateManager.pushMatrix();
                GlStateManager.translate(x, y, z);
                RenderUtils.filledBox(new AxisAlignedBB(p2[0] - 0.5, p2[1], p2[2] - 0.5, p2[0] + 0.5, p2[1] + 2.0, p2[2] + 0.5), Colors.getColor(255, 0, 0, 100), true);
                GlStateManager.popMatrix();
            }
        }
    }
    
    private float[] getRotationsToEnt(final Vec3 ent, final EntityPlayerSP playerSP) {
        final double differenceX = ent.xCoord - playerSP.posX;
        final double differenceY = ent.yCoord + this.target.height - (playerSP.posY + playerSP.height);
        final double differenceZ = ent.zCoord - playerSP.posZ;
        final float rotationYaw = (float)(Math.atan2(differenceZ, differenceX) * 180.0 / 3.141592653589793) - 90.0f;
        final float rotationPitch = (float)(Math.atan2(differenceY, playerSP.getDistanceToEntity(this.target)) * 180.0 / 3.141592653589793);
        final float finishedYaw = playerSP.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - playerSP.rotationYaw);
        final float finishedPitch = playerSP.rotationPitch + MathHelper.wrapAngleTo180_float(rotationPitch - playerSP.rotationPitch);
        return new float[] { finishedYaw, -finishedPitch };
    }
    
    public static boolean checkPing(final EntityPlayer entity) {
        final NetworkPlayerInfo info = AimBot.mc.getNetHandler().getPlayerInfo(entity.getUniqueID());
        return info != null && info.getResponseTime() == 1;
    }
    
    private double[] getPredPos(final Entity entity) {
        final double xDelta = entity.posX - entity.lastTickPosX;
        final double zDelta = entity.posZ - entity.lastTickPosZ;
        final double yDelta = entity.posY - entity.lastTickPosY;
        double d = AimBot.mc.thePlayer.getDistanceToEntity(entity);
        d -= d % 0.8;
        double xMulti = 1.0;
        double zMulti = 1.0;
        final boolean sprint = entity.isSprinting();
        xMulti = d / 0.8 * xDelta * (sprint ? 1.2 : 1.1);
        zMulti = d / 0.8 * zDelta * (sprint ? 1.2 : 1.1);
        final double yMulti = d / 0.8 * yDelta * 0.1;
        final double x = entity.posX + xMulti - AimBot.mc.thePlayer.posX;
        final double z = entity.posZ + zMulti - AimBot.mc.thePlayer.posZ;
        final double y = entity.posY + yMulti - AimBot.mc.thePlayer.posY;
        return new double[] { x, y, z };
    }
    
    private Vec3 predict(final EntityPlayer player) {
        final int pingTicks = (int)Math.ceil(AimBot.mc.getNetHandler().getPlayerInfo(AimBot.mc.thePlayer.getUniqueID()).getResponseTime() / 50.0) + 1;
        return predictPos(player, (float)pingTicks);
    }
    
    private static Vec3 lerp(final Vec3 pos, final Vec3 prev, final float time) {
        final double x = pos.xCoord + (pos.xCoord - prev.xCoord) * time;
        final double y = pos.yCoord + (pos.yCoord - prev.yCoord) * time;
        final double z = pos.zCoord + (pos.zCoord - prev.zCoord) * time;
        return new Vec3(x, y, z);
    }
    
    public static Vec3 predictPos(final Entity entity, final float time) {
        return lerp(new Vec3(entity.posX, entity.posY, entity.posZ), new Vec3(entity.prevPosX, entity.prevPosY, entity.prevPosZ), time);
    }
    
    public static boolean isTeam(final EntityPlayer e, final EntityPlayer e2) {
        return e.getDisplayName().getFormattedText().contains("§" + isTeam(e)) && e2.getDisplayName().getFormattedText().contains("§" + isTeam(e));
    }
    
    private static String isTeam(final EntityPlayer player) {
        final Matcher m = Pattern.compile("§(.).*§r").matcher(player.getDisplayName().getFormattedText());
        return m.find() ? m.group(1) : "f";
    }
    
    public boolean isVisibleFOV(final EntityLivingBase e, final EntityLivingBase e2, final float fov) {
        return ((Math.abs(getRotations(e)[0] - e2.rotationYaw) % 360.0f > 180.0f) ? (360.0f - Math.abs(getRotations(e)[0] - e2.rotationYaw) % 360.0f) : (Math.abs(getRotations(e)[0] - e2.rotationYaw) % 360.0f)) <= fov;
    }
    
    public static float[] getRotations(final EntityLivingBase ent) {
        final double x = ent.posX;
        final double z = ent.posZ;
        final double y = ent.posY + ent.getEyeHeight() / 2.0f;
        return getRotationFromPosition(x, z, y);
    }
    
    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - AimBot.mc.thePlayer.posX;
        final double zDiff = z - AimBot.mc.thePlayer.posZ;
        final double yDiff = y - AimBot.mc.thePlayer.posY - 1.2;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public double getTargetWeight(final EntityPlayer p) {
        double weight = -AimBot.mc.thePlayer.getDistanceToEntity(p);
        if (p.lastTickPosX == p.posX && p.lastTickPosY == p.posY && p.lastTickPosZ == p.posZ) {
            weight += 200.0;
        }
        weight -= p.getDistanceToEntity(AimBot.mc.thePlayer) / 5.0f;
        return weight;
    }
    
    private Entity predictPlayerMovement(final EntityPlayer target) {
        int pingTicks = 0;
        try {
            pingTicks = (int)Math.ceil(AimBot.mc.getNetHandler().getPlayerInfo(AimBot.mc.thePlayer.getUniqueID()).getResponseTime() / 50.0);
        }
        catch (Exception ex) {}
        return this.predictPlayerLocation(target, pingTicks);
    }
    
    public Entity predictPlayerLocation(final EntityPlayer player, final int ticks) {
        if (this.playerPositions.containsKey(player)) {
            final List previousPositions = this.playerPositions.get(player);
            if (previousPositions.size() > 1) {
                final Vec3 origin = previousPositions.get(0);
                final List deltas = new ArrayList();
                Vec3 previous = origin;
                for (final Vec3 position : previousPositions) {
                    deltas.add(new Vec3(position.xCoord - previous.xCoord, position.yCoord - previous.yCoord, position.zCoord - previous.zCoord));
                    previous = position;
                }
                double x = 0.0;
                double y = 0.0;
                double z = 0.0;
                for (final Vec3 delta : deltas) {
                    x += delta.xCoord * 1.5;
                    y += delta.yCoord;
                    z += delta.zCoord * 1.5;
                }
                x /= deltas.size();
                y /= deltas.size();
                z /= deltas.size();
                final EntityPlayer simulated = new EntityOtherPlayerMP(AimBot.mc.theWorld, player.getGameProfile());
                simulated.noClip = false;
                simulated.setPosition(player.posX, player.posY + 0.5, player.posZ);
                for (int i = 0; i < ticks; ++i) {
                    simulated.moveEntity(x, y, z);
                }
                return simulated;
            }
        }
        return player;
    }
}
