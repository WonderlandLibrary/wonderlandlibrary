package de.theBest.MythicX.modules.world;

import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.events.EventUpdate;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import de.Hero.settings.Setting;
import eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.CombatTracker;

import java.awt.*;
import java.util.ArrayList;

public class NoFall extends Module {
    private CombatTracker time1;

    public NoFall() {
        super("No Fall", Type.World, 0, Category.WORLD, Color.green, "Prevents you from getting fall damage");
    }
    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.fallDistance > 2f) {
            if (MythicX.setmgr.getSettingByName("NoFall").getValString().equalsIgnoreCase("Vanilla")) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            } else if (MythicX.setmgr.getSettingByName("NoFall").getValString().equalsIgnoreCase("Flag")) {

                mc.player.setPosition(mc.player.posX, mc.player.posY + 1, mc.player.posZ);
                mc.player.fallDistance = 0f;

                C03PacketPlayer packet = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ, true);
                mc.getNetHandler().addToSendQueue(packet);
            }
        }
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }

    public void setup(){
        ArrayList<String> options = new ArrayList<>();
        options.add("Vanilla");
        options.add("Flag");
        MythicX.setmgr.rSetting(new Setting("NoFall", this, "Vanilla", options));
    }
}
