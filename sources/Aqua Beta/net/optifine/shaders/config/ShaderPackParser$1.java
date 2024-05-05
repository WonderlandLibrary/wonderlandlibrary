package net.optifine.shaders.config;

import java.util.Comparator;
import net.optifine.shaders.config.ShaderOption;

static final class ShaderPackParser.1
implements Comparator<ShaderOption> {
    ShaderPackParser.1() {
    }

    public int compare(ShaderOption o1, ShaderOption o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
