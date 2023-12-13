package shadersmod.client;

import java.io.InputStream;
import shadersmod.client.IShaderPack;
import shadersmod.client.Shaders;

public class ShaderPackNone implements IShaderPack {
   public String getName() {
      return Shaders.packNameNone;
   }

   public InputStream getResourceAsStream(String resName) {
      return null;
   }

   public void close() {
   }

   public boolean hasDirectory(String name) {
      return false;
   }
}
