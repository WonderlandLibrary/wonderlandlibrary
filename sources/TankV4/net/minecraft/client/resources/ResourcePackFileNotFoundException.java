package net.minecraft.client.resources;

import java.io.File;
import java.io.FileNotFoundException;

public class ResourcePackFileNotFoundException extends FileNotFoundException {
   public ResourcePackFileNotFoundException(File var1, String var2) {
      super(String.format("'%s' in ResourcePack '%s'", var2, var1));
   }
}
