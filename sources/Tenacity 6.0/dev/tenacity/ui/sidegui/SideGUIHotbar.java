// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui;

import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.misc.Multithreading;
import dev.tenacity.Tenacity;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.ui.sidegui.utils.IconButton;
import dev.tenacity.ui.sidegui.utils.CarouselButtons;
import dev.tenacity.ui.sidegui.utils.DropdownObject;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.objects.TextField;
import dev.tenacity.ui.Screen;

public class SideGUIHotbar implements Screen
{
    public float x;
    public float y;
    public float width;
    public float height;
    public float alpha;
    public final TextField searchField;
    private final Animation searchAnimation;
    public final DropdownObject searchType;
    private final CarouselButtons carouselButtons;
    private final IconButton refreshButton;
    private String currentPanel;
    int ticks;
    private final TimerUtil refreshTimer;
    private final Animation refreshText;
    
    public SideGUIHotbar() {
        this.searchField = new TextField(SideGUIHotbar.tenacityFont20);
        this.searchAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.searchType = new DropdownObject("Type", new String[] { "Configs", "Scripts" });
        this.carouselButtons = new CarouselButtons(new String[] { "Scripts", "Configs", "Info" });
        this.refreshButton = new IconButton("D", "Refresh all of the cloud and local data");
        this.ticks = 0;
        this.refreshTimer = new TimerUtil();
        this.refreshText = new DecelerateAnimation(250, 1.0);
    }
    
    @Override
    public void initGui() {
        this.currentPanel = this.carouselButtons.getCurrentButton();
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (keyCode != 1) {
            this.searchField.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final boolean searching = this.searchField.isFocused() || !this.searchField.getText().equals("");
        RoundedUtil.drawRound(this.x + 0.625f, this.y + 0.625f, this.width - 1.25f, this.height - 1.25f, 5.0f, ColorUtil.tripleColor(25, this.alpha));
        Gui.drawRect2(this.x, this.y + this.height - 4.0f, this.width, 4.0, ColorUtil.tripleColor(25, this.alpha * this.alpha * this.alpha).getRGB());
        final Color textColor = ColorUtil.applyOpacity(Color.WHITE, this.alpha);
        SideGUIHotbar.tenacityBoldFont32.drawString("Tenacity", this.x + 9.5f, this.y + SideGUIHotbar.tenacityBoldFont32.getMiddleOfBox(this.height), textColor);
        SideGUIHotbar.tenacityFont18.drawString("6.0", this.x + 9.5f + SideGUIHotbar.tenacityBoldFont32.getStringWidth("Tenacity") - 2.0f, this.y + SideGUIHotbar.tenacityBoldFont32.getMiddleOfBox(this.height) - 2.5f, ColorUtil.applyOpacity(textColor, 0.5f));
        this.searchAnimation.setDirection((this.searchField.isFocused() || !this.searchField.getText().equals("")) ? Direction.FORWARDS : Direction.BACKWARDS);
        final float searchAnim = this.searchAnimation.getOutput().floatValue();
        final float carouselAlpha = this.alpha * (1.0f - searchAnim);
        this.carouselButtons.setAlpha(carouselAlpha);
        this.carouselButtons.setRectWidth(75.0f);
        this.carouselButtons.setRectHeight(20.5f);
        this.carouselButtons.setX(this.x + this.width / 2.0f - this.carouselButtons.getTotalWidth() / 2.0f);
        this.carouselButtons.setY(this.y + this.height / 2.0f - this.carouselButtons.getRectHeight() / 2.0f);
        this.carouselButtons.drawScreen(mouseX, mouseY);
        if (!searching) {
            this.refreshText.setDirection((this.refreshTimer.getTime() > 12000L) ? Direction.BACKWARDS : Direction.FORWARDS);
            SideGUIHotbar.tenacityFont14.drawString(String.valueOf(Math.round(this.refreshTimer.getTime() / 1000.0f)), this.refreshButton.getX() - 15.0f, this.y + SideGUIHotbar.tenacityFont14.getMiddleOfBox(this.height), ColorUtil.applyOpacity(-1, this.refreshText.getOutput().floatValue()));
            this.refreshButton.setAlpha(carouselAlpha);
            this.refreshButton.setX(this.carouselButtons.getX() - (this.refreshButton.getWidth() + 10.0f));
            this.refreshButton.setY(this.carouselButtons.getY() + 1.0f + this.carouselButtons.getRectHeight() / 2.0f - this.refreshButton.getHeight() / 2.0f);
            this.refreshButton.setAccentColor(Color.WHITE);
            this.refreshButton.setIconFont(SideGUIHotbar.iconFont20);
            this.refreshButton.setClickAction(() -> Multithreading.runAsync(() -> Tenacity.INSTANCE.getSideGui().getConfigPanel()));
            this.refreshButton.drawScreen(mouseX, mouseY);
            RenderUtil.rotateEnd();
        }
        this.searchField.setRadius(5.0f);
        this.searchField.setFill(ColorUtil.tripleColor(17, this.alpha));
        this.searchField.setOutline(ColorUtil.applyOpacity(Color.WHITE, 0.0f));
        this.searchField.setHeight(this.carouselButtons.getRectHeight());
        this.searchField.setWidth(145.5f + 200.0f * searchAnim);
        final float searchX = this.x + this.width - (this.searchField.getRealWidth() + 11.0f);
        this.searchField.setXPosition(MathUtils.interpolateFloat(searchX, this.x + this.width / 2.0f - this.searchField.getRealWidth() / 2.0f, searchAnim));
        this.searchField.setYPosition(this.y + this.height / 2.0f - this.searchField.getHeight() / 2.0f);
        this.searchField.setBackgroundText("Search");
        this.searchField.drawTextBox();
        if (!this.searchAnimation.isDone() || this.searchAnimation.finished(Direction.FORWARDS)) {
            this.searchType.setWidth(75.0f);
            this.searchType.setHeight(this.carouselButtons.getRectHeight() - 5.5f);
            this.searchType.setX(this.x + this.width - (this.searchType.getWidth() + 11.0f));
            this.searchType.setY(this.y + this.height / 2.0f - this.searchType.getHeight() / 2.0f);
            this.searchType.setAlpha(this.alpha * searchAnim);
            this.searchType.setAccentColor(ColorUtil.applyOpacity(HUDMod.getClientColors().getFirst(), this.searchType.getAlpha()));
            this.searchType.drawScreen(mouseX, mouseY);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        this.searchField.mouseClicked(mouseX, mouseY, button);
        if (this.searchField.isFocused() || !this.searchField.getText().equals("")) {
            this.searchType.mouseClicked(mouseX, mouseY, button);
            this.currentPanel = "Search";
            return;
        }
        if (!Tenacity.INSTANCE.getCloudDataManager().isRefreshing() && this.refreshTimer.hasTimeElapsed(12000L)) {
            this.refreshButton.mouseClicked(mouseX, mouseY, button);
        }
        this.carouselButtons.mouseClicked(mouseX, mouseY, button);
        this.currentPanel = this.carouselButtons.getCurrentButton();
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public CarouselButtons getCarouselButtons() {
        return this.carouselButtons;
    }
    
    public String getCurrentPanel() {
        return this.currentPanel;
    }
    
    public void setCurrentPanel(final String currentPanel) {
        this.currentPanel = currentPanel;
    }
}
