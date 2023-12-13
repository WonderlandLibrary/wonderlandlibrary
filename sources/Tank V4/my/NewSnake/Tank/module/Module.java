package my.NewSnake.Tank.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.Tank.option.OptionManager;
import my.NewSnake.event.EventManager;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;

public class Module {
   private String displayName;
   private Module.Category category;
   public static Minecraft mc = Minecraft.getMinecraft();
   private int keybind;
   private String suffix;
   private boolean shown;
   private String id;
   private int slide = 0;
   private boolean enabled;

   public void setProperties(String var1, String var2, Module.Category var3, int var4, String var5, boolean var6) {
      this.id = var1;
      this.displayName = var2;
      this.category = var3;
      this.keybind = var4;
      this.suffix = var5;
      this.shown = var6;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void enable() {
      this.enabled = true;
      EventManager.register(this);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setSuffix(String var1) {
      this.suffix = var1;
   }

   public String getSuffix() {
      return this.suffix;
   }

   public int getKeybind() {
      return this.keybind;
   }

   public void toggle() {
      if (this.enabled) {
         this.disable();
      } else {
         this.enable();
      }

   }

   public String getId() {
      return this.id;
   }

   public int getSlide() {
      if (this.slide == -1) {
         this.slide = 0;
      }

      return this.slide;
   }

   public void setSlide(int var1) {
      this.slide = var1;
   }

   public void setKeybind(int var1) {
      this.keybind = var1;
   }

   public void setDisplayName(String var1) {
      this.displayName = var1;
   }

   public boolean isShown() {
      return this.shown;
   }

   public void setShown(boolean var1) {
      this.shown = var1;
   }

   public void disable() {
      this.enabled = false;
      EventManager.unregister(this);
   }

   public List getOptions() {
      ArrayList var1 = new ArrayList();
      Iterator var3 = OptionManager.getOptionList().iterator();

      while(var3.hasNext()) {
         Option var2 = (Option)var3.next();
         if (var2.getModule().equals(this)) {
            var1.add(var2);
         }
      }

      return var1;
   }

   public boolean drawDisplayName(float var1, float var2) {
      if (this.enabled && this.shown) {
         ClientUtils.clientFont().drawStringWithShadow(String.format("%s" + (this.suffix.length() > 0 ? " §4[%s]" : ""), this.displayName, this.suffix), (double)var1, (double)var2, 6908265);
         return true;
      } else {
         return false;
      }
   }

   public Module getInstance() {
      Iterator var2 = ModuleManager.getModules().iterator();

      while(var2.hasNext()) {
         Module var1 = (Module)var2.next();
         if (var1.getClass().equals(this.getClass())) {
            return var1;
         }
      }

      return null;
   }

   public void postInitialize() {
   }

   public void preInitialize() {
   }

   public Module.Category getCategory() {
      return this.category;
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.TYPE})
   public @interface Mod {
      boolean shown() default true;

      int keybind() default -1;

      boolean enabled() default false;

      String displayName() default "null";

      String suffix() default "";
   }

   public static enum Category {
      private static final Module.Category[] ENUM$VALUES = new Module.Category[]{COMBAT, MOVEMENT, PLAYER, REGENS, RENDER, WORLD};
      PLAYER("PLAYER", 3),
      MOVEMENT("MOVEMENT", 2),
      REGENS("REGENS", 4),
      RENDER("RENDER", 5),
      COMBAT("COMBAT", 1),
      WORLD("WORLD", 6);

      private Category(String var3, int var4) {
      }
   }
}
