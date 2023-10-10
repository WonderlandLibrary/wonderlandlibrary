package summer.base.font;
import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import summer.ui.clickuiutils.Manager;
import summer.ui.clickuiutils.TTFRenderer;

public class FontManager implements Manager {
    private ResourceLocation darrow = new ResourceLocation("SF-UI-Display-Regular.otf");
    private TTFFontRenderer defaultFont;
    public FontManager getInstance() {
        return instance;
    }
    public TTFFontRenderer getFont(String key) {
        return fonts.getOrDefault(key, defaultFont);
    }
    private FontManager instance;
    private HashMap<String, TTFFontRenderer> fonts = new HashMap<>();
    public TTFRenderer hud = null;
    public TTFRenderer arial10 = null;
    public TTFRenderer arial11 = null;
    public TTFRenderer arial12 = null;
    public TTFRenderer arial13 = null;
    public TTFRenderer arial14 = null;
    public TTFRenderer arial15 = null;
    public TTFRenderer arial16 = null;
    public TTFRenderer arial17 = null;
    public TTFRenderer arial18 = null;
    public TTFRenderer arial19 = null;
    public TTFRenderer arial20 = null;

    @Override
    public void setup() {
        this.hud = new TTFRenderer("Arial", 0, 18);
        this.arial10 = new TTFRenderer("Arial", 0, 10);
        this.arial11 = new TTFRenderer("Arial", 0, 11);
        this.arial12 = new TTFRenderer("Arial", 0, 12);
        this.arial13 = new TTFRenderer("Arial", 0, 13);
        this.arial14 = new TTFRenderer("Arial", 0, 14);
        this.arial15 = new TTFRenderer("Arial", 0, 15);
        this.arial16 = new TTFRenderer("Arial", 0, 16);
        this.arial17 = new TTFRenderer("Arial", 0, 17);
        this.arial18 = new TTFRenderer("Arial", 0, 18);
        this.arial19 = new TTFRenderer("Arial", 0, 19);
        this.arial20 = new TTFRenderer("Arial", 0, 20);
    }

    public FontManager() {
        instance = this;
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
        ConcurrentLinkedQueue<TextureData> textureQueue = new ConcurrentLinkedQueue<>();
        defaultFont = new TTFFontRenderer(executorService, textureQueue, new Font("Verdana", Font.PLAIN, 18));
        try {
            for (int i : new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/Panton.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("PAN " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (int i : new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/Evogria.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("EVO " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (int i : new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 21, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/Whitney-Book.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("WHIT " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (int i : new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 21, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/Roboto-Regular.ttf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("ROBO " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (int i : new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 21, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/tahoma.ttf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("TAHOMA " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (int i : new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 21, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/Comfortaa.ttf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("COM " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (int i : new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 21, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 60}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/nova/fonts/jellolight.ttf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("JELLO " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (int i : new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 21, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 60}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/nova/fonts/jelloregular.ttf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("JELLO1 " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            fonts.put("Verdana 12", new TTFFontRenderer(executorService, textureQueue, new Font("Verdana", Font.PLAIN, 12)));

            fonts.put("Verdana Bold 16", new TTFFontRenderer(executorService, textureQueue, new Font("Verdana Bold", Font.PLAIN, 16)));
            fonts.put("Verdana Bold 20", new TTFFontRenderer(executorService, textureQueue, new Font("Verdana Bold", Font.PLAIN, 20)));
        } catch (Exception ignored) {

        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!textureQueue.isEmpty()) {
                TextureData textureData = textureQueue.poll();
                GlStateManager.bindTexture(textureData.getTextureId());

                // Sets the texture parameter stuff.
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                // Uploads the texture to opengl.
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureData.getWidth(), textureData.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureData.getBuffer());
            }
        }
    }
}
