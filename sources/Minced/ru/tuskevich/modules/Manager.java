// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules;

import java.util.Iterator;
import java.util.List;
import java.util.Comparator;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.modules.impl.MISC.TestFeature;
import ru.tuskevich.modules.impl.PLAYER.CameraClip;
import ru.tuskevich.modules.impl.RENDER.WorldColor;
import ru.tuskevich.modules.impl.MISC.Disabler;
import ru.tuskevich.modules.impl.RENDER.Trails;
import ru.tuskevich.modules.impl.COMBAT.Criticals;
import ru.tuskevich.modules.impl.MOVEMENT.WaterSpeed;
import ru.tuskevich.modules.impl.MOVEMENT.WallClimb;
import ru.tuskevich.modules.impl.MISC.AutoBrew;
import ru.tuskevich.modules.impl.MOVEMENT.NoFall;
import ru.tuskevich.modules.impl.HUD.NoOverlay;
import ru.tuskevich.modules.impl.PLAYER.AutoPot;
import ru.tuskevich.modules.impl.RENDER.Tracers;
import ru.tuskevich.modules.impl.HUD.TargetHUD;
import ru.tuskevich.modules.impl.RENDER.FullBright;
import ru.tuskevich.modules.impl.MISC.ClientSound;
import ru.tuskevich.modules.impl.MOVEMENT.Strafe;
import ru.tuskevich.modules.impl.MOVEMENT.NoSlow;
import ru.tuskevich.modules.impl.HUD.Hud;
import ru.tuskevich.modules.impl.COMBAT.AutoApple;
import ru.tuskevich.modules.impl.PLAYER.GoldenAppleTimer;
import ru.tuskevich.modules.impl.MOVEMENT.Speed;
import ru.tuskevich.modules.impl.COMBAT.Velocity;
import ru.tuskevich.modules.impl.COMBAT.KillAura;
import ru.tuskevich.modules.impl.RENDER.HandTranslate;
import ru.tuskevich.modules.impl.PLAYER.NoDelay;
import ru.tuskevich.modules.impl.COMBAT.AutoArmor;
import ru.tuskevich.modules.impl.COMBAT.HitBox;
import ru.tuskevich.modules.impl.COMBAT.FastBow;
import ru.tuskevich.modules.impl.PLAYER.ItemSwapFix;
import ru.tuskevich.modules.impl.PLAYER.NoRotation;
import ru.tuskevich.modules.impl.COMBAT.AutoExplosion;
import ru.tuskevich.modules.impl.PLAYER.MiddleClick;
import ru.tuskevich.modules.impl.RENDER.EnchantmentColor;
import ru.tuskevich.modules.impl.PLAYER.AutoRespawn;
import ru.tuskevich.modules.impl.PLAYER.ItemScroller;
import ru.tuskevich.modules.impl.PLAYER.DeathCoords;
import ru.tuskevich.modules.impl.PLAYER.NoInteract;
import ru.tuskevich.modules.impl.PLAYER.AntiAFK;
import ru.tuskevich.modules.impl.MISC.Hider;
import ru.tuskevich.modules.impl.PLAYER.AutoTPAccept;
import ru.tuskevich.modules.impl.PLAYER.AutoTool;
import ru.tuskevich.modules.impl.MISC.BetterChat;
import ru.tuskevich.modules.impl.PLAYER.StreamerMode;
import ru.tuskevich.modules.impl.PLAYER.Timer;
import ru.tuskevich.modules.impl.HUD.Notifications;
import ru.tuskevich.modules.impl.RENDER.AirDropWay;
import ru.tuskevich.modules.impl.RENDER.WorldTime;
import ru.tuskevich.modules.impl.MOVEMENT.DamageSpeed;
import ru.tuskevich.modules.impl.PLAYER.AirStealler;
import ru.tuskevich.modules.impl.MOVEMENT.KTLeave;
import ru.tuskevich.modules.impl.RENDER.ItemESP;
import ru.tuskevich.modules.impl.MISC.MenuClose;
import ru.tuskevich.modules.impl.COMBAT.MultiActions;
import ru.tuskevich.modules.impl.MISC.Optimization;
import ru.tuskevich.modules.impl.MOVEMENT.LongJump;
import ru.tuskevich.modules.impl.RENDER.Predict;
import ru.tuskevich.modules.impl.PLAYER.FreeCamera;
import ru.tuskevich.modules.impl.MOVEMENT.NoClip;
import ru.tuskevich.modules.impl.PLAYER.NoPush;
import ru.tuskevich.modules.impl.COMBAT.HvHHelper;
import ru.tuskevich.modules.impl.MOVEMENT.Flight;
import ru.tuskevich.modules.impl.RENDER.Glass;
import ru.tuskevich.modules.impl.RENDER.NameTags;
import ru.tuskevich.modules.impl.MOVEMENT.TargetStrafe;
import ru.tuskevich.modules.impl.RENDER.JumpCircle;
import ru.tuskevich.modules.impl.COMBAT.AntiBot;
import ru.tuskevich.modules.impl.MOVEMENT.Jesus;
import ru.tuskevich.modules.impl.MOVEMENT.SunElytra;
import ru.tuskevich.modules.impl.MOVEMENT.MoveDisabler;
import ru.tuskevich.modules.impl.RENDER.CustomFog;
import ru.tuskevich.modules.impl.COMBAT.AutoTotem;
import ru.tuskevich.modules.impl.MISC.PearlThrowBlock;
import ru.tuskevich.modules.impl.RENDER.SpawnerESP;
import ru.tuskevich.modules.impl.COMBAT.BackTrack;
import ru.tuskevich.modules.impl.RENDER.ESP;
import ru.tuskevich.modules.impl.MISC.ElytraSwap;
import ru.tuskevich.modules.impl.PLAYER.StaffInfo;
import ru.tuskevich.modules.impl.MOVEMENT.AirJump;
import ru.tuskevich.modules.impl.HUD.PotionHUD;
import ru.tuskevich.modules.impl.MOVEMENT.InvWalk;
import ru.tuskevich.modules.impl.MOVEMENT.AutoSprint;
import ru.tuskevich.modules.impl.HUD.ClickGui;
import java.util.concurrent.CopyOnWriteArrayList;

