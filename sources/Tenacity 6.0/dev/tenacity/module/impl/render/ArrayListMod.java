// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import net.minecraft.util.StringUtils;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.animations.Animation;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.animations.Direction;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.event.impl.render.ShaderEvent;
import java.util.Comparator;
import dev.tenacity.module.ModuleCollection;
import dev.tenacity.module.settings.ParentAttribute;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.Tenacity;
import dev.tenacity.module.Category;
import dev.tenacity.utils.objects.Dragging;
import java.util.List;
import dev.tenacity.utils.font.AbstractFontRenderer;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Module;

public class ArrayListMod extends Module
{
    public final BooleanSetting importantModules;
    private final ModeSetting textShadow;
    private final MultipleBoolSetting rectangle;
    private final BooleanSetting partialGlow;
    private final BooleanSetting minecraftFont;
    private final MultipleBoolSetting fontSettings;
    public final NumberSetting height;
    private final BooleanSetting spacedNames;
    private final ModeSetting animation;
    private final NumberSetting colorIndex;
    private final NumberSetting colorSpeed;
    private final BooleanSetting background;
    private final BooleanSetting backgroundColor;
    private final NumberSetting backgroundAlpha;
    public AbstractFontRenderer font;
    public List<Module> modules;
    public Dragging arraylistDrag;
    public String longest;
    Module lastModule;
    int lastCount;
    
    public ArrayListMod() {
        super("ArrayList", "Array List", Category.RENDER, "Displays your active modules");
        this.importantModules = new BooleanSetting("Important", false);
        this.textShadow = new ModeSetting("Text Shadow", "Black", new String[] { "Colored", "Black", "None" });
        this.rectangle = new MultipleBoolSetting("Rectangle", new BooleanSetting[] { new BooleanSetting("Top", true), new BooleanSetting("Bottom", false), new BooleanSetting("Left", false), new BooleanSetting("Right", false) });
        this.partialGlow = new BooleanSetting("Partial Glow", true);
        this.minecraftFont = new BooleanSetting("Minecraft Font", false);
        this.fontSettings = new MultipleBoolSetting("Font Settings", new BooleanSetting[] { new BooleanSetting("Bold", false), new BooleanSetting("Small Font", false), this.minecraftFont });
        this.height = new NumberSetting("Height", 11.0, 20.0, 9.0, 0.5);
        this.spacedNames = new BooleanSetting("Spaced Modules", true);
        this.animation = new ModeSetting("Animation", "Scale in", new String[] { "Move in", "Scale in" });
        this.colorIndex = new NumberSetting("Color Seperation", 20.0, 100.0, 5.0, 1.0);
        this.colorSpeed = new NumberSetting("Color Speed", 15.0, 30.0, 2.0, 1.0);
        this.background = new BooleanSetting("Background", true);
        this.backgroundColor = new BooleanSetting("Background Color", false);
        this.backgroundAlpha = new NumberSetting("Background Alpha", 0.35, 1.0, 0.0, 0.01);
        this.font = ArrayListMod.tenacityFont.size(20);
        this.arraylistDrag = Tenacity.INSTANCE.createDrag(this, "arraylist", 2.0f, 1.0f);
        this.longest = "";
        this.addSettings(this.importantModules, this.rectangle, this.partialGlow, this.textShadow, this.fontSettings, this.height, this.spacedNames, this.animation, this.colorIndex, this.colorSpeed, this.background, this.backgroundColor, this.backgroundAlpha);
        this.backgroundAlpha.addParent(this.background, ParentAttribute.BOOLEAN_CONDITION);
        this.backgroundColor.addParent(this.background, ParentAttribute.BOOLEAN_CONDITION);
        this.partialGlow.addParent(this.rectangle, r -> r.isEnabled("Top") || r.isEnabled("Bottom") || r.isEnabled("Left") || r.isEnabled("Right"));
        if (!this.enabled) {
            this.toggleSilent();
        }
    }
    
    public void getModulesAndSort() {
        if (this.modules == null || ModuleCollection.reloadModules) {
            final List<Class<? extends Module>> hiddenModules = Tenacity.INSTANCE.getModuleCollection().getHiddenModules();
            final List<Module> moduleList = Tenacity.INSTANCE.getModuleCollection().getModules();
            moduleList.removeIf(module -> hiddenModules.stream().anyMatch(moduleClass -> moduleClass == module.getClass()));
            this.modules = moduleList;
        }
        String string;
        final StringBuilder sb;
        final String name;
        this.modules.sort(Comparator.comparingDouble(m -> {
            new StringBuilder().append(this.spacedNames.isEnabled() ? m.getSpacedName() : m.getName());
            if (m.hasMode()) {
                string = " " + m.getSuffix();
            }
            else {
                string = "";
            }
            name = HUDMod.get(sb.append(string).toString());
            return (double)this.font.getStringWidth(this.applyText(name));
        }).reversed());
    }
    
