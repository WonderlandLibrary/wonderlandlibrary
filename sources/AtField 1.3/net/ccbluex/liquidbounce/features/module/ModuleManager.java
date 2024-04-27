/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module;

import cc.paimon.modules.combat.BlatantAura;
import cc.paimon.modules.combat.HytAntiVoid;
import cc.paimon.modules.combat.OldVelocity;
import cc.paimon.modules.hyt.AutoLeos;
import cc.paimon.modules.hyt.Helper;
import cc.paimon.modules.hyt.ScaffoldHelper;
import cc.paimon.modules.misc.StrafeFix;
import cc.paimon.modules.render.NewGUI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import liying.module.combat.KillFix;
import liying.module.combat.ScaffoldFix;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCommand;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.autumn.AttackParticles;
import net.ccbluex.liquidbounce.features.module.modules.autumn.GrimFull2;
import net.ccbluex.liquidbounce.features.module.modules.autumn.JumpCircle;
import net.ccbluex.liquidbounce.features.module.modules.autumn.NewSuperKnockback;
import net.ccbluex.liquidbounce.features.module.modules.autumn.SuperKnockbackPlus;
import net.ccbluex.liquidbounce.features.module.modules.autumn.VelocityPro;
import net.ccbluex.liquidbounce.features.module.modules.client.AntiFakePlayer;
import net.ccbluex.liquidbounce.features.module.modules.color.Gident;
import net.ccbluex.liquidbounce.features.module.modules.color.Rainbow;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aimbot;
import net.ccbluex.liquidbounce.features.module.modules.combat.AntiFireBall;
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
import net.ccbluex.liquidbounce.features.module.modules.combat.GrimFull;
import net.ccbluex.liquidbounce.features.module.modules.combat.HitBox;
import net.ccbluex.liquidbounce.features.module.modules.combat.Ignite;
import net.ccbluex.liquidbounce.features.module.modules.combat.KbHelper;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura2;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura3;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura4;
import net.ccbluex.liquidbounce.features.module.modules.combat.LegitAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.combat.SuperKnockback;
import net.ccbluex.liquidbounce.features.module.modules.combat.TNTBlock;
import net.ccbluex.liquidbounce.features.module.modules.combat.TargetStrafe;
import net.ccbluex.liquidbounce.features.module.modules.combat.TeleportHit;
import net.ccbluex.liquidbounce.features.module.modules.combat.Trigger;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity2;
import net.ccbluex.liquidbounce.features.module.modules.combat.VelocityPlus;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AbortBreaking;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AntiHunger;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Clip;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ConsoleSpammer;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Damage;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ForceUnicodeChat;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ItemTeleport;
import net.ccbluex.liquidbounce.features.module.modules.exploit.KeepContainer;
import net.ccbluex.liquidbounce.features.module.modules.exploit.NoC03;
import net.ccbluex.liquidbounce.features.module.modules.exploit.NoPitchLimit;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Phase;
import net.ccbluex.liquidbounce.features.module.modules.exploit.PingSpoof;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Plugins;
import net.ccbluex.liquidbounce.features.module.modules.exploit.PortalMenu;
import net.ccbluex.liquidbounce.features.module.modules.exploit.ServerCrasher;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Teleport;
import net.ccbluex.liquidbounce.features.module.modules.exploit.VehicleOneHit;
import net.ccbluex.liquidbounce.features.module.modules.hyt.AutoBlock;
import net.ccbluex.liquidbounce.features.module.modules.hyt.DisablerC0f;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytAutoLeos;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytDisabler;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytGapple;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytGetName;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytNoFucker;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytNoHurt;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytNoLagBack;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytSpeed;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.misc.AtAllProvider;
import net.ccbluex.liquidbounce.features.module.modules.misc.ComponentOnHover;
import net.ccbluex.liquidbounce.features.module.modules.misc.MidClick;
import net.ccbluex.liquidbounce.features.module.modules.misc.NameProtect;
import net.ccbluex.liquidbounce.features.module.modules.misc.NoRotateSet;
import net.ccbluex.liquidbounce.features.module.modules.misc.ResourcePackSpoof;
import net.ccbluex.liquidbounce.features.module.modules.misc.Spammer;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.features.module.modules.misc.Test;
import net.ccbluex.liquidbounce.features.module.modules.misc.Title;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirLadder;
import net.ccbluex.liquidbounce.features.module.modules.movement.AutoWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.BufferSpeed;
import net.ccbluex.liquidbounce.features.module.modules.movement.BugUp;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastClimb;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastStairs;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.movement.Freeze;
import net.ccbluex.liquidbounce.features.module.modules.movement.HighJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.IceSpeed;
import net.ccbluex.liquidbounce.features.module.modules.movement.InventoryMove;
import net.ccbluex.liquidbounce.features.module.modules.movement.LiquidWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.LongJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoClip;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoJumpDelay;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow2;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoWeb;
import net.ccbluex.liquidbounce.features.module.modules.movement.Parkour;
import net.ccbluex.liquidbounce.features.module.modules.movement.SafeWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sneak;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sprint;
import net.ccbluex.liquidbounce.features.module.modules.movement.Step;
import net.ccbluex.liquidbounce.features.module.modules.movement.Strafe;
import net.ccbluex.liquidbounce.features.module.modules.movement.WallClimb;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiAFK;
import net.ccbluex.liquidbounce.features.module.modules.player.AntiCactus;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoFish;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoGG;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoL;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoRespawn;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoTool;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.player.Eagle;
import net.ccbluex.liquidbounce.features.module.modules.player.FastUse;
import net.ccbluex.liquidbounce.features.module.modules.player.HytBlink;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.features.module.modules.player.NoFall;
import net.ccbluex.liquidbounce.features.module.modules.player.PotionSaver;
import net.ccbluex.liquidbounce.features.module.modules.player.Reach;
import net.ccbluex.liquidbounce.features.module.modules.player.Regen;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockESP;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay;
import net.ccbluex.liquidbounce.features.module.modules.render.BlurSettings;
import net.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs;
import net.ccbluex.liquidbounce.features.module.modules.render.CameraClip;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomFont;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomHUD;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import net.ccbluex.liquidbounce.features.module.modules.render.EnchantEffect;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.features.module.modules.render.Fullbright;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.ItemESP;
import net.ccbluex.liquidbounce.features.module.modules.render.NameTags;
import net.ccbluex.liquidbounce.features.module.modules.render.NoBob;
import net.ccbluex.liquidbounce.features.module.modules.render.NoFOV;
import net.ccbluex.liquidbounce.features.module.modules.render.NoHurtCam;
import net.ccbluex.liquidbounce.features.module.modules.render.NoScoreboard;
import net.ccbluex.liquidbounce.features.module.modules.render.NoSwing;
import net.ccbluex.liquidbounce.features.module.modules.render.OldHitting;
import net.ccbluex.liquidbounce.features.module.modules.render.PictureColor;
import net.ccbluex.liquidbounce.features.module.modules.render.PictureColor2;
import net.ccbluex.liquidbounce.features.module.modules.render.Projectiles;
import net.ccbluex.liquidbounce.features.module.modules.render.ProphuntESP;
import net.ccbluex.liquidbounce.features.module.modules.render.Rotations;
import net.ccbluex.liquidbounce.features.module.modules.render.StorageESP;
import net.ccbluex.liquidbounce.features.module.modules.render.TNTESP;
import net.ccbluex.liquidbounce.features.module.modules.render.Tracers;
import net.ccbluex.liquidbounce.features.module.modules.render.TrueSight;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import net.ccbluex.liquidbounce.features.module.modules.tomk.MemoryFix;
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
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold2;
import net.ccbluex.liquidbounce.features.module.modules.world.ScaffoldNew;
import net.ccbluex.liquidbounce.features.module.modules.world.SpeedMine;
import net.ccbluex.liquidbounce.features.module.modules.world.Timer;
import net.ccbluex.liquidbounce.features.module.modules.world.Tower;
import net.ccbluex.liquidbounce.features.module.modules.world.WorldTime;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.jetbrains.annotations.Nullable;

