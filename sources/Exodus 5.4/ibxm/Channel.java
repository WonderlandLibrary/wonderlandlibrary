/*
 * Decompiled with CFR 0.152.
 */
package ibxm;

import ibxm.Envelope;
import ibxm.Instrument;
import ibxm.LogTable;
import ibxm.Module;
import ibxm.Sample;

public class Channel {
    private boolean key_on;
    private int volume;
    private int left_gain;
    private int tremolo_wave;
    private Instrument instrument;
    private boolean silent;
    private int[] current_note;
    private Sample sample;
    private int tremolo_depth;
    private static final int LOG_2_1712;
    private int key_add;
    private int log_2_c2_rate;
    private int panning_envelope_tick;
    private int period;
    private boolean fast_volume_slides;
    private int vibrato_depth;
    private int step;
    private int tremolo_add;
    private int sample_idx;
    private int tremolo_speed;
    private int vibrato_tick;
    private boolean linear_periods;
    private int volume_slide_param;
    private int volume_envelope_tick;
    private int retrig_param;
    private int right_gain;
    private int[] global_volume;
    private int sample_frac;
    private int random_seed;
    private Module module;
    private int portamento_param;
    private static final int[] sine_table;
    private int trigger_tick;
    private int vibrato_speed;
    private int porta_period;
    private static final int LOG_2_29024;
    private int panning;
    private int fine_tune;
    private int tremolo_tick;
    private int vibrato_add;
    private int effect_tick;
    private int log_2_sampling_rate;
    private int vibrato_wave;
    private int fade_out_volume;
    public int pattern_loop_row;

    static {
        LOG_2_29024 = LogTable.log_2(29024);
        LOG_2_1712 = LogTable.log_2(1712);
        int[] nArray = new int[32];
        nArray[1] = 24;
        nArray[2] = 49;
        nArray[3] = 74;
        nArray[4] = 97;
        nArray[5] = 120;
        nArray[6] = 141;
        nArray[7] = 161;
        nArray[8] = 180;
        nArray[9] = 197;
        nArray[10] = 212;
        nArray[11] = 224;
        nArray[12] = 235;
        nArray[13] = 244;
        nArray[14] = 250;
        nArray[15] = 253;
        nArray[16] = 255;
        nArray[17] = 253;
        nArray[18] = 250;
        nArray[19] = 244;
        nArray[20] = 235;
        nArray[21] = 224;
        nArray[22] = 212;
        nArray[23] = 197;
        nArray[24] = 180;
        nArray[25] = 161;
        nArray[26] = 141;
        nArray[27] = 120;
        nArray[28] = 97;
        nArray[29] = 74;
        nArray[30] = 49;
        nArray[31] = 24;
        sine_table = nArray;
    }

