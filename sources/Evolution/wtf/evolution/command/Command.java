package wtf.evolution.command;

import net.minecraft.client.Minecraft;

import java.util.List;

public class Command {

    public String command;
    public CommandInfo info = this.getClass().getAnnotation(CommandInfo.class);
    public Minecraft mc = Minecraft.getMinecraft();

    public Command() {
        this.command = info.name();
    }

    public void execute(String[] args) {

    }

    public void onError() {

    }

    public String getCommand() {
        return command;
    }

    public List<String> getSuggestions(String[] args) {
        return null;
    }

}
