/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources;

import java.io.File;
import java.io.FileNotFoundException;

public class ResourcePackFileNotFoundException
extends FileNotFoundException {
    public ResourcePackFileNotFoundException(File file, String string) {
        super(String.format("'%s' in ResourcePack '%s'", string, file));
    }
}

