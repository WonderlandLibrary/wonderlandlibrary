// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Union;
import com.sun.jna.Pointer;
import com.sun.jna.win32.W32APITypeMapper;
import com.sun.jna.Native;
import com.sun.jna.Structure;

public interface Ntifs extends WinDef, BaseTSD
{
    public static final int MAXIMUM_REPARSE_DATA_BUFFER_SIZE = 16384;
    public static final int REPARSE_BUFFER_HEADER_SIZE = 8;
    public static final int SYMLINK_FLAG_RELATIVE = 1;
    
    @FieldOrder({ "SubstituteNameOffset", "SubstituteNameLength", "PrintNameOffset", "PrintNameLength", "Flags", "PathBuffer" })
    public static class SymbolicLinkReparseBuffer extends Structure
    {
        public short SubstituteNameOffset;
        public short SubstituteNameLength;
        public short PrintNameOffset;
        public short PrintNameLength;
        public int Flags;
        public char[] PathBuffer;
        
        public static int sizeOf() {
            return Native.getNativeSize(MountPointReparseBuffer.class, null);
        }
        
        public SymbolicLinkReparseBuffer() {
            super(W32APITypeMapper.UNICODE);
            this.SubstituteNameOffset = 0;
            this.SubstituteNameLength = 0;
            this.PrintNameOffset = 0;
            this.PrintNameLength = 0;
            this.Flags = 0;
            this.PathBuffer = new char[8192];
        }
        
        public SymbolicLinkReparseBuffer(final Pointer memory) {
            super(memory, 0, W32APITypeMapper.UNICODE);
            this.SubstituteNameOffset = 0;
            this.SubstituteNameLength = 0;
            this.PrintNameOffset = 0;
            this.PrintNameLength = 0;
            this.Flags = 0;
            this.PathBuffer = new char[8192];
            this.read();
        }
        
        public SymbolicLinkReparseBuffer(final String substituteName, final String printName, final int Flags) {
            this.SubstituteNameOffset = 0;
            this.SubstituteNameLength = 0;
            this.PrintNameOffset = 0;
            this.PrintNameLength = 0;
            this.Flags = 0;
            this.PathBuffer = new char[8192];
            final String bothNames = substituteName + printName;
            this.PathBuffer = bothNames.toCharArray();
            this.SubstituteNameOffset = 0;
            this.SubstituteNameLength = (short)(substituteName.length() * 2);
            this.PrintNameOffset = (short)(substituteName.length() * 2);
            this.PrintNameLength = (short)(printName.length() * 2);
            this.Flags = Flags;
            this.write();
        }
        
        public SymbolicLinkReparseBuffer(final short SubstituteNameOffset, final short SubstituteNameLength, final short PrintNameOffset, final short PrintNameLength, final int Flags, final String PathBuffer) {
            this.SubstituteNameOffset = 0;
            this.SubstituteNameLength = 0;
            this.PrintNameOffset = 0;
            this.PrintNameLength = 0;
            this.Flags = 0;
            this.PathBuffer = new char[8192];
            this.SubstituteNameOffset = SubstituteNameOffset;
            this.SubstituteNameLength = SubstituteNameLength;
            this.PrintNameOffset = PrintNameOffset;
            this.PrintNameLength = PrintNameLength;
            this.Flags = Flags;
            this.PathBuffer = PathBuffer.toCharArray();
            this.write();
        }
        
        public String getPrintName() {
            return String.copyValueOf(this.PathBuffer, this.PrintNameOffset / 2, this.PrintNameLength / 2);
        }
        
        public String getSubstituteName() {
            return String.copyValueOf(this.PathBuffer, this.SubstituteNameOffset / 2, this.SubstituteNameLength / 2);
        }
        
