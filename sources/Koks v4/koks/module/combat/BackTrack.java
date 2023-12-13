package koks.module.combat;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.TimeHelper;
import koks.event.PacketEvent;
import koks.event.Render3DEvent;
import koks.event.TickEvent;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.server.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.util.ArrayList;

import static koks.mainmenu.interfaces.Element.renderUtil;

@Module.Info(name = "Backtrack", description = "You can hit entities at their old position", category = Module.Category.COMBAT)
public class BackTrack extends Module {

    private final ArrayList<Packet<INetHandler>> packets = new ArrayList<>();

    @Value(name = "Delay", minimum = 0, maximum = 20000)
    public int delay = 450;

    @Value(name = "MaxHitRange", minimum = 0, maximum = 6)
    public double maxHitRange = 6;

    @Value(name = "OnlyWhenNeed")
    public boolean onlyWhenNeed = false;

    @Value(name = "Esp")
    public boolean esp = true;

    @Value(name = "Color", colorPicker = true)
    public int color = Color.red.getRGB();

    @Value(name = "CancelVelocity")
    public boolean velocity = true;

    @Value(name = "CancelKeepAlive")
    public boolean keepAlive = true;

    @Value(name = "CancelTimeUpdate")
    public boolean timeUpdate = true;

    private EntityLivingBase entity = null;
    private INetHandler packetListener = null;
    private KillAura killAura;
    private WorldClient lastWorld;
    private final TimeHelper timeHelper = new TimeHelper();

