/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.InvalidDataException;

public class MpegFrame {
    public static final String MPEG_VERSION_1_0 = "1.0";
    public static final String MPEG_VERSION_2_0 = "2.0";
    public static final String MPEG_VERSION_2_5 = "2.5";
    public static final String MPEG_LAYER_1 = "I";
    public static final String MPEG_LAYER_2 = "II";
    public static final String MPEG_LAYER_3 = "III";
    public static final String[] MPEG_LAYERS = new String[]{null, "I", "II", "III"};
    public static final String CHANNEL_MODE_MONO = "Mono";
    public static final String CHANNEL_MODE_DUAL_MONO = "Dual mono";
    public static final String CHANNEL_MODE_JOINT_STEREO = "Joint stereo";
    public static final String CHANNEL_MODE_STEREO = "Stereo";
    public static final String MODE_EXTENSION_BANDS_4_31 = "Bands 4-31";
    public static final String MODE_EXTENSION_BANDS_8_31 = "Bands 8-31";
    public static final String MODE_EXTENSION_BANDS_12_31 = "Bands 12-31";
    public static final String MODE_EXTENSION_BANDS_16_31 = "Bands 16-31";
    public static final String MODE_EXTENSION_NONE = "None";
    public static final String MODE_EXTENSION_INTENSITY_STEREO = "Intensity stereo";
    public static final String MODE_EXTENSION_M_S_STEREO = "M/S stereo";
    public static final String MODE_EXTENSION_INTENSITY_M_S_STEREO = "Intensity & M/S stereo";
    public static final String MODE_EXTENSION_NA = "n/a";
    public static final String EMPHASIS_NONE = "None";
    public static final String EMPHASIS__50_15_MS = "50/15 ms";
    public static final String EMPHASIS_CCITT_J_17 = "CCITT J.17";
    private static final int FRAME_DATA_LENGTH = 4;
    private static final int FRAME_SYNC = 2047;
    private static final long BITMASK_FRAME_SYNC = 0xFFE00000L;
    private static final long BITMASK_VERSION = 0x180000L;
    private static final long BITMASK_LAYER = 393216L;
    private static final long BITMASK_PROTECTION = 65536L;
    private static final long BITMASK_BITRATE = 61440L;
    private static final long BITMASK_SAMPLE_RATE = 3072L;
    private static final long BITMASK_PADDING = 512L;
    private static final long BITMASK_PRIVATE = 256L;
    private static final long BITMASK_CHANNEL_MODE = 192L;
    private static final long BITMASK_MODE_EXTENSION = 48L;
    private static final long BITMASK_COPYRIGHT = 8L;
    private static final long BITMASK_ORIGINAL = 4L;
    private static final long BITMASK_EMPHASIS = 3L;
    private String version;
    private int layer;
    private boolean protection;
    private int bitrate;
    private int sampleRate;
    private boolean padding;
    private boolean privat;
    private String channelMode;
    private String modeExtension;
    private boolean copyright;
    private boolean original;
    private String emphasis;

    public MpegFrame(byte[] byArray) throws InvalidDataException {
        if (byArray.length < 4) {
            throw new InvalidDataException("Mpeg frame too short");
        }
        long l2 = BufferTools.unpackInteger(byArray[0], byArray[1], byArray[2], byArray[3]);
        this.setFields(l2);
    }

    public MpegFrame(byte by, byte by2, byte by3, byte by4) throws InvalidDataException {
        long l2 = BufferTools.unpackInteger(by, by2, by3, by4);
        this.setFields(l2);
    }

    protected MpegFrame() {
    }

    private void setFields(long l2) throws InvalidDataException {
        long l3 = this.extractField(l2, 0xFFE00000L);
        if (l3 != 2047L) {
            throw new InvalidDataException("Frame sync missing");
        }
        this.setVersion(this.extractField(l2, 0x180000L));
        this.setLayer(this.extractField(l2, 393216L));
        this.setProtection(this.extractField(l2, 65536L));
        this.setBitRate(this.extractField(l2, 61440L));
        this.setSampleRate(this.extractField(l2, 3072L));
        this.setPadding(this.extractField(l2, 512L));
        this.setPrivate(this.extractField(l2, 256L));
        this.setChannelMode(this.extractField(l2, 192L));
        this.setModeExtension(this.extractField(l2, 48L));
        this.setCopyright(this.extractField(l2, 8L));
        this.setOriginal(this.extractField(l2, 4L));
        this.setEmphasis(this.extractField(l2, 3L));
    }

