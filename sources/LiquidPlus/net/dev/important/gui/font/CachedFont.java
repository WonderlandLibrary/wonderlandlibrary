/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.gui.font;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0012\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0007H\u00c6\u0003J'\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00072\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\b\u0010\u0019\u001a\u00020\u001aH\u0004J\t\u0010\u001b\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006\u001e"}, d2={"Lnet/dev/important/gui/font/CachedFont;", "", "displayList", "", "lastUsage", "", "deleted", "", "(IJZ)V", "getDeleted", "()Z", "setDeleted", "(Z)V", "getDisplayList", "()I", "getLastUsage", "()J", "setLastUsage", "(J)V", "component1", "component2", "component3", "copy", "equals", "other", "finalize", "", "hashCode", "toString", "", "LiquidBounce"})
public final class CachedFont {
    private final int displayList;
    private long lastUsage;
    private boolean deleted;

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

    protected final void finalize() {
        if (!this.deleted) {
            GL11.glDeleteLists((int)this.displayList, (int)1);
        }
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

    @NotNull
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

    @NotNull
    public String toString() {
        return "CachedFont(displayList=" + this.displayList + ", lastUsage=" + this.lastUsage + ", deleted=" + this.deleted + ')';
    }

    public int hashCode() {
        int result = Integer.hashCode(this.displayList);
        result = result * 31 + Long.hashCode(this.lastUsage);
        int n = this.deleted ? 1 : 0;
        if (n != 0) {
            n = 1;
        }
        result = result * 31 + n;
        return result;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CachedFont)) {
            return false;
        }
        CachedFont cachedFont = (CachedFont)other;
        if (this.displayList != cachedFont.displayList) {
            return false;
        }
        if (this.lastUsage != cachedFont.lastUsage) {
            return false;
        }
        return this.deleted == cachedFont.deleted;
    }
}

