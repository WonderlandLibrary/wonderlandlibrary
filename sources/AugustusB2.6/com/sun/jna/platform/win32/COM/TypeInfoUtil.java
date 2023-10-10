// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OleAuto;

public class TypeInfoUtil
{
    public static final OleAuto OLEAUTO;
    private ITypeInfo typeInfo;
    
    public TypeInfoUtil(final ITypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }
    
    public OaIdl.TYPEATTR getTypeAttr() {
        final PointerByReference ppTypeAttr = new PointerByReference();
        final WinNT.HRESULT hr = this.typeInfo.GetTypeAttr(ppTypeAttr);
        COMUtils.checkRC(hr);
        return new OaIdl.TYPEATTR(ppTypeAttr.getValue());
    }
    
    public TypeComp getTypeComp() {
        final PointerByReference ppTypeAttr = new PointerByReference();
        final WinNT.HRESULT hr = this.typeInfo.GetTypeComp(ppTypeAttr);
        COMUtils.checkRC(hr);
        return new TypeComp(ppTypeAttr.getValue());
    }
    
    public OaIdl.FUNCDESC getFuncDesc(final int index) {
        final PointerByReference ppFuncDesc = new PointerByReference();
        final WinNT.HRESULT hr = this.typeInfo.GetFuncDesc(new WinDef.UINT((long)index), ppFuncDesc);
        COMUtils.checkRC(hr);
        return new OaIdl.FUNCDESC(ppFuncDesc.getValue());
    }
    
    public OaIdl.VARDESC getVarDesc(final int index) {
        final PointerByReference ppVarDesc = new PointerByReference();
        final WinNT.HRESULT hr = this.typeInfo.GetVarDesc(new WinDef.UINT((long)index), ppVarDesc);
        COMUtils.checkRC(hr);
        return new OaIdl.VARDESC(ppVarDesc.getValue());
    }
    
    public String[] getNames(final OaIdl.MEMBERID memid, final int maxNames) {
        final WTypes.BSTR[] rgBstrNames = new WTypes.BSTR[maxNames];
        final WinDef.UINTByReference pcNames = new WinDef.UINTByReference();
        final WinNT.HRESULT hr = this.typeInfo.GetNames(memid, rgBstrNames, new WinDef.UINT((long)maxNames), pcNames);
        COMUtils.checkRC(hr);
        final int cNames = pcNames.getValue().intValue();
        final String[] result = new String[cNames];
        for (int i = 0; i < result.length; ++i) {
            result[i] = rgBstrNames[i].getValue();
            TypeInfoUtil.OLEAUTO.SysFreeString(rgBstrNames[i]);
        }
        return result;
    }
    
    public OaIdl.HREFTYPE getRefTypeOfImplType(final int index) {
        final OaIdl.HREFTYPEByReference ppTInfo = new OaIdl.HREFTYPEByReference();
        final WinNT.HRESULT hr = this.typeInfo.GetRefTypeOfImplType(new WinDef.UINT((long)index), ppTInfo);
        COMUtils.checkRC(hr);
        return ppTInfo.getValue();
    }
    
    public int getImplTypeFlags(final int index) {
        final IntByReference pImplTypeFlags = new IntByReference();
        final WinNT.HRESULT hr = this.typeInfo.GetImplTypeFlags(new WinDef.UINT((long)index), pImplTypeFlags);
        COMUtils.checkRC(hr);
        return pImplTypeFlags.getValue();
    }
    
    public OaIdl.MEMBERID[] getIDsOfNames(final WTypes.LPOLESTR[] rgszNames, final int cNames) {
        final OaIdl.MEMBERID[] pMemId = new OaIdl.MEMBERID[cNames];
        final WinNT.HRESULT hr = this.typeInfo.GetIDsOfNames(rgszNames, new WinDef.UINT((long)cNames), pMemId);
        COMUtils.checkRC(hr);
        return pMemId;
    }
    
