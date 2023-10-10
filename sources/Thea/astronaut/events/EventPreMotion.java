package astronaut.events;



import eventapi.events.Event;
import net.minecraft.client.Minecraft;

public class EventPreMotion implements Event {
    public static EventPreMotion getInstance;
    private static float yaw;
    private static float pitch;

    public EventPreMotion(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        Minecraft.getMinecraft().thePlayer.rotationYawHead = yaw;
    }

    public static float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        Minecraft.getMinecraft().thePlayer.prevRotationPitchHead = pitch;
        Minecraft.getMinecraft().thePlayer.rotationPitchHead = pitch;
    }
}
