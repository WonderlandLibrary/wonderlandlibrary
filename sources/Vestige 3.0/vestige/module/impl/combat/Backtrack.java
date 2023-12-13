package vestige.module.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.*;
import net.minecraft.util.MovingObjectPosition;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.PacketReceiveEvent;
import vestige.event.impl.PostMotionEvent;
import vestige.handler.packet.DelayedPacket;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.DoubleSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.util.player.PendingVelocity;

import java.util.concurrent.CopyOnWriteArrayList;

public class Backtrack extends Module {

    private final IntegerSetting delay = new IntegerSetting("Delay", 500, 100, 2000, 50);
    private final DoubleSetting minRange = new DoubleSetting("Min range", 2.8, 1, 6, 0.1);

    private final BooleanSetting delayPing = new BooleanSetting("Delay ping", true);
    private final BooleanSetting delayVelocity = new BooleanSetting("Delay velocity", () -> delayPing.isEnabled(), true);

    private final CopyOnWriteArrayList<DelayedPacket> delayedPackets = new CopyOnWriteArrayList<>();

    private Killaura killauraModule;

    private EntityLivingBase lastTarget;

    private EntityLivingBase lastCursorTarget;

    private int cursorTargetTicks;

    private PendingVelocity lastVelocity;

    public Backtrack() {
        super("Backtrack", Category.COMBAT);
        this.addSettings(delay, minRange, delayPing, delayVelocity);
    }

    @Override
    public void onClientStarted() {
        killauraModule = Vestige.instance.getModuleManager().getModule(Killaura.class);
    }

    @Listener
    public void onReceive(PacketReceiveEvent event) {
        if(mc.thePlayer == null || mc.thePlayer.ticksExisted < 5) {
            if(!delayedPackets.isEmpty()) {
                delayedPackets.clear();
            }
        }

        EntityLivingBase currentTarget = getCurrentTarget();

        if(currentTarget != lastTarget) {
            clearPackets();
        }

        if(currentTarget == null) {
            clearPackets();
        } else {
            if(event.getPacket() instanceof S14PacketEntity) {
                S14PacketEntity packet = event.getPacket();

                if(packet.getEntity(mc.getNetHandler().clientWorldController) == currentTarget) {
                    int x = currentTarget.serverPosX + packet.getX();
                    int y = currentTarget.serverPosY + packet.getY();
                    int z = currentTarget.serverPosZ + packet.getZ();

                    double posX = (double) x / 32.0D;
                    double posY = (double) y / 32.0D;
                    double posZ = (double) z / 32.0D;

                    if(killauraModule.getDistanceCustomPosition(posX, posY, posZ, currentTarget.getEyeHeight()) >= minRange.getValue()) {
                        event.setCancelled(true);
                        delayedPackets.add(new DelayedPacket(packet));
                    }
                }
            } else if(event.getPacket() instanceof S18PacketEntityTeleport) {
                S18PacketEntityTeleport packet = event.getPacket();

                if(packet.getEntityId() == currentTarget.getEntityId()) {
                    double serverX = packet.getX();
                    double serverY = packet.getY();
                    double serverZ = packet.getZ();

                    double d0 = (double) serverX / 32.0D;
                    double d1 = (double) serverY / 32.0D;
                    double d2 = (double) serverZ / 32.0D;

                    double x, y, z;

                    if(Math.abs(serverX - d0) < 0.03125D && Math.abs(serverY - d1) < 0.015625D && Math.abs(serverZ - d2) < 0.03125D) {
                        x = currentTarget.posX;
                        y = currentTarget.posY;
                        z = currentTarget.posZ;
                    } else {
                        x = d0;
                        y = d1;
                        z = d2;
                    }

                    if(killauraModule.getDistanceCustomPosition(x, y, z, currentTarget.getEyeHeight()) >= minRange.getValue()) {
                        event.setCancelled(true);
                        delayedPackets.add(new DelayedPacket(packet));
                    }
                }
            } else if(event.getPacket() instanceof S32PacketConfirmTransaction || event.getPacket() instanceof S00PacketKeepAlive) {
                if(!delayedPackets.isEmpty() && delayPing.isEnabled()) {
                    event.setCancelled(true);
                    delayedPackets.add(new DelayedPacket(event.getPacket()));
                }
            } else if(event.getPacket() instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity packet = event.getPacket();

                if(packet.getEntityID() == mc.thePlayer.getEntityId()) {
                    if(!delayedPackets.isEmpty() && delayPing.isEnabled() && delayVelocity.isEnabled()) {
                        event.setCancelled(true);
                        lastVelocity = new PendingVelocity(packet.getMotionX() / 8000.0, packet.getMotionY() / 8000.0, packet.getMotionZ() / 8000.0);
                    }
                }
            }
        }

        lastTarget = currentTarget;
    }

    @Listener
    public void onPostMotion(PostMotionEvent event) {
        updatePackets();
    }

