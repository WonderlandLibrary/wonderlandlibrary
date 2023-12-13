package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.util.EnumChatFormatting;
import today.flux.Flux;
import today.flux.event.PacketReceiveEvent;
import today.flux.event.TickEvent;
import today.flux.event.WorldRenderEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.ModeValue;

public class Ambience extends Module {
    public static ModeValue Mode = new ModeValue("Ambience", "Mode", "Darkness", "Sunset", "Day", "Sunrise");

    public Ambience() {
        super("Ambience", Category.Render, false);
    }

    @EventTarget
    public void onRenderWorld(WorldRenderEvent event) {
        if (Mode.getValue().equals("Darkness")) {
            this.mc.theWorld.setWorldTime(-18000);
        } else if (Mode.getValue().equals("Sunset")) {
            this.mc.theWorld.setWorldTime(-13000);
        } else if (Mode.getValue().equals("Day")) {
            this.mc.theWorld.setWorldTime(2000);
        } else if(Mode.getValue().equals("Sunrise")) {
            this.mc.theWorld.setWorldTime(22500);
        }
    }

    @EventTarget
    public void onRecv(PacketReceiveEvent event) {
        if (event.packet instanceof S03PacketTimeUpdate) {
            event.setCancelled(true);
        }
    }
}
