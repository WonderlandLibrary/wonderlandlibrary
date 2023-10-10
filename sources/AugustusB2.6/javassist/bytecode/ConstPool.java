// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;
import javassist.CtClass;
import java.util.HashMap;

public final class ConstPool
{
    LongVector items;
    int numOfItems;
    HashMap classes;
    HashMap strings;
    ConstInfo[] constInfoCache;
    int[] constInfoIndexCache;
    int thisClassInfo;
    private static final int CACHE_SIZE = 32;
    public static final int CONST_Class = 7;
    public static final int CONST_Fieldref = 9;
    public static final int CONST_Methodref = 10;
    public static final int CONST_InterfaceMethodref = 11;
    public static final int CONST_String = 8;
    public static final int CONST_Integer = 3;
    public static final int CONST_Float = 4;
    public static final int CONST_Long = 5;
    public static final int CONST_Double = 6;
    public static final int CONST_NameAndType = 12;
    public static final int CONST_Utf8 = 1;
    public static final CtClass THIS;
    
    private static int hashFunc(final int a, final int b) {
        int h = -2128831035;
        final int prime = 16777619;
        h = (h ^ (a & 0xFF)) * 16777619;
        h = (h ^ (b & 0xFF)) * 16777619;
        h = (h >> 5 ^ (h & 0x1F));
        return h & 0x1F;
    }
    
    public ConstPool(final String thisclass) {
        this.items = new LongVector();
        this.numOfItems = 0;
        this.addItem(null);
        this.classes = new HashMap();
        this.strings = new HashMap();
        this.constInfoCache = new ConstInfo[32];
        this.constInfoIndexCache = new int[32];
        this.thisClassInfo = this.addClassInfo(thisclass);
    }
    
    public ConstPool(final DataInputStream in) throws IOException {
        this.classes = new HashMap();
        this.strings = new HashMap();
        this.constInfoCache = new ConstInfo[32];
        this.constInfoIndexCache = new int[32];
        this.thisClassInfo = 0;
        this.read(in);
    }
    
    void prune() {
        this.classes = new HashMap();
        this.strings = new HashMap();
        this.constInfoCache = new ConstInfo[32];
        this.constInfoIndexCache = new int[32];
    }
    
    public int getSize() {
        return this.numOfItems;
    }
    
    public String getClassName() {
        return this.getClassInfo(this.thisClassInfo);
    }
    
    public int getThisClassInfo() {
        return this.thisClassInfo;
    }
    
    void setThisClassInfo(final int i) {
        this.thisClassInfo = i;
    }
    
    ConstInfo getItem(final int n) {
        return this.items.elementAt(n);
    }
    
    public int getTag(final int index) {
        return this.getItem(index).getTag();
    }
    
    public String getClassInfo(final int index) {
        final ClassInfo c = (ClassInfo)this.getItem(index);
        if (c == null) {
            return null;
        }
        return Descriptor.toJavaName(this.getUtf8Info(c.name));
    }
    
    public int getNameAndTypeName(final int index) {
        final NameAndTypeInfo ntinfo = (NameAndTypeInfo)this.getItem(index);
        return ntinfo.memberName;
    }
    
    public int getNameAndTypeDescriptor(final int index) {
        final NameAndTypeInfo ntinfo = (NameAndTypeInfo)this.getItem(index);
        return ntinfo.typeDescriptor;
    }
    
    public int getMemberClass(final int index) {
        final MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
        return minfo.classIndex;
    }
    
    public int getMemberNameAndType(final int index) {
        final MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
        return minfo.nameAndTypeIndex;
    }
    
    public int getFieldrefClass(final int index) {
        final FieldrefInfo finfo = (FieldrefInfo)this.getItem(index);
        return finfo.classIndex;
    }
    
    public String getFieldrefClassName(final int index) {
        final FieldrefInfo f = (FieldrefInfo)this.getItem(index);
        if (f == null) {
            return null;
        }
        return this.getClassInfo(f.classIndex);
    }
    
    public int getFieldrefNameAndType(final int index) {
        final FieldrefInfo finfo = (FieldrefInfo)this.getItem(index);
        return finfo.nameAndTypeIndex;
    }
    
    public String getFieldrefName(final int index) {
        final FieldrefInfo f = (FieldrefInfo)this.getItem(index);
        if (f == null) {
            return null;
        }
        final NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(f.nameAndTypeIndex);
        if (n == null) {
            return null;
        }
        return this.getUtf8Info(n.memberName);
    }
    
    public String getFieldrefType(final int index) {
        final FieldrefInfo f = (FieldrefInfo)this.getItem(index);
        if (f == null) {
            return null;
        }
        final NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(f.nameAndTypeIndex);
        if (n == null) {
            return null;
        }
        return this.getUtf8Info(n.typeDescriptor);
    }
    
