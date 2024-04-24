package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.potion.Potion;

public class PotionInstance extends Block {

    public PotionInstance() {
        super("PotionInstance", MainBlockType.Potion, Tabs.Entity, BlockObjects.Name, BlockObjects.DropDown);
        this.description = "Returns the Potion";


        ddOptions.add("speed");
        ddOptions.add("slowness");
        ddOptions.add("haste");
        ddOptions.add("mining_fatigue");
        ddOptions.add("strength");
        ddOptions.add("instant_health");
        ddOptions.add("instant_damage");
        ddOptions.add("jump_boost");
        ddOptions.add("nausea");
        ddOptions.add("regeneration");
        ddOptions.add("resistance");
        ddOptions.add("fire_resistance");
        ddOptions.add("water_breathing");
        ddOptions.add("invisibility");
        ddOptions.add("blindness");
        ddOptions.add("night_vision");
        ddOptions.add("hunger");
        ddOptions.add("weakness");
        ddOptions.add("poison");
        ddOptions.add("wither");
        ddOptions.add("health_boost");
        ddOptions.add("absorption");
        ddOptions.add("saturation");
        ddOptions.add("glowing");
        ddOptions.add("levitation");
        ddOptions.add("luck");
        ddOptions.add("unluck");
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "speed":
                return Potion.getPotionById(1);
            case "slowness":
                return Potion.getPotionById(2);
            case "haste":
                return Potion.getPotionById(3);
            case "mining_fatigue":
                return Potion.getPotionById(4);
            case "strength":
                return Potion.getPotionById(5);
            case "instant_health":
                return Potion.getPotionById(6);
            case "instant_damage":
                return Potion.getPotionById(7);
            case "jump_boost":
                return Potion.getPotionById(8);
            case "nausea":
                return Potion.getPotionById(9);
            case "regeneration":
                return Potion.getPotionById(10);
            case "resistance":
                return Potion.getPotionById(11);
            case "fire_resistance":
                return Potion.getPotionById(12);
            case "water_breathing":
                return Potion.getPotionById(13);
            case "invisibility":
                return Potion.getPotionById(14);
            case "blindness":
                return Potion.getPotionById(15);
            case "night_vision":
                return Potion.getPotionById(16);
            case "hunger":
                return Potion.getPotionById(17);
            case "weakness":
                return Potion.getPotionById(18);
            case "poison":
                return Potion.getPotionById(19);
            case "wither":
                return Potion.getPotionById(20);
            case "health_boost":
                return Potion.getPotionById(21);
            case "absorption":
                return Potion.getPotionById(22);
            case "saturation":
                return Potion.getPotionById(23);
            case "glowing":
                return Potion.getPotionById(24);
            case "levitation":
                return Potion.getPotionById(25);
            case "luck":
                return Potion.getPotionById(26);
            case "unluck":
                return Potion.getPotionById(27);


        }
        return false;

    }

}
