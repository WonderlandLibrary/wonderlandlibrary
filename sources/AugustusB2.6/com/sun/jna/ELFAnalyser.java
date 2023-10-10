// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.util.LinkedList;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.math.BigInteger;
import java.util.Map;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.io.RandomAccessFile;
import java.io.IOException;

class ELFAnalyser
{
    private static final byte[] ELF_MAGIC;
    private static final int EF_ARM_ABI_FLOAT_HARD = 1024;
    private static final int EF_ARM_ABI_FLOAT_SOFT = 512;
    private static final int EI_DATA_BIG_ENDIAN = 2;
    private static final int E_MACHINE_ARM = 40;
    private static final int EI_CLASS_64BIT = 2;
    private final String filename;
    private boolean ELF;
    private boolean _64Bit;
    private boolean bigEndian;
    private boolean armHardFloatFlag;
    private boolean armSoftFloatFlag;
    private boolean armEabiAapcsVfp;
    private boolean arm;
    
    public static ELFAnalyser analyse(final String filename) throws IOException {
        final ELFAnalyser res = new ELFAnalyser(filename);
        res.runDetection();
        return res;
    }
    
    public boolean isELF() {
        return this.ELF;
    }
    
    public boolean is64Bit() {
        return this._64Bit;
    }
    
    public boolean isBigEndian() {
        return this.bigEndian;
    }
    
    public String getFilename() {
        return this.filename;
    }
    
    public boolean isArmHardFloat() {
        return this.isArmEabiAapcsVfp() || this.isArmHardFloatFlag();
    }
    
    public boolean isArmEabiAapcsVfp() {
        return this.armEabiAapcsVfp;
    }
    
    public boolean isArmHardFloatFlag() {
        return this.armHardFloatFlag;
    }
    
    public boolean isArmSoftFloatFlag() {
        return this.armSoftFloatFlag;
    }
    
    public boolean isArm() {
        return this.arm;
    }
    
    private ELFAnalyser(final String filename) {
        this.ELF = false;
        this._64Bit = false;
        this.bigEndian = false;
        this.armHardFloatFlag = false;
        this.armSoftFloatFlag = false;
        this.armEabiAapcsVfp = false;
        this.arm = false;
        this.filename = filename;
    }
    
