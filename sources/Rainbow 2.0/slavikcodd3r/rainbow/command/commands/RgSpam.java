package slavikcodd3r.rainbow.command.commands;

import net.minecraft.client.Minecraft;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "rgspam", "rgs" })
public class RgSpam extends Command
{
    public static Minecraft mc;
    
    static {
        RgSpam.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void runCommand(final String[] args) {
        String regionName = "";
        String playerName = "";
        if (args.length > 1 || regionName != "" || playerName != "") {
            regionName = args[1];
            playerName = args[2];
            RgSpam.mc.thePlayer.sendChatMessage("/rg claim " + regionName);
            RgSpam.mc.thePlayer.sendChatMessage("/rg addmember " + regionName + " " + playerName);
            RgSpam.mc.thePlayer.sendChatMessage("/rg removeowner " + regionName + " " + RgSpam.mc.thePlayer.getGameProfile().getName());
            ClientUtils.sendMessage("Region: " + regionName + ", added " + playerName + " into /rg list");
        }
        if (regionName == "" || playerName == "" || args.length <= 1) {
            ClientUtils.sendMessage("Not enogh arguments! use rgspam <regionname> <playername>");
        }
    }
    
    @Override
    public String getHelp() {
        return "rgspam";
    }
}
