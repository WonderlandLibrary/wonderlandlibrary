/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block.material;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.material.MaterialLogic;
import net.minecraft.block.material.MaterialPortal;
import net.minecraft.block.material.MaterialTransparent;

public class Material {
    public static final Material AIR = new MaterialTransparent(MapColor.AIR);
    public static final Material GRASS = new Material(MapColor.GRASS);
    public static final Material GROUND = new Material(MapColor.DIRT);
    public static final Material WOOD = new Material(MapColor.WOOD).setBurning();
    public static final Material ROCK = new Material(MapColor.STONE).setRequiresTool();
    public static final Material IRON = new Material(MapColor.IRON).setRequiresTool();
    public static final Material ANVIL = new Material(MapColor.IRON).setRequiresTool().setImmovableMobility();
    public static final Material WATER = new MaterialLiquid(MapColor.WATER).setNoPushMobility();
    public static final Material LAVA = new MaterialLiquid(MapColor.TNT).setNoPushMobility();
    public static final Material LEAVES = new Material(MapColor.FOLIAGE).setBurning().setTranslucent().setNoPushMobility();
    public static final Material PLANTS = new MaterialLogic(MapColor.FOLIAGE).setNoPushMobility();
    public static final Material VINE = new MaterialLogic(MapColor.FOLIAGE).setBurning().setNoPushMobility().setReplaceable();
    public static final Material SPONGE = new Material(MapColor.YELLOW);
    public static final Material CLOTH = new Material(MapColor.CLOTH).setBurning();
    public static final Material FIRE = new MaterialTransparent(MapColor.AIR).setNoPushMobility();
    public static final Material SAND = new Material(MapColor.SAND);
    public static final Material CIRCUITS = new MaterialLogic(MapColor.AIR).setNoPushMobility();
    public static final Material CARPET = new MaterialLogic(MapColor.CLOTH).setBurning();
    public static final Material GLASS = new Material(MapColor.AIR).setTranslucent().setAdventureModeExempt();
    public static final Material REDSTONE_LIGHT = new Material(MapColor.AIR).setAdventureModeExempt();
    public static final Material TNT = new Material(MapColor.TNT).setBurning().setTranslucent();
    public static final Material CORAL = new Material(MapColor.FOLIAGE).setNoPushMobility();
    public static final Material ICE = new Material(MapColor.ICE).setTranslucent().setAdventureModeExempt();
    public static final Material PACKED_ICE = new Material(MapColor.ICE).setAdventureModeExempt();
    public static final Material SNOW = new MaterialLogic(MapColor.SNOW).setReplaceable().setTranslucent().setRequiresTool().setNoPushMobility();
    public static final Material CRAFTED_SNOW = new Material(MapColor.SNOW).setRequiresTool();
    public static final Material CACTUS = new Material(MapColor.FOLIAGE).setTranslucent().setNoPushMobility();
    public static final Material CLAY = new Material(MapColor.CLAY);
    public static final Material GOURD = new Material(MapColor.FOLIAGE).setNoPushMobility();
    public static final Material DRAGON_EGG = new Material(MapColor.FOLIAGE).setNoPushMobility();
    public static final Material PORTAL = new MaterialPortal(MapColor.AIR).setImmovableMobility();
    public static final Material CAKE = new Material(MapColor.AIR).setNoPushMobility();
    public static final Material WEB = new Material(MapColor.CLOTH){

        @Override
        public boolean blocksMovement() {
            return false;
        }
    }.setRequiresTool().setNoPushMobility();
    public static final Material PISTON = new Material(MapColor.STONE).setImmovableMobility();
    public static final Material BARRIER = new Material(MapColor.AIR).setRequiresTool().setImmovableMobility();
    public static final Material STRUCTURE_VOID = new MaterialTransparent(MapColor.AIR);
    private boolean canBurn;
    private boolean replaceable;
    private boolean isTranslucent;
    private final MapColor materialMapColor;
    private boolean requiresNoTool = true;
    private EnumPushReaction mobilityFlag = EnumPushReaction.NORMAL;
    private boolean isAdventureModeExempt;

    public Material(MapColor color) {
        this.materialMapColor = color;
    }

    public boolean isLiquid() {
        return false;
    }

    public boolean isSolid() {
        return true;
    }

    public boolean blocksLight() {
        return true;
    }

    public boolean blocksMovement() {
        return true;
    }

    private Material setTranslucent() {
        this.isTranslucent = true;
        return this;
    }

    protected Material setRequiresTool() {
        this.requiresNoTool = false;
        return this;
    }

    protected Material setBurning() {
        this.canBurn = true;
        return this;
    }

    public boolean getCanBurn() {
        return this.canBurn;
    }

    public Material setReplaceable() {
        this.replaceable = true;
        return this;
    }

    public boolean isReplaceable() {
        return this.replaceable;
    }

    public boolean isOpaque() {
        return this.isTranslucent ? false : this.blocksMovement();
    }

    public boolean isToolNotRequired() {
        return this.requiresNoTool;
    }

    public EnumPushReaction getMobilityFlag() {
        return this.mobilityFlag;
    }

    protected Material setNoPushMobility() {
        this.mobilityFlag = EnumPushReaction.DESTROY;
        return this;
    }

    protected Material setImmovableMobility() {
        this.mobilityFlag = EnumPushReaction.BLOCK;
        return this;
    }

    protected Material setAdventureModeExempt() {
        this.isAdventureModeExempt = true;
        return this;
    }

    public MapColor getMaterialMapColor() {
        return this.materialMapColor;
    }
}

