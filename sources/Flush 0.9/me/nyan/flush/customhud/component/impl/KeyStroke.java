package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.setting.impl.BooleanSetting;
import me.nyan.flush.customhud.setting.impl.FontSetting;
import me.nyan.flush.customhud.setting.impl.TextSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class KeyStroke extends Component {
    private FontSetting font;
    private BooleanSetting fontShadow;
    private TextSetting text;
    private GlyphPageFontRenderer fr;
    private ArrayList<Float> circles = new ArrayList<>();

    @Override
    public void onAdded() {
        settings.add(font = new FontSetting("Font", "Roboto Light", 30));
        settings.add(fontShadow = new BooleanSetting("Font Shadow", true));
        settings.add(text = new TextSetting("Key", "W"));
    }

    @Override
    public void draw(float x, float y) {
        if (text.getValue().length() > 1) {
            text.setValue(text.getValue().substring(0, 1));
        }
        fr = Flush.getFont(font.getFont(), Math.min(font.getSize(), 40));

        int keyIndex = getKeyIndex();
        if (keyIndex == Keyboard.KEY_NONE) {
            return;
        }

        boolean keyDown = Keyboard.isKeyDown(keyIndex);
        int keyColor = 0x40000000;

        int i = 0;
        ArrayList<Float> newCircles = new ArrayList<>();
        for (float circle : circles) {
            if (i == circles.size() - 1 && keyDown) {
                if (circle >= 1) {
                    newCircles.add(1F);
                    continue;
                }
            }
            if (circle >= 2) {
                i++;
                continue;
            }
            newCircles.add(Math.min(circle + 0.0025F * Flush.getFrameTime(), 2F));
            i++;
        }
        circles = newCircles;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);

        //BlurUtil.blur(x, y, width(), height(), 30);
        Gui.drawRect(0, 0, width(), height(), keyColor);
        if (text.getValue().equals(" ")) {
            if (fontShadow.getValue()) {
                Gui.drawRect(16, height() / 2 + 1, width() - 14, height() / 2 + 2, 0xAA000000);
            }
            Gui.drawRect(15, height() / 2, width() - 15, height() / 2 + 1, -1);
        } else {
            drawCenteredString(Keyboard.getKeyName(keyIndex), width() / 2 - 1, height() / 2 + 1);
        }

        glEnable(GL_SCISSOR_TEST);
        RenderUtils.glScissor(x, y + 0.25, x + width(), y + height());
        for (float circle : circles) {
            if (circle > 0) {
                int color = ColorUtils.alpha(-1, Math.min(circle > 1 ? 50 * (1 - (circle - 1)) : 50, 50));
                RenderUtils.fillCircle(width() / 2, height() / 2, Math.min(circle, 1) * width(), color);
            }
        }
        glDisable(GL_SCISSOR_TEST);

        GlStateManager.popMatrix();
    }

    @Override
    public void onKey(int key) {
        int keyIndex = getKeyIndex();
        if (keyIndex == Keyboard.KEY_NONE) {
            return;
        }
        if (key != keyIndex) {
            return;
        }

        circles.add(0F);
    }

    private int getKeyIndex() {
        return Keyboard.getKeyIndex(text.getValue().replace(" ", "space").toUpperCase());
    }

    private void drawCenteredString(String s, float x, float y) {
        if (font.isMinecraftFont()) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(2, 2, 1);
            mc.fontRendererObj.drawString(
                    s,
                    x / 2 - mc.fontRendererObj.getStringWidth(s) / 2F + 1,
                    y / 2 - mc.fontRendererObj.FONT_HEIGHT / 2F + 1,
                    -1,
                    fontShadow.getValue()
            );
            GlStateManager.popMatrix();
        } else {
            fr.drawString(
                    s,
                    x - fr.getStringWidth(s) / 2F,
                    y - fr.getFontHeight() / 2F,
                    -1,
                    fontShadow.getValue()
            );
        }
    }

    @Override
    public int width() {
        return text.getValue().equals(" ") ? 100 : 30;
    }

    @Override
    public int height() {
        return text.getValue().equals(" ") ? 15 : 30;
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.CUSTOM;
    }
}
