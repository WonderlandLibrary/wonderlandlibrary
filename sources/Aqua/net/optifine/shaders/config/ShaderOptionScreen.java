package net.optifine.shaders.config;

import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.ShaderOption;

public class ShaderOptionScreen
extends ShaderOption {
    public ShaderOptionScreen(String name) {
        super(name, (String)null, (String)null, new String[0], (String)null, (String)null);
    }

    public String getNameText() {
        return Shaders.translate((String)("screen." + this.getName()), (String)this.getName());
    }

    public String getDescriptionText() {
        return Shaders.translate((String)("screen." + this.getName() + ".comment"), (String)null);
    }
}
