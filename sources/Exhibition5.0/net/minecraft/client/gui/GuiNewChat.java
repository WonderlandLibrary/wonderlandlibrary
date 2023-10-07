// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ChatComponentText;
import java.util.Iterator;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class GuiNewChat extends Gui
{
    private static final Logger logger;
    private final Minecraft mc;
    private final List sentMessages;
    private final List chatLines;
    private final List field_146253_i;
    private int scrollPos;
    private boolean isScrolled;
    private static final String __OBFID = "CL_00000669";
    
    public GuiNewChat(final Minecraft mcIn) {
        this.sentMessages = Lists.newArrayList();
        this.chatLines = Lists.newArrayList();
        this.field_146253_i = Lists.newArrayList();
        this.mc = mcIn;
    }
    
    public void drawChat(final int p_146230_1_) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            final int var2 = this.getLineCount();
            boolean var3 = false;
            int var4 = 0;
            final int var5 = this.field_146253_i.size();
            final float var6 = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (var5 > 0) {
                if (this.getChatOpen()) {
                    var3 = true;
                }
                final float var7 = this.getChatScale();
                final int var8 = MathHelper.ceiling_float_int(this.getChatWidth() / var7);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0f, 20.0f, 0.0f);
                GlStateManager.scale(var7, var7, 1.0f);
                for (int var9 = 0; var9 + this.scrollPos < this.field_146253_i.size() && var9 < var2; ++var9) {
                    final ChatLine var10 = this.field_146253_i.get(var9 + this.scrollPos);
                    if (var10 != null) {
                        final int var11 = p_146230_1_ - var10.getUpdatedCounter();
                        if (var11 < 200 || var3) {
                            double var12 = var11 / 200.0;
                            var12 = 1.0 - var12;
                            var12 *= 10.0;
                            var12 = MathHelper.clamp_double(var12, 0.0, 1.0);
                            var12 *= var12;
                            int var13 = (int)(255.0 * var12);
                            if (var3) {
                                var13 = 255;
                            }
                            var13 *= (int)var6;
                            ++var4;
                            if (var13 > 3) {
                                final byte var14 = 0;
                                final int var15 = -var9 * 9;
                                Gui.drawRect(var14, var15 - 9, var14 + var8 + 4, var15, var13 / 2 << 24);
                                final String var16 = var10.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                this.mc.fontRendererObj.drawStringWithShadow(var16, var14, var15 - 8, 16777215 + (var13 << 24));
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }
                if (var3) {
                    final int var9 = this.mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0f, 0.0f, 0.0f);
                    final int var17 = var5 * var9 + var5;
                    final int var11 = var4 * var9 + var4;
                    final int var18 = this.scrollPos * var11 / var5;
                    final int var19 = var11 * var11 / var17;
                    if (var17 != var11) {
                        final int var13 = (var18 > 0) ? 170 : 96;
                        final int var20 = this.isScrolled ? 13382451 : 3355562;
                        Gui.drawRect(0, -var18, 2, -var18 - var19, var20 + (var13 << 24));
                        Gui.drawRect(2, -var18, 1, -var18 - var19, 13421772 + (var13 << 24));
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }
    
    public void clearChatMessages() {
        this.field_146253_i.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }
    
    public void printChatMessage(final IChatComponent p_146227_1_) {
        this.printChatMessageWithOptionalDeletion(p_146227_1_, 0);
    }
    
    public void printChatMessageWithOptionalDeletion(final IChatComponent p_146234_1_, final int p_146234_2_) {
        this.setChatLine(p_146234_1_, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
        GuiNewChat.logger.info("[CHAT] " + p_146234_1_.getUnformattedText());
    }
    
    private void setChatLine(final IChatComponent p_146237_1_, final int p_146237_2_, final int p_146237_3_, final boolean p_146237_4_) {
        if (p_146237_2_ != 0) {
            this.deleteChatLine(p_146237_2_);
        }
        final int var5 = MathHelper.floor_float(this.getChatWidth() / this.getChatScale());
        final List var6 = GuiUtilRenderComponents.func_178908_a(p_146237_1_, var5, this.mc.fontRendererObj, false, false);
        final boolean var7 = this.getChatOpen();
        for (final IChatComponent var9 : var6) {
            if (var7 && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }
            this.field_146253_i.add(0, new ChatLine(p_146237_3_, var9, p_146237_2_));
        }
        while (this.field_146253_i.size() > 100) {
            this.field_146253_i.remove(this.field_146253_i.size() - 1);
        }
        if (!p_146237_4_) {
            this.chatLines.add(0, new ChatLine(p_146237_3_, p_146237_1_, p_146237_2_));
            while (this.chatLines.size() > 100) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }
    
    public void refreshChat() {
        this.field_146253_i.clear();
        this.resetScroll();
        for (int var1 = this.chatLines.size() - 1; var1 >= 0; --var1) {
            final ChatLine var2 = this.chatLines.get(var1);
            this.setChatLine(var2.getChatComponent(), var2.getChatLineID(), var2.getUpdatedCounter(), true);
        }
    }
    
    public List getSentMessages() {
        return this.sentMessages;
    }
    
    public void addToSentMessages(final String p_146239_1_) {
        if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(p_146239_1_)) {
            this.sentMessages.add(p_146239_1_);
        }
    }
    
    public void resetScroll() {
        this.scrollPos = 0;
        this.isScrolled = false;
    }
    
    public void scroll(final int p_146229_1_) {
        this.scrollPos += p_146229_1_;
        final int var2 = this.field_146253_i.size();
        if (this.scrollPos > var2 - this.getLineCount()) {
            this.scrollPos = var2 - this.getLineCount();
        }
        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }
    
    public IChatComponent getChatComponent(final int p_146236_1_, final int p_146236_2_) {
        if (!this.getChatOpen()) {
            return null;
        }
        final ScaledResolution var3 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        final int var4 = var3.getScaleFactor();
        final float var5 = this.getChatScale();
        int var6 = p_146236_1_ / var4 - 3;
        int var7 = p_146236_2_ / var4 - 27;
        var6 = MathHelper.floor_float(var6 / var5);
        var7 = MathHelper.floor_float(var7 / var5);
        if (var6 < 0 || var7 < 0) {
            return null;
        }
        final int var8 = Math.min(this.getLineCount(), this.field_146253_i.size());
        if (var6 <= MathHelper.floor_float(this.getChatWidth() / this.getChatScale()) && var7 < this.mc.fontRendererObj.FONT_HEIGHT * var8 + var8) {
            final int var9 = var7 / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
            if (var9 >= 0 && var9 < this.field_146253_i.size()) {
                final ChatLine var10 = this.field_146253_i.get(var9);
                int var11 = 0;
                for (final IChatComponent var13 : var10.getChatComponent()) {
                    if (var13 instanceof ChatComponentText) {
                        var11 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)var13).getChatComponentText_TextValue(), false));
                        if (var11 > var6) {
                            return var13;
                        }
                        continue;
                    }
                }
            }
            return null;
        }
        return null;
    }
    
    public boolean getChatOpen() {
        return this.mc.currentScreen instanceof GuiChat;
    }
    
    public void deleteChatLine(final int p_146242_1_) {
        Iterator var2 = this.field_146253_i.iterator();
        while (var2.hasNext()) {
            final ChatLine var3 = var2.next();
            if (var3.getChatLineID() == p_146242_1_) {
                var2.remove();
            }
        }
        var2 = this.chatLines.iterator();
        while (var2.hasNext()) {
            final ChatLine var3 = var2.next();
            if (var3.getChatLineID() == p_146242_1_) {
                var2.remove();
                break;
            }
        }
    }
    
    public int getChatWidth() {
        return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }
    
    public int getChatHeight() {
        return calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }
    
    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }
    
    public static int calculateChatboxWidth(final float p_146233_0_) {
        final short var1 = 320;
        final byte var2 = 40;
        return MathHelper.floor_float(p_146233_0_ * (var1 - var2) + var2);
    }
    
    public static int calculateChatboxHeight(final float p_146243_0_) {
        final short var1 = 180;
        final byte var2 = 20;
        return MathHelper.floor_float(p_146243_0_ * (var1 - var2) + var2);
    }
    
    public int getLineCount() {
        return this.getChatHeight() / 9;
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
