/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GLContext
 *  tv.twitch.ErrorCode
 */
package net.minecraft.client.gui.stream;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Session;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import tv.twitch.ErrorCode;

public class GuiStreamUnavailable
extends GuiScreen {
    private static final Logger field_152322_a = LogManager.getLogger();
    private final IChatComponent field_152324_f = new ChatComponentTranslation("stream.unavailable.title", new Object[0]);
    private final GuiScreen field_152325_g;
    private final Reason field_152326_h;
    private final List field_152327_i;
    private final List field_152323_r = Lists.newArrayList();
    private static final String __OBFID = "CL_00001840";

    public GuiStreamUnavailable(GuiScreen p_i1070_1_, Reason p_i1070_2_) {
        this(p_i1070_1_, p_i1070_2_, null);
    }

    public GuiStreamUnavailable(GuiScreen p_i46311_1_, Reason p_i46311_2_, List p_i46311_3_) {
        this.field_152325_g = p_i46311_1_;
        this.field_152326_h = p_i46311_2_;
        this.field_152327_i = p_i46311_3_;
    }

    @Override
    public void initGui() {
        if (this.field_152323_r.isEmpty()) {
            this.field_152323_r.addAll(this.fontRendererObj.listFormattedStringToWidth(this.field_152326_h.func_152561_a().getFormattedText(), (int)((float)this.width * 0.75f)));
            if (this.field_152327_i != null) {
                this.field_152323_r.add("");
                for (ChatComponentTranslation var2 : this.field_152327_i) {
                    this.field_152323_r.add(var2.getUnformattedTextForChat());
                }
            }
        }
        if (this.field_152326_h.func_152559_b() != null) {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 50, 150, 20, I18n.format("gui.cancel", new Object[0])));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 155 + 160, this.height - 50, 150, 20, I18n.format(this.field_152326_h.func_152559_b().getFormattedText(), new Object[0])));
        } else {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 75, this.height - 50, 150, 20, I18n.format("gui.cancel", new Object[0])));
        }
    }

    @Override
    public void onGuiClosed() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int var4 = Math.max((int)((double)this.height * 0.85 / 2.0 - (double)((float)(this.field_152323_r.size() * this.fontRendererObj.FONT_HEIGHT) / 2.0f)), 50);
        this.drawCenteredString(this.fontRendererObj, this.field_152324_f.getFormattedText(), this.width / 2, var4 - this.fontRendererObj.FONT_HEIGHT * 2, 0xFFFFFF);
        for (String var6 : this.field_152323_r) {
            this.drawCenteredString(this.fontRendererObj, var6, this.width / 2, var4, 0xA0A0A0);
            var4 += this.fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                switch (this.field_152326_h) {
                    case ACCOUNT_NOT_BOUND: 
                    case FAILED_TWITCH_AUTH: {
                        this.func_152320_a("https://account.mojang.com/me/settings");
                        break;
                    }
                    case ACCOUNT_NOT_MIGRATED: {
                        this.func_152320_a("https://account.mojang.com/migrate");
                        break;
                    }
                    case UNSUPPORTED_OS_MAC: {
                        this.func_152320_a("http://www.apple.com/osx/");
                        break;
                    }
                    case UNKNOWN: 
                    case LIBRARY_FAILURE: 
                    case INITIALIZATION_FAILURE: {
                        this.func_152320_a("http://bugs.mojang.com/browse/MC");
                    }
                }
            }
            this.mc.displayGuiScreen(this.field_152325_g);
        }
    }

    private void func_152320_a(String p_152320_1_) {
        try {
            Class<?> var2 = Class.forName("java.awt.Desktop");
            Object var3 = var2.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            var2.getMethod("browse", URI.class).invoke(var3, new URI(p_152320_1_));
        }
        catch (Throwable var4) {
            field_152322_a.error("Couldn't open link", var4);
        }
    }

    public static void func_152321_a(GuiScreen p_152321_0_) {
        Minecraft var1 = Minecraft.getMinecraft();
        IStream var2 = var1.getTwitchStream();
        if (!OpenGlHelper.framebufferSupported) {
            ArrayList var3 = Lists.newArrayList();
            var3.add(new ChatComponentTranslation("stream.unavailable.no_fbo.version", GL11.glGetString((int)7938)));
            var3.add(new ChatComponentTranslation("stream.unavailable.no_fbo.blend", GLContext.getCapabilities().GL_EXT_blend_func_separate));
            var3.add(new ChatComponentTranslation("stream.unavailable.no_fbo.arb", GLContext.getCapabilities().GL_ARB_framebuffer_object));
            var3.add(new ChatComponentTranslation("stream.unavailable.no_fbo.ext", GLContext.getCapabilities().GL_EXT_framebuffer_object));
            var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.NO_FBO, var3));
        } else if (var2 instanceof NullStream) {
            if (((NullStream)var2).func_152937_a().getMessage().contains("Can't load AMD 64-bit .dll on a IA 32-bit platform")) {
                var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.LIBRARY_ARCH_MISMATCH));
            } else {
                var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.LIBRARY_FAILURE));
            }
        } else if (!var2.func_152928_D() && var2.func_152912_E() == ErrorCode.TTV_EC_OS_TOO_OLD) {
            switch (Util.getOSType()) {
                case WINDOWS: {
                    var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.UNSUPPORTED_OS_WINDOWS));
                    break;
                }
                case OSX: {
                    var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.UNSUPPORTED_OS_MAC));
                    break;
                }
                default: {
                    var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.UNSUPPORTED_OS_OTHER));
                    break;
                }
            }
        } else if (!var1.func_180509_L().containsKey((Object)"twitch_access_token")) {
            if (var1.getSession().getSessionType() == Session.Type.LEGACY) {
                var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.ACCOUNT_NOT_MIGRATED));
            } else {
                var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.ACCOUNT_NOT_BOUND));
            }
        } else if (!var2.func_152913_F()) {
            switch (var2.func_152918_H()) {
                case INVALID_TOKEN: {
                    var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.FAILED_TWITCH_AUTH));
                    break;
                }
                default: {
                    var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.FAILED_TWITCH_AUTH_ERROR));
                    break;
                }
            }
        } else if (var2.func_152912_E() != null) {
            List<ChatComponentTranslation> var4 = Arrays.asList(new ChatComponentTranslation("stream.unavailable.initialization_failure.extra", ErrorCode.getString((ErrorCode)var2.func_152912_E())));
            var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.INITIALIZATION_FAILURE, var4));
        } else {
            var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.UNKNOWN));
        }
    }

    public static enum Reason {
        NO_FBO("NO_FBO", 0, new ChatComponentTranslation("stream.unavailable.no_fbo", new Object[0])),
        LIBRARY_ARCH_MISMATCH("LIBRARY_ARCH_MISMATCH", 1, new ChatComponentTranslation("stream.unavailable.library_arch_mismatch", new Object[0])),
        LIBRARY_FAILURE("LIBRARY_FAILURE", 2, new ChatComponentTranslation("stream.unavailable.library_failure", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])),
        UNSUPPORTED_OS_WINDOWS("UNSUPPORTED_OS_WINDOWS", 3, new ChatComponentTranslation("stream.unavailable.not_supported.windows", new Object[0])),
        UNSUPPORTED_OS_MAC("UNSUPPORTED_OS_MAC", 4, new ChatComponentTranslation("stream.unavailable.not_supported.mac", new Object[0]), new ChatComponentTranslation("stream.unavailable.not_supported.mac.okay", new Object[0])),
        UNSUPPORTED_OS_OTHER("UNSUPPORTED_OS_OTHER", 5, new ChatComponentTranslation("stream.unavailable.not_supported.other", new Object[0])),
        ACCOUNT_NOT_MIGRATED("ACCOUNT_NOT_MIGRATED", 6, new ChatComponentTranslation("stream.unavailable.account_not_migrated", new Object[0]), new ChatComponentTranslation("stream.unavailable.account_not_migrated.okay", new Object[0])),
        ACCOUNT_NOT_BOUND("ACCOUNT_NOT_BOUND", 7, new ChatComponentTranslation("stream.unavailable.account_not_bound", new Object[0]), new ChatComponentTranslation("stream.unavailable.account_not_bound.okay", new Object[0])),
        FAILED_TWITCH_AUTH("FAILED_TWITCH_AUTH", 8, new ChatComponentTranslation("stream.unavailable.failed_auth", new Object[0]), new ChatComponentTranslation("stream.unavailable.failed_auth.okay", new Object[0])),
        FAILED_TWITCH_AUTH_ERROR("FAILED_TWITCH_AUTH_ERROR", 9, new ChatComponentTranslation("stream.unavailable.failed_auth_error", new Object[0])),
        INITIALIZATION_FAILURE("INITIALIZATION_FAILURE", 10, new ChatComponentTranslation("stream.unavailable.initialization_failure", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])),
        UNKNOWN("UNKNOWN", 11, new ChatComponentTranslation("stream.unavailable.unknown", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0]));

        private final IChatComponent field_152574_m;
        private final IChatComponent field_152575_n;
        private static final Reason[] $VALUES;
        private static final String __OBFID = "CL_00001838";

        static {
            $VALUES = new Reason[]{NO_FBO, LIBRARY_ARCH_MISMATCH, LIBRARY_FAILURE, UNSUPPORTED_OS_WINDOWS, UNSUPPORTED_OS_MAC, UNSUPPORTED_OS_OTHER, ACCOUNT_NOT_MIGRATED, ACCOUNT_NOT_BOUND, FAILED_TWITCH_AUTH, FAILED_TWITCH_AUTH_ERROR, INITIALIZATION_FAILURE, UNKNOWN};
        }

        private Reason(String p_i1066_1_, int p_i1066_2_, IChatComponent p_i1066_3_) {
            this(p_i1066_1_, p_i1066_2_, p_i1066_3_, null);
        }

        private Reason(String p_i1067_1_, int p_i1067_2_, IChatComponent p_i1067_3_, IChatComponent p_i1067_4_) {
            this.field_152574_m = p_i1067_3_;
            this.field_152575_n = p_i1067_4_;
        }

        public IChatComponent func_152561_a() {
            return this.field_152574_m;
        }

        public IChatComponent func_152559_b() {
            return this.field_152575_n;
        }
    }
}