    public Invoke Invoke(final WinDef.PVOID pvInstance, final OaIdl.MEMBERID memid, final WinDef.WORD wFlags, final OleAuto.DISPPARAMS.ByReference pDispParams) {
        final Variant.VARIANT.ByReference pVarResult = new Variant.VARIANT.ByReference();
        final OaIdl.EXCEPINFO.ByReference pExcepInfo = new OaIdl.EXCEPINFO.ByReference();
        final WinDef.UINTByReference puArgErr = new WinDef.UINTByReference();
        final WinNT.HRESULT hr = this.typeInfo.Invoke(pvInstance, memid, wFlags, pDispParams, pVarResult, pExcepInfo, puArgErr);
        COMUtils.checkRC(hr);
        return new Invoke(pVarResult, pExcepInfo, puArgErr.getValue().intValue());
    }
    
    public TypeInfoDoc getDocumentation(final OaIdl.MEMBERID memid) {
        final WTypes.BSTRByReference pBstrName = new WTypes.BSTRByReference();
        final WTypes.BSTRByReference pBstrDocString = new WTypes.BSTRByReference();
        final WinDef.DWORDByReference pdwHelpContext = new WinDef.DWORDByReference();
        final WTypes.BSTRByReference pBstrHelpFile = new WTypes.BSTRByReference();
        final WinNT.HRESULT hr = this.typeInfo.GetDocumentation(memid, pBstrName, pBstrDocString, pdwHelpContext, pBstrHelpFile);
        COMUtils.checkRC(hr);
        final TypeInfoDoc TypeInfoDoc = new TypeInfoDoc(pBstrName.getString(), pBstrDocString.getString(), pdwHelpContext.getValue().intValue(), pBstrHelpFile.getString());
        TypeInfoUtil.OLEAUTO.SysFreeString(pBstrName.getValue());
        TypeInfoUtil.OLEAUTO.SysFreeString(pBstrDocString.getValue());
        TypeInfoUtil.OLEAUTO.SysFreeString(pBstrHelpFile.getValue());
        return TypeInfoDoc;
    }
    
    public DllEntry GetDllEntry(final OaIdl.MEMBERID memid, final OaIdl.INVOKEKIND invKind) {
        final WTypes.BSTRByReference pBstrDllName = new WTypes.BSTRByReference();
        final WTypes.BSTRByReference pBstrName = new WTypes.BSTRByReference();
        final WinDef.WORDByReference pwOrdinal = new WinDef.WORDByReference();
        final WinNT.HRESULT hr = this.typeInfo.GetDllEntry(memid, invKind, pBstrDllName, pBstrName, pwOrdinal);
        COMUtils.checkRC(hr);
        TypeInfoUtil.OLEAUTO.SysFreeString(pBstrDllName.getValue());
        TypeInfoUtil.OLEAUTO.SysFreeString(pBstrName.getValue());
        return new DllEntry(pBstrDllName.getString(), pBstrName.getString(), pwOrdinal.getValue().intValue());
    }
    
    public ITypeInfo getRefTypeInfo(final OaIdl.HREFTYPE hreftype) {
        final PointerByReference ppTInfo = new PointerByReference();
        final WinNT.HRESULT hr = this.typeInfo.GetRefTypeInfo(hreftype, ppTInfo);
        COMUtils.checkRC(hr);
        return new TypeInfo(ppTInfo.getValue());
    }
    
    public PointerByReference AddressOfMember(final OaIdl.MEMBERID memid, final OaIdl.INVOKEKIND invKind) {
        final PointerByReference ppv = new PointerByReference();
        final WinNT.HRESULT hr = this.typeInfo.AddressOfMember(memid, invKind, ppv);
        COMUtils.checkRC(hr);
        return ppv;
    }
    
    public PointerByReference CreateInstance(final IUnknown pUnkOuter, final Guid.REFIID riid) {
        final PointerByReference ppvObj = new PointerByReference();
        final WinNT.HRESULT hr = this.typeInfo.CreateInstance(pUnkOuter, riid, ppvObj);
        COMUtils.checkRC(hr);
        return ppvObj;
    }
    
