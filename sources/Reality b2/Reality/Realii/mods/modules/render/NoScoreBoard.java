
package Reality.Realii.mods.modules.render;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import Reality.Realii.utils.cheats.world.TimerUtil;
import net.minecraft.util.ResourceLocation;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.rendering.Shader3DEvent;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;

public class NoScoreBoard
        extends Module {
	
	
	
    public NoScoreBoard() {
        super("NoScoreBoard", ModuleType.Render);
       
    }
    


    
    @EventHandler
    public void onPre(EventPreUpdate e) {
    	Scoreboard scoreboard = mc.theWorld.getScoreboard();

    	ScoreObjective scoreObjective = scoreboard.getObjectiveInDisplaySlot(1);
    	if (scoreObjective != null) {
    	    scoreboard.setObjectiveInDisplaySlot(1, null);
    	}
    	//scoreboard.addScoreObjective("NyghtfullBestRo", null);
    }

    
}

