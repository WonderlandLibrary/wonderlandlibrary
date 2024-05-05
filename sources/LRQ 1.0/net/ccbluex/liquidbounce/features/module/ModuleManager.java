/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCommand;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aimbot;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoArmor;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoBlockHelper;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoBow;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoClicker;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoHead;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoLeave;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoPot;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoSoup;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoWeapon;
import net.ccbluex.liquidbounce.features.module.modules.combat.BowAimbot;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals2;
import net.ccbluex.liquidbounce.features.module.modules.combat.FastBow;
import net.ccbluex.liquidbounce.features.module.modules.combat.GrimFull;
import net.ccbluex.liquidbounce.features.module.modules.combat.GrimVelocity;
import net.ccbluex.liquidbounce.features.module.modules.combat.GrimVelocity2;
import net.ccbluex.liquidbounce.features.module.modules.combat.HitBox;
import net.ccbluex.liquidbounce.features.module.modules.combat.Ignite;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura2;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.combat.SuperKnockback;
import net.ccbluex.liquidbounce.features.module.modules.combat.TNTBlock;
import net.ccbluex.liquidbounce.features.module.modules.combat.TeleportHit;
import net.ccbluex.liquidbounce.features.module.modules.combat.Trigger;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity2;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AbortBreaking;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AntiHunger;
import net.ccbluex.liquidbounce.features.module.modules.exploit.BedGodMode;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Clip;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ConsoleSpammer;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Damage;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Derp;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ForceUnicodeChat;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Ghost;
import net.ccbluex.liquidbounce.features.module.modules.exploit.GhostHand;
import net.ccbluex.liquidbounce.features.module.modules.exploit.GodMode;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ItemTeleport;
import net.ccbluex.liquidbounce.features.module.modules.exploit.KeepContainer;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Kick;
import net.ccbluex.liquidbounce.features.module.modules.exploit.MoreCarry;
import net.ccbluex.liquidbounce.features.module.modules.exploit.MultiActions;
import net.ccbluex.liquidbounce.features.module.modules.exploit.NoPitchLimit;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Paralyze;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Phase;
import net.ccbluex.liquidbounce.features.module.modules.exploit.PingSpoof;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Plugins;
import net.ccbluex.liquidbounce.features.module.modules.exploit.PortalMenu;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ServerCrasher;
import net.ccbluex.liquidbounce.features.module.modules.exploit.SkinDerp;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Teleport;
import net.ccbluex.liquidbounce.features.module.modules.exploit.VehicleOneHit;
import net.ccbluex.liquidbounce.features.module.modules.hyt.Animations;
import net.ccbluex.liquidbounce.features.module.modules.hyt.AutoGG;
import net.ccbluex.liquidbounce.features.module.modules.hyt.DisablerC03;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytAutoLeos;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytDisabler;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytGapple;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytGetName;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytNoLagBack;
import net.ccbluex.liquidbounce.features.module.modules.hyt.KillFix;
import net.ccbluex.liquidbounce.features.module.modules.hyt.ScaffoldHelp;
import net.ccbluex.liquidbounce.features.module.modules.hyt.Title;
import net.ccbluex.liquidbounce.features.module.modules.hyt.VisualColor;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.misc.AtAllProvider;
import net.ccbluex.liquidbounce.features.module.modules.misc.ComponentOnHover;
import net.ccbluex.liquidbounce.features.module.modules.misc.LiquidChat;
import net.ccbluex.liquidbounce.features.module.modules.misc.MidClick;
import net.ccbluex.liquidbounce.features.module.modules.misc.NameProtect;
import net.ccbluex.liquidbounce.features.module.modules.misc.NoRotateSet;
import net.ccbluex.liquidbounce.features.module.modules.misc.ResourcePackSpoof;
import net.ccbluex.liquidbounce.features.module.modules.misc.Spammer;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirLadder;
import net.ccbluex.liquidbounce.features.module.modules.movement.AutoWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.BlockWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.BufferSpeed;
import net.ccbluex.liquidbounce.features.module.modules.movement.BugUp;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastClimb;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastStairs;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.movement.Freeze;
import net.ccbluex.liquidbounce.features.module.modules.movement.HighJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.IceSpeed;
import net.ccbluex.liquidbounce.features.module.modules.movement.InventoryMove;
import net.ccbluex.liquidbounce.features.module.modules.movement.LadderJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.LiquidWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.LongJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoClip;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoJumpDelay;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow2;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoWeb;
import net.ccbluex.liquidbounce.features.module.modules.movement.Parkour;
import net.ccbluex.liquidbounce.features.module.modules.movement.PerfectHorseJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.ReverseStep;
import net.ccbluex.liquidbounce.features.module.modules.movement.SafeWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.SlimeJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sneak;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sprint;
import net.ccbluex.liquidbounce.features.module.modules.movement.Step;
import net.ccbluex.liquidbounce.features.module.modules.movement.Strafe;
import net.ccbluex.liquidbounce.features.module.modules.movement.WallClimb;
import net.ccbluex.liquidbounce.features.module.modules.movement.WaterSpeed;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiAFK;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiCactus;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoFish;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoRespawn;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoTool;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.player.BlinkPro;
import net.ccbluex.liquidbounce.features.module.modules.player.Eagle;
import net.ccbluex.liquidbounce.features.module.modules.player.FastUse;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.features.module.modules.player.KeepAlive;
import net.ccbluex.liquidbounce.features.module.modules.player.NoFall;
import net.ccbluex.liquidbounce.features.module.modules.player.PotionSaver;
import net.ccbluex.liquidbounce.features.module.modules.player.Reach;
import net.ccbluex.liquidbounce.features.module.modules.player.Regen;
import net.ccbluex.liquidbounce.features.module.modules.player.SpeedMine;
import net.ccbluex.liquidbounce.features.module.modules.player.Zoot;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockESP;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay;
import net.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs;
import net.ccbluex.liquidbounce.features.module.modules.render.CameraClip;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import net.ccbluex.liquidbounce.features.module.modules.render.EnchantEffect;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.features.module.modules.render.Fullbright;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.ItemESP;
import net.ccbluex.liquidbounce.features.module.modules.render.Logo;
import net.ccbluex.liquidbounce.features.module.modules.render.NameTags;
import net.ccbluex.liquidbounce.features.module.modules.render.NoBob;
import net.ccbluex.liquidbounce.features.module.modules.render.NoFOV;
import net.ccbluex.liquidbounce.features.module.modules.render.NoHurtCam;
import net.ccbluex.liquidbounce.features.module.modules.render.NoScoreboard;
import net.ccbluex.liquidbounce.features.module.modules.render.NoSwing;
import net.ccbluex.liquidbounce.features.module.modules.render.OldHitting;
import net.ccbluex.liquidbounce.features.module.modules.render.Projectiles;
import net.ccbluex.liquidbounce.features.module.modules.render.ProphuntESP;
import net.ccbluex.liquidbounce.features.module.modules.render.Rotations;
import net.ccbluex.liquidbounce.features.module.modules.render.StorageESP;
import net.ccbluex.liquidbounce.features.module.modules.render.SwingAnimation;
import net.ccbluex.liquidbounce.features.module.modules.render.TNTESP;
import net.ccbluex.liquidbounce.features.module.modules.render.Tracers;
import net.ccbluex.liquidbounce.features.module.modules.render.TrueSight;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import net.ccbluex.liquidbounce.features.module.modules.world.AutoBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.ccbluex.liquidbounce.features.module.modules.world.CivBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.FastBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.FastPlace;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.features.module.modules.world.Liquids;
import net.ccbluex.liquidbounce.features.module.modules.world.NoSlowBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.Nuker;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.features.module.modules.world.Timer;
import net.ccbluex.liquidbounce.features.module.modules.world.Tower;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.Nullable;

