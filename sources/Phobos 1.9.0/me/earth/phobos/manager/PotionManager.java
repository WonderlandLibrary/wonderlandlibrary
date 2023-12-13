package me.earth.phobos.manager;

import me.earth.phobos.features.Feature;
import me.earth.phobos.features.modules.client.HUD;
import me.earth.phobos.features.modules.client.Managers;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PotionManager
        extends Feature {
    private final Map<EntityPlayer, PotionList> potions = new ConcurrentHashMap<EntityPlayer, PotionList>();

    public void onLogout() {
        this.potions.clear();
    }

    public void updatePlayer() {
        PotionList list = new PotionList();
        for (PotionEffect effect : PotionManager.mc.player.getActivePotionEffects()) {
            list.addEffect(effect);
        }
        this.potions.put(PotionManager.mc.player, list);
    }

    public void update() {
        this.updatePlayer();
        if (HUD.getInstance().isOn() && HUD.getInstance().textRadar.getValue().booleanValue() && Managers.getInstance().potions.getValue().booleanValue()) {
            ArrayList<EntityPlayer> removeList = new ArrayList<EntityPlayer>();
            for (Map.Entry<EntityPlayer, PotionList> potionEntry : this.potions.entrySet()) {
                boolean notFound = true;
                for (EntityPlayer player : PotionManager.mc.world.playerEntities) {
                    if (this.potions.get(player) == null) {
                        PotionList list = new PotionList();
                        for (PotionEffect effect : player.getActivePotionEffects()) {
                            list.addEffect(effect);
                        }
                        this.potions.put(player, list);
                        notFound = false;
                    }
                    if (!potionEntry.getKey().equals(player)) continue;
                    notFound = false;
                }
                if (!notFound) continue;
                removeList.add(potionEntry.getKey());
            }
            for (EntityPlayer player : removeList) {
                this.potions.remove(player);
            }
        }
    }

    public List<PotionEffect> getOwnPotions() {
        return this.getPlayerPotions(PotionManager.mc.player);
    }

    public List<PotionEffect> getPlayerPotions(EntityPlayer player) {
        PotionList list = this.potions.get(player);
        List<PotionEffect> potions = new ArrayList<PotionEffect>();
        if (list != null) {
            potions = list.getEffects();
        }
        return potions;
    }

    public void onTotemPop(EntityPlayer player) {
        PotionList list = new PotionList();
        this.potions.put(player, list);
    }

    public PotionEffect[] getImportantPotions(EntityPlayer player) {
        PotionEffect[] array = new PotionEffect[3];
        for (PotionEffect effect : this.getPlayerPotions(player)) {
            Potion potion = effect.getPotion();
            switch (I18n.format(potion.getName(), new Object[0]).toLowerCase()) {
                case "strength": {
                    array[0] = effect;
                    break;
                }
                case "weakness": {
                    array[1] = effect;
                    break;
                }
                case "speed": {
                    array[2] = effect;
                    break;
                }
            }
        }
        return array;
    }

    public String getPotionString(PotionEffect effect) {
        Potion potion = effect.getPotion();
        return I18n.format(potion.getName(), new Object[0]) + " " + (HUD.getInstance().potions1.getValue() == false && effect.getAmplifier() == 0 ? "" : effect.getAmplifier() + 1 + " ") + "\u00a7f" + Potion.getPotionDurationString(effect, 1.0f);
    }

    public String getColoredPotionString(PotionEffect effect) {
        Potion potion = effect.getPotion();
        switch (I18n.format(potion.getName(), new Object[0])) {
            case "Jump Boost":
            case "Speed": {
                return "\u00a7b" + this.getPotionString(effect);
            }
            case "Resistance":
            case "Strength": {
                return "\u00a7c" + this.getPotionString(effect);
            }
            case "Wither":
            case "Slowness":
            case "Weakness": {
                return "\u00a70" + this.getPotionString(effect);
            }
            case "Absorption": {
                return "\u00a79" + this.getPotionString(effect);
            }
            case "Haste":
            case "Fire Resistance": {
                return "\u00a76" + this.getPotionString(effect);
            }
            case "Regeneration": {
                return "\u00a7d" + this.getPotionString(effect);
            }
            case "Night Vision":
            case "Poison": {
                return "\u00a7a" + this.getPotionString(effect);
            }
        }
        return "\u00a7f" + this.getPotionString(effect);
    }

    public String getTextRadarPotionWithDuration(EntityPlayer player) {
        PotionEffect[] array = this.getImportantPotions(player);
        PotionEffect strength = array[0];
        PotionEffect weakness = array[1];
        PotionEffect speed = array[2];
        return "" + (strength != null ? "\u00a7c S" + (strength.getAmplifier() + 1) + " " + Potion.getPotionDurationString(strength, 1.0f) : "") + (weakness != null ? "\u00a78 W " + Potion.getPotionDurationString(weakness, 1.0f) : "") + (speed != null ? "\u00a7b S" + (speed.getAmplifier() + 1) + " " + Potion.getPotionDurationString(weakness, 1.0f) : "");
    }

    public String getTextRadarPotion(EntityPlayer player) {
        PotionEffect[] array = this.getImportantPotions(player);
        PotionEffect strength = array[0];
        PotionEffect weakness = array[1];
        PotionEffect speed = array[2];
        return "" + (strength != null ? "\u00a7c S" + (strength.getAmplifier() + 1) + " " : "") + (weakness != null ? "\u00a78 W " : "") + (speed != null ? "\u00a7b S" + (speed.getAmplifier() + 1) + " " : "");
    }

    public static class PotionList {
        private final List<PotionEffect> effects = new ArrayList<PotionEffect>();

        public void addEffect(PotionEffect effect) {
            if (effect != null) {
                this.effects.add(effect);
            }
        }

        public List<PotionEffect> getEffects() {
            return this.effects;
        }
    }
}

