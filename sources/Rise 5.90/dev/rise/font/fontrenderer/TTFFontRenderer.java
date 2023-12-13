package dev.rise.font.fontrenderer;

import dev.rise.module.impl.other.NameProtect;
import dev.rise.module.impl.render.Streamer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
public class TTFFontRenderer {

    private final boolean antiAlias;
    private final Font font;
    private boolean fractionalMetrics;
    private CharacterData[] regularData;
    private CharacterData[] boldData;
    private CharacterData[] italicsData;
    private final int[] colorCodes;
    private static int RANDOM_OFFSET;

    public TTFFontRenderer(final ExecutorService executorService, final ConcurrentLinkedQueue<TextureData> textureQueue, final Font font) {
        this(executorService, textureQueue, font, 256);
    }

    public TTFFontRenderer(final ExecutorService executorService, final ConcurrentLinkedQueue<TextureData> textureQueue, final Font font, final int characterCount) {
        this(executorService, textureQueue, font, characterCount, true);
    }

    public TTFFontRenderer(final ExecutorService executorService, final ConcurrentLinkedQueue<TextureData> textureQueue, final Font font, final boolean antiAlias) {
        this(executorService, textureQueue, font, 256, antiAlias);
    }

    public TTFFontRenderer(final ExecutorService executorService, final ConcurrentLinkedQueue<TextureData> textureQueue, final Font font, final int characterCount, final boolean antiAlias) {
        this.fractionalMetrics = false;
        this.colorCodes = new int[32];
        this.font = font;
        this.fractionalMetrics = true;
        this.antiAlias = antiAlias;
        final int[] regularTexturesIds = new int[characterCount];
        final int[] boldTexturesIds = new int[characterCount];
        final int[] italicTexturesIds = new int[characterCount];
        for (int i = 0; i < characterCount; ++i) {
            regularTexturesIds[i] = GL11.glGenTextures();
            boldTexturesIds[i] = GL11.glGenTextures();
            italicTexturesIds[i] = GL11.glGenTextures();
        }
        executorService.execute(() -> this.regularData = this.setup(new CharacterData[characterCount], regularTexturesIds, textureQueue, 0));
        executorService.execute(() -> this.boldData = this.setup(new CharacterData[characterCount], boldTexturesIds, textureQueue, 1));
        executorService.execute(() -> this.italicsData = this.setup(new CharacterData[characterCount], italicTexturesIds, textureQueue, 2));
    }

    private CharacterData[] setup(final CharacterData[] characterData, final int[] texturesIds, final ConcurrentLinkedQueue<TextureData> textureQueue, final int type) {
        this.generateColors();
        final Font font = this.font.deriveFont(type);
        final BufferedImage utilityImage = new BufferedImage(1, 1, 2);
        final Graphics2D utilityGraphics = (Graphics2D) utilityImage.getGraphics();
        utilityGraphics.setFont(font);
        final FontMetrics fontMetrics = utilityGraphics.getFontMetrics();


        for (int index = 0; index < characterData.length; ++index) {
            final char character = (char) index;
            final Rectangle2D characterBounds = fontMetrics.getStringBounds(character + "", utilityGraphics);
            final float width = (float) characterBounds.getWidth() + 8.0f;
            final float height = (float) characterBounds.getHeight();
            final BufferedImage characterImage = new BufferedImage(MathHelper.ceiling_double_int(width), MathHelper.ceiling_double_int(height), 2);
            final Graphics2D graphics = (Graphics2D) characterImage.getGraphics();
            graphics.setFont(font);
            graphics.setColor(new Color(255, 255, 255, 0));
            graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
            graphics.setColor(Color.WHITE);
            if (this.antiAlias) {
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            }
            graphics.drawString(character + "", 4, fontMetrics.getAscent());
            final int textureId = texturesIds[index];
            this.createTexture(textureId, characterImage, textureQueue);
            characterData[index] = new CharacterData(character, (float) characterImage.getWidth(), (float) characterImage.getHeight(), textureId);
        }
        return characterData;
    }

