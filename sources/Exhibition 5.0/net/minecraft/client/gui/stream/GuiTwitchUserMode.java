// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.stream;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentTranslation;
import java.util.Iterator;
import java.util.ArrayList;
import tv.twitch.chat.ChatUserSubscription;
import tv.twitch.chat.ChatUserMode;
import java.util.Collection;
import java.util.Set;
import net.minecraft.util.ChatComponentText;
import com.google.common.collect.Lists;
import net.minecraft.client.stream.IStream;
import java.util.List;
import net.minecraft.util.IChatComponent;
import tv.twitch.chat.ChatUserInfo;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.GuiScreen;

public class GuiTwitchUserMode extends GuiScreen
{
    private static final EnumChatFormatting field_152331_a;
    private static final EnumChatFormatting field_152335_f;
    private static final EnumChatFormatting field_152336_g;
    private final ChatUserInfo field_152337_h;
    private final IChatComponent field_152338_i;
    private final List field_152332_r;
    private final IStream field_152333_s;
    private int field_152334_t;
    private static final String __OBFID = "CL_00001837";
    
    public GuiTwitchUserMode(final IStream p_i1064_1_, final ChatUserInfo p_i1064_2_) {
        this.field_152332_r = Lists.newArrayList();
        this.field_152333_s = p_i1064_1_;
        this.field_152337_h = p_i1064_2_;
        this.field_152338_i = new ChatComponentText(p_i1064_2_.displayName);
        this.field_152332_r.addAll(func_152328_a(p_i1064_2_.modes, p_i1064_2_.subscriptions, p_i1064_1_));
    }
    
    public static List func_152328_a(final Set p_152328_0_, final Set p_152328_1_, final IStream p_152328_2_) {
        final String var3 = (p_152328_2_ == null) ? null : p_152328_2_.func_152921_C();
        final boolean var4 = p_152328_2_ != null && p_152328_2_.func_152927_B();
        final ArrayList var5 = Lists.newArrayList();
        for (final ChatUserMode var7 : p_152328_0_) {
            final IChatComponent var8 = func_152329_a(var7, var3, var4);
            if (var8 != null) {
                final ChatComponentText var9 = new ChatComponentText("- ");
                var9.appendSibling(var8);
                var5.add(var9);
            }
        }
        for (final ChatUserSubscription var10 : p_152328_1_) {
            final IChatComponent var8 = func_152330_a(var10, var3, var4);
            if (var8 != null) {
                final ChatComponentText var9 = new ChatComponentText("- ");
                var9.appendSibling(var8);
                var5.add(var9);
            }
        }
        return var5;
    }
    
    public static IChatComponent func_152330_a(final ChatUserSubscription p_152330_0_, final String p_152330_1_, final boolean p_152330_2_) {
        ChatComponentTranslation var3 = null;
        if (p_152330_0_ == ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER) {
            if (p_152330_1_ == null) {
                var3 = new ChatComponentTranslation("stream.user.subscription.subscriber", new Object[0]);
            }
            else if (p_152330_2_) {
                var3 = new ChatComponentTranslation("stream.user.subscription.subscriber.self", new Object[0]);
            }
            else {
                var3 = new ChatComponentTranslation("stream.user.subscription.subscriber.other", new Object[] { p_152330_1_ });
            }
            var3.getChatStyle().setColor(GuiTwitchUserMode.field_152331_a);
        }
        else if (p_152330_0_ == ChatUserSubscription.TTV_CHAT_USERSUB_TURBO) {
            var3 = new ChatComponentTranslation("stream.user.subscription.turbo", new Object[0]);
            var3.getChatStyle().setColor(GuiTwitchUserMode.field_152336_g);
        }
        return var3;
    }
    
