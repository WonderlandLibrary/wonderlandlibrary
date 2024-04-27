package net.ccbluex.liquidbounce.features.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCommand;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.color.Gident;
import net.ccbluex.liquidbounce.features.module.modules.color.Rainbow;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aimbot;
import net.ccbluex.liquidbounce.features.module.modules.combat.AntiFireBall;
import net.ccbluex.liquidbounce.features.module.modules.combat.AuraFix;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoArmor;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoBow;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoClicker;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoHead;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoLFix;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoLeave;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoPot;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoSoup;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoWeapon;
import net.ccbluex.liquidbounce.features.module.modules.combat.BowAimbot;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals2;
import net.ccbluex.liquidbounce.features.module.modules.combat.FastBow;
import net.ccbluex.liquidbounce.features.module.modules.combat.HitBox;
import net.ccbluex.liquidbounce.features.module.modules.combat.Ignite;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura2;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura3;
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
import net.ccbluex.liquidbounce.features.module.modules.exploit.Disabler2;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ForceUnicodeChat;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Ghost;
import net.ccbluex.liquidbounce.features.module.modules.exploit.GhostHand;
import net.ccbluex.liquidbounce.features.module.modules.exploit.GodMode;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ItemTeleport;
import net.ccbluex.liquidbounce.features.module.modules.exploit.KeepContainer;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Kick;
import net.ccbluex.liquidbounce.features.module.modules.exploit.LZQDisabler;
import net.ccbluex.liquidbounce.features.module.modules.exploit.MoreCarry;
import net.ccbluex.liquidbounce.features.module.modules.exploit.MultiActions;
import net.ccbluex.liquidbounce.features.module.modules.exploit.NoPitchLimit;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Phase;
import net.ccbluex.liquidbounce.features.module.modules.exploit.PingSpoof;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Plugins;
import net.ccbluex.liquidbounce.features.module.modules.exploit.PortalMenu;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ServerCrasher;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Teleport;
import net.ccbluex.liquidbounce.features.module.modules.exploit.VehicleOneHit;
import net.ccbluex.liquidbounce.features.module.modules.hyt.AutoLeos;
import net.ccbluex.liquidbounce.features.module.modules.hyt.GidentColor;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytDisabler;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytGapple;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytNoLag;
import net.ccbluex.liquidbounce.features.module.modules.hyt.NoC03;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.misc.AtAllProvider;
import net.ccbluex.liquidbounce.features.module.modules.misc.AutoLobby;
import net.ccbluex.liquidbounce.features.module.modules.misc.ComponentOnHover;
import net.ccbluex.liquidbounce.features.module.modules.misc.HytGetName;
import net.ccbluex.liquidbounce.features.module.modules.misc.LiquidChat;
import net.ccbluex.liquidbounce.features.module.modules.misc.MemoryFix;
import net.ccbluex.liquidbounce.features.module.modules.misc.MidClick;
import net.ccbluex.liquidbounce.features.module.modules.misc.NameProtect;
import net.ccbluex.liquidbounce.features.module.modules.misc.NoFucker;
import net.ccbluex.liquidbounce.features.module.modules.misc.NoRotateSet;
import net.ccbluex.liquidbounce.features.module.modules.misc.ResourcePackSpoof;
import net.ccbluex.liquidbounce.features.module.modules.misc.Spammer;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirLadder;
import net.ccbluex.liquidbounce.features.module.modules.movement.AutoWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.BlockWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.BoatJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.BowJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.BufferSpeed;
import net.ccbluex.liquidbounce.features.module.modules.movement.BugUp;
import net.ccbluex.liquidbounce.features.module.modules.movement.CustomBhop;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastClimb;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastStairs;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.movement.Freeze;
import net.ccbluex.liquidbounce.features.module.modules.movement.HighJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.IceSpeed;
import net.ccbluex.liquidbounce.features.module.modules.movement.InventoryMove;
import net.ccbluex.liquidbounce.features.module.modules.movement.KeepSprint;
import net.ccbluex.liquidbounce.features.module.modules.movement.LadderJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.LiquidWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.LongJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoClip;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoJumpDelay;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoWeb;
import net.ccbluex.liquidbounce.features.module.modules.movement.Parkour;
import net.ccbluex.liquidbounce.features.module.modules.movement.PerfectHorseJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.ReverseStep;
import net.ccbluex.liquidbounce.features.module.modules.movement.SafeWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.SlimeJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sneak;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.Spider;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sprint;
import net.ccbluex.liquidbounce.features.module.modules.movement.Step;
import net.ccbluex.liquidbounce.features.module.modules.movement.Strafe;
import net.ccbluex.liquidbounce.features.module.modules.movement.TargetStrafe;
import net.ccbluex.liquidbounce.features.module.modules.movement.WallClimb;
import net.ccbluex.liquidbounce.features.module.modules.movement.WaterSpeed;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiAFK;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiCactus;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoFish;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoPlay;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoRespawn;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoTool;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
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
import net.ccbluex.liquidbounce.features.module.modules.render.AsianHat;
import net.ccbluex.liquidbounce.features.module.modules.render.AttackEffects;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockESP;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay;
import net.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs;
import net.ccbluex.liquidbounce.features.module.modules.render.CameraClip;
import net.ccbluex.liquidbounce.features.module.modules.render.Cape;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.DMGParticle;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import net.ccbluex.liquidbounce.features.module.modules.render.EnchantEffect;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.features.module.modules.render.Fullbright;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.ItemESP;
import net.ccbluex.liquidbounce.features.module.modules.render.JelloArraylist;
import net.ccbluex.liquidbounce.features.module.modules.render.MotionBlur;
import net.ccbluex.liquidbounce.features.module.modules.render.NameTags;
import net.ccbluex.liquidbounce.features.module.modules.render.NoBob;
import net.ccbluex.liquidbounce.features.module.modules.render.NoFOV;
import net.ccbluex.liquidbounce.features.module.modules.render.NoHurtCam;
import net.ccbluex.liquidbounce.features.module.modules.render.NoScoreboard;
import net.ccbluex.liquidbounce.features.module.modules.render.NoSwing;
import net.ccbluex.liquidbounce.features.module.modules.render.OldHitting;
import net.ccbluex.liquidbounce.features.module.modules.render.Pendant;
import net.ccbluex.liquidbounce.features.module.modules.render.PlayerHealthSend;
import net.ccbluex.liquidbounce.features.module.modules.render.Projectiles;
import net.ccbluex.liquidbounce.features.module.modules.render.ProphuntESP;
import net.ccbluex.liquidbounce.features.module.modules.render.Rotations;
import net.ccbluex.liquidbounce.features.module.modules.render.StorageESP;
import net.ccbluex.liquidbounce.features.module.modules.render.SwingAnimation;
import net.ccbluex.liquidbounce.features.module.modules.render.TNTESP;
import net.ccbluex.liquidbounce.features.module.modules.render.Title;
import net.ccbluex.liquidbounce.features.module.modules.render.Tracers;
import net.ccbluex.liquidbounce.features.module.modules.render.Trail;
import net.ccbluex.liquidbounce.features.module.modules.render.TrueSight;
import net.ccbluex.liquidbounce.features.module.modules.render.WolrdAnim;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import net.ccbluex.liquidbounce.features.module.modules.world.Ambience;
import net.ccbluex.liquidbounce.features.module.modules.world.AutoBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.BetterFPS;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.ccbluex.liquidbounce.features.module.modules.world.CivBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.FastBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.FastPlace;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.features.module.modules.world.Liquids;
import net.ccbluex.liquidbounce.features.module.modules.world.NoSlowBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.Nuker;
import net.ccbluex.liquidbounce.features.module.modules.world.OldScaffold;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.features.module.modules.world.Timer;
import net.ccbluex.liquidbounce.features.module.modules.world.Tower;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000^\n\n\n\b\n\n\n\n\n\u0000\n\n\b\n\n\b\n\b\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\u000020B¢J02 0H\u0000¢\b!J\"02\n#\b0HJ$02\n%\b0J$02\b&0'J\b(0\rHJ)02*0+HJ,02%\n\b00HJ,02 0J-0J1-02\"\b\b\n\b000.\"\n\b00H¢/J002 0R2&\b\b000j\b\b00`X¢\n\u0000R\b\b00\t¢\b\n\u0000\b\nR\f0\rX¢\n\u0000\b\"\bR0X¢\n\u0000\b\"\bR0X¢\n\u0000\b\"\b¨1"}, d2={"Lnet/ccbluex/liquidbounce/features/module/ModuleManager;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "moduleClassMap", "Ljava/util/HashMap;", "Ljava/lang/Class;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "Lkotlin/collections/HashMap;", "modules", "Ljava/util/TreeSet;", "getModules", "()Ljava/util/TreeSet;", "shouldNotify", "", "getShouldNotify", "()Z", "setShouldNotify", "(Z)V", "toggleSoundMode", "", "getToggleSoundMode", "()I", "setToggleSoundMode", "(I)V", "toggleVolume", "", "getToggleVolume", "()F", "setToggleVolume", "(F)V", "generateCommand", "", "module", "generateCommand$Pride", "get", "clazz", "getModule", "moduleClass", "moduleName", "", "handleEvents", "onKey", "event", "Lnet/ccbluex/liquidbounce/event/KeyEvent;", "registerModule", "registerModules", "", "([Ljava/lang/Class;)V", "unregisterModule", "Pride"})
public final class ModuleManager
implements Listenable {
    @NotNull
    private final TreeSet<Module> modules = new TreeSet(modules.1.INSTANCE);
    private final HashMap<Class<?>, Module> moduleClassMap;
    private boolean shouldNotify;
    private int toggleSoundMode;
    private float toggleVolume;

    @NotNull
    public final TreeSet<Module> getModules() {
        return this.modules;
    }

    public final boolean getShouldNotify() {
        return this.shouldNotify;
    }

    public final void setShouldNotify(boolean bl) {
        this.shouldNotify = bl;
    }

    public final int getToggleSoundMode() {
        return this.toggleSoundMode;
    }

    public final void setToggleSoundMode(int n) {
        this.toggleSoundMode = n;
    }

    public final float getToggleVolume() {
        return this.toggleVolume;
    }

    public final void setToggleVolume(float f) {
        this.toggleVolume = f;
    }

    public final void registerModules() {
        ClientUtils.getLogger().info("[ModuleManager] Loading modules...");
        this.registerModules(SpeedMine.class, AutoArmor.class, DMGParticle.class, Rainbow.class, AutoBow.class, AutoLeave.class, EnchantEffect.class, BetterFPS.class, Gident.class, AutoPlay.class, AutoPot.class, Trail.class, MemoryFix.class, AsianHat.class, LZQDisabler.class, Ambience.class, MotionBlur.class, AutoSoup.class, WolrdAnim.class, AutoWeapon.class, AutoLFix.class, BowAimbot.class, Pendant.class, Criticals.class, KillAura.class, BoatJump.class, Trigger.class, Velocity.class, Fly.class, ClickGUI.class, KeepSprint.class, HighJump.class, Spider.class, InventoryMove.class, Disabler2.class, CustomBhop.class, NoSlow.class, LiquidWalk.class, SafeWalk.class, WallClimb.class, PlayerHealthSend.class, Strafe.class, Sprint.class, BowJump.class, Teams.class, NoRotateSet.class, ChestStealer.class, Scaffold.class, CivBreak.class, Tower.class, FastBreak.class, FastPlace.class, ESP.class, Speed.class, Tracers.class, TargetStrafe.class, NameTags.class, FastUse.class, Teleport.class, Fullbright.class, ItemESP.class, StorageESP.class, Projectiles.class, AutoHead.class, NoClip.class, Nuker.class, PingSpoof.class, FastClimb.class, Step.class, NoFucker.class, AutoRespawn.class, AutoTool.class, AntiFireBall.class, NoWeb.class, Spammer.class, IceSpeed.class, Zoot.class, HytGetName.class, AutoLobby.class, Regen.class, NoFall.class, Blink.class, NameProtect.class, NoHurtCam.class, Ghost.class, MidClick.class, XRay.class, Timer.class, Sneak.class, GhostHand.class, AutoWalk.class, AutoBreak.class, FreeCam.class, Aimbot.class, Eagle.class, HitBox.class, AntiCactus.class, Plugins.class, AntiHunger.class, ConsoleSpammer.class, LongJump.class, Parkour.class, LadderJump.class, FastBow.class, MultiActions.class, AirJump.class, AutoClicker.class, NoBob.class, BlockOverlay.class, NoFriends.class, BlockESP.class, Chams.class, Clip.class, Phase.class, ServerCrasher.class, NoFOV.class, FastStairs.class, SwingAnimation.class, ReverseStep.class, TNTBlock.class, InventoryCleaner.class, TrueSight.class, LiquidChat.class, AntiBlind.class, NoSwing.class, BedGodMode.class, BugUp.class, Breadcrumbs.class, AbortBreaking.class, PotionSaver.class, CameraClip.class, WaterSpeed.class, Ignite.class, SlimeJump.class, MoreCarry.class, NoPitchLimit.class, Kick.class, Liquids.class, AtAllProvider.class, AttackEffects.class, AirLadder.class, GodMode.class, TeleportHit.class, ForceUnicodeChat.class, ItemTeleport.class, BufferSpeed.class, SuperKnockback.class, ProphuntESP.class, AutoFish.class, Damage.class, Freeze.class, KeepContainer.class, VehicleOneHit.class, Reach.class, Rotations.class, NoJumpDelay.class, BlockWalk.class, AntiAFK.class, PerfectHorseJump.class, HUD.class, TNTESP.class, ComponentOnHover.class, KeepAlive.class, ResourcePackSpoof.class, OldHitting.class, Cape.class, Title.class, NoSlowBreak.class, PortalMenu.class, AutoLeos.class, NoC03.class, HytGapple.class, HytDisabler.class, HytNoLag.class, AuraFix.class, KillAura2.class, KillAura3.class, JelloArraylist.class, Gident.class, Rainbow.class, OldScaffold.class, Velocity2.class, GidentColor.class, Criticals2.class);
        this.registerModule(NoScoreboard.INSTANCE);
        this.registerModule(Fucker.INSTANCE);
        this.registerModule(ChestAura.INSTANCE);
        this.registerModule(AntiBot.INSTANCE);
        ClientUtils.getLogger().info("[ModuleManager] Loaded " + this.modules.size() + " modules.");
    }

    public final void registerModule(@NotNull Module module) {
        Intrinsics.checkParameterIsNotNull(module, "module");
        if (!module.isSupported()) {
            return;
        }
        Collection collection = this.modules;
        boolean bl = false;
        collection.add(module);
        ((Map)this.moduleClassMap).put(module.getClass(), module);
        this.generateCommand$Pride(module);
        LiquidBounce.INSTANCE.getEventManager().registerListener(module);
    }

    private final void registerModule(Class<? extends Module> moduleClass) {
        try {
            Module module = moduleClass.newInstance();
            Intrinsics.checkExpressionValueIsNotNull(module, "moduleClass.newInstance()");
            this.registerModule(module);
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
        Intrinsics.checkParameterIsNotNull(modules2, "modules");
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

    public final void unregisterModule(@NotNull Module module) {
        Intrinsics.checkParameterIsNotNull(module, "module");
        this.modules.remove(module);
        this.moduleClassMap.remove(module.getClass());
        LiquidBounce.INSTANCE.getEventManager().unregisterListener(module);
    }

    public final void generateCommand$Pride(@NotNull Module module) {
        Intrinsics.checkParameterIsNotNull(module, "module");
        List<Value<?>> values = module.getValues();
        if (values.isEmpty()) {
            return;
        }
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(new ModuleCommand(module, values));
    }

    @NotNull
    public final Module getModule(@NotNull Class<?> moduleClass) {
        Intrinsics.checkParameterIsNotNull(moduleClass, "moduleClass");
        Module module = this.moduleClassMap.get(moduleClass);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        Intrinsics.checkExpressionValueIsNotNull(module, "moduleClassMap[moduleClass]!!");
        return module;
    }

    @NotNull
    public final Module get(@NotNull Class<?> clazz) {
        Intrinsics.checkParameterIsNotNull(clazz, "clazz");
        return this.getModule(clazz);
    }

    @Nullable
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
                if (!StringsKt.equals(it.getName(), moduleName, true)) continue;
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
