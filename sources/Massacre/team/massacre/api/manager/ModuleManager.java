package team.massacre.api.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.impl.module.combat.AutoArmor;
import team.massacre.impl.module.combat.AutoPot;
import team.massacre.impl.module.combat.KillAura2;
import team.massacre.impl.module.combat.Reach;
import team.massacre.impl.module.combat.TargetStrafe;
import team.massacre.impl.module.combat.Velocity;
import team.massacre.impl.module.miscellaneous.AntiDesync;
import team.massacre.impl.module.miscellaneous.AutoPlay;
import team.massacre.impl.module.miscellaneous.ChatBypass;
import team.massacre.impl.module.miscellaneous.ChestStealer;
import team.massacre.impl.module.miscellaneous.Disabler;
import team.massacre.impl.module.miscellaneous.FastUse;
import team.massacre.impl.module.miscellaneous.InvManager;
import team.massacre.impl.module.movement.Flight;
import team.massacre.impl.module.movement.HentaiVoid;
import team.massacre.impl.module.movement.InvMove;
import team.massacre.impl.module.movement.Longjump;
import team.massacre.impl.module.movement.NoFall;
import team.massacre.impl.module.movement.NoSlowDown;
import team.massacre.impl.module.movement.Speed;
import team.massacre.impl.module.movement.Sprint;
import team.massacre.impl.module.playr.AimAssist;
import team.massacre.impl.module.playr.AutoClicker;
import team.massacre.impl.module.playr.NoRotate;
import team.massacre.impl.module.render.Ambience;
import team.massacre.impl.module.render.Animations;
import team.massacre.impl.module.render.ChestESP;
import team.massacre.impl.module.render.ClickUI;
import team.massacre.impl.module.render.ESP2D;
import team.massacre.impl.module.render.Fard;
import team.massacre.impl.module.render.Hud;
import team.massacre.impl.module.render.ItemPhysics;
import team.massacre.impl.module.render.NoHurtCam;
import team.massacre.impl.module.render.TargetHUD;
import team.massacre.impl.module.world.Scaffold2;

public class ModuleManager {
   public Map<Class, Module> moduleMap = new LinkedHashMap();

   public ModuleManager() {
      this.moduleMap.put(NoRotate.class, new NoRotate());
      this.moduleMap.put(HentaiVoid.class, new HentaiVoid());
      this.moduleMap.put(Animations.class, new Animations());
      this.moduleMap.put(Ambience.class, new Ambience());
      this.moduleMap.put(ChatBypass.class, new ChatBypass());
      this.moduleMap.put(AutoPlay.class, new AutoPlay());
      this.moduleMap.put(InvManager.class, new InvManager());
      this.moduleMap.put(AutoPot.class, new AutoPot());
      this.moduleMap.put(KillAura2.class, new KillAura2());
      this.moduleMap.put(TargetStrafe.class, new TargetStrafe());
      this.moduleMap.put(Reach.class, new Reach());
      this.moduleMap.put(AutoClicker.class, new AutoClicker());
      this.moduleMap.put(Velocity.class, new Velocity());
      this.moduleMap.put(AimAssist.class, new AimAssist());
      this.moduleMap.put(ClickUI.class, new ClickUI());
      this.moduleMap.put(Flight.class, new Flight());
      this.moduleMap.put(Sprint.class, new Sprint());
      this.moduleMap.put(TargetHUD.class, new TargetHUD());
      this.moduleMap.put(Scaffold2.class, new Scaffold2());
      this.moduleMap.put(FastUse.class, new FastUse());
      this.moduleMap.put(ChestStealer.class, new ChestStealer());
      this.moduleMap.put(AutoArmor.class, new AutoArmor());
      this.moduleMap.put(Fard.class, new Fard());
      this.moduleMap.put(ESP2D.class, new ESP2D());
      this.moduleMap.put(ChestESP.class, new ChestESP());
      this.moduleMap.put(NoHurtCam.class, new NoHurtCam());
      this.moduleMap.put(ItemPhysics.class, new ItemPhysics());
      this.moduleMap.put(NoFall.class, new NoFall());
      this.moduleMap.put(Speed.class, new Speed());
      this.moduleMap.put(Longjump.class, new Longjump());
      this.moduleMap.put(Disabler.class, new Disabler());
      this.moduleMap.put(NoSlowDown.class, new NoSlowDown());
      this.moduleMap.put(InvMove.class, new InvMove());
      this.moduleMap.put(AntiDesync.class, new AntiDesync());
      this.moduleMap.put(Hud.class, new Hud());
   }

   public Module getModule(String name) {
      return (Module)this.moduleMap.values().stream().filter((module) -> {
         return module.getName().equalsIgnoreCase(name);
      }).findFirst().orElse((Object)null);
   }

   public Module getModule(Class name) {
      return (Module)this.moduleMap.values().stream().filter((module) -> {
         return module.getClass().getName().equalsIgnoreCase(name.getName());
      }).findFirst().orElse((Object)null);
   }

   public List<Module> getModulesInCategory(Category category) {
      List<Module> m2 = new ArrayList();
      Iterator var3 = this.getModules().iterator();

      while(var3.hasNext()) {
         Module i = (Module)var3.next();
         if (i.getType() == category) {
            m2.add(i);
         }
      }

      return m2;
   }

   public Collection<Module> getModules() {
      return this.moduleMap.values();
   }
}
