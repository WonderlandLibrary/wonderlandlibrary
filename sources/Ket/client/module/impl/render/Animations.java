package client.module.impl.render;

import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.value.impl.ModeValue;
import client.value.impl.NumberValue;
import client.value.impl.SubMode;
import lombok.Getter;

@Getter
@ModuleInfo(name = "Animations", description = "Niggers are black", category = Category.RENDER)
public class Animations extends Module {
    private final ModeValue blockAnimation = new ModeValue("Block", this)
            .add(new SubMode("Vanilla"))
            .add(new SubMode("ket"))
            .add(new SubMode("Test"))
            .add(new SubMode("Test2"))
            .add(new SubMode("Virtue"))
            .add(new SubMode("Remix"))
            .add(new SubMode("Sensation"))
            .add(new SubMode("Swank"))
            .add(new SubMode("Swang"))
            .setDefault("Vanilla");
    private final ModeValue swingAnimation = new ModeValue("Swing", this)
            .add(new SubMode("Vanilla"))
            .add(new SubMode("Smooth"))
            .add(new SubMode("Punch"))
            .setDefault("Vanilla");
    private final NumberValue scale = new NumberValue("scale", this, 0.4F, 0, 1, 0.01f);
    private final NumberValue x = new NumberValue("x", this, 0.56F, 0, 1, 0.01f);
    private final NumberValue y = new NumberValue("y", this, -0.52F, -1, 0, 0.01f);
    private final NumberValue z = new NumberValue("z", this, -0.71999997F, -1, 0, 0.01f);
}