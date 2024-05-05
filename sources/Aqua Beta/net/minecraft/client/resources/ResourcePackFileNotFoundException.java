package net.minecraft.client.resources;

import java.io.File;
import java.io.FileNotFoundException;

public class ResourcePackFileNotFoundException
extends FileNotFoundException {
    public ResourcePackFileNotFoundException(File resourcePack, String p_i1294_2_) {
        super(String.format((String)"'%s' in ResourcePack '%s'", (Object[])new Object[]{p_i1294_2_, resourcePack}));
    }
}