    protected int extractField(long l2, long l3) {
        int n2 = 0;
        for (int i2 = 0; i2 <= 31; ++i2) {
            if ((l3 >> i2 & 1L) == 0L) continue;
            n2 = i2;
            break;
        }
        return (int)(l2 >> n2 & l3 >> n2);
    }

    private void setVersion(int n2) throws InvalidDataException {
        switch (n2) {
            case 0: {
                this.version = MPEG_VERSION_2_5;
                break;
            }
            case 2: {
                this.version = MPEG_VERSION_2_0;
                break;
            }
            case 3: {
                this.version = MPEG_VERSION_1_0;
                break;
            }
            default: {
                throw new InvalidDataException("Invalid mpeg audio version in frame header");
            }
        }
    }

    private void setLayer(int n2) throws InvalidDataException {
        switch (n2) {
            case 1: {
                this.layer = 3;
                break;
            }
            case 2: {
                this.layer = 2;
                break;
            }
            case 3: {
                this.layer = 1;
                break;
            }
            default: {
                throw new InvalidDataException("Invalid mpeg layer description in frame header");
            }
        }
    }

    private void setProtection(int n2) {
        this.protection = n2 == 1;
    }

    private void setBitRate(int n2) throws InvalidDataException {
        if (MPEG_VERSION_1_0.equals(this.version)) {
            if (this.layer == 1) {
                switch (n2) {
                    case 1: {
                        this.bitrate = 32;
                        return;
                    }
                    case 2: {
                        this.bitrate = 64;
                        return;
                    }
                    case 3: {
                        this.bitrate = 96;
                        return;
                    }
                    case 4: {
                        this.bitrate = 128;
                        return;
                    }
                    case 5: {
                        this.bitrate = 160;
                        return;
                    }
                    case 6: {
                        this.bitrate = 192;
                        return;
                    }
                    case 7: {
                        this.bitrate = 224;
                        return;
                    }
                    case 8: {
                        this.bitrate = 256;
                        return;
                    }
                    case 9: {
                        this.bitrate = 288;
                        return;
                    }
                    case 10: {
                        this.bitrate = 320;
                        return;
                    }
                    case 11: {
                        this.bitrate = 352;
                        return;
                    }
                    case 12: {
                        this.bitrate = 384;
                        return;
                    }
                    case 13: {
                        this.bitrate = 416;
                        return;
                    }
                    case 14: {
                        this.bitrate = 448;
                        return;
                    }
                }
            } else if (this.layer == 2) {
                switch (n2) {
                    case 1: {
                        this.bitrate = 32;
                        return;
                    }
                    case 2: {
                        this.bitrate = 48;
                        return;
                    }
                    case 3: {
                        this.bitrate = 56;
                        return;
                    }
                    case 4: {
                        this.bitrate = 64;
                        return;
                    }
                    case 5: {
                        this.bitrate = 80;
                        return;
                    }
                    case 6: {
                        this.bitrate = 96;
                        return;
                    }
                    case 7: {
                        this.bitrate = 112;
                        return;
                    }
                    case 8: {
                        this.bitrate = 128;
                        return;
                    }
                    case 9: {
                        this.bitrate = 160;
                        return;
                    }
                    case 10: {
                        this.bitrate = 192;
                        return;
                    }
                    case 11: {
                        this.bitrate = 224;
                        return;
                    }
                    case 12: {
                        this.bitrate = 256;
                        return;
                    }
                    case 13: {
                        this.bitrate = 320;
                        return;
                    }
                    case 14: {
                        this.bitrate = 384;
                        return;
                    }
                }
            } else if (this.layer == 3) {
                switch (n2) {
                    case 1: {
                        this.bitrate = 32;
                        return;
                    }
                    case 2: {
                        this.bitrate = 40;
                        return;
                    }
                    case 3: {
                        this.bitrate = 48;
                        return;
                    }
                    case 4: {
                        this.bitrate = 56;
                        return;
                    }
                    case 5: {
                        this.bitrate = 64;
                        return;
                    }
                    case 6: {
                        this.bitrate = 80;
                        return;
                    }
                    case 7: {
                        this.bitrate = 96;
                        return;
                    }
                    case 8: {
                        this.bitrate = 112;
                        return;
                    }
                    case 9: {
                        this.bitrate = 128;
                        return;
                    }
                    case 10: {
                        this.bitrate = 160;
                        return;
                    }
                    case 11: {
                        this.bitrate = 192;
                        return;
                    }
                    case 12: {
                        this.bitrate = 224;
                        return;
                    }
                    case 13: {
                        this.bitrate = 256;
                        return;
                    }
                    case 14: {
                        this.bitrate = 320;
                        return;
                    }
                }
            }
        } else if (MPEG_VERSION_2_0.equals(this.version) || MPEG_VERSION_2_5.equals(this.version)) {
            if (this.layer == 1) {
                switch (n2) {
                    case 1: {
                        this.bitrate = 32;
                        return;
                    }
                    case 2: {
                        this.bitrate = 48;
                        return;
                    }
                    case 3: {
                        this.bitrate = 56;
                        return;
                    }
                    case 4: {
                        this.bitrate = 64;
                        return;
                    }
                    case 5: {
                        this.bitrate = 80;
                        return;
                    }
                    case 6: {
                        this.bitrate = 96;
                        return;
                    }
                    case 7: {
                        this.bitrate = 112;
                        return;
                    }
                    case 8: {
                        this.bitrate = 128;
                        return;
                    }
                    case 9: {
                        this.bitrate = 144;
                        return;
                    }
                    case 10: {
                        this.bitrate = 160;
                        return;
                    }
                    case 11: {
                        this.bitrate = 176;
                        return;
                    }
                    case 12: {
                        this.bitrate = 192;
                        return;
                    }
                    case 13: {
                        this.bitrate = 224;
                        return;
                    }
                    case 14: {
                        this.bitrate = 256;
                        return;
                    }
                }
            } else if (this.layer == 2 || this.layer == 3) {
                switch (n2) {
                    case 1: {
                        this.bitrate = 8;
                        return;
                    }
                    case 2: {
                        this.bitrate = 16;
                        return;
                    }
                    case 3: {
                        this.bitrate = 24;
                        return;
                    }
                    case 4: {
                        this.bitrate = 32;
                        return;
                    }
                    case 5: {
                        this.bitrate = 40;
                        return;
                    }
                    case 6: {
                        this.bitrate = 48;
                        return;
                    }
                    case 7: {
                        this.bitrate = 56;
                        return;
                    }
                    case 8: {
                        this.bitrate = 64;
                        return;
                    }
                    case 9: {
                        this.bitrate = 80;
                        return;
                    }
                    case 10: {
                        this.bitrate = 96;
                        return;
                    }
                    case 11: {
                        this.bitrate = 112;
                        return;
                    }
                    case 12: {
                        this.bitrate = 128;
                        return;
                    }
                    case 13: {
                        this.bitrate = 144;
                        return;
                    }
                    case 14: {
                        this.bitrate = 160;
                        return;
                    }
                }
            }
        }
        throw new InvalidDataException("Invalid bitrate in frame header");
    }

