package com.initial.commands.impl;

import com.initial.commands.*;
import com.initial.*;

public class Help extends Command
{
    public Help() {
        super("Help", "?", " ", new String[] { "help" });
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        Astomero.addChatMessage("§7§m----------------------------------------");
        Astomero.addChatMessage("§c§lAstomero §7- §f" + Astomero.client_build);
        Astomero.addChatMessage("§7§oDeveloped by Initial");
        Astomero.addChatMessage(" ");
        Astomero.addChatMessage("§c§lCommands:");
        Astomero.addChatMessage("§c.hide §7- Hides a module by name");
        Astomero.addChatMessage("§c.vclip §7- Clips you down or up");
        Astomero.addChatMessage("§c.bind §7- Binds a module");
        Astomero.addChatMessage("§c.rename §7- Changes the client name");
        Astomero.addChatMessage("§c.name §7- Copies your Minecraft username to clipboard");
        Astomero.addChatMessage("§c.toggle §7- Toggles a module");
        Astomero.addChatMessage("§c.hclip §7- Clips you horizontal");
        Astomero.addChatMessage("§7§m----------------------------------------");
    }
}
