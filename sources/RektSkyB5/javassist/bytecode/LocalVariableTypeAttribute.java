/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import javassist.bytecode.ByteArray;
import javassist.bytecode.ConstPool;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.SignatureAttribute;

public class LocalVariableTypeAttribute
extends LocalVariableAttribute {
    public static final String tag = "LocalVariableTypeTable";

    public LocalVariableTypeAttribute(ConstPool cp) {
        super(cp, tag, new byte[2]);
        ByteArray.write16bit(0, this.info, 0);
    }

    LocalVariableTypeAttribute(ConstPool cp, int n2, DataInputStream in) throws IOException {
        super(cp, n2, in);
    }

    private LocalVariableTypeAttribute(ConstPool cp, byte[] dest) {
        super(cp, tag, dest);
    }

    @Override
    String renameEntry(String desc, String oldname, String newname) {
        return SignatureAttribute.renameClass(desc, oldname, newname);
    }

    @Override
    String renameEntry(String desc, Map<String, String> classnames) {
        return SignatureAttribute.renameClass(desc, classnames);
    }

    @Override
    LocalVariableAttribute makeThisAttr(ConstPool cp, byte[] dest) {
        return new LocalVariableTypeAttribute(cp, dest);
    }
}

