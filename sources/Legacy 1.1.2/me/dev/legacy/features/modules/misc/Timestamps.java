package me.dev.legacy.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.legacy.features.modules.Module;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Timestamps extends Module {
    public Timestamps() {
        super("Timestamps", "Prefixes chat messages with the time", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
        String strDate = dateFormatter.format(date);
        TextComponentString time = new TextComponentString(ChatFormatting.GREEN + "<" + ChatFormatting.GREEN + strDate + ChatFormatting.GREEN + ">" + ChatFormatting.GREEN + " ");
        event.setMessage(time.appendSibling(event.getMessage()));
    }
}