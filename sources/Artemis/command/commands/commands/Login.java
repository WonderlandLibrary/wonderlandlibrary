package artemis.command.commands;

import artemis.Artemis;
import artemis.altmanager.AltLoginThread;
import artemis.command.Command;

public class Login extends Command{
	private AltLoginThread thread;
	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return "login";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "logs in to a diffrent account";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return ".login <email> <password>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		try {
			
		this.thread = new AltLoginThread(args[0], args[1]);
        this.thread.start();

		}catch(Exception e) {
			e.printStackTrace();
		}
		  
	 Artemis.addChatMessage("Logged into account! Username/E-mail: " + args[0]);
	}

}
