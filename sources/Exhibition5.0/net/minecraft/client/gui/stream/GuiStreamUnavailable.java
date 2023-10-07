// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.stream;

import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import net.minecraft.client.stream.IStream;
import java.util.Arrays;
import net.minecraft.util.Session;
import net.minecraft.util.Util;
import tv.twitch.ErrorCode;
import net.minecraft.client.stream.NullStream;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.Minecraft;
import java.net.URI;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import java.util.Collection;
import com.google.common.collect.Lists;
import net.minecraft.util.ChatComponentTranslation;
import java.util.List;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.GuiScreen;

public class GuiStreamUnavailable extends GuiScreen
{
    private static final Logger field_152322_a;
    private final IChatComponent field_152324_f;
    private final GuiScreen field_152325_g;
    private final Reason field_152326_h;
    private final List field_152327_i;
    private final List field_152323_r;
    private static final String __OBFID = "CL_00001840";
    
    public GuiStreamUnavailable(final GuiScreen p_i1070_1_, final Reason p_i1070_2_) {
        this(p_i1070_1_, p_i1070_2_, null);
    }
    
    public GuiStreamUnavailable(final GuiScreen p_i46311_1_, final Reason p_i46311_2_, final List p_i46311_3_) {
        this.field_152324_f = new ChatComponentTranslation("stream.unavailable.title", new Object[0]);
        this.field_152323_r = Lists.newArrayList();
        this.field_152325_g = p_i46311_1_;
        this.field_152326_h = p_i46311_2_;
        this.field_152327_i = p_i46311_3_;
    }
    
