// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Union;
import com.sun.jna.Memory;
import com.sun.jna.StringArray;
import com.sun.jna.Pointer;
import com.sun.jna.win32.W32APITypeMapper;
import com.sun.jna.Structure;

public interface Winevt
{
    public static final int EVT_VARIANT_TYPE_ARRAY = 128;
    public static final int EVT_VARIANT_TYPE_MASK = 127;
    public static final int EVT_READ_ACCESS = 1;
    public static final int EVT_WRITE_ACCESS = 2;
    public static final int EVT_ALL_ACCESS = 7;
    public static final int EVT_CLEAR_ACCESS = 4;
    
    public enum EVT_VARIANT_TYPE
    {
        EvtVarTypeNull(""), 
        EvtVarTypeString("String"), 
        EvtVarTypeAnsiString("AnsiString"), 
        EvtVarTypeSByte("SByte"), 
        EvtVarTypeByte("Byte"), 
        EvtVarTypeInt16("Int16"), 
        EvtVarTypeUInt16("UInt16"), 
        EvtVarTypeInt32("Int32"), 
        EvtVarTypeUInt32("UInt32"), 
        EvtVarTypeInt64("Int64"), 
        EvtVarTypeUInt64("UInt64"), 
        EvtVarTypeSingle("Single"), 
        EvtVarTypeDouble("Double"), 
        EvtVarTypeBoolean("Boolean"), 
        EvtVarTypeBinary("Binary"), 
        EvtVarTypeGuid("Guid"), 
        EvtVarTypeSizeT("SizeT"), 
        EvtVarTypeFileTime("FileTime"), 
        EvtVarTypeSysTime("SysTime"), 
        EvtVarTypeSid("Sid"), 
        EvtVarTypeHexInt32("Int32"), 
        EvtVarTypeHexInt64("Int64"), 
        EvtVarTypeEvtHandle("EvtHandle"), 
        EvtVarTypeEvtXml("Xml");
        
        private final String field;
        
        private EVT_VARIANT_TYPE(final String field) {
            this.field = field;
        }
        
        public String getField() {
            return this.field.isEmpty() ? "" : (this.field + "Val");
        }
        
        public String getArrField() {
            return this.field.isEmpty() ? "" : (this.field + "Arr");
        }
    }
    
    @FieldOrder({ "field1", "Count", "Type" })
    public static class EVT_VARIANT extends Structure
    {
        public field1_union field1;
        public int Count;
        public int Type;
        private Object holder;
        
        public EVT_VARIANT() {
            super(W32APITypeMapper.DEFAULT);
        }
        
        public EVT_VARIANT(final Pointer peer) {
            super(peer, 0, W32APITypeMapper.DEFAULT);
        }
        
        public void use(final Pointer m) {
            this.useMemory(m, 0);
        }
        
        private int getBaseType() {
            return this.Type & 0x7F;
        }
        
        public boolean isArray() {
            return (this.Type & 0x80) == 0x80;
        }
        
        public EVT_VARIANT_TYPE getVariantType() {
            return EVT_VARIANT_TYPE.values()[this.getBaseType()];
        }
        
