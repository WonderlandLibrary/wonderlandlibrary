/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module;

import ad.novoline.module.Ambience;
import ad.novoline.module.ChatTranslator;
import ad.novoline.module.TianKengHelper;
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
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleCommand;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.client.CapeManager;
import net.ccbluex.liquidbounce.features.module.modules.color.ColorMixer;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aimbot;
import net.ccbluex.liquidbounce.features.module.modules.combat.AuraKeepSprint;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoArmor;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoBow;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoClicker;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoHead;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoPot;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoRunaway;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoSoup;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoWeapon;
import net.ccbluex.liquidbounce.features.module.modules.combat.BowAimbot;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.FastBow;
import net.ccbluex.liquidbounce.features.module.modules.combat.HitBox;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura2;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillFix;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.combat.SuperKnockback;
import net.ccbluex.liquidbounce.features.module.modules.combat.TNTBlock;
import net.ccbluex.liquidbounce.features.module.modules.combat.TargetStrafe;
import net.ccbluex.liquidbounce.features.module.modules.combat.TeleportHit;
import net.ccbluex.liquidbounce.features.module.modules.combat.Trigger;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity2;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AbortBreaking;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Clip;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ConsoleSpammer;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Damage;
import net.ccbluex.liquidbounce.features.module.modules.exploit.KeepChest;
import net.ccbluex.liquidbounce.features.module.modules.exploit.KeepContainer;
import net.ccbluex.liquidbounce.features.module.modules.exploit.NoPitchLimit;
import net.ccbluex.liquidbounce.features.module.modules.exploit.PortalMenu;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ServerCrasher;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Teleport;
import net.ccbluex.liquidbounce.features.module.modules.exploit.VehicleOneHit;
import net.ccbluex.liquidbounce.features.module.modules.hyt.AutoPlay;
import net.ccbluex.liquidbounce.features.module.modules.hyt.BedFucker;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytGetName;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytNoHurt;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytSpeed;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.misc.ComponentOnHover;
import net.ccbluex.liquidbounce.features.module.modules.misc.FakeFPS;
import net.ccbluex.liquidbounce.features.module.modules.misc.NameProtect;
import net.ccbluex.liquidbounce.features.module.modules.misc.NoRotateSet;
import net.ccbluex.liquidbounce.features.module.modules.misc.ResourcePackSpoof;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.features.module.modules.misc.Test;
import net.ccbluex.liquidbounce.features.module.modules.misc.Title;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirLadder;
import net.ccbluex.liquidbounce.features.module.modules.movement.BufferSpeed;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastClimb;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastStairs;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.movement.InventoryMove;
import net.ccbluex.liquidbounce.features.module.modules.movement.LongJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoClip;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoJumpDelay;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoWeb;
import net.ccbluex.liquidbounce.features.module.modules.movement.SafeWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sprint;
import net.ccbluex.liquidbounce.features.module.modules.movement.Step;
import net.ccbluex.liquidbounce.features.module.modules.movement.Strafe;
import net.ccbluex.liquidbounce.features.module.modules.movement.WallClimb;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiAFK;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiCactus;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiVoid;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoRespawn;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoTool;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.player.Eagle;
import net.ccbluex.liquidbounce.features.module.modules.player.FastUse;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.features.module.modules.player.NoFall;
import net.ccbluex.liquidbounce.features.module.modules.player.PotionSaver;
import net.ccbluex.liquidbounce.features.module.modules.player.Reach;
import net.ccbluex.liquidbounce.features.module.modules.player.Regen;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.features.module.modules.render.BlurSettings;
import net.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs;
import net.ccbluex.liquidbounce.features.module.modules.render.CameraClip;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.Crosshair;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomTitle;
import net.ccbluex.liquidbounce.features.module.modules.render.DMGParticle;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import net.ccbluex.liquidbounce.features.module.modules.render.EnchantEffect;
import net.ccbluex.liquidbounce.features.module.modules.render.Fullbright;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.HotbarSettings;
import net.ccbluex.liquidbounce.features.module.modules.render.ItemESP;
import net.ccbluex.liquidbounce.features.module.modules.render.JumpCircle;
import net.ccbluex.liquidbounce.features.module.modules.render.MusicPlayer;
import net.ccbluex.liquidbounce.features.module.modules.render.NameTags;
import net.ccbluex.liquidbounce.features.module.modules.render.NewGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.NoBob;
import net.ccbluex.liquidbounce.features.module.modules.render.NoFOV;
import net.ccbluex.liquidbounce.features.module.modules.render.NoHurtCam;
import net.ccbluex.liquidbounce.features.module.modules.render.NoScoreboard;
import net.ccbluex.liquidbounce.features.module.modules.render.NoSwing;
import net.ccbluex.liquidbounce.features.module.modules.render.ProphuntESP;
import net.ccbluex.liquidbounce.features.module.modules.render.Rotations;
import net.ccbluex.liquidbounce.features.module.modules.render.TrueSight;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.ccbluex.liquidbounce.features.module.modules.world.CivBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.FastPlace;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.features.module.modules.world.NoC03;
import net.ccbluex.liquidbounce.features.module.modules.world.NoSlowBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.features.module.modules.world.SpeedMine;
import net.ccbluex.liquidbounce.features.module.modules.world.Timer;
import net.ccbluex.liquidbounce.features.module.modules.world.Tower;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0015\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0006H\u0000\u00a2\u0006\u0002\b\u000fJ\u0015\u0010\u0010\u001a\u00020\u00062\n\u0010\u0011\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0086\u0002J\u0012\u0010\u0012\u001a\u00020\u00062\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u0005J\u0012\u0010\u0012\u001a\u0004\u0018\u00010\u00062\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015J\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00060\u00172\u0006\u0010\u0018\u001a\u00020\u0019J\u0018\u0010\u001a\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0006\u0018\u00010\u001b2\u0006\u0010\u001c\u001a\u00020\u0019J\b\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010\u001f\u001a\u00020\r2\u0006\u0010 \u001a\u00020!H\u0003J\u0018\u0010\"\u001a\u00020\r2\u000e\u0010\u0013\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005H\u0002J\u000e\u0010\"\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0006J\u0006\u0010#\u001a\u00020\rJ1\u0010#\u001a\u00020\r2\"\u0010\b\u001a\u0012\u0012\u000e\b\u0001\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u00050$\"\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005H\u0007\u00a2\u0006\u0002\u0010%J\u000e\u0010&\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0006R2\u0010\u0003\u001a&\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0005\u0012\u0004\u0012\u00020\u0006`\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00060\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006'"}, d2={"Lnet/ccbluex/liquidbounce/features/module/ModuleManager;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "moduleClassMap", "Ljava/util/HashMap;", "Ljava/lang/Class;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "Lkotlin/collections/HashMap;", "modules", "Ljava/util/TreeSet;", "getModules", "()Ljava/util/TreeSet;", "generateCommand", "", "module", "generateCommand$LiKingSense", "get", "clazz", "getModule", "moduleClass", "moduleName", "", "getModuleInCategory", "", "category", "Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;", "getModulesInCategory", "Ljava/util/ArrayList;", "cat", "handleEvents", "", "onKey", "event", "Lnet/ccbluex/liquidbounce/event/KeyEvent;", "registerModule", "registerModules", "", "([Ljava/lang/Class;)V", "unregisterModule", "LiKingSense"})
public final class ModuleManager
implements Listenable {
    @NotNull
    private final TreeSet<Module> modules = new TreeSet(modules.1.INSTANCE);
    private final HashMap<Class<?>, Module> moduleClassMap;

    @NotNull
    public final TreeSet<Module> getModules() {
        return this.modules;
    }

    public final void registerModules() {
        ClientUtils.getLogger().info("[ModuleManager] Loading modules...");
        this.registerModules(NoC03.class, KillAura2.class, Velocity2.class, KillFix.class, NewGUI.class, CustomTitle.class, TargetStrafe.class, Title.class, MusicPlayer.class, AutoArmor.class, AutoBow.class, AutoRunaway.class, AutoPot.class, AutoSoup.class, AutoWeapon.class, BowAimbot.class, Criticals.class, KillAura.class, Trigger.class, Fly.class, ClickGUI.class, InventoryMove.class, SafeWalk.class, WallClimb.class, Strafe.class, Sprint.class, Teams.class, NoRotateSet.class, AntiBot.class, ChestStealer.class, Scaffold.class, CivBreak.class, Tower.class, FastPlace.class, ESP.class, HytNoHurt.class, HytSpeed.class, NoSlow.class, Velocity.class, Speed.class, NameTags.class, FastUse.class, Teleport.class, Fullbright.class, ItemESP.class, NoClip.class, FastClimb.class, Step.class, AutoRespawn.class, AutoTool.class, NoWeb.class, Regen.class, NoFall.class, Blink.class, NameProtect.class, NoHurtCam.class, XRay.class, Timer.class, Aimbot.class, Eagle.class, HitBox.class, AntiCactus.class, ConsoleSpammer.class, LongJump.class, FastBow.class, AutoClicker.class, NoBob.class, NoFriends.class, Chams.class, Clip.class, ServerCrasher.class, NoFOV.class, FastStairs.class, TNTBlock.class, InventoryCleaner.class, TrueSight.class, AntiBlind.class, NoSwing.class, Breadcrumbs.class, AntiVoid.class, AbortBreaking.class, PotionSaver.class, CameraClip.class, NoPitchLimit.class, AirLadder.class, TeleportHit.class, BufferSpeed.class, SuperKnockback.class, ProphuntESP.class, Damage.class, KeepContainer.class, VehicleOneHit.class, Reach.class, Rotations.class, NoJumpDelay.class, AntiAFK.class, HUD.class, ComponentOnHover.class, ResourcePackSpoof.class, NoSlowBreak.class, PortalMenu.class, EnchantEffect.class, KeepChest.class, SpeedMine.class, AutoHead.class, Animations.class, Test.class, BlurSettings.class, JumpCircle.class, CapeManager.class, AutoPlay.class, HytGetName.class, Ambience.class, ChatTranslator.class, DMGParticle.class, TianKengHelper.class, FakeFPS.class, ColorMixer.class, AuraKeepSprint.class, BedFucker.class, HotbarSettings.class, Crosshair.class);
        this.registerModule(NoScoreboard.INSTANCE);
        this.registerModule(Fucker.INSTANCE);
        this.registerModule(ChestAura.INSTANCE);
        ClientUtils.getLogger().info("[ModuleManager] Loaded " + this.modules.size() + " modules.");
    }

    public final void registerModule(@NotNull Module module) {
        Intrinsics.checkParameterIsNotNull((Object)module, (String)"module");
        Collection collection = this.modules;
        boolean bl = false;
        collection.add(module);
        ((Map)this.moduleClassMap).put(module.getClass(), module);
        this.generateCommand$LiKingSense(module);
        LiquidBounce.INSTANCE.getEventManager().registerListener(module);
    }

    private final void registerModule(Class<? extends Module> moduleClass) {
        try {
            Module module = moduleClass.newInstance();
            Intrinsics.checkExpressionValueIsNotNull((Object)module, (String)"moduleClass.newInstance()");
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
        Intrinsics.checkParameterIsNotNull(modules2, (String)"modules");
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

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final List<Module> getModuleInCategory(@NotNull ModuleCategory category) {
        void $this$filterTo$iv$iv;
        Intrinsics.checkParameterIsNotNull((Object)((Object)category), (String)"category");
        Iterable $this$filter$iv = this.modules;
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Module it = (Module)element$iv$iv;
            boolean bl = false;
            if (!(it.getCategory() == category)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        return (List)destination$iv$iv;
    }

    public final void unregisterModule(@NotNull Module module) {
        Intrinsics.checkParameterIsNotNull((Object)module, (String)"module");
        this.modules.remove(module);
        this.moduleClassMap.remove(module.getClass());
        LiquidBounce.INSTANCE.getEventManager().unregisterListener(module);
    }

    @Nullable
    public final ArrayList<Module> getModulesInCategory(@NotNull ModuleCategory cat) {
        Intrinsics.checkParameterIsNotNull((Object)((Object)cat), (String)"cat");
        ArrayList<Module> modsInCat = new ArrayList<Module>();
        for (Module mod : LiquidBounce.INSTANCE.getModuleManager().modules) {
            if (mod.getCategory() != cat) continue;
            modsInCat.add(mod);
        }
        return modsInCat;
    }

    public final void generateCommand$LiKingSense(@NotNull Module module) {
        Intrinsics.checkParameterIsNotNull((Object)module, (String)"module");
        List<Value<?>> values = module.getValues();
        if (values.isEmpty()) {
            return;
        }
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(new ModuleCommand(module, values));
    }

    @NotNull
    public final Module getModule(@NotNull Class<?> moduleClass) {
        Intrinsics.checkParameterIsNotNull(moduleClass, (String)"moduleClass");
        Module module = this.moduleClassMap.get(moduleClass);
        Intrinsics.checkExpressionValueIsNotNull((Object)module, (String)"moduleClassMap[moduleClass]!!");
        return module;
    }

    @NotNull
    public final Module get(@NotNull Class<?> clazz) {
        Intrinsics.checkParameterIsNotNull(clazz, (String)"clazz");
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
            for (Object t2 : iterable2) {
                Module it = (Module)t2;
                boolean bl3 = false;
                if (!StringsKt.equals((String)it.getName(), (String)moduleName, (boolean)true)) continue;
                v0 = t2;
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

