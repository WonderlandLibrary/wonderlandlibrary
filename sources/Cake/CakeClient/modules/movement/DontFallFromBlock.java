package CakeClient.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import CakeClient.modules.Module;

public class DontFallFromBlock extends Module
{
    public DontFallFromBlock() {
        super("Dont fall from block");
    }
    public static boolean onlyOnGround = true;
    public void onLeftConfig() {onlyOnGround = false; }
    public void onRightConfig() {onlyOnGround = true; }
    public String getConfigStatus()
    {
    	if (onlyOnGround) return "onlyOnGround OFF";
    	else return "onlyOnGround ON";
    }
}