// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.ITypeInfo;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;

public class TlbCoClass extends TlbBase
{
    public TlbCoClass(final int index, final String packagename, final TypeLibUtil typeLibUtil, final String bindingMode) {
        super(index, typeLibUtil, null);
        final TypeInfoUtil typeInfoUtil = typeLibUtil.getTypeInfoUtil(index);
        final TypeLibUtil.TypeLibDoc typeLibDoc = this.typeLibUtil.getDocumentation(index);
        final String docString = typeLibDoc.getDocString();
        if (typeLibDoc.getName().length() > 0) {
            this.name = typeLibDoc.getName();
        }
        this.logInfo("Type of kind 'CoClass' found: " + this.name);
        this.createPackageName(packagename);
        this.createClassName(this.name);
        this.setFilename(this.name);
        final String guidStr = this.typeLibUtil.getLibAttr().guid.toGuidString();
        final int majorVerNum = this.typeLibUtil.getLibAttr().wMajorVerNum.intValue();
        final int minorVerNum = this.typeLibUtil.getLibAttr().wMinorVerNum.intValue();
        final String version = majorVerNum + "." + minorVerNum;
        final String clsid = typeInfoUtil.getTypeAttr().guid.toGuidString();
        this.createJavaDocHeader(guidStr, version, docString);
        this.createCLSID(clsid);
        this.createCLSIDName(this.name);
        final OaIdl.TYPEATTR typeAttr = typeInfoUtil.getTypeAttr();
        final int cImplTypes = typeAttr.cImplTypes.intValue();
        String interfaces = "";
        for (int i = 0; i < cImplTypes; ++i) {
            final OaIdl.HREFTYPE refTypeOfImplType = typeInfoUtil.getRefTypeOfImplType(i);
            final ITypeInfo refTypeInfo = typeInfoUtil.getRefTypeInfo(refTypeOfImplType);
            final TypeInfoUtil refTypeInfoUtil = new TypeInfoUtil(refTypeInfo);
            this.createFunctions(refTypeInfoUtil, bindingMode);
            final TypeInfoUtil.TypeInfoDoc documentation = refTypeInfoUtil.getDocumentation(new OaIdl.MEMBERID(-1));
            interfaces += documentation.getName();
            if (i < cImplTypes - 1) {
                interfaces += ", ";
            }
        }
        this.createInterfaces(interfaces);
        this.createContent(this.content);
    }
    
    protected void createFunctions(final TypeInfoUtil typeInfoUtil, final String bindingMode) {
        final OaIdl.TYPEATTR typeAttr = typeInfoUtil.getTypeAttr();
        for (int cFuncs = typeAttr.cFuncs.intValue(), i = 0; i < cFuncs; ++i) {
            final OaIdl.FUNCDESC funcDesc = typeInfoUtil.getFuncDesc(i);
            TlbAbstractMethod method = null;
            if (funcDesc.invkind.value == OaIdl.INVOKEKIND.INVOKE_FUNC.value) {
                if (this.isVTableMode()) {
                    method = new TlbFunctionVTable(i, this.index, this.typeLibUtil, funcDesc, typeInfoUtil);
                }
                else {
                    method = new TlbFunctionDispId(i, this.index, this.typeLibUtil, funcDesc, typeInfoUtil);
                }
            }
            else if (funcDesc.invkind.value == OaIdl.INVOKEKIND.INVOKE_PROPERTYGET.value) {
                method = new TlbPropertyGet(i, this.index, this.typeLibUtil, funcDesc, typeInfoUtil);
            }
            else if (funcDesc.invkind.value == OaIdl.INVOKEKIND.INVOKE_PROPERTYPUT.value) {
                method = new TlbPropertyPut(i, this.index, this.typeLibUtil, funcDesc, typeInfoUtil);
            }
            else if (funcDesc.invkind.value == OaIdl.INVOKEKIND.INVOKE_PROPERTYPUTREF.value) {
                method = new TlbPropertyPut(i, this.index, this.typeLibUtil, funcDesc, typeInfoUtil);
            }
            if (!this.isReservedMethod(method.getMethodName())) {
                this.content += (Object)method.getClassBuffer();
                if (i < cFuncs - 1) {
                    this.content += "\n";
                }
            }
            typeInfoUtil.ReleaseFuncDesc(funcDesc);
        }
    }
    
    protected void createJavaDocHeader(final String guid, final String version, final String helpstring) {
        this.replaceVariable("uuid", guid);
        this.replaceVariable("version", version);
        this.replaceVariable("helpstring", helpstring);
    }
    
    protected void createCLSIDName(final String clsidName) {
        this.replaceVariable("clsidname", clsidName.toUpperCase());
    }
    
    protected void createCLSID(final String clsid) {
        this.replaceVariable("clsid", clsid);
    }
    
    protected void createInterfaces(final String interfaces) {
        this.replaceVariable("interfaces", interfaces);
    }
    
    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbCoClass.template";
    }
}
