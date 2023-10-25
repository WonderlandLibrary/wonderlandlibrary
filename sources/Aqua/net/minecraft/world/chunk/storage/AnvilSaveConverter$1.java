package net.minecraft.world.chunk.storage;

import java.io.File;
import java.io.FilenameFilter;

class AnvilSaveConverter.1
implements FilenameFilter {
    AnvilSaveConverter.1() {
    }

    public boolean accept(File p_accept_1_, String p_accept_2_) {
        return p_accept_2_.endsWith(".mcr");
    }
}
