package rosegoldclient.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.ResourcePackFileNotFoundException;
import net.minecraft.util.ResourceLocation;
import rosegoldclient.Main;

import java.io.FileNotFoundException;

public class Fonts {

    public static FontRenderer openSans;
    public static FontRenderer fontBig;
    public static FontRenderer defaultFont = Main.mc.fontRenderer;

    private Fonts() {
    }

    public static void bootstrap() {
        try {
            openSans = new FontRenderer(Main.mc.gameSettings, new ResourceLocation("rosegoldclient", "fonts/OpenSans-Regular.ttf"), Main.mc.renderEngine, false);
            fontBig = new FontRenderer(Main.mc.gameSettings, new ResourceLocation("rosegoldclient", "fonts/robotoMedium.ttf"), Main.mc.renderEngine, false);
        } catch (Exception e) {
            e.printStackTrace();
            openSans = Main.mc.fontRenderer;
            fontBig = Main.mc.fontRenderer;
        }
    }
}
