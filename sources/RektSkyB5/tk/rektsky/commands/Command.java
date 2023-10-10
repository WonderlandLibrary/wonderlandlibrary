/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands;

import net.minecraft.client.Minecraft;
import tk.rektsky.event.Event;

public class Command {
    private String name;
    private String description;
    private String[] aliases;
    private String argumentDescription;
    public Minecraft mc;

    public Command(String name, String[] aliases, String argumentDescription, String description) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.argumentDescription = argumentDescription;
        Command command = this;
        this.mc = command.mc.getMinecraft();
    }

    public Command(String name, String argumentDescription, String description) {
        this(name, new String[0], argumentDescription, description);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getArgumentDescription() {
        return this.argumentDescription;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public void onEvent(Event event) {
    }

    public void onCommand(String label, String[] args) {
    }
}

