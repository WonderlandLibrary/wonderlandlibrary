package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.modules.visual.Shadow;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
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
    private final List<String> sentMessages = Lists.newArrayList();
    private final List<ChatLine> chatLines = Lists.newArrayList();
    private final List<ChatLine> drawnChatLines = Lists.newArrayList();
    private int scrollPos;
    private boolean isScrolled;
    public static boolean animatedChatOpen;

    public GuiNewChat(Minecraft mcIn) {
        this.mc = mcIn;
    }

    public void drawChat(int updateCounter) {
        int finalColor;
        int color = Aqua.setmgr.getSetting("HUDColor").getColor();
        int secondColor = Aqua.setmgr.getSetting("ArraylistColor").getColor();
        int n = finalColor = Aqua.setmgr.getSetting("CustomChatFade").isState() ? Arraylist.getGradientOffset((Color)new Color(color), (Color)new Color(secondColor), (double)15.0).getRGB() : color;
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = this.getLineCount();
            boolean flag = false;
            int j = 0;
            int k = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (k > 0) {
                if (this.getChatOpen()) {
                    flag = true;
                }
                float f1 = this.getChatScale();
                int l = MathHelper.ceiling_float_int((float)((float)this.getChatWidth() / f1));
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)2.0f, (float)20.0f, (float)0.0f);
                GlStateManager.scale((float)f1, (float)f1, (float)1.0f);
                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1) {
                    int j1;
                    ChatLine chatline = (ChatLine)this.drawnChatLines.get(i1 + this.scrollPos);
                    if (chatline == null || (j1 = updateCounter - chatline.getUpdatedCounter()) >= 200 && !flag) continue;
                    double d0 = (double)j1 / 200.0;
                    d0 = 1.0 - d0;
                    d0 *= 10.0;
                    d0 = MathHelper.clamp_double((double)d0, (double)0.0, (double)1.0);
                    d0 *= d0;
                    int l1 = (int)(255.0 * d0);
                    if (flag) {
                        l1 = 255;
                    }
                    l1 = (int)((float)l1 * f);
                    ++j;
                    if (l1 <= 3) continue;
                    int i2 = 0;
                    int j2 = -i1 * 9;
                    int finalL = l1;
                    if (Aqua.moduleManager.getModuleByName("CustomChat").isToggled()) {
                        if (Aqua.setmgr.getSetting("CustomChatShaders").isState()) {
                            if (Aqua.setmgr.getSetting("CustomChatMode").getCurrentMode().equalsIgnoreCase("Glow")) {
                                if (Aqua.moduleManager.getModuleByName("Arraylist").isToggled()) {
                                    Arraylist.drawGlowArray(() -> GuiNewChat.drawRect((int)i2, (int)(j2 - 9), (int)(i2 + l + 4), (int)j2, (int)finalColor), (boolean)false);
                                }
                            } else if (Aqua.setmgr.getSetting("CustomChatMode").getCurrentMode().equalsIgnoreCase("Shadow") && Aqua.moduleManager.getModuleByName("Shadow").isToggled()) {
                                Shadow.drawGlow(() -> GuiNewChat.drawRect((int)i2, (int)(j2 - 9), (int)(i2 + l + 4), (int)j2, (int)Color.black.getRGB()), (boolean)false);
                            }
                            try {
                                GuiNewChat.drawRect((int)i2, (int)(j2 - 9), (int)(i2 + l + 4), (int)j2, (int)new Color(0, 0, 0, (int)Math.round((double)Aqua.setmgr.getSetting("CustomChatAlpha").getCurrentNumber())).getRGB());
                            }
                            catch (Exception exception) {}
                        }
                    } else {
                        GuiNewChat.drawRect((int)i2, (int)(j2 - 9), (int)(i2 + l + 4), (int)j2, (int)(l1 / 2 << 24));
                    }
                    String s = chatline.getChatComponent().getFormattedText();
                    GlStateManager.enableBlend();
                    if (Aqua.setmgr.getSetting("CustomChatFont").isState()) {
                        this.mc.fontRendererObj.drawString(s, i2, j2 - 9, 0xFFFFFF + (l1 << 24));
                    } else {
                        this.mc.fontRendererObj.drawString(s, i2, j2 - 9, 0xFFFFFF + (l1 << 24));
                    }
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                if (flag) {
                    FontRenderer cfr_ignored_0 = this.mc.fontRendererObj;
                    int k2 = FontRenderer.FONT_HEIGHT;
                    GlStateManager.translate((float)-3.0f, (float)0.0f, (float)0.0f);
                    int l2 = k * k2 + k;
                    int i3 = j * k2 + j;
                    int j3 = this.scrollPos * i3 / k;
                    int k1 = i3 * i3 / l2;
                    if (l2 != i3) {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = this.isScrolled ? 0xCC3333 : 0x3333AA;
                        GuiNewChat.drawRect((int)0, (int)(-j3), (int)2, (int)(-j3 - k1), (int)(l3 + (k3 << 24)));
                        GuiNewChat.drawRect((int)2, (int)(-j3), (int)1, (int)(-j3 - k1), (int)(0xCCCCCC + (k3 << 24)));
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }

    public void drawChat2(int updateCounter) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = this.getLineCount();
            boolean flag = false;
            int j = 0;
            int k = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (k > 0) {
                if (this.getChatOpen()) {
                    flag = true;
                }
                float f1 = this.getChatScale();
                int l = MathHelper.ceiling_float_int((float)((float)this.getChatWidth() / f1));
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)2.0f, (float)20.0f, (float)0.0f);
                GlStateManager.scale((float)f1, (float)f1, (float)1.0f);
                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1) {
                    int j1;
                    ChatLine chatline = (ChatLine)this.drawnChatLines.get(i1 + this.scrollPos);
                    if (chatline == null || (j1 = updateCounter - chatline.getUpdatedCounter()) >= 200 && !flag) continue;
                    double d0 = (double)j1 / 200.0;
                    d0 = 1.0 - d0;
                    d0 *= 10.0;
                    d0 = MathHelper.clamp_double((double)d0, (double)0.0, (double)1.0);
                    d0 *= d0;
                    int l1 = (int)(255.0 * d0);
                    if (flag) {
                        l1 = 255;
                    }
                    l1 = (int)((float)l1 * f);
                    ++j;
                    if (l1 <= 3) continue;
                    int i2 = 0;
                    int j2 = -i1 * 9;
                    int finalL = l1;
                    if (Aqua.moduleManager.getModuleByName("CustomChat").isToggled() && Aqua.moduleManager.getModuleByName("Blur").isToggled() && Aqua.setmgr.getSetting("CustomChatBlur").isState()) {
                        Blur.drawBlurred(() -> GuiNewChat.drawRect((int)i2, (int)(j2 - 9), (int)(i2 + l + 4), (int)j2, (int)Color.black.getRGB()), (boolean)false);
                    }
                    String s = chatline.getChatComponent().getFormattedText();
                    GlStateManager.enableBlend();
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                if (flag) {
                    FontRenderer cfr_ignored_0 = this.mc.fontRendererObj;
                    int k2 = FontRenderer.FONT_HEIGHT;
                    GlStateManager.translate((float)-3.0f, (float)0.0f, (float)0.0f);
                    int l2 = k * k2 + k;
                    int i3 = j * k2 + j;
                    int j3 = this.scrollPos * i3 / k;
                    int k1 = i3 * i3 / l2;
                    if (l2 != i3) {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = this.isScrolled ? 0xCC3333 : 0x3333AA;
                        GuiNewChat.drawRect((int)0, (int)(-j3), (int)2, (int)(-j3 - k1), (int)(l3 + (k3 << 24)));
                        GuiNewChat.drawRect((int)2, (int)(-j3), (int)1, (int)(-j3 - k1), (int)(0xCCCCCC + (k3 << 24)));
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }

    public void clearChatMessages() {
        this.drawnChatLines.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }

    public void printChatMessage(IChatComponent chatComponent) {
        this.printChatMessageWithOptionalDeletion(chatComponent, 0);
    }

    public void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId) {
        this.setChatLine(chatComponent, chatLineId, this.mc.ingameGUI.getUpdateCounter(), false);
        logger.info("[CHAT] " + chatComponent.getUnformattedText());
    }

    private void setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        if (chatLineId != 0) {
            this.deleteChatLine(chatLineId);
        }
        int i = MathHelper.floor_float((float)((float)this.getChatWidth() / this.getChatScale()));
        List list = GuiUtilRenderComponents.splitText((IChatComponent)chatComponent, (int)i, (FontRenderer)this.mc.fontRendererObj, (boolean)false, (boolean)false);
        boolean flag = this.getChatOpen();
        for (IChatComponent ichatcomponent : list) {
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }
            this.drawnChatLines.add(0, (Object)new ChatLine(updateCounter, ichatcomponent, chatLineId));
        }
        while (this.drawnChatLines.size() > 100) {
            this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
        }
        if (!displayOnly) {
            this.chatLines.add(0, (Object)new ChatLine(updateCounter, chatComponent, chatLineId));
            while (this.chatLines.size() > 100) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }

    public void refreshChat() {
        this.drawnChatLines.clear();
        this.resetScroll();
        for (int i = this.chatLines.size() - 1; i >= 0; --i) {
            ChatLine chatline = (ChatLine)this.chatLines.get(i);
            this.setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
        }
    }

    public List<String> getSentMessages() {
        return this.sentMessages;
    }

    public void addToSentMessages(String message) {
        if (this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals((Object)message)) {
            this.sentMessages.add((Object)message);
        }
    }

    public void resetScroll() {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    public void scroll(int amount) {
        this.scrollPos += amount;
        int i = this.drawnChatLines.size();
        if (this.scrollPos > i - this.getLineCount()) {
            this.scrollPos = i - this.getLineCount();
        }
        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }

    public IChatComponent getChatComponent(int mouseX, int mouseY) {
        if (!this.getChatOpen()) {
            return null;
        }
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int i = scaledresolution.getScaleFactor();
        float f = this.getChatScale();
        int j = mouseX / i - 3;
        int k = mouseY / i - 27;
        j = MathHelper.floor_float((float)((float)j / f));
        k = MathHelper.floor_float((float)((float)k / f));
        if (j >= 0 && k >= 0) {
            int l = Math.min((int)this.getLineCount(), (int)this.drawnChatLines.size());
            if (j <= MathHelper.floor_float((float)((float)this.getChatWidth() / this.getChatScale()))) {
                FontRenderer cfr_ignored_0 = this.mc.fontRendererObj;
                if (k < FontRenderer.FONT_HEIGHT * l + l) {
                    FontRenderer cfr_ignored_1 = this.mc.fontRendererObj;
                    int i1 = k / FontRenderer.FONT_HEIGHT + this.scrollPos;
                    if (i1 >= 0 && i1 < this.drawnChatLines.size()) {
                        ChatLine chatline = (ChatLine)this.drawnChatLines.get(i1);
                        int j1 = 0;
                        for (IChatComponent ichatcomponent : chatline.getChatComponent()) {
                            if (!(ichatcomponent instanceof ChatComponentText) || (j1 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a((String)((ChatComponentText)ichatcomponent).getChatComponentText_TextValue(), (boolean)false))) <= j) continue;
                            return ichatcomponent;
                        }
                    }
                    return null;
                }
            }
            return null;
        }
        return null;
    }

    public static boolean getChatOpen() {
        return Minecraft.getMinecraft().currentScreen instanceof GuiChat;
    }

    public void deleteChatLine(int id) {
        Iterator iterator = this.drawnChatLines.iterator();
        while (iterator.hasNext()) {
            ChatLine chatline = (ChatLine)iterator.next();
            if (chatline.getChatLineID() != id) continue;
            iterator.remove();
        }
        iterator = this.chatLines.iterator();
        while (iterator.hasNext()) {
            ChatLine chatline1 = (ChatLine)iterator.next();
            if (chatline1.getChatLineID() != id) continue;
            iterator.remove();
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

    public static int calculateChatboxWidth(float scale) {
        int i = 320;
        int j = 40;
        return MathHelper.floor_float((float)(scale * (float)(i - j) + (float)j));
    }

    public static int calculateChatboxHeight(float scale) {
        int i = 180;
        int j = 20;
        return MathHelper.floor_float((float)(scale * (float)(i - j) + (float)j));
    }

    public int getLineCount() {
        return this.getChatHeight() / 9;
    }
}
