package intent.AquaDev.aqua.modules.combat;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventReceivedPacket;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.PlayerUtil;
import intent.AquaDev.aqua.utils.TimeUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity
extends Module {
    TimeUtil timeUtil = new TimeUtil();

    public Velocity() {
        super("Velocity", "Velocity", 0, Category.Combat);
        Aqua.setmgr.register(new Setting("Mode", (Module)this, "Cancel", new String[]{"CancelLongjump", "Intave", "Cancel"}));
    }

    public void setup() {
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event event) {
        if (event instanceof EventReceivedPacket) {
            S12PacketEntityVelocity packet;
            Packet p;
            if (Aqua.setmgr.getSetting("VelocityMode").getCurrentMode().equalsIgnoreCase("CancelLongjump")) {
                p = EventReceivedPacket.INSTANCE.getPacket();
                if (p instanceof S12PacketEntityVelocity && (packet = (S12PacketEntityVelocity)p).getEntityID() == Velocity.mc.thePlayer.getEntityId() && Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Gamster") && !Aqua.moduleManager.getModuleByName("Longjump").isToggled()) {
                    EventReceivedPacket.INSTANCE.setCancelled(true);
                }
                if (p instanceof S27PacketExplosion) {
                    EventReceivedPacket.INSTANCE.setCancelled(true);
                }
            }
            if (Aqua.setmgr.getSetting("VelocityMode").getCurrentMode().equalsIgnoreCase("Cancel")) {
                p = EventReceivedPacket.INSTANCE.getPacket();
                if (p instanceof S12PacketEntityVelocity && (packet = (S12PacketEntityVelocity)p).getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
                    EventReceivedPacket.INSTANCE.setCancelled(true);
                }
                if (p instanceof S27PacketExplosion) {
                    EventReceivedPacket.INSTANCE.setCancelled(true);
                }
            }
        }
        if (event instanceof EventUpdate && Aqua.setmgr.getSetting("VelocityMode").getCurrentMode().equalsIgnoreCase("Intave") && Velocity.mc.thePlayer.hurtTime != 0 && Velocity.mc.thePlayer.ticksExisted % 2 == 0 && !Velocity.mc.thePlayer.isBurning()) {
            PlayerUtil.setSpeed((double)0.0);
        }
    }
}
