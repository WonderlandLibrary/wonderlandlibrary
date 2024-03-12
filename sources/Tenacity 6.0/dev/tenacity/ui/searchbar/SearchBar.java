// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.searchbar;

import dev.tenacity.module.Module;
import dev.tenacity.utils.render.RoundedUtil;
import java.awt.Color;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.Tenacity;
import net.minecraft.client.gui.GuiScreen;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.objects.TextField;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.ui.Screen;

public class SearchBar implements Screen
{
    private boolean focused;
    private boolean typing;
    private boolean hoveringBottomOfScreen;
    private final Animation focusAnimation;
    private final Animation hoverAnimation;
    private final Animation openAnimation;
    private final TextField searchField;
    private float alpha;
    
    public SearchBar() {
        this.focusAnimation = new DecelerateAnimation(175, 1.0).setDirection(Direction.BACKWARDS);
        this.hoverAnimation = new DecelerateAnimation(175, 1.0).setDirection(Direction.BACKWARDS);
        this.openAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.searchField = new TextField(SearchBar.tenacityFont18);
    }
    
    @Override
    public void initGui() {
        this.openAnimation.setDirection(Direction.FORWARDS);
        this.searchField.setText("");
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (keyCode == 1) {
            this.searchField.setFocused(false);
            return;
        }
        if (GuiScreen.isCtrlKeyDown() && keyCode == 33) {
            this.searchField.setFocused(true);
            Tenacity.INSTANCE.getModuleCollection().getModules().forEach(module -> module.setExpanded(false));
            return;
        }
        this.searchField.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        this.focused = (this.searchField.isFocused() || !this.searchField.getText().isEmpty());
        this.typing = this.searchField.isFocused();
        final ScaledResolution sr = new ScaledResolution(SearchBar.mc);
        final float width = (float)sr.getScaledWidth();
        final float height = (float)sr.getScaledHeight();
        this.hoveringBottomOfScreen = HoveringUtil.isHovering(width / 2.0f - 120.0f, (float)(sr.getScaledHeight() - 100), 240.0f, 100.0f, mouseX, mouseY);
        this.hoverAnimation.setDirection((this.hoveringBottomOfScreen && !this.focused) ? Direction.FORWARDS : Direction.BACKWARDS);
        this.focusAnimation.setDirection(this.focused ? Direction.FORWARDS : Direction.BACKWARDS);
        final float focusAnim = this.focusAnimation.getOutput().floatValue();
        final float hover = this.hoverAnimation.getOutput().floatValue();
        final float openAnim = Math.min(1.0f, this.alpha);
        final float searchAlpha = Math.min(1.0f, hover + focusAnim);
        SearchBar.tenacityFont26.drawCenteredString("Do §lCTRL§r+§lF§r to open the search bar", sr.getScaledWidth() / 2.0f, (float)(sr.getScaledHeight() - 75), ColorUtil.applyOpacity(-1, 0.3f * (1.0f - searchAlpha) * openAnim));
        this.searchField.setWidth(200.0f);
        this.searchField.setHeight(25.0f);
        this.searchField.setFont(SearchBar.tenacityFont24);
        this.searchField.setXPosition(sr.getScaledWidth() / 2.0f - 100.0f);
        this.searchField.setYPosition(sr.getScaledHeight() - (70.0f + 25.0f * hover + 60.0f * focusAnim));
        this.searchField.setRadius(5.0f);
        this.searchField.setAlpha(Math.max(hover * 0.85f, focusAnim));
        this.searchField.setTextAlpha(this.searchField.getAlpha());
        this.searchField.setFill(ColorUtil.tripleColor(17));
        this.searchField.setOutline(null);
        this.searchField.setBackgroundText("Search");
        this.searchField.drawTextBox();
    }
    
    public void drawEffects() {
        final ScaledResolution sr = new ScaledResolution(SearchBar.mc);
        final float hover = this.hoverAnimation.getOutput().floatValue();
        final float focusAnim = this.focusAnimation.getOutput().floatValue();
        final float openAnim = Math.min(1.0f, this.alpha);
        final float searchAlpha = Math.min(1.0f, hover + focusAnim);
        SearchBar.tenacityFont26.drawCenteredString("Do §lCTRL§r+§lF§r to open the search bar", sr.getScaledWidth() / 2.0f, (float)(sr.getScaledHeight() - 75), ColorUtil.applyOpacity(Color.BLACK, 1.0f * (1.0f - searchAlpha) * openAnim));
        RoundedUtil.drawRound(this.searchField.getXPosition(), this.searchField.getYPosition(), 200.0f, this.searchField.getHeight(), 5.0f, ColorUtil.applyOpacity(Color.BLACK, Math.max(hover, focusAnim)));
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final boolean focused = this.searchField.isFocused();
        this.searchField.mouseClicked(mouseX, mouseY, button);
        if (!focused && this.searchField.isFocused()) {
            Tenacity.INSTANCE.getModuleCollection().getModules().forEach(module -> module.setExpanded(false));
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public boolean isFocused() {
        return this.focused;
    }
    
    public boolean isTyping() {
        return this.typing;
    }
    
    public boolean isHoveringBottomOfScreen() {
        return this.hoveringBottomOfScreen;
    }
    
    public Animation getFocusAnimation() {
        return this.focusAnimation;
    }
    
    public Animation getHoverAnimation() {
        return this.hoverAnimation;
    }
    
    public Animation getOpenAnimation() {
        return this.openAnimation;
    }
    
    public TextField getSearchField() {
        return this.searchField;
    }
    
    public float getAlpha() {
        return this.alpha;
    }
    
    public void setFocused(final boolean focused) {
        this.focused = focused;
    }
    
    public void setTyping(final boolean typing) {
        this.typing = typing;
    }
    
    public void setHoveringBottomOfScreen(final boolean hoveringBottomOfScreen) {
        this.hoveringBottomOfScreen = hoveringBottomOfScreen;
    }
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
    }
}