public final class ModuleManager
implements Listenable {
    private final TreeSet modules = new TreeSet(modules.1.INSTANCE);
    private final HashMap moduleClassMap;

    public final Module get(Class clazz) {
        return this.getModule(clazz);
    }

    public final void generateCommand$AtField(Module module) {
        List list = module.getValues();
        if (list.isEmpty()) {
            return;
        }
        LiquidBounce.INSTANCE.getCommandManager().registerCommand(new ModuleCommand(module, list));
    }

    @EventTarget
    private final void onKey(KeyEvent keyEvent) {
        Iterable iterable = this.modules;
        boolean bl = false;
        Iterable iterable2 = iterable;
        Collection collection2 = new ArrayList();
        boolean bl2 = false;
        Iterator iterator2 = iterable2.iterator();
        while (iterator2.hasNext()) {
            Object t = iterator2.next();
            Module module = (Module)t;
            boolean bl3 = false;
            if (!(module.getKeyBind() == keyEvent.getKey())) continue;
            collection2.add(t);
        }
        iterable = (List)collection2;
        bl = false;
        for (Collection collection2 : iterable) {
            Module module = (Module)((Object)collection2);
            boolean bl4 = false;
            module.toggle();
        }
    }

    @SafeVarargs
    public final void registerModules(Class ... classArray) {
        Class[] classArray2 = classArray;
        ModuleManager moduleManager = this;
        boolean bl = false;
        Class[] classArray3 = classArray2;
        int n = classArray3.length;
        for (int i = 0; i < n; ++i) {
            Class clazz;
            Class clazz2 = clazz = classArray3[i];
            boolean bl2 = false;
            moduleManager.registerModule(clazz2);
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    public ModuleManager() {
        HashMap hashMap;
        ModuleManager moduleManager = this;
        boolean bl = false;
        moduleManager.moduleClassMap = hashMap = new HashMap();
        LiquidBounce.INSTANCE.getEventManager().registerListener(this);
    }

    public final Module getModule(Class clazz) {
        Object v = this.moduleClassMap.get(clazz);
        if (v == null) {
            Intrinsics.throwNpe();
        }
        return (Module)v;
    }

    public final TreeSet getModules() {
        return this.modules;
    }

    public final void registerModule(Module module) {
        if (!module.isSupported()) {
            return;
        }
        Collection collection = this.modules;
        boolean bl = false;
        collection.add(module);
        ((Map)this.moduleClassMap).put(module.getClass(), module);
        this.generateCommand$AtField(module);
        LiquidBounce.INSTANCE.getEventManager().registerListener(module);
    }

    public final void registerModules() {
        ClientUtils.getLogger().info("[ModuleManager] Loading modules...");
        this.registerModules(HytDisabler.class, HytNoLagBack.class, HytAutoLeos.class, KillAura.class, KbHelper.class, CustomColor.class, AutoGG.class, AutoL.class, AntiHunger.class, HytGapple.class, HytNoFucker.class, HytGetName.class, CustomFont.class, KillFix.class, ScaffoldFix.class, CustomHUD.class, Title.class, AutoArmor.class, AutoBow.class, AutoRunaway.class, AutoPot.class, AutoSoup.class, AutoWeapon.class, BowAimbot.class, Criticals.class, Trigger.class, Fly.class, ClickGUI.class, HighJump.class, InventoryMove.class, LiquidWalk.class, SafeWalk.class, WallClimb.class, Strafe.class, Sprint.class, Teams.class, NoRotateSet.class, ChestStealer.class, Scaffold.class, CivBreak.class, Tower.class, FastBreak.class, FastPlace.class, ESP.class, HytNoHurt.class, HytSpeed.class, NoSlow.class, Velocity.class, Speed.class, Tracers.class, NameTags.class, FastUse.class, Teleport.class, Fullbright.class, ItemESP.class, StorageESP.class, Projectiles.class, NoClip.class, Nuker.class, PingSpoof.class, FastClimb.class, Step.class, AutoRespawn.class, AutoTool.class, NoWeb.class, Spammer.class, IceSpeed.class, Regen.class, NoFall.class, Blink.class, NameProtect.class, NoHurtCam.class, MidClick.class, XRay.class, Timer.class, Sneak.class, FreeCam.class, Aimbot.class, Eagle.class, HitBox.class, AntiCactus.class, Plugins.class, ConsoleSpammer.class, LongJump.class, Parkour.class, FastBow.class, AutoClicker.class, NoBob.class, BlockOverlay.class, NoFriends.class, BlockESP.class, Chams.class, Clip.class, Phase.class, ServerCrasher.class, NoFOV.class, FastStairs.class, TNTBlock.class, InventoryCleaner.class, TrueSight.class, AntiBlind.class, NoSwing.class, Breadcrumbs.class, AbortBreaking.class, PotionSaver.class, CameraClip.class, Ignite.class, NoPitchLimit.class, Liquids.class, AtAllProvider.class, AirLadder.class, TeleportHit.class, ForceUnicodeChat.class, ItemTeleport.class, BufferSpeed.class, SuperKnockback.class, ProphuntESP.class, AutoFish.class, Damage.class, Freeze.class, KeepContainer.class, VehicleOneHit.class, Reach.class, Rotations.class, NoJumpDelay.class, AntiAFK.class, HUD.class, TNTESP.class, ComponentOnHover.class, ResourcePackSpoof.class, NoSlowBreak.class, PortalMenu.class, EnchantEffect.class, SpeedMine.class, AutoHead.class, Animations.class, Test.class, BlurSettings.class, StrafeFix.class, ScaffoldHelper.class, NewGUI.class, TargetStrafe.class, OldVelocity.class, Helper.class, AutoLeos.class, HytAntiVoid.class, BlatantAura.class, KillAura2.class, OldHitting.class, Rainbow.class, Gident.class, HytBlink.class, DisablerC0f.class, PictureColor.class, PictureColor2.class, NoC03.class, AutoBlock.class, GrimFull.class, ScaffoldNew.class, LegitAura.class, AntiFakePlayer.class, MemoryFix.class, VelocityPlus.class, BugUp.class, Velocity2.class, AutoWalk.class, Scaffold2.class, KillAura3.class, KillAura4.class, AntiFireBall.class, NoSlow2.class, JumpCircle.class, VelocityPro.class, NewSuperKnockback.class, AttackParticles.class, WorldTime.class, GrimFull2.class, SuperKnockbackPlus.class);
        this.registerModule(NoScoreboard.INSTANCE);
        this.registerModule(Fucker.INSTANCE);
        this.registerModule(ChestAura.INSTANCE);
        this.registerModule(AntiBot.INSTANCE);
        ClientUtils.getLogger().info("[ModuleManager] Loaded " + this.modules.size() + " modules.");
    }

    private final void registerModule(Class clazz) {
        try {
            this.registerModule((Module)clazz.newInstance());
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to load module: " + clazz.getName() + " (" + throwable.getClass().getName() + ": " + throwable.getMessage() + ')');
        }
    }

    public final Module getModule(@Nullable String string) {
        Object v0;
        block1: {
            Iterable iterable = this.modules;
            boolean bl = false;
            Iterable iterable2 = iterable;
            boolean bl2 = false;
            for (Object t : iterable2) {
                Module module = (Module)t;
                boolean bl3 = false;
                if (!StringsKt.equals((String)module.getName(), (String)string, (boolean)true)) continue;
                v0 = t;
                break block1;
            }
            v0 = null;
        }
        return v0;
    }

    public final void unregisterModule(Module module) {
        this.modules.remove(module);
        this.moduleClassMap.remove(module.getClass());
        LiquidBounce.INSTANCE.getEventManager().unregisterListener(module);
    }
}