    public String GetMops(final OaIdl.MEMBERID memid) {
        final WTypes.BSTRByReference pBstrMops = new WTypes.BSTRByReference();
        final WinNT.HRESULT hr = this.typeInfo.GetMops(memid, pBstrMops);
        COMUtils.checkRC(hr);
        return pBstrMops.getString();
    }
    
    public ContainingTypeLib GetContainingTypeLib() {
        final PointerByReference ppTLib = new PointerByReference();
        final WinDef.UINTByReference pIndex = new WinDef.UINTByReference();
        final WinNT.HRESULT hr = this.typeInfo.GetContainingTypeLib(ppTLib, pIndex);
        COMUtils.checkRC(hr);
        return new ContainingTypeLib(new TypeLib(ppTLib.getValue()), pIndex.getValue().intValue());
    }
    
    public void ReleaseTypeAttr(final OaIdl.TYPEATTR pTypeAttr) {
        this.typeInfo.ReleaseTypeAttr(pTypeAttr);
    }
    
    public void ReleaseFuncDesc(final OaIdl.FUNCDESC pFuncDesc) {
        this.typeInfo.ReleaseFuncDesc(pFuncDesc);
    }
    
    public void ReleaseVarDesc(final OaIdl.VARDESC pVarDesc) {
        this.typeInfo.ReleaseVarDesc(pVarDesc);
    }
    
    static {
        OLEAUTO = OleAuto.INSTANCE;
    }
    
    public static class Invoke
    {
        private Variant.VARIANT.ByReference pVarResult;
        private OaIdl.EXCEPINFO.ByReference pExcepInfo;
        private int puArgErr;
        
        public Invoke(final Variant.VARIANT.ByReference pVarResult, final OaIdl.EXCEPINFO.ByReference pExcepInfo, final int puArgErr) {
            this.pVarResult = pVarResult;
            this.pExcepInfo = pExcepInfo;
            this.puArgErr = puArgErr;
        }
        
        public Variant.VARIANT.ByReference getpVarResult() {
            return this.pVarResult;
        }
        
        public OaIdl.EXCEPINFO.ByReference getpExcepInfo() {
            return this.pExcepInfo;
        }
        
        public int getPuArgErr() {
            return this.puArgErr;
        }
    }
    
    public static class TypeInfoDoc
    {
        private String name;
        private String docString;
        private int helpContext;
        private String helpFile;
        
        public TypeInfoDoc(final String name, final String docString, final int helpContext, final String helpFile) {
            this.name = name;
            this.docString = docString;
            this.helpContext = helpContext;
            this.helpFile = helpFile;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getDocString() {
            return this.docString;
        }
        
        public int getHelpContext() {
            return this.helpContext;
        }
        
        public String getHelpFile() {
            return this.helpFile;
        }
    }
    
    public static class DllEntry
    {
        private String dllName;
        private String name;
        private int ordinal;
        
        public DllEntry(final String dllName, final String name, final int ordinal) {
            this.dllName = dllName;
            this.name = name;
            this.ordinal = ordinal;
        }
        
        public String getDllName() {
            return this.dllName;
        }
        
        public void setDllName(final String dllName) {
            this.dllName = dllName;
        }
        
        public String getName() {
            return this.name;
        }
        
        public void setName(final String name) {
            this.name = name;
        }
        
        public int getOrdinal() {
            return this.ordinal;
        }
        
        public void setOrdinal(final int ordinal) {
            this.ordinal = ordinal;
        }
    }
    
    public static class ContainingTypeLib
    {
        private ITypeLib typeLib;
        private int index;
        
        public ContainingTypeLib(final ITypeLib typeLib, final int index) {
            this.typeLib = typeLib;
            this.index = index;
        }
        
        public ITypeLib getTypeLib() {
            return this.typeLib;
        }
        
        public void setTypeLib(final ITypeLib typeLib) {
            this.typeLib = typeLib;
        }
        
        public int getIndex() {
            return this.index;
        }
        
        public void setIndex(final int index) {
            this.index = index;
        }
    }
}
