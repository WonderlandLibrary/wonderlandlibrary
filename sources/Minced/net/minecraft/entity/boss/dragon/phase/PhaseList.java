// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import java.util.Arrays;
import java.lang.reflect.Constructor;
import net.minecraft.entity.boss.EntityDragon;

public class PhaseList<T extends IPhase>
{
    private static PhaseList<?>[] phases;
    public static final PhaseList<PhaseHoldingPattern> HOLDING_PATTERN;
    public static final PhaseList<PhaseStrafePlayer> STRAFE_PLAYER;
    public static final PhaseList<PhaseLandingApproach> LANDING_APPROACH;
    public static final PhaseList<PhaseLanding> LANDING;
    public static final PhaseList<PhaseTakeoff> TAKEOFF;
    public static final PhaseList<PhaseSittingFlaming> SITTING_FLAMING;
    public static final PhaseList<PhaseSittingScanning> SITTING_SCANNING;
    public static final PhaseList<PhaseSittingAttacking> SITTING_ATTACKING;
    public static final PhaseList<PhaseChargingPlayer> CHARGING_PLAYER;
    public static final PhaseList<PhaseDying> DYING;
    public static final PhaseList<PhaseHover> HOVER;
    private final Class<? extends IPhase> clazz;
    private final int id;
    private final String name;
    
    private PhaseList(final int idIn, final Class<? extends IPhase> clazzIn, final String nameIn) {
        this.id = idIn;
        this.clazz = clazzIn;
        this.name = nameIn;
    }
    
    public IPhase createPhase(final EntityDragon dragon) {
        try {
            final Constructor<? extends IPhase> constructor = this.getConstructor();
            return (IPhase)constructor.newInstance(dragon);
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
    
    @Override
    public String toString() {
        return this.name + " (#" + this.id + ")";
    }
    
    public static PhaseList<?> getById(final int idIn) {
        return (idIn >= 0 && idIn < PhaseList.phases.length) ? PhaseList.phases[idIn] : PhaseList.HOLDING_PATTERN;
    }
    
    public static int getTotalPhases() {
        return PhaseList.phases.length;
    }
    
    private static <T extends IPhase> PhaseList<T> create(final Class<T> phaseIn, final String nameIn) {
        final PhaseList<T> phaselist = new PhaseList<T>(PhaseList.phases.length, phaseIn, nameIn);
        PhaseList.phases = Arrays.copyOf(PhaseList.phases, PhaseList.phases.length + 1);
        return (PhaseList<T>)(PhaseList.phases[phaselist.getId()] = phaselist);
    }
    
    static {
        PhaseList.phases = (PhaseList<?>[])new PhaseList[0];
        HOLDING_PATTERN = create(PhaseHoldingPattern.class, "HoldingPattern");
        STRAFE_PLAYER = create(PhaseStrafePlayer.class, "StrafePlayer");
        LANDING_APPROACH = create(PhaseLandingApproach.class, "LandingApproach");
        LANDING = create(PhaseLanding.class, "Landing");
        TAKEOFF = create(PhaseTakeoff.class, "Takeoff");
        SITTING_FLAMING = create(PhaseSittingFlaming.class, "SittingFlaming");
        SITTING_SCANNING = create(PhaseSittingScanning.class, "SittingScanning");
        SITTING_ATTACKING = create(PhaseSittingAttacking.class, "SittingAttacking");
        CHARGING_PLAYER = create(PhaseChargingPlayer.class, "ChargingPlayer");
        DYING = create(PhaseDying.class, "Dying");
        HOVER = create(PhaseHover.class, "Hover");
    }
}
