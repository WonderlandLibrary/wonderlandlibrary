/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiNewChat
extends Gui {
    private static final Logger logger = LogManager.getLogger();
    private final Minecraft mc;
    private final List sentMessages = Lists.newArrayList();
    private final List chatLines = Lists.newArrayList();
    private final List field_146253_i = Lists.newArrayList();
    private int scrollPos;
    private boolean isScrolled;
    private static final String __OBFID = "CL_00000669";

    public GuiNewChat(Minecraft mcIn) {
        this.mc = mcIn;
    }

    public void drawChat(int p_146230_1_) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int var2 = this.getLineCount();
            boolean var3 = false;
            int var4 = 0;
            int var5 = this.field_146253_i.size();
            float var6 = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (var5 > 0) {
                int var14;
                int var11;
                int var9;
                if (this.getChatOpen()) {
                    var3 = true;
                }
                float var7 = this.getChatScale();
                int var8 = MathHelper.ceiling_float_int((float)this.getChatWidth() / var7);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0f, 20.0f, 0.0f);
                GlStateManager.scale(var7, var7, 1.0f);
                for (var9 = 0; var9 + this.scrollPos < this.field_146253_i.size() && var9 < var2; ++var9) {
                    ChatLine var10 = (ChatLine)this.field_146253_i.get(var9 + this.scrollPos);
                    if (var10 == null || (var11 = p_146230_1_ - var10.getUpdatedCounter()) >= 200 && !var3) continue;
                    double var12 = (double)var11 / 200.0;
                    var12 = 1.0 - var12;
                    var12 *= 10.0;
                    var12 = MathHelper.clamp_double(var12, 0.0, 1.0);
                    var12 *= var12;
                    var14 = (int)(255.0 * var12);
                    if (var3) {
                        var14 = 255;
                    }
                    var14 = (int)((float)var14 * var6);
                    ++var4;
                    if (var14 <= 3) continue;
                    int var15 = 0;
                    int var16 = -var9 * 9;
                    GuiNewChat.drawRect(var15, var16 - 9, var15 + var8 + 4, var16, var14 / 2 << 24);
                    String var17 = var10.getChatComponent().getFormattedText();
                    GlStateManager.enableBlend();
                    this.mc.fontRendererObj.drawStringWithShadow(var17, var15, var16 - 8, 0xFFFFFF + (var14 << 24));
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                if (var3) {
                    var9 = this.mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0f, 0.0f, 0.0f);
                    int var18 = var5 * var9 + var5;
                    var11 = var4 * var9 + var4;
                    int var19 = this.scrollPos * var11 / var5;
                    int var13 = var11 * var11 / var18;
                    if (var18 != var11) {
                        var14 = var19 > 0 ? 170 : 96;
                        int var20 = this.isScrolled ? 0xCC3333 : 0x3333AA;
                        GuiNewChat.drawRect(0.0, -var19, 2.0, -var19 - var13, var20 + (var14 << 24));
                        GuiNewChat.drawRect(2.0, -var19, 1.0, -var19 - var13, 0xCCCCCC + (var14 << 24));
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

    public void printChatMessage(IChatComponent p_146227_1_) {
        this.printChatMessageWithOptionalDeletion(p_146227_1_, 0);
    }

    public void printChatMessageWithOptionalDeletion(IChatComponent p_146234_1_, int p_146234_2_) {
        this.setChatLine(p_146234_1_, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
        logger.info("[CHAT] " + p_146234_1_.getUnformattedText());
    }

    private void setChatLine(IChatComponent p_146237_1_, int p_146237_2_, int p_146237_3_, boolean p_146237_4_) {
        if (p_146237_2_ != 0) {
            this.deleteChatLine(p_146237_2_);
        }
        int var5 = MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale());
        List var6 = GuiUtilRenderComponents.func_178908_a(p_146237_1_, var5, this.mc.fontRendererObj, false, false);
        boolean var7 = this.getChatOpen();
        for (IChatComponent var9 : var6) {
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
            ChatLine var2 = (ChatLine)this.chatLines.get(var1);
            this.setChatLine(var2.getChatComponent(), var2.getChatLineID(), var2.getUpdatedCounter(), true);
        }
    }

    public List getSentMessages() {
        return this.sentMessages;
    }

    public void addToSentMessages(String p_146239_1_) {
        if (this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(p_146239_1_)) {
            this.sentMessages.add(p_146239_1_);
        }
    }

    public void resetScroll() {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    public void scroll(int p_146229_1_) {
        this.scrollPos += p_146229_1_;
        int var2 = this.field_146253_i.size();
        if (this.scrollPos > var2 - this.getLineCount()) {
            this.scrollPos = var2 - this.getLineCount();
        }
        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }

    public IChatComponent getChatComponent(int p_146236_1_, int p_146236_2_) {
        if (!this.getChatOpen()) {
            return null;
        }
        ScaledResolution var3 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int var4 = var3.getScaleFactor();
        float var5 = this.getChatScale();
        int var6 = p_146236_1_ / var4 - 3;
        int var7 = p_146236_2_ / var4 - 27;
        var6 = MathHelper.floor_float((float)var6 / var5);
        var7 = MathHelper.floor_float((float)var7 / var5);
        if (var6 >= 0 && var7 >= 0) {
            int var8 = Math.min(this.getLineCount(), this.field_146253_i.size());
            if (var6 <= MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale()) && var7 < this.mc.fontRendererObj.FONT_HEIGHT * var8 + var8) {
                int var9 = var7 / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
                if (var9 >= 0 && var9 < this.field_146253_i.size()) {
                    ChatLine var10 = (ChatLine)this.field_146253_i.get(var9);
                    int var11 = 0;
                    for (IChatComponent var13 : var10.getChatComponent()) {
                        if (!(var13 instanceof ChatComponentText) || (var11 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)var13).getChatComponentText_TextValue(), false))) <= var6) continue;
                        return var13;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public boolean getChatOpen() {
        return this.mc.currentScreen instanceof GuiChat;
    }

    public void deleteChatLine(int p_146242_1_) {
        ChatLine var3;
        Iterator var2 = this.field_146253_i.iterator();
        while (var2.hasNext()) {
            var3 = (ChatLine)var2.next();
            if (var3.getChatLineID() != p_146242_1_) continue;
            var2.remove();
        }
        var2 = this.chatLines.iterator();
        while (var2.hasNext()) {
            var3 = (ChatLine)var2.next();
            if (var3.getChatLineID() != p_146242_1_) continue;
            var2.remove();
            break;
        }
    }

    public int getChatWidth() {
        return GuiNewChat.calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }

    public int getChatHeight() {
        return GuiNewChat.calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }

    public static int calculateChatboxWidth(float p_146233_0_) {
        int var1 = 320;
        int var2 = 40;
        return MathHelper.floor_float(p_146233_0_ * (float)(var1 - var2) + (float)var2);
    }

    public static int calculateChatboxHeight(float p_146243_0_) {
        int var1 = 180;
        int var2 = 20;
        return MathHelper.floor_float(p_146243_0_ * (float)(var1 - var2) + (float)var2);
    }

    public int getLineCount() {
        return this.getChatHeight() / 9;
    }
}

