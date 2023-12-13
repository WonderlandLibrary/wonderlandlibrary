/*
 * Decompiled with CFR 0.150.
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
    private final Entity owner;
    private boolean isBlank = true;
    private static final Map dataTypes = Maps.newHashMap();
    private final Map watchedObjects = Maps.newHashMap();
    private boolean objectChanged;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final String __OBFID = "CL_00001559";

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

    public DataWatcher(Entity owner) {
        this.owner = owner;
    }

    public void addObject(int id, Object object) {
        Integer var3 = (Integer)dataTypes.get(object.getClass());
        if (var3 == null) {
            throw new IllegalArgumentException("Unknown data type: " + object.getClass());
        }
        if (id > 31) {
            throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + 31 + ")");
        }
        if (this.watchedObjects.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate id value for " + id + "!");
        }
        WatchableObject var4 = new WatchableObject(var3, id, object);
        this.lock.writeLock().lock();
        this.watchedObjects.put(id, var4);
        this.lock.writeLock().unlock();
        this.isBlank = false;
    }

    public void addObjectByDataType(int id, int type) {
        WatchableObject var3 = new WatchableObject(type, id, null);
        this.lock.writeLock().lock();
        this.watchedObjects.put(id, var3);
        this.lock.writeLock().unlock();
        this.isBlank = false;
    }

    public byte getWatchableObjectByte(int id) {
        return (Byte)this.getWatchedObject(id).getObject();
    }

    public short getWatchableObjectShort(int id) {
        return (Short)this.getWatchedObject(id).getObject();
    }

    public int getWatchableObjectInt(int id) {
        return (Integer)this.getWatchedObject(id).getObject();
    }

    public float getWatchableObjectFloat(int id) {
        return ((Float)this.getWatchedObject(id).getObject()).floatValue();
    }

    public String getWatchableObjectString(int id) {
        return (String)this.getWatchedObject(id).getObject();
    }

    public ItemStack getWatchableObjectItemStack(int id) {
        return (ItemStack)this.getWatchedObject(id).getObject();
    }

    private WatchableObject getWatchedObject(int id) {
        WatchableObject var2;
        this.lock.readLock().lock();
        try {
            var2 = (WatchableObject)this.watchedObjects.get(id);
        }
        catch (Throwable var6) {
            CrashReport var4 = CrashReport.makeCrashReport(var6, "Getting synched entity data");
            CrashReportCategory var5 = var4.makeCategory("Synched entity data");
            var5.addCrashSection("Data ID", id);
            throw new ReportedException(var4);
        }
        this.lock.readLock().unlock();
        return var2;
    }

    public Rotations getWatchableObjectRotations(int id) {
        return (Rotations)this.getWatchedObject(id).getObject();
    }

    public void updateObject(int id, Object newData) {
        WatchableObject var3 = this.getWatchedObject(id);
        if (ObjectUtils.notEqual((Object)newData, (Object)var3.getObject())) {
            var3.setObject(newData);
            this.owner.func_145781_i(id);
            var3.setWatched(true);
            this.objectChanged = true;
        }
    }

    public void setObjectWatched(int id) {
        this.getWatchedObject(id).watched = true;
        this.objectChanged = true;
    }

    public boolean hasObjectChanged() {
        return this.objectChanged;
    }

    public static void writeWatchedListToPacketBuffer(List objectsList, PacketBuffer buffer) throws IOException {
        if (objectsList != null) {
            for (WatchableObject var3 : objectsList) {
                DataWatcher.writeWatchableObjectToPacketBuffer(buffer, var3);
            }
        }
        buffer.writeByte(127);
    }

    public List getChanged() {
        ArrayList var1 = null;
        if (this.objectChanged) {
            this.lock.readLock().lock();
            for (WatchableObject var3 : this.watchedObjects.values()) {
                if (!var3.isWatched()) continue;
                var3.setWatched(false);
                if (var1 == null) {
                    var1 = Lists.newArrayList();
                }
                var1.add(var3);
            }
            this.lock.readLock().unlock();
        }
        this.objectChanged = false;
        return var1;
    }

    public void writeTo(PacketBuffer buffer) throws IOException {
        this.lock.readLock().lock();
        for (WatchableObject var3 : this.watchedObjects.values()) {
            DataWatcher.writeWatchableObjectToPacketBuffer(buffer, var3);
        }
        this.lock.readLock().unlock();
        buffer.writeByte(127);
    }

    public List getAllWatched() {
        ArrayList var1 = null;
        this.lock.readLock().lock();
        for (WatchableObject var3 : this.watchedObjects.values()) {
            if (var1 == null) {
                var1 = Lists.newArrayList();
            }
            var1.add(var3);
        }
        this.lock.readLock().unlock();
        return var1;
    }

    private static void writeWatchableObjectToPacketBuffer(PacketBuffer buffer, WatchableObject object) throws IOException {
        int var2 = (object.getObjectType() << 5 | object.getDataValueId() & 0x1F) & 0xFF;
        buffer.writeByte(var2);
        switch (object.getObjectType()) {
            case 0: {
                buffer.writeByte(((Byte)object.getObject()).byteValue());
                break;
            }
            case 1: {
                buffer.writeShort(((Short)object.getObject()).shortValue());
                break;
            }
            case 2: {
                buffer.writeInt((Integer)object.getObject());
                break;
            }
            case 3: {
                buffer.writeFloat(((Float)object.getObject()).floatValue());
                break;
            }
            case 4: {
                buffer.writeString((String)object.getObject());
                break;
            }
            case 5: {
                ItemStack var3 = (ItemStack)object.getObject();
                buffer.writeItemStackToBuffer(var3);
                break;
            }
            case 6: {
                BlockPos var4 = (BlockPos)object.getObject();
                buffer.writeInt(var4.getX());
                buffer.writeInt(var4.getY());
                buffer.writeInt(var4.getZ());
                break;
            }
            case 7: {
                Rotations var5 = (Rotations)object.getObject();
                buffer.writeFloat(var5.func_179415_b());
                buffer.writeFloat(var5.func_179416_c());
                buffer.writeFloat(var5.func_179413_d());
            }
        }
    }

    public static List readWatchedListFromPacketBuffer(PacketBuffer buffer) throws IOException {
        ArrayList var1 = null;
        byte var2 = buffer.readByte();
        while (var2 != 127) {
            if (var1 == null) {
                var1 = Lists.newArrayList();
            }
            int var3 = (var2 & 0xE0) >> 5;
            int var4 = var2 & 0x1F;
            WatchableObject var5 = null;
            switch (var3) {
                case 0: {
                    var5 = new WatchableObject(var3, var4, buffer.readByte());
                    break;
                }
                case 1: {
                    var5 = new WatchableObject(var3, var4, buffer.readShort());
                    break;
                }
                case 2: {
                    var5 = new WatchableObject(var3, var4, buffer.readInt());
                    break;
                }
                case 3: {
                    var5 = new WatchableObject(var3, var4, Float.valueOf(buffer.readFloat()));
                    break;
                }
                case 4: {
                    var5 = new WatchableObject(var3, var4, buffer.readStringFromBuffer(32767));
                    break;
                }
                case 5: {
                    var5 = new WatchableObject(var3, var4, buffer.readItemStackFromBuffer());
                    break;
                }
                case 6: {
                    int var6 = buffer.readInt();
                    int var7 = buffer.readInt();
                    int var8 = buffer.readInt();
                    var5 = new WatchableObject(var3, var4, new BlockPos(var6, var7, var8));
                    break;
                }
                case 7: {
                    float var9 = buffer.readFloat();
                    float var10 = buffer.readFloat();
                    float var11 = buffer.readFloat();
                    var5 = new WatchableObject(var3, var4, new Rotations(var9, var10, var11));
                }
            }
            var1.add(var5);
            var2 = buffer.readByte();
        }
        return var1;
    }

    public void updateWatchedObjectsFromList(List p_75687_1_) {
        this.lock.writeLock().lock();
        for (WatchableObject var3 : p_75687_1_) {
            WatchableObject var4 = (WatchableObject)this.watchedObjects.get(var3.getDataValueId());
            if (var4 == null) continue;
            var4.setObject(var3.getObject());
            this.owner.func_145781_i(var3.getDataValueId());
        }
        this.lock.writeLock().unlock();
        this.objectChanged = true;
    }

    public boolean getIsBlank() {
        return this.isBlank;
    }

    public void func_111144_e() {
        this.objectChanged = false;
    }

    public static class WatchableObject {
        private final int objectType;
        private final int dataValueId;
        private Object watchedObject;
        private boolean watched;
        private static final String __OBFID = "CL_00001560";

        public WatchableObject(int type, int id, Object object) {
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
    }
}