    private void setSampleRate(int n2) throws InvalidDataException {
        if (MPEG_VERSION_1_0.equals(this.version)) {
            switch (n2) {
                case 0: {
                    this.sampleRate = 44100;
                    return;
                }
                case 1: {
                    this.sampleRate = 48000;
                    return;
                }
                case 2: {
                    this.sampleRate = 32000;
                    return;
                }
            }
        } else if (MPEG_VERSION_2_0.equals(this.version)) {
            switch (n2) {
                case 0: {
                    this.sampleRate = 22050;
                    return;
                }
                case 1: {
                    this.sampleRate = 24000;
                    return;
                }
                case 2: {
                    this.sampleRate = 16000;
                    return;
                }
            }
        } else if (MPEG_VERSION_2_5.equals(this.version)) {
            switch (n2) {
                case 0: {
                    this.sampleRate = 11025;
                    return;
                }
                case 1: {
                    this.sampleRate = 12000;
                    return;
                }
                case 2: {
                    this.sampleRate = 8000;
                    return;
                }
            }
        }
        throw new InvalidDataException("Invalid sample rate in frame header");
    }

    private void setPadding(int n2) {
        this.padding = n2 == 1;
    }

    private void setPrivate(int n2) {
        this.privat = n2 == 1;
    }

    private void setChannelMode(int n2) throws InvalidDataException {
        switch (n2) {
            case 0: {
                this.channelMode = CHANNEL_MODE_STEREO;
                break;
            }
            case 1: {
                this.channelMode = CHANNEL_MODE_JOINT_STEREO;
                break;
            }
            case 2: {
                this.channelMode = CHANNEL_MODE_DUAL_MONO;
                break;
            }
            case 3: {
                this.channelMode = CHANNEL_MODE_MONO;
                break;
            }
            default: {
                throw new InvalidDataException("Invalid channel mode in frame header");
            }
        }
    }

