package net.minecraft.client.gui;

import cc.novoline.Novoline;
import cc.novoline.modules.visual.HUD;
import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.fonts.impl.Fonts;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class GuiNewChat extends Gui {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Minecraft mc;
    private final List<String> sentMessages = Lists.newArrayList();
    private final List<ChatLine> chatLines = Lists.newArrayList();
    private final List<ChatLine> field_146253_i = Lists.newArrayList();
    private int scrollPos;
    private boolean isScrolled;

    public GuiNewChat(Minecraft mcIn) {
        this.mc = mcIn;
    }


    public void drawChat(int updateCounter) {
        if (this.mc.gameSettings.chatVisibility == EntityPlayer.EnumChatVisibility.HIDDEN) return;

        final int i = this.getLineCount();
        boolean isChatOpened = false;
        int j = 0;
        final int messagesSize = this.field_146253_i.size();
        final float alpha = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

        if (messagesSize <= 0) return;
        if (getChatOpen()) isChatOpened = true;

        final float chatScale = this.getChatScale();
        final int chatWidth = MathHelper.ceiling_float_int((float) getChatWidth() / chatScale);
        GlStateManager.pushMatrix();
        GlStateManager.translate(2.0F, 10.0F, 0.0F);
        GlStateManager.scale(chatScale, chatScale, 1.0F);
        double gay = 0;
        double gay2 = 0;

        for (int messageIndex = 0; messageIndex + this.scrollPos < this.field_146253_i.size() && messageIndex < i; ++messageIndex) {
            final ChatLine chatline = this.field_146253_i.get(messageIndex + this.scrollPos);

            if (chatline != null) {
                final int elapsedTicks = updateCounter - chatline.getUpdatedCounter();
                if (elapsedTicks < 200 || isChatOpened) {
                    int opacity = 255;
                    if (isChatOpened) opacity = 255;
                    opacity = (int) ((float) opacity * alpha);
                    if (opacity > 3) gay2 = -messageIndex * 9;
                    gay++;
                }
            }
        }

        if (gay != 0) {
            RenderUtils.drawBorderedRect(-1, gay2 - (isClientStyle() ? 11 : 10),
                    chatWidth + 4, isClientStyle() ? 1 : 2, 1,
                    new Color(29, 29, 29, 255).getRGB(), new Color(0, 0, 0, 100).getRGB());
        }

        for (int messageIndex = 0; messageIndex + this.scrollPos < this.field_146253_i.size() && messageIndex < i; ++messageIndex) {
            final ChatLine chatline = this.field_146253_i.get(messageIndex + this.scrollPos);
            if (chatline != null) {
                final int elapsedTicks = updateCounter - chatline.getUpdatedCounter();
                if (elapsedTicks < 200 || isChatOpened) {
                    int opacity = 255;

                    if (isChatOpened) {
                        opacity = 255;
                    }

                    opacity = (int) ((float) opacity * alpha);
                    ++j;

                    if (opacity > 3) {
                        final int left = 0;
                        final int bottom = -messageIndex * 9;
                        drawRect(left, bottom - 9, left + chatWidth + 4, bottom, 255);
                        String message = chatline.getChatComponent().getFormattedText();
                        GlStateManager.enableBlend();

                        drawString(message, (float) left + 2, (float) (bottom - 8), 16777215 + (opacity << 24));
                        GlStateManager.disableAlpha();
                        GlStateManager.disableBlend();
                    }
                }
            }
        }

        if (isChatOpened) {
            final int k2 = getHeight() - (isClientStyle() ? -1 : 1);
            GlStateManager.translate(-3.0F, 0.0F, 0.0F);
            final int l2 = messagesSize * k2 + messagesSize;
            final int i3 = j * k2 + j;
            final int j3 = this.scrollPos * i3 / messagesSize;
            final int k1 = i3 * i3 / l2;

            if (l2 != i3) {
                final int k3 = j3 > 0 ? 170 : 96;
                final int l3 = this.isScrolled ? 13382451 : 3355562;

                drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
            }
        }

        GlStateManager.popMatrix();
    }

    /**
     * Clears the chat.
     */
    public void clearChatMessages() {
        this.field_146253_i.clear();
        this.chatLines.clear();

        //todo Clear sent messages on F3+D
        this.sentMessages.clear();
    }

    public void printChatMessage(IChatComponent p_146227_1_) {
        this.printChatMessageWithOptionalDeletion(p_146227_1_, 0);
    }

    /**
     * Prints the ChatComponent to Chat. If the ID is not 0, deletes an existing Chat Line of that ID from the GUI
     */
    public void printChatMessageWithOptionalDeletion(@NotNull IChatComponent message, int p_146234_2_) {
        setChatLine(message, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
        LOGGER.info("[CHAT] " + message.getUnformattedText());
    }

    private void setChatLine(IChatComponent message, int p_146237_2_, int p_146237_3_, boolean p_146237_4_) {
        if (p_146237_2_ != 0) deleteChatLine(p_146237_2_);

        final int i = MathHelper.floor_float((float) getChatWidth() / getChatScale());
        final List<IChatComponent> list = getList(message, i);
        final boolean flag = this.getChatOpen();

        for (IChatComponent component : list) {
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                scroll(1);
            }

            this.field_146253_i.add(0, new ChatLine(p_146237_3_, component, p_146237_2_));
        }

        while (this.field_146253_i.size() > 1000) {
            this.field_146253_i.remove(this.field_146253_i.size() - 1);
        }

        if (!p_146237_4_) {
            this.chatLines.add(0, new ChatLine(p_146237_3_, message, p_146237_2_));

            while (this.chatLines.size() > 1000) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }

    public void refreshChat() {
        this.field_146253_i.clear();
        resetScroll();

        for (int i = this.chatLines.size() - 1; i >= 0; --i) {
            final ChatLine chatline = this.chatLines.get(i);
            setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
        }
    }

    public List<String> getSentMessages() {
        return this.sentMessages;
    }

    /**
     * Adds this string to the list of sent messages, for recall using the up/down arrow keys
     */
    public void addToSentMessages(String p_146239_1_) {
        if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(p_146239_1_)) {
            this.sentMessages.add(p_146239_1_);
        }
    }

    /**
     * Resets the chat scroll (executed when the GUI is closed, among others)
     */
    public void resetScroll() {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    /**
     * Scrolls the chat by the given number of lines.
     */
    public void scroll(int p_146229_1_) {
        this.scrollPos += p_146229_1_;
        final int i = this.field_146253_i.size();

        if (this.scrollPos > i - getLineCount()) {
            this.scrollPos = i - getLineCount();
        }

        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }

    /**
     * Gets the chat component under the mouse
     */
    public IChatComponent getChatComponent(int p_146236_1_, int p_146236_2_) {
        if (!getChatOpen()) return null;

        final ScaledResolution resolution = new ScaledResolution(this.mc);
        final int i = resolution.getScaleFactor();
        final float f = getChatScale();

        int j = p_146236_1_ / i - 3, // @off
                k = p_146236_2_ / i - 27; // @on

        j = MathHelper.floor_float((float) j / f);
        k = MathHelper.floor_float((float) k / f);

        if (j < 0 || k < 0) {
            return null;
        }

        final int l = Math.min(getLineCount(), this.field_146253_i.size());

        if (j > MathHelper.floor_float((float) getChatWidth() / getChatScale()) || k >= getHeight() * l + l) {
            return null;
        }

        final int i1 = k / getHeight() + this.scrollPos;

        if (i1 < 0 || i1 >= this.field_146253_i.size()) {
            return null;
        }

        final ChatLine chatline = this.field_146253_i.get(i1);
        int j1 = 0;

        for (IChatComponent chatComponent : chatline.getChatComponent()) {
            if (chatComponent instanceof ChatComponentText) {
                j1 += getWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText) chatComponent).getChatComponentText_TextValue(), false));

                if (j1 > j) {
                    return chatComponent;
                }
            }
        }

        return null;
    }

    /**
     * Returns true if the chat GUI is open
     */
    public boolean getChatOpen() {
        return this.mc.currentScreen instanceof GuiChat;
    }

    /**
     * finds and deletes a Chat line by ID
     */
    public void deleteChatLine(int chatLineId) {
        Iterator<ChatLine> iterator = this.field_146253_i.iterator();

        while (iterator.hasNext()) {
            final ChatLine chatline = iterator.next();

            if (chatline.getChatLineID() == chatLineId) {
                iterator.remove();
            }
        }

        iterator = this.chatLines.iterator();

        while (iterator.hasNext()) {
            final ChatLine line = iterator.next();

            if (line.getChatLineID() == chatLineId) {
                iterator.remove();
                break;
            }
        }
    }

    public int getChatWidth() {
        return calculateChatBoxWidth(this.mc.gameSettings.chatWidth);
    }

    public int getChatHeight() {
        return calculateChatBoxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    /**
     * Returns the chat scale from mc.gameSettings.chatScale
     */
    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }

    public static int calculateChatBoxWidth(float p_146233_0_) {
        final int i = 320;
        final int j = 40;
        return MathHelper.floor_float(p_146233_0_ * (float) (i - j) + (float) j);
    }

    public static int calculateChatBoxHeight(float p_146243_0_) {
        final int i = 180;
        final int j = 20;
        return MathHelper.floor_float(p_146243_0_ * (float) (i - j) + (float) j);
    }

    public int getLineCount() {
        return this.getChatHeight() / 9;
    }

    private boolean isClientStyle() {
        return Novoline.getInstance().getModuleManager().getModule(HUD.class).isEnabled() &&
                Novoline.getInstance().getModuleManager().getModule(HUD.class).getChatStyle();
    }

    private List<IChatComponent> getList(IChatComponent message, int i) {
        if (isClientStyle()) {
            return GuiUtilRenderComponents.func_178908_a(message, i, getClientFont(), false, false);
        } else {
            return GuiUtilRenderComponents.func_178908_a(message, i, getVanillaFont(), false, false);
        }
    }

    private int getHeight() {
        return isClientStyle() ? getClientFont().getHeight() : getVanillaFont().getHeight();
    }

    private float getWidth(String string) {
        return isClientStyle() ? getClientFont().stringWidth(string) : getVanillaFont().getStringWidth(string);
    }

    private void drawString(String string, float x, float y, int color) {
        if (isClientStyle()) {
            getClientFont().drawString(string, x, y, color, true);
        } else {
            getVanillaFont().drawString(string, x, y, color, true);
        }
    }

    private FontRenderer getVanillaFont() {
        return Minecraft.getInstance().fontRendererObj;
    }

    private cc.novoline.utils.fonts.api.FontRenderer getClientFont() {
        return Fonts.SF.SF_18.SF_18;
    }

}
