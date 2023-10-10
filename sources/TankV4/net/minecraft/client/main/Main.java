package net.minecraft.client.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.List;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class Main {
   public static void main(String[] var0) {
      System.setProperty("java.net.preferIPv4Stack", "true");
      OptionParser var1 = new OptionParser();
      var1.allowsUnrecognizedOptions();
      var1.accepts("demo");
      var1.accepts("fullscreen");
      var1.accepts("checkGlErrors");
      ArgumentAcceptingOptionSpec var2 = var1.accepts("server").withRequiredArg();
      ArgumentAcceptingOptionSpec var3 = var1.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565);
      ArgumentAcceptingOptionSpec var4 = var1.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."));
      ArgumentAcceptingOptionSpec var5 = var1.accepts("assetsDir").withRequiredArg().ofType(File.class);
      ArgumentAcceptingOptionSpec var6 = var1.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
      ArgumentAcceptingOptionSpec var7 = var1.accepts("proxyHost").withRequiredArg();
      ArgumentAcceptingOptionSpec var8 = var1.accepts("proxyPort").withRequiredArg().defaultsTo("8080").ofType(Integer.class);
      ArgumentAcceptingOptionSpec var9 = var1.accepts("proxyUser").withRequiredArg();
      ArgumentAcceptingOptionSpec var10 = var1.accepts("proxyPass").withRequiredArg();
      ArgumentAcceptingOptionSpec var11 = var1.accepts("username").withRequiredArg().defaultsTo("ItzSnakexyz");
      ArgumentAcceptingOptionSpec var12 = var1.accepts("uuid").withRequiredArg();
      ArgumentAcceptingOptionSpec var13 = var1.accepts("accessToken").withRequiredArg().required();
      ArgumentAcceptingOptionSpec var14 = var1.accepts("version").withRequiredArg().required();
      ArgumentAcceptingOptionSpec var15 = var1.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854);
      ArgumentAcceptingOptionSpec var16 = var1.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480);
      ArgumentAcceptingOptionSpec var17 = var1.accepts("userProperties").withRequiredArg().defaultsTo("{}");
      ArgumentAcceptingOptionSpec var18 = var1.accepts("profileProperties").withRequiredArg().defaultsTo("{}");
      ArgumentAcceptingOptionSpec var19 = var1.accepts("assetIndex").withRequiredArg();
      ArgumentAcceptingOptionSpec var20 = var1.accepts("userType").withRequiredArg().defaultsTo("legacy");
      NonOptionArgumentSpec var21 = var1.nonOptions();
      OptionSet var22 = var1.parse(var0);
      List var23 = var22.valuesOf((OptionSpec)var21);
      if (!var23.isEmpty()) {
         System.out.println("Completely ignored arguments: " + var23);
      }

      String var24 = (String)var22.valueOf((OptionSpec)var7);
      Proxy var25 = Proxy.NO_PROXY;
      if (var24 != null) {
         try {
            var25 = new Proxy(Type.SOCKS, new InetSocketAddress(var24, (Integer)var22.valueOf((OptionSpec)var8)));
         } catch (Exception var46) {
         }
      }

      String var26 = (String)var22.valueOf((OptionSpec)var9);
      String var27 = (String)var22.valueOf((OptionSpec)var10);
      if (!var25.equals(Proxy.NO_PROXY) && var26 != false && var27 != false) {
         Authenticator.setDefault(new Authenticator(var26, var27) {
            private final String val$s1;
            private final String val$s2;

            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(this.val$s1, this.val$s2.toCharArray());
            }

            {
               this.val$s1 = var1;
               this.val$s2 = var2;
            }
         });
      }

      int var28 = (Integer)var22.valueOf((OptionSpec)var15);
      int var29 = (Integer)var22.valueOf((OptionSpec)var16);
      boolean var30 = var22.has("fullscreen");
      boolean var31 = var22.has("checkGlErrors");
      boolean var32 = var22.has("demo");
      String var33 = (String)var22.valueOf((OptionSpec)var14);
      Gson var34 = (new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();
      PropertyMap var35 = (PropertyMap)var34.fromJson((String)var22.valueOf((OptionSpec)var17), PropertyMap.class);
      PropertyMap var36 = (PropertyMap)var34.fromJson((String)var22.valueOf((OptionSpec)var18), PropertyMap.class);
      File var37 = (File)var22.valueOf((OptionSpec)var4);
      File var38 = var22.has((OptionSpec)var5) ? (File)var22.valueOf((OptionSpec)var5) : new File(var37, "assets/");
      File var39 = var22.has((OptionSpec)var6) ? (File)var22.valueOf((OptionSpec)var6) : new File(var37, "resourcepacks/");
      String var40 = var22.has((OptionSpec)var12) ? (String)var12.value(var22) : (String)var11.value(var22);
      String var41 = var22.has((OptionSpec)var19) ? (String)var19.value(var22) : null;
      String var42 = (String)var22.valueOf((OptionSpec)var2);
      Integer var43 = (Integer)var22.valueOf((OptionSpec)var3);
      Session var44 = new Session((String)var11.value(var22), var40, (String)var13.value(var22), (String)var20.value(var22));
      GameConfiguration var45 = new GameConfiguration(new GameConfiguration.UserInformation(var44, var35, var36, var25), new GameConfiguration.DisplayInformation(var28, var29, var30, var31), new GameConfiguration.FolderInformation(var37, var39, var38, var41), new GameConfiguration.GameInformation(var32, var33), new GameConfiguration.ServerInformation(var42, var43));
      Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread") {
         public void run() {
            Minecraft.stopIntegratedServer();
         }
      });
      Thread.currentThread().setName("Client thread");
      (new Minecraft(var45)).run();
   }
}
