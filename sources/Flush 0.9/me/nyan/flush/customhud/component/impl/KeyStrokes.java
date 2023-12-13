package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.setting.impl.BooleanSetting;
import me.nyan.flush.customhud.setting.impl.FontSetting;
import me.nyan.flush.customhud.setting.impl.NumberSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.PboUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector2f;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class KeyStrokes extends Component {
    private FontSetting font;
    private BooleanSetting fontShadow;
    private NumberSetting quality;

    private GlyphPageFontRenderer fr;

    private final Key[] keys = new Key[] {
            new Key(),
            new Key(), new Key(), new Key(),
            new Key()
    };
    private final KeyBinding[] keyBindings = new KeyBinding[]{
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindJump
    };
    private final Vector2f[] positions = new Vector2f[]{
            new Vector2f(1, 0),
            new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(2, 1),
            new Vector2f(0, 2)
    };

    @Override
    public void onAdded() {
        settings.add(font = new FontSetting("Font", "Roboto Light", 30));
        settings.add(fontShadow = new BooleanSetting("Font Shadow", true));
        settings.add(quality = new NumberSetting("Quality", 3, 0, 10, 1));
    }

    @Override
    public void draw(float x, float y) {
        if (mc.currentScreen == null) {
            Keyboard.enableRepeatEvents(false);
        }

        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        int i = 0;
        for (Key key : keys) {
            key.setCode(keyBindings[i].getKeyCode());
            Vector2f position = positions[i];
            key.draw(x + position.x * 33, y + position.y * 33);
            i++;
        }
    }

    @Override
    public void onKey(int code) {
        for (Key key : keys) {
            key.onKey(code);
        }
    }

    @Override
    public int width() {
        return 30 * 3 + 3 * 2;
    }

    @Override
    public int height() {
        return 30 * 2 + 15 + 3 * 2;
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.CUSTOM;
    }

    private class Key {
        private int keyCode;
        private ArrayList<Float> circles = new ArrayList<>();

        private int startColorA;
        private int endColorA;

        private final PboUtils pbos = new PboUtils(2);

        public void draw(float x, float y) {
            fr = Flush.getFont(font.getFont(), Math.min(font.getSize(), 40));

            boolean keyDown = Keyboard.isKeyDown(keyCode);

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

            if (quality.getValue() > 0) {
                float factor = new ScaledResolution(mc).getScaleFactor();
                float readWidth = width() / (11 - quality.getValueFloat());

                pbos.init(0, readWidth, 1 / factor);
                pbos.init(1, readWidth, 1 / factor);
                int startColor = ColorUtils.brightness(pbos.getAverageColor(0), 0.95F);
                int endColor = ColorUtils.brightness(pbos.getAverageColor(1), 0.95F);
                pbos.update(0, x + width() / 2F - readWidth / 2F, y + 1);
                pbos.update(1, x + width() / 2F - readWidth / 2F, y + height() - 1);

                startColorA = ColorUtils.animateColor2(startColorA, startColor, 8);
                endColorA = ColorUtils.animateColor2(endColorA, endColor, 8);

                RenderUtils.drawGradientRect(0, 0, width(), height(), startColorA, endColorA);
            } else {
                Gui.drawRect(0, 0, width(), height(), 0x40000000);
            }

            if (keyCode == Keyboard.KEY_SPACE) {
                if (fontShadow.getValue()) {
                    Gui.drawRect(16, height() / 2 + 1, width() - 14, height() / 2 + 2, 0xAA000000);
                }
                Gui.drawRect(15, height() / 2, width() - 15, height() / 2 + 1, -1);
            } else {
                drawCenteredString(Keyboard.getKeyName(keyCode), width() / 2 - 1, height() / 2 + 1);
            }

            glEnable(GL_SCISSOR_TEST);
            RenderUtils.glScissor(x + 0.5F, y, x + width() + 0.5F, y - 0.5F + height());
            for (float circle : circles) {
                if (circle > 0) {
                    int color = ColorUtils.alpha(-1, Math.min(circle > 1 ? 50 * (1 - (circle - 1)) : 50, 50));
                    RenderUtils.fillCircle(width() / 2, height() / 2, Math.min(circle, 1) * width(), color);
                }
            }
            glDisable(GL_SCISSOR_TEST);

            GlStateManager.popMatrix();
        }

        public void onKey(int key) {
            if (key != keyCode) {
                return;
            }

            circles.add(0F);
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

        private int width() {
            return keyCode == Keyboard.KEY_SPACE ? 96 : 30;
        }

        private int height() {
            return keyCode == Keyboard.KEY_SPACE ? 15 : 30;
        }

        public void setCode(int keyCode) {
            this.keyCode = keyCode;
        }
    }
}
