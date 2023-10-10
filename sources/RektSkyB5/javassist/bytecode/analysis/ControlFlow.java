/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode.analysis;

import java.util.ArrayList;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.analysis.Analyzer;
import javassist.bytecode.analysis.Frame;
import javassist.bytecode.stackmap.BasicBlock;

public class ControlFlow {
    private CtClass clazz;
    private MethodInfo methodInfo;
    private Block[] basicBlocks;
    private Frame[] frames;

    public ControlFlow(CtMethod method) throws BadBytecode {
        this(method.getDeclaringClass(), method.getMethodInfo2());
    }

    public ControlFlow(CtClass ctclazz, MethodInfo minfo) throws BadBytecode {
        Block b2;
        int i2;
        this.clazz = ctclazz;
        this.methodInfo = minfo;
        this.frames = null;
        this.basicBlocks = (Block[])new BasicBlock.Maker(){

            @Override
            protected BasicBlock makeBlock(int pos) {
                return new Block(pos, ControlFlow.this.methodInfo);
            }

            @Override
            protected BasicBlock[] makeArray(int size) {
                return new Block[size];
            }
        }.make(minfo);
        if (this.basicBlocks == null) {
            this.basicBlocks = new Block[0];
        }
        int size = this.basicBlocks.length;
        int[] counters = new int[size];
        for (i2 = 0; i2 < size; ++i2) {
            b2 = this.basicBlocks[i2];
            b2.index = i2;
            b2.entrances = new Block[b2.incomings()];
            counters[i2] = 0;
        }
        for (i2 = 0; i2 < size; ++i2) {
            b2 = this.basicBlocks[i2];
            for (int k2 = 0; k2 < b2.exits(); ++k2) {
                Block e2 = b2.exit(k2);
                int n2 = e2.index;
                int n3 = counters[n2];
                counters[n2] = n3 + 1;
                e2.entrances[n3] = b2;
            }
            Catcher[] catchers = b2.catchers();
            for (int k3 = 0; k3 < catchers.length; ++k3) {
                Block catchBlock = catchers[k3].node;
                int n4 = catchBlock.index;
                int n5 = counters[n4];
                counters[n4] = n5 + 1;
                catchBlock.entrances[n5] = b2;
            }
        }
    }

    public Block[] basicBlocks() {
        return this.basicBlocks;
    }

    public Frame frameAt(int pos) throws BadBytecode {
        if (this.frames == null) {
            this.frames = new Analyzer().analyze(this.clazz, this.methodInfo);
        }
        return this.frames[pos];
    }

    public Node[] dominatorTree() {
        int size = this.basicBlocks.length;
        if (size == 0) {
            return null;
        }
        Node[] nodes = new Node[size];
        boolean[] visited = new boolean[size];
        int[] distance = new int[size];
        for (int i2 = 0; i2 < size; ++i2) {
            nodes[i2] = new Node(this.basicBlocks[i2]);
            visited[i2] = false;
        }
        Access access = new Access(nodes){

            @Override
            BasicBlock[] exits(Node n2) {
                return n2.block.getExit();
            }

            @Override
            BasicBlock[] entrances(Node n2) {
                return ((Node)n2).block.entrances;
            }
        };
        nodes[0].makeDepth1stTree(null, visited, 0, distance, access);
        do {
            for (int i3 = 0; i3 < size; ++i3) {
                visited[i3] = false;
            }
        } while (nodes[0].makeDominatorTree(visited, distance, access));
        Node.setChildren(nodes);
        return nodes;
    }

    public Node[] postDominatorTree() {
        boolean changed;
        int size = this.basicBlocks.length;
        if (size == 0) {
            return null;
        }
        Node[] nodes = new Node[size];
        boolean[] visited = new boolean[size];
        int[] distance = new int[size];
        for (int i2 = 0; i2 < size; ++i2) {
            nodes[i2] = new Node(this.basicBlocks[i2]);
            visited[i2] = false;
        }
        Access access = new Access(nodes){

            @Override
            BasicBlock[] exits(Node n2) {
                return ((Node)n2).block.entrances;
            }

            @Override
            BasicBlock[] entrances(Node n2) {
                return n2.block.getExit();
            }
        };
        int counter = 0;
        for (int i3 = 0; i3 < size; ++i3) {
            if (nodes[i3].block.exits() != 0) continue;
            counter = nodes[i3].makeDepth1stTree(null, visited, counter, distance, access);
        }
        do {
            int i4;
            for (i4 = 0; i4 < size; ++i4) {
                visited[i4] = false;
            }
            changed = false;
            for (i4 = 0; i4 < size; ++i4) {
                if (nodes[i4].block.exits() != 0 || !nodes[i4].makeDominatorTree(visited, distance, access)) continue;
                changed = true;
            }
        } while (changed);
        Node.setChildren(nodes);
        return nodes;
    }

    public static class Catcher {
        private Block node;
        private int typeIndex;

        Catcher(BasicBlock.Catch c2) {
            this.node = (Block)c2.body;
            this.typeIndex = c2.typeIndex;
        }

        public Block block() {
            return this.node;
        }

        public String type() {
            if (this.typeIndex == 0) {
                return "java.lang.Throwable";
            }
            return this.node.method.getConstPool().getClassInfo(this.typeIndex);
        }
    }

    public static class Node {
        private Block block;
        private Node parent;
        private Node[] children;

