// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.dropdown;

import dev.tenacity.ui.searchbar.SearchBar;
import dev.tenacity.ui.sidegui.SideGUI;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.utils.render.Theme;
import dev.tenacity.module.impl.movement.InventoryMove;
import dev.tenacity.module.impl.render.ClickGUIMod;
import dev.tenacity.module.Category;
import java.util.ArrayList;
import dev.tenacity.utils.animations.Direction;
import java.util.Iterator;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.animations.impl.EaseBackIn;
import java.util.List;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.tuples.Pair;
import net.minecraft.client.gui.GuiScreen;

public class DropdownClickGUI extends GuiScreen
{
    private final Pair<Animation, Animation> openingAnimations;
    private List<CategoryPanel> categoryPanels;
    public boolean binding;
    public static boolean gradient;
    
    public DropdownClickGUI() {
        this.openingAnimations = (Pair<Animation, Animation>)Pair.of(new EaseBackIn(400, 1.0, 2.0f), new EaseBackIn(400, 0.4000000059604645, 2.0f));
    }
    
    @Override
    public void onDrag(final int mouseX, final int mouseY) {
        for (final CategoryPanel catPanels : this.categoryPanels) {
            catPanels.onDrag(mouseX, mouseY);
        }
        Tenacity.INSTANCE.getSideGui().onDrag(mouseX, mouseY);
    }
    
    @Override
    public void initGui() {
        this.openingAnimations.use((fade, opening) -> {
            fade.setDirection(Direction.FORWARDS);
            opening.setDirection(Direction.FORWARDS);
            return;
        });
        if (this.categoryPanels == null) {
            this.categoryPanels = new ArrayList<CategoryPanel>();
            for (final Category category : Category.values()) {
                this.categoryPanels.add(new CategoryPanel(category, this.openingAnimations));
            }
        }
        Tenacity.INSTANCE.getSideGui().initGui();
        Tenacity.INSTANCE.getSearchBar().initGui();
        for (final CategoryPanel catPanels : this.categoryPanels) {
            catPanels.initGui();
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) {
        if (keyCode == 1 && !this.binding) {
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
            this.openingAnimations.use((fade, opening) -> {
                fade.setDirection(Direction.BACKWARDS);
                opening.setDirection(Direction.BACKWARDS);
                return;
            });
        }
        Tenacity.INSTANCE.getSideGui().keyTyped(typedChar, keyCode);
        Tenacity.INSTANCE.getSearchBar().keyTyped(typedChar, keyCode);
        this.categoryPanels.forEach(categoryPanel -> categoryPanel.keyTyped(typedChar, keyCode));
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.binding = (this.categoryPanels.stream().anyMatch(CategoryPanel::isTyping) || (Tenacity.INSTANCE.getSideGui().isFocused() && Tenacity.INSTANCE.getSideGui().typing) || Tenacity.INSTANCE.getSearchBar().isTyping());
        if (ClickGUIMod.walk.isEnabled() && !this.binding) {
            InventoryMove.updateStates();
        }
        if (this.openingAnimations.getSecond().finished(Direction.BACKWARDS)) {
            DropdownClickGUI.mc.displayGuiScreen(null);
            return;
        }
        DropdownClickGUI.gradient = (Theme.getCurrentTheme().isGradient() || ClickGUIMod.gradient.isEnabled());
        final boolean focusedConfigGui = Tenacity.INSTANCE.getSideGui().isFocused() || Tenacity.INSTANCE.getSearchBar().isTyping();
        final int fakeMouseX = focusedConfigGui ? 0 : mouseX;
        final int fakeMouseY = focusedConfigGui ? 0 : mouseY;
        final ScaledResolution sr = new ScaledResolution(DropdownClickGUI.mc);
        RenderUtil.scaleStart(sr.getScaledWidth() / 2.0f, sr.getScaledHeight() / 2.0f, this.openingAnimations.getSecond().getOutput().floatValue() + 0.6f);
        for (final CategoryPanel catPanels : this.categoryPanels) {
            catPanels.drawScreen(fakeMouseX, fakeMouseY);
        }
        RenderUtil.scaleEnd();
        this.categoryPanels.forEach(categoryPanel -> categoryPanel.drawToolTips(fakeMouseX, fakeMouseY));
        final SideGUI sideGUI = Tenacity.INSTANCE.getSideGui();
        sideGUI.getOpenAnimation().setDirection(this.openingAnimations.getFirst().getDirection());
        sideGUI.drawScreen(mouseX, mouseY);
        final SearchBar searchBar = Tenacity.INSTANCE.getSearchBar();
        searchBar.setAlpha(this.openingAnimations.getFirst().getOutput().floatValue() * (1.0f - sideGUI.getClickAnimation().getOutput().floatValue()));
        searchBar.drawScreen(fakeMouseX, fakeMouseY);
    }
    
    public void renderEffects() {
        final ScaledResolution sr = new ScaledResolution(DropdownClickGUI.mc);
        RenderUtil.scaleStart(sr.getScaledWidth() / 2.0f, sr.getScaledHeight() / 2.0f, this.openingAnimations.getSecond().getOutput().floatValue() + 0.6f);
        for (final CategoryPanel catPanels : this.categoryPanels) {
            catPanels.renderEffects();
        }
        RenderUtil.scaleEnd();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        final boolean focused = Tenacity.INSTANCE.getSideGui().isFocused();
        Tenacity.INSTANCE.getSideGui().mouseClicked(mouseX, mouseY, mouseButton);
        Tenacity.INSTANCE.getSearchBar().mouseClicked(mouseX, mouseY, mouseButton);
        if (!focused) {
            this.categoryPanels.forEach(cat -> cat.mouseClicked(mouseX, mouseY, mouseButton));
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        final boolean focused = Tenacity.INSTANCE.getSideGui().isFocused();
        Tenacity.INSTANCE.getSideGui().mouseReleased(mouseX, mouseY, state);
        Tenacity.INSTANCE.getSearchBar().mouseReleased(mouseX, mouseY, state);
        if (!focused) {
            this.categoryPanels.forEach(cat -> cat.mouseReleased(mouseX, mouseY, state));
        }
    }
    
    @Override
    public void onGuiClosed() {
        if (ClickGUIMod.rescale.isEnabled()) {
            DropdownClickGUI.mc.gameSettings.guiScale = ClickGUIMod.prevGuiScale;
        }
    }
}
