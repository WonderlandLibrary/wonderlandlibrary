/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.Rise;
import dev.rise.event.impl.other.KeyEvent;
import dev.rise.event.impl.render.BlurEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.ui.ingame.IngameGUI;
import dev.rise.util.math.MathUtil;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.render.ColorUtil;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "TabGui", description = "Displays a Gui where you can toggle modules", category = Category.RENDER)
public final class TabGui extends Module {

    private final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("Comfortaa 18");
    private final TTFFontRenderer fontRendererSmall = CustomFont.FONT_MANAGER.getFont("Comfortaa 16");

    private Category selectedCategory = Category.COMBAT;
    private Category lastCategory = selectedCategory;
    private Category lastLastCategory = lastCategory;

    private final TimeUtil timer = new TimeUtil();
    private final TimeUtil moduleTimer = new TimeUtil();
    private final TimeUtil rectTimer = new TimeUtil();

    private float selectorPositionY = 0.0F;
    private float selectedTextPositionX = 0.0F;

    private float moduleSelectorPositionY = 0.0F;
    private float selectedModuleTextPositionX = 0.0F;

    private float moduleRectWidth = 0.0F;

    private int lastModule;
    private int lastLastModule;

    private boolean moduleCocked;
    private boolean categoryCocked;

    private boolean expand;

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        // Basis exemptions for the tab gui.
        if (mc.gameSettings.showDebugInfo) return;

        // y position offset depending on theme.
        float yOffset = 0;

        // Get the current theme of the client.
        final ModeSetting setting = ((ModeSetting) Rise.INSTANCE.getModuleManager().getSetting("Interface", "Theme"));

        // If the setting was null return to prevent crashes bc shit setting system.
        if (setting == null) return;

        // Get the theme and convert it to lower case.
        final String theme = setting.getMode().toLowerCase();

        switch (theme) {
            case "rise rainbow":
            case "rise":
                yOffset = 3;
                break;
        }

        // The base positions for the TabGUI.
        final float x = 2;
        final float y = 25 + yOffset;

        // The size of our category boxes.
        final float categoryHeight = 12.0F;
        final float categoryWidth = 61.0F;

        //Offset of font on the x axis
        final float offset = 2;

        // Default category and module pos.
        final float categoryPosition = x + 2;
        final float modulePosition = x + categoryWidth + 2;

        // The offset for the category list.
        float categoryOffset = 0.0F;

        // Get the color theme of the client and adjust based on it.
        int color = ThemeUtil.getThemeColorInt(ThemeType.GENERAL);

        // If the user is using a rainbow theme adjust based on it.
        if (theme.contains("rainbow")) {
            color = ColorUtil.getColor(1 * 0 * 1.4f, 0.5f, 1);
        }

        // If the user is on the blend theme blend accordingly.
        else if (theme.contains("blend")) {
            final Color color1 = new Color(78, 161, 253, 100);
            final Color color2 = new Color(78, 253, 154, 100);

            color = ColorUtil.mixColors(color1, color2, (Math.sin(IngameGUI.ticks + 0 * 0.4f) + 1) * 0.5f).hashCode();
        }

        // Render the background in order for the text to be visible.
        RenderUtil.roundedRect(x, y - 1, categoryWidth, categoryHeight * (Category.values().length - 3) + 1, 6, new Color(0, 0, 0, 140));

