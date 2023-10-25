package ru.smertnix.celestial.feature.impl.movement;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.MathHelper;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventReceivePacket;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.feature.impl.combat.AttackAura;
import ru.smertnix.celestial.ui.notification.NotificationMode;
import ru.smertnix.celestial.ui.notification.NotificationRenderer;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.math.TimerHelper;
import ru.smertnix.celestial.utils.movement.MovementUtils;

import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;

public class Flight extends Feature {
    public boolean damage = false;
    public boolean flaging = false;
    public TimerHelper timerHelper = new TimerHelper();
    public static ListSetting flyMode = new ListSetting("Mode", "Glide", () -> true, "Glide", "Vanilla", "ReallyWorld", "ReallyWorld2");
    public final NumberSetting speed = new NumberSetting("Flight Speed", 2F, 0.5F, 5, 0.1F, () -> flyMode.currentMode.equals("Vanilla") || flyMode.currentMode.equals("Matrix Glide"));
    boolean yes = true;
    public float ticks = 0;

    public Flight() {
        super("Flight", "Позволяет летать", FeatureCategory.Movement);
        addSettings(flyMode, speed);
    }
    
    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (this.isEnabled()) {
            if(event.getPacket() instanceof SPacketPlayerPosLook) {
                yes = false;
                return;
        	}
        }
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
    	 String mode = flyMode.getOptions();
     	if (mode.equalsIgnoreCase("ReallyWorld2")) {
     		if (mc.player.onGround) return;
            int eIndex = -1;

            for (int i = 0; i < 45; ++i) {
                ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
                if (itemStack.getItem() == Items.ELYTRA) {
                    eIndex = i;
                }
            }
            /*
            if (Celestial.instance.featureManager.getFeature(AttackAura.class).isEnabled() && AttackAura.target != null) {
         	   return;
            } else {
         	   event.setPitch(0);
                mc.player.rotationPitchHead = 0;
            }*/
            
            if (flaging) {
          	   mc.playerController.windowClick(0, eIndex, 0, ClickType.PICKUP, mc.player);
                 mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
                 flaging = false;
             }
           
            if (mc.player.isElytraFlying() ) {
     		   if (mc.player.ticksExisted % 30 <= 5) {
     			   mc.timer.timerSpeed = 0.6f;
     		   } else {
     			   mc.timer.timerSpeed = 1.5f;
     		   }
     		   mc.player.motionY = 0.42D; 
            } else {
         	   if (mc.player.ticksExisted % 3 == 0) {
         		   mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
         	   }
            }
     	}
    }
    
    @EventTarget
    public void onPreUpdate(EventUpdate event) {
        String mode = flyMode.getOptions();
    	if (mode.equalsIgnoreCase("ReallyWorld")) {
    		if (mc.player.onGround) return;
       int eIndex = -1;

       for (int i = 0; i < 45; ++i) {
           ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
           if (itemStack.getItem() == Items.ELYTRA) {
               eIndex = i;
           }
       }
       /*
       if (Celestial.instance.featureManager.getFeature(AttackAura.class).isEnabled() && AttackAura.target != null) {
    	   return;
       } else {
    	   event.setPitch(0);
           mc.player.rotationPitchHead = 0;
       }*/
       
       if (flaging) {
     	   mc.playerController.windowClick(0, eIndex, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
            flaging = false;
        }
      
           if (mc.player.isElytraFlying() ) {
    	   mc.timer.timerSpeed = 1;
    	   if (mc.player.ticksExisted % 20 < 15) {
    		   mc.player.motionY = 0.05;
    	   } else {
    		   mc.player.motionY = -0.05f;
    	   }
    	   if (mc.gameSettings.keyBindSneak.isKeyDown()) {
    		   mc.player.motionY -= 0.1f;
    	   }
    	   if (mc.gameSettings.keyBindJump.isKeyDown()) {
    		   if (mc.player.ticksExisted % 30 <= 5) {
    			   mc.timer.timerSpeed = 0.6f;
    		   } else {
    			   mc.timer.timerSpeed = 1.5f;
    		   }
    			   mc.player.motionY = 0.42;
    	   }
       } else {
    	   if (mc.player.ticksExisted % 3 == 0) {
    		   mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
    	   }
       }
        }
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        String mode = flyMode.getOptions();
       if (mode.equalsIgnoreCase("Glide")) {
        	
        	
        		 if (mc.player.onGround) {
                     mc.player.jump();
                     timerHelper.reset();
                 } else if (!mc.player.onGround && timerHelper.hasReached(280)) {
                     mc.player.motionY = -0.04D;

                     MovementUtils.setSpeed(speed.getNumberValue());
                 }
        	
        	
        } else if (mode.equalsIgnoreCase("Vanilla")) {
            mc.player.capabilities.isFlying = true;
            mc.player.capabilities.allowFlying = true;

            if (mc.gameSettings.keyBindJump.pressed) {
                mc.player.motionY = 2.0;
            }

            if (mc.gameSettings.keyBindSneak.pressed) {
                mc.player.motionY = -2.0;
            }
            MovementUtils.setSpeed(speed.getNumberValue());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        String mode = flyMode.getOptions();
        int eIndex = -1;

        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.ELYTRA) {
                eIndex = i;
            }
        }
        mc.player.motionX = 0;
 	   mc.player.motionZ = 0;
 	  if (mode.equalsIgnoreCase("ReallyWorld") || mode.equalsIgnoreCase("ReallyWorld2")) {
 		 mc.playerController.windowClick(0, 6, 0, ClickType.QUICK_MOVE, mc.player);
         mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, mc.player);
 	  }
        yes = true;
        flaging = true;
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.allowFlying = false;
        mc.timer.timerSpeed = 1f;
        ticks = 0;
    }
}