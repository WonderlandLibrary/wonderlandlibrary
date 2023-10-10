// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;

public class TlbDispInterface extends TlbBase
{
    public TlbDispInterface(final int index, final String packagename, final TypeLibUtil typeLibUtil) {
        super(index, typeLibUtil, null);
        final TypeLibUtil.TypeLibDoc typeLibDoc = this.typeLibUtil.getDocumentation(index);
        final String docString = typeLibDoc.getDocString();
        if (typeLibDoc.getName().length() > 0) {
            this.name = typeLibDoc.getName();
        }
        this.logInfo("Type of kind 'DispInterface' found: " + this.name);
        this.createPackageName(packagename);
        this.createClassName(this.name);
        this.setFilename(this.name);
        final TypeInfoUtil typeInfoUtil = typeLibUtil.getTypeInfoUtil(index);
        final OaIdl.TYPEATTR typeAttr = typeInfoUtil.getTypeAttr();
        this.createJavaDocHeader(typeAttr.guid.toGuidString(), docString);
        for (int cFuncs = typeAttr.cFuncs.intValue(), i = 0; i < cFuncs; ++i) {
            final OaIdl.FUNCDESC funcDesc = typeInfoUtil.getFuncDesc(i);
            final OaIdl.MEMBERID memberID = funcDesc.memid;
            final TypeInfoUtil.TypeInfoDoc typeInfoDoc2 = typeInfoUtil.getDocumentation(memberID);
            final String methodName = typeInfoDoc2.getName();
            TlbAbstractMethod method = null;
            if (!this.isReservedMethod(methodName)) {
                if (funcDesc.invkind.value == OaIdl.INVOKEKIND.INVOKE_FUNC.value) {
                    method = new TlbFunctionStub(index, typeLibUtil, funcDesc, typeInfoUtil);
                }
                else if (funcDesc.invkind.value == OaIdl.INVOKEKIND.INVOKE_PROPERTYGET.value) {
                    method = new TlbPropertyGetStub(index, typeLibUtil, funcDesc, typeInfoUtil);
                }
                else if (funcDesc.invkind.value == OaIdl.INVOKEKIND.INVOKE_PROPERTYPUT.value) {
                    method = new TlbPropertyPutStub(index, typeLibUtil, funcDesc, typeInfoUtil);
                }
                else if (funcDesc.invkind.value == OaIdl.INVOKEKIND.INVOKE_PROPERTYPUTREF.value) {
                    method = new TlbPropertyPutStub(index, typeLibUtil, funcDesc, typeInfoUtil);
                }
                this.content += (Object)method.getClassBuffer();
                if (i < cFuncs - 1) {
                    this.content += "\n";
                }
            }
            typeInfoUtil.ReleaseFuncDesc(funcDesc);
        }
        this.createContent(this.content);
    }
    
    protected void createJavaDocHeader(final String guid, final String helpstring) {
        this.replaceVariable("uuid", guid);
        this.replaceVariable("helpstring", helpstring);
    }
    
    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbDispInterface.template";
    }
}
