package dev.rise.anticheat.check.manager;

import dev.rise.anticheat.check.Check;
import dev.rise.anticheat.check.impl.combat.autoclicker.AutoClickerA;
import dev.rise.anticheat.check.impl.combat.keepsprint.KeepSprintA;
import dev.rise.anticheat.check.impl.movement.speed.SpeedA;
import dev.rise.anticheat.check.impl.other.eagle.EagleA;
import dev.rise.anticheat.check.impl.other.groundspoof.GroundSpoofA;
import dev.rise.anticheat.check.impl.other.groundspoof.GroundSpoofB;
import dev.rise.anticheat.check.impl.other.invalid.InvalidA;
import dev.rise.anticheat.data.PlayerData;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public final class CheckManager {

    public static final Class<?>[] CHECKS = new Class[]{
            /*
             * COMBAT
             */

            // AutoClicker
            AutoClickerA.class,

            // KeepSprint
            KeepSprintA.class,

            /*
             * MOVEMENT
             */

            // Speed
            SpeedA.class,

            /*
             * OTHER
             */

            // Eagle
            EagleA.class,

            // GroundSpoof
            GroundSpoofA.class,
            GroundSpoofB.class,

            // Invalid
            InvalidA.class
    };

    private static final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();

    public static List<Check> loadChecks(final PlayerData data) {
        final List<Check> checkList = new ArrayList<>();

        for (final Constructor<?> constructor : CONSTRUCTORS) {
            try {
                checkList.add((Check) constructor.newInstance(data));
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        }

        return checkList;
    }

    public static void setup() {
        for (final Class<?> clazz : CHECKS) {
            try {
                CONSTRUCTORS.add(clazz.getConstructor(PlayerData.class));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
