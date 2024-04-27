/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.Label;
import jdk.nashorn.internal.ir.debug.NashornTextifier;

public class NashornClassReader
extends ClassReader {
    private final Map<String, List<Label>> labelMap = new HashMap<String, List<Label>>();
    private static String[] type = new String[]{"<error>", "UTF8", "<error>", "Integer", "Float", "Long", "Double", "Class", "String", "Fieldref", "Methodref", "InterfaceMethodRef", "NameAndType", "<error>", "<error>", "MethodHandle", "MethodType", "<error>", "Invokedynamic"};

    public NashornClassReader(byte[] bytecode) {
        super(bytecode);
        this.parse(bytecode);
    }

    List<Label> getExtraLabels(String className, String methodName, String methodDesc) {
        String key = NashornClassReader.fullyQualifiedName(className, methodName, methodDesc);
        return this.labelMap.get(key);
    }

    private static int readByte(byte[] bytecode, int index) {
        return (byte)(bytecode[index] & 0xFF);
    }

    private static int readShort(byte[] bytecode, int index) {
        return (short)((bytecode[index] & 0xFF) << 8) | bytecode[index + 1] & 0xFF;
    }

    private static int readInt(byte[] bytecode, int index) {
        return (bytecode[index] & 0xFF) << 24 | (bytecode[index + 1] & 0xFF) << 16 | (bytecode[index + 2] & 0xFF) << 8 | bytecode[index + 3] & 0xFF;
    }

    private static long readLong(byte[] bytecode, int index) {
        int hi = NashornClassReader.readInt(bytecode, index);
        int lo = NashornClassReader.readInt(bytecode, index + 4);
        return (long)hi << 32 | (long)lo;
    }

    private static String readUTF(int index, int utfLen, byte[] bytecode) {
        int endIndex = index + utfLen;
        char[] buf = new char[utfLen * 2];
        int strLen = 0;
        int st = 0;
        int cc = 0;
        int i = index;
        block5: while (i < endIndex) {
            int c = bytecode[i++];
            switch (st) {
                case 0: {
                    if ((c &= 0xFF) < 128) {
                        buf[strLen++] = (char)c;
                        continue block5;
                    }
                    if (c < 224 && c > 191) {
                        cc = (char)(c & 0x1F);
                        st = 1;
                        continue block5;
                    }
                    cc = (char)(c & 0xF);
                    st = 2;
                    continue block5;
                }
                case 1: {
                    buf[strLen++] = (char)(cc << 6 | c & 0x3F);
                    st = 0;
                    continue block5;
                }
                case 2: {
                    cc = (char)(cc << 6 | c & 0x3F);
                    st = 1;
                    continue block5;
                }
            }
        }
        return new String(buf, 0, strLen);
    }

    private String parse(byte[] bytecode) {
        int ac;
        int u = 0;
        int magic = NashornClassReader.readInt(bytecode, u);
        u += 4;
        assert (magic == -889275714) : Integer.toHexString(magic);
        NashornClassReader.readShort(bytecode, u);
        NashornClassReader.readShort(bytecode, u += 2);
        int cpc = NashornClassReader.readShort(bytecode, u += 2);
        u += 2;
        ArrayList<Constant> cp = new ArrayList<Constant>(cpc);
        cp.add(null);
        block14: for (int i = 1; i < cpc; ++i) {
            int tag = NashornClassReader.readByte(bytecode, u);
            ++u;
            switch (tag) {
                case 7: {
                    cp.add(new IndexInfo(cp, tag, NashornClassReader.readShort(bytecode, u)));
                    u += 2;
                    continue block14;
                }
                case 9: 
                case 10: 
                case 11: {
                    cp.add(new IndexInfo2(cp, tag, NashornClassReader.readShort(bytecode, u), NashornClassReader.readShort(bytecode, u + 2)));
                    u += 4;
                    continue block14;
                }
                case 8: {
                    cp.add(new IndexInfo(cp, tag, NashornClassReader.readShort(bytecode, u)));
                    u += 2;
                    continue block14;
                }
                case 3: {
                    cp.add(new DirectInfo<Integer>(cp, tag, NashornClassReader.readInt(bytecode, u)));
                    u += 4;
                    continue block14;
                }
                case 4: {
                    cp.add(new DirectInfo<Float>(cp, tag, Float.valueOf(Float.intBitsToFloat(NashornClassReader.readInt(bytecode, u)))));
                    u += 4;
                    continue block14;
                }
                case 5: {
                    cp.add(new DirectInfo<Long>(cp, tag, NashornClassReader.readLong(bytecode, u)));
                    cp.add(null);
                    ++i;
                    u += 8;
                    continue block14;
                }
                case 6: {
                    cp.add(new DirectInfo<Double>(cp, tag, Double.longBitsToDouble(NashornClassReader.readLong(bytecode, u))));
                    cp.add(null);
                    ++i;
                    u += 8;
                    continue block14;
                }
                case 12: {
                    cp.add(new IndexInfo2(cp, tag, NashornClassReader.readShort(bytecode, u), NashornClassReader.readShort(bytecode, u + 2)));
                    u += 4;
                    continue block14;
                }
                case 1: {
                    int len = NashornClassReader.readShort(bytecode, u);
                    cp.add(new DirectInfo<String>(cp, tag, NashornClassReader.readUTF(u += 2, len, bytecode)));
                    u += len;
                    continue block14;
                }
                case 16: {
                    cp.add(new IndexInfo(cp, tag, NashornClassReader.readShort(bytecode, u)));
                    u += 2;
                    continue block14;
                }
                case 18: {
                    cp.add(new IndexInfo2(cp, tag, NashornClassReader.readShort(bytecode, u), NashornClassReader.readShort(bytecode, u + 2)){

                        @Override
                        public String toString() {
                            return "#" + this.index + ' ' + ((Constant)this.cp.get(this.index2)).toString();
                        }
                    });
                    u += 4;
                    continue block14;
                }
                case 15: {
                    int kind = NashornClassReader.readByte(bytecode, u);
                    assert (kind >= 1 && kind <= 9) : kind;
                    cp.add(new IndexInfo2(cp, tag, kind, NashornClassReader.readShort(bytecode, u + 1)){

                        @Override
                        public String toString() {
                            return "#" + this.index + ' ' + ((Constant)this.cp.get(this.index2)).toString();
                        }
                    });
                    u += 3;
                    continue block14;
                }
                default: {
                    assert (false) : tag;
                    continue block14;
                }
            }
        }
        NashornClassReader.readShort(bytecode, u);
        int cls = NashornClassReader.readShort(bytecode, u += 2);
        u += 2;
        String thisClassName = ((Constant)cp.get(cls)).toString();
        int ifc = NashornClassReader.readShort(bytecode, u += 2);
        u += 2;
        int fc = NashornClassReader.readShort(bytecode, u += ifc * 2);
        u += 2;
        for (int i = 0; i < fc; ++i) {
            NashornClassReader.readShort(bytecode, u += 2);
            u += 2;
            ac = NashornClassReader.readShort(bytecode, u += 2);
            u += 2;
            for (int j = 0; j < ac; ++j) {
                int len = NashornClassReader.readInt(bytecode, u += 2);
                u += 4;
                u += len;
            }
        }
        int mc = NashornClassReader.readShort(bytecode, u);
        u += 2;
        for (int i = 0; i < mc; ++i) {
            NashornClassReader.readShort(bytecode, u);
            int methodNameIndex = NashornClassReader.readShort(bytecode, u += 2);
            String methodName = cp.get(methodNameIndex).toString();
            int methodDescIndex = NashornClassReader.readShort(bytecode, u += 2);
            String methodDesc = cp.get(methodDescIndex).toString();
            int ac2 = NashornClassReader.readShort(bytecode, u += 2);
            u += 2;
            for (int j = 0; j < ac2; ++j) {
                int nameIndex = NashornClassReader.readShort(bytecode, u);
                String attrName = cp.get(nameIndex).toString();
                int attrLen = NashornClassReader.readInt(bytecode, u += 2);
                u += 4;
                if ("Code".equals(attrName)) {
                    NashornClassReader.readShort(bytecode, u);
                    NashornClassReader.readShort(bytecode, u += 2);
                    int len = NashornClassReader.readInt(bytecode, u += 2);
                    this.parseCode(bytecode, u += 4, len, NashornClassReader.fullyQualifiedName(thisClassName, methodName, methodDesc));
                    int elen = NashornClassReader.readShort(bytecode, u += len);
                    u += 2;
                    int ac22 = NashornClassReader.readShort(bytecode, u += elen * 8);
                    u += 2;
                    for (int k = 0; k < ac22; ++k) {
                        int aclen = NashornClassReader.readInt(bytecode, u += 2);
                        u += 4;
                        u += aclen;
                    }
                    continue;
                }
                u += attrLen;
            }
        }
        ac = NashornClassReader.readShort(bytecode, u);
        u += 2;
        for (int i = 0; i < ac; ++i) {
            NashornClassReader.readShort(bytecode, u);
            int len = NashornClassReader.readInt(bytecode, u += 2);
            u += 4;
            u += len;
        }
        return thisClassName;
    }

    private static String fullyQualifiedName(String className, String methodName, String methodDesc) {
        return className + '.' + methodName + methodDesc;
    }

    private void parseCode(byte[] bytecode, int index, int len, String desc) {
        ArrayList<NashornTextifier.NashornLabel> labels = new ArrayList<NashornTextifier.NashornLabel>();
        this.labelMap.put(desc, labels);
        boolean wide = false;
        int i = index;
        while (i < index + len) {
            byte opcode = bytecode[i];
            labels.add(new NashornTextifier.NashornLabel(opcode, i - index));
            switch (opcode & 0xFF) {
                case 196: {
                    wide = true;
                    ++i;
                    break;
                }
                case 169: {
                    i += wide ? 4 : 2;
                    break;
                }
                case 171: {
                    ++i;
                    while ((i - index & 3) != 0) {
                        ++i;
                    }
                    NashornClassReader.readInt(bytecode, i);
                    int npairs = NashornClassReader.readInt(bytecode, i += 4);
                    i += 4;
                    i += 8 * npairs;
                    break;
                }
                case 170: {
                    ++i;
                    while ((i - index & 3) != 0) {
                        ++i;
                    }
                    NashornClassReader.readInt(bytecode, i);
                    int lo = NashornClassReader.readInt(bytecode, i += 4);
                    int hi = NashornClassReader.readInt(bytecode, i += 4);
                    i += 4;
                    i += 4 * (hi - lo + 1);
                    break;
                }
                case 197: {
                    i += 4;
                    break;
                }
                case 21: 
                case 22: 
                case 23: 
                case 24: 
                case 25: 
                case 54: 
                case 55: 
                case 56: 
                case 57: 
                case 58: {
                    i += wide ? 3 : 2;
                    break;
                }
                case 16: 
                case 18: 
                case 188: {
                    i += 2;
                    break;
                }
                case 17: 
                case 19: 
                case 20: 
                case 153: 
                case 154: 
                case 155: 
                case 156: 
                case 157: 
                case 158: 
                case 159: 
                case 160: 
                case 161: 
                case 162: 
                case 163: 
                case 164: 
                case 165: 
                case 166: 
                case 167: 
                case 168: 
                case 178: 
                case 179: 
                case 180: 
                case 181: 
                case 182: 
                case 183: 
                case 184: 
                case 187: 
                case 189: 
                case 192: 
                case 193: 
                case 198: 
                case 199: {
                    i += 3;
                    break;
                }
                case 132: {
                    i += wide ? 5 : 3;
                    break;
                }
                case 185: 
                case 186: 
                case 200: 
                case 201: {
                    i += 5;
                    break;
                }
                default: {
                    ++i;
                }
            }
            if (!wide) continue;
            wide = false;
        }
    }

    @Override
    public void accept(ClassVisitor classVisitor, Attribute[] attrs, int flags) {
        super.accept(classVisitor, attrs, flags);
    }

    @Override
    protected Label readLabel(int offset, Label[] labels) {
        Label label = super.readLabel(offset, labels);
        label.info = offset;
        return label;
    }

    private static class DirectInfo<T>
    extends Constant {
        protected final T info;

        DirectInfo(ArrayList<Constant> cp, int tag, T info) {
            super(cp, tag);
            this.info = info;
        }

        public String toString() {
            return this.info.toString();
        }
    }

    private static class IndexInfo2
    extends IndexInfo {
        protected final int index2;

        IndexInfo2(ArrayList<Constant> cp, int tag, int index, int index2) {
            super(cp, tag, index);
            this.index2 = index2;
        }

        @Override
        public String toString() {
            return super.toString() + ' ' + ((Constant)this.cp.get(this.index2)).toString();
        }
    }

    private static class IndexInfo
    extends Constant {
        protected final int index;

        IndexInfo(ArrayList<Constant> cp, int tag, int index) {
            super(cp, tag);
            this.index = index;
        }

        public String toString() {
            return ((Constant)this.cp.get(this.index)).toString();
        }
    }

    private static abstract class Constant {
        protected ArrayList<Constant> cp;
        protected int tag;

        protected Constant(ArrayList<Constant> cp, int tag) {
            this.cp = cp;
            this.tag = tag;
        }

        final String getType() {
            String str = type[this.tag];
            while (str.length() < 16) {
                str = str + " ";
            }
            return str;
        }
    }
}

