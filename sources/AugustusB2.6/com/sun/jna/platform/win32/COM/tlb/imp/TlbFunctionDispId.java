// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.COM.TypeLibUtil;

public class TlbFunctionDispId extends TlbAbstractMethod
{
    public TlbFunctionDispId(final int count, final int index, final TypeLibUtil typeLibUtil, final OaIdl.FUNCDESC funcDesc, final TypeInfoUtil typeInfoUtil) {
        super(index, typeLibUtil, funcDesc, typeInfoUtil);
        final String[] names = typeInfoUtil.getNames(funcDesc.memid, this.paramCount + 1);
        for (int i = 0; i < this.paramCount; ++i) {
            final OaIdl.ELEMDESC elemdesc = funcDesc.lprgelemdescParam.elemDescArg[i];
            final String methodName = names[i + 1].toLowerCase();
            final String type = this.getType(elemdesc.tdesc);
            final String _methodName = this.replaceJavaKeyword(methodName);
            this.methodparams = this.methodparams + type + " " + _methodName;
            if (type.equals("VARIANT")) {
                this.methodvariables += _methodName;
            }
            else {
                this.methodvariables = this.methodvariables + "new VARIANT(" + _methodName + ")";
            }
            if (i < this.paramCount - 1) {
                this.methodparams += ", ";
                this.methodvariables += ", ";
            }
        }
        String returnValue;
        if (this.returnType.equalsIgnoreCase("VARIANT")) {
            returnValue = "pResult";
        }
        else {
            returnValue = "((" + this.returnType + ") pResult.getValue())";
        }
        this.replaceVariable("helpstring", this.docStr);
        this.replaceVariable("returntype", this.returnType);
        this.replaceVariable("returnvalue", returnValue);
        this.replaceVariable("methodname", this.methodName);
        this.replaceVariable("methodparams", this.methodparams);
        this.replaceVariable("methodvariables", this.methodvariables);
        this.replaceVariable("vtableid", String.valueOf(this.vtableId));
        this.replaceVariable("memberid", String.valueOf(this.memberid));
        this.replaceVariable("functionCount", String.valueOf(count));
    }
    
    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbFunctionDispId.template";
    }
}
