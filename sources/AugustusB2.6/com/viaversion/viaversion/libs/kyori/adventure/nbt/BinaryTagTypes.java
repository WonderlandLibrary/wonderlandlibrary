// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

public final class BinaryTagTypes
{
    public static final BinaryTagType<EndBinaryTag> END;
    public static final BinaryTagType<ByteBinaryTag> BYTE;
    public static final BinaryTagType<ShortBinaryTag> SHORT;
    public static final BinaryTagType<IntBinaryTag> INT;
    public static final BinaryTagType<LongBinaryTag> LONG;
    public static final BinaryTagType<FloatBinaryTag> FLOAT;
    public static final BinaryTagType<DoubleBinaryTag> DOUBLE;
    public static final BinaryTagType<ByteArrayBinaryTag> BYTE_ARRAY;
    public static final BinaryTagType<StringBinaryTag> STRING;
    public static final BinaryTagType<ListBinaryTag> LIST;
    public static final BinaryTagType<CompoundBinaryTag> COMPOUND;
    public static final BinaryTagType<IntArrayBinaryTag> INT_ARRAY;
    public static final BinaryTagType<LongArrayBinaryTag> LONG_ARRAY;
    
    private BinaryTagTypes() {
    }
    
    static {
        END = BinaryTagType.register(EndBinaryTag.class, (byte)0, input -> EndBinaryTag.get(), null);
        BYTE = BinaryTagType.registerNumeric(ByteBinaryTag.class, (byte)1, input -> ByteBinaryTag.of(input.readByte()), (tag, output) -> output.writeByte(tag.value()));
        SHORT = BinaryTagType.registerNumeric(ShortBinaryTag.class, (byte)2, input -> ShortBinaryTag.of(input.readShort()), (tag, output) -> output.writeShort(tag.value()));
        INT = BinaryTagType.registerNumeric(IntBinaryTag.class, (byte)3, input -> IntBinaryTag.of(input.readInt()), (tag, output) -> output.writeInt(tag.value()));
        LONG = BinaryTagType.registerNumeric(LongBinaryTag.class, (byte)4, input -> LongBinaryTag.of(input.readLong()), (tag, output) -> output.writeLong(tag.value()));
        FLOAT = BinaryTagType.registerNumeric(FloatBinaryTag.class, (byte)5, input -> FloatBinaryTag.of(input.readFloat()), (tag, output) -> output.writeFloat(tag.value()));
        DOUBLE = BinaryTagType.registerNumeric(DoubleBinaryTag.class, (byte)6, input -> DoubleBinaryTag.of(input.readDouble()), (tag, output) -> output.writeDouble(tag.value()));
        final int length;
        final BinaryTagScope ignored;
        byte[] value;
        final ByteArrayBinaryTag byteArrayBinaryTag;
        final byte[] value2;
        BYTE_ARRAY = BinaryTagType.register(ByteArrayBinaryTag.class, (byte)7, input -> {
            length = input.readInt();
            ignored = TrackingDataInput.enter(input, length);
            try {
                value = new byte[length];
                input.readFully(value);
                ByteArrayBinaryTag.of(value);
                if (ignored != null) {
                    ignored.close();
                }
                return byteArrayBinaryTag;
            }
            catch (Throwable t) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }, (tag, output) -> {
            value2 = ByteArrayBinaryTagImpl.value(tag);
            output.writeInt(value2.length);
            output.write(value2);
            return;
        });
        STRING = BinaryTagType.register(StringBinaryTag.class, (byte)8, input -> StringBinaryTag.of(input.readUTF()), (tag, output) -> output.writeUTF(tag.value()));
        final BinaryTagType<? extends BinaryTag> type;
        final int length2;
        final BinaryTagScope ignored2;
        ArrayList tags;
        int i;
        final ListBinaryTag listBinaryTag;
        final int size;
        final Iterator<BinaryTag> iterator;
        BinaryTag item;
        LIST = BinaryTagType.register(ListBinaryTag.class, (byte)9, input -> {
            type = BinaryTagType.of(input.readByte());
            length2 = input.readInt();
            ignored2 = TrackingDataInput.enter(input, length2 * 8L);
            try {
                tags = new ArrayList<BinaryTag>(length2);
                for (i = 0; i < length2; ++i) {
                    tags.add((BinaryTag)type.read(input));
                }
                ListBinaryTag.of(type, (List<BinaryTag>)tags);
                if (ignored2 != null) {
                    ignored2.close();
                }
                return listBinaryTag;
            }
            catch (Throwable t2) {
                if (ignored2 != null) {
                    try {
                        ignored2.close();
                    }
                    catch (Throwable exception2) {
                        t2.addSuppressed(exception2);
                    }
                }
                throw t2;
            }
        }, (tag, output) -> {
            output.writeByte(tag.elementType().id());
            size = tag.size();
            output.writeInt(size);
            tag.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                BinaryTagType.write(item.type(), item, output);
            }
            return;
        });
        final BinaryTagScope ignored3;
        HashMap<String, BinaryTag> tags2;
        BinaryTagType<? extends BinaryTag> type2;
        final Object o;
        String key;
        BinaryTag tag2;
        CompoundBinaryTagImpl compoundBinaryTagImpl;
        final Iterator<Map.Entry<String, ? extends BinaryTag>> iterator2;
        Map.Entry<String, ? extends BinaryTag> entry;
        BinaryTag value3;
        BinaryTagType<? extends BinaryTag> type3;
        COMPOUND = BinaryTagType.register(CompoundBinaryTag.class, (byte)10, input -> {
            ignored3 = TrackingDataInput.enter(input);
            try {
                tags2 = new HashMap<String, BinaryTag>();
                while (true) {
                    type2 = BinaryTagType.of(input.readByte());
                    if (o != BinaryTagTypes.END) {
                        key = input.readUTF();
                        tag2 = (BinaryTag)type2.read(input);
                        tags2.put(key, tag2);
                    }
                    else {
                        break;
                    }
                }
                compoundBinaryTagImpl = new CompoundBinaryTagImpl(tags2);
                if (ignored3 != null) {
                    ignored3.close();
                }
                return compoundBinaryTagImpl;
            }
            catch (Throwable t3) {
                if (ignored3 != null) {
                    try {
                        ignored3.close();
                    }
                    catch (Throwable exception3) {
                        t3.addSuppressed(exception3);
                    }
                }
                throw t3;
            }
        }, (tag, output) -> {
            tag.iterator();
            while (iterator2.hasNext()) {
                entry = iterator2.next();
                value3 = (BinaryTag)entry.getValue();
                if (value3 != null) {
                    type3 = value3.type();
                    output.writeByte(type3.id());
                    if (type3 != BinaryTagTypes.END) {
                        output.writeUTF(entry.getKey());
                        BinaryTagType.write(type3, value3, output);
                    }
                    else {
                        continue;
                    }
                }
            }
            output.writeByte(BinaryTagTypes.END.id());
            return;
        });
        final int length3;
        final BinaryTagScope ignored4;
        int[] value4;
        int j;
        final IntArrayBinaryTag intArrayBinaryTag;
        final int[] value5;
        final int length4;
        int k;
        INT_ARRAY = BinaryTagType.register(IntArrayBinaryTag.class, (byte)11, input -> {
            length3 = input.readInt();
            ignored4 = TrackingDataInput.enter(input, length3 * 4L);
            try {
                value4 = new int[length3];
                for (j = 0; j < length3; ++j) {
                    value4[j] = input.readInt();
                }
                IntArrayBinaryTag.of(value4);
                if (ignored4 != null) {
                    ignored4.close();
                }
                return intArrayBinaryTag;
            }
            catch (Throwable t4) {
                if (ignored4 != null) {
                    try {
                        ignored4.close();
                    }
                    catch (Throwable exception4) {
                        t4.addSuppressed(exception4);
                    }
                }
                throw t4;
            }
        }, (tag, output) -> {
            value5 = IntArrayBinaryTagImpl.value(tag);
            length4 = value5.length;
            output.writeInt(length4);
            for (k = 0; k < length4; ++k) {
                output.writeInt(value5[k]);
            }
            return;
        });
        final int length5;
        final BinaryTagScope ignored5;
        long[] value6;
        int l;
        final LongArrayBinaryTag longArrayBinaryTag;
        final long[] value7;
        final int length6;
        int m;
        LONG_ARRAY = BinaryTagType.register(LongArrayBinaryTag.class, (byte)12, input -> {
            length5 = input.readInt();
            ignored5 = TrackingDataInput.enter(input, length5 * 8L);
            try {
                value6 = new long[length5];
                for (l = 0; l < length5; ++l) {
                    value6[l] = input.readLong();
                }
                LongArrayBinaryTag.of(value6);
                if (ignored5 != null) {
                    ignored5.close();
                }
                return longArrayBinaryTag;
            }
            catch (Throwable t5) {
                if (ignored5 != null) {
                    try {
                        ignored5.close();
                    }
                    catch (Throwable exception5) {
                        t5.addSuppressed(exception5);
                    }
                }
                throw t5;
            }
        }, (tag, output) -> {
            value7 = LongArrayBinaryTagImpl.value(tag);
            length6 = value7.length;
            output.writeInt(length6);
            for (m = 0; m < length6; ++m) {
                output.writeLong(value7[m]);
            }
        });
    }
}
