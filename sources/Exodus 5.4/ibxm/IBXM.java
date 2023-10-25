/*
 * Decompiled with CFR 0.152.
 */
package ibxm;

import ibxm.Channel;
import ibxm.Module;
import ibxm.Pattern;
import java.nio.ByteOrder;

public class IBXM {
    public static final String VERSION = "ibxm alpha 45 (c)2006 mumart@gmail.com";
    private int[] note;
    private int current_tick_samples;
    private int ticks_per_row;
    private int next_row;
    private Module module;
    private int tick_counter;
    private int[] mixing_buffer;
    private int tick_length_samples;
    public static final int FP_ONE = 32768;
    public static final int FP_SHIFT = 15;
    private Channel[] channels;
    private int sampling_rate;
    private int current_row;
    private int resampling_quality;
    private int volume_ramp_length;
    private int next_sequence_index;
    private int current_sequence_index;
    private int[] global_volume;
    private int pattern_loop_count;
    private int pattern_loop_channel;
    private int[] volume_ramp_buffer;
    public static final int FP_MASK = Short.MAX_VALUE;

    private void mix_tick() {
        int n = 0;
        int n2 = this.tick_length_samples + this.volume_ramp_length << 1;
        while (n < n2) {
            this.mixing_buffer[n] = 0;
            ++n;
        }
        int n3 = 0;
        while (n3 < this.channels.length) {
            n2 = this.tick_length_samples + this.volume_ramp_length;
            this.channels[n3].resample(this.mixing_buffer, 0, n2, this.resampling_quality);
            ++n3;
        }
        this.volume_ramp();
    }

    private boolean next_row() {
        boolean bl = false;
        if (this.next_sequence_index < 0) {
            this.next_sequence_index = 0;
            this.next_row = 0;
        }
        if (this.next_sequence_index >= this.module.get_sequence_length()) {
            bl = true;
            this.next_sequence_index = this.module.restart_sequence_index;
            if (this.next_sequence_index < 0) {
                this.next_sequence_index = 0;
            }
            if (this.next_sequence_index >= this.module.get_sequence_length()) {
                this.next_sequence_index = 0;
            }
            this.next_row = 0;
        }
        if (this.next_sequence_index < this.current_sequence_index) {
            bl = true;
        }
        if (this.next_sequence_index == this.current_sequence_index && this.next_row <= this.current_row && this.pattern_loop_count < 0) {
            bl = true;
        }
        this.current_sequence_index = this.next_sequence_index;
        Pattern pattern = this.module.get_pattern_from_sequence(this.current_sequence_index);
        if (this.next_row < 0 || this.next_row >= pattern.num_rows) {
            this.next_row = 0;
        }
        this.current_row = this.next_row;
        this.next_row = this.current_row + 1;
        if (this.next_row >= pattern.num_rows) {
            this.next_sequence_index = this.current_sequence_index + 1;
            this.next_row = 0;
        }
        int n = 0;
        while (n < this.channels.length) {
            pattern.get_note(this.note, this.current_row * this.channels.length + n);
            int n2 = this.note[3];
            int n3 = this.note[4];
            this.channels[n].row(this.note[0], this.note[1], this.note[2], n2, n3);
            switch (n2) {
                case 11: {
                    if (this.pattern_loop_count >= 0) break;
                    this.next_sequence_index = n3;
                    this.next_row = 0;
                    break;
                }
                case 13: {
                    if (this.pattern_loop_count >= 0) break;
                    this.next_sequence_index = this.current_sequence_index + 1;
                    this.next_row = (n3 >> 4) * 10 + (n3 & 0xF);
                    break;
                }
                case 14: {
                    switch (n3 & 0xF0) {
                        case 96: {
                            if ((n3 & 0xF) == 0) {
                                this.channels[n].pattern_loop_row = this.current_row;
                            }
                            if (this.channels[n].pattern_loop_row >= this.current_row) break;
                            if (this.pattern_loop_count < 0) {
                                this.pattern_loop_count = n3 & 0xF;
                                this.pattern_loop_channel = n;
                            }
                            if (this.pattern_loop_channel != n) break;
                            if (this.pattern_loop_count == 0) {
                                this.channels[n].pattern_loop_row = this.current_row + 1;
                            } else {
                                this.next_row = this.channels[n].pattern_loop_row;
                                this.next_sequence_index = this.current_sequence_index;
                            }
                            --this.pattern_loop_count;
                            break;
                        }
                        case 224: {
                            this.tick_counter += this.ticks_per_row * (n3 & 0xF);
                        }
                    }
                    break;
                }
                case 15: {
                    if (n3 < 32) {
                        this.set_speed(n3);
                        this.tick_counter = this.ticks_per_row;
                        break;
                    }
                    this.set_tempo(n3);
                    break;
                }
                case 37: {
                    this.set_speed(n3);
                    this.tick_counter = this.ticks_per_row;
                }
            }
            ++n;
        }
        return bl;
    }

    private void set_speed(int n) {
        if (n > 0 && n < 256) {
            this.ticks_per_row = n;
        }
    }