        public void setValue(final EVT_VARIANT_TYPE type, final Object value) {
            this.allocateMemory();
            if (type == null) {
                throw new IllegalArgumentException("setValue must not be called with type set to NULL");
            }
            this.holder = null;
            if (value == null || type == EVT_VARIANT_TYPE.EvtVarTypeNull) {
                this.Type = EVT_VARIANT_TYPE.EvtVarTypeNull.ordinal();
                this.Count = 0;
                this.field1.writeField("pointerValue", Pointer.NULL);
            }
            else {
                switch (type) {
                    case EvtVarTypeAnsiString: {
                        if (value.getClass().isArray() && value.getClass().getComponentType() == String.class) {
                            this.Type = (type.ordinal() | 0x80);
                            final StringArray sa = new StringArray((String[])value, false);
                            this.holder = sa;
                            this.Count = ((String[])value).length;
                            this.field1.writeField("pointerValue", sa);
                            break;
                        }
                        if (value.getClass() == String.class) {
                            this.Type = type.ordinal();
                            final Memory mem = new Memory(((String)value).length() + 1);
                            mem.setString(0L, (String)value);
                            this.holder = mem;
                            this.Count = 0;
                            this.field1.writeField("pointerValue", mem);
                            break;
                        }
                        throw new IllegalArgumentException(type.name() + " must be set from String/String[]");
                    }
                    case EvtVarTypeBoolean: {
                        if (value.getClass().isArray() && value.getClass().getComponentType() == WinDef.BOOL.class) {
                            this.Type = (type.ordinal() | 0x80);
                            final Memory mem = new Memory(((WinDef.BOOL[])value).length * 4);
                            for (int i = 0; i < ((WinDef.BOOL[])value).length; ++i) {
                                mem.setInt(i * 4, ((WinDef.BOOL[])value)[i].intValue());
                            }
                            this.holder = mem;
                            this.Count = 0;
                            this.field1.writeField("pointerValue", mem);
                            break;
                        }
                        if (value.getClass() == WinDef.BOOL.class) {
                            this.Type = type.ordinal();
                            this.Count = 0;
                            this.field1.writeField("intValue", ((WinDef.BOOL)value).intValue());
                            break;
                        }
                        throw new IllegalArgumentException(type.name() + " must be set from BOOL/BOOL[]");
                    }
                    case EvtVarTypeString:
                    case EvtVarTypeEvtXml: {
                        if (value.getClass().isArray() && value.getClass().getComponentType() == String.class) {
                            this.Type = (type.ordinal() | 0x80);
                            final StringArray sa = new StringArray((String[])value, true);
                            this.holder = sa;
                            this.Count = ((String[])value).length;
                            this.field1.writeField("pointerValue", sa);
                            break;
                        }
                        if (value.getClass() == String.class) {
                            this.Type = type.ordinal();
                            final Memory mem = new Memory((((String)value).length() + 1) * 2);
                            mem.setWideString(0L, (String)value);
                            this.holder = mem;
                            this.Count = 0;
                            this.field1.writeField("pointerValue", mem);
                            break;
                        }
                        throw new IllegalArgumentException(type.name() + " must be set from String/String[]");
                    }
                    case EvtVarTypeSByte:
                    case EvtVarTypeByte: {
                        if (value.getClass().isArray() && value.getClass().getComponentType() == Byte.TYPE) {
                            this.Type = (type.ordinal() | 0x80);
                            final Memory mem = new Memory(((byte[])value).length * 1);
                            mem.write(0L, (byte[])value, 0, ((byte[])value).length);
                            this.holder = mem;
                            this.Count = 0;
                            this.field1.writeField("pointerValue", mem);
                            break;
                        }
                        if (value.getClass() == Byte.TYPE) {
                            this.Type = type.ordinal();
                            this.Count = 0;
                            this.field1.writeField("byteValue", value);
                            break;
                        }
                        throw new IllegalArgumentException(type.name() + " must be set from byte/byte[]");
                    }
                    case EvtVarTypeInt16:
                    case EvtVarTypeUInt16: {
                        if (value.getClass().isArray() && value.getClass().getComponentType() == Short.TYPE) {
                            this.Type = (type.ordinal() | 0x80);
                            final Memory mem = new Memory(((short[])value).length * 2);
                            mem.write(0L, (short[])value, 0, ((short[])value).length);
                            this.holder = mem;
                            this.Count = 0;
                            this.field1.writeField("pointerValue", mem);
                            break;
                        }
                        if (value.getClass() == Short.TYPE) {
                            this.Type = type.ordinal();
                            this.Count = 0;
                            this.field1.writeField("shortValue", value);
                            break;
                        }
                        throw new IllegalArgumentException(type.name() + " must be set from short/short[]");
                    }
                    case EvtVarTypeHexInt32:
                    case EvtVarTypeInt32:
                    case EvtVarTypeUInt32: {
                        if (value.getClass().isArray() && value.getClass().getComponentType() == Integer.TYPE) {
                            this.Type = (type.ordinal() | 0x80);
                            final Memory mem = new Memory(((int[])value).length * 4);
                            mem.write(0L, (int[])value, 0, ((int[])value).length);
                            this.holder = mem;
                            this.Count = 0;
                            this.field1.writeField("pointerValue", mem);
                            break;
                        }
                        if (value.getClass() == Integer.TYPE) {
                            this.Type = type.ordinal();
                            this.Count = 0;
                            this.field1.writeField("intValue", value);
                            break;
                        }
                        throw new IllegalArgumentException(type.name() + " must be set from int/int[]");
                    }
                    case EvtVarTypeHexInt64:
                    case EvtVarTypeInt64:
                    case EvtVarTypeUInt64: {
                        if (value.getClass().isArray() && value.getClass().getComponentType() == Long.TYPE) {
                            this.Type = (type.ordinal() | 0x80);
                            final Memory mem = new Memory(((long[])value).length * 4);
                            mem.write(0L, (long[])value, 0, ((long[])value).length);
                            this.holder = mem;
                            this.Count = 0;
                            this.field1.writeField("pointerValue", mem);
                            break;
                        }
                        if (value.getClass() == Long.TYPE) {
                            this.Type = type.ordinal();
                            this.Count = 0;
                            this.field1.writeField("longValue", value);
                            break;
                        }
                        throw new IllegalArgumentException(type.name() + " must be set from long/long[]");
                    }
                    case EvtVarTypeSingle: {
                        if (value.getClass().isArray() && value.getClass().getComponentType() == Float.TYPE) {
                            this.Type = (type.ordinal() | 0x80);
                            final Memory mem = new Memory(((float[])value).length * 4);
                            mem.write(0L, (float[])value, 0, ((float[])value).length);
                            this.holder = mem;
                            this.Count = 0;
                            this.field1.writeField("pointerValue", mem);
                            break;
                        }
                        if (value.getClass() == Float.TYPE) {
                            this.Type = type.ordinal();
                            this.Count = 0;
                            this.field1.writeField("floatValue", value);
                            break;
                        }
                        throw new IllegalArgumentException(type.name() + " must be set from float/float[]");
                    }
                    case EvtVarTypeDouble: {
                        if (value.getClass().isArray() && value.getClass().getComponentType() == Double.TYPE) {
                            this.Type = (type.ordinal() | 0x80);
                            final Memory mem = new Memory(((double[])value).length * 4);
                            mem.write(0L, (double[])value, 0, ((double[])value).length);
                            this.holder = mem;
                            this.Count = 0;
                            this.field1.writeField("pointerValue", mem);
                            break;
                        }
                        if (value.getClass() == Double.TYPE) {
                            this.Type = type.ordinal();
                            this.Count = 0;
                            this.field1.writeField("doubleVal", value);
                            break;
                        }
                        throw new IllegalArgumentException(type.name() + " must be set from double/double[]");
                    }
                    case EvtVarTypeBinary: {
                        if (value.getClass().isArray() && value.getClass().getComponentType() == Byte.TYPE) {
                            this.Type = type.ordinal();
                            final Memory mem = new Memory(((byte[])value).length * 1);
                            mem.write(0L, (byte[])value, 0, ((byte[])value).length);
                            this.holder = mem;
                            this.Count = 0;
                            this.field1.writeField("pointerValue", mem);
                            break;
                        }
                        throw new IllegalArgumentException(type.name() + " must be set from byte[]");
                    }
                    default: {
                        throw new IllegalStateException(String.format("NOT IMPLEMENTED: getValue(%s) (Array: %b, Count: %d)", type, this.isArray(), this.Count));
                    }
                }
            }
            this.write();
        }
        
