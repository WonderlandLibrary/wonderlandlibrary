// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.combat;

import ru.fluger.client.helpers.world.InventoryHelper;
import ru.fluger.client.event.events.impl.packet.EventReceivePacket;
import ru.fluger.client.event.events.impl.player.EventPostMotion;
import ru.fluger.client.helpers.world.EntityHelper;
import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.feature.impl.movement.GlideFly;
import ru.fluger.client.Fluger;
import ru.fluger.client.helpers.player.KillAuraHelper;
import ru.fluger.client.helpers.misc.TpsHelper;
import ru.fluger.client.event.events.impl.packet.EventAttackSilent;
import org.lwjgl.input.Mouse;
import ru.fluger.client.ui.notification.NotificationManager;
import ru.fluger.client.ui.notification.NotificationType;
import ru.fluger.client.event.EventManager;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.helpers.math.RotationHelper;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.helpers.math.MathematicHelper;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.feature.Feature;

public class KillAura extends Feature
{
    public static TimerHelper oldTimerPvp;
    public static TimerHelper timer;
    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting invis;
    public static BooleanSetting walls;
    public static vp target;
    public static NumberSetting range;
    public static NumberSetting fov;
    public static BooleanSetting onlyCrit;
    public static BooleanSetting spaceOnly;
    public static NumberSetting oldPvpSystemDelayMin;
    public static NumberSetting oldPvpSystemDelayMax;
    public static NumberSetting preAimRange;
    public static BooleanSetting autoDisable;
    public static BooleanSetting rayTrace;
    public static NumberSetting rayTraceBox;
    public static BooleanSetting sprinting;
    public static BooleanSetting shieldBreaker;
    public static BooleanSetting autoBlock;
    public static NumberSetting sendDelay;
    public static NumberSetting outDelay;
    public static BooleanSetting shieldFixer;
    public static BooleanSetting shieldDesync;
    public static NumberSetting fixerDelay;
    public static NumberSetting critFallDistance;
    public static NumberSetting attackCoolDown;
    public static ListSetting rotationMode;
    public static ListSetting sort;
    public static ListSetting clickMode;
    private final TimerHelper blockTimer;
    private final TimerHelper blockTimer1;
    private final TimerHelper shieldFixerTimer;
    public static boolean isAttacking;
    private boolean isBlocking;
    public static int critCounter;
    public static boolean canDo;
    public static boolean isBreaked;
    public static int shieldTicks;
    private int notiTicks;
    
