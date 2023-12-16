// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.ChatComponentText;
import java.util.Iterator;
import moonsense.event.impl.SCChatEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import moonsense.ui.utils.GuiUtils;
import moonsense.utils.ColorObject;
import moonsense.features.SCModule;
import moonsense.features.modules.type.hud.ChatModule;
import moonsense.config.ModuleConfig;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
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
    private float percentComplete;
    private int newLines;
    private long prevMillis;
    private float animationPercent;
    private int lineBeingDrawn;
    private String lastMessage;
    private int sameMessageAmount;
    private int line;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public GuiNewChat(final Minecraft mcIn) {
        this.sentMessages = Lists.newArrayList();
        this.chatLines = Lists.newArrayList();
        this.field_146253_i = Lists.newArrayList();
        this.lastMessage = "";
        this.mc = mcIn;
    }
    
    private void updatePercentage(final long diff) {
        if (this.percentComplete < 1.0f) {
            this.percentComplete += 0.004f * diff;
        }
        this.percentComplete = MathHelper.clamp(this.percentComplete, 0.0f, 1.0f);
    }
    
    public void drawChat(final int p_146230_1_) {
        final long current = System.currentTimeMillis();
        final long diff = current - this.prevMillis;
        this.prevMillis = current;
        this.updatePercentage(diff);
        float t = this.percentComplete;
        this.animationPercent = MathHelper.clamp(1.0f - --t * t * t * t, 0.0f, 1.0f);
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
                float y = 20.0f;
                if (ModuleConfig.INSTANCE.isEnabled(ChatModule.INSTANCE) && ChatModule.INSTANCE.smoothChat.getBoolean() && !this.isScrolled) {
                    y += (9.0f - 9.0f * this.animationPercent) * this.getChatScale();
                }
                if (ModuleConfig.INSTANCE.isEnabled(ChatModule.INSTANCE) && ChatModule.INSTANCE.chatHeightFix.getBoolean()) {
                    y -= 12.0f;
                }
                GlStateManager.translate(2.0f, y, 0.0f);
                GlStateManager.scale(var7, var7, 1.0f);
                for (int var9 = 0; var9 + this.scrollPos < this.field_146253_i.size() && var9 < var2; ++var9) {
                    final List field_146253_i = this.field_146253_i;
                    final int lineBeingDrawn = var9 + this.scrollPos;
                    this.lineBeingDrawn = lineBeingDrawn;
                    final ChatLine var10 = field_146253_i.get(lineBeingDrawn);
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
                                final ColorObject clr = (ColorObject)ChatModule.INSTANCE.color.getObject();
                                Gui.drawRect(var14, var15 - 9, var14 + var8 + 4, var15, ModuleConfig.INSTANCE.isEnabled(ChatModule.INSTANCE) ? clr.getColor() : (var13 / 2 << 24));
                                final String var16 = var10.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                final int original = 16777215 + (var13 << 24);
                                int opac;
                                if (ModuleConfig.INSTANCE.isEnabled(ChatModule.INSTANCE) && ChatModule.INSTANCE.smoothChat.getBoolean() && this.lineBeingDrawn <= this.newLines) {
                                    int opacity = original >> 24 & 0xFF;
                                    opacity *= (int)this.animationPercent;
                                    opac = ((original & 0xFFFFFF) | opacity << 24);
                                }
                                else {
                                    opac = original;
                                }
                                GuiUtils.drawString(var16, var14, (float)(var15 - 8), opac, !ModuleConfig.INSTANCE.isEnabled(ChatModule.INSTANCE) || ChatModule.INSTANCE.shadow.getBoolean());
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
        final IChatComponent message = p_146227_1_;
        if (ModuleConfig.INSTANCE.isEnabled(ChatModule.INSTANCE) && ChatModule.INSTANCE.stackSpamMessages.getBoolean()) {
            if (message.getUnformattedText().equals(this.lastMessage)) {
                Minecraft.getMinecraft().ingameGUI.getChatGUI().deleteChatLine(this.line);
                ++this.sameMessageAmount;
                this.lastMessage = message.getUnformattedText();
                message.appendText(EnumChatFormatting.RED + " (" + this.sameMessageAmount + "x)");
            }
            else {
                this.sameMessageAmount = 1;
                this.lastMessage = message.getUnformattedText();
            }
            ++this.line;
            if (this.line > 256) {
                this.line = 0;
            }
            this.printChatMessageWithOptionalDeletion(message, this.line);
        }
        else {
            this.printChatMessageWithOptionalDeletion(message, 0);
        }
    }
    
    public void printChatMessageWithOptionalDeletion(final IChatComponent p_146234_1_, final int p_146234_2_) {
        this.percentComplete = 0.0f;
        final SCChatEvent e = new SCChatEvent(p_146234_1_, p_146234_2_);
        e.call();
        if (e.isCancelled()) {
            return;
        }
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
        this.newLines = this.field_146253_i.size() - 1;
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
    
    public IChatComponent getChatComponent(final int mouseX, int mouseY) {
        if (!this.getChatOpen()) {
            return null;
        }
        final ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        final int scaleFactor = sr.getScaleFactor();
        final float chatScale = this.getChatScale();
        if (ModuleConfig.INSTANCE.isEnabled(ChatModule.INSTANCE) && ChatModule.INSTANCE.chatHeightFix.getBoolean()) {
            mouseY -= 24;
        }
        int newX = mouseX / scaleFactor - 3;
        int newY = mouseY / scaleFactor - 27;
        newX = MathHelper.floor_float(newX / chatScale);
        newY = MathHelper.floor_float(newY / chatScale);
        if (newX < 0 || newY < 0) {
            return null;
        }
        final int var8 = Math.min(this.getLineCount(), this.field_146253_i.size());
        if (newX <= MathHelper.floor_float(this.getChatWidth() / this.getChatScale()) && newY < this.mc.fontRendererObj.FONT_HEIGHT * var8 + var8) {
            final int var9 = newY / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
            if (var9 >= 0 && var9 < this.field_146253_i.size()) {
                final ChatLine var10 = this.field_146253_i.get(var9);
                int var11 = 0;
                for (final IChatComponent var13 : var10.getChatComponent()) {
                    if (var13 instanceof ChatComponentText) {
                        var11 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)var13).getChatComponentText_TextValue(), false));
                        if (var11 > newX) {
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
}
