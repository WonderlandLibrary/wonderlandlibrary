// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.player;

import java.util.function.ToDoubleFunction;
import java.util.function.Function;
import java.util.Comparator;
import ru.fluger.client.helpers.math.RotationHelper;
import java.util.ArrayList;
import java.util.Iterator;
import ru.fluger.client.feature.impl.combat.KillAura;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.combat.AntiBot;
import ru.fluger.client.friend.Friend;
import ru.fluger.client.Fluger;
import ru.fluger.client.helpers.Helper;

public class KillAuraHelper implements Helper
{
    public static boolean canAttack(final vp player) {
        for (final Friend friend : Fluger.instance.friendManager.getFriends()) {
            if (!player.h_().equals(friend.getName())) {
                continue;
            }
            return false;
        }
        if (Fluger.instance.featureManager.getFeatureByClass(AntiBot.class).getState() && !AntiBot.isRealPlayer.contains(player) && AntiBot.antiBotMode.currentMode.equals("Need Hit")) {
            return false;
        }
        if (Fluger.instance.featureManager.getFeatureByClass(AntiBot.class).getState() && AntiBot.isBotPlayer.contains(player)) {
            return false;
        }
        if (player instanceof adl && !KillAura.mobs.getCurrentValue()) {
            return false;
        }
        if (player instanceof add && !KillAura.mobs.getCurrentValue()) {
            return false;
        }
        if (player instanceof abd && !KillAura.mobs.getCurrentValue()) {
            return false;
        }
        if (player instanceof acu && !KillAura.mobs.getCurrentValue()) {
            return false;
        }
        if (player instanceof aal && !KillAura.mobs.getCurrentValue()) {
            return false;
        }
        if (player instanceof aed || player instanceof zv || player instanceof ade || player instanceof ady) {
            if (player instanceof aed && !KillAura.players.getCurrentValue()) {
                return false;
            }
            if (player instanceof zv && !KillAura.mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof ade && !KillAura.mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof ady && !KillAura.mobs.getCurrentValue()) {
                return false;
            }
        }
        if (player instanceof abz) {
            return false;
        }
        if (player.aX() && !KillAura.invis.getCurrentValue()) {
            return false;
        }
        if (!range(player, KillAura.range.getCurrentValue() + KillAura.preAimRange.getCurrentValue())) {
            return false;
        }
        if (!canSeeEntityAtFov(player, KillAura.fov.getCurrentValue() * 2.0f)) {
            return false;
        }
        if (!player.D(KillAuraHelper.mc.h)) {
            return KillAura.walls.getCurrentValue();
        }
        return player != KillAuraHelper.mc.h && KillAuraHelper.mc.h != null && KillAuraHelper.mc.f != null;
    }
    
    public static boolean canSeeEntityAtFov(final vg entityLiving, final float scope) {
        final double diffZ = entityLiving.r - KillAuraHelper.mc.h.r;
        final double diffX = entityLiving.p - KillAuraHelper.mc.h.p;
        final float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        final double difference = angleDifference(yaw, KillAuraHelper.mc.h.v);
        return difference <= scope;
    }
    
    public static double angleDifference(final double a, final double b) {
        float yaw360 = (float)(Math.abs(a - b) % 360.0);
        if (yaw360 > 180.0f) {
            yaw360 = 360.0f - yaw360;
        }
        return yaw360;
    }
    
    private static boolean range(final vp entity, final double range) {
        return KillAuraHelper.mc.h.g(entity) <= range;
    }
    
    public static vp getSortEntities() {
        final ArrayList<vp> entity = new ArrayList<vp>();
        for (final vg e : KillAuraHelper.mc.f.e) {
            if (e != null) {
                if (!(e instanceof vp)) {
                    continue;
                }
                final vp player = (vp)e;
                if (player.cd() > 0.0f && !player.F) {
                    if (KillAuraHelper.mc.h.g(player) > KillAura.range.getCurrentValue() + KillAura.preAimRange.getCurrentValue()) {
                        continue;
                    }
                    if (!canAttack(player)) {
                        continue;
                    }
                    entity.add(player);
                }
                else {
                    entity.remove(player);
                }
            }
        }
        final String sortMode = KillAura.sort.getOptions();
        if (sortMode.equalsIgnoreCase("Angle")) {
            entity.sort((o1, o2) -> (int)(Math.abs(RotationHelper.getAngleEntity(o1) - KillAuraHelper.mc.h.v) - Math.abs(RotationHelper.getAngleEntity(o2) - KillAuraHelper.mc.h.v)));
        }
        else if (sortMode.equalsIgnoreCase("Higher Armor")) {
            entity.sort(Comparator.comparing((Function<? super Object, ? extends Comparable>)vp::cg).reversed());
        }
        else if (sortMode.equalsIgnoreCase("Lowest Armor")) {
            entity.sort(Comparator.comparing((Function<? super vp, ? extends Comparable>)vp::cg));
        }
        else if (sortMode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int)(o1.cd() - o2.cd()));
        }
        else if (sortMode.equalsIgnoreCase("Distance")) {
            entity.sort(Comparator.comparingDouble((ToDoubleFunction<? super vp>)KillAuraHelper.mc.h::g));
        }
        else if (sortMode.equalsIgnoreCase("Blocking Status")) {
            entity.sort(Comparator.comparing((Function<? super Object, ? extends Comparable>)vp::isUsingItem).reversed());
        }
        if (entity.isEmpty()) {
            return null;
        }
        return entity.get(0);
    }
}