        public Object getValue() {
            final EVT_VARIANT_TYPE type = this.getVariantType();
            switch (type) {
                case EvtVarTypeAnsiString: {
                    return this.isArray() ? this.field1.getPointer().getPointer(0L).getStringArray(0L, this.Count) : this.field1.getPointer().getPointer(0L).getString(0L);
                }
                case EvtVarTypeBoolean: {
                    if (this.isArray()) {
                        final int[] rawValue = this.field1.getPointer().getPointer(0L).getIntArray(0L, this.Count);
                        final WinDef.BOOL[] result = new WinDef.BOOL[rawValue.length];
                        for (int i = 0; i < result.length; ++i) {
                            result[i] = new WinDef.BOOL((long)rawValue[i]);
                        }
                        return result;
                    }
                    return new WinDef.BOOL((long)this.field1.getPointer().getInt(0L));
                }
                case EvtVarTypeString:
                case EvtVarTypeEvtXml: {
                    return this.isArray() ? this.field1.getPointer().getPointer(0L).getWideStringArray(0L, this.Count) : this.field1.getPointer().getPointer(0L).getWideString(0L);
                }
                case EvtVarTypeFileTime: {
                    if (this.isArray()) {
                        final WinBase.FILETIME resultFirst = Structure.newInstance(WinBase.FILETIME.class, this.field1.getPointer().getPointer(0L));
                        resultFirst.read();
                        return resultFirst.toArray(this.Count);
                    }
                    final WinBase.FILETIME result2 = new WinBase.FILETIME(this.field1.getPointer());
                    result2.read();
                    return result2;
                }
                case EvtVarTypeSysTime: {
                    if (this.isArray()) {
                        final WinBase.SYSTEMTIME resultFirst2 = Structure.newInstance(WinBase.SYSTEMTIME.class, this.field1.getPointer().getPointer(0L));
                        resultFirst2.read();
                        return resultFirst2.toArray(this.Count);
                    }
                    final WinBase.SYSTEMTIME result3 = Structure.newInstance(WinBase.SYSTEMTIME.class, this.field1.getPointer().getPointer(0L));
                    result3.read();
                    return result3;
                }
                case EvtVarTypeSByte:
                case EvtVarTypeByte: {
                    return this.isArray() ? this.field1.getPointer().getPointer(0L).getByteArray(0L, this.Count) : Byte.valueOf(this.field1.getPointer().getByte(0L));
                }
                case EvtVarTypeInt16:
                case EvtVarTypeUInt16: {
                    return this.isArray() ? this.field1.getPointer().getPointer(0L).getShortArray(0L, this.Count) : Short.valueOf(this.field1.getPointer().getShort(0L));
                }
                case EvtVarTypeHexInt32:
                case EvtVarTypeInt32:
                case EvtVarTypeUInt32: {
                    return this.isArray() ? this.field1.getPointer().getPointer(0L).getIntArray(0L, this.Count) : Integer.valueOf(this.field1.getPointer().getInt(0L));
                }
                case EvtVarTypeHexInt64:
                case EvtVarTypeInt64:
                case EvtVarTypeUInt64: {
                    return this.isArray() ? this.field1.getPointer().getPointer(0L).getLongArray(0L, this.Count) : Long.valueOf(this.field1.getPointer().getLong(0L));
                }
                case EvtVarTypeSingle: {
                    return this.isArray() ? this.field1.getPointer().getPointer(0L).getFloatArray(0L, this.Count) : Float.valueOf(this.field1.getPointer().getFloat(0L));
                }
                case EvtVarTypeDouble: {
                    return this.isArray() ? this.field1.getPointer().getPointer(0L).getDoubleArray(0L, this.Count) : Double.valueOf(this.field1.getPointer().getDouble(0L));
                }
                case EvtVarTypeBinary: {
                    assert !this.isArray();
                    return this.field1.getPointer().getPointer(0L).getByteArray(0L, this.Count);
                }
                case EvtVarTypeNull: {
                    return null;
                }
                case EvtVarTypeGuid: {
                    if (this.isArray()) {
                        final Guid.GUID resultFirst3 = Structure.newInstance(Guid.GUID.class, this.field1.getPointer().getPointer(0L));
                        resultFirst3.read();
                        return resultFirst3.toArray(this.Count);
                    }
                    final Guid.GUID result4 = Structure.newInstance(Guid.GUID.class, this.field1.getPointer().getPointer(0L));
                    result4.read();
                    return result4;
                }
                case EvtVarTypeSid: {
                    if (this.isArray()) {
                        final WinNT.PSID resultFirst4 = Structure.newInstance(WinNT.PSID.class, this.field1.getPointer().getPointer(0L));
                        resultFirst4.read();
                        return resultFirst4.toArray(this.Count);
                    }
                    final WinNT.PSID result5 = Structure.newInstance(WinNT.PSID.class, this.field1.getPointer().getPointer(0L));
                    result5.read();
                    return result5;
                }
                case EvtVarTypeSizeT: {
                    if (this.isArray()) {
                        final long[] rawValue2 = this.field1.getPointer().getPointer(0L).getLongArray(0L, this.Count);
                        final BaseTSD.SIZE_T[] result6 = new BaseTSD.SIZE_T[rawValue2.length];
                        for (int i = 0; i < result6.length; ++i) {
                            result6[i] = new BaseTSD.SIZE_T(rawValue2[i]);
                        }
                        return result6;
                    }
                    return new BaseTSD.SIZE_T(this.field1.getPointer().getLong(0L));
                }
                case EvtVarTypeEvtHandle: {
                    if (this.isArray()) {
                        final Pointer[] rawValue3 = this.field1.getPointer().getPointer(0L).getPointerArray(0L, this.Count);
                        final WinNT.HANDLE[] result7 = new WinNT.HANDLE[rawValue3.length];
                        for (int i = 0; i < result7.length; ++i) {
                            result7[i] = new WinNT.HANDLE(rawValue3[i]);
                        }
                        return result7;
                    }
                    return new WinNT.HANDLE(this.field1.getPointer().getPointer(0L));
                }
                default: {
                    throw new IllegalStateException(String.format("NOT IMPLEMENTED: getValue(%s) (Array: %b, Count: %d)", type, this.isArray(), this.Count));
                }
            }
        }
        
