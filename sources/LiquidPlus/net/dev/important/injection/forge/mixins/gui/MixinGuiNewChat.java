/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ChatLine
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiNewChat
 *  net.minecraft.client.gui.GuiUtilRenderComponents
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.player.EntityPlayer$EnumChatVisibility
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.MathHelper
 */
package net.dev.important.injection.forge.mixins.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import net.dev.important.Client;
import net.dev.important.modules.module.modules.misc.Patcher;
import net.dev.important.modules.module.modules.render.HUD;
import net.dev.important.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiNewChat.class})
public abstract class MixinGuiNewChat {
    private float displayPercent;
    private float animationPercent = 0.0f;
    private int lineBeingDrawn;
    private int newLines;
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
    @Shadow
    @Final
    private List<ChatLine> field_146252_h;
    private String lastMessage;
    private int sameMessageAmount;
    private int line;
    private HUD hud;
    private final HashMap<String, String> stringCache = new HashMap();

    @Shadow
    public abstract int func_146232_i();

    @Shadow
    public abstract boolean func_146241_e();

    @Shadow
    public abstract float func_146244_h();

    @Shadow
    public abstract int func_146228_f();

    @Shadow
    public abstract void func_146242_c(int var1);

    @Shadow
    public abstract void func_146229_b(int var1);

    @Shadow
    public abstract void func_146234_a(IChatComponent var1, int var2);

    private void checkHud() {
        if (this.hud == null) {
            this.hud = (HUD)Client.moduleManager.getModule(HUD.class);
        }
    }

    @Overwrite
    public void func_146227_a(IChatComponent chatComponent) {
        this.checkHud();
        if (!((Boolean)this.hud.getChatCombineValue().get()).booleanValue()) {
            this.func_146234_a(chatComponent, this.line);
            return;
        }
        String text = this.fixString(chatComponent.func_150254_d());
        if (text.equals(this.lastMessage)) {
            Minecraft.func_71410_x().field_71456_v.func_146158_b().func_146242_c(this.line);
            ++this.sameMessageAmount;
            this.lastMessage = text;
            chatComponent.func_150258_a(ChatFormatting.WHITE + " (x" + this.sameMessageAmount + ")");
        } else {
            this.sameMessageAmount = 1;
            this.lastMessage = text;
        }
        ++this.line;
        if (this.line > 256) {
            this.line = 0;
        }
        this.func_146234_a(chatComponent, this.line);
    }

    @Inject(method={"printChatMessageWithOptionalDeletion"}, at={@At(value="HEAD")})
    private void resetPercentage(CallbackInfo ci) {
        this.displayPercent = 0.0f;
    }

