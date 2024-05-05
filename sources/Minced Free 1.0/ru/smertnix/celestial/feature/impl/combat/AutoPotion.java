package ru.smertnix.celestial.feature.impl.combat;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.MultipleBoolSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.math.TimerHelper;

public class AutoPotion extends Feature {
    public TimerHelper timerHelper = new TimerHelper();
    public static MultipleBoolSetting potions = new MultipleBoolSetting("Potions seletion",
    		new BooleanSetting("Speed", true),
    		new BooleanSetting("Strength", true),
    		new BooleanSetting("Fire Resistance", true));

    public AutoPotion() {
        super("Auto Pot", "Автоматически бросает зелья для пвп", FeatureCategory.Combat);
        addSettings(potions);
    }

        @EventTarget
        public void onUpdate(EventPreMotion event) {
            if ((mc.currentScreen instanceof GuiInventory)) {
                return;
            }
            if (!mc.player.onGround) {
                return;
            }
            if (timerHelper.hasReached(500)) {
            	if (potions.getSetting("Strength").getBoolValue()) {
            		if (!mc.player.isPotionActive((Potion.getPotionById(5))) && isPotionOnHotBar(Potions.STRENGTH)) {
                        if (mc.player.onGround) {
                            event.setPitch(90);
                        } else {
                            event.setPitch(-90);
                        }
                        if (mc.player.onGround) {
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, mc.player.onGround));
                        } else {
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -90, mc.player.onGround));
                        }
                        if (mc.player.onGround) {
                            if (event.getPitch() == 90) {
                                throwPot(Potions.STRENGTH);
                            }
                        } else {
                            if (event.getPitch() == -90) {
                                throwPot(Potions.STRENGTH);
                            }
                        }
                    }
            	}
            	if (potions.getSetting("Speed").getBoolValue()) {
            		if (!mc.player.isPotionActive((Potion.getPotionById(1))) && isPotionOnHotBar(Potions.SPEED)) {
                        if (mc.player.onGround) {
                            event.setPitch(90);
                        } else {
                            event.setPitch(-90);

                        }
                        if (mc.player.onGround) {
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, mc.player.onGround));
                        } else {
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -90, mc.player.onGround));
                        }
                        if (mc.player.onGround) {
                            if (event.getPitch() == 90) {
                                throwPot(Potions.SPEED);
                            }
                        } else {
                            if (event.getPitch() == -90) {
                                throwPot(Potions.SPEED);
                            }
                        }
                    }
            	}
            	if (potions.getSetting("Fire Resistance").getBoolValue()) {
            		if (!mc.player.isPotionActive((Potion.getPotionById(12))) && isPotionOnHotBar(Potions.FIRERES)) {
                        if (mc.player.onGround) {
                            event.setPitch(90);
                        } else {
                            event.setPitch(-90);
                        }
                        if (mc.player.onGround) {
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, mc.player.onGround));
                        } else {
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -90, mc.player.onGround));
                        }
                        if (mc.player.onGround) {
                            if (event.getPitch() == 90) {
                                throwPot(Potions.FIRERES);
                            }
                        } else {
                            if (event.getPitch() == -90) {
                                throwPot(Potions.FIRERES);
                            }
                        }
                    }
            	}
                timerHelper.reset();
            }
        }

        void throwPot(Potions potion) {
            int slot = getPotionSlot(potion);
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.playerController.updateController();
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.playerController.updateController();
            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
        }

        public int getPotionSlot(Potions potion) {
            for (int i = 0; i < 9; ++i) {
                if (this.isStackPotion(mc.player.inventory.getStackInSlot(i), potion))
                    return i;
            }
            return -1;
        }

        public boolean isPotionOnHotBar(Potions potions) {
            for (int i = 0; i < 9; ++i) {
                if (isStackPotion(mc.player.inventory.getStackInSlot(i), potions)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isStackPotion(ItemStack stack, Potions potion) {
            if (stack == null)
                return false;

            Item item = stack.getItem();

            if (item == Items.SPLASH_POTION) {
                int id = 5;

                switch (potion) {
                    case STRENGTH: {
                    	id = 5;
                        break;
                    }
                    case SPEED: {
                        id = 1;
                        break;
                    }
                    case FIRERES: {
                        id = 12;
                        break;
                    }
                }

                for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
                    if (effect.getPotion() == Potion.getPotionById(id)) {
                        return true;
                    }
                }
            }
            return false;
        }

        enum Potions {
            STRENGTH, SPEED, FIRERES
        }
    }