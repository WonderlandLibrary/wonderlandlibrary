/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  org.apache.commons.lang3.ObjectUtils
 */
package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Rotations;
import org.apache.commons.lang3.ObjectUtils;

public class DataWatcher {
    private final Map<Integer, WatchableObject> watchedObjects = Maps.newHashMap();
    private boolean isBlank = true;
    private final Entity owner;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private boolean objectChanged;
    private static final Map<Class<?>, Integer> dataTypes = Maps.newHashMap();

    public <T> void addObject(int n, T t) {
        Integer n2 = dataTypes.get(t.getClass());
        if (n2 == null) {
            throw new IllegalArgumentException("Unknown data type: " + t.getClass());
        }
        if (n > 31) {
            throw new IllegalArgumentException("Data value id is too big with " + n + "! (Max is " + 31 + ")");
        }
        if (this.watchedObjects.containsKey(n)) {
            throw new IllegalArgumentException("Duplicate id value for " + n + "!");
        }
        WatchableObject watchableObject = new WatchableObject(n2, n, t);
        this.lock.writeLock().lock();
        this.watchedObjects.put(n, watchableObject);
        this.lock.writeLock().unlock();
        this.isBlank = false;
    }

    public byte getWatchableObjectByte(int n) {
        return (Byte)this.getWatchedObject(n).getObject();
    }

    public DataWatcher(Entity entity) {
        this.owner = entity;
    }

    public int getWatchableObjectInt(int n) {
        return (Integer)this.getWatchedObject(n).getObject();
    }

    public ItemStack getWatchableObjectItemStack(int n) {
        return (ItemStack)this.getWatchedObject(n).getObject();
    }

    public boolean hasObjectChanged() {
        return this.objectChanged;
    }

    public void setObjectWatched(int n) {
        this.getWatchedObject(n).watched = true;
        this.objectChanged = true;
    }

    public void func_111144_e() {
        this.objectChanged = false;
    }

    public List<WatchableObject> getAllWatched() {
        ArrayList arrayList = null;
        this.lock.readLock().lock();
        for (WatchableObject watchableObject : this.watchedObjects.values()) {
            if (arrayList == null) {
                arrayList = Lists.newArrayList();
            }
            arrayList.add(watchableObject);
        }
        this.lock.readLock().unlock();
        return arrayList;
    }

    public static void writeWatchedListToPacketBuffer(List<WatchableObject> list, PacketBuffer packetBuffer) throws IOException {
        if (list != null) {
            for (WatchableObject watchableObject : list) {
                DataWatcher.writeWatchableObjectToPacketBuffer(packetBuffer, watchableObject);
            }
        }
        packetBuffer.writeByte(127);
    }

    private static void writeWatchableObjectToPacketBuffer(PacketBuffer packetBuffer, WatchableObject watchableObject) throws IOException {
        int n = (watchableObject.getObjectType() << 5 | watchableObject.getDataValueId() & 0x1F) & 0xFF;
        packetBuffer.writeByte(n);
        switch (watchableObject.getObjectType()) {
            case 0: {
                packetBuffer.writeByte(((Byte)watchableObject.getObject()).byteValue());
                break;
            }
            case 1: {
                packetBuffer.writeShort(((Short)watchableObject.getObject()).shortValue());
                break;
            }
            case 2: {
                packetBuffer.writeInt((Integer)watchableObject.getObject());
                break;
            }
            case 3: {
                packetBuffer.writeFloat(((Float)watchableObject.getObject()).floatValue());
                break;
            }
            case 4: {
                packetBuffer.writeString((String)watchableObject.getObject());
                break;
            }
            case 5: {
                ItemStack itemStack = (ItemStack)watchableObject.getObject();
                packetBuffer.writeItemStackToBuffer(itemStack);
                break;
            }
            case 6: {
                BlockPos blockPos = (BlockPos)watchableObject.getObject();
                packetBuffer.writeInt(blockPos.getX());
                packetBuffer.writeInt(blockPos.getY());
                packetBuffer.writeInt(blockPos.getZ());
                break;
            }
            case 7: {
                Rotations rotations = (Rotations)watchableObject.getObject();
                packetBuffer.writeFloat(rotations.getX());
                packetBuffer.writeFloat(rotations.getY());
                packetBuffer.writeFloat(rotations.getZ());
            }
        }
    }

    public void updateWatchedObjectsFromList(List<WatchableObject> list) {
        this.lock.writeLock().lock();
        for (WatchableObject watchableObject : list) {
            WatchableObject watchableObject2 = this.watchedObjects.get(watchableObject.getDataValueId());
            if (watchableObject2 == null) continue;
            watchableObject2.setObject(watchableObject.getObject());
            this.owner.onDataWatcherUpdate(watchableObject.getDataValueId());
        }
        this.lock.writeLock().unlock();
        this.objectChanged = true;
    }

    static {
        dataTypes.put(Byte.class, 0);
        dataTypes.put(Short.class, 1);
        dataTypes.put(Integer.class, 2);
        dataTypes.put(Float.class, 3);
        dataTypes.put(String.class, 4);
        dataTypes.put(ItemStack.class, 5);
        dataTypes.put(BlockPos.class, 6);
        dataTypes.put(Rotations.class, 7);
    }

