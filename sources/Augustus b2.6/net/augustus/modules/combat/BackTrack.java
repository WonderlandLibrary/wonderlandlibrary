// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.combat;

import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.augustus.utils.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.augustus.events.EventRender3D;
import net.augustus.modules.misc.MidClick;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.EnumPacketDirection;
import net.augustus.events.EventReadPacket;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import java.util.Comparator;
import java.util.function.Predicate;
import net.augustus.events.EventEarlyTick;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.augustus.settings.Setting;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.network.INetHandler;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class BackTrack extends Module
{
    private final TimeHelper timeHelper;
    private final ArrayList<Packet> packets;
    public AxisAlignedBB boundingBox;
    public boolean b;
    public boolean bb;
    public boolean aBoolean;
    public DoubleValue hitRange;
    public DoubleValue timerDelay;
    public BooleanValue esp;
    public BooleanValue onlyWhenNeed;
    public BooleanValue player;
    public BooleanValue mob;
    public BooleanValue animal;
    public BooleanValue villager;
    public BooleanValue armorStand;
    public BooleansSetting targets;
    public BooleanValue onlyKillAura;
    public DoubleValue range;
    public BooleanValue packetVelocity;
    public BooleanValue packetVelocityExplosion;
    public BooleanValue packetTimeUpdate;
    public BooleanValue packetKeepAlive;
    public BooleansSetting packetsToDelay;
    private EntityLivingBase entity;
    private boolean blockPackets;
    private WorldClient lastWorld;
    private INetHandler packetListener;
    
    public BackTrack() {
        super("BackTrack", new Color(110, 186, 9), Categorys.COMBAT);
        this.timeHelper = new TimeHelper();
        this.packets = new ArrayList<Packet>();
        this.hitRange = new DoubleValue(6, "MaxHitRange", this, 6.0, 3.0, 6.0, 2);
        this.timerDelay = new DoubleValue(7, "Time", this, 4000.0, 0.0, 30000.0, 0);
        this.esp = new BooleanValue(2, "Esp", this, true);
        this.onlyWhenNeed = new BooleanValue(19, "OnlyWhenNeed", this, true);
        this.player = new BooleanValue(9, "Player", this, true);
        this.mob = new BooleanValue(10, "Mob", this, true);
        this.animal = new BooleanValue(11, "Animal", this, true);
        this.villager = new BooleanValue(12, "Villager", this, true);
        this.armorStand = new BooleanValue(13, "ArmorStand", this, true);
        this.targets = new BooleansSetting(31, "Targets", this, new Setting[] { this.player, this.mob, this.animal, this.villager, this.armorStand });
        this.onlyKillAura = new BooleanValue(18, "OnlyKillAura", this, true);
        this.range = new DoubleValue(5, "PreAimRange", this, 4.0, 0.0, 15.0, 1);
        this.packetVelocity = new BooleanValue(14, "Velocity", this, true);
        this.packetVelocityExplosion = new BooleanValue(15, "ExplosionVelocity", this, true);
        this.packetTimeUpdate = new BooleanValue(16, "TimeUpdate", this, true);
        this.packetKeepAlive = new BooleanValue(17, "KeepAlive", this, true);
        this.packetsToDelay = new BooleansSetting(32, "PacketsToDelay", this, new Setting[] { this.packetVelocity, this.packetVelocityExplosion, this.packetTimeUpdate, this.packetKeepAlive });
        this.entity = null;
        this.packetListener = null;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.blockPackets = false;
        this.b = true;
        if (BackTrack.mc.theWorld != null && BackTrack.mc.thePlayer != null) {
            for (final Entity entity : BackTrack.mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityLivingBase) {
                    final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                    entityLivingBase.realPosX = entityLivingBase.serverPosX;
                    entityLivingBase.realPosZ = entityLivingBase.serverPosZ;
                    entityLivingBase.realPosY = entityLivingBase.serverPosY;
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (this.packets.size() > 0 && this.packetListener != null) {
            this.resetPackets(this.packetListener);
        }
        this.packets.clear();
    }
    
    @EventTarget
    public void onEventEarlyTick(final EventEarlyTick eventEarlyTick) {
        if (BackTrack.mm.killAura.isToggled()) {
            this.entity = BackTrack.mm.killAura.target;
        }
        else {
            final Object[] listOfTargets = BackTrack.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble(entityy -> BackTrack.mc.thePlayer.getDistanceToEntity(entityy))).toArray();
            if (listOfTargets.length > 0) {
                this.entity = (EntityLivingBase)listOfTargets[0];
            }
            if (this.onlyKillAura.getBoolean()) {
                this.entity = null;
            }
        }
        if (this.entity != null && BackTrack.mc.thePlayer != null && this.packetListener != null && BackTrack.mc.theWorld != null) {
            final double d0 = this.entity.realPosX / 32.0;
            final double d2 = this.entity.realPosY / 32.0;
            final double d3 = this.entity.realPosZ / 32.0;
            final double d4 = this.entity.serverPosX / 32.0;
            final double d5 = this.entity.serverPosY / 32.0;
            final double d6 = this.entity.serverPosZ / 32.0;
            final float f = this.entity.width / 2.0f;
            final AxisAlignedBB entityServerPos = new AxisAlignedBB(d4 - f, d5, d6 - f, d4 + f, d5 + this.entity.height, d6 + f);
            final Vec3 positionEyes = BackTrack.mc.thePlayer.getPositionEyes(BackTrack.mc.getTimer().renderPartialTicks);
            final double currentX = MathHelper.clamp_double(positionEyes.xCoord, entityServerPos.minX, entityServerPos.maxX);
            final double currentY = MathHelper.clamp_double(positionEyes.yCoord, entityServerPos.minY, entityServerPos.maxY);
            final double currentZ = MathHelper.clamp_double(positionEyes.zCoord, entityServerPos.minZ, entityServerPos.maxZ);
            final AxisAlignedBB entityPosMe = new AxisAlignedBB(d0 - f, d2, d3 - f, d0 + f, d2 + this.entity.height, d3 + f);
            final double realX = MathHelper.clamp_double(positionEyes.xCoord, entityPosMe.minX, entityPosMe.maxX);
            final double realY = MathHelper.clamp_double(positionEyes.yCoord, entityPosMe.minY, entityPosMe.maxY);
            final double realZ = MathHelper.clamp_double(positionEyes.zCoord, entityPosMe.minZ, entityPosMe.maxZ);
            double distance = this.hitRange.getValue();
            if (!BackTrack.mc.thePlayer.canEntityBeSeen(this.entity)) {
                distance = ((distance > 3.0) ? 3.0 : distance);
            }
            final double collision = this.entity.getCollisionBorderSize();
            final double width = BackTrack.mc.thePlayer.width / 2.0f;
            final double mePosXForPlayer = BackTrack.mc.thePlayer.getLastServerPosition().xCoord + (BackTrack.mc.thePlayer.getSeverPosition().xCoord - BackTrack.mc.thePlayer.getLastServerPosition().xCoord) / MathHelper.clamp_int(BackTrack.mc.thePlayer.rotIncrement, 1, 3);
            final double mePosYForPlayer = BackTrack.mc.thePlayer.getLastServerPosition().yCoord + (BackTrack.mc.thePlayer.getSeverPosition().yCoord - BackTrack.mc.thePlayer.getLastServerPosition().yCoord) / MathHelper.clamp_int(BackTrack.mc.thePlayer.rotIncrement, 1, 3);
            final double mePosZForPlayer = BackTrack.mc.thePlayer.getLastServerPosition().zCoord + (BackTrack.mc.thePlayer.getSeverPosition().zCoord - BackTrack.mc.thePlayer.getLastServerPosition().zCoord) / MathHelper.clamp_int(BackTrack.mc.thePlayer.rotIncrement, 1, 3);
            AxisAlignedBB mePosForPlayerBox = new AxisAlignedBB(mePosXForPlayer - width, mePosYForPlayer, mePosZForPlayer - width, mePosXForPlayer + width, mePosYForPlayer + BackTrack.mc.thePlayer.height, mePosZForPlayer + width);
            mePosForPlayerBox = mePosForPlayerBox.expand(collision, collision, collision);
            final Vec3 entityPosEyes = new Vec3(d4, d5 + this.entity.getEyeHeight(), d6);
            final double bestX = MathHelper.clamp_double(entityPosEyes.xCoord, mePosForPlayerBox.minX, mePosForPlayerBox.maxX);
            final double bestY = MathHelper.clamp_double(entityPosEyes.yCoord, mePosForPlayerBox.minY, mePosForPlayerBox.maxY);
            final double bestZ = MathHelper.clamp_double(entityPosEyes.zCoord, mePosForPlayerBox.minZ, mePosForPlayerBox.maxZ);
            boolean b = false;
            if (entityPosEyes.distanceTo(new Vec3(bestX, bestY, bestZ)) > 3.0 || (BackTrack.mc.thePlayer.hurtTime < 8 && BackTrack.mc.thePlayer.hurtTime > 3)) {
                b = true;
            }
            if (!this.onlyWhenNeed.getBoolean()) {
                b = true;
            }
            if (b && positionEyes.distanceTo(new Vec3(realX, realY, realZ)) > positionEyes.distanceTo(new Vec3(currentX, currentY, currentZ)) && BackTrack.mc.thePlayer.getSeverPosition().distanceTo(new Vec3(d0, d2, d3)) < distance && !this.timeHelper.reached((long)this.timerDelay.getValue())) {
                this.blockPackets = true;
            }
            else {
                this.blockPackets = false;
                this.resetPackets(this.packetListener);
                this.timeHelper.reset();
            }
        }
    }
    
    @EventTarget
    public synchronized void onEventReadPacket(final EventReadPacket eventReadPacket) {
        if (eventReadPacket.getNetHandler() != null) {
            this.packetListener = eventReadPacket.getNetHandler();
        }
        if (eventReadPacket.getDirection() != EnumPacketDirection.CLIENTBOUND) {
            return;
        }
        final Packet p = eventReadPacket.getPacket();
        if (p instanceof S08PacketPlayerPosLook) {
            this.resetPackets(eventReadPacket.getNetHandler());
        }
        if (p instanceof S14PacketEntity) {
            final S14PacketEntity packet = (S14PacketEntity)p;
            final Entity entity1 = BackTrack.mc.theWorld.getEntityByID(packet.getEntityId());
            if (entity1 instanceof EntityLivingBase) {
                final EntityLivingBase entityLivingBase2;
                final EntityLivingBase entityLivingBase = entityLivingBase2 = (EntityLivingBase)entity1;
                entityLivingBase2.realPosX += packet.func_149062_c();
                final EntityLivingBase entityLivingBase3 = entityLivingBase;
                entityLivingBase3.realPosY += packet.func_149061_d();
                final EntityLivingBase entityLivingBase4 = entityLivingBase;
                entityLivingBase4.realPosZ += packet.func_149064_e();
            }
        }
        if (p instanceof S18PacketEntityTeleport) {
            final S18PacketEntityTeleport packet2 = (S18PacketEntityTeleport)p;
            final Entity entity1 = BackTrack.mc.theWorld.getEntityByID(packet2.getEntityId());
            if (entity1 instanceof EntityLivingBase) {
                final EntityLivingBase entityLivingBase = (EntityLivingBase)entity1;
                entityLivingBase.realPosX = packet2.getX();
                entityLivingBase.realPosY = packet2.getY();
                entityLivingBase.realPosZ = packet2.getZ();
            }
        }
        if (this.entity == null) {
            this.resetPackets(eventReadPacket.getNetHandler());
            return;
        }
        if (BackTrack.mc.theWorld != null && BackTrack.mc.thePlayer != null) {
            if (this.lastWorld != BackTrack.mc.theWorld) {
                this.resetPackets(eventReadPacket.getNetHandler());
                this.lastWorld = BackTrack.mc.theWorld;
                return;
            }
            this.addPackets(p, eventReadPacket);
        }
        this.lastWorld = BackTrack.mc.theWorld;
    }
    
    private boolean canAttacked(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            if (entity.isInvisible()) {
                return false;
            }
            if (((EntityLivingBase)entity).deathTime > 1) {
                return false;
            }
            if (entity instanceof EntityArmorStand && !this.armorStand.getBoolean()) {
                return false;
            }
            if (entity instanceof EntityAnimal && !this.animal.getBoolean()) {
                return false;
            }
            if (entity instanceof EntityMob && !this.mob.getBoolean()) {
                return false;
            }
            if (entity instanceof EntityPlayer && !this.player.getBoolean()) {
                return false;
            }
            if (entity instanceof EntityVillager && !this.villager.getBoolean()) {
                return false;
            }
            if (entity.ticksExisted < 50) {
                return false;
            }
            if (entity instanceof EntityPlayer && BackTrack.mm.teams.isToggled() && BackTrack.mm.teams.getTeammates().contains(entity)) {
                return false;
            }
            if (entity instanceof EntityPlayer && (entity.getName().equals("§aShop") || entity.getName().equals("SHOP") || entity.getName().equals("UPGRADES"))) {
                return false;
            }
            if (entity.isDead) {
                return false;
            }
            if (entity instanceof EntityPlayer && BackTrack.mm.antiBot.isToggled() && AntiBot.bots.contains(entity)) {
                return false;
            }
            if (entity instanceof EntityPlayer && !BackTrack.mm.midClick.noFiends && MidClick.friends.contains(entity.getName())) {
                return false;
            }
        }
        return entity instanceof EntityLivingBase && entity != BackTrack.mc.thePlayer && BackTrack.mc.thePlayer.getDistanceToEntity(entity) < this.range.getValue();
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
        if (this.esp.getBoolean()) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            GlStateManager.disableCull();
            GL11.glDepthMask(false);
            if (this.entity != null && this.blockPackets) {
                this.render(this.entity);
            }
            GL11.glDepthMask(true);
            GlStateManager.enableCull();
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2848);
        }
    }
    
    private void render(final EntityLivingBase entity) {
        final float red = 0.0f;
        final float green = 1.1333333f;
        final float blue = 0.0f;
        float lineWidth = 3.0f;
        final float alpha = 0.03137255f;
        if (BackTrack.mc.thePlayer.getDistanceToEntity(entity) > 1.0f) {
            double d0 = 1.0f - BackTrack.mc.thePlayer.getDistanceToEntity(entity) / 20.0f;
            if (d0 < 0.3) {
                d0 = 0.3;
            }
            lineWidth *= (float)d0;
        }
        RenderUtil.drawEntityServerESP(entity, red, green, blue, alpha, 1.0f, lineWidth);
    }
    
    private void resetPackets(final INetHandler netHandler) {
        if (this.packets.size() > 0) {
            while (this.packets.size() != 0) {
                final Packet packet = this.packets.get(0);
                try {
                    if (packet != null) {
                        if (BackTrack.mm.velocity.isToggled() && (BackTrack.mm.velocity.mode.getSelected().equals("Spoof") || (BackTrack.mm.velocity.mode.getSelected().equals("Basic") && BackTrack.mm.velocity.XZValue.getValue() == 0.0 && BackTrack.mm.velocity.YValue.getValue() == 0.0))) {
                            if (!(packet instanceof S12PacketEntityVelocity)) {
                                if (!(packet instanceof S27PacketExplosion) || !BackTrack.mm.velocity.ignoreExplosion.getBoolean() || !BackTrack.mm.velocity.mode.getSelected().equals("Basic")) {
                                    packet.processPacket(netHandler);
                                }
                            }
                        }
                        else {
                            packet.processPacket(netHandler);
                        }
                    }
                }
                catch (ThreadQuickExitException ex) {}
                this.packets.remove(this.packets.get(0));
            }
        }
    }
    
    private void addPackets(final Packet packet, final EventReadPacket eventReadPacket) {
        synchronized (this.packets) {
            if (this.delayPackets(packet)) {
                this.aBoolean = true;
                this.packets.add(packet);
                eventReadPacket.setCanceled(true);
            }
        }
    }
    
    private boolean delayPackets(final Packet packet) {
        if (BackTrack.mc.currentScreen != null) {
            return false;
        }
        if (packet instanceof S03PacketTimeUpdate) {
            return this.packetTimeUpdate.getBoolean();
        }
        if (packet instanceof S00PacketKeepAlive) {
            return this.packetKeepAlive.getBoolean();
        }
        if (packet instanceof S12PacketEntityVelocity) {
            return this.packetVelocity.getBoolean();
        }
        if (packet instanceof S27PacketExplosion) {
            return this.packetVelocityExplosion.getBoolean();
        }
        if (packet instanceof S19PacketEntityStatus) {
            final S19PacketEntityStatus entityStatus = (S19PacketEntityStatus)packet;
            return entityStatus.getOpCode() != 2 || !(BackTrack.mc.theWorld.getEntityByID(entityStatus.getEntityId()) instanceof EntityLivingBase);
        }
        return !(packet instanceof S06PacketUpdateHealth) && !(packet instanceof S29PacketSoundEffect) && !(packet instanceof S3EPacketTeams) && !(packet instanceof S0CPacketSpawnPlayer);
    }
}