    @Override
    public void initGui() {
        if (this.field_152323_r.isEmpty()) {
            this.field_152323_r.addAll(this.fontRendererObj.listFormattedStringToWidth(this.field_152326_h.func_152561_a().getFormattedText(), (int)(this.width * 0.75f)));
            if (this.field_152327_i != null) {
                this.field_152323_r.add("");
                for (final ChatComponentTranslation var2 : this.field_152327_i) {
                    this.field_152323_r.add(var2.getUnformattedTextForChat());
                }
            }
        }
        if (this.field_152326_h.func_152559_b() != null) {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 50, 150, 20, I18n.format("gui.cancel", new Object[0])));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 155 + 160, this.height - 50, 150, 20, I18n.format(this.field_152326_h.func_152559_b().getFormattedText(), new Object[0])));
        }
        else {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 75, this.height - 50, 150, 20, I18n.format("gui.cancel", new Object[0])));
        }
    }
    
    @Override
    public void onGuiClosed() {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        int var4 = Math.max((int)(this.height * 0.85 / 2.0 - this.field_152323_r.size() * this.fontRendererObj.FONT_HEIGHT / 2.0f), 50);
        this.drawCenteredString(this.fontRendererObj, this.field_152324_f.getFormattedText(), this.width / 2, var4 - this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
        for (final String var6 : this.field_152323_r) {
            this.drawCenteredString(this.fontRendererObj, var6, this.width / 2, var4, 10526880);
            var4 += this.fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                switch (SwitchReason.field_152577_a[this.field_152326_h.ordinal()]) {
                    case 1:
                    case 2: {
                        this.func_152320_a("https://account.mojang.com/me/settings");
                        break;
                    }
                    case 3: {
                        this.func_152320_a("https://account.mojang.com/migrate");
                        break;
                    }
                    case 4: {
                        this.func_152320_a("http://www.apple.com/osx/");
                        break;
                    }
                    case 5:
                    case 6:
                    case 7: {
                        this.func_152320_a("http://bugs.mojang.com/browse/MC");
                        break;
                    }
                }
            }
            this.mc.displayGuiScreen(this.field_152325_g);
        }
    }
    
    private void func_152320_a(final String p_152320_1_) {
        try {
            final Class var2 = Class.forName("java.awt.Desktop");
            final Object var3 = var2.getMethod("getDesktop", (Class[])new Class[0]).invoke(null, new Object[0]);
            var2.getMethod("browse", URI.class).invoke(var3, new URI(p_152320_1_));
        }
        catch (Throwable var4) {
            GuiStreamUnavailable.field_152322_a.error("Couldn't open link", var4);
        }
    }
    
    public static void func_152321_a(final GuiScreen p_152321_0_) {
        final Minecraft var1 = Minecraft.getMinecraft();
        final IStream var2 = var1.getTwitchStream();
        if (!OpenGlHelper.framebufferSupported) {
            final ArrayList var3 = Lists.newArrayList();
            var3.add(new ChatComponentTranslation("stream.unavailable.no_fbo.version", new Object[] { GL11.glGetString(7938) }));
            var3.add(new ChatComponentTranslation("stream.unavailable.no_fbo.blend", new Object[] { GLContext.getCapabilities().GL_EXT_blend_func_separate }));
            var3.add(new ChatComponentTranslation("stream.unavailable.no_fbo.arb", new Object[] { GLContext.getCapabilities().GL_ARB_framebuffer_object }));
            var3.add(new ChatComponentTranslation("stream.unavailable.no_fbo.ext", new Object[] { GLContext.getCapabilities().GL_EXT_framebuffer_object }));
            var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.NO_FBO, var3));
        }
        else if (var2 instanceof NullStream) {
            if (((NullStream)var2).func_152937_a().getMessage().contains("Can't load AMD 64-bit .dll on a IA 32-bit platform")) {
                var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.LIBRARY_ARCH_MISMATCH));
            }
            else {
                var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.LIBRARY_FAILURE));
            }
        }
        else if (!var2.func_152928_D() && var2.func_152912_E() == ErrorCode.TTV_EC_OS_TOO_OLD) {
            switch (SwitchReason.field_152578_b[Util.getOSType().ordinal()]) {
                case 1: {
                    var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.UNSUPPORTED_OS_WINDOWS));
                    break;
                }
                case 2: {
                    var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.UNSUPPORTED_OS_MAC));
                    break;
                }
                default: {
                    var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.UNSUPPORTED_OS_OTHER));
                    break;
                }
            }
        }
        else if (!var1.func_180509_L().containsKey((Object)"twitch_access_token")) {
            if (var1.getSession().getSessionType() == Session.Type.LEGACY) {
                var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.ACCOUNT_NOT_MIGRATED));
            }
            else {
                var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.ACCOUNT_NOT_BOUND));
            }
        }
        else if (!var2.func_152913_F()) {
            switch (SwitchReason.field_152579_c[var2.func_152918_H().ordinal()]) {
                case 1: {
                    var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.FAILED_TWITCH_AUTH));
                    break;
                }
                default: {
                    var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.FAILED_TWITCH_AUTH_ERROR));
                    break;
                }
            }
        }
        else if (var2.func_152912_E() != null) {
            final List var4 = Arrays.asList(new ChatComponentTranslation("stream.unavailable.initialization_failure.extra", new Object[] { ErrorCode.getString(var2.func_152912_E()) }));
            var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.INITIALIZATION_FAILURE, var4));
        }
        else {
            var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.UNKNOWN));
        }
    }
    
    static {
        field_152322_a = LogManager.getLogger();
    }
    
    public enum Reason
    {
        NO_FBO("NO_FBO", 0, (IChatComponent)new ChatComponentTranslation("stream.unavailable.no_fbo", new Object[0])), 
        LIBRARY_ARCH_MISMATCH("LIBRARY_ARCH_MISMATCH", 1, (IChatComponent)new ChatComponentTranslation("stream.unavailable.library_arch_mismatch", new Object[0])), 
        LIBRARY_FAILURE("LIBRARY_FAILURE", 2, (IChatComponent)new ChatComponentTranslation("stream.unavailable.library_failure", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])), 
        UNSUPPORTED_OS_WINDOWS("UNSUPPORTED_OS_WINDOWS", 3, (IChatComponent)new ChatComponentTranslation("stream.unavailable.not_supported.windows", new Object[0])), 
        UNSUPPORTED_OS_MAC("UNSUPPORTED_OS_MAC", 4, (IChatComponent)new ChatComponentTranslation("stream.unavailable.not_supported.mac", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.not_supported.mac.okay", new Object[0])), 
        UNSUPPORTED_OS_OTHER("UNSUPPORTED_OS_OTHER", 5, (IChatComponent)new ChatComponentTranslation("stream.unavailable.not_supported.other", new Object[0])), 
        ACCOUNT_NOT_MIGRATED("ACCOUNT_NOT_MIGRATED", 6, (IChatComponent)new ChatComponentTranslation("stream.unavailable.account_not_migrated", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.account_not_migrated.okay", new Object[0])), 
        ACCOUNT_NOT_BOUND("ACCOUNT_NOT_BOUND", 7, (IChatComponent)new ChatComponentTranslation("stream.unavailable.account_not_bound", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.account_not_bound.okay", new Object[0])), 
        FAILED_TWITCH_AUTH("FAILED_TWITCH_AUTH", 8, (IChatComponent)new ChatComponentTranslation("stream.unavailable.failed_auth", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.failed_auth.okay", new Object[0])), 
        FAILED_TWITCH_AUTH_ERROR("FAILED_TWITCH_AUTH_ERROR", 9, (IChatComponent)new ChatComponentTranslation("stream.unavailable.failed_auth_error", new Object[0])), 
        INITIALIZATION_FAILURE("INITIALIZATION_FAILURE", 10, (IChatComponent)new ChatComponentTranslation("stream.unavailable.initialization_failure", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])), 
        UNKNOWN("UNKNOWN", 11, (IChatComponent)new ChatComponentTranslation("stream.unavailable.unknown", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0]));
        
        private final IChatComponent field_152574_m;
        private final IChatComponent field_152575_n;
        private static final Reason[] $VALUES;
        private static final String __OBFID = "CL_00001838";
        
        private Reason(final String p_i1066_1_, final int p_i1066_2_, final IChatComponent p_i1066_3_) {
            this(p_i1066_1_, p_i1066_2_, p_i1066_3_, null);
        }
        
        private Reason(final String p_i1067_1_, final int p_i1067_2_, final IChatComponent p_i1067_3_, final IChatComponent p_i1067_4_) {
            this.field_152574_m = p_i1067_3_;
            this.field_152575_n = p_i1067_4_;
        }
        
        public IChatComponent func_152561_a() {
            return this.field_152574_m;
        }
        
        public IChatComponent func_152559_b() {
            return this.field_152575_n;
        }
        
        static {
            $VALUES = new Reason[] { Reason.NO_FBO, Reason.LIBRARY_ARCH_MISMATCH, Reason.LIBRARY_FAILURE, Reason.UNSUPPORTED_OS_WINDOWS, Reason.UNSUPPORTED_OS_MAC, Reason.UNSUPPORTED_OS_OTHER, Reason.ACCOUNT_NOT_MIGRATED, Reason.ACCOUNT_NOT_BOUND, Reason.FAILED_TWITCH_AUTH, Reason.FAILED_TWITCH_AUTH_ERROR, Reason.INITIALIZATION_FAILURE, Reason.UNKNOWN };
        }
    }
    
    static final class SwitchReason
    {
        static final int[] field_152577_a;
        static final int[] field_152578_b;
        static final int[] field_152579_c;
        private static final String __OBFID = "CL_00001839";
        
        static {
            field_152579_c = new int[IStream.AuthFailureReason.values().length];
            try {
                SwitchReason.field_152579_c[IStream.AuthFailureReason.INVALID_TOKEN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchReason.field_152579_c[IStream.AuthFailureReason.ERROR.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            field_152578_b = new int[Util.EnumOS.values().length];
            try {
                SwitchReason.field_152578_b[Util.EnumOS.WINDOWS.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchReason.field_152578_b[Util.EnumOS.OSX.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            field_152577_a = new int[Reason.values().length];
            try {
                SwitchReason.field_152577_a[Reason.ACCOUNT_NOT_BOUND.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchReason.field_152577_a[Reason.FAILED_TWITCH_AUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                SwitchReason.field_152577_a[Reason.ACCOUNT_NOT_MIGRATED.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
            try {
                SwitchReason.field_152577_a[Reason.UNSUPPORTED_OS_MAC.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError8) {}
            try {
                SwitchReason.field_152577_a[Reason.UNKNOWN.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError9) {}
            try {
                SwitchReason.field_152577_a[Reason.LIBRARY_FAILURE.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError10) {}
            try {
                SwitchReason.field_152577_a[Reason.INITIALIZATION_FAILURE.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError11) {}
        }
    }
}