        public static class field1_union extends Union
        {
            public byte byteValue;
            public short shortValue;
            public int intValue;
            public long longValue;
            public float floatValue;
            public double doubleVal;
            public Pointer pointerValue;
        }
        
        public static class ByReference extends EVT_VARIANT implements Structure.ByReference
        {
            public ByReference(final Pointer p) {
                super(p);
            }
            
            public ByReference() {
            }
        }
        
        public static class ByValue extends EVT_VARIANT implements Structure.ByValue
        {
            public ByValue(final Pointer p) {
                super(p);
            }
            
            public ByValue() {
            }
        }
    }
    
    @FieldOrder({ "Server", "User", "Domain", "Password", "Flags" })
    public static class EVT_RPC_LOGIN extends Structure
    {
        public String Server;
        public String User;
        public String Domain;
        public String Password;
        public int Flags;
        
        public EVT_RPC_LOGIN() {
            super(W32APITypeMapper.UNICODE);
        }
        
        public EVT_RPC_LOGIN(final String Server, final String User, final String Domain, final String Password, final int Flags) {
            super(W32APITypeMapper.UNICODE);
            this.Server = Server;
            this.User = User;
            this.Domain = Domain;
            this.Password = Password;
            this.Flags = Flags;
        }
        
        public EVT_RPC_LOGIN(final Pointer peer) {
            super(peer, 0, W32APITypeMapper.UNICODE);
        }
        
