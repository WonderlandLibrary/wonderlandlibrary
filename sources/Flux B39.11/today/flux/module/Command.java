package today.flux.module;

import com.soterdev.SoterObfuscator;
import today.flux.utility.ChatUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public abstract class Command {
	private String name;
	private String help;
	private String[] syntax;
	protected Minecraft mc;

	public Command() {
		this.mc = Minecraft.getMinecraft();
		this.name = this.getClass().getAnnotation(Info.class).name();
		this.help = this.getClass().getAnnotation(Info.class).help();
		this.syntax = this.getClass().getAnnotation(Info.class).syntax();
	}

	public Command(String name, String help, String... syntax) {
		this.mc = Minecraft.getMinecraft();
		this.name = name;
		this.help = help;
		this.syntax = syntax;
	}

	public final String getCmdName() {
		return this.name;
	}

	public final String getHelp() {
		return this.help;
	}

	public final String[] getSyntax() {
		return this.syntax;
	}

	protected final void syntaxError() {
		ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "Invalid arguments." + EnumChatFormatting.RESET + getUsage());
	}

	@SoterObfuscator.Obfuscation(flags = "+native")
	protected String getUsage(){
		String output = "\n." + getCmdName();

		if (getSyntax().length > 0) {
		    output = "";
			for (int i = 0; i < this.getSyntax().length; ++i) {
                output = output + "\n" + EnumChatFormatting.GRAY + "." + getCmdName() + " " + getSyntax()[i];
            }
		}

		return output;
	}

	public abstract void execute(final String[] args);

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Info {
		String name();

		String help();

		String[] syntax();
	}
}
