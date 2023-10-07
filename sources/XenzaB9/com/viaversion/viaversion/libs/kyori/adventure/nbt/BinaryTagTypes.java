// 
// Decompiled by Procyon v0.6.0
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
        BYTE_ARRAY = BinaryTagType.register(ByteArrayBinaryTag.class, (byte)7, input -> {
            final int length = input.readInt();
            try (final BinaryTagScope ignored = TrackingDataInput.enter(input, length)) {
                final byte[] value = new byte[length];
                input.readFully(value);
                ByteArrayBinaryTag.of(value);
                return;
            }
        }, (tag, output) -> {
            final byte[] value2 = ByteArrayBinaryTagImpl.value(tag);
            output.writeInt(value2.length);
            output.write(value2);
            return;
        });
        STRING = BinaryTagType.register(StringBinaryTag.class, (byte)8, input -> StringBinaryTag.of(input.readUTF()), (tag, output) -> output.writeUTF(tag.value()));
        LIST = BinaryTagType.register(ListBinaryTag.class, (byte)9, input -> {
            final BinaryTagType<? extends BinaryTag> type = BinaryTagType.of(input.readByte());
            final int length2 = input.readInt();
            try (final BinaryTagScope ignored2 = TrackingDataInput.enter(input, length2 * 8L)) {
                final ArrayList tags = new ArrayList<BinaryTag>(length2);
                for (int i = 0; i < length2; ++i) {
                    tags.add((BinaryTag)type.read(input));
                }
                ListBinaryTag.of(type, (List<BinaryTag>)tags);
                return;
            }
        }, (tag, output) -> {
            output.writeByte(tag.elementType().id());
            final int size = tag.size();
            output.writeInt(size);
            tag.iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final BinaryTag item = iterator.next();
                BinaryTagType.write(item.type(), item, output);
            }
            return;
        });
        COMPOUND = BinaryTagType.register(CompoundBinaryTag.class, (byte)10, input -> {
            try (final BinaryTagScope ignored3 = TrackingDataInput.enter(input)) {
                final HashMap<String, BinaryTag> tags2 = new HashMap<String, BinaryTag>();
                while (true) {
                    final BinaryTagType<? extends BinaryTag> type2 = BinaryTagType.of(input.readByte());
                    final Object o;
                    if (o != BinaryTagTypes.END) {
                        final String key = input.readUTF();
                        final BinaryTag tag2 = (BinaryTag)type2.read(input);
                        tags2.put(key, tag2);
                    }
                    else {
                        break;
                    }
                }
                return new CompoundBinaryTagImpl(tags2);
            }
        }, (tag, output) -> {
            tag.iterator();
            final Iterator iterator2;
            while (iterator2.hasNext()) {
                final Map.Entry<String, ? extends BinaryTag> entry = iterator2.next();
                final BinaryTag value3 = (BinaryTag)entry.getValue();
                if (value3 != null) {
                    final BinaryTagType<? extends BinaryTag> type3 = value3.type();
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
        INT_ARRAY = BinaryTagType.register(IntArrayBinaryTag.class, (byte)11, input -> {
            final int length3 = input.readInt();
            try (final BinaryTagScope ignored4 = TrackingDataInput.enter(input, length3 * 4L)) {
                final int[] value4 = new int[length3];
                for (int j = 0; j < length3; ++j) {
                    value4[j] = input.readInt();
                }
                IntArrayBinaryTag.of(value4);
                return;
            }
        }, (tag, output) -> {
            final int[] value5 = IntArrayBinaryTagImpl.value(tag);
            final int length4 = value5.length;
            output.writeInt(length4);
            for (int k = 0; k < length4; ++k) {
                output.writeInt(value5[k]);
            }
            return;
        });
        LONG_ARRAY = BinaryTagType.register(LongArrayBinaryTag.class, (byte)12, input -> {
            final int length5 = input.readInt();
            try (final BinaryTagScope ignored5 = TrackingDataInput.enter(input, length5 * 8L)) {
                final long[] value6 = new long[length5];
                for (int l = 0; l < length5; ++l) {
                    value6[l] = input.readLong();
                }
                LongArrayBinaryTag.of(value6);
                return;
            }
        }, (tag, output) -> {
            final long[] value7 = LongArrayBinaryTagImpl.value(tag);
            final int length6 = value7.length;
            output.writeInt(length6);
            for (int m = 0; m < length6; ++m) {
                output.writeLong(value7[m]);
            }
        });
    }
}
