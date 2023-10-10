package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatComponentTranslation extends ChatComponentStyle {
   private final String key;
   private final Object syncLock = new Object();
   List children = Lists.newArrayList();
   private final Object[] formatArgs;
   public static final Pattern stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
   private long lastTranslationUpdateTimeInMilliseconds = -1L;

   public IChatComponent createCopy() {
      return this.createCopy();
   }

   public int hashCode() {
      int var1 = super.hashCode();
      var1 = 31 * var1 + this.key.hashCode();
      var1 = 31 * var1 + Arrays.hashCode(this.formatArgs);
      return var1;
   }

   public Object[] getFormatArgs() {
      return this.formatArgs;
   }

   public Iterator iterator() {
      this.ensureInitialized();
      return Iterators.concat(createDeepCopyIterator(this.children), createDeepCopyIterator(this.siblings));
   }

   public ChatComponentTranslation createCopy() {
      Object[] var1 = new Object[this.formatArgs.length];

      for(int var2 = 0; var2 < this.formatArgs.length; ++var2) {
         if (this.formatArgs[var2] instanceof IChatComponent) {
            var1[var2] = ((IChatComponent)this.formatArgs[var2]).createCopy();
         } else {
            var1[var2] = this.formatArgs[var2];
         }
      }

      ChatComponentTranslation var5 = new ChatComponentTranslation(this.key, var1);
      var5.setChatStyle(this.getChatStyle().createShallowCopy());
      Iterator var4 = this.getSiblings().iterator();

      while(var4.hasNext()) {
         IChatComponent var3 = (IChatComponent)var4.next();
         var5.appendSibling(var3.createCopy());
      }

      return var5;
   }

   synchronized void ensureInitialized() {
      Object var1;
      synchronized(var1 = this.syncLock){}
      long var2 = StatCollector.getLastTranslationUpdateTimeInMilliseconds();
      if (var2 != this.lastTranslationUpdateTimeInMilliseconds) {
         this.lastTranslationUpdateTimeInMilliseconds = var2;
         this.children.clear();

         try {
            this.initializeFromFormat(StatCollector.translateToLocal(this.key));
         } catch (ChatComponentTranslationFormatException var5) {
            this.children.clear();

            try {
               this.initializeFromFormat(StatCollector.translateToFallback(this.key));
            } catch (ChatComponentTranslationFormatException var4) {
               throw var5;
            }
         }

      }
   }

   public IChatComponent setChatStyle(ChatStyle var1) {
      super.setChatStyle(var1);
      Object[] var5;
      int var4 = (var5 = this.formatArgs).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Object var2 = var5[var3];
         if (var2 instanceof IChatComponent) {
            ((IChatComponent)var2).getChatStyle().setParentStyle(this.getChatStyle());
         }
      }

      if (this.lastTranslationUpdateTimeInMilliseconds > -1L) {
         Iterator var8 = this.children.iterator();

         while(var8.hasNext()) {
            IChatComponent var7 = (IChatComponent)var8.next();
            var7.getChatStyle().setParentStyle(var1);
         }
      }

      return this;
   }

   protected void initializeFromFormat(String var1) {
      boolean var2 = false;
      Matcher var3 = stringVariablePattern.matcher(var1);
      int var4 = 0;
      int var5 = 0;

      try {
         int var6;
         for(; var3.find(var5); var5 = var6) {
            int var7 = var3.start();
            var6 = var3.end();
            if (var7 > var5) {
               ChatComponentText var8 = new ChatComponentText(String.format(var1.substring(var5, var7)));
               var8.getChatStyle().setParentStyle(this.getChatStyle());
               this.children.add(var8);
            }

            String var15 = var3.group(2);
            String var9 = var1.substring(var7, var6);
            if ("%".equals(var15) && "%%".equals(var9)) {
               ChatComponentText var16 = new ChatComponentText("%");
               var16.getChatStyle().setParentStyle(this.getChatStyle());
               this.children.add(var16);
            } else {
               if (!"s".equals(var15)) {
                  throw new ChatComponentTranslationFormatException(this, "Unsupported format: '" + var9 + "'");
               }

               String var10 = var3.group(1);
               int var11 = var10 != null ? Integer.parseInt(var10) - 1 : var4++;
               if (var11 < this.formatArgs.length) {
                  this.children.add(this.getFormatArgumentAsComponent(var11));
               }
            }
         }

         if (var5 < var1.length()) {
            ChatComponentText var14 = new ChatComponentText(String.format(var1.substring(var5)));
            var14.getChatStyle().setParentStyle(this.getChatStyle());
            this.children.add(var14);
         }

      } catch (IllegalFormatException var13) {
         throw new ChatComponentTranslationFormatException(this, var13);
      }
   }

   public String getUnformattedTextForChat() {
      this.ensureInitialized();
      StringBuilder var1 = new StringBuilder();
      Iterator var3 = this.children.iterator();

      while(var3.hasNext()) {
         IChatComponent var2 = (IChatComponent)var3.next();
         var1.append(var2.getUnformattedTextForChat());
      }

      return String.valueOf(var1);
   }

   private IChatComponent getFormatArgumentAsComponent(int var1) {
      if (var1 >= this.formatArgs.length) {
         throw new ChatComponentTranslationFormatException(this, var1);
      } else {
         Object var2 = this.formatArgs[var1];
         Object var3;
         if (var2 instanceof IChatComponent) {
            var3 = (IChatComponent)var2;
         } else {
            var3 = new ChatComponentText(var2 == null ? "null" : var2.toString());
            ((IChatComponent)var3).getChatStyle().setParentStyle(this.getChatStyle());
         }

         return (IChatComponent)var3;
      }
   }

   public ChatComponentTranslation(String var1, Object... var2) {
      this.key = var1;
      this.formatArgs = var2;
      Object[] var6 = var2;
      int var5 = var2.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Object var3 = var6[var4];
         if (var3 instanceof IChatComponent) {
            ((IChatComponent)var3).getChatStyle().setParentStyle(this.getChatStyle());
         }
      }

   }

   public String getKey() {
      return this.key;
   }

   public String toString() {
      return "TranslatableComponent{key='" + this.key + '\'' + ", args=" + Arrays.toString(this.formatArgs) + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ChatComponentTranslation)) {
         return false;
      } else {
         ChatComponentTranslation var2 = (ChatComponentTranslation)var1;
         return Arrays.equals(this.formatArgs, var2.formatArgs) && this.key.equals(var2.key) && super.equals(var1);
      }
   }
}
