package me.nyan.flush.customhud.setting.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.customhud.setting.Setting;
import me.nyan.flush.ui.elements.CheckBox;
import me.nyan.flush.ui.elements.ModeSwitch;
import me.nyan.flush.ui.fontrenderer.FontManager;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

import java.util.Arrays;
import java.util.function.BooleanSupplier;

public class FontSetting extends Setting {
    private final Minecraft mc = Minecraft.getMinecraft();

    private final CheckBox minecraftFont;
    private final ModeSwitch fontSwitch;
    private final ModeSwitch sizeSwitch;

    public FontSetting(String name, String font, int size) {
        this(name, font, size, null);
    }

    public FontSetting(String name, String font, int size, BooleanSupplier supplier) {
        super(name, supplier);

        fontSwitch = new ModeSwitch(name + " name", 0, 0, getWidth(), getHeight(),
                font.toUpperCase(), 0xFFBB86FC, FontManager.AVAILABLE_FONTS);
        sizeSwitch = new ModeSwitch(name + " size", 0, 0, getWidth(), getHeight(),
                String.valueOf(size), 0xFFBB86FC, Arrays.stream(FontManager.AVAILABLE_SIZES)
                .mapToObj(Integer::toString).toArray(String[]::new));

        minecraftFont = new CheckBox("Use MC font", 0, 0, getWidth(), getHeight(), false, 0xFFBB86FC);
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        if (!minecraftFont.getValue()) {
            fontSwitch.drawScreen(mouseX - (int) x, mouseY - (int) y, partialTicks);
            GlStateManager.translate(0, fontSwitch.getFullHeight(), 0);
            sizeSwitch.drawScreen(mouseX - (int) x, mouseY - (int) y, partialTicks);
            GlStateManager.translate(0, sizeSwitch.getFullHeight(), 0);
        }
        minecraftFont.drawScreen(mouseX - (int) x, mouseY - (int) y, partialTicks);
        GlStateManager.popMatrix();
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        if (!minecraftFont.getValue()) {
            fontSwitch.mouseClicked(mouseX - (int) x, mouseY - (int) y, button);
            y += (int) fontSwitch.getFullHeight();

            sizeSwitch.mouseClicked(mouseX - (int) x, mouseY - (int) y, button);
            y += (int) sizeSwitch.getFullHeight();
        }

        minecraftFont.mouseClicked(mouseX - (int) x, mouseY - (int) y, button);
    }

    @Override
    public void mouseReleased(float x, float y, int mouseX, int mouseY, int button) {
        super.mouseReleased(x, y, mouseX, mouseY, button);
    }

    public String getFont() {
        return fontSwitch.getValue();
    }

    public void setFont(String value) {
        fontSwitch.setValue(value);
    }

    public int getSize() {
        return Integer.parseInt(sizeSwitch.getValue());
    }

    public void setSize(int value) {
        sizeSwitch.setValue(String.valueOf(value));
    }

    public boolean isMinecraftFont() {
        return minecraftFont.getValue();
    }

    public void setMinecraftFont(boolean value) {
        minecraftFont.setValue(value);
    }

    @Override
    public float getWidth() {
        return 120;
    }

    @Override
    public float getHeight() {
        return 16;
    }

    @Override
    public float getFullHeight() {
        return (minecraftFont.getValue() ? 0 : fontSwitch.getFullHeight() + sizeSwitch.getFullHeight()) + minecraftFont.getHeight();
    }

    public int drawString(String text, float x, float y, int color, boolean shadow) {
        if (isMinecraftFont()) {
            return mc.fontRendererObj.drawString(text, x, y + 2, color, shadow);
        }
        return Flush.getFont(getFont(), getSize()).drawString(text, x, y, color, shadow);
    }

    public int drawString(String text, float x, float y, int color) {
        return drawString(text, x, y, color, false);
    }

    public int drawXYCenteredString(String text, float x, float y, int color, boolean shadow) {
        return drawString(text, x - getStringWidth(text) / 2F, y - getFontHeight() / 2F, color, shadow);
    }

    public int drawXYCenteredString(String text, float x, float y, int color) {
        return drawXYCenteredString(text, x, y, color, false);
    }

    public int drawXCenteredString(String text, float x, float y, int color, boolean shadow) {
        return drawString(text, x - getStringWidth(text) / 2F, y, color, shadow);
    }

    public int drawXCenteredString(String text, float x, float y, int color) {
        return drawXCenteredString(text, x, y, color, false);
    }

    public int drawYCenteredString(String text, float x, float y, int color, boolean shadow) {
        return drawString(text, x, y - getFontHeight() / 2F, color, shadow);
    }

    public int drawYCenteredString(String text, float x, float y, int color) {
        return drawYCenteredString(text, x, y, color, false);
    }

    public float drawCharWithShadow(char c, float x, float y, int color, boolean shadow) {
        if (isMinecraftFont()) {
            return mc.fontRendererObj.drawString(String.valueOf(c), x, y + 2, color, shadow);
        }
        return Flush.getFont(getFont(), getSize()).drawChar(c, x, y, color, shadow);
    }

    public int getStringWidth(String text) {
        if (isMinecraftFont()) {
            return mc.fontRendererObj.getStringWidth(text);
        }
        return Flush.getFont(getFont(), getSize()).getStringWidth(text);
    }

    public int getFontHeight() {
        if (isMinecraftFont()) {
            return mc.fontRendererObj.FONT_HEIGHT;
        }
        return Flush.getFont(getFont(), getSize()).getFontHeight();
    }
}
