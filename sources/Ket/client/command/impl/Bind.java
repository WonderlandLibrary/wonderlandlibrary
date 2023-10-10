package client.command.impl;

import client.Client;
import client.command.Command;
import client.module.Module;
import client.util.ChatUtil;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public final class Bind extends Command {

    public Bind() {
        super("Binds a module to the given key", "bind");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length == 3) {
            final Module module = Client.INSTANCE.getModuleManager().get(args[1]);
            if (module == null) {
                ChatUtil.display("Invalid module");
                return;
            }
            final int keyCode = Keyboard.getKeyIndex(args[2].toUpperCase());
            module.setKeyCode(keyCode);
            ChatUtil.display("Bound " + module.getInfo().name() + " to " + Keyboard.getKeyName(keyCode) + ".");
        } else if (args.length == 2 && args[1].equalsIgnoreCase("list")) {
            ChatUtil.display("Displaying all active binds");
            Client.INSTANCE.getModuleManager().getAll().forEach(module -> {
                if (module.getKeyCode() != 0) {
                    final EnumChatFormatting color = EnumChatFormatting.AQUA;
                    final ChatComponentText chatText = new ChatComponentText(color + "> " + module.getInfo().name() + "§f " + Keyboard.getKeyName(module.getKeyCode()));
                    chatText.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ".bind " + module.getInfo().name().replace(" ","") + " NONE")).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to remove " + module.getInfo().name() + " bind")));
                    mc.thePlayer.addChatMessage(chatText);
                }
            });
        } else error(".bind <list/module> (KEY)");
    }
}