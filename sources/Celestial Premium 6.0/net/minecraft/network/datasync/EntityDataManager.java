/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.DecoderException
 *  io.netty.handler.codec.EncoderException
 */
package net.minecraft.network.datasync;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.ReportedException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityDataManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<Class<? extends Entity>, Integer> NEXT_ID_MAP = Maps.newHashMap();
    private final Entity entity;
    private final Map<Integer, DataEntry<?>> entries = Maps.newHashMap();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private boolean empty = true;
    private boolean dirty;

    public EntityDataManager(Entity entityIn) {
        this.entity = entityIn;
    }

    public static <T> DataParameter<T> createKey(Class<? extends Entity> clazz, DataSerializer<T> serializer) {
        int j;
        if (LOGGER.isDebugEnabled()) {
            try {
                Class<?> oclass = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
                if (!oclass.equals(clazz)) {
                    LOGGER.debug("defineId called for: {} from {}", clazz, oclass, new RuntimeException());
                }
            }
            catch (ClassNotFoundException oclass) {
                // empty catch block
            }
        }
        if (NEXT_ID_MAP.containsKey(clazz)) {
            j = NEXT_ID_MAP.get(clazz) + 1;
        } else {
            int i = 0;
            Class<? extends Entity> oclass1 = clazz;
            while (oclass1 != Entity.class) {
                if (!NEXT_ID_MAP.containsKey(oclass1 = oclass1.getSuperclass())) continue;
                i = NEXT_ID_MAP.get(oclass1) + 1;
                break;
            }
            j = i;
        }
        if (j > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + j + "! (Max is " + 254 + ")");
        }
        NEXT_ID_MAP.put(clazz, j);
        return serializer.createKey(j);
    }

    public <T> void register(DataParameter<T> key, T value) {
        int i = key.getId();
        if (i > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + i + "! (Max is " + 254 + ")");
        }
        if (this.entries.containsKey(i)) {
            throw new IllegalArgumentException("Duplicate id value for " + i + "!");
        }
        if (DataSerializers.getSerializerId(key.getSerializer()) < 0) {
            throw new IllegalArgumentException("Unregistered serializer " + key.getSerializer() + " for " + i + "!");
        }
        this.setEntry(key, value);
    }

    private <T> void setEntry(DataParameter<T> key, T value) {
        DataEntry<T> dataentry = new DataEntry<T>(key, value);
        this.lock.writeLock().lock();
        this.entries.put(key.getId(), dataentry);
        this.empty = false;
        this.lock.writeLock().unlock();
    }

    private <T> DataEntry<T> getEntry(DataParameter<T> key) {
        DataEntry<?> dataentry;
        this.lock.readLock().lock();
        try {
            dataentry = this.entries.get(key.getId());
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting synched entity data");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
            crashreportcategory.addCrashSection("Data ID", key);
            throw new ReportedException(crashreport);
        }
        this.lock.readLock().unlock();
        return dataentry;
    }

    public <T> T get(DataParameter<T> key) {
        return this.getEntry(key).getValue();
    }

    public <T> void set(DataParameter<T> key, T value) {
        DataEntry<T> dataentry = this.getEntry(key);
        if (ObjectUtils.notEqual(value, dataentry.getValue())) {
            dataentry.setValue(value);
            this.entity.notifyDataManagerChange(key);
            dataentry.setDirty(true);
            this.dirty = true;
        }
    }

    public <T> void setDirty(DataParameter<T> key) {
        ((DataEntry)this.getEntry(key)).dirty = true;
        this.dirty = true;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public static void writeEntries(List<DataEntry<?>> entriesIn, PacketBuffer buf) throws IOException {
        if (entriesIn != null) {
            int j = entriesIn.size();
            for (int i = 0; i < j; ++i) {
                DataEntry<?> dataentry = entriesIn.get(i);
                EntityDataManager.writeEntry(buf, dataentry);
            }
        }
        buf.writeByte(255);
    }

    @Nullable
    public List<DataEntry<?>> getDirty() {
        ArrayList<DataEntry<?>> list = null;
        if (this.dirty) {
            this.lock.readLock().lock();
            for (DataEntry<?> dataentry : this.entries.values()) {
                if (!dataentry.isDirty()) continue;
                dataentry.setDirty(false);
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(dataentry.func_192735_d());
            }
            this.lock.readLock().unlock();
        }
        this.dirty = false;
        return list;
    }

    public void writeEntries(PacketBuffer buf) throws IOException {
        this.lock.readLock().lock();
        for (DataEntry<?> dataentry : this.entries.values()) {
            EntityDataManager.writeEntry(buf, dataentry);
        }
        this.lock.readLock().unlock();
        buf.writeByte(255);
    }

    @Nullable
    public List<DataEntry<?>> getAll() {
        ArrayList<DataEntry<?>> list = null;
        this.lock.readLock().lock();
        for (DataEntry<?> dataentry : this.entries.values()) {
            if (list == null) {
                list = Lists.newArrayList();
            }
            list.add(dataentry.func_192735_d());
        }
        this.lock.readLock().unlock();
        return list;
    }

    private static <T> void writeEntry(PacketBuffer buf, DataEntry<T> entry) throws IOException {
        DataParameter<T> dataparameter = entry.getKey();
        int i = DataSerializers.getSerializerId(dataparameter.getSerializer());
        if (i < 0) {
            throw new EncoderException("Unknown serializer type " + dataparameter.getSerializer());
        }
        buf.writeByte(dataparameter.getId());
        buf.writeVarIntToBuffer(i);
        dataparameter.getSerializer().write(buf, entry.getValue());
    }

    @Nullable
    public static List<DataEntry<?>> readEntries(PacketBuffer buf) throws IOException {
        short i;
        ArrayList<DataEntry<?>> list = null;
        while ((i = buf.readUnsignedByte()) != 255) {
            int j;
            DataSerializer<?> dataserializer;
            if (list == null) {
                list = Lists.newArrayList();
            }
            if ((dataserializer = DataSerializers.getSerializer(j = buf.readVarIntFromBuffer())) == null) {
                throw new DecoderException("Unknown serializer type " + j);
            }
            list.add(new DataEntry(dataserializer.createKey(i), dataserializer.read(buf)));
        }
        return list;
    }

    public void setEntryValues(List<DataEntry<?>> entriesIn) {
        this.lock.writeLock().lock();
        for (DataEntry<?> dataentry : entriesIn) {
            DataEntry<?> dataentry1 = this.entries.get(dataentry.getKey().getId());
            if (dataentry1 == null) continue;
            this.setEntryValue(dataentry1, dataentry);
            this.entity.notifyDataManagerChange(dataentry.getKey());
        }
        this.lock.writeLock().unlock();
        this.dirty = true;
    }

    protected <T> void setEntryValue(DataEntry<T> target, DataEntry<?> source) {
        target.setValue(source.getValue());
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public void setClean() {
        this.dirty = false;
        this.lock.readLock().lock();
        for (DataEntry<?> dataentry : this.entries.values()) {
            dataentry.setDirty(false);
        }
        this.lock.readLock().unlock();
    }

    public static class DataEntry<T> {
        private final DataParameter<T> key;
        private T value;
        private boolean dirty;

        public DataEntry(DataParameter<T> keyIn, T valueIn) {
            this.key = keyIn;
            this.value = valueIn;
            this.dirty = true;
        }

        public DataParameter<T> getKey() {
            return this.key;
        }

        public void setValue(T valueIn) {
            this.value = valueIn;
        }

        public T getValue() {
            return this.value;
        }

        public boolean isDirty() {
            return this.dirty;
        }

        public void setDirty(boolean dirtyIn) {
            this.dirty = dirtyIn;
        }

        public DataEntry<T> func_192735_d() {
            return new DataEntry<T>(this.key, this.key.getSerializer().func_192717_a(this.value));
        }
    }
}

