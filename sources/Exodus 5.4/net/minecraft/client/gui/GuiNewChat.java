/*
 * Decompiled with CFR 0.152.
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
    private int scrollPos;
    private final List<ChatLine> chatLines;
    private boolean isScrolled;
    private final List<String> sentMessages = Lists.newArrayList();
    private final List<ChatLine> field_146253_i;
    private final Minecraft mc;

    public int getChatHeight() {
        return GuiNewChat.calculateChatboxHeight(this.getChatOpen() ? Minecraft.gameSettings.chatHeightFocused : Minecraft.gameSettings.chatHeightUnfocused);
    }

    public GuiNewChat(Minecraft minecraft) {
        this.chatLines = Lists.newArrayList();
        this.field_146253_i = Lists.newArrayList();
        this.mc = minecraft;
    }

    public void clearChatMessages() {
        this.field_146253_i.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }

    public boolean getChatOpen() {
        return this.mc.currentScreen instanceof GuiChat;
    }

    public void printChatMessageWithOptionalDeletion(IChatComponent iChatComponent, int n) {
        this.setChatLine(iChatComponent, n, this.mc.ingameGUI.getUpdateCounter(), false);
        logger.info("[CHAT] " + iChatComponent.getUnformattedText());
    }

    private void setChatLine(IChatComponent iChatComponent, int n, int n2, boolean bl) {
        if (n != 0) {
            this.deleteChatLine(n);
        }
        int n3 = MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale());
        List<IChatComponent> list = GuiUtilRenderComponents.func_178908_a(iChatComponent, n3, Minecraft.fontRendererObj, false, false);
        boolean bl2 = this.getChatOpen();
        for (IChatComponent iChatComponent2 : list) {
            if (bl2 && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }
            this.field_146253_i.add(0, new ChatLine(n2, iChatComponent2, n));
        }
        while (this.field_146253_i.size() > 100) {
            this.field_146253_i.remove(this.field_146253_i.size() - 1);
        }
        if (!bl) {
            this.chatLines.add(0, new ChatLine(n2, iChatComponent, n));
            while (this.chatLines.size() > 100) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }

    public void drawChat(int n) {
        if (Minecraft.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int n2 = this.getLineCount();
            boolean bl = false;
            int n3 = 0;
            int n4 = this.field_146253_i.size();
            float f = Minecraft.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (n4 > 0) {
                int n5;
                int n6;
                int n7;
                if (this.getChatOpen()) {
                    bl = true;
                }
                float f2 = this.getChatScale();
                int n8 = MathHelper.ceiling_float_int((float)this.getChatWidth() / f2);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0f, 20.0f, 0.0f);
                GlStateManager.scale(f2, f2, 1.0f);
                int n9 = 0;
                while (n9 + this.scrollPos < this.field_146253_i.size() && n9 < n2) {
                    ChatLine chatLine = this.field_146253_i.get(n9 + this.scrollPos);
                    if (chatLine != null && ((n7 = n - chatLine.getUpdatedCounter()) < 200 || bl)) {
                        double d = (double)n7 / 200.0;
                        d = 1.0 - d;
                        d *= 10.0;
                        d = MathHelper.clamp_double(d, 0.0, 1.0);
                        d *= d;
                        n6 = (int)(255.0 * d);
                        if (bl) {
                            n6 = 255;
                        }
                        n6 = (int)((float)n6 * f);
                        ++n3;
                        if (n6 > 3) {
                            n5 = 0;
                            int n10 = -n9 * 9;
                            GuiNewChat.drawRect(n5, n10 - 9, n5 + n8 + 4, n10, n6 / 2 << 24);
                            String string = chatLine.getChatComponent().getFormattedText();
                            GlStateManager.enableBlend();
                            Minecraft.fontRendererObj.drawStringWithShadow(string, n5, n10 - 8, 0xFFFFFF + (n6 << 24));
                            GlStateManager.disableAlpha();
                            GlStateManager.disableBlend();
                        }
                    }
                    ++n9;
                }
                if (bl) {
                    n9 = Minecraft.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0f, 0.0f, 0.0f);
                    int n11 = n4 * n9 + n4;
                    n7 = n3 * n9 + n3;
                    int n12 = this.scrollPos * n7 / n4;
                    int n13 = n7 * n7 / n11;
                    if (n11 != n7) {
                        n6 = n12 > 0 ? 170 : 96;
                        n5 = this.isScrolled ? 0xCC3333 : 0x3333AA;
                        GuiNewChat.drawRect(0.0, -n12, 2.0, -n12 - n13, n5 + (n6 << 24));
                        GuiNewChat.drawRect(2.0, -n12, 1.0, -n12 - n13, 0xCCCCCC + (n6 << 24));
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }

    public float getChatScale() {
        return Minecraft.gameSettings.chatScale;
    }

    public void resetScroll() {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    public IChatComponent getChatComponent(int n, int n2) {
        if (!this.getChatOpen()) {
            return null;
        }
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int n3 = scaledResolution.getScaleFactor();
        float f = this.getChatScale();
        int n4 = n / n3 - 3;
        int n5 = n2 / n3 - 27;
        n4 = MathHelper.floor_float((float)n4 / f);
        n5 = MathHelper.floor_float((float)n5 / f);
        if (n4 >= 0 && n5 >= 0) {
            int n6 = Math.min(this.getLineCount(), this.field_146253_i.size());
            if (n4 <= MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale()) && n5 < Minecraft.fontRendererObj.FONT_HEIGHT * n6 + n6) {
                int n7 = n5 / Minecraft.fontRendererObj.FONT_HEIGHT + this.scrollPos;
                if (n7 >= 0 && n7 < this.field_146253_i.size()) {
                    ChatLine chatLine = this.field_146253_i.get(n7);
                    int n8 = 0;
                    for (IChatComponent iChatComponent : chatLine.getChatComponent()) {
                        if (!(iChatComponent instanceof ChatComponentText) || (n8 += Minecraft.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)iChatComponent).getChatComponentText_TextValue(), false))) <= n4) continue;
                        return iChatComponent;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public List<String> getSentMessages() {
        return this.sentMessages;
    }

    public static int calculateChatboxHeight(float f) {
        int n = 180;
        int n2 = 20;
        return MathHelper.floor_float(f * (float)(n - n2) + (float)n2);
    }

    public void refreshChat() {
        this.field_146253_i.clear();
        this.resetScroll();
        int n = this.chatLines.size() - 1;
        while (n >= 0) {
            ChatLine chatLine = this.chatLines.get(n);
            this.setChatLine(chatLine.getChatComponent(), chatLine.getChatLineID(), chatLine.getUpdatedCounter(), true);
            --n;
        }
    }

    public void deleteChatLine(int n) {
        ChatLine chatLine;
        Iterator<ChatLine> iterator = this.field_146253_i.iterator();
        while (iterator.hasNext()) {
            chatLine = iterator.next();
            if (chatLine.getChatLineID() != n) continue;
            iterator.remove();
        }
        iterator = this.chatLines.iterator();
        while (iterator.hasNext()) {
            chatLine = iterator.next();
            if (chatLine.getChatLineID() != n) continue;
            iterator.remove();
            break;
        }
    }

    public void printChatMessage(IChatComponent iChatComponent) {
        this.printChatMessageWithOptionalDeletion(iChatComponent, 0);
    }

    public void scroll(int n) {
        this.scrollPos += n;
        int n2 = this.field_146253_i.size();
        if (this.scrollPos > n2 - this.getLineCount()) {
            this.scrollPos = n2 - this.getLineCount();
        }
        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }

    public int getLineCount() {
        return this.getChatHeight() / 9;
    }

    public void addToSentMessages(String string) {
        if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(string)) {
            this.sentMessages.add(string);
        }
    }

    public int getChatWidth() {
        return GuiNewChat.calculateChatboxWidth(Minecraft.gameSettings.chatWidth);
    }

    public static int calculateChatboxWidth(float f) {
        int n = 320;
        int n2 = 40;
        return MathHelper.floor_float(f * (float)(n - n2) + (float)n2);
    }
}

