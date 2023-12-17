// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.material;

public class Material
{
    public static final Material AIR;
    public static final Material GRASS;
    public static final Material GROUND;
    public static final Material WOOD;
    public static final Material ROCK;
    public static final Material IRON;
    public static final Material ANVIL;
    public static final Material WATER;
    public static final Material LAVA;
    public static final Material LEAVES;
    public static final Material PLANTS;
    public static final Material VINE;
    public static final Material SPONGE;
    public static final Material CLOTH;
    public static final Material FIRE;
    public static final Material SAND;
    public static final Material CIRCUITS;
    public static final Material CARPET;
    public static final Material GLASS;
    public static final Material REDSTONE_LIGHT;
    public static final Material TNT;
    public static final Material CORAL;
    public static final Material ICE;
    public static final Material PACKED_ICE;
    public static final Material SNOW;
    public static final Material CRAFTED_SNOW;
    public static final Material CACTUS;
    public static final Material CLAY;
    public static final Material GOURD;
    public static final Material DRAGON_EGG;
    public static final Material PORTAL;
    public static final Material CAKE;
    public static final Material WEB;
    public static final Material PISTON;
    public static final Material BARRIER;
    public static final Material STRUCTURE_VOID;
    private boolean canBurn;
    private boolean replaceable;
    private boolean isTranslucent;
    private final MapColor materialMapColor;
    private boolean requiresNoTool;
    private EnumPushReaction pushReaction;
    private boolean isAdventureModeExempt;
    
    public Material(final MapColor color) {
        this.requiresNoTool = true;
        this.pushReaction = EnumPushReaction.NORMAL;
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
        return !this.isTranslucent && this.blocksMovement();
    }
    
    public boolean isToolNotRequired() {
        return this.requiresNoTool;
    }
    
    public EnumPushReaction getPushReaction() {
        return this.pushReaction;
    }
    
    protected Material setNoPushMobility() {
        this.pushReaction = EnumPushReaction.DESTROY;
        return this;
    }
    
    protected Material setImmovableMobility() {
        this.pushReaction = EnumPushReaction.BLOCK;
        return this;
    }
    
    protected Material setAdventureModeExempt() {
        this.isAdventureModeExempt = true;
        return this;
    }
    
    public MapColor getMaterialMapColor() {
        return this.materialMapColor;
    }
    
    static {
        AIR = new MaterialTransparent(MapColor.AIR);
        GRASS = new Material(MapColor.GRASS);
        GROUND = new Material(MapColor.DIRT);
        WOOD = new Material(MapColor.WOOD).setBurning();
        ROCK = new Material(MapColor.STONE).setRequiresTool();
        IRON = new Material(MapColor.IRON).setRequiresTool();
        ANVIL = new Material(MapColor.IRON).setRequiresTool().setImmovableMobility();
        WATER = new MaterialLiquid(MapColor.WATER).setNoPushMobility();
        LAVA = new MaterialLiquid(MapColor.TNT).setNoPushMobility();
        LEAVES = new Material(MapColor.FOLIAGE).setBurning().setTranslucent().setNoPushMobility();
        PLANTS = new MaterialLogic(MapColor.FOLIAGE).setNoPushMobility();
        VINE = new MaterialLogic(MapColor.FOLIAGE).setBurning().setNoPushMobility().setReplaceable();
        SPONGE = new Material(MapColor.YELLOW);
        CLOTH = new Material(MapColor.CLOTH).setBurning();
        FIRE = new MaterialTransparent(MapColor.AIR).setNoPushMobility();
        SAND = new Material(MapColor.SAND);
        CIRCUITS = new MaterialLogic(MapColor.AIR).setNoPushMobility();
        CARPET = new MaterialLogic(MapColor.CLOTH).setBurning();
        GLASS = new Material(MapColor.AIR).setTranslucent().setAdventureModeExempt();
        REDSTONE_LIGHT = new Material(MapColor.AIR).setAdventureModeExempt();
        TNT = new Material(MapColor.TNT).setBurning().setTranslucent();
        CORAL = new Material(MapColor.FOLIAGE).setNoPushMobility();
        ICE = new Material(MapColor.ICE).setTranslucent().setAdventureModeExempt();
        PACKED_ICE = new Material(MapColor.ICE).setAdventureModeExempt();
        SNOW = new MaterialLogic(MapColor.SNOW).setReplaceable().setTranslucent().setRequiresTool().setNoPushMobility();
        CRAFTED_SNOW = new Material(MapColor.SNOW).setRequiresTool();
        CACTUS = new Material(MapColor.FOLIAGE).setTranslucent().setNoPushMobility();
        CLAY = new Material(MapColor.CLAY);
        GOURD = new Material(MapColor.FOLIAGE).setNoPushMobility();
        DRAGON_EGG = new Material(MapColor.FOLIAGE).setNoPushMobility();
        PORTAL = new MaterialPortal(MapColor.AIR).setImmovableMobility();
        CAKE = new Material(MapColor.AIR).setNoPushMobility();
        WEB = new Material(MapColor.CLOTH) {
            @Override
            public boolean blocksMovement() {
                return false;
            }
        }.setRequiresTool().setNoPushMobility();
        PISTON = new Material(MapColor.STONE).setImmovableMobility();
        BARRIER = new Material(MapColor.AIR).setRequiresTool().setImmovableMobility();
        STRUCTURE_VOID = new MaterialTransparent(MapColor.AIR);
    }
}
