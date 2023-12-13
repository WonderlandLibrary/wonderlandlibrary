/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package markgg.ui;

import java.awt.Color;
import java.io.IOException;
import markgg.Client;
import markgg.modules.Module;
import markgg.settings.BooleanSetting;
import markgg.settings.KeybindSetting;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import markgg.settings.Setting;
import markgg.utilities.ColorUtil;
import markgg.utilities.font.CustomFontRenderer;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GUIMethod
extends GuiScreen {
    boolean isClicked;
    int key;
    boolean ChangingKey;
    NumberSetting dragging;

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.DrawClickGUI(mouseX, mouseY, mouseButton, ClickType.CLICK);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void playPressSound() {
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
    }

    public void playDeepPressSound() {
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 0.7f));
    }

    public void DrawClickGUI(int mouseX, int mouseY, int button, ClickType type) {
        CustomFontRenderer fr = Client.customFont;
        int primaryColor = ColorUtil.getRainbow(4.0f, 1.0f, 1.0f);
        int secondaryColor = ColorUtil.getRainbow(4.0f, 1.0f, 1.0f);
        this.drawDefaultBackground();
        GUIMethod.drawRect(150.0, 2.0, 220.0, 14.0, secondaryColor);
        fr.drawShadedString("Combat", 152.0, 4.0, new Color(255, 255, 255));
        GUIMethod.drawRect(250.0, 2.0, 320.0, 14.0, secondaryColor);
        fr.drawShadedString("Render", 252.0, 4.0, new Color(255, 255, 255));
        GUIMethod.drawRect(350.0, 2.0, 420.0, 14.0, secondaryColor);
        fr.drawShadedString("Movement", 352.0, 4.0, new Color(255, 255, 255));
        GUIMethod.drawRect(450.0, 2.0, 520.0, 14.0, secondaryColor);
        fr.drawShadedString("Player", 452.0, 4.0, new Color(255, 255, 255));
        GUIMethod.drawRect(550.0, 2.0, 620.0, 14.0, secondaryColor);
        fr.drawShadedString("Ghost", 552.0, 4.0, new Color(255, 255, 255));
        for (Module.Category c : Module.Category.values()) {
            int count = 1;
            for (Module m : Client.modules) {
                if (m.category != c) continue;
                GUIMethod.drawRect(this.getXLoc(m), (int)((double)count * 13.9), this.getXLoc2(m), (int)((double)count * 13.9 + 14.0), this.getSuitedColor(m));
                fr.drawShadedString(m.name, this.getXLoc(m) + 2, (float)((double)count * 13.9 + 2.0), new Color(255, 255, 255));
                if (GUIMethod.isHovered(this.getXLoc(m), (int)((double)count * 13.9), this.getXLoc2(m), (int)((double)count * 13.9 + 14.0), mouseX, mouseY)) {
                    if (Mouse.isButtonDown((int)0) && !this.isClicked) {
                        m.toggle();
                        if (m.isEnabled()) {
                            this.playPressSound();
                        } else {
                            this.playDeepPressSound();
                        }
                        this.isClicked = true;
                    }
                    if (Mouse.isButtonDown((int)1) && !this.isClicked) {
                        m.expanded = !m.expanded;
                        this.isClicked = true;
                    }
                }
                ++count;
                if (!m.expanded) continue;
                for (Setting setting : m.settings) {
                    if (setting instanceof BooleanSetting) {
                        BooleanSetting bool = (BooleanSetting)setting;
                        GUIMethod.drawRect(this.getXLoc(m), (int)((double)count * 13.9), this.getXLoc2(m), (int)((double)count * 13.9 + 14.0), -1493172224);
                        fr.drawShadedString(String.valueOf(setting.name) + ": " + bool.isEnabled(), this.getXLoc(m) + 2, (float)((double)count * 13.9 + 2.0), new Color(255, 255, 255));
                        if (GUIMethod.isHovered(this.getXLoc(m), (int)((double)count * 13.9), this.getXLoc2(m), (int)((double)count * 13.9 + 14.0), mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.isClicked) {
                            bool.toggle();
                            this.isClicked = true;
                        }
                        ++count;
                    }
                    if (setting instanceof ModeSetting) {
                        ModeSetting mode = (ModeSetting)setting;
                        GUIMethod.drawRect(this.getXLoc(m), (int)((double)count * 13.9), this.getXLoc2(m), (int)((double)count * 13.9 + 14.0), -1879048192);
                        fr.drawShadedString(String.valueOf(setting.name) + ": " + mode.getMode(), this.getXLoc(m) + 2, (float)((double)count * 13.9 + 2.0), new Color(255, 255, 255));
                        if (GUIMethod.isHovered(this.getXLoc(m), (int)((double)count * 13.9), this.getXLoc2(m), (int)((double)count * 13.9 + 14.0), mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.isClicked) {
                            mode.cycle();
                            this.isClicked = true;
                        }
                        ++count;
                    }
                    if (setting instanceof NumberSetting) {
                        int width = 70;
                        NumberSetting number = (NumberSetting)setting;
                        double percent = (number.getValue() - number.getMinimum()) / (number.getMaximum() - number.getMinimum());
                        double numberWidth = percent * (double)width;
                        GUIMethod.drawRect(this.getXLoc(m), (int)((double)count * 13.9), this.getXLoc2(m), (int)((double)count * 13.9 + 14.0), -1879048192);
                        if (this.dragging != null && this.dragging == number) {
                            double largeMousePercent = (float)(mouseX - this.getXLoc(m)) / 70.0f;
                            double mousePercent = (double)Math.round(largeMousePercent * 100.0) / 100.0;
                            double newValue = mousePercent * (number.getMaximum() - number.getMinimum()) + number.getMinimum();
                            if (newValue < 1.0) {
                                newValue = 1.0;
                            }
                            if (newValue > number.getMaximum()) {
                                newValue = number.getMaximum();
                            }
                            newValue = (double)Math.round(newValue * 100.0) / 100.0;
                            System.out.println(newValue);
                            number.setValue(newValue);
                        }
                        fr.drawShadedString(String.valueOf(number.name) + ": " + number.getValue(), this.getXLoc(m), (float)((double)count * 13.9 + 2.0), new Color(255, 255, 255));
                        Gui.drawRect(this.getXLoc(m), (int)((double)count * 13.9 + 10.0), (int)((double)this.getXLoc(m) + numberWidth), (int)((double)count * 13.9 + 12.0), secondaryColor);
                        if (GUIMethod.isHovered(this.getXLoc(m), (int)((double)count * 13.9), this.getXLoc2(m), (int)((double)count * 13.9 + 14.0), mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
                            this.dragging = number;
                            this.isClicked = true;
                        }
                        ++count;
                    }
                    if (!(setting instanceof KeybindSetting)) continue;
                    KeybindSetting keybind = (KeybindSetting)setting;
                    String name = null;
                    String changingmodule = null;
                    if (GUIMethod.isHovered(this.getXLoc(m), (int)((double)count * 13.9), this.getXLoc2(m), (int)((double)count * 13.9 + 14.0), mouseX, mouseY)) {
                        int key;
                        if (Mouse.isButtonDown((int)0)) {
                            changingmodule = m.name;
                            this.ChangingKey = true;
                            this.isClicked = true;
                        }
                        if (this.ChangingKey && Keyboard.isKeyDown((int)(key = Keyboard.getEventKey()))) {
                            m.keyCode.code = key == 57 ? 0 : key;
                        }
                    }
                    name = this.ChangingKey ? (changingmodule == m.name ? "Changing..." : String.valueOf(setting.name) + ": " + Keyboard.getKeyName((int)m.keyCode.getKeyCode())) : String.valueOf(setting.name) + ": " + Keyboard.getKeyName((int)m.keyCode.getKeyCode());
                    GUIMethod.drawRect(this.getXLoc(m), (int)((double)count * 13.9), this.getXLoc2(m), (int)((double)count * 13.9 + 14.0), -1879048192);
                    fr.drawShadedString(name, this.getXLoc(m) + 2, (float)((double)count * 13.9 + 2.0), new Color(255, 255, 255));
                    ++count;
                }
            }
            if (type != ClickType.RELEASE || button != 0) continue;
            this.dragging = null;
            this.dragging = null;
        }
    }

    public String getModuleNameById(int id) {
        int i = 0;
        for (Module m : Client.modules) {
            if (++i != id) continue;
            return m.name;
        }
        return null;
    }

    public int getSuitedColor(Module m) {
        int primaryColor = ColorUtil.getRainbow(4.0f, 1.0f, 1.0f);
        int secondaryColor = ColorUtil.getRainbow(4.0f, 1.0f, 1.0f);
        if (!m.toggled) {
            return new Color(0, 0, 0, 150).getRGB();
        }
        return primaryColor;
    }

    public int getXLoc(Module m) {
        if (m.getCategory() == Module.Category.COMBAT) {
            return 150;
        }
        if (m.getCategory() == Module.Category.RENDER) {
            return 250;
        }
        if (m.getCategory() == Module.Category.MOVEMENT) {
            return 350;
        }
        if (m.getCategory() == Module.Category.PLAYER) {
            return 450;
        }
        if (m.getCategory() == Module.Category.GHOST) {
            return 550;
        }
        return 0;
    }

    public int getXLoc2(Module m) {
        if (m.getCategory() == Module.Category.COMBAT) {
            return 220;
        }
        if (m.getCategory() == Module.Category.RENDER) {
            return 320;
        }
        if (m.getCategory() == Module.Category.MOVEMENT) {
            return 420;
        }
        if (m.getCategory() == Module.Category.PLAYER) {
            return 520;
        }
        if (m.getCategory() == Module.Category.GHOST) {
            return 620;
        }
        return 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.DrawClickGUI(mouseX, mouseY, -1, ClickType.DRAW);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        this.DrawClickGUI(mouseX, mouseY, button, ClickType.RELEASE);
        this.isClicked = false;
        this.ChangingKey = false;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static boolean isHovered(float left, float top, float right, float bottom, int mouseX, int mouseY) {
        return (float)mouseX >= left && (float)mouseY >= top && (float)mouseX < right && (float)mouseY < bottom;
    }

    public void keyPress(int key) {
        if (key <= 0) {
            return;
        }
        key = this.key;
    }

    public static enum ClickType {
        CLICK,
        RELEASE,
        DRAG,
        DRAW;

    }
}