    public void row(int n, int n2, int n3, int n4, int n5) {
        if ((n4 &= 0xFF) >= 48) {
            n4 = 0;
        }
        if (n4 == 0 && n5 != 0) {
            n4 = 64;
        }
        if (n4 == 14) {
            n4 = 48 + ((n5 & 0xF0) >> 4);
            n5 &= 0xF;
        }
        if (n4 == 33) {
            n4 = 64 + ((n5 & 0xF0) >> 4);
            n5 &= 0xF;
        }
        this.current_note[0] = n;
        this.current_note[1] = n2;
        this.current_note[2] = n3;
        this.current_note[3] = n4;
        this.current_note[4] = n5;
        this.effect_tick = 0;
        ++this.trigger_tick;
        this.update_envelopes();
        this.key_add = 0;
        this.vibrato_add = 0;
        this.tremolo_add = 0;
        if (n4 != 61 || n5 <= 0) {
            this.trigger(n, n2, n3, n4);
            switch (n3 & 0xF0) {
                case 0: {
                    break;
                }
                case 96: {
                    break;
                }
                case 112: {
                    break;
                }
                case 128: {
                    this.set_volume(this.volume - (n3 & 0xF));
                    break;
                }
                case 144: {
                    this.set_volume(this.volume + (n3 & 0xF));
                    break;
                }
                case 160: {
                    this.set_vibrato_speed(n3 & 0xF);
                    break;
                }
                case 176: {
                    this.set_vibrato_depth(n3 & 0xF);
                    this.vibrato();
                    break;
                }
                case 192: {
                    this.set_panning((n3 & 0xF) << 4);
                    break;
                }
                case 208: {
                    break;
                }
                case 224: {
                    break;
                }
                case 240: {
                    this.set_portamento_param(n3 & 0xF);
                    break;
                }
                default: {
                    this.set_volume(n3 - 16);
                }
            }
        }
        if (this.instrument.vibrato_depth > 0) {
            this.auto_vibrato();
        }
        switch (n4) {
            case 1: {
                this.set_portamento_param(n5);
                this.portamento_up();
                break;
            }
            case 2: {
                this.set_portamento_param(n5);
                this.portamento_down();
                break;
            }
            case 3: {
                this.set_portamento_param(n5);
                break;
            }
            case 4: {
                this.set_vibrato_speed((n5 & 0xF0) >> 4);
                this.set_vibrato_depth(n5 & 0xF);
                this.vibrato();
                break;
            }
            case 5: {
                this.set_volume_slide_param(n5);
                this.volume_slide();
                break;
            }
            case 6: {
                this.set_volume_slide_param(n5);
                this.vibrato();
                this.volume_slide();
                break;
            }
            case 7: {
                this.set_tremolo_speed((n5 & 0xF0) >> 4);
                this.set_tremolo_depth(n5 & 0xF);
                this.tremolo();
                break;
            }
            case 8: {
                this.set_panning(n5);
                break;
            }
            case 9: {
                this.set_sample_index(n5 << 8);
                break;
            }
            case 10: {
                this.set_volume_slide_param(n5);
                this.volume_slide();
                break;
            }
            case 11: {
                break;
            }
            case 12: {
                this.set_volume(n5);
                break;
            }
            case 13: {
                break;
            }
            case 14: {
                break;
            }
            case 15: {
                break;
            }
            case 16: {
                this.set_global_volume(n5);
                break;
            }
            case 17: {
                this.set_volume_slide_param(n5);
                break;
            }
            case 20: {
                if (n5 != 0) break;
                this.key_on = false;
                break;
            }
            case 21: {
                this.set_envelope_tick(n5);
                break;
            }
            case 25: {
                this.set_volume_slide_param(n5);
                break;
            }
            case 27: {
                this.set_retrig_param(n5);
                this.retrig_volume_slide();
                break;
            }
            case 29: {
                this.set_retrig_param(n5);
                this.tremor();
                break;
            }
            case 36: {
                this.set_vibrato_speed((n5 & 0xF0) >> 4);
                this.set_vibrato_depth(n5 & 0xF);
                this.fine_vibrato();
                break;
            }
            case 37: {
                break;
            }
            case 48: {
                break;
            }
            case 49: {
                this.set_portamento_param(0xF0 | n5);
                this.portamento_up();
                break;
            }
            case 50: {
                this.set_portamento_param(0xF0 | n5);
                this.portamento_down();
                break;
            }
            case 51: {
                break;
            }
            case 52: {
                this.set_vibrato_wave(n5);
                break;
            }
            case 53: {
                break;
            }
            case 54: {
                break;
            }
            case 55: {
                this.set_tremolo_wave(n5);
                break;
            }
            case 56: {
                break;
            }
            case 57: {
                this.set_retrig_param(n5);
                break;
            }
            case 58: {
                this.set_volume_slide_param(n5 << 4 | 0xF);
                this.volume_slide();
                break;
            }
            case 59: {
                this.set_volume_slide_param(0xF0 | n5);
                this.volume_slide();
                break;
            }
            case 60: {
                if (n5 != 0) break;
                this.set_volume(0);
                break;
            }
            case 61: {
                break;
            }
            case 62: {
                break;
            }
            case 63: {
                break;
            }
            case 64: {
                break;
            }
            case 65: {
                this.set_portamento_param(0xE0 | n5);
                this.portamento_up();
                break;
            }
            case 66: {
                this.set_portamento_param(0xE0 | n5);
                this.portamento_down();
            }
        }
        this.calculate_amplitude();
        this.calculate_frequency();
    }

