package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.item.*;

public class EItemInstance extends Block {

    public EItemInstance() {
        super("EItemInstance", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Items));
        this.description = "Returns true if input Entity is Instanceof Entity " + "\n" + "Entity instanceof EntityLiving";

        ddOptions.add("ItemAir");
        ddOptions.add("ItemAnvilBlock");
        ddOptions.add("ItemAppleGold");
        ddOptions.add("ItemArmor");
        ddOptions.add("ItemArmorStand");
        ddOptions.add("ItemArrow");
        ddOptions.add("ItemAxe");
        ddOptions.add("ItemBanner");
        ddOptions.add("ItemBed");
        ddOptions.add("ItemBlock");
        ddOptions.add("ItemBlockSpecial");
        ddOptions.add("ItemBoat");
        ddOptions.add("ItemBook");
        ddOptions.add("ItemBow");
        ddOptions.add("ItemBucket");
        ddOptions.add("ItemBucketMilk");
        ddOptions.add("ItemCarrotOnAStick");
        ddOptions.add("ItemChorusFruit");
        ddOptions.add("ItemClock");
        ddOptions.add("ItemCloth");
        ddOptions.add("ItemCoal");
        ddOptions.add("ItemColored");
        ddOptions.add("ItemCompass");
        ddOptions.add("ItemDoor");
        ddOptions.add("ItemDye");
        ddOptions.add("ItemEgg");
        ddOptions.add("ItemElytra");
        ddOptions.add("ItemEmptyMap");
        ddOptions.add("ItemEnchantedBook");
        ddOptions.add("ItemEndCrystal");
        ddOptions.add("ItemEnderEye");
        ddOptions.add("ItemEnderPearl");
        ddOptions.add("ItemExpBottle");
        ddOptions.add("ItemFireball");
        ddOptions.add("ItemFirework");
        ddOptions.add("ItemFireworkCharge");
        ddOptions.add("ItemFishFood");
        ddOptions.add("ItemFishingRod");
        ddOptions.add("ItemFlintAndSteel");
        ddOptions.add("ItemFood");
        ddOptions.add("ItemGlassBottle");
        ddOptions.add("ItemHangingEntity");
        ddOptions.add("ItemHoe");
        ddOptions.add("ItemKnowledgeBook");
        ddOptions.add("ItemLead");
        ddOptions.add("ItemLeaves");
        ddOptions.add("ItemLilyPad");
        ddOptions.add("ItemLingeringPotion");
        ddOptions.add("ItemMap");
        ddOptions.add("ItemMapBase");
        ddOptions.add("ItemMinecart");
        ddOptions.add("ItemMonsterPlacer");
        ddOptions.add("ItemMultiTexture");
        ddOptions.add("ItemNameTag");
        ddOptions.add("ItemPickaxe");
        ddOptions.add("ItemPiston");
        ddOptions.add("ItemPotion");
        ddOptions.add("ItemRecord");
        ddOptions.add("ItemRedstone");
        ddOptions.add("ItemSaddle");
        ddOptions.add("ItemSeedFood");
        ddOptions.add("ItemSeeds");
        ddOptions.add("ItemShears");
        ddOptions.add("ItemShield");
        ddOptions.add("ItemShulkerBox");
        ddOptions.add("ItemSign");
        ddOptions.add("ItemSimpleFoiled");
        ddOptions.add("ItemSkull");
        ddOptions.add("ItemSlab");
        ddOptions.add("ItemSnow");
        ddOptions.add("ItemSnowball");
        ddOptions.add("ItemSoup");
        ddOptions.add("ItemSpade");
        ddOptions.add("ItemSpectralArrow");
        ddOptions.add("ItemSplashPotion");
        ddOptions.add("ItemStack");
        ddOptions.add("ItemSword");
        ddOptions.add("ItemTippedArrow");
        ddOptions.add("ItemTool");
        ddOptions.add("ItemWritableBook");
        ddOptions.add("ItemWrittenBook");
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "ItemAir":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemAir;
            case "ItemAnvilBlock":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemAnvilBlock;
            case "ItemAppleGold":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemAppleGold;
            case "ItemArmor":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemArmor;
            case "ItemArmorStand":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemArmorStand;
            case "ItemArrow":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemArrow;
            case "ItemAxe":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemAxe;
            case "ItemBanner":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemBanner;
            case "ItemBed":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemBed;
            case "ItemBlock":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemBlock;
            case "ItemBlockSpecial":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemBlockSpecial;

