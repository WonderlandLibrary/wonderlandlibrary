/*
 * Decompiled with CFR 0.152.
 */
package ibxm;

import ibxm.Instrument;
import ibxm.LogTable;
import ibxm.Module;
import ibxm.Pattern;
import ibxm.Sample;
import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ProTracker {
    private static int unsigned_short_be(byte[] byArray, int n) {
        int n2 = (byArray[n] & 0xFF) << 8;
        return n2 |= byArray[n + 1] & 0xFF;
    }

    private static int calculate_num_channels(byte[] byArray) {
        int n;
        switch (byArray[1082] << 8 | byArray[1083]) {
            case 19233: 
            case 19246: 
            case 21550: 
            case 21556: {
                n = 4;
                break;
            }
            case 18510: {
                n = byArray[1080] - 48;
                break;
            }
            case 17224: {
                n = (byArray[1080] - 48) * 10 + (byArray[1081] - 48);
                break;
            }
            default: {
                n = 0;
            }
        }
        return n;
    }

    private static Instrument read_mod_instrument(byte[] byArray, int n, DataInput dataInput) throws IOException {
        int n2 = (n - 1) * 30 + 20;
        Instrument instrument = new Instrument();
        instrument.name = ProTracker.ascii_text(byArray, n2, 22);
        Sample sample = new Sample();
        sample.c2_rate = 8287;
        int n3 = ProTracker.unsigned_short_be(byArray, n2 + 22) << 1;
        sample.fine_tune = (byArray[n2 + 24] & 0xF) << 4;
        if (sample.fine_tune > 127) {
            sample.fine_tune -= 256;
        }
        sample.volume = byArray[n2 + 25] & 0x7F;
        int n4 = ProTracker.unsigned_short_be(byArray, n2 + 26) << 1;
        int n5 = ProTracker.unsigned_short_be(byArray, n2 + 28) << 1;
        if (n5 < 4) {
            n5 = 0;
        }
        byte[] byArray2 = new byte[n3];
        short[] sArray = new short[n3];
        try {
            dataInput.readFully(byArray2);
        }
        catch (EOFException eOFException) {
            System.out.println("ProTracker: Instrument " + n + " has samples missing.");
        }
        int n6 = 0;
        while (n6 < byArray2.length) {
            sArray[n6] = (short)(byArray2[n6] << 8);
            ++n6;
        }
        sample.set_sample_data(sArray, n4, n5, false);
        instrument.set_num_samples(1);
        instrument.set_sample(0, sample);
        return instrument;
    }

    public static boolean is_mod(byte[] byArray) {
        boolean bl = false;
        if (ProTracker.calculate_num_channels(byArray) > 0) {
            bl = true;
        }
        return bl;
    }

    private static byte to_key(int n) {
        int n2;
        if (n < 32) {
            n2 = 0;
        } else {
            int n3 = LogTable.log_2(7256) - LogTable.log_2(n);
            if (n3 < 0) {
                n2 = 0;
            } else {
                n2 = n3 * 12;
                n2 >>= 14;
                n2 = (n2 >> 1) + (n2 & 1);
            }
        }
        return (byte)n2;
    }

    private static int calculate_num_patterns(byte[] byArray) {
        int n = 0;
        int n2 = 0;
        while (n2 < 128) {
            int n3 = byArray[952 + n2] & 0x7F;
            if (n3 >= n) {
                n = n3 + 1;
            }
            ++n2;
        }
        return n;
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

    public static Module load_mod(byte[] byArray, DataInput dataInput) throws IOException {
        int n = ProTracker.calculate_num_channels(byArray);
        if (n < 1) {
            throw new IllegalArgumentException("ProTracker: Unrecognised module format!");
        }
        Module module = new Module();
        module.song_title = ProTracker.ascii_text(byArray, 0, 20);
        module.global_volume = 64;
        module.channel_gain = 12288;
        module.default_speed = 6;
        module.default_tempo = 125;
        module.set_num_channels(n);
        int n2 = 0;
        while (n2 < n) {
            int n3 = 64;
            if ((n2 & 3) == 1 || (n2 & 3) == 2) {
                n3 = 192;
            }
            module.set_initial_panning(n2, n3);
            ++n2;
        }
        int n4 = byArray[951] & 0x7F;
        int n5 = byArray[950] & 0x7F;
        if (n4 >= n5) {
            n4 = 0;
        }
        module.restart_sequence_index = n4;
        module.set_sequence_length(n5);
        int n6 = 0;
        while (n6 < n5) {
            module.set_sequence(n6, byArray[952 + n6] & 0x7F);
            ++n6;
        }
        int n7 = ProTracker.calculate_num_patterns(byArray);
        module.set_num_patterns(n7);
        int n8 = 0;
        while (n8 < n7) {
            module.set_pattern(n8, ProTracker.read_mod_pattern(dataInput, n));
            ++n8;
        }
        module.set_num_instruments(31);
        int n9 = 1;
        while (n9 <= 31) {
            module.set_instrument(n9, ProTracker.read_mod_instrument(byArray, n9, dataInput));
            ++n9;
        }
        return module;
    }

    private static Pattern read_mod_pattern(DataInput dataInput, int n) throws IOException {
        Pattern pattern = new Pattern();
        pattern.num_rows = 64;
        byte[] byArray = new byte[64 * n * 4];
        byte[] byArray2 = new byte[64 * n * 5];
        dataInput.readFully(byArray);
        int n2 = 0;
        int n3 = 0;
        while (n2 < byArray.length) {
            int n4 = (byArray[n2] & 0xF) << 8;
            byArray2[n3] = ProTracker.to_key(n4 |= byArray[n2 + 1] & 0xFF);
            int n5 = byArray[n2] & 0x10;
            byArray2[n3 + 1] = (byte)(n5 |= (byArray[n2 + 2] & 0xF0) >> 4);
            int n6 = byArray[n2 + 2] & 0xF;
            int n7 = byArray[n2 + 3] & 0xFF;
            if (n6 == 8 && n == 4) {
                n6 = 0;
                n7 = 0;
            }
            if (n6 == 10 && n7 == 0) {
                n6 = 0;
            }
            if (n6 == 5 && n7 == 0) {
                n6 = 3;
            }
            if (n6 == 6 && n7 == 0) {
                n6 = 4;
            }
            byArray2[n3 + 3] = (byte)n6;
            byArray2[n3 + 4] = (byte)n7;
            n2 += 4;
            n3 += 5;
        }
        pattern.set_pattern_data(byArray2);
        return pattern;
    }
}

