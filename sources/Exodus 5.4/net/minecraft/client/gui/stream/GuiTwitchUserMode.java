/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  tv.twitch.chat.ChatUserInfo
 *  tv.twitch.chat.ChatUserMode
 *  tv.twitch.chat.ChatUserSubscription
 */
package net.minecraft.client.gui.stream;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IStream;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import tv.twitch.chat.ChatUserInfo;
import tv.twitch.chat.ChatUserMode;
import tv.twitch.chat.ChatUserSubscription;

public class GuiTwitchUserMode
extends GuiScreen {
    private final IStream stream;
    private int field_152334_t;
    private final IChatComponent field_152338_i;
    private static final EnumChatFormatting field_152331_a = EnumChatFormatting.DARK_GREEN;
    private static final EnumChatFormatting field_152335_f = EnumChatFormatting.RED;
    private final List<IChatComponent> field_152332_r = Lists.newArrayList();
    private final ChatUserInfo field_152337_h;
    private static final EnumChatFormatting field_152336_g = EnumChatFormatting.DARK_PURPLE;

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 0) {
                this.stream.func_152917_b("/ban " + this.field_152337_h.displayName);
            } else if (guiButton.id == 3) {
                this.stream.func_152917_b("/unban " + this.field_152337_h.displayName);
            } else if (guiButton.id == 2) {
                this.stream.func_152917_b("/mod " + this.field_152337_h.displayName);
            } else if (guiButton.id == 4) {
                this.stream.func_152917_b("/unmod " + this.field_152337_h.displayName);
            } else if (guiButton.id == 1) {
                this.stream.func_152917_b("/timeout " + this.field_152337_h.displayName);
            }
            this.mc.displayGuiScreen(null);
        }
    }

    public static List<IChatComponent> func_152328_a(Set<ChatUserMode> set, Set<ChatUserSubscription> set2, IStream iStream) {
        ChatComponentText chatComponentText;
        IChatComponent iChatComponent;
        String string = iStream == null ? null : iStream.func_152921_C();
        boolean bl = iStream != null && iStream.func_152927_B();
        ArrayList arrayList = Lists.newArrayList();
        for (ChatUserMode chatUserMode : set) {
            iChatComponent = GuiTwitchUserMode.func_152329_a(chatUserMode, string, bl);
            if (iChatComponent == null) continue;
            chatComponentText = new ChatComponentText("- ");
            chatComponentText.appendSibling(iChatComponent);
            arrayList.add(chatComponentText);
        }
        for (ChatUserSubscription chatUserSubscription : set2) {
            iChatComponent = GuiTwitchUserMode.func_152330_a(chatUserSubscription, string, bl);
            if (iChatComponent == null) continue;
            chatComponentText = new ChatComponentText("- ");
            chatComponentText.appendSibling(iChatComponent);
            arrayList.add(chatComponentText);
        }
        return arrayList;
    }

    public static IChatComponent func_152330_a(ChatUserSubscription chatUserSubscription, String string, boolean bl) {
        ChatComponentTranslation chatComponentTranslation = null;
        if (chatUserSubscription == ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER) {
            chatComponentTranslation = string == null ? new ChatComponentTranslation("stream.user.subscription.subscriber", new Object[0]) : (bl ? new ChatComponentTranslation("stream.user.subscription.subscriber.self", new Object[0]) : new ChatComponentTranslation("stream.user.subscription.subscriber.other", string));
            chatComponentTranslation.getChatStyle().setColor(field_152331_a);
        } else if (chatUserSubscription == ChatUserSubscription.TTV_CHAT_USERSUB_TURBO) {
            chatComponentTranslation = new ChatComponentTranslation("stream.user.subscription.turbo", new Object[0]);
            chatComponentTranslation.getChatStyle().setColor(field_152336_g);
        }
        return chatComponentTranslation;
    }

    @Override
    public void initGui() {
        int n = width / 3;
        int n2 = n - 130;
        this.buttonList.add(new GuiButton(1, n * 0 + n2 / 2, height - 70, 130, 20, I18n.format("stream.userinfo.timeout", new Object[0])));
        this.buttonList.add(new GuiButton(0, n * 1 + n2 / 2, height - 70, 130, 20, I18n.format("stream.userinfo.ban", new Object[0])));
        this.buttonList.add(new GuiButton(2, n * 2 + n2 / 2, height - 70, 130, 20, I18n.format("stream.userinfo.mod", new Object[0])));
        this.buttonList.add(new GuiButton(5, n * 0 + n2 / 2, height - 45, 130, 20, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(new GuiButton(3, n * 1 + n2 / 2, height - 45, 130, 20, I18n.format("stream.userinfo.unban", new Object[0])));
        this.buttonList.add(new GuiButton(4, n * 2 + n2 / 2, height - 45, 130, 20, I18n.format("stream.userinfo.unmod", new Object[0])));
        int n3 = 0;
        for (IChatComponent iChatComponent : this.field_152332_r) {
            n3 = Math.max(n3, this.fontRendererObj.getStringWidth(iChatComponent.getFormattedText()));
        }
        this.field_152334_t = width / 2 - n3 / 2;
    }

    public static IChatComponent func_152329_a(ChatUserMode chatUserMode, String string, boolean bl) {
        ChatComponentTranslation chatComponentTranslation = null;
        if (chatUserMode == ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) {
            chatComponentTranslation = new ChatComponentTranslation("stream.user.mode.administrator", new Object[0]);
            chatComponentTranslation.getChatStyle().setColor(field_152336_g);
        } else if (chatUserMode == ChatUserMode.TTV_CHAT_USERMODE_BANNED) {
            chatComponentTranslation = string == null ? new ChatComponentTranslation("stream.user.mode.banned", new Object[0]) : (bl ? new ChatComponentTranslation("stream.user.mode.banned.self", new Object[0]) : new ChatComponentTranslation("stream.user.mode.banned.other", string));
            chatComponentTranslation.getChatStyle().setColor(field_152335_f);
        } else if (chatUserMode == ChatUserMode.TTV_CHAT_USERMODE_BROADCASTER) {
            chatComponentTranslation = string == null ? new ChatComponentTranslation("stream.user.mode.broadcaster", new Object[0]) : (bl ? new ChatComponentTranslation("stream.user.mode.broadcaster.self", new Object[0]) : new ChatComponentTranslation("stream.user.mode.broadcaster.other", new Object[0]));
            chatComponentTranslation.getChatStyle().setColor(field_152331_a);
        } else if (chatUserMode == ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) {
            chatComponentTranslation = string == null ? new ChatComponentTranslation("stream.user.mode.moderator", new Object[0]) : (bl ? new ChatComponentTranslation("stream.user.mode.moderator.self", new Object[0]) : new ChatComponentTranslation("stream.user.mode.moderator.other", string));
            chatComponentTranslation.getChatStyle().setColor(field_152331_a);
        } else if (chatUserMode == ChatUserMode.TTV_CHAT_USERMODE_STAFF) {
            chatComponentTranslation = new ChatComponentTranslation("stream.user.mode.staff", new Object[0]);
            chatComponentTranslation.getChatStyle().setColor(field_152336_g);
        }
        return chatComponentTranslation;
    }

    public GuiTwitchUserMode(IStream iStream, ChatUserInfo chatUserInfo) {
        this.stream = iStream;
        this.field_152337_h = chatUserInfo;
        this.field_152338_i = new ChatComponentText(chatUserInfo.displayName);
        this.field_152332_r.addAll(GuiTwitchUserMode.func_152328_a(chatUserInfo.modes, chatUserInfo.subscriptions, iStream));
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_152338_i.getUnformattedText(), width / 2, 70, 0xFFFFFF);
        int n3 = 80;
        for (IChatComponent iChatComponent : this.field_152332_r) {
            this.drawString(this.fontRendererObj, iChatComponent.getFormattedText(), this.field_152334_t, n3, 0xFFFFFF);
            n3 += this.fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(n, n2, f);
    }
}

