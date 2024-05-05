// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import vip.Resolute.util.misc.AStarCustomPathFinder;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.item.EntityArmorStand;
import java.util.Iterator;
import vip.Resolute.util.render.RenderUtils;
import java.awt.Color;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.util.player.RotationUtils;
import java.util.Collections;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.entity.Entity;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.util.concurrent.CopyOnWriteArrayList;
import vip.Resolute.util.misc.TimerUtils;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;
import vip.Resolute.util.world.Vec3;
import java.util.ArrayList;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class TPAura extends Module
{
    public NumberSetting cps;
    public NumberSetting maxTargets;
    public NumberSetting range;
    private ArrayList<Vec3> path;
    private List<Vec3>[] test;
    private List<EntityLivingBase> targets;
    private TimerUtils cpstimer;
    public static TimerUtils timer;
    public static boolean canReach;
    int ticks;
    double startX;
    double startY;
    double startZ;
    private float lastHealth;
    double dashDistance;
    
    public TPAura() {
        super("TPAura", 0, "Allows for infinite aura range", Category.COMBAT);
        this.cps = new NumberSetting("APS", 5.0, 1.0, 20.0, 1.0);
        this.maxTargets = new NumberSetting("Max Targets", 1.0, 1.0, 5.0, 1.0);
        this.range = new NumberSetting("Range", 300.0, 50.0, 300.0, 10.0);
        this.path = new ArrayList<Vec3>();
        this.test = (List<Vec3>[])new ArrayList[50];
        this.targets = new CopyOnWriteArrayList<EntityLivingBase>();
        this.cpstimer = new TimerUtils();
        this.ticks = 0;
        this.lastHealth = 0.0f;
        this.dashDistance = 5.0;
        this.addSettings(this.cps, this.range, this.maxTargets);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion) {
            final EventMotion event = (EventMotion)e;
            if (e.isPre()) {
                this.targets = this.getTargets();
                if (this.cpstimer.hasTimeElapsed((long)(1000.0 / this.cps.getValue()), true) && this.targets.size() > 0) {
                    this.test = (List<Vec3>[])new ArrayList[50];
                    for (int i = 0; i < ((this.targets.size() > 1) ? this.maxTargets.getValue() : this.targets.size()); ++i) {
                        final EntityLivingBase T = this.targets.get(i);
                        if (TPAura.mc.thePlayer.getDistanceToEntity(T) > this.range.getValue()) {
                            return;
                        }
                        final Vec3 topFrom = new Vec3(TPAura.mc.thePlayer.posX, TPAura.mc.thePlayer.posY, TPAura.mc.thePlayer.posZ);
                        final Vec3 to = new Vec3(T.posX, T.posY, T.posZ);
                        this.path = this.computePath(topFrom, to);
                        this.test[i] = this.path;
                        for (final Vec3 pathElm : this.path) {
                            TPAura.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
                        }
                        TPAura.mc.thePlayer.swingItem();
                        TPAura.mc.playerController.attackEntity(TPAura.mc.thePlayer, T);
                        Collections.reverse(this.path);
                        for (final Vec3 pathElm : this.path) {
                            TPAura.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
                        }
                        final float[] rots = RotationUtils.getRotations(T);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                    }
                    this.cpstimer.reset();
                }
            }
        }
        if (e instanceof EventRender3D && this.targets.size() > 0) {
            for (int j = 0; j < this.path.size(); ++j) {
                final Vec3 pathElm2 = this.path.get(j);
                final Vec3 pathOther = this.path.get((j < this.path.size() - 1) ? (j + 1) : j);
                final double n = pathElm2.getX() + (pathElm2.getX() - pathElm2.getX()) * TPAura.mc.timer.renderPartialTicks;
                TPAura.mc.getRenderManager();
                final double x = n - RenderManager.renderPosX;
                final double n2 = pathElm2.getY() + (pathElm2.getY() - pathElm2.getY()) * TPAura.mc.timer.renderPartialTicks;
                TPAura.mc.getRenderManager();
                final double y = n2 - RenderManager.renderPosY;
                final double n3 = pathElm2.getZ() + (pathElm2.getZ() - pathElm2.getZ()) * TPAura.mc.timer.renderPartialTicks;
                TPAura.mc.getRenderManager();
                final double z = n3 - RenderManager.renderPosZ;
                final double n4 = pathOther.getX() + (pathOther.getX() - pathOther.getX()) * TPAura.mc.timer.renderPartialTicks;
                TPAura.mc.getRenderManager();
                final double x2 = n4 - RenderManager.renderPosX;
                final double n5 = pathOther.getY() + (pathOther.getY() - pathOther.getY()) * TPAura.mc.timer.renderPartialTicks;
                TPAura.mc.getRenderManager();
                final double y2 = n5 - RenderManager.renderPosY;
                final double n6 = pathOther.getZ() + (pathOther.getZ() - pathOther.getZ()) * TPAura.mc.timer.renderPartialTicks;
                TPAura.mc.getRenderManager();
                final double z2 = n6 - RenderManager.renderPosZ;
                final AxisAlignedBB var12 = new AxisAlignedBB(x2, y2, z2, x, y, z);
                RenderUtils.glColor(new Color(255, 255, 255).getRGB());
                RenderUtils.drawOutlinedBoundingBox(var12);
            }
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.startX = TPAura.mc.thePlayer.posX;
        this.startY = TPAura.mc.thePlayer.posY;
        this.startZ = TPAura.mc.thePlayer.posZ;
    }
    
    public boolean canAttack(final EntityLivingBase player) {
        return player != TPAura.mc.thePlayer && !(player instanceof EntityArmorStand) && player instanceof EntityPlayer;
    }
    
    private List<EntityLivingBase> getTargets() {
        final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (final Object o3 : TPAura.mc.theWorld.getLoadedEntityList()) {
            if (o3 instanceof EntityLivingBase) {
                final EntityLivingBase entity = (EntityLivingBase)o3;
                if (!this.canAttack(entity)) {
                    continue;
                }
                targets.add(entity);
            }
        }
        targets.sort((o1, o2) -> (int)(o1.getDistanceToEntity(TPAura.mc.thePlayer) * 1000.0f - o2.getDistanceToEntity(TPAura.mc.thePlayer) * 1000.0f));
        return targets;
    }
    
    private ArrayList<Vec3> computePath(Vec3 topFrom, final Vec3 to) {
        if (!this.canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0.0, 1.0, 0.0);
        }
        final AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
        pathfinder.compute();
        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        final ArrayList<Vec3> path = new ArrayList<Vec3>();
        final ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
        for (final Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0.0, 0.5));
                lastDashLoc = pathElm;
            }
            else {
                boolean canContinue = true;
                Label_0356: {
                    if (pathElm.squareDistanceTo(lastDashLoc) > this.dashDistance * this.dashDistance) {
                        canContinue = false;
                    }
                    else {
                        final double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                        final double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                        final double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                        final double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                        final double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                        final double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                        for (int x = (int)smallX; x <= bigX; ++x) {
                            for (int y = (int)smallY; y <= bigY; ++y) {
                                for (int z = (int)smallZ; z <= bigZ; ++z) {
                                    if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
                                        canContinue = false;
                                        break Label_0356;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }
    
    private boolean canPassThrow(final BlockPos pos) {
        final Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }
    
    static {
        TPAura.timer = new TimerUtils();
    }
}
