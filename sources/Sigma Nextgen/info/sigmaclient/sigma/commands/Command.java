package info.sigmaclient.sigma.commands;

import info.sigmaclient.sigma.utils.ChatUtils;
import net.minecraft.client.Minecraft;

public abstract class Command {
    protected static Minecraft mc = Minecraft.getInstance();
    public abstract void onChat(String[] args, String joinArgs);
    public abstract String usages();
    public abstract String describe();
    public abstract String[] getName();
    public void sendUsages(){
        ChatUtils.sendMessageWithPrefix("Syntax error. Usages: " + getName()[0] + " " + usages());
    }
}
