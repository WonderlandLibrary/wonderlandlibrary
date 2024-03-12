// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.dropdown;

import java.util.Collection;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.impl.render.PostProcessing;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.render.StencilUtil;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.module.impl.render.ClickGUIMod;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.render.Theme;
import dev.tenacity.module.ModuleCollection;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import dev.tenacity.module.Module;
import dev.tenacity.Tenacity;
import java.util.ArrayList;
import dev.tenacity.ui.clickguis.dropdown.components.ModuleRect;
import java.util.List;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.module.Category;
import dev.tenacity.ui.Screen;

public class CategoryPanel implements Screen
{
    private final Category category;
    private final float rectWidth = 105.0f;
    private final float categoryRectHeight = 15.0f;
    private boolean typing;
    public final Pair<Animation, Animation> openingAnimations;
    private List<ModuleRect> moduleRects;
    float actualHeight;
    private final List<String> searchTerms;
    private String searchText;
    private final List<ModuleRect> moduleRectFilter;
    
    public CategoryPanel(final Category category, final Pair<Animation, Animation> openingAnimations) {
        this.actualHeight = 0.0f;
        this.searchTerms = new ArrayList<String>();
        this.moduleRectFilter = new ArrayList<ModuleRect>();
        this.category = category;
        this.openingAnimations = openingAnimations;
    }
    
