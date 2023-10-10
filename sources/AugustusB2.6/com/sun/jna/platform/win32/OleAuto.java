// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Structure;
import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface OleAuto extends StdCallLibrary
{
    public static final OleAuto INSTANCE = Native.load("OleAut32", OleAuto.class, W32APIOptions.DEFAULT_OPTIONS);
    public static final int DISPATCH_METHOD = 1;
    public static final int DISPATCH_PROPERTYGET = 2;
    public static final int DISPATCH_PROPERTYPUT = 4;
    public static final int DISPATCH_PROPERTYPUTREF = 8;
    public static final int FADF_AUTO = 1;
    public static final int FADF_STATIC = 2;
    public static final int FADF_EMBEDDED = 4;
    public static final int FADF_FIXEDSIZE = 16;
    public static final int FADF_RECORD = 32;
    public static final int FADF_HAVEIID = 64;
    public static final int FADF_HAVEVARTYPE = 128;
    public static final int FADF_BSTR = 256;
    public static final int FADF_UNKNOWN = 512;
    public static final int FADF_DISPATCH = 1024;
    public static final int FADF_VARIANT = 2048;
    public static final int FADF_RESERVED = 61448;
    public static final short VARIANT_NOVALUEPROP = 1;
    public static final short VARIANT_ALPHABOOL = 2;
    public static final short VARIANT_NOUSEROVERRIDE = 4;
    public static final short VARIANT_CALENDAR_HIJRI = 8;
    public static final short VARIANT_LOCALBOOL = 16;
    public static final short VARIANT_CALENDAR_THAI = 32;
    public static final short VARIANT_CALENDAR_GREGORIAN = 64;
    public static final short VARIANT_USE_NLS = 128;
    
    WTypes.BSTR SysAllocString(final String p0);
    
    void SysFreeString(final WTypes.BSTR p0);
    
    int SysStringByteLen(final WTypes.BSTR p0);
    
    int SysStringLen(final WTypes.BSTR p0);
    
    void VariantInit(final Variant.VARIANT.ByReference p0);
    
    void VariantInit(final Variant.VARIANT p0);
    
    WinNT.HRESULT VariantCopy(final Pointer p0, final Variant.VARIANT p1);
    
    WinNT.HRESULT VariantClear(final Variant.VARIANT p0);
    
    WinNT.HRESULT VariantChangeType(final Variant.VARIANT p0, final Variant.VARIANT p1, final short p2, final WTypes.VARTYPE p3);
    
    WinNT.HRESULT VariantChangeType(final Variant.VARIANT.ByReference p0, final Variant.VARIANT.ByReference p1, final short p2, final WTypes.VARTYPE p3);
    
    OaIdl.SAFEARRAY.ByReference SafeArrayCreate(final WTypes.VARTYPE p0, final WinDef.UINT p1, final OaIdl.SAFEARRAYBOUND[] p2);
    
    WinNT.HRESULT SafeArrayPutElement(final OaIdl.SAFEARRAY p0, final WinDef.LONG[] p1, final Pointer p2);
    
    WinNT.HRESULT SafeArrayGetUBound(final OaIdl.SAFEARRAY p0, final WinDef.UINT p1, final WinDef.LONGByReference p2);
    
    WinNT.HRESULT SafeArrayGetLBound(final OaIdl.SAFEARRAY p0, final WinDef.UINT p1, final WinDef.LONGByReference p2);
    
    WinNT.HRESULT SafeArrayGetElement(final OaIdl.SAFEARRAY p0, final WinDef.LONG[] p1, final Pointer p2);
    
    WinNT.HRESULT SafeArrayPtrOfIndex(final OaIdl.SAFEARRAY p0, final WinDef.LONG[] p1, final PointerByReference p2);
    
    WinNT.HRESULT SafeArrayLock(final OaIdl.SAFEARRAY p0);
    
    WinNT.HRESULT SafeArrayUnlock(final OaIdl.SAFEARRAY p0);
    
    WinNT.HRESULT SafeArrayDestroy(final OaIdl.SAFEARRAY p0);
    
    WinNT.HRESULT SafeArrayRedim(final OaIdl.SAFEARRAY p0, final OaIdl.SAFEARRAYBOUND p1);
    
    WinNT.HRESULT SafeArrayGetVartype(final OaIdl.SAFEARRAY p0, final WTypes.VARTYPEByReference p1);
    
    WinDef.UINT SafeArrayGetDim(final OaIdl.SAFEARRAY p0);
    
    WinNT.HRESULT SafeArrayAccessData(final OaIdl.SAFEARRAY p0, final PointerByReference p1);
    
    WinNT.HRESULT SafeArrayUnaccessData(final OaIdl.SAFEARRAY p0);
    
    WinDef.UINT SafeArrayGetElemsize(final OaIdl.SAFEARRAY p0);
    
    WinNT.HRESULT GetActiveObject(final Guid.GUID p0, final WinDef.PVOID p1, final PointerByReference p2);
    
    WinNT.HRESULT LoadRegTypeLib(final Guid.GUID p0, final int p1, final int p2, final WinDef.LCID p3, final PointerByReference p4);
    
    WinNT.HRESULT LoadTypeLib(final String p0, final PointerByReference p1);
    
    int SystemTimeToVariantTime(final WinBase.SYSTEMTIME p0, final DoubleByReference p1);
    
    @FieldOrder({ "rgvarg", "rgdispidNamedArgs", "cArgs", "cNamedArgs" })
    public static class DISPPARAMS extends Structure
    {
        public Variant.VariantArg.ByReference rgvarg;
        public Pointer rgdispidNamedArgs;
        public WinDef.UINT cArgs;
        public WinDef.UINT cNamedArgs;
        
        public OaIdl.DISPID[] getRgdispidNamedArgs() {
            OaIdl.DISPID[] namedArgs = null;
            final int count = this.cNamedArgs.intValue();
            if (this.rgdispidNamedArgs != null && count > 0) {
                final int[] rawData = this.rgdispidNamedArgs.getIntArray(0L, count);
                namedArgs = new OaIdl.DISPID[count];
                for (int i = 0; i < count; ++i) {
                    namedArgs[i] = new OaIdl.DISPID(rawData[i]);
                }
            }
            else {
                namedArgs = new OaIdl.DISPID[0];
            }
            return namedArgs;
        }
        
        public void setRgdispidNamedArgs(OaIdl.DISPID[] namedArgs) {
            if (namedArgs == null) {
                namedArgs = new OaIdl.DISPID[0];
            }
            this.cNamedArgs = new WinDef.UINT((long)namedArgs.length);
            this.rgdispidNamedArgs = new Memory(OaIdl.DISPID.SIZE * namedArgs.length);
            final int[] rawData = new int[namedArgs.length];
            for (int i = 0; i < rawData.length; ++i) {
                rawData[i] = namedArgs[i].intValue();
            }
            this.rgdispidNamedArgs.write(0L, rawData, 0, namedArgs.length);
        }
        
        public Variant.VARIANT[] getArgs() {
            if (this.rgvarg != null) {
                this.rgvarg.setArraySize(this.cArgs.intValue());
                return this.rgvarg.variantArg;
            }
            return new Variant.VARIANT[0];
        }
        
        public void setArgs(Variant.VARIANT[] arguments) {
            if (arguments == null) {
                arguments = new Variant.VARIANT[0];
            }
            this.rgvarg = new Variant.VariantArg.ByReference(arguments);
            this.cArgs = new WinDef.UINT((long)arguments.length);
        }
        
        public DISPPARAMS() {
            this.rgdispidNamedArgs = Pointer.NULL;
            this.cArgs = new WinDef.UINT(0L);
            this.cNamedArgs = new WinDef.UINT(0L);
        }
        
        public DISPPARAMS(final Pointer memory) {
            super(memory);
            this.rgdispidNamedArgs = Pointer.NULL;
            this.cArgs = new WinDef.UINT(0L);
            this.cNamedArgs = new WinDef.UINT(0L);
            this.read();
        }
        
        public static class ByReference extends DISPPARAMS implements Structure.ByReference
        {
        }
    }
}