    @Override
    public void onShaderEvent(final ShaderEvent e) {
        if (this.modules == null) {
            return;
        }
        float yOffset = 0.0f;
        final ScaledResolution sr = new ScaledResolution(ArrayListMod.mc);
        int count = 0;
        for (final Module module : this.modules) {
            if (this.importantModules.isEnabled() && module.getCategory() == Category.RENDER) {
                continue;
            }
            final Animation moduleAnimation = module.getAnimation();
            if (!module.isEnabled() && moduleAnimation.finished(Direction.BACKWARDS)) {
                continue;
            }
            String displayText = HUDMod.get(this.spacedNames.isEnabled() ? module.getSpacedName() : module.getName()) + (module.hasMode() ? (" §7" + module.getSuffix()) : "");
            displayText = this.applyText(displayText);
            final float textWidth = this.font.getStringWidth(displayText);
            final float xValue = sr.getScaledWidth() - this.arraylistDrag.getX();
            final boolean flip = xValue <= sr.getScaledWidth() / 2.0f;
            float x = flip ? xValue : (sr.getScaledWidth() - (textWidth + this.arraylistDrag.getX()));
            final float y = yOffset + this.arraylistDrag.getY();
            final float heightVal = this.height.getValue().floatValue() + 1.0f;
            boolean scaleIn = false;
            final String mode = this.animation.getMode();
            switch (mode) {
                case "Move in": {
                    if (flip) {
                        x -= Math.abs((moduleAnimation.getOutput().floatValue() - 1.0f) * (sr.getScaledWidth() - (this.arraylistDrag.getX() + textWidth)));
                        break;
                    }
                    x += Math.abs((moduleAnimation.getOutput().floatValue() - 1.0f) * (this.arraylistDrag.getX() + textWidth));
                    break;
                }
                case "Scale in": {
                    if (!moduleAnimation.isDone()) {
                        RenderUtil.scaleStart(x + this.font.getStringWidth(displayText) / 2.0f, y + heightVal / 2.0f - this.font.getHeight() / 2.0f, moduleAnimation.getOutput().floatValue());
                    }
                    scaleIn = true;
                    break;
                }
            }
            final int index = (int)(count * this.colorIndex.getValue());
            final Pair<Color, Color> colors = HUDMod.getClientColors();
            Color textcolor = ColorUtil.interpolateColorsBackAndForth(this.colorSpeed.getValue().intValue(), index, colors.getFirst(), colors.getSecond(), false);
            if (HUDMod.isRainbowTheme()) {
                textcolor = ColorUtil.rainbow(this.colorSpeed.getValue().intValue(), index, HUDMod.color1.getRainbow().getSaturation(), 1.0f, 1.0f);
            }
            if (this.background.isEnabled()) {
                final float offset = this.minecraftFont.isEnabled() ? 4.0f : 5.0f;
                final int rectColor = e.getBloomOptions().getSetting("Arraylist").isEnabled() ? textcolor.getRGB() : ((this.rectangle.getSetting("Top").isEnabled() && this.rectangle.getSetting("Bottom").isEnabled() && this.rectangle.getSetting("Left").isEnabled() && this.rectangle.getSetting("Right").isEnabled() && this.partialGlow.isEnabled()) ? textcolor.getRGB() : Color.BLACK.getRGB());
                Gui.drawRect2(x - 2.0f, y, this.font.getStringWidth(displayText) + offset, heightVal, scaleIn ? ColorUtil.applyOpacity(rectColor, moduleAnimation.getOutput().floatValue()) : rectColor);
                final float offset2 = this.minecraftFont.isEnabled() ? 1.0f : 0.0f;
                int rectangleColor = this.partialGlow.isEnabled() ? textcolor.getRGB() : Color.BLACK.getRGB();
                if (scaleIn) {
                    rectangleColor = ColorUtil.applyOpacity(rectangleColor, moduleAnimation.getOutput().floatValue());
                }
                if (this.rectangle.getSetting("Top").isEnabled() && count == 0) {
                    Gui.drawRect2(x - 2.0f, y - 1.0f, textWidth + 5.0f - offset2, 9.0, rectangleColor);
                }
                if (this.rectangle.getSetting("Left").isEnabled() && count == 0) {
                    if (flip) {
                        Gui.drawRect2(x + textWidth - 7.0f, y, 9.0, heightVal, rectangleColor);
                    }
                    else {
                        Gui.drawRect2(x - 3.0f, y, 9.0, heightVal, textcolor.getRGB());
                    }
                }
                if (this.rectangle.getSetting("Right").isEnabled() && count == 0) {
                    if (flip) {
                        Gui.drawRect2(x - 3.0f, y, 9.0, heightVal, textcolor.getRGB());
                    }
                    else {
                        Gui.drawRect2(x + textWidth - 7.0f, y, 9.0, heightVal, rectangleColor);
                    }
                }
            }
            if (this.animation.is("Scale in") && !moduleAnimation.isDone()) {
                RenderUtil.scaleEnd();
            }
            yOffset += moduleAnimation.getOutput().floatValue() * heightVal;
            ++count;
        }
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent e) {
        this.font = this.getFont();
        this.getModulesAndSort();
        String longestModule = "";
        float longestWidth = 0.0f;
        double yOffset = 0.0;
        final ScaledResolution sr = new ScaledResolution(ArrayListMod.mc);
        int count = 0;
        for (final Module module : this.modules) {
            if (this.importantModules.isEnabled() && module.getCategory() == Category.RENDER) {
                continue;
            }
            final Animation moduleAnimation = module.getAnimation();
            moduleAnimation.setDirection(module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
            if (!module.isEnabled() && moduleAnimation.finished(Direction.BACKWARDS)) {
                continue;
            }
            String displayText = HUDMod.get((this.spacedNames.isEnabled() ? module.getSpacedName() : module.getName()) + (module.hasMode() ? ((module.getCategory().equals(Category.SCRIPTS) ? " §c" : " §7") + module.getSuffix()) : ""));
            displayText = this.applyText(displayText);
            final float textWidth = this.font.getStringWidth(displayText);
            if (textWidth > longestWidth) {
                longestModule = displayText;
                longestWidth = textWidth;
            }
            final double xValue = sr.getScaledWidth() - this.arraylistDrag.getX();
            final boolean flip = xValue <= sr.getScaledWidth() / 2.0f;
            float x = (float)(flip ? xValue : (sr.getScaledWidth() - (textWidth + this.arraylistDrag.getX())));
            float alphaAnimation = 1.0f;
            float y = (float)(yOffset + this.arraylistDrag.getY());
            final float heightVal = (float)(this.height.getValue() + 1.0);
            final String mode = this.animation.getMode();
            switch (mode) {
                case "Move in": {
                    if (flip) {
                        x -= Math.abs((moduleAnimation.getOutput().floatValue() - 1.0f) * (sr.getScaledWidth() - (this.arraylistDrag.getX() - textWidth)));
                        break;
                    }
                    x += Math.abs((moduleAnimation.getOutput().floatValue() - 1.0f) * (this.arraylistDrag.getX() + textWidth));
                    break;
                }
                case "Scale in": {
                    if (!moduleAnimation.isDone()) {
                        RenderUtil.scaleStart(x + this.font.getStringWidth(displayText) / 2.0f, y + heightVal / 2.0f - this.font.getHeight() / 2.0f, moduleAnimation.getOutput().floatValue());
                    }
                    alphaAnimation = moduleAnimation.getOutput().floatValue();
                    break;
                }
            }
            final int index = (int)(count * this.colorIndex.getValue());
            final Pair<Color, Color> colors = HUDMod.getClientColors();
            Color textcolor = ColorUtil.interpolateColorsBackAndForth(this.colorSpeed.getValue().intValue(), index, colors.getFirst(), colors.getSecond(), false);
            if (HUDMod.isRainbowTheme()) {
                textcolor = ColorUtil.rainbow(this.colorSpeed.getValue().intValue(), index, HUDMod.color1.getRainbow().getSaturation(), 1.0f, 1.0f);
            }
            if (this.background.isEnabled()) {
                final float offset = this.minecraftFont.isEnabled() ? 4.0f : 5.0f;
                final Color color = this.backgroundColor.isEnabled() ? textcolor : new Color(10, 10, 10);
                Gui.drawRect2(x - 2.0f, y, this.font.getStringWidth(displayText) + offset, heightVal, ColorUtil.applyOpacity(color, this.backgroundAlpha.getValue().floatValue() * alphaAnimation).getRGB());
            }
            final float offset = this.minecraftFont.isEnabled() ? 1.0f : 0.0f;
            if (this.rectangle.getSetting("Top").isEnabled() && count == 0) {
                Gui.drawRect2(x - 2.0f, y - 1.0f, textWidth + 5.0f - offset, 1.0, textcolor.getRGB());
            }
            if (this.rectangle.getSetting("Left").isEnabled()) {
                if (flip) {
                    Gui.drawRect2(x + textWidth + 2.0f, y, 1.0, heightVal, textcolor.getRGB());
                }
                else {
                    Gui.drawRect2(x - 3.0f, y, 1.0, heightVal, textcolor.getRGB());
                }
            }
            if (this.rectangle.getSetting("Right").isEnabled()) {
                if (flip) {
                    Gui.drawRect2(x - 3.0f, y, 1.0, heightVal, textcolor.getRGB());
                }
                else {
                    Gui.drawRect2(x + textWidth + 2.0f, y, 1.0, heightVal, textcolor.getRGB());
                }
            }
            if (this.rectangle.getSetting("Left").isEnabled() && this.rectangle.getSetting("Right").isEnabled() && this.rectangle.getSetting("Top").isEnabled() && this.rectangle.getSetting("Bottom").isEnabled()) {
                if (count != 0) {
                    final String modText = this.applyText(HUDMod.get(this.lastModule.getName() + (this.lastModule.hasMode() ? (" " + this.lastModule.getSuffix()) : "")));
                    final float texWidth = this.font.getStringWidth(modText) - textWidth;
                    if (flip) {
                        Gui.drawRect2(x + textWidth + 3.0f, y, 1.0, heightVal, textcolor.getRGB());
                        Gui.drawRect2(x + textWidth + 3.0f, y, texWidth + 1.0f, 1.0, textcolor.getRGB());
                    }
                    else {
                        Gui.drawRect2(x - (3.0f + texWidth), y, texWidth + 1.0f, 1.0, textcolor.getRGB());
                        Gui.drawRect2(x - 3.0f, y, 1.0, heightVal, textcolor.getRGB());
                    }
                    if (count == this.lastCount - 1) {
                        Gui.drawRect2(x - 3.0f, y + heightVal, textWidth + 6.0f, 1.0, textcolor.getRGB());
                    }
                }
                else {
                    if (flip) {
                        Gui.drawRect2(x + textWidth + 3.0f, y, 1.0, heightVal, textcolor.getRGB());
                    }
                    else {
                        Gui.drawRect2(x - 3.0f, y, 1.0, heightVal, textcolor.getRGB());
                    }
                    Gui.drawRect2(x - 3.0f, y - 1.0f, textWidth + 6.0f, 1.0, textcolor.getRGB());
                }
                if (flip) {
                    Gui.drawRect2(x - 3.0f, y, 1.0, heightVal, textcolor.getRGB());
                }
                else {
                    Gui.drawRect2(x + textWidth + 2.0f, y, 1.0, heightVal, textcolor.getRGB());
                }
            }
            final float textYOffset = this.minecraftFont.isEnabled() ? 0.5f : 0.0f;
            y += textYOffset;
            final Color color2 = ColorUtil.applyOpacity(textcolor, alphaAnimation);
            final String mode2 = this.textShadow.getMode();
            switch (mode2) {
                case "None": {
                    this.font.drawString(displayText, x, y + this.font.getMiddleOfBox(heightVal), color2.getRGB());
                    break;
                }
                case "Colored": {
                    RenderUtil.resetColor();
                    this.font.drawString(StringUtils.stripColorCodes(displayText), x + 1.0f, y + this.font.getMiddleOfBox(heightVal) + 1.0f, ColorUtil.darker(color2, 0.5f).getRGB());
                    RenderUtil.resetColor();
                    this.font.drawString(displayText, x, y + this.font.getMiddleOfBox(heightVal), color2.getRGB());
                    break;
                }
                case "Black": {
                    RenderUtil.resetColor();
                    final float f = this.minecraftFont.isEnabled() ? 1.0f : 0.5f;
                    this.font.drawString(StringUtils.stripColorCodes(displayText), x + f, y + this.font.getMiddleOfBox(heightVal) + f, ColorUtil.applyOpacity(Color.BLACK, alphaAnimation));
                    RenderUtil.resetColor();
                    this.font.drawString(displayText, x, y + this.font.getMiddleOfBox(heightVal), color2.getRGB());
                    break;
                }
            }
            if (this.animation.is("Scale in") && !moduleAnimation.isDone()) {
                RenderUtil.scaleEnd();
            }
            this.lastModule = module;
            yOffset += moduleAnimation.getOutput().floatValue() * heightVal;
            ++count;
        }
        this.lastCount = count;
        this.longest = longestModule;
    }
    
    private String applyText(final String text) {
        if (this.minecraftFont.isEnabled() && this.fontSettings.getSetting("Bold").isEnabled()) {
            return "§l" + text.replace("§7", "§7§l");
        }
        return text;
    }
    
    private AbstractFontRenderer getFont() {
        final boolean smallFont = this.fontSettings.getSetting("Small Font").isEnabled();
        if (this.minecraftFont.isEnabled()) {
            return ArrayListMod.mc.fontRendererObj;
        }
        if (!this.fontSettings.getSetting("Bold").isEnabled()) {
            return smallFont ? ArrayListMod.tenacityFont18 : ArrayListMod.tenacityFont20;
        }
        if (smallFont) {
            return ArrayListMod.tenacityBoldFont18;
        }
        return ArrayListMod.tenacityBoldFont20;
    }
}
