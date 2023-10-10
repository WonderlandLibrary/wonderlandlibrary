// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class ChainingExample
{
    public static void main(final String[] array) {
        VorbisFile vorbisFile;
        try {
            vorbisFile = new VorbisFile(System.in, null, -1);
        }
        catch (Exception x) {
            System.err.println(x);
            return;
        }
        if (vorbisFile.seekable()) {
            System.out.println("Input bitstream contained " + vorbisFile.streams() + " logical bitstream section(s).");
            System.out.println("Total bitstream playing time: " + vorbisFile.time_total(-1) + " seconds\n");
        }
        else {
            System.out.println("Standard input was not seekable.");
            System.out.println("First logical bitstream information:\n");
        }
        for (int i = 0; i < vorbisFile.streams(); ++i) {
            final Info info = vorbisFile.getInfo(i);
            System.out.println("\tlogical bitstream section " + (i + 1) + " information:");
            System.out.println("\t\t" + info.rate + "Hz " + info.channels + " channels bitrate " + vorbisFile.bitrate(i) / 1000 + "kbps serial number=" + vorbisFile.serialnumber(i));
            System.out.print("\t\tcompressed length: " + vorbisFile.raw_total(i) + " bytes ");
            System.out.println(" play time: " + vorbisFile.time_total(i) + "s");
            System.out.println(vorbisFile.getComment(i));
        }
    }
}
