package cn.hanabi.utils;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.awt.Point;
import java.net.Proxy;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

public class Utils {
   private static final Random RANDOM = new Random();

   public static int random(int min, int max) {
      return max <= min ? min : RANDOM.nextInt(max - min) + min;
   }

   public static Session createSession(String username, String password, @NotNull Proxy proxy) throws Exception {
      YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(proxy, "");
      YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
      auth.setUsername(username);
      auth.setPassword(password);
      auth.logIn();
      return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
   }

   public static double getDirection() {
      Minecraft mc = Minecraft.getMinecraft();
      float yaw = mc.thePlayer.rotationYaw;
      if (mc.thePlayer.moveForward < 0.0F) {
         yaw += 180.0F;
      }

      float forward = 1.0F;
      if (mc.thePlayer.moveForward < 0.0F) {
         forward = -0.5F;
      } else if (mc.thePlayer.moveForward > 0.0F) {
         forward = 0.5F;
      }

      if (mc.thePlayer.moveStrafing > 0.0F) {
         yaw -= 90.0F * forward;
      }

      if (mc.thePlayer.moveStrafing < 0.0F) {
         yaw += 90.0F * forward;
      }

      return Math.toRadians((double)yaw);
   }

   public static Point calculateMouseLocation() {
      Minecraft minecraft = Minecraft.getMinecraft();
      int scale = minecraft.gameSettings.guiScale;
      if (scale == 0) {
         scale = 1000;
      }

      int scaleFactor;
      for(scaleFactor = 0; scaleFactor < scale && minecraft.displayWidth / (scaleFactor + 1) >= 320 && minecraft.displayHeight / (scaleFactor + 1) >= 240; ++scaleFactor) {
      }

      return new Point(Mouse.getX() / scaleFactor, minecraft.displayHeight / scaleFactor - Mouse.getY() / scaleFactor - 1);
   }
}
