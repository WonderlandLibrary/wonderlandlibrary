package Method.Client.utils.Screens;

import Method.Client.Main;
import Method.Client.utils.Screens.Override.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;

import java.util.ArrayList;

public abstract class Screen {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    public boolean visible = true;

    public TextFormatting ColorfromInt(int i) {
        if (i < 16)
            return TextFormatting.fromColorIndex(i);

        switch (i) {
            case 16:
                return TextFormatting.OBFUSCATED;
            case 17:
                return TextFormatting.BOLD;
            case 18:
                return TextFormatting.STRIKETHROUGH;
            case 19:
                return TextFormatting.UNDERLINE;
            case 20:
                return TextFormatting.ITALIC;
            case 21:
                return TextFormatting.RESET;
        }
        return null;
    }

    public static ArrayList<Screen> Screens = new ArrayList<>();

    public static void Initalise() {
        addScreen(new EscInsert());
        addScreen(new SignInsert());
        addScreen(new ChestGuiInsert());
        addScreen(new ShulkerGuiInsert());
        addScreen(new BookInsert());
        addScreen(new DisconnectedInsert());
        addScreen(new ConnectingInsert());
    }

    public static void addScreen(Screen m) {
        if (Screens.contains(m)) return;
        Screens.add(m);
        Main.eventBus.register(m);
    }

    public void DrawScreenEvent(GuiScreenEvent.DrawScreenEvent event) {
    }

}
