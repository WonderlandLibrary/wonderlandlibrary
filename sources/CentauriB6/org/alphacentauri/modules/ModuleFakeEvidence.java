package org.alphacentauri.modules;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.src.CapeUtils;
import net.minecraft.util.ResourceLocation;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRenderString;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModuleFakeEvidence extends Module implements EventListener {
   private Property real = new Property(this, "RealPlayer", "ChangeME");
   private Property fake = new Property(this, "FakePlayer", "ChangeME");
   private Property sprint = new Property(this, "Sprint", Boolean.valueOf(true));
   private Property sneak = new Property(this, "Sneak", Boolean.valueOf(true));
   private Property block = new Property(this, "Block", Boolean.valueOf(true));

   public ModuleFakeEvidence() {
      super("FakeEvidence", "Get players you dont like banned.", new String[]{"fakeevidence"}, Module.Category.Fun, 118094);
   }

   public void setEnabledSilent(boolean enabled) {
      super.setEnabledSilent(enabled);
      if(enabled) {
         if(((String)this.fake.value).equalsIgnoreCase((String)this.real.value)) {
            return;
         }

         for(EntityPlayer playerEntity : AC.getMC().getWorld().playerEntities) {
            if(playerEntity instanceof AbstractClientPlayer && playerEntity.getName().equalsIgnoreCase((String)this.real.value)) {
               NetworkPlayerInfo playerInfo = ((AbstractClientPlayer)playerEntity).getPlayerInfo();
               playerInfo.playerTexturesLoaded = false;
               playerInfo.locationSkin = new ResourceLocation("fakeskin/" + (String)this.fake.value);
               AbstractClientPlayer.getDownloadImageSkin(playerInfo.locationSkin, (String)this.fake.value);
               CapeUtils.downloadCape((AbstractClientPlayer)playerEntity, (String)this.fake.value);
            }
         }
      } else {
         if(((String)this.fake.value).equalsIgnoreCase((String)this.real.value)) {
            return;
         }

         for(EntityPlayer playerEntity : AC.getMC().getWorld().playerEntities) {
            if(playerEntity instanceof AbstractClientPlayer && playerEntity.getName().equalsIgnoreCase((String)this.real.value)) {
               NetworkPlayerInfo playerInfo = ((AbstractClientPlayer)playerEntity).getPlayerInfo();
               playerInfo.playerTexturesLoaded = false;
               playerInfo.locationSkin = null;
            }
         }
      }

   }

   public void onEvent(Event event) {
      if(event instanceof EventRenderString) {
         if(((EventRenderString)event).getText().contains((CharSequence)this.real.value)) {
            ((EventRenderString)event).setText(((EventRenderString)event).getText().replace((CharSequence)this.real.value, (CharSequence)this.fake.value));
         }
      } else if(event instanceof EventTick) {
         for(EntityPlayer playerEntity : AC.getMC().getWorld().playerEntities) {
            if(playerEntity.getName().equalsIgnoreCase((String)this.real.value)) {
               if(((Boolean)this.sprint.value).booleanValue()) {
                  playerEntity.setSprinting(true);
               }

               if(((Boolean)this.sneak.value).booleanValue()) {
                  playerEntity.setSneaking(true);
               }

               if(((Boolean)this.block.value).booleanValue() && playerEntity.getHeldItem() != null && playerEntity.getHeldItem().getItem() instanceof ItemSword) {
                  playerEntity.setItemInUse(playerEntity.getHeldItem(), 1337);
               }
            }
         }
      }

   }
}
