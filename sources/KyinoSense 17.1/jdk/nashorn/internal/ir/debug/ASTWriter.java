/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir.debug;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.Terminal;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.annotations.Ignore;
import jdk.nashorn.internal.ir.annotations.Reference;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.Debug;

public final class ASTWriter {
    private final Node root;
    private static final int TABWIDTH = 4;

    public ASTWriter(Node root) {
        this.root = root;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.printAST(sb, null, null, "root", this.root, 0);
        return sb.toString();
    }

    public Node[] toArray() {
        ArrayList<Node> preorder = new ArrayList<Node>();
        this.printAST(new StringBuilder(), preorder, null, "root", this.root, 0);
        return preorder.toArray(new Node[preorder.size()]);
    }

    private void printAST(StringBuilder sb, List<Node> preorder, Field field, String name, Node node, int indent) {
        Symbol symbol;
        ASTWriter.indent(sb, indent);
        if (node == null) {
            sb.append("[Object ");
            sb.append(name);
            sb.append(" null]\n");
            return;
        }
        if (preorder != null) {
            preorder.add(node);
        }
        boolean isReference = field != null && field.isAnnotationPresent(Reference.class);
        Class<?> clazz = node.getClass();
        String type = clazz.getName();
        int truncate = (type = type.substring(type.lastIndexOf(46) + 1, type.length())).indexOf("Node");
        if (truncate == -1) {
            truncate = type.indexOf("Statement");
        }
        if (truncate != -1) {
            type = type.substring(0, truncate);
        }
        type = type.toLowerCase();
        if (isReference) {
            type = "ref: " + type;
        }
        if ((symbol = node instanceof IdentNode ? ((IdentNode)node).getSymbol() : null) != null) {
            type = type + ">" + symbol;
        }
        if (node instanceof Block && ((Block)node).needsScope()) {
            type = type + " <scope>";
        }
        LinkedList<Field> children = new LinkedList<Field>();
        if (!isReference) {
            ASTWriter.enqueueChildren(node, clazz, children);
        }
        String status = "";
        if (node instanceof Terminal && ((Terminal)((Object)node)).isTerminal()) {
            status = status + " Terminal";
        }
        if (node instanceof Statement && ((Statement)node).hasGoto()) {
            status = status + " Goto ";
        }
        if (symbol != null) {
            status = status + symbol;
        }
        if (!"".equals(status = status.trim())) {
            status = " [" + status + "]";
        }
        if (symbol != null) {
            String tname = ((Expression)node).getType().toString();
            if (tname.indexOf(46) != -1) {
                tname = tname.substring(tname.lastIndexOf(46) + 1, tname.length());
            }
            status = status + " (" + tname + ")";
        }
        status = status + " @" + Debug.id(node);
        if (children.isEmpty()) {
            sb.append("[").append(type).append(' ').append(name).append(" = '").append(node).append("'").append(status).append("] ").append('\n');
        } else {
            sb.append("[").append(type).append(' ').append(name).append(' ').append(Token.toString(node.getToken())).append(status).append("]").append('\n');
            for (Field child : children) {
                Object value;
                if (child.isAnnotationPresent(Ignore.class)) continue;
                try {
                    value = child.get(node);
                }
                catch (IllegalAccessException | IllegalArgumentException e) {
                    Context.printStackTrace(e);
                    return;
                }
                if (value instanceof Node) {
                    this.printAST(sb, preorder, child, child.getName(), (Node)value, indent + 1);
                    continue;
                }
                if (!(value instanceof Collection)) continue;
                int pos = 0;
                ASTWriter.indent(sb, indent + 1);
                sb.append('[').append(child.getName()).append("[0..").append(((Collection)value).size()).append("]]").append('\n');
                for (Node member : (Collection)value) {
                    this.printAST(sb, preorder, child, child.getName() + "[" + pos++ + "]", member, indent + 2);
                }
            }
        }
    }

    private static void enqueueChildren(Node node, Class<?> nodeClass, List<Field> children) {
        Iterator iter;
        ArrayDeque<Class<Object>> stack = new ArrayDeque<Class<Object>>();
        Class<?> clazz = nodeClass;
        do {
            stack.push(clazz);
        } while ((clazz = clazz.getSuperclass()) != null);
        if (node instanceof TernaryNode) {
            stack.push((Class<Object>)stack.removeLast());
        }
        Iterator iterator2 = iter = node instanceof BinaryNode ? stack.descendingIterator() : stack.iterator();
        while (iter.hasNext()) {
            Class c = (Class)iter.next();
            for (Field f : c.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    Object child = f.get(node);
                    if (child == null) continue;
                    if (child instanceof Node) {
                        children.add(f);
                        continue;
                    }
                    if (!(child instanceof Collection) || ((Collection)child).isEmpty()) continue;
                    children.add(f);
                }
                catch (IllegalAccessException | IllegalArgumentException e) {
                    return;
                }
            }
        }
    }

    private static void indent(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; ++i) {
            for (int j = 0; j < 4; ++j) {
                sb.append(' ');
            }
        }
    }
}