    @Override
    public void initGui() {
        if (this.moduleRects == null) {
            this.moduleRects = new ArrayList<ModuleRect>();
            for (final Module module : Tenacity.INSTANCE.getModuleCollection().getModulesInCategory(this.category).stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)Module::getName)).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList())) {
                this.moduleRects.add(new ModuleRect(module));
            }
        }
        if (this.moduleRects != null) {
            this.moduleRects.forEach(ModuleRect::initGui);
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.moduleRects != null) {
            this.moduleRects.forEach(moduleRect -> moduleRect.keyTyped(typedChar, keyCode));
        }
    }
    
    @Override
    public void onDrag(final int mouseX, final int mouseY) {
        this.category.getDrag().onDraw(mouseX, mouseY);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        if (this.moduleRects == null) {
            return;
        }
        if (this.category.equals(Category.SCRIPTS) && ModuleCollection.reloadModules) {
            this.moduleRects.clear();
            for (final Module module : Tenacity.INSTANCE.getModuleCollection().getModulesInCategory(this.category).stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)Module::getName)).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList())) {
                this.moduleRects.add(new ModuleRect(module));
            }
            ModuleCollection.reloadModules = false;
            return;
        }
        if (this.openingAnimations == null) {
            return;
        }
        final float alpha = Math.min(1.0f, this.openingAnimations.getFirst().getOutput().floatValue());
        final Theme theme = Theme.getCurrentTheme();
        final Pair<Color, Color> clientColors = HUDMod.getClientColors();
        float alphaValue = alpha * alpha;
        if (ClickGUIMod.transparent.isEnabled()) {
            alphaValue *= 0.75f;
        }
        Color clientFirst = ColorUtil.applyOpacity(clientColors.getFirst(), alphaValue);
        Color clientSecond = ColorUtil.applyOpacity(clientColors.getSecond(), alphaValue);
        final int textColor = ColorUtil.applyOpacity(-1, alpha);
        final float x = this.category.getDrag().getX();
        final float y = this.category.getDrag().getY();
        if (ClickGUIMod.scrollMode.getMode().equals("Value")) {
            Module.allowedClickGuiHeight = ClickGUIMod.clickHeight.getValue().floatValue();
        }
        else {
            final ScaledResolution sr = new ScaledResolution(CategoryPanel.mc);
            Module.allowedClickGuiHeight = 2 * sr.getScaledHeight() / 3.0f;
        }
        final float allowedHeight = Module.allowedClickGuiHeight;
        final boolean hoveringMods = HoveringUtil.isHovering(x, y + 15.0f, 105.0f, allowedHeight, mouseX, mouseY);
        RenderUtil.resetColor();
        final float realHeight = Math.min(this.actualHeight, Module.allowedClickGuiHeight);
        if (ClickGUIMod.outlineAccent.isEnabled()) {
            if (theme.equals(Theme.RED_COFFEE)) {
                final Color temp = clientFirst;
                clientFirst = clientSecond;
                clientSecond = temp;
            }
            if (DropdownClickGUI.gradient) {
                RoundedUtil.drawGradientVertical(x - 0.75f, y - 0.5f, 106.5f, realHeight + 15.0f + 1.5f, 5.0f, clientFirst, clientSecond);
            }
            else {
                RoundedUtil.drawRound(x - 0.75f, y - 0.5f, 106.5f, realHeight + 15.0f + 1.5f, 5.0f, clientFirst);
            }
        }
        else {
            RoundedUtil.drawRound(x - 0.75f, y - 0.5f, 106.5f, realHeight + 15.0f + 1.5f, 5.0f, ColorUtil.tripleColor(20, alphaValue));
            if (!ClickGUIMod.transparent.isEnabled()) {
                Gui.drawRect2(x, y + 15.0f, 105.0, 3.0, clientFirst.getRGB());
            }
            if (DropdownClickGUI.gradient) {
                RoundedUtil.drawGradientVertical(x + 1.0f, y + 15.0f + 1.0f, 103.0f, realHeight - 2.0f, 4.0f, clientFirst, clientSecond);
            }
            else {
                RoundedUtil.drawRound(x + 0.8f, y + 15.0f + 0.8f, 103.4f, realHeight - 1.6f, 3.5f, clientFirst);
            }
        }
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x + 1.0f, y + 15.0f + 5.0f, 103.0f, realHeight - 6.0f, 3.0f, Color.BLACK);
        Gui.drawRect2(x, y + 15.0f, 105.0, 10.0, Color.BLACK.getRGB());
        StencilUtil.readStencilBuffer(1);
        final double scroll = this.category.getScroll().getScroll();
        double count = 0.0;
        final float rectHeight = 15.0f;
        for (final ModuleRect moduleRect : this.getModuleRects()) {
            moduleRect.alpha = alpha;
            moduleRect.x = x - 0.5f;
            moduleRect.height = rectHeight;
            moduleRect.panelLimitY = y + 15.0f - 2.0f;
            moduleRect.y = (float)(y + 15.0f + count * rectHeight + MathUtils.roundToHalf(scroll));
            moduleRect.width = 106.0f;
            moduleRect.drawScreen(mouseX, mouseY);
            count += 1.0 + moduleRect.getSettingSize() * 1.0666667222976685;
        }
        this.typing = this.getModuleRects().stream().anyMatch(ModuleRect::isTyping);
        this.actualHeight = (float)(count * rectHeight);
        if (hoveringMods) {
            this.category.getScroll().onScroll(25);
            final float hiddenHeight = (float)(count * rectHeight - allowedHeight);
            this.category.getScroll().setMaxScroll(Math.max(0.0f, hiddenHeight));
        }
        StencilUtil.uninitStencilBuffer();
        RenderUtil.resetColor();
        final String name = this.category.name;
        float yMovement = 0.0f;
        switch (name) {
            case "Movement":
            case "Player":
            case "Misc": {
                yMovement = 0.5f;
                break;
            }
            case "Render": {
                yMovement = 1.0f;
                break;
            }
            case "Exploit":
            case "Scripts": {
                yMovement = 1.0f;
                break;
            }
            default: {
                yMovement = 0.0f;
                break;
            }
        }
        RenderUtil.resetColor();
        final float textWidth = CategoryPanel.tenacityBoldFont22.getStringWidth(this.category.name + " ") / 2.0f;
        CategoryPanel.iconFont20.drawCenteredString(this.category.icon, x + 52.5f + textWidth, y + CategoryPanel.iconFont20.getMiddleOfBox(15.0f) + yMovement, textColor);
        RenderUtil.resetColor();
        CategoryPanel.tenacityBoldFont22.drawString(this.category.name, x + (52.5f - textWidth - CategoryPanel.iconFont20.getStringWidth(this.category.icon) / 2.0f), y + CategoryPanel.tenacityBoldFont22.getMiddleOfBox(15.0f), textColor);
    }
    
    public void renderEffects() {
        final float x = this.category.getDrag().getX();
        final float y = this.category.getDrag().getY();
        float alpha = Math.min(1.0f, this.openingAnimations.getFirst().getOutput().floatValue());
        alpha *= alpha;
        final Theme theme = Theme.getCurrentTheme();
        final Pair<Color, Color> clientColors = theme.getColors();
        Color clientFirst = ColorUtil.applyOpacity(clientColors.getFirst(), alpha);
        Color clientSecond = ColorUtil.applyOpacity(clientColors.getSecond(), alpha);
        final float allowedHeight = Math.min(this.actualHeight, Module.allowedClickGuiHeight);
        final boolean glow = PostProcessing.glowOptions.getSetting("ClickGui").isEnabled();
        if (!ClickGUIMod.outlineAccent.isEnabled()) {
            RoundedUtil.drawRound(x - 0.75f, y - 0.5f, 106.5f, allowedHeight + 15.0f + 1.5f, 5.0f, Color.BLACK);
            return;
        }
        if (DropdownClickGUI.gradient && glow && ClickGUIMod.outlineAccent.isEnabled()) {
            if (theme.equals(Theme.RED_COFFEE)) {
                final Color temp = clientFirst;
                clientFirst = clientSecond;
                clientSecond = temp;
            }
            RoundedUtil.drawGradientVertical(x - 0.75f, y - 0.5f, 106.5f, allowedHeight + 15.0f + 1.5f, 5.0f, clientFirst, clientSecond);
        }
        else {
            RoundedUtil.drawRound(x - 0.75f, y - 0.5f, 106.5f, allowedHeight + 15.0f + 1.5f, 5.0f, (glow && ClickGUIMod.outlineAccent.isEnabled()) ? clientFirst : ColorUtil.applyOpacity(Color.BLACK, alpha));
        }
    }
    
    public void drawToolTips(final int mouseX, final int mouseY) {
        this.getModuleRects().forEach(moduleRect -> moduleRect.tooltipObject.drawScreen(mouseX, mouseY));
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final boolean canDrag = HoveringUtil.isHovering(this.category.getDrag().getX(), this.category.getDrag().getY(), 105.0f, 15.0f, mouseX, mouseY);
        this.category.getDrag().onClick(mouseX, mouseY, button, canDrag);
        this.getModuleRects().forEach(moduleRect -> moduleRect.mouseClicked(mouseX, mouseY, button));
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.category.getDrag().onRelease(state);
        this.getModuleRects().forEach(moduleRect -> moduleRect.mouseReleased(mouseX, mouseY, state));
    }
    
    public List<ModuleRect> getModuleRects() {
        if (!Tenacity.INSTANCE.getSearchBar().isFocused()) {
            return this.moduleRects;
        }
        final String search = Tenacity.INSTANCE.getSearchBar().getSearchField().getText();
        if (search.equals(this.searchText)) {
            return this.moduleRectFilter;
        }
        this.searchText = search;
        this.moduleRectFilter.clear();
        for (final ModuleRect moduleRect2 : this.moduleRects) {
            this.searchTerms.clear();
            final Module module = moduleRect2.module;
            this.searchTerms.add(module.getName());
            this.searchTerms.add(module.getCategory().name);
            if (!module.getAuthor().isEmpty()) {
                this.searchTerms.add(module.getAuthor());
            }
            for (final Setting setting : module.getSettingsList()) {
                this.searchTerms.add(setting.name);
            }
            moduleRect2.setSearchScore(FuzzySearch.extractOne(search, (Collection)this.searchTerms).getScore());
        }
        this.moduleRectFilter.addAll(this.moduleRects.stream().filter(moduleRect -> moduleRect.getSearchScore() > 60).sorted(Comparator.comparingInt(ModuleRect::getSearchScore).reversed()).collect((Collector<? super Object, ?, Collection<? extends ModuleRect>>)Collectors.toList()));
        return this.moduleRectFilter;
    }
    
    public boolean isTyping() {
        return this.typing;
    }
}