    private void tremor() {
        int n = ((this.retrig_param & 0xF0) >> 4) + 1;
        int n2 = n + (this.retrig_param & 0xF) + 1;
        int n3 = this.trigger_tick % n2;
        if (n3 >= n) {
            this.tremolo_add = -64;
        }
    }

    private int key_to_period(int n) {
        int n2;
        n += this.sample.relative_note;
        if (this.linear_periods) {
            n2 = 7744 - n * 64;
        } else {
            int n3 = LOG_2_29024 - (n << 15) / 12;
            n2 = LogTable.raise_2(n3) >> 15;
        }
        return n2;
    }

    private void set_envelope_tick(int n) {
        this.volume_envelope_tick = n;
        this.panning_envelope_tick = n;
    }

    public void set_volume(int n) {
        if (n < 0) {
            n = 0;
        }
        if (n > 64) {
            n = 64;
        }
        this.volume = n;
    }

    private void set_vibrato_wave(int n) {
        if (n < 0 || n > 7) {
            n = 0;
        }
        this.vibrato_wave = n;
    }

    public void tick() {
        int n = this.current_note[2];
        int n2 = this.current_note[3];
        int n3 = this.current_note[4];
        ++this.effect_tick;
        if (n2 == 61 && n3 == this.effect_tick) {
            this.row(this.current_note[0], this.current_note[1], n, 0, 0);
        } else {
            ++this.trigger_tick;
            ++this.vibrato_tick;
            ++this.tremolo_tick;
            this.update_envelopes();
            this.key_add = 0;
            this.vibrato_add = 0;
            this.tremolo_add = 0;
            if (this.instrument.vibrato_depth > 0) {
                this.auto_vibrato();
            }
            switch (n & 0xF0) {
                case 96: {
                    this.set_volume(this.volume - (n & 0xF));
                    break;
                }
                case 112: {
                    this.set_volume(this.volume + (n & 0xF));
                    break;
                }
                case 176: {
                    this.vibrato();
                    break;
                }
                case 208: {
                    this.set_panning(this.panning - (n & 0xF));
                    break;
                }
                case 224: {
                    this.set_panning(this.panning + (n & 0xF));
                    break;
                }
                case 240: {
                    this.tone_portamento();
                }
            }
            block8 : switch (n2) {
                case 1: {
                    this.portamento_up();
                    break;
                }
                case 2: {
                    this.portamento_down();
                    break;
                }
                case 3: {
                    this.tone_portamento();
                    break;
                }
                case 4: {
                    this.vibrato();
                    break;
                }
                case 5: {
                    this.tone_portamento();
                    this.volume_slide();
                    break;
                }
                case 6: {
                    this.vibrato();
                    this.volume_slide();
                    break;
                }
                case 7: {
                    this.tremolo();
                    break;
                }
                case 10: {
                    this.volume_slide();
                    break;
                }
                case 17: {
                    this.global_volume_slide();
                    break;
                }
                case 20: {
                    if (this.effect_tick != n3) break;
                    this.key_on = false;
                    break;
                }
                case 25: {
                    this.panning_slide();
                    break;
                }
                case 27: {
                    this.retrig_volume_slide();
                    break;
                }
                case 29: {
                    this.tremor();
                    break;
                }
                case 36: {
                    this.fine_vibrato();
                    break;
                }
                case 57: {
                    this.retrig_volume_slide();
                    break;
                }
                case 60: {
                    if (this.effect_tick != n3) break;
                    this.set_volume(0);
                    break;
                }
                case 64: {
                    switch (this.effect_tick % 3) {
                        case 1: {
                            this.key_add = (n3 & 0xF0) >> 4;
                            break block8;
                        }
                        case 2: {
                            this.key_add = n3 & 0xF;
                        }
                    }
                }
            }
        }
        this.calculate_amplitude();
        this.calculate_frequency();
    }

    private void vibrato() {
        int n = this.vibrato_tick * this.vibrato_speed;
        this.vibrato_add += this.waveform(n, this.vibrato_wave) * this.vibrato_depth >> 5;
    }

