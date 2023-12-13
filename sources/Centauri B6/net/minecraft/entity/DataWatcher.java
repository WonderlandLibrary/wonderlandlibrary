package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.DataWatcher.WatchableObject;
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

   public DataWatcher(Entity owner) {
      this.owner = owner;
   }

   static {
      dataTypes.put(Byte.class, Integer.valueOf(0));
      dataTypes.put(Short.class, Integer.valueOf(1));
      dataTypes.put(Integer.class, Integer.valueOf(2));
      dataTypes.put(Float.class, Integer.valueOf(3));
      dataTypes.put(String.class, Integer.valueOf(4));
      dataTypes.put(ItemStack.class, Integer.valueOf(5));
      dataTypes.put(BlockPos.class, Integer.valueOf(6));
      dataTypes.put(Rotations.class, Integer.valueOf(7));
   }

   public void writeTo(PacketBuffer buffer) throws IOException {
      this.lock.readLock().lock();

      for(WatchableObject datawatcher$watchableobject : this.watchedObjects.values()) {
         writeWatchableObjectToPacketBuffer(buffer, datawatcher$watchableobject);
      }

      this.lock.readLock().unlock();
      buffer.writeByte(127);
   }

   public int getWatchableObjectInt(int id) {
      return ((Integer)this.getWatchedObject(id).getObject()).intValue();
   }

   public void updateWatchedObjectsFromList(List p_75687_1_) {
      this.lock.writeLock().lock();

      for(WatchableObject datawatcher$watchableobject : p_75687_1_) {
         WatchableObject datawatcher$watchableobject1 = (WatchableObject)this.watchedObjects.get(Integer.valueOf(datawatcher$watchableobject.getDataValueId()));
         if(datawatcher$watchableobject1 != null) {
            datawatcher$watchableobject1.setObject(datawatcher$watchableobject.getObject());
            this.owner.onDataWatcherUpdate(datawatcher$watchableobject.getDataValueId());
         }
      }

      this.lock.writeLock().unlock();
      this.objectChanged = true;
   }

   public short getWatchableObjectShort(int id) {
      return ((Short)this.getWatchedObject(id).getObject()).shortValue();
   }

   public String getWatchableObjectString(int id) {
      return (String)this.getWatchedObject(id).getObject();
   }

   public float getWatchableObjectFloat(int id) {
      return ((Float)this.getWatchedObject(id).getObject()).floatValue();
   }

   public byte getWatchableObjectByte(int id) {
      try {
         return ((Byte)this.getWatchedObject(id).getObject()).byteValue();
      } catch (Exception var3) {
         return (byte)0;
      }
   }

   public List getAllWatched() {
      List<WatchableObject> list = null;
      this.lock.readLock().lock();

      for(WatchableObject datawatcher$watchableobject : this.watchedObjects.values()) {
         if(list == null) {
            list = Lists.newArrayList();
         }

         list.add(datawatcher$watchableobject);
      }

      this.lock.readLock().unlock();
      return list;
   }

   public void addObject(int id, Object object) {
      Integer integer = (Integer)dataTypes.get(object.getClass());
      if(integer == null) {
         throw new IllegalArgumentException("Unknown data type: " + object.getClass());
      } else if(id > 31) {
         throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + 31 + ")");
      } else if(this.watchedObjects.containsKey(Integer.valueOf(id))) {
         throw new IllegalArgumentException("Duplicate id value for " + id + "!");
      } else {
         WatchableObject datawatcher$watchableobject = new WatchableObject(integer.intValue(), id, object);
         this.lock.writeLock().lock();
         this.watchedObjects.put(Integer.valueOf(id), datawatcher$watchableobject);
         this.lock.writeLock().unlock();
         this.isBlank = false;
      }
   }

   public void updateObject(int id, Object newData) {
      WatchableObject datawatcher$watchableobject = this.getWatchedObject(id);
      if(ObjectUtils.notEqual(newData, datawatcher$watchableobject.getObject())) {
         datawatcher$watchableobject.setObject(newData);
         this.owner.onDataWatcherUpdate(id);
         datawatcher$watchableobject.setWatched(true);
         this.objectChanged = true;
      }

   }

   public void setObjectWatched(int id) {
      WatchableObject.access$002(this.getWatchedObject(id), true);
      this.objectChanged = true;
   }

   public Rotations getWatchableObjectRotations(int id) {
      return (Rotations)this.getWatchedObject(id).getObject();
   }

   public ItemStack getWatchableObjectItemStack(int id) {
      return (ItemStack)this.getWatchedObject(id).getObject();
   }

   public void addObjectByDataType(int id, int type) {
      WatchableObject datawatcher$watchableobject = new WatchableObject(type, id, (Object)null);
      this.lock.writeLock().lock();
      this.watchedObjects.put(Integer.valueOf(id), datawatcher$watchableobject);
      this.lock.writeLock().unlock();
      this.isBlank = false;
   }

   public void func_111144_e() {
      this.objectChanged = false;
   }

   public boolean hasObjectChanged() {
      return this.objectChanged;
   }

   public static List readWatchedListFromPacketBuffer(PacketBuffer buffer) throws IOException {
      List<WatchableObject> list = null;

      for(int i = buffer.readByte(); i != 127; i = buffer.readByte()) {
         if(list == null) {
            list = Lists.newArrayList();
         }

         int j = (i & 224) >> 5;
         int k = i & 31;
         WatchableObject datawatcher$watchableobject = null;
         switch(j) {
         case 0:
            datawatcher$watchableobject = new WatchableObject(j, k, Byte.valueOf(buffer.readByte()));
            break;
         case 1:
            datawatcher$watchableobject = new WatchableObject(j, k, Short.valueOf(buffer.readShort()));
            break;
         case 2:
            datawatcher$watchableobject = new WatchableObject(j, k, Integer.valueOf(buffer.readInt()));
            break;
         case 3:
            datawatcher$watchableobject = new WatchableObject(j, k, Float.valueOf(buffer.readFloat()));
            break;
         case 4:
            datawatcher$watchableobject = new WatchableObject(j, k, buffer.readStringFromBuffer(32767));
            break;
         case 5:
            datawatcher$watchableobject = new WatchableObject(j, k, buffer.readItemStackFromBuffer());
            break;
         case 6:
            int l = buffer.readInt();
            int i1 = buffer.readInt();
            int j1 = buffer.readInt();
            datawatcher$watchableobject = new WatchableObject(j, k, new BlockPos(l, i1, j1));
            break;
         case 7:
            float f = buffer.readFloat();
            float f1 = buffer.readFloat();
            float f2 = buffer.readFloat();
            datawatcher$watchableobject = new WatchableObject(j, k, new Rotations(f, f1, f2));
         }

         list.add(datawatcher$watchableobject);
      }

      return list;
   }

   public static void writeWatchedListToPacketBuffer(List objectsList, PacketBuffer buffer) throws IOException {
      if(objectsList != null) {
         for(WatchableObject datawatcher$watchableobject : objectsList) {
            writeWatchableObjectToPacketBuffer(buffer, datawatcher$watchableobject);
         }
      }

      buffer.writeByte(127);
   }

   private WatchableObject getWatchedObject(int id) {
      this.lock.readLock().lock();

      WatchableObject datawatcher$watchableobject;
      try {
         datawatcher$watchableobject = (WatchableObject)this.watchedObjects.get(Integer.valueOf(id));
      } catch (Throwable var6) {
         CrashReport crashreport = CrashReport.makeCrashReport(var6, "Getting synched entity data");
         CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
         crashreportcategory.addCrashSection("Data ID", Integer.valueOf(id));
         throw new ReportedException(crashreport);
      }

      this.lock.readLock().unlock();
      return datawatcher$watchableobject;
   }

   public boolean getIsBlank() {
      return this.isBlank;
   }

   public List getChanged() {
      List<WatchableObject> list = null;
      if(this.objectChanged) {
         this.lock.readLock().lock();

         for(WatchableObject datawatcher$watchableobject : this.watchedObjects.values()) {
            if(datawatcher$watchableobject.isWatched()) {
               datawatcher$watchableobject.setWatched(false);
               if(list == null) {
                  list = Lists.newArrayList();
               }

               list.add(datawatcher$watchableobject);
            }
         }

         this.lock.readLock().unlock();
      }

      this.objectChanged = false;
      return list;
   }

   private static void writeWatchableObjectToPacketBuffer(PacketBuffer buffer, WatchableObject object) throws IOException {
      int i = (object.getObjectType() << 5 | object.getDataValueId() & 31) & 255;
      buffer.writeByte(i);
      switch(object.getObjectType()) {
      case 0:
         buffer.writeByte(((Byte)object.getObject()).byteValue());
         break;
      case 1:
         buffer.writeShort(((Short)object.getObject()).shortValue());
         break;
      case 2:
         buffer.writeInt(((Integer)object.getObject()).intValue());
         break;
      case 3:
         buffer.writeFloat(((Float)object.getObject()).floatValue());
         break;
      case 4:
         buffer.writeString((String)object.getObject());
         break;
      case 5:
         ItemStack itemstack = (ItemStack)object.getObject();
         buffer.writeItemStackToBuffer(itemstack);
         break;
      case 6:
         BlockPos blockpos = (BlockPos)object.getObject();
         buffer.writeInt(blockpos.getX());
         buffer.writeInt(blockpos.getY());
         buffer.writeInt(blockpos.getZ());
         break;
      case 7:
         Rotations rotations = (Rotations)object.getObject();
         buffer.writeFloat(rotations.getX());
         buffer.writeFloat(rotations.getY());
         buffer.writeFloat(rotations.getZ());
      }

   }
}
