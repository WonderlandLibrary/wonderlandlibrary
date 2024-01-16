// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.combat;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.ui.notification.NotificationManager;
import ru.fluger.client.ui.notification.NotificationType;
import ru.fluger.client.event.EventManager;
import ru.fluger.client.Fluger;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.helpers.math.RotationHelper;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class TargetStrafe extends Feature
{
    public static NumberSetting range;
    public static NumberSetting spd;
    public static NumberSetting boostValue;
    public static NumberSetting boostTicks;
    public static BooleanSetting reversed;
    public static NumberSetting reversedDistance;
    public static BooleanSetting boost;
    public static BooleanSetting autoJump;
    public static BooleanSetting autoShift;
    public static BooleanSetting autoThirdPerson;
    public static BooleanSetting autoDisable;
    public static BooleanSetting alwaysSprint;
    public static BooleanSetting switchOnLook;
    public static NumberSetting lookRadius;
    private double wrap;
    private boolean switchDir;
    public boolean canShift;
    
    public TargetStrafe() {
        super("TargetStrafe", "\u0421\u0442\u0440\u0435\u0444\u0438\u0442 \u0432\u043e\u043a\u0440\u0443\u0433 \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0435\u0439", Type.Combat);
        this.wrap = 0.0;
        this.switchDir = true;
        TargetStrafe.spd = new NumberSetting("Strafe Speed", 0.23f, 0.1f, 2.0f, 0.01f, () -> true);
        TargetStrafe.range = new NumberSetting("Strafe Distance", 2.4f, 0.1f, 6.0f, 0.1f, () -> true);
        TargetStrafe.boost = new BooleanSetting("DamageBoost", false, () -> true);
        TargetStrafe.boostValue = new NumberSetting("Boost Value", 0.5f, 0.1f, 4.0f, 0.01f, TargetStrafe.boost::getCurrentValue);
        TargetStrafe.boostTicks = new NumberSetting("Boost Ticks", 8.0f, 0.0f, 9.0f, 1.0f, TargetStrafe.boost::getCurrentValue);
        TargetStrafe.reversed = new BooleanSetting("KeepDistance", false, () -> true);
        TargetStrafe.reversedDistance = new NumberSetting("KeepDistance Distance", 3.0f, 1.0f, 6.0f, 0.1f, () -> TargetStrafe.reversed.getCurrentValue());
        TargetStrafe.autoJump = new BooleanSetting("AutoJump", true, () -> true);
        TargetStrafe.autoShift = new BooleanSetting("AutoShift", false, () -> true);
        TargetStrafe.autoThirdPerson = new BooleanSetting("Auto Third Person", false, () -> true);
        TargetStrafe.autoDisable = new BooleanSetting("Auto Disable", false, () -> true);
        TargetStrafe.alwaysSprint = new BooleanSetting("Always Sprint", false, () -> true);
        TargetStrafe.switchOnLook = new BooleanSetting("Switch On Look", false, () -> true);
        TargetStrafe.lookRadius = new NumberSetting("Look Radius", 65.0f, 5.0f, 180.0f, 1.0f, () -> TargetStrafe.switchOnLook.getCurrentValue());
        this.addSettings(TargetStrafe.boost, TargetStrafe.boostTicks, TargetStrafe.boostValue, TargetStrafe.reversed, TargetStrafe.reversedDistance, TargetStrafe.autoJump, TargetStrafe.autoShift, TargetStrafe.autoThirdPerson, TargetStrafe.autoDisable, TargetStrafe.alwaysSprint, TargetStrafe.spd, TargetStrafe.range, TargetStrafe.switchOnLook, TargetStrafe.lookRadius);
    }
    
    public boolean needToSwitch(final double x, final double z) {
        if (TargetStrafe.mc.h.A || TargetStrafe.mc.t.U.g() || TargetStrafe.mc.t.W.g()) {
            return true;
        }
        if (RotationHelper.isAimAtMe(KillAura.target, TargetStrafe.lookRadius.getCurrentValue()) && TargetStrafe.switchOnLook.getCurrentValue()) {
            return !this.switchDir;
        }
        for (int i = (int)(TargetStrafe.mc.h.q + 4.0); i >= 0; --i) {
            final et playerPos = new et(x, i, z);
            if (TargetStrafe.mc.f.o(playerPos).u().equals(aox.l) || TargetStrafe.mc.f.o(playerPos).u().equals(aox.ab)) {
                return true;
            }
            if (!TargetStrafe.mc.f.d(playerPos)) {
                return false;
            }
        }
        return true;
    }
    
    private float wrapDS(final float x, final float z) {
        final double diffX = x - TargetStrafe.mc.h.p;
        final double diffZ = z - TargetStrafe.mc.h.r;
        return (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793 - 90.0);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix("" + TargetStrafe.range.getCurrentValue());
        final vp entity = KillAura.target;
        if (TargetStrafe.mc.h == null || TargetStrafe.mc.f == null) {
            return;
        }
        if (TargetStrafe.alwaysSprint.getCurrentValue()) {
            TargetStrafe.mc.h.f(true);
        }
        if (TargetStrafe.autoThirdPerson.getCurrentValue()) {
            if (entity.cd() > 0.0f && TargetStrafe.mc.h.g(entity) <= KillAura.range.getCurrentValue() && TargetStrafe.mc.h.cd() > 0.0f) {
                if (this.getState() && Fluger.instance.featureManager.getFeatureByClass(KillAura.class).getState()) {
                    TargetStrafe.mc.t.aw = 1;
                }
            }
            else {
                TargetStrafe.mc.t.aw = 0;
            }
        }
        if (TargetStrafe.autoDisable.getCurrentValue() && ((TargetStrafe.mc.m instanceof bkv && !TargetStrafe.mc.h.aC()) || TargetStrafe.mc.h.T <= 1)) {
            EventManager.unregister(this);
            this.onDisable();
            if (this.getState()) {
                this.setState(false);
            }
            NotificationManager.publicity("AutoDisable", "TargetStrafe was toggled off!", 4, NotificationType.INFO);
        }
        if (entity == null) {
            return;
        }
        if (TargetStrafe.mc.h.g(entity) <= KillAura.range.getCurrentValue() && entity.cd() > 0.0f) {
            this.canShift = true;
            final float speed = (TargetStrafe.mc.h.ay > TargetStrafe.boostTicks.getCurrentValue() && TargetStrafe.boost.getCurrentValue() && !TargetStrafe.mc.h.z) ? TargetStrafe.boostValue.getCurrentValue() : TargetStrafe.spd.getCurrentValue();
            final float searchValue = (Fluger.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState() && TargetStrafe.reversed.getCurrentValue() && TargetStrafe.mc.h.g(KillAura.target) < TargetStrafe.reversedDistance.getCurrentValue()) ? -90.0f : 0.0f;
            final float value = rk.a(TargetStrafe.mc.h.g(entity), 0.01f, KillAura.range.getCurrentValue());
            this.wrap = (float)Math.atan2(TargetStrafe.mc.h.r - entity.r, TargetStrafe.mc.h.p - entity.p);
            this.wrap += (this.switchDir ? (speed / value) : ((double)(-(speed / value))));
            float x = (float)(entity.p + TargetStrafe.range.getCurrentValue() * Math.cos(this.wrap));
            float z = (float)(entity.r + TargetStrafe.range.getCurrentValue() * Math.sin(this.wrap));
            if (this.needToSwitch(x, z)) {
                this.switchDir = !this.switchDir;
                this.wrap += 2.0f * (this.switchDir ? (speed / value) : (-(speed / value)));
                x = (float)(entity.p + TargetStrafe.range.getCurrentValue() * Math.cos(this.wrap));
                z = (float)(entity.r + TargetStrafe.range.getCurrentValue() * Math.sin(this.wrap));
            }
            if (TargetStrafe.mc.h.ay > TargetStrafe.boostTicks.getCurrentValue() && TargetStrafe.boost.getCurrentValue() && !TargetStrafe.mc.h.z) {
                final bud h = TargetStrafe.mc.h;
                h.aR *= 60.0f;
            }
            final float reversedValue = (!TargetStrafe.mc.t.U.e() && !TargetStrafe.mc.t.W.e() && !TargetStrafe.mc.h.A) ? searchValue : 0.0f;
            TargetStrafe.mc.h.s = speed * -Math.sin((float)Math.toRadians(this.wrapDS(x + reversedValue, z + reversedValue)));
            TargetStrafe.mc.h.u = speed * Math.cos((float)Math.toRadians(this.wrapDS(x + reversedValue, z + reversedValue)));
            if (TargetStrafe.autoJump.getCurrentValue() && Fluger.instance.featureManager.getFeatureByClass(KillAura.class).getState() && Fluger.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState() && TargetStrafe.mc.h.z) {
                TargetStrafe.mc.h.cu();
            }
            if (TargetStrafe.autoShift.getCurrentValue() && this.canShift) {
                TargetStrafe.mc.t.Y.setPressed(TargetStrafe.mc.h.L > KillAura.critFallDistance.getCurrentValue() + 0.1);
            }
        }
    }
}