        public static class ByReference extends EVT_RPC_LOGIN implements Structure.ByReference
        {
        }
        
        public static class ByValue extends EVT_RPC_LOGIN implements Structure.ByValue
        {
        }
    }
    
    public static class EVT_HANDLE extends WinNT.HANDLE
    {
        public EVT_HANDLE() {
        }
        
        public EVT_HANDLE(final Pointer p) {
            super(p);
        }
    }
    
    public interface EVT_EVENT_PROPERTY_ID
    {
        public static final int EvtEventQueryIDs = 0;
        public static final int EvtEventPath = 1;
        public static final int EvtEventPropertyIdEND = 2;
    }
    
    public interface EVT_QUERY_PROPERTY_ID
    {
        public static final int EvtQueryNames = 0;
        public static final int EvtQueryStatuses = 1;
        public static final int EvtQueryPropertyIdEND = 2;
    }
    
    public interface EVT_EVENT_METADATA_PROPERTY_ID
    {
        public static final int EventMetadataEventID = 0;
        public static final int EventMetadataEventVersion = 1;
        public static final int EventMetadataEventChannel = 2;
        public static final int EventMetadataEventLevel = 3;
        public static final int EventMetadataEventOpcode = 4;
        public static final int EventMetadataEventTask = 5;
        public static final int EventMetadataEventKeyword = 6;
        public static final int EventMetadataEventMessageID = 7;
        public static final int EventMetadataEventTemplate = 8;
        public static final int EvtEventMetadataPropertyIdEND = 9;
    }
    
