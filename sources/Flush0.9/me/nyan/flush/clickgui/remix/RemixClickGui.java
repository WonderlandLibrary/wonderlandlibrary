package me.nyan.flush.clickgui.remix;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RemixClickGui extends ClickGui {
    //clickgui module
    private final ModuleClickGui module;

    //panel
    private float panelX;
    private float panelY;
    private final float panelWidth;
    private final float panelHeight;
    private final float titleHeight;

    //catogories
    private final float categoriesWidth;
    private boolean categoriesExtended;

    //settings
    private Module currentModule;
    private Module listening;
    private NumberSetting draggingSlider;
    private Module.Category currentCategory;

    //dragging
    private boolean dragging;
    private float clickedX;
    private float clickedY;

    //scrolling
    private float modulesScroll;
    private float settingsScroll;

    public RemixClickGui(float x, float y) {
        panelX = x;
        panelY = y;
        panelWidth = 385;
        panelHeight = 318;
        titleHeight = 36;
        dragging = false;
        draggingSlider = null;
        categoriesExtended = false;
        categoriesWidth = 32;
        module = moduleManager.getModule(ModuleClickGui.class);
        currentCategory = Module.Category.values()[0];
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int offset = 42;
        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 18);

        //dragging
        if (dragging) {
            panelX = Math.max(Math.min(clickedX + mouseX, width - panelWidth), categoriesExtended ? 58 : 0);
            panelY = Math.max(Math.min(clickedY + mouseY, height - panelHeight), 0);
        }

        //scrolling
        {
            //settings scrolling
            if (MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f, panelY + titleHeight,
                    panelX + panelWidth, panelY + panelHeight) && currentModule != null) {
                List<Setting> settings = getSettings();
                float settingsMax = -panelHeight + titleHeight + settings.size() * offset;
                if (Mouse.hasWheel()) {
                    int wheel = Mouse.getDWheel();
                    if (wheel > 0 && settingsScroll > 0) {
                        settingsScroll -= 30;
                    } else if (wheel < 0) {
                        settingsScroll += 30;
                    }

                    settingsScroll = settings.size() * offset < panelHeight - titleHeight ? 0 :
                            Math.min(Math.max(0, settingsScroll), settingsMax);
                }
            }

            //modules scrolling
            if (MouseUtils.hovered(mouseX, mouseY, panelX + categoriesWidth, panelY + titleHeight,
                    panelX + panelWidth / 2f, panelY + panelHeight) && currentCategory != null) {
                float modulesMax = -panelHeight + titleHeight + moduleManager.getModulesByCategory(currentCategory).size() * offset;
                if (Mouse.hasWheel()) {
                    int wheel = Mouse.getDWheel();
                    if (wheel > 0 && modulesScroll > 0) {
                        modulesScroll -= 30;
                    } else if (wheel < 0) {
                        modulesScroll += 30;
                    }

                    modulesScroll = moduleManager.getModulesByCategory(currentCategory).size() * offset < panelHeight - titleHeight ? 0 :
                            Math.min(Math.max(0, modulesScroll), modulesMax);
                }
            }
        }

        GlStateManager.pushMatrix();

        GlStateManager.translate(panelX, panelY, 0);

        //background
        drawRect(categoriesExtended ? -58 : 0, 0, panelWidth, panelHeight, 0xFF282828);

        //modules background
        drawRect(categoriesWidth, 0, panelWidth / 2f, panelHeight, 0xFF1F1F1F);

        //menu logo (categories extending)
        {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 1);
            GlStateManager.color(1, 1, 1, 1);
            RenderUtils.drawImageCentered(new ResourceLocation("flush/icons/remix/menu.png"), (int) ((16 - (categoriesExtended ? 58 : 0)) / 0.5)
                    , (int) ((titleHeight - 12) / 0.5), 28, 28);
            GlStateManager.popMatrix();
        }

        //current category
        {
            GlyphPageFontRenderer robotoMedium = Flush.getFont("GoogleSansDisplay Medium", 28);
            robotoMedium.drawString(currentCategory.name.replace("Movement", "Move"), 48, robotoMedium.getFontHeight() / 2f + 4, -1);

            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 1);
            RenderUtils.fillCircle((64 + robotoMedium.getStringWidth(currentCategory.name.replace("Movement", "Move"))) / 0.5f,
                    (13 + robotoMedium.getFontHeight() / 2f) / 0.5F, 12, currentCategory.remixColor);
            GlStateManager.popMatrix();

            Flush.getFont("GoogleSansDisplay", 16)
                    .drawXCenteredString(String.valueOf(moduleManager.getModulesByCategory(currentCategory).size()),
                    62.5f + robotoMedium.getStringWidth(currentCategory.name.replace("Movement", "Move")),
                            8.5f + robotoMedium.getFontHeight() / 2f, -1);
        }

        if (currentModule != null && currentCategory != null) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.9, 0.9, 1);
            Flush.getFont("GoogleSansDisplay", 18).drawString(currentCategory.name + " / " + currentModule.getName(), (panelWidth / 2f + 12) / 0.9f,
                    12 / 0.9f, 0xFF686868);
            GlStateManager.popMatrix();
        }

        GlStateManager.translate(0, titleHeight, 0);

        GlStateManager.pushMatrix();

        if (categoriesExtended) {
            GlStateManager.translate(-58, 0, 0);
        }

        //categories
        int catindex = 0;
        for (Module.Category category : Module.Category.values()) {
            if (currentCategory == category) {
                drawRect(0, catindex * 38 + 10, 2, catindex * 38 + 34, category.remixColor);
                drawRect(2, catindex * 38 + 10,
                        categoriesWidth + (categoriesExtended ? 58 : 0),
                        catindex * 38 + 34, 0xFF393939);
            }

            if (categoriesExtended) {
                Flush.getFont("GoogleSansDisplay Medium", 20)
                        .drawString(category.name.replace("Movement", "Move"), 32,
                        catindex * 38 + 16, 0xFFBDC3C7);
            }

            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 1);
            RenderUtils.glColor(category.remixColor);
            RenderUtils.drawImageCentered(category.remixIcon, (int) (16 / 0.5), (int) ((catindex * 38 + 22) / 0.5), 25, 25);
            GlStateManager.popMatrix();
            catindex++;
        }

        GlStateManager.popMatrix();

        ArrayList<Module> modulesSorted = new ArrayList<>(moduleManager.getModulesByCategory(currentCategory));
        modulesSorted.sort((module1, module2) -> module1.getName().compareToIgnoreCase(module2.getName()));

        //modules
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, -modulesScroll, 0);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissor(panelX + categoriesWidth, panelY + titleHeight, panelX + panelWidth / 2f, panelY + panelHeight);

        int moduleIndex = 0;
        for (Module module : modulesSorted) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.8, 0.8, 1);
            RenderUtils.fillCircle((categoriesWidth + 23) / 0.8f,
                    (offset / 2f + moduleIndex * offset) / 0.8F, 2,
                    module.isEnabled() ? 0xFF2ECC71 : 0xFFE74C3C);
            GlStateManager.popMatrix();

            font.drawString(listening == module ? "Listening..." : module.getName(),
                    categoriesWidth + 42, (offset / 2f - font.getFontHeight() / 2f + moduleIndex * offset),
                    0xFFFFFFFF, true);

            drawRect(categoriesWidth + 8, offset - 0.25 + moduleIndex * offset,
                    panelWidth / 2f - 8, offset + 0.25 + moduleIndex * offset,
                    0xFF292929);
            moduleIndex++;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        //settings
        if (currentModule != null) {
            List<Setting> settings = getSettings();
            ArrayList<Setting> sortedSettings = new ArrayList<>();
            sortedSettings.addAll(settings.stream().filter(setting -> setting instanceof ModeSetting)
                    .collect(Collectors.toList()));
            sortedSettings.addAll(settings.stream().filter(setting -> setting instanceof NumberSetting)
                    .collect(Collectors.toList()));
            sortedSettings.addAll(settings.stream().filter(setting -> setting instanceof BooleanSetting)
                    .collect(Collectors.toList()));

            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtils.glScissor(panelX + panelWidth / 2f, panelY + titleHeight, panelX + panelWidth, panelY + panelHeight);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0, -settingsScroll, 0);

            int settingIndex = 0;
            for (Setting setting : sortedSettings) {
                RenderUtils.glColor(-1);

                font.drawString(setting.getName(), panelWidth / 2f + 12, offset / 2f - font.getFontHeight() / 2f + settingIndex, 0xFFFFFFFF);

                if (setting instanceof BooleanSetting) {
                    RenderUtils.fillRoundRect(panelWidth / 2f + 76 + 87, settingIndex + offset / 2f - 6.5F, 13, 13, 3, currentCategory.remixColor);

                    if (!((BooleanSetting) setting).getValue()) {
                        RenderUtils.fillRoundRect(panelWidth / 2f + 76 + 87.5F, settingIndex + offset / 2f - 6, 12, 12, 3, 0xFF242424);
                    } else {
                        GL11.glLineWidth(1.5f);
                        RenderUtils.drawLine(panelWidth / 2f + 76 + 87.5f + 6, settingIndex + offset / 2f + 3, panelWidth / 2f + 76 + 89.5f,
                                settingIndex + offset / 2f + 1, -1);
                        RenderUtils.drawLine(panelWidth / 2f + 76 + 87.5f + 6, settingIndex + offset / 2f + 3, panelWidth / 2f + 76 + 87.5f + 9,
                                settingIndex + offset / 2f - 3, -1);
                    }
                } else if (setting instanceof NumberSetting) {
                    drawRect(panelWidth / 2f + 76, offset / 2f - 0.5 + settingIndex, panelWidth / 2f + 76 + 100, offset / 2f + 0.5 +
                            settingIndex, 0xFF202020);
                    drawRect(panelWidth / 2f + 76, offset / 2f - 0.5 + settingIndex, panelWidth / 2f + 76 + (((NumberSetting) setting).getValue() -
                                    ((NumberSetting) setting).getMin()) / (((NumberSetting) setting).getMax() - ((NumberSetting) setting).getMin()) * 100,
                            offset / 2f + 0.5 + settingIndex, currentCategory.remixColor);

                    GlStateManager.pushMatrix();
                    GlStateManager.scale(0.8, 0.8, 1);
                    RenderUtils.fillCircle((float) (panelWidth / 2f + 76 + (((NumberSetting) setting).getValue() -
                                    ((NumberSetting) setting).getMin()) / (((NumberSetting) setting).getMax() - ((NumberSetting) setting).getMin()) * 100) / 0.8f,
                            (offset / 2f + settingIndex) / 0.8F, 3, currentCategory.remixColor);
                    GlStateManager.popMatrix();

                    String value = ((NumberSetting) setting).getValue() % 1.0 == 0.0 ? String.valueOf(((NumberSetting) setting).getValue()).replace(".0", "")
                            : String.valueOf(Math.round(((NumberSetting) setting).getValue() * 100f) / 100f);

                    Flush.getFont("GoogleSansDisplay", 16).drawXCenteredString(value, (float) (75 + panelWidth / 2f + (((NumberSetting) setting).getValue() -
                                    ((NumberSetting) setting).getMin()) / (((NumberSetting) setting).getMax() - ((NumberSetting) setting).getMin()) * 100),
                            offset / 2f + settingIndex - Flush.getFont("GoogleSansDisplay", 16).getFontHeight() - 4, 0xFFFFFFFF);
                }

                settingIndex += offset;
            }

            ArrayList<Setting> settingsReversed = new ArrayList<>(sortedSettings);
            Collections.reverse(settingsReversed);

            int index = settingsReversed.size() * offset - offset;

            for (Setting setting : settingsReversed) {
                if (setting instanceof ModeSetting) {
                    ModeSetting modeSetting = (ModeSetting) setting;

                    ArrayList<String> options = new ArrayList<>(modeSetting.getOptions());
                    options.removeIf(option -> option.equalsIgnoreCase(modeSetting.getValue()));

                    if (modeSetting.extended) {
                        RenderUtils.fillRoundRect(panelWidth / 2f + 76, index + offset / 2f + 12, 100, 20 * options.size(), 2, 0xFF2E2E2E);

                        int optionsIndex = 0;
                        for (String option : options) {
                            boolean hovered = MouseUtils.hovered(
                                    mouseX,
                                    mouseY,
                                    panelX + panelWidth / 2f + 76,
                                    panelY + titleHeight + index + offset / 2f + 12 + optionsIndex * 20 - settingsScroll,
                                    panelX + panelWidth / 2f + 76 + 100,
                                    panelY + titleHeight + index + offset / 2f + 12 + optionsIndex * 20 + 19 - settingsScroll
                            );

                            if (hovered) {
                                RenderUtils.fillRoundRect(panelWidth / 2f + 76, index + offset / 2f + 12 + optionsIndex * 20, 100, 20, 2, currentCategory.remixColor);
                            }

                            RenderUtils.glColor(-1);

                            Flush.getFont("GoogleSansDisplay", 16)
                                    .drawString(
                                            option,
                                            panelWidth / 2f + 76 + 6,
                                            22 + optionsIndex * 20 + index + offset / 2f - Flush.getFont("GoogleSansDisplay", 16).getFontHeight() / 2F,
                                            hovered ? -1 : 0xFFABABAB
                                    );
                            optionsIndex++;
                        }
                    }

                    RenderUtils.glColor(-1);

                    RenderUtils.fillRoundRect(panelWidth / 2f + 76, index + offset / 2f - 10, 100, 20, 2, currentCategory.remixColor);

                    RenderUtils.fillRoundRect(panelWidth / 2f + 76.5F, index + offset / 2f - 9.5F, 99, 19, 2, 0xFF2E2E2E);

                    Flush.getFont("GoogleSansDisplay", 16).drawString(modeSetting.getValue(), panelWidth / 2f + 76 + 6, index + offset / 2f -
                            Flush.getFont("GoogleSansDisplay", 16).getFontHeight() / 2F, 0xFFABABAB);
                }

                index -= offset;
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
        int offset = 42;
        boolean shouldMove = true;
        if (mouseButton == 0) {
            //categories
            int catindex = 0;
            for (Module.Category category : Module.Category.values()) {
                if (MouseUtils.hovered(mouseX, mouseY, panelX + 6 - (categoriesExtended ? 58 : 0), panelY +
                        titleHeight + 7 + catindex * 38, panelX + categoriesWidth, panelY + titleHeight + 38 + catindex * 38)) {
                    if (currentCategory != category) {
                        currentCategory = category;
                        modulesScroll = 0;
                        currentModule = null;
                        listening = null;
                    }
                }
                catindex++;
            }

            //categories extending
            if (MouseUtils.hovered(mouseX, mouseY, panelX + 18 - 25 / 2f - (categoriesExtended ? 58 : 0), panelY + (titleHeight - 12) -
                    25 / 2f, panelX + 18 + 20 / 2f - (categoriesExtended ? 58 : 0), panelY + (titleHeight - 12) + 20 / 2f)) {
                categoriesExtended = !categoriesExtended;
                shouldMove = false;
            }
        }

        //settings
        if (mouseButton == 0) {
            if (currentModule != null) {
                List<Setting> settings = getSettings();
                ArrayList<Setting> sortedSettings = new ArrayList<>();
                sortedSettings.addAll(settings.stream().filter(setting -> setting instanceof ModeSetting)
                        .collect(Collectors.toList()));
                sortedSettings.addAll(settings.stream().filter(setting -> setting instanceof NumberSetting)
                        .collect(Collectors.toList()));
                sortedSettings.addAll(settings.stream().filter(setting -> setting instanceof BooleanSetting)
                        .collect(Collectors.toList()));

                if (MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f, panelY + titleHeight, panelX + panelWidth, panelY + panelHeight)) {
                    int settingIndex = 0;
                    for (Setting setting : sortedSettings) {
                        int settingIndex1 = 0;
                        for (Setting setting1 : sortedSettings) {
                            if (setting1 instanceof ModeSetting) {
                                ModeSetting modeSetting = (ModeSetting) setting1;

                                ArrayList<String> options = new ArrayList<>(modeSetting.getOptions());
                                options.removeIf(option -> option.equalsIgnoreCase(modeSetting.getValue()));

                                if (modeSetting.extended) {
                                    int optionsIndex = 0;
                                    for (String option : options) {
                                        boolean hovered = MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f + 76, panelY + titleHeight + settingIndex1 +
                                                offset / 2f + 12 + optionsIndex * 20 - settingsScroll, panelX + panelWidth / 2f + 76 + 100, panelY + titleHeight + settingIndex1 + offset / 2f +
                                                12 + optionsIndex * 20 + 19 - settingsScroll);

                                        if (hovered) {
                                            modeSetting.setValue(option);
                                            modeSetting.extended = false;
                                            return;
                                        }
                                        optionsIndex++;
                                    }
                                }
                            }
                            settingIndex1 += offset;
                        }

                        if (setting instanceof BooleanSetting) {
                            if (MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f + 76 + 87, panelY + titleHeight + settingIndex +
                                    offset / 2f - 6.5 - settingsScroll, panelX + panelWidth / 2f + 76 + 100, panelY + titleHeight +
                                    settingIndex + offset / 2f - 6.5 + 13 - settingsScroll))
                                ((BooleanSetting) setting).setValue(!((BooleanSetting) setting).getValue());
                        } else if (setting instanceof ModeSetting) {
                            ModeSetting modeSetting = (ModeSetting) setting;

                            if (MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f + 76, panelY + titleHeight + settingIndex +
                                    offset / 2f - 10 - settingsScroll, panelX + panelWidth / 2f + 76 + 100, panelY + titleHeight + settingIndex +
                                    offset / 2f - 10 - settingsScroll + 20) && modeSetting.getOptions().size() > 1) {
                                modeSetting.extended = !modeSetting.extended;
                                shouldMove = false;
                            }

                            ArrayList<String> options = new ArrayList<>(modeSetting.getOptions());
                            options.removeIf(option -> option.equalsIgnoreCase(modeSetting.getValue()));

                            if (modeSetting.extended) {
                                int optionsIndex = 0;
                                for (String option : options) {
                                    boolean hovered = MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f + 76, panelY + titleHeight + settingIndex +
                                            offset / 2f + 12 + optionsIndex * 20 - settingsScroll, panelX + panelWidth / 2f + 76 + 100, panelY + titleHeight + settingIndex + offset / 2f +
                                            12 + optionsIndex * 20 + 19 - settingsScroll);

                                    if (hovered) {
                                        modeSetting.setValue(option);
                                        modeSetting.extended = false;
                                        shouldMove = false;
                                    }
                                    optionsIndex++;
                                }
                            }
                        }

                        if (MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f + 75, panelY + titleHeight + offset / 2f - 2 +
                                settingIndex - settingsScroll, panelX + panelWidth / 2f + 77 + 100, panelY + titleHeight + offset / 2f
                                + 2 + settingIndex - settingsScroll)) {
                            shouldMove = false;
                        }

                        settingIndex += offset;
                    }
                }
            }

            if (shouldMove && MouseUtils.hovered(mouseX, mouseY, panelX, panelY, panelX + panelWidth, panelY + panelHeight) &&
                    !MouseUtils.hovered(mouseX, mouseY, panelX, panelY + titleHeight, panelX + panelWidth / 2f,
                            panelY + panelHeight)) {
                clickedX = panelX - mouseX;
                clickedY = panelY - mouseY;
                dragging = true;
            }
        }

        //modules
        if (!MouseUtils.hovered(mouseX, mouseY, panelX + categoriesWidth, panelY + titleHeight, panelX + panelWidth / 2f,
                panelY + panelHeight)) {
            return;
        }

        ArrayList<Module> modulesSorted = new ArrayList<>(moduleManager.getModulesByCategory(currentCategory));
        modulesSorted.sort((module1, module2) -> module1.getName().compareToIgnoreCase(module2.getName()));

        int moduleIndex = 0;
        for (Module module : modulesSorted) {
            if (MouseUtils.hovered(mouseX, mouseY, panelX + categoriesWidth, panelY + titleHeight + moduleIndex * offset - modulesScroll,
                    panelX + panelWidth / 2F, panelY + titleHeight + offset + moduleIndex * offset - modulesScroll)) {
                switch (mouseButton) {
                    case 0:
                        module.toggle();
                        break;

                    case 1:
                        currentModule = module;
                        settingsScroll = 0;
                        listening = null;
                        break;

                    case 2:
                        listening = module;
                        break;
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
        int offset = 42;
        if (clickedMouseButton == 0) {
            if (currentModule != null) {
                if (!MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f, panelY + titleHeight,
                        panelX + panelWidth, panelY + panelHeight)) {
                    return;
                }

                List<Setting> settings = getSettings();
                ArrayList<Setting> sortedSettings = new ArrayList<>();
                sortedSettings.addAll(settings.stream().filter(setting -> setting instanceof ModeSetting).collect(Collectors.toList()));
                sortedSettings.addAll(settings.stream().filter(setting -> setting instanceof NumberSetting).collect(Collectors.toList()));
                sortedSettings.addAll(settings.stream().filter(setting -> setting instanceof BooleanSetting).collect(Collectors.toList()));

                int settingIndex = 0;
                for (Setting setting : sortedSettings) {
                    if (setting instanceof NumberSetting) {
                        if (MouseUtils.hovered(mouseX, mouseY, panelX + panelWidth / 2f + 76, panelY + titleHeight + offset / 2f - 2 +
                                settingIndex - settingsScroll, panelX + panelWidth / 2f + 76 + 100, panelY + titleHeight + offset / 2f
                                + 2 + settingIndex - settingsScroll) && draggingSlider == null)
                            draggingSlider = (NumberSetting) setting;

                        if (draggingSlider != null) {
                            draggingSlider.setValue(draggingSlider.getMin() + (MathHelper.clamp_double((mouseX - (panelX + panelWidth / 2f + 76))
                                    / 100, 0, 1)) * (draggingSlider.getMax() - draggingSlider.getMin()));
                        }
                    }

                    settingIndex += offset;
                }
            }
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    private List<Setting> getSettings() {
        return currentModule.getSettings().stream().filter(Setting::shouldShow).collect(Collectors.toList());
    }

    @Override
    public void onGuiClosed() {
        listening = null;
        module.disable();
        dragging = false;
        draggingSlider = null;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (listening != null) {
            listening.clearKeys();
            if (keyCode == Keyboard.KEY_ESCAPE) {
                listening = null;
                return;
            }
            listening.addKey(keyCode);
            listening = null;
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
