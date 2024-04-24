package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.*;

public class EntityInstance extends Block {

    public EntityInstance() {
        super("EntityInstance", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns true if input Entity is Instanceof Entity " + "\n" + "Entity instanceof EntityLiving";

        ddOptions.add("EntityLiving");
        ddOptions.add("EntityLivingBase");
        ddOptions.add("EntityTameable");
        ddOptions.add("EntityOtherPlayerMP");
        ddOptions.add("IAnimals");
        ddOptions.add("IMob");
        ddOptions.add("AbstractHorse");
        ddOptions.add("AbstractChestHorse");
        ddOptions.add("EntityAmbientCreature");
        ddOptions.add("EntityAnimal");
        ddOptions.add("EntityArmorStand");
        ddOptions.add("EntityArrow");
        ddOptions.add("EntityBat");
        ddOptions.add("EntityBlaze");
        ddOptions.add("EntityBoat");
        ddOptions.add("EntityCaveSpider");
        ddOptions.add("EntityChicken");
        ddOptions.add("EntityCow");
        ddOptions.add("EntityCreeper");
        ddOptions.add("EntityDonkey");
        ddOptions.add("EntityDragon");
        ddOptions.add("EntityDragonFireball");
        ddOptions.add("EntityEgg");
        ddOptions.add("EntityElderGuardian");
        ddOptions.add("EntityEnderCrystal");
        ddOptions.add("EntityEnderEye");
        ddOptions.add("EntityEnderPearl");
        ddOptions.add("EntityEnderman");
        ddOptions.add("EntityEndermite");
        ddOptions.add("EntityEvoker");
        ddOptions.add("EntityEvokerFangs");
        ddOptions.add("EntityExpBottle");
        ddOptions.add("EntityFallingBlock");
        ddOptions.add("EntityFireball");
        ddOptions.add("EntityFireworkRocket");
        ddOptions.add("EntityFishHook");
        ddOptions.add("EntityFlying");
        ddOptions.add("EntityGhast");
        ddOptions.add("EntityGiantZombie");
        ddOptions.add("EntityGolem");
        ddOptions.add("EntityGuardian");
        ddOptions.add("EntityHorse");
        ddOptions.add("EntityHusk");
        ddOptions.add("EntityIllusionIllager");
        ddOptions.add("EntityIronGolem");
        ddOptions.add("EntityItem");
        ddOptions.add("EntityItemFrame");
        ddOptions.add("EntityLargeFireball");
        ddOptions.add("EntityLlama");
        ddOptions.add("EntityLlamaSpit");
        ddOptions.add("EntityMagmaCube");
        ddOptions.add("EntityMinecart");
        ddOptions.add("EntityMinecartChest");
        ddOptions.add("EntityMinecartCommandBlock");
        ddOptions.add("EntityMinecartContainer");
        ddOptions.add("EntityMinecartEmpty");
        ddOptions.add("EntityMinecartFurnace");
        ddOptions.add("EntityMinecartHopper");
        ddOptions.add("EntityMinecartMobSpawner");
        ddOptions.add("EntityMinecartTNT");
        ddOptions.add("EntityMob");
        ddOptions.add("EntityMooshroom");
        ddOptions.add("EntityMule");
        ddOptions.add("EntityOcelot");
        ddOptions.add("EntityPainting");
        ddOptions.add("EntityParrot");
        ddOptions.add("EntityPig");
        ddOptions.add("EntityPigZombie");
        ddOptions.add("EntityPlayer");
        ddOptions.add("EntityPlayerMP");
        ddOptions.add("EntityPolarBear");
        ddOptions.add("EntityPotion");
        ddOptions.add("EntityRabbit");
        ddOptions.add("EntitySheep");
        ddOptions.add("EntityShoulderRiding");
        ddOptions.add("EntityShulker");
        ddOptions.add("EntityShulkerBullet");
        ddOptions.add("EntitySilverfish");
        ddOptions.add("EntitySkeleton");
        ddOptions.add("EntitySkeletonHorse");
        ddOptions.add("EntitySlime");
        ddOptions.add("EntitySmallFireball");
        ddOptions.add("EntitySnowball");
        ddOptions.add("EntitySnowman");
        ddOptions.add("EntitySpectralArrow");
        ddOptions.add("EntitySpellcasterIllager");
        ddOptions.add("EntitySpider");
        ddOptions.add("EntitySquid");
        ddOptions.add("EntityStray");
        ddOptions.add("EntityTNTPrimed");
        ddOptions.add("EntityThrowable");
        ddOptions.add("EntityTippedArrow");
        ddOptions.add("EntityVex");
        ddOptions.add("EntityVillager");
        ddOptions.add("EntityVindicator");
        ddOptions.add("EntityWaterMob");
        ddOptions.add("EntityWitch");
        ddOptions.add("EntityWither");
        ddOptions.add("EntityWitherSkeleton");
        ddOptions.add("EntityWitherSkull");
        ddOptions.add("EntityWolf");
        ddOptions.add("EntityXPOrb");
        ddOptions.add("EntityZombie");
        ddOptions.add("EntityZombieHorse");
        ddOptions.add("EntityZombieVillager");

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "EntityAmbientCreature":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityAmbientCreature;
            case "EntityAnimal":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityAnimal;
            case "EntityArmorStand":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityArmorStand;
            case "EntityArrow":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityArrow;
            case "EntityBat":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityBat;
            case "EntityBlaze":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityBlaze;
            case "EntityBoat":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityBoat;
            case "EntityCaveSpider":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityCaveSpider;
            case "EntityChicken":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityChicken;
            case "EntityCow":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityCow;
            case "EntityCreeper":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityCreeper;
            case "EntityDonkey":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityDonkey;
            case "EntityDragon":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityDragon;
            case "EntityDragonFireball":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityDragonFireball;
            case "EntityEgg":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityEgg;
            case "EntityElderGuardian":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityElderGuardian;
            case "EntityEnderCrystal":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityEnderCrystal;
            case "EntityEnderEye":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityEnderEye;
            case "EntityEnderPearl":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityEnderPearl;
            case "EntityEnderman":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityEnderman;
            case "EntityEndermite":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityEndermite;
            case "EntityEvoker":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityEvoker;
            case "EntityEvokerFangs":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityEvokerFangs;
            case "EntityExpBottle":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityExpBottle;
            case "EntityFallingBlock":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityFallingBlock;
            case "EntityFireball":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityFireball;
            case "EntityFireworkRocket":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityFireworkRocket;
            case "EntityFishHook":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityFishHook;
            case "EntityFlying":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityFlying;
            case "EntityGhast":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityGhast;
            case "EntityGiantZombie":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityGiantZombie;
            case "EntityGolem":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityGolem;
            case "EntityGuardian":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityGuardian;
            case "EntityHorse":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityHorse;
            case "EntityHusk":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityHusk;
            case "EntityIllusionIllager":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityIllusionIllager;
            case "EntityIronGolem":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityIronGolem;
            case "EntityItem":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityItem;
            case "EntityItemFrame":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityItemFrame;
            case "EntityLargeFireball":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityLargeFireball;
            case "EntityLlama":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityLlama;
            case "EntityLlamaSpit":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityLlamaSpit;
            case "EntityMagmaCube":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMagmaCube;
            case "EntityMinecart":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMinecart;
            case "EntityMinecartChest":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMinecartChest;
            case "EntityMinecartCommandBlock":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMinecartCommandBlock;
            case "EntityMinecartContainer":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMinecartContainer;
            case "EntityMinecartEmpty":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMinecartEmpty;
            case "EntityMinecartFurnace":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMinecartFurnace;
            case "EntityMinecartHopper":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMinecartHopper;
            case "EntityMinecartMobSpawner":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMinecartMobSpawner;
            case "EntityMinecartTNT":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMinecartTNT;
            case "EntityMob":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMob;
            case "EntityMooshroom":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMooshroom;
            case "EntityMule":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityMule;
            case "EntityOcelot":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityOcelot;
            case "EntityPainting":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityPainting;
            case "EntityParrot":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityParrot;
            case "EntityPig":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityPig;
            case "EntityPigZombie":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityPigZombie;
            case "EntityPlayer":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityPlayer;
            case "EntityPlayerMP":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityPlayerMP;
            case "EntityPolarBear":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityPolarBear;
            case "EntityPotion":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityPotion;
            case "EntityRabbit":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityRabbit;
            case "EntitySheep":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntitySheep;
            case "EntityShoulderRiding":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityShoulderRiding;
            case "EntityShulker":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityShulker;
            case "EntityShulkerBullet":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityShulkerBullet;
            case "EntitySilverfish":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntitySilverfish;
            case "EntitySkeleton":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntitySkeleton;
            case "EntitySkeletonHorse":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntitySkeletonHorse;
            case "EntitySlime":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntitySlime;
            case "EntitySmallFireball":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntitySmallFireball;
            case "EntitySnowball":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntitySnowball;
            case "EntitySnowman":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntitySnowman;
            case "EntitySpectralArrow":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntitySpectralArrow;
            case "EntitySpellcasterIllager":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntitySpellcasterIllager;
            case "EntitySpider":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntitySpider;
            case "EntitySquid":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntitySquid;
            case "EntityStray":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityStray;
            case "EntityTNTPrimed":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityTNTPrimed;

            case "EntityThrowable":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityThrowable;
            case "EntityTippedArrow":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityTippedArrow;
            case "EntityVex":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityVex;
            case "EntityVillager":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityVillager;
            case "EntityVindicator":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityVindicator;
            case "EntityWaterMob":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityWaterMob;
            case "EntityWitch":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityWitch;
            case "EntityWither":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityWither;
            case "EntityWitherSkeleton":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityWitherSkeleton;
            case "EntityWitherSkull":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityWitherSkull;
            case "EntityWolf":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityWolf;
            case "EntityXPOrb":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityXPOrb;
            case "EntityZombie":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityZombie;
            case "EntityZombieHorse":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityZombieHorse;
            case "EntityZombieVillager":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityZombieVillager;
            case "EntityLiving":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityLiving;
            case "EntityLivingBase":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityLivingBase;
            case "EntityTameable":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityTameable;
            case "EntityOtherPlayerMP":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityOtherPlayerMP;
            case "IAnimals":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof IAnimals;
            case "IMob":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof IMob;
            case "AbstractHorse":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof AbstractHorse;
            case "AbstractChestHorse":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof AbstractChestHorse;
        }
        return false;

    }

}
