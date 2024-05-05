package artemis.command.commands;

import artemis.Artemis;
import artemis.module;
import artemis.command.Command;
import artemis.util.MovementUtils;

public class HClip extends Command{

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "hclip";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "teleports forward";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return ".hclip <amount>";
	}


		

		
	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if(args[0] == "one") {
			MovementUtils.forward(1);
		}else if(args[0] == "two") {
			MovementUtils.forward(2);
		}
		else if(args[0] == "three") {
			MovementUtils.forward(3);
		}else if(args[0] == "four") {
			MovementUtils.forward(4);
		}
	else if(args[0] == "five") {
		MovementUtils.forward(5);
	}
else if(args[0] == "six") {
	MovementUtils.forward(6);
}
else if(args[0] == "seven") {
	MovementUtils.forward(7);
}
else if(args[0] == "eight") {
	MovementUtils.forward(8);
}
else if(args[0] == "nine") {
	MovementUtils.forward(9);
}else if(args[0] == "ten") {
	MovementUtils.forward(10);	
}
		
Artemis.addChatMessage("Teleported forward!");
		
	}
	}


