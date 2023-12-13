package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.*;
import today.flux.Flux;
import today.flux.event.BBSetEvent;
import today.flux.event.MoveEvent;
import today.flux.event.PacketSendEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.implement.Movement.Speed;
import today.flux.module.value.FloatValue;

public class Freecam extends Module {

    public static FloatValue speed = new FloatValue("Freecam", "Speed", 1f, 0.1f, 3.0f, 0.1f);

    public Freecam() {
        super("Freecam", Category.Render, false);
    }

    private double oldX, oldY, oldZ;
    private float oldYaw, oldPitch;
    private EntityOtherPlayerMP player;

    public void onEnable() {
        if (this.mc.theWorld == null) {
            this.setEnabled(false);
            return;
        }

        this.mc.thePlayer.noClip = true;
        this.oldX = this.mc.thePlayer.posX;
        this.oldY = this.mc.thePlayer.posY;
        this.oldZ = this.mc.thePlayer.posZ;
        this.oldYaw = this.mc.thePlayer.rotationYaw;
        this.oldPitch = this.mc.thePlayer.rotationPitch;
        (player = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile()))
                .clonePlayer(this.mc.thePlayer, true);
        player.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
        player.rotationYawHead = this.mc.thePlayer.rotationYaw;
        player.rotationPitch = this.mc.thePlayer.rotationPitch;
        player.setSneaking(this.mc.thePlayer.isSneaking());
        this.mc.theWorld.addEntityToWorld(-1337, player);

        Flux.INSTANCE.getFriendManager().addFriend(player.getName());

        super.onEnable();
    }


    public void onDisable() {
        this.mc.thePlayer.noClip = false;
        this.mc.thePlayer.capabilities.isFlying = false;
        this.mc.thePlayer.setPositionAndRotation(oldX, oldY, oldZ, oldYaw, oldPitch);
        this.mc.theWorld.removeEntity(player);

        Flux.INSTANCE.getFriendManager().delFriend(player.getName());

        super.onDisable();
    }

    @EventTarget
    public void onBBSet(BBSetEvent event) {
        event.boundingBox = null;
    }


    @EventTarget
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C0BPacketEntityAction
                || event.getPacket() instanceof C0APacketAnimation || event.getPacket() instanceof C02PacketUseEntity
                || event.getPacket() instanceof C09PacketHeldItemChange || event.getPacket() instanceof C07PacketPlayerDigging) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onUpdate(MoveEvent event) {
        this.mc.thePlayer.noClip = true;
        event.x = 0;
        event.y = 0;
        event.z = 0;

        if (this.mc.gameSettings.keyBindSneak.pressed) {
            event.y = -1;
        } else if (this.mc.gameSettings.keyBindJump.pressed) {
            event.y = 1;
        }

        Speed.setMoveSpeed(event, speed.getValue());
    }
}