    public int calculate_song_duration() {
        this.set_sequence_index(0, 0);
        this.next_tick();
        int n = this.tick_length_samples;
        while (!this.next_tick()) {
            n += this.tick_length_samples;
        }
        this.set_sequence_index(0, 0);
        return n;
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

    public void seek(int n) {
        this.set_sequence_index(0, 0);
        this.next_tick();
        while (n > this.tick_length_samples) {
            n -= this.tick_length_samples;
            this.next_tick();
        }
        this.next_tick();
        this.current_tick_samples = n;
    }

    private boolean next_tick() {
        boolean bl;
        int n = 0;
        while (n < this.channels.length) {
            this.channels[n].update_sample_idx(this.tick_length_samples);
            ++n;
        }
        --this.tick_counter;
        if (this.tick_counter <= 0) {
            this.tick_counter = this.ticks_per_row;
            bl = this.next_row();
        } else {
            n = 0;
            while (n < this.channels.length) {
                this.channels[n].tick();
                ++n;
            }
            bl = false;
        }
        return bl;
    }

    public void set_module(Module module) {
        this.module = module;
        this.channels = new Channel[this.module.get_num_channels()];
        int n = 0;
        while (n < this.channels.length) {
            this.channels[n] = new Channel(this.module, this.sampling_rate, this.global_volume);
            ++n;
        }
        this.set_sequence_index(0, 0);
    }

    public IBXM(int n) {
        System.out.println("ibxm alpha 45 (c)2006 mumart@gmail.com");
        if (n < 8000) {
            n = 8000;
        }
        this.sampling_rate = n;
        this.volume_ramp_length = this.sampling_rate >> 10;
        this.volume_ramp_buffer = new int[this.volume_ramp_length * 2];
        this.mixing_buffer = new int[this.sampling_rate / 6];
        this.global_volume = new int[1];
        this.note = new int[5];
        this.set_module(new Module());
        this.set_resampling_quality(1);
    }

    private void set_tempo(int n) {
        if (n > 31 && n < 256) {
            this.tick_length_samples = this.sampling_rate * 5 / (n * 2);
        }
    }

    public void set_sequence_index(int n, int n2) {
        this.global_volume[0] = 64;
        int n3 = 0;
        while (n3 < this.channels.length) {
            this.channels[n3].reset();
            this.channels[n3].set_panning(this.module.get_initial_panning(n3));
            ++n3;
        }
        this.set_global_volume(this.module.global_volume);
        this.set_speed(6);
        this.set_speed(this.module.default_speed);
        this.set_tempo(125);
        this.set_tempo(this.module.default_tempo);
        this.pattern_loop_count = -1;
        this.next_sequence_index = n;
        this.next_row = n2;
        this.tick_counter = 0;
        this.current_tick_samples = this.tick_length_samples;
    }

    private void volume_ramp() {
        int n = 0;
        int n2 = 32768 / this.volume_ramp_length;
        int n3 = 0;
        int n4 = 0;
        int n5 = 2 * this.tick_length_samples;
        int n6 = this.volume_ramp_length * 2 - 1;
        while (n4 <= n6) {
            n = this.volume_ramp_buffer[n4] * (32768 - n3) >> 15;
            this.mixing_buffer[n4] = n + (this.mixing_buffer[n4] * n3 >> 15);
            this.volume_ramp_buffer[n4] = this.mixing_buffer[n5 + n4];
            n = this.volume_ramp_buffer[n4 + 1] * (32768 - n3) >> 15;
            this.mixing_buffer[n4 + 1] = n + (this.mixing_buffer[n4 + 1] * n3 >> 15);
            this.volume_ramp_buffer[n4 + 1] = this.mixing_buffer[n5 + n4 + 1];
            n3 += n2;
            n4 += 2;
        }
    }

    public void set_resampling_quality(int n) {
        this.resampling_quality = n;
    }

    public void get_audio(byte[] byArray, int n) {
        boolean bl = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
        int n2 = 0;
        while (n > 0) {
            int n3 = this.tick_length_samples - this.current_tick_samples;
            if (n3 > n) {
                n3 = n;
            }
            int n4 = this.current_tick_samples << 1;
            int n5 = n4 + (n3 << 1) - 1;
            while (n4 <= n5) {
                int n6 = this.mixing_buffer[n4];
                if (n6 > Short.MAX_VALUE) {
                    n6 = Short.MAX_VALUE;
                }
                if (n6 < Short.MIN_VALUE) {
                    n6 = Short.MIN_VALUE;
                }
                if (bl) {
                    byArray[n2] = (byte)(n6 >> 8);
                    byArray[n2 + 1] = (byte)(n6 & 0xFF);
                } else {
                    byArray[n2] = (byte)(n6 & 0xFF);
                    byArray[n2 + 1] = (byte)(n6 >> 8);
                }
                n2 += 2;
                ++n4;
            }
            this.current_tick_samples = n4 >> 1;
            if ((n -= n3) <= 0) continue;
            this.next_tick();
            this.mix_tick();
            this.current_tick_samples = 0;
        }
    }
}

