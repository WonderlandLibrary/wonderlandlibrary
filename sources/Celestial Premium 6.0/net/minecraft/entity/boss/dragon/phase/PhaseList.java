/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss.dragon.phase;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseChargingPlayer;
import net.minecraft.entity.boss.dragon.phase.PhaseDying;
import net.minecraft.entity.boss.dragon.phase.PhaseHoldingPattern;
import net.minecraft.entity.boss.dragon.phase.PhaseHover;
import net.minecraft.entity.boss.dragon.phase.PhaseLanding;
import net.minecraft.entity.boss.dragon.phase.PhaseLandingApproach;
import net.minecraft.entity.boss.dragon.phase.PhaseSittingAttacking;
import net.minecraft.entity.boss.dragon.phase.PhaseSittingFlaming;
import net.minecraft.entity.boss.dragon.phase.PhaseSittingScanning;
import net.minecraft.entity.boss.dragon.phase.PhaseStrafePlayer;
import net.minecraft.entity.boss.dragon.phase.PhaseTakeoff;

public class PhaseList<T extends IPhase> {
    private static PhaseList<?>[] phases = new PhaseList[0];
    public static final PhaseList<PhaseHoldingPattern> HOLDING_PATTERN = PhaseList.create(PhaseHoldingPattern.class, "HoldingPattern");
    public static final PhaseList<PhaseStrafePlayer> STRAFE_PLAYER = PhaseList.create(PhaseStrafePlayer.class, "StrafePlayer");
    public static final PhaseList<PhaseLandingApproach> LANDING_APPROACH = PhaseList.create(PhaseLandingApproach.class, "LandingApproach");
    public static final PhaseList<PhaseLanding> LANDING = PhaseList.create(PhaseLanding.class, "Landing");
    public static final PhaseList<PhaseTakeoff> TAKEOFF = PhaseList.create(PhaseTakeoff.class, "Takeoff");
    public static final PhaseList<PhaseSittingFlaming> SITTING_FLAMING = PhaseList.create(PhaseSittingFlaming.class, "SittingFlaming");
    public static final PhaseList<PhaseSittingScanning> SITTING_SCANNING = PhaseList.create(PhaseSittingScanning.class, "SittingScanning");
    public static final PhaseList<PhaseSittingAttacking> SITTING_ATTACKING = PhaseList.create(PhaseSittingAttacking.class, "SittingAttacking");
    public static final PhaseList<PhaseChargingPlayer> CHARGING_PLAYER = PhaseList.create(PhaseChargingPlayer.class, "ChargingPlayer");
    public static final PhaseList<PhaseDying> DYING = PhaseList.create(PhaseDying.class, "Dying");
    public static final PhaseList<PhaseHover> HOVER = PhaseList.create(PhaseHover.class, "Hover");
    private final Class<? extends IPhase> clazz;
    private final int id;
    private final String name;

    private PhaseList(int idIn, Class<? extends IPhase> clazzIn, String nameIn) {
        this.id = idIn;
        this.clazz = clazzIn;
        this.name = nameIn;
    }

    public IPhase createPhase(EntityDragon dragon) {
        try {
            Constructor<IPhase> constructor = this.getConstructor();
            return constructor.newInstance(dragon);
        }
        catch (Exception exception) {
            throw new Error(exception);
        }
    }

    protected Constructor<? extends IPhase> getConstructor() throws NoSuchMethodException {
        return this.clazz.getConstructor(EntityDragon.class);
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return this.name + " (#" + this.id + ")";
    }

    public static PhaseList<?> getById(int p_188738_0_) {
        return p_188738_0_ >= 0 && p_188738_0_ < phases.length ? phases[p_188738_0_] : HOLDING_PATTERN;
    }

    public static int getTotalPhases() {
        return phases.length;
    }

    private static <T extends IPhase> PhaseList<T> create(Class<T> phaseIn, String nameIn) {
        PhaseList<T> phaselist = new PhaseList<T>(phases.length, phaseIn, nameIn);
        phases = Arrays.copyOf(phases, phases.length + 1);
        PhaseList.phases[phaselist.getId()] = phaselist;
        return phaselist;
    }
}