    @Override
    @Event.Info(priority = Event.Priority.EXTREME)
    public void onEvent(Event event) {
        if (event instanceof PacketEvent packetEvent) {
            if (packetEvent.getINetHandler() != null && packetEvent.getINetHandler() instanceof OldServerPinger) return;
            if (mc.theWorld != null)
                if (packetEvent.getType() == PacketEvent.Type.RECEIVE) {
                    this.packetListener = packetEvent.getINetHandler();
                    synchronized (BackTrack.class) {
                        final Packet<?> p = packetEvent.getPacket();
                        if (p instanceof S14PacketEntity packetEntity) {
                            final Entity entity = getWorld().getEntityByID(packetEntity.getEntityId());
                            if (entity instanceof EntityLivingBase entityLivingBase) {
                                entityLivingBase.realPosX += packetEntity.func_149062_c();
                                entityLivingBase.realPosY += packetEntity.func_149061_d();
                                entityLivingBase.realPosZ += packetEntity.func_149064_e();
                            }
                        }
                        if (p instanceof S18PacketEntityTeleport teleportPacket) {
                            final Entity entity = getWorld().getEntityByID(teleportPacket.getEntityId());
                            if (entity instanceof EntityLivingBase entityLivingBase) {
                                entityLivingBase.realPosX = teleportPacket.getX();
                                entityLivingBase.realPosY = teleportPacket.getY();
                                entityLivingBase.realPosZ = teleportPacket.getZ();
                            }
                        }

                        this.entity = null;
                        if (this.killAura.isToggled()) {
                            this.entity = KillAura.curEntity;
                        }
                        if (this.entity == null) {
                            this.resetPackets(packetEvent.getINetHandler());
                            return;
                        }
                        if (getWorld() != null && getPlayer() != null) {
                            if (this.lastWorld != getWorld()) {
                                resetPackets(packetEvent.getINetHandler());
                                this.lastWorld = getWorld();
                                return;
                            }
                            this.addPackets(p, packetEvent);
                        }
                        this.lastWorld = getWorld();
                    }
                }
        }

        if (event instanceof TickEvent) {
            if (entity != null && getPlayer() != null && this.packetListener != null && getWorld() != null) {
                double d0 = (double) this.entity.realPosX / 32.0D;
                double d1 = (double) this.entity.realPosY / 32.0D;
                double d2 = (double) this.entity.realPosZ / 32.0D;
                double d3 = (double) this.entity.serverPosX / 32.0D;
                double d4 = (double) this.entity.serverPosY / 32.0D;
                double d5 = (double) this.entity.serverPosZ / 32.0D;
                AxisAlignedBB alignedBB = new AxisAlignedBB(d3 - (double) this.entity.width, d4, d5 - (double) this.entity.width, d3 + (double) this.entity.width, d4 + (double) this.entity.height, d5 + (double) this.entity.width);
                Vec3 positionEyes = getPlayer().getPositionEyes(getTimer().renderPartialTicks);
                double currentX = MathHelper.clamp_double(positionEyes.xCoord, alignedBB.minX, alignedBB.maxX);
                double currentY = MathHelper.clamp_double(positionEyes.yCoord, alignedBB.minY, alignedBB.maxY);
                double currentZ = MathHelper.clamp_double(positionEyes.zCoord, alignedBB.minZ, alignedBB.maxZ);
                AxisAlignedBB alignedBB2 = new AxisAlignedBB(d0 - (double) this.entity.width, d1, d2 - (double) this.entity.width, d0 + (double) this.entity.width, d1 + (double) this.entity.height, d2 + (double) this.entity.width);
                double realX = MathHelper.clamp_double(positionEyes.xCoord, alignedBB2.minX, alignedBB2.maxX);
                double realY = MathHelper.clamp_double(positionEyes.yCoord, alignedBB2.minY, alignedBB2.maxY);
                double realZ = MathHelper.clamp_double(positionEyes.zCoord, alignedBB2.minZ, alignedBB2.maxZ);
                double distance = this.maxHitRange;
                if (!this.getPlayer().canEntityBeSeen(this.entity)) {
                    distance = distance > 3 ? 3 : distance;
                }
                double bestX = MathHelper.clamp_double(positionEyes.xCoord, this.entity.getEntityBoundingBox().minX, this.entity.getEntityBoundingBox().maxX);
                double bestY = MathHelper.clamp_double(positionEyes.yCoord, this.entity.getEntityBoundingBox().minY, this.entity.getEntityBoundingBox().maxY);
                double bestZ = MathHelper.clamp_double(positionEyes.zCoord, this.entity.getEntityBoundingBox().minZ, this.entity.getEntityBoundingBox().maxZ);
                boolean b = false;
                if (positionEyes.distanceTo(new Vec3(bestX, bestY, bestZ)) > 2.9 || (getPlayer().hurtTime < 8 && getPlayer().hurtTime > 1)) {
                    b = true;
                }
                if (!this.onlyWhenNeed) {
                    b = true;
                }
                if (!(b && positionEyes.distanceTo(new Vec3(realX, realY, realZ)) > positionEyes.distanceTo(new Vec3(currentX, currentY, currentZ)) + 0.05) || !(getPlayer().getDistance(d0, d1, d2) < distance) || this.timeHelper.hasReached((long) this.delay)) {
                    this.resetPackets(this.packetListener);
                    this.timeHelper.reset();
                }
            }
        }

        if (event instanceof Render3DEvent && this.esp) {
            if (this.entity != null && this.entity != getPlayer() && !this.entity.isInvisible()) {
                try {
                    final double x = this.entity.realPosX / 32D - mc.getRenderManager().renderPosX;
                    final double y = this.entity.realPosY / 32D - mc.getRenderManager().renderPosY;
                    final double z = this.entity.realPosZ / 32D - mc.getRenderManager().renderPosZ;

                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y + this.entity.height, z);
                    GL11.glNormal3d(0.0, 1.0, 0.0);
                    GL11.glRotated(90, 1, 0, 0);

                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glEnable(GL11.GL_BLEND);

                    GL11.glDisable(GL11.GL_DEPTH_TEST);

                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glLineWidth(1);
                    renderUtil.setColor(new Color(this.color));
                    final Cylinder cylinder = new Cylinder();

                    cylinder.setDrawStyle(GLU.GLU_LINE);
                    cylinder.setOrientation(GLU.GLU_INSIDE);
                    cylinder.draw(0.62f, 0.62f, this.entity.height, 8, 1);

                    renderUtil.setColor(renderUtil.getAlphaColor(new Color(this.color), 150));
                    cylinder.setDrawStyle(GLU.GLU_FILL);
                    cylinder.setOrientation(GLU.GLU_INSIDE);
                    cylinder.draw(0.62f, 0.65f, this.entity.height, 8, 1);

                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glPopMatrix();
                } catch (NullPointerException exception) {
                    System.out.println("IntelliJ logic moment, crash nullpointer with nullcheck :5head:");
                }
            }
        }
    }

    @Override
    public void onEnable() {
        this.killAura = ModuleRegistry.getModule(KillAura.class);
    }

    @Override
    public void onDisable() {

    }

    private void resetPackets(INetHandler netHandler) {
        if (this.packets.size() > 0) {
            synchronized (this.packets) {
                while (this.packets.size() != 0) {
                    try {
                        this.packets.get(0).processPacket(netHandler);
                    } catch (Exception ignored) {
                    }
                    this.packets.remove(this.packets.get(0));
                }

            }
        }
    }

    private void addPackets(Packet packet, Event eventReadPacket) {
        synchronized (this.packets) {
            if (this.blockPacket(packet)) {
                this.packets.add(packet);
                eventReadPacket.setCanceled(true);
            }
        }
    }

    private boolean blockPacket(Packet packet) {
        if (packet instanceof S03PacketTimeUpdate) {
            return this.timeUpdate;
        } else if (packet instanceof S00PacketKeepAlive) {
            return this.keepAlive;
        } else if (packet instanceof S12PacketEntityVelocity || packet instanceof S27PacketExplosion) {
            return this.velocity;
        } else {
            return packet instanceof S32PacketConfirmTransaction || packet instanceof S14PacketEntity || packet instanceof S19PacketEntityStatus || packet instanceof S19PacketEntityHeadLook || packet instanceof S18PacketEntityTeleport || packet instanceof S0FPacketSpawnMob;
        }
    }
}
