package me.aquavit.liquidsense.ui.font;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.events.TextEvent;
import me.aquavit.liquidsense.utils.mc.ClassUtils;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.render.shader.shaders.RainbowFontShader;
import me.aquavit.liquidsense.module.modules.client.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;

public class GameFontRenderer extends FontRenderer {

    private AWTFontRenderer defaultFont;
    private AWTFontRenderer boldFont;
    private AWTFontRenderer italicFont;
    private AWTFontRenderer boldItalicFont;

    public GameFontRenderer(Font font) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), ClassUtils.hasForge() ? null :
                        Minecraft.getMinecraft().getTextureManager(), false);
        this.defaultFont = new AWTFontRenderer(font);
        this.boldFont = new AWTFontRenderer(font.deriveFont(Font.BOLD));
        this.italicFont = new AWTFontRenderer(font.deriveFont(Font.ITALIC));
        this.boldItalicFont = new AWTFontRenderer(font.deriveFont(Font.BOLD | Font.ITALIC));
        this.FONT_HEIGHT = this.getHeight();
    }

    public AWTFontRenderer getDefaultFont() {
        return this.defaultFont;
    }

    public void setDefaultFont(AWTFontRenderer aWTFontRenderer) {
        this.defaultFont = aWTFontRenderer;
    }

    public int getHeight() {
        return this.defaultFont.getHeight();
    }

    public int getSize() {
        Font font = defaultFont.getFont();
        return font.getSize();
    }

    public int drawString(String s, float x, float y, int color) {
        return drawString(s, x, y, color, false);
    }

    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        return drawString(text, x, y, color, true);
    }

    public int drawCenteredString(String s, float x, float y, int color, boolean shadow) {
        return drawString(s, x - (float)this.getStringWidth(s) / 2.0f, y, color, shadow);
    }

    public int drawCenteredString(String s, float x, float y, int color) {
        return drawStringWithShadow(s, x - (float)this.getStringWidth(s) / 2.0f, y, color);
    }

    @Override
    public int drawString(String text, float x, float y, int color, boolean shadow) {
        String currentText = text;
        TextEvent event = new TextEvent(currentText);
        LiquidSense.eventManager.callEvent(event);
        String getText = event.getText();
        if (getText == null) return 0;
        currentText = getText;

        float currY = y - 3.0f;

        boolean rainbow = RainbowFontShader.INSTANCE.isInUse();

        if (shadow) {
            switch (HUD.fontShadow.get().toLowerCase()) {
                case "liquidbounce":
                    drawText(currentText, x + 1f, currY + 1f, new Color(0, 0, 0, 150).getRGB(), true, false);
                    break;
                case "default":
                    drawText(currentText, x + 0.5f, currY + 0.5f, new Color(0, 0, 0, 130).getRGB(), true, false);
                    break;
                case "autumn":
                    drawText(currentText, x + 1f, currY + 1f, new Color(20, 20, 20, 200).getRGB(), true, false);
                    break;
                case "outline": {
                    drawText(currentText, x + 0.5f, currY + 0.5f, new Color(0, 0, 0, 130).getRGB(), true, false);
                    drawText(currentText, x - 0.5f, currY - 0.5f, new Color(0, 0, 0, 130).getRGB(), true, false);
                    drawText(currentText, x + 0.5f, currY - 0.5f, new Color(0, 0, 0, 130).getRGB(), true, false);
                    drawText(currentText, x - 0.5f, currY + 0.5f, new Color(0, 0, 0, 130).getRGB(), true, false);
                    break;
                }

            }
        }


        return drawText(currentText, x, currY, color, false, rainbow);
    }

    private int drawText(String text, float x, float y, int color, boolean ignoreColor, boolean rainbow) {
        if (text == null)
            return 0;

        if (text.isEmpty())
            return (int)x;

        int rainbowShaderId = RainbowFontShader.INSTANCE.getProgramId();

        if (rainbow) GL20.glUseProgram(rainbowShaderId);

        GlStateManager.translate(x - 1.5, y + 0.5, 0.0);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.enableTexture2D();

        int currentColor = color;

        if ((currentColor & 0xFC000000) == 0) currentColor |= 0xFF000000;

        int defaultColor = currentColor;
        int alpha = currentColor >> 24 & 0xFF;

        if (text.contains("§")) {
            String[] parts = text.split("§");
            AWTFontRenderer currentFont = defaultFont;
            int width = 0;
            boolean randomCase = false;
            boolean bold = false;
            boolean italic = false;
            boolean strikeThrough = false;
            boolean underline = false;

            int index = 0;
            for (String part : parts) {
                if (!part.isEmpty()) {
                    if (index == 0) {
                        currentFont.drawString(part, width, 0.0, currentColor);
                        width += currentFont.getStringWidth(part);
                    } else {
                        String words = part.substring(1);
                        char type = part.charAt(0);

                        final int colorIndex = "0123456789abcdefklmnor".indexOf(type);
                        if (colorIndex != -1) {
                            if (colorIndex < 16) {
                                if (!ignoreColor) {
                                    currentColor = ColorUtils.hexColors[colorIndex] | alpha << 24;
                                    if (rainbow)
                                        GL20.glUseProgram(0);
                                }
                                bold = false;
                                italic = false;
                                randomCase = false;
                                underline = false;
                                strikeThrough = false;
                            } else if (colorIndex == 16) {
                                randomCase = true;
                            } else if (colorIndex == 17) {
                                bold = true;
                            } else if (colorIndex == 18) {
                                strikeThrough = true;
                            } else if (colorIndex == 19) {
                                underline = true;
                            } else if (colorIndex == 20) {
                                italic = true;
                            } else if (colorIndex == 21) {
                                currentColor = color;

                                if ((currentColor & 0xFC000000) == 0) {
                                    currentColor |= 0xFF000000;
                                }

                                if (rainbow)
                                    GL20.glUseProgram(rainbowShaderId);

                                bold = false;
                                italic = false;
                                randomCase = false;
                                underline = false;
                                strikeThrough = false;
                            }
                        }
                        currentFont = bold && italic ? this.boldItalicFont : (bold ? this.boldFont : (italic ? this.italicFont : this.defaultFont));
                        currentFont.drawString(randomCase ? ColorUtils.randomMagicText(words) : words, width, 0.0, currentColor);
                        if (strikeThrough) {
                            RenderUtils.drawLine(width / 2.0 + 1.0, (double) currentFont.getHeight() / 3.0, (width + (double) currentFont.getStringWidth(words)) / 2.0 + 1.0, (double) currentFont.getHeight() / 3.0, (float) this.FONT_HEIGHT / 16.0f);
                        }
                        if (underline) {
                            RenderUtils.drawLine(width / 2.0 + 1.0, (double) currentFont.getHeight() / 2.0, (width + (double) currentFont.getStringWidth(words)) / 2.0 + 1.0, (double) currentFont.getHeight() / 2.0, (float) this.FONT_HEIGHT / 16.0f);
                        }
                        width += (double) currentFont.getStringWidth(words);
                    }

                }
                index++;
            }

        } else {
            defaultFont.drawString(text, 0.0, 0.0, currentColor);
        }

        GlStateManager.disableBlend();
        GlStateManager.translate(-(x - 1.5), -(y + 0.5), 0.0);
        GlStateManager.color(1f, 1f, 1f, 1f);

        return (int) (x + getStringWidth(text));

    }

    @Override
    public int getColorCode(char charCode) {
        return ColorUtils.hexColors["0123456789abcdef".indexOf(charCode)];
    }

    @Override
    public int getStringWidth(String text) {
        String currentText = text;
        TextEvent event = new TextEvent(currentText);
        LiquidSense.eventManager.callEvent(event);
        String getText = event.getText();
        if (getText == null) return 0;
        currentText = getText;

        if (text.contains("§")) {
            final String[] parts = text.split("§");
            AWTFontRenderer currentFont = defaultFont;
            int width = 0;
            boolean bold = false;
            boolean italic = false;

            int index = 0;
            for (String part : parts) {
                if (!part.isEmpty()) {
                    if (index == 0) {
                        width += currentFont.getStringWidth(part);
                    } else {
                        String words = part.substring(1);
                        char type = part.charAt(0);
                        int colorIndex = "0123456789abcdefklmnor".indexOf(type);
                        if (colorIndex != -1) {
                            if (colorIndex < 16) {
                                bold = false;
                                italic = false;
                            }
                            else if (colorIndex == 17) {
                                bold = true;
                            }
                            else if (colorIndex == 20) {
                                italic = true;
                            }
                            else if (colorIndex == 21) {
                                bold = false;
                                italic = false;
                            }
                        }
                        if (bold && italic) {
                            currentFont = this.boldItalicFont;
                        }
                        else if (bold) {
                            currentFont = this.boldFont;
                        }
                        else if (italic) {
                            currentFont = this.italicFont;
                        }
                        else {
                            currentFont = this.defaultFont;
                        }
                        width += currentFont.getStringWidth(words);
                    }
                }
                index++;
            }
            return width;
        }
        return defaultFont.getStringWidth(text);
    }

    @Override
    public int getCharWidth(char character) {
        return getStringWidth(String.valueOf(character));
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) { }

    @Override
    protected void bindTexture(ResourceLocation location) { }

}