    public KillAura() {
        super("KillAura", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u044c\u0435\u0442 \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0435\u0439 \u0432\u043e\u043a\u0440\u0443\u0433 \u0442\u0435\u0431\u044f", Type.Combat);
        this.blockTimer = new TimerHelper();
        this.blockTimer1 = new TimerHelper();
        this.shieldFixerTimer = new TimerHelper();
        KillAura.rotationMode = new ListSetting("Rotation Mode", "Matrix", () -> true, new String[] { "Matrix", "Snap", "Sunrise", "ReallyWorld", "Client" });
        KillAura.sort = new ListSetting("TargetSort Mode", "Health", () -> true, new String[] { "Distance", "Higher Armor", "Blocking Status", "Lowest Armor", "Health", "Angle", "HurtTime" });
        KillAura.clickMode = new ListSetting("Click Mode", "1.9", () -> true, new String[] { "1.9", "1.8" });
        KillAura.fov = new NumberSetting("FOV", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0440\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0440\u0430\u0434\u0438\u0443\u0441 \u0432 \u043a\u043e\u0442\u043e\u0440\u043e\u043c \u0432\u044b \u043c\u043e\u0436\u0435\u0442\u0435 \u0443\u0434\u0430\u0440\u0438\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u0430", 180.0f, 5.0f, 180.0f, 5.0f, () -> true);
        KillAura.attackCoolDown = new NumberSetting("Attack Cooldown", "\u0420\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u0443\u0435\u0442 \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0443\u0434\u0430\u0440\u0430", 0.9f, 0.1f, 1.0f, 0.01f, () -> KillAura.clickMode.currentMode.equals("1.9"));
        KillAura.oldPvpSystemDelayMin = new NumberSetting("Min APS", "\u041c\u0438\u043d\u0438\u043c\u0430\u043b\u044c\u043d\u043e\u0435 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u043a\u043b\u0438\u043a\u043e\u0432 \u0432 \u0441\u0435\u043a\u0443\u043d\u0434\u0443", 12.0f, 1.0f, 20.0f, 1.0f, () -> KillAura.clickMode.currentMode.equals("1.8"));
        KillAura.oldPvpSystemDelayMax = new NumberSetting("Max APS", "\u041c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u044c\u043d\u043e\u0435 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u043a\u043b\u0438\u043a\u043e\u0432 \u0432 \u0441\u0435\u043a\u0443\u043d\u0434\u0443", 13.0f, 1.0f, 20.0f, 1.0f, () -> KillAura.clickMode.currentMode.equals("1.8"));
        KillAura.range = new NumberSetting("AttackRange", "\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f \u0432 \u043a\u043e\u0442\u043e\u0440\u043e\u0439 \u0432\u044b \u043c\u043e\u0436\u0435\u0442\u0435 \u0443\u0434\u0430\u0440\u0438\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u0430", 3.6f, 3.0f, 7.0f, 0.01f, () -> true);
        KillAura.preAimRange = new NumberSetting("Pre Aim Range", "\u0418\u0433\u0440\u043e\u043a \u0431\u0443\u0434\u0435\u0442 \u043d\u0430\u0432\u043e\u0434\u0438\u0442\u044c\u0441\u044f \u0434\u043e \u0430\u0442\u0430\u043a\u0438", 0.0f, 0.0f, 4.0f, 0.1f, () -> !KillAura.rotationMode.currentMode.equals("None"));
        KillAura.players = new BooleanSetting("Players", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u043e\u0432", true, () -> true);
        KillAura.mobs = new BooleanSetting("Mobs", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u043c\u043e\u0431\u043e\u0432", false, () -> true);
        KillAura.invis = new BooleanSetting("Invisible", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u043d\u0435\u0432\u0438\u0434\u0435\u043c\u044b\u0445 \u0441\u0443\u0449\u0435\u0441\u0442\u0432", true, () -> true);
        KillAura.walls = new BooleanSetting("Walls", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u0441\u043a\u0432\u043e\u0437\u044c \u0441\u0442\u0435\u043d\u044b", true, () -> true);
        KillAura.rayTrace = new BooleanSetting("Ray-Trace", "\u041f\u0440\u043e\u0432\u0435\u0440\u044f\u0435\u0442 \u0447\u0442\u043e \u0431\u044b \u0432\u0430\u0448\u0430 \u0440\u043e\u0442\u0430\u0446\u0438\u044f \u0441\u043c\u043e\u0442\u0440\u0435\u043b\u0430 \u043d\u0430 \u0445\u0438\u0442\u0431\u043e\u043a\u0441 \u0438\u0433\u0440\u043e\u043a\u0430", true, () -> !KillAura.rotationMode.currentMode.equals("Sunrise") && !KillAura.rotationMode.currentMode.equals("ReallyWorld"));
        KillAura.rayTraceBox = new NumberSetting("Ray-Trace Box", "\u0423\u0432\u0435\u043b\u0438\u0447\u0438\u0432\u0430\u0435\u0442 \u0440\u0430\u0434\u0438\u0443\u0441 \u0440\u0435\u0439-\u0442\u0440\u0435\u0439\u0441\u0430", 0.06f, -0.5f, 0.5f, 0.01f, () -> KillAura.rayTrace.getCurrentValue() && !KillAura.rotationMode.currentMode.equals("Sunrise") && !KillAura.rotationMode.currentMode.equals("ReallyWorld"));
        KillAura.sprinting = new BooleanSetting("Stop Sprinting", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0432\u044b\u043a\u043b\u044e\u0447\u0430\u0435\u0442 \u0441\u043f\u0440\u0438\u043d\u0442", false, () -> true);
        KillAura.shieldBreaker = new BooleanSetting("Shield Breaker", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043b\u043e\u043c\u0430\u0435\u0442 \u0449\u0438\u0442 \u0441\u043e\u043f\u0435\u0440\u043d\u0438\u043a\u0443", false, () -> true);
        KillAura.shieldFixer = new BooleanSetting("Shield Fixer", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u043e\u0432 \u0447\u0435\u0440\u0435\u0437 \u0449\u0438\u0442 (\u043e\u0431\u0445\u043e\u0434)", false, () -> true);
        KillAura.fixerDelay = new NumberSetting("Fixer Delay", "\u0420\u0435\u0433\u0443\u043b\u0438\u0440\u043e\u0432\u043a\u0430 \u043a\u0430\u043a \u0434\u043e\u043b\u0433\u043e \u0449\u0438\u0442 \u0431\u0443\u0434\u0435\u0442 \u043e\u0442\u0436\u043c\u0438\u043c\u0430\u0442\u044c\u0441\u044f (\u0447\u0435\u043c \u0431\u043e\u043b\u044c\u0448\u0435, \u0442\u0435\u043c \u0449\u0438\u0442 \u0431\u0443\u0434\u0435\u0442 \u0434\u043e\u043b\u044c\u0448\u0435 \u043e\u0442\u0436\u0438\u043c\u0430\u0442\u044c\u0441\u044f)", 150.0f, 0.0f, 600.0f, 10.0f, () -> KillAura.shieldFixer.getCurrentValue());
        KillAura.shieldDesync = new BooleanSetting("Shield Desync", "\u0414\u0435\u0441\u0438\u043d\u043a\u0430\u0435\u0442 \u0449\u0438\u043b\u0434\u0431\u0440\u0435\u0439\u043a\u0435\u0440\u044b \u0434\u0440\u0443\u0433\u0438\u0445 \u0447\u0438\u0442\u0435\u0440\u043e\u0432", false, () -> true);
        KillAura.autoBlock = new BooleanSetting("Auto Block", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0436\u043c\u0435\u0442 \u043f\u043a\u043c \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435 (\u043d\u0443\u0436\u043d\u043e \u0434\u043b\u044f 1.8 \u0441\u0435\u0440\u0432\u0435\u0440\u043e\u0432)", false, () -> KillAura.clickMode.currentMode.equals("1.8"));
        KillAura.sendDelay = new NumberSetting("Send Block Delay", "\u0420\u0435\u0433\u0443\u043b\u0438\u0440\u043e\u0432\u043a\u0430 \u0434\u0435\u043b\u044d\u044f \u0434\u043b\u044f \u0430\u0432\u0442\u043e\u0431\u043b\u043e\u043a\u0430", 100.0f, 0.0f, 300.0f, 10.0f, () -> KillAura.autoBlock.getCurrentValue());
        KillAura.outDelay = new NumberSetting("Out Block Delay", "\u041a\u0430\u043a \u0434\u043e\u043b\u0433\u043e \u0438\u0433\u0440\u043e\u043a \u0431\u0443\u0434\u0435\u0442 \u043e\u0442\u0436\u0438\u043c\u0430\u0442\u044c \u0430\u0432\u0442\u043e\u0431\u043b\u043e\u043a", 0.0f, 0.0f, 300.0f, 10.0f, () -> KillAura.autoBlock.getCurrentValue());
        KillAura.autoDisable = new BooleanSetting("Auto Disable", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0432\u044b\u043a\u043b\u044e\u0447\u0430\u0435\u0442 \u043a\u0438\u043b\u043b\u0430\u0443\u0440\u0430 \u043f\u0440\u0438 \u0441\u043c\u0435\u0440\u0442\u0438 \u0438 \u0442.\u0434", false, () -> true);
        KillAura.onlyCrit = new BooleanSetting("Only Crits", "\u0411\u044c\u0435\u0442 \u0432 \u043d\u0443\u0436\u043d\u044b\u0439 \u043c\u043e\u043c\u0435\u043d\u0442 \u0434\u043b\u044f \u043a\u0440\u0438\u0442\u0430", false, () -> true);
        KillAura.spaceOnly = new BooleanSetting("Space Only", "Only Crits \u0431\u0443\u0434\u0443\u0442 \u0440\u0430\u0431\u043e\u0442\u0430\u0442\u044c \u0435\u0441\u043b\u0438 \u0437\u0430\u0436\u0430\u0442 \u043f\u0440\u043e\u0431\u0435\u043b", false, () -> KillAura.onlyCrit.getCurrentValue());
        KillAura.critFallDistance = new NumberSetting("Crits Fall Distance", "\u0420\u0435\u0433\u0443\u043b\u0438\u0440\u043e\u0432\u043a\u0430 \u0434\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u0438 \u0434\u043e \u0437\u0435\u043c\u043b\u0438 \u0434\u043b\u044f \u043a\u0440\u0438\u0442\u0430", 0.2f, 0.08f, 0.42f, 0.01f, () -> KillAura.onlyCrit.getCurrentValue());
        this.addSettings(KillAura.rotationMode, KillAura.clickMode, KillAura.oldPvpSystemDelayMin, KillAura.oldPvpSystemDelayMax, KillAura.sort, KillAura.attackCoolDown, KillAura.fov, KillAura.range, KillAura.preAimRange, KillAura.players, KillAura.mobs, KillAura.invis, KillAura.walls, KillAura.rayTrace, KillAura.rayTraceBox, KillAura.sprinting, KillAura.shieldBreaker, KillAura.shieldFixer, KillAura.fixerDelay, KillAura.shieldDesync, KillAura.autoBlock, KillAura.sendDelay, KillAura.outDelay, KillAura.autoDisable, KillAura.onlyCrit, KillAura.spaceOnly, KillAura.critFallDistance);
    }
    
    public static boolean canApsAttack() {
        final int apsMultiplier = 14 / MathematicHelper.middleRandomize((int)KillAura.oldPvpSystemDelayMax.getCurrentValue(), (int)KillAura.oldPvpSystemDelayMin.getCurrentValue());
        if (KillAura.oldTimerPvp.hasReached(50 * apsMultiplier)) {
            KillAura.oldTimerPvp.reset();
            return true;
        }
        return false;
    }
    
    private int getAxe() {
        for (int i = 0; i < 9; ++i) {
            final aip itemStack = KillAura.mc.h.bv.a(i);
            if (itemStack.c() instanceof agy) {
                return i;
            }
        }
        return 1;
    }
    
    @Override
    public void onDisable() {
        KillAura.target = null;
        if (this.isBlocking) {
            KillAura.mc.h.d.a(new lp(lp.a.f, et.a, fa.a));
            this.isBlocking = false;
        }
        KillAura.critCounter = 0;
        KillAura.isBreaked = false;
        KillAura.shieldTicks = 0;
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @EventTarget
    public void onPreAttack(final EventPreMotion preAttack) {
        this.doAuraPre(preAttack);
    }
    
    @EventTarget
    public void onRotations(final EventPreMotion event) {
        final String mode = KillAura.rotationMode.getOptions();
        final int speedMultiplier = 50;
        if (KillAura.target == null) {
            return;
        }
        if (!KillAura.target.F) {
            final float[] rots = RotationHelper.getRotationsCustom(KillAura.target, 5.0f * speedMultiplier, !KillAura.rotationMode.currentMode.equals("Sunrise"));
            if (mode.equalsIgnoreCase("Matrix")) {
                event.setYaw(rots[0]);
                event.setPitch(rots[1]);
            }
            else if (mode.equalsIgnoreCase("Sunrise")) {
                event.setYaw(rots[0]);
                event.setPitch(rots[1]);
            }
            else if (mode.equalsIgnoreCase("ReallyWorld")) {
                event.setYaw(rots[0]);
                event.setPitch(rots[1]);
            }
            else if (mode.equalsIgnoreCase("Client")) {
                KillAura.mc.h.v = rots[0];
                KillAura.mc.h.w = rots[1];
            }
            if (!KillAura.rotationMode.currentMode.equals("AAC") && !KillAura.rotationMode.currentMode.equals("None") && !KillAura.rotationMode.currentMode.equals("Snap")) {
                KillAura.mc.h.aN = rots[0];
                KillAura.mc.h.aP = rots[0];
                KillAura.mc.h.rotationPitchHead = rots[1];
            }
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (KillAura.autoDisable.getCurrentValue() && ((KillAura.mc.m instanceof bkv && !KillAura.mc.h.aC()) || KillAura.mc.h.T <= 1)) {
            EventManager.unregister(this);
            this.onDisable();
            if (this.getState()) {
                this.setState(false);
            }
            NotificationManager.publicity("AutoDisable", "KillAura was toggled off!", 4, NotificationType.INFO);
        }
        if (KillAura.target == null) {
            return;
        }
        if (KillAura.shieldFixer.getCurrentValue() && KillAura.mc.h.cp().c() instanceof ajm) {
            if (KillAura.target.co().c() instanceof agy) {
                if (KillAura.mc.t.ad.e()) {
                    KillAura.mc.t.ad.i = false;
                }
            }
            else {
                KillAura.mc.t.ad.i = Mouse.isButtonDown(1);
            }
        }
        if (KillAura.shieldDesync.getCurrentValue() && KillAura.mc.h.isBlocking() && KillAura.target != null && KillAura.mc.h.T % 8 == 0) {
            KillAura.mc.h.d.a(new lp(lp.a.f, new et(900, 900, 900), fa.a));
            KillAura.mc.c.a(KillAura.mc.h, KillAura.mc.f, ub.b);
        }
    }
    
    @EventTarget
    public void onAttackSilent(final EventAttackSilent eventAttackSilent) {
        KillAura.isAttacking = true;
        if (KillAura.mc.h.isBlocking() && this.shieldFixerTimer.hasReached(KillAura.fixerDelay.getCurrentValue()) && KillAura.mc.h.b(ub.b).c() instanceof ajm && KillAura.shieldFixer.getCurrentValue()) {
            KillAura.mc.h.d.a(new lp(lp.a.f, new et(900, 900, 900), fa.b));
            KillAura.mc.c.a(KillAura.mc.h, KillAura.mc.f, ub.b);
            this.shieldFixerTimer.reset();
        }
    }
    
    private void attackEntitySuccess(final vp target) {
        if (target == null || KillAura.mc.h.cd() < 0.0f) {
            return;
        }
        if (KillAura.mc.h.g(target) > KillAura.range.getCurrentValue()) {
            return;
        }
        if (!target.F) {
            final String options = KillAura.clickMode.getOptions();
            switch (options) {
                case "1.9": {
                    final float attackDelay = KillAura.attackCoolDown.getCurrentValue() * TpsHelper.getTickRate() / 20.0f;
                    if (KillAura.mc.h.n(0.0f) < attackDelay) {
                        break;
                    }
                    KillAura.mc.c.a(KillAura.mc.h, target);
                    KillAura.mc.h.a(ub.a);
                    BreakShield(target);
                    break;
                }
                case "1.8": {
                    if (!canApsAttack()) {
                        break;
                    }
                    if (this.isBlocking && KillAura.autoBlock.getCurrentValue() && KillAura.mc.h.co().c() instanceof ajy && this.blockTimer1.hasReached(KillAura.sendDelay.getCurrentValue())) {
                        KillAura.mc.h.d.a(new lp(lp.a.f, new et(-1, -1, -1), fa.a));
                        this.isBlocking = false;
                        this.blockTimer1.reset();
                    }
                    KillAura.mc.c.a(KillAura.mc.h, target);
                    KillAura.mc.h.a(ub.a);
                    break;
                }
            }
        }
    }
    
    private void doAuraPre(final EventPreMotion event) {
        if (KillAura.mc.h.cd() > 0.0f) {
            final String mode = KillAura.rotationMode.getOptions();
            this.setSuffix(mode);
            KillAura.target = KillAuraHelper.getSortEntities();
            if (KillAura.target == null) {
                return;
            }
        }
        else if (KillAura.autoDisable.getCurrentValue()) {
            this.toggle();
            NotificationManager.publicity("KillAura", "KillAura was disabled because of Death!", 3, NotificationType.INFO);
        }
        final float[] rots = RotationHelper.getRotationsCustom(KillAura.target, Float.MAX_VALUE, !KillAura.rotationMode.currentMode.equals("Sunrise"));
        final float yaw = (KillAura.rotationMode.currentMode.equals("Matrix") || KillAura.rotationMode.currentMode.equals("Sunrise") || KillAura.rotationMode.currentMode.equals("ReallyWorld") || KillAura.rotationMode.currentMode.equals("AAC")) ? RotationHelper.Rotation.packetYaw : KillAura.mc.h.v;
        final float pitch = (KillAura.rotationMode.currentMode.equals("Matrix") || KillAura.rotationMode.currentMode.equals("Sunrise") || KillAura.rotationMode.currentMode.equals("ReallyWorld") || KillAura.rotationMode.currentMode.equals("AAC")) ? RotationHelper.Rotation.packetPitch : KillAura.mc.h.w;
        KillAura.mc.h.bD = 0;
        final float f2 = KillAura.mc.h.n(0.5f);
        final boolean bl;
        final boolean flag = bl = (f2 > 0.9f);
        if (!flag && KillAura.onlyCrit.getCurrentValue()) {
            return;
        }
        if (KillAura.mc.t.X.e() || !KillAura.spaceOnly.getCurrentValue() || Fluger.instance.featureManager.getFeatureByClass(GlideFly.class).getState()) {
            if (MovementHelper.airBlockAboveHead()) {
                if (KillAura.mc.h.L < KillAura.critFallDistance.getCurrentValue() && KillAura.onlyCrit.getCurrentValue() && !KillAura.mc.h.aS() && !KillAura.mc.h.m_() && !KillAura.mc.h.isInLiquid() && !KillAura.mc.h.E) {
                    KillAura.mc.h.d.a(new lq(KillAura.mc.h, lq.a.e));
                    return;
                }
            }
            else if (KillAura.mc.h.L > 0.0f && !KillAura.mc.h.z && KillAura.onlyCrit.getCurrentValue() && !KillAura.mc.h.aS() && !KillAura.mc.h.m_() && !KillAura.mc.h.isInLiquid() && !KillAura.mc.h.E) {
                KillAura.mc.h.d.a(new lq(KillAura.mc.h, lq.a.e));
                return;
            }
        }
        Label_0702: {
            if (KillAura.rotationMode.currentMode.equalsIgnoreCase("Sunrise") || KillAura.rotationMode.currentMode.equalsIgnoreCase("ReallyWorld")) {
                if (RotationHelper.isLookingAtEntity(false, yaw, pitch, 0.12f, 0.12f, 0.12f, KillAura.target, KillAura.range.getCurrentValue() + KillAura.preAimRange.getCurrentValue())) {
                    break Label_0702;
                }
            }
            else if (KillAura.rotationMode.currentMode.equals("Snap") || RotationHelper.isLookingAtEntity(false, yaw, pitch, KillAura.rayTraceBox.getCurrentValue(), KillAura.rayTraceBox.getCurrentValue(), KillAura.rayTraceBox.getCurrentValue(), KillAura.target, KillAura.range.getCurrentValue() + KillAura.preAimRange.getCurrentValue()) || !KillAura.rayTrace.getCurrentValue()) {
                break Label_0702;
            }
            return;
        }
        KillAura.canDo = true;
        if (KillAura.rotationMode.currentMode.equals("Snap")) {
            final float[] rots2 = RotationHelper.getRotationsCustom(KillAura.target, 250.0f, true);
            if (KillAura.mc.h.n(0.0f) >= KillAura.attackCoolDown.getCurrentValue()) {
                KillAura.mc.h.v = rots2[0];
                KillAura.mc.h.w = rots2[1];
            }
        }
        this.attackEntitySuccess(EntityHelper.rayCast(KillAura.target, KillAura.range.getCurrentValue()));
    }
    
    @EventTarget
    public void onPostMotion(final EventPostMotion event) {
        if (KillAura.target == null) {
            return;
        }
        if (KillAura.mc.h.co().c() instanceof ajy && !this.isBlocking && KillAura.autoBlock.getCurrentValue() && this.blockTimer.hasReached(KillAura.outDelay.getCurrentValue())) {
            KillAura.mc.h.d.a(new mb(ub.a));
            this.blockTimer.reset();
            this.isBlocking = true;
        }
    }
    
    @EventTarget
    public void breakNotifications(final EventReceivePacket sound) {
        if (KillAura.shieldBreaker.getCurrentValue() && sound.getPacket() instanceof iz) {
            final iz sPacketEntityStatus = (iz)sound.getPacket();
            if (sPacketEntityStatus.a() == 30 && sPacketEntityStatus.a(KillAura.mc.f) == KillAura.target) {
                if (this.notiTicks < 2) {
                    NotificationManager.publicity(a.k + "Shield-Breaker", "Successfully destroyed " + KillAura.target.h_() + " shield", 2, NotificationType.SUCCESS);
                }
                else {
                    this.notiTicks = 0;
                }
            }
        }
    }
    
    public static void BreakShield(final vp tg) {
        if (InventoryHelper.doesHotbarHaveAxe() && KillAura.shieldBreaker.getCurrentValue()) {
            final int item = InventoryHelper.getAxe();
            if (InventoryHelper.getAxe() >= 0 && tg instanceof aed && tg.cG() && tg.cJ().c() instanceof ajm) {
                KillAura.mc.h.d.a(new lv(item));
                KillAura.mc.c.a(KillAura.mc.h, KillAura.target);
                KillAura.mc.h.a(ub.a);
                KillAura.mc.h.d.a(new lv(KillAura.mc.h.bv.d));
            }
        }
    }
    
    static {
        KillAura.oldTimerPvp = new TimerHelper();
        KillAura.timer = new TimerHelper();
    }
}
