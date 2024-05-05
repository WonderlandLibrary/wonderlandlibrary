package Velo.api.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;

public abstract class Command {
	
	public String commandName, desc, syntax;
	public List<String> othernames = new ArrayList<String>();
	public static Minecraft mc =  Minecraft.getMinecraft();
	
	public Command(String commandName, String desc, String syntax, String...othernames) {
		this.commandName = commandName;
		this.desc = desc;
		this.syntax = syntax;
		this.othernames = Arrays.asList(othernames);
	}
	
	public abstract void onCommand(String[] args, String command);

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSyntax() {
		return syntax;
	}

	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}

	public List<String> getOthernames() {
		return othernames;
	}

	public void setOthernames(List<String> othernames) {
		this.othernames = othernames;
	}
	
	
}
