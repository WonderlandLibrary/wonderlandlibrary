/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.RectBounds;
import java.util.Arrays;

public final class DirtyRegionContainer {
    public static final int DTR_OK = 1;
    public static final int DTR_CONTAINS_CLIP = 0;
    private RectBounds[] dirtyRegions;
    private int emptyIndex;
    private int[][] heap;
    private int heapSize;
    private long invalidMask;

    public DirtyRegionContainer(int n) {
        this.initDirtyRegions(n);
    }

    public boolean equals(Object object) {
        if (object instanceof DirtyRegionContainer) {
            DirtyRegionContainer dirtyRegionContainer = (DirtyRegionContainer)object;
            if (this.size() != dirtyRegionContainer.size()) {
                return false;
            }
            for (int i = 0; i < this.emptyIndex; ++i) {
                if (this.getDirtyRegion(i).equals(dirtyRegionContainer.getDirtyRegion(i))) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int n = 5;
        n = 97 * n + Arrays.deepHashCode(this.dirtyRegions);
        n = 97 * n + this.emptyIndex;
        return n;
    }

    public DirtyRegionContainer deriveWithNewRegion(RectBounds rectBounds) {
        if (rectBounds == null) {
            return this;
        }
        this.dirtyRegions[0].deriveWithNewBounds(rectBounds);
        this.emptyIndex = 1;
        return this;
    }

    public DirtyRegionContainer deriveWithNewRegions(RectBounds[] arrrectBounds) {
        if (arrrectBounds == null || arrrectBounds.length == 0) {
            return this;
        }
        if (arrrectBounds.length > this.maxSpace()) {
            this.initDirtyRegions(arrrectBounds.length);
        }
        this.regioncopy(arrrectBounds, 0, this.dirtyRegions, 0, arrrectBounds.length);
        this.emptyIndex = arrrectBounds.length;
        return this;
    }

    public DirtyRegionContainer deriveWithNewContainer(DirtyRegionContainer dirtyRegionContainer) {
        if (dirtyRegionContainer == null || dirtyRegionContainer.maxSpace() == 0) {
            return this;
        }
        if (dirtyRegionContainer.maxSpace() > this.maxSpace()) {
            this.initDirtyRegions(dirtyRegionContainer.maxSpace());
        }
        this.regioncopy(dirtyRegionContainer.dirtyRegions, 0, this.dirtyRegions, 0, dirtyRegionContainer.emptyIndex);
        this.emptyIndex = dirtyRegionContainer.emptyIndex;
        return this;
    }

    private void initDirtyRegions(int n) {
        this.dirtyRegions = new RectBounds[n];
        for (int i = 0; i < n; ++i) {
            this.dirtyRegions[i] = new RectBounds();
        }
        this.emptyIndex = 0;
    }

    public DirtyRegionContainer copy() {
        DirtyRegionContainer dirtyRegionContainer = new DirtyRegionContainer(this.maxSpace());
        this.regioncopy(this.dirtyRegions, 0, dirtyRegionContainer.dirtyRegions, 0, this.emptyIndex);
        dirtyRegionContainer.emptyIndex = this.emptyIndex;
        return dirtyRegionContainer;
    }

    public int maxSpace() {
        return this.dirtyRegions.length;
    }

    public RectBounds getDirtyRegion(int n) {
        return this.dirtyRegions[n];
    }

    public void setDirtyRegion(int n, RectBounds rectBounds) {
        this.dirtyRegions[n] = rectBounds;
    }

    public void addDirtyRegion(RectBounds rectBounds) {
        RectBounds rectBounds2;
        if (rectBounds.isEmpty()) {
            return;
        }
        int n = 0;
        int n2 = this.emptyIndex;
        for (int i = 0; i < n2; ++i) {
            rectBounds2 = this.dirtyRegions[n];
            if (rectBounds.intersects(rectBounds2)) {
                rectBounds.unionWith(rectBounds2);
                RectBounds rectBounds3 = this.dirtyRegions[n];
                this.dirtyRegions[n] = this.dirtyRegions[this.emptyIndex - 1];
                this.dirtyRegions[this.emptyIndex - 1] = rectBounds3;
                --this.emptyIndex;
                continue;
            }
            ++n;
        }
        if (this.hasSpace()) {
            rectBounds2 = this.dirtyRegions[this.emptyIndex];
            rectBounds2.deriveWithNewBounds(rectBounds);
            ++this.emptyIndex;
            return;
        }
        if (this.dirtyRegions.length == 1) {
            this.dirtyRegions[0].deriveWithUnion(rectBounds);
        } else {
            this.compress(rectBounds);
        }
    }

    public void merge(DirtyRegionContainer dirtyRegionContainer) {
        int n = dirtyRegionContainer.size();
        for (int i = 0; i < n; ++i) {
            this.addDirtyRegion(dirtyRegionContainer.getDirtyRegion(i));
        }
    }

    public int size() {
        return this.emptyIndex;
    }

    public void reset() {
        this.emptyIndex = 0;
    }

    private RectBounds compress(RectBounds rectBounds) {
        this.compress_heap();
        this.addDirtyRegion(rectBounds);
        return rectBounds;
    }

    private boolean hasSpace() {
        return this.emptyIndex < this.dirtyRegions.length;
    }

    private void regioncopy(RectBounds[] arrrectBounds, int n, RectBounds[] arrrectBounds2, int n2, int n3) {
        for (int i = 0; i < n3; ++i) {
            RectBounds rectBounds;
            if ((rectBounds = arrrectBounds[n++]) == null) {
                arrrectBounds2[n2++].makeEmpty();
                continue;
            }
            arrrectBounds2[n2++].deriveWithNewBounds(rectBounds);
        }
    }

    public boolean checkAndClearRegion(int n) {
        boolean bl = false;
        if (this.dirtyRegions[n].isEmpty()) {
            System.arraycopy(this.dirtyRegions, n + 1, this.dirtyRegions, n, this.emptyIndex - n - 1);
            --this.emptyIndex;
            bl = true;
        }
        return bl;
    }

    public void grow(int n, int n2) {
        if (n != 0 || n2 != 0) {
            for (int i = 0; i < this.emptyIndex; ++i) {
                this.getDirtyRegion(i).grow(n, n2);
            }
        }
    }

    public void roundOut() {
        for (int i = 0; i < this.emptyIndex; ++i) {
            this.dirtyRegions[i].roundOut();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.emptyIndex; ++i) {
            stringBuilder.append(this.dirtyRegions[i]);
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    private void heapCompress() {
        int n;
        this.invalidMask = 0L;
        int[] arrn = new int[this.dirtyRegions.length];
        for (int i = 0; i < arrn.length; ++i) {
            arrn[i] = i;
        }
        for (int i = 0; i < this.dirtyRegions.length / 2; ++i) {
            int n2;
            int[] arrn2 = this.takeMinWithMap(arrn);
            n = this.resolveMap(arrn, arrn2[1]);
            if (n == (n2 = this.resolveMap(arrn, arrn2[2]))) continue;
            this.dirtyRegions[n].deriveWithUnion(this.dirtyRegions[n2]);
            arrn[n2] = n;
            this.invalidMask |= (long)(1 << n);
            this.invalidMask |= (long)(1 << n2);
        }
        for (n = 0; n < this.emptyIndex; ++n) {
            if (arrn[n] == n) continue;
            while (arrn[this.emptyIndex - 1] != this.emptyIndex - 1) {
                --this.emptyIndex;
            }
            if (n >= this.emptyIndex - 1) continue;
            RectBounds rectBounds = this.dirtyRegions[this.emptyIndex - 1];
            this.dirtyRegions[this.emptyIndex - 1] = this.dirtyRegions[n];
            this.dirtyRegions[n] = rectBounds;
            arrn[n] = n;
            --this.emptyIndex;
        }
    }

    private void heapify() {
        for (int i = this.heapSize / 2; i >= 0; --i) {
            this.siftDown(i);
        }
    }

    private void siftDown(int n) {
        int n2 = this.heapSize >> 1;
        while (n < n2) {
            int n3 = (n << 1) + 1;
            int[] arrn = this.heap[n3];
            if (n3 + 1 < this.heapSize && this.heap[n3 + 1][0] < arrn[0]) {
                ++n3;
            }
            if (this.heap[n3][0] >= this.heap[n][0]) break;
            int[] arrn2 = this.heap[n3];
            this.heap[n3] = this.heap[n];
            this.heap[n] = arrn2;
            n = n3;
        }
    }

    private int[] takeMinWithMap(int[] arrn) {
        int[] arrn2 = this.heap[0];
        while (((long)(1 << arrn2[1] | 1 << arrn2[2]) & this.invalidMask) > 0L) {
            arrn2[0] = this.unifiedRegionArea(this.resolveMap(arrn, arrn2[1]), this.resolveMap(arrn, arrn2[2]));
            this.siftDown(0);
            if (this.heap[0] == arrn2) break;
            arrn2 = this.heap[0];
        }
        this.heap[this.heapSize - 1] = arrn2;
        this.siftDown(0);
        --this.heapSize;
        return arrn2;
    }

    private int[] takeMin() {
        int[] arrn = this.heap[0];
        this.heap[0] = this.heap[this.heapSize - 1];
        this.heap[this.heapSize - 1] = arrn;
        this.siftDown(0);
        --this.heapSize;
        return arrn;
    }

    private int resolveMap(int[] arrn, int n) {
        while (arrn[n] != n) {
            n = arrn[n];
        }
        return n;
    }

    private int unifiedRegionArea(int n, int n2) {
        RectBounds rectBounds = this.dirtyRegions[n];
        RectBounds rectBounds2 = this.dirtyRegions[n2];
        float f = rectBounds.getMinX() < rectBounds2.getMinX() ? rectBounds.getMinX() : rectBounds2.getMinX();
        float f2 = rectBounds.getMinY() < rectBounds2.getMinY() ? rectBounds.getMinY() : rectBounds2.getMinY();
        float f3 = rectBounds.getMaxX() > rectBounds2.getMaxX() ? rectBounds.getMaxX() : rectBounds2.getMaxX();
        float f4 = rectBounds.getMaxY() > rectBounds2.getMaxY() ? rectBounds.getMaxY() : rectBounds2.getMaxY();
        return (int)((f3 - f) * (f4 - f2));
    }

    private void compress_heap() {
        int n;
        assert (this.dirtyRegions.length == this.emptyIndex);
        if (this.heap == null) {
            n = this.dirtyRegions.length;
            this.heap = new int[n * (n - 1) / 2][3];
        }
        this.heapSize = this.heap.length;
        n = 0;
        for (int i = 0; i < this.dirtyRegions.length - 1; ++i) {
            int n2 = i + 1;
            while (n2 < this.dirtyRegions.length) {
                this.heap[n][0] = this.unifiedRegionArea(i, n2);
                this.heap[n][1] = i;
                this.heap[n++][2] = n2++;
            }
        }
        this.heapify();
        this.heapCompress();
    }
}

