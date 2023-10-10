// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OaIdlUtil;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.WString;
import com.sun.jna.Pointer;

public interface Wbemcli
{
    public static final int WBEM_FLAG_RETURN_IMMEDIATELY = 16;
    public static final int WBEM_FLAG_FORWARD_ONLY = 32;
    public static final int WBEM_INFINITE = -1;
    public static final int WBEM_S_NO_ERROR = 0;
    public static final int WBEM_S_FALSE = 1;
    public static final int WBEM_S_TIMEDOUT = 262148;
    public static final int WBEM_S_NO_MORE_DATA = 262149;
    public static final int WBEM_E_INVALID_NAMESPACE = -2147217394;
    public static final int WBEM_E_INVALID_CLASS = -2147217392;
    public static final int WBEM_E_INVALID_QUERY = -2147217385;
    public static final int CIM_ILLEGAL = 4095;
    public static final int CIM_EMPTY = 0;
    public static final int CIM_SINT8 = 16;
    public static final int CIM_UINT8 = 17;
    public static final int CIM_SINT16 = 2;
    public static final int CIM_UINT16 = 18;
    public static final int CIM_SINT32 = 3;
    public static final int CIM_UINT32 = 19;
    public static final int CIM_SINT64 = 20;
    public static final int CIM_UINT64 = 21;
    public static final int CIM_REAL32 = 4;
    public static final int CIM_REAL64 = 5;
    public static final int CIM_BOOLEAN = 11;
    public static final int CIM_STRING = 8;
    public static final int CIM_DATETIME = 101;
    public static final int CIM_REFERENCE = 102;
    public static final int CIM_CHAR16 = 103;
    public static final int CIM_OBJECT = 13;
    public static final int CIM_FLAG_ARRAY = 8192;
    
    public static class IWbemClassObject extends Unknown
    {
        public IWbemClassObject() {
        }
        
        public IWbemClassObject(final Pointer pvInstance) {
            super(pvInstance);
        }
        
        public WinNT.HRESULT Get(final WString wszName, final int lFlags, final Variant.VARIANT.ByReference pVal, final IntByReference pType, final IntByReference plFlavor) {
            return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[] { this.getPointer(), wszName, lFlags, pVal, pType, plFlavor }, WinNT.HRESULT.class);
        }
        
        public WinNT.HRESULT Get(final String wszName, final int lFlags, final Variant.VARIANT.ByReference pVal, final IntByReference pType, final IntByReference plFlavor) {
            return this.Get((wszName == null) ? null : new WString(wszName), lFlags, pVal, pType, plFlavor);
        }
        
        public WinNT.HRESULT GetNames(final String wszQualifierName, final int lFlags, final Variant.VARIANT.ByReference pQualifierVal, final PointerByReference pNames) {
            return this.GetNames((wszQualifierName == null) ? null : new WString(wszQualifierName), lFlags, pQualifierVal, pNames);
        }
        
        public WinNT.HRESULT GetNames(final WString wszQualifierName, final int lFlags, final Variant.VARIANT.ByReference pQualifierVal, final PointerByReference pNames) {
            return (WinNT.HRESULT)this._invokeNativeObject(7, new Object[] { this.getPointer(), wszQualifierName, lFlags, pQualifierVal, pNames }, WinNT.HRESULT.class);
        }
        
