package me.nyan.flush.clickgui.discord;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.ClickGui;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.render.ModuleClickGui;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.module.settings.Setting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class DiscordClickGui extends ClickGui {
    private boolean dragging;
    private float modulesScroll;
    private float aModulesScroll;
    private float settingsScroll;
    private float aSettingsScroll;
    private float panelX;
    private float panelY;
    private final float panelWidth;
    private final float panelHeight;
    private final float titleHeight;
    private final float closeButtonWidth;
    private float clickedX;
    private float clickedY;
    private Module currentModule;
    private Module.Category currentCategory;
    private boolean listening;
    private NumberSetting draggingSlider;
    private boolean focused;
    private final ModuleClickGui module;

    public DiscordClickGui(float x, float y) {
        panelX = x;
        panelY = y;
        panelWidth = 400;
        panelHeight = 300;
        titleHeight = 20;
        closeButtonWidth = 34;
        dragging = false;
        draggingSlider = null;
        module = flush.getModuleManager().getModule(ModuleClickGui.class);
        currentCategory = Module.Category.values()[0];
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean lightTheme = module.isLightTheme();

        //settings scrolling
        if (MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f - 42, panelY + titleHeight, panelX + panelWidth,
                panelY + panelHeight)) {
            if (currentModule != null && currentModule.getSettings().size() > 18) {
                if (focused) {
                    float settingsMax = -262;
                    for (Setting setting : currentModule.getSettings()) {
                        settingsMax += setting instanceof NumberSetting ? 18 : 14;
                    }

                    if (Mouse.hasWheel()) {
                        int wheel = Mouse.getDWheel();
                        if (wheel > 0 && aSettingsScroll > 0) {
                            settingsScroll -= 30;
                        } else if (wheel < 0) {
                            settingsScroll += 30;
                        }

                        settingsScroll = Math.min(Math.max(0, settingsScroll), settingsMax);
                    }
                }
            } else {
                settingsScroll = 0;
            }

            if (aSettingsScroll > settingsScroll) {
                if (aSettingsScroll - 0.75F * Flush.getFrameTime() < settingsScroll)
                    aSettingsScroll = settingsScroll;
                else aSettingsScroll -= 0.75F * Flush.getFrameTime();
            } else if (aSettingsScroll < settingsScroll) {
                if (aSettingsScroll + 0.75F * Flush.getFrameTime() > settingsScroll)
                    aSettingsScroll = settingsScroll;
                else aSettingsScroll += 0.75F * Flush.getFrameTime();
            }
        }

        if (MouseUtils.hovered(mouseX, mouseY, panelX + 42, panelY + titleHeight, panelX + panelWidth / 2f - 42,
                panelY + panelHeight)) {
            if (currentCategory != null && moduleManager.getModulesByCategory(currentCategory).size() > 17) {
                if (focused) {
                    float modulesMax = -258;
                    for (Module ignored : moduleManager.getModulesByCategory(currentCategory)) {
                        modulesMax += 15;
                    }

                    if (Mouse.hasWheel()) {
                        int wheel = Mouse.getDWheel();
                        if (wheel > 0 && aModulesScroll > 0) {
                            modulesScroll -= 30;
                        } else if (wheel < 0) {
                            modulesScroll += 30;
                        }

                        modulesScroll = Math.min(Math.max(0, modulesScroll), modulesMax);
                    }
                }
            } else {
                modulesScroll = 0;
            }

            if (aModulesScroll > modulesScroll) {
                if (aModulesScroll - 0.75F * Flush.getFrameTime() < modulesScroll)
                    aModulesScroll = modulesScroll;
                else aModulesScroll -= 0.75F * Flush.getFrameTime();
            } else if (aModulesScroll < modulesScroll) {
                if (aModulesScroll + 0.75F * Flush.getFrameTime() > modulesScroll)
                    aModulesScroll = modulesScroll;
                else aModulesScroll += 0.75F * Flush.getFrameTime();
            }
        }

        //title bar
        boolean closeButtonHovered = MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth - closeButtonWidth, panelY, panelX + panelWidth,
                panelY + titleHeight);
        boolean blurButtonHovered = MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth - closeButtonWidth * 2, panelY, panelX +
                panelWidth - closeButtonWidth, panelY + titleHeight);

        if (dragging) {
            panelX = clickedX + mouseX;
            panelY = clickedY + mouseY;
        }

        GlyphPageFontRenderer font18 = Flush.getFont("GoogleSansDisplay", 18);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 0);

        drawRect(panelX, panelY, panelX + panelWidth, panelY + panelHeight, lightTheme ? 0xFFFFFFFF : 0xFF2C2E33);
        drawRect(panelX, panelY, panelX + panelWidth, panelY + titleHeight, (lightTheme ? 0xFFFFFFFF : (focused ? 0xFF202124 : 0xFF282A2F)));

        RenderUtils.glColor(-1);
        RenderUtils.drawImage(new ResourceLocation("flush/icons/icon_32x32.png"), (int) panelX + 4, (int) panelY + 5,
                (int) titleHeight / 2F, (int) titleHeight / 2F);
        font18.drawString(Flush.NAME + " " + Flush.VERSION, panelX + 6 + titleHeight / 2,
                (panelY + titleHeight / 2f - font18.getFontHeight() / 2F), lightTheme ? (focused ? 0xFF171717 : 0xFFB3B3B3) : 0xFFB3B4B5);

        drawRect(panelX + panelWidth - closeButtonWidth, panelY, panelX + panelWidth, panelY + titleHeight,
                closeButtonHovered ? 0xFFFF1616 : (lightTheme ? 0xFFFFFFFF : (focused ? 0xFF202124 : 0xFF282A2F)));

        drawRect(panelX + panelWidth - closeButtonWidth * 2, panelY, panelX + panelWidth - closeButtonWidth,
                panelY + titleHeight, blurButtonHovered ? (lightTheme ? 0xFFE5E5E5 : 0xFF373739) : (lightTheme ? 0xFFFFFFFF :
                        (focused ? 0xFF202124 : 0xFF282A2F)));

        font18.drawXYCenteredString("Blur", panelX + panelWidth - closeButtonWidth * 2 +
                closeButtonWidth / 2 - 1, panelY + titleHeight / 2f, lightTheme ? (focused ? 0xFF171717 : 0xFFB3B3B3) : 0xFFA7B1BB);

        GL11.glLineWidth(1);
        RenderUtils.drawCross(panelX + panelWidth - closeButtonWidth / 2F - 3, panelY + titleHeight / 2f - 3, 6,
                6, lightTheme ? (focused ? (closeButtonHovered ? -1 : 0xFF171717) : 0xFFB3B3B3) : -1);

        GlStateManager.translate(panelX, panelY + titleHeight, 0);

        //categories background
        drawRect(0, 0, 42, panelHeight - titleHeight, lightTheme ? 0xFFE3E5E8 : 0xFF202124);

        //modules background
        drawRect(42, 0, panelWidth / 2f - 42, panelHeight - titleHeight, lightTheme ? 0xFFF2F3F5 : 0xFF282A2F);

        //categories
        int cIndex = 0;
        for (Module.Category category : Module.Category.values()) {
            if (currentCategory == category) {
                RenderUtils.fillRoundRect(5.5f, 4 + cIndex * 34, 30, 30, 12, module.getClickGUIColor());
            } else {
                RenderUtils.fillCircle(20, 19 + cIndex * 34, 16, lightTheme ? -1 : 0xFF2A2C31);

                if (MouseUtils.hovered(mouseX, mouseY, panelX + 6, panelY + titleHeight + 6 + cIndex * 34, panelX + 34,
                        panelY + titleHeight + 34 + cIndex * 34)) {
                    RenderUtils.fillCircle(20, 19 + cIndex * 34, 16, 0x70000000);
                }
            }

            String name = String.valueOf(category.name.charAt(0));
            float x = 19.5F;
            Flush.getFont("GoogleSansDisplay", 30).drawXYCenteredString(name, x, 19.5F + cIndex *
                    34, currentCategory == category ? -1 : lightTheme ? 0xFF676B6F : 0xFFA7B1BB);
            cIndex++;
        }

        ArrayList<Module> modulesSorted = new ArrayList<>(moduleManager.getModulesByCategory(currentCategory));
        modulesSorted.sort((module1, module2) -> module1.getName().compareToIgnoreCase(module2.getName()));

        //modules
        GlyphPageFontRenderer moduleFont = Flush.getFont("GoogleSansDisplay", 22);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, -aModulesScroll, 0);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissor(panelX + 42, panelY + titleHeight, panelX + panelWidth / 2f - 42, panelY + panelHeight);

        int moduleIndex = 0;
        for (Module module : modulesSorted) {
            GL11.glLineWidth(2);
            RenderUtils.drawHashtag(46, 4 + moduleIndex * 16, 12, 12, lightTheme ? 0xFF727D8B : 0xFF575F64);
            moduleFont.drawString(module.getName(), 60, 4 + moduleIndex * 16, module.isEnabled() ? (lightTheme ? 0xFF19232D : 0xFFB3B4B5)
                    : (lightTheme ? 0xFF707E98 : 0xFF8E918F));
            moduleIndex++;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        //settings
        GlyphPageFontRenderer settingsFont = Flush.getFont("GoogleSansDisplay", 18);
        if (currentModule != null) {
            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtils.glScissor(panelX + panelWidth / 2F - 42, panelY + titleHeight, panelX + panelWidth, panelY + panelHeight);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0, -aSettingsScroll, 0);

            settingsFont.drawString(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? "Hidden: " + currentModule.isHidden()
                            : "Keybind: " + (listening ? "listening..." : currentModule.getKeysString()),
                    panelWidth / 2f - 42 + 6, 6, lightTheme ? 0xFF060708 : 0xFFB3B4B5);

            int settingYIndex = 0;
            for (Setting setting : currentModule.getSettings()) {
                String settingName = setting.getName() + (setting instanceof ModeSetting ? ": " + ((ModeSetting) setting).getValue() : "");

                settingsFont.drawString(settingName, panelWidth / 2f - 42 + 6, 20 + settingYIndex,
                        (setting instanceof BooleanSetting ?
                                (((BooleanSetting) setting).getValue() ?
                                        (lightTheme ? 0xFF060708 : 0xFFB3B4B5) :
                                        (lightTheme ? 0xFF788595 : 0xFF8E918F)) :
                                (lightTheme ? 0xFF060708 : 0xFFB3B4B5)));

                if (setting instanceof NumberSetting) {
                    drawRect(panelWidth / 2f - 42 + 8, 32 + settingYIndex,
                            panelWidth / 2f - 42 + 8 + 120, 33 + settingYIndex,
                            lightTheme ? 0xFFDCDDDE : 0xFF202124);
                    drawRect(panelWidth / 2f - 42 + 8, 32 + settingYIndex,
                            panelWidth / 2f - 42 + 8 + (((NumberSetting) setting).getValue() -
                                    ((NumberSetting) setting).getMin()) / (((NumberSetting) setting).getMax() -
                                    ((NumberSetting) setting).getMin()) * 120,
                            33 + settingYIndex, module.getClickGUIColor());
                    drawRect(panelWidth / 2f - 42 + 8 + (((NumberSetting) setting).getValue()
                                    - ((NumberSetting) setting).getMin()) / (((NumberSetting) setting).getMax()
                                    - ((NumberSetting) setting).getMin()) * 120,
                            32 + settingYIndex,
                            panelWidth / 2f - 42 + 9 + (((NumberSetting) setting).getValue()
                                    - ((NumberSetting) setting).getMin()) / (((NumberSetting) setting).getMax()
                                    - ((NumberSetting) setting).getMin()) * 120, 33 + settingYIndex,
                            lightTheme ? 0xFFFFFFFF : 0xFFDCDDDE);

                    String value = ((NumberSetting) setting).getValue() % 1.0 == 0.0 ?
                            String.valueOf(((NumberSetting) setting).getValue()).replace(".0", "")
                            : String.valueOf(Math.round(((NumberSetting) setting).getValue() * 100f) / 100f);

                    settingsFont.drawString(value, panelWidth / 2f - 42 + 126 -
                            settingsFont.getStringWidth(value), 19 + settingYIndex, 0xFFB3B4B5);

                    settingYIndex += 18;
                } else {
                    settingYIndex += 14;
                }
            }
            GlStateManager.popMatrix();

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            if (MouseUtils.hovered(mouseX, mouseY, panelX, panelY, panelX + panelWidth - closeButtonWidth, panelY + titleHeight)) {
                clickedX = panelX - mouseX;
                clickedY = panelY - mouseY;
                dragging = true;
            }

            float settingsHeight = titleHeight + 18;
            if (currentModule != null) {
                for (Setting setting : currentModule.getSettings()) {
                    settingsHeight += setting instanceof NumberSetting ? 18 : 14;
                }
            }

            focused = MouseUtils.hovered(mouseX, mouseY, panelX, panelY, panelX + panelWidth, panelY + Math.max(panelHeight, settingsHeight));

            if (MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth - closeButtonWidth, panelY, panelX + panelWidth,
                    panelY + titleHeight)) {
                mc.displayGuiScreen(null);
            }

            //categories
            int catindex = 0;
            for (Module.Category category : Module.Category.values()) {
                if (MouseUtils.hovered(mouseX, mouseY, panelX + 6, panelY + titleHeight + 6 + catindex * 34, panelX + 34,
                        panelY + titleHeight + 34 + catindex * 34)) {
                    if (currentCategory != category) {
                        currentCategory = category;
                        aModulesScroll = 0;
                        currentModule = null;
                        listening = false;
                    }
                }
                catindex++;
            }
        }

        //settings
        GlyphPageFontRenderer settingsFont = Flush.getFont("GoogleSansDisplay", 18);
        if (currentModule != null) {
            if (mouseButton == 0) {
                if (MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f - 42 + 4, panelY + titleHeight + 4,
                        panelX + panelWidth / 2f - 42 + settingsFont.getStringWidth(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? "Hidden: " +
                                currentModule.isHidden() : "Keybind: " + currentModule.getKeysString()) + 8, panelY +
                                titleHeight + 16))
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        currentModule.setHidden(!currentModule.isHidden());
                    } else {
                        listening = true;
                    }
            }

            if (MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f - 42, panelY + titleHeight,
                    panelX + panelWidth, panelY + panelHeight)) {
                int settingIndex = 0;
                for (Setting setting : currentModule.getSettings()) {
                    String settingName = (setting.getName().startsWith(setting.getModule().getName()) ?
                            setting.getName().replace(setting.getModule().getName() + " ", "") : setting.getName()) +
                            (setting instanceof ModeSetting ? ": " + ((ModeSetting) setting).getValue() : "");

                    if (MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f - 42 + 6, panelY + titleHeight + 20 + settingIndex - aSettingsScroll,
                            panelX + panelWidth / 2f - 42 + 7 + settingsFont.getStringWidth(settingName), panelY + titleHeight + 32 + settingIndex - aSettingsScroll)) {
                        if (setting instanceof BooleanSetting && mouseButton == 0)
                            ((BooleanSetting) setting).setValue(!((BooleanSetting) setting).getValue());

                        if (setting instanceof ModeSetting) {
                            ModeSetting combo = (ModeSetting) setting;
                            if (mouseButton == 0) {
                                combo.cycle();
                            } else if (mouseButton == 1) {
                                combo.cycleInverted();
                            }
                        }
                    }

                    settingIndex += setting instanceof NumberSetting ? 18 : 14;
                }
            }
        }

        //modules
        if (!MouseUtils.hovered(mouseX, mouseY, panelX + 42, panelY + titleHeight, panelX + panelWidth / 2f - 42,
                panelY + panelHeight)) {
            return;
        }

        GlyphPageFontRenderer modulesFont = Flush.getFont("GoogleSansDisplay", 22);
        ArrayList<Module> modulesSorted = new ArrayList<>(moduleManager.getModulesByCategory(currentCategory));
        modulesSorted.sort((module1, module2) -> module1.getName().compareToIgnoreCase(module2.getName()));

        int moduleIndex = 0;
        for (Module module : modulesSorted) {
            if (MouseUtils.hovered(mouseX, mouseY, panelX + 46,
                    panelY + titleHeight + 6 + moduleIndex * 16 - aModulesScroll,
                    panelX + 62 + modulesFont.getStringWidth(module.getName()),
                    panelY + titleHeight + 18 + moduleIndex * 16 - aModulesScroll)) {
                if (mouseButton == 0) {
                    module.toggle();
                } else if (mouseButton == 1) {
                    currentModule = module;
                    aSettingsScroll = 0;
                    listening = false;
                }
            }
            moduleIndex++;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        draggingSlider = null;
        dragging = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clickedMouseButton == 0) {
            if (currentModule != null) {
                if (!MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f - 42, panelY + titleHeight,
                        panelX + panelWidth, panelY + panelHeight)) {
                    return;
                }

                int settingIndex = 0;
                for (Setting setting : currentModule.getSettings()) {
                    if (setting instanceof NumberSetting) {
                        if (MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f - 42 + 8, panelY + titleHeight + 30 +
                                settingIndex - aSettingsScroll, panelX + panelWidth / 2f - 42 + 8 + 120, panelY + titleHeight + 35 + settingIndex - aSettingsScroll) && draggingSlider == null)
                            draggingSlider = (NumberSetting) setting;

                        if (draggingSlider != null) {
                            draggingSlider.setValue(draggingSlider.getMin() + (MathHelper.clamp_double((mouseX - (panelX + panelWidth / 2f - 42 + 8)) / 120, 0, 1)) * (draggingSlider.getMax() - draggingSlider.getMin()));
                        }

                        settingIndex += 18;
                    } else {
                        settingIndex += 14;
                    }
                }
            }
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void initGui() {
        super.initGui();
        focused = true;
    }

    @Override
    public void onGuiClosed() {
        listening = false;
        module.disable();
        dragging = false;
        draggingSlider = null;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (listening) {
            currentModule.clearKeys();
            listening = false;

            if (keyCode == Keyboard.KEY_ESCAPE) {
                return;
            }
            currentModule.addKey(keyCode);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
