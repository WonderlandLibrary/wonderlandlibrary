// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.COM.TypeLibUtil;

public class TlbPropertyPut extends TlbAbstractMethod
{
    public TlbPropertyPut(final int count, final int index, final TypeLibUtil typeLibUtil, final OaIdl.FUNCDESC funcDesc, final TypeInfoUtil typeInfoUtil) {
        super(index, typeLibUtil, funcDesc, typeInfoUtil);
        this.methodName = "set" + this.getMethodName();
        final String[] names = typeInfoUtil.getNames(funcDesc.memid, this.paramCount + 1);
        if (this.paramCount > 0) {
            this.methodvariables += ", ";
        }
        for (int i = 0; i < this.paramCount; ++i) {
            final OaIdl.ELEMDESC elemdesc = funcDesc.lprgelemdescParam.elemDescArg[i];
            final String varType = this.getType(elemdesc);
            this.methodparams = this.methodparams + varType + " " + this.replaceJavaKeyword(names[i].toLowerCase());
            this.methodvariables += this.replaceJavaKeyword(names[i].toLowerCase());
            if (i < this.paramCount - 1) {
                this.methodparams += ", ";
                this.methodvariables += ", ";
            }
        }
        this.replaceVariable("helpstring", this.docStr);
        this.replaceVariable("methodname", this.methodName);
        this.replaceVariable("methodparams", this.methodparams);
        this.replaceVariable("methodvariables", this.methodvariables);
        this.replaceVariable("vtableid", String.valueOf(this.vtableId));
        this.replaceVariable("memberid", String.valueOf(this.memberid));
        this.replaceVariable("functionCount", String.valueOf(count));
    }
    
    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbPropertyPut.template";
    }
}
