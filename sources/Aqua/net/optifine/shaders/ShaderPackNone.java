package net.optifine.shaders;

import java.io.InputStream;
import net.optifine.shaders.IShaderPack;

public class ShaderPackNone
implements IShaderPack {
    public void close() {
    }

    public InputStream getResourceAsStream(String resName) {
        return null;
    }

    public boolean hasDirectory(String name) {
        return false;
    }

    public String getName() {
        return "OFF";
    }
}
