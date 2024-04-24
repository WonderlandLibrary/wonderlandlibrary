package me.crimp.claudius.mod.modules.render;

import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.init.SoundEvents;


public class EntityAlerts extends Module {
    public Setting<Boolean> Donkeye = register(new Setting<>("Donkey", true));
    public Setting<Boolean> Iteme = register(new Setting<>("Item", true));
    public Setting<Boolean> Ghaste = register(new Setting<>("Ghast", true));
    public Setting<Boolean> Horsee = register(new Setting<>("Horse", true));
    public Setting<Boolean> Llamae = register(new Setting<>("Llama", true));
    public Setting<Boolean> Parrot = register(new Setting<>("Parrot", true));
    public Setting<Boolean> Sound = register(new Setting<>("Sound", true));

    public EntityAlerts() {
        super("EntityAlerts", "Alerts You When A Few Different Entity Are In Render Distance", Category.Render, false, false);
    }
    int Sounddelay;
    int delay1;
    int delay2;
    int delay3;
    int delay4;
    int delay5;
    int delay6;

    @Override
    public void onEnable() {
        delay1 = 0;
        delay2 = 0;
        delay3 = 0;
        delay4 = 0;
        delay5 = 0;
        delay6 = 0;
        Sounddelay = 0;
    }

    @Override
    public void onUpdate() {
        if (Sounddelay > 0) --Sounddelay;
        if (this.Donkeye.getValue()) {
            if (delay1 > 0) --delay1;
            if (Sounddelay == 0) {
                mc.world.loadedEntityList.forEach(entity -> {
                    if (entity instanceof EntityDonkey && delay1 == 0) {
                        Command.sendMessage("Donkey Has Benn Spotted");
                        delay1 = 50;
                        if (this.Sound.getValue()) {
                            mc.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 4.0f, 12.0f);
                            Sounddelay = 25;
                        }

                    }
                });
            }
        }
        if (this.Iteme.getValue()) {
            if (delay2 > 0) --delay2;
            if (Sounddelay == 0) {
                mc.world.loadedEntityList.forEach(entity -> {
                            if (entity instanceof EntityItem && delay2 == 0) {
                                EntityItem item = (EntityItem) entity;
                                Command.sendMessage("Items On Ground Are" + item);
                                delay2 = 100;
                                if (this.Sound.getValue()) {
                                    mc.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 4.0f, 12.0f);
                                    Sounddelay = 25;
                                }
                            }
                        }
                );
            }
        }
        if (this.Ghaste.getValue()) {
            if (delay3 > 0) --delay3;
            if (Sounddelay == 0) {
                mc.world.loadedEntityList.forEach(entity -> {
                    if (entity instanceof EntityGhast && delay3 == 0) {
                        Command.sendMessage("Ghast Has Benn Spotted");
                        delay3 = 50;
                        if (this.Sound.getValue()) {
                            mc.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 4.0f, 12.0f);
                            Sounddelay = 25;
                        }

                    }
                });
            }
        }
        if (this.Horsee.getValue()) {
            if (delay4 > 0) --delay4;
            if (Sounddelay == 0) {
                mc.world.loadedEntityList.forEach(entity -> {
                    if (entity instanceof EntityHorse && delay4 == 0) {
                        Command.sendMessage("Horse Has Benn Spotted");
                        delay4 = 50;
                        if (this.Sound.getValue()) {
                            mc.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 4.0f, 12.0f);
                            Sounddelay = 25;
                        }
                    }
                });
            }
        }
        if (this.Llamae.getValue()) {
            if (delay5 > 0) --delay5;
            if (Sounddelay == 0) {
                mc.world.loadedEntityList.forEach(entity -> {
                    if (entity instanceof EntityLlama && delay5 == 0) {
                        Command.sendMessage("Llama Has Benn Spotted");
                        delay4 = 50;
                        if (this.Sound.getValue()) {
                            mc.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 4.0f, 12.0f);
                            Sounddelay = 25;
                        }
                    }
                });
            }
        }
        if (this.Parrot.getValue()) {
            if (delay6 > 0) --delay6;
            if (Sounddelay == 0) {
                mc.world.loadedEntityList.forEach(entity -> {
                    if (entity instanceof EntityParrot && delay6 == 0) {
                        Command.sendMessage("Parrot Has Benn Spotted");
                        delay6 = 50;
                        if (this.Sound.getValue()) {
                            mc.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 4.0f, 12.0f);
                            Sounddelay = 25;
                        }
                    }
                });
            }
        }
    }
}