package net.minecraft.client.gui.stream;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

public class GuiStreamUnavailable extends GuiScreen {
   private static final Logger field_152322_a = LogManager.getLogger();
   private final List field_152323_r;
   private final GuiStreamUnavailable.Reason field_152326_h;
   private final GuiScreen parentScreen;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$client$gui$stream$GuiStreamUnavailable$Reason;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$Util$EnumOS;
   private final IChatComponent field_152324_f;
   private final List field_152327_i;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$client$stream$IStream$AuthFailureReason;

   public GuiStreamUnavailable(GuiScreen var1, GuiStreamUnavailable.Reason var2, List var3) {
      this.field_152324_f = new ChatComponentTranslation("stream.unavailable.title", new Object[0]);
      this.field_152323_r = Lists.newArrayList();
      this.parentScreen = var1;
      this.field_152326_h = var2;
      this.field_152327_i = var3;
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      int var4 = Math.max((int)((double)height * 0.85D / 2.0D - (double)((float)(this.field_152323_r.size() * this.fontRendererObj.FONT_HEIGHT) / 2.0F)), 50);
      this.drawCenteredString(this.fontRendererObj, this.field_152324_f.getFormattedText(), width / 2, var4 - this.fontRendererObj.FONT_HEIGHT * 2, 16777215);

      for(Iterator var6 = this.field_152323_r.iterator(); var6.hasNext(); var4 += this.fontRendererObj.FONT_HEIGHT) {
         String var5 = (String)var6.next();
         this.drawCenteredString(this.fontRendererObj, var5, width / 2, var4, 10526880);
      }

      super.drawScreen(var1, var2, var3);
   }

   private void func_152320_a(String var1) {
      try {
         Class var2 = Class.forName("java.awt.Desktop");
         Object var3 = var2.getMethod("getDesktop").invoke((Object)null);
         var2.getMethod("browse", URI.class).invoke(var3, new URI(var1));
      } catch (Throwable var4) {
         field_152322_a.error("Couldn't open link", var4);
      }

   }

   static int[] $SWITCH_TABLE$net$minecraft$util$Util$EnumOS() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$Util$EnumOS;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[Util.EnumOS.values().length];

