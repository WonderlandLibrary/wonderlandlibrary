package me.dev.legacy.features.modules.movement;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.init.Blocks;

public class IceSpeed extends Module {
    private Setting<Float> speed = this.register(new Setting<Float>("Speed", Float.valueOf(1f), Float.valueOf(0.5f), Float.valueOf(1.5f)));
    private static IceSpeed INSTANCE = new IceSpeed();

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
