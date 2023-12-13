package vip.radium.module.impl.visuals;

import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;

@ModuleInfo(label = "Animations", category = ModuleCategory.VISUALS)
public final class Animations extends Module {

    public final EnumProperty<Mode> animationModeProperty = new EnumProperty<>("Mode", Mode.LUL);
    public final Property<Boolean> equipProgressProperty = new Property<>("Equip Prog", true);
    public final DoubleProperty equipProgMultProperty = new DoubleProperty("E-Prog Multiplier", 2,
            equipProgressProperty::getValue, 0.5, 3.0, 0.1);
    public final DoubleProperty itemScale = new DoubleProperty("Item Scale", 0.7, 0.0, 2.0, 0.05);
    public final DoubleProperty swingSpeed = new DoubleProperty("Swing Duration", 1.0, 0.1, 2.0, 0.1);
    public final DoubleProperty xPosProperty = new DoubleProperty("X", 0.0, -1, 1, 0.05);
    public final DoubleProperty yPosProperty = new DoubleProperty("Y", 0.0, -1, 1, 0.05);
    public final DoubleProperty zPosProperty = new DoubleProperty("Z", 0.0, -1, 1, 0.05);


    public static Animations getInstance() {
        return ModuleManager.getInstance(Animations.class);
    }

    public Animations() {
        toggle();
        setHidden(true);
    }

    public enum Mode {
        LOL, LEL, LIL, LUL, EXHIBIBI, EXHIBOBO
    }
}
