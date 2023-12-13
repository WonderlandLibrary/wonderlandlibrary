/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.Info;
import com.jcraft.jorbis.VorbisFile;

class ChainingExample {
    ChainingExample() {
    }

    public static void main(String[] arg) {
        VorbisFile ov = null;
        try {
            ov = arg.length > 0 ? new VorbisFile(arg[0]) : new VorbisFile(System.in, null, -1);
        }
        catch (Exception e2) {
            System.err.println(e2);
            return;
        }
        if (ov.seekable()) {
            System.out.println("Input bitstream contained " + ov.streams() + " logical bitstream section(s).");
            System.out.println("Total bitstream playing time: " + ov.time_total(-1) + " seconds\n");
        } else {
            System.out.println("Standard input was not seekable.");
            System.out.println("First logical bitstream information:\n");
        }
        for (int i2 = 0; i2 < ov.streams(); ++i2) {
            Info vi = ov.getInfo(i2);
            System.out.println("\tlogical bitstream section " + (i2 + 1) + " information:");
            System.out.println("\t\t" + vi.rate + "Hz " + vi.channels + " channels bitrate " + ov.bitrate(i2) / 1000 + "kbps serial number=" + ov.serialnumber(i2));
            System.out.print("\t\tcompressed length: " + ov.raw_total(i2) + " bytes ");
            System.out.println(" play time: " + ov.time_total(i2) + "s");
            Comment vc = ov.getComment(i2);
            System.out.println(vc);
        }
    }
}

