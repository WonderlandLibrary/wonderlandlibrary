// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui;

import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.module.Module;
import dev.tenacity.Tenacity;
import dev.tenacity.module.impl.render.HUDMod;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.ui.sidegui.forms.impl.SaveForm;
import dev.tenacity.ui.sidegui.forms.impl.EditForm;
import dev.tenacity.ui.sidegui.forms.impl.UploadForm;
import dev.tenacity.ui.sidegui.panels.scriptpanel.ScriptPanel;
import dev.tenacity.ui.sidegui.panels.configpanel.ConfigPanel;
import dev.tenacity.ui.sidegui.panels.infopanel.InfoPanel;
import dev.tenacity.ui.sidegui.panels.searchpanel.SearchPanel;
import java.util.ArrayList;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.utils.objects.Drag;
import java.awt.Color;
import dev.tenacity.ui.sidegui.utils.TooltipObject;
import java.util.List;
import dev.tenacity.ui.sidegui.forms.Form;
import dev.tenacity.ui.sidegui.panels.Panel;
import java.util.HashMap;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.ui.Screen;

public class SideGUI implements Screen
{
    private boolean focused;
    private float rectWidth;
    private float rectHeight;
    private final Animation openAnimation;
    private final Animation hoverAnimation;
    private final Animation clickAnimation;
    private SideGUIHotbar hotbar;
    private HashMap<String, Panel> panels;
    private HashMap<String, Form> forms;
    private static Form currentForm;
    private final List<TooltipObject> tooltips;
    public boolean typing;
    private final Color greenEnabledColor;
    private final Color redBadColor;
    private final Animation formFadeAnimation;
    private Drag drag;
    private TimerUtil timerUtil;
    private float animateX;
    private boolean canSnap;
    
    public SideGUI() {
        this.openAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.clickAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.hotbar = new SideGUIHotbar();
        this.tooltips = new ArrayList<TooltipObject>();
        this.typing = false;
        this.greenEnabledColor = new Color(70, 220, 130);
        this.redBadColor = new Color(209, 56, 56);
        this.formFadeAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.animateX = 0.0f;
    }
    
    @Override
    public void onDrag(final int mouseX, final int mouseY) {
        if (this.drag != null) {
            this.drag.onDraw(mouseX, mouseY);
        }
    }
    
