package me.earth.phobos.manager;

import me.earth.phobos.features.Feature;
import me.earth.phobos.features.modules.client.Managers;
import me.earth.phobos.features.modules.combat.AutoCrystal;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.DamageUtil;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SafetyManager
        extends Feature
        implements Runnable {
    private final Timer syncTimer = new Timer();
    private final AtomicBoolean SAFE = new AtomicBoolean(false);
    private ScheduledExecutorService service;

    @Override
    public void run() {
        if (AutoCrystal.getInstance().isOff() || AutoCrystal.getInstance().threadMode.getValue() == AutoCrystal.ThreadMode.NONE) {
            this.doSafetyCheck();
        }
    }

    public void doSafetyCheck() {
        if (!SafetyManager.fullNullCheck()) {
            EntityPlayer closest;
            boolean safe = true;
            EntityPlayer entityPlayer = closest = Managers.getInstance().safety.getValue() != false ? EntityUtil.getClosestEnemy(18.0) : null;
            if (Managers.getInstance().safety.getValue().booleanValue() && closest == null) {
                this.SAFE.set(true);
                return;
            }
            ArrayList<Entity> crystals = new ArrayList<>(SafetyManager.mc.world.loadedEntityList);
            for (Entity crystal : crystals) {
                if (!(crystal instanceof EntityEnderCrystal) || !((double) DamageUtil.calculateDamage(crystal, SafetyManager.mc.player) > 4.0) || closest != null && !(closest.getDistanceSq(crystal) < 40.0))
                    continue;
                safe = false;
                break;
            }
            if (safe) {
                for (BlockPos pos : BlockUtil.possiblePlacePositions(4.0f, false, Managers.getInstance().oneDot15.getValue())) {
                    if (!((double) DamageUtil.calculateDamage(pos, SafetyManager.mc.player) > 4.0) || closest != null && !(closest.getDistanceSq(pos) < 40.0))
                        continue;
                    safe = false;
                    break;
                }
            }
            this.SAFE.set(safe);
        }
    }

    public void onUpdate() {
        this.run();
    }

    public String getSafetyString() {
        if (this.SAFE.get()) {
            return "\u00a7aSecure";
        }
        return "\u00a7cUnsafe";
    }

    public boolean isSafe() {
        return this.SAFE.get();
    }

    public ScheduledExecutorService getService() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, 0L, Managers.getInstance().safetyCheck.getValue().intValue(), TimeUnit.MILLISECONDS);
        return service;
    }
}

