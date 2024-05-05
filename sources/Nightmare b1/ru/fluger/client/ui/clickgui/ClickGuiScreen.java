// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.clickgui;

import ru.fluger.client.ui.clickgui.component.impl.component.property.impl.SliderPropertyComponent;
import ru.fluger.client.ui.clickgui.component.impl.ExpandableComponent;
import java.io.IOException;
import java.util.Iterator;
import org.lwjgl.input.Mouse;
import ru.fluger.client.helpers.render.animations.EasingHelper;
import ru.fluger.client.prot.UserData;
import ru.fluger.client.Fluger;
import ru.fluger.client.helpers.render.RenderHelper;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.ui.clickgui.component.impl.panel.impl.CategoryPanel;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.ui.button.ImageButton;
import java.util.ArrayList;
import ru.fluger.client.ui.clickgui.component.Component;
import java.util.List;

public class ClickGuiScreen extends blk
{
    private static nf ANIME_GIRL;
    public static double phase;
    public static double animation;
    public static boolean escapeKeyInUse;
    public static GuiSearcher search;
    private static ClickGuiScreen instance;
    private final List<Component> components;
    private final Palette palette;
    protected ArrayList<ImageButton> imageButtons;
    private Component selectedPanel;
    public static blk oldScreen;
    private float progress;
    private long lastMS;
    
    public ClickGuiScreen() {
        this.components = new ArrayList<Component>();
        this.imageButtons = new ArrayList<ImageButton>();
        this.progress = 0.0f;
        this.lastMS = 0L;
        ClickGuiScreen.instance = this;
        this.palette = Palette.DEFAULT;
        final Type[] categories = Type.values();
        for (int i = categories.length - 1; i >= 0; --i) {
            this.components.add(new CategoryPanel(categories[i], (float)(18 + 128 * i), 10.0f));
            this.selectedPanel = new CategoryPanel(categories[i], (float)(18 + 128 * i), 10.0f);
        }
        ClickGuiScreen.oldScreen = this;
    }
    