    public EntityLivingBase getCurrentTarget() {
        if(killauraModule == null) {
            killauraModule = Vestige.instance.getModuleManager().getModule(Killaura.class);
        }

        if(killauraModule.isEnabled() && killauraModule.getTarget() != null) {
            return killauraModule.getTarget();
        } else if(mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            lastCursorTarget = (EntityLivingBase) mc.objectMouseOver.entityHit;

            return (EntityLivingBase) mc.objectMouseOver.entityHit;
        } else if(lastCursorTarget != null) {
            if(++cursorTargetTicks > 10) {
                lastCursorTarget = null;
            } else {
                return lastCursorTarget;
            }
        }

        return null;
    }

    public void updatePackets() {
        if(!delayedPackets.isEmpty()) {
            for(DelayedPacket p : delayedPackets) {
                if(p.getTimer().getTimeElapsed() >= delay.getValue()) {
                    clearPackets();

                    if(lastVelocity != null) {
                        mc.thePlayer.motionX = lastVelocity.getX();
                        mc.thePlayer.motionY = lastVelocity.getY();
                        mc.thePlayer.motionZ = lastVelocity.getZ();
                        lastVelocity = null;
                    }

                    return;
                }
            }
        }

        /*
        if(!delayedPackets.isEmpty()) {
            ArrayList<DelayedPacket> toRemove = new ArrayList<>();

            for(DelayedPacket p : delayedPackets) {
                if(p.getTimer().getTimeElapsed() >= delay.getValue()) {
                    handlePacket(p.getPacket());
                    toRemove.add(p);
                }
            }

            if(!toRemove.isEmpty()) {
                for(DelayedPacket p : toRemove) {
                    delayedPackets.remove(p);
                }
            }

            toRemove.clear();
        }
        */
    }

    public void clearPackets() {
        if(lastVelocity != null) {
            mc.thePlayer.motionX = lastVelocity.getX();
            mc.thePlayer.motionY = lastVelocity.getY();
            mc.thePlayer.motionZ = lastVelocity.getZ();
            lastVelocity = null;
        }

        if(!delayedPackets.isEmpty()) {
            for(DelayedPacket p : delayedPackets) {
                handlePacket(p.getPacket());
            }
            delayedPackets.clear();
        }
    }

    public void handlePacket(Packet packet) {
        if(packet instanceof S14PacketEntity) {
            handleEntityMovement((S14PacketEntity) packet);
        } else if(packet instanceof S18PacketEntityTeleport) {
            handleEntityTeleport((S18PacketEntityTeleport) packet);
        } else if(packet instanceof S32PacketConfirmTransaction) {
            handleConfirmTransaction((S32PacketConfirmTransaction) packet);
        } else if(packet instanceof S00PacketKeepAlive) {
            mc.getNetHandler().handleKeepAlive((S00PacketKeepAlive) packet);
        }
    }

    public void handleEntityMovement(S14PacketEntity packetIn) {
        Entity entity = packetIn.getEntity(mc.getNetHandler().clientWorldController);

        if (entity != null) {
            entity.serverPosX += packetIn.getX();
            entity.serverPosY += packetIn.getY();
            entity.serverPosZ += packetIn.getZ();
            double d0 = (double) entity.serverPosX / 32.0D;
            double d1 = (double) entity.serverPosY / 32.0D;
            double d2 = (double) entity.serverPosZ / 32.0D;
            float f = packetIn.func_149060_h() ? (float) (packetIn.getYaw() * 360) / 256.0F : entity.rotationYaw;
            float f1 = packetIn.func_149060_h() ? (float) (packetIn.getPitch() * 360) / 256.0F : entity.rotationPitch;
            entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, false);
            entity.onGround = packetIn.getOnGround();
        }
    }

    public void handleEntityTeleport(S18PacketEntityTeleport packetIn) {
        Entity entity = mc.getNetHandler().clientWorldController.getEntityByID(packetIn.getEntityId());

        if (entity != null) {
            entity.serverPosX = packetIn.getX();
            entity.serverPosY = packetIn.getY();
            entity.serverPosZ = packetIn.getZ();
            double d0 = (double)entity.serverPosX / 32.0D;
            double d1 = (double)entity.serverPosY / 32.0D;
            double d2 = (double)entity.serverPosZ / 32.0D;
            float f = (float)(packetIn.getYaw() * 360) / 256.0F;
            float f1 = (float)(packetIn.getPitch() * 360) / 256.0F;

            if (Math.abs(entity.posX - d0) < 0.03125D && Math.abs(entity.posY - d1) < 0.015625D && Math.abs(entity.posZ - d2) < 0.03125D) {
                entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ, f, f1, 3, true);
            } else {
                entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, true);
            }

            entity.onGround = packetIn.getOnGround();
        }
    }

    public void handleConfirmTransaction(S32PacketConfirmTransaction packetIn) {
        Container container = null;
        EntityPlayer entityplayer = mc.thePlayer;

        if (packetIn.getWindowId() == 0) {
            container = entityplayer.inventoryContainer;
        } else if (packetIn.getWindowId() == entityplayer.openContainer.windowId) {
            container = entityplayer.openContainer;
        }

        if (container != null && !packetIn.func_148888_e()) {
            mc.getNetHandler().addToSendQueue(new C0FPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
        }
    }

    public boolean isDelaying() {
        return this.isEnabled() && !delayedPackets.isEmpty();
    }

}