    @Override
    public void initGui() {
        if (this.panels == null) {
            (this.panels = new HashMap<String, Panel>()).put("Search", new SearchPanel());
            this.panels.put("Info", new InfoPanel());
            this.panels.put("Configs", new ConfigPanel());
            this.panels.put("Scripts", new ScriptPanel());
        }
        if (this.forms == null) {
            (this.forms = new HashMap<String, Form>()).put("Upload Script", new UploadForm("Script"));
            this.forms.put("Upload Config", new UploadForm("Config"));
            this.forms.put("Edit Script", new EditForm("Script"));
            this.forms.put("Edit Config", new EditForm("Config"));
            this.forms.put("Save Config", new SaveForm());
        }
        this.hotbar.initGui();
        this.panels.values().forEach(Screen::initGui);
        this.focused = false;
        SideGUI.currentForm = null;
        this.timerUtil = new TimerUtil();
        this.rectWidth = 550.0f;
        this.rectHeight = 350.0f;
        final ScaledResolution sr = new ScaledResolution(SideGUI.mc);
        this.drag = new Drag((float)(sr.getScaledWidth() - 40), sr.getScaledHeight() / 2.0f - this.rectHeight / 2.0f);
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.focused) {
            if (SideGUI.currentForm != null) {
                SideGUI.currentForm.keyTyped(typedChar, keyCode);
                if (keyCode == 1) {
                    this.formFadeAnimation.setDirection(Direction.BACKWARDS);
                    SideGUI.currentForm.clear();
                }
                return;
            }
            this.hotbar.keyTyped(typedChar, keyCode);
            if (keyCode == 1) {
                if (this.hotbar.searchField.isFocused() || !this.hotbar.searchField.getText().equals("")) {
                    this.hotbar.setCurrentPanel(this.hotbar.getCarouselButtons().getCurrentButton());
                }
                this.hotbar.searchField.setText("");
                this.hotbar.searchField.setFocused(false);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final ScaledResolution sr = new ScaledResolution(SideGUI.mc);
        final HUDMod hudMod = (HUDMod)Tenacity.INSTANCE.getModuleCollection().get(HUDMod.class);
        final Pair<Color, Color> colors = HUDMod.getClientColors();
        this.clickAnimation.setDirection(this.focused ? Direction.FORWARDS : Direction.BACKWARDS);
        if (this.animateX == 0.0f) {
            this.animateX = this.drag.getX();
        }
        else if (this.clickAnimation.isDone()) {
            this.animateX = this.drag.getX();
        }
        if (this.clickAnimation.getDirection().forwards() && !this.clickAnimation.isDone()) {
            this.drag.setX(MathUtils.interpolateFloat(this.animateX, sr.getScaledWidth() / 2.0f - this.rectWidth / 2.0f, this.clickAnimation.getOutput()));
        }
        else {
            this.drag.setX(MathUtils.interpolateFloat(this.drag.getInitialX(), this.animateX, this.clickAnimation.getOutput()));
        }
        final float x = MathUtils.interpolateFloat((float)sr.getScaledWidth(), this.drag.getX(), this.openAnimation.getOutput());
        final float y = this.drag.getY();
        final boolean hovering = isHovering(x, y, this.rectWidth, this.rectHeight, mouseX, mouseY);
        if (this.clickAnimation.isDone()) {
            this.hoverAnimation.setDirection((hovering || this.focused) ? Direction.FORWARDS : Direction.BACKWARDS);
        }
        if (this.clickAnimation.finished(Direction.FORWARDS)) {
            this.canSnap = (this.drag.getX() + this.rectWidth / 2.0f + this.rectWidth / 4.0f > sr.getScaledWidth());
            this.hoverAnimation.setDirection(this.canSnap ? Direction.BACKWARDS : this.hoverAnimation.getDirection());
        }
        else {
            this.canSnap = false;
        }
        final Color color = ColorUtil.tripleColor(35, 0.7f + 0.25f * this.hoverAnimation.getOutput().floatValue() + 0.05f * this.clickAnimation.getOutput().floatValue());
        RoundedUtil.drawRound(x + 0.625f, y + 0.625f, this.rectWidth - 1.25f, this.rectHeight - 1.25f, 5.0f, color);
        final float alpha = 0.25f + 0.15f * this.hoverAnimation.getOutput().floatValue() + 0.6f * this.clickAnimation.getOutput().floatValue();
        this.hotbar.x = x;
        this.hotbar.y = y;
        this.hotbar.width = this.rectWidth;
        this.hotbar.height = 36.0f;
        this.hotbar.alpha = alpha;
        this.hotbar.drawScreen(mouseX, mouseY);
        this.typing = (this.hotbar.searchField.isFocused() || !this.hotbar.searchField.getText().equals("") || SideGUI.currentForm != null);
        if (!this.focused) {
            return;
        }
        if (this.panels.containsKey(this.hotbar.getCurrentPanel())) {
            final Panel panel = this.panels.get(this.hotbar.getCurrentPanel());
            if (panel instanceof SearchPanel) {
                ((SearchPanel)panel).setSearchType(this.hotbar.searchType.getSelection());
            }
            panel.setX(x);
            panel.setY(y + this.hotbar.height);
            panel.setWidth(this.rectWidth);
            panel.setHeight(this.rectHeight - this.hotbar.height);
            panel.setAlpha(alpha);
            panel.drawScreen(mouseX, mouseY);
        }
        if (this.formFadeAnimation.finished(Direction.BACKWARDS)) {
            SideGUI.currentForm = null;
        }
        if (!this.formFadeAnimation.isDone() || this.formFadeAnimation.finished(Direction.FORWARDS)) {
            final float formAnim = this.formFadeAnimation.getOutput().floatValue();
            RoundedUtil.drawRound(x + 0.625f, y + 0.625f, this.rectWidth - 1.25f, this.rectHeight - 1.25f, 5.0f, ColorUtil.applyOpacity(Color.BLACK, 0.4f * formAnim));
            SideGUI.currentForm.setX(x + this.rectWidth / 2.0f - SideGUI.currentForm.getWidth() / 2.0f);
            SideGUI.currentForm.setY(y + this.rectHeight / 2.0f - SideGUI.currentForm.getHeight() / 2.0f);
            SideGUI.currentForm.setAlpha(alpha * formAnim);
            SideGUI.currentForm.drawScreen(mouseX, mouseY);
        }
    }
    
    public void drawForEffects(final boolean bloom) {
        final ScaledResolution sr = new ScaledResolution(SideGUI.mc);
        final float x = MathUtils.interpolateFloat((float)sr.getScaledWidth(), this.drag.getX(), this.openAnimation.getOutput());
        final float y = this.drag.getY();
        RoundedUtil.drawRound(x + 0.625f, y + 0.625f, this.rectWidth - 1.25f, this.rectHeight - 1.25f, 5.0f, Color.BLACK);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final boolean hovering = isHovering(this.drag.getX(), this.drag.getY(), this.rectWidth, this.rectHeight, mouseX, mouseY);
        if (!this.focused && hovering) {
            this.focused = true;
        }
        else if (this.focused) {
            if (SideGUI.currentForm != null) {
                SideGUI.currentForm.mouseClicked(mouseX, mouseY, button);
                return;
            }
            final boolean hoveringTop = isHovering(this.drag.getX(), this.drag.getY(), this.rectWidth, 40.0f, mouseX, mouseY);
            if (!this.hotbar.searchField.isFocused() && !this.hotbar.getCarouselButtons().isHovering()) {
                this.drag.onClick(mouseX, mouseY, button, hoveringTop);
            }
            this.hotbar.mouseClicked(mouseX, mouseY, button);
            if (this.panels.containsKey(this.hotbar.getCurrentPanel())) {
                this.panels.get(this.hotbar.getCurrentPanel()).mouseClicked(mouseX, mouseY, button);
            }
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int button) {
        this.drag.onRelease(button);
        if (this.canSnap) {
            this.focused = false;
        }
    }
    
    public ScriptPanel getScriptPanel() {
        return this.panels.get("Scripts");
    }
    
    public ConfigPanel getConfigPanel() {
        return this.panels.get("Configs");
    }
    
    public void addTooltip(final TooltipObject tooltip) {
        if (this.tooltips.contains(tooltip)) {
            return;
        }
        this.tooltips.add(tooltip);
    }
    
    public Form displayForm(final String form) {
        if (form == null) {
            SideGUI.currentForm.clear();
            this.formFadeAnimation.setDirection(Direction.BACKWARDS);
            return null;
        }
        SideGUI.currentForm = this.forms.get(form);
        this.formFadeAnimation.setDirection(Direction.FORWARDS);
        return SideGUI.currentForm;
    }
    
    public static boolean isHovering(final float x, final float y, final float width, final float height, final int mouseX, final int mouseY) {
        return SideGUI.currentForm == null && HoveringUtil.isHovering(x, y, width, height, mouseX, mouseY);
    }
    
    public boolean isFocused() {
        return this.focused;
    }
    
    public float getRectWidth() {
        return this.rectWidth;
    }
    
    public float getRectHeight() {
        return this.rectHeight;
    }
    
    public Animation getOpenAnimation() {
        return this.openAnimation;
    }
    
    public Animation getHoverAnimation() {
        return this.hoverAnimation;
    }
    
    public Animation getClickAnimation() {
        return this.clickAnimation;
    }
    
    public SideGUIHotbar getHotbar() {
        return this.hotbar;
    }
    
    public HashMap<String, Panel> getPanels() {
        return this.panels;
    }
    
    public HashMap<String, Form> getForms() {
        return this.forms;
    }
    
    public List<TooltipObject> getTooltips() {
        return this.tooltips;
    }
    
    public boolean isTyping() {
        return this.typing;
    }
    
    public Color getGreenEnabledColor() {
        return this.greenEnabledColor;
    }
    
    public Color getRedBadColor() {
        return this.redBadColor;
    }
    
    public Animation getFormFadeAnimation() {
        return this.formFadeAnimation;
    }
    
    public Drag getDrag() {
        return this.drag;
    }
    
    public TimerUtil getTimerUtil() {
        return this.timerUtil;
    }
    
    public float getAnimateX() {
        return this.animateX;
    }
    
    public boolean isCanSnap() {
        return this.canSnap;
    }
    
    public void setFocused(final boolean focused) {
        this.focused = focused;
    }
}
