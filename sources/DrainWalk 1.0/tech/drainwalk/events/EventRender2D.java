package tech.drainwalk.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.Packet;

public class EventRender2D implements Event {

    private ScaledResolution sr;
    private Packet<?> packet;

    public EventRender2D(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public EventRender2D(ScaledResolution sr) {
        this.sr = sr;
    }

    public ScaledResolution getScaledResolution() {
        return sr;
    }

    public void setScaledResolution(ScaledResolution sr) {
        this.sr = sr;
    }
}
