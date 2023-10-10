// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.util.BitSet;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class PolygonsIntersection
{
    private static final ByStartComparator byStartComparator;
    private static final ByEndComparator byEndComparator;
    protected final float[] verticesXY;
    private float minX;
    private float minY;
    private float maxX;
    private float maxY;
    private float centerX;
    private float centerY;
    private float radiusSquared;
    private IntervalTreeNode tree;
    
    public PolygonsIntersection(final float[] verticesXY, final int[] polygons, final int count) {
        this.verticesXY = verticesXY;
        this.preprocess(count, polygons);
    }
    
    private IntervalTreeNode buildNode(final List intervals, final float center) {
        List left = null;
        List right = null;
        List byStart = null;
        List byEnd = null;
        float leftMin = 1.0E38f;
        float leftMax = -1.0E38f;
        float rightMin = 1.0E38f;
        float rightMax = -1.0E38f;
        float thisMin = 1.0E38f;
        float thisMax = -1.0E38f;
        for (int i = 0; i < intervals.size(); ++i) {
            final Interval ival = intervals.get(i);
            if (ival.start < center && ival.end < center) {
                if (left == null) {
                    left = new ArrayList();
                }
                left.add(ival);
                leftMin = ((leftMin < ival.start) ? leftMin : ival.start);
                leftMax = ((leftMax > ival.end) ? leftMax : ival.end);
            }
            else if (ival.start > center && ival.end > center) {
                if (right == null) {
                    right = new ArrayList();
                }
                right.add(ival);
                rightMin = ((rightMin < ival.start) ? rightMin : ival.start);
                rightMax = ((rightMax > ival.end) ? rightMax : ival.end);
            }
            else {
                if (byStart == null || byEnd == null) {
                    byStart = new ArrayList();
                    byEnd = new ArrayList();
                }
                thisMin = ((ival.start < thisMin) ? ival.start : thisMin);
                thisMax = ((ival.end > thisMax) ? ival.end : thisMax);
                byStart.add(ival);
                byEnd.add(ival);
            }
        }
        if (byStart != null) {
            Collections.sort((List<Object>)byStart, PolygonsIntersection.byStartComparator);
            Collections.sort((List<Object>)byEnd, PolygonsIntersection.byEndComparator);
        }
        final IntervalTreeNode tree = new IntervalTreeNode();
        tree.byBeginning = byStart;
        tree.byEnding = byEnd;
        tree.center = center;
        if (left != null) {
            tree.left = this.buildNode(left, (leftMin + leftMax) / 2.0f);
            tree.left.childrenMinMax = leftMax;
        }
        if (right != null) {
            tree.right = this.buildNode(right, (rightMin + rightMax) / 2.0f);
            tree.right.childrenMinMax = rightMin;
        }
        return tree;
    }
    
    private void preprocess(final int count, final int[] polygons) {
        int j = 0;
        final float n = 1.0E38f;
        this.minY = n;
        this.minX = n;
        final float n2 = -1.0E38f;
        this.maxY = n2;
        this.maxX = n2;
        final List intervals = new ArrayList(count);
        int first = 0;
        int currPoly = 0;
        int i;
        for (i = 1; i < count; ++i) {
            if (polygons != null && polygons.length > currPoly && polygons[currPoly] == i) {
                final float prevy = this.verticesXY[2 * (i - 1) + 1];
                final float firsty = this.verticesXY[2 * first + 1];
                final Interval ival = new Interval();
                ival.start = ((prevy < firsty) ? prevy : firsty);
                ival.end = ((firsty > prevy) ? firsty : prevy);
                ival.i = i - 1;
                ival.j = first;
                ival.polyIndex = currPoly;
                intervals.add(ival);
                first = i;
                ++currPoly;
                j = ++i - 1;
            }
            final float yi = this.verticesXY[2 * i + 1];
            final float xi = this.verticesXY[2 * i + 0];
            final float yj = this.verticesXY[2 * j + 1];
            this.minX = ((xi < this.minX) ? xi : this.minX);
            this.minY = ((yi < this.minY) ? yi : this.minY);
            this.maxX = ((xi > this.maxX) ? xi : this.maxX);
            this.maxY = ((yi > this.maxY) ? yi : this.maxY);
            final Interval ival2 = new Interval();
            ival2.start = ((yi < yj) ? yi : yj);
            ival2.end = ((yj > yi) ? yj : yi);
            ival2.i = i;
            ival2.j = j;
            ival2.polyIndex = currPoly;
            intervals.add(ival2);
            j = i;
        }
        final float yi = this.verticesXY[2 * (i - 1) + 1];
        final float xi = this.verticesXY[2 * (i - 1) + 0];
        final float yj = this.verticesXY[2 * first + 1];
        this.minX = ((xi < this.minX) ? xi : this.minX);
        this.minY = ((yi < this.minY) ? yi : this.minY);
        this.maxX = ((xi > this.maxX) ? xi : this.maxX);
        this.maxY = ((yi > this.maxY) ? yi : this.maxY);
        final Interval ival2 = new Interval();
        ival2.start = ((yi < yj) ? yi : yj);
        ival2.end = ((yj > yi) ? yj : yi);
        ival2.i = i - 1;
        ival2.j = first;
        ival2.polyIndex = currPoly;
        intervals.add(ival2);
        this.centerX = (this.maxX + this.minX) * 0.5f;
        this.centerY = (this.maxY + this.minY) * 0.5f;
        final float dx = this.maxX - this.centerX;
        final float dy = this.maxY - this.centerY;
        this.radiusSquared = dx * dx + dy * dy;
        this.tree = this.buildNode(intervals, this.centerY);
    }
    
    public boolean testPoint(final float x, final float y) {
        return this.testPoint(x, y, null);
    }
    
    public boolean testPoint(final float x, final float y, final BitSet inPolys) {
        final float dx = x - this.centerX;
        final float dy = y - this.centerY;
        if (inPolys != null) {
            inPolys.clear();
        }
        if (dx * dx + dy * dy > this.radiusSquared) {
            return false;
        }
        if (this.maxX < x || this.maxY < y || this.minX > x || this.minY > y) {
            return false;
        }
        final boolean res = this.tree.traverse(this.verticesXY, x, y, false, inPolys);
        return res;
    }
    
    static {
        byStartComparator = new ByStartComparator();
        byEndComparator = new ByEndComparator();
    }
    
    static class ByStartComparator implements Comparator
    {
        public int compare(final Object o1, final Object o2) {
            final Interval i1 = (Interval)o1;
            final Interval i2 = (Interval)o2;
            return Float.compare(i1.start, i2.start);
        }
    }
    
    static class ByEndComparator implements Comparator
    {
        public int compare(final Object o1, final Object o2) {
            final Interval i1 = (Interval)o1;
            final Interval i2 = (Interval)o2;
            return Float.compare(i2.end, i1.end);
        }
    }
    
    static class Interval
    {
        float start;
        float end;
        int i;
        int j;
        int polyIndex;
    }
    
    static class IntervalTreeNode
    {
        float center;
        float childrenMinMax;
        IntervalTreeNode left;
        IntervalTreeNode right;
        List byBeginning;
        List byEnding;
        
        static boolean computeEvenOdd(final float[] verticesXY, final Interval ival, final float x, final float y, final boolean evenOdd, final BitSet inPolys) {
            boolean newEvenOdd = evenOdd;
            final int i = ival.i;
            final int j = ival.j;
            final float yi = verticesXY[2 * i + 1];
            final float yj = verticesXY[2 * j + 1];
            final float xi = verticesXY[2 * i + 0];
            final float xj = verticesXY[2 * j + 0];
            if (((yi < y && yj >= y) || (yj < y && yi >= y)) && (xi <= x || xj <= x)) {
                final float xDist = xi + (y - yi) / (yj - yi) * (xj - xi) - x;
                newEvenOdd ^= (xDist < 0.0f);
                if (newEvenOdd != evenOdd && inPolys != null) {
                    inPolys.flip(ival.polyIndex);
                }
            }
            return newEvenOdd;
        }
        
        boolean traverse(final float[] verticesXY, final float x, final float y, final boolean evenOdd, final BitSet inPolys) {
            boolean newEvenOdd = evenOdd;
            if (y == this.center && this.byBeginning != null) {
                for (int size = this.byBeginning.size(), b = 0; b < size; ++b) {
                    final Interval ival = this.byBeginning.get(b);
                    newEvenOdd = computeEvenOdd(verticesXY, ival, x, y, newEvenOdd, inPolys);
                }
            }
            else if (y < this.center) {
                if (this.left != null && this.left.childrenMinMax >= y) {
                    newEvenOdd = this.left.traverse(verticesXY, x, y, newEvenOdd, inPolys);
                }
                if (this.byBeginning != null) {
                    for (int size = this.byBeginning.size(), b = 0; b < size; ++b) {
                        final Interval ival = this.byBeginning.get(b);
                        if (ival.start > y) {
                            break;
                        }
                        newEvenOdd = computeEvenOdd(verticesXY, ival, x, y, newEvenOdd, inPolys);
                    }
                }
            }
            else if (y > this.center) {
                if (this.right != null && this.right.childrenMinMax <= y) {
                    newEvenOdd = this.right.traverse(verticesXY, x, y, newEvenOdd, inPolys);
                }
                if (this.byEnding != null) {
                    for (int size = this.byEnding.size(), b = 0; b < size; ++b) {
                        final Interval ival = this.byEnding.get(b);
                        if (ival.end < y) {
                            break;
                        }
                        newEvenOdd = computeEvenOdd(verticesXY, ival, x, y, newEvenOdd, inPolys);
                    }
                }
            }
            return newEvenOdd;
        }
    }
}
