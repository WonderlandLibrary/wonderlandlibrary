/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

public final class PixelFormat
extends Enum<PixelFormat> {
    public static final /* enum */ PixelFormat INT_ARGB_PRE = new PixelFormat(DataType.INT, 1, true, false);
    public static final /* enum */ PixelFormat BYTE_BGRA_PRE = new PixelFormat(DataType.BYTE, 4, true, false);
    public static final /* enum */ PixelFormat BYTE_RGB = new PixelFormat(DataType.BYTE, 3, true, true);
    public static final /* enum */ PixelFormat BYTE_GRAY = new PixelFormat(DataType.BYTE, 1, true, true);
    public static final /* enum */ PixelFormat BYTE_ALPHA = new PixelFormat(DataType.BYTE, 1, false, false);
    public static final /* enum */ PixelFormat MULTI_YCbCr_420 = new PixelFormat(DataType.BYTE, 1, false, true);
    public static final /* enum */ PixelFormat BYTE_APPLE_422 = new PixelFormat(DataType.BYTE, 2, false, true);
    public static final /* enum */ PixelFormat FLOAT_XYZW = new PixelFormat(DataType.FLOAT, 4, false, true);
    public static final int YCBCR_PLANE_LUMA = 0;
    public static final int YCBCR_PLANE_CHROMARED = 1;
    public static final int YCBCR_PLANE_CHROMABLUE = 2;
    public static final int YCBCR_PLANE_ALPHA = 3;
    private DataType dataType;
    private int elemsPerPixelUnit;
    private boolean rgb;
    private boolean opaque;
    private static final /* synthetic */ PixelFormat[] $VALUES;

    public static PixelFormat[] values() {
        return (PixelFormat[])$VALUES.clone();
    }

    public static PixelFormat valueOf(String string) {
        return Enum.valueOf(PixelFormat.class, string);
    }

    private PixelFormat(DataType dataType, int n2, boolean bl, boolean bl2) {
        this.dataType = dataType;
        this.elemsPerPixelUnit = n2;
        this.rgb = bl;
        this.opaque = bl2;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public int getBytesPerPixelUnit() {
        return this.elemsPerPixelUnit * this.dataType.getSizeInBytes();
    }

    public int getElemsPerPixelUnit() {
        return this.elemsPerPixelUnit;
    }

    public boolean isRGB() {
        return this.rgb;
    }

    public boolean isOpaque() {
        return this.opaque;
    }

    private static /* synthetic */ PixelFormat[] $values() {
        return new PixelFormat[]{INT_ARGB_PRE, BYTE_BGRA_PRE, BYTE_RGB, BYTE_GRAY, BYTE_ALPHA, MULTI_YCbCr_420, BYTE_APPLE_422, FLOAT_XYZW};
    }

    static {
        $VALUES = PixelFormat.$values();
    }

    public static final class DataType
    extends Enum<DataType> {
        public static final /* enum */ DataType BYTE = new DataType(1);
        public static final /* enum */ DataType INT = new DataType(4);
        public static final /* enum */ DataType FLOAT = new DataType(4);
        private int sizeInBytes;
        private static final /* synthetic */ DataType[] $VALUES;

        public static DataType[] values() {
            return (DataType[])$VALUES.clone();
        }

        public static DataType valueOf(String string) {
            return Enum.valueOf(DataType.class, string);
        }

        private DataType(int n2) {
            this.sizeInBytes = n2;
        }

        public int getSizeInBytes() {
            return this.sizeInBytes;
        }

        private static /* synthetic */ DataType[] $values() {
            return new DataType[]{BYTE, INT, FLOAT};
        }

        static {
            $VALUES = DataType.$values();
        }
    }
}

