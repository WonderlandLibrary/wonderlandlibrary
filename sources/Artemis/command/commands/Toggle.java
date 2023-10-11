package artemis.command.commands;

import artemis.Artemis;
import artemis.module;
import artemis.command.Command;

public class Toggle extends Command{

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "toggle";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Toggles a hack though the console";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return ".toggle <Module>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		boolean found = false;
		for(module m: Artemis.getModules()){
			if(args[0].equalsIgnoreCase(m.getName())){
				m.toggle();
				found = true;
				Artemis.addChatMessage(m.getName() + " was toggled!");
			}
		}
		if(found == false){
			Artemis.addChatMessage("Targeted Module was not found!");
		}
	}

}
