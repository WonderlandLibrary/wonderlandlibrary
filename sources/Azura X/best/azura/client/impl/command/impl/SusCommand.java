package best.azura.client.impl.command.impl;

import best.azura.client.api.command.ACommand;
import best.azura.client.util.crypt.Crypter;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SusCommand extends ACommand {

	@Override
	public String getName() {
		return "sus";
	}

	@Override
	public String getDescription() {
		return "Be sussy :)";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"sussy"};
	}


	@Override
	public void handleCommand(String[] args) {
		try {
			Desktop.getDesktop().browse(new URI(Crypter.decode("aHR0cHM6Ly9wb3JuaHViLmNvbS9nYXk=")));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}