package rosegoldclient.utils;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import rosegoldclient.Main;

public class Utils {

    public static void sendCreditMessage(String message, String link) {
        ITextComponent msg = new TextComponentString(message);
        msg.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
        msg.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.YELLOW + "Open: " + link)));
        Main.mc.player.sendMessage(msg);
    }

    public static void sendCreditMessage(String message) {
        ITextComponent msg = new TextComponentString(message);
        Main.mc.player.sendMessage(msg);
    }

    public static void sendMessage(String message) {
        if(Main.configFile.silentMode) return;
        if(Main.mc.player != null && Main.mc.world != null) {
            if(!message.contains("§")) {
                message = message.replace("&", "\u00a7");
            }
            Main.mc.player.sendMessage(new TextComponentTranslation(message));
        }
    }

    public static void sendMessageAsPlayer(String message) {
        Main.mc.player.sendChatMessage(message);
    }

    public static void sendModMessage(Object message) {
        sendMessage("&f[&aRoseGoldClient&f] " + message.toString());
    }

    public static String getGuiName(GuiScreen gui) {
        if(gui instanceof GuiChest) {
            return ((ContainerChest) ((GuiChest) gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        }
        return "";
    }
}
