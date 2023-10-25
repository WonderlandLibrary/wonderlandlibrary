/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.clickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.hud.ClickGui;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.animations.EasingHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.button.ImageButton;
import org.celestial.client.ui.clickgui.GuiSearcher;
import org.celestial.client.ui.clickgui.Palette;
import org.celestial.client.ui.clickgui.component.Component;
import org.celestial.client.ui.clickgui.component.impl.ExpandableComponent;
import org.celestial.client.ui.clickgui.component.impl.component.property.impl.SliderPropertyComponent;
import org.celestial.client.ui.clickgui.component.impl.panel.impl.CategoryPanel;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ClickGuiScreen
extends GuiScreen {
    private static ResourceLocation ANIME_GIRL;
    public static double phase;
    public static double animation;
    public static boolean escapeKeyInUse;
    public static GuiSearcher search;
    private static ClickGuiScreen instance;
    private final List<Component> components = new ArrayList<Component>();
    private final Palette palette;
    protected ArrayList<ImageButton> imageButtons = new ArrayList();
    private Component selectedPanel;
    public static GuiScreen oldScreen;
    private float progress = 0.0f;
    private long lastMS = 0L;

    public ClickGuiScreen() {
        instance = this;
        this.palette = Palette.DEFAULT;
        Type[] categories = Type.values();
        for (int i = categories.length - 1; i >= 0; --i) {
            this.components.add(new CategoryPanel(categories[i], 18 + 128 * i, 10.0f));
            this.selectedPanel = new CategoryPanel(categories[i], 18 + 128 * i, 10.0f);
        }
        oldScreen = this;
    }

    public static ClickGuiScreen getInstance() {
        return instance;
    }

    @Override
    public void initGui() {
        if (ClickGui.girlMode.currentMode.equals("Random")) {
            ANIME_GIRL = new ResourceLocation("celestial/girls/girl" + (int)MathematicHelper.randomizeFloat(1.0f, 7.0f) + ".png");
        }
        this.lastMS = System.currentTimeMillis();
        this.progress = 0.0f;
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.imageButtons.clear();
        this.imageButtons.add(new ImageButton(new ResourceLocation("celestial/brush.png"), sr.getScaledWidth() - this.mc.robotoRegularFontRender.getStringWidth("Wellcome") - 19, sr.getScaledHeight() - this.mc.robotoRegularFontRender.getFontHeight() - 55, 50, 50, "", 18));
        this.imageButtons.add(new ImageButton(new ResourceLocation("celestial/config.png"), sr.getScaledWidth() - this.mc.robotoRegularFontRender.getStringWidth("Wellcome") - 89, sr.getScaledHeight() - this.mc.robotoRegularFontRender.getFontHeight() - 55, 50, 50, "", 22));
        search = new GuiSearcher(1337, this.mc.fontRendererObj, sr.getScaledWidth() / 2 + 320, 10, 150, 18);
        super.initGui();
    }

    public static void callback() {
        if (animation == 0.0 || !ClickGui.girl.getCurrentValue()) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        GL11.glPushMatrix();
        GL11.glColor4d(animation, animation, animation, animation);
        String animeGirlStr = "";
        if (ClickGui.girlMode.currentMode.equals("Girl1")) {
            animeGirlStr = "girl1";
        } else if (ClickGui.girlMode.currentMode.equals("Girl2")) {
            animeGirlStr = "girl2";
        } else if (ClickGui.girlMode.currentMode.equals("Girl3")) {
            animeGirlStr = "girl3";
        } else if (ClickGui.girlMode.currentMode.equals("Girl4")) {
            animeGirlStr = "girl4";
        } else if (ClickGui.girlMode.currentMode.equals("Girl5")) {
            animeGirlStr = "girl5";
        } else if (ClickGui.girlMode.currentMode.equals("Girl6")) {
            animeGirlStr = "girl6";
        }
        if (!ClickGui.girlMode.currentMode.equals("Random")) {
            ANIME_GIRL = new ResourceLocation("celestial/girls/" + animeGirlStr + ".png");
        }
        mc.getTextureManager().bindTexture(ANIME_GIRL);
        RenderHelper.drawImage(ANIME_GIRL, (float)((double)sr.getScaledWidth() - 350.0 * animation), sr.getScaledHeight() - 370, 400.0f, 400.0f, Color.WHITE);
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    public static double createAnimation(double phase) {
        return 1.0 - Math.pow(1.0 - phase, 3.0);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.drawDefaultBackground();
        if (ClickGui.gradientBackground.getCurrentValue()) {
            int cl = ClickGui.color.getColor();
            this.drawGradientRect(0.0f, 0.0f, this.width, this.height, cl, cl);
        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(ClickGui.scale.getCurrentValue(), ClickGui.scale.getCurrentValue(), ClickGui.scale.getCurrentValue());
        mouseX = (int)((float)mouseX / ClickGui.scale.getCurrentValue());
        mouseY = (int)((float)mouseY / ClickGui.scale.getCurrentValue());
        if (ClickGui.backGroundBlur.getCurrentValue()) {
            if (this.mc.gameSettings.ofFastRender) {
                this.mc.gameSettings.ofFastRender = false;
            }
            RenderHelper.renderBlur(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), (int)ClickGui.backGroundBlurStrength.getCurrentValue());
        }
        this.progress = this.progress >= 1.0f ? 1.0f : (float)(System.currentTimeMillis() - this.lastMS) / 850.0f;
        double trueAnim = EasingHelper.easeOutQuart(this.progress);
        for (Component component : this.components) {
            if (component == null) continue;
            component.drawComponent(sr, mouseX, mouseY);
            this.updateMouseWheel(mouseX);
        }
        float x = (float)sr.getScaledWidth() / 2.0f + 320.0f;
        float y = 12.0f;
        int left = (int)x;
        int bottom = (int)(y + 12.0f);
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color2 = c.getRGB();
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(new Color(color2), (double)left, (double)((int)y + bottom - 8), 160.0, 4.0, 22);
        }
        RectHelper.drawColorRect(left, y + (float)bottom - 8.0f, x + 150.0f, y + (float)bottom - 6.0f, new Color(color2).brighter(), new Color(color2), new Color(color2).darker(), new Color(color2).darker().darker());
        search.drawTextBox();
        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (!Mouse.isButtonDown(0)) continue;
            imageButton.onClick(mouseX, mouseY);
        }
        if (search.getText().isEmpty() && !search.isFocused()) {
            this.mc.fontRenderer.drawStringWithShadow("Search Feature...", (float)sr.getScaledWidth() / 2.0f + 362.0f, 16.0, -1);
        }
        GlStateManager.popMatrix();
        if (!Celestial.instance.getLicenseName().isEmpty()) {
            this.mc.fontRenderer.drawStringWithShadow("License expires " + Celestial.instance.getLicenseDate(), 2.0, sr.getScaledHeight() - this.mc.robotoRegularFontRender.getFontHeight() - 4, -1);
        }
    }

    public void updateMouseWheel(int mouseX) {
        mouseX = (int)((float)mouseX / ClickGui.scale.getCurrentValue());
        int scrollWheel = Mouse.getDWheel();
        for (Component panel : this.components) {
            float x = (float)panel.getX();
            if (ClickGui.scrollMode.currentMode.equals("One Panel") && (!((float)mouseX > x) || !((float)mouseX < x + panel.getWidth()))) continue;
            if (ClickGui.scrollInversion.getCurrentValue()) {
                if (scrollWheel < 0) {
                    panel.setY((float)(panel.getY() - (double)ClickGui.scrollSpeed.getCurrentValue()));
                    continue;
                }
                if (scrollWheel <= 0) continue;
                panel.setY((float)(panel.getY() + (double)ClickGui.scrollSpeed.getCurrentValue()));
                continue;
            }
            if (scrollWheel > 0) {
                panel.setY((float)(panel.getY() - (double)ClickGui.scrollSpeed.getCurrentValue()));
                continue;
            }
            if (scrollWheel >= 0) continue;
            panel.setY((float)(panel.getY() + (double)ClickGui.scrollSpeed.getCurrentValue()));
        }
    }

    public Palette getPalette() {
        return this.palette;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.selectedPanel.onKeyPress(keyCode);
        if (!escapeKeyInUse) {
            super.keyTyped(typedChar, keyCode);
        }
        search.textboxKeyTyped(typedChar, keyCode);
        if ((typedChar == '\t' || typedChar == '\r') && search.isFocused()) {
            search.setFocused(!search.isFocused());
        }
        escapeKeyInUse = false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        mouseX = (int)((float)mouseX / ClickGui.scale.getCurrentValue());
        mouseY = (int)((float)mouseY / ClickGui.scale.getCurrentValue());
        search.setFocused(false);
        search.setText("");
        search.mouseClicked(mouseX, mouseY, mouseButton);
        for (Component component : this.components) {
            ExpandableComponent expandableComponent;
            float x = (float)component.getX();
            float y = (float)component.getY();
            float cHeight = component.getHeight();
            if (component instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)component).isExpanded()) {
                cHeight = expandableComponent.getHeightWithExpand();
            }
            if (!((float)mouseX > x) || !((float)mouseY > y) || !((float)mouseX < x + component.getWidth()) || !((float)mouseY < y + cHeight)) continue;
            this.selectedPanel = component;
            component.onMouseClick(mouseX, mouseY, mouseButton);
            break;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        mouseY = (int)((float)mouseY / ClickGui.scale.getCurrentValue());
        this.selectedPanel.onMouseRelease(state);
    }

    @Override
    public void onGuiClosed() {
        this.mc.entityRenderer.theShaderGroup = null;
        this.mc.currentScreen = null;
        SliderPropertyComponent.sliding2 = false;
        super.onGuiClosed();
    }
}

