package de.theBest.MythicX.modules.combat;

import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.events.EventReceivedPacket;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import de.Hero.settings.Setting;
import eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

import java.awt.*;
import java.util.ArrayList;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Type.Combat, 0, Category.COMBAT, Color.green, "No Knockback");
    }

    @EventTarget
    public void onUpdate(EventReceivedPacket e) {
        Packet<?> p = EventReceivedPacket.getPacket();
        if (p instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) p;
            if (packet.getEntityID() == mc.thePlayer.getEntityId() && MythicX.setmgr.getSettingByName("Velocity").getValString().equalsIgnoreCase("Hypixel")) {
                packet.setMotionZ(0);
                packet.setMotionX(0);
                //e.setCancelled(true);
            }else if(packet.getEntityID() == mc.thePlayer.getEntityId() && MythicX.setmgr.getSettingByName("Velocity").getValString().equalsIgnoreCase("Cancel")) {
                e.setCancelled(true);
            }else if(packet.getEntityID() == mc.thePlayer.getEntityId() && MythicX.setmgr.getSettingByName("Velocity").getValString().equalsIgnoreCase("Jump")){
                if(mc.thePlayer.hurtTime != 0){
                    mc.thePlayer.jump();
                }
            } else if (packet.getEntityID() == mc.thePlayer.getEntityId() && MythicX.setmgr.getSettingByName("Velocity").getValString().equalsIgnoreCase("Gomme")) {
                if(mc.thePlayer.hurtTime >= 3){
                    mc.thePlayer.jump();
                }
            }
        }
        if (p instanceof S27PacketExplosion) {
            if(MythicX.setmgr.getSettingByName("Velocity").getValString().equalsIgnoreCase("Cancel")) {
                e.setCancelled(true);
            }
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
        ArrayList<String> options = new ArrayList<>();
        options.add("Cancel");
        options.add("Hypixel");
        options.add("Jump");
        options.add("Gomme");
        MythicX.setmgr.rSetting(new Setting("Velocity", this, "Jump", options));
    }
}
