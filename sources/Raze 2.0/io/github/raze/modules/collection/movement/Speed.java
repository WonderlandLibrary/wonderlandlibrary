package io.github.raze.modules.collection.movement;

import io.github.raze.Raze;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.settings.collection.NumberSetting;

public class Speed extends AbstractModule {

    private final ArraySetting mode;
    public final ArraySetting strafeMode, ncpMode,oldNcpMode, strafeState, matrixMode, spartanMode, incognitoMode, verusMode, vulcanMode;

    public final NumberSetting motionY,minusMotionY,airSpeed,downMultiplier,groundSpeed,timerSpeed;
    public final BooleanSetting strafe,sprint,yPort,damageBoost,ncpTimer,vulcanTimer;

    private ModeSpeed modeSpeed;

    public Speed() {
        super("Speed", "Makes you go zoom.", ModuleCategory.MOVEMENT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Legit", "Legit", "Custom", "Slow Hop", "Strafe", "Verus", "Vulcan", "Matrix", "BlocksMC", "Hypixel", "Spoof", "NCP", "Old NCP", "MushMC Slow", "Spartan", "Incognito", "Karhu", "MineMenClub"),

                strafeMode = new ArraySetting(this, "Strafe Mode", "Normal", "Normal", "Bypass")
                        .setHidden(() -> !mode.compare("Strafe")),

                matrixMode = new ArraySetting(this, "Matrix Mode", "Normal", "Normal", "SkyCave")
                        .setHidden(() -> !mode.compare("Matrix")),

                spartanMode = new ArraySetting(this, "Spartan Mode", "Normal", "Normal", "LowHop", "Timer")
                        .setHidden(() -> !mode.compare("Spartan")),

                incognitoMode = new ArraySetting(this, "Incognito Mode", "Normal", "Normal", "Exploit")
                        .setHidden(() -> !mode.compare("Incognito")),

                strafeState = new ArraySetting(this, "Strafe State", "Post", "Post", "Pre")
                        .setHidden(() -> !mode.compare("Strafe")),

                ncpMode = new ArraySetting(this, "NCP Mode", "Normal", "Normal", "Low", "Slow", "Fast", "Stable")
                        .setHidden(() -> !mode.compare("NCP")),

                oldNcpMode = new ArraySetting(this, "Old NCP Mode", "Y-Port", "Y-Port")
                        .setHidden(() -> !mode.compare("Old NCP")),

                verusMode = new ArraySetting(this, "Verus Mode", "Hop 1", "Hop 1", "Hop 2", "Funny")
                        .setHidden(() -> !mode.compare("Verus")),

                vulcanMode = new ArraySetting(this, "Vulcan Mode", "BHop", "BHop", "BHop 2", "Low", "Funny")
                        .setHidden(() -> !mode.compare("Vulcan")),

                ncpTimer = new BooleanSetting(this, "NCP Timer", false)
                        .setHidden(() -> !mode.compare("NCP")),

                vulcanTimer = new BooleanSetting(this, "Vulcan Timer", false)
                        .setHidden(() -> !mode.compare("Vulcan")),

                motionY = new NumberSetting(this, "Motion Y", 0, 2, 0.42)
                        .setHidden(() -> !mode.compare("Custom")),

                airSpeed = new NumberSetting(this, "Air Speed", 0, 2, 0.4)
                        .setHidden(() -> !mode.compare("Custom")),

                downMultiplier = new NumberSetting(this, "Friction", 0, 3, 1)
                        .setHidden(() -> !mode.compare("Custom")),

                timerSpeed = new NumberSetting(this, "Timer", 0.1, 3, 1)
                        .setHidden(() -> !mode.compare("Custom")),

                groundSpeed = new NumberSetting(this, "Ground Speed", 0.1, 3, 0.1)
                        .setHidden(() -> !mode.compare("Custom")),

                strafe = new BooleanSetting(this, "Strafe", true)
                        .setHidden(() -> !mode.compare("Custom")),

                sprint = new BooleanSetting(this, "Sprint", true)
                        .setHidden(() -> !mode.compare("Custom")),

                yPort = new BooleanSetting(this, "Y-Port", false)
                        .setHidden(() -> !mode.compare("Custom")),

                minusMotionY = new NumberSetting(this, "Minus Motion Y", 0, 2, 0.4)
                        .setHidden(() -> !mode.compare("Custom")),
                
                damageBoost = new BooleanSetting(this, "Damage Boost", false)
                        .setHidden(() -> (mode.compare("MineMenClub") || mode.compare("Slow Hop")))

        );

        ModeSpeed.init();
        this.modeSpeed = ModeSpeed.getMode(mode.get());
    }

    @Override
    public void onEnable() {
        this.modeSpeed = ModeSpeed.getMode(mode.get());
        this.modeSpeed.setParent(this);
        this.modeSpeed.onEnable();
        Raze.INSTANCE.managerRegistry.eventManager.subscribe(this.modeSpeed);
    }

    @Override
    public void onDisable() {
        this.modeSpeed.onDisable();
        Raze.INSTANCE.managerRegistry.eventManager.unsubscribe(this.modeSpeed);
    }
}