    @Overwrite
    public void func_146230_a(int updateCounter) {
        boolean canFont;
        this.checkHud();
        boolean bl = canFont = this.hud.getState() && (Boolean)this.hud.getFontChatValue().get() != false;
        if (((Boolean)Patcher.chatPosition.get()).booleanValue()) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)0.0f, (float)-12.0f, (float)0.0f);
        }
        if (this.field_146247_f.field_71474_y.field_74343_n != EntityPlayer.EnumChatVisibility.HIDDEN) {
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
                if (this.field_146251_k || !this.hud.getState() || !((Boolean)this.hud.getChatAnimationValue().get()).booleanValue()) {
                    this.displayPercent = 1.0f;
                } else if (this.displayPercent < 1.0f) {
                    this.displayPercent += ((Float)this.hud.getChatAnimationSpeedValue().get()).floatValue() / 10.0f * (float)RenderUtils.deltaTime;
                    this.displayPercent = MathHelper.func_76131_a((float)this.displayPercent, (float)0.0f, (float)1.0f);
                }
                float t = this.displayPercent;
                this.animationPercent = MathHelper.func_76131_a((float)(1.0f - (t -= 1.0f) * t * t * t), (float)0.0f, (float)1.0f);
                float f1 = this.func_146244_h();
                int l = MathHelper.func_76123_f((float)((float)this.func_146228_f() / f1));
                GlStateManager.func_179094_E();
                if (this.hud.getState() && ((Boolean)this.hud.getChatAnimationValue().get()).booleanValue()) {
                    GlStateManager.func_179109_b((float)0.0f, (float)((1.0f - this.animationPercent) * 9.0f * this.func_146244_h()), (float)0.0f);
                }
                GlStateManager.func_179109_b((float)2.0f, (float)20.0f, (float)0.0f);
                GlStateManager.func_179152_a((float)f1, (float)f1, (float)1.0f);
                for (i1 = 0; i1 + this.field_146250_j < this.field_146253_i.size() && i1 < i; ++i1) {
                    ChatLine chatline = this.field_146253_i.get(i1 + this.field_146250_j);
                    this.lineBeingDrawn = i1 + this.field_146250_j;
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
                    int i2 = 0;
                    int j2 = -i1 * 9;
                    if (this.hud.getState() && ((Boolean)this.hud.getChatRectValue().get()).booleanValue()) {
                        if (((Boolean)this.hud.getChatAnimationValue().get()).booleanValue() && this.lineBeingDrawn <= this.newLines && !flag) {
                            RenderUtils.drawRect((float)i2, (float)(j2 - 9), (float)(i2 + l + 4), (float)j2, new Color(0.0f, 0.0f, 0.0f, this.animationPercent * ((float)d0 / 2.0f)).getRGB());
                        } else {
                            RenderUtils.drawRect((float)i2, (float)(j2 - 9), (float)(i2 + l + 4), (float)j2, l1 / 2 << 24);
                        }
                    }
                    GlStateManager.func_179117_G();
                    GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    String s = this.fixString(chatline.func_151461_a().func_150254_d());
                    GlStateManager.func_179147_l();
                    if (this.hud.getState() && ((Boolean)this.hud.getChatAnimationValue().get()).booleanValue() && this.lineBeingDrawn <= this.newLines) {
                        (canFont ? (FontRenderer)this.hud.getFontType().get() : this.field_146247_f.field_71466_p).func_175065_a(s, (float)i2, (float)(j2 - 8), new Color(1.0f, 1.0f, 1.0f, this.animationPercent * (float)d0).getRGB(), true);
                    } else {
                        (canFont ? (FontRenderer)this.hud.getFontType().get() : this.field_146247_f.field_71466_p).func_175065_a(s, (float)i2, (float)(j2 - 8), 0xFFFFFF + (l1 << 24), true);
                    }
                    GlStateManager.func_179118_c();
                    GlStateManager.func_179084_k();
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
        if (((Boolean)Patcher.chatPosition.get()).booleanValue()) {
            GlStateManager.func_179121_F();
        }
    }

    private String fixString(String str) {
        if (this.stringCache.containsKey(str)) {
            return this.stringCache.get(str);
        }
        str = str.replaceAll("\uf8ff", "");
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c > '\uff01' && c < '\uff60') {
                sb.append(Character.toChars(c - 65248));
                continue;
            }
            sb.append(c);
        }
        String result = sb.toString();
        this.stringCache.put(str, result);
        return result;
    }

    @ModifyVariable(method={"setChatLine"}, at=@At(value="STORE"), ordinal=0)
    private List<IChatComponent> setNewLines(List<IChatComponent> original) {
        this.newLines = original.size() - 1;
        return original;
    }

    @Overwrite
    public IChatComponent func_146236_a(int p_146236_1_, int p_146236_2_) {
        boolean flagFont;
        this.checkHud();
        boolean bl = flagFont = this.hud.getState() && (Boolean)this.hud.getFontChatValue().get() != false;
        if (!this.func_146241_e()) {
            return null;
        }
        ScaledResolution sc = new ScaledResolution(this.field_146247_f);
        int scaleFactor = sc.func_78325_e();
        float chatScale = this.func_146244_h();
        int mX = p_146236_1_ / scaleFactor - 3;
        int mY = p_146236_2_ / scaleFactor - 27 - ((Boolean)Patcher.chatPosition.get() != false ? 12 : 0);
        mX = MathHelper.func_76141_d((float)((float)mX / chatScale));
        mY = MathHelper.func_76141_d((float)((float)mY / chatScale));
        if (mX >= 0 && mY >= 0) {
            int lineCount = Math.min(this.func_146232_i(), this.field_146253_i.size());
            if (mX <= MathHelper.func_76141_d((float)((float)this.func_146228_f() / this.func_146244_h())) && mY < (flagFont ? (FontRenderer)this.hud.getFontType().get() : this.field_146247_f.field_71466_p).field_78288_b * lineCount + lineCount) {
                int line = mY / (flagFont ? (FontRenderer)this.hud.getFontType().get() : this.field_146247_f.field_71466_p).field_78288_b + this.field_146250_j;
                if (line >= 0 && line < this.field_146253_i.size()) {
                    ChatLine chatLine = this.field_146253_i.get(line);
                    int maxWidth = 0;
                    for (IChatComponent iterator2 : chatLine.func_151461_a()) {
                        if (!(iterator2 instanceof ChatComponentText) || (maxWidth += (flagFont ? (FontRenderer)this.hud.getFontType().get() : this.field_146247_f.field_71466_p).func_78256_a(GuiUtilRenderComponents.func_178909_a((String)((ChatComponentText)iterator2).func_150265_g(), (boolean)false))) <= mX) continue;
                        return iterator2;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }
}

