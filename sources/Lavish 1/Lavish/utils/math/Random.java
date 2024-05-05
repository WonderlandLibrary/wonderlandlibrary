// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.math;

public class Random
{
    public static String generateRandomString() {
        final String[] names = { "Terminator", "Slicer", "Ninja", "cow", "Robot", "littlegirl", "SnarkyTug", "Snarkie", "FrostyIsh", "Imagination", "Magicain", "AnCar", "TeasyCar", "JewishBull", "JewishNya", "Reditar", "SmellyFaty", "TearySmell", "BlackIce", "IcySmash", "Alisha", "XXDiamondXX", "NovaXX", "Novola", "Lavish", "Playeris", "ILoveNuma", "NatySticky", "StickyBoy", "CumSticky", "KillaBull", "ILikeNumb", "littlegirlsm", "IkeaStore", "BananaMilk", "SmokeyBalls", "Fernando69", "TinderLover", "MilfWanter", "Proporcione", "modificado", "rafealman", "JuanPlays", "Copper", "Resorte", "Gato", "Inapripoatedo", "Tearsballs", "Razon", "Cliente", "Durante", "Predator", "DinoAngel", "SmellyDood", "LoveJean", "JxanOP", "Lavish", "Stiv", "MinoriKone" };
        final String name = String.valueOf(names[(int)(Math.random() * names.length)]) + MathUtils.getRandom(9999);
        return name;
    }
}
