/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package cc.paimon.skid.lbp.newVer;

import cc.paimon.modules.render.NewGUI;
import cc.paimon.skid.lbp.newVer.IconManager;
import cc.paimon.skid.lbp.newVer.MouseUtils;
import cc.paimon.skid.lbp.newVer.element.CategoryElement;
import cc.paimon.skid.lbp.newVer.element.SearchElement;
import cc.paimon.skid.lbp.newVer.element.module.ModuleElement;
import cc.paimon.utils.ClickEffect;
import dev.sakura_starring.util.safe.QQUtils;
import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class NewUi
extends GuiScreen {
    private float startYAnim;
    private static NewUi instance;
    private boolean got = false;
    private SearchElement searchElement;
    public final List categoryElements;
    private float fading = 0.0f;
    private float endYAnim;
    private List clickEffects = new ArrayList();

    private static void lambda$mouseClicked$0(CategoryElement categoryElement) {
        categoryElement.setFocused(false);
    }

    public static void resetInstance() {
        instance = new NewUi();
    }

    protected void func_146286_b(int n, int n2, int n3) {
        this.searchElement.handleMouseRelease(n, n2, n3, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, this.categoryElements);
        if (!this.searchElement.isTyping()) {
            for (CategoryElement categoryElement : this.categoryElements) {
                if (!categoryElement.getFocused()) continue;
                categoryElement.handleMouseRelease(n, n2, n3, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80);
            }
        }
        super.func_146286_b(n, n2, n3);
    }

    public static final NewUi getInstance() {
        return instance == null ? (instance = new NewUi()) : instance;
    }

    protected void func_73869_a(char c, int n) throws IOException {
        for (CategoryElement categoryElement : this.categoryElements) {
            if (!categoryElement.getFocused() || !categoryElement.handleKeyTyped(c, n)) continue;
            return;
        }
        if (this.searchElement.handleTyping(c, n, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, this.categoryElements)) {
            return;
        }
        super.func_73869_a(c, n);
    }

    private NewUi() {
        this.categoryElements = new ArrayList();
        this.startYAnim = (float)this.field_146295_m / 2.0f;
        this.endYAnim = (float)this.field_146295_m / 2.0f;
        for (ModuleCategory moduleCategory : ModuleCategory.values()) {
            this.categoryElements.add(new CategoryElement(moduleCategory));
        }
        ((CategoryElement)this.categoryElements.get(0)).setFocused(true);
    }

    protected void func_73864_a(int n, int n2, int n3) throws IOException {
        if (MouseUtils.mouseWithinBounds(n, n2, (float)this.field_146294_l - 54.0f, 30.0f, (float)this.field_146294_l - 30.0f, 50.0f)) {
            this.field_146297_k.func_147108_a(null);
            return;
        }
        float f = 24.0f;
        float f2 = 140.0f;
        ClickEffect clickEffect = new ClickEffect(n, n2);
        this.clickEffects.add(clickEffect);
        this.searchElement.handleMouseClick(n, n2, n3, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, this.categoryElements);
        if (!this.searchElement.isTyping()) {
            for (CategoryElement categoryElement : this.categoryElements) {
                if (categoryElement.getFocused()) {
                    categoryElement.handleMouseClick(n, n2, n3, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80);
                }
                if (MouseUtils.mouseWithinBounds(n, n2, 30.0f, f2, 230.0f, f2 + 24.0f) && !this.searchElement.isTyping()) {
                    this.categoryElements.forEach(NewUi::lambda$mouseClicked$0);
                    categoryElement.setFocused(true);
                    return;
                }
                f2 += 24.0f;
            }
        }
    }

    public void func_73863_a(int n, int n2, float f) {
        this.drawFullSized(n, n2, f, NewGUI.getAccentColor());
        if (this.clickEffects.size() > 0) {
            Iterator iterator2 = this.clickEffects.iterator();
            while (iterator2.hasNext()) {
                ClickEffect clickEffect = (ClickEffect)iterator2.next();
                clickEffect.draw();
                if (!clickEffect.canRemove()) continue;
                iterator2.remove();
            }
        }
    }

    private void drawFullSized(int n, int n2, float f, Color color) {
        RenderUtils.drawRoundedRect(30.0f, 30.0f, (float)this.field_146294_l - 30.0f, (float)this.field_146295_m - 30.0f, 8.0f, new Color(238, 238, 238).getRGB());
        this.fading = MouseUtils.mouseWithinBounds(n, n2, (float)this.field_146294_l - 54.0f, 30.0f, (float)this.field_146294_l - 30.0f, 50.0f) ? (this.fading += 0.2f * (float)RenderUtils.deltaTime * 0.045f) : (this.fading -= 0.2f * (float)RenderUtils.deltaTime * 0.045f);
        this.fading = MathHelper.func_76131_a((float)this.fading, (float)0.0f, (float)1.0f);
        RenderUtils.customRounded((float)this.field_146294_l - 54.0f, 30.0f, (float)this.field_146294_l - 30.0f, 50.0f, 0.0f, 8.0f, 0.0f, 8.0f, new Color(1.0f, 0.0f, 0.0f, this.fading).getRGB());
        GlStateManager.func_179118_c();
        RenderUtils.drawImage(IconManager.removeIcon, this.field_146294_l - 47, 35, 10, 10);
        GlStateManager.func_179141_d();
        Stencil.write(true);
        RenderUtils.drawFilledCircle(65, 80, 25.0f, new Color(45, 45, 45));
        Stencil.erase(true);
        if (this.field_146297_k.field_71439_g != null) {
            if (!this.got) {
                try {
                    this.field_146297_k.func_110434_K().func_110579_a(new ResourceLocation("sb"), (ITextureObject)new DynamicTexture(ImageIO.read(new URL("https://q.qlogo.cn/headimg_dl?dst_uin=" + QQUtils.qqNumber + "&spec=640&img_type=png"))));
                    this.got = true;
                }
                catch (IOException iOException) {
                    throw new RuntimeException(iOException);
                }
            }
            this.field_146297_k.func_110434_K().func_110577_a(new ResourceLocation("sb"));
            GL11.glPushMatrix();
            GL11.glTranslatef((float)40.0f, (float)55.0f, (float)0.0f);
            GL11.glDisable((int)2929);
            GL11.glEnable((int)3042);
            GL11.glDepthMask((boolean)false);
            OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            RenderUtils.drawImage(LiquidBounce.wrapper.getClassProvider().createResourceLocation("sb"), 0, 0, 50, 50);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)2929);
            GL11.glPopMatrix();
        }
        Stencil.dispose();
        if (Fonts.posterama35.getStringWidth(this.field_146297_k.field_71439_g.func_146103_bH().getName()) > 70) {
            Fonts.posterama40.drawString(Fonts.posterama40.getStringWidth(this.field_146297_k.field_71439_g.func_70005_c_()) + "...", 100, 60 - Fonts.posterama40.getFontHeight() + 15, new Color(26, 26, 26).getRGB());
        } else {
            Fonts.posterama40.drawString(this.field_146297_k.field_71439_g.func_70005_c_(), 100, 60 - Fonts.posterama40.getFontHeight() + 15, new Color(26, 26, 26).getRGB());
        }
        if (this.searchElement.drawBox(n, n2, color)) {
            this.searchElement.drawPanel(n, n2, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, Mouse.getDWheel(), this.categoryElements, color);
            return;
        }
        float f2 = 24.0f;
        float f3 = 140.0f;
        for (CategoryElement categoryElement : this.categoryElements) {
            categoryElement.drawLabel(n, n2, 30.0f, f3, 200.0f, 24.0f);
            if (categoryElement.getFocused()) {
                float f4 = (Boolean)NewGUI.fastRenderValue.get() != false ? f3 + 6.0f : (this.startYAnim = AnimationUtils.animate(f3 + 6.0f, this.startYAnim, (this.startYAnim - (f3 + 5.0f) > 0.0f ? 0.65f : 0.55f) * (float)RenderUtils.deltaTime * 0.025f));
                this.endYAnim = (Boolean)NewGUI.fastRenderValue.get() != false ? f3 + 24.0f - 6.0f : AnimationUtils.animate(f3 + 24.0f - 6.0f, this.endYAnim, (this.endYAnim - (f3 + 24.0f - 5.0f) < 0.0f ? 0.65f : 0.55f) * (float)RenderUtils.deltaTime * 0.025f);
                categoryElement.drawPanel(n, n2, 230.0f, 50.0f, this.field_146294_l - 260, this.field_146295_m - 80, Mouse.getDWheel(), color);
            }
            f3 += 24.0f;
        }
        RenderUtils.drawRoundedRect(32.0f, this.startYAnim, 34.0f, this.endYAnim, 1.0f, color.getRGB());
        super.func_73863_a(n, n2, f);
    }

    public boolean func_73868_f() {
        return false;
    }

    public void func_146281_b() {
        for (CategoryElement categoryElement : this.categoryElements) {
            if (!categoryElement.getFocused()) continue;
            categoryElement.handleMouseRelease(-1, -1, 0, 0.0f, 0.0f, 0.0f, 0.0f);
        }
        Keyboard.enableRepeatEvents((boolean)false);
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        for (CategoryElement categoryElement : this.categoryElements) {
            for (ModuleElement moduleElement : categoryElement.getModuleElements()) {
                if (!moduleElement.listeningKeybind()) continue;
                moduleElement.resetState();
            }
        }
        this.searchElement = new SearchElement(40.0f, 115.0f, 180.0f, 20.0f);
        super.func_73866_w_();
    }
}

