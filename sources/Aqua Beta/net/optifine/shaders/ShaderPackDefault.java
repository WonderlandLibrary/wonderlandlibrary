package net.optifine.shaders;

import java.io.InputStream;
import net.optifine.shaders.IShaderPack;

public class ShaderPackDefault
implements IShaderPack {
    public void close() {
    }

    public InputStream getResourceAsStream(String resName) {
        return ShaderPackDefault.class.getResourceAsStream(resName);
    }

    public String getName() {
        return "(internal)";
    }

    public boolean hasDirectory(String name) {
        return false;
    }
}
