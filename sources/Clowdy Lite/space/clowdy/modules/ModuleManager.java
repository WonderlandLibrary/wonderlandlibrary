package space.clowdy.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import space.clowdy.modules.impl.AntiAFK;
import space.clowdy.modules.impl.AntiDebug;
import space.clowdy.modules.impl.AutoRespawn;
import space.clowdy.modules.impl.AutoShiftTap;
import space.clowdy.modules.impl.Crasher;
import space.clowdy.modules.impl.FogColor;
import space.clowdy.modules.impl.FullBright;
import space.clowdy.modules.impl.GlowESP;
import space.clowdy.modules.impl.HUD;
import space.clowdy.modules.impl.HitBox;
import space.clowdy.modules.impl.NoFriendFire;
import space.clowdy.modules.impl.Particles;
import space.clowdy.modules.impl.SelfDestruct;
import space.clowdy.modules.impl.Sprint;
import space.clowdy.modules.impl.ViewModel;
import space.clowdy.modules.impl.Watermark;

public class ModuleManager {
     public static CopyOnWriteArrayList modules = new CopyOnWriteArrayList();

     public static void handleKeyboard(int key, int integer2) {
          if (integer2 == 1) {
               Iterator var2 = modules.iterator();

               while(var2.hasNext()) {
                    Module domingo4 = (Module)var2.next();
                    if (domingo4.getKey() == key && Minecraft.getInstance().currentScreen == null) {
                         domingo4.toggle();
                    }
               }
          }

     }

     public static void getModulesByName(String name) {
          Iterator var1 = modules.iterator();

          while(var1.hasNext()) {
               Module domingo3 = (Module)var1.next();
               if (Objects.equals(domingo3.getName(), name)) {
                    domingo3.toggle();
               }
          }

     }

     public static boolean getEnabledModulesByName(String name) {
          Iterator var1 = modules.iterator();

          Module domingo3;
          do {
               if (!var1.hasNext()) {
                    return false;
               }

               domingo3 = (Module)var1.next();
          } while(!domingo3.isEnabled() || !Objects.equals(domingo3.name, name));

          return true;
     }

     public static ArrayList getModulesByCategory(Category laquita) {
          ArrayList arrayList2 = new ArrayList();
          Iterator var2 = modules.iterator();

          while(var2.hasNext()) {
               Module domingo4 = (Module)var2.next();
               if (domingo4.getCategory().name().equalsIgnoreCase(laquita.name())) {
                    arrayList2.add(domingo4);
               }
          }

          return arrayList2;
     }

     public static void addModules() {
          modules.add(new FogColor());
          modules.add(new GlowESP());
          modules.add(new AntiAFK());
          modules.add(new ViewModel());
          modules.add(new AutoRespawn());
          modules.add(new HitBox());
          modules.add(new Particles());
          modules.add(new Sprint());
          modules.add(new SelfDestruct());
          modules.add(new FullBright());
          modules.add(new HUD());
          modules.add(new AntiDebug());
          modules.add(new AutoShiftTap());
          modules.add(new NoFriendFire());
          modules.add(new Watermark());
          modules.add(new Crasher());
     }
}
