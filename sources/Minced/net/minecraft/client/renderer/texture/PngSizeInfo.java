// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import java.io.Closeable;
import net.minecraft.client.resources.IResource;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;

public class PngSizeInfo
{
    public final int pngWidth;
    public final int pngHeight;
    
    public PngSizeInfo(final InputStream stream) throws IOException {
        final DataInputStream datainputstream = new DataInputStream(stream);
        if (datainputstream.readLong() != -8552249625308161526L) {
            throw new IOException("Bad PNG Signature");
        }
        if (datainputstream.readInt() != 13) {
            throw new IOException("Bad length for IHDR chunk!");
        }
        if (datainputstream.readInt() != 1229472850) {
            throw new IOException("Bad type for IHDR chunk!");
        }
        this.pngWidth = datainputstream.readInt();
        this.pngHeight = datainputstream.readInt();
        IOUtils.closeQuietly((InputStream)datainputstream);
    }
    
    public static PngSizeInfo makeFromResource(final IResource resource) throws IOException {
        PngSizeInfo pngsizeinfo;
        try {
            pngsizeinfo = new PngSizeInfo(resource.getInputStream());
        }
        finally {
            IOUtils.closeQuietly((Closeable)resource);
        }
        return pngsizeinfo;
    }
}
