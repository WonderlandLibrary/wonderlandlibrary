package net.minecraft.entity;

public static class DataWatcher.WatchableObject {
    private final int objectType;
    private final int dataValueId;
    private Object watchedObject;
    private boolean watched;

    public DataWatcher.WatchableObject(int type, int id, Object object) {
        this.dataValueId = id;
        this.watchedObject = object;
        this.objectType = type;
        this.watched = true;
    }

    public int getDataValueId() {
        return this.dataValueId;
    }

    public void setObject(Object object) {
        this.watchedObject = object;
    }

    public Object getObject() {
        return this.watchedObject;
    }

    public int getObjectType() {
        return this.objectType;
    }

    public boolean isWatched() {
        return this.watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    static /* synthetic */ boolean access$002(DataWatcher.WatchableObject x0, boolean x1) {
        x0.watched = x1;
        return x0.watched;
    }
}
