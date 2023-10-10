// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.PointerType;
import java.util.Iterator;
import java.security.SecureRandom;
import java.util.Arrays;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface Guid
{
    public static final IID IID_NULL = new IID();
    
    @FieldOrder({ "Data1", "Data2", "Data3", "Data4" })
    public static class GUID extends Structure
    {
        public int Data1;
        public short Data2;
        public short Data3;
        public byte[] Data4;
        
        public GUID() {
            this.Data4 = new byte[8];
        }
        
        public GUID(final GUID guid) {
            this.Data4 = new byte[8];
            this.Data1 = guid.Data1;
            this.Data2 = guid.Data2;
            this.Data3 = guid.Data3;
            this.Data4 = guid.Data4;
            this.writeFieldsToMemory();
        }
        
        public GUID(final String guid) {
            this(fromString(guid));
        }
        
        public GUID(final byte[] data) {
            this(fromBinary(data));
        }
        
        public GUID(final Pointer memory) {
            super(memory);
            this.Data4 = new byte[8];
            this.read();
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == null) {
                return false;
            }
            if (this == o) {
                return true;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final GUID other = (GUID)o;
            return this.Data1 == other.Data1 && this.Data2 == other.Data2 && this.Data3 == other.Data3 && Arrays.equals(this.Data4, other.Data4);
        }
        
        @Override
        public int hashCode() {
            return this.Data1 + this.Data2 & 65535 + this.Data3 & 65535 + Arrays.hashCode(this.Data4);
        }
        
        public static GUID fromBinary(final byte[] data) {
            if (data.length != 16) {
                throw new IllegalArgumentException("Invalid data length: " + data.length);
            }
            final GUID newGuid = new GUID();
            long data1Temp = data[0] & 0xFF;
            data1Temp <<= 8;
            data1Temp |= (data[1] & 0xFF);
            data1Temp <<= 8;
            data1Temp |= (data[2] & 0xFF);
            data1Temp <<= 8;
            data1Temp |= (data[3] & 0xFF);
            newGuid.Data1 = (int)data1Temp;
            int data2Temp = data[4] & 0xFF;
            data2Temp <<= 8;
            data2Temp |= (data[5] & 0xFF);
            newGuid.Data2 = (short)data2Temp;
            int data3Temp = data[6] & 0xFF;
            data3Temp <<= 8;
            data3Temp |= (data[7] & 0xFF);
            newGuid.Data3 = (short)data3Temp;
            newGuid.Data4[0] = data[8];
            newGuid.Data4[1] = data[9];
            newGuid.Data4[2] = data[10];
            newGuid.Data4[3] = data[11];
            newGuid.Data4[4] = data[12];
            newGuid.Data4[5] = data[13];
            newGuid.Data4[6] = data[14];
            newGuid.Data4[7] = data[15];
            newGuid.writeFieldsToMemory();
            return newGuid;
        }
        
        public static GUID fromString(final String guid) {
            int y = 0;
            final char[] _cnewguid = new char[32];
            final char[] _cguid = guid.toCharArray();
            final byte[] bdata = new byte[16];
            final GUID newGuid = new GUID();
            if (guid.length() > 38) {
                throw new IllegalArgumentException("Invalid guid length: " + guid.length());
            }
            for (int i = 0; i < _cguid.length; ++i) {
                if (_cguid[i] != '{' && _cguid[i] != '-' && _cguid[i] != '}') {
                    _cnewguid[y++] = _cguid[i];
                }
            }
            for (int i = 0; i < 32; i += 2) {
                bdata[i / 2] = (byte)((Character.digit(_cnewguid[i], 16) << 4) + Character.digit(_cnewguid[i + 1], 16) & 0xFF);
            }
            if (bdata.length != 16) {
                throw new IllegalArgumentException("Invalid data length: " + bdata.length);
            }
            long data1Temp = bdata[0] & 0xFF;
            data1Temp <<= 8;
            data1Temp |= (bdata[1] & 0xFF);
            data1Temp <<= 8;
            data1Temp |= (bdata[2] & 0xFF);
            data1Temp <<= 8;
            data1Temp |= (bdata[3] & 0xFF);
            newGuid.Data1 = (int)data1Temp;
            int data2Temp = bdata[4] & 0xFF;
            data2Temp <<= 8;
            data2Temp |= (bdata[5] & 0xFF);
            newGuid.Data2 = (short)data2Temp;
            int data3Temp = bdata[6] & 0xFF;
            data3Temp <<= 8;
            data3Temp |= (bdata[7] & 0xFF);
            newGuid.Data3 = (short)data3Temp;
            newGuid.Data4[0] = bdata[8];
            newGuid.Data4[1] = bdata[9];
            newGuid.Data4[2] = bdata[10];
            newGuid.Data4[3] = bdata[11];
            newGuid.Data4[4] = bdata[12];
            newGuid.Data4[5] = bdata[13];
            newGuid.Data4[6] = bdata[14];
            newGuid.Data4[7] = bdata[15];
            newGuid.writeFieldsToMemory();
            return newGuid;
        }
        
        public static GUID newGuid() {
            final SecureRandom ng = new SecureRandom();
            final byte[] randomBytes = new byte[16];
            ng.nextBytes(randomBytes);
            final byte[] array = randomBytes;
            final int n = 6;
            array[n] &= 0xF;
            final byte[] array2 = randomBytes;
            final int n2 = 6;
            array2[n2] |= 0x40;
            final byte[] array3 = randomBytes;
            final int n3 = 8;
            array3[n3] &= 0x3F;
            final byte[] array4 = randomBytes;
            final int n4 = 8;
            array4[n4] |= (byte)128;
            return new GUID(randomBytes);
        }
        
        public byte[] toByteArray() {
            final byte[] guid = new byte[16];
            final byte[] bytes1 = { (byte)(this.Data1 >> 24), (byte)(this.Data1 >> 16), (byte)(this.Data1 >> 8), (byte)(this.Data1 >> 0) };
            final byte[] bytes2 = { (byte)(this.Data2 >> 24), (byte)(this.Data2 >> 16), (byte)(this.Data2 >> 8), (byte)(this.Data2 >> 0) };
            final byte[] bytes3 = { (byte)(this.Data3 >> 24), (byte)(this.Data3 >> 16), (byte)(this.Data3 >> 8), (byte)(this.Data3 >> 0) };
            System.arraycopy(bytes1, 0, guid, 0, 4);
            System.arraycopy(bytes2, 2, guid, 4, 2);
            System.arraycopy(bytes3, 2, guid, 6, 2);
            System.arraycopy(this.Data4, 0, guid, 8, 8);
            return guid;
        }
        
        public String toGuidString() {
            final String HEXES = "0123456789ABCDEF";
            final byte[] bGuid = this.toByteArray();
            final StringBuilder hexStr = new StringBuilder(2 * bGuid.length);
            hexStr.append("{");
            for (int i = 0; i < bGuid.length; ++i) {
                final char ch1 = "0123456789ABCDEF".charAt((bGuid[i] & 0xF0) >> 4);
                final char ch2 = "0123456789ABCDEF".charAt(bGuid[i] & 0xF);
                hexStr.append(ch1).append(ch2);
                if (i == 3 || i == 5 || i == 7 || i == 9) {
                    hexStr.append("-");
                }
            }
            hexStr.append("}");
            return hexStr.toString();
        }
        
        protected void writeFieldsToMemory() {
            for (final String name : this.getFieldOrder()) {
                this.writeField(name);
            }
        }
        
        public static class ByValue extends GUID implements Structure.ByValue
        {
            public ByValue() {
            }
            
            public ByValue(final GUID guid) {
                super(guid.getPointer());
                this.Data1 = guid.Data1;
                this.Data2 = guid.Data2;
                this.Data3 = guid.Data3;
                this.Data4 = guid.Data4;
            }
            
            public ByValue(final Pointer memory) {
                super(memory);
            }
        }
        
        public static class ByReference extends GUID implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final GUID guid) {
                super(guid.getPointer());
                this.Data1 = guid.Data1;
                this.Data2 = guid.Data2;
                this.Data3 = guid.Data3;
                this.Data4 = guid.Data4;
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    public static class CLSID extends GUID
    {
        public CLSID() {
        }
        
        public CLSID(final String guid) {
            super(guid);
        }
        
        public CLSID(final GUID guid) {
            super(guid);
        }
        
        public static class ByReference extends GUID
        {
            public ByReference() {
            }
            
            public ByReference(final GUID guid) {
                super(guid);
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    public static class REFIID extends PointerType
    {
        public REFIID() {
        }
        
        public REFIID(final Pointer memory) {
            super(memory);
        }
        
        public REFIID(final IID guid) {
            super(guid.getPointer());
        }
        
        public void setValue(final IID value) {
            this.setPointer(value.getPointer());
        }
        
        public IID getValue() {
            return new IID(this.getPointer());
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == null) {
                return false;
            }
            if (this == o) {
                return true;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final REFIID other = (REFIID)o;
            return this.getValue().equals(other.getValue());
        }
        
        @Override
        public int hashCode() {
            return this.getValue().hashCode();
        }
    }
    
    public static class IID extends GUID
    {
        public IID() {
        }
        
        public IID(final Pointer memory) {
            super(memory);
        }
        
        public IID(final String iid) {
            super(iid);
        }
        
        public IID(final byte[] data) {
            super(data);
        }
        
        public IID(final GUID guid) {
            this(guid.toGuidString());
        }
    }
}
