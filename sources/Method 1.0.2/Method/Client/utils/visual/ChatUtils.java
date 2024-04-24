package Method.Client.utils.visual;

import Method.Client.Main;
import Method.Client.utils.system.Wrapper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class ChatUtils {

    public static void component(ITextComponent component) {
        if (Wrapper.INSTANCE.player() == null) {
            return;
        } else {
            Wrapper.INSTANCE.mc().ingameGUI.getChatGUI();
        }
        Wrapper.INSTANCE.mc().ingameGUI.getChatGUI()
                .printChatMessage(new TextComponentTranslation("")
                        .appendSibling(component));
    }

    public static void message(String message) {
        component(new TextComponentTranslation(ChatFormatting.GRAY + Main.NAME + ChatFormatting.RESET +" "+ message));
    }

    public static void warning(String message) {
        message(ChatFormatting.GRAY + "[" + ChatFormatting.DARK_RED + " WARNING" + ChatFormatting.GRAY + " ] " + ChatFormatting.RED + message);
    }

    public static void error(String message) {
        message(ChatFormatting.GRAY + "[" + ChatFormatting.YELLOW + " WARNING" + ChatFormatting.GRAY + " ] " + ChatFormatting.YELLOW + message);
    }
}