    private void calculate_amplitude() {
        Envelope envelope;
        int n = 0;
        if (this.instrument.volume_envelope_active) {
            envelope = this.instrument.get_volume_envelope();
            n = envelope.calculate_ampl(this.volume_envelope_tick);
        } else if (this.key_on) {
            n = 64;
        }
        int n2 = this.volume + this.tremolo_add;
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 > 64) {
            n2 = 64;
        }
        int n3 = n2 << 9;
        n3 = n3 * n >> 6;
        n3 = n3 * this.fade_out_volume >> 15;
        n3 = n3 * this.global_volume[0] >> 6;
        n3 = n3 * this.module.channel_gain >> 15;
        this.silent = this.sample.has_finished(this.sample_idx);
        if (n3 <= 0) {
            this.silent = true;
        } else {
            int n4;
            int n5;
            int n6 = 32;
            if (this.instrument.panning_envelope_active) {
                envelope = this.instrument.get_panning_envelope();
                n6 = envelope.calculate_ampl(this.panning_envelope_tick);
            }
            if ((n5 = 32768 - (n4 = (this.panning & 0xFF) << 7)) > n4) {
                n5 = n4;
            }
            this.left_gain = n3 * (32768 - (n4 += n5 * (n6 - 32) >> 5)) >> 15;
            this.right_gain = n3 * n4 >> 15;
        }
    }

    private void set_tremolo_wave(int n) {
        if (n < 0 || n > 7) {
            n = 0;
        }
        this.tremolo_wave = n;
    }

    public void resample(int[] nArray, int n, int n2, int n3) {
        if (!this.silent) {
            switch (n3) {
                default: {
                    this.sample.resample_nearest(this.sample_idx, this.sample_frac, this.step, this.left_gain, this.right_gain, nArray, n, n2);
                    break;
                }
                case 1: {
                    this.sample.resample_linear(this.sample_idx, this.sample_frac, this.step, this.left_gain, this.right_gain, nArray, n, n2);
                    break;
                }
                case 2: {
                    this.sample.resample_sinc(this.sample_idx, this.sample_frac, this.step, this.left_gain, this.right_gain, nArray, n, n2);
                }
            }
        }
    }

    private void set_period(int n) {
        if (n < 32) {
            n = 32;
        }
        if (n > 32768) {
            n = 32768;
        }
        this.period = n;
    }

    private void update_envelopes() {
        Envelope envelope;
        if (this.instrument.volume_envelope_active) {
            if (!this.key_on) {
                this.fade_out_volume -= this.instrument.volume_fade_out & 0xFFFF;
                if (this.fade_out_volume < 0) {
                    this.fade_out_volume = 0;
                }
            }
            envelope = this.instrument.get_volume_envelope();
            this.volume_envelope_tick = envelope.next_tick(this.volume_envelope_tick, this.key_on);
        }
        if (this.instrument.panning_envelope_active) {
            envelope = this.instrument.get_panning_envelope();
            this.panning_envelope_tick = envelope.next_tick(this.panning_envelope_tick, this.key_on);
        }
    }

    private void set_vibrato_speed(int n) {
        if (n > 0) {
            this.vibrato_speed = n;
        }
    }

    private int waveform(int n, int n2) {
        int n3 = 0;
        switch (n2 & 3) {
            case 0: {
                if ((n & 0x20) == 0) {
                    n3 = sine_table[n & 0x1F];
                    break;
                }
                n3 = -sine_table[n & 0x1F];
                break;
            }
            case 1: {
                if ((n & 0x20) == 0) {
                    n3 = (n & 0x1F) << 3;
                    break;
                }
                n3 = ((n & 0x1F) << 3) - 255;
                break;
            }
            case 2: {
                if ((n & 0x20) == 0) {
                    n3 = 255;
                    break;
                }
                n3 = -255;
                break;
            }
            case 3: {
                n3 = (this.random_seed >> 15) - 255;
                this.random_seed = this.random_seed * 65 + 17 & 0xFFFFFF;
            }
        }
        return n3;
    }

    private void portamento_up() {
        if ((this.portamento_param & 0xF0) == 224) {
            if (this.effect_tick == 0) {
                this.set_period(this.period - (this.portamento_param & 0xF));
            }
        } else if ((this.portamento_param & 0xF0) == 240) {
            if (this.effect_tick == 0) {
                this.set_period(this.period - ((this.portamento_param & 0xF) << 2));
            }
        } else if (this.effect_tick > 0) {
            this.set_period(this.period - (this.portamento_param << 2));
        }
    }

    private void tremolo() {
        int n = this.tremolo_tick * this.tremolo_speed;
        this.tremolo_add += this.waveform(n, this.tremolo_wave) * this.tremolo_depth >> 6;
    }

    private void set_retrig_param(int n) {
        if (n != 0) {
            this.retrig_param = n;
        }
    }

    public void update_sample_idx(int n) {
        this.sample_frac += this.step * n;
        this.sample_idx += this.sample_frac >> 15;
        this.sample_frac &= Short.MAX_VALUE;
    }

    private void set_vibrato_depth(int n) {
        if (n > 0) {
            this.vibrato_depth = n;
        }
    }

    private void trigger(int n, int n2, int n3, int n4) {
        if (n2 > 0) {
            this.instrument = this.module.get_instrument(n2);
            this.sample = this.instrument.get_sample_from_key(n);
            this.set_volume(this.sample.volume);
            if (this.sample.set_panning) {
                this.set_panning(this.sample.panning);
            }
            this.log_2_c2_rate = LogTable.log_2(this.sample.c2_rate);
            this.set_envelope_tick(0);
            this.fade_out_volume = 32768;
            this.key_on = true;
        }
        if (n > 0) {
            if (n < 97) {
                this.porta_period = this.key_to_period(n);
                if (n4 != 3 && n4 != 5 && (n3 & 0xF0) != 240) {
                    this.trigger_tick = 0;
                    if (this.vibrato_wave < 4) {
                        this.vibrato_tick = 0;
                    }
                    if (this.tremolo_wave < 4) {
                        this.tremolo_tick = 0;
                    }
                    this.set_period(this.porta_period);
                    this.set_sample_index(0);
                }
            } else {
                this.key_on = false;
            }
        }
    }

    private void set_portamento_param(int n) {
        if (n != 0) {
            this.portamento_param = n;
        }
    }

    private void retrig_volume_slide() {
        int n = (this.retrig_param & 0xF0) >> 4;
        int n2 = this.retrig_param & 0xF;
        if (n2 > 0 && this.trigger_tick % n2 == 0) {
            this.set_sample_index(0);
            switch (n) {
                case 1: {
                    this.set_volume(this.volume - 1);
                    break;
                }
                case 2: {
                    this.set_volume(this.volume - 2);
                    break;
                }
                case 3: {
                    this.set_volume(this.volume - 4);
                    break;
                }
                case 4: {
                    this.set_volume(this.volume - 8);
                    break;
                }
                case 5: {
                    this.set_volume(this.volume - 16);
                    break;
                }
                case 6: {
                    this.set_volume(this.volume - this.volume / 3);
                    break;
                }
                case 7: {
                    this.set_volume(this.volume / 2);
                    break;
                }
                case 9: {
                    this.set_volume(this.volume + 1);
                    break;
                }
                case 10: {
                    this.set_volume(this.volume + 2);
                    break;
                }
                case 11: {
                    this.set_volume(this.volume + 4);
                    break;
                }
                case 12: {
                    this.set_volume(this.volume + 8);
                    break;
                }
                case 13: {
                    this.set_volume(this.volume + 16);
                    break;
                }
                case 14: {
                    this.set_volume(this.volume + this.volume / 2);
                    break;
                }
                case 15: {
                    this.set_volume(this.volume * 2);
                }
            }
        }
    }

    private void auto_vibrato() {
        int n = this.instrument.vibrato_sweep & 0xFF;
        int n2 = this.instrument.vibrato_depth & 0xF;
        int n3 = this.instrument.vibrato_rate & 0x3F;
        if (this.trigger_tick < n) {
            n2 = n2 * this.trigger_tick / n;
        }
        this.vibrato_add += this.waveform(this.trigger_tick * n3, 0) * n2 >> 9;
    }

    private void volume_slide() {
        int n = (this.volume_slide_param & 0xF0) >> 4;
        int n2 = this.volume_slide_param & 0xF;
        if (n2 == 15 && n > 0) {
            if (this.effect_tick == 0) {
                this.set_volume(this.volume + n);
            }
        } else if (n == 15 && n2 > 0) {
            if (this.effect_tick == 0) {
                this.set_volume(this.volume - n2);
            }
        } else if (this.effect_tick > 0 || this.fast_volume_slides) {
            this.set_volume(this.volume + n - n2);
        }
    }

    private void set_tremolo_depth(int n) {
        if (n > 0) {
            this.tremolo_depth = n;
        }
    }

    private void calculate_frequency() {
        int n = this.period + this.vibrato_add;
        if (n < 32) {
            n = 32;
        }
        if (n > 32768) {
            n = 32768;
        }
        int n2 = this.linear_periods ? this.log_2_c2_rate + (4608 - n << 15) / 768 : this.log_2_c2_rate + LOG_2_1712 - LogTable.log_2(n);
        this.step = LogTable.raise_2((n2 += ((this.key_add << 7) + this.sample.fine_tune << 15) / 1536) - this.log_2_sampling_rate);
    }

    private void portamento_down() {
        if ((this.portamento_param & 0xF0) == 224) {
            if (this.effect_tick == 0) {
                this.set_period(this.period + (this.portamento_param & 0xF));
            }
        } else if ((this.portamento_param & 0xF0) == 240) {
            if (this.effect_tick == 0) {
                this.set_period(this.period + ((this.portamento_param & 0xF) << 2));
            }
        } else if (this.effect_tick > 0) {
            this.set_period(this.period + (this.portamento_param << 2));
        }
    }

    private void set_tremolo_speed(int n) {
        if (n > 0) {
            this.tremolo_speed = n;
        }
    }

    private void set_sample_index(int n) {
        if (n < 0) {
            n = 0;
        }
        this.sample_idx = n;
        this.sample_frac = 0;
    }

    private void tone_portamento() {
        int n;
        if (this.porta_period < this.period) {
            n = this.period - (this.portamento_param << 2);
            if (n < this.porta_period) {
                n = this.porta_period;
            }
            this.set_period(n);
        }
        if (this.porta_period > this.period) {
            n = this.period + (this.portamento_param << 2);
            if (n > this.porta_period) {
                n = this.porta_period;
            }
            this.set_period(n);
        }
    }

    private void fine_vibrato() {
        int n = this.vibrato_tick * this.vibrato_speed;
        this.vibrato_add += this.waveform(n, this.vibrato_wave) * this.vibrato_depth >> 7;
    }

    private void set_volume_slide_param(int n) {
        if (n != 0) {
            this.volume_slide_param = n;
        }
    }

    public void set_panning(int n) {
        if (n < 0) {
            n = 0;
        }
        if (n > 255) {
            n = 255;
        }
        this.panning = n;
    }

    private void global_volume_slide() {
        int n = (this.volume_slide_param & 0xF0) >> 4;
        int n2 = this.volume_slide_param & 0xF;
        this.set_global_volume(this.global_volume[0] + n - n2);
    }

    public void reset() {
        this.tremolo_speed = 0;
        this.tremolo_depth = 0;
        this.tremolo_wave = 0;
        this.vibrato_speed = 0;
        this.vibrato_depth = 0;
        this.vibrato_wave = 0;
        this.volume_slide_param = 0;
        this.portamento_param = 0;
        this.retrig_param = 0;
        this.random_seed = 11256099;
        this.instrument = this.module.get_instrument(0);
        this.row(48, 256, 0, 0, 0);
    }

    public Channel(Module module, int n, int[] nArray) {
        this.module = module;
        this.global_volume = nArray;
        this.linear_periods = this.module.linear_periods;
        this.fast_volume_slides = this.module.fast_volume_slides;
        this.current_note = new int[5];
        this.log_2_sampling_rate = LogTable.log_2(n);
    }

    private void set_global_volume(int n) {
        if (n < 0) {
            n = 0;
        }
        if (n > 64) {
            n = 64;
        }
        this.global_volume[0] = n;
    }

    private void panning_slide() {
        int n = (this.volume_slide_param & 0xF0) >> 4;
        int n2 = this.volume_slide_param & 0xF;
        this.set_panning(this.panning - n + n2);
    }
}

