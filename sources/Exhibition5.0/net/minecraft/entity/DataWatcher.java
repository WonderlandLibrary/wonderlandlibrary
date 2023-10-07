// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity;

import net.minecraft.util.BlockPos;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.network.PacketBuffer;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import net.minecraft.util.Rotations;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.ItemStack;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.google.common.collect.Maps;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.Map;

public class DataWatcher
{
    private final Entity owner;
    private boolean isBlank;
    private static final Map dataTypes;
    private final Map watchedObjects;
    private boolean objectChanged;
    private ReadWriteLock lock;
    private static final String __OBFID = "CL_00001559";
    
    public DataWatcher(final Entity owner) {
        this.isBlank = true;
        this.watchedObjects = Maps.newHashMap();
        this.lock = new ReentrantReadWriteLock();
        this.owner = owner;
    }
    
    public void addObject(final int id, final Object object) {
        final Integer var3 = DataWatcher.dataTypes.get(object.getClass());
        if (var3 == null) {
            throw new IllegalArgumentException("Unknown data type: " + object.getClass());
        }
        if (id > 31) {
            throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + 31 + ")");
        }
        if (this.watchedObjects.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate id value for " + id + "!");
        }
        final WatchableObject var4 = new WatchableObject(var3, id, object);
        this.lock.writeLock().lock();
        this.watchedObjects.put(id, var4);
        this.lock.writeLock().unlock();
        this.isBlank = false;
    }
    
    public void addObjectByDataType(final int id, final int type) {
        final WatchableObject var3 = new WatchableObject(type, id, null);
        this.lock.writeLock().lock();
        this.watchedObjects.put(id, var3);
        this.lock.writeLock().unlock();
        this.isBlank = false;
    }
    
    public byte getWatchableObjectByte(final int id) {
        return (byte)this.getWatchedObject(id).getObject();
    }
    
    public short getWatchableObjectShort(final int id) {
        return (short)this.getWatchedObject(id).getObject();
    }
    
    public int getWatchableObjectInt(final int id) {
        return (int)this.getWatchedObject(id).getObject();
    }
    
    public float getWatchableObjectFloat(final int id) {
        return (float)this.getWatchedObject(id).getObject();
    }
    
    public String getWatchableObjectString(final int id) {
        return (String)this.getWatchedObject(id).getObject();
    }
    
    public ItemStack getWatchableObjectItemStack(final int id) {
        return (ItemStack)this.getWatchedObject(id).getObject();
    }
    
    private WatchableObject getWatchedObject(final int id) {
        this.lock.readLock().lock();
        WatchableObject var2;
        try {
            var2 = this.watchedObjects.get(id);
        }
        catch (Throwable var4) {
            final CrashReport var3 = CrashReport.makeCrashReport(var4, "Getting synched entity data");
            final CrashReportCategory var5 = var3.makeCategory("Synched entity data");
            var5.addCrashSection("Data ID", id);
            throw new ReportedException(var3);
        }
        this.lock.readLock().unlock();
        return var2;
    }
    
    public Rotations getWatchableObjectRotations(final int id) {
        return (Rotations)this.getWatchedObject(id).getObject();
    }
    
    public void updateObject(final int id, final Object newData) {
        final WatchableObject var3 = this.getWatchedObject(id);
        if (ObjectUtils.notEqual(newData, var3.getObject())) {
            var3.setObject(newData);
            this.owner.func_145781_i(id);
            var3.setWatched(true);
            this.objectChanged = true;
        }
    }
    
    public void setObjectWatched(final int id) {
        this.getWatchedObject(id).watched = true;
        this.objectChanged = true;
    }
    
    public boolean hasObjectChanged() {
        return this.objectChanged;
    }
    
    public static void writeWatchedListToPacketBuffer(final List objectsList, final PacketBuffer buffer) throws IOException {
        if (objectsList != null) {
            for (final WatchableObject var3 : objectsList) {
                writeWatchableObjectToPacketBuffer(buffer, var3);
            }
        }
        buffer.writeByte(127);
    }
    
    public List getChanged() {
        ArrayList var1 = null;
        if (this.objectChanged) {
            this.lock.readLock().lock();
            for (final WatchableObject var3 : this.watchedObjects.values()) {
                if (var3.isWatched()) {
                    var3.setWatched(false);
                    if (var1 == null) {
                        var1 = Lists.newArrayList();
                    }
                    var1.add(var3);
                }
            }
            this.lock.readLock().unlock();
        }
        this.objectChanged = false;
        return var1;
    }
    
    public void writeTo(final PacketBuffer buffer) throws IOException {
        this.lock.readLock().lock();
        for (final WatchableObject var3 : this.watchedObjects.values()) {
            writeWatchableObjectToPacketBuffer(buffer, var3);
        }
        this.lock.readLock().unlock();
        buffer.writeByte(127);
    }
    
    public List getAllWatched() {
        ArrayList var1 = null;
        this.lock.readLock().lock();
        for (final WatchableObject var3 : this.watchedObjects.values()) {
            if (var1 == null) {
                var1 = Lists.newArrayList();
            }
            var1.add(var3);
        }
        this.lock.readLock().unlock();
        return var1;
    }
    
    private static void writeWatchableObjectToPacketBuffer(final PacketBuffer buffer, final WatchableObject object) throws IOException {
        final int var2 = (object.getObjectType() << 5 | (object.getDataValueId() & 0x1F)) & 0xFF;
        buffer.writeByte(var2);
        switch (object.getObjectType()) {
            case 0: {
                buffer.writeByte((byte)object.getObject());
                break;
            }
            case 1: {
                buffer.writeShort((short)object.getObject());
                break;
            }
            case 2: {
                buffer.writeInt((int)object.getObject());
                break;
            }
            case 3: {
                buffer.writeFloat((float)object.getObject());
                break;
            }
            case 4: {
                buffer.writeString((String)object.getObject());
                break;
            }
            case 5: {
                final ItemStack var3 = (ItemStack)object.getObject();
                buffer.writeItemStackToBuffer(var3);
                break;
            }
            case 6: {
                final BlockPos var4 = (BlockPos)object.getObject();
                buffer.writeInt(var4.getX());
                buffer.writeInt(var4.getY());
                buffer.writeInt(var4.getZ());
                break;
            }
            case 7: {
                final Rotations var5 = (Rotations)object.getObject();
                buffer.writeFloat(var5.func_179415_b());
                buffer.writeFloat(var5.func_179416_c());
                buffer.writeFloat(var5.func_179413_d());
                break;
            }
        }
    }
    
    public static List readWatchedListFromPacketBuffer(final PacketBuffer buffer) throws IOException {
        ArrayList var1 = null;
        for (byte var2 = buffer.readByte(); var2 != 127; var2 = buffer.readByte()) {
            if (var1 == null) {
                var1 = Lists.newArrayList();
            }
            final int var3 = (var2 & 0xE0) >> 5;
            final int var4 = var2 & 0x1F;
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
                    var5 = new WatchableObject(var3, var4, buffer.readFloat());
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
                    final int var6 = buffer.readInt();
                    final int var7 = buffer.readInt();
                    final int var8 = buffer.readInt();
                    var5 = new WatchableObject(var3, var4, new BlockPos(var6, var7, var8));
                    break;
                }
                case 7: {
                    final float var9 = buffer.readFloat();
                    final float var10 = buffer.readFloat();
                    final float var11 = buffer.readFloat();
                    var5 = new WatchableObject(var3, var4, new Rotations(var9, var10, var11));
                    break;
                }
            }
            var1.add(var5);
        }
        return var1;
    }
    
    public void updateWatchedObjectsFromList(final List p_75687_1_) {
        this.lock.writeLock().lock();
        for (final WatchableObject var3 : p_75687_1_) {
            final WatchableObject var4 = this.watchedObjects.get(var3.getDataValueId());
            if (var4 != null) {
                var4.setObject(var3.getObject());
                this.owner.func_145781_i(var3.getDataValueId());
            }
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
    
    static {
        (dataTypes = Maps.newHashMap()).put(Byte.class, 0);
        DataWatcher.dataTypes.put(Short.class, 1);
        DataWatcher.dataTypes.put(Integer.class, 2);
        DataWatcher.dataTypes.put(Float.class, 3);
        DataWatcher.dataTypes.put(String.class, 4);
        DataWatcher.dataTypes.put(ItemStack.class, 5);
        DataWatcher.dataTypes.put(BlockPos.class, 6);
        DataWatcher.dataTypes.put(Rotations.class, 7);
    }
    
    public static class WatchableObject
    {
        private final int objectType;
        private final int dataValueId;
        private Object watchedObject;
        private boolean watched;
        private static final String __OBFID = "CL_00001560";
        
        public WatchableObject(final int type, final int id, final Object object) {
            this.dataValueId = id;
            this.watchedObject = object;
            this.objectType = type;
            this.watched = true;
        }
        
        public int getDataValueId() {
            return this.dataValueId;
        }
        
        public void setObject(final Object object) {
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
        
        public void setWatched(final boolean watched) {
            this.watched = watched;
        }
    }
}
