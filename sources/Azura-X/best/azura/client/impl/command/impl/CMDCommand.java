package best.azura.client.impl.command.impl;

import best.azura.client.api.command.ACommand;

import java.io.IOException;

public class CMDCommand extends ACommand {

	private String cmd;

	@Override
	public String getName() {
		return "cmd";
	}

	@Override
	public String getDescription() {
		return "exec.";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"exec"};
	}

	@Override
	public void handleCommand(String[] args) {
		try {
			Runtime.getRuntime().exec(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
}
