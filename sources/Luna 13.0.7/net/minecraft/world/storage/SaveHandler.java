package net.minecraft.world.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveHandler
  implements ISaveHandler, IPlayerFileData
{
  private static final Logger logger = ;
  private final File worldDirectory;
  private final File playersDirectory;
  private final File mapDataDir;
  private final long initializationTime = MinecraftServer.getCurrentTimeMillis();
  private final String saveDirectoryName;
  private static final String __OBFID = "CL_00000585";
  
  public SaveHandler(File p_i2146_1_, String p_i2146_2_, boolean p_i2146_3_)
  {
    this.worldDirectory = new File(p_i2146_1_, p_i2146_2_);
    this.worldDirectory.mkdirs();
    this.playersDirectory = new File(this.worldDirectory, "playerdata");
    this.mapDataDir = new File(this.worldDirectory, "data");
    this.mapDataDir.mkdirs();
    this.saveDirectoryName = p_i2146_2_;
    if (p_i2146_3_) {
      this.playersDirectory.mkdirs();
    }
    setSessionLock();
  }
  
  /* Error */
  private void setSessionLock()
  {
    // Byte code:
    //   0: new 4	java/io/File
    //   3: dup
    //   4: aload_0
    //   5: getfield 6	net/minecraft/world/storage/SaveHandler:worldDirectory	Ljava/io/File;
    //   8: ldc 14
    //   10: invokespecial 5	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   13: astore_1
    //   14: new 15	java/io/DataOutputStream
    //   17: dup
    //   18: new 16	java/io/FileOutputStream
    //   21: dup
    //   22: aload_1
    //   23: invokespecial 17	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   26: invokespecial 18	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   29: astore_2
    //   30: aload_2
    //   31: aload_0
    //   32: getfield 3	net/minecraft/world/storage/SaveHandler:initializationTime	J
    //   35: invokevirtual 19	java/io/DataOutputStream:writeLong	(J)V
    //   38: aload_2
    //   39: invokevirtual 20	java/io/DataOutputStream:close	()V
    //   42: goto +10 -> 52
    //   45: astore_3
    //   46: aload_2
    //   47: invokevirtual 20	java/io/DataOutputStream:close	()V
    //   50: aload_3
    //   51: athrow
    //   52: goto +18 -> 70
    //   55: astore_1
    //   56: aload_1
    //   57: invokevirtual 22	java/io/IOException:printStackTrace	()V
    //   60: new 23	java/lang/RuntimeException
    //   63: dup
    //   64: ldc 24
    //   66: invokespecial 25	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   69: athrow
    //   70: return
    // Line number table:
    //   Java source line #63	-> byte code offset #0
    //   Java source line #64	-> byte code offset #14
    //   Java source line #68	-> byte code offset #30
    //   Java source line #72	-> byte code offset #38
    //   Java source line #73	-> byte code offset #42
    //   Java source line #72	-> byte code offset #45
    //   Java source line #73	-> byte code offset #50
    //   Java source line #79	-> byte code offset #52
    //   Java source line #75	-> byte code offset #55
    //   Java source line #77	-> byte code offset #56
    //   Java source line #78	-> byte code offset #60
    //   Java source line #80	-> byte code offset #70
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	71	0	this	SaveHandler
    //   13	10	1	var1	File
    //   55	2	1	var7	java.io.IOException
    //   29	18	2	var2	java.io.DataOutputStream
    //   45	6	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   30	38	45	finally
    //   0	52	55	java/io/IOException
  }
  
  public File getWorldDirectory()
  {
    return this.worldDirectory;
  }
  
  /* Error */
  public void checkSessionLock()
    throws net.minecraft.world.MinecraftException
  {
    // Byte code:
    //   0: new 4	java/io/File
    //   3: dup
    //   4: aload_0
    //   5: getfield 6	net/minecraft/world/storage/SaveHandler:worldDirectory	Ljava/io/File;
    //   8: ldc 14
    //   10: invokespecial 5	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   13: astore_1
    //   14: new 26	java/io/DataInputStream
    //   17: dup
    //   18: new 27	java/io/FileInputStream
    //   21: dup
    //   22: aload_1
    //   23: invokespecial 28	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   26: invokespecial 29	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
    //   29: astore_2
    //   30: aload_2
    //   31: invokevirtual 30	java/io/DataInputStream:readLong	()J
    //   34: aload_0
    //   35: getfield 3	net/minecraft/world/storage/SaveHandler:initializationTime	J
    //   38: lcmp
    //   39: ifeq +13 -> 52
    //   42: new 31	net/minecraft/world/MinecraftException
    //   45: dup
    //   46: ldc 32
    //   48: invokespecial 33	net/minecraft/world/MinecraftException:<init>	(Ljava/lang/String;)V
    //   51: athrow
    //   52: aload_2
    //   53: invokevirtual 34	java/io/DataInputStream:close	()V
    //   56: goto +10 -> 66
    //   59: astore_3
    //   60: aload_2
    //   61: invokevirtual 34	java/io/DataInputStream:close	()V
    //   64: aload_3
    //   65: athrow
    //   66: goto +14 -> 80
    //   69: astore_1
    //   70: new 31	net/minecraft/world/MinecraftException
    //   73: dup
    //   74: ldc 24
    //   76: invokespecial 33	net/minecraft/world/MinecraftException:<init>	(Ljava/lang/String;)V
    //   79: athrow
    //   80: return
    // Line number table:
    //   Java source line #97	-> byte code offset #0
    //   Java source line #98	-> byte code offset #14
    //   Java source line #102	-> byte code offset #30
    //   Java source line #104	-> byte code offset #42
    //   Java source line #109	-> byte code offset #52
    //   Java source line #110	-> byte code offset #56
    //   Java source line #109	-> byte code offset #59
    //   Java source line #110	-> byte code offset #64
    //   Java source line #115	-> byte code offset #66
    //   Java source line #112	-> byte code offset #69
    //   Java source line #114	-> byte code offset #70
    //   Java source line #116	-> byte code offset #80
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	81	0	this	SaveHandler
    //   13	10	1	var1	File
    //   69	2	1	var7	java.io.IOException
    //   29	32	2	var2	java.io.DataInputStream
    //   59	6	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   30	52	59	finally
    //   0	66	69	java/io/IOException
  }
  
  public IChunkLoader getChunkLoader(WorldProvider p_75763_1_)
  {
    throw new RuntimeException("Old Chunk Storage is no longer supported.");
  }
  
  public WorldInfo loadWorldInfo()
  {
    File var1 = new File(this.worldDirectory, "level.dat");
    if (var1.exists()) {
      try
      {
        NBTTagCompound var2 = CompressedStreamTools.readCompressed(new FileInputStream(var1));
        NBTTagCompound var3 = var2.getCompoundTag("Data");
        return new WorldInfo(var3);
      }
      catch (Exception var5)
      {
        var5.printStackTrace();
      }
    }
    var1 = new File(this.worldDirectory, "level.dat_old");
    if (var1.exists()) {
      try
      {
        NBTTagCompound var2 = CompressedStreamTools.readCompressed(new FileInputStream(var1));
        NBTTagCompound var3 = var2.getCompoundTag("Data");
        return new WorldInfo(var3);
      }
      catch (Exception var4)
      {
        var4.printStackTrace();
      }
    }
    return null;
  }
  
  public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_)
  {
    NBTTagCompound var3 = p_75755_1_.cloneNBTCompound(p_75755_2_);
    NBTTagCompound var4 = new NBTTagCompound();
    var4.setTag("Data", var3);
    try
    {
      File var5 = new File(this.worldDirectory, "level.dat_new");
      File var6 = new File(this.worldDirectory, "level.dat_old");
      File var7 = new File(this.worldDirectory, "level.dat");
      CompressedStreamTools.writeCompressed(var4, new FileOutputStream(var5));
      if (var6.exists()) {
        var6.delete();
      }
      var7.renameTo(var6);
      if (var7.exists()) {
        var7.delete();
      }
      var5.renameTo(var7);
      if (var5.exists()) {
        var5.delete();
      }
    }
    catch (Exception var8)
    {
      var8.printStackTrace();
    }
  }
  
  public void saveWorldInfo(WorldInfo p_75761_1_)
  {
    NBTTagCompound var2 = p_75761_1_.getNBTTagCompound();
    NBTTagCompound var3 = new NBTTagCompound();
    var3.setTag("Data", var2);
    try
    {
      File var4 = new File(this.worldDirectory, "level.dat_new");
      File var5 = new File(this.worldDirectory, "level.dat_old");
      File var6 = new File(this.worldDirectory, "level.dat");
      CompressedStreamTools.writeCompressed(var3, new FileOutputStream(var4));
      if (var5.exists()) {
        var5.delete();
      }
      var6.renameTo(var5);
      if (var6.exists()) {
        var6.delete();
      }
      var4.renameTo(var6);
      if (var4.exists()) {
        var4.delete();
      }
    }
    catch (Exception var7)
    {
      var7.printStackTrace();
    }
  }
  
  public void writePlayerData(EntityPlayer p_75753_1_)
  {
    try
    {
      NBTTagCompound var2 = new NBTTagCompound();
      p_75753_1_.writeToNBT(var2);
      File var3 = new File(this.playersDirectory, p_75753_1_.getUniqueID().toString() + ".dat.tmp");
      File var4 = new File(this.playersDirectory, p_75753_1_.getUniqueID().toString() + ".dat");
      CompressedStreamTools.writeCompressed(var2, new FileOutputStream(var3));
      if (var4.exists()) {
        var4.delete();
      }
      var3.renameTo(var4);
    }
    catch (Exception var5)
    {
      logger.warn("Failed to save player data for " + p_75753_1_.getName());
    }
  }
  
  public NBTTagCompound readPlayerData(EntityPlayer p_75752_1_)
  {
    NBTTagCompound var2 = null;
    try
    {
      File var3 = new File(this.playersDirectory, p_75752_1_.getUniqueID().toString() + ".dat");
      if ((var3.exists()) && (var3.isFile())) {
        var2 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
      }
    }
    catch (Exception var4)
    {
      logger.warn("Failed to load player data for " + p_75752_1_.getName());
    }
    if (var2 != null) {
      p_75752_1_.readFromNBT(var2);
    }
    return var2;
  }
  
  public IPlayerFileData getPlayerNBTManager()
  {
    return this;
  }
  
  public String[] getAvailablePlayerDat()
  {
    String[] var1 = this.playersDirectory.list();
    if (var1 == null) {
      var1 = new String[0];
    }
    for (int var2 = 0; var2 < var1.length; var2++) {
      if (var1[var2].endsWith(".dat")) {
        var1[var2] = var1[var2].substring(0, var1[var2].length() - 4);
      }
    }
    return var1;
  }
  
  public void flush() {}
  
  public File getMapFileFromName(String p_75758_1_)
  {
    return new File(this.mapDataDir, p_75758_1_ + ".dat");
  }
  
  public String getWorldDirectoryName()
  {
    return this.saveDirectoryName;
  }
}
