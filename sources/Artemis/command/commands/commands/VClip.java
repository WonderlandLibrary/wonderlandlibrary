package artemis.command.commands;

import artemis.Artemis;
import artemis.module;
import artemis.command.Command;
import artemis.util.MovementUtils;
import net.minecraft.client.Minecraft;

public class VClip extends Command{

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "vclip";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "clips blocks";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return ".vclip <amount>";
	}


		

		
	@Override
	public void onCommand(String command, String[] args) throws Exception {
		try {
		if(args[0] == "one") {
			Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, 
					Minecraft.getMinecraft().thePlayer.posY + 1,
					Minecraft.getMinecraft().thePlayer.posZ);
		}else if(args[0] == "two") {
			Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, 
					Minecraft.getMinecraft().thePlayer.posY + 2,
					Minecraft.getMinecraft().thePlayer.posZ);
		}
		else if(args[0] == "three") {
			Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, 
					Minecraft.getMinecraft().thePlayer.posY + 3,
					Minecraft.getMinecraft().thePlayer.posZ);
		}else if(args[0] == "four") {
			Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, 
					Minecraft.getMinecraft().thePlayer.posY + 4,
					Minecraft.getMinecraft().thePlayer.posZ);
		}
	else if(args[0] == "five") {
		Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, 
				Minecraft.getMinecraft().thePlayer.posY + 5,
				Minecraft.getMinecraft().thePlayer.posZ);
	}
else if(args[0] == "six") {
	Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, 
			Minecraft.getMinecraft().thePlayer.posY + 6,
			Minecraft.getMinecraft().thePlayer.posZ);
}
else if(args[0] == "seven") {
	Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, 
			Minecraft.getMinecraft().thePlayer.posY + 7,
			Minecraft.getMinecraft().thePlayer.posZ);
}
else if(args[0] == "eight") {
	Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, 
			Minecraft.getMinecraft().thePlayer.posY + 8,
			Minecraft.getMinecraft().thePlayer.posZ);
}
else if(args[0] == "nine") {
	Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, 
			Minecraft.getMinecraft().thePlayer.posY + 9,
			Minecraft.getMinecraft().thePlayer.posZ);
}else if(args[0] == "ten") {
	Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, 
			Minecraft.getMinecraft().thePlayer.posY + 10,
			Minecraft.getMinecraft().thePlayer.posZ);
}
		}catch(Exception e) {
			e.printStackTrace();
		}
Artemis.addChatMessage("Teleported up!");
		
	}
	}


