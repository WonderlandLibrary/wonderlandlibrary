package team.massacre.api.ui.main.components.tab;

import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.util.ResourceLocation;
import optifine.ReflectorForge;
import team.massacre.api.ui.framework.StaticallySizedImage;

public enum Tab {
   RAGE("rage.PNG", 32.0F),
   ANTI_AIM("antiaim.PNG", 32.0F),
   LEGIT("legit.PNG", 32.0F),
   VISUALS("visuals.PNG", 28.0F),
   SETTINGS("settings.PNG", 26.0F),
   PLAYER("player.PNG", 24.0F),
   SAVE("save.PNG", 24.0F);

   private final StaticallySizedImage icon;
   private final float renderSize;

   private Tab(String name, float renderSize) {
      try {
         this.icon = new StaticallySizedImage(ImageIO.read(getResourceStream(new ResourceLocation("massacre/gui/icons/" + name))), 8);
      } catch (IOException var6) {
         throw new IllegalArgumentException("Resource does not exist.");
      }

      this.renderSize = renderSize;
   }

   private static InputStream getResourceStream(ResourceLocation location) {
      String s = "/assets/" + location.getResourceDomain() + "/" + location.getResourcePath();
      InputStream inputstream = ReflectorForge.getOptiFineResourceStream(s);
      return inputstream != null ? inputstream : Tab.class.getResourceAsStream(s);
   }

   public float getRenderSize() {
      return this.renderSize;
   }

   public StaticallySizedImage getIcon() {
      return this.icon;
   }
}
