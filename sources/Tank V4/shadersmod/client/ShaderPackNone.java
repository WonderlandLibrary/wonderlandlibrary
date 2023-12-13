package shadersmod.client;

import java.io.InputStream;

public class ShaderPackNone implements IShaderPack {
   public void close() {
   }

   public InputStream getResourceAsStream(String var1) {
      return null;
   }

   public boolean hasDirectory(String var1) {
      return false;
   }

   public String getName() {
      return Shaders.packNameNone;
   }
}
