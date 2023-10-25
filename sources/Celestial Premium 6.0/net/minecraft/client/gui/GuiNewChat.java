/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.events.impl.packet.EventReceiveMessage;
import org.celestial.client.feature.impl.misc.CustomChat;
import org.celestial.client.helpers.render.AnimationHelper;
import org.celestial.client.ui.font.MCFontRenderer;

public class GuiNewChat
extends Gui {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Minecraft mc;
    private final List<String> sentMessages = Lists.newArrayList();
    private final List<ChatLine> chatLines = Lists.newArrayList();
    private final List<ChatLine> drawnChatLines = Lists.newArrayList();
    private int scrollPos;
    private boolean isScrolled;
    private String lastMessage;
    private int spamCounter;
    private int line;
    private final HashMap<String, String> strCache = new HashMap();

    public GuiNewChat(Minecraft mcIn) {
        this.mc = mcIn;
    }

    private MCFontRenderer getFontRender() {
        MCFontRenderer font = this.mc.fontRenderer;
        String mode = CustomChat.fontType.getOptions();
        if (mode.equalsIgnoreCase("Comfortaa")) {
            font = this.mc.comfortaa;
        } else if (mode.equalsIgnoreCase("SF UI")) {
            font = this.mc.fontRenderer;
        } else if (mode.equalsIgnoreCase("Verdana")) {
            font = this.mc.verdanaFontRender;
        } else if (mode.equalsIgnoreCase("RobotoRegular")) {
            font = this.mc.robotoRegularFontRender;
        } else if (mode.equalsIgnoreCase("Lato")) {
            font = this.mc.latoFontRender;
        } else if (mode.equalsIgnoreCase("Open Sans")) {
            font = this.mc.openSansFontRender;
        } else if (mode.equalsIgnoreCase("Ubuntu")) {
            font = this.mc.ubuntuFontRender;
        } else if (mode.equalsIgnoreCase("LucidaConsole")) {
            font = this.mc.lucidaConsoleFontRenderer;
        } else if (mode.equalsIgnoreCase("Calibri")) {
            font = this.mc.calibri;
        } else if (mode.equalsIgnoreCase("Product Sans")) {
            font = this.mc.productsans;
        } else if (mode.equalsIgnoreCase("RaleWay")) {
            font = this.mc.raleway;
        } else if (mode.equalsIgnoreCase("Kollektif")) {
            font = this.mc.kollektif;
        } else if (mode.equalsIgnoreCase("Bebas Book")) {
            font = this.mc.bebasRegular;
        }
        return font;
    }

    public void drawChat(int updateCounter) {
        GlStateManager.pushMatrix();
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = this.getLineCount();
            int j = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (j > 0) {
                boolean flag = false;
                if (this.getChatOpen()) {
                    flag = true;
                }
                float f1 = this.getChatScale();
                int k = MathHelper.ceil((float)this.getChatWidth() / f1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0f, 8.0f, 0.0f);
                GlStateManager.scale(f1, f1, 1.0f);
                int l = 0;
                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1) {
                    int j1;
                    ChatLine chatline = this.drawnChatLines.get(i1 + this.scrollPos);
                    if (chatline == null || (j1 = updateCounter - chatline.getUpdatedCounter()) >= 200 && !flag) continue;
                    double d0 = (double)j1 / 200.0;
                    d0 = 1.0 - d0;
                    d0 *= 10.0;
                    d0 = MathHelper.clamp(d0, 0.0, 1.0);
                    d0 *= d0;
                    int l1 = (int)(255.0 * d0);
                    if (flag) {
                        l1 = 255;
                    }
                    l1 = (int)((float)l1 * f);
                    ++l;
                    if (l1 <= 3) continue;
                    int j2 = -i1 * 9;
                    if (!Celestial.instance.featureManager.getFeatureByClass(CustomChat.class).getState() || !CustomChat.noBackground.getCurrentValue()) {
                        GuiNewChat.drawRect(-2, j2 - 9, k + 4, j2, l1 / 2 << 24);
                    }
                    String s = chatline.getChatComponent().getFormattedText();
                    GlStateManager.enableBlend();
                    if (!Celestial.instance.featureManager.getFeatureByClass(CustomChat.class).getState() || !CustomChat.customFont.getCurrentValue()) {
                        chatline.setPosY(AnimationHelper.animation2(chatline.getPosY(), (float)j2 - 8.5f, (float)((double)0.1f * Minecraft.frameTime * (double)0.1f)));
                    } else {
                        chatline.setPosY(AnimationHelper.animation2(chatline.getPosY(), j2 - this.getFontRender().getFontHeight(), (float)((double)0.1f * Minecraft.frameTime * (double)0.1f)));
                    }
                    chatline.setPosX(AnimationHelper.animation2(chatline.getPosX(), 0.0f, (float)((double)0.1f * Minecraft.frameTime * (double)0.1f)));
                    if (!Celestial.instance.featureManager.getFeatureByClass(CustomChat.class).getState() || !CustomChat.customFont.getCurrentValue()) {
                        this.mc.fontRendererObj.drawStringWithShadow(s, flag ? 0.0f : chatline.getPosX(), flag ? (float)((double)j2 - 9.5) + f1 : chatline.getPosY(), 0xFFFFFF + (l1 << 24));
                    } else {
                        this.getFontRender().drawStringWithShadow(s, flag ? 0.0 : (double)chatline.getPosX(), flag ? (double)((float)((double)j2 - 9.5) + f1) : (double)chatline.getPosY(), 0xFFFFFF + (l1 << 24));
                    }
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                if (flag) {
                    int k2 = !Celestial.instance.featureManager.getFeatureByClass(CustomChat.class).getState() || !CustomChat.customFont.getCurrentValue() ? this.mc.fontRendererObj.FONT_HEIGHT : this.getFontRender().getFontHeight();
                    GlStateManager.translate(-3.0f, 0.0f, 0.0f);
                    int l2 = j * k2 + j;
                    int i3 = l * k2 + l;
                    int j3 = this.scrollPos * i3 / j;
                    int k1 = i3 * i3 / l2;
                    if (l2 != i3) {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = this.isScrolled ? 0xCC3333 : 0x3333AA;
                        GuiNewChat.drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                        GuiNewChat.drawRect(2, -j3, 1, -j3 - k1, 0xCCCCCC + (k3 << 24));
                    }
                }
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
    }

    public void clearChatMessages(boolean p_146231_1_) {
        this.drawnChatLines.clear();
        this.chatLines.clear();
        if (p_146231_1_) {
            this.sentMessages.clear();
        }
    }

    public void printChatMessage(ITextComponent chatComponent) {
        String text = this.stringFix(chatComponent.getFormattedText());
        if (text.equals(this.lastMessage)) {
            this.mc.ingameGUI.getChatGUI().deleteChatLine(this.line);
            ++this.spamCounter;
            this.lastMessage = text;
            chatComponent.appendText((Object)((Object)ChatFormatting.WHITE) + " (x" + this.spamCounter + ")");
        } else {
            this.spamCounter = 1;
            this.lastMessage = text;
        }
        ++this.line;
        if (this.line > 256) {
            this.line = 0;
        }
        this.printChatMessageWithOptionalDeletion(chatComponent, this.line);
        EventReceiveMessage event = new EventReceiveMessage(chatComponent.getUnformattedText());
        EventManager.call(event);
    }

    private String stringFix(String str) {
        if (this.strCache.containsKey(str)) {
            return this.strCache.get(str);
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
        this.strCache.put(str, result);
        return result;
    }

    public void printChatMessageWithOptionalDeletion(ITextComponent chatComponent, int chatLineId) {
        this.setChatLine(chatComponent, chatLineId, this.mc.ingameGUI.getUpdateCounter(), false);
        LOGGER.info("[CHAT] {}", chatComponent.getUnformattedText().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));
    }

    private void setChatLine(ITextComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        if (chatLineId != 0) {
            this.deleteChatLine(chatLineId);
        }
        int i = MathHelper.floor((float)this.getChatWidth() / this.getChatScale());
        List<ITextComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, this.mc.fontRendererObj, false, false);
        boolean flag = this.getChatOpen();
        for (ITextComponent itextcomponent : list) {
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }
            this.drawnChatLines.add(0, new ChatLine(updateCounter, itextcomponent, chatLineId));
        }
        while (this.drawnChatLines.size() > 100) {
            this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
        }
        if (!displayOnly) {
            this.chatLines.add(0, new ChatLine(updateCounter, chatComponent, chatLineId));
            while (this.chatLines.size() > 100) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }

    public void refreshChat() {
        this.drawnChatLines.clear();
        this.resetScroll();
        for (int i = this.chatLines.size() - 1; i >= 0; --i) {
            ChatLine chatline = this.chatLines.get(i);
            this.setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
        }
    }

    public List<String> getSentMessages() {
        return this.sentMessages;
    }

    public void addToSentMessages(String message) {
        if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(message)) {
            this.sentMessages.add(message);
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

    @Nullable
    public ITextComponent getChatComponent(int mouseX, int mouseY) {
        if (!this.getChatOpen()) {
            return null;
        }
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int i = scaledresolution.getScaleFactor();
        float f = this.getChatScale();
        int j = mouseX / i - 2;
        int k = mouseY / i - 40;
        j = MathHelper.floor((float)j / f);
        k = MathHelper.floor((float)k / f);
        if (j >= 0 && k >= 0) {
            int l = Math.min(this.getLineCount(), this.drawnChatLines.size());
            if (j <= MathHelper.floor((float)this.getChatWidth() / this.getChatScale()) && k < this.mc.fontRendererObj.FONT_HEIGHT * l + l) {
                int i1 = k / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
                if (i1 >= 0 && i1 < this.drawnChatLines.size()) {
                    ChatLine chatline = this.drawnChatLines.get(i1);
                    int j1 = 0;
                    for (ITextComponent itextcomponent : chatline.getChatComponent()) {
                        if (!(itextcomponent instanceof TextComponentString) || (j1 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(((TextComponentString)itextcomponent).getText(), false))) <= j) continue;
                        return itextcomponent;
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

    public void deleteChatLine(int id) {
        Iterator<ChatLine> iterator = this.drawnChatLines.iterator();
        while (iterator.hasNext()) {
            ChatLine chatline = iterator.next();
            if (chatline.getChatLineID() != id) continue;
            iterator.remove();
        }
        iterator = this.chatLines.iterator();
        while (iterator.hasNext()) {
            ChatLine chatline1 = iterator.next();
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
        return MathHelper.floor(scale * 280.0f + 40.0f);
    }

    public static int calculateChatboxHeight(float scale) {
        int i = 180;
        int j = 20;
        return MathHelper.floor(scale * 160.0f + 20.0f);
    }

    public int getLineCount() {
        return this.getChatHeight() / 9;
    }
}

