// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import java.util.Date;
import java.text.SimpleDateFormat;
import com.elementars.eclient.command.Command;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.util.text.ChatType;
import net.minecraft.network.play.server.SPacketChat;
import com.elementars.eclient.event.events.EventReceivePacket;
import com.elementars.eclient.util.ColorTextUtils;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import net.minecraft.util.text.TextComponentString;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Chat extends Module
{
    public final Value<Boolean> customFont;
    private final Value<Boolean> ncb;
    public static Value<Boolean> nochatshadow;
    private final Value<Boolean> namehighlight;
    private final Value<String> namemode;
    private final Value<String> playername;
    private final Value<String> playerColor;
    private final Value<Boolean> timestamps;
    private final Value<Boolean> mode;
    private final Value<String> bracketmode;
    private final Value<String> color;
    public static Chat INSTANCE;
    public static TextComponentString componentStringOld;
    
    public Chat() {
        super("Chat", "Tampers with chat", 0, Category.RENDER, true);
        this.customFont = this.register(new Value<Boolean>("Custom Font", this, false));
        this.ncb = this.register(new Value<Boolean>("No Chat Background", this, false));
        this.namehighlight = this.register(new Value<Boolean>("Name Highlight", this, false));
        this.namemode = this.register(new Value<String>("Highlight Mode", this, "Highlight", new ArrayList<String>(Arrays.asList("Highlight", "Hide"))));
        this.playername = this.register(new Value<String>("Player Tag", this, "<Player>", new String[] { "<Player>", "[Player]:", "Player:", "Player ->" }));
        this.playerColor = this.register(new Value<String>("Player Color", this, "White", ColorTextUtils.colors));
        this.timestamps = this.register(new Value<Boolean>("Time Stamps", this, false));
        this.mode = this.register(new Value<Boolean>("24 Hour Time", this, false));
        this.bracketmode = this.register(new Value<String>("Bracket Type", this, "<>", new ArrayList<String>(Arrays.asList("()", "<>", "[]", "{}"))));
        this.color = this.register(new Value<String>("Color", this, "LightGray", ColorTextUtils.colors));
        Chat.nochatshadow = this.register(new Value<Boolean>("No Chat Shadow", this, false));
        Chat.INSTANCE = this;
    }
    
    @EventTarget
    public void onPacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketChat) {
            final SPacketChat packet = (SPacketChat)event.getPacket();
            if (packet.getType() != ChatType.GAME_INFO && this.tryProcessChat(packet.getChatComponent().getFormattedText(), packet.getChatComponent().getUnformattedText())) {
                event.setCancelled(true);
            }
        }
    }
    
    private boolean tryProcessChat(String message, final String unformatted) {
        String out = message;
        final String[] parts = out.split(" ");
        final String[] partsUnformatted = unformatted.split(" ");
        parts[0] = partsUnformatted[0];
        if (parts[0].startsWith("<") && parts[0].endsWith(">")) {
            parts[0] = parts[0].replaceAll("<", "");
            parts[0] = parts[0].replaceAll(">", "");
            parts[0] = Command.SECTIONSIGN() + ColorTextUtils.getColor(this.playerColor.getValue()).substring(1) + parts[0] + Command.SECTIONSIGN() + "r";
            if (this.playername.getValue().equalsIgnoreCase("<Player>")) {
                String temp = "<" + parts[0] + ">";
                for (int i = 1; i < parts.length; ++i) {
                    temp = temp + " " + parts[i];
                }
                message = temp;
            }
            else if (this.playername.getValue().equalsIgnoreCase("[Player]:")) {
                String temp = "[" + parts[0] + "]:";
                for (int i = 1; i < parts.length; ++i) {
                    temp = temp + " " + parts[i];
                }
                message = temp;
            }
            else if (this.playername.getValue().equalsIgnoreCase("Player:")) {
                String temp = parts[0] + ":";
                for (int i = 1; i < parts.length; ++i) {
                    temp = temp + " " + parts[i];
                }
                message = temp;
            }
            else if (this.playername.getValue().equalsIgnoreCase("Player ->")) {
                String temp = parts[0] + " ->";
                for (int i = 1; i < parts.length; ++i) {
                    temp = temp + " " + parts[i];
                }
                message = temp;
            }
            else {
                String temp = "<" + parts[0] + ">";
                for (int i = 1; i < parts.length; ++i) {
                    temp = temp + " " + parts[i];
                }
                message = temp;
            }
        }
        out = message;
        if (this.timestamps.getValue()) {
            String date = "";
            if (this.mode.getValue()) {
                date = new SimpleDateFormat("k:mm").format(new Date());
            }
            else {
                date = new SimpleDateFormat("h:mm a").format(new Date());
            }
            if (this.bracketmode.getValue().equalsIgnoreCase("<>")) {
                out = "§" + ColorTextUtils.getColor(this.color.getValue()).substring(1) + "<" + date + ">§r " + message;
            }
            else if (this.bracketmode.getValue().equalsIgnoreCase("()")) {
                out = "§" + ColorTextUtils.getColor(this.color.getValue()).substring(1) + "(" + date + ")§r " + message;
            }
            else if (this.bracketmode.getValue().equalsIgnoreCase("[]")) {
                out = "§" + ColorTextUtils.getColor(this.color.getValue()).substring(1) + "[" + date + "]§r " + message;
            }
            else if (this.bracketmode.getValue().equalsIgnoreCase("{}")) {
                out = "§" + ColorTextUtils.getColor(this.color.getValue()).substring(1) + "{" + date + "}§r " + message;
            }
        }
        if (this.namehighlight.getValue()) {
            if (Chat.mc.player == null) {
                return false;
            }
            if (this.namemode.getValue().equalsIgnoreCase("Hide")) {
                out = out.replace(Chat.mc.player.getName(), "HIDDEN");
            }
            else {
                out = out.replace(Chat.mc.player.getName(), "§b" + Chat.mc.player.getName() + "§r");
            }
        }
        Command.sendRawChatMessage(out);
        return true;
    }
}
