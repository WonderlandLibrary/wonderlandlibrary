package xyz.northclient.features.events;

import xyz.northclient.features.Event;
import net.minecraft.network.play.client.C03PacketPlayer;

public class TeleportEvent extends Event {
    private C03PacketPlayer response;
    private double posX;
    private double posY;
    private double posZ;
    private float yaw;
    private float pitch;
    public TeleportEvent(final C03PacketPlayer response, final double posx, final double posy, final double posz, final float yaw, final float pitch) {
        this.response = response;
        this.posX = posx;
        this.posY = posy;
        this.posZ = posz;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
