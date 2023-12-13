package wtf.monsoon.api.command;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {

    public String name, description, syntax;
    public List<String> aliases = new ArrayList<String>();
    public Minecraft mc = Minecraft.getMinecraft();

    public Command(String name, String desc, String syntax, String...aliases) {
        this.name = name;
        this.description = desc;
        this.syntax = syntax;
        this.aliases = Arrays.asList(aliases);
    }

    public abstract void onCommand(String[] args, String command);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public List<String> getAliases() {
        return aliases;
    }

}
