// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands.impl;

import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.util.IChatComponent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChatComponentText;
import dev.tenacity.Tenacity;
import dev.tenacity.commands.Command;

public class HelpCommand extends Command
{
    public HelpCommand() {
        super("help", "Displays information about the commands", ".help", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        final ChatComponentText response = new ChatComponentText("\n§l§dTenacity §r§d6.0§l " + Tenacity.RELEASE.getName() + " §7- §r§6Hover to see command usages.");
        final ChatComponentText temp = new ChatComponentText("");
        float maxLength = 0.0f;
        for (final Command cmd : Tenacity.INSTANCE.getCommandHandler().getCommands()) {
            final String info = String.format("\n§r§b.%s \u2022 §7%s", cmd.getName().toLowerCase(), cmd.getDescription());
            temp.appendSibling(new ChatComponentText(info).setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(String.format("§7Usage: §e%s", cmd.getUsage()))))));
            if (this.mc.fontRendererObj.getStringWidth(info) > maxLength) {
                maxLength = this.mc.fontRendererObj.getStringWidth(info);
            }
        }
        response.appendSibling(new ChatComponentText(String.format("\n§7§m%s§r", StringUtils.repeat(" ", (int)(maxLength / 4.0f))))).appendSibling(temp);
        this.mc.thePlayer.addChatMessage(response);
    }
}