    public static IChatComponent func_152329_a(final ChatUserMode p_152329_0_, final String p_152329_1_, final boolean p_152329_2_) {
        ChatComponentTranslation var3 = null;
        if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) {
            var3 = new ChatComponentTranslation("stream.user.mode.administrator", new Object[0]);
            var3.getChatStyle().setColor(GuiTwitchUserMode.field_152336_g);
        }
        else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BANNED) {
            if (p_152329_1_ == null) {
                var3 = new ChatComponentTranslation("stream.user.mode.banned", new Object[0]);
            }
            else if (p_152329_2_) {
                var3 = new ChatComponentTranslation("stream.user.mode.banned.self", new Object[0]);
            }
            else {
                var3 = new ChatComponentTranslation("stream.user.mode.banned.other", new Object[] { p_152329_1_ });
            }
            var3.getChatStyle().setColor(GuiTwitchUserMode.field_152335_f);
        }
        else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BROADCASTER) {
            if (p_152329_1_ == null) {
                var3 = new ChatComponentTranslation("stream.user.mode.broadcaster", new Object[0]);
            }
            else if (p_152329_2_) {
                var3 = new ChatComponentTranslation("stream.user.mode.broadcaster.self", new Object[0]);
            }
            else {
                var3 = new ChatComponentTranslation("stream.user.mode.broadcaster.other", new Object[0]);
            }
            var3.getChatStyle().setColor(GuiTwitchUserMode.field_152331_a);
        }
        else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) {
            if (p_152329_1_ == null) {
                var3 = new ChatComponentTranslation("stream.user.mode.moderator", new Object[0]);
            }
            else if (p_152329_2_) {
                var3 = new ChatComponentTranslation("stream.user.mode.moderator.self", new Object[0]);
            }
            else {
                var3 = new ChatComponentTranslation("stream.user.mode.moderator.other", new Object[] { p_152329_1_ });
            }
            var3.getChatStyle().setColor(GuiTwitchUserMode.field_152331_a);
        }
        else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_STAFF) {
            var3 = new ChatComponentTranslation("stream.user.mode.staff", new Object[0]);
            var3.getChatStyle().setColor(GuiTwitchUserMode.field_152336_g);
        }
        return var3;
    }
    
    @Override
    public void initGui() {
        final int var1 = this.width / 3;
        final int var2 = var1 - 130;
        this.buttonList.add(new GuiButton(1, var1 * 0 + var2 / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.timeout", new Object[0])));
        this.buttonList.add(new GuiButton(0, var1 * 1 + var2 / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.ban", new Object[0])));
        this.buttonList.add(new GuiButton(2, var1 * 2 + var2 / 2, this.height - 70, 130, 20, I18n.format("stream.userinfo.mod", new Object[0])));
        this.buttonList.add(new GuiButton(5, var1 * 0 + var2 / 2, this.height - 45, 130, 20, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(new GuiButton(3, var1 * 1 + var2 / 2, this.height - 45, 130, 20, I18n.format("stream.userinfo.unban", new Object[0])));
        this.buttonList.add(new GuiButton(4, var1 * 2 + var2 / 2, this.height - 45, 130, 20, I18n.format("stream.userinfo.unmod", new Object[0])));
        int var3 = 0;
        for (final IChatComponent var5 : this.field_152332_r) {
            var3 = Math.max(var3, this.fontRendererObj.getStringWidth(var5.getFormattedText()));
        }
        this.field_152334_t = this.width / 2 - var3 / 2;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                this.field_152333_s.func_152917_b("/ban " + this.field_152337_h.displayName);
            }
            else if (button.id == 3) {
                this.field_152333_s.func_152917_b("/unban " + this.field_152337_h.displayName);
            }
            else if (button.id == 2) {
                this.field_152333_s.func_152917_b("/mod " + this.field_152337_h.displayName);
            }
            else if (button.id == 4) {
                this.field_152333_s.func_152917_b("/unmod " + this.field_152337_h.displayName);
            }
            else if (button.id == 1) {
                this.field_152333_s.func_152917_b("/timeout " + this.field_152337_h.displayName);
            }
            this.mc.displayGuiScreen(null);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_152338_i.getUnformattedText(), this.width / 2, 70, 16777215);
        int var4 = 80;
        for (final IChatComponent var6 : this.field_152332_r) {
            this.drawString(this.fontRendererObj, var6.getFormattedText(), this.field_152334_t, var4, 16777215);
            var4 += this.fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    static {
        field_152331_a = EnumChatFormatting.DARK_GREEN;
        field_152335_f = EnumChatFormatting.RED;
        field_152336_g = EnumChatFormatting.DARK_PURPLE;
    }
}
