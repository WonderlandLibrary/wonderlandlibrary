/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.scanners;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.reflections.Store;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.scanners.AbstractScanner;
import org.reflections.util.Utils;

public class MethodParameterNamesScanner
extends AbstractScanner {
    @Override
    public void scan(Object cls, Store store) {
        MetadataAdapter md = this.getMetadataAdapter();
        for (Object method : md.getMethods(cls)) {
            String key = md.getMethodFullKey(cls, method);
            if (!this.acceptResult(key)) continue;
            CodeAttribute codeAttribute = ((MethodInfo)method).getCodeAttribute();
            LocalVariableAttribute table = codeAttribute != null ? (LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTable") : null;
            int length = table != null ? table.tableLength() : 0;
            int i2 = Modifier.isStatic(((MethodInfo)method).getAccessFlags()) ? 0 : 1;
            if (i2 >= length) continue;
            ArrayList<String> names = new ArrayList<String>(length - i2);
            while (i2 < length) {
                names.add(((MethodInfo)method).getConstPool().getUtf8Info(table.nameIndex(i2++)));
            }
            this.put(store, key, Utils.join(names, ", "));
        }
    }
}