        // Render the categories in order for later selection.
        for (final Category category : Category.values()) {
            if (category != Category.CONFIGS && category != Category.STATISTICS && category != Category.STORE) {
                // Process category name.
                final String text = StringUtils.capitalize(category.name().toLowerCase());

                // If the last category was different than current player must have scrolled.
                if (categoryCocked) {
                    selectedTextPositionX = 1;
                    categoryCocked = false;
                }

                // If the last module was different than current player must have scrolled.
                if (moduleCocked) {
                    selectedModuleTextPositionX = 0;
                    moduleCocked = false;
                }

                if (category == selectedCategory) {
                    // Update the animations
                    if (timer.hasReached(5)) {
                        selectorPositionY = MathUtil.lerp(selectorPositionY, y + categoryOffset, 0.2F);
                        selectedTextPositionX = MathUtil.lerp(selectedTextPositionX, 3, 0.2F);

                        timer.reset();
                    }

                    // Get all of the modules on the selected category and put them on a list.
                    final List<Module> modulesOnCategory = getModulesFromCategory(selectedCategory);

                    // Offset for the module rendering.
                    float moduleOffset = 0.0F;

                    // The size of our module boxes.
                    final float moduleHeight = 9.0F;
                    // Get the longest text in the module list with a stream for it to fit on the box.
                    final float moduleWidth = getMaximum();

                    if (moduleRectWidth > 2)
                        RenderUtil.roundedRect(x + categoryWidth + 1, y + categoryOffset - 2, moduleRectWidth, moduleHeight * modulesOnCategory.size() + 3, 6, new Color(0, 0, 0, 140));

                    // Render the category name.
                    fontRenderer.drawString(text, categoryPosition + selectedTextPositionX + offset, (float) round(y + categoryOffset + ((categoryHeight / 2.0F) - (fontRenderer.getHeight() / 2.0F)) + 1, 1), -1);

                    // Draw a rectangle which shows if the category is selected.
                    Gui.drawRect(x + 1 + offset, selectorPositionY + 1, x + 2, selectorPositionY + categoryHeight - 2, color);

                    // Update animations.
                    if (rectTimer.hasReached(10)) {
                        moduleRectWidth = MathUtil.lerp(moduleRectWidth, (expand) ? moduleWidth + offset + 2 : 0, 0.2F);

                        rectTimer.reset();
                    }

                    // If the user expanded the category show all modules in that category.
                    if (moduleRectWidth > 2) {
                        // Push a GL Matrix and enable the scissors module.
                        GlStateManager.pushMatrix();
                        GL11.glEnable(GL11.GL_SCISSOR_TEST);

                        // Scissor the text according to the box behind it.
                        RenderUtil.scissor(x + categoryWidth + 1, y + categoryOffset - 2, moduleRectWidth, moduleHeight * modulesOnCategory.size() + 3);

                        // If the category has modules list them else tell the user there are no available modules.
                        if (modulesOnCategory.size() > 0) {
                            // Render the background in order for the text to be visible.
                            int count = 0;

                            // Loop thru all of the modules on the category selected and do the rendering.
                            for (final Module module : modulesOnCategory) {
                                if (count == selectedCategory.moduleIndex) {
                                    // Update animations for modules.
                                    if (moduleTimer.hasReached(1)) {
                                        moduleSelectorPositionY = MathUtil.lerp(moduleSelectorPositionY, y + categoryOffset + (moduleHeight * count), 0.1F);
                                        selectedModuleTextPositionX = MathUtil.lerp(selectedModuleTextPositionX, 2, 0.1F);

                                        moduleTimer.reset();
                                    }

                                    // Render the category name.
                                    fontRendererSmall.drawString(module.getModuleInfo().name(), modulePosition + selectedModuleTextPositionX + offset, y + categoryOffset + (moduleOffset + ((moduleHeight / 2.0F) - (fontRendererSmall.getHeight() / 2.0F)) + 1), -1);
                                } else {
                                    // Render the category name.
                                    fontRendererSmall.drawString(module.getModuleInfo().name(), modulePosition + offset + (count == lastLastModule ? 2 - selectedModuleTextPositionX : 0), y + categoryOffset + (moduleOffset + ((moduleHeight / 2.0F) - (fontRendererSmall.getHeight() / 2.0F)) + 1), -1);
                                }

                                // Adjust our offset by the moduleHeight of the box.
                                moduleOffset += moduleHeight;
                                ++count;
                            }

                            // Draw a rectangle which shows if the module is selected.
                            Gui.drawRect(x + categoryWidth + 2, moduleSelectorPositionY + 1, x + categoryWidth + 3, moduleSelectorPositionY + moduleHeight - 2, color);
                        } else {
                            // Render the background in order for the text to be visible.
                            fontRendererSmall.drawString("No modules.", x + categoryWidth + 1 + offset, y + categoryOffset + (moduleHeight / 2.0F), new Color(0, 0, 0, 140).getRGB());
                        }

                        // Disable GL Scissors and pop our matrix.
                        GL11.glDisable(GL11.GL_SCISSOR_TEST);
                        GlStateManager.popMatrix();
                    }
                } else {
                    // Render the category name.
                    fontRenderer.drawString(text, (float) round(x + (category == lastLastCategory ? 3 - selectedTextPositionX : 0) + offset, 1), (float) (y + categoryOffset + ((categoryHeight / 2.0F) - round(fontRenderer.getHeight() / 2.0F, 1)) + 1), -1);
                }

                // Adjust our offset by the categoryHeight of the box.
                categoryOffset += categoryHeight;
            }
        }

