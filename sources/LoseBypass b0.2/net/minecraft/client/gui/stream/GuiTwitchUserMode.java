/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  tv.twitch.chat.ChatUserInfo
 *  tv.twitch.chat.ChatUserMode
 *  tv.twitch.chat.ChatUserSubscription
 */
package net.minecraft.client.gui.stream;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
    private static final EnumChatFormatting field_152331_a = EnumChatFormatting.DARK_GREEN;
    private static final EnumChatFormatting field_152335_f = EnumChatFormatting.RED;
    private static final EnumChatFormatting field_152336_g = EnumChatFormatting.DARK_PURPLE;
    private final ChatUserInfo field_152337_h;
    private final IChatComponent field_152338_i;
    private final List<IChatComponent> field_152332_r = Lists.newArrayList();
    private final IStream stream;
    private int field_152334_t;

    public GuiTwitchUserMode(IStream streamIn, ChatUserInfo p_i1064_2_) {
        this.stream = streamIn;
        this.field_152337_h = p_i1064_2_;
        this.field_152338_i = new ChatComponentText(p_i1064_2_.displayName);
        this.field_152332_r.addAll(GuiTwitchUserMode.func_152328_a(p_i1064_2_.modes, p_i1064_2_.subscriptions, streamIn));
    }

    public static List<IChatComponent> func_152328_a(Set<ChatUserMode> p_152328_0_, Set<ChatUserSubscription> p_152328_1_, IStream p_152328_2_) {
        String s = p_152328_2_ == null ? null : p_152328_2_.func_152921_C();
        boolean flag = p_152328_2_ != null && p_152328_2_.func_152927_B();
        ArrayList<IChatComponent> list = Lists.newArrayList();
        for (ChatUserMode chatusermode : p_152328_0_) {
            IChatComponent ichatcomponent = GuiTwitchUserMode.func_152329_a(chatusermode, s, flag);
            if (ichatcomponent == null) continue;
            ChatComponentText ichatcomponent1 = new ChatComponentText("- ");
            ichatcomponent1.appendSibling(ichatcomponent);
            list.add(ichatcomponent1);
        }
        Iterator<ChatUserMode> iterator = p_152328_1_.iterator();
        while (iterator.hasNext()) {
            ChatUserSubscription chatusersubscription = (ChatUserSubscription)iterator.next();
            IChatComponent ichatcomponent2 = GuiTwitchUserMode.func_152330_a(chatusersubscription, s, flag);
            if (ichatcomponent2 == null) continue;
            ChatComponentText ichatcomponent3 = new ChatComponentText("- ");
            ichatcomponent3.appendSibling(ichatcomponent2);
            list.add(ichatcomponent3);
        }
        return list;
    }

    public static IChatComponent func_152330_a(ChatUserSubscription p_152330_0_, String p_152330_1_, boolean p_152330_2_) {
        ChatComponentTranslation ichatcomponent = null;
        if (p_152330_0_ != ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER) {
            if (p_152330_0_ != ChatUserSubscription.TTV_CHAT_USERSUB_TURBO) return ichatcomponent;
            ichatcomponent = new ChatComponentTranslation("stream.user.subscription.turbo", new Object[0]);
            ichatcomponent.getChatStyle().setColor(field_152336_g);
            return ichatcomponent;
        }
        ichatcomponent = p_152330_1_ == null ? new ChatComponentTranslation("stream.user.subscription.subscriber", new Object[0]) : (p_152330_2_ ? new ChatComponentTranslation("stream.user.subscription.subscriber.self", new Object[0]) : new ChatComponentTranslation("stream.user.subscription.subscriber.other", p_152330_1_));
        ichatcomponent.getChatStyle().setColor(field_152331_a);
        return ichatcomponent;
    }

    public static IChatComponent func_152329_a(ChatUserMode p_152329_0_, String p_152329_1_, boolean p_152329_2_) {
        ChatComponentTranslation ichatcomponent = null;
        if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) {
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.administrator", new Object[0]);
            ichatcomponent.getChatStyle().setColor(field_152336_g);
            return ichatcomponent;
        }
        if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BANNED) {
            ichatcomponent = p_152329_1_ == null ? new ChatComponentTranslation("stream.user.mode.banned", new Object[0]) : (p_152329_2_ ? new ChatComponentTranslation("stream.user.mode.banned.self", new Object[0]) : new ChatComponentTranslation("stream.user.mode.banned.other", p_152329_1_));
            ichatcomponent.getChatStyle().setColor(field_152335_f);
            return ichatcomponent;
        }
        if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BROADCASTER) {
            ichatcomponent = p_152329_1_ == null ? new ChatComponentTranslation("stream.user.mode.broadcaster", new Object[0]) : (p_152329_2_ ? new ChatComponentTranslation("stream.user.mode.broadcaster.self", new Object[0]) : new ChatComponentTranslation("stream.user.mode.broadcaster.other", new Object[0]));
            ichatcomponent.getChatStyle().setColor(field_152331_a);
            return ichatcomponent;
        }
        if (p_152329_0_ != ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) {
            if (p_152329_0_ != ChatUserMode.TTV_CHAT_USERMODE_STAFF) return ichatcomponent;
            ichatcomponent = new ChatComponentTranslation("stream.user.mode.staff", new Object[0]);
            ichatcomponent.getChatStyle().setColor(field_152336_g);
            return ichatcomponent;
        }
        ichatcomponent = p_152329_1_ == null ? new ChatComponentTranslation("stream.user.mode.moderator", new Object[0]) : (p_152329_2_ ? new ChatComponentTranslation("stream.user.mode.moderator.self", new Object[0]) : new ChatComponentTranslation("stream.user.mode.moderator.other", p_152329_1_));
        ichatcomponent.getChatStyle().setColor(field_152331_a);
        return ichatcomponent;
    }

    @Override
    public void initGui() {
        int i = this.width / 3;
        int j = i - 130;
        this.buttonList.add(new GuiButton(1, i * 0 + j / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.timeout", new Object[0])));
        this.buttonList.add(new GuiButton(0, i * 1 + j / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.ban", new Object[0])));
        this.buttonList.add(new GuiButton(2, i * 2 + j / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.mod", new Object[0])));
        this.buttonList.add(new GuiButton(5, i * 0 + j / 2, this.height - 45, 130, 20, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(new GuiButton(3, i * 1 + j / 2, this.height - 45, 130, 20, I18n.format("stream.userinfo.unban", new Object[0])));
        this.buttonList.add(new GuiButton(4, i * 2 + j / 2, this.height - 45, 130, 20, I18n.format("stream.userinfo.unmod", new Object[0])));
        int k = 0;
        Iterator<IChatComponent> iterator = this.field_152332_r.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.field_152334_t = this.width / 2 - k / 2;
                return;
            }
            IChatComponent ichatcomponent = iterator.next();
            k = Math.max(k, this.fontRendererObj.getStringWidth(ichatcomponent.getFormattedText()));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (!button.enabled) return;
        if (button.id == 0) {
            this.stream.func_152917_b("/ban " + this.field_152337_h.displayName);
        } else if (button.id == 3) {
            this.stream.func_152917_b("/unban " + this.field_152337_h.displayName);
        } else if (button.id == 2) {
            this.stream.func_152917_b("/mod " + this.field_152337_h.displayName);
        } else if (button.id == 4) {
            this.stream.func_152917_b("/unmod " + this.field_152337_h.displayName);
        } else if (button.id == 1) {
            this.stream.func_152917_b("/timeout " + this.field_152337_h.displayName);
        }
        this.mc.displayGuiScreen(null);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_152338_i.getUnformattedText(), this.width / 2, 70, 0xFFFFFF);
        int i = 80;
        Iterator<IChatComponent> iterator = this.field_152332_r.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                super.drawScreen(mouseX, mouseY, partialTicks);
                return;
            }
            IChatComponent ichatcomponent = iterator.next();
            this.drawString(this.fontRendererObj, ichatcomponent.getFormattedText(), this.field_152334_t, i, 0xFFFFFF);
            i += this.fontRendererObj.FONT_HEIGHT;
        }
    }
}

