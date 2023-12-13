/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module;

import java.util.ArrayList;
import java.util.Comparator;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.module.combat.AimAssist;
import me.wintware.client.module.combat.AntiBot;
import me.wintware.client.module.combat.AutoArmor;
import me.wintware.client.module.combat.AutoGapple;
import me.wintware.client.module.combat.AutoPot;
import me.wintware.client.module.combat.AutoShield;
import me.wintware.client.module.combat.AutoShift;
import me.wintware.client.module.combat.AutoTotem;
import me.wintware.client.module.combat.Criticals;
import me.wintware.client.module.combat.FastBow;
import me.wintware.client.module.combat.HitBox;
import me.wintware.client.module.combat.KeepSprint;
import me.wintware.client.module.combat.KillAura;
import me.wintware.client.module.combat.PushAttack;
import me.wintware.client.module.combat.Reach;
import me.wintware.client.module.combat.TargetStrafe;
import me.wintware.client.module.combat.TriggerBot;
import me.wintware.client.module.combat.Velocity;
import me.wintware.client.module.hud.ArreyList;
import me.wintware.client.module.hud.ClickGUI;
import me.wintware.client.module.hud.HUD;
import me.wintware.client.module.hud.Keystrokes;
import me.wintware.client.module.movement.AirJump;
import me.wintware.client.module.movement.AntiLevitation;
import me.wintware.client.module.movement.BoatFly;
import me.wintware.client.module.movement.ElytraFly;
import me.wintware.client.module.movement.Fly;
import me.wintware.client.module.movement.HighJump;
import me.wintware.client.module.movement.InvWalk;
import me.wintware.client.module.movement.Jesus;
import me.wintware.client.module.movement.LongJump;
import me.wintware.client.module.movement.NoJumpDelay;
import me.wintware.client.module.movement.NoSlowDown;
import me.wintware.client.module.movement.NoStepDelay;
import me.wintware.client.module.movement.Scaffold;
import me.wintware.client.module.movement.Speed;
import me.wintware.client.module.movement.Step;
import me.wintware.client.module.movement.Strafe;
import me.wintware.client.module.movement.Timer;
import me.wintware.client.module.movement.WaterSpeed;
import me.wintware.client.module.other.AutoAuth;
import me.wintware.client.module.other.ChatAppend;
import me.wintware.client.module.other.MemoryFix;
import me.wintware.client.module.other.XrayBypass;
import me.wintware.client.module.player.AntiAim;
import me.wintware.client.module.player.AntiCollision;
import me.wintware.client.module.player.AutoRespawn;
import me.wintware.client.module.player.AutoSprint;
import me.wintware.client.module.player.ChestStealer;
import me.wintware.client.module.player.MiddleClickPearl;
import me.wintware.client.module.player.NoBlockPush;
import me.wintware.client.module.player.NoClip;
import me.wintware.client.module.player.NoDamageTeam;
import me.wintware.client.module.player.NoFall;
import me.wintware.client.module.player.NoRotateSet;
import me.wintware.client.module.player.NoWaterPush;
import me.wintware.client.module.player.NoWeb;
import me.wintware.client.module.player.Parkour;
import me.wintware.client.module.player.SpeedMine;
import me.wintware.client.module.player.VClip;
import me.wintware.client.module.player.XCarry;
import me.wintware.client.module.visual.AntiRain;
import me.wintware.client.module.visual.AntiTotemAnimation;
import me.wintware.client.module.visual.ArmorHUD;
import me.wintware.client.module.visual.CameraClip;
import me.wintware.client.module.visual.Chams;
import me.wintware.client.module.visual.ChestESP;
import me.wintware.client.module.visual.Crosshair;
import me.wintware.client.module.visual.ESP;
import me.wintware.client.module.visual.EnchantEffect;
import me.wintware.client.module.visual.ExpandedArms;
import me.wintware.client.module.visual.FullBright;
import me.wintware.client.module.visual.HurtCam;
import me.wintware.client.module.visual.ItemESP;
import me.wintware.client.module.visual.NameTags;
import me.wintware.client.module.visual.NoArmorRender;
import me.wintware.client.module.visual.NoElementOverlay;
import me.wintware.client.module.visual.NoExpBar;
import me.wintware.client.module.visual.NoPotionDebug;
import me.wintware.client.module.visual.NoPumkinOverlay;
import me.wintware.client.module.visual.NoSmoothCamera;
import me.wintware.client.module.visual.Radar;
import me.wintware.client.module.visual.ScoreBoard;
import me.wintware.client.module.visual.ShadowESP;
import me.wintware.client.module.visual.SkeletonESP;
import me.wintware.client.module.visual.SkyColor;
import me.wintware.client.module.visual.Tracers;
import me.wintware.client.module.visual.Trajectories;
import me.wintware.client.module.visual.ViewModel;
import me.wintware.client.module.visual.WorldTime;
import me.wintware.client.module.world.BullingBot;
import me.wintware.client.module.world.FakeHack;
import me.wintware.client.module.world.FastPlace;
import me.wintware.client.module.world.FlagDetector;
import me.wintware.client.module.world.FreeCam;
import me.wintware.client.module.world.MCF;
import me.wintware.client.module.world.NameProtect;
import me.wintware.client.module.world.NoBreakDelay;
import me.wintware.client.module.world.NoSneakMotion;
import me.wintware.client.module.world.PingSpoff;
import me.wintware.client.module.world.SafeWalk;
import me.wintware.client.module.world.Spammer;
import net.minecraft.client.Minecraft;