        lastLastCategory = selectedCategory != lastCategory ? lastCategory : lastLastCategory;
        lastCategory = selectedCategory;

        lastLastModule = selectedCategory.moduleIndex != lastModule ? lastModule : lastLastModule;
        lastModule = selectedCategory.moduleIndex;
    }

    @Override
    public void onBlur(final BlurEvent event) {
        // Basis exemptions for the tab gui.
        if (mc.gameSettings.showDebugInfo) return;

        // y position offset depending on theme.
        float yOffset = 0;

        // Get the current theme of the client.
        final ModeSetting setting = ((ModeSetting) Rise.INSTANCE.getModuleManager().getSetting("Interface", "Theme"));

        // If the setting was null return to prevent crashes bc shit setting system.
        if (setting == null) return;

        // Get the theme and convert it to lower case.
        final String theme = setting.getMode().toLowerCase();

        switch (theme) {
            case "rise rainbow":
            case "rise":
                yOffset = 3;
                break;
        }

        // The base positions for the TabGUI.
        final float x = 2;
        final float y = 25 + yOffset;

        // The size of our category boxes.
        final float categoryHeight = 12.0F;
        final float categoryWidth = 61.0F;

        // Render the background in order for the text to be visible.
        RenderUtil.roundedRect(x, y - 1, categoryWidth, categoryHeight * (Category.values().length - 3) + 1, 6, Color.BLACK);
    }

    @Override
    public void onKey(final KeyEvent event) {
        final int key = event.getKey();
        final List<Module> modulesOnCategory = getModulesFromCategory(selectedCategory);

        switch (key) {
            case Keyboard.KEY_UP: {
                if (expand) {
                    if (selectedCategory.moduleIndex <= 0) {
                        selectedCategory.moduleIndex = modulesOnCategory.size() - 2;
                    } else {
                        selectedCategory.moduleIndex--;
                    }

                    moduleCocked = true;
                } else {

                    if (moduleRectWidth > 1)
                        return;

                    if (selectedCategory.ordinal() <= 0) {
                        selectedCategory = Category.values()[Category.values().length - 4];
                    } else {
                        selectedCategory = Category.values()[selectedCategory.ordinal() - 1];
                    }

                    categoryCocked = true;
                }

                break;
            }

            case Keyboard.KEY_DOWN: {
                if (expand) {
                    if (selectedCategory.moduleIndex >= modulesOnCategory.size() - 1) {
                        selectedCategory.moduleIndex = 0;
                    } else {
                        selectedCategory.moduleIndex++;
                    }

                    moduleCocked = true;
                } else {

                    if (moduleRectWidth > 1)
                        return;

                    if (selectedCategory.ordinal() >= Category.values().length - 4) {
                        selectedCategory = Category.values()[0];
                    } else {
                        selectedCategory = Category.values()[selectedCategory.ordinal() + 1];
                    }

                    categoryCocked = true;
                }

                break;
            }

            case Keyboard.KEY_RIGHT: {
                if (expand) {
                    modulesOnCategory.get(selectedCategory.moduleIndex).toggleModule();
                } else {
                    expand = true;
                }

                break;
            }

            case Keyboard.KEY_LEFT: {
                expand = false;

                break;
            }
        }
    }

    private float getMaximum() {
        float maximum = Float.MIN_VALUE;

        for (final Module module : Rise.INSTANCE.getModuleManager().getModuleList()) {
            if (module.getModuleInfo().category() == selectedCategory) {
                final float width = fontRendererSmall.getWidth(module.getModuleInfo().name()) + 2;

                if (width > maximum) {
                    maximum = width;
                }
            }
        }

        return maximum;
    }

    private List<Module> getModulesFromCategory(final Category category) {
        final List<Module> modules = new ArrayList<>();

        for (final Module module : Rise.INSTANCE.getModuleManager().getModuleList()) {
            if (module.getModuleInfo().category() == category) {
                modules.add(module);
            }
        }

        return modules;
    }

    private static double round(final double value, final int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
