package net.minecraft.util;

import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;

public class ChatComponentProcessor {
   public static IChatComponent processComponent(ICommandSender var0, IChatComponent var1, Entity var2) throws CommandException {
      Object var3 = null;
      if (var1 instanceof ChatComponentScore) {
         ChatComponentScore var4 = (ChatComponentScore)var1;
         String var5 = var4.getName();
         if (PlayerSelector.hasArguments(var5)) {
            List var6 = PlayerSelector.matchEntities(var0, var5, Entity.class);
            if (var6.size() != 1) {
               throw new EntityNotFoundException();
            }

            var5 = ((Entity)var6.get(0)).getName();
         }

         var3 = var2 != null && var5.equals("*") ? new ChatComponentScore(var2.getName(), var4.getObjective()) : new ChatComponentScore(var5, var4.getObjective());
         ((ChatComponentScore)var3).setValue(var4.getUnformattedTextForChat());
      } else if (var1 instanceof ChatComponentSelector) {
         String var8 = ((ChatComponentSelector)var1).getSelector();
         var3 = PlayerSelector.matchEntitiesToChatComponent(var0, var8);
         if (var3 == null) {
            var3 = new ChatComponentText("");
         }
      } else if (var1 instanceof ChatComponentText) {
         var3 = new ChatComponentText(((ChatComponentText)var1).getChatComponentText_TextValue());
      } else {
         if (!(var1 instanceof ChatComponentTranslation)) {
            return var1;
         }

         Object[] var9 = ((ChatComponentTranslation)var1).getFormatArgs();

         for(int var11 = 0; var11 < var9.length; ++var11) {
            Object var12 = var9[var11];
            if (var12 instanceof IChatComponent) {
               var9[var11] = processComponent(var0, (IChatComponent)var12, var2);
            }
         }

         var3 = new ChatComponentTranslation(((ChatComponentTranslation)var1).getKey(), var9);
      }

      ChatStyle var10 = var1.getChatStyle();
      if (var10 != null) {
         ((IChatComponent)var3).setChatStyle(var10.createShallowCopy());
      }

      Iterator var13 = var1.getSiblings().iterator();

      while(var13.hasNext()) {
         IChatComponent var14 = (IChatComponent)var13.next();
         ((IChatComponent)var3).appendSibling(processComponent(var0, var14, var2));
      }

      return (IChatComponent)var3;
   }
}
