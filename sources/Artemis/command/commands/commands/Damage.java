package artemis.command.commands;

import artemis.Artemis;
import artemis.module;
import artemis.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Damage extends Command{
	protected Minecraft mc = Minecraft.getMinecraft();
	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "damage";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "does damage to yourself using an ncp exploit";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return ".damage";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		        double x = mc.thePlayer.posX;
		        double y = mc.thePlayer.posY;
		        double z = mc.thePlayer.posZ;

		        for (int i = 0; i < 65 * 5; i++) {
		            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049, z, false));
		            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
		        }
		     mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));

		
		Artemis.addChatMessage("Did damage to self!");
		 
	}

}