public class ModuleManager {
    public ArrayList<Module> modules = new ArrayList();

    public ModuleManager() {
        this.modules.clear();
        this.modules.add(new WaterSpeed());
        this.modules.add(new AutoShield());
        this.modules.add(new NoSneakMotion());
        this.modules.add(new MiddleClickPearl());
        this.modules.add(new BoatFly());
        this.modules.add(new ExpandedArms());
        this.modules.add(new SpeedMine());
        this.modules.add(new NoStepDelay());
        this.modules.add(new ChatAppend());
        this.modules.add(new SkyColor());
        this.modules.add(new AimAssist());
        this.modules.add(new AirJump());
        this.modules.add(new AutoShift());
        this.modules.add(new HitBox());
        this.modules.add(new Reach());
        this.modules.add(new KeepSprint());
        this.modules.add(new TargetStrafe());
        this.modules.add(new Criticals());
        this.modules.add(new AutoGapple());
        this.modules.add(new Velocity());
        this.modules.add(new KillAura());
        this.modules.add(new AutoArmor());
        this.modules.add(new ChestStealer());
        this.modules.add(new ChestESP());
        this.modules.add(new AntiBot());
        this.modules.add(new FastBow());
        this.modules.add(new PushAttack());
        this.modules.add(new AutoTotem());
        this.modules.add(new TriggerBot());
        this.modules.add(new VClip());
        this.modules.add(new NoWaterPush());
        this.modules.add(new BullingBot());
        this.modules.add(new Timer());
        this.modules.add(new AntiAim());
        this.modules.add(new AutoPot());
        this.modules.add(new AutoSprint());
        this.modules.add(new HighJump());
        this.modules.add(new Speed());
        this.modules.add(new Jesus());
        this.modules.add(new Fly());
        this.modules.add(new NoSlowDown());
        this.modules.add(new XCarry());
        this.modules.add(new NoSmoothCamera());
        this.modules.add(new LongJump());
        this.modules.add(new AntiLevitation());
        this.modules.add(new ElytraFly());
        this.modules.add(new Scaffold());
        this.modules.add(new InvWalk());
        this.modules.add(new FastPlace());
        this.modules.add(new NoClip());
        this.modules.add(new NoFall());
        this.modules.add(new Strafe());
        this.modules.add(new MemoryFix());
        this.modules.add(new AutoAuth());
        this.modules.add(new NoDamageTeam());
        this.modules.add(new NoBlockPush());
        this.modules.add(new AutoRespawn());
        this.modules.add(new FlagDetector());
        this.modules.add(new SafeWalk());
        this.modules.add(new AntiRain());
        this.modules.add(new PingSpoff());
        this.modules.add(new Parkour());
        this.modules.add(new NoWeb());
        this.modules.add(new Step());
        this.modules.add(new ESP());
        this.modules.add(new MCF());
        this.modules.add(new XrayBypass());
        this.modules.add(new NoPotionDebug());
        this.modules.add(new NameTags());
        this.modules.add(new NoPumkinOverlay());
        this.modules.add(new NameProtect());
        this.modules.add(new SkeletonESP());
        this.modules.add(new Crosshair());
        this.modules.add(new AntiTotemAnimation());
        this.modules.add(new WorldTime());
        this.modules.add(new ScoreBoard());
        this.modules.add(new CameraClip());
        this.modules.add(new NoElementOverlay());
        this.modules.add(new HurtCam());
        this.modules.add(new FullBright());
        this.modules.add(new Tracers());
        this.modules.add(new EnchantEffect());
        this.modules.add(new ArmorHUD());
        this.modules.add(new NoJumpDelay());
        this.modules.add(new NoArmorRender());
        this.modules.add(new Chams());
        this.modules.add(new Spammer());
        this.modules.add(new NoBreakDelay());
        this.modules.add(new ShadowESP());
        this.modules.add(new FreeCam());
        this.modules.add(new NoExpBar());
        this.modules.add(new ViewModel());
        this.modules.add(new AntiCollision());
        this.modules.add(new NoRotateSet());
        this.modules.add(new ItemESP());
        this.modules.add(new Trajectories());
        this.modules.add(new Radar());
        this.modules.add(new ExpandedArms());
        this.modules.add(new ArreyList());
        this.modules.add(new ClickGUI());
        this.modules.add(new HUD());
        this.modules.add(new FakeHack());
        this.modules.add(new Keystrokes());
        this.modules.sort(Comparator.comparing(m -> Minecraft.getMinecraft().fontRenderer.getStringWidth(((Module)m).getName())).reversed());
    }

    public ArrayList<Module> getModules() {
        return this.modules;
    }

    public Module[] getModulesInCategory(Category category) {
        return this.modules.stream().filter(module -> module.getCategory() == category).toArray(Module[]::new);
    }

    public Module getModuleByName(String name) {
        for (Module m : this.modules) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }

    public Module getModuleByClass(Class<? extends Module> clazz) {
        for (Module module : this.getModules()) {
            if (module.getClass() != clazz) continue;
            return module;
        }
        return null;
    }
}

