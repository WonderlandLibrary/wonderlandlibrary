package cn.hanabi.utils;

import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;

public class WorldUtil {
   public static List getLivingEntities() {
      return Arrays.asList(Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter((entity) -> {
         return entity instanceof EntityLivingBase;
      }).filter((entity) -> {
         return entity != Minecraft.getMinecraft().thePlayer;
      }).map((entity) -> {
         return (EntityLivingBase)entity;
      }).toArray((x$0) -> {
         return new EntityLivingBase[x$0];
      }));
   }

   public static List getEntities() {
      return Arrays.asList(Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter((entity) -> {
         return entity != Minecraft.getMinecraft().thePlayer;
      }).toArray((x$0) -> {
         return new Entity[x$0];
      }));
   }

   public static List getLivingPlayers() {
      return Arrays.asList(Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter((entity) -> {
         return entity instanceof EntityPlayer;
      }).filter((entity) -> {
         return entity != Minecraft.getMinecraft().thePlayer;
      }).map((entity) -> {
         return (EntityPlayer)entity;
      }).toArray((x$0) -> {
         return new EntityPlayer[x$0];
      }));
   }

   public static List getTileEntities() {
      return Minecraft.getMinecraft().theWorld.loadedTileEntityList;
   }

   public static List getChestTileEntities() {
      return Arrays.asList(Minecraft.getMinecraft().theWorld.loadedTileEntityList.stream().filter((entity) -> {
         return entity instanceof TileEntityChest;
      }).map((entity) -> {
         return (TileEntityChest)entity;
      }).toArray((x$0) -> {
         return new TileEntityChest[x$0];
      }));
   }

   public static List getEnderChestTileEntities() {
      return Arrays.asList(Minecraft.getMinecraft().theWorld.loadedTileEntityList.stream().filter((entity) -> {
         return entity instanceof TileEntityEnderChest;
      }).map((entity) -> {
         return (TileEntityEnderChest)entity;
      }).toArray((x$0) -> {
         return new TileEntityEnderChest[x$0];
      }));
   }
}
