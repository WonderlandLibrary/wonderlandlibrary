package astronaut.modules.combat;

import astronaut.Duckware;
import astronaut.events.EventReceivedPacket;
import astronaut.events.EventUpdate;
import astronaut.modules.Category;
import astronaut.modules.Module;
import de.Hero.settings.Setting;
import eventapi.EventManager;
import eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Type.Combat, 0, Category.COMBAT, Color.green, "No Knockback");
    }

    @EventTarget
    public void onUpdate(EventReceivedPacket e) {
        Packet<?> p = EventReceivedPacket.getPacket();
        if (p instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) p;
            if (packet.getEntityID() == mc.thePlayer.getEntityId() && Duckware.setmgr.getSettingByName("Hypixel").getValBoolean()) {
                packet.setMotionZ(0);
                packet.setMotionX(0);
                //e.setCancelled(true);
            }else {
                e.setCancelled(true);
            }
        }
        if (p instanceof S27PacketExplosion) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void setup() {
        Duckware.setmgr.rSetting(new Setting("Hypixel", this, false));
    }
}
