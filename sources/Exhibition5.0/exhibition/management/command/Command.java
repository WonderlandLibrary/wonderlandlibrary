// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command;

import java.util.Arrays;
import exhibition.util.misc.ChatUtil;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.Minecraft;

public abstract class Command implements Fireable
{
    private final String[] names;
    private final String description;
    public static final String chatPrefix = "§4[§cE§4]§8 ";
    public Minecraft mc;
    
    public Command(final String[] names, final String description) {
        this.mc = Minecraft.getMinecraft();
        this.names = names;
        this.description = description;
    }
    
    protected void printDescription() {
        final String message = "§4[§cE§4]§8 " + this.getName() + EnumChatFormatting.GRAY + ": " + this.description;
        ChatUtil.printChat(message);
    }
    
    protected void printUsage() {
        final String message = "§4[§cE§4]§8 " + this.getName() + EnumChatFormatting.GRAY + ": " + this.getUsage();
        ChatUtil.printChat(message);
    }
    
    public void register(final CommandManager manager) {
        for (final String name : this.names) {
            manager.addCommand(name.toLowerCase(), this);
        }
    }
    
    public abstract String getUsage();
    
    public String getName() {
        return this.names[0];
    }
    
    public boolean isMatch(final String text) {
        return Arrays.asList(this.names).contains(text.toLowerCase());
    }
    
    public String getDescription() {
        return this.description;
    }
}