    public float getWatchableObjectFloat(int n) {
        return ((Float)this.getWatchedObject(n).getObject()).floatValue();
    }

    public void addObjectByDataType(int n, int n2) {
        WatchableObject watchableObject = new WatchableObject(n2, n, null);
        this.lock.writeLock().lock();
        this.watchedObjects.put(n, watchableObject);
        this.lock.writeLock().unlock();
        this.isBlank = false;
    }

    public boolean getIsBlank() {
        return this.isBlank;
    }

    public List<WatchableObject> getChanged() {
        ArrayList arrayList = null;
        if (this.objectChanged) {
            this.lock.readLock().lock();
            for (WatchableObject watchableObject : this.watchedObjects.values()) {
                if (!watchableObject.isWatched()) continue;
                watchableObject.setWatched(false);
                if (arrayList == null) {
                    arrayList = Lists.newArrayList();
                }
                arrayList.add(watchableObject);
            }
            this.lock.readLock().unlock();
        }
        this.objectChanged = false;
        return arrayList;
    }

    public short getWatchableObjectShort(int n) {
        return (Short)this.getWatchedObject(n).getObject();
    }

    public <T> void updateObject(int n, T t) {
        WatchableObject watchableObject = this.getWatchedObject(n);
        if (ObjectUtils.notEqual(t, (Object)watchableObject.getObject())) {
            watchableObject.setObject(t);
            this.owner.onDataWatcherUpdate(n);
            watchableObject.setWatched(true);
            this.objectChanged = true;
        }
    }

    public Rotations getWatchableObjectRotations(int n) {
        return (Rotations)this.getWatchedObject(n).getObject();
    }

    public void writeTo(PacketBuffer packetBuffer) throws IOException {
        this.lock.readLock().lock();
        for (WatchableObject watchableObject : this.watchedObjects.values()) {
            DataWatcher.writeWatchableObjectToPacketBuffer(packetBuffer, watchableObject);
        }
        this.lock.readLock().unlock();
        packetBuffer.writeByte(127);
    }

    private WatchableObject getWatchedObject(int n) {
        WatchableObject watchableObject;
        this.lock.readLock().lock();
        try {
            watchableObject = this.watchedObjects.get(n);
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Getting synched entity data");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Synched entity data");
            crashReportCategory.addCrashSection("Data ID", n);
            throw new ReportedException(crashReport);
        }
        this.lock.readLock().unlock();
        return watchableObject;
    }

    public String getWatchableObjectString(int n) {
        return (String)this.getWatchedObject(n).getObject();
    }

    public static List<WatchableObject> readWatchedListFromPacketBuffer(PacketBuffer packetBuffer) throws IOException {
        ArrayList arrayList = null;
        byte by = packetBuffer.readByte();
        while (by != 127) {
            if (arrayList == null) {
                arrayList = Lists.newArrayList();
            }
            int n = (by & 0xE0) >> 5;
            int n2 = by & 0x1F;
            WatchableObject watchableObject = null;
            switch (n) {
                case 0: {
                    watchableObject = new WatchableObject(n, n2, packetBuffer.readByte());
                    break;
                }
                case 1: {
                    watchableObject = new WatchableObject(n, n2, packetBuffer.readShort());
                    break;
                }
                case 2: {
                    watchableObject = new WatchableObject(n, n2, packetBuffer.readInt());
                    break;
                }
                case 3: {
                    watchableObject = new WatchableObject(n, n2, Float.valueOf(packetBuffer.readFloat()));
                    break;
                }
                case 4: {
                    watchableObject = new WatchableObject(n, n2, packetBuffer.readStringFromBuffer(Short.MAX_VALUE));
                    break;
                }
                case 5: {
                    watchableObject = new WatchableObject(n, n2, packetBuffer.readItemStackFromBuffer());
                    break;
                }
                case 6: {
                    int n3 = packetBuffer.readInt();
                    int n4 = packetBuffer.readInt();
                    int n5 = packetBuffer.readInt();
                    watchableObject = new WatchableObject(n, n2, new BlockPos(n3, n4, n5));
                    break;
                }
                case 7: {
                    float f = packetBuffer.readFloat();
                    float f2 = packetBuffer.readFloat();
                    float f3 = packetBuffer.readFloat();
                    watchableObject = new WatchableObject(n, n2, new Rotations(f, f2, f3));
                }
            }
            arrayList.add(watchableObject);
            by = packetBuffer.readByte();
        }
        return arrayList;
    }

    public static class WatchableObject {
        private Object watchedObject;
        private final int objectType;
        private boolean watched;
        private final int dataValueId;

        public boolean isWatched() {
            return this.watched;
        }

        public int getObjectType() {
            return this.objectType;
        }

        public void setWatched(boolean bl) {
            this.watched = bl;
        }

        public void setObject(Object object) {
            this.watchedObject = object;
        }

        public Object getObject() {
            return this.watchedObject;
        }

        public int getDataValueId() {
            return this.dataValueId;
        }

        public WatchableObject(int n, int n2, Object object) {
            this.dataValueId = n2;
            this.watchedObject = object;
            this.objectType = n;
            this.watched = true;
        }
    }
}

