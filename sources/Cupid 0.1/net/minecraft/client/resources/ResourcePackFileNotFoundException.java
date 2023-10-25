package net.minecraft.client.resources;

import java.io.File;
import java.io.FileNotFoundException;

public class ResourcePackFileNotFoundException extends FileNotFoundException {
  public ResourcePackFileNotFoundException(File p_i1294_1_, String p_i1294_2_) {
    super(String.format("'%s' in ResourcePack '%s'", new Object[] { p_i1294_2_, p_i1294_1_ }));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\resources\ResourcePackFileNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */