package intent.AquaDev.aqua.modules.combat;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventPacket;
import events.listeners.EventTick;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.RenderUtil;
import intent.AquaDev.aqua.utils.TimeUtil;
import java.util.ArrayList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Backtrack
extends Module {
    private final ArrayList<Packet> packets = new ArrayList();
    private EntityLivingBase entity = null;
    public static EntityPlayer target = null;
    private boolean blockPackets;
    private INetHandler packetListener = null;
    private WorldClient lastWorld;
    private final TimeUtil timeUtil = new TimeUtil();

    public Backtrack() {
        super("Backtrack", "Backtrack", 0, Category.Combat);
        Aqua.setmgr.register(new Setting("Range", (Module)this, 6.0, 3.0, 6.0, false));
        Aqua.setmgr.register(new Setting("BacktrackMS", (Module)this, 1000.0, 50.0, 2000.0, false));
    }

    public void onEnable() {
        this.blockPackets = false;
        this.packets.clear();
        super.onEnable();
    }

    public void onDisable() {
        this.packets.clear();
        super.onDisable();
    }

    public void onEvent(Event event) {
        if (event instanceof EventPacket) {
            EntityLivingBase entityLivingBase;
            S14PacketEntity packet;
            Entity entity1;
            EventPacket eventPacket = (EventPacket)event;
            if (eventPacket.getDirection() != EnumPacketDirection.CLIENTBOUND) {
                return;
            }
            this.packetListener = eventPacket.getNetHandler();
            Packet p = EventPacket.getPacket();
            if (p instanceof S14PacketEntity && (entity1 = Backtrack.mc.theWorld.getEntityByID((packet = (S14PacketEntity)EventPacket.getPacket()).getEntityId())) instanceof EntityLivingBase) {
                entityLivingBase = (EntityLivingBase)entity1;
                entityLivingBase.realPosX += packet.func_149062_c();
                entityLivingBase.realPosY += packet.func_149061_d();
                entityLivingBase.realPosZ += packet.func_149064_e();
            }
            if (p instanceof S18PacketEntityTeleport && (entity1 = Backtrack.mc.theWorld.getEntityByID((packet = (S18PacketEntityTeleport)EventPacket.getPacket()).getEntityId())) instanceof EntityLivingBase) {
                entityLivingBase = (EntityLivingBase)entity1;
                entityLivingBase.realPosX = packet.getX();
                entityLivingBase.realPosY = packet.getY();
                entityLivingBase.realPosZ = packet.getZ();
            }
            this.entity = target;
            if (this.entity == null) {
                this.resetPackets(eventPacket.getNetHandler());
                return;
            }
            if (Backtrack.mc.theWorld != null && Backtrack.mc.thePlayer != null) {
                if (this.lastWorld != Backtrack.mc.theWorld) {
                    this.resetPackets(eventPacket.getNetHandler());
                    this.lastWorld = Backtrack.mc.theWorld;
                    return;
                }
                this.addPackets(p, eventPacket);
            }
            this.lastWorld = Backtrack.mc.theWorld;
        }
        if (event instanceof EventTick) {
            target = Aqua.moduleManager.getModuleByName("Killaura").isToggled() ? this.searchTargets() : null;
            if (Backtrack.mc.thePlayer != null && this.packetListener != null && Backtrack.mc.theWorld != null) {
                if (this.entity == null) {
                    this.resetPackets(this.packetListener);
                    return;
                }
                double d0 = (double)this.entity.realPosX / 32.0;
                double d1 = (double)this.entity.realPosY / 32.0;
                double d2 = (double)this.entity.realPosZ / 32.0;
                double d3 = (double)this.entity.serverPosX / 32.0;
                double d4 = (double)this.entity.serverPosY / 32.0;
                double d5 = (double)this.entity.serverPosZ / 32.0;
                AxisAlignedBB alignedBB = new AxisAlignedBB(d3 - (double)this.entity.width, d4, d5 - (double)this.entity.width, d3 + (double)this.entity.width, d4 + (double)this.entity.height, d5 + (double)this.entity.width);
                Vec3 positionEyes = Backtrack.mc.thePlayer.getPositionEyes(Backtrack.mc.timer.renderPartialTicks);
                double currentX = MathHelper.clamp_double((double)positionEyes.xCoord, (double)alignedBB.minX, (double)alignedBB.maxX);
                double currentY = MathHelper.clamp_double((double)positionEyes.yCoord, (double)alignedBB.minY, (double)alignedBB.maxY);
                double currentZ = MathHelper.clamp_double((double)positionEyes.zCoord, (double)alignedBB.minZ, (double)alignedBB.maxZ);
                AxisAlignedBB alignedBB2 = new AxisAlignedBB(d0 - (double)this.entity.width, d1, d2 - (double)this.entity.width, d0 + (double)this.entity.width, d1 + (double)this.entity.height, d2 + (double)this.entity.width);
                double realX = MathHelper.clamp_double((double)positionEyes.xCoord, (double)alignedBB2.minX, (double)alignedBB2.maxX);
                double realY = MathHelper.clamp_double((double)positionEyes.yCoord, (double)alignedBB2.minY, (double)alignedBB2.maxY);
                double realZ = MathHelper.clamp_double((double)positionEyes.zCoord, (double)alignedBB2.minZ, (double)alignedBB2.maxZ);
                double distance = 6.0;
                if (!Backtrack.mc.thePlayer.canEntityBeSeen((Entity)this.entity)) {
                    distance = 3.0;
                }
                double bestX = MathHelper.clamp_double((double)positionEyes.xCoord, (double)this.entity.getEntityBoundingBox().minX, (double)this.entity.getEntityBoundingBox().maxX);
                double bestY = MathHelper.clamp_double((double)positionEyes.yCoord, (double)this.entity.getEntityBoundingBox().minY, (double)this.entity.getEntityBoundingBox().maxY);
                double bestZ = MathHelper.clamp_double((double)positionEyes.zCoord, (double)this.entity.getEntityBoundingBox().minZ, (double)this.entity.getEntityBoundingBox().maxZ);
                boolean b = false;
                Vec3 vec3 = new Vec3(bestX, bestY, bestZ);
                if (positionEyes.distanceTo(vec3) > 2.98) {
                    b = true;
                }
                float delayMS = (float)Aqua.setmgr.getSetting("BacktrackBacktrackMS").getCurrentNumber();
                if (!b || !(positionEyes.distanceTo(new Vec3(realX, realY, realZ)) > positionEyes.distanceTo(new Vec3(currentX, currentY, currentZ)) + 0.05) || !(Backtrack.mc.thePlayer.getDistance(d0, d1, d2) < distance) || this.timeUtil.hasReached((long)delayMS)) {
                    this.resetPackets(this.packetListener);
                    this.timeUtil.reset();
                }
            }
        }
    }

    private void resetPackets(INetHandler netHandler) {
        if (this.packets.size() > 0) {
            while (this.packets.size() != 0) {
                try {
                    ((Packet)this.packets.get(0)).processPacket(netHandler);
                }
                catch (ThreadQuickExitException threadQuickExitException) {
                    // empty catch block
                }
                this.packets.remove(this.packets.get(0));
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void addPackets(Packet packet, EventPacket eventPacket) {
        ArrayList<Packet> arrayList = this.packets;
        synchronized (arrayList) {
            if (this.blockPacket(packet)) {
                this.packets.add((Object)packet);
                eventPacket.setCancelled(true);
            }
        }
    }

    private boolean blockPacket(Packet packet) {
        if (packet instanceof S03PacketTimeUpdate) {
            return true;
        }
        if (packet instanceof S00PacketKeepAlive) {
            return true;
        }
        if (packet instanceof S12PacketEntityVelocity) {
            return true;
        }
        if (packet instanceof S27PacketExplosion) {
            return true;
        }
        return packet instanceof S32PacketConfirmTransaction || packet instanceof S14PacketEntity || packet instanceof S19PacketEntityHeadLook || packet instanceof S18PacketEntityTeleport || packet instanceof S0FPacketSpawnMob || packet instanceof S08PacketPlayerPosLook;
    }

    public EntityPlayer searchTargets() {
        float range = (float)Aqua.setmgr.getSetting("BacktrackRange").getCurrentNumber();
        EntityPlayer player = null;
        double closestDist = 100000.0;
        for (Entity o : Backtrack.mc.theWorld.loadedEntityList) {
            double dist;
            if (o.getName().equals((Object)Backtrack.mc.thePlayer.getName()) || !(o instanceof EntityPlayer) || !(Backtrack.mc.thePlayer.getDistanceToEntity(o) < range) || !((dist = (double)Backtrack.mc.thePlayer.getDistanceToEntity(o)) < closestDist)) continue;
            closestDist = dist;
            player = (EntityPlayer)o;
        }
        return player;
    }

    private void render(EntityLivingBase entity) {
        float red = 0.0f;
        float green = 1.1333333f;
        float blue = 0.0f;
        float lineWidth = 3.0f;
        float alpha = 0.03137255f;
        if (Backtrack.mc.thePlayer.getDistanceToEntity((Entity)entity) > 1.0f) {
            double d0 = 1.0f - Backtrack.mc.thePlayer.getDistanceToEntity((Entity)entity) / 20.0f;
            if (d0 < 0.3) {
                d0 = 0.3;
            }
            lineWidth = (float)((double)lineWidth * d0);
        }
        RenderUtil.drawEntityServerESP((Entity)entity, (float)red, (float)green, (float)blue, (float)alpha, (float)1.0f, (float)1.0f);
    }
}