    public int getMethodrefClass(final int index) {
        final MethodrefInfo minfo = (MethodrefInfo)this.getItem(index);
        return minfo.classIndex;
    }
    
    public String getMethodrefClassName(final int index) {
        final MethodrefInfo minfo = (MethodrefInfo)this.getItem(index);
        if (minfo == null) {
            return null;
        }
        return this.getClassInfo(minfo.classIndex);
    }
    
    public int getMethodrefNameAndType(final int index) {
        final MethodrefInfo minfo = (MethodrefInfo)this.getItem(index);
        return minfo.nameAndTypeIndex;
    }
    
    public String getMethodrefName(final int index) {
        final MethodrefInfo minfo = (MethodrefInfo)this.getItem(index);
        if (minfo == null) {
            return null;
        }
        final NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(minfo.nameAndTypeIndex);
        if (n == null) {
            return null;
        }
        return this.getUtf8Info(n.memberName);
    }
    
    public String getMethodrefType(final int index) {
        final MethodrefInfo minfo = (MethodrefInfo)this.getItem(index);
        if (minfo == null) {
            return null;
        }
        final NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(minfo.nameAndTypeIndex);
        if (n == null) {
            return null;
        }
        return this.getUtf8Info(n.typeDescriptor);
    }
    
    public int getInterfaceMethodrefClass(final int index) {
        final InterfaceMethodrefInfo minfo = (InterfaceMethodrefInfo)this.getItem(index);
        return minfo.classIndex;
    }
    
    public String getInterfaceMethodrefClassName(final int index) {
        final InterfaceMethodrefInfo minfo = (InterfaceMethodrefInfo)this.getItem(index);
        return this.getClassInfo(minfo.classIndex);
    }
    
    public int getInterfaceMethodrefNameAndType(final int index) {
        final InterfaceMethodrefInfo minfo = (InterfaceMethodrefInfo)this.getItem(index);
        return minfo.nameAndTypeIndex;
    }
    
    public String getInterfaceMethodrefName(final int index) {
        final InterfaceMethodrefInfo minfo = (InterfaceMethodrefInfo)this.getItem(index);
        if (minfo == null) {
            return null;
        }
        final NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(minfo.nameAndTypeIndex);
        if (n == null) {
            return null;
        }
        return this.getUtf8Info(n.memberName);
    }
    
    public String getInterfaceMethodrefType(final int index) {
        final InterfaceMethodrefInfo minfo = (InterfaceMethodrefInfo)this.getItem(index);
        if (minfo == null) {
            return null;
        }
        final NameAndTypeInfo n = (NameAndTypeInfo)this.getItem(minfo.nameAndTypeIndex);
        if (n == null) {
            return null;
        }
        return this.getUtf8Info(n.typeDescriptor);
    }
    
    public Object getLdcValue(final int index) {
        final ConstInfo constInfo = this.getItem(index);
        Object value = null;
        if (constInfo instanceof StringInfo) {
            value = this.getStringInfo(index);
        }
        else if (constInfo instanceof FloatInfo) {
            value = new Float(this.getFloatInfo(index));
        }
        else if (constInfo instanceof IntegerInfo) {
            value = new Integer(this.getIntegerInfo(index));
        }
        else if (constInfo instanceof LongInfo) {
            value = new Long(this.getLongInfo(index));
        }
        else if (constInfo instanceof DoubleInfo) {
            value = new Double(this.getDoubleInfo(index));
        }
        else {
            value = null;
        }
        return value;
    }
    
    public int getIntegerInfo(final int index) {
        final IntegerInfo i = (IntegerInfo)this.getItem(index);
        return i.value;
    }
    
    public float getFloatInfo(final int index) {
        final FloatInfo i = (FloatInfo)this.getItem(index);
        return i.value;
    }
    
    public long getLongInfo(final int index) {
        final LongInfo i = (LongInfo)this.getItem(index);
        return i.value;
    }
    
    public double getDoubleInfo(final int index) {
        final DoubleInfo i = (DoubleInfo)this.getItem(index);
        return i.value;
    }
    
    public String getStringInfo(final int index) {
        final StringInfo si = (StringInfo)this.getItem(index);
        return this.getUtf8Info(si.string);
    }
    
    public String getUtf8Info(final int index) {
        final Utf8Info utf = (Utf8Info)this.getItem(index);
        return utf.string;
    }
    
    public int isConstructor(final String classname, final int index) {
        return this.isMember(classname, "<init>", index);
    }
    
