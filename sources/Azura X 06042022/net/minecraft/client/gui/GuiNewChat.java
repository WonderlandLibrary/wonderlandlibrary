package net.minecraft.client.gui;

import best.azura.irc.core.entities.*;
import best.azura.client.impl.Client;
import best.azura.client.impl.module.impl.other.IRCModule;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.module.impl.render.CustomMinecraft;
import best.azura.client.impl.ui.chat.GuiCustomChat;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GuiNewChat extends Gui {
    private static final Logger logger = LogManager.getLogger();
    private final Minecraft mc;
    private final List<String> sentMessages = Lists.newArrayList();
    private final List<ChatLine> chatLines = Lists.newArrayList();
    private final List<ChatLine> drawnChatLines = Lists.newArrayList();
    private final List<ChatLine> ircLines = Lists.newArrayList();
    private final List<ChatLine> seenIrcLines = Lists.newArrayList();
    private int scrollPos;
    private boolean isScrolled;

    public GuiNewChat(Minecraft mcIn) {
        this.mc = mcIn;
    }

    public void drawChat(int updateCounter) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            final ArrayList<ChatLine> oldDrawn = new ArrayList<>(drawnChatLines);
            final ArrayList<ChatLine> oldLines = new ArrayList<>(chatLines);
            if (Client.INSTANCE.getModuleManager().getModule(IRCModule.class).isEnabled() && (mc.currentScreen instanceof GuiCustomChat || GuiChat.redirectToCustom)) {
                this.drawnChatLines.clear();
                this.chatLines.clear();
                try {
                    this.drawnChatLines.addAll(ircLines.stream().filter(line -> line.getIrcChannel() == GuiCustomChat.currentTabId ||
                            line.getIrcChannel() == -1).collect(Collectors.toList()));
                } catch (Exception ignored) {}
            }

            int i = this.getLineCount();
            boolean flag = false;
            int j = 0;
            int k = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
            if (k > 0) {
                if (this.getChatOpen()) {
                    flag = true;
                }

                float f1 = this.getChatScale();
                int l = MathHelper.ceiling_float_int((float) this.getChatWidth() / f1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 20.0F, 0.0F);
                GlStateManager.scale(f1, f1, 1.0F);

                final CustomMinecraft customMinecraft = (CustomMinecraft) Client.INSTANCE.getModuleManager().getModule(CustomMinecraft.class);
                final boolean blur = BlurModule.blurChat.getObject() && Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled();
                int offset = 0;
                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1) {
                    ChatLine chatline = this.drawnChatLines.get(i1 + this.scrollPos);
                    if (chatline != null) {
                        if (Client.INSTANCE.getModuleManager().getModule(IRCModule.class).isEnabled() && (mc.currentScreen instanceof GuiCustomChat || GuiChat.redirectToCustom) &&
                                (!chatline.isIrc() || !ircLines.contains(chatline))) continue;
                        if (!(Client.INSTANCE.getModuleManager().getModule(IRCModule.class).isEnabled() && (mc.currentScreen instanceof GuiCustomChat || GuiChat.redirectToCustom)) &&
                                chatline.isIrc()) continue;
                        int j1 = updateCounter - chatline.getUpdatedCounter();

                        if (j1 < 200 || flag) {
                            double d0 = (double) j1 / 200.0D;
                            d0 = 1.0D - d0;
                            d0 = d0 * 10.0D;
                            d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
                            d0 = d0 * d0;
                            double d1 = (double) j1 / 75.0D;
                            d1 = d1 * 10.0D;
                            d1 = MathHelper.clamp_double(d1, 0.2D, 1.0D);
                            d1 = d1 * d1;
                            int l1 = (int) (255.0D * d0);

                            if (flag) {
                                l1 = 255;
                            }

                            l1 = (int) ((float) l1 * f);
                            ++j;

                            if (l1 > 3) {
                                int i2 = 0;
                                int j2 = -offset * 9 - 8;
                                if (!(customMinecraft.chatNoBackground.getObject() || blur)) {
                                    drawRect(i2, j2 - 9, i2 + l + 4, j2, l1 / 2 << 24);
                                }

                                if (blur) {
                                    GlStateManager.scale(1.0 / f1, 1.0 / f1, 1.0F);
                                    GlStateManager.translate(-2.0F, -20.0F, 0.0F);
                                    try {
                                        BlurModule.blurTasks.add(() -> {
                                            final ScaledResolution sr = new ScaledResolution(mc);
                                            GlStateManager.translate(0, sr.getScaledHeight() - 47, 0);
                                            GlStateManager.translate(2.0F, 20.0F, 0.0F);
                                            GlStateManager.scale(f1, f1, 1.0F);
                                            drawRect(i2, j2 - 9, i2 + l + 4, j2, -1);
                                            GlStateManager.scale(1.0 / f1, 1.0 / f1, 1.0F);
                                            GlStateManager.translate(-2.0F, -20.0F, 0.0F);
                                            GlStateManager.translate(0, -(sr.getScaledHeight() - 47), 0);
                                        });
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    GlStateManager.translate(2.0F, 20.0F, 0.0F);
                                    GlStateManager.scale(f1, f1, 1.0F);
                                }
                                String s = chatline.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                try {
                                    if (Client.INSTANCE.getModuleManager().getModule(IRCModule.class).isEnabled() && !chatline.isIrc()) {
                                        String lastColorCode = "";
                                        for (User user : Client.INSTANCE.getIrcConnector().getIrcCache().getIrcUserHashMap().values().stream().filter(user -> user.getMinecraftName() != null && !user.getMinecraftName().isEmpty()).collect(Collectors.toList())) {
                                            if (user.getMinecraftName().equals("Player")) continue;
                                            if (lastColorCode.isEmpty()) {
                                                String s1 = s.split(user.getMinecraftName())[0];
                                                lastColorCode = Character.toString(ChatFormatting.PREFIX_CODE) + s1.charAt(s1.lastIndexOf('§') + 1);
                                            }
                                            s = s.replace(user.getMinecraftName(), user.getIrcRank().getPrefix() + " " + user.getUsername() + " " + user.getFormattedIGN() + lastColorCode);
                                        }
                                    }
                                } catch (Exception ignored) {}

                                if (d1 < 1 && customMinecraft.fancyChat.getObject() && customMinecraft.isEnabled()) {
                                    this.mc.fontRendererObj.drawStringWithShadow(s, (float) i2, (float) (j2 - 8), 16777215 + ((int) (l1 * d1) << 24));
                                } else {
                                    this.mc.fontRendererObj.drawStringWithShadow(s, (float) i2, (float) (j2 - 8), 16777215 + (l1 << 24));
                                }
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                        offset++;
                    }
                }

                if (flag) {
                    int k2 = this.mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int l2 = k * k2 + k;
                    int i3 = j * k2 + j;
                    int j3 = this.scrollPos * i3 / k;
                    int k1 = i3 * i3 / l2;

                    if (l2 != i3) {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = this.isScrolled ? 13382451 : 3355562;
                        drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                        drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
                    }
                }

                GlStateManager.popMatrix();
            }
            drawnChatLines.clear();
            chatLines.clear();
            drawnChatLines.addAll(oldDrawn);
            chatLines.addAll(oldLines);
        }
    }

    /**
     * Clears the chat.
     */
    public void clearChatMessages() {
        this.drawnChatLines.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }

    public void printChatMessage(IChatComponent chatComponent) {
        this.printChatMessageWithOptionalDeletion(chatComponent, 0);
    }

    public void printChatMessageIRC(IChatComponent chatComponent, int ircId) {
        this.addIRCLine(chatComponent, this.mc.ingameGUI.getUpdateCounter(), ircId <= -1 ? GuiCustomChat.currentTabId : ircId);
        logger.info("[CHAT] " + chatComponent.getUnformattedText());
    }

    /**
     * prints the ChatComponent to Chat. If the ID is not 0, deletes an existing Chat Line of that ID from the GUI
     */
    public void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId) {
        if (chatComponent.getUnformattedText().contains("${")) return;
        int chatLine = this.setChatLine(chatComponent, chatLineId, this.mc.ingameGUI.getUpdateCounter(), false);
        logger.info("[CHAT] " + chatComponent.getUnformattedText());
    }

    private void addIRCLine(IChatComponent chatComponent, int updateCounter, int ircId) {
        if (chatComponent.getUnformattedText().startsWith("${")) return;

        int i = MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale());
        List<IChatComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, this.mc.fontRendererObj, false, false);
        boolean flag = this.getChatOpen();

        for (IChatComponent ichatcomponent : list) {
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }

            final ChatLine line = new ChatLine(updateCounter, ichatcomponent, 0);
            line.setIrc(true);
            line.setIrcChannel(ircId);

            this.ircLines.add(0, line);
        }
    }

    public void viewIRCLines() {
        this.seenIrcLines.clear();
        this.seenIrcLines.addAll(this.ircLines);
    }

    public boolean hasUnseenMessages() {
        return this.ircLines.size() > this.seenIrcLines.size();
    }

    private int setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        if (chatComponent.getUnformattedText().startsWith("${")) return -1;

        if (chatLineId != 0) {
            this.deleteChatLine(chatLineId);
        }

        int i = MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale());
        List<IChatComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, this.mc.fontRendererObj, false, false);
        boolean flag = this.getChatOpen();

        for (IChatComponent ichatcomponent : list) {
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }

            this.drawnChatLines.add(0, new ChatLine(updateCounter, ichatcomponent, chatLineId));
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
        return list.size();
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

    /**
     * Adds this string to the list of sent messages, for recall using the up/down arrow keys
     *
     * @param message The message to add in the sendMessage List
     */
    public void addToSentMessages(String message) {
        if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(message)) {
            this.sentMessages.add(message);
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
     *
     * @param amount The amount to scroll
     */
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

    /**
     * Gets the chat component under the mouse
     *
     * @param mouseX The x position of the mouse
     * @param mouseY The y position of the mouse
     */
    public IChatComponent getChatComponent(int mouseX, int mouseY) {
        if (!this.getChatOpen()) {
            return null;
        } else {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int i = scaledresolution.getScaleFactor();
            float f = this.getChatScale();
            int j = mouseX / i - 3;
            int k = mouseY / i - 27 - 8;
            j = MathHelper.floor_float((float) j / f);
            k = MathHelper.floor_float((float) k / f);
            final ArrayList<ChatLine> oldDrawn = new ArrayList<>(drawnChatLines);
            if (Client.INSTANCE.getModuleManager().getModule(IRCModule.class).isEnabled() && (mc.currentScreen instanceof GuiCustomChat || GuiChat.redirectToCustom)) {
                this.drawnChatLines.clear();
                this.drawnChatLines.addAll(ircLines.stream().filter(line -> line.getIrcChannel() == GuiCustomChat.currentTabId ||
                        line.getIrcChannel() == -1).collect(Collectors.toList()));
            }

            if (j >= 0 && k >= 0) {
                int l = Math.min(this.getLineCount(), this.drawnChatLines.size());

                if (j <= MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale()) && k < this.mc.fontRendererObj.FONT_HEIGHT * l + l) {
                    int i1 = k / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;

                    if (i1 >= 0 && i1 < this.drawnChatLines.size()) {
                        ChatLine chatline = this.drawnChatLines.get(i1);
                        int j1 = 0;

                        for (IChatComponent ichatcomponent : chatline.getChatComponent()) {
                            if (ichatcomponent instanceof ChatComponentText) {
                                j1 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText) ichatcomponent).getChatComponentText_TextValue(), false));

                                if (j1 > j) {
                                    drawnChatLines.clear();
                                    drawnChatLines.addAll(oldDrawn);
                                    return ichatcomponent;
                                }
                            }
                        }
                    }
                    drawnChatLines.clear();
                    drawnChatLines.addAll(oldDrawn);

                    return null;
                } else {
                    drawnChatLines.clear();
                    drawnChatLines.addAll(oldDrawn);
                    return null;
                }
            } else {
                drawnChatLines.clear();
                drawnChatLines.addAll(oldDrawn);
                return null;
            }
        }
    }

    /**
     * Returns true if the chat GUI is open
     */
    public boolean getChatOpen() {
        return this.mc.currentScreen instanceof GuiChat;
    }

    /**
     * finds and deletes a Chat line by ID
     *
     * @param id The ChatLine's id to delete
     */
    public void deleteChatLine(int id) {
        Iterator<ChatLine> iterator = this.drawnChatLines.iterator();

        while (iterator.hasNext()) {
            ChatLine chatline = iterator.next();

            if (chatline.getChatLineID() == id) {
                iterator.remove();
            }
        }

        iterator = this.chatLines.iterator();

        while (iterator.hasNext()) {
            ChatLine chatline1 = iterator.next();

            if (chatline1.getChatLineID() == id) {
                iterator.remove();
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

    /**
     * Returns the chatscale from mc.gameSettings.chatScale
     */
    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }

    public static int calculateChatboxWidth(float scale) {
        int i = 320;
        int j = 40;
        return MathHelper.floor_float(scale * (float) (i - j) + (float) j);
    }

    public static int calculateChatboxHeight(float scale) {
        int i = 180;
        int j = 20;
        return MathHelper.floor_float(scale * (float) (i - j) + (float) j);
    }

    public int getLineCount() {
        return this.getChatHeight() / 9;
    }
}