        Node(Block b2) {
            this.block = b2;
            this.parent = null;
        }

        public String toString() {
            StringBuffer sbuf = new StringBuffer();
            sbuf.append("Node[pos=").append(this.block().position());
            sbuf.append(", parent=");
            sbuf.append(this.parent == null ? "*" : Integer.toString(this.parent.block().position()));
            sbuf.append(", children{");
            for (int i2 = 0; i2 < this.children.length; ++i2) {
                sbuf.append(this.children[i2].block().position()).append(", ");
            }
            sbuf.append("}]");
            return sbuf.toString();
        }

        public Block block() {
            return this.block;
        }

        public Node parent() {
            return this.parent;
        }

        public int children() {
            return this.children.length;
        }

        public Node child(int n2) {
            return this.children[n2];
        }

        int makeDepth1stTree(Node caller, boolean[] visited, int counter, int[] distance, Access access) {
            int index = this.block.index;
            if (visited[index]) {
                return counter;
            }
            visited[index] = true;
            this.parent = caller;
            BasicBlock[] exits = access.exits(this);
            if (exits != null) {
                for (int i2 = 0; i2 < exits.length; ++i2) {
                    Node n2 = access.node(exits[i2]);
                    counter = n2.makeDepth1stTree(this, visited, counter, distance, access);
                }
            }
            distance[index] = counter++;
            return counter;
        }

        boolean makeDominatorTree(boolean[] visited, int[] distance, Access access) {
            BasicBlock[] entrances;
            int index = this.block.index;
            if (visited[index]) {
                return false;
            }
            visited[index] = true;
            boolean changed = false;
            BasicBlock[] exits = access.exits(this);
            if (exits != null) {
                for (int i2 = 0; i2 < exits.length; ++i2) {
                    Node n2 = access.node(exits[i2]);
                    if (!n2.makeDominatorTree(visited, distance, access)) continue;
                    changed = true;
                }
            }
            if ((entrances = access.entrances(this)) != null) {
                for (int i3 = 0; i3 < entrances.length; ++i3) {
                    Node n3;
                    if (this.parent == null || (n3 = Node.getAncestor(this.parent, access.node(entrances[i3]), distance)) == this.parent) continue;
                    this.parent = n3;
                    changed = true;
                }
            }
            return changed;
        }

        private static Node getAncestor(Node n1, Node n2, int[] distance) {
            while (n1 != n2) {
                if (distance[n1.block.index] < distance[n2.block.index]) {
                    n1 = n1.parent;
                } else {
                    n2 = n2.parent;
                }
                if (n1 != null && n2 != null) continue;
                return null;
            }
            return n1;
        }

        private static void setChildren(Node[] all) {
            int i2;
            int size = all.length;
            int[] nchildren = new int[size];
            for (i2 = 0; i2 < size; ++i2) {
                nchildren[i2] = 0;
            }
            for (i2 = 0; i2 < size; ++i2) {
                Node p2 = all[i2].parent;
                if (p2 == null) continue;
                int n2 = p2.block.index;
                nchildren[n2] = nchildren[n2] + 1;
            }
            for (i2 = 0; i2 < size; ++i2) {
                all[i2].children = new Node[nchildren[i2]];
            }
            for (i2 = 0; i2 < size; ++i2) {
                nchildren[i2] = 0;
            }
            for (i2 = 0; i2 < size; ++i2) {
                Node n3 = all[i2];
                Node p3 = n3.parent;
                if (p3 == null) continue;
                int n4 = p3.block.index;
                int n5 = nchildren[n4];
                nchildren[n4] = n5 + 1;
                p3.children[n5] = n3;
            }
        }
    }

    static abstract class Access {
        Node[] all;

        Access(Node[] nodes) {
            this.all = nodes;
        }

        Node node(BasicBlock b2) {
            return this.all[((Block)b2).index];
        }

        abstract BasicBlock[] exits(Node var1);

        abstract BasicBlock[] entrances(Node var1);
    }

    public static class Block
    extends BasicBlock {
        public Object clientData = null;
        int index;
        MethodInfo method;
        Block[] entrances;

        Block(int pos, MethodInfo minfo) {
            super(pos);
            this.method = minfo;
        }

        @Override
        protected void toString2(StringBuffer sbuf) {
            super.toString2(sbuf);
            sbuf.append(", incoming{");
            for (int i2 = 0; i2 < this.entrances.length; ++i2) {
                sbuf.append(this.entrances[i2].position).append(", ");
            }
            sbuf.append("}");
        }

        BasicBlock[] getExit() {
            return this.exit;
        }

        public int index() {
            return this.index;
        }

        public int position() {
            return this.position;
        }

        public int length() {
            return this.length;
        }

        public int incomings() {
            return this.incoming;
        }

        public Block incoming(int n2) {
            return this.entrances[n2];
        }

        public int exits() {
            return this.exit == null ? 0 : this.exit.length;
        }

        public Block exit(int n2) {
            return (Block)this.exit[n2];
        }

        public Catcher[] catchers() {
            ArrayList<Catcher> catchers = new ArrayList<Catcher>();
            BasicBlock.Catch c2 = this.toCatch;
            while (c2 != null) {
                catchers.add(new Catcher(c2));
                c2 = c2.next;
            }
            return catchers.toArray(new Catcher[catchers.size()]);
        }
    }
}

