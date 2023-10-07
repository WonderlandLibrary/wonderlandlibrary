package best.azura.client.impl.module.impl.other;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.impl.value.dependency.BooleanDependency;
import best.azura.client.impl.value.dependency.ModeDependency;

@ModuleInfo(name = "Animations", category = Category.RENDER, description = "Change the way you're blocking with the sword")
public class Animations extends Module {
    public final ModeValue mode = new ModeValue("Mode", "Change the type of modification", "Normal", "Normal", "Exhibition", "Azura");
    public final ModeValue normalModes = new ModeValue("Normal Modes", "Change the type of modification", new ModeDependency(mode, "Normal"), "1.7", "1.7", "1.8", "Old");
    public final ModeValue exhibitionModes = new ModeValue("Exhibition Modes", "Change the type of modification", new ModeDependency(mode, "Exhibition"), "Swong", "Swaing", "Swang", "Swank", "Swong", "Swonk", "Swing");
    public final ModeValue azuraModes = new ModeValue("Azura Modes", "Change the type of modification", new ModeDependency(mode, "Azura"), "Spin",
            "Spin", "Spun","Yam", "Yom", "Yem", "Yim",
            "Yiim", "Yoom", "Yaom", "Yoam", "Yiem", "Yiom","Yaam", "Yum", "Yeam", "Yooom", "Yoaem", "Error", "Jordy", "Jordy2", "Jordy3", "Jordy4", "Jordy5",
            "Wingy", "Slide", "Slide1", "Slide2", "Jerkin","PepoHax", "Anthrecite", "Small");
    public final NumberValue<Integer> slowHitAmount = new NumberValue<>("Slow Amount", "Change the slowdown of the animation", 0, 1, -4, 26);
    public final BooleanValue itemProgressBooleanValue = new BooleanValue("Equip progress", "Equipped progress", false);
    public final NumberValue<Float> equippedProgress = new NumberValue<>("Equipped progress multiplier", "Change the strength of the progress", new BooleanDependency(itemProgressBooleanValue, true), 0.5F, 0.1F, 0.1F, 2.0F);
    public final BooleanValue otherSwingValue = new BooleanValue("Other Swing", "Modify swing", false);
    public final NumberValue<Float> scale = new NumberValue<>("Scale", "Change the scale of the Item", 1.0F, 0.05F, 0.0F, 2.0F);
    public final NumberValue<Float> xPos = new NumberValue<>("X", "Change the X position of the Item", 0.0F, 0.05F, -2.0F, 2.0F);
    public final NumberValue<Float> yPos = new NumberValue<>("Y", "Change the Y position of the Item", 0.0F, 0.05F, -2.0F, 2.0F);
    public final NumberValue<Float> zPos = new NumberValue<>("Z", "Change the Z position of the Item", 0.0F, 0.05F, -2.0F, 2.0F);
    public final NumberValue<Float> heightValue = new NumberValue<>("Height", "Change the Y position of the Item outside blocking", 0.0F, 0.05F, -2.0F, 2.0F);

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}
