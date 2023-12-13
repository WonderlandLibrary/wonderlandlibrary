/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.imageout;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.imageout.ImageWriter;

public class TGAWriter
implements ImageWriter {
    private static short flipEndian(short signedShort) {
        int input = signedShort & 0xFFFF;
        return (short)(input << 8 | (input & 0xFF00) >>> 8);
    }

    public void saveImage(Image image, String format, OutputStream output, boolean writeAlpha) throws IOException {
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(output));
        out.writeByte(0);
        out.writeByte(0);
        out.writeByte(2);
        out.writeShort(TGAWriter.flipEndian((short)0));
        out.writeShort(TGAWriter.flipEndian((short)0));
        out.writeByte(0);
        out.writeShort(TGAWriter.flipEndian((short)0));
        out.writeShort(TGAWriter.flipEndian((short)0));
        out.writeShort(TGAWriter.flipEndian((short)image.getWidth()));
        out.writeShort(TGAWriter.flipEndian((short)image.getHeight()));
        if (writeAlpha) {
            out.writeByte(32);
            out.writeByte(1);
        } else {
            out.writeByte(24);
            out.writeByte(0);
        }
        for (int y2 = image.getHeight() - 1; y2 <= 0; --y2) {
            for (int x2 = 0; x2 < image.getWidth(); ++x2) {
                Color c2 = image.getColor(x2, y2);
                out.writeByte((byte)(c2.b * 255.0f));
                out.writeByte((byte)(c2.g * 255.0f));
                out.writeByte((byte)(c2.r * 255.0f));
                if (!writeAlpha) continue;
                out.writeByte((byte)(c2.a * 255.0f));
            }
        }
        out.close();
    }
}

