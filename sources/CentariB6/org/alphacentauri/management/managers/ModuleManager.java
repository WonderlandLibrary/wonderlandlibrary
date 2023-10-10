package org.alphacentauri.management.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.alphacentauri.AC;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.modules.ModuleAntiBlindness;
import org.alphacentauri.modules.ModuleAntiBots;
import org.alphacentauri.modules.ModuleAutoArmor;
import org.alphacentauri.modules.ModuleAutoClicker;
import org.alphacentauri.modules.ModuleAutoPotion;
import org.alphacentauri.modules.ModuleAutoSoup;
import org.alphacentauri.modules.ModuleAutoSword;
import org.alphacentauri.modules.ModuleBowAimBot;
import org.alphacentauri.modules.ModuleChestAura;
import org.alphacentauri.modules.ModuleChestESP;
import org.alphacentauri.modules.ModuleChestStealer;
import org.alphacentauri.modules.ModuleClientFriends;
import org.alphacentauri.modules.ModuleCriticals;
import org.alphacentauri.modules.ModuleDamage;
import org.alphacentauri.modules.ModuleDamageIndicator;
import org.alphacentauri.modules.ModuleDebug;
import org.alphacentauri.modules.ModuleDolphin;
import org.alphacentauri.modules.ModuleFakeEvidence;
import org.alphacentauri.modules.ModuleFakeName;
import org.alphacentauri.modules.ModuleFastLadder;
import org.alphacentauri.modules.ModuleFly;
import org.alphacentauri.modules.ModuleFreecam;
import org.alphacentauri.modules.ModuleFucker;
import org.alphacentauri.modules.ModuleFullBright;
import org.alphacentauri.modules.ModuleInvWalk;
import org.alphacentauri.modules.ModuleItemEsp;
import org.alphacentauri.modules.ModuleJesus;
import org.alphacentauri.modules.ModuleKick;
import org.alphacentauri.modules.ModuleKillAura;
import org.alphacentauri.modules.ModuleNameTags;
import org.alphacentauri.modules.ModuleNoBob;
import org.alphacentauri.modules.ModuleNoFall;
import org.alphacentauri.modules.ModuleNoFriends;
import org.alphacentauri.modules.ModuleNoIRC;
import org.alphacentauri.modules.ModuleNoRotations;
import org.alphacentauri.modules.ModuleNoScoreboard;
import org.alphacentauri.modules.ModuleNoSlowdown;
import org.alphacentauri.modules.ModuleNoSwing;
import org.alphacentauri.modules.ModuleNoWeather;
import org.alphacentauri.modules.ModulePanic;
import org.alphacentauri.modules.ModulePingSpoof;
import org.alphacentauri.modules.ModulePlayerESP;
import org.alphacentauri.modules.ModuleSafeWalk;
import org.alphacentauri.modules.ModuleScaffold;
import org.alphacentauri.modules.ModuleServerCrasher;
import org.alphacentauri.modules.ModuleSmoothAimbot;
import org.alphacentauri.modules.ModuleSpammer;
import org.alphacentauri.modules.ModuleSpeed;
import org.alphacentauri.modules.ModuleSprint;
import org.alphacentauri.modules.ModuleStep;
import org.alphacentauri.modules.ModuleStrafe;
import org.alphacentauri.modules.ModuleTP;
import org.alphacentauri.modules.ModuleTeam;
import org.alphacentauri.modules.ModuleTimer;
import org.alphacentauri.modules.ModuleTower;
import org.alphacentauri.modules.ModuleTracers;
import org.alphacentauri.modules.ModuleTrajectories;
import org.alphacentauri.modules.ModuleTriggerBot;
import org.alphacentauri.modules.ModuleTrueSight;
import org.alphacentauri.modules.ModuleVelocity;

public class ModuleManager {
   public HashMap modules = new HashMap();

   public ModuleManager() {
      this.registerModules();
   }

   public Module get(Class moduleClass) {
      return (Module)this.modules.get(moduleClass);
   }

   public Collection all() {
      return this.modules.values();
   }

   public ArrayList allEnabled() {
      return (ArrayList)this.modules.values().stream().filter(Module::isEnabled).collect(Collectors.toCollection(ArrayList::<init>));
   }

   public void registerModules() {
      this.registerModule(new ModuleSprint());
      this.registerModule(new ModuleKillAura());
      this.registerModule(new ModuleFullBright());
      this.registerModule(new ModuleVelocity());
      this.registerModule(new ModuleDolphin());
      this.registerModule(new ModuleNoWeather());
      this.registerModule(new ModuleAntiBots());
      this.registerModule(new ModuleTracers());
      this.registerModule(new ModuleTrueSight());
      this.registerModule(new ModuleSpeed());
      this.registerModule(new ModulePlayerESP());
      this.registerModule(new ModuleNoSlowdown());
      this.registerModule(new ModuleInvWalk());
      this.registerModule(new ModuleChestESP());
      this.registerModule(new ModuleChestStealer());
      this.registerModule(new ModuleChestAura());
      this.registerModule(new ModuleDamageIndicator());
      this.registerModule(new ModuleServerCrasher());
      this.registerModule(new ModuleNoRotations());
      this.registerModule(new ModuleFly());
      this.registerModule(new ModuleJesus());
      this.registerModule(new ModulePingSpoof());
      this.registerModule(new ModuleSpammer());
      this.registerModule(new ModuleDamage());
      this.registerModule(new ModuleKick());
      this.registerModule(new ModuleCriticals());
      this.registerModule(new ModuleTeam());
      this.registerModule(new ModuleNameTags());
      this.registerModule(new ModuleTower());
      this.registerModule(new ModulePanic());
      this.registerModule(new ModuleNoFriends());
      this.registerModule(new ModuleScaffold());
      this.registerModule(new ModuleNoScoreboard());
      this.registerModule(new ModuleFakeName());
      this.registerModule(new ModuleSmoothAimbot());
      this.registerModule(new ModuleAutoClicker());
      this.registerModule(new ModuleTriggerBot());
      this.registerModule(new ModuleBowAimBot());
      this.registerModule(new ModuleStep());
      this.registerModule(new ModuleFucker());
      this.registerModule(new ModuleAutoArmor());
      this.registerModule(new ModuleAutoSoup());
      this.registerModule(new ModuleAutoPotion());
      this.registerModule(new ModuleFastLadder());
      this.registerModule(new ModuleSafeWalk());
      this.registerModule(new ModuleNoBob());
      this.registerModule(new ModuleFreecam());
      this.registerModule(new ModuleNoFall());
      this.registerModule(new ModuleNoSwing());
      this.registerModule(new ModuleStrafe());
      this.registerModule(new ModuleTP());
      this.registerModule(new ModuleClientFriends());
      this.registerModule(new ModuleAntiBlindness());
      this.registerModule(new ModuleAutoSword());
      this.registerModule(new ModuleTimer());
      this.registerModule(new ModuleItemEsp());
      this.registerModule(new ModuleNoIRC());
      this.registerModule(new ModuleTrajectories());
      this.registerModule(new ModuleFakeEvidence());
      if(AC.isDebug()) {
         this.registerModule(new ModuleDebug());
      }

   }

   public ArrayList getOfCategory(Module.Category category) {
      return (ArrayList)this.modules.values().stream().filter((mod) -> {
         return mod.getCategory() == category;
      }).sorted((o1, o2) -> {
         return o1.getName().charAt(0) - o2.getName().charAt(0);
      }).collect(Collectors.toCollection(ArrayList::<init>));
   }

   public void registerModule(Module module) {
      this.modules.put(module.getClass(), module);
   }
}
