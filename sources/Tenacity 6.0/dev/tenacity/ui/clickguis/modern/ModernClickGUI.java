// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.modern;

import java.util.Comparator;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import dev.tenacity.module.settings.Setting;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Function;
import java.util.Collection;
import dev.tenacity.ui.searchbar.SearchBar;
import dev.tenacity.ui.sidegui.SideGUI;
import java.util.Iterator;
import dev.tenacity.utils.font.FontUtil;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.StencilUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.module.impl.movement.InventoryMove;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCollection;
import java.util.function.Consumer;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.module.impl.render.ClickGUIMod;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.ui.clickguis.modern.components.CategoryButton;
import dev.tenacity.ui.clickguis.modern.components.ModuleRect;
import java.util.ArrayList;
import java.util.HashMap;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.Category;
import dev.tenacity.ui.clickguis.modern.components.Component;
import dev.tenacity.ui.clickguis.modern.components.ClickCircle;
import java.util.List;
import java.awt.Color;
import dev.tenacity.utils.objects.Drag;
import net.minecraft.client.gui.GuiScreen;

public class ModernClickGUI extends GuiScreen
{
    public static final Drag drag;
    public static boolean searching;
    private final Color backgroundColor;
    private final Color categoryColor;
    private final Color lighterGray;
    private final List<ClickCircle> circleClicks;
    private final List<Component> categories;
    public float rectHeight;
    public float rectWidth;
    private Category currentCategory;
    private Animation openingAnimation;
    private Animation expandedAnimation;
    private ModulesPanel modpanel;
    private HashMap<Category, ArrayList<ModuleRect>> moduleRects;
    private boolean firstOpen;
    public boolean typing;
    private float adjustment;
    private final List<ModuleRect> searchResults;
    private final List<String> searchTerms;
    private String searchText;
    
    public ModernClickGUI() {
        this.backgroundColor = new Color(30, 31, 35);
        this.categoryColor = new Color(47, 49, 54);
        this.lighterGray = new Color(68, 71, 78);
        this.circleClicks = new ArrayList<ClickCircle>();
        this.categories = (List<Component>)new ArrayList() {
            {
                for (final Category category : Category.values()) {
                    this.add(new CategoryButton(category));
                }
            }
        };
        this.rectHeight = 255.0f;
        this.rectWidth = 370.0f;
        this.currentCategory = Category.COMBAT;
        this.firstOpen = true;
        this.adjustment = 0.0f;
        this.searchResults = new ArrayList<ModuleRect>();
        this.searchTerms = new ArrayList<String>();
    }
    
    public void drawBigRect() {
        float x = ModernClickGUI.drag.getX();
        final float y = ModernClickGUI.drag.getY();
        if (!this.openingAnimation.isDone()) {
            x -= this.width + this.rectWidth / 2.0f;
            x += (this.width + this.rectWidth / 2.0f) * this.openingAnimation.getOutput().floatValue();
        }
        else if (this.openingAnimation.getDirection().equals(Direction.BACKWARDS)) {
            return;
        }
        RoundedUtil.drawRound(x, y, this.rectWidth, this.rectHeight, 10.0f, Color.BLACK);
    }
    
    @Override
    public void onDrag(final int mouseX, final int mouseY) {
        if (this.firstOpen) {
            ModernClickGUI.drag.setX(this.width / 2.0f - this.rectWidth / 2.0f);
            ModernClickGUI.drag.setY(this.height / 2.0f - this.rectHeight / 2.0f);
            this.firstOpen = false;
        }
        ModernClickGUI.drag.onDraw(mouseX, mouseY);
        Tenacity.INSTANCE.getSideGui().onDrag(mouseX, mouseY);
    }
    
    @Override
    public void initGui() {
        if (this.firstOpen) {
            ModernClickGUI.drag.setX(this.width / 2.0f - this.rectWidth / 2.0f);
            ModernClickGUI.drag.setY(this.height / 2.0f - this.rectHeight / 2.0f);
            this.firstOpen = false;
        }
        if (this.modpanel == null) {
            this.modpanel = new ModulesPanel();
        }
        Tenacity.INSTANCE.getSideGui().initGui();
        Tenacity.INSTANCE.getSearchBar().initGui();
        final ClickGUIMod clickMod = Tenacity.INSTANCE.getModuleCollection().getModule(ClickGUIMod.class);
        this.currentCategory = clickMod.getActiveCategory();
        this.categories.forEach(Component::initGui);
        this.openingAnimation = new DecelerateAnimation(300, 1.0);
        this.expandedAnimation = new DecelerateAnimation(250, 1.0);
        if (this.moduleRects != null) {
            this.moduleRects.forEach((cat, list) -> list.forEach(ModuleRect::initGui));
        }
        this.modpanel.initGui();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) {
        if (keyCode == 1 && !this.typing) {
            if (Tenacity.INSTANCE.getSearchBar().isFocused()) {
                Tenacity.INSTANCE.getSearchBar().getSearchField().setText("");
                Tenacity.INSTANCE.getSearchBar().getSearchField().setFocused(false);
                return;
            }
            if (Tenacity.INSTANCE.getSideGui().isFocused()) {
                Tenacity.INSTANCE.getSideGui().setFocused(false);
                return;
            }
            Tenacity.INSTANCE.getSearchBar().getOpenAnimation().setDirection(Direction.BACKWARDS);
            this.openingAnimation.setDirection(Direction.BACKWARDS);
            final ClickGUIMod clickMod = Tenacity.INSTANCE.getModuleCollection().getModule(ClickGUIMod.class);
            clickMod.setActiveCategory(this.currentCategory);
        }
        Tenacity.INSTANCE.getSideGui().keyTyped(typedChar, keyCode);
        Tenacity.INSTANCE.getSearchBar().keyTyped(typedChar, keyCode);
        this.modpanel.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (ModuleCollection.reloadModules || this.moduleRects == null) {
            if (this.moduleRects == null) {
                this.moduleRects = new HashMap<Category, ArrayList<ModuleRect>>();
            }
            else {
                this.moduleRects.clear();
            }
            for (final Category category : Category.values()) {
                final ArrayList<ModuleRect> modules = new ArrayList<ModuleRect>();
                for (final Module module : Tenacity.INSTANCE.getModuleCollection().getModulesInCategory(category)) {
                    modules.add(new ModuleRect(module));
                }
                this.moduleRects.put(category, modules);
            }
            this.moduleRects.forEach((cat, list) -> list.forEach(ModuleRect::initGui));
            this.modpanel.refreshSettingMap();
            ModuleCollection.reloadModules = false;
            return;
        }
        this.typing = (this.modpanel.isTyping() || (Tenacity.INSTANCE.getSideGui().isFocused() && Tenacity.INSTANCE.getSideGui().isTyping()) || Tenacity.INSTANCE.getSearchBar().isTyping());
        if (ClickGUIMod.walk.isEnabled() && !this.typing) {
            InventoryMove.updateStates();
        }
        final boolean focusedConfigGui = Tenacity.INSTANCE.getSideGui().isFocused();
        final int fakeMouseX = focusedConfigGui ? 0 : mouseX;
        final int fakeMouseY = focusedConfigGui ? 0 : mouseY;
        this.adjustment = 0.0f;
        ModernClickGUI.drag.onDraw(fakeMouseX, fakeMouseY);
        float x = ModernClickGUI.drag.getX();
        final float y = ModernClickGUI.drag.getY();
        if (!this.openingAnimation.isDone()) {
            x -= this.width + this.rectWidth / 2.0f;
            x += (this.width + this.rectWidth / 2.0f) * this.openingAnimation.getOutput().floatValue();
        }
        else if (this.openingAnimation.getDirection().equals(Direction.BACKWARDS)) {
            ModernClickGUI.mc.displayGuiScreen(null);
            return;
        }
        RoundedUtil.drawRound(x, y, this.rectWidth, this.rectHeight, 10.0f, this.backgroundColor);
        final float catWidth = 100.0f - 55.0f * this.expandedAnimation.getOutput().floatValue();
        final boolean hoveringCat = HoveringUtil.isHovering(x, y, catWidth, this.rectHeight, fakeMouseX, fakeMouseY);
        final boolean searching = Tenacity.INSTANCE.getSearchBar().isFocused();
        if (this.expandedAnimation.isDone()) {
            this.expandedAnimation.setDirection((hoveringCat && !searching) ? Direction.BACKWARDS : Direction.FORWARDS);
        }
        RoundedUtil.drawRound(x, y, 100.0f - 55.0f * this.expandedAnimation.getOutput().floatValue(), this.rectHeight, 10.0f, this.categoryColor);
        this.adjustWidth(55.0f - 55.0f * this.expandedAnimation.getOutput().floatValue());
        StencilUtil.initStencilToWrite();
        Gui.drawRect2(x, y, 100.0f - 55.0f * this.expandedAnimation.getOutput().floatValue(), this.rectHeight, -1);
        StencilUtil.readStencilBuffer(1);
        GL11.glEnable(3042);
        ModernClickGUI.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/modernlogo.png"));
        Gui.drawModalRectWithCustomSizedTexture(x + 9.0f + 3.0f * this.expandedAnimation.getOutput().floatValue(), y + 6.0f, 0.0f, 0.0f, 20.5f, 20.5f, 20.5f, 20.5f);
        GL11.glDisable(3042);
        Gui.drawRect2(x + 10.0f, y + 35.0f, 80.0f - 55.0f * this.expandedAnimation.getOutput().floatValue(), 1.0, this.lighterGray.getRGB());
        final float xAdjust = 10.0f * this.expandedAnimation.getOutput().floatValue();
        FontUtil.tenacityFont20.drawString("Tenacity", x + 35.0f + xAdjust, y + 13.0f, -1);
        FontUtil.tenacityFont14.drawString("6.0", x + 41.0f + FontUtil.tenacityFont18.getStringWidth("Tenacity") + xAdjust, y + 15.5f, new Color(98, 98, 98));
        int spacing = 0;
        for (final Component category2 : this.categories) {
            category2.x = x + 8.0f + 4.0f * this.expandedAnimation.getOutput().floatValue();
            category2.y = y + 50.0f + spacing;
            final CategoryButton currentCatego = (CategoryButton)category2;
            currentCatego.expandAnimation = this.expandedAnimation;
            currentCatego.currentCategory = (searching ? null : this.currentCategory);
            category2.drawScreen(fakeMouseX, fakeMouseY);
            spacing += 30;
        }
        StencilUtil.uninitStencilBuffer();
        final float recWidth = 100.0f - 55.0f * this.expandedAnimation.getOutput().floatValue();
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x, y, this.rectWidth, this.rectHeight, 10.0f, this.backgroundColor);
        StencilUtil.readStencilBuffer(1);
        this.modpanel.x = x + recWidth + 10.0f;
        this.modpanel.y = y + 20.0f;
        this.modpanel.bigRecty = y;
        this.modpanel.modules = this.getModuleRects(this.currentCategory);
        this.modpanel.currentCategory = (searching ? null : this.currentCategory);
        this.modpanel.expandAnim = this.expandedAnimation;
        this.modpanel.drawScreen(fakeMouseX, fakeMouseY);
        StencilUtil.uninitStencilBuffer();
        final SideGUI sideGUI = Tenacity.INSTANCE.getSideGui();
        sideGUI.getOpenAnimation().setDirection(this.openingAnimation.getDirection());
        sideGUI.drawScreen(mouseX, mouseY);
        final SearchBar searchBar = Tenacity.INSTANCE.getSearchBar();
        searchBar.setAlpha(this.openingAnimation.getOutput().floatValue() * (1.0f - sideGUI.getClickAnimation().getOutput().floatValue()));
        searchBar.drawScreen(fakeMouseX, fakeMouseY);
        for (final ClickCircle clickCircle : this.circleClicks) {
            clickCircle.drawScreen(fakeMouseX, fakeMouseY);
        }
        this.rectWidth = 370.0f + this.adjustment;
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        final float rectWidth = 400.0f;
        final double x = ModernClickGUI.drag.getX();
        final double y = ModernClickGUI.drag.getY();
        final boolean canDrag = HoveringUtil.isHovering((float)x, (float)y, rectWidth, 20.0f, mouseX, mouseY);
        if (!Tenacity.INSTANCE.getSideGui().isFocused()) {
            ModernClickGUI.drag.onClick(mouseX, mouseY, mouseButton, canDrag);
            this.circleClicks.removeIf(clickCircle1 -> clickCircle1.fadeAnimation.isDone() && clickCircle1.fadeAnimation.getDirection().equals(Direction.BACKWARDS));
            final ClickCircle clickCircle2 = new ClickCircle();
            clickCircle2.x = (float)mouseX;
            clickCircle2.y = (float)mouseY;
            this.circleClicks.add(clickCircle2);
            for (final Component category : this.categories) {
                category.mouseClicked(mouseX, mouseY, mouseButton);
                if (category.hovering) {
                    this.currentCategory = ((CategoryButton)category).category;
                    return;
                }
            }
            this.modpanel.mouseClicked(mouseX, mouseY, mouseButton);
        }
        Tenacity.INSTANCE.getSideGui().mouseClicked(mouseX, mouseY, mouseButton);
        Tenacity.INSTANCE.getSearchBar().mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (!Tenacity.INSTANCE.getSideGui().isFocused()) {
            ModernClickGUI.drag.onRelease(state);
            this.modpanel.mouseReleased(mouseX, mouseY, state);
        }
        Tenacity.INSTANCE.getSideGui().mouseReleased(mouseX, mouseY, state);
        Tenacity.INSTANCE.getSearchBar().mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public void adjustWidth(final float adjustment) {
        this.adjustment += adjustment;
    }
    
    public List<ModuleRect> getModuleRects(final Category category) {
        if (!Tenacity.INSTANCE.getSearchBar().isFocused()) {
            return this.moduleRects.get(category);
        }
        final String search = Tenacity.INSTANCE.getSearchBar().getSearchField().getText();
        if (search.equals(this.searchText)) {
            return this.searchResults;
        }
        this.searchText = search;
        final List<ModuleRect> moduleRects1 = this.moduleRects.values().stream().flatMap((Function<? super ArrayList<ModuleRect>, ? extends Stream<?>>)Collection::stream).collect((Collector<? super Object, ?, List<ModuleRect>>)Collectors.toList());
        this.searchResults.clear();
        final Module module;
        final Iterator<Setting> iterator;
        Setting setting;
        final String s;
        moduleRects1.forEach(moduleRect -> {
            this.searchTerms.clear();
            module = moduleRect.module;
            this.searchTerms.add(module.getName());
            this.searchTerms.add(module.getCategory().name);
            if (!module.getAuthor().isEmpty()) {
                this.searchTerms.add(module.getAuthor());
            }
            module.getSettingsList().iterator();
            while (iterator.hasNext()) {
                setting = iterator.next();
                this.searchTerms.add(setting.name);
            }
            moduleRect.setSearchScore(FuzzySearch.extractOne(s, (Collection)this.searchTerms).getScore());
            return;
        });
        this.searchResults.addAll(moduleRects1.stream().filter(moduleRect -> moduleRect.getSearchScore() > 60).sorted(Comparator.comparingInt(ModuleRect::getSearchScore).reversed()).collect((Collector<? super Object, ?, Collection<? extends ModuleRect>>)Collectors.toList()));
        return this.searchResults;
    }
    
    static {
        drag = new Drag(40.0f, 40.0f);
        ModernClickGUI.searching = false;
    }
}
