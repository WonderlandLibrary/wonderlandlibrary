package CakeClient.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import CakeClient.modules.Module;
import net.minecraft.util.BlockPos;

public class Scafold extends Module
{
    public Scafold() {
        super("scafold");
    }
    
    @Override
    public void onUpdate() {
    	BlockPos pos = mc.thePlayer.getPosition().add(0,1,0);
        }
}
