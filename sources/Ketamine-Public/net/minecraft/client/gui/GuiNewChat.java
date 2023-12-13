package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

import io.github.nevalackin.client.util.render.BlurUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class GuiNewChat extends Gui
{
    private static final Logger logger = LogManager.getLogger();
    private final Minecraft mc;
    private final List<String> sentMessages = Lists.<String>newArrayList();
    private final List<ChatLine> chatLines = Lists.<ChatLine>newArrayList();
    private final List<ChatLine> lines = Lists.<ChatLine>newArrayList();
    private int scrollPos;
    private boolean isScrolled;

    public GuiNewChat(Minecraft mcIn)
    {
        this.mc = mcIn;
    }

    private int calculatedHeight;

    public void drawChat(final ScaledResolution scaledResolution,
                         final int updateCounter) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            final int lineCount = this.getLineCount();
            final int nLines = this.lines.size();
            final float opacity = this.mc.gameSettings.chatOpacity;

            final boolean chatOpen = this.getChatOpen();

            final float scale = this.getChatScale();

            if (nLines > 0) {
                glPushMatrix();
                glTranslatef(2, scaledResolution.getScaledHeight() - (chatOpen ? 16 : 2), 0);
                glScalef(scale, scale, 1);

                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

                final int chatHeight = this.getChatHeight();

                final int chatWidth = this.getChatWidth();

                if (chatOpen) {
                    BlurUtil.blurArea(2, scaledResolution.getScaledHeight() - 16 + (-chatHeight - 2) * scale,
                                      (chatWidth + 6) * scale, (chatHeight + 2) * scale);
                    DrawUtil.glDrawFilledQuad(0, -chatHeight - 2, chatWidth + 6, chatHeight + 2, (int) (opacity * 255) << 24);
                } else if (this.calculatedHeight > 0) {
                    BlurUtil.blurArea(2, scaledResolution.getScaledHeight() - 2 + (-this.calculatedHeight - 2) * scale,
                                      (chatWidth + 6) * scale, (this.calculatedHeight + 2) * scale);
                    DrawUtil.glDrawFilledQuad(0, -this.calculatedHeight - 2, chatWidth + 6, this.calculatedHeight + 2, (int) (opacity * 255) << 24);
                }

                final int visibleDuration = 150;
                final int fadeInDelay = 10;
                final int fadeOutDelay = 30;

                this.calculatedHeight = 0;

                for (int i = 0; i + this.scrollPos < nLines; i++) {
                    final int yOffset = (i + 1) * 9;
                    if (yOffset > chatHeight) break;

                    final ChatLine chatline = this.lines.get(i + this.scrollPos);

                    final int ticksExisted = updateCounter - chatline.getUpdatedCounter();

                    if (!chatOpen && ticksExisted > fadeInDelay + visibleDuration + fadeOutDelay) break;

                    int colour = 0xFFFFFF;

                    if (chatOpen) {
                        colour |= 0xFF000000;
                    } else if (ticksExisted <= fadeInDelay) {
                        colour += (ticksExisted * (255 / fadeInDelay)) << 24;
                    } else if (ticksExisted <= visibleDuration + fadeInDelay) {
                        colour |= 0xFF000000;
                    } else {
                        final float alpha = (ticksExisted - (visibleDuration + fadeInDelay)) / (float) fadeOutDelay;
                        final int inv = (int) ((1.0F - alpha) * 255.0F);
                        if (inv < 0x20) break;
                        colour += inv << 24;
                    }

                    this.calculatedHeight += 9;

                    this.mc.fontRendererObj.drawStringWithShadow(chatline.getChatComponent().getFormattedText(), 2, -yOffset, colour);
                }

                glPopMatrix();
            }
        }
    }

    /**
     * Clears the chat.
     */
    public void clearChatMessages()
    {
        this.lines.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }

    public void printChatMessage(IChatComponent p_146227_1_)
    {
        this.printChatMessageWithOptionalDeletion(p_146227_1_, 0);
    }

    /**
     * prints the ChatComponent to Chat. If the ID is not 0, deletes an existing Chat Line of that ID from the GUI
     */
    public void printChatMessageWithOptionalDeletion(IChatComponent p_146234_1_, int p_146234_2_)
    {
        this.setChatLine(p_146234_1_, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
        logger.info("[CHAT] " + p_146234_1_.getUnformattedText());
    }

    private void setChatLine(IChatComponent p_146237_1_, int p_146237_2_, int p_146237_3_, boolean p_146237_4_)
    {
        if (p_146237_2_ != 0)
        {
            this.deleteChatLine(p_146237_2_);
        }

        int i = MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale());
        List<IChatComponent> list = GuiUtilRenderComponents.func_178908_a(p_146237_1_, i, this.mc.fontRendererObj, false, false);
        boolean flag = this.getChatOpen();

        for (IChatComponent ichatcomponent : list)
        {
            if (flag && this.scrollPos > 0)
            {
                this.isScrolled = true;
                this.scroll(1);
            }

            this.lines.add(0, new ChatLine(p_146237_3_, ichatcomponent, p_146237_2_));
        }

        while (this.lines.size() > 100)
        {
            this.lines.remove(this.lines.size() - 1);
        }

        if (!p_146237_4_)
        {
            this.chatLines.add(0, new ChatLine(p_146237_3_, p_146237_1_, p_146237_2_));

            while (this.chatLines.size() > 100)
            {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }

    public void refreshChat()
    {
        this.lines.clear();
        this.resetScroll();

        for (int i = this.chatLines.size() - 1; i >= 0; --i)
        {
            ChatLine chatline = (ChatLine)this.chatLines.get(i);
            this.setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
        }
    }

    public List<String> getSentMessages()
    {
        return this.sentMessages;
    }

    /**
     * Adds this string to the list of sent messages, for recall using the up/down arrow keys
     */
    public void addToSentMessages(String p_146239_1_)
    {
        if (this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(p_146239_1_))
        {
            this.sentMessages.add(p_146239_1_);
        }
    }

    /**
     * Resets the chat scroll (executed when the GUI is closed, among others)
     */
    public void resetScroll()
    {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    /**
     * Scrolls the chat by the given number of lines.
     */
    public void scroll(int p_146229_1_)
    {
        this.scrollPos += p_146229_1_;
        int i = this.lines.size();

        if (this.scrollPos > i - this.getLineCount())
        {
            this.scrollPos = i - this.getLineCount();
        }

        if (this.scrollPos <= 0)
        {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }

    /**
     * Gets the chat component under the mouse
     */
    public IChatComponent getChatComponent(int mouseX, int mouseY) {
        if (this.getChatOpen()) {
            final ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            final float scale = this.getChatScale();

            final float textHeight = this.mc.fontRendererObj.FONT_HEIGHT * scale;

            if (mouseX > 2 && mouseX < (2 + this.getChatWidth() * scale) && mouseY < scaledResolution.getScaledHeight() - (14 + 2) &&
                mouseY > (scaledResolution.getScaledHeight() - (14 + 2)) - this.getChatHeight() * scale) {

                final int mouseIndexOffWithScroll = (int) (((scaledResolution.getScaledHeight() - (14 + 2)) - mouseY) / textHeight) + this.scrollPos;

                if (mouseIndexOffWithScroll >= 0 && mouseIndexOffWithScroll < this.lines.size()) {
                    final ChatLine hoveredLine = this.lines.get(mouseIndexOffWithScroll);

                    int textXOffset = 0;

                    for (final IChatComponent ichatcomponent : hoveredLine.getChatComponent()) {
                        if (ichatcomponent instanceof ChatComponentText) {
                            textXOffset += this.mc.fontRendererObj.getStringWidth(
                                GuiUtilRenderComponents.func_178909_a(
                                    ((ChatComponentText) ichatcomponent).getChatComponentText_TextValue(), false));

                            if (textXOffset > mouseX) {
                                return ichatcomponent;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Returns true if the chat GUI is open
     */
    public boolean getChatOpen()
    {
        return this.mc.currentScreen instanceof GuiChat;
    }

    /**
     * finds and deletes a Chat line by ID
     */
    public void deleteChatLine(int p_146242_1_)
    {
        Iterator<ChatLine> iterator = this.lines.iterator();

        while (iterator.hasNext())
        {
            ChatLine chatline = (ChatLine)iterator.next();

            if (chatline.getChatLineID() == p_146242_1_)
            {
                iterator.remove();
            }
        }

        iterator = this.chatLines.iterator();

        while (iterator.hasNext())
        {
            ChatLine chatline1 = (ChatLine)iterator.next();

            if (chatline1.getChatLineID() == p_146242_1_)
            {
                iterator.remove();
                break;
            }
        }
    }

    public int getChatWidth()
    {
        return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }

    public int getChatHeight()
    {
        return calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    /**
     * Returns the chatscale from mc.gameSettings.chatScale
     */
    public float getChatScale()
    {
        return this.mc.gameSettings.chatScale;
    }

    public static int calculateChatboxWidth(float p_146233_0_)
    {
        int i = 320;
        int j = 40;
        return MathHelper.floor_float(p_146233_0_ * (float)(i - j) + (float)j);
    }

    public static int calculateChatboxHeight(float p_146243_0_)
    {
        int i = 180;
        int j = 20;
        return MathHelper.floor_float(p_146243_0_ * (float)(i - j) + (float)j);
    }

    public int getLineCount()
    {
        return this.getChatHeight() / 9;
    }
}
