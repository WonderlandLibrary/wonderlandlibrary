package com.enjoytheban.module.modules.player;

import net.minecraft.entity.player.EnumPlayerModelParts;

import java.awt.*;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventTick;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

/**
 * Makes ur skin FLASH
 *
 * @author someone
 */

public class SkinFlash extends Module {

    public SkinFlash() {
        super("SkinFlash", new String[]{"derpskin"}, ModuleType.Player);
        setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
    }

    @Override
    public void onDisable() {
    	 if (mc.thePlayer != null) {
             EnumPlayerModelParts[] parts = EnumPlayerModelParts.values();
             if (parts != null) {
                 EnumPlayerModelParts[] arrayOfEnumPlayerModelParts1;
                 int j = (arrayOfEnumPlayerModelParts1 = parts).length;
                 for (int i = 0; i < j; i++) {
                     EnumPlayerModelParts part = arrayOfEnumPlayerModelParts1[i];
                     mc.gameSettings.func_178878_a(part, true);
                 }
             }
         }
     }
    
    @EventHandler
    private void onTick(EventTick e) {
        if (mc.thePlayer != null) {
            EnumPlayerModelParts[] parts = EnumPlayerModelParts.values();
            if (parts != null) {
                EnumPlayerModelParts[] arrayOfEnumPlayerModelParts1;
                int j = (arrayOfEnumPlayerModelParts1 = parts).length;
                for (int i = 0; i < j; i++) {
                    EnumPlayerModelParts part = arrayOfEnumPlayerModelParts1[i];
                    boolean newState = isEnabled() ? this.random.nextBoolean() : true;
                    mc.gameSettings.func_178878_a(part, newState);
                }
            }
        }
    }
}