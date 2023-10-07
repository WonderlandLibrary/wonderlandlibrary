/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.newVer;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.report.liquidware.utils.ui.FuckerNMSL;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.IconManager;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.CategoryElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.SearchElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.module.ModuleElement;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.AnimationUtils;
import net.ccbluex.liquidbounce.utils.MouseUtils;
import net.ccbluex.liquidbounce.utils.render.BlurUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class NewUi
extends GuiScreen {
    private static NewUi instance;
    public final List<CategoryElement> categoryElements = new ArrayList<CategoryElement>();
    private float startYAnim = (float)this.field_146295_m / 2.0f;
    private float endYAnim = (float)this.field_146295_m / 2.0f;
    private SearchElement searchElement;
    private float fading = 0.0f;

    public static final NewUi getInstance() {
        return instance == null ? (instance = new NewUi()) : instance;
    }

    public static void resetInstance() {
        instance = new NewUi();
    }

    private NewUi() {
        for (ModuleCategory c : ModuleCategory.values()) {
            this.categoryElements.add(new CategoryElement(c));
        }
        this.categoryElements.get(0).setFocused(true);
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        for (CategoryElement ce : this.categoryElements) {
            for (ModuleElement me : ce.getModuleElements()) {
                if (!me.listeningKeybind()) continue;
                me.resetState();
            }
        }
        this.searchElement = new SearchElement(40.0f, 115.0f, 180.0f, 20.0f);
        super.func_73866_w_();
    }

    public void func_146281_b() {
        for (CategoryElement ce : this.categoryElements) {
            if (!ce.getFocused()) continue;
            ce.handleMouseRelease(-1, -1, 0, 0.0f, 0.0f, 0.0f, 0.0f);
        }
        Keyboard.enableRepeatEvents((boolean)false);
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.drawFullSized(mouseX, mouseY, partialTicks, ClickGUI.generateColor());
    }

    private void drawFullSized(int mouseX, int mouseY, float partialTicks, Color accentColor) {
        BlurUtils.blurAreaRounded(30.0f, 30.0f, (float)this.field_146294_l - 30.0f, (float)this.field_146295_m - 30.0f, 8.0f, 10.0f);
        this.fading = MouseUtils.mouseWithinBounds(mouseX, mouseY, (float)this.field_146294_l - 54.0f, 30.0f, (float)this.field_146294_l - 30.0f, 50.0f) ? (this.fading += 0.2f * (float)RenderUtils.deltaTime * 0.045f) : (this.fading -= 0.2f * (float)RenderUtils.deltaTime * 0.045f);
        this.fading = MathHelper.func_76131_a((float)this.fading, (float)0.0f, (float)1.0f);
        RenderUtils.customRounded((float)this.field_146294_l - 54.0f, 30.0f, (float)this.field_146294_l - 30.0f, 50.0f, 0.0f, 8.0f, 0.0f, 8.0f, new Color(1.0f, 0.0f, 0.0f, this.fading).getRGB());
        GlStateManager.func_179118_c();
        RenderUtils.drawImage(IconManager.removeIcon, this.field_146294_l - 47, 35, 10, 10);
        GlStateManager.func_179141_d();
        Stencil.write(true);
        RenderUtils.drawFilledCircleGui(65.0f, 80.0f, 25.0f, new Color(45, 45, 45));
        Stencil.erase(true);
        if (FuckerNMSL.username.getText() != null) {
            ResourceLocation skin = this.field_146297_k.func_147114_u().func_175102_a(this.field_146297_k.field_71439_g.func_110124_au()).func_178837_g();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)40.0f, (float)55.0f, (float)0.0f);
            GL11.glDisable((int)2929);
            GL11.glEnable((int)3042);
            GL11.glDepthMask((boolean)false);
            OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.field_146297_k.func_110434_K().func_110577_a(skin);
            Gui.func_152125_a((int)0, (int)0, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)50, (int)50, (float)64.0f, (float)64.0f);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)2929);
            GL11.glPopMatrix();
        }
        Stencil.dispose();
        if (Fonts.fontLarge.func_78256_a(FuckerNMSL.username.getText()) > 70) {
            Fonts.fontLarge.func_78276_b(Fonts.fontLarge.func_78269_a(FuckerNMSL.username.getText(), 50) + "...", 100, 78 - Fonts.fontLarge.field_78288_b + 15, -1);
        } else {
            Fonts.fontLarge.func_78276_b(FuckerNMSL.username.getText(), 100, 78 - Fonts.fontLarge.field_78288_b + 15, -1);
        }
        if (this.searchElement.drawBox(mouseX, mouseY, accentColor)) {
            this.searchElement.drawPanel(mouseX, mouseY, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, Mouse.getDWheel(), this.categoryElements, accentColor);
            return;
        }
        float elementHeight = 24.0f;
        float startY = 140.0f;
        for (CategoryElement ce : this.categoryElements) {
            ce.drawLabel(mouseX, mouseY, 30.0f, startY, 200.0f, 24.0f);
            if (ce.getFocused()) {
                float f = (Boolean)ClickGUI.fastRenderValue.get() != false ? startY + 6.0f : (this.startYAnim = AnimationUtils.animate(startY + 6.0f, this.startYAnim, (this.startYAnim - (startY + 5.0f) > 0.0f ? 0.65f : 0.55f) * (float)RenderUtils.deltaTime * 0.025f));
                this.endYAnim = (Boolean)ClickGUI.fastRenderValue.get() != false ? startY + 24.0f - 6.0f : AnimationUtils.animate(startY + 24.0f - 6.0f, this.endYAnim, (this.endYAnim - (startY + 24.0f - 5.0f) < 0.0f ? 0.65f : 0.55f) * (float)RenderUtils.deltaTime * 0.025f);
                ce.drawPanel(mouseX, mouseY, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, Mouse.getDWheel(), accentColor);
            }
            startY += 24.0f;
        }
        RenderUtils.originalRoundedRect(32.0f, this.startYAnim, 34.0f, this.endYAnim, 1.0f, accentColor.getRGB());
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (MouseUtils.mouseWithinBounds(mouseX, mouseY, (float)this.field_146294_l - 54.0f, 30.0f, (float)this.field_146294_l - 30.0f, 50.0f)) {
            this.field_146297_k.func_147108_a(null);
            return;
        }
        float elementHeight = 24.0f;
        float startY = 140.0f;
        this.searchElement.handleMouseClick(mouseX, mouseY, mouseButton, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, this.categoryElements);
        if (!this.searchElement.isTyping()) {
            for (CategoryElement ce : this.categoryElements) {
                if (ce.getFocused()) {
                    ce.handleMouseClick(mouseX, mouseY, mouseButton, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80);
                }
                if (MouseUtils.mouseWithinBounds(mouseX, mouseY, 30.0f, startY, 230.0f, startY + 24.0f) && !this.searchElement.isTyping()) {
                    this.categoryElements.forEach(e -> e.setFocused(false));
                    ce.setFocused(true);
                    return;
                }
                startY += 24.0f;
            }
        }
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        for (CategoryElement ce : this.categoryElements) {
            if (!ce.getFocused() || !ce.handleKeyTyped(typedChar, keyCode)) continue;
            return;
        }
        if (this.searchElement.handleTyping(typedChar, keyCode, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, this.categoryElements)) {
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }

    protected void func_146286_b(int mouseX, int mouseY, int state) {
        this.searchElement.handleMouseRelease(mouseX, mouseY, state, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, this.categoryElements);
        if (!this.searchElement.isTyping()) {
            for (CategoryElement ce : this.categoryElements) {
                if (!ce.getFocused()) continue;
                ce.handleMouseRelease(mouseX, mouseY, state, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80);
            }
        }
        super.func_146286_b(mouseX, mouseY, state);
    }

    public boolean func_73868_f() {
        return false;
    }
}