    private void setModeExtension(int n2) throws InvalidDataException {
        if (CHANNEL_MODE_JOINT_STEREO.equals(this.channelMode)) {
            if (this.layer == 1 || this.layer == 2) {
                switch (n2) {
                    case 0: {
                        this.modeExtension = MODE_EXTENSION_BANDS_4_31;
                        return;
                    }
                    case 1: {
                        this.modeExtension = MODE_EXTENSION_BANDS_8_31;
                        return;
                    }
                    case 2: {
                        this.modeExtension = MODE_EXTENSION_BANDS_12_31;
                        return;
                    }
                    case 3: {
                        this.modeExtension = MODE_EXTENSION_BANDS_16_31;
                        return;
                    }
                }
            } else if (this.layer == 3) {
                switch (n2) {
                    case 0: {
                        this.modeExtension = "None";
                        return;
                    }
                    case 1: {
                        this.modeExtension = MODE_EXTENSION_INTENSITY_STEREO;
                        return;
                    }
                    case 2: {
                        this.modeExtension = MODE_EXTENSION_M_S_STEREO;
                        return;
                    }
                    case 3: {
                        this.modeExtension = MODE_EXTENSION_INTENSITY_M_S_STEREO;
                        return;
                    }
                }
            }
            throw new InvalidDataException("Invalid mode extension in frame header");
        }
        this.modeExtension = MODE_EXTENSION_NA;
    }

    private void setCopyright(int n2) {
        this.copyright = n2 == 1;
    }

    private void setOriginal(int n2) {
        this.original = n2 == 1;
    }

    private void setEmphasis(int n2) throws InvalidDataException {
        switch (n2) {
            case 0: {
                this.emphasis = "None";
                break;
            }
            case 1: {
                this.emphasis = EMPHASIS__50_15_MS;
                break;
            }
            case 3: {
                this.emphasis = EMPHASIS_CCITT_J_17;
                break;
            }
            default: {
                throw new InvalidDataException("Invalid emphasis in frame header");
            }
        }
    }

    public int getBitrate() {
        return this.bitrate;
    }

    public String getChannelMode() {
        return this.channelMode;
    }

    public boolean isCopyright() {
        return this.copyright;
    }

    public String getEmphasis() {
        return this.emphasis;
    }

    public String getLayer() {
        return MPEG_LAYERS[this.layer];
    }

    public String getModeExtension() {
        return this.modeExtension;
    }

    public boolean isOriginal() {
        return this.original;
    }

    public boolean hasPadding() {
        return this.padding;
    }

    public boolean isPrivate() {
        return this.privat;
    }

    public boolean isProtection() {
        return this.protection;
    }

    public int getSampleRate() {
        return this.sampleRate;
    }

    public String getVersion() {
        return this.version;
    }

    public int getLengthInBytes() {
        int n2 = this.padding ? 1 : 0;
        long l2 = this.layer == 1 ? (long)(48000 * this.bitrate / this.sampleRate + n2 * 4) : (long)(144000 * this.bitrate / this.sampleRate + n2);
        return (int)l2;
    }
}

