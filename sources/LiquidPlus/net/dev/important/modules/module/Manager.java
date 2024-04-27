/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.KeyEvent;
import net.dev.important.event.Listenable;
import net.dev.important.modules.module.Command;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.client.CustomTitle;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.modules.module.modules.combat.Aimbot;
import net.dev.important.modules.module.modules.combat.AntiFireBall;
import net.dev.important.modules.module.modules.combat.AutoBow;
import net.dev.important.modules.module.modules.combat.AutoClicker;
import net.dev.important.modules.module.modules.combat.AutoPot;
import net.dev.important.modules.module.modules.combat.AutoSoup;
import net.dev.important.modules.module.modules.combat.AutoWeapon;
import net.dev.important.modules.module.modules.combat.BowAimbot;
import net.dev.important.modules.module.modules.combat.Criticals;
import net.dev.important.modules.module.modules.combat.FastBow;
import net.dev.important.modules.module.modules.combat.HitBox;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.modules.module.modules.combat.NoFriends;
import net.dev.important.modules.module.modules.combat.SuperKnockback;
import net.dev.important.modules.module.modules.combat.TNTBlock;
import net.dev.important.modules.module.modules.combat.TeleportAura;
import net.dev.important.modules.module.modules.combat.Velocity;
import net.dev.important.modules.module.modules.exploit.AbortBreaking;
import net.dev.important.modules.module.modules.exploit.AntiHunger;
import net.dev.important.modules.module.modules.exploit.Clip;
import net.dev.important.modules.module.modules.exploit.ConsoleSpammer;
import net.dev.important.modules.module.modules.exploit.CustomDisabler;
import net.dev.important.modules.module.modules.exploit.Disabler;
import net.dev.important.modules.module.modules.exploit.FakeLag;
import net.dev.important.modules.module.modules.exploit.GhostHand;
import net.dev.important.modules.module.modules.exploit.ItemTeleport;
import net.dev.important.modules.module.modules.exploit.MultiActions;
import net.dev.important.modules.module.modules.exploit.PacketFixer;
import net.dev.important.modules.module.modules.exploit.Phase;
import net.dev.important.modules.module.modules.exploit.PingSpoof;
import net.dev.important.modules.module.modules.exploit.Plugins;
import net.dev.important.modules.module.modules.exploit.PortalMenu;
import net.dev.important.modules.module.modules.exploit.ServerCrasher;
import net.dev.important.modules.module.modules.exploit.Teleport;
import net.dev.important.modules.module.modules.exploit.VehicleOneHit;
import net.dev.important.modules.module.modules.misc.AntiBan;
import net.dev.important.modules.module.modules.misc.AntiBot;
import net.dev.important.modules.module.modules.misc.AntiDesync;
import net.dev.important.modules.module.modules.misc.AntiVanish;
import net.dev.important.modules.module.modules.misc.AuthBypass;
import net.dev.important.modules.module.modules.misc.AutoDisable;
import net.dev.important.modules.module.modules.misc.AutoHypixel;
import net.dev.important.modules.module.modules.misc.AutoKit;
import net.dev.important.modules.module.modules.misc.AutoLogin;
import net.dev.important.modules.module.modules.misc.AutoPlay;
import net.dev.important.modules.module.modules.misc.BanChecker;
import net.dev.important.modules.module.modules.misc.MidClick;
import net.dev.important.modules.module.modules.misc.NameProtect;
import net.dev.important.modules.module.modules.misc.NoInvClose;
import net.dev.important.modules.module.modules.misc.NoRotateSet;
import net.dev.important.modules.module.modules.misc.PackSpoofer;
import net.dev.important.modules.module.modules.misc.Patcher;
import net.dev.important.modules.module.modules.misc.Performance;
import net.dev.important.modules.module.modules.misc.Spammer;
import net.dev.important.modules.module.modules.misc.SpinBot;
import net.dev.important.modules.module.modules.misc.Teams;
import net.dev.important.modules.module.modules.movement.AirJump;
import net.dev.important.modules.module.modules.movement.BowJump;
import net.dev.important.modules.module.modules.movement.FastClimb;
import net.dev.important.modules.module.modules.movement.FastStairs;
import net.dev.important.modules.module.modules.movement.Fly;
import net.dev.important.modules.module.modules.movement.Freeze;
import net.dev.important.modules.module.modules.movement.GuiMove;
import net.dev.important.modules.module.modules.movement.HighJump;
import net.dev.important.modules.module.modules.movement.KeepSprint;
import net.dev.important.modules.module.modules.movement.LiquidWalk;
import net.dev.important.modules.module.modules.movement.LongJump;
import net.dev.important.modules.module.modules.movement.NoJumpDelay;
import net.dev.important.modules.module.modules.movement.NoSlow;
import net.dev.important.modules.module.modules.movement.NoWeb;
import net.dev.important.modules.module.modules.movement.Parkour;
import net.dev.important.modules.module.modules.movement.ReverseStep;
import net.dev.important.modules.module.modules.movement.SafeWalk;
import net.dev.important.modules.module.modules.movement.SlimeJump;
import net.dev.important.modules.module.modules.movement.Sneak;
import net.dev.important.modules.module.modules.movement.Speed;
import net.dev.important.modules.module.modules.movement.Sprint;
import net.dev.important.modules.module.modules.movement.Step;
import net.dev.important.modules.module.modules.movement.Strafe;
import net.dev.important.modules.module.modules.movement.TargetStrafe;
import net.dev.important.modules.module.modules.movement.WallClimb;
import net.dev.important.modules.module.modules.movement.WaterSpeed;
import net.dev.important.modules.module.modules.player.AntiCactus;
import net.dev.important.modules.module.modules.player.AntiVoid;
import net.dev.important.modules.module.modules.player.AutoRespawn;
import net.dev.important.modules.module.modules.player.AutoTool;
import net.dev.important.modules.module.modules.player.Blink;
import net.dev.important.modules.module.modules.player.Eagle;
import net.dev.important.modules.module.modules.player.FastUse;
import net.dev.important.modules.module.modules.player.Gapple;
import net.dev.important.modules.module.modules.player.GetName;
import net.dev.important.modules.module.modules.player.Heal;
import net.dev.important.modules.module.modules.player.InvManager;
import net.dev.important.modules.module.modules.player.NoFall;
import net.dev.important.modules.module.modules.player.PotionSaver;
import net.dev.important.modules.module.modules.player.Reach;
import net.dev.important.modules.module.modules.player.Regen;
import net.dev.important.modules.module.modules.render.Animations;
import net.dev.important.modules.module.modules.render.AntiBlind;
import net.dev.important.modules.module.modules.render.AsianHat;
import net.dev.important.modules.module.modules.render.BlockESP;
import net.dev.important.modules.module.modules.render.BlockOverlay;
import net.dev.important.modules.module.modules.render.Breadcrumbs;
import net.dev.important.modules.module.modules.render.CameraClip;
import net.dev.important.modules.module.modules.render.Cape;
import net.dev.important.modules.module.modules.render.Chams;
import net.dev.important.modules.module.modules.render.ClickGUI;
import net.dev.important.modules.module.modules.render.Crosshair;
import net.dev.important.modules.module.modules.render.DamageParticle;
import net.dev.important.modules.module.modules.render.ESP;
import net.dev.important.modules.module.modules.render.ESP2D;
import net.dev.important.modules.module.modules.render.EnchantEffect;
import net.dev.important.modules.module.modules.render.FreeCam;
import net.dev.important.modules.module.modules.render.Fullbright;
import net.dev.important.modules.module.modules.render.HUD;
import net.dev.important.modules.module.modules.render.ItemESP;
import net.dev.important.modules.module.modules.render.ItemPhysics;
import net.dev.important.modules.module.modules.render.JelloArraylist;
import net.dev.important.modules.module.modules.render.NameTags;
import net.dev.important.modules.module.modules.render.NoFOV;
import net.dev.important.modules.module.modules.render.NoHurtCam;
import net.dev.important.modules.module.modules.render.NoRender;
import net.dev.important.modules.module.modules.render.PlayerEdit;
import net.dev.important.modules.module.modules.render.PointerESP;
import net.dev.important.modules.module.modules.render.Projectiles;
import net.dev.important.modules.module.modules.render.Rotations;
import net.dev.important.modules.module.modules.render.Skeletal;
import net.dev.important.modules.module.modules.render.StorageESP;
import net.dev.important.modules.module.modules.render.TNTESP;
import net.dev.important.modules.module.modules.render.TargetMark;
import net.dev.important.modules.module.modules.render.Tracers;
import net.dev.important.modules.module.modules.render.TrueSight;
import net.dev.important.modules.module.modules.render.XRay;
import net.dev.important.modules.module.modules.world.Ambience;
import net.dev.important.modules.module.modules.world.ChestAura;
import net.dev.important.modules.module.modules.world.ChestStealer;
import net.dev.important.modules.module.modules.world.FastBreak;
import net.dev.important.modules.module.modules.world.FastPlace;
import net.dev.important.modules.module.modules.world.Fucker;
import net.dev.important.modules.module.modules.world.Lightning;
import net.dev.important.modules.module.modules.world.NoSlowBreak;
import net.dev.important.modules.module.modules.world.Scaffold;
import net.dev.important.modules.module.modules.world.Timer;
import net.dev.important.utils.ClientUtils;
import net.dev.important.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0015\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0006H\u0000\u00a2\u0006\u0002\b!J\u0017\u0010\"\u001a\u0004\u0018\u00010\u00062\n\u0010#\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0086\u0002J\u0014\u0010$\u001a\u0004\u0018\u00010\u00062\n\u0010%\u001a\u0006\u0012\u0002\b\u00030\u0005J\u0012\u0010$\u001a\u0004\u0018\u00010\u00062\b\u0010&\u001a\u0004\u0018\u00010'J\b\u0010(\u001a\u00020\rH\u0016J\u0010\u0010)\u001a\u00020\u001f2\u0006\u0010*\u001a\u00020+H\u0003J\u0018\u0010,\u001a\u00020\u001f2\u000e\u0010%\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005H\u0002J\u000e\u0010,\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0006J\u0006\u0010-\u001a\u00020\u001fJ1\u0010-\u001a\u00020\u001f2\"\u0010\b\u001a\u0012\u0012\u000e\b\u0001\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u00050.\"\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005H\u0007\u00a2\u0006\u0002\u0010/J\u000e\u00100\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0006R2\u0010\u0003\u001a&\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0005\u0012\u0004\u0012\u00020\u0006`\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00060\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d\u00a8\u00061"}, d2={"Lnet/dev/important/modules/module/Manager;", "Lnet/dev/important/event/Listenable;", "()V", "moduleClassMap", "Ljava/util/HashMap;", "Ljava/lang/Class;", "Lnet/dev/important/modules/module/Module;", "Lkotlin/collections/HashMap;", "modules", "Ljava/util/TreeSet;", "getModules", "()Ljava/util/TreeSet;", "shouldNotify", "", "getShouldNotify", "()Z", "setShouldNotify", "(Z)V", "toggleSoundMode", "", "getToggleSoundMode", "()I", "setToggleSoundMode", "(I)V", "toggleVolume", "", "getToggleVolume", "()F", "setToggleVolume", "(F)V", "generateCommand", "", "module", "generateCommand$LiquidBounce", "get", "clazz", "getModule", "moduleClass", "moduleName", "", "handleEvents", "onKey", "event", "Lnet/dev/important/event/KeyEvent;", "registerModule", "registerModules", "", "([Ljava/lang/Class;)V", "unregisterModule", "LiquidBounce"})
public final class Manager
implements Listenable {
    @NotNull
    private final TreeSet<Module> modules = new TreeSet(Manager::modules$lambda-0);
    @NotNull
    private final HashMap<Class<?>, Module> moduleClassMap = new HashMap();
    private boolean shouldNotify;
    private int toggleSoundMode;
    private float toggleVolume;

    public Manager() {
        Client.INSTANCE.getEventManager().registerListener(this);
    }

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
        Class[] classArray = new Class[]{PlayerEdit.class, GetName.class, Patcher.class, Performance.class, AutoWeapon.class, BowAimbot.class, Aimbot.class, AutoBow.class, AutoSoup.class, FastBow.class, Criticals.class, KillAura.class, Velocity.class, Fly.class, ClickGUI.class, HighJump.class, GuiMove.class, NoSlow.class, LiquidWalk.class, Strafe.class, Sprint.class, Teams.class, NoRotateSet.class, AntiBot.class, ChestStealer.class, Scaffold.class, FastBreak.class, FastPlace.class, ESP.class, Sneak.class, Speed.class, Tracers.class, NameTags.class, FastUse.class, Fullbright.class, ItemESP.class, StorageESP.class, Projectiles.class, PingSpoof.class, Step.class, AutoRespawn.class, AutoTool.class, NoWeb.class, Spammer.class, Regen.class, NoFall.class, Blink.class, NameProtect.class, NoHurtCam.class, MidClick.class, XRay.class, Timer.class, FreeCam.class, HitBox.class, Plugins.class, LongJump.class, AutoClicker.class, BlockOverlay.class, NoFriends.class, BlockESP.class, Chams.class, Clip.class, Phase.class, ServerCrasher.class, NoFOV.class, Animations.class, ReverseStep.class, TNTBlock.class, InvManager.class, TrueSight.class, AntiBlind.class, Breadcrumbs.class, AbortBreaking.class, PotionSaver.class, CameraClip.class, WaterSpeed.class, SuperKnockback.class, Reach.class, Rotations.class, NoJumpDelay.class, HUD.class, TNTESP.class, PackSpoofer.class, NoSlowBreak.class, PortalMenu.class, Ambience.class, EnchantEffect.class, Cape.class, NoRender.class, DamageParticle.class, AntiVanish.class, Lightning.class, Skeletal.class, ItemPhysics.class, AutoLogin.class, Heal.class, AuthBypass.class, Gapple.class, ColorMixer.class, Disabler.class, CustomDisabler.class, AutoDisable.class, Crosshair.class, VehicleOneHit.class, SpinBot.class, MultiActions.class, AntiVoid.class, AutoHypixel.class, TargetStrafe.class, ESP2D.class, BanChecker.class, TargetMark.class, AntiFireBall.class, KeepSprint.class, ItemTeleport.class, Teleport.class, AsianHat.class, BowJump.class, ConsoleSpammer.class, PointerESP.class, SafeWalk.class, CustomTitle.class, GhostHand.class, AntiHunger.class, AirJump.class, Freeze.class, AntiCactus.class, Eagle.class, FastClimb.class, FastStairs.class, SlimeJump.class, Parkour.class, WallClimb.class, AntiDesync.class, FakeLag.class, PacketFixer.class, AutoPlay.class, AutoKit.class, AntiBan.class, NoInvClose.class, TeleportAura.class, AutoPot.class, JelloArraylist.class};
        this.registerModules(classArray);
        this.registerModule(Fucker.INSTANCE);
        this.registerModule(ChestAura.INSTANCE);
        ClientUtils.getLogger().info("[ModuleManager] Successfully loaded " + this.modules.size() + " modules.");
    }

    public final void registerModule(@NotNull Module module2) {
        Intrinsics.checkNotNullParameter(module2, "module");
        ((Collection)this.modules).add(module2);
        Map map = this.moduleClassMap;
        Class<?> clazz = module2.getClass();
        map.put(clazz, module2);
        module2.onInitialize();
        this.generateCommand$LiquidBounce(module2);
        Client.INSTANCE.getEventManager().registerListener(module2);
    }

    private final void registerModule(Class<? extends Module> moduleClass) {
        try {
            Module module2 = moduleClass.newInstance();
            Intrinsics.checkNotNullExpressionValue(module2, "moduleClass.newInstance()");
            this.registerModule(module2);
        }
        catch (Throwable e) {
            ClientUtils.getLogger().error("Failed to load module: " + moduleClass.getName() + " (" + e.getClass().getName() + ": " + e.getMessage() + ')');
        }
    }

    @SafeVarargs
    public final void registerModules(Class<? extends Module> ... modules) {
        Intrinsics.checkNotNullParameter(modules, "modules");
        Class<? extends Module>[] $this$forEach$iv = modules;
        boolean $i$f$forEach = false;
        for (Class<? extends Module> element$iv : $this$forEach$iv) {
            Class<? extends Module> p0 = element$iv;
            boolean bl = false;
            this.registerModule(p0);
        }
    }

    public final void unregisterModule(@NotNull Module module2) {
        Intrinsics.checkNotNullParameter(module2, "module");
        this.modules.remove(module2);
        this.moduleClassMap.remove(module2.getClass());
        Client.INSTANCE.getEventManager().unregisterListener(module2);
    }

    public final void generateCommand$LiquidBounce(@NotNull Module module2) {
        Intrinsics.checkNotNullParameter(module2, "module");
        List<Value<?>> values2 = module2.getValues();
        if (values2.isEmpty()) {
            return;
        }
        Client.INSTANCE.getCommandManager().registerCommand(new Command(module2, values2));
    }

    @Nullable
    public final Module getModule(@NotNull Class<?> moduleClass) {
        Intrinsics.checkNotNullParameter(moduleClass, "moduleClass");
        return this.moduleClassMap.get(moduleClass);
    }

    @Nullable
    public final Module get(@NotNull Class<?> clazz) {
        Intrinsics.checkNotNullParameter(clazz, "clazz");
        return this.getModule(clazz);
    }

    @Nullable
    public final Module getModule(@Nullable String moduleName) {
        Object v0;
        block1: {
            for (Object t : (Iterable)this.modules) {
                Module it = (Module)t;
                boolean bl = false;
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

    private static final int modules$lambda-0(Module module1, Module module2) {
        return module1.getName().compareTo(module2.getName());
    }
}

