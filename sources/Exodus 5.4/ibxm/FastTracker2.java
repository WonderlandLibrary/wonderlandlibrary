/*
 * Decompiled with CFR 0.152.
 */
package ibxm;

import ibxm.Envelope;
import ibxm.Instrument;
import ibxm.Module;
import ibxm.Pattern;
import ibxm.Sample;
import java.io.DataInput;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FastTracker2 {
    private static int unsigned_short_le(byte[] byArray, int n) {
        int n2 = byArray[n] & 0xFF;
        return n2 |= (byArray[n + 1] & 0xFF) << 8;
    }

    private static Sample read_xm_sample(byte[] byArray, int n, DataInput dataInput) throws IOException {
        int n2 = n * 40;
        Sample sample = new Sample();
        int n3 = FastTracker2.int_le(byArray, n2);
        int n4 = FastTracker2.int_le(byArray, n2 + 4);
        int n5 = FastTracker2.int_le(byArray, n2 + 8);
        sample.volume = byArray[n2 + 12] & 0xFF;
        sample.fine_tune = byArray[n2 + 13];
        sample.set_panning = true;
        int n6 = byArray[n2 + 14] & 0xFF;
        if ((n6 & 3) == 0) {
            n5 = 0;
        }
        boolean bl = (n6 & 2) == 2;
        boolean bl2 = (n6 & 0x10) == 16;
        sample.panning = byArray[n2 + 15] & 0xFF;
        sample.relative_note = byArray[n2 + 16];
        sample.name = FastTracker2.ascii_text(byArray, n2 + 18, 22);
        byte[] byArray2 = new byte[n3];
        dataInput.readFully(byArray2);
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        if (bl2) {
            short[] sArray = new short[n3 >> 1];
            while (n7 < byArray2.length) {
                n9 = byArray2[n7] & 0xFF;
                sArray[n8] = (short)(n10 += (n9 |= (byArray2[n7 + 1] & 0xFF) << 8));
                n7 += 2;
                ++n8;
            }
            sample.set_sample_data(sArray, n4 >> 1, n5 >> 1, bl);
        } else {
            short[] sArray = new short[n3];
            while (n7 < byArray2.length) {
                n9 = byArray2[n7] & 0xFF;
                sArray[n8] = (short)((n10 += n9) << 8);
                ++n7;
                ++n8;
            }
            sample.set_sample_data(sArray, n4, n5, bl);
        }
        return sample;
    }

    public static Module load_xm(byte[] byArray, DataInput dataInput) throws IOException {
        if (!FastTracker2.is_xm(byArray)) {
            throw new IllegalArgumentException("Not an XM file!");
        }
        int n = FastTracker2.unsigned_short_le(byArray, 58);
        if (n != 260) {
            throw new IllegalArgumentException("Sorry, XM version " + n + " is not supported!");
        }
        Module module = new Module();
        module.song_title = FastTracker2.ascii_text(byArray, 17, 20);
        byte[] byArray2 = new byte[4];
        dataInput.readFully(byArray2);
        int n2 = FastTracker2.int_le(byArray2, 0);
        byte[] byArray3 = new byte[n2];
        dataInput.readFully(byArray3, 4, n2 - 4);
        int n3 = FastTracker2.unsigned_short_le(byArray3, 4);
        module.restart_sequence_index = FastTracker2.unsigned_short_le(byArray3, 6);
        int n4 = FastTracker2.unsigned_short_le(byArray3, 8);
        int n5 = FastTracker2.unsigned_short_le(byArray3, 10);
        int n6 = FastTracker2.unsigned_short_le(byArray3, 12);
        int n7 = FastTracker2.unsigned_short_le(byArray3, 14);
        module.linear_periods = (n7 & 1) == 1;
        module.global_volume = 64;
        module.channel_gain = 12288;
        module.default_speed = FastTracker2.unsigned_short_le(byArray3, 16);
        module.default_tempo = FastTracker2.unsigned_short_le(byArray3, 18);
        module.set_num_channels(n4);
        int n8 = 0;
        while (n8 < n4) {
            module.set_initial_panning(n8, 128);
            ++n8;
        }
        module.set_sequence_length(n3);
        n8 = 0;
        while (n8 < n3) {
            module.set_sequence(n8, byArray3[20 + n8] & 0xFF);
            ++n8;
        }
        module.set_num_patterns(n5);
        n8 = 0;
        while (n8 < n5) {
            module.set_pattern(n8, FastTracker2.read_xm_pattern(dataInput, n4));
            ++n8;
        }
        module.set_num_instruments(n6);
        n8 = 1;
        while (n8 <= n6) {
            module.set_instrument(n8, FastTracker2.read_xm_instrument(dataInput));
            ++n8;
        }
        return module;
    }

    public static boolean is_xm(byte[] byArray) {
        String string = FastTracker2.ascii_text(byArray, 0, 17);
        return string.equals("Extended Module: ");
    }

    private static Instrument read_xm_instrument(DataInput dataInput) throws IOException {
        byte[] byArray = new byte[4];
        dataInput.readFully(byArray);
        int n = FastTracker2.int_le(byArray, 0);
        byte[] byArray2 = new byte[n];
        dataInput.readFully(byArray2, 4, n - 4);
        Instrument instrument = new Instrument();
        instrument.name = FastTracker2.ascii_text(byArray2, 4, 22);
        int n2 = FastTracker2.unsigned_short_le(byArray2, 27);
        if (n2 > 0) {
            int n3;
            int n4;
            instrument.set_num_samples(n2);
            int n5 = 0;
            while (n5 < 96) {
                instrument.set_key_to_sample(n5 + 1, byArray2[33 + n5] & 0xFF);
                ++n5;
            }
            Envelope envelope = new Envelope();
            int n6 = byArray2[225] & 0xFF;
            envelope.set_num_points(n6);
            n5 = 0;
            while (n5 < n6) {
                n4 = FastTracker2.unsigned_short_le(byArray2, 129 + n5 * 4);
                n3 = FastTracker2.unsigned_short_le(byArray2, 131 + n5 * 4);
                envelope.set_point(n5, n4, n3);
                ++n5;
            }
            envelope.set_sustain_point(byArray2[227] & 0xFF);
            envelope.set_loop_points(byArray2[228] & 0xFF, byArray2[229] & 0xFF);
            int n7 = byArray2[233] & 0xFF;
            instrument.volume_envelope_active = (n7 & 1) == 1;
            envelope.sustain = (n7 & 2) == 2;
            envelope.looped = (n7 & 4) == 4;
            instrument.set_volume_envelope(envelope);
            envelope = new Envelope();
            n6 = byArray2[226] & 0xFF;
            envelope.set_num_points(n6);
            n5 = 0;
            while (n5 < n6) {
                n4 = FastTracker2.unsigned_short_le(byArray2, 177 + n5 * 4);
                n3 = FastTracker2.unsigned_short_le(byArray2, 179 + n5 * 4);
                envelope.set_point(n5, n4, n3);
                ++n5;
            }
            envelope.set_sustain_point(byArray2[230] & 0xFF);
            envelope.set_loop_points(byArray2[231] & 0xFF, byArray2[232] & 0xFF);
            n7 = byArray2[234] & 0xFF;
            instrument.panning_envelope_active = (n7 & 1) == 1;
            envelope.sustain = (n7 & 2) == 2;
            envelope.looped = (n7 & 4) == 4;
            instrument.set_panning_envelope(envelope);
            instrument.vibrato_type = byArray2[235] & 0xFF;
            instrument.vibrato_sweep = byArray2[236] & 0xFF;
            instrument.vibrato_depth = byArray2[237] & 0xFF;
            instrument.vibrato_rate = byArray2[238] & 0xFF;
            instrument.volume_fade_out = FastTracker2.unsigned_short_le(byArray2, 239);
            byte[] byArray3 = new byte[n2 * 40];
            dataInput.readFully(byArray3);
            n5 = 0;
            while (n5 < n2) {
                instrument.set_sample(n5, FastTracker2.read_xm_sample(byArray3, n5, dataInput));
                ++n5;
            }
        }
        return instrument;
    }

    private static int int_le(byte[] byArray, int n) {
        int n2 = byArray[n] & 0xFF;
        n2 |= (byArray[n + 1] & 0xFF) << 8;
        n2 |= (byArray[n + 2] & 0xFF) << 16;
        return n2 |= (byArray[n + 3] & 0x7F) << 24;
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

    private static Pattern read_xm_pattern(DataInput dataInput, int n) throws IOException {
        byte[] byArray = new byte[4];
        dataInput.readFully(byArray);
        int n2 = FastTracker2.int_le(byArray, 0);
        byte[] byArray2 = new byte[n2];
        dataInput.readFully(byArray2, 4, n2 - 4);
        byte by = byArray2[4];
        if (by != 0) {
            throw new IllegalArgumentException("Pattern packing type " + by + " is not supported!");
        }
        Pattern pattern = new Pattern();
        pattern.num_rows = FastTracker2.unsigned_short_le(byArray2, 5);
        int n3 = FastTracker2.unsigned_short_le(byArray2, 7);
        byte[] byArray3 = new byte[n3];
        dataInput.readFully(byArray3);
        pattern.set_pattern_data(byArray3);
        return pattern;
    }
}