        public String[] GetNames(final String wszQualifierName, final int lFlags, final Variant.VARIANT.ByReference pQualifierVal) {
            final PointerByReference pbr = new PointerByReference();
            COMUtils.checkRC(this.GetNames(wszQualifierName, lFlags, pQualifierVal, pbr));
            final Object[] nameObjects = (Object[])OaIdlUtil.toPrimitiveArray(new OaIdl.SAFEARRAY(pbr.getValue()), true);
            final String[] names = new String[nameObjects.length];
            for (int i = 0; i < nameObjects.length; ++i) {
                names[i] = (String)nameObjects[i];
            }
            return names;
        }
    }
    
    public static class IEnumWbemClassObject extends Unknown
    {
        public IEnumWbemClassObject() {
        }
        
        public IEnumWbemClassObject(final Pointer pvInstance) {
            super(pvInstance);
        }
        
        public WinNT.HRESULT Next(final int lTimeOut, final int uCount, final Pointer[] ppObjects, final IntByReference puReturned) {
            return (WinNT.HRESULT)this._invokeNativeObject(4, new Object[] { this.getPointer(), lTimeOut, uCount, ppObjects, puReturned }, WinNT.HRESULT.class);
        }
        
        public IWbemClassObject[] Next(final int lTimeOut, final int uCount) {
            final Pointer[] resultArray = new Pointer[uCount];
            final IntByReference resultCount = new IntByReference();
            final WinNT.HRESULT result = this.Next(lTimeOut, uCount, resultArray, resultCount);
            COMUtils.checkRC(result);
            final IWbemClassObject[] returnValue = new IWbemClassObject[resultCount.getValue()];
            for (int i = 0; i < resultCount.getValue(); ++i) {
                returnValue[i] = new IWbemClassObject(resultArray[i]);
            }
            return returnValue;
        }
    }
    
    public static class IWbemLocator extends Unknown
    {
        public static final Guid.CLSID CLSID_WbemLocator;
        public static final Guid.GUID IID_IWbemLocator;
        
        public IWbemLocator() {
        }
        
        private IWbemLocator(final Pointer pvInstance) {
            super(pvInstance);
        }
        
        public static IWbemLocator create() {
            final PointerByReference pbr = new PointerByReference();
            final WinNT.HRESULT hres = Ole32.INSTANCE.CoCreateInstance(IWbemLocator.CLSID_WbemLocator, null, 1, IWbemLocator.IID_IWbemLocator, pbr);
            if (COMUtils.FAILED(hres)) {
                return null;
            }
            return new IWbemLocator(pbr.getValue());
        }
        
        public WinNT.HRESULT ConnectServer(final WTypes.BSTR strNetworkResource, final WTypes.BSTR strUser, final WTypes.BSTR strPassword, final WTypes.BSTR strLocale, final int lSecurityFlags, final WTypes.BSTR strAuthority, final IWbemContext pCtx, final PointerByReference ppNamespace) {
            return (WinNT.HRESULT)this._invokeNativeObject(3, new Object[] { this.getPointer(), strNetworkResource, strUser, strPassword, strLocale, lSecurityFlags, strAuthority, pCtx, ppNamespace }, WinNT.HRESULT.class);
        }
        
        public IWbemServices ConnectServer(final String strNetworkResource, final String strUser, final String strPassword, final String strLocale, final int lSecurityFlags, final String strAuthority, final IWbemContext pCtx) {
            final WTypes.BSTR strNetworkResourceBSTR = OleAuto.INSTANCE.SysAllocString(strNetworkResource);
            final WTypes.BSTR strUserBSTR = OleAuto.INSTANCE.SysAllocString(strUser);
            final WTypes.BSTR strPasswordBSTR = OleAuto.INSTANCE.SysAllocString(strPassword);
            final WTypes.BSTR strLocaleBSTR = OleAuto.INSTANCE.SysAllocString(strLocale);
            final WTypes.BSTR strAuthorityBSTR = OleAuto.INSTANCE.SysAllocString(strAuthority);
            final PointerByReference pbr = new PointerByReference();
            try {
                final WinNT.HRESULT result = this.ConnectServer(strNetworkResourceBSTR, strUserBSTR, strPasswordBSTR, strLocaleBSTR, lSecurityFlags, strAuthorityBSTR, pCtx, pbr);
                COMUtils.checkRC(result);
                return new IWbemServices(pbr.getValue());
            }
            finally {
                OleAuto.INSTANCE.SysFreeString(strNetworkResourceBSTR);
                OleAuto.INSTANCE.SysFreeString(strUserBSTR);
                OleAuto.INSTANCE.SysFreeString(strPasswordBSTR);
                OleAuto.INSTANCE.SysFreeString(strLocaleBSTR);
                OleAuto.INSTANCE.SysFreeString(strAuthorityBSTR);
            }
        }
        
        static {
            CLSID_WbemLocator = new Guid.CLSID("4590f811-1d3a-11d0-891f-00aa004b2e24");
            IID_IWbemLocator = new Guid.GUID("dc12a687-737f-11cf-884d-00aa004b2e24");
        }
    }
    
    public static class IWbemServices extends Unknown
    {
        public IWbemServices() {
        }
        
        public IWbemServices(final Pointer pvInstance) {
            super(pvInstance);
        }
        
        public WinNT.HRESULT ExecQuery(final WTypes.BSTR strQueryLanguage, final WTypes.BSTR strQuery, final int lFlags, final IWbemContext pCtx, final PointerByReference ppEnum) {
            return (WinNT.HRESULT)this._invokeNativeObject(20, new Object[] { this.getPointer(), strQueryLanguage, strQuery, lFlags, pCtx, ppEnum }, WinNT.HRESULT.class);
        }
        
        public IEnumWbemClassObject ExecQuery(final String strQueryLanguage, final String strQuery, final int lFlags, final IWbemContext pCtx) {
            final WTypes.BSTR strQueryLanguageBSTR = OleAuto.INSTANCE.SysAllocString(strQueryLanguage);
            final WTypes.BSTR strQueryBSTR = OleAuto.INSTANCE.SysAllocString(strQuery);
            try {
                final PointerByReference pbr = new PointerByReference();
                final WinNT.HRESULT res = this.ExecQuery(strQueryLanguageBSTR, strQueryBSTR, lFlags, pCtx, pbr);
                COMUtils.checkRC(res);
                return new IEnumWbemClassObject(pbr.getValue());
            }
            finally {
                OleAuto.INSTANCE.SysFreeString(strQueryLanguageBSTR);
                OleAuto.INSTANCE.SysFreeString(strQueryBSTR);
            }
        }
    }
    
    public static class IWbemContext extends Unknown
    {
        public IWbemContext() {
        }
        
        public IWbemContext(final Pointer pvInstance) {
            super(pvInstance);
        }
    }
    
    public interface WBEM_CONDITION_FLAG_TYPE
    {
        public static final int WBEM_FLAG_ALWAYS = 0;
        public static final int WBEM_FLAG_ONLY_IF_TRUE = 1;
        public static final int WBEM_FLAG_ONLY_IF_FALSE = 2;
        public static final int WBEM_FLAG_ONLY_IF_IDENTICAL = 3;
        public static final int WBEM_MASK_PRIMARY_CONDITION = 3;
        public static final int WBEM_FLAG_KEYS_ONLY = 4;
        public static final int WBEM_FLAG_REFS_ONLY = 8;
        public static final int WBEM_FLAG_LOCAL_ONLY = 16;
        public static final int WBEM_FLAG_PROPAGATED_ONLY = 32;
        public static final int WBEM_FLAG_SYSTEM_ONLY = 48;
        public static final int WBEM_FLAG_NONSYSTEM_ONLY = 64;
        public static final int WBEM_MASK_CONDITION_ORIGIN = 112;
        public static final int WBEM_FLAG_CLASS_OVERRIDES_ONLY = 256;
        public static final int WBEM_FLAG_CLASS_LOCAL_AND_OVERRIDES = 512;
        public static final int WBEM_MASK_CLASS_CONDITION = 768;
    }
}
