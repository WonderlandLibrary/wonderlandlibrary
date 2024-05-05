package astronaut.modules.world;

import astronaut.Duckware;
import astronaut.events.EventUpdate;
import astronaut.modules.Category;
import astronaut.modules.Module;
import de.Hero.settings.Setting;
import eventapi.EventTarget;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
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
            if (Duckware.setmgr.getSettingByName("Mode").getValString().equalsIgnoreCase("Vanilla")) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            } else if (Duckware.setmgr.getSettingByName("Mode").getValString().equalsIgnoreCase("Flag")) {

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
        Duckware.setmgr.rSetting(new Setting("Mode", this, "Vanilla", options));
    }
}
