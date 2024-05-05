/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ChatLine
 *  net.minecraft.client.gui.GuiNewChat
 *  net.minecraft.client.gui.GuiUtilRenderComponents
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.player.EntityPlayer$EnumChatVisibility
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.BlurUtils;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GuiNewChat.class})
public abstract class MixinGuiNewChat {
    @Shadow
    @Final
    private Minecraft field_146247_f;
    @Shadow
    @Final
    private List<ChatLine> field_146253_i;
    @Shadow
    private int field_146250_j;
    @Shadow
    private boolean field_146251_k;
    private String lastMessage;
    private int sameMessageAmount;
    private int line;
    private final HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);

    @Shadow
    public abstract int func_146232_i();

    @Shadow
    public abstract boolean func_146241_e();

    @Shadow
    public abstract float func_146244_h();

    @Shadow
    public abstract int func_146228_f();

    @Shadow
    public abstract void func_146234_a(IChatComponent var1, int var2);

    @Overwrite
    public void func_146227_a(IChatComponent chatComponent) {
        if (!this.hud.getState() || !((Boolean)this.hud.chatCombineValue.get()).booleanValue()) {
            this.func_146234_a(chatComponent, 0);
        } else if (((Boolean)this.hud.chatCombineValue.get()).booleanValue()) {
            String text = this.fixString(chatComponent.func_150254_d());
            if (text.equals(this.lastMessage)) {
                Minecraft.func_71410_x().field_71456_v.func_146158_b().func_146242_c(this.line);
                ++this.sameMessageAmount;
                chatComponent.func_150258_a(ChatFormatting.WHITE + " [x" + this.sameMessageAmount + "]");
            } else {
                this.sameMessageAmount = 1;
            }
            this.lastMessage = text;
            ++this.line;
            if (this.line > 256) {
                this.line = 0;
            }
            this.func_146234_a(chatComponent, this.line);
        }
    }

    @ModifyConstant(method={"setChatLine"}, constant={@Constant(intValue=100)})
    private int fixMsgLimit(int constant) {
        if (this.hud.getState() && ((Boolean)this.hud.chatClearValue.get()).booleanValue()) {
            return 114514;
        }
        return 100;
    }

    private String fixString(String str) {
        str = str.replaceAll("\uf8ff", "");
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c > '\uff01' && c < '\uff60') {
                sb.append(Character.toChars(c - 65248));
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    @Overwrite
    public void func_146230_a(int updateCounter) {
        boolean canFont;
        boolean bl = canFont = this.hud.getState() && (Boolean)this.hud.fontChatValue.get() != false;
        if (this.field_146247_f.field_71474_y.field_74343_n != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int minH = 1000;
            int maxH = -1000;
            int i = this.func_146232_i();
            boolean flag = false;
            int j = 0;
            int k = this.field_146253_i.size();
            float f = this.field_146247_f.field_71474_y.field_74357_r * 0.9f + 0.1f;
            if (k > 0) {
                int l1;
                int j1;
                int i1;
                if (this.func_146241_e()) {
                    flag = true;
                }
                float f1 = this.func_146244_h();
                int l = MathHelper.func_76123_f((float)((float)this.func_146228_f() / f1));
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b((float)2.0f, (float)20.0f, (float)0.0f);
                GlStateManager.func_179152_a((float)f1, (float)f1, (float)1.0f);
                for (i1 = 0; i1 + this.field_146250_j < this.field_146253_i.size() && i1 < i; ++i1) {
                    ChatLine chatline = this.field_146253_i.get(i1 + this.field_146250_j);
                    if (chatline == null || (j1 = updateCounter - chatline.func_74540_b()) >= 200 && !flag) continue;
                    double d0 = (double)j1 / 200.0;
                    d0 = 1.0 - d0;
                    d0 *= 10.0;
                    d0 = MathHelper.func_151237_a((double)d0, (double)0.0, (double)1.0);
                    d0 *= d0;
                    l1 = (int)(255.0 * d0);
                    if (flag) {
                        l1 = 255;
                    }
                    l1 = (int)((float)l1 * f);
                    ++j;
                    if (l1 <= 3) continue;
                    GL11.glPushMatrix();
                    int i2 = 0;
                    int j2 = -i1 * 9;
                    if (((Boolean)this.hud.chatAnimValue.get()).booleanValue() && !flag) {
                        if (j1 <= 20) {
                            GL11.glTranslatef((float)((float)((double)(-(l + 4)) * EaseUtils.easeInExpo(1.0 - (double)((float)j1 + this.field_146247_f.field_71428_T.field_74281_c) / 20.0))), (float)0.0f, (float)0.0f);
                        }
                        if (j1 >= 180) {
                            GL11.glTranslatef((float)((float)((double)(-(l + 4)) * EaseUtils.easeInExpo((double)((float)j1 + this.field_146247_f.field_71428_T.field_74281_c - 180.0f) / 20.0))), (float)0.0f, (float)0.0f);
                        }
                    }
                    if (((Boolean)this.hud.chatRectValue.get()).booleanValue()) {
                        RenderUtils.drawRect((float)(i2 - 2), (float)(j2 - 9), (float)(i2 + l + 4), (float)j2, l1 / 2 << 24);
                        if (j2 - 9 < minH) {
                            minH = j2 - 9;
                        }
                        if (j2 > maxH) {
                            maxH = j2;
                        }
                    }
                    if (((Boolean)this.hud.chatRectRoundValue.get()).booleanValue()) {
                        RenderUtils.drawRoundedRect(i2 - 2, j2 - 9, i2 + l + 4, j2, 5.0f, l1 / 2 << 24);
                        if (j2 - 9 < minH) {
                            minH = j2 - 9;
                        }
                        if (j2 > maxH) {
                            maxH = j2;
                        }
                    }
                    if (((Boolean)this.hud.chatRectBlurValue.get()).booleanValue()) {
                        BlurUtils.blurAreaRounded(i2 - 2, j2 - 9, i2 + l + 4, j2, 5.0f, 10.0f);
                        if (j2 - 9 < minH) {
                            minH = j2 - 9;
                        }
                        if (j2 > maxH) {
                            maxH = j2;
                        }
                    }
                    GlStateManager.func_179147_l();
                    if (((Boolean)this.hud.chatRectValue.get()).booleanValue()) {
                        if (canFont) {
                            Fonts.font40.drawString(chatline.func_151461_a().func_150254_d(), i2, j2 - 8, new Color(255, 255, 255).getRGB());
                        } else {
                            this.field_146247_f.field_71466_p.func_175065_a(chatline.func_151461_a().func_150254_d(), (float)i2, (float)(j2 - 8), 0xFFFFFF + (l1 << 24), false);
                        }
                    } else if (canFont) {
                        Fonts.font40.func_175065_a(chatline.func_151461_a().func_150254_d(), i2, j2 - 8, new Color(255, 255, 255).getRGB(), true);
                    } else {
                        this.field_146247_f.field_71466_p.func_175063_a(chatline.func_151461_a().func_150254_d(), (float)i2, (float)(j2 - 8), 0xFFFFFF + (l1 << 24));
                    }
                    GlStateManager.func_179118_c();
                    GlStateManager.func_179084_k();
                    GL11.glPopMatrix();
                }
                if (((Boolean)this.hud.betterChatRectValue.get()).booleanValue() && minH < 900) {
                    RenderUtils.drawShadow(-2.0f, minH, MathHelper.func_76123_f((float)((float)this.func_146228_f() / f1)) + 4, maxH - minH);
                }
                if (flag) {
                    i1 = this.field_146247_f.field_71466_p.field_78288_b;
                    GlStateManager.func_179109_b((float)-3.0f, (float)0.0f, (float)0.0f);
                    int l2 = k * i1 + k;
                    j1 = j * i1 + j;
                    int j3 = this.field_146250_j * j1 / k;
                    int k1 = j1 * j1 / l2;
                    if (l2 != j1) {
                        l1 = j3 > 0 ? 170 : 96;
                        int l3 = this.field_146251_k ? 0xCC3333 : 0x3333AA;
                        RenderUtils.drawRect(0.0f, (float)(-j3), 2.0f, (float)(-j3 - k1), l3 + (l1 << 24));
                        RenderUtils.drawRect(2.0f, (float)(-j3), 1.0f, (float)(-j3 - k1), 0xCCCCCC + (l1 << 24));
                    }
                }
                GlStateManager.func_179121_F();
            }
        }
    }

    @Inject(method={"getChatComponent"}, at={@At(value="HEAD")}, cancellable=true)
    private void getChatComponent(int p_getChatComponent_1_, int p_getChatComponent_2_, CallbackInfoReturnable<IChatComponent> callbackInfo) {
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        if (hud.getState() && ((Boolean)hud.fontChatValue.get()).booleanValue()) {
            if (!this.func_146241_e()) {
                callbackInfo.setReturnValue(null);
            } else {
                ScaledResolution lvt_3_1_ = new ScaledResolution(this.field_146247_f);
                int lvt_4_1_ = lvt_3_1_.func_78325_e();
                float lvt_5_1_ = this.func_146244_h();
                int lvt_6_1_ = p_getChatComponent_1_ / lvt_4_1_ - 3;
                int lvt_7_1_ = p_getChatComponent_2_ / lvt_4_1_ - 27;
                lvt_6_1_ = MathHelper.func_76141_d((float)((float)lvt_6_1_ / lvt_5_1_));
                lvt_7_1_ = MathHelper.func_76141_d((float)((float)lvt_7_1_ / lvt_5_1_));
                if (lvt_6_1_ >= 0 && lvt_7_1_ >= 0) {
                    int lvt_8_1_ = Math.min(this.func_146232_i(), this.field_146253_i.size());
                    if (lvt_6_1_ <= MathHelper.func_76141_d((float)((float)this.func_146228_f() / this.func_146244_h())) && lvt_7_1_ < Fonts.font40.field_78288_b * lvt_8_1_ + lvt_8_1_) {
                        int lvt_9_1_ = lvt_7_1_ / Fonts.font40.field_78288_b + this.field_146250_j;
                        if (lvt_9_1_ >= 0 && lvt_9_1_ < this.field_146253_i.size()) {
                            ChatLine lvt_10_1_ = this.field_146253_i.get(lvt_9_1_);
                            int lvt_11_1_ = 0;
                            for (IChatComponent lvt_13_1_ : lvt_10_1_.func_151461_a()) {
                                if (!(lvt_13_1_ instanceof ChatComponentText) || (lvt_11_1_ += Fonts.font40.func_78256_a(GuiUtilRenderComponents.func_178909_a((String)((ChatComponentText)lvt_13_1_).func_150265_g(), (boolean)false))) <= lvt_6_1_) continue;
                                callbackInfo.setReturnValue(lvt_13_1_);
                                return;
                            }
                        }
                        callbackInfo.setReturnValue(null);
                    } else {
                        callbackInfo.setReturnValue(null);
                    }
                } else {
                    callbackInfo.setReturnValue(null);
                }
            }
        }
    }
}

