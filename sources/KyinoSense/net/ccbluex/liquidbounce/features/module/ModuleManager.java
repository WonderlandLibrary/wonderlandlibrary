/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.report.liquidware.modules.fun.AAC5Diabler;
import me.report.liquidware.modules.fun.AutoKit;
import me.report.liquidware.modules.fun.AutoLogin;
import me.report.liquidware.modules.fun.BlocksmcDisabler;
import me.report.liquidware.modules.fun.ChatBypass;
import me.report.liquidware.modules.fun.Disabler;
import me.report.liquidware.modules.fun.DisablerFixer;
import me.report.liquidware.modules.fun.FakePlayer;
import me.report.liquidware.modules.fun.GetName;
import me.report.liquidware.modules.fun.HackerDetector;
import me.report.liquidware.modules.fun.LagBack;
import me.report.liquidware.modules.fun.MemoryFixer;
import me.report.liquidware.modules.fun.PacketMonitor;
import me.report.liquidware.modules.fun.Rotations;
import me.report.liquidware.modules.misc.AAC437Helper;
import me.report.liquidware.modules.misc.AutoL;
import me.report.liquidware.modules.misc.LookTP;
import me.report.liquidware.modules.misc.PacketDebugger;
import me.report.liquidware.modules.misc.PacketFixer;
import me.report.liquidware.modules.movement.BoatFly;
import me.report.liquidware.modules.movement.BunnyHop;
import me.report.liquidware.modules.movement.Dash;
import me.report.liquidware.modules.movement.InvMove;
import me.report.liquidware.modules.movement.MatrixFly;
import me.report.liquidware.modules.movement.PeralFly;
import me.report.liquidware.modules.movement.Spider;
import me.report.liquidware.modules.movement.Stair;
import me.report.liquidware.modules.movement.TargetStrafe;
import me.report.liquidware.modules.movement.VerusFly;
import me.report.liquidware.modules.player.AutoBypass;
import me.report.liquidware.modules.player.AutoHypixel;
import me.report.liquidware.modules.player.AutoPlay;
import me.report.liquidware.modules.player.ChatBot;
import me.report.liquidware.modules.player.ChatTranslator;
import me.report.liquidware.modules.player.FastLadder;
import me.report.liquidware.modules.player.LTap;
import me.report.liquidware.modules.player.NoFallPlus;
import me.report.liquidware.modules.player.TPAura;
import me.report.liquidware.modules.render.Ambience;
import me.report.liquidware.modules.render.Animations;
import me.report.liquidware.modules.render.AntiAim;
import me.report.liquidware.modules.render.Camera;
import me.report.liquidware.modules.render.Cape;
import me.report.liquidware.modules.render.ColorMixer;
import me.report.liquidware.modules.render.Crosshair;
import me.report.liquidware.modules.render.CustomModel;
import me.report.liquidware.modules.render.DMGParticles;
import me.report.liquidware.modules.render.EnchantEffect;
import me.report.liquidware.modules.render.FPSHurtCam;
import me.report.liquidware.modules.render.FakeDead;
import me.report.liquidware.modules.render.HanabiEffects;
import me.report.liquidware.modules.render.HeahHat;
import me.report.liquidware.modules.render.Health;
import me.report.liquidware.modules.render.Healths;
import me.report.liquidware.modules.render.HitSound;
import me.report.liquidware.modules.render.HudColors;
import me.report.liquidware.modules.render.HudDesigner;
import me.report.liquidware.modules.render.ItemPhysics;
import me.report.liquidware.modules.render.ItemRotate;
import me.report.liquidware.modules.render.NewNameTags;
import me.report.liquidware.modules.render.NoAchievements;
import me.report.liquidware.modules.render.NoInvClose;
import me.report.liquidware.modules.render.NoRender;
import me.report.liquidware.modules.render.PlayerEdit;
import me.report.liquidware.modules.render.PlayerHealthSend;
import me.report.liquidware.modules.render.PlayerPosition;
import me.report.liquidware.modules.render.PlayerSize;
import me.report.liquidware.modules.render.Rotate;
import me.report.liquidware.modules.render.SilentView;
import me.report.liquidware.modules.render.Title;
import me.report.liquidware.modules.world.AntiStaff;
import me.report.liquidware.modules.world.AutoHeal;
import me.report.liquidware.modules.world.GameSpeed;
import me.report.liquidware.modules.world.LightningDetector;
import me.report.liquidware.modules.world.ResetVL;
import me.report.liquidware.modules.world.SpeedMine;
import me.report.liquidware.modules.world.VClip;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleCommand;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aimbot;
import net.ccbluex.liquidbounce.features.module.modules.combat.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoArmor;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoBow;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoClicker;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoLeave;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoPotion;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoSword;
import net.ccbluex.liquidbounce.features.module.modules.combat.BowAimbot;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.FastBow;
import net.ccbluex.liquidbounce.features.module.modules.combat.HitBox;
import net.ccbluex.liquidbounce.features.module.modules.combat.Ignite;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.combat.SuperKnockback;
import net.ccbluex.liquidbounce.features.module.modules.combat.TNTBlock;
import net.ccbluex.liquidbounce.features.module.modules.combat.Teams;
import net.ccbluex.liquidbounce.features.module.modules.combat.TeleportHit;
import net.ccbluex.liquidbounce.features.module.modules.combat.Trigger;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity;
import net.ccbluex.liquidbounce.features.module.modules.fun.AbortBreaking;
import net.ccbluex.liquidbounce.features.module.modules.fun.AntiHunger;
import net.ccbluex.liquidbounce.features.module.modules.fun.AtAllProvider;
import net.ccbluex.liquidbounce.features.module.modules.fun.BedGodMode;
import net.ccbluex.liquidbounce.features.module.modules.fun.Clip;
import net.ccbluex.liquidbounce.features.module.modules.fun.ComponentOnHover;
import net.ccbluex.liquidbounce.features.module.modules.fun.ConsoleSpammer;
import net.ccbluex.liquidbounce.features.module.modules.fun.Damage;
import net.ccbluex.liquidbounce.features.module.modules.fun.Derp;
import net.ccbluex.liquidbounce.features.module.modules.fun.ForceUnicodeChat;
import net.ccbluex.liquidbounce.features.module.modules.fun.Ghost;
import net.ccbluex.liquidbounce.features.module.modules.fun.GhostHand;
import net.ccbluex.liquidbounce.features.module.modules.fun.GodMode;
import net.ccbluex.liquidbounce.features.module.modules.fun.ItemTeleport;
import net.ccbluex.liquidbounce.features.module.modules.fun.KeepContainer;
import net.ccbluex.liquidbounce.features.module.modules.fun.Kick;
import net.ccbluex.liquidbounce.features.module.modules.fun.MidClick;
import net.ccbluex.liquidbounce.features.module.modules.fun.MoreCarry;
import net.ccbluex.liquidbounce.features.module.modules.fun.MultiActions;
import net.ccbluex.liquidbounce.features.module.modules.fun.NameProtect;
import net.ccbluex.liquidbounce.features.module.modules.fun.NoPitchLimit;
import net.ccbluex.liquidbounce.features.module.modules.fun.ResourcePackSpoof;
import net.ccbluex.liquidbounce.features.module.modules.fun.SkinDerp;
import net.ccbluex.liquidbounce.features.module.modules.fun.Spammer;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirLadder;
import net.ccbluex.liquidbounce.features.module.modules.movement.AntiFall;
import net.ccbluex.liquidbounce.features.module.modules.movement.AutoWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.BlockWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.BufferSpeed;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastClimb;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.movement.Freeze;
import net.ccbluex.liquidbounce.features.module.modules.movement.HighJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.IceSpeed;
import net.ccbluex.liquidbounce.features.module.modules.movement.LadderJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.LiquidWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.LongJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoClip;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoJumpDelay;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoRotateSet;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlowDown;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoWeb;
import net.ccbluex.liquidbounce.features.module.modules.movement.Paralyze;
import net.ccbluex.liquidbounce.features.module.modules.movement.Parkour;
import net.ccbluex.liquidbounce.features.module.modules.movement.PerfectHorseJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.Phase;
import net.ccbluex.liquidbounce.features.module.modules.movement.PingSpoof;
import net.ccbluex.liquidbounce.features.module.modules.movement.Plugins;
import net.ccbluex.liquidbounce.features.module.modules.movement.PortalMenu;
import net.ccbluex.liquidbounce.features.module.modules.movement.ReverseStep;
import net.ccbluex.liquidbounce.features.module.modules.movement.SafeWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.ServerCrasher;
import net.ccbluex.liquidbounce.features.module.modules.movement.SlimeJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sneak;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sprint;
import net.ccbluex.liquidbounce.features.module.modules.movement.Step;
import net.ccbluex.liquidbounce.features.module.modules.movement.Strafe;
import net.ccbluex.liquidbounce.features.module.modules.movement.Teleport;
import net.ccbluex.liquidbounce.features.module.modules.movement.VehicleOneHit;
import net.ccbluex.liquidbounce.features.module.modules.movement.WaterSpeed;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiAFK;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiCactus;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoFish;
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
import net.ccbluex.liquidbounce.features.module.modules.player.Zoot;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockESP;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay;
import net.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP2D;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.features.module.modules.render.Fullbright;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.ItemESP;
import net.ccbluex.liquidbounce.features.module.modules.render.NameTags;
import net.ccbluex.liquidbounce.features.module.modules.render.NoBob;
import net.ccbluex.liquidbounce.features.module.modules.render.NoFOV;
import net.ccbluex.liquidbounce.features.module.modules.render.NoScoreboard;
import net.ccbluex.liquidbounce.features.module.modules.render.NoSwing;
import net.ccbluex.liquidbounce.features.module.modules.render.Projectiles;
import net.ccbluex.liquidbounce.features.module.modules.render.ProphuntESP;
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
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0015\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0006H\u0000\u00a2\u0006\u0002\b!J\u0017\u0010\"\u001a\u0004\u0018\u00010\u00062\n\u0010#\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0086\u0002J\u0014\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00060%2\u0006\u0010&\u001a\u00020\u0013J\u0014\u0010'\u001a\u0004\u0018\u00010\u00062\n\u0010(\u001a\u0006\u0012\u0002\b\u00030\u0005J\u0012\u0010'\u001a\u0004\u0018\u00010\u00062\b\u0010)\u001a\u0004\u0018\u00010*J\u0014\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00060,2\u0006\u0010-\u001a\u00020.J\u0014\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00060%2\u0006\u00100\u001a\u00020*J\b\u00101\u001a\u00020\rH\u0016J\u0010\u00102\u001a\u00020\u001f2\u0006\u00103\u001a\u000204H\u0003J\u0018\u00105\u001a\u00020\u001f2\u000e\u0010(\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005H\u0002J\u000e\u00105\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0006J\b\u00106\u001a\u00020\u001fH\u0007J1\u00106\u001a\u00020\u001f2\"\u0010\b\u001a\u0012\u0012\u000e\b\u0001\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u000507\"\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005H\u0007\u00a2\u0006\u0002\u00108J\u000e\u00109\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0006R2\u0010\u0003\u001a&\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0005\u0012\u0004\u0012\u00020\u0006`\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00060\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d\u00a8\u0006:"}, d2={"Lnet/ccbluex/liquidbounce/features/module/ModuleManager;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "moduleClassMap", "Ljava/util/HashMap;", "Ljava/lang/Class;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "Lkotlin/collections/HashMap;", "modules", "Ljava/util/TreeSet;", "getModules", "()Ljava/util/TreeSet;", "shouldNotify", "", "getShouldNotify", "()Z", "setShouldNotify", "(Z)V", "toggleSoundMode", "", "getToggleSoundMode", "()I", "setToggleSoundMode", "(I)V", "toggleVolume", "", "getToggleVolume", "()F", "setToggleVolume", "(F)V", "generateCommand", "", "module", "generateCommand$KyinoClient", "get", "clazz", "getKeyBind", "", "key", "getModule", "moduleClass", "moduleName", "", "getModuleInCategory", "Ljava/util/ArrayList;", "Category", "Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;", "getModulesByName", "name", "handleEvents", "onKey", "event", "Lnet/ccbluex/liquidbounce/event/KeyEvent;", "registerModule", "registerModules", "", "([Ljava/lang/Class;)V", "unregisterModule", "KyinoClient"})
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

    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void registerModules() {
        ClientUtils.getLogger().info("[ModuleManager] Loading modules...");
        this.registerModules(TargetStrafe.class, SilentView.class, Rotate.class, PlayerEdit.class, ItemRotate.class, HudDesigner.class, NewNameTags.class, AutoL.class, NoFallPlus.class, VClip.class, HanabiEffects.class, PacketDebugger.class, AutoBypass.class, Rotations.class, HudColors.class, NoInvClose.class, FPSHurtCam.class, PacketFixer.class, AAC437Helper.class, GetName.class, TPAura.class, DisablerFixer.class, Cape.class, Sneak.class, LagBack.class, AutoLogin.class, MatrixFly.class, FakePlayer.class, ResetVL.class, AutoHypixel.class, Title.class, HitSound.class, Crosshair.class, MemoryFixer.class, LTap.class, PlayerSize.class, AntiAim.class, ChatBypass.class, NoAchievements.class, EnchantEffect.class, AutoPlay.class, ChatTranslator.class, NoRender.class, Spider.class, AutoHeal.class, Ambience.class, DMGParticles.class, AutoKit.class, ItemPhysics.class, HeahHat.class, BoatFly.class, PacketMonitor.class, Dash.class, FastLadder.class, Animations.class, HackerDetector.class, LightningDetector.class, Stair.class, ESP2D.class, Healths.class, Health.class, AAC5Diabler.class, SpeedMine.class, ChatBot.class, ColorMixer.class, BunnyHop.class, Camera.class, GameSpeed.class, VerusFly.class, LookTP.class, PeralFly.class, Disabler.class, BlocksmcDisabler.class, PlayerPosition.class, PlayerHealthSend.class, FakeDead.class, AntiStaff.class, CustomModel.class, AutoArmor.class, AutoBow.class, AutoLeave.class, AutoPotion.class, AutoSword.class, BowAimbot.class, Criticals.class, KillAura.class, Trigger.class, Velocity.class, Fly.class, ClickGUI.class, HighJump.class, InvMove.class, NoSlowDown.class, LiquidWalk.class, SafeWalk.class, Strafe.class, Sprint.class, Teams.class, NoRotateSet.class, AntiBot.class, ChestStealer.class, CivBreak.class, Scaffold.class, Tower.class, FastBreak.class, FastPlace.class, ESP.class, Speed.class, Tracers.class, NameTags.class, FastUse.class, Teleport.class, Fullbright.class, ItemESP.class, StorageESP.class, Projectiles.class, NoClip.class, Nuker.class, PingSpoof.class, FastClimb.class, Step.class, AutoRespawn.class, AutoTool.class, NoWeb.class, Spammer.class, IceSpeed.class, Zoot.class, Regen.class, NoFall.class, Blink.class, NameProtect.class, Ghost.class, MidClick.class, XRay.class, Timer.class, Sneak.class, SkinDerp.class, Paralyze.class, GhostHand.class, AutoWalk.class, AutoBreak.class, FreeCam.class, Aimbot.class, Eagle.class, HitBox.class, AntiCactus.class, Plugins.class, AntiHunger.class, ConsoleSpammer.class, LongJump.class, Parkour.class, LadderJump.class, FastBow.class, MultiActions.class, AirJump.class, AutoClicker.class, NoBob.class, BlockOverlay.class, NoFriends.class, BlockESP.class, Chams.class, Clip.class, Phase.class, ServerCrasher.class, NoFOV.class, Stair.class, SwingAnimation.class, Derp.class, ReverseStep.class, TNTBlock.class, InventoryCleaner.class, TrueSight.class, NoSwing.class, BedGodMode.class, AntiFall.class, Breadcrumbs.class, AbortBreaking.class, PotionSaver.class, WaterSpeed.class, Ignite.class, SlimeJump.class, MoreCarry.class, NoPitchLimit.class, Kick.class, Liquids.class, AtAllProvider.class, AirLadder.class, GodMode.class, TeleportHit.class, ForceUnicodeChat.class, ItemTeleport.class, BufferSpeed.class, SuperKnockback.class, ProphuntESP.class, AutoFish.class, Damage.class, Freeze.class, KeepContainer.class, VehicleOneHit.class, Reach.class, NoJumpDelay.class, BlockWalk.class, AntiAFK.class, PerfectHorseJump.class, HUD.class, TNTESP.class, ComponentOnHover.class, KeepAlive.class, ResourcePackSpoof.class, NoSlowBreak.class, PortalMenu.class);
        this.registerModule(NoScoreboard.INSTANCE);
        this.registerModule(Fucker.INSTANCE);
        this.registerModule(ChestAura.INSTANCE);
        ClientUtils.getLogger().info("[ModuleManager] Loaded " + this.modules.size() + " modules.");
    }

    public final void registerModule(@NotNull Module module) {
        Intrinsics.checkParameterIsNotNull(module, "module");
        Collection collection = this.modules;
        boolean bl = false;
        collection.add(module);
        ((Map)this.moduleClassMap).put(module.getClass(), module);
        this.generateCommand$KyinoClient(module);
        LiquidBounce.INSTANCE.getEventManager().registerListener(module);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final List<Module> getModulesByName(@NotNull String name) {
        void $this$filterTo$iv$iv;
        Intrinsics.checkParameterIsNotNull(name, "name");
        Iterable $this$filter$iv = this.modules;
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            String string;
            Module it = (Module)element$iv$iv;
            boolean bl = false;
            String string2 = it.getName();
            boolean bl2 = false;
            String string3 = string2;
            if (string3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string4 = string3.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toLowerCase()");
            string2 = name;
            CharSequence charSequence = string4;
            bl2 = false;
            Intrinsics.checkExpressionValueIsNotNull(string2.toLowerCase(), "(this as java.lang.String).toLowerCase()");
            if (!StringsKt.contains$default(charSequence, string, false, 2, null)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        return (List)destination$iv$iv;
    }

    @NotNull
    public final ArrayList<Module> getModuleInCategory(@NotNull ModuleCategory Category2) {
        Intrinsics.checkParameterIsNotNull((Object)Category2, "Category");
        ArrayList<Module> moduleInCategory = new ArrayList<Module>();
        for (Module i : this.modules) {
            if (i.getCategory() != Category2) continue;
            moduleInCategory.add(i);
        }
        return moduleInCategory;
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

    public final void generateCommand$KyinoClient(@NotNull Module module) {
        Intrinsics.checkParameterIsNotNull(module, "module");
        List<Value<?>> values2 = module.getValues();
        if (values2.isEmpty()) {
            return;
        }
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(new ModuleCommand(module, values2));
    }

    @Nullable
    public final Module getModule(@NotNull Class<?> moduleClass) {
        Intrinsics.checkParameterIsNotNull(moduleClass, "moduleClass");
        return this.moduleClassMap.get(moduleClass);
    }

    @Nullable
    public final Module get(@NotNull Class<?> clazz) {
        Intrinsics.checkParameterIsNotNull(clazz, "clazz");
        return this.getModule(clazz);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final List<Module> getKeyBind(int key) {
        void $this$filterTo$iv$iv;
        Iterable $this$filter$iv = this.modules;
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Module it = (Module)element$iv$iv;
            boolean bl = false;
            if (!(it.getKeyBind() == key)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        return (List)destination$iv$iv;
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

