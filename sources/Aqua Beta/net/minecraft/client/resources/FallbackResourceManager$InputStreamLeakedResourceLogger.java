package net.minecraft.client.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.util.ResourceLocation;

/*
 * Exception performing whole class analysis ignored.
 */
static class FallbackResourceManager.InputStreamLeakedResourceLogger
extends InputStream {
    private final InputStream inputStream;
    private final String message;
    private boolean isClosed = false;

    public FallbackResourceManager.InputStreamLeakedResourceLogger(InputStream p_i46093_1_, ResourceLocation location, String resourcePack) {
        this.inputStream = p_i46093_1_;
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        new Exception().printStackTrace(new PrintStream((OutputStream)bytearrayoutputstream));
        this.message = "Leaked resource: '" + location + "' loaded from pack: '" + resourcePack + "'\n" + bytearrayoutputstream.toString();
    }

    public void close() throws IOException {
        this.inputStream.close();
        this.isClosed = true;
    }

    protected void finalize() throws Throwable {
        if (!this.isClosed) {
            FallbackResourceManager.access$000().warn(this.message);
        }
        super.finalize();
    }

    public int read() throws IOException {
        return this.inputStream.read();
    }
}
