// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;

public class TlbEnum extends TlbBase
{
    public TlbEnum(final int index, final String packagename, final TypeLibUtil typeLibUtil) {
        super(index, typeLibUtil, null);
        final TypeLibUtil.TypeLibDoc typeLibDoc = this.typeLibUtil.getDocumentation(index);
        final String docString = typeLibDoc.getDocString();
        if (typeLibDoc.getName().length() > 0) {
            this.name = typeLibDoc.getName();
        }
        this.logInfo("Type of kind 'Enum' found: " + this.name);
        this.createPackageName(packagename);
        this.createClassName(this.name);
        this.setFilename(this.name);
        final TypeInfoUtil typeInfoUtil = typeLibUtil.getTypeInfoUtil(index);
        final OaIdl.TYPEATTR typeAttr = typeInfoUtil.getTypeAttr();
        this.createJavaDocHeader(typeAttr.guid.toGuidString(), docString);
        for (int cVars = typeAttr.cVars.intValue(), i = 0; i < cVars; ++i) {
            final OaIdl.VARDESC varDesc = typeInfoUtil.getVarDesc(i);
            final Variant.VARIANT constValue = varDesc._vardesc.lpvarValue;
            final Object value = constValue.getValue();
            final OaIdl.MEMBERID memberID = varDesc.memid;
            final TypeInfoUtil.TypeInfoDoc typeInfoDoc2 = typeInfoUtil.getDocumentation(memberID);
            this.content = this.content + "\t\t//" + typeInfoDoc2.getName() + "\n";
            this.content = this.content + "\t\tpublic static final int " + typeInfoDoc2.getName() + " = " + value.toString() + ";";
            if (i < cVars - 1) {
                this.content += "\n";
            }
            typeInfoUtil.ReleaseVarDesc(varDesc);
        }
        this.createContent(this.content);
    }
    
    protected void createJavaDocHeader(final String guid, final String helpstring) {
        this.replaceVariable("uuid", guid);
        this.replaceVariable("helpstring", helpstring);
    }
    
    @Override
    protected String getClassTemplate() {
        return "com/sun/jna/platform/win32/COM/tlb/imp/TlbEnum.template";
    }
}
