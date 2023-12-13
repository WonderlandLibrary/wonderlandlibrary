package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import today.flux.event.DrawTextEvent;
import today.flux.irc.IRCClient;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.utility.WorldUtil;
import today.flux.module.value.BooleanValue;

import java.util.HashMap;
import java.util.Map;

public class NameProtect extends Module {
    public static String name = IRCClient.loggedPacket.getRealUsername();
    public static Map<String, String> Player_IGN = new HashMap<>();
    public static BooleanValue skin = new BooleanValue("NameProtect", "Skin", false);
    public static BooleanValue others = new BooleanValue("NameProtect", "Others", false);

    public NameProtect() {
        super("NameProtect", Category.Render, false);
    }

    @EventTarget
    public void onDrawText(DrawTextEvent event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null)
            return;

        if (event.text == null)
            return;

        String input = event.text;

        //replace my name
        input = input.replace(this.mc.thePlayer.getName(), name).replace(this.mc.thePlayer.getName().toLowerCase(), name).replace(this.mc.thePlayer.getName().toUpperCase(), name);

        //replace nicknames
        for (String name : Player_IGN.keySet())
            input = input.replace(name, Player_IGN.get(name));

        //replace other players
        if (others.getValue()) {
            for (NetworkPlayerInfo item : mc.getNetHandler().getPlayerInfoMap()) {
                if (item == null || item.getGameProfile() == null)
                    continue;

                input = input.replace(item.getGameProfile().getName(), EnumChatFormatting.RESET + "" + EnumChatFormatting.OBFUSCATED + item.getGameProfile().getName() + EnumChatFormatting.RESET);
            }

            for (EntityPlayer player : WorldUtil.getLivingPlayers()) {
                input = input.replace(player.getName(), EnumChatFormatting.WHITE + "" + EnumChatFormatting.OBFUSCATED + player.getName() + EnumChatFormatting.WHITE);
            }
        }

        event.text = input;
    }
    
    public int getMaxProtectedNameLength() {
    	int max = 0;
        for (String name : Player_IGN.keySet()) {
        	max = Math.max(max, mc.fontRendererObj.getStringWidth(Player_IGN.get(name)));
        }
        return max;
    }
}