    public interface EVT_PUBLISHER_METADATA_PROPERTY_ID
    {
        public static final int EvtPublisherMetadataPublisherGuid = 0;
        public static final int EvtPublisherMetadataResourceFilePath = 1;
        public static final int EvtPublisherMetadataParameterFilePath = 2;
        public static final int EvtPublisherMetadataMessageFilePath = 3;
        public static final int EvtPublisherMetadataHelpLink = 4;
        public static final int EvtPublisherMetadataPublisherMessageID = 5;
        public static final int EvtPublisherMetadataChannelReferences = 6;
        public static final int EvtPublisherMetadataChannelReferencePath = 7;
        public static final int EvtPublisherMetadataChannelReferenceIndex = 8;
        public static final int EvtPublisherMetadataChannelReferenceID = 9;
        public static final int EvtPublisherMetadataChannelReferenceFlags = 10;
        public static final int EvtPublisherMetadataChannelReferenceMessageID = 11;
        public static final int EvtPublisherMetadataLevels = 12;
        public static final int EvtPublisherMetadataLevelName = 13;
        public static final int EvtPublisherMetadataLevelValue = 14;
        public static final int EvtPublisherMetadataLevelMessageID = 15;
        public static final int EvtPublisherMetadataTasks = 16;
        public static final int EvtPublisherMetadataTaskName = 17;
        public static final int EvtPublisherMetadataTaskEventGuid = 18;
        public static final int EvtPublisherMetadataTaskValue = 19;
        public static final int EvtPublisherMetadataTaskMessageID = 20;
        public static final int EvtPublisherMetadataOpcodes = 21;
        public static final int EvtPublisherMetadataOpcodeName = 22;
        public static final int EvtPublisherMetadataOpcodeValue = 23;
        public static final int EvtPublisherMetadataOpcodeMessageID = 24;
        public static final int EvtPublisherMetadataKeywords = 25;
        public static final int EvtPublisherMetadataKeywordName = 26;
        public static final int EvtPublisherMetadataKeywordValue = 27;
        public static final int EvtPublisherMetadataKeywordMessageID = 28;
        public static final int EvtPublisherMetadataPropertyIdEND = 29;
    }
    
    public interface EVT_CHANNEL_REFERENCE_FLAGS
    {
        public static final int EvtChannelReferenceImported = 1;
    }
    
    public interface EVT_CHANNEL_SID_TYPE
    {
        public static final int EvtChannelSidTypeNone = 0;
        public static final int EvtChannelSidTypePublishing = 1;
    }
    
    public interface EVT_CHANNEL_CLOCK_TYPE
    {
        public static final int EvtChannelClockTypeSystemTime = 0;
        public static final int EvtChannelClockTypeQPC = 1;
    }
    
    public interface EVT_CHANNEL_ISOLATION_TYPE
    {
        public static final int EvtChannelIsolationTypeApplication = 0;
        public static final int EvtChannelIsolationTypeSystem = 1;
        public static final int EvtChannelIsolationTypeCustom = 2;
    }
    
    public interface EVT_CHANNEL_TYPE
    {
        public static final int EvtChannelTypeAdmin = 0;
        public static final int EvtChannelTypeOperational = 1;
        public static final int EvtChannelTypeAnalytic = 2;
        public static final int EvtChannelTypeDebug = 3;
    }
    
    public interface EVT_CHANNEL_CONFIG_PROPERTY_ID
    {
        public static final int EvtChannelConfigEnabled = 0;
        public static final int EvtChannelConfigIsolation = 1;
        public static final int EvtChannelConfigType = 2;
        public static final int EvtChannelConfigOwningPublisher = 3;
        public static final int EvtChannelConfigClassicEventlog = 4;
        public static final int EvtChannelConfigAccess = 5;
        public static final int EvtChannelLoggingConfigRetention = 6;
        public static final int EvtChannelLoggingConfigAutoBackup = 7;
        public static final int EvtChannelLoggingConfigMaxSize = 8;
        public static final int EvtChannelLoggingConfigLogFilePath = 9;
        public static final int EvtChannelPublishingConfigLevel = 10;
        public static final int EvtChannelPublishingConfigKeywords = 11;
        public static final int EvtChannelPublishingConfigControlGuid = 12;
        public static final int EvtChannelPublishingConfigBufferSize = 13;
        public static final int EvtChannelPublishingConfigMinBuffers = 14;
        public static final int EvtChannelPublishingConfigMaxBuffers = 15;
        public static final int EvtChannelPublishingConfigLatency = 16;
        public static final int EvtChannelPublishingConfigClockType = 17;
        public static final int EvtChannelPublishingConfigSidType = 18;
        public static final int EvtChannelPublisherList = 19;
        public static final int EvtChannelPublishingConfigFileMax = 20;
        public static final int EvtChannelConfigPropertyIdEND = 21;
    }
    
    public interface EVT_EXPORTLOG_FLAGS
    {
        public static final int EvtExportLogChannelPath = 1;
        public static final int EvtExportLogFilePath = 2;
        public static final int EvtExportLogTolerateQueryErrors = 4096;
        public static final int EvtExportLogOverwrite = 8192;
    }
    
    public interface EVT_LOG_PROPERTY_ID
    {
        public static final int EvtLogCreationTime = 0;
        public static final int EvtLogLastAccessTime = 1;
        public static final int EvtLogLastWriteTime = 2;
        public static final int EvtLogFileSize = 3;
        public static final int EvtLogAttributes = 4;
        public static final int EvtLogNumberOfLogRecords = 5;
        public static final int EvtLogOldestRecordNumber = 6;
        public static final int EvtLogFull = 7;
    }
    
