package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.init.Blocks;

public class IceSpeed
        extends Module {
    private static IceSpeed INSTANCE = new IceSpeed();
    private final Setting<Float> speed = this.register(new Setting<Float>("Speed", Float.valueOf(0.4f), Float.valueOf(0.2f), Float.valueOf(1.5f)));

    public IceSpeed() {
        super("IceSpeed", "Speeds you up on ice.", Module.Category.MOVEMENT, false, false, false);
        INSTANCE = this;
    }

    public static IceSpeed getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new IceSpeed();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        Blocks.ICE.slipperiness = this.speed.getValue().floatValue();
        Blocks.PACKED_ICE.slipperiness = this.speed.getValue().floatValue();
        Blocks.FROSTED_ICE.slipperiness = this.speed.getValue().floatValue();
    }

    @Override
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }
}

