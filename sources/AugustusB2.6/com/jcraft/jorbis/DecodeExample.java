// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import java.io.InputStream;
import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import java.io.FileInputStream;

class DecodeExample
{
    static int convsize;
    static byte[] convbuffer;
    
    public static void main(final String[] array) {
        InputStream in = System.in;
        if (array.length > 0) {
            try {
                in = new FileInputStream(array[0]);
            }
            catch (Exception x) {
                System.err.println(x);
            }
        }
        final SyncState syncState = new SyncState();
        final StreamState streamState = new StreamState();
        final Page page = new Page();
        final Packet packet = new Packet();
        final Info info = new Info();
        final Comment comment = new Comment();
        final DspState dspState = new DspState();
        final Block block = new Block(dspState);
        int n = 0;
        syncState.init();
        while (true) {
            int i = 0;
            final int buffer = syncState.buffer(4096);
            final byte[] data = syncState.data;
            try {
                n = in.read(data, buffer, 4096);
            }
            catch (Exception x2) {
                System.err.println(x2);
                System.exit(-1);
            }
            syncState.wrote(n);
            if (syncState.pageout(page) != 1) {
                if (n < 4096) {
                    break;
                }
                System.err.println("Input does not appear to be an Ogg bitstream.");
                System.exit(1);
            }
            streamState.init(page.serialno());
            info.init();
            comment.init();
            if (streamState.pagein(page) < 0) {
                System.err.println("Error reading first page of Ogg bitstream data.");
                System.exit(1);
            }
            if (streamState.packetout(packet) != 1) {
                System.err.println("Error reading initial header packet.");
                System.exit(1);
            }
            if (info.synthesis_headerin(comment, packet) < 0) {
                System.err.println("This Ogg bitstream does not contain Vorbis audio data.");
                System.exit(1);
            }
            int j = 0;
            while (j < 2) {
                while (j < 2) {
                    final int pageout = syncState.pageout(page);
                    if (pageout == 0) {
                        break;
                    }
                    if (pageout != 1) {
                        continue;
                    }
                    streamState.pagein(page);
                    while (j < 2) {
                        final int packetout = streamState.packetout(packet);
                        if (packetout == 0) {
                            break;
                        }
                        if (packetout == -1) {
                            System.err.println("Corrupt secondary header.  Exiting.");
                            System.exit(1);
                        }
                        info.synthesis_headerin(comment, packet);
                        ++j;
                    }
                }
                final int buffer2 = syncState.buffer(4096);
                final byte[] data2 = syncState.data;
                try {
                    n = in.read(data2, buffer2, 4096);
                }
                catch (Exception x3) {
                    System.err.println(x3);
                    System.exit(1);
                }
                if (n == 0 && j < 2) {
                    System.err.println("End of file before finding all Vorbis headers!");
                    System.exit(1);
                }
                syncState.wrote(n);
            }
            final byte[][] user_comments = comment.user_comments;
            for (int n2 = 0; n2 < user_comments.length && user_comments[n2] != null; ++n2) {
                System.err.println(new String(user_comments[n2], 0, user_comments[n2].length - 1));
            }
            System.err.println("\nBitstream is " + info.channels + " channel, " + info.rate + "Hz");
            System.err.println("Encoded by: " + new String(comment.vendor, 0, comment.vendor.length - 1) + "\n");
            DecodeExample.convsize = 4096 / info.channels;
            dspState.synthesis_init(info);
            block.init(dspState);
            final float[][][] array2 = { null };
            final int[] array3 = new int[info.channels];
            while (i == 0) {
                while (i == 0) {
                    final int pageout2 = syncState.pageout(page);
                    if (pageout2 == 0) {
                        break;
                    }
                    if (pageout2 == -1) {
                        System.err.println("Corrupt or missing data in bitstream; continuing...");
                    }
                    else {
                        streamState.pagein(page);
                        while (true) {
                            final int packetout2 = streamState.packetout(packet);
                            if (packetout2 == 0) {
                                break;
                            }
                            if (packetout2 == -1) {
                                continue;
                            }
                            if (block.synthesis(packet) == 0) {
                                dspState.synthesis_blockin(block);
                            }
                            int synthesis_pcmout;
                            while ((synthesis_pcmout = dspState.synthesis_pcmout(array2, array3)) > 0) {
                                final float[][] array4 = array2[0];
                                final int n3 = (synthesis_pcmout < DecodeExample.convsize) ? synthesis_pcmout : DecodeExample.convsize;
                                for (int k = 0; k < info.channels; ++k) {
                                    int n4 = k * 2;
                                    final int n5 = array3[k];
                                    for (int l = 0; l < n3; ++l) {
                                        int n6 = (int)(array4[k][n5 + l] * 32767.0);
                                        if (n6 > 32767) {
                                            n6 = 32767;
                                        }
                                        if (n6 < -32768) {
                                            n6 = -32768;
                                        }
                                        if (n6 < 0) {
                                            n6 |= 0x8000;
                                        }
                                        DecodeExample.convbuffer[n4] = (byte)n6;
                                        DecodeExample.convbuffer[n4 + 1] = (byte)(n6 >>> 8);
                                        n4 += 2 * info.channels;
                                    }
                                }
                                System.out.write(DecodeExample.convbuffer, 0, 2 * info.channels * n3);
                                dspState.synthesis_read(n3);
                            }
                        }
                        if (page.eos() == 0) {
                            continue;
                        }
                        i = 1;
                    }
                }
                if (i == 0) {
                    final int buffer3 = syncState.buffer(4096);
                    final byte[] data3 = syncState.data;
                    try {
                        n = in.read(data3, buffer3, 4096);
                    }
                    catch (Exception x4) {
                        System.err.println(x4);
                        System.exit(1);
                    }
                    syncState.wrote(n);
                    if (n != 0) {
                        continue;
                    }
                    i = 1;
                }
            }
            streamState.clear();
            block.clear();
            dspState.clear();
            info.clear();
        }
        syncState.clear();
        System.err.println("Done.");
    }
    
    static {
        DecodeExample.convsize = 8192;
        DecodeExample.convbuffer = new byte[DecodeExample.convsize];
    }
}