    private void runDetection() throws IOException {
        final RandomAccessFile raf = new RandomAccessFile(this.filename, "r");
        try {
            if (raf.length() > 4L) {
                final byte[] magic = new byte[4];
                raf.seek(0L);
                raf.read(magic);
                if (Arrays.equals(magic, ELFAnalyser.ELF_MAGIC)) {
                    this.ELF = true;
                }
            }
            if (!this.ELF) {
                return;
            }
            raf.seek(4L);
            final byte sizeIndicator = raf.readByte();
            final byte endianessIndicator = raf.readByte();
            this._64Bit = (sizeIndicator == 2);
            this.bigEndian = (endianessIndicator == 2);
            raf.seek(0L);
            final ByteBuffer headerData = ByteBuffer.allocate(this._64Bit ? 64 : 52);
            raf.getChannel().read(headerData, 0L);
            headerData.order(this.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
            this.arm = (headerData.get(18) == 40);
            if (this.arm) {
                final int flags = headerData.getInt(this._64Bit ? 48 : 36);
                this.armHardFloatFlag = ((flags & 0x400) == 0x400);
                this.armSoftFloatFlag = ((flags & 0x200) == 0x200);
                this.parseEabiAapcsVfp(headerData, raf);
            }
        }
        finally {
            try {
                raf.close();
            }
            catch (IOException ex) {}
        }
    }
    
    private void parseEabiAapcsVfp(final ByteBuffer headerData, final RandomAccessFile raf) throws IOException {
        final ELFSectionHeaders sectionHeaders = new ELFSectionHeaders(this._64Bit, this.bigEndian, headerData, raf);
        for (final ELFSectionHeaderEntry eshe : sectionHeaders.getEntries()) {
            if (".ARM.attributes".equals(eshe.getName())) {
                final ByteBuffer armAttributesBuffer = ByteBuffer.allocate(eshe.getSize());
                armAttributesBuffer.order(this.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
                raf.getChannel().read(armAttributesBuffer, eshe.getOffset());
                armAttributesBuffer.rewind();
                final Map<Integer, Map<ArmAeabiAttributesTag, Object>> armAttributes = parseArmAttributes(armAttributesBuffer);
                final Map<ArmAeabiAttributesTag, Object> fileAttributes = armAttributes.get(1);
                if (fileAttributes == null) {
                    continue;
                }
                final Object abiVFPargValue = fileAttributes.get(ArmAeabiAttributesTag.ABI_VFP_args);
                if (abiVFPargValue instanceof Integer && ((Integer)abiVFPargValue).equals(1)) {
                    this.armEabiAapcsVfp = true;
                }
                else {
                    if (!(abiVFPargValue instanceof BigInteger) || ((BigInteger)abiVFPargValue).intValue() != 1) {
                        continue;
                    }
                    this.armEabiAapcsVfp = true;
                }
            }
        }
    }
    
    private static Map<Integer, Map<ArmAeabiAttributesTag, Object>> parseArmAttributes(final ByteBuffer bb) {
        final byte format = bb.get();
        if (format != 65) {
            return (Map<Integer, Map<ArmAeabiAttributesTag, Object>>)Collections.EMPTY_MAP;
        }
        while (bb.position() < bb.limit()) {
            final int posSectionStart = bb.position();
            final int sectionLength = bb.getInt();
            if (sectionLength <= 0) {
                break;
            }
            final String vendorName = readNTBS(bb, null);
            if ("aeabi".equals(vendorName)) {
                return parseAEABI(bb);
            }
            bb.position(posSectionStart + sectionLength);
        }
        return (Map<Integer, Map<ArmAeabiAttributesTag, Object>>)Collections.EMPTY_MAP;
    }
    
    private static Map<Integer, Map<ArmAeabiAttributesTag, Object>> parseAEABI(final ByteBuffer buffer) {
        final Map<Integer, Map<ArmAeabiAttributesTag, Object>> data = new HashMap<Integer, Map<ArmAeabiAttributesTag, Object>>();
        while (buffer.position() < buffer.limit()) {
            final int pos = buffer.position();
            final int subsectionTag = readULEB128(buffer).intValue();
            final int length = buffer.getInt();
            if (subsectionTag == 1) {
                data.put(subsectionTag, parseFileAttribute(buffer));
            }
            buffer.position(pos + length);
        }
        return data;
    }
    
    private static Map<ArmAeabiAttributesTag, Object> parseFileAttribute(final ByteBuffer bb) {
        final Map<ArmAeabiAttributesTag, Object> result = new HashMap<ArmAeabiAttributesTag, Object>();
        while (bb.position() < bb.limit()) {
            final int tagValue = readULEB128(bb).intValue();
            final ArmAeabiAttributesTag tag = ArmAeabiAttributesTag.getByValue(tagValue);
            switch (tag.getParameterType()) {
                case UINT32: {
                    result.put(tag, bb.getInt());
                    continue;
                }
                case NTBS: {
                    result.put(tag, readNTBS(bb, null));
                    continue;
                }
                case ULEB128: {
                    result.put(tag, readULEB128(bb));
                    continue;
                }
            }
        }
        return result;
    }
    
    private static String readNTBS(final ByteBuffer buffer, final Integer position) {
        if (position != null) {
            buffer.position(position);
        }
        final int startingPos = buffer.position();
        byte currentByte;
        do {
            currentByte = buffer.get();
        } while (currentByte != 0 && buffer.position() <= buffer.limit());
        final int terminatingPosition = buffer.position();
        final byte[] data = new byte[terminatingPosition - startingPos - 1];
        buffer.position(startingPos);
        buffer.get(data);
        buffer.position(buffer.position() + 1);
        try {
            return new String(data, "ASCII");
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private static BigInteger readULEB128(final ByteBuffer buffer) {
        BigInteger result = BigInteger.ZERO;
        int shift = 0;
        while (true) {
            final byte b = buffer.get();
            result = result.or(BigInteger.valueOf(b & 0x7F).shiftLeft(shift));
            if ((b & 0x80) == 0x0) {
                break;
            }
            shift += 7;
        }
        return result;
    }
    
    static {
        ELF_MAGIC = new byte[] { 127, 69, 76, 70 };
    }
    
    static class ELFSectionHeaders
    {
        private final List<ELFSectionHeaderEntry> entries;
        
        public ELFSectionHeaders(final boolean _64bit, final boolean bigEndian, final ByteBuffer headerData, final RandomAccessFile raf) throws IOException {
            this.entries = new ArrayList<ELFSectionHeaderEntry>();
            long shoff;
            int shentsize;
            int shnum;
            short shstrndx;
            if (_64bit) {
                shoff = headerData.getLong(40);
                shentsize = headerData.getShort(58);
                shnum = headerData.getShort(60);
                shstrndx = headerData.getShort(62);
            }
            else {
                shoff = headerData.getInt(32);
                shentsize = headerData.getShort(46);
                shnum = headerData.getShort(48);
                shstrndx = headerData.getShort(50);
            }
            final int tableLength = shnum * shentsize;
            final ByteBuffer data = ByteBuffer.allocate(tableLength);
            data.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
            raf.getChannel().read(data, shoff);
            for (int i = 0; i < shnum; ++i) {
                data.position(i * shentsize);
                final ByteBuffer header = data.slice();
                header.order(data.order());
                header.limit(shentsize);
                this.entries.add(new ELFSectionHeaderEntry(_64bit, header));
            }
            final ELFSectionHeaderEntry stringTable = this.entries.get(shstrndx);
            final ByteBuffer stringBuffer = ByteBuffer.allocate(stringTable.getSize());
            stringBuffer.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
            raf.getChannel().read(stringBuffer, stringTable.getOffset());
            stringBuffer.rewind();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(20);
            for (final ELFSectionHeaderEntry eshe : this.entries) {
                baos.reset();
                stringBuffer.position(eshe.getNameOffset());
                while (stringBuffer.position() < stringBuffer.limit()) {
                    final byte b = stringBuffer.get();
                    if (b == 0) {
                        break;
                    }
                    baos.write(b);
                }
                eshe.setName(baos.toString("ASCII"));
            }
        }
        
        public List<ELFSectionHeaderEntry> getEntries() {
            return this.entries;
        }
    }
    
    static class ELFSectionHeaderEntry
    {
        private final int nameOffset;
        private String name;
        private final int type;
        private final int flags;
        private final int offset;
        private final int size;
        
        public ELFSectionHeaderEntry(final boolean _64bit, final ByteBuffer sectionHeaderData) {
            this.nameOffset = sectionHeaderData.getInt(0);
            this.type = sectionHeaderData.getInt(4);
            this.flags = (int)(_64bit ? sectionHeaderData.getLong(8) : sectionHeaderData.getInt(8));
            this.offset = (int)(_64bit ? sectionHeaderData.getLong(24) : sectionHeaderData.getInt(16));
            this.size = (int)(_64bit ? sectionHeaderData.getLong(32) : sectionHeaderData.getInt(20));
        }
        
        public String getName() {
            return this.name;
        }
        
        public void setName(final String name) {
            this.name = name;
        }
        
        public int getNameOffset() {
            return this.nameOffset;
        }
        
        public int getType() {
            return this.type;
        }
        
        public int getFlags() {
            return this.flags;
        }
        
        public int getOffset() {
            return this.offset;
        }
        
        public int getSize() {
            return this.size;
        }
        
        @Override
        public String toString() {
            return "ELFSectionHeaderEntry{nameIdx=" + this.nameOffset + ", name=" + this.name + ", type=" + this.type + ", flags=" + this.flags + ", offset=" + this.offset + ", size=" + this.size + '}';
        }
    }
    
    static class ArmAeabiAttributesTag
    {
        private final int value;
        private final String name;
        private final ParameterType parameterType;
        private static final List<ArmAeabiAttributesTag> tags;
        private static final Map<Integer, ArmAeabiAttributesTag> valueMap;
        private static final Map<String, ArmAeabiAttributesTag> nameMap;
        public static final ArmAeabiAttributesTag File;
        public static final ArmAeabiAttributesTag Section;
        public static final ArmAeabiAttributesTag Symbol;
        public static final ArmAeabiAttributesTag CPU_raw_name;
        public static final ArmAeabiAttributesTag CPU_name;
        public static final ArmAeabiAttributesTag CPU_arch;
        public static final ArmAeabiAttributesTag CPU_arch_profile;
        public static final ArmAeabiAttributesTag ARM_ISA_use;
        public static final ArmAeabiAttributesTag THUMB_ISA_use;
        public static final ArmAeabiAttributesTag FP_arch;
        public static final ArmAeabiAttributesTag WMMX_arch;
        public static final ArmAeabiAttributesTag Advanced_SIMD_arch;
        public static final ArmAeabiAttributesTag PCS_config;
        public static final ArmAeabiAttributesTag ABI_PCS_R9_use;
        public static final ArmAeabiAttributesTag ABI_PCS_RW_data;
        public static final ArmAeabiAttributesTag ABI_PCS_RO_data;
        public static final ArmAeabiAttributesTag ABI_PCS_GOT_use;
        public static final ArmAeabiAttributesTag ABI_PCS_wchar_t;
        public static final ArmAeabiAttributesTag ABI_FP_rounding;
        public static final ArmAeabiAttributesTag ABI_FP_denormal;
        public static final ArmAeabiAttributesTag ABI_FP_exceptions;
        public static final ArmAeabiAttributesTag ABI_FP_user_exceptions;
        public static final ArmAeabiAttributesTag ABI_FP_number_model;
        public static final ArmAeabiAttributesTag ABI_align_needed;
        public static final ArmAeabiAttributesTag ABI_align8_preserved;
        public static final ArmAeabiAttributesTag ABI_enum_size;
        public static final ArmAeabiAttributesTag ABI_HardFP_use;
        public static final ArmAeabiAttributesTag ABI_VFP_args;
        public static final ArmAeabiAttributesTag ABI_WMMX_args;
        public static final ArmAeabiAttributesTag ABI_optimization_goals;
        public static final ArmAeabiAttributesTag ABI_FP_optimization_goals;
        public static final ArmAeabiAttributesTag compatibility;
        public static final ArmAeabiAttributesTag CPU_unaligned_access;
        public static final ArmAeabiAttributesTag FP_HP_extension;
        public static final ArmAeabiAttributesTag ABI_FP_16bit_format;
        public static final ArmAeabiAttributesTag MPextension_use;
        public static final ArmAeabiAttributesTag DIV_use;
        public static final ArmAeabiAttributesTag nodefaults;
        public static final ArmAeabiAttributesTag also_compatible_with;
        public static final ArmAeabiAttributesTag conformance;
        public static final ArmAeabiAttributesTag T2EE_use;
        public static final ArmAeabiAttributesTag Virtualization_use;
        public static final ArmAeabiAttributesTag MPextension_use2;
        
        public ArmAeabiAttributesTag(final int value, final String name, final ParameterType parameterType) {
            this.value = value;
            this.name = name;
            this.parameterType = parameterType;
        }
        
        public int getValue() {
            return this.value;
        }
        
        public String getName() {
            return this.name;
        }
        
        public ParameterType getParameterType() {
            return this.parameterType;
        }
        
        @Override
        public String toString() {
            return this.name + " (" + this.value + ")";
        }
        
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + this.value;
            return hash;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final ArmAeabiAttributesTag other = (ArmAeabiAttributesTag)obj;
            return this.value == other.value;
        }
        
        private static ArmAeabiAttributesTag addTag(final int value, final String name, final ParameterType type) {
            final ArmAeabiAttributesTag tag = new ArmAeabiAttributesTag(value, name, type);
            if (!ArmAeabiAttributesTag.valueMap.containsKey(tag.getValue())) {
                ArmAeabiAttributesTag.valueMap.put(tag.getValue(), tag);
            }
            if (!ArmAeabiAttributesTag.nameMap.containsKey(tag.getName())) {
                ArmAeabiAttributesTag.nameMap.put(tag.getName(), tag);
            }
            ArmAeabiAttributesTag.tags.add(tag);
            return tag;
        }
        
        public static List<ArmAeabiAttributesTag> getTags() {
            return Collections.unmodifiableList((List<? extends ArmAeabiAttributesTag>)ArmAeabiAttributesTag.tags);
        }
        
        public static ArmAeabiAttributesTag getByName(final String name) {
            return ArmAeabiAttributesTag.nameMap.get(name);
        }
        
        public static ArmAeabiAttributesTag getByValue(final int value) {
            if (ArmAeabiAttributesTag.valueMap.containsKey(value)) {
                return ArmAeabiAttributesTag.valueMap.get(value);
            }
            final ArmAeabiAttributesTag pseudoTag = new ArmAeabiAttributesTag(value, "Unknown " + value, getParameterType(value));
            return pseudoTag;
        }
        
        private static ParameterType getParameterType(final int value) {
            final ArmAeabiAttributesTag tag = getByValue(value);
            if (tag != null) {
                return tag.getParameterType();
            }
            if (value % 2 == 0) {
                return ParameterType.ULEB128;
            }
            return ParameterType.NTBS;
        }
        
        static {
            tags = new LinkedList<ArmAeabiAttributesTag>();
            valueMap = new HashMap<Integer, ArmAeabiAttributesTag>();
            nameMap = new HashMap<String, ArmAeabiAttributesTag>();
            File = addTag(1, "File", ParameterType.UINT32);
            Section = addTag(2, "Section", ParameterType.UINT32);
            Symbol = addTag(3, "Symbol", ParameterType.UINT32);
            CPU_raw_name = addTag(4, "CPU_raw_name", ParameterType.NTBS);
            CPU_name = addTag(5, "CPU_name", ParameterType.NTBS);
            CPU_arch = addTag(6, "CPU_arch", ParameterType.ULEB128);
            CPU_arch_profile = addTag(7, "CPU_arch_profile", ParameterType.ULEB128);
            ARM_ISA_use = addTag(8, "ARM_ISA_use", ParameterType.ULEB128);
            THUMB_ISA_use = addTag(9, "THUMB_ISA_use", ParameterType.ULEB128);
            FP_arch = addTag(10, "FP_arch", ParameterType.ULEB128);
            WMMX_arch = addTag(11, "WMMX_arch", ParameterType.ULEB128);
            Advanced_SIMD_arch = addTag(12, "Advanced_SIMD_arch", ParameterType.ULEB128);
            PCS_config = addTag(13, "PCS_config", ParameterType.ULEB128);
            ABI_PCS_R9_use = addTag(14, "ABI_PCS_R9_use", ParameterType.ULEB128);
            ABI_PCS_RW_data = addTag(15, "ABI_PCS_RW_data", ParameterType.ULEB128);
            ABI_PCS_RO_data = addTag(16, "ABI_PCS_RO_data", ParameterType.ULEB128);
            ABI_PCS_GOT_use = addTag(17, "ABI_PCS_GOT_use", ParameterType.ULEB128);
            ABI_PCS_wchar_t = addTag(18, "ABI_PCS_wchar_t", ParameterType.ULEB128);
            ABI_FP_rounding = addTag(19, "ABI_FP_rounding", ParameterType.ULEB128);
            ABI_FP_denormal = addTag(20, "ABI_FP_denormal", ParameterType.ULEB128);
            ABI_FP_exceptions = addTag(21, "ABI_FP_exceptions", ParameterType.ULEB128);
            ABI_FP_user_exceptions = addTag(22, "ABI_FP_user_exceptions", ParameterType.ULEB128);
            ABI_FP_number_model = addTag(23, "ABI_FP_number_model", ParameterType.ULEB128);
            ABI_align_needed = addTag(24, "ABI_align_needed", ParameterType.ULEB128);
            ABI_align8_preserved = addTag(25, "ABI_align8_preserved", ParameterType.ULEB128);
            ABI_enum_size = addTag(26, "ABI_enum_size", ParameterType.ULEB128);
            ABI_HardFP_use = addTag(27, "ABI_HardFP_use", ParameterType.ULEB128);
            ABI_VFP_args = addTag(28, "ABI_VFP_args", ParameterType.ULEB128);
            ABI_WMMX_args = addTag(29, "ABI_WMMX_args", ParameterType.ULEB128);
            ABI_optimization_goals = addTag(30, "ABI_optimization_goals", ParameterType.ULEB128);
            ABI_FP_optimization_goals = addTag(31, "ABI_FP_optimization_goals", ParameterType.ULEB128);
            compatibility = addTag(32, "compatibility", ParameterType.NTBS);
            CPU_unaligned_access = addTag(34, "CPU_unaligned_access", ParameterType.ULEB128);
            FP_HP_extension = addTag(36, "FP_HP_extension", ParameterType.ULEB128);
            ABI_FP_16bit_format = addTag(38, "ABI_FP_16bit_format", ParameterType.ULEB128);
            MPextension_use = addTag(42, "MPextension_use", ParameterType.ULEB128);
            DIV_use = addTag(44, "DIV_use", ParameterType.ULEB128);
            nodefaults = addTag(64, "nodefaults", ParameterType.ULEB128);
            also_compatible_with = addTag(65, "also_compatible_with", ParameterType.NTBS);
            conformance = addTag(67, "conformance", ParameterType.NTBS);
            T2EE_use = addTag(66, "T2EE_use", ParameterType.ULEB128);
            Virtualization_use = addTag(68, "Virtualization_use", ParameterType.ULEB128);
            MPextension_use2 = addTag(70, "MPextension_use", ParameterType.ULEB128);
        }
        
        public enum ParameterType
        {
            UINT32, 
            NTBS, 
            ULEB128;
        }
    }
}
