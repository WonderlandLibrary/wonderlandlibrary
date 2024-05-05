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
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Rotations;
import net.minecraft.world.biome.BiomeGenBase;
import org.apache.commons.lang3.ObjectUtils;

/*
 * Exception performing whole class analysis ignored.
 */
public class DataWatcher {
    private final Entity owner;
    private boolean isBlank = true;
    private static final Map<Class<?>, Integer> dataTypes = Maps.newHashMap();
    private final Map<Integer, WatchableObject> watchedObjects = Maps.newHashMap();
    private boolean objectChanged;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    public BiomeGenBase spawnBiome = BiomeGenBase.plains;
    public BlockPos spawnPosition = BlockPos.ORIGIN;

    public DataWatcher(Entity owner) {
        this.owner = owner;
    }

    public <T> void addObject(int id, T object) {
        Integer integer = (Integer)dataTypes.get((Object)object.getClass());
        if (integer == null) {
            throw new IllegalArgumentException("Unknown data type: " + object.getClass());
        }
        if (id > 31) {
            throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + 31 + ")");
        }
        if (this.watchedObjects.containsKey((Object)id)) {
            throw new IllegalArgumentException("Duplicate id value for " + id + "!");
        }
        WatchableObject datawatcher$watchableobject = new WatchableObject(integer.intValue(), id, object);
        this.lock.writeLock().lock();
        this.watchedObjects.put((Object)id, (Object)datawatcher$watchableobject);
        this.lock.writeLock().unlock();
        this.isBlank = false;
    }

    public void addObjectByDataType(int id, int type) {
        WatchableObject datawatcher$watchableobject = new WatchableObject(type, id, null);
        this.lock.writeLock().lock();
        this.watchedObjects.put((Object)id, (Object)datawatcher$watchableobject);
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
        WatchableObject datawatcher$watchableobject;
        this.lock.readLock().lock();
        try {
            datawatcher$watchableobject = (WatchableObject)this.watchedObjects.get((Object)id);
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Getting synched entity data");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
            crashreportcategory.addCrashSection("Data ID", (Object)id);
            throw new ReportedException(crashreport);
        }
        this.lock.readLock().unlock();
        return datawatcher$watchableobject;
    }

    public Rotations getWatchableObjectRotations(int id) {
        return (Rotations)this.getWatchedObject(id).getObject();
    }

    public <T> void updateObject(int id, T newData) {
        WatchableObject datawatcher$watchableobject = this.getWatchedObject(id);
        if (ObjectUtils.notEqual(newData, (Object)datawatcher$watchableobject.getObject())) {
            datawatcher$watchableobject.setObject(newData);
            this.owner.onDataWatcherUpdate(id);
            datawatcher$watchableobject.setWatched(true);
            this.objectChanged = true;
        }
    }

    public void setObjectWatched(int id) {
        WatchableObject.access$002((WatchableObject)this.getWatchedObject(id), (boolean)true);
        this.objectChanged = true;
    }

    public boolean hasObjectChanged() {
        return this.objectChanged;
    }

    public static void writeWatchedListToPacketBuffer(List<WatchableObject> objectsList, PacketBuffer buffer) throws IOException {
        if (objectsList != null) {
            for (WatchableObject datawatcher$watchableobject : objectsList) {
                DataWatcher.writeWatchableObjectToPacketBuffer(buffer, datawatcher$watchableobject);
            }
        }
        buffer.writeByte(127);
    }

    public List<WatchableObject> getChanged() {
        ArrayList list = null;
        if (this.objectChanged) {
            this.lock.readLock().lock();
            for (WatchableObject datawatcher$watchableobject : this.watchedObjects.values()) {
                if (!datawatcher$watchableobject.isWatched()) continue;
                datawatcher$watchableobject.setWatched(false);
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add((Object)datawatcher$watchableobject);
            }
            this.lock.readLock().unlock();
        }
        this.objectChanged = false;
        return list;
    }

    public void writeTo(PacketBuffer buffer) throws IOException {
        this.lock.readLock().lock();
        for (WatchableObject datawatcher$watchableobject : this.watchedObjects.values()) {
            DataWatcher.writeWatchableObjectToPacketBuffer(buffer, datawatcher$watchableobject);
        }
        this.lock.readLock().unlock();
        buffer.writeByte(127);
    }

    public List<WatchableObject> getAllWatched() {
        ArrayList list = null;
        this.lock.readLock().lock();
        for (WatchableObject datawatcher$watchableobject : this.watchedObjects.values()) {
            if (list == null) {
                list = Lists.newArrayList();
            }
            list.add((Object)datawatcher$watchableobject);
        }
        this.lock.readLock().unlock();
        return list;
    }

    private static void writeWatchableObjectToPacketBuffer(PacketBuffer buffer, WatchableObject object) throws IOException {
        int i = (object.getObjectType() << 5 | object.getDataValueId() & 0x1F) & 0xFF;
        buffer.writeByte(i);
        switch (object.getObjectType()) {
            case 0: {
                buffer.writeByte((int)((Byte)object.getObject()).byteValue());
                break;
            }
            case 1: {
                buffer.writeShort((int)((Short)object.getObject()).shortValue());
                break;
            }
            case 2: {
                buffer.writeInt(((Integer)object.getObject()).intValue());
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
                ItemStack itemstack = (ItemStack)object.getObject();
                buffer.writeItemStackToBuffer(itemstack);
                break;
            }
            case 6: {
                BlockPos blockpos = (BlockPos)object.getObject();
                buffer.writeInt(blockpos.getX());
                buffer.writeInt(blockpos.getY());
                buffer.writeInt(blockpos.getZ());
                break;
            }
            case 7: {
                Rotations rotations = (Rotations)object.getObject();
                buffer.writeFloat(rotations.getX());
                buffer.writeFloat(rotations.getY());
                buffer.writeFloat(rotations.getZ());
            }
        }
    }

    public static List<WatchableObject> readWatchedListFromPacketBuffer(PacketBuffer buffer) throws IOException {
        ArrayList list = null;
        byte i = buffer.readByte();
        while (i != 127) {
            if (list == null) {
                list = Lists.newArrayList();
            }
            int j = (i & 0xE0) >> 5;
            int k = i & 0x1F;
            WatchableObject datawatcher$watchableobject = null;
            switch (j) {
                case 0: {
                    datawatcher$watchableobject = new WatchableObject(j, k, (Object)buffer.readByte());
                    break;
                }
                case 1: {
                    datawatcher$watchableobject = new WatchableObject(j, k, (Object)buffer.readShort());
                    break;
                }
                case 2: {
                    datawatcher$watchableobject = new WatchableObject(j, k, (Object)buffer.readInt());
                    break;
                }
                case 3: {
                    datawatcher$watchableobject = new WatchableObject(j, k, (Object)Float.valueOf((float)buffer.readFloat()));
                    break;
                }
                case 4: {
                    datawatcher$watchableobject = new WatchableObject(j, k, (Object)buffer.readStringFromBuffer(Short.MAX_VALUE));
                    break;
                }
                case 5: {
                    datawatcher$watchableobject = new WatchableObject(j, k, (Object)buffer.readItemStackFromBuffer());
                    break;
                }
                case 6: {
                    int l = buffer.readInt();
                    int i1 = buffer.readInt();
                    int j1 = buffer.readInt();
                    datawatcher$watchableobject = new WatchableObject(j, k, (Object)new BlockPos(l, i1, j1));
                    break;
                }
                case 7: {
                    float f = buffer.readFloat();
                    float f1 = buffer.readFloat();
                    float f2 = buffer.readFloat();
                    datawatcher$watchableobject = new WatchableObject(j, k, (Object)new Rotations(f, f1, f2));
                }
            }
            list.add(datawatcher$watchableobject);
            i = buffer.readByte();
        }
        return list;
    }

    public void updateWatchedObjectsFromList(List<WatchableObject> p_75687_1_) {
        this.lock.writeLock().lock();
        for (WatchableObject datawatcher$watchableobject : p_75687_1_) {
            WatchableObject datawatcher$watchableobject1 = (WatchableObject)this.watchedObjects.get((Object)datawatcher$watchableobject.getDataValueId());
            if (datawatcher$watchableobject1 == null) continue;
            datawatcher$watchableobject1.setObject(datawatcher$watchableobject.getObject());
            this.owner.onDataWatcherUpdate(datawatcher$watchableobject.getDataValueId());
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
        dataTypes.put(Byte.class, (Object)0);
        dataTypes.put(Short.class, (Object)1);
        dataTypes.put(Integer.class, (Object)2);
        dataTypes.put(Float.class, (Object)3);
        dataTypes.put(String.class, (Object)4);
        dataTypes.put(ItemStack.class, (Object)5);
        dataTypes.put(BlockPos.class, (Object)6);
        dataTypes.put(Rotations.class, (Object)7);
    }
}
