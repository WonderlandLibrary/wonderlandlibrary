/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Quantizer;
import java.io.PrintStream;
import java.util.Vector;

public class OctTreeQuantizer
implements Quantizer {
    static final int MAX_LEVEL = 5;
    private int nodes = 0;
    private OctTreeNode root;
    private int reduceColors;
    private int maximumColors;
    private int colors = 0;
    private Vector[] colorList;

    public OctTreeQuantizer() {
        this.setup(256);
        this.colorList = new Vector[6];
        for (int i = 0; i < 6; ++i) {
            this.colorList[i] = new Vector();
        }
        this.root = new OctTreeNode();
    }

    @Override
    public void setup(int numColors) {
        this.maximumColors = numColors;
        this.reduceColors = Math.max(512, numColors * 2);
    }

    @Override
    public void addPixels(int[] pixels, int offset, int count) {
        for (int i = 0; i < count; ++i) {
            this.insertColor(pixels[i + offset]);
            if (this.colors <= this.reduceColors) continue;
            this.reduceTree(this.reduceColors);
        }
    }

    @Override
    public int getIndexForColor(int rgb) {
        int red = rgb >> 16 & 0xFF;
        int green = rgb >> 8 & 0xFF;
        int blue = rgb & 0xFF;
        OctTreeNode node = this.root;
        for (int level = 0; level <= 5; ++level) {
            OctTreeNode child;
            int bit = 128 >> level;
            int index = 0;
            if ((red & bit) != 0) {
                index += 4;
            }
            if ((green & bit) != 0) {
                index += 2;
            }
            if ((blue & bit) != 0) {
                ++index;
            }
            if ((child = node.leaf[index]) == null) {
                return node.index;
            }
            if (child.isLeaf) {
                return child.index;
            }
            node = child;
        }
        System.out.println("getIndexForColor failed");
        return 0;
    }

    private void insertColor(int rgb) {
        int red = rgb >> 16 & 0xFF;
        int green = rgb >> 8 & 0xFF;
        int blue = rgb & 0xFF;
        OctTreeNode node = this.root;
        for (int level = 0; level <= 5; ++level) {
            OctTreeNode child;
            int bit = 128 >> level;
            int index = 0;
            if ((red & bit) != 0) {
                index += 4;
            }
            if ((green & bit) != 0) {
                index += 2;
            }
            if ((blue & bit) != 0) {
                ++index;
            }
            if ((child = node.leaf[index]) == null) {
                ++node.children;
                child = new OctTreeNode();
                child.parent = node;
                node.leaf[index] = child;
                node.isLeaf = false;
                ++this.nodes;
                this.colorList[level].addElement(child);
                if (level == 5) {
                    child.isLeaf = true;
                    child.count = 1;
                    child.totalRed = red;
                    child.totalGreen = green;
                    child.totalBlue = blue;
                    child.level = level;
                    ++this.colors;
                    return;
                }
                node = child;
                continue;
            }
            if (child.isLeaf) {
                ++child.count;
                child.totalRed += red;
                child.totalGreen += green;
                child.totalBlue += blue;
                return;
            }
            node = child;
        }
        System.out.println("insertColor failed");
    }

    private void reduceTree(int numColors) {
        for (int level = 4; level >= 0; --level) {
            Vector v = this.colorList[level];
            if (v == null || v.size() <= 0) continue;
            for (int j = 0; j < v.size(); ++j) {
                OctTreeNode node = (OctTreeNode)v.elementAt(j);
                if (node.children <= 0) continue;
                for (int i = 0; i < 8; ++i) {
                    OctTreeNode child = node.leaf[i];
                    if (child == null) continue;
                    if (!child.isLeaf) {
                        System.out.println("not a leaf!");
                    }
                    node.count += child.count;
                    node.totalRed += child.totalRed;
                    node.totalGreen += child.totalGreen;
                    node.totalBlue += child.totalBlue;
                    node.leaf[i] = null;
                    --node.children;
                    --this.colors;
                    --this.nodes;
                    this.colorList[level + 1].removeElement(child);
                }
                node.isLeaf = true;
                ++this.colors;
                if (this.colors > numColors) continue;
                return;
            }
        }
        System.out.println("Unable to reduce the OctTree");
    }

    @Override
    public int[] buildColorTable() {
        int[] table = new int[this.colors];
        this.buildColorTable(this.root, table, 0);
        return table;
    }

    public void buildColorTable(int[] inPixels, int[] table) {
        int count = inPixels.length;
        this.maximumColors = table.length;
        for (int i = 0; i < count; ++i) {
            this.insertColor(inPixels[i]);
            if (this.colors <= this.reduceColors) continue;
            this.reduceTree(this.reduceColors);
        }
        if (this.colors > this.maximumColors) {
            this.reduceTree(this.maximumColors);
        }
        this.buildColorTable(this.root, table, 0);
    }

    private int buildColorTable(OctTreeNode node, int[] table, int index) {
        if (this.colors > this.maximumColors) {
            this.reduceTree(this.maximumColors);
        }
        if (node.isLeaf) {
            int count = node.count;
            table[index] = 0xFF000000 | node.totalRed / count << 16 | node.totalGreen / count << 8 | node.totalBlue / count;
            node.index = index++;
        } else {
            for (int i = 0; i < 8; ++i) {
                if (node.leaf[i] == null) continue;
                node.index = index;
                index = this.buildColorTable(node.leaf[i], table, index);
            }
        }
        return index;
    }

    class OctTreeNode {
        int children;
        int level;
        OctTreeNode parent;
        OctTreeNode[] leaf = new OctTreeNode[8];
        boolean isLeaf;
        int count;
        int totalRed;
        int totalGreen;
        int totalBlue;
        int index;

        OctTreeNode() {
        }

        public void list(PrintStream s, int level) {
            int i;
            for (i = 0; i < level; ++i) {
                System.out.print(' ');
            }
            if (this.count == 0) {
                System.out.println(String.valueOf(this.index) + ": count=" + this.count);
            } else {
                System.out.println(String.valueOf(this.index) + ": count=" + this.count + " red=" + this.totalRed / this.count + " green=" + this.totalGreen / this.count + " blue=" + this.totalBlue / this.count);
            }
            for (i = 0; i < 8; ++i) {
                if (this.leaf[i] == null) continue;
                this.leaf[i].list(s, level + 2);
            }
        }
    }
}

