package xyz.northclient.util.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class FontUtil {
    public static CustomFont Default;
    public static CustomFont DefaultSmall;
    public static CustomFont SFProMedium;
    public static CustomFont SFProMediumSmall;
    public static CustomFont SFProMediumBig;
    public static CustomFont SFProMediumBigger;
    public static CustomFont SFProRegular;
    public static CustomFont notificationFont;
    public static CustomFont DefaultSmallBold;

    public static void Init() {
        try {
            Default = createFont("ProductSans.ttf",24);
            DefaultSmall = createFont("ProductSans.ttf",18);
            SFProMedium = createFont("SFProMedium.ttf",18);
            SFProMediumSmall = createFont("SFProRegular.ttf",16);
            SFProMediumBig = createFont("SFProMedium.ttf",28);
            SFProMediumBigger = createFont("SFProMedium.ttf",48);
            SFProRegular = createFont("SFProRegular.ttf",18);
            notificationFont = createFont("notify.ttf",50);
            DefaultSmallBold = createFont("Product Sans Bold.ttf",18);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CustomFont createFont(String name, int size) throws IOException, FontFormatException {
        return new CustomFont(Font.createFont(Font.TRUETYPE_FONT, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("tecness/" + name)).getInputStream()).deriveFont(Font.PLAIN,size));
    }
}
