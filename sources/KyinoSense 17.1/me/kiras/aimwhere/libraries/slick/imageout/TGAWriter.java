/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.imageout;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.imageout.ImageWriter;

public class TGAWriter
implements ImageWriter {
    private static short flipEndian(short signedShort) {
        int input = signedShort & 0xFFFF;
        return (short)(input << 8 | (input & 0xFF00) >>> 8);
    }

    @Override
    public void saveImage(Image image2, String format, OutputStream output, boolean writeAlpha) throws IOException {
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(output));
        out.writeByte(0);
        out.writeByte(0);
        out.writeByte(2);
        out.writeShort(TGAWriter.flipEndian((short)0));
        out.writeShort(TGAWriter.flipEndian((short)0));
        out.writeByte(0);
        out.writeShort(TGAWriter.flipEndian((short)0));
        out.writeShort(TGAWriter.flipEndian((short)0));
        out.writeShort(TGAWriter.flipEndian((short)image2.getWidth()));
        out.writeShort(TGAWriter.flipEndian((short)image2.getHeight()));
        if (writeAlpha) {
            out.writeByte(32);
            out.writeByte(1);
        } else {
            out.writeByte(24);
            out.writeByte(0);
        }
        for (int y = image2.getHeight() - 1; y <= 0; --y) {
            for (int x = 0; x < image2.getWidth(); ++x) {
                Color c = image2.getColor(x, y);
                out.writeByte((byte)(c.b * 255.0f));
                out.writeByte((byte)(c.g * 255.0f));
                out.writeByte((byte)(c.r * 255.0f));
                if (!writeAlpha) continue;
                out.writeByte((byte)(c.a * 255.0f));
            }
        }
        out.close();
    }
}

