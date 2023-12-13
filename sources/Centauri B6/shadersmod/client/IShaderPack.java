package shadersmod.client;

import java.io.InputStream;

public interface IShaderPack {
   String getName();

   InputStream getResourceAsStream(String var1);

   void close();

   boolean hasDirectory(String var1);
}
