// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.COM.TypeLibUtil;

public class TlbPropertyGetStub extends TlbAbstractMethod
{
    public TlbPropertyGetStub(final int index, final TypeLibUtil typeLibUtil, final OaIdl.FUNCDESC funcDesc, final TypeInfoUtil typeInfoUtil) {
        super(index, typeLibUtil, funcDesc, typeInfoUtil);
        final TypeInfoUtil.TypeInfoDoc typeInfoDoc = typeInfoUtil.getDocumentation(funcDesc.memid);
        final String docStr = typeInfoDoc.getDocString();
        final String methodname = "get" + typeInfoDoc.getName();
        this.replaceVariable("helpstring", docStr);
        this.replaceVariable("returntype", this.returnType);
        this.replaceVariable("methodname", methodname);
        this.replaceVariable("vtableid", String.valueOf(this.vtableId));
        this.replaceVariable("memberid", String.valueOf(this.memberid));
    }
    
    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbPropertyGetStub.template";
    }
}
