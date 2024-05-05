package epsilon.anticheat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class TrackedPlayerManager {

	private final Minecraft mc = Minecraft.getMinecraft();
	
	private List<EntityPlayer> trackedPlayers = new ArrayList<EntityPlayer>();
	
	public List<EntityPlayer> getTrackedPlayers() {
		return trackedPlayers;
	}
	
	public void onUpd() {

		
		if(mc.theWorld!=null) {
			
			trackedPlayers.clear();
			
			for (final Object t: mc.theWorld.loadedEntityList) {
				
				
				if(t instanceof EntityPlayer) {
					final EntityPlayer p = (EntityPlayer) t;
					trackedPlayers.add(p);
				}
				
			}
			
			
			
		}
					
		
		
	}
	
	
	
}
