package me.earth.phobos.features.modules.player;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.PushEvent;
import me.earth.phobos.features.Feature;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.MathUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Freecam extends Module {
    private static Freecam INSTANCE;

    static {
        Freecam.INSTANCE = new Freecam();
    }

    public Setting<Double> speed;
    public Setting<Boolean> view;
    public Setting<Boolean> packet;
    public Setting<Boolean> disable;
    public Setting<Boolean> legit;
    private AxisAlignedBB oldBoundingBox;
    private EntityOtherPlayerMP entity;
    private Vec3d position;
    private Entity riding;
    private float yaw;
    private float pitch;

    public Freecam() {
        super("Freecam", "Look around freely.", Category.PLAYER, true, false, false);
        this.speed = (Setting<Double>) this.register(new Setting("Speed", 0.5D, 0.1D, 5.0D));
        this.view = (Setting<Boolean>) this.register(new Setting("3D", false));
        this.packet = (Setting<Boolean>) this.register(new Setting("Packet", true));
        this.disable = (Setting<Boolean>) this.register(new Setting("Logout/Off", true));
        this.legit = (Setting<Boolean>) this.register(new Setting("Legit", false));
        this.setInstance();
    }

    public static Freecam getInstance() {
        if (Freecam.INSTANCE == null) {
            Freecam.INSTANCE = new Freecam();
        }
        return Freecam.INSTANCE;
    }

    private void setInstance() {
        Freecam.INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (!Feature.fullNullCheck()) {
            this.oldBoundingBox = Freecam.mc.player.getEntityBoundingBox();
            Freecam.mc.player.setEntityBoundingBox(new AxisAlignedBB(Freecam.mc.player.posX, Freecam.mc.player.posY, Freecam.mc.player.posZ, Freecam.mc.player.posX, Freecam.mc.player.posY, Freecam.mc.player.posZ));
            if (Freecam.mc.player.getRidingEntity() != null) {
                this.riding = Freecam.mc.player.getRidingEntity();
                Freecam.mc.player.dismountRidingEntity();
            }
            (this.entity = new EntityOtherPlayerMP(Freecam.mc.world, Freecam.mc.session.getProfile())).copyLocationAndAnglesFrom(Freecam.mc.player);
            this.entity.rotationYaw = Freecam.mc.player.rotationYaw;
            this.entity.rotationYawHead = Freecam.mc.player.rotationYawHead;
            this.entity.inventory.copyInventory(Freecam.mc.player.inventory);
            Freecam.mc.world.addEntityToWorld(69420, this.entity);
            this.position = Freecam.mc.player.getPositionVector();
            this.yaw = Freecam.mc.player.rotationYaw;
            this.pitch = Freecam.mc.player.rotationPitch;
            Freecam.mc.player.noClip = true;
        }
    }

    @Override
    public void onDisable() {
        if (!Feature.fullNullCheck()) {
            Freecam.mc.player.setEntityBoundingBox(this.oldBoundingBox);
            if (this.riding != null) {
                Freecam.mc.player.startRiding(this.riding, true);
            }
            if (this.entity != null) {
                Freecam.mc.world.removeEntity(this.entity);
            }
            if (this.position != null) {
                Freecam.mc.player.setPosition(this.position.x, this.position.y, this.position.z);
            }
            Freecam.mc.player.rotationYaw = this.yaw;
            Freecam.mc.player.rotationPitch = this.pitch;
            Freecam.mc.player.noClip = false;
        }
    }

    @Override
    public void onUpdate() {
        Freecam.mc.player.noClip = true;
        Freecam.mc.player.setVelocity(0.0, 0.0, 0.0);
        Freecam.mc.player.jumpMovementFactor = this.speed.getValue().floatValue();
        final double[] dir = MathUtil.directionSpeed(this.speed.getValue());
        if (Freecam.mc.player.movementInput.moveStrafe != 0.0f || Freecam.mc.player.movementInput.moveForward != 0.0f) {
            Freecam.mc.player.motionX = dir[0];
            Freecam.mc.player.motionZ = dir[1];
        } else {
            Freecam.mc.player.motionX = 0.0;
            Freecam.mc.player.motionZ = 0.0;
        }
        Freecam.mc.player.setSprinting(false);
        if (this.view.getValue() && !Freecam.mc.gameSettings.keyBindSneak.isKeyDown() && !Freecam.mc.gameSettings.keyBindJump.isKeyDown()) {
            Freecam.mc.player.motionY = this.speed.getValue() * -MathUtil.degToRad(Freecam.mc.player.rotationPitch) * Freecam.mc.player.movementInput.moveForward;
        }
        if (Freecam.mc.gameSettings.keyBindJump.isKeyDown()) {
            final EntityPlayerSP player = Freecam.mc.player;
            player.motionY += this.speed.getValue();
        }
        if (Freecam.mc.gameSettings.keyBindSneak.isKeyDown()) {
            final EntityPlayerSP player2 = Freecam.mc.player;
            player2.motionY -= this.speed.getValue();
        }
    }

    @Override
    public void onLogout() {
        if (this.disable.getValue()) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (this.legit.getValue() && this.entity != null && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packetPlayer = event.getPacket();
            packetPlayer.x = this.entity.posX;
            packetPlayer.y = this.entity.posY;
            packetPlayer.z = this.entity.posZ;
            return;
        }
        if (this.packet.getValue()) {
            if (event.getPacket() instanceof CPacketPlayer) {
                event.setCanceled(true);
            }
        } else if (!(event.getPacket() instanceof CPacketUseEntity) && !(event.getPacket() instanceof CPacketPlayerTryUseItem) && !(event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) && !(event.getPacket() instanceof CPacketPlayer) && !(event.getPacket() instanceof CPacketVehicleMove) && !(event.getPacket() instanceof CPacketChatMessage) && !(event.getPacket() instanceof CPacketKeepAlive)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSetPassengers) {
            final SPacketSetPassengers packet = event.getPacket();
            final Entity riding = Freecam.mc.world.getEntityByID(packet.getEntityId());
            if (riding != null && riding == this.riding) {
                this.riding = null;
            }
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet2 = event.getPacket();
            if (this.packet.getValue()) {
                if (this.entity != null) {
                    this.entity.setPositionAndRotation(packet2.getX(), packet2.getY(), packet2.getZ(), packet2.getYaw(), packet2.getPitch());
                }
                this.position = new Vec3d(packet2.getX(), packet2.getY(), packet2.getZ());
                Freecam.mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet2.getTeleportId()));
                event.setCanceled(true);
            } else {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPush(final PushEvent event) {
        if (event.getStage() == 1) {
            event.setCanceled(true);
        }
    }
}
