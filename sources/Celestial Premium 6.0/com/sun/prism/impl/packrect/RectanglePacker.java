/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.packrect;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Texture;
import com.sun.prism.impl.packrect.Level;
import java.util.ArrayList;
import java.util.List;

public class RectanglePacker {
    private Texture backingStore;
    private List<Level> levels = new ArrayList<Level>(150);
    private static final int MIN_SIZE = 8;
    private static final int ROUND_UP = 4;
    private int recentUsedLevelIndex = 0;
    private int length;
    private int size;
    private int sizeOffset;
    private int x;
    private int y;
    private boolean vertical;

    public RectanglePacker(Texture texture, int n, int n2, int n3, int n4, boolean bl) {
        this.backingStore = texture;
        if (bl) {
            this.length = n4;
            this.size = n3;
        } else {
            this.length = n3;
            this.size = n4;
        }
        this.x = n;
        this.y = n2;
        this.vertical = bl;
    }

    public RectanglePacker(Texture texture, int n, int n2) {
        this(texture, 0, 0, n, n2, false);
    }

    public final Texture getBackingStore() {
        return this.backingStore;
    }

    public final boolean add(Rectangle rectangle) {
        int n;
        int n2 = this.vertical ? rectangle.height : rectangle.width;
        int n3 = n = this.vertical ? rectangle.width : rectangle.height;
        if (n2 > this.length) {
            return false;
        }
        if (n > this.size) {
            return false;
        }
        int n4 = 8 > n ? 8 : n;
        n4 = n4 + 4 - 1 - (n4 - 1) % 4;
        int n5 = this.recentUsedLevelIndex < this.levels.size() && this.levels.get((int)this.recentUsedLevelIndex).size != n4 ? RectanglePacker.binarySearch(this.levels, n4) : this.recentUsedLevelIndex;
        boolean bl = this.sizeOffset + n4 <= this.size;
        int n6 = this.levels.size();
        for (int i = n5; i < n6; ++i) {
            Level level = this.levels.get(i);
            if (level.size > n4 + 8 && bl) break;
            if (!level.add(rectangle, this.x, this.y, n2, n, this.vertical)) continue;
            this.recentUsedLevelIndex = i;
            return true;
        }
        if (!bl) {
            return false;
        }
        Level level = new Level(this.length, n4, this.sizeOffset);
        this.sizeOffset += n4;
        if (n5 < this.levels.size() && this.levels.get((int)n5).size <= n4) {
            this.levels.add(n5 + 1, level);
            this.recentUsedLevelIndex = n5 + 1;
        } else {
            this.levels.add(n5, level);
            this.recentUsedLevelIndex = n5;
        }
        return level.add(rectangle, this.x, this.y, n2, n, this.vertical);
    }

    public void clear() {
        this.levels.clear();
        this.sizeOffset = 0;
        this.recentUsedLevelIndex = 0;
    }

    public void dispose() {
        if (this.backingStore != null) {
            this.backingStore.dispose();
        }
        this.backingStore = null;
        this.levels = null;
    }

    private static int binarySearch(List<Level> list, int n) {
        int n2 = n + 1;
        int n3 = 0;
        int n4 = list.size() - 1;
        int n5 = 0;
        int n6 = 0;
        if (n4 < 0) {
            return 0;
        }
        while (n3 <= n4) {
            n5 = (n3 + n4) / 2;
            n6 = list.get((int)n5).size;
            if (n2 < n6) {
                n4 = n5 - 1;
                continue;
            }
            n3 = n5 + 1;
        }
        if (n6 < n) {
            return n5 + 1;
        }
        if (n6 > n) {
            return n5 > 0 ? n5 - 1 : 0;
        }
        return n5;
    }
}