    public static void callback() {
        if (ClickGuiScreen.animation == 0.0 || !ClickGui.girl.getCurrentValue()) {
            return;
        }
        final bib mc = bib.z();
        final bit sr = new bit(mc);
        GL11.glPushMatrix();
        GL11.glColor4d(ClickGuiScreen.animation, ClickGuiScreen.animation, ClickGuiScreen.animation, ClickGuiScreen.animation);
        String animeGirlStr = "";
        if (ClickGui.girlMode.currentMode.equals("Girl1")) {
            animeGirlStr = "girl1";
        }
        else if (ClickGui.girlMode.currentMode.equals("Girl2")) {
            animeGirlStr = "girl2";
        }
        else if (ClickGui.girlMode.currentMode.equals("Girl3")) {
            animeGirlStr = "girl3";
        }
        else if (ClickGui.girlMode.currentMode.equals("Girl4")) {
            animeGirlStr = "girl4";
        }
        else if (ClickGui.girlMode.currentMode.equals("Girl5")) {
            animeGirlStr = "girl5";
        }
        else if (ClickGui.girlMode.currentMode.equals("Girl6")) {
            animeGirlStr = "girl6";
        }
        else if (ClickGui.girlMode.currentMode.equals("Girl7")) {
            animeGirlStr = "girl7";
        }
        ClickGuiScreen.ANIME_GIRL = new nf("nightmare/girls/" + animeGirlStr + ".png");
        mc.N().a(ClickGuiScreen.ANIME_GIRL);
        RenderHelper.drawImage(ClickGuiScreen.ANIME_GIRL, (float)(sr.a() - 370.0 * ClickGuiScreen.animation), (float)(sr.b() - 370), 400.0f, 400.0f, Color.WHITE);
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        bus.c(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    public static ClickGuiScreen getInstance() {
        return ClickGuiScreen.instance;
    }
    
    @Override
    public void b() {
        this.lastMS = System.currentTimeMillis();
        this.progress = 0.0f;
        final bit sr = new bit(this.j);
        this.imageButtons.clear();
        ClickGuiScreen.search = new GuiSearcher(1337, this.j.k, sr.a() / 2 + 320, 10, 150, 18);
        if (Fluger.status.equals("a")) {
            this.j.n();
            this.j.h();
            System.exit(-1);
        }
        super.b();
    }
    
    public static double createAnimation(final double phase) {
        return 1.0 - Math.pow(1.0 - phase, 3.0);
    }
    
    @Override
    public void a(int mouseX, int mouseY, final float partialTicks) {
        final bit sr = new bit(this.j);
        this.c();
        if (ClickGui.gradientBackground.getCurrentValue()) {
            final int cl = ClickGui.color.getColor();
            this.drawGradientRect(0.0f, 0.0f, (float)this.l, (float)this.m, cl, cl);
        }
        bus.G();
        bus.b(ClickGui.scale.getCurrentValue(), ClickGui.scale.getCurrentValue(), ClickGui.scale.getCurrentValue());
        mouseX /= (int)ClickGui.scale.getCurrentValue();
        mouseY /= (int)ClickGui.scale.getCurrentValue();
        if (ClickGui.backGroundBlur.getCurrentValue()) {
            if (this.j.t.ofFastRender) {
                this.j.t.ofFastRender = false;
            }
            RenderHelper.renderBlur(0, 0, sr.a(), sr.b(), (int)ClickGui.backGroundBlurStrength.getCurrentValue());
        }
        this.j.rubik_18.drawStringWithShadow("\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u044f \u0437\u0430\u043a\u043e\u043d\u0447\u0438\u0442\u0441\u044f - " + UserData.instance().getLicenseDate(), 2.0, sr.b() - 10, new Color(255, 255, 255).getRGB());
        this.j.rubik_18.drawStringWithShadow("UID " + UserData.instance().getID(), sr.a() - this.j.rubik_18.getStringWidth("UID " + UserData.instance().getID()) - 4, sr.b() - 9, new Color(255, 255, 255).getRGB());
        this.progress = ((this.progress >= 1.0f) ? 1.0f : ((System.currentTimeMillis() - this.lastMS) / 850.0f));
        final double trueAnim = EasingHelper.easeOutQuart(this.progress);
        for (final Component component : this.components) {
            if (component == null) {
                continue;
            }
            component.drawComponent(sr, mouseX, mouseY);
            this.updateMouseWheel();
        }
        for (final ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (!Mouse.isButtonDown(0)) {
                continue;
            }
            imageButton.onClick(mouseX, mouseY);
        }
        bus.H();
    }
    
    public void updateMouseWheel() {
        final int scrollWheel = Mouse.getDWheel();
        for (final Component panel : this.components) {
            final float x = (float)panel.getX();
            if (scrollWheel < 0) {
                panel.setY((float)(panel.getY() - 13.0));
            }
            else {
                if (scrollWheel <= 0) {
                    continue;
                }
                panel.setY((float)(panel.getY() + 13.0));
            }
        }
    }
    
    public Palette getPalette() {
        return this.palette;
    }
    
    @Override
    protected void a(final char typedChar, final int keyCode) throws IOException {
        this.selectedPanel.onKeyPress(keyCode);
        if (!ClickGuiScreen.escapeKeyInUse) {
            super.a(typedChar, keyCode);
        }
        ClickGuiScreen.escapeKeyInUse = false;
    }
    
    @Override
    protected void a(int mouseX, int mouseY, final int mouseButton) {
        mouseX /= (int)ClickGui.scale.getCurrentValue();
        mouseY /= (int)ClickGui.scale.getCurrentValue();
        ClickGuiScreen.search.setFocused(false);
        ClickGuiScreen.search.setText("");
        ClickGuiScreen.search.mouseClicked(mouseX, mouseY, mouseButton);
        for (final Component component : this.components) {
            final float x = (float)component.getX();
            final float y = (float)component.getY();
            float cHeight = component.getHeight();
            final ExpandableComponent expandableComponent;
            if (component instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)component).isExpanded()) {
                cHeight = (float)expandableComponent.getHeightWithExpand();
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth()) {
                if (mouseY >= y + cHeight) {
                    continue;
                }
                (this.selectedPanel = component).onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }
    
    @Override
    protected void b(final int mouseX, int mouseY, final int state) {
        mouseY /= (int)ClickGui.scale.getCurrentValue();
        this.selectedPanel.onMouseRelease(state);
    }
    
    @Override
    public void m() {
        this.j.o.af = null;
        this.j.m = null;
        SliderPropertyComponent.sliding2 = false;
        super.m();
    }
}
