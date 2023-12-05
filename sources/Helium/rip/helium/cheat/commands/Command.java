package rip.helium.cheat.commands;

import net.minecraft.client.Minecraft;

public abstract class Command {
    public String name;
    protected Minecraft mc = Minecraft.getMinecraft();
    private final String[] aliases;

    public Command(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public abstract void run(String[] args);

}
