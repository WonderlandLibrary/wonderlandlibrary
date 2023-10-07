package dev.rise.font.fontrenderer;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public final class FontManager {

    private final HashMap<String, TTFFontRenderer> fonts = new HashMap<>();
    private final TTFFontRenderer defaultFont;

    public TTFFontRenderer getFont(final String key) {
        return this.fonts.getOrDefault(key, this.defaultFont);
    }

    public FontManager() {

        final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        final ConcurrentLinkedQueue<TextureData> textureQueue = new ConcurrentLinkedQueue<>();

        this.defaultFont = new TTFFontRenderer(executorService, textureQueue, new Font("Verdana", Font.PLAIN, 18));

        try {
            for (final int i : new int[]{60, 96}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/Dreamscape.ttf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("Dreamscape " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

            for (final int i : new int[]{14, 16, 18, 19, 20, 22, 24, 36, 48, 72, 96}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/Light.ttf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("Light " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

            for (final int i : new int[]{18}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/SigmaRegular.ttf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("SigmaBold " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

            for (final int i : new int[]{12}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/TahomaBold.ttf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("SkeetBold " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

            for (final int i : new int[]{18, 16}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/Tahoma.ttf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("Skeet " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

            for (final int i : new int[]{16, 18, 28, 36, 48}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/Biko_Regular.otf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("Biko " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

            for (final int i : new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 32, 128}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/Comfortaa-Regular.ttf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("Comfortaa " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

            for (final int i : new int[]{18}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/Icon-Font.ttf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("Icon " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

            for (final int i : new int[]{18}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/icon2.ttf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("Icon2 " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

            for (final int i : new int[]{18, 20, 24, 36, 48, 72, 96}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/Jello.ttf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("Jello " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

            for (final int i : new int[]{18, 20, 24, 36, 48, 72, 96}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/aldotheapache.ttf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("Skid " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

            for (final int i : new int[]{18, 20, 24}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/MuseoSans_900.otf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("Museo " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

            for (final int i : new int[]{16, 18, 20, 24}) {
                final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/rise/font/Eaves.ttf");

                Font myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(Font.PLAIN, (float) i);
                this.fonts.put("Eaves " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }

        } catch (final Exception ignored) {
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10L);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
            while (!textureQueue.isEmpty()) {
                final TextureData textureData = textureQueue.poll();
                GlStateManager.bindTexture(textureData.getTextureId());
                GL11.glTexParameteri(3553, 10241, 9728);
                GL11.glTexParameteri(3553, 10240, 9728);
                GL11.glTexImage2D(3553, 0, 6408, textureData.getWidth(), textureData.getHeight(), 0, 6408, 5121, textureData.getBuffer());
            }
        }
    }
}