public class Manager
{
    public CopyOnWriteArrayList<Module> list;
    
    public Manager() {
        (this.list = new CopyOnWriteArrayList<Module>()).add(new ClickGui());
        this.list.add(new AutoSprint());
        this.list.add(new InvWalk());
        this.list.add(new PotionHUD());
        this.list.add(new AirJump());
        this.list.add(new StaffInfo());
        this.list.add(new ElytraSwap());
        this.list.add(new ESP());
        this.list.add(new BackTrack());
        this.list.add(new SpawnerESP());
        this.list.add(new PearlThrowBlock());
        this.list.add(new AutoTotem());
        this.list.add(new CustomFog());
        this.list.add(new MoveDisabler());
        this.list.add(new SunElytra());
        this.list.add(new Jesus());
        this.list.add(new AntiBot());
        this.list.add(new JumpCircle());
        this.list.add(new TargetStrafe());
        this.list.add(new NameTags());
        this.list.add(new Glass());
        this.list.add(new Flight());
        this.list.add(new HvHHelper());
        this.list.add(new NoPush());
        this.list.add(new NoClip());
        this.list.add(new FreeCamera());
        this.list.add(new Predict());
        this.list.add(new LongJump());
        this.list.add(new Optimization());
        this.list.add(new MultiActions());
        this.list.add(new MenuClose());
        this.list.add(new ItemESP());
        this.list.add(new KTLeave());
        this.list.add(new AirStealler());
        this.list.add(new DamageSpeed());
        this.list.add(new WorldTime());
        this.list.add(new AirDropWay());
        this.list.add(new Notifications());
        this.list.add(new Timer());
        this.list.add(new StreamerMode());
        this.list.add(new BetterChat());
        this.list.add(new AutoTool());
        this.list.add(new AutoTPAccept());
        this.list.add(new Hider());
        this.list.add(new AntiAFK());
        this.list.add(new NoInteract());
        this.list.add(new DeathCoords());
        this.list.add(new ItemScroller());
        this.list.add(new AutoRespawn());
        this.list.add(new EnchantmentColor());
        this.list.add(new MiddleClick());
        this.list.add(new AutoExplosion());
        this.list.add(new NoRotation());
        this.list.add(new ItemSwapFix());
        this.list.add(new FastBow());
        this.list.add(new HitBox());
        this.list.add(new AutoArmor());
        this.list.add(new NoDelay());
        this.list.add(new HandTranslate());
        this.list.add(new KillAura());
        this.list.add(new Velocity());
        this.list.add(new Speed());
        this.list.add(new GoldenAppleTimer());
        this.list.add(new AutoApple());
        this.list.add(new Hud());
        this.list.add(new NoSlow());
        this.list.add(new Strafe());
        this.list.add(new ClientSound());
        this.list.add(new FullBright());
        this.list.add(new TargetHUD());
        this.list.add(new Tracers());
        this.list.add(new AutoPot());
        this.list.add(new NoOverlay());
        this.list.add(new NoFall());
        this.list.add(new AutoBrew());
        this.list.add(new WallClimb());
        this.list.add(new WaterSpeed());
        this.list.add(new Criticals());
        this.list.add(new Trails());
        this.list.add(new Disabler());
        this.list.add(new WorldColor());
        this.list.add(new CameraClip());
        this.list.add(new TestFeature());
        this.list.sort(Comparator.comparingInt(m -> Fonts.MONTSERRAT12.getStringWidth(m.name)).reversed());
    }
    
    public List<Module> getModules() {
        return this.list;
    }
    
    public Module[] getModulesFromCategory(final Type category) {
        return this.list.stream().filter(module -> module.category == category).toArray(Module[]::new);
    }
    
    public Module getModule(final Class<? extends Module> classModule) {
        for (final Module module : this.getModules()) {
            if (module != null && module.getClass() == classModule) {
                return module;
            }
        }
        return null;
    }
    
    public Module getModule(final String name) {
        for (final Module module : this.getModules()) {
            if (module.name.equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }
}