    private void createTexture(final int textureId, final BufferedImage image, final ConcurrentLinkedQueue<TextureData> textureQueue) {
        final int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                final int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) (pixel >> 16 & 0xFF));
                buffer.put((byte) (pixel >> 8 & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) (pixel >> 24 & 0xFF));
            }
        }
        buffer.flip();
        textureQueue.add(new TextureData(textureId, image.getWidth(), image.getHeight(), buffer));
    }

    public void drawString(final String text, final float x, final float y, final int color) {
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        this.renderString(text, x, y, color, false);
    }

    public void drawStringWithColorCode(final String text, final float x, final float y, final int color) {
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        this.renderString(text, x, y, color, false, true);
    }

    public void drawCenteredString(final String text, final float x, final float y, final int color) {
        final float width = this.getWidth(text) / 2.0f;
        this.renderString(text, x - width, y, color, false);
    }

    public void drawStringWithShadow(final String text, final float x, final float y, final int color) {
        GL11.glTranslated(0.5, 0.5, 0.0);
        this.renderString(text, x, y, color, true);
        GL11.glTranslated(-0.5, -0.5, 0.0);
        this.renderString(text, x, y, color, false);
    }

    private int renderString(String text, float x, float y, final int color, final boolean shadow) {
        if (text.equals("") || text.length() == 0) {
            return 0;
        }

        x = Math.round(x * 10.0F) / 10.0F;
        y = Math.round(y * 10.0F) / 10.0F;

        final Minecraft mc = Minecraft.getMinecraft();

        if (mc != null && mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.getName() != null && text.length() > 1) {
            if (NameProtect.enabled) {
                text = text.replaceAll(mc.thePlayer.getName(), "Rise User");
            }

            if (Streamer.enabled) {
                for (final String s : Streamer.PLAYERS) {
                    text = text.replaceAll(s, "Player");
                }
            }
        }

        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 1.0);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);

        x -= 2.0f;
        y -= 2.0f;
        x += 0.5f;
        y += 0.5f;
        x *= 2.0f;
        y *= 2.0f;

        final CharacterData[] characterData = this.regularData;

        final int length = text.length();
        final double multiplier = 255.0 * (shadow ? 4 : 1);

        final Color c = new Color(color);

        GL11.glColor4d(c.getRed() / multiplier, c.getGreen() / multiplier, c.getBlue() / multiplier, (color >> 24 & 0xFF) / 255.0);

        try {
            for (int i = 0; i < length; ++i) {
                final char character = text.charAt(i);
                this.drawChar(character, characterData, x, y);

                if (character >= characterData.length) continue;

                final CharacterData charData = characterData[character];
                x += charData.width - 8.0f;
            }
        } catch (final StringIndexOutOfBoundsException ex) {
            System.out.println("[Rise] Couldn't render text");
            ex.printStackTrace();
        }

        GL11.glPopMatrix();
        GlStateManager.disableBlend();
        GlStateManager.bindTexture(0);
        GlStateManager.resetColor();

        return (int) x;
    }

    private int renderString(String text, float x, float y, final int color, final boolean shadow, final boolean nickga) {
        if (text.equals("") || text.length() == 0) {
            return 0;
        }

        x = Math.round(x * 10.0F) / 10.0F;
        y = Math.round(y * 10.0F) / 10.0F;

        final Minecraft mc = Minecraft.getMinecraft();

        if (mc != null && mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.getName() != null && text.length() > 1) {
            if (NameProtect.enabled) {
                text = text.replaceAll(mc.thePlayer.getName(), "Rise User");
            }

            if (Streamer.enabled) {
                for (final String s : Streamer.PLAYERS) {
                    text = text.replaceAll(s, "Player");
                }
            }
        }

        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 1.0);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);

        x -= 2.0f;
        y -= 2.0f;
        x += 0.5f;
        y += 0.5f;
        x *= 2.0f;
        y *= 2.0f;

        CharacterData[] characterData = this.regularData;
        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;
        final int length = text.length();
        final double multiplier = 255.0 * (shadow ? 4 : 1);
        final Color c = new Color(color);
        GL11.glColor4d(c.getRed() / multiplier, c.getGreen() / multiplier, c.getBlue() / multiplier, (color >> 24 & 0xFF) / 255.0);
        for (int i = 0; i < length; ++i) {
            char character = text.charAt(i);
            final char previous = (i > 0) ? text.charAt(i - 1) : '.';
            if (previous != '§') {
                if (character == '§') {
                    try {
                        int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                        if (index < 16) {
                            obfuscated = false;
                            strikethrough = false;
                            underlined = false;
                            characterData = this.regularData;
                            if (index < 0) {
                                index = 15;
                            }
                            if (shadow) {
                                index += 16;
                            }
                            final int textColor = this.colorCodes[index];
                            GL11.glColor4d((textColor >> 16) / 255.0, (textColor >> 8 & 0xFF) / 255.0, (textColor & 0xFF) / 255.0, (color >> 24 & 0xFF) / 255.0);
                        } else if (index <= 20) {
                            switch (index) {
                                case 16:
                                    obfuscated = true;
                                    break;

                                case 17:
                                    characterData = this.boldData;
                                    break;

                                case 18:
                                    strikethrough = true;
                                    break;

                                case 19:
                                    underlined = true;
                                    break;

                                case 20:
                                    characterData = this.italicsData;
                                    break;
                            }
                        } else {
                            obfuscated = false;
                            strikethrough = false;
                            underlined = false;
                            characterData = this.regularData;
                            GL11.glColor4d((shadow ? 0.25 : 1.0), (shadow ? 0.25 : 1.0), (shadow ? 0.25 : 1.0), (color >> 24 & 0xFF) / 255.0);
                        }

                    } catch (final StringIndexOutOfBoundsException ignored) {
                    }
                } else if (character <= '\u00ff') {
                    if (obfuscated) {
                        character += (char) TTFFontRenderer.RANDOM_OFFSET;
                    }
                    this.drawChar(character, characterData, x, y);
                    final CharacterData charData = characterData[character];
                    if (strikethrough) {
                        this.drawLine(new Vector2f(0.0f, charData.height / 2.0f), new Vector2f(charData.width, charData.height / 2.0f), 3.0f);
                    }
                    if (underlined) {
                        this.drawLine(new Vector2f(0.0f, charData.height - 15.0f), new Vector2f(charData.width, charData.height - 15.0f), 3.0f);
                    }
                    x += charData.width - 8.0f;
                }
            }
        }

        GL11.glPopMatrix();
        GlStateManager.disableBlend();
        GlStateManager.bindTexture(0);
        GlStateManager.resetColor();

        return (int) x;
    }

    public float getWidth(final String text) {
        float width = 0.0f;
        final CharacterData[] characterData = this.regularData;

        try {
            for (int length = text.length(), i = 0; i < length; ++i) {
                final char character = text.charAt(i);

                final CharacterData charData = characterData[character];
                width += (charData.width - 8.0f) / 2.0f;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            return getWidth("A");
        }

        return width + 2.0f;
    }

    public float getWidthProtect(String text) {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc != null && mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.getName() != null && text.length() > 1) {
            if (NameProtect.enabled) {
                text = text.replaceAll(mc.thePlayer.getName(), "Rise User");
            }

            if (Streamer.enabled) {
                for (final String s : Streamer.PLAYERS) {
                    text = text.replaceAll(s, "Player");
                }
            }
        }

        float width = 0.0f;
        final CharacterData[] characterData = this.regularData;

        for (int length = text.length(), i = 0; i < length; ++i) {
            final char character = text.charAt(i);

            final CharacterData charData = characterData[character];
            width += (charData.width - 8.0f) / 2.0f;
        }

        return width + 2.0f;
    }

    public float getHeight(final String text) {
        float height = 0.0f;
        final CharacterData[] characterData = this.regularData;

        for (int length = text.length(), i = 0; i < length; ++i) {
            final char character = text.charAt(i);
            final CharacterData charData = characterData[character];
            height = Math.max(height, charData.height);
        }

        return height / 2.0f - 2.0f;
    }

    public float getHeight() {
        return getHeight("I");
    }

    private void drawChar(final char character, final CharacterData[] characterData, final float x, final float y) {
        if (character >= characterData.length) return;

        final CharacterData charData = characterData[character];
        charData.bind();

        GL11.glBegin(6);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2d(x, y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2d(x, y + charData.height);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2d(x + charData.width, y + charData.height);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2d(x + charData.width, y);
        GL11.glEnd();
    }

    private void drawLine(final Vector2f start, final Vector2f end) {
        GL11.glDisable(3553);
        GL11.glLineWidth((float) 3.0);
        GL11.glBegin(1);
        GL11.glVertex2f(start.x, start.y);
        GL11.glVertex2f(end.x, end.y);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    private void drawLine(final Vector2f start, final Vector2f end, final float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2f(start.x, start.y);
        GL11.glVertex2f(end.x, end.y);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    private void generateColors() {
        for (int i = 0; i < 32; ++i) {
            final int thingy = (i >> 3 & 0x1) * 85;
            int red = (i >> 2 & 0x1) * 170 + thingy;
            int green = (i >> 1 & 0x1) * 170 + thingy;
            int blue = (i & 0x1) * 170 + thingy;
            if (i == 6) {
                red += 85;
            }
            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCodes[i] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
        }
    }

    static {
        TTFFontRenderer.RANDOM_OFFSET = 1;
    }

    static class CharacterData {
        public char character;
        public float width;
        public float height;
        private final int textureId;

        public CharacterData(final char character, final float width, final float height, final int textureId) {
            this.character = character;
            this.width = width;
            this.height = height;
            this.textureId = textureId;
        }

        public void bind() {
            GL11.glBindTexture(3553, this.textureId);
        }
    }

    /**
     * Trims a string to fit a specified Width.
     */
    public String trimStringToWidth(final String text, final int width) {
        return this.trimStringToWidth(text, width, false);
    }

    /**
     * Trims a string to a specified width, and will reverse it if par3 is set.
     */
    public String trimStringToWidth(final String text, final int width, final boolean reverse) {
        final StringBuilder stringbuilder = new StringBuilder();
        float f = 0.0F;
        final int i = reverse ? text.length() - 1 : 0;
        final int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;

        for (int k = i; k >= 0 && k < text.length() && f < (float) width; k += j) {
            final char c0 = text.charAt(k);
            final float f1 = this.getWidth(String.valueOf(c0));

            if (flag) {
                flag = false;

                if (c0 != 108 && c0 != 76) {
                    if (c0 == 114 || c0 == 82) {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (f1 < 0.0F) {
                flag = true;
            } else {
                f += f1;

                if (flag1) {
                    ++f;
                }
            }

            if (f > (float) width) {
                break;
            }

            if (reverse) {
                stringbuilder.insert(0, c0);
            } else {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }
}
