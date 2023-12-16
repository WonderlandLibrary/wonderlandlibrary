package wtf.expensive.util.font.styled;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import wtf.expensive.util.font.Wrapper;
import wtf.expensive.util.render.RenderUtil;

public final class StyledFontRenderer implements Wrapper {

    public static final String STYLE_CODES = "0123456789abcdefklmnor";
    public static final int[] COLOR_CODES = new int[32];

    static {
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;

            if (i == 6) {
                k += 85;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            COLOR_CODES[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }

    public static float drawString(MatrixStack matrices, StyledFont font, String text, double x, double y, int color) {
        return renderString(matrices, font, text, x, y, false, color);
    }

    public static float drawString(MatrixStack matrices, StyledFont font, ITextComponent text, double x, double y, int color) {
        return renderString(matrices, font, text, x, y, false, color);
    }


    public static float drawShadowedString(MatrixStack matrices, StyledFont font, ITextComponent text, double x, double y, int color) {
        return StyledFontRenderer.drawShadowITextComponentString(matrices, font, text, x, y, color, StyledFontRenderer.getShadowColor(color));
    }

    public static float drawShadowITextComponentString(MatrixStack matrices, StyledFont font, ITextComponent text, double x, double y, int color, int shadowColor) {
        renderString(matrices, font, text, x + 1.0f, y, true, shadowColor);
        return renderString(matrices, font, text, x, y - 1.0f, false, color) + 1.0f;
    }

    public static float drawCenteredXString(MatrixStack matrices, StyledFont font, String text, double x, double y, int color) {
        return renderString(matrices, font, text, x - font.getWidth(text) / 2.0f, y, false, color);
    }

    public static void drawCenteredString(MatrixStack matrixStack, StyledFont font, ITextComponent text, double x, double y, int color) {
        renderString(matrixStack, font, text, x - font.getWidth(text.getString()) / 2.0f, y, false, color);
    }

    public static void drawCenteredString(MatrixStack matrixStack, StyledFont font, String text, double x, double y, int color) {
        renderString(matrixStack, font, text, x - font.getWidth(text) / 2.0f, y, false, color);
    }

    public static float drawCenteredYString(MatrixStack matrices, StyledFont font, String text, double x, double y, int color) {
        return renderString(matrices, font, text, x, y + font.getLifting() / 2.0f + 0.5f, false, color);
    }

    public static float drawCenteredXYString(MatrixStack matrices, StyledFont font, String text, double x, double y, int color) {
        return renderString(matrices, font, text, x - font.getWidth(text) / 2.0f, y + font.getLifting() / 2.0f + 0.5f, false, color);
    }

    public static float drawShadowedString(MatrixStack matrices, StyledFont font, String text, double x, double y, int color) {
        return renderStringWithShadow(matrices, font, text, x, y, color, getShadowColor(color));
    }


    private static float renderStringWithShadow(MatrixStack matrices, StyledFont font, String text, double x, double y, int color, int shadowColor) {
        renderString(matrices, font, text, x + 1.0f, y, true, shadowColor);
        return renderString(matrices, font, text, x, y - 1.0f, false, color) + 1.0f;
    }

    private static float renderString(MatrixStack matrices, StyledFont font, String text, double x, double y, boolean shadow, int color) {
        y -= 3;
        GL11.glColor4f(1, 1, 1, 1);

        float startPos = (float) x * 2.0f;
        float posX = startPos;
        float posY = (float) y * 2.0f;

        float[] rgb = RenderUtil.IntColor.rgb(color);
        float red = rgb[0];
        float green = rgb[1];
        float blue = rgb[2];
        float alpha = rgb[3];


        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        matrices.push();
        matrices.scale(0.5f, 0.5f, 1f);

        Matrix4f matrix = matrices.getLast().getMatrix();
        int length = text.length();
        String lowerCaseText = text.toLowerCase();

        for (int i = 0; i < length; i++) {
            char c0 = text.charAt(i);

            if (c0 == 167 && i + 1 < length && STYLE_CODES.indexOf(lowerCaseText.charAt(i + 1)) != -1) {
                int i1 = STYLE_CODES.indexOf(lowerCaseText.charAt(i + 1));

                if (i1 < 16) {
                    if (shadow) {
                        i1 += 16;
                    }

                    int j1 = COLOR_CODES[i1];

                    red = (float) (j1 >> 16 & 255) / 255.0F;
                    green = (float) (j1 >> 8 & 255) / 255.0F;
                    blue = (float) (j1 & 255) / 255.0F;
                }
                i++;
            } else {
                posX += font.renderGlyph(matrix, c0, posX, posY, false, false, red, green, blue, alpha);
            }
        }

        matrices.pop();
        GlStateManager.disableBlend();

        return (posX - startPos) / 2.0f;
    }

    public static float renderStringGradient(MatrixStack matrices, StyledFont font, ITextComponent text, double x, double y, boolean shadow, int color) {
        y -= 3;
        GL11.glColor4f(1, 1, 1, 1);

        float startPos = (float) x * 2.0f;
        float posX = startPos;
        float posY = (float) y * 2.0f;

        float[] rgb = RenderUtil.IntColor.rgb(color);






        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        matrices.push();
        matrices.scale(0.5f, 0.5f, 1f);

        Matrix4f matrix = matrices.getLast().getMatrix();


//        float red = 1;
//        float green = 1;
//        float blue = 1;
//        float alpha = 1;
        //System.out.println(text.deepCopy().func_241878_f().accept());
        float red = 1;
        float green = 1;
        float blue = 1;
        for (ITextComponent textComponent : text.getSiblings()) {

            if (textComponent.getSiblings().isEmpty()) {
                {
                    String texto = !text.getSiblings().get(0).getString().isEmpty() ? " " + textComponent.getString() : textComponent.getString();
                    int length = texto.length();
                    String lowerCaseText = texto.toLowerCase();

                    for (int i = 0; i < length; i++) {
                        char c0 = texto.charAt(i);
                        if (c0 == 167 && i + 1 < length && STYLE_CODES.indexOf(lowerCaseText.charAt(i + 1)) != -1) {
                            int i1 = STYLE_CODES.indexOf(lowerCaseText.charAt(i + 1));

                            if (i1 < 16) {
                                if (shadow) {
                                    i1 += 16;
                                }
                                int j1 = COLOR_CODES[i1];
                                red = (float) (j1 >> 16 & 255) / 255.0F;
                                green = (float) (j1 >> 8 & 255) / 255.0F;
                                blue = (float) (j1 & 255) / 255.0F;
                            }
                            i++;
                        } else {
                            System.out.println(red + " " + green + " " + blue);
                            posX += font.renderGlyph(matrix, c0, posX, posY, false, false, red, green, blue, 1);
                        }
                    }
                }
            }
            for (ITextComponent textComponent1 : textComponent.getSiblings()) {
                if (textComponent1.getString().isEmpty()) continue;
                char c0 = textComponent1.getString().charAt(0);
                float r1 = 1,r2 = 1,r3 = 1,r4 = 1;
                if (textComponent1.getStyle().getColor() != null) {
                    rgb = RenderUtil.IntColor.rgb(textComponent1.getStyle().getColor().getColor());
                    r1 = rgb[0];
                    r2 = rgb[1];
                    r3 = rgb[2];
                    r4 = 1;
                }
                posX += font.renderGlyph(matrix, c0, posX, posY, false, false, r1,r2,r3,r4);
            }
        }

        matrices.pop();
        GlStateManager.disableBlend();

        return (posX - startPos) / 2.0f;
    }


    // returns string width
    private static float renderString(MatrixStack matrices, StyledFont font, ITextComponent text, double x, double y, boolean shadow, int color) {
        y -= 3;
        GL11.glColor4f(1, 1, 1, 1);

        float startPos = (float) x * 2.0f;
        float posX = startPos;
        float posY = (float) y * 2.0f;
        float red = 1;
        float green = 1;
        float blue = 1;
        float alpha = 1;
        boolean bold = false;
        boolean italic = false;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        matrices.push();
        matrices.scale(0.5f, 0.5f, 1f);

        Matrix4f matrix = matrices.getLast().getMatrix();

        for (int i = 0; i < text.getString().length(); i++) {
            if (i >= text.getSiblings().size()) {
                char c0 = text.getString().charAt(i);

                if (c0 == 167 && i + 1 < text.getString().length() && STYLE_CODES.indexOf(text.getString().toLowerCase().charAt(i + 1)) != -1) {
                    int i1 = STYLE_CODES.indexOf(text.getString().toLowerCase().charAt(i + 1));

                    if (i1 < 16) {
                        if (shadow) {
                            i1 += 16;
                        }

                        int j1 = COLOR_CODES[i1];

                        red = (float) (j1 >> 16 & 255) / 255.0F;
                        green = (float) (j1 >> 8 & 255) / 255.0F;
                        blue = (float) (j1 & 255) / 255.0F;
                    }
                    i++;
                } else {
                    posX += font.renderGlyph(matrix, c0, posX, posY, false, false, red, green, blue, alpha);
                }
                continue;
            }
            ITextComponent c = text.getSiblings().get(i);
            if (c.getString().isEmpty()) {
                char c0 = text.getString().charAt(i);

                if (c0 == 167 && i + 1 < text.getString().length() && STYLE_CODES.indexOf(text.getString().toLowerCase().charAt(i + 1)) != -1) {
                    int i1 = STYLE_CODES.indexOf(text.getString().toLowerCase().charAt(i + 1));

                    if (i1 < 16) {
                        if (shadow) {
                            i1 += 16;
                        }

                        int j1 = COLOR_CODES[i1];

                        red = (float) (j1 >> 16 & 255) / 255.0F;
                        green = (float) (j1 >> 8 & 255) / 255.0F;
                        blue = (float) (j1 & 255) / 255.0F;
                    }
                    i++;
                } else {
                    posX += font.renderGlyph(matrix, c0, posX, posY, false, false, red, green, blue, alpha);
                }
                continue;
            }
            char c0 = c.getString().charAt(0);

            if (c.getStyle().getColor() != null) {
                int col = c.getStyle().getColor().getColor();
                red = (float) (col >> 16 & 255) / 255.0F;
                green = (float) (col >> 8 & 255) / 255.0F;
                blue = (float) (col & 255) / 255.0F;
            }

            float f = font.renderGlyph(matrix, c0, posX, posY, bold, italic, red, green, blue, alpha);
            posX += f;

        }


        matrices.pop();
        GlStateManager.disableBlend();

        return (posX - startPos) / 2.0f;
    }

    public static int getShadowColor(int color) {
        return new Color((color & 16579836) >> 2 | color & -16777216).getRGB();
    }

}
