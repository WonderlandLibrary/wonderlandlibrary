/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import java.io.FileInputStream;
import java.io.InputStream;

class DecodeExample {
    static int convsize = 8192;
    static byte[] convbuffer = new byte[convsize];

    DecodeExample() {
    }

    public static void main(String[] arg) {
        InputStream input = System.in;
        if (arg.length > 0) {
            try {
                input = new FileInputStream(arg[0]);
            }
            catch (Exception e2) {
                System.err.println(e2);
            }
        }
        SyncState oy = new SyncState();
        StreamState os = new StreamState();
        Page og = new Page();
        Packet op = new Packet();
        Info vi = new Info();
        Comment vc = new Comment();
        DspState vd = new DspState();
        Block vb = new Block(vd);
        int bytes = 0;
        oy.init();
        while (true) {
            boolean eos = false;
            int index = oy.buffer(4096);
            byte[] buffer = oy.data;
            try {
                bytes = input.read(buffer, index, 4096);
            }
            catch (Exception e3) {
                System.err.println(e3);
                System.exit(-1);
            }
            oy.wrote(bytes);
            if (oy.pageout(og) != 1) {
                if (bytes < 4096) break;
                System.err.println("Input does not appear to be an Ogg bitstream.");
                System.exit(1);
            }
            os.init(og.serialno());
            vi.init();
            vc.init();
            if (os.pagein(og) < 0) {
                System.err.println("Error reading first page of Ogg bitstream data.");
                System.exit(1);
            }
            if (os.packetout(op) != 1) {
                System.err.println("Error reading initial header packet.");
                System.exit(1);
            }
            if (vi.synthesis_headerin(vc, op) < 0) {
                System.err.println("This Ogg bitstream does not contain Vorbis audio data.");
                System.exit(1);
            }
            int i2 = 0;
            while (i2 < 2) {
                int result;
                while (i2 < 2 && (result = oy.pageout(og)) != 0) {
                    if (result != 1) continue;
                    os.pagein(og);
                    while (i2 < 2 && (result = os.packetout(op)) != 0) {
                        if (result == -1) {
                            System.err.println("Corrupt secondary header.  Exiting.");
                            System.exit(1);
                        }
                        vi.synthesis_headerin(vc, op);
                        ++i2;
                    }
                }
                index = oy.buffer(4096);
                buffer = oy.data;
                try {
                    bytes = input.read(buffer, index, 4096);
                }
                catch (Exception e4) {
                    System.err.println(e4);
                    System.exit(1);
                }
                if (bytes == 0 && i2 < 2) {
                    System.err.println("End of file before finding all Vorbis headers!");
                    System.exit(1);
                }
                oy.wrote(bytes);
            }
            byte[][] ptr = vc.user_comments;
            for (int j2 = 0; j2 < ptr.length && ptr[j2] != null; ++j2) {
                System.err.println(new String(ptr[j2], 0, ptr[j2].length - 1));
            }
            System.err.println("\nBitstream is " + vi.channels + " channel, " + vi.rate + "Hz");
            System.err.println("Encoded by: " + new String(vc.vendor, 0, vc.vendor.length - 1) + "\n");
            convsize = 4096 / vi.channels;
            vd.synthesis_init(vi);
            vb.init(vd);
            float[][][] _pcm = new float[1][][];
            int[] _index = new int[vi.channels];
            while (!eos) {
                int result;
                while (!eos && (result = oy.pageout(og)) != 0) {
                    if (result == -1) {
                        System.err.println("Corrupt or missing data in bitstream; continuing...");
                        continue;
                    }
                    os.pagein(og);
                    while ((result = os.packetout(op)) != 0) {
                        int samples;
                        if (result == -1) continue;
                        if (vb.synthesis(op) == 0) {
                            vd.synthesis_blockin(vb);
                        }
                        while ((samples = vd.synthesis_pcmout(_pcm, _index)) > 0) {
                            float[][] pcm = _pcm[0];
                            int bout = samples < convsize ? samples : convsize;
                            for (i2 = 0; i2 < vi.channels; ++i2) {
                                int ptr2 = i2 * 2;
                                int mono = _index[i2];
                                for (int j3 = 0; j3 < bout; ++j3) {
                                    int val2 = (int)((double)pcm[i2][mono + j3] * 32767.0);
                                    if (val2 > Short.MAX_VALUE) {
                                        val2 = Short.MAX_VALUE;
                                    }
                                    if (val2 < Short.MIN_VALUE) {
                                        val2 = Short.MIN_VALUE;
                                    }
                                    if (val2 < 0) {
                                        val2 |= 0x8000;
                                    }
                                    DecodeExample.convbuffer[ptr2] = (byte)val2;
                                    DecodeExample.convbuffer[ptr2 + 1] = (byte)(val2 >>> 8);
                                    ptr2 += 2 * vi.channels;
                                }
                            }
                            System.out.write(convbuffer, 0, 2 * vi.channels * bout);
                            vd.synthesis_read(bout);
                        }
                    }
                    if (og.eos() == 0) continue;
                    eos = true;
                }
                if (eos) continue;
                index = oy.buffer(4096);
                buffer = oy.data;
                try {
                    bytes = input.read(buffer, index, 4096);
                }
                catch (Exception e5) {
                    System.err.println(e5);
                    System.exit(1);
                }
                oy.wrote(bytes);
                if (bytes != 0) continue;
                eos = true;
            }
            os.clear();
            vb.clear();
            vd.clear();
            vi.clear();
        }
        oy.clear();
        System.err.println("Done.");
    }
}

