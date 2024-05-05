package artemis.mods.Combat;

 
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import artemis.Artemis;
import artemis.Category;
import artemis.module;
import artemis.events.EventReceivePacket;
import artemis.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
 
public class Antibot extends module {
	String mode = Artemis.instance.settingsManager.getSettingByName("AntiBot Mode").getValString();
	
    public Antibot() {
        super("Antibot", "Removes anticheat bots from game", Keyboard.KEY_TAB, 1, Category.OTHER);
    }
    
    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<String>();
        options.add("Advanced");
        options.add("Watchdog");
        Artemis.instance.settingsManager.rSetting(new Setting("AntiBot Mode", this, "Advanced", options));
    }

 
    public void onDisable() {
        super.onDisable();
      }
    
    public void onReceivePacket(EventReceivePacket event) {

        if(mode.equalsIgnoreCase("Advanced") && event.getPacket() instanceof S0CPacketSpawnPlayer) {
            S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer)event.getPacket();
            double posX = packet.func_148942_f() / 32D;
            double posY = packet.func_148949_g() / 32D;
            double posZ = packet.func_148946_h() / 32D;

            double diffX = mc.thePlayer.posX - posX;
            double diffY = mc.thePlayer.posY - posY;
            double diffZ = mc.thePlayer.posZ - posZ;

            double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);

            if(dist <= 17D && posX != mc.thePlayer.posX && posY != mc.thePlayer.posY && posZ != mc.thePlayer.posZ)
                event.setCancelled(true);
        }
    }

    @Override
    public void onUpdate() {
        if (isToggled()) {
        	this.setName("AntiBot \u00A77" + mode);

            if(mode.equalsIgnoreCase("Watchdog")) {
        	for(Object entity : mc.theWorld.loadedEntityList)
                if(((Entity)entity).isInvisible() && entity != mc.thePlayer) {
                    mc.theWorld.removeEntity((Entity)entity);
            }
            }
        }
 }
}

