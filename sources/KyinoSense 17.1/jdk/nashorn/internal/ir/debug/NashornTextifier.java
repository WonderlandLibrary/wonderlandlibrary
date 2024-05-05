/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir.debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.signature.SignatureReader;
import jdk.internal.org.objectweb.asm.signature.SignatureVisitor;
import jdk.internal.org.objectweb.asm.util.Printer;
import jdk.internal.org.objectweb.asm.util.TraceSignatureVisitor;
import jdk.nashorn.internal.ir.debug.NashornClassReader;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;

public final class NashornTextifier
extends Printer {
    private String currentClassName;
    private Iterator<Label> labelIter;
    private Graph graph;
    private String currentBlock;
    private boolean lastWasNop = false;
    private boolean lastWasEllipse = false;
    private static final int INTERNAL_NAME = 0;
    private static final int FIELD_DESCRIPTOR = 1;
    private static final int FIELD_SIGNATURE = 2;
    private static final int METHOD_DESCRIPTOR = 3;
    private static final int METHOD_SIGNATURE = 4;
    private static final int CLASS_SIGNATURE = 5;
    private final String tab = "  ";
    private final String tab2 = "    ";
    private final String tab3 = "      ";
    private Map<Label, String> labelNames;
    private boolean localVarsStarted = false;
    private NashornClassReader cr;
    private ScriptEnvironment env;

    public NashornTextifier(ScriptEnvironment env, NashornClassReader cr) {
        this(327680);
        this.env = env;
        this.cr = cr;
    }

    private NashornTextifier(ScriptEnvironment env, NashornClassReader cr, Iterator<Label> labelIter, Graph graph) {
        this(env, cr);
        this.labelIter = labelIter;
        this.graph = graph;
    }

    protected NashornTextifier(int api) {
        super(api);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        int major = version & 0xFFFF;
        int minor = version >>> 16;
        this.currentClassName = name;
        StringBuilder sb = new StringBuilder();
        sb.append("// class version ").append(major).append('.').append(minor).append(" (").append(version).append(")\n");
        if ((access & 0x20000) != 0) {
            sb.append("// DEPRECATED\n");
        }
        sb.append("// access flags 0x").append(Integer.toHexString(access).toUpperCase()).append('\n');
        NashornTextifier.appendDescriptor(sb, 5, signature);
        if (signature != null) {
            TraceSignatureVisitor sv = new TraceSignatureVisitor(access);
            SignatureReader r = new SignatureReader(signature);
            r.accept(sv);
            sb.append("// declaration: ").append(name).append(sv.getDeclaration()).append('\n');
        }
        NashornTextifier.appendAccess(sb, access & 0xFFFFFFDF);
        if ((access & 0x2000) != 0) {
            sb.append("@interface ");
        } else if ((access & 0x200) != 0) {
            sb.append("interface ");
        } else if ((access & 0x4000) == 0) {
            sb.append("class ");
        }
        NashornTextifier.appendDescriptor(sb, 0, name);
        if (superName != null && !"java/lang/Object".equals(superName)) {
            sb.append(" extends ");
            NashornTextifier.appendDescriptor(sb, 0, superName);
            sb.append(' ');
        }
        if (interfaces != null && interfaces.length > 0) {
            sb.append(" implements ");
            for (String interface1 : interfaces) {
                NashornTextifier.appendDescriptor(sb, 0, interface1);
                sb.append(' ');
            }
        }
        sb.append(" {\n");
        this.addText(sb);
    }

    @Override
    public void visitSource(String file, String debug) {
        StringBuilder sb = new StringBuilder();
        if (file != null) {
            sb.append("  ").append("// compiled from: ").append(file).append('\n');
        }
        if (debug != null) {
            sb.append("  ").append("// debug info: ").append(debug).append('\n');
        }
        if (sb.length() > 0) {
            this.addText(sb);
        }
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ").append("outer class ");
        NashornTextifier.appendDescriptor(sb, 0, owner);
        sb.append(' ');
        if (name != null) {
            sb.append(name).append(' ');
        }
        NashornTextifier.appendDescriptor(sb, 3, desc);
        sb.append('\n');
        this.addText(sb);
    }

    @Override
    public NashornTextifier visitField(int access, String name, String desc, String signature, Object value) {
        StringBuilder sb = new StringBuilder();
        if ((access & 0x20000) != 0) {
            sb.append("  ").append("// DEPRECATED\n");
        }
        if (signature != null) {
            sb.append("  ");
            NashornTextifier.appendDescriptor(sb, 2, signature);
            TraceSignatureVisitor sv = new TraceSignatureVisitor(0);
            SignatureReader r = new SignatureReader(signature);
            r.acceptType(sv);
            sb.append("  ").append("// declaration: ").append(sv.getDeclaration()).append('\n');
        }
        sb.append("  ");
        NashornTextifier.appendAccess(sb, access);
        String prunedDesc = desc.endsWith(";") ? desc.substring(0, desc.length() - 1) : desc;
        NashornTextifier.appendDescriptor(sb, 1, prunedDesc);
        sb.append(' ').append(name);
        if (value != null) {
            sb.append(" = ");
            if (value instanceof String) {
                sb.append('\"').append(value).append('\"');
            } else {
                sb.append(value);
            }
        }
        sb.append(";\n");
        this.addText(sb);
        NashornTextifier t = this.createNashornTextifier();
        this.addText(t.getText());
        return t;
    }

    @Override
    public NashornTextifier visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        this.graph = new Graph(name);
        List<Label> extraLabels = this.cr.getExtraLabels(this.currentClassName, name, desc);
        this.labelIter = extraLabels == null ? null : extraLabels.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        if ((access & 0x20000) != 0) {
            sb.append("  ").append("// DEPRECATED\n");
        }
        sb.append("  ").append("// access flags 0x").append(Integer.toHexString(access).toUpperCase()).append('\n');
        if (signature != null) {
            sb.append("  ");
            NashornTextifier.appendDescriptor(sb, 4, signature);
            String[] v = new TraceSignatureVisitor(0);
            SignatureReader r = new SignatureReader(signature);
            r.accept((SignatureVisitor)v);
            String genericDecl = v.getDeclaration();
            String genericReturn = v.getReturnType();
            String genericExceptions = v.getExceptions();
            sb.append("  ").append("// declaration: ").append(genericReturn).append(' ').append(name).append(genericDecl);
            if (genericExceptions != null) {
                sb.append(" throws ").append(genericExceptions);
            }
            sb.append('\n');
        }
        sb.append("  ");
        NashornTextifier.appendAccess(sb, access);
        if ((access & 0x100) != 0) {
            sb.append("native ");
        }
        if ((access & 0x80) != 0) {
            sb.append("varargs ");
        }
        if ((access & 0x40) != 0) {
            sb.append("bridge ");
        }
        sb.append(name);
        NashornTextifier.appendDescriptor(sb, 3, desc);
        if (exceptions != null && exceptions.length > 0) {
            sb.append(" throws ");
            for (String exception : exceptions) {
                NashornTextifier.appendDescriptor(sb, 0, exception);
                sb.append(' ');
            }
        }
        sb.append('\n');
        this.addText(sb);
        NashornTextifier t = this.createNashornTextifier();
        this.addText(t.getText());
        return t;
    }

    @Override
    public void visitClassEnd() {
        this.addText("}\n");
    }

    @Override
    public void visitFieldEnd() {
    }

    @Override
    public void visitParameter(String name, int access) {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append("// parameter ");
        NashornTextifier.appendAccess(sb, access);
        sb.append(' ').append(name == null ? "<no name>" : name).append('\n');
        this.addText(sb);
    }

    @Override
    public void visitCode() {
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        StringBuilder sb = new StringBuilder();
        sb.append("frame ");
        switch (type) {
            case -1: 
            case 0: {
                sb.append("full [");
                this.appendFrameTypes(sb, nLocal, local);
                sb.append("] [");
                this.appendFrameTypes(sb, nStack, stack);
                sb.append(']');
                break;
            }
            case 1: {
                sb.append("append [");
                this.appendFrameTypes(sb, nLocal, local);
                sb.append(']');
                break;
            }
            case 2: {
                sb.append("chop ").append(nLocal);
                break;
            }
            case 3: {
                sb.append("same");
                break;
            }
            case 4: {
                sb.append("same1 ");
                this.appendFrameTypes(sb, 1, stack);
                break;
            }
            default: {
                assert (false);
                break;
            }
        }
        sb.append('\n');
        sb.append('\n');
        this.addText(sb);
    }

    private StringBuilder appendOpcode(StringBuilder sb, int opcode) {
        Label next = this.getNextLabel();
        if (next instanceof NashornLabel) {
            int bci = next.getOffset();
            if (bci != -1) {
                String bcis = "" + bci;
                for (int i = 0; i < 5 - bcis.length(); ++i) {
                    sb.append(' ');
                }
                sb.append(bcis);
                sb.append(' ');
            } else {
                sb.append("       ");
            }
        }
        return sb.append("    ").append(OPCODES[opcode].toLowerCase());
    }

    private Label getNextLabel() {
        return this.labelIter == null ? null : this.labelIter.next();
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == 0) {
            if (this.lastWasEllipse) {
                this.getNextLabel();
                return;
            }
            if (this.lastWasNop) {
                this.getNextLabel();
                this.addText("          ...\n");
                this.lastWasEllipse = true;
                return;
            }
            this.lastWasNop = true;
        } else {
            this.lastWasEllipse = false;
            this.lastWasNop = false;
        }
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append('\n');
        this.addText(sb);
        this.checkNoFallThru(opcode, null);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append(' ').append(opcode == 188 ? TYPES[operand] : Integer.toString(operand)).append('\n');
        this.addText(sb);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append(' ').append(var).append('\n');
        this.addText(sb);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append(' ');
        NashornTextifier.appendDescriptor(sb, 0, type);
        sb.append('\n');
        this.addText(sb);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append(' ');
        NashornTextifier.appendDescriptor(sb, 0, owner);
        sb.append('.').append(name).append(" : ");
        NashornTextifier.appendDescriptor(sb, 1, desc);
        sb.append('\n');
        this.addText(sb);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append(' ');
        NashornTextifier.appendDescriptor(sb, 0, owner);
        sb.append('.').append(name);
        NashornTextifier.appendDescriptor(sb, 3, desc);
        sb.append('\n');
        this.addText(sb);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object ... bsmArgs) {
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, 186).append(' ');
        sb.append(name);
        NashornTextifier.appendDescriptor(sb, 3, desc);
        int len = sb.length();
        for (int i = 0; i < 80 - len; ++i) {
            sb.append(' ');
        }
        sb.append(" [");
        NashornTextifier.appendHandle(sb, bsm);
        if (bsmArgs.length == 0) {
            sb.append("none");
        } else {
            for (Object cst : bsmArgs) {
                if (cst instanceof String) {
                    NashornTextifier.appendStr(sb, (String)cst);
                } else if (cst instanceof Type) {
                    sb.append(((Type)cst).getDescriptor()).append(".class");
                } else if (cst instanceof Handle) {
                    NashornTextifier.appendHandle(sb, (Handle)cst);
                } else if (cst instanceof Integer) {
                    int c = (Integer)cst;
                    int pp = c >> 11;
                    if (pp != 0) {
                        sb.append(" pp=").append(pp);
                    }
                    sb.append(NashornCallSiteDescriptor.toString(c & 0x7FF));
                } else {
                    sb.append(cst);
                }
                sb.append(", ");
            }
            sb.setLength(sb.length() - 2);
        }
        sb.append("]\n");
        this.addText(sb);
    }

    private static final boolean noFallThru(int opcode) {
        switch (opcode) {
            case 167: 
            case 172: 
            case 173: 
            case 174: 
            case 175: 
            case 176: 
            case 191: {
                return true;
            }
        }
        return false;
    }

    private void checkNoFallThru(int opcode, String to) {
        if (NashornTextifier.noFallThru(opcode)) {
            this.graph.setNoFallThru(this.currentBlock);
        }
        if (this.currentBlock != null && to != null) {
            this.graph.addEdge(this.currentBlock, to);
        }
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, opcode).append(' ');
        String to = this.appendLabel(sb, label);
        sb.append('\n');
        this.addText(sb);
        this.checkNoFallThru(opcode, to);
    }

    private void addText(Object t) {
        this.text.add(t);
        if (this.currentBlock != null) {
            this.graph.addText(this.currentBlock, t.toString());
        }
    }

    @Override
    public void visitLabel(Label label) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        String name = this.appendLabel(sb, label);
        sb.append(" [bci=");
        sb.append(label.info);
        sb.append("]");
        sb.append("\n");
        this.graph.addNode(name);
        if (this.currentBlock != null && !this.graph.isNoFallThru(this.currentBlock)) {
            this.graph.addEdge(this.currentBlock, name);
        }
        this.currentBlock = name;
        this.addText(sb);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, 18).append(' ');
        if (cst instanceof String) {
            NashornTextifier.appendStr(sb, (String)cst);
        } else if (cst instanceof Type) {
            sb.append(((Type)cst).getDescriptor()).append(".class");
        } else {
            sb.append(cst);
        }
        sb.append('\n');
        this.addText(sb);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, 132).append(' ');
        sb.append(var).append(' ').append(increment).append('\n');
        this.addText(sb);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label ... labels) {
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, 170).append(' ');
        for (int i = 0; i < labels.length; ++i) {
            sb.append("      ").append(min + i).append(": ");
            String to = this.appendLabel(sb, labels[i]);
            this.graph.addEdge(this.currentBlock, to);
            sb.append('\n');
        }
        sb.append("      ").append("default: ");
        this.appendLabel(sb, dflt);
        sb.append('\n');
        this.addText(sb);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys2, Label[] labels) {
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, 171).append(' ');
        for (int i = 0; i < labels.length; ++i) {
            sb.append("      ").append(keys2[i]).append(": ");
            String to = this.appendLabel(sb, labels[i]);
            this.graph.addEdge(this.currentBlock, to);
            sb.append('\n');
        }
        sb.append("      ").append("default: ");
        String to = this.appendLabel(sb, dflt);
        this.graph.addEdge(this.currentBlock, to);
        sb.append('\n');
        this.addText(sb.toString());
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        StringBuilder sb = new StringBuilder();
        this.appendOpcode(sb, 197).append(' ');
        NashornTextifier.appendDescriptor(sb, 1, desc);
        sb.append(' ').append(dims).append('\n');
        this.addText(sb);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append("try ");
        String from = this.appendLabel(sb, start);
        sb.append(' ');
        this.appendLabel(sb, end);
        sb.append(' ');
        String to = this.appendLabel(sb, handler);
        sb.append(' ');
        NashornTextifier.appendDescriptor(sb, 0, type);
        sb.append('\n');
        this.addText(sb);
        this.graph.setIsCatch(to, type);
        this.graph.addTryCatch(from, to);
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        int i;
        StringBuilder sb = new StringBuilder();
        if (!this.localVarsStarted) {
            this.text.add("\n");
            this.localVarsStarted = true;
            this.graph.addNode("vars");
            this.currentBlock = "vars";
        }
        sb.append("    ").append("local ").append(name).append(' ');
        int len = sb.length();
        for (int i2 = 0; i2 < 25 - len; ++i2) {
            sb.append(' ');
        }
        String label = this.appendLabel(sb, start);
        for (i = 0; i < 5 - label.length(); ++i) {
            sb.append(' ');
        }
        label = this.appendLabel(sb, end);
        for (i = 0; i < 5 - label.length(); ++i) {
            sb.append(' ');
        }
        sb.append(index).append("    ");
        NashornTextifier.appendDescriptor(sb, 1, desc);
        sb.append('\n');
        if (signature != null) {
            sb.append("    ");
            NashornTextifier.appendDescriptor(sb, 2, signature);
            TraceSignatureVisitor sv = new TraceSignatureVisitor(0);
            SignatureReader r = new SignatureReader(signature);
            r.acceptType(sv);
            sb.append("    ").append("// declaration: ").append(sv.getDeclaration()).append('\n');
        }
        this.addText(sb.toString());
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        StringBuilder sb = new StringBuilder();
        sb.append("<line ");
        sb.append(line);
        sb.append(">\n");
        this.addText(sb.toString());
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        sb.append("    ").append("max stack  = ").append(maxStack);
        sb.append(", max locals = ").append(maxLocals).append('\n');
        this.addText(sb.toString());
    }

    private void printToDir(Graph g) {
        if (this.env._print_code_dir != null) {
            File file;
            File dir = new File(this.env._print_code_dir);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new RuntimeException(dir.toString());
            }
            int uniqueId = 0;
            do {
                String fileName = g.getName() + (uniqueId == 0 ? "" : "_" + uniqueId) + ".dot";
                file = new File(dir, fileName);
                ++uniqueId;
            } while (file.exists());
            try (PrintWriter pw = new PrintWriter(new FileOutputStream(file));){
                pw.println(g);
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void visitMethodEnd() {
        if ((this.env._print_code_func == null || this.env._print_code_func.equals(this.graph.getName())) && this.env._print_code_dir != null) {
            this.printToDir(this.graph);
        }
    }

    protected NashornTextifier createNashornTextifier() {
        return new NashornTextifier(this.env, this.cr, this.labelIter, this.graph);
    }

    private static void appendDescriptor(StringBuilder sb, int type, String desc) {
        if (desc != null) {
            if (type == 5 || type == 2 || type == 4) {
                sb.append("// signature ").append(desc).append('\n');
            } else {
                NashornTextifier.appendShortDescriptor(sb, desc);
            }
        }
    }

    private String appendLabel(StringBuilder sb, Label l) {
        String name;
        if (this.labelNames == null) {
            this.labelNames = new HashMap<Label, String>();
        }
        if ((name = this.labelNames.get(l)) == null) {
            name = "L" + this.labelNames.size();
            this.labelNames.put(l, name);
        }
        sb.append(name);
        return name;
    }

    private static void appendHandle(StringBuilder sb, Handle h) {
        switch (h.getTag()) {
            case 1: {
                sb.append("getfield");
                break;
            }
            case 2: {
                sb.append("getstatic");
                break;
            }
            case 3: {
                sb.append("putfield");
                break;
            }
            case 4: {
                sb.append("putstatic");
                break;
            }
            case 9: {
                sb.append("interface");
                break;
            }
            case 7: {
                sb.append("special");
                break;
            }
            case 6: {
                sb.append("static");
                break;
            }
            case 5: {
                sb.append("virtual");
                break;
            }
            case 8: {
                sb.append("new_special");
                break;
            }
            default: {
                assert (false);
                break;
            }
        }
        sb.append(" '");
        sb.append(h.getName());
        sb.append("'");
    }

    private static void appendAccess(StringBuilder sb, int access) {
        if ((access & 1) != 0) {
            sb.append("public ");
        }
        if ((access & 2) != 0) {
            sb.append("private ");
        }
        if ((access & 4) != 0) {
            sb.append("protected ");
        }
        if ((access & 0x10) != 0) {
            sb.append("final ");
        }
        if ((access & 8) != 0) {
            sb.append("static ");
        }
        if ((access & 0x20) != 0) {
            sb.append("synchronized ");
        }
        if ((access & 0x40) != 0) {
            sb.append("volatile ");
        }
        if ((access & 0x80) != 0) {
            sb.append("transient ");
        }
        if ((access & 0x400) != 0) {
            sb.append("abstract ");
        }
        if ((access & 0x800) != 0) {
            sb.append("strictfp ");
        }
        if ((access & 0x1000) != 0) {
            sb.append("synthetic ");
        }
        if ((access & 0x8000) != 0) {
            sb.append("mandated ");
        }
        if ((access & 0x4000) != 0) {
            sb.append("enum ");
        }
    }

    private void appendFrameTypes(StringBuilder sb, int n, Object[] o) {
        block9: for (int i = 0; i < n; ++i) {
            if (i > 0) {
                sb.append(' ');
            }
            if (o[i] instanceof String) {
                String desc = (String)o[i];
                if (desc.startsWith("[")) {
                    NashornTextifier.appendDescriptor(sb, 1, desc);
                    continue;
                }
                NashornTextifier.appendDescriptor(sb, 0, desc);
                continue;
            }
            if (o[i] instanceof Integer) {
                switch ((Integer)o[i]) {
                    case 0: {
                        NashornTextifier.appendDescriptor(sb, 1, "T");
                        continue block9;
                    }
                    case 1: {
                        NashornTextifier.appendDescriptor(sb, 1, "I");
                        continue block9;
                    }
                    case 2: {
                        NashornTextifier.appendDescriptor(sb, 1, "F");
                        continue block9;
                    }
                    case 3: {
                        NashornTextifier.appendDescriptor(sb, 1, "D");
                        continue block9;
                    }
                    case 4: {
                        NashornTextifier.appendDescriptor(sb, 1, "J");
                        continue block9;
                    }
                    case 5: {
                        NashornTextifier.appendDescriptor(sb, 1, "N");
                        continue block9;
                    }
                    case 6: {
                        NashornTextifier.appendDescriptor(sb, 1, "U");
                        continue block9;
                    }
                    default: {
                        assert (false);
                        continue block9;
                    }
                }
            }
            this.appendLabel(sb, (Label)o[i]);
        }
    }

    private static void appendShortDescriptor(StringBuilder sb, String desc) {
        if (desc.charAt(0) == '(') {
            for (int i = 0; i < desc.length(); ++i) {
                if (desc.charAt(i) == 'L') {
                    int slash = i;
                    while (desc.charAt(i) != ';') {
                        if (desc.charAt(++i) != '/') continue;
                        slash = i;
                    }
                    sb.append(desc.substring(slash + 1, i)).append(';');
                    continue;
                }
                sb.append(desc.charAt(i));
            }
        } else {
            int lastSlash = desc.lastIndexOf(47);
            int lastBracket = desc.lastIndexOf(91);
            if (lastBracket != -1) {
                sb.append(desc, 0, lastBracket + 1);
            }
            sb.append(lastSlash == -1 ? desc : desc.substring(lastSlash + 1));
        }
    }

    private static void appendStr(StringBuilder sb, String s) {
        sb.append('\"');
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == '\n') {
                sb.append("\\n");
                continue;
            }
            if (c == '\r') {
                sb.append("\\r");
                continue;
            }
            if (c == '\\') {
                sb.append("\\\\");
                continue;
            }
            if (c == '\"') {
                sb.append("\\\"");
                continue;
            }
            if (c < ' ' || c > '\u007f') {
                sb.append("\\u");
                if (c < '\u0010') {
                    sb.append("000");
                } else if (c < '\u0100') {
                    sb.append("00");
                } else if (c < '\u1000') {
                    sb.append('0');
                }
                sb.append(Integer.toString(c, 16));
                continue;
            }
            sb.append(c);
        }
        sb.append('\"');
    }

    @Override
    public Printer visitAnnotationDefault() {
        throw new AssertionError();
    }

    @Override
    public Printer visitClassAnnotation(String arg0, boolean arg1) {
        return this;
    }

    @Override
    public void visitClassAttribute(Attribute arg0) {
        throw new AssertionError();
    }

    @Override
    public Printer visitFieldAnnotation(String arg0, boolean arg1) {
        throw new AssertionError();
    }

    @Override
    public void visitFieldAttribute(Attribute arg0) {
        throw new AssertionError();
    }

    @Override
    public Printer visitMethodAnnotation(String arg0, boolean arg1) {
        return this;
    }

    @Override
    public void visitMethodAttribute(Attribute arg0) {
        throw new AssertionError();
    }

    @Override
    public Printer visitParameterAnnotation(int arg0, String arg1, boolean arg2) {
        throw new AssertionError();
    }

    @Override
    public void visit(String arg0, Object arg1) {
        throw new AssertionError();
    }

    @Override
    public Printer visitAnnotation(String arg0, String arg1) {
        throw new AssertionError();
    }

    @Override
    public void visitAnnotationEnd() {
    }

    @Override
    public Printer visitArray(String arg0) {
        throw new AssertionError();
    }

    @Override
    public void visitEnum(String arg0, String arg1, String arg2) {
        throw new AssertionError();
    }

    @Override
    public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {
        throw new AssertionError();
    }

    static class NashornLabel
    extends Label {
        final Label label;
        final int bci;
        final int opcode;

        NashornLabel(Label label, int bci) {
            this.label = label;
            this.bci = bci;
            this.opcode = -1;
        }

        NashornLabel(int opcode, int bci) {
            this.opcode = opcode;
            this.bci = bci;
            this.label = null;
        }

        Label getLabel() {
            return this.label;
        }

        @Override
        public int getOffset() {
            return this.bci;
        }

        @Override
        public String toString() {
            return "label " + this.bci;
        }
    }

    private static class Graph {
        private final LinkedHashSet<String> nodes;
        private final Map<String, StringBuilder> contents;
        private final Map<String, Set<String>> edges;
        private final Set<String> hasPreds;
        private final Set<String> noFallThru;
        private final Map<String, String> catches;
        private final Map<String, Set<String>> exceptionMap;
        private final String name;
        private static final String LEFT_ALIGN = "\\l";
        private static final String COLOR_CATCH = "\"#ee9999\"";
        private static final String COLOR_ORPHAN = "\"#9999bb\"";
        private static final String COLOR_DEFAULT = "\"#99bb99\"";
        private static final String COLOR_LOCALVARS = "\"#999999\"";

        Graph(String name) {
            this.name = name;
            this.nodes = new LinkedHashSet();
            this.contents = new HashMap<String, StringBuilder>();
            this.edges = new HashMap<String, Set<String>>();
            this.hasPreds = new HashSet<String>();
            this.catches = new HashMap<String, String>();
            this.noFallThru = new HashSet<String>();
            this.exceptionMap = new HashMap<String, Set<String>>();
        }

        void addEdge(String from, String to) {
            Set<String> edgeSet = this.edges.get(from);
            if (edgeSet == null) {
                edgeSet = new LinkedHashSet<String>();
                this.edges.put(from, edgeSet);
            }
            edgeSet.add(to);
            this.hasPreds.add(to);
        }

        void addTryCatch(String tryNode, String catchNode) {
            Set<String> tryNodes = this.exceptionMap.get(catchNode);
            if (tryNodes == null) {
                tryNodes = new HashSet<String>();
                this.exceptionMap.put(catchNode, tryNodes);
            }
            if (!tryNodes.contains(tryNode)) {
                this.addEdge(tryNode, catchNode);
            }
            tryNodes.add(tryNode);
        }

        void addNode(String node) {
            assert (!this.nodes.contains(node));
            this.nodes.add(node);
        }

        void setNoFallThru(String node) {
            this.noFallThru.add(node);
        }

        boolean isNoFallThru(String node) {
            return this.noFallThru.contains(node);
        }

        void setIsCatch(String node, String exception) {
            this.catches.put(node, exception);
        }

        String getName() {
            return this.name;
        }

        void addText(String node, String text) {
            StringBuilder sb = this.contents.get(node);
            if (sb == null) {
                sb = new StringBuilder();
            }
            block4: for (int i = 0; i < text.length(); ++i) {
                switch (text.charAt(i)) {
                    case '\n': {
                        sb.append(LEFT_ALIGN);
                        continue block4;
                    }
                    case '\"': {
                        sb.append("'");
                        continue block4;
                    }
                    default: {
                        sb.append(text.charAt(i));
                    }
                }
            }
            this.contents.put(node, sb);
        }

        private static String dottyFriendly(String name) {
            return name.replace(':', '_');
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("digraph " + Graph.dottyFriendly(this.name) + " {");
            sb.append("\n");
            sb.append("\tgraph [fontname=courier]\n");
            sb.append("\tnode [style=filled,color=\"#99bb99\",fontname=courier]\n");
            sb.append("\tedge [fontname=courier]\n\n");
            for (String node : this.nodes) {
                String ex;
                sb.append("\t");
                sb.append(node);
                sb.append(" [");
                sb.append("id=");
                sb.append(node);
                sb.append(", label=\"");
                String c = this.contents.get(node).toString();
                if (c.startsWith(LEFT_ALIGN)) {
                    c = c.substring(LEFT_ALIGN.length());
                }
                if ((ex = this.catches.get(node)) != null) {
                    sb.append("*** CATCH: ").append(ex).append(" ***\\l");
                }
                sb.append(c);
                sb.append("\"]\n");
            }
            for (String from : this.edges.keySet()) {
                for (String to : this.edges.get(from)) {
                    sb.append("\t");
                    sb.append(from);
                    sb.append(" -> ");
                    sb.append(to);
                    sb.append("[label=\"");
                    sb.append(to);
                    sb.append("\"");
                    if (this.catches.get(to) != null) {
                        sb.append(", color=red, style=dashed");
                    }
                    sb.append(']');
                    sb.append(";\n");
                }
            }
            sb.append("\n");
            for (String node : this.nodes) {
                sb.append("\t");
                sb.append(node);
                sb.append(" [shape=box");
                if (this.catches.get(node) != null) {
                    sb.append(", color=\"#ee9999\"");
                } else if ("vars".equals(node)) {
                    sb.append(", shape=hexagon, color=\"#999999\"");
                } else if (!this.hasPreds.contains(node)) {
                    sb.append(", color=\"#9999bb\"");
                }
                sb.append("]\n");
            }
            sb.append("}\n");
            return sb.toString();
        }
    }
}