    public int isMember(final String classname, final String membername, final int index) {
        final MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
        if (this.getClassInfo(minfo.classIndex).equals(classname)) {
            final NameAndTypeInfo ntinfo = (NameAndTypeInfo)this.getItem(minfo.nameAndTypeIndex);
            if (this.getUtf8Info(ntinfo.memberName).equals(membername)) {
                return ntinfo.typeDescriptor;
            }
        }
        return 0;
    }
    
    public String eqMember(final String membername, final String desc, final int index) {
        final MemberrefInfo minfo = (MemberrefInfo)this.getItem(index);
        final NameAndTypeInfo ntinfo = (NameAndTypeInfo)this.getItem(minfo.nameAndTypeIndex);
        if (this.getUtf8Info(ntinfo.memberName).equals(membername) && this.getUtf8Info(ntinfo.typeDescriptor).equals(desc)) {
            return this.getClassInfo(minfo.classIndex);
        }
        return null;
    }
    
    private int addItem(final ConstInfo info) {
        this.items.addElement(info);
        return this.numOfItems++;
    }
    
    public int copy(final int n, final ConstPool dest, final Map classnames) {
        if (n == 0) {
            return 0;
        }
        final ConstInfo info = this.getItem(n);
        return info.copy(this, dest, classnames);
    }
    
    int addConstInfoPadding() {
        return this.addItem(new ConstInfoPadding());
    }
    
    public int addClassInfo(final CtClass c) {
        if (c == ConstPool.THIS) {
            return this.thisClassInfo;
        }
        if (!c.isArray()) {
            return this.addClassInfo(c.getName());
        }
        return this.addClassInfo(Descriptor.toJvmName(c));
    }
    
    public int addClassInfo(final String qname) {
        ClassInfo info = this.classes.get(qname);
        if (info != null) {
            return info.index;
        }
        final int utf8 = this.addUtf8Info(Descriptor.toJvmName(qname));
        info = new ClassInfo(utf8, this.numOfItems);
        this.classes.put(qname, info);
        return this.addItem(info);
    }
    
    public int addNameAndTypeInfo(final String name, final String type) {
        return this.addNameAndTypeInfo(this.addUtf8Info(name), this.addUtf8Info(type));
    }
    
    public int addNameAndTypeInfo(final int name, final int type) {
        final int h = hashFunc(name, type);
        final ConstInfo ci = this.constInfoCache[h];
        if (ci != null && ci instanceof NameAndTypeInfo && ci.hashCheck(name, type)) {
            return this.constInfoIndexCache[h];
        }
        final NameAndTypeInfo item = new NameAndTypeInfo(name, type);
        this.constInfoCache[h] = item;
        final int i = this.addItem(item);
        return this.constInfoIndexCache[h] = i;
    }
    
    public int addFieldrefInfo(final int classInfo, final String name, final String type) {
        final int nt = this.addNameAndTypeInfo(name, type);
        return this.addFieldrefInfo(classInfo, nt);
    }
    
    public int addFieldrefInfo(final int classInfo, final int nameAndTypeInfo) {
        final int h = hashFunc(classInfo, nameAndTypeInfo);
        final ConstInfo ci = this.constInfoCache[h];
        if (ci != null && ci instanceof FieldrefInfo && ci.hashCheck(classInfo, nameAndTypeInfo)) {
            return this.constInfoIndexCache[h];
        }
        final FieldrefInfo item = new FieldrefInfo(classInfo, nameAndTypeInfo);
        this.constInfoCache[h] = item;
        final int i = this.addItem(item);
        return this.constInfoIndexCache[h] = i;
    }
    
    public int addMethodrefInfo(final int classInfo, final String name, final String type) {
        final int nt = this.addNameAndTypeInfo(name, type);
        return this.addMethodrefInfo(classInfo, nt);
    }
    
    public int addMethodrefInfo(final int classInfo, final int nameAndTypeInfo) {
        final int h = hashFunc(classInfo, nameAndTypeInfo);
        final ConstInfo ci = this.constInfoCache[h];
        if (ci != null && ci instanceof MethodrefInfo && ci.hashCheck(classInfo, nameAndTypeInfo)) {
            return this.constInfoIndexCache[h];
        }
        final MethodrefInfo item = new MethodrefInfo(classInfo, nameAndTypeInfo);
        this.constInfoCache[h] = item;
        final int i = this.addItem(item);
        return this.constInfoIndexCache[h] = i;
    }
    
    public int addInterfaceMethodrefInfo(final int classInfo, final String name, final String type) {
        final int nt = this.addNameAndTypeInfo(name, type);
        return this.addInterfaceMethodrefInfo(classInfo, nt);
    }
    