         try {
            var0[Util.EnumOS.LINUX.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[Util.EnumOS.OSX.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[Util.EnumOS.SOLARIS.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[Util.EnumOS.UNKNOWN.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[Util.EnumOS.WINDOWS.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$util$Util$EnumOS = var0;
         return var0;
      }
   }

   public static void func_152321_a(GuiScreen var0) {
      Minecraft var1 = Minecraft.getMinecraft();
      IStream var2 = var1.getTwitchStream();
      if (!OpenGlHelper.framebufferSupported) {
         ArrayList var3 = Lists.newArrayList();
         var3.add(new ChatComponentTranslation("stream.unavailable.no_fbo.version", new Object[]{GL11.glGetString(7938)}));
         var3.add(new ChatComponentTranslation("stream.unavailable.no_fbo.blend", new Object[]{GLContext.getCapabilities().GL_EXT_blend_func_separate}));
         var3.add(new ChatComponentTranslation("stream.unavailable.no_fbo.arb", new Object[]{GLContext.getCapabilities().GL_ARB_framebuffer_object}));
         var3.add(new ChatComponentTranslation("stream.unavailable.no_fbo.ext", new Object[]{GLContext.getCapabilities().GL_EXT_framebuffer_object}));
         var1.displayGuiScreen(new GuiStreamUnavailable(var0, GuiStreamUnavailable.Reason.NO_FBO, var3));
      } else if (var2 instanceof NullStream) {
         if (((NullStream)var2).func_152937_a().getMessage().contains("Can't load AMD 64-bit .dll on a IA 32-bit platform")) {
            var1.displayGuiScreen(new GuiStreamUnavailable(var0, GuiStreamUnavailable.Reason.LIBRARY_ARCH_MISMATCH));
         } else {
            var1.displayGuiScreen(new GuiStreamUnavailable(var0, GuiStreamUnavailable.Reason.LIBRARY_FAILURE));
         }
      } else if (!var2.func_152928_D() && var2.func_152912_E() == ErrorCode.TTV_EC_OS_TOO_OLD) {
         switch($SWITCH_TABLE$net$minecraft$util$Util$EnumOS()[Util.getOSType().ordinal()]) {
         case 3:
            var1.displayGuiScreen(new GuiStreamUnavailable(var0, GuiStreamUnavailable.Reason.UNSUPPORTED_OS_WINDOWS));
            break;
         case 4:
            var1.displayGuiScreen(new GuiStreamUnavailable(var0, GuiStreamUnavailable.Reason.UNSUPPORTED_OS_MAC));
            break;
         default:
            var1.displayGuiScreen(new GuiStreamUnavailable(var0, GuiStreamUnavailable.Reason.UNSUPPORTED_OS_OTHER));
         }
      } else if (!var1.getTwitchDetails().containsKey("twitch_access_token")) {
         if (var1.getSession().getSessionType() == Session.Type.LEGACY) {
            var1.displayGuiScreen(new GuiStreamUnavailable(var0, GuiStreamUnavailable.Reason.ACCOUNT_NOT_MIGRATED));
         } else {
            var1.displayGuiScreen(new GuiStreamUnavailable(var0, GuiStreamUnavailable.Reason.ACCOUNT_NOT_BOUND));
         }
      } else if (!var2.func_152913_F()) {
         switch($SWITCH_TABLE$net$minecraft$client$stream$IStream$AuthFailureReason()[var2.func_152918_H().ordinal()]) {
         case 1:
         default:
            var1.displayGuiScreen(new GuiStreamUnavailable(var0, GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH_ERROR));
            break;
         case 2:
            var1.displayGuiScreen(new GuiStreamUnavailable(var0, GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH));
         }
      } else if (var2.func_152912_E() != null) {
         List var5 = Arrays.asList(new ChatComponentTranslation("stream.unavailable.initialization_failure.extra", new Object[]{ErrorCode.getString(var2.func_152912_E())}));
         var1.displayGuiScreen(new GuiStreamUnavailable(var0, GuiStreamUnavailable.Reason.INITIALIZATION_FAILURE, var5));
      } else {
         var1.displayGuiScreen(new GuiStreamUnavailable(var0, GuiStreamUnavailable.Reason.UNKNOWN));
      }

   }

   public void initGui() {
      if (this.field_152323_r.isEmpty()) {
         this.field_152323_r.addAll(this.fontRendererObj.listFormattedStringToWidth(this.field_152326_h.func_152561_a().getFormattedText(), (int)((float)width * 0.75F)));
         if (this.field_152327_i != null) {
            this.field_152323_r.add("");
            Iterator var2 = this.field_152327_i.iterator();

            while(var2.hasNext()) {
               ChatComponentTranslation var1 = (ChatComponentTranslation)var2.next();
               this.field_152323_r.add(var1.getUnformattedTextForChat());
            }
         }
      }

      if (this.field_152326_h.func_152559_b() != null) {
         this.buttonList.add(new GuiButton(0, width / 2 - 155, height - 50, 150, 20, I18n.format("gui.cancel")));
         this.buttonList.add(new GuiButton(1, width / 2 - 155 + 160, height - 50, 150, 20, I18n.format(this.field_152326_h.func_152559_b().getFormattedText())));
      } else {
         this.buttonList.add(new GuiButton(0, width / 2 - 75, height - 50, 150, 20, I18n.format("gui.cancel")));
      }

   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id == 1) {
            switch($SWITCH_TABLE$net$minecraft$client$gui$stream$GuiStreamUnavailable$Reason()[this.field_152326_h.ordinal()]) {
            case 3:
            case 11:
            case 12:
               this.func_152320_a("http://bugs.mojang.com/browse/MC");
            case 4:
            case 6:
            case 10:
            default:
               break;
            case 5:
               this.func_152320_a("http://www.apple.com/osx/");
               break;
            case 7:
               this.func_152320_a("https://account.mojang.com/migrate");
               break;
            case 8:
            case 9:
               this.func_152320_a("https://account.mojang.com/me/settings");
            }
         }

         this.mc.displayGuiScreen(this.parentScreen);
      }

   }

   static int[] $SWITCH_TABLE$net$minecraft$client$gui$stream$GuiStreamUnavailable$Reason() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$client$gui$stream$GuiStreamUnavailable$Reason;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[GuiStreamUnavailable.Reason.values().length];

         try {
            var0[GuiStreamUnavailable.Reason.ACCOUNT_NOT_BOUND.ordinal()] = 8;
         } catch (NoSuchFieldError var12) {
         }

         try {
            var0[GuiStreamUnavailable.Reason.ACCOUNT_NOT_MIGRATED.ordinal()] = 7;
         } catch (NoSuchFieldError var11) {
         }

         try {
            var0[GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH.ordinal()] = 9;
         } catch (NoSuchFieldError var10) {
         }

         try {
            var0[GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH_ERROR.ordinal()] = 10;
         } catch (NoSuchFieldError var9) {
         }

         try {
            var0[GuiStreamUnavailable.Reason.INITIALIZATION_FAILURE.ordinal()] = 11;
         } catch (NoSuchFieldError var8) {
         }

         try {
            var0[GuiStreamUnavailable.Reason.LIBRARY_ARCH_MISMATCH.ordinal()] = 2;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[GuiStreamUnavailable.Reason.LIBRARY_FAILURE.ordinal()] = 3;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[GuiStreamUnavailable.Reason.NO_FBO.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[GuiStreamUnavailable.Reason.UNKNOWN.ordinal()] = 12;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[GuiStreamUnavailable.Reason.UNSUPPORTED_OS_MAC.ordinal()] = 5;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[GuiStreamUnavailable.Reason.UNSUPPORTED_OS_OTHER.ordinal()] = 6;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[GuiStreamUnavailable.Reason.UNSUPPORTED_OS_WINDOWS.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$client$gui$stream$GuiStreamUnavailable$Reason = var0;
         return var0;
      }
   }

   public void onGuiClosed() {
   }

   public GuiStreamUnavailable(GuiScreen var1, GuiStreamUnavailable.Reason var2) {
      this(var1, var2, (List)null);
   }

   static int[] $SWITCH_TABLE$net$minecraft$client$stream$IStream$AuthFailureReason() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$client$stream$IStream$AuthFailureReason;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[IStream.AuthFailureReason.values().length];

         try {
            var0[IStream.AuthFailureReason.ERROR.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[IStream.AuthFailureReason.INVALID_TOKEN.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$client$stream$IStream$AuthFailureReason = var0;
         return var0;
      }
   }

   public static enum Reason {
      ACCOUNT_NOT_BOUND(new ChatComponentTranslation("stream.unavailable.account_not_bound", new Object[0]), new ChatComponentTranslation("stream.unavailable.account_not_bound.okay", new Object[0])),
      FAILED_TWITCH_AUTH_ERROR(new ChatComponentTranslation("stream.unavailable.failed_auth_error", new Object[0]));

      private final IChatComponent field_152574_m;
      LIBRARY_FAILURE(new ChatComponentTranslation("stream.unavailable.library_failure", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])),
      UNSUPPORTED_OS_OTHER(new ChatComponentTranslation("stream.unavailable.not_supported.other", new Object[0]));

      private final IChatComponent field_152575_n;
      ACCOUNT_NOT_MIGRATED(new ChatComponentTranslation("stream.unavailable.account_not_migrated", new Object[0]), new ChatComponentTranslation("stream.unavailable.account_not_migrated.okay", new Object[0]));

      private static final GuiStreamUnavailable.Reason[] ENUM$VALUES = new GuiStreamUnavailable.Reason[]{NO_FBO, LIBRARY_ARCH_MISMATCH, LIBRARY_FAILURE, UNSUPPORTED_OS_WINDOWS, UNSUPPORTED_OS_MAC, UNSUPPORTED_OS_OTHER, ACCOUNT_NOT_MIGRATED, ACCOUNT_NOT_BOUND, FAILED_TWITCH_AUTH, FAILED_TWITCH_AUTH_ERROR, INITIALIZATION_FAILURE, UNKNOWN};
      LIBRARY_ARCH_MISMATCH(new ChatComponentTranslation("stream.unavailable.library_arch_mismatch", new Object[0])),
      UNSUPPORTED_OS_WINDOWS(new ChatComponentTranslation("stream.unavailable.not_supported.windows", new Object[0])),
      NO_FBO(new ChatComponentTranslation("stream.unavailable.no_fbo", new Object[0])),
      UNSUPPORTED_OS_MAC(new ChatComponentTranslation("stream.unavailable.not_supported.mac", new Object[0]), new ChatComponentTranslation("stream.unavailable.not_supported.mac.okay", new Object[0])),
      FAILED_TWITCH_AUTH(new ChatComponentTranslation("stream.unavailable.failed_auth", new Object[0]), new ChatComponentTranslation("stream.unavailable.failed_auth.okay", new Object[0])),
      INITIALIZATION_FAILURE(new ChatComponentTranslation("stream.unavailable.initialization_failure", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])),
      UNKNOWN(new ChatComponentTranslation("stream.unavailable.unknown", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0]));

      public IChatComponent func_152559_b() {
         return this.field_152575_n;
      }

      private Reason(IChatComponent var3) {
         this(var3, (IChatComponent)null);
      }

      public IChatComponent func_152561_a() {
         return this.field_152574_m;
      }

      private Reason(IChatComponent var3, IChatComponent var4) {
         this.field_152574_m = var3;
         this.field_152575_n = var4;
      }
   }
}
