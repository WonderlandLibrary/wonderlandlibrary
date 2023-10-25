package net.minecraft.world.chunk.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

class RegionFile.ChunkBuffer
extends ByteArrayOutputStream {
    private int chunkX;
    private int chunkZ;

    public RegionFile.ChunkBuffer(int x, int z) {
        super(8096);
        this.chunkX = x;
        this.chunkZ = z;
    }

    public void close() throws IOException {
        RegionFile.this.write(this.chunkX, this.chunkZ, this.buf, this.count);
    }
}
