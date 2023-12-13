package slavikcodd3r.rainbow.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "damage", "dmg", "d" })
public class Damage extends Command
{
	Minecraft mc = Minecraft.getMinecraft();
	
    @Override
    public void runCommand(final String[] args) {
    	for (int i = 0; i < 80.0 + 40.0 * (0.5 - 0.5); ++i) {
            ClientUtils.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY + 0.049, ClientUtils.player().posZ, false));
            ClientUtils.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY, ClientUtils.player().posZ, false));
        }
        ClientUtils.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY, ClientUtils.player().posZ, true));
    }
    
    @Override
    public String getHelp() {
        return "damage";
    }
}
