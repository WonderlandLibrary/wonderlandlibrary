package Reality.Realii.mods.modules.player;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.misc.EventChat;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.player.PlayerUtils;

import java.awt.Color;
import java.util.Random;

public class SpinBot
extends Module {
    public SpinBot(){
        super("AntiAim", ModuleType.Player);

    }

    @EventHandler
    private void OnUpdate(EventPreUpdate e) {
    	Random randomYaw = new Random();
        Random randomPitch = new Random();
        if (mc.thePlayer.ticksExisted % 2 == 0) {
        float yaw = randomYaw.nextInt(100);
        float pitch = randomPitch.nextInt(100);
        
        
        PlayerUtils.rotate(yaw, pitch);

        e.setYaw(yaw);
        e.setPitch(pitch);
        } else {
          
            
            
            PlayerUtils.rotate(360, 360);
            
            e.setYaw(360);
            e.setPitch(360);
        	
        }
    	
    	
    }
    }