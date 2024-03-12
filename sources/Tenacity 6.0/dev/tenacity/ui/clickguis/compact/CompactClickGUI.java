// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.compact;

import java.util.Comparator;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import dev.tenacity.module.settings.Setting;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Function;
import java.util.Collection;
import dev.tenacity.utils.misc.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import dev.tenacity.ui.searchbar.SearchBar;
import dev.tenacity.ui.sidegui.SideGUI;
import dev.tenacity.utils.objects.DiscordAccount;
import java.util.Iterator;
import dev.tenacity.utils.render.StencilUtil;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.font.FontUtil;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.module.impl.movement.InventoryMove;
import dev.tenacity.module.impl.render.ClickGUIMod;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCollection;
import net.minecraft.client.gui.Gui;
import java.util.function.Consumer;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import java.util.List;
import java.awt.Color;
import dev.tenacity.ui.clickguis.compact.impl.ModuleRect;
import java.util.ArrayList;
import dev.tenacity.module.Category;
import java.util.HashMap;
import dev.tenacity.utils.objects.Drag;
import dev.tenacity.utils.animations.Animation;
import net.minecraft.client.gui.GuiScreen;

public class CompactClickGUI extends GuiScreen
{
    private final Animation openingAnimation;
    private final Drag drag;
    private final ModulePanel modulePanel;
    private float rectWidth;
    private float rectHeight;
    public boolean typing;
    private HashMap<Category, ArrayList<ModuleRect>> moduleRects;
    private Color firstColor;
    private Color secondColor;
    private final List<ModuleRect> searchResults;
    private final List<String> searchTerms;
    private String searchText;
    
    public CompactClickGUI() {
        this.openingAnimation = new DecelerateAnimation(250, 1.0);
        this.drag = new Drag(40.0f, 40.0f);
        this.modulePanel = new ModulePanel();
        this.rectWidth = 400.0f;
        this.rectHeight = 300.0f;
        this.firstColor = Color.BLACK;
        this.secondColor = Color.BLACK;
        this.searchResults = new ArrayList<ModuleRect>();
        this.searchTerms = new ArrayList<String>();
    }
    
    @Override
    public void onDrag(final int mouseX, final int mouseY) {
        final boolean focusedConfigGui = Tenacity.INSTANCE.getSideGui().isFocused();
        final int fakeMouseX = focusedConfigGui ? 0 : mouseX;
        final int fakeMouseY = focusedConfigGui ? 0 : mouseY;
        this.drag.onDraw(fakeMouseX, fakeMouseY);
        Tenacity.INSTANCE.getSideGui().onDrag(mouseX, mouseY);
    }
    
    @Override
    public void initGui() {
        this.openingAnimation.setDirection(Direction.FORWARDS);
        this.rectWidth = 500.0f;
        this.rectHeight = 350.0f;
        if (this.moduleRects != null) {
            this.moduleRects.forEach((cat, list) -> list.forEach(ModuleRect::initGui));
        }
        this.modulePanel.initGui();
        Tenacity.INSTANCE.getSideGui().initGui();
    }
    
    public void bloom() {
        float x = this.drag.getX();
        final float y = this.drag.getY();
        if (!this.openingAnimation.isDone()) {
            x -= this.width + this.rectWidth / 2.0f;
            x += (this.width + this.rectWidth / 2.0f) * this.openingAnimation.getOutput().floatValue();
        }
        Gui.drawRect2(x, y, this.rectWidth, this.rectHeight, new Color(20, 20, 20).getRGB());
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) {
        if (keyCode == 1) {
            if (Tenacity.INSTANCE.getSearchBar().isFocused()) {
                Tenacity.INSTANCE.getSearchBar().getSearchField().setText("");
                Tenacity.INSTANCE.getSearchBar().getSearchField().setFocused(false);
                return;
            }
            if (Tenacity.INSTANCE.getSideGui().isFocused()) {
                Tenacity.INSTANCE.getSideGui().setFocused(false);
                return;
            }
            this.openingAnimation.setDirection(Direction.BACKWARDS);
        }
        this.modulePanel.keyTyped(typedChar, keyCode);
        Tenacity.INSTANCE.getSideGui().keyTyped(typedChar, keyCode);
        Tenacity.INSTANCE.getSearchBar().keyTyped(typedChar, keyCode);
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
            ModuleCollection.reloadModules = false;
            return;
        }
        this.typing = (this.modulePanel.typing || (Tenacity.INSTANCE.getSideGui().isFocused() && Tenacity.INSTANCE.getSideGui().isTyping()) || Tenacity.INSTANCE.getSearchBar().isTyping());
        if (ClickGUIMod.walk.isEnabled() && !this.typing) {
            InventoryMove.updateStates();
        }
        final boolean focusedConfigGui = Tenacity.INSTANCE.getSideGui().isFocused();
        final int fakeMouseX = focusedConfigGui ? 0 : mouseX;
        final int fakeMouseY = focusedConfigGui ? 0 : mouseY;
        float x = this.drag.getX();
        final float y = this.drag.getY();
        if (!this.openingAnimation.isDone()) {
            x -= this.width + this.rectWidth / 2.0f;
            x += (this.width + this.rectWidth / 2.0f) * this.openingAnimation.getOutput().floatValue();
        }
        else if (this.openingAnimation.getDirection().equals(Direction.BACKWARDS)) {
            CompactClickGUI.mc.displayGuiScreen(null);
            return;
        }
        this.rectWidth = 475.0f;
        this.rectHeight = 300.0f;
        Gui.drawRect2(x, y, this.rectWidth, this.rectHeight, new Color(27, 27, 27).getRGB());
        Gui.drawRect2(x, y, 90.0, this.rectHeight, new Color(39, 39, 39).getRGB());
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3042);
        CompactClickGUI.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/modernlogo.png"));
        Gui.drawModalRectWithCustomSizedTexture(x + 5.0f, y + 5.0f, 0.0f, 0.0f, 20.5f, 20.5f, 20.5f, 20.5f);
        FontUtil.tenacityBoldFont22.drawString("Tenacity", x + 33.0f, y + 7.0f, -1);
        FontUtil.tenacityFont16.drawCenteredString("6.0", x + 31.0f + FontUtil.tenacityBoldFont22.getStringWidth("Tenacity") / 2.0f, y + 19.0f, -1);
        final boolean searching = Tenacity.INSTANCE.getSearchBar().isFocused();
        final float bannerHeight = 37.5f;
        Gui.drawRect2(x + 5.0f, y + 31.0f, 80.0, 0.5, new Color(110, 110, 110).getRGB());
        Gui.drawRect2(x + 5.0f, y + this.rectHeight - (bannerHeight + 3.0f), 80.0, 0.5, new Color(110, 110, 110).getRGB());
        if (Tenacity.INSTANCE.getDiscordAccount() != null) {
            final DiscordAccount discordAccount = Tenacity.INSTANCE.getDiscordAccount();
            final float avatarSize = 20.0f;
            final float bannerWidth = 90.0f;
            final boolean hoveringDiscord = HoveringUtil.isHovering(x, y + this.rectHeight - bannerHeight, bannerWidth, bannerHeight, fakeMouseX, fakeMouseY);
            if (discordAccount.discordBanner != null) {
                final float alpha = 50.0f + (hoveringDiscord ? 120 : 0);
                CompactClickGUI.mc.getTextureManager().bindTexture(discordAccount.discordBanner);
                GlStateManager.color(1.0f, 1.0f, 1.0f, alpha / 255.0f);
                GL11.glEnable(3042);
                Gui.drawModalRectWithCustomSizedTexture(x, y + this.rectHeight - bannerHeight, 0.0f, 0.0f, bannerWidth, bannerHeight, bannerWidth, bannerHeight);
            }
            else {
                final String stringBuilder = "ff" + discordAccount.bannerColor;
                final int integer = (int)Long.parseLong(stringBuilder, 16);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                final float alpha2 = hoveringDiscord ? 170.0f : 0.0f;
                Gui.drawRect2(x, y + this.rectHeight - bannerHeight, bannerWidth, bannerHeight, ColorUtil.applyOpacity(integer, alpha2 / 255.0f));
            }
            CompactClickGUI.mc.getTextureManager().bindTexture(discordAccount.discordAvatar);
            RoundedUtil.drawRoundTextured(x + 5.0f, y + this.rectHeight - (avatarSize + 15.0f), avatarSize, avatarSize, 10.0f, 1.0f);
        }
        final float minus = bannerHeight + 3.0f + 33.0f;
        final ClickGUIMod clickGUIMod = Tenacity.INSTANCE.getModuleCollection().getModule(ClickGUIMod.class);
        final float catHeight = (this.rectHeight - minus) / Category.values().length;
        float seperation = 0.0f;
        for (final Category category2 : Category.values()) {
            final float catY = y + 33.0f + seperation;
            final boolean hovering = HoveringUtil.isHovering(x, catY + 8.0f, 90.0f, catHeight - 16.0f, fakeMouseX, fakeMouseY);
            final Color categoryColor = hovering ? ColorUtil.tripleColor(110).brighter() : ColorUtil.tripleColor(110);
            final Color selectColor = (clickGUIMod.getActiveCategory() == category2) ? Color.WHITE : categoryColor;
            if (!searching && clickGUIMod.getActiveCategory() == category2) {
                Gui.drawRect2(x, catY, 90.0, catHeight, new Color(27, 27, 27).getRGB());
            }
            RenderUtil.resetColor();
            CompactClickGUI.tenacityBoldFont22.drawString(category2.name, x + 8.0f, catY + CompactClickGUI.tenacityFont22.getMiddleOfBox(catHeight), selectColor.getRGB());
            RenderUtil.resetColor();
            seperation += catHeight;
        }
        this.modulePanel.currentCat = (searching ? null : clickGUIMod.getActiveCategory());
        this.modulePanel.moduleRects = this.getModuleRects(clickGUIMod.getActiveCategory());
        this.modulePanel.x = x;
        this.modulePanel.y = y;
        this.modulePanel.rectHeight = this.rectHeight;
        this.modulePanel.rectWidth = this.rectWidth;
        StencilUtil.initStencilToWrite();
        Gui.drawRect2(x, y, this.rectWidth, this.rectHeight, -1);
        StencilUtil.readStencilBuffer(1);
        this.modulePanel.drawScreen(fakeMouseX, fakeMouseY);
        StencilUtil.uninitStencilBuffer();
        this.modulePanel.drawTooltips(fakeMouseX, fakeMouseY);
        final SideGUI sideGUI = Tenacity.INSTANCE.getSideGui();
        sideGUI.getOpenAnimation().setDirection(this.openingAnimation.getDirection());
        sideGUI.drawScreen(mouseX, mouseY);
        final SearchBar searchBar = Tenacity.INSTANCE.getSearchBar();
        searchBar.setAlpha(this.openingAnimation.getOutput().floatValue() * (1.0f - sideGUI.getClickAnimation().getOutput().floatValue()));
        searchBar.drawScreen(fakeMouseX, fakeMouseY);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (!Tenacity.INSTANCE.getSideGui().isFocused()) {
            this.drag.onClick(mouseX, mouseY, mouseButton, HoveringUtil.isHovering(this.drag.getX(), this.drag.getY(), this.rectWidth, 10.0f, mouseX, mouseY));
            final float bannerWidth = 90.0f;
            final float bannerHeight = 37.5f;
            final ClickGUIMod clickGUIMod = Tenacity.INSTANCE.getModuleCollection().getModule(ClickGUIMod.class);
            if (HoveringUtil.isHovering(this.drag.getX(), this.drag.getY() + this.rectHeight - bannerHeight, bannerWidth, bannerHeight, mouseX, mouseY)) {
                if (RandomUtils.nextBoolean()) {
                    IOUtils.openLink("https://www.youtube.com/channel/UC2tPaPIMGeDETMTr1FQuMSA?sub_confirmation=1");
                }
                else {
                    IOUtils.openLink("https://www.youtube.com/channel/UCC5eswf_s4GMyH4W-K0RUuA?sub_confirmation=1");
                }
            }
            int separation = 0;
            final float minus = bannerHeight + 3.0f + 33.0f;
            final float catHeight = (this.rectHeight - minus) / Category.values().length;
            for (final Category category : Category.values()) {
                final float catY = this.drag.getY() + 33.0f + separation;
                final boolean hovering = HoveringUtil.isHovering(this.drag.getX(), catY + 8.0f, 90.0f, catHeight - 16.0f, mouseX, mouseY);
                if (hovering) {
                    clickGUIMod.setActiveCategory(category);
                }
                separation += (int)catHeight;
            }
            this.modulePanel.mouseClicked(mouseX, mouseY, mouseButton);
            Tenacity.INSTANCE.getSearchBar().mouseClicked(mouseX, mouseY, mouseButton);
        }
        Tenacity.INSTANCE.getSideGui().mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (!Tenacity.INSTANCE.getSideGui().isFocused()) {
            this.drag.onRelease(state);
            this.modulePanel.mouseReleased(mouseX, mouseY, state);
        }
        Tenacity.INSTANCE.getSideGui().mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
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
}
