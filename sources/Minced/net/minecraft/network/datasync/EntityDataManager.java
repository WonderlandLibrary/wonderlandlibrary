// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.datasync;

import org.apache.logging.log4j.LogManager;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Biomes;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.google.common.collect.Maps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import java.util.concurrent.locks.ReadWriteLock;
import net.minecraft.entity.Entity;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class EntityDataManager
{
    private static final Logger LOGGER;
    private static final Map<Class<? extends Entity>, Integer> NEXT_ID_MAP;
    private final Entity entity;
    private final Map<Integer, DataEntry<?>> entries;
    private final ReadWriteLock lock;
    private boolean empty;
    private boolean dirty;
    public Biome spawnBiome;
    public BlockPos spawnPosition;
    
    public EntityDataManager(final Entity entityIn) {
        this.entries = (Map<Integer, DataEntry<?>>)Maps.newHashMap();
        this.lock = new ReentrantReadWriteLock();
        this.empty = true;
        this.spawnBiome = Biomes.PLAINS;
        this.spawnPosition = BlockPos.ORIGIN;
        this.entity = entityIn;
    }
    
    public static <T> DataParameter<T> createKey(final Class<? extends Entity> clazz, final DataSerializer<T> serializer) {
        if (EntityDataManager.LOGGER.isDebugEnabled()) {
            try {
                final Class<?> oclass = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
                if (!oclass.equals(clazz)) {
                    EntityDataManager.LOGGER.debug("defineId called for: {} from {}", (Object)clazz, (Object)oclass, (Object)new RuntimeException());
                }
            }
            catch (ClassNotFoundException ex) {}
        }
        int j;
        if (EntityDataManager.NEXT_ID_MAP.containsKey(clazz)) {
            j = EntityDataManager.NEXT_ID_MAP.get(clazz) + 1;
        }
        else {
            int i = 0;
            Class<?> oclass2 = clazz;
            while (oclass2 != Entity.class) {
                oclass2 = oclass2.getSuperclass();
                if (EntityDataManager.NEXT_ID_MAP.containsKey(oclass2)) {
                    i = EntityDataManager.NEXT_ID_MAP.get(oclass2) + 1;
                    break;
                }
            }
            j = i;
        }
        if (j > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + j + "! (Max is " + 254 + ")");
        }
        EntityDataManager.NEXT_ID_MAP.put(clazz, j);
        return serializer.createKey(j);
    }
    
    public <T> void register(final DataParameter<T> key, final T value) {
        final int i = key.getId();
        if (i > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + i + "! (Max is " + 254 + ")");
        }
        if (this.entries.containsKey(i)) {
            throw new IllegalArgumentException("Duplicate id value for " + i + "!");
        }
        if (DataSerializers.getSerializerId(key.getSerializer()) < 0) {
            throw new IllegalArgumentException("Unregistered serializer " + key.getSerializer() + " for " + i + "!");
        }
        this.setEntry((DataParameter<Object>)key, value);
    }
    
    private <T> void setEntry(final DataParameter<T> key, final T value) {
        final DataEntry<T> dataentry = new DataEntry<T>(key, value);
        this.lock.writeLock().lock();
        this.entries.put(key.getId(), dataentry);
        this.empty = false;
        this.lock.writeLock().unlock();
    }
    
    private <T> DataEntry<T> getEntry(final DataParameter<T> key) {
        this.lock.readLock().lock();
        DataEntry<T> dataentry;
        try {
            dataentry = (DataEntry<T>)this.entries.get(key.getId());
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting synched entity data");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
            crashreportcategory.addCrashSection("Data ID", key);
            throw new ReportedException(crashreport);
        }
        this.lock.readLock().unlock();
        return dataentry;
    }
    
    public <T> T get(final DataParameter<T> key) {
        return this.getEntry(key).getValue();
    }
    
    public <T> void set(final DataParameter<T> key, final T value) {
        final DataEntry<T> dataentry = this.getEntry(key);
        if (ObjectUtils.notEqual((Object)value, (Object)dataentry.getValue())) {
            dataentry.setValue(value);
            this.entity.notifyDataManagerChange(key);
            dataentry.setDirty(true);
            this.dirty = true;
        }
    }
    
    public <T> void setDirty(final DataParameter<T> key) {
        ((DataEntry<Object>)this.getEntry(key)).dirty = true;
        this.dirty = true;
    }
    
    public boolean isDirty() {
        return this.dirty;
    }
    
    public static void writeEntries(final List<DataEntry<?>> entriesIn, final PacketBuffer buf) throws IOException {
        if (entriesIn != null) {
            for (int i = 0, j = entriesIn.size(); i < j; ++i) {
                final DataEntry<?> dataentry = entriesIn.get(i);
                writeEntry(buf, dataentry);
            }
        }
        buf.writeByte(255);
    }
    
    @Nullable
    public List<DataEntry<?>> getDirty() {
        List<DataEntry<?>> list = null;
        if (this.dirty) {
            this.lock.readLock().lock();
            for (final DataEntry<?> dataentry : this.entries.values()) {
                if (dataentry.isDirty()) {
                    dataentry.setDirty(false);
                    if (list == null) {
                        list = (List<DataEntry<?>>)Lists.newArrayList();
                    }
                    list.add(dataentry.copy());
                }
            }
            this.lock.readLock().unlock();
        }
        this.dirty = false;
        return list;
    }
    
    public void writeEntries(final PacketBuffer buf) throws IOException {
        this.lock.readLock().lock();
        for (final DataEntry<?> dataentry : this.entries.values()) {
            writeEntry(buf, dataentry);
        }
        this.lock.readLock().unlock();
        buf.writeByte(255);
    }
    
    @Nullable
    public List<DataEntry<?>> getAll() {
        List<DataEntry<?>> list = null;
        this.lock.readLock().lock();
        for (final DataEntry<?> dataentry : this.entries.values()) {
            if (list == null) {
                list = (List<DataEntry<?>>)Lists.newArrayList();
            }
            list.add(dataentry.copy());
        }
        this.lock.readLock().unlock();
        return list;
    }
    
    private static <T> void writeEntry(final PacketBuffer buf, final DataEntry<T> entry) throws IOException {
        final DataParameter<T> dataparameter = entry.getKey();
        final int i = DataSerializers.getSerializerId(dataparameter.getSerializer());
        if (i < 0) {
            throw new EncoderException("Unknown serializer type " + dataparameter.getSerializer());
        }
        buf.writeByte(dataparameter.getId());
        buf.writeVarInt(i);
        dataparameter.getSerializer().write(buf, entry.getValue());
    }
    
    @Nullable
    public static List<DataEntry<?>> readEntries(final PacketBuffer buf) throws IOException {
        List<DataEntry<?>> list = null;
        int i;
        while ((i = buf.readUnsignedByte()) != 255) {
            if (list == null) {
                list = (List<DataEntry<?>>)Lists.newArrayList();
            }
            final int j = buf.readVarInt();
            final DataSerializer<?> dataserializer = DataSerializers.getSerializer(j);
            if (dataserializer == null) {
                throw new DecoderException("Unknown serializer type " + j);
            }
            list.add(new DataEntry<Object>((DataParameter<Object>)dataserializer.createKey(i), dataserializer.read(buf)));
        }
        return list;
    }
    
    public void setEntryValues(final List<DataEntry<?>> entriesIn) {
        this.lock.writeLock().lock();
        for (final DataEntry<?> dataentry : entriesIn) {
            final DataEntry<?> dataentry2 = this.entries.get(dataentry.getKey().getId());
            if (dataentry2 != null) {
                this.setEntryValue(dataentry2, dataentry);
                this.entity.notifyDataManagerChange(dataentry.getKey());
            }
        }
        this.lock.writeLock().unlock();
        this.dirty = true;
    }
    
    protected <T> void setEntryValue(final DataEntry<T> target, final DataEntry<?> source) {
        target.setValue((T)source.getValue());
    }
    
    public boolean isEmpty() {
        return this.empty;
    }
    
    public void setClean() {
        this.dirty = false;
        this.lock.readLock().lock();
        for (final DataEntry<?> dataentry : this.entries.values()) {
            dataentry.setDirty(false);
        }
        this.lock.readLock().unlock();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        NEXT_ID_MAP = Maps.newHashMap();
    }
    
    public static class DataEntry<T>
    {
        private final DataParameter<T> key;
        private T value;
        private boolean dirty;
        
        public DataEntry(final DataParameter<T> keyIn, final T valueIn) {
            this.key = keyIn;
            this.value = valueIn;
            this.dirty = true;
        }
        
        public DataParameter<T> getKey() {
            return this.key;
        }
        
        public void setValue(final T valueIn) {
            this.value = valueIn;
        }
        
        public T getValue() {
            return this.value;
        }
        
        public boolean isDirty() {
            return this.dirty;
        }
        
        public void setDirty(final boolean dirtyIn) {
            this.dirty = dirtyIn;
        }
        
        public DataEntry<T> copy() {
            return new DataEntry<T>(this.key, this.key.getSerializer().copyValue(this.value));
        }
    }
}
