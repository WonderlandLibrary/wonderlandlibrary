package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.List;

public class SoundList {
   private final List soundList = Lists.newArrayList();
   private SoundCategory category;
   private boolean replaceExisting;

   public List getSoundList() {
      return this.soundList;
   }

   public void setSoundCategory(SoundCategory var1) {
      this.category = var1;
   }

   public void setReplaceExisting(boolean var1) {
      this.replaceExisting = var1;
   }

   public boolean canReplaceExisting() {
      return this.replaceExisting;
   }

   public SoundCategory getSoundCategory() {
      return this.category;
   }

   public static class SoundEntry {
      private SoundList.SoundEntry.Type type;
      private float pitch = 1.0F;
      private boolean streaming;
      private float volume = 1.0F;
      private String name;
      private int weight = 1;

      public SoundList.SoundEntry.Type getSoundEntryType() {
         return this.type;
      }

      public void setSoundEntryWeight(int var1) {
         this.weight = var1;
      }

      public void setSoundEntryType(SoundList.SoundEntry.Type var1) {
         this.type = var1;
      }

      public String getSoundEntryName() {
         return this.name;
      }

      public SoundEntry() {
         this.type = SoundList.SoundEntry.Type.FILE;
         this.streaming = false;
      }

      public float getSoundEntryVolume() {
         return this.volume;
      }

      public float getSoundEntryPitch() {
         return this.pitch;
      }

      public int getSoundEntryWeight() {
         return this.weight;
      }

      public void setStreaming(boolean var1) {
         this.streaming = var1;
      }

      public boolean isStreaming() {
         return this.streaming;
      }

      public void setSoundEntryPitch(float var1) {
         this.pitch = var1;
      }

      public void setSoundEntryName(String var1) {
         this.name = var1;
      }

      public void setSoundEntryVolume(float var1) {
         this.volume = var1;
      }

      public static enum Type {
         private static final SoundList.SoundEntry.Type[] ENUM$VALUES = new SoundList.SoundEntry.Type[]{FILE, SOUND_EVENT};
         private final String field_148583_c;
         FILE("file"),
         SOUND_EVENT("event");

         private Type(String var3) {
            this.field_148583_c = var3;
         }

         public static SoundList.SoundEntry.Type getType(String var0) {
            SoundList.SoundEntry.Type[] var4;
            int var3 = (var4 = values()).length;

            for(int var2 = 0; var2 < var3; ++var2) {
               SoundList.SoundEntry.Type var1 = var4[var2];
               if (var1.field_148583_c.equals(var0)) {
                  return var1;
               }
            }

            return null;
         }
      }
   }
}
