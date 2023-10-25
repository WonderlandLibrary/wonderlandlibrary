/*
 * Decompiled with CFR 0.152.
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
    private final Reason field_152326_h;
    private final IChatComponent field_152324_f = new ChatComponentTranslation("stream.unavailable.title", new Object[0]);
    private final GuiScreen parentScreen;
    private static final Logger field_152322_a = LogManager.getLogger();
    private final List<String> field_152323_r = Lists.newArrayList();
    private final List<ChatComponentTranslation> field_152327_i;

    public GuiStreamUnavailable(GuiScreen guiScreen, Reason reason) {
        this(guiScreen, reason, null);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 1) {
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
                    case LIBRARY_FAILURE: 
                    case INITIALIZATION_FAILURE: 
                    case UNKNOWN: {
                        this.func_152320_a("http://bugs.mojang.com/browse/MC");
                    }
                }
            }
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        int n3 = Math.max((int)((double)height * 0.85 / 2.0 - (double)((float)(this.field_152323_r.size() * this.fontRendererObj.FONT_HEIGHT) / 2.0f)), 50);
        this.drawCenteredString(this.fontRendererObj, this.field_152324_f.getFormattedText(), width / 2, n3 - this.fontRendererObj.FONT_HEIGHT * 2, 0xFFFFFF);
        for (String string : this.field_152323_r) {
            this.drawCenteredString(this.fontRendererObj, string, width / 2, n3, 0xA0A0A0);
            n3 += this.fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(n, n2, f);
    }

    public static void func_152321_a(GuiScreen guiScreen) {
        Minecraft minecraft = Minecraft.getMinecraft();
        IStream iStream = minecraft.getTwitchStream();
        if (!OpenGlHelper.framebufferSupported) {
            ArrayList arrayList = Lists.newArrayList();
            arrayList.add(new ChatComponentTranslation("stream.unavailable.no_fbo.version", GL11.glGetString((int)7938)));
            arrayList.add(new ChatComponentTranslation("stream.unavailable.no_fbo.blend", GLContext.getCapabilities().GL_EXT_blend_func_separate));
            arrayList.add(new ChatComponentTranslation("stream.unavailable.no_fbo.arb", GLContext.getCapabilities().GL_ARB_framebuffer_object));
            arrayList.add(new ChatComponentTranslation("stream.unavailable.no_fbo.ext", GLContext.getCapabilities().GL_EXT_framebuffer_object));
            minecraft.displayGuiScreen(new GuiStreamUnavailable(guiScreen, Reason.NO_FBO, arrayList));
        } else if (iStream instanceof NullStream) {
            if (((NullStream)iStream).func_152937_a().getMessage().contains("Can't load AMD 64-bit .dll on a IA 32-bit platform")) {
                minecraft.displayGuiScreen(new GuiStreamUnavailable(guiScreen, Reason.LIBRARY_ARCH_MISMATCH));
            } else {
                minecraft.displayGuiScreen(new GuiStreamUnavailable(guiScreen, Reason.LIBRARY_FAILURE));
            }
        } else if (!iStream.func_152928_D() && iStream.func_152912_E() == ErrorCode.TTV_EC_OS_TOO_OLD) {
            switch (Util.getOSType()) {
                case WINDOWS: {
                    minecraft.displayGuiScreen(new GuiStreamUnavailable(guiScreen, Reason.UNSUPPORTED_OS_WINDOWS));
                    break;
                }
                case OSX: {
                    minecraft.displayGuiScreen(new GuiStreamUnavailable(guiScreen, Reason.UNSUPPORTED_OS_MAC));
                    break;
                }
                default: {
                    minecraft.displayGuiScreen(new GuiStreamUnavailable(guiScreen, Reason.UNSUPPORTED_OS_OTHER));
                    break;
                }
            }
        } else if (!minecraft.getTwitchDetails().containsKey((Object)"twitch_access_token")) {
            if (minecraft.getSession().getSessionType() == Session.Type.LEGACY) {
                minecraft.displayGuiScreen(new GuiStreamUnavailable(guiScreen, Reason.ACCOUNT_NOT_MIGRATED));
            } else {
                minecraft.displayGuiScreen(new GuiStreamUnavailable(guiScreen, Reason.ACCOUNT_NOT_BOUND));
            }
        } else if (!iStream.func_152913_F()) {
            switch (iStream.func_152918_H()) {
                case INVALID_TOKEN: {
                    minecraft.displayGuiScreen(new GuiStreamUnavailable(guiScreen, Reason.FAILED_TWITCH_AUTH));
                    break;
                }
                default: {
                    minecraft.displayGuiScreen(new GuiStreamUnavailable(guiScreen, Reason.FAILED_TWITCH_AUTH_ERROR));
                    break;
                }
            }
        } else if (iStream.func_152912_E() != null) {
            List<ChatComponentTranslation> list = Arrays.asList(new ChatComponentTranslation("stream.unavailable.initialization_failure.extra", ErrorCode.getString((ErrorCode)iStream.func_152912_E())));
            minecraft.displayGuiScreen(new GuiStreamUnavailable(guiScreen, Reason.INITIALIZATION_FAILURE, list));
        } else {
            minecraft.displayGuiScreen(new GuiStreamUnavailable(guiScreen, Reason.UNKNOWN));
        }
    }

    private void func_152320_a(String string) {
        try {
            Class<?> clazz = Class.forName("java.awt.Desktop");
            Object object = clazz.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            clazz.getMethod("browse", URI.class).invoke(object, new URI(string));
        }
        catch (Throwable throwable) {
            field_152322_a.error("Couldn't open link", throwable);
        }
    }

    @Override
    public void onGuiClosed() {
    }

    public GuiStreamUnavailable(GuiScreen guiScreen, Reason reason, List<ChatComponentTranslation> list) {
        this.parentScreen = guiScreen;
        this.field_152326_h = reason;
        this.field_152327_i = list;
    }

    @Override
    public void initGui() {
        if (this.field_152323_r.isEmpty()) {
            this.field_152323_r.addAll(this.fontRendererObj.listFormattedStringToWidth(this.field_152326_h.func_152561_a().getFormattedText(), (int)((float)width * 0.75f)));
            if (this.field_152327_i != null) {
                this.field_152323_r.add("");
                for (ChatComponentTranslation chatComponentTranslation : this.field_152327_i) {
                    this.field_152323_r.add(chatComponentTranslation.getUnformattedTextForChat());
                }
            }
        }
        if (this.field_152326_h.func_152559_b() != null) {
            this.buttonList.add(new GuiButton(0, width / 2 - 155, height - 50, 150, 20, I18n.format("gui.cancel", new Object[0])));
            this.buttonList.add(new GuiButton(1, width / 2 - 155 + 160, height - 50, 150, 20, I18n.format(this.field_152326_h.func_152559_b().getFormattedText(), new Object[0])));
        } else {
            this.buttonList.add(new GuiButton(0, width / 2 - 75, height - 50, 150, 20, I18n.format("gui.cancel", new Object[0])));
        }
    }

    public static enum Reason {
        NO_FBO(new ChatComponentTranslation("stream.unavailable.no_fbo", new Object[0])),
        LIBRARY_ARCH_MISMATCH(new ChatComponentTranslation("stream.unavailable.library_arch_mismatch", new Object[0])),
        LIBRARY_FAILURE(new ChatComponentTranslation("stream.unavailable.library_failure", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])),
        UNSUPPORTED_OS_WINDOWS(new ChatComponentTranslation("stream.unavailable.not_supported.windows", new Object[0])),
        UNSUPPORTED_OS_MAC(new ChatComponentTranslation("stream.unavailable.not_supported.mac", new Object[0]), new ChatComponentTranslation("stream.unavailable.not_supported.mac.okay", new Object[0])),
        UNSUPPORTED_OS_OTHER(new ChatComponentTranslation("stream.unavailable.not_supported.other", new Object[0])),
        ACCOUNT_NOT_MIGRATED(new ChatComponentTranslation("stream.unavailable.account_not_migrated", new Object[0]), new ChatComponentTranslation("stream.unavailable.account_not_migrated.okay", new Object[0])),
        ACCOUNT_NOT_BOUND(new ChatComponentTranslation("stream.unavailable.account_not_bound", new Object[0]), new ChatComponentTranslation("stream.unavailable.account_not_bound.okay", new Object[0])),
        FAILED_TWITCH_AUTH(new ChatComponentTranslation("stream.unavailable.failed_auth", new Object[0]), new ChatComponentTranslation("stream.unavailable.failed_auth.okay", new Object[0])),
        FAILED_TWITCH_AUTH_ERROR(new ChatComponentTranslation("stream.unavailable.failed_auth_error", new Object[0])),
        INITIALIZATION_FAILURE(new ChatComponentTranslation("stream.unavailable.initialization_failure", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])),
        UNKNOWN(new ChatComponentTranslation("stream.unavailable.unknown", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0]));

        private final IChatComponent field_152574_m;
        private final IChatComponent field_152575_n;

        private Reason(IChatComponent iChatComponent, IChatComponent iChatComponent2) {
            this.field_152574_m = iChatComponent;
            this.field_152575_n = iChatComponent2;
        }

        public IChatComponent func_152559_b() {
            return this.field_152575_n;
        }

        private Reason(IChatComponent iChatComponent) {
            this(iChatComponent, null);
        }

        public IChatComponent func_152561_a() {
            return this.field_152574_m;
        }
    }
}