    public int addInterfaceMethodrefInfo(final int classInfo, final int nameAndTypeInfo) {
        final int h = hashFunc(classInfo, nameAndTypeInfo);
        final ConstInfo ci = this.constInfoCache[h];
        if (ci != null && ci instanceof InterfaceMethodrefInfo && ci.hashCheck(classInfo, nameAndTypeInfo)) {
            return this.constInfoIndexCache[h];
        }
        final InterfaceMethodrefInfo item = new InterfaceMethodrefInfo(classInfo, nameAndTypeInfo);
        this.constInfoCache[h] = item;
        final int i = this.addItem(item);
        return this.constInfoIndexCache[h] = i;
    }
    
    public int addStringInfo(final String str) {
        return this.addItem(new StringInfo(this.addUtf8Info(str)));
    }
    
    public int addIntegerInfo(final int i) {
        return this.addItem(new IntegerInfo(i));
    }
    
    public int addFloatInfo(final float f) {
        return this.addItem(new FloatInfo(f));
    }
    
    public int addLongInfo(final long l) {
        final int i = this.addItem(new LongInfo(l));
        this.addItem(new ConstInfoPadding());
        return i;
    }
    
    public int addDoubleInfo(final double d) {
        final int i = this.addItem(new DoubleInfo(d));
        this.addItem(new ConstInfoPadding());
        return i;
    }
    
    public int addUtf8Info(final String utf8) {
        Utf8Info info = this.strings.get(utf8);
        if (info != null) {
            return info.index;
        }
        info = new Utf8Info(utf8, this.numOfItems);
        this.strings.put(utf8, info);
        return this.addItem(info);
    }
    
    public Set getClassNames() {
        final HashSet result = new HashSet();
        final LongVector v = this.items;
        for (int size = this.numOfItems, i = 1; i < size; ++i) {
            final String className = v.elementAt(i).getClassName(this);
            if (className != null) {
                result.add(className);
            }
        }
        return result;
    }
    
    public void renameClass(final String oldName, final String newName) {
        final LongVector v = this.items;
        final int size = this.numOfItems;
        this.classes = new HashMap(this.classes.size() * 2);
        for (int i = 1; i < size; ++i) {
            final ConstInfo ci = v.elementAt(i);
            ci.renameClass(this, oldName, newName);
            ci.makeHashtable(this);
        }
    }
    
    public void renameClass(final Map classnames) {
        final LongVector v = this.items;
        final int size = this.numOfItems;
        this.classes = new HashMap(this.classes.size() * 2);
        for (int i = 1; i < size; ++i) {
            final ConstInfo ci = v.elementAt(i);
            ci.renameClass(this, classnames);
            ci.makeHashtable(this);
        }
    }
    
    private void read(final DataInputStream in) throws IOException {
        int n = in.readUnsignedShort();
        this.items = new LongVector(n);
        this.numOfItems = 0;
        this.addItem(null);
        while (--n > 0) {
            final int tag = this.readOne(in);
            if (tag == 5 || tag == 6) {
                this.addItem(new ConstInfoPadding());
                --n;
            }
        }
        int i = 1;
        while (true) {
            final ConstInfo info = this.items.elementAt(i++);
            if (info == null) {
                break;
            }
            info.makeHashtable(this);
        }
    }
    
    private int readOne(final DataInputStream in) throws IOException {
        final int tag = in.readUnsignedByte();
        ConstInfo info = null;
        switch (tag) {
            case 1: {
                info = new Utf8Info(in, this.numOfItems);
                this.strings.put(((Utf8Info)info).string, info);
                break;
            }
            case 3: {
                info = new IntegerInfo(in);
                break;
            }
            case 4: {
                info = new FloatInfo(in);
                break;
            }
            case 5: {
                info = new LongInfo(in);
                break;
            }
            case 6: {
                info = new DoubleInfo(in);
                break;
            }
            case 7: {
                info = new ClassInfo(in, this.numOfItems);
                break;
            }
            case 8: {
                info = new StringInfo(in);
                break;
            }
            case 9: {
                info = new FieldrefInfo(in);
                break;
            }
            case 10: {
                info = new MethodrefInfo(in);
                break;
            }
            case 11: {
                info = new InterfaceMethodrefInfo(in);
                break;
            }
            case 12: {
                info = new NameAndTypeInfo(in);
                break;
            }
            default: {
                throw new IOException("invalid constant type: " + tag);
            }
        }
        this.addItem(info);
        return tag;
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeShort(this.numOfItems);
        final LongVector v = this.items;
        for (int size = this.numOfItems, i = 1; i < size; ++i) {
            v.elementAt(i).write(out);
        }
    }
    
    public void print() {
        this.print(new PrintWriter(System.out, true));
    }
    
    public void print(final PrintWriter out) {
        for (int size = this.numOfItems, i = 1; i < size; ++i) {
            out.print(i);
            out.print(" ");
            this.items.elementAt(i).print(out);
        }
    }
    
    static {
        THIS = null;
    }
}
