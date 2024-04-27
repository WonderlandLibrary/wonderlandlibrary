package me.aquavit.liquidsense.ui.font;

import org.lwjgl.opengl.GL11;

public class CachedFont {
    private final int displayList;
    private long lastUsage;
    private boolean deleted;

    public CachedFont(int displayList, long lastUsage, boolean deleted) {
        this.displayList = displayList;
        this.lastUsage = lastUsage;
        this.deleted = deleted;
    }

    protected final void finalize() {
        if (!this.deleted) {
            GL11.glDeleteLists(this.displayList, 1);
        }
    }

    public final int getDisplayList() {
        return this.displayList;
    }

    public final long getLastUsage() {
        return this.lastUsage;
    }

    public final void setLastUsage(long lastUsage) {
        this.lastUsage = lastUsage;
    }

    public final boolean getDeleted() {
        return this.deleted;
    }

    public final void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