        public static class ByReference extends SymbolicLinkReparseBuffer implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "SubstituteNameOffset", "SubstituteNameLength", "PrintNameOffset", "PrintNameLength", "PathBuffer" })
    public static class MountPointReparseBuffer extends Structure
    {
        public short SubstituteNameOffset;
        public short SubstituteNameLength;
        public short PrintNameOffset;
        public short PrintNameLength;
        public char[] PathBuffer;
        
        public static int sizeOf() {
            return Native.getNativeSize(MountPointReparseBuffer.class, null);
        }
        
        public MountPointReparseBuffer() {
            super(W32APITypeMapper.UNICODE);
            this.SubstituteNameOffset = 0;
            this.SubstituteNameLength = 0;
            this.PrintNameOffset = 0;
            this.PrintNameLength = 0;
            this.PathBuffer = new char[8192];
        }
        
        public MountPointReparseBuffer(final Pointer memory) {
            super(memory, 0, W32APITypeMapper.UNICODE);
            this.SubstituteNameOffset = 0;
            this.SubstituteNameLength = 0;
            this.PrintNameOffset = 0;
            this.PrintNameLength = 0;
            this.PathBuffer = new char[8192];
            this.read();
        }
        
        public MountPointReparseBuffer(final String substituteName, final String printName) {
            this.SubstituteNameOffset = 0;
            this.SubstituteNameLength = 0;
            this.PrintNameOffset = 0;
            this.PrintNameLength = 0;
            this.PathBuffer = new char[8192];
            final String bothNames = substituteName + printName;
            this.PathBuffer = bothNames.toCharArray();
            this.SubstituteNameOffset = 0;
            this.SubstituteNameLength = (short)substituteName.length();
            this.PrintNameOffset = (short)(substituteName.length() * 2);
            this.PrintNameLength = (short)(printName.length() * 2);
            this.write();
        }
        
        public MountPointReparseBuffer(final short SubstituteNameOffset, final short SubstituteNameLength, final short PrintNameOffset, final short PrintNameLength, final String PathBuffer) {
            this.SubstituteNameOffset = 0;
            this.SubstituteNameLength = 0;
            this.PrintNameOffset = 0;
            this.PrintNameLength = 0;
            this.PathBuffer = new char[8192];
            this.SubstituteNameOffset = SubstituteNameOffset;
            this.SubstituteNameLength = SubstituteNameLength;
            this.PrintNameOffset = PrintNameOffset;
            this.PrintNameLength = PrintNameLength;
            this.PathBuffer = PathBuffer.toCharArray();
            this.write();
        }
        
        public static class ByReference extends MountPointReparseBuffer implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "DataBuffer" })
    public static class GenericReparseBuffer extends Structure
    {
        public byte[] DataBuffer;
        
        public static int sizeOf() {
            return Native.getNativeSize(GenericReparseBuffer.class, null);
        }
        
        public GenericReparseBuffer() {
            this.DataBuffer = new byte[16384];
        }
        
        public GenericReparseBuffer(final Pointer memory) {
            super(memory);
            this.DataBuffer = new byte[16384];
            this.read();
        }
        
        public GenericReparseBuffer(final String DataBuffer) {
            this.DataBuffer = new byte[16384];
            this.DataBuffer = DataBuffer.getBytes();
            this.write();
        }
        
        public static class ByReference extends GenericReparseBuffer implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    @FieldOrder({ "ReparseTag", "ReparseDataLength", "Reserved", "u" })
    public static class REPARSE_DATA_BUFFER extends Structure
    {
        public int ReparseTag;
        public short ReparseDataLength;
        public short Reserved;
        public REPARSE_UNION u;
        
        public static int sizeOf() {
            return Native.getNativeSize(REPARSE_DATA_BUFFER.class, null);
        }
        
        public int getSize() {
            return 8 + this.ReparseDataLength;
        }
        
        public REPARSE_DATA_BUFFER() {
            this.ReparseTag = 0;
            this.ReparseDataLength = 0;
            this.Reserved = 0;
        }
        
        public REPARSE_DATA_BUFFER(final int ReparseTag, final short Reserved) {
            this.ReparseTag = 0;
            this.ReparseDataLength = 0;
            this.Reserved = 0;
            this.ReparseTag = ReparseTag;
            this.Reserved = Reserved;
            this.ReparseDataLength = 0;
            this.write();
        }
        
        public REPARSE_DATA_BUFFER(final int ReparseTag, final short Reserved, final SymbolicLinkReparseBuffer symLinkReparseBuffer) {
            this.ReparseTag = 0;
            this.ReparseDataLength = 0;
            this.Reserved = 0;
            this.ReparseTag = ReparseTag;
            this.Reserved = Reserved;
            this.ReparseDataLength = (short)symLinkReparseBuffer.size();
            this.u.setType(SymbolicLinkReparseBuffer.class);
            this.u.symLinkReparseBuffer = symLinkReparseBuffer;
            this.write();
        }
        
        public REPARSE_DATA_BUFFER(final Pointer memory) {
            super(memory);
            this.ReparseTag = 0;
            this.ReparseDataLength = 0;
            this.Reserved = 0;
            this.read();
        }
        
        @Override
        public void read() {
            super.read();
            switch (this.ReparseTag) {
                default: {
                    this.u.setType(GenericReparseBuffer.class);
                    break;
                }
                case -1610612733: {
                    this.u.setType(MountPointReparseBuffer.class);
                    break;
                }
                case -1610612724: {
                    this.u.setType(SymbolicLinkReparseBuffer.class);
                    break;
                }
            }
            this.u.read();
        }
        
        public static class ByReference extends REPARSE_DATA_BUFFER implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
        
        public static class REPARSE_UNION extends Union
        {
            public SymbolicLinkReparseBuffer symLinkReparseBuffer;
            public MountPointReparseBuffer mountPointReparseBuffer;
            public GenericReparseBuffer genericReparseBuffer;
            
            public REPARSE_UNION() {
            }
            
            public REPARSE_UNION(final Pointer memory) {
                super(memory);
            }
            
            public static class ByReference extends REPARSE_UNION implements Structure.ByReference
            {
            }
        }
    }
}
