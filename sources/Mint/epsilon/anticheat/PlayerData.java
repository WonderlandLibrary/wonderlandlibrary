package epsilon.anticheat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity;

public class PlayerData {

	private final EntityPlayer t;
	
	private double x, y, z, lX, lY, lZ;
	
	private int ticksSinceVelo;
	
	private boolean onGround, clientG;
	
	public List<S14PacketEntity> posList = new ArrayList<S14PacketEntity>();	
	
	public PlayerData(final EntityPlayer t) {
		this.t = t;
	}
	
	public void update() {
		
		ticksSinceVelo++;
		
		x = t.posX; lX = t.lastTickPosX; y = t.posY; lY = t.lastTickPosY; z = t.posZ; lZ = t.lastTickPosZ;
		
	}
	
	public void manage(final Packet p) {
		
		if(p instanceof S14PacketEntity.S15PacketEntityRelMove || p instanceof S14PacketEntity.S17PacketEntityLookMove) {
			
			final S14PacketEntity move = (S14PacketEntity) p;
			
            if (move.id == this.t.getEntityId()) {
    			posList.add(move);
            }
			
			
			
		}
		if(p instanceof S12PacketEntityVelocity) {
			S12PacketEntityVelocity packet = (S12PacketEntityVelocity) p;

			if(packet.id != Minecraft.getMinecraft().thePlayer.getEntityId()) {
				ticksSinceVelo = 0;
			}
		}
		
		
	}
	
}