            case "ItemBoat":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemBoat;
            case "ItemBook":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemBook;
            case "ItemBow":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemBow;
            case "ItemBucket":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemBucket;
            case "ItemBucketMilk":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemBucketMilk;
            case "ItemCarrotOnAStick":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemCarrotOnAStick;
            case "ItemChorusFruit":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemChorusFruit;
            case "ItemClock":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemClock;
            case "ItemCloth":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemCloth;
            case "ItemCoal":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemCoal;
            case "ItemColored":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemColored;
            case "ItemCompass":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemCompass;
            case "ItemDoor":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemDoor;
            case "ItemDye":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemDye;
            case "ItemEgg":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemEgg;
            case "ItemElytra":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemElytra;
            case "ItemEmptyMap":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemEmptyMap;
            case "ItemEnchantedBook":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemEnchantedBook;
            case "ItemEndCrystal":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemEndCrystal;
            case "ItemEnderEye":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemEnderEye;
            case "ItemEnderPearl":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemEnderPearl;
            case "ItemExpBottle":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemExpBottle;
            case "ItemFireball":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemFireball;
            case "ItemFirework":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemFirework;
            case "ItemFireworkCharge":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemFireworkCharge;
            case "ItemFishFood":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemFishFood;
            case "ItemFishingRod":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemFishingRod;
            case "ItemFlintAndSteel":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemFlintAndSteel;
            case "ItemFood":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemFood;
            case "ItemGlassBottle":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemGlassBottle;
            case "ItemHangingEntity":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemHangingEntity;
            case "ItemHoe":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemHoe;
            case "ItemKnowledgeBook":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemKnowledgeBook;
            case "ItemLead":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemLead;
            case "ItemLeaves":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemLeaves;
            case "ItemLilyPad":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemLilyPad;
            case "ItemLingeringPotion":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemLingeringPotion;
            case "ItemMap":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemMap;
            case "ItemMapBase":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemMapBase;
            case "ItemMinecart":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemMinecart;
            case "ItemMonsterPlacer":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemMonsterPlacer;
            case "ItemMultiTexture":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemMultiTexture;
            case "ItemNameTag":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemNameTag;
            case "ItemPickaxe":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemPickaxe;
            case "ItemPiston":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemPiston;
            case "ItemPotion":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemPotion;
            case "ItemRecord":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemRecord;
            case "ItemRedstone":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemRedstone;
            case "ItemSaddle":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSaddle;
            case "ItemSeedFood":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSeedFood;
            case "ItemSeeds":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSeeds;
            case "ItemShears":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemShears;
            case "ItemShield":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemShield;
            case "ItemShulkerBox":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemShulkerBox;
            case "ItemSign":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSign;
            case "ItemSimpleFoiled":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSimpleFoiled;
            case "ItemSkull":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSkull;
            case "ItemSlab":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSlab;
            case "ItemSnow":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSnow;
            case "ItemSnowball":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSnowball;
            case "ItemSoup":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSoup;
            case "ItemSpade":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSpade;
            case "ItemSpectralArrow":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSpectralArrow;
            case "ItemSplashPotion":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSplashPotion;
            case "ItemStack":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemStack;
            case "ItemSword":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemSword;
            case "ItemTippedArrow":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemTippedArrow;
            case "ItemTool":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemTool;
            case "ItemWritableBook":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemWritableBook;
            case "ItemWrittenBook":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemWrittenBook;
            default:
                return false;
        }
    }


}
