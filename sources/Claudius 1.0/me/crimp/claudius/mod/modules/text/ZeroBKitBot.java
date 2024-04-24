package me.crimp.claudius.mod.modules.text;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZeroBKitBot extends Module {

        //Credit: FB#7334 for the orignal src of this module
        //thank you to Konas for parseChatMessage


        public ZeroBKitBot() {
            super("KitBot","Automatically teleports to people when they type a password in chat.", Category.Text, false,false);
        }

        public Setting<String> password = register(new Setting<>("Password", "!kit"));
        public Setting<Boolean> whitelist = register(new Setting<>("Freinds Only", true));
        public Setting<Boolean> kill = register(new Setting<>("AutoSuicide", true));
        public Setting<Boolean> debug = register(new Setting<>("Announce", true));

        @SubscribeEvent
        public void onChat(ClientChatReceivedEvent event) {
            if (event.getMessage().getUnformattedText().toLowerCase().contains(password.getValue())) {
            Optional<Map.Entry<String, String>> parsedMessage = parseChatMessage(event.getMessage().getUnformattedText());



               // if (!whitelist.getValue() || whitelist.getValue() && claudius.friendManager.isFriend(parsedMessage.get().getKey())) {


                if (debug.getValue()) {
                    Command.sendOverwriteClientMessage("Registered a kit request.");
                }
                if (debug.getValue()) {
                    Command.sendOverwriteClientMessage("Attempting to teleport to: " + parsedMessage.get().getKey() + ".");
                }
                mc.player.sendChatMessage("/tpa " + parsedMessage.get().getKey());
            }

            if (event.getMessage().getUnformattedText().contains("teleporting to:")) {

                if (debug.getValue()) {
                    Command.sendOverwriteClientMessage("Teleport success!");
                }

                if (kill.getValue()) {

                    if (debug.getValue()) {
                        Command.sendOverwriteClientMessage("Attempting to /kill back to stash.");
                    }

                    mc.player.sendChatMessage("/kill");
                }
            }
        }


        //on 0b0t redstone is shit so when you respawn you will not always get a kit if you step on the pressureplate
        //simple fix: if your inventory is empty, jump onto the pressure plate until you get a kit
        @Override
        public void onUpdate() {
            if (mc.player.inventory.isEmpty() && !mc.player.isDead && mc.player.onGround) {
                if (debug.getValue()) {
                    Command.sendOverwriteClientMessage("Inventory empty, attempting to jump to trip tripwire.");
                }
                mc.player.jump();
            }
        }

        public static Optional<Map.Entry<String, String>> parseChatMessage(String messageRaw) {

            Matcher matcher = Pattern.compile("^<(" + "[a-zA-Z0-9_]{3,16}" + ")> (.+)$").matcher(messageRaw);

            String senderName = null;
            String message = null;

            while (matcher.find()) {
                senderName = matcher.group(1);
                message = matcher.group(2);
            }

            if (senderName == null || senderName.isEmpty()) {
                return Optional.empty();
            }

            if (message == null || message.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(new AbstractMap.SimpleEntry<>(senderName, message));
        }
}