    public interface EVT_OPEN_LOG_FLAGS
    {
        public static final int EvtOpenChannelPath = 1;
        public static final int EvtOpenFilePath = 2;
    }
    
    public interface EVT_FORMAT_MESSAGE_FLAGS
    {
        public static final int EvtFormatMessageEvent = 1;
        public static final int EvtFormatMessageLevel = 2;
        public static final int EvtFormatMessageTask = 3;
        public static final int EvtFormatMessageOpcode = 4;
        public static final int EvtFormatMessageKeyword = 5;
        public static final int EvtFormatMessageChannel = 6;
        public static final int EvtFormatMessageProvider = 7;
        public static final int EvtFormatMessageId = 8;
        public static final int EvtFormatMessageXml = 9;
    }
    
    public interface EVT_RENDER_FLAGS
    {
        public static final int EvtRenderEventValues = 0;
        public static final int EvtRenderEventXml = 1;
        public static final int EvtRenderBookmark = 2;
    }
    
    public interface EVT_RENDER_CONTEXT_FLAGS
    {
        public static final int EvtRenderContextValues = 0;
        public static final int EvtRenderContextSystem = 1;
        public static final int EvtRenderContextUser = 2;
    }
    
    public interface EVT_SYSTEM_PROPERTY_ID
    {
        public static final int EvtSystemProviderName = 0;
        public static final int EvtSystemProviderGuid = 1;
        public static final int EvtSystemEventID = 2;
        public static final int EvtSystemQualifiers = 3;
        public static final int EvtSystemLevel = 4;
        public static final int EvtSystemTask = 5;
        public static final int EvtSystemOpcode = 6;
        public static final int EvtSystemKeywords = 7;
        public static final int EvtSystemTimeCreated = 8;
        public static final int EvtSystemEventRecordId = 9;
        public static final int EvtSystemActivityID = 10;
        public static final int EvtSystemRelatedActivityID = 11;
        public static final int EvtSystemProcessID = 12;
        public static final int EvtSystemThreadID = 13;
        public static final int EvtSystemChannel = 14;
        public static final int EvtSystemComputer = 15;
        public static final int EvtSystemUserID = 16;
        public static final int EvtSystemVersion = 17;
        public static final int EvtSystemPropertyIdEND = 18;
    }
    
    public interface EVT_SUBSCRIBE_NOTIFY_ACTION
    {
        public static final int EvtSubscribeActionError = 0;
        public static final int EvtSubscribeActionDeliver = 1;
    }
    
    public interface EVT_SUBSCRIBE_FLAGS
    {
        public static final int EvtSubscribeToFutureEvents = 1;
        public static final int EvtSubscribeStartAtOldestRecord = 2;
        public static final int EvtSubscribeStartAfterBookmark = 3;
        public static final int EvtSubscribeOriginMask = 3;
        public static final int EvtSubscribeTolerateQueryErrors = 4096;
        public static final int EvtSubscribeStrict = 65536;
    }
    
    public interface EVT_SEEK_FLAGS
    {
        public static final int EvtSeekRelativeToFirst = 1;
        public static final int EvtSeekRelativeToLast = 2;
        public static final int EvtSeekRelativeToCurrent = 3;
        public static final int EvtSeekRelativeToBookmark = 4;
        public static final int EvtSeekOriginMask = 7;
        public static final int EvtSeekStrict = 65536;
    }
    
    public interface EVT_QUERY_FLAGS
    {
        public static final int EvtQueryChannelPath = 1;
        public static final int EvtQueryFilePath = 2;
        public static final int EvtQueryForwardDirection = 256;
        public static final int EvtQueryReverseDirection = 512;
        public static final int EvtQueryTolerateQueryErrors = 4096;
    }
    
    public interface EVT_RPC_LOGIN_FLAGS
    {
        public static final int EvtRpcLoginAuthDefault = 0;
        public static final int EvtRpcLoginAuthNegotiate = 1;
        public static final int EvtRpcLoginAuthKerberos = 2;
        public static final int EvtRpcLoginAuthNTLM = 3;
    }
    
    public interface EVT_LOGIN_CLASS
    {
        public static final int EvtRpcLogin = 1;
    }
}
