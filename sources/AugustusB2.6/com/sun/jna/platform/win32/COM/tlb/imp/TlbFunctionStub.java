// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.COM.TypeLibUtil;

public class TlbFunctionStub extends TlbAbstractMethod
{
    public TlbFunctionStub(final int index, final TypeLibUtil typeLibUtil, final OaIdl.FUNCDESC funcDesc, final TypeInfoUtil typeInfoUtil) {
        super(index, typeLibUtil, funcDesc, typeInfoUtil);
        final TypeInfoUtil.TypeInfoDoc typeInfoDoc = typeInfoUtil.getDocumentation(funcDesc.memid);
        final String methodname = typeInfoDoc.getName();
        final String docStr = typeInfoDoc.getDocString();
        final String[] names = typeInfoUtil.getNames(funcDesc.memid, this.paramCount + 1);
        if (this.paramCount > 0) {
            this.methodvariables = ", ";
        }
        for (int i = 0; i < this.paramCount; ++i) {
            final OaIdl.ELEMDESC elemdesc = funcDesc.lprgelemdescParam.elemDescArg[i];
            final String methodName = names[i + 1].toLowerCase();
            this.methodparams = this.methodparams + this.getType(elemdesc.tdesc) + " " + this.replaceJavaKeyword(methodName);
            this.methodvariables += methodName;
            if (i < this.paramCount - 1) {
                this.methodparams += ", ";
                this.methodvariables += ", ";
            }
        }
        this.replaceVariable("helpstring", docStr);
        this.replaceVariable("returntype", this.returnType);
        this.replaceVariable("methodname", methodname);
        this.replaceVariable("methodparams", this.methodparams);
        this.replaceVariable("vtableid", String.valueOf(this.vtableId));
        this.replaceVariable("memberid", String.valueOf(this.memberid));
    }
    
    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbFunctionStub.template";
    }
}
