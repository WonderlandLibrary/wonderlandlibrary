package tech.atani.client.feature.anticheat.check;

import de.florianmichael.rclasses.storage.Storage;
import tech.atani.client.feature.anticheat.check.impl.*;
import tech.atani.client.feature.anticheat.data.PlayerData;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class CheckStorage extends Storage<Check> {

    private static CheckStorage instance;

    private final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();
    Class<?>[] checkClasses = new Class[] {
            InvalidCheck.class,
            StepCheck.class,
            GroundCheck.class,
            MoveCheck.class,
    };

    @Override
    public void init() {
        instance = this;

        for(Class<?> clazz : checkClasses) {
            try {
                CONSTRUCTORS.add(clazz.getConstructor(PlayerData.class));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Check> loadChecks(PlayerData data) {
        List<Check> checkList = new ArrayList<>();

        for(Constructor<?> constructor : CONSTRUCTORS) {
            try {
                Check check = (Check) constructor.newInstance(data);

                checkList.add(check);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return checkList;
    }

    public List<Check> getChecks() {
        return instance.getList();
    }

    public static CheckStorage getInstance() {
        return instance;
    }

    public static void setInstance(CheckStorage instance) {
        CheckStorage.instance = instance;
    }
}