public final class ModuleManager
implements Listenable {
    private final TreeSet<Module> modules = new TreeSet(modules.1.INSTANCE);
    private final HashMap<Class<?>, Module> moduleClassMap;
    private int toggleSoundMode;

    public final TreeSet<Module> getModules() {
        return this.modules;
    }

    public final int getToggleSoundMode() {
        return this.toggleSoundMode;
    }

    public final void setToggleSoundMode(int n) {
        this.toggleSoundMode = n;
    }

    public final void registerModules() {
        ClientUtils.getLogger().info("[ModuleManager] Loading modules...");
        this.registerModules(AutoBlockHelper.class, Logo.class, GrimFull.class, GrimVelocity.class, GrimVelocity2.class, BlinkPro.class, VisualColor.class, Animations.class, Criticals2.class, NoSlow2.class, Velocity2.class, DisablerC03.class, HytGapple.class, HytDisabler.class, KillAura2.class, Title.class, KillFix.class, HytNoLagBack.class, HytGetName.class, HytAutoLeos.class, AutoGG.class, AutoArmor.class, AutoBow.class, AutoLeave.class, AutoPot.class, AutoSoup.class, AutoWeapon.class, BowAimbot.class, Criticals.class, KillAura.class, Trigger.class, Fly.class, ClickGUI.class, HighJump.class, InventoryMove.class, LiquidWalk.class, SafeWalk.class, WallClimb.class, Strafe.class, Sprint.class, Teams.class, NoRotateSet.class, AntiBot.class, ChestStealer.class, Scaffold.class, CivBreak.class, Tower.class, FastBreak.class, FastPlace.class, ESP.class, NoSlow.class, Velocity.class, Speed.class, Tracers.class, NameTags.class, FastUse.class, Teleport.class, Fullbright.class, ItemESP.class, StorageESP.class, Projectiles.class, NoClip.class, Nuker.class, PingSpoof.class, FastClimb.class, Step.class, AutoRespawn.class, AutoTool.class, NoWeb.class, Spammer.class, IceSpeed.class, Zoot.class, Regen.class, NoFall.class, Blink.class, NameProtect.class, NoHurtCam.class, Ghost.class, MidClick.class, XRay.class, Timer.class, Sneak.class, SkinDerp.class, Paralyze.class, GhostHand.class, AutoWalk.class, AutoBreak.class, FreeCam.class, Aimbot.class, Eagle.class, HitBox.class, AntiCactus.class, Plugins.class, AntiHunger.class, ConsoleSpammer.class, LongJump.class, Parkour.class, LadderJump.class, FastBow.class, MultiActions.class, AirJump.class, AutoClicker.class, NoBob.class, BlockOverlay.class, NoFriends.class, BlockESP.class, Chams.class, Clip.class, Phase.class, ServerCrasher.class, NoFOV.class, FastStairs.class, SwingAnimation.class, Derp.class, ReverseStep.class, TNTBlock.class, InventoryCleaner.class, TrueSight.class, LiquidChat.class, AntiBlind.class, NoSwing.class, BedGodMode.class, BugUp.class, Breadcrumbs.class, AbortBreaking.class, PotionSaver.class, CameraClip.class, WaterSpeed.class, Ignite.class, SlimeJump.class, MoreCarry.class, NoPitchLimit.class, Kick.class, Liquids.class, AtAllProvider.class, AirLadder.class, GodMode.class, TeleportHit.class, ForceUnicodeChat.class, ItemTeleport.class, BufferSpeed.class, SuperKnockback.class, ProphuntESP.class, AutoFish.class, Damage.class, Freeze.class, KeepContainer.class, VehicleOneHit.class, Reach.class, Rotations.class, NoJumpDelay.class, BlockWalk.class, AntiAFK.class, PerfectHorseJump.class, HUD.class, TNTESP.class, ComponentOnHover.class, KeepAlive.class, ResourcePackSpoof.class, NoSlowBreak.class, PortalMenu.class, EnchantEffect.class, SpeedMine.class, OldHitting.class, AutoHead.class, ScaffoldHelp.class);
        this.registerModule(NoScoreboard.INSTANCE);
        this.registerModule(Fucker.INSTANCE);
        this.registerModule(ChestAura.INSTANCE);
        ClientUtils.getLogger().info("[ModuleManager] Loaded " + this.modules.size() + " modules.");
    }

    public final void registerModule(Module module) {
        if (!module.isSupported()) {
            return;
        }
        Collection collection = this.modules;
        boolean bl = false;
        collection.add(module);
        ((Map)this.moduleClassMap).put(module.getClass(), module);
        this.generateCommand$LiquidSense(module);
        LiquidBounce.INSTANCE.getEventManager().registerListener(module);
    }

    private final void registerModule(Class<? extends Module> moduleClass) {
        try {
            this.registerModule(moduleClass.newInstance());
        }
        catch (Throwable e) {
            ClientUtils.getLogger().error("Failed to load module: " + moduleClass.getName() + " (" + e.getClass().getName() + ": " + e.getMessage() + ')');
        }
    }

    /*
     * WARNING - void declaration
     */
    @SafeVarargs
    public final void registerModules(Class<? extends Module> ... modules2) {
        void $this$forEach$iv;
        Class<? extends Module>[] classArray = modules2;
        ModuleManager moduleManager = this;
        boolean $i$f$forEach = false;
        void var5_5 = $this$forEach$iv;
        int n = ((void)var5_5).length;
        for (int i = 0; i < n; ++i) {
            void element$iv;
            void p1 = element$iv = var5_5[i];
            boolean bl = false;
            moduleManager.registerModule((Class<? extends Module>)p1);
        }
    }

    private final void registerModule(Object cbModule) {
        Object object = cbModule;
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<out net.ccbluex.liquidbounce.features.module.Module>");
        }
        this.registerModule((Module)((Class)object).newInstance());
    }

    public final void unregisterModule(Module module) {
        this.modules.remove(module);
        this.moduleClassMap.remove(module.getClass());
        LiquidBounce.INSTANCE.getEventManager().unregisterListener(module);
    }

    public final void generateCommand$LiquidSense(Module module) {
        List<Value<?>> values = module.getValues();
        if (values.isEmpty()) {
            return;
        }
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(new ModuleCommand(module, values));
    }

    public final Module getModule(Class<?> moduleClass) {
        Module module = this.moduleClassMap.get(moduleClass);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        return module;
    }

    public final Module get(Class<?> clazz) {
        return this.getModule(clazz);
    }

    public final Module getModule(@Nullable String moduleName) {
        Object v0;
        block1: {
            Iterable iterable = this.modules;
            boolean bl = false;
            Iterable iterable2 = iterable;
            boolean bl2 = false;
            for (Object t : iterable2) {
                Module it = (Module)t;
                boolean bl3 = false;
                if (!StringsKt.equals((String)it.getName(), (String)moduleName, (boolean)true)) continue;
                v0 = t;
                break block1;
            }
            v0 = null;
        }
        return v0;
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    private final void onKey(KeyEvent event) {
        void $this$filterTo$iv$iv;
        Iterable $this$filter$iv = this.modules;
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Module it = (Module)element$iv$iv;
            boolean bl = false;
            if (!(it.getKeyBind() == event.getKey())) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        Iterable $this$forEach$iv = (List)destination$iv$iv;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Module it = (Module)element$iv;
            boolean bl = false;
            it.toggle();
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    public ModuleManager() {
        ModuleManager moduleManager = this;
        boolean bl = false;
        HashMap hashMap = new HashMap();
        moduleManager.moduleClassMap = hashMap;
        LiquidBounce.INSTANCE.getEventManager().registerListener(this);
    }
}

