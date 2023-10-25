/*
 * Decompiled with CFR 0.152.
 */
package ibxm;

import ibxm.Instrument;
import ibxm.Module;
import ibxm.Pattern;
import ibxm.Sample;
import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ScreamTracker3 {
    private static final int[] effect_s_map;
    private static final int[] effect_map;

    static {
        int[] nArray = new int[32];
        nArray[0] = 255;
        nArray[1] = 37;
        nArray[2] = 11;
        nArray[3] = 13;
        nArray[4] = 10;
        nArray[5] = 2;
        nArray[6] = 1;
        nArray[7] = 3;
        nArray[8] = 4;
        nArray[9] = 29;
        nArray[11] = 6;
        nArray[12] = 5;
        nArray[13] = 255;
        nArray[14] = 255;
        nArray[15] = 9;
        nArray[16] = 255;
        nArray[17] = 27;
        nArray[18] = 7;
        nArray[19] = 14;
        nArray[20] = 15;
        nArray[21] = 36;
        nArray[22] = 16;
        nArray[23] = 255;
        nArray[24] = 255;
        nArray[25] = 255;
        nArray[26] = 255;
        nArray[27] = 255;
        nArray[28] = 255;
        nArray[29] = 255;
        nArray[30] = 255;
        nArray[31] = 255;
        effect_map = nArray;
        int[] nArray2 = new int[16];
        nArray2[1] = 3;
        nArray2[2] = 5;
        nArray2[3] = 4;
        nArray2[4] = 7;
        nArray2[5] = 255;
        nArray2[6] = 255;
        nArray2[7] = 255;
        nArray2[8] = 8;
        nArray2[9] = 255;
        nArray2[10] = 9;
        nArray2[11] = 6;
        nArray2[12] = 12;
        nArray2[13] = 13;
        nArray2[14] = 14;
        nArray2[15] = 15;
        effect_s_map = nArray2;
    }

    private static int get_pattern_length(byte[] byArray, int n) {
        int n2 = ScreamTracker3.unsigned_short_le(byArray, n);
        return n2;
    }

    private static String ascii_text(byte[] byArray, int n, int n2) {
        String string;
        byte[] byArray2 = new byte[n2];
        int n3 = 0;
        while (n3 < n2) {
            int n4 = byArray[n + n3];
            if (n4 < 32) {
                n4 = 32;
            }
            byArray2[n3] = (byte)n4;
            ++n3;
        }
        try {
            string = new String(byArray2, 0, n2, "ISO-8859-1");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            string = "";
        }
        return string;
    }

    private static int unsigned_short_le(byte[] byArray, int n) {
        int n2 = byArray[n] & 0xFF;
        return n2 |= (byArray[n + 1] & 0xFF) << 8;
    }

    private static int get_sample_data_length(byte[] byArray, int n) {
        int n2 = 0;
        if (byArray[n] == 1) {
            n2 = ScreamTracker3.unsigned_short_le(byArray, n + 16);
        }
        return n2;
    }

    private static int[] read_s3m_sequence(byte[] byArray) {
        int n;
        int n2 = ScreamTracker3.get_num_pattern_orders(byArray);
        int n3 = 0;
        int n4 = 0;
        while (n4 < n2) {
            n = byArray[96 + n4] & 0xFF;
            if (n == 255) break;
            if (n < 254) {
                ++n3;
            }
            ++n4;
        }
        int[] nArray = new int[n3];
        int n5 = 0;
        n4 = 0;
        while (n4 < n2) {
            n = byArray[96 + n4] & 0xFF;
            if (n == 255) break;
            if (n < 254) {
                nArray[n5] = n;
                ++n5;
            }
            ++n4;
        }
        return nArray;
    }

    private static Instrument read_s3m_instrument(byte[] byArray, int n, boolean bl) {
        int n2 = ScreamTracker3.get_instrument_offset(byArray, n);
        Instrument instrument = new Instrument();
        instrument.name = ScreamTracker3.ascii_text(byArray, n2 + 48, 28);
        Sample sample = new Sample();
        if (byArray[n2] == 1) {
            int n3 = ScreamTracker3.get_sample_data_length(byArray, n2);
            int n4 = ScreamTracker3.unsigned_short_le(byArray, n2 + 20);
            int n5 = ScreamTracker3.unsigned_short_le(byArray, n2 + 24) - n4;
            sample.volume = byArray[n2 + 28] & 0xFF;
            if (byArray[n2 + 30] != 0) {
                throw new IllegalArgumentException("ScreamTracker3: Packed samples not supported!");
            }
            if ((byArray[n2 + 31] & 1) == 0) {
                n5 = 0;
            }
            sample.c2_rate = ScreamTracker3.unsigned_short_le(byArray, n2 + 32);
            short[] sArray = new short[n3];
            int n6 = ScreamTracker3.get_sample_data_offset(byArray, n2);
            if (bl) {
                int n7 = 0;
                while (n7 < n3) {
                    int n8 = byArray[n6 + n7] << 8;
                    sArray[n7] = (short)(n8 << 8);
                    ++n7;
                }
            } else {
                int n9 = 0;
                while (n9 < n3) {
                    int n10 = (byArray[n6 + n9] & 0xFF) - 128;
                    sArray[n9] = (short)(n10 << 8);
                    ++n9;
                }
            }
            sample.set_sample_data(sArray, n4, n5, false);
        }
        instrument.set_num_samples(1);
        instrument.set_sample(0, sample);
        return instrument;
    }

    private static int get_num_instruments(byte[] byArray) {
        int n = ScreamTracker3.unsigned_short_le(byArray, 34);
        return n;
    }

    private static int get_pattern_offset(byte[] byArray, int n) {
        int n2 = 96 + ScreamTracker3.get_num_pattern_orders(byArray);
        int n3 = ScreamTracker3.unsigned_short_le(byArray, (n2 += ScreamTracker3.get_num_instruments(byArray) * 2) + n * 2) << 4;
        return n3;
    }

    private static byte[] read_s3m_file(byte[] byArray, DataInput dataInput) throws IOException {
        int n;
        int n2;
        if (!ScreamTracker3.is_s3m(byArray)) {
            throw new IllegalArgumentException("ScreamTracker3: Not an S3M file!");
        }
        byte[] byArray2 = byArray;
        int n3 = byArray.length;
        int n4 = ScreamTracker3.get_num_pattern_orders(byArray2);
        int n5 = ScreamTracker3.get_num_instruments(byArray2);
        int n6 = ScreamTracker3.get_num_patterns(byArray2);
        n3 += n4;
        n3 += n5 * 2;
        byArray2 = ScreamTracker3.read_more(byArray2, n3 += n6 * 2, dataInput);
        int n7 = 0;
        while (n7 < n5) {
            n2 = ScreamTracker3.get_instrument_offset(byArray2, n7);
            if ((n2 += 80) > n3) {
                n3 = n2;
            }
            ++n7;
        }
        int n8 = 0;
        while (n8 < n6) {
            n = ScreamTracker3.get_pattern_offset(byArray2, n8);
            if ((n += 2) > n3) {
                n3 = n;
            }
            ++n8;
        }
        byArray2 = ScreamTracker3.read_more(byArray2, n3, dataInput);
        n7 = 0;
        while (n7 < n5) {
            n2 = ScreamTracker3.get_instrument_offset(byArray2, n7);
            int n9 = ScreamTracker3.get_sample_data_offset(byArray2, n2);
            if ((n9 += ScreamTracker3.get_sample_data_length(byArray2, n2)) > n3) {
                n3 = n9;
            }
            ++n7;
        }
        n8 = 0;
        while (n8 < n6) {
            n = ScreamTracker3.get_pattern_offset(byArray2, n8);
            n += ScreamTracker3.get_pattern_length(byArray2, n);
            if ((n += 2) > n3) {
                n3 = n;
            }
            ++n8;
        }
        byArray2 = ScreamTracker3.read_more(byArray2, n3, dataInput);
        return byArray2;
    }

    private static int get_num_patterns(byte[] byArray) {
        int n = ScreamTracker3.unsigned_short_le(byArray, 36);
        return n;
    }

    private static Pattern read_s3m_pattern(byte[] byArray, int n, int[] nArray) {
        int n2 = 0;
        int n3 = 0;
        while (n3 < 32) {
            if (nArray[n3] >= n2) {
                n2 = n3 + 1;
            }
            ++n3;
        }
        int n4 = n2 * 64;
        byte[] byArray2 = new byte[n4 * 5];
        int n5 = 0;
        int n6 = ScreamTracker3.get_pattern_offset(byArray, n) + 2;
        while (n5 < 64) {
            int n7 = byArray[n6] & 0xFF;
            ++n6;
            if (n7 > 0) {
                n3 = nArray[n7 & 0x1F];
                int n8 = (n2 * n5 + n3) * 5;
                if ((n7 & 0x20) == 32) {
                    if (n3 >= 0) {
                        int n9 = byArray[n6] & 0xFF;
                        if (n9 == 255) {
                            n9 = 0;
                        } else if (n9 == 254) {
                            n9 = 97;
                        } else {
                            n9 = ((n9 & 0xF0) >> 4) * 12 + (n9 & 0xF) + 1;
                            while (n9 > 96) {
                                n9 -= 12;
                            }
                        }
                        byArray2[n8] = (byte)n9;
                        byArray2[n8 + 1] = byArray[n6 + 1];
                    }
                    n6 += 2;
                }
                if ((n7 & 0x40) == 64) {
                    if (n3 >= 0) {
                        int n10 = (byArray[n6] & 0xFF) + 16;
                        byArray2[n8 + 2] = (byte)n10;
                    }
                    ++n6;
                }
                if ((n7 & 0x80) != 128) continue;
                if (n3 >= 0) {
                    int n11 = byArray[n6] & 0xFF;
                    int n12 = byArray[n6 + 1] & 0xFF;
                    if ((n11 = effect_map[n11 & 0x1F]) == 255) {
                        n11 = 0;
                        n12 = 0;
                    }
                    if (n11 == 14) {
                        n11 = effect_s_map[(n12 & 0xF0) >> 4];
                        n12 &= 0xF;
                        switch (n11) {
                            case 8: {
                                n11 = 8;
                                n12 <<= 4;
                                break;
                            }
                            case 9: {
                                n11 = 8;
                                if (n12 > 7) {
                                    n12 -= 8;
                                    break;
                                }
                                n12 += 8;
                                break;
                            }
                            case 255: {
                                n11 = 0;
                                n12 = 0;
                                break;
                            }
                            default: {
                                n12 = (n11 & 0xF) << 4 | n12 & 0xF;
                                n11 = 14;
                            }
                        }
                    }
                    byArray2[n8 + 3] = (byte)n11;
                    byArray2[n8 + 4] = (byte)n12;
                }
                n6 += 2;
                continue;
            }
            ++n5;
        }
        Pattern pattern = new Pattern();
        pattern.num_rows = 64;
        pattern.set_pattern_data(byArray2);
        return pattern;
    }

    public static Module load_s3m(byte[] byArray, DataInput dataInput) throws IOException {
        int n;
        byte[] byArray2 = ScreamTracker3.read_s3m_file(byArray, dataInput);
        Module module = new Module();
        module.song_title = ScreamTracker3.ascii_text(byArray2, 0, 28);
        int n2 = ScreamTracker3.get_num_pattern_orders(byArray2);
        int n3 = ScreamTracker3.get_num_instruments(byArray2);
        int n4 = ScreamTracker3.get_num_patterns(byArray2);
        int n5 = ScreamTracker3.unsigned_short_le(byArray2, 38);
        int n6 = ScreamTracker3.unsigned_short_le(byArray2, 40);
        if ((n5 & 0x40) == 64 || n6 == 4864) {
            module.fast_volume_slides = true;
        }
        boolean bl = false;
        if (ScreamTracker3.unsigned_short_le(byArray2, 42) == 1) {
            bl = true;
        }
        module.global_volume = byArray2[48] & 0xFF;
        module.default_speed = byArray2[49] & 0xFF;
        module.default_tempo = byArray2[50] & 0xFF;
        int n7 = byArray2[51] & 0x7F;
        module.channel_gain = n7 << 15 >> 7;
        boolean bl2 = (byArray2[51] & 0x80) == 128;
        boolean bl3 = (byArray2[53] & 0xFF) == 252;
        int[] nArray = new int[32];
        int n8 = 0;
        int n9 = 0;
        while (n9 < 32) {
            n = byArray2[64 + n9] & 0xFF;
            nArray[n9] = -1;
            if (n < 16) {
                nArray[n9] = n8++;
            }
            ++n9;
        }
        module.set_num_channels(n8);
        int n10 = 96 + n2 + n3 * 2 + n4 * 2;
        n9 = 0;
        while (n9 < 32) {
            n = byArray2[64 + n9] & 0xFF;
            if (nArray[n9] >= 0) {
                int n11 = 128;
                if (bl2) {
                    n11 = n < 8 ? 64 : 192;
                }
                if (bl3 && ((n5 = byArray2[n10 + n9] & 0xFF) & 0x20) == 32) {
                    n11 = (n5 & 0xF) << 4;
                }
                module.set_initial_panning(nArray[n9], n11);
            }
            ++n9;
        }
        int[] nArray2 = ScreamTracker3.read_s3m_sequence(byArray2);
        module.set_sequence_length(nArray2.length);
        int n12 = 0;
        while (n12 < nArray2.length) {
            module.set_sequence(n12, nArray2[n12]);
            ++n12;
        }
        module.set_num_instruments(n3);
        int n13 = 0;
        while (n13 < n3) {
            Instrument instrument = ScreamTracker3.read_s3m_instrument(byArray2, n13, bl);
            module.set_instrument(n13 + 1, instrument);
            ++n13;
        }
        module.set_num_patterns(n4);
        int n14 = 0;
        while (n14 < n4) {
            module.set_pattern(n14, ScreamTracker3.read_s3m_pattern(byArray2, n14, nArray));
            ++n14;
        }
        return module;
    }

    private static int get_sample_data_offset(byte[] byArray, int n) {
        int n2 = 0;
        if (byArray[n] == 1) {
            n2 = (byArray[n + 13] & 0xFF) << 20;
            n2 |= ScreamTracker3.unsigned_short_le(byArray, n + 14) << 4;
        }
        return n2;
    }

    private static int get_num_pattern_orders(byte[] byArray) {
        int n = ScreamTracker3.unsigned_short_le(byArray, 32);
        return n;
    }

    private static int get_instrument_offset(byte[] byArray, int n) {
        int n2 = 96 + ScreamTracker3.get_num_pattern_orders(byArray);
        int n3 = ScreamTracker3.unsigned_short_le(byArray, n2 + n * 2) << 4;
        return n3;
    }

    private static byte[] read_more(byte[] byArray, int n, DataInput dataInput) throws IOException {
        byte[] byArray2 = byArray;
        if (n > byArray.length) {
            byArray2 = new byte[n];
            System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
            try {
                dataInput.readFully(byArray2, byArray.length, byArray2.length - byArray.length);
            }
            catch (EOFException eOFException) {
                System.out.println("ScreamTracker3: Module has been truncated!");
            }
        }
        return byArray2;
    }

    public static boolean is_s3m(byte[] byArray) {
        String string = ScreamTracker3.ascii_text(byArray, 44, 4);
        return string.equals("SCRM");
    }
}

