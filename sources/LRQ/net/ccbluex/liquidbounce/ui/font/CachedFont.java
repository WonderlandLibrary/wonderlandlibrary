/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.font;

import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public final class CachedFont {
    private final int displayList;
    private long lastUsage;
    private boolean deleted;

    protected final void finalize() {
        if (!this.deleted) {
            GL11.glDeleteLists((int)this.displayList, (int)1);
        }
    }

    public final int getDisplayList() {
        return this.displayList;
    }

    public final long getLastUsage() {
        return this.lastUsage;
    }

    public final void setLastUsage(long l) {
        this.lastUsage = l;
    }

    public final boolean getDeleted() {
        return this.deleted;
    }

    public final void setDeleted(boolean bl) {
        this.deleted = bl;
    }

    public CachedFont(int displayList, long lastUsage, boolean deleted) {
        this.displayList = displayList;
        this.lastUsage = lastUsage;
        this.deleted = deleted;
    }

    public /* synthetic */ CachedFont(int n, long l, boolean bl, int n2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n2 & 4) != 0) {
            bl = false;
        }
        this(n, l, bl);
    }

    public final int component1() {
        return this.displayList;
    }

    public final long component2() {
        return this.lastUsage;
    }

    public final boolean component3() {
        return this.deleted;
    }

    public final CachedFont copy(int displayList, long lastUsage, boolean deleted) {
        return new CachedFont(displayList, lastUsage, deleted);
    }

    public static /* synthetic */ CachedFont copy$default(CachedFont cachedFont, int n, long l, boolean bl, int n2, Object object) {
        if ((n2 & 1) != 0) {
            n = cachedFont.displayList;
        }
        if ((n2 & 2) != 0) {
            l = cachedFont.lastUsage;
        }
        if ((n2 & 4) != 0) {
            bl = cachedFont.deleted;
        }
        return cachedFont.copy(n, l, bl);
    }

    public String toString() {
        return "CachedFont(displayList=" + this.displayList + ", lastUsage=" + this.lastUsage + ", deleted=" + this.deleted + ")";
    }

    public int hashCode() {
        int n = (Integer.hashCode(this.displayList) * 31 + Long.hashCode(this.lastUsage)) * 31;
        int n2 = this.deleted ? 1 : 0;
        if (n2 != 0) {
            n2 = 1;
        }
        return n + n2;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof CachedFont)) break block3;
                CachedFont cachedFont = (CachedFont)object;
                if (this.displayList != cachedFont.displayList || this.lastUsage != cachedFont.lastUsage || this.deleted != cachedFont.deleted) break block3;
            }
            return true;
        }
        return false;
    }
}

