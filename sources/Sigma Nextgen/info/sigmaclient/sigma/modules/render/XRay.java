package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class XRay extends Module {
    NumberValue alpha = new NumberValue("Alpha", 0, 0, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public XRay() {
        super("XRay", Category.Render, "Shows ores.");
     registerValue(alpha);
    }

    @Override
    public void onEnable() {
        mc.worldRenderer.loadRenderers();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.worldRenderer.loadRenderers();
        super.onDisable();
    }
}
