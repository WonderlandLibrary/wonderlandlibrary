package wtf.diablo.commands;

import net.minecraft.client.Minecraft;

public abstract class Command {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    public abstract void run(String[] args);

}
