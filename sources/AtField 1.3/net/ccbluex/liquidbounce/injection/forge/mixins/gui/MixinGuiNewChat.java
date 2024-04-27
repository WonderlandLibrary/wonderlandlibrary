/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ChatLine
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiNewChat
 *  net.minecraft.client.gui.GuiUtilRenderComponents
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.player.EntityPlayer$EnumChatVisibility
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  org.lwjgl.opengl.GL11
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.font.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GuiNewChat.class})
public abstract class MixinGuiNewChat {
    @Shadow
    @Final
    private List field_146253_i;
    @Shadow
    @Final
    private Minecraft field_146247_f;
    @Shadow
    private boolean field_146251_k;
    @Shadow
    private int field_146250_j;

    @Inject(method={"getChatComponent"}, at={@At(value="HEAD")}, cancellable=true)
    private void getChatComponent(int n, int n2, CallbackInfoReturnable callbackInfoReturnable) {
        HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        if (hUD.getState() && ((Boolean)hUD.getFontChatValue().get()).booleanValue()) {
            if (this.func_146241_e()) {
                ScaledResolution scaledResolution = new ScaledResolution(this.field_146247_f);
                int n3 = scaledResolution.func_78325_e();
                float f = this.func_146244_h();
                int n4 = n / n3 - 3;
                int n5 = n2 / n3 - 27;
                n4 = MathHelper.func_76141_d((float)((float)n4 / f));
                n5 = MathHelper.func_76141_d((float)((float)n5 / f));
                if (n4 >= 0 && n5 >= 0) {
                    int n6;
                    int n7 = Math.min(this.func_146232_i(), this.field_146253_i.size());
                    if (n4 <= MathHelper.func_76141_d((float)((float)this.func_146228_f() / this.func_146244_h())) && n5 < FontLoaders.F18.FONT_HEIGHT * n7 + n7 && (n6 = n5 / FontLoaders.F18.FONT_HEIGHT + this.field_146250_j) >= 0 && n6 < this.field_146253_i.size()) {
                        ChatLine chatLine = (ChatLine)this.field_146253_i.get(n6);
                        int n8 = 0;
                        for (ITextComponent iTextComponent : chatLine.func_151461_a()) {
                            if (!(iTextComponent instanceof TextComponentString) || (n8 += FontLoaders.F18.getStringWidth(GuiUtilRenderComponents.func_178909_a((String)((TextComponentString)iTextComponent).func_150265_g(), (boolean)false))) <= n4) continue;
                            callbackInfoReturnable.setReturnValue((Object)iTextComponent);
                            return;
                        }
                    }
                }
            }
            callbackInfoReturnable.setReturnValue(null);
        }
    }

    @Shadow
    public abstract boolean func_146241_e();

    @Shadow
    public abstract int func_146232_i();

    @Shadow
    public abstract int func_146228_f();

    @Inject(method={"drawChat"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawChat(int n, CallbackInfo callbackInfo) {
        HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        if (hUD.getState() && ((Boolean)hUD.getFontChatValue().get()).booleanValue()) {
            callbackInfo.cancel();
            if (this.field_146247_f.field_71474_y.field_74343_n != EntityPlayer.EnumChatVisibility.HIDDEN) {
                int n2 = this.func_146232_i();
                boolean bl = false;
                int n3 = 0;
                int n4 = this.field_146253_i.size();
                float f = this.field_146247_f.field_71474_y.field_74357_r * 0.9f + 0.1f;
                if (n4 > 0) {
                    int n5;
                    int n6;
                    int n7;
                    int n8;
                    if (this.func_146241_e()) {
                        bl = true;
                    }
                    float f2 = this.func_146244_h();
                    int n9 = MathHelper.func_76123_f((float)((float)this.func_146228_f() / f2));
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179109_b((float)2.0f, (float)20.0f, (float)0.0f);
                    GlStateManager.func_179152_a((float)f2, (float)f2, (float)1.0f);
                    for (n8 = 0; n8 + this.field_146250_j < this.field_146253_i.size() && n8 < n2; ++n8) {
                        ChatLine chatLine = (ChatLine)this.field_146253_i.get(n8 + this.field_146250_j);
                        if (chatLine == null || (n7 = n - chatLine.func_74540_b()) >= 200 && !bl) continue;
                        double d = (double)n7 / 200.0;
                        d = 1.0 - d;
                        d *= 10.0;
                        d = MathHelper.func_151237_a((double)d, (double)0.0, (double)1.0);
                        d *= d;
                        n6 = (int)(255.0 * d);
                        if (bl) {
                            n6 = 255;
                        }
                        n6 = (int)((float)n6 * f);
                        ++n3;
                        if (n6 <= 3) continue;
                        n5 = 0;
                        int n10 = -n8 * 9;
                        if (((Boolean)hUD.getChatAnimValue().get()).booleanValue() && !bl) {
                            if (n7 <= 20) {
                                GL11.glTranslatef((float)((float)((double)(-(n9 + 4)) * EaseUtils.INSTANCE.easeInQuart(1.0 - (double)((float)n7 + this.field_146247_f.field_71428_T.field_194147_b) / 20.0))), (float)0.0f, (float)0.0f);
                            }
                            if (n7 >= 180) {
                                GL11.glTranslatef((float)((float)((double)(-(n9 + 4)) * EaseUtils.INSTANCE.easeInQuart((double)((float)n7 + this.field_146247_f.field_71428_T.field_194147_b - 180.0f) / 20.0))), (float)0.0f, (float)0.0f);
                            }
                        }
                        if (((Boolean)hUD.getChatRect().get()).booleanValue()) {
                            Gui.func_73734_a((int)n5, (int)(n10 - 15), (int)(n5 + n9 + 4), (int)(n10 - 6), (int)(n6 / 2 << 24));
                        }
                        GlStateManager.func_179147_l();
                        GlStateManager.func_179141_d();
                        String string = chatLine.func_151461_a().func_150254_d();
                        FontLoaders.F18.drawStringWithShadow(string, n5 + 2, n10 - 14, 0xFFFFFF + (n6 << 24));
                        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        GlStateManager.func_179117_G();
                    }
                    if (bl) {
                        n8 = FontLoaders.F18.FONT_HEIGHT;
                        GlStateManager.func_179109_b((float)-3.0f, (float)0.0f, (float)0.0f);
                        int n11 = n4 * n8 + n4;
                        n7 = n3 * n8 + n3;
                        int n12 = this.field_146250_j * n7 / n4;
                        int n13 = n7 * n7 / n11;
                        if (n11 != n7) {
                            n6 = n12 > 0 ? 170 : 96;
                            n5 = this.field_146251_k ? 0xCC3333 : 0x3333AA;
                            Gui.func_73734_a((int)0, (int)(-n12), (int)2, (int)(-n12 - n13), (int)(n5 + (n6 << 24)));
                            Gui.func_73734_a((int)2, (int)(-n12), (int)1, (int)(-n12 - n13), (int)(0xCCCCCC + (n6 << 24)));
                        }
                    }
                    GlStateManager.func_179121_F();
                }
            }
        }
    }

    @Shadow
    public abstract float func_146244_h();
}

