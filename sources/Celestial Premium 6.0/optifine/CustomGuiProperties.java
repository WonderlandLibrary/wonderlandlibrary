/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntArraySet
 */
package optifine;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.biome.Biome;
import optifine.Config;
import optifine.ConnectedParser;
import optifine.CustomGuis;
import optifine.Matches;
import optifine.NbtTagValue;
import optifine.RangeListInt;
import optifine.StrUtils;
import optifine.TextureUtils;
import optifine.VillagerProfession;

public class CustomGuiProperties {
    private String fileName = null;
    private String basePath = null;
    private EnumContainer container = null;
    private Map<ResourceLocation, ResourceLocation> textureLocations = null;
    private NbtTagValue nbtName = null;
    private Biome[] biomes = null;
    private RangeListInt heights = null;
    private Boolean large = null;
    private Boolean trapped = null;
    private Boolean christmas = null;
    private Boolean ender = null;
    private RangeListInt levels = null;
    private VillagerProfession[] professions = null;
    private EnumVariant[] variants = null;
    private EnumDyeColor[] colors = null;
    private static final VillagerProfession[] PROFESSIONS_INVALID = new VillagerProfession[0];
    private static final EnumVariant[] VARIANTS_HORSE = new EnumVariant[]{EnumVariant.HORSE, EnumVariant.DONKEY, EnumVariant.MULE, EnumVariant.LLAMA};
    private static final EnumVariant[] VARIANTS_DISPENSER = new EnumVariant[]{EnumVariant.DISPENSER, EnumVariant.DROPPER};
    private static final EnumVariant[] VARIANTS_INVALID = new EnumVariant[0];
    private static final EnumDyeColor[] COLORS_INVALID = new EnumDyeColor[0];
    private static final ResourceLocation ANVIL_GUI_TEXTURE = new ResourceLocation("textures/gui/container/anvil.png");
    private static final ResourceLocation BEACON_GUI_TEXTURE = new ResourceLocation("textures/gui/container/beacon.png");
    private static final ResourceLocation BREWING_STAND_GUI_TEXTURE = new ResourceLocation("textures/gui/container/brewing_stand.png");
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/crafting_table.png");
    private static final ResourceLocation HORSE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/horse.png");
    private static final ResourceLocation DISPENSER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/dispenser.png");
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
    private static final ResourceLocation FURNACE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");
    private static final ResourceLocation HOPPER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/hopper.png");
    private static final ResourceLocation INVENTORY_GUI_TEXTURE = new ResourceLocation("textures/gui/container/inventory.png");
    private static final ResourceLocation SHULKER_BOX_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static final ResourceLocation VILLAGER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");

    public CustomGuiProperties(Properties p_i32_1_, String p_i32_2_) {
        ConnectedParser connectedparser = new ConnectedParser("CustomGuis");
        this.fileName = connectedparser.parseName(p_i32_2_);
        this.basePath = connectedparser.parseBasePath(p_i32_2_);
        this.container = (EnumContainer)connectedparser.parseEnum(p_i32_1_.getProperty("container"), EnumContainer.values(), "container");
        this.textureLocations = CustomGuiProperties.parseTextureLocations(p_i32_1_, "texture", this.container, "textures/gui/", this.basePath);
        this.nbtName = CustomGuiProperties.parseNbtTagValue("name", p_i32_1_.getProperty("name"));
        this.biomes = connectedparser.parseBiomes(p_i32_1_.getProperty("biomes"));
        this.heights = connectedparser.parseRangeListInt(p_i32_1_.getProperty("heights"));
        this.large = connectedparser.parseBooleanObject(p_i32_1_.getProperty("large"));
        this.trapped = connectedparser.parseBooleanObject(p_i32_1_.getProperty("trapped"));
        this.christmas = connectedparser.parseBooleanObject(p_i32_1_.getProperty("christmas"));
        this.ender = connectedparser.parseBooleanObject(p_i32_1_.getProperty("ender"));
        this.levels = connectedparser.parseRangeListInt(p_i32_1_.getProperty("levels"));
        this.professions = CustomGuiProperties.parseProfessions(p_i32_1_.getProperty("professions"));
        Enum[] acustomguiproperties$enumvariant = CustomGuiProperties.getContainerVariants(this.container);
        this.variants = (EnumVariant[])connectedparser.parseEnums(p_i32_1_.getProperty("variants"), acustomguiproperties$enumvariant, "variants", VARIANTS_INVALID);
        this.colors = CustomGuiProperties.parseEnumDyeColors(p_i32_1_.getProperty("colors"));
    }

    private static EnumVariant[] getContainerVariants(EnumContainer p_getContainerVariants_0_) {
        if (p_getContainerVariants_0_ == EnumContainer.HORSE) {
            return VARIANTS_HORSE;
        }
        return p_getContainerVariants_0_ == EnumContainer.DISPENSER ? VARIANTS_DISPENSER : new EnumVariant[0];
    }

    private static EnumDyeColor[] parseEnumDyeColors(String p_parseEnumDyeColors_0_) {
        if (p_parseEnumDyeColors_0_ == null) {
            return null;
        }
        p_parseEnumDyeColors_0_ = p_parseEnumDyeColors_0_.toLowerCase();
        String[] astring = Config.tokenize(p_parseEnumDyeColors_0_, " ");
        EnumDyeColor[] aenumdyecolor = new EnumDyeColor[astring.length];
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            EnumDyeColor enumdyecolor = CustomGuiProperties.parseEnumDyeColor(s);
            if (enumdyecolor == null) {
                CustomGuiProperties.warn("Invalid color: " + s);
                return COLORS_INVALID;
            }
            aenumdyecolor[i] = enumdyecolor;
        }
        return aenumdyecolor;
    }

    private static EnumDyeColor parseEnumDyeColor(String p_parseEnumDyeColor_0_) {
        if (p_parseEnumDyeColor_0_ == null) {
            return null;
        }
        EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        for (int i = 0; i < aenumdyecolor.length; ++i) {
            EnumDyeColor enumdyecolor = aenumdyecolor[i];
            if (enumdyecolor.getName().equals(p_parseEnumDyeColor_0_)) {
                return enumdyecolor;
            }
            if (!enumdyecolor.getUnlocalizedName().equals(p_parseEnumDyeColor_0_)) continue;
            return enumdyecolor;
        }
        return null;
    }

    private static NbtTagValue parseNbtTagValue(String p_parseNbtTagValue_0_, String p_parseNbtTagValue_1_) {
        return p_parseNbtTagValue_0_ != null && p_parseNbtTagValue_1_ != null ? new NbtTagValue(p_parseNbtTagValue_0_, p_parseNbtTagValue_1_) : null;
    }

    private static VillagerProfession[] parseProfessions(String p_parseProfessions_0_) {
        if (p_parseProfessions_0_ == null) {
            return null;
        }
        ArrayList<VillagerProfession> list = new ArrayList<VillagerProfession>();
        String[] astring = Config.tokenize(p_parseProfessions_0_, " ");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            VillagerProfession villagerprofession = CustomGuiProperties.parseProfession(s);
            if (villagerprofession == null) {
                CustomGuiProperties.warn("Invalid profession: " + s);
                return PROFESSIONS_INVALID;
            }
            list.add(villagerprofession);
        }
        if (list.isEmpty()) {
            return null;
        }
        VillagerProfession[] avillagerprofession = list.toArray(new VillagerProfession[list.size()]);
        return avillagerprofession;
    }

    private static VillagerProfession parseProfession(String p_parseProfession_0_) {
        int i;
        String[] astring = Config.tokenize(p_parseProfession_0_ = p_parseProfession_0_.toLowerCase(), ":");
        if (astring.length > 2) {
            return null;
        }
        String s = astring[0];
        String s1 = null;
        if (astring.length > 1) {
            s1 = astring[1];
        }
        if ((i = CustomGuiProperties.parseProfessionId(s)) < 0) {
            return null;
        }
        int[] aint = null;
        if (s1 != null && (aint = CustomGuiProperties.parseCareerIds(i, s1)) == null) {
            return null;
        }
        return new VillagerProfession(i, aint);
    }

    private static int parseProfessionId(String p_parseProfessionId_0_) {
        int i = Config.parseInt(p_parseProfessionId_0_, -1);
        if (i >= 0) {
            return i;
        }
        if (p_parseProfessionId_0_.equals("farmer")) {
            return 0;
        }
        if (p_parseProfessionId_0_.equals("librarian")) {
            return 1;
        }
        if (p_parseProfessionId_0_.equals("priest")) {
            return 2;
        }
        if (p_parseProfessionId_0_.equals("blacksmith")) {
            return 3;
        }
        if (p_parseProfessionId_0_.equals("butcher")) {
            return 4;
        }
        return p_parseProfessionId_0_.equals("nitwit") ? 5 : -1;
    }

    private static int[] parseCareerIds(int p_parseCareerIds_0_, String p_parseCareerIds_1_) {
        IntArraySet intset = new IntArraySet();
        String[] astring = Config.tokenize(p_parseCareerIds_1_, ",");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            int j = CustomGuiProperties.parseCareerId(p_parseCareerIds_0_, s);
            if (j < 0) {
                return null;
            }
            intset.add(j);
        }
        int[] aint = intset.toIntArray();
        return aint;
    }

    private static int parseCareerId(int p_parseCareerId_0_, String p_parseCareerId_1_) {
        int i = Config.parseInt(p_parseCareerId_1_, -1);
        if (i >= 0) {
            return i;
        }
        if (p_parseCareerId_0_ == 0) {
            if (p_parseCareerId_1_.equals("farmer")) {
                return 1;
            }
            if (p_parseCareerId_1_.equals("fisherman")) {
                return 2;
            }
            if (p_parseCareerId_1_.equals("shepherd")) {
                return 3;
            }
            if (p_parseCareerId_1_.equals("fletcher")) {
                return 4;
            }
        }
        if (p_parseCareerId_0_ == 1) {
            if (p_parseCareerId_1_.equals("librarian")) {
                return 1;
            }
            if (p_parseCareerId_1_.equals("cartographer")) {
                return 2;
            }
        }
        if (p_parseCareerId_0_ == 2 && p_parseCareerId_1_.equals("cleric")) {
            return 1;
        }
        if (p_parseCareerId_0_ == 3) {
            if (p_parseCareerId_1_.equals("armor")) {
                return 1;
            }
            if (p_parseCareerId_1_.equals("weapon")) {
                return 2;
            }
            if (p_parseCareerId_1_.equals("tool")) {
                return 3;
            }
        }
        if (p_parseCareerId_0_ == 4) {
            if (p_parseCareerId_1_.equals("butcher")) {
                return 1;
            }
            if (p_parseCareerId_1_.equals("leather")) {
                return 2;
            }
        }
        return p_parseCareerId_0_ == 5 && p_parseCareerId_1_.equals("nitwit") ? 1 : -1;
    }

    private static ResourceLocation parseTextureLocation(String p_parseTextureLocation_0_, String p_parseTextureLocation_1_) {
        if (p_parseTextureLocation_0_ == null) {
            return null;
        }
        String s = TextureUtils.fixResourcePath(p_parseTextureLocation_0_ = p_parseTextureLocation_0_.trim(), p_parseTextureLocation_1_);
        if (!s.endsWith(".png")) {
            s = s + ".png";
        }
        return new ResourceLocation(p_parseTextureLocation_1_ + "/" + s);
    }

    private static Map<ResourceLocation, ResourceLocation> parseTextureLocations(Properties p_parseTextureLocations_0_, String p_parseTextureLocations_1_, EnumContainer p_parseTextureLocations_2_, String p_parseTextureLocations_3_, String p_parseTextureLocations_4_) {
        HashMap<ResourceLocation, ResourceLocation> map = new HashMap<ResourceLocation, ResourceLocation>();
        String s = p_parseTextureLocations_0_.getProperty(p_parseTextureLocations_1_);
        if (s != null) {
            ResourceLocation resourcelocation = CustomGuiProperties.getGuiTextureLocation(p_parseTextureLocations_2_);
            ResourceLocation resourcelocation1 = CustomGuiProperties.parseTextureLocation(s, p_parseTextureLocations_4_);
            if (resourcelocation != null && resourcelocation1 != null) {
                map.put(resourcelocation, resourcelocation1);
            }
        }
        String s5 = p_parseTextureLocations_1_ + ".";
        for (Object s1 : p_parseTextureLocations_0_.keySet()) {
            if (!((String)s1).startsWith(s5)) continue;
            String s2 = ((String)s1).substring(s5.length());
            s2 = s2.replace('\\', '/');
            s2 = StrUtils.removePrefixSuffix(s2, "/", ".png");
            String s3 = p_parseTextureLocations_3_ + s2 + ".png";
            String s4 = p_parseTextureLocations_0_.getProperty((String)s1);
            ResourceLocation resourcelocation2 = new ResourceLocation(s3);
            ResourceLocation resourcelocation3 = CustomGuiProperties.parseTextureLocation(s4, p_parseTextureLocations_4_);
            map.put(resourcelocation2, resourcelocation3);
        }
        return map;
    }

    private static ResourceLocation getGuiTextureLocation(EnumContainer p_getGuiTextureLocation_0_) {
        switch (p_getGuiTextureLocation_0_) {
            case ANVIL: {
                return ANVIL_GUI_TEXTURE;
            }
            case BEACON: {
                return BEACON_GUI_TEXTURE;
            }
            case BREWING_STAND: {
                return BREWING_STAND_GUI_TEXTURE;
            }
            case CHEST: {
                return CHEST_GUI_TEXTURE;
            }
            case CRAFTING: {
                return CRAFTING_TABLE_GUI_TEXTURE;
            }
            case CREATIVE: {
                return null;
            }
            case DISPENSER: {
                return DISPENSER_GUI_TEXTURE;
            }
            case ENCHANTMENT: {
                return ENCHANTMENT_TABLE_GUI_TEXTURE;
            }
            case FURNACE: {
                return FURNACE_GUI_TEXTURE;
            }
            case HOPPER: {
                return HOPPER_GUI_TEXTURE;
            }
            case HORSE: {
                return HORSE_GUI_TEXTURE;
            }
            case INVENTORY: {
                return INVENTORY_GUI_TEXTURE;
            }
            case SHULKER_BOX: {
                return SHULKER_BOX_GUI_TEXTURE;
            }
            case VILLAGER: {
                return VILLAGER_GUI_TEXTURE;
            }
        }
        return null;
    }

    public boolean isValid(String p_isValid_1_) {
        if (this.fileName != null && this.fileName.length() > 0) {
            if (this.basePath == null) {
                CustomGuiProperties.warn("No base path found: " + p_isValid_1_);
                return false;
            }
            if (this.container == null) {
                CustomGuiProperties.warn("No container found: " + p_isValid_1_);
                return false;
            }
            if (this.textureLocations.isEmpty()) {
                CustomGuiProperties.warn("No texture found: " + p_isValid_1_);
                return false;
            }
            if (this.professions == PROFESSIONS_INVALID) {
                CustomGuiProperties.warn("Invalid professions or careers: " + p_isValid_1_);
                return false;
            }
            if (this.variants == VARIANTS_INVALID) {
                CustomGuiProperties.warn("Invalid variants: " + p_isValid_1_);
                return false;
            }
            if (this.colors == COLORS_INVALID) {
                CustomGuiProperties.warn("Invalid colors: " + p_isValid_1_);
                return false;
            }
            return true;
        }
        CustomGuiProperties.warn("No name found: " + p_isValid_1_);
        return false;
    }

    private static void warn(String p_warn_0_) {
        Config.warn("[CustomGuis] " + p_warn_0_);
    }

    private boolean matchesGeneral(EnumContainer p_matchesGeneral_1_, BlockPos p_matchesGeneral_2_, IBlockAccess p_matchesGeneral_3_) {
        Biome biome;
        if (this.container != p_matchesGeneral_1_) {
            return false;
        }
        if (this.biomes != null && !Matches.biome(biome = p_matchesGeneral_3_.getBiome(p_matchesGeneral_2_), this.biomes)) {
            return false;
        }
        return this.heights == null || this.heights.isInRange(p_matchesGeneral_2_.getY());
    }

    public boolean matchesPos(EnumContainer p_matchesPos_1_, BlockPos p_matchesPos_2_, IBlockAccess p_matchesPos_3_) {
        if (!this.matchesGeneral(p_matchesPos_1_, p_matchesPos_2_, p_matchesPos_3_)) {
            return false;
        }
        switch (p_matchesPos_1_) {
            case BEACON: {
                return this.matchesBeacon(p_matchesPos_2_, p_matchesPos_3_);
            }
            case BREWING_STAND: {
                return this.matchesNameable(p_matchesPos_2_, p_matchesPos_3_);
            }
            case CHEST: {
                return this.matchesChest(p_matchesPos_2_, p_matchesPos_3_);
            }
            default: {
                return true;
            }
            case DISPENSER: {
                return this.matchesDispenser(p_matchesPos_2_, p_matchesPos_3_);
            }
            case ENCHANTMENT: {
                return this.matchesNameable(p_matchesPos_2_, p_matchesPos_3_);
            }
            case FURNACE: {
                return this.matchesNameable(p_matchesPos_2_, p_matchesPos_3_);
            }
            case HOPPER: {
                return this.matchesNameable(p_matchesPos_2_, p_matchesPos_3_);
            }
            case SHULKER_BOX: 
        }
        return this.matchesShulker(p_matchesPos_2_, p_matchesPos_3_);
    }

    private boolean matchesNameable(BlockPos p_matchesNameable_1_, IBlockAccess p_matchesNameable_2_) {
        String s;
        TileEntity tileentity = p_matchesNameable_2_.getTileEntity(p_matchesNameable_1_);
        if (!(tileentity instanceof IWorldNameable)) {
            return false;
        }
        IWorldNameable iworldnameable = (IWorldNameable)((Object)tileentity);
        return this.nbtName == null || this.nbtName.matchesValue(s = iworldnameable.getName());
    }

    private boolean matchesBeacon(BlockPos p_matchesBeacon_1_, IBlockAccess p_matchesBeacon_2_) {
        String s;
        int i;
        TileEntity tileentity = p_matchesBeacon_2_.getTileEntity(p_matchesBeacon_1_);
        if (!(tileentity instanceof TileEntityBeacon)) {
            return false;
        }
        TileEntityBeacon tileentitybeacon = (TileEntityBeacon)tileentity;
        if (this.levels != null && !this.levels.isInRange(i = tileentitybeacon.getLevels())) {
            return false;
        }
        return this.nbtName == null || this.nbtName.matchesValue(s = tileentitybeacon.getName());
    }

    private boolean matchesChest(BlockPos p_matchesChest_1_, IBlockAccess p_matchesChest_2_) {
        TileEntity tileentity = p_matchesChest_2_.getTileEntity(p_matchesChest_1_);
        if (tileentity instanceof TileEntityChest) {
            TileEntityChest tileentitychest = (TileEntityChest)tileentity;
            return this.matchesChest(tileentitychest, p_matchesChest_1_, p_matchesChest_2_);
        }
        if (tileentity instanceof TileEntityEnderChest) {
            TileEntityEnderChest tileentityenderchest = (TileEntityEnderChest)tileentity;
            return this.matchesEnderChest(tileentityenderchest, p_matchesChest_1_, p_matchesChest_2_);
        }
        return false;
    }

    private boolean matchesChest(TileEntityChest p_matchesChest_1_, BlockPos p_matchesChest_2_, IBlockAccess p_matchesChest_3_) {
        boolean flag = p_matchesChest_1_.adjacentChestXNeg != null || p_matchesChest_1_.adjacentChestXPos != null || p_matchesChest_1_.adjacentChestZNeg != null || p_matchesChest_1_.adjacentChestZPos != null;
        boolean flag1 = p_matchesChest_1_.getChestType() == BlockChest.Type.TRAP;
        boolean flag2 = CustomGuis.isChristmas;
        boolean flag3 = false;
        String s = p_matchesChest_1_.getName();
        return this.matchesChest(flag, flag1, flag2, flag3, s);
    }

    private boolean matchesEnderChest(TileEntityEnderChest p_matchesEnderChest_1_, BlockPos p_matchesEnderChest_2_, IBlockAccess p_matchesEnderChest_3_) {
        return this.matchesChest(false, false, false, true, null);
    }

    private boolean matchesChest(boolean p_matchesChest_1_, boolean p_matchesChest_2_, boolean p_matchesChest_3_, boolean p_matchesChest_4_, String p_matchesChest_5_) {
        if (this.large != null && this.large != p_matchesChest_1_) {
            return false;
        }
        if (this.trapped != null && this.trapped != p_matchesChest_2_) {
            return false;
        }
        if (this.christmas != null && this.christmas != p_matchesChest_3_) {
            return false;
        }
        if (this.ender != null && this.ender != p_matchesChest_4_) {
            return false;
        }
        return this.nbtName == null || this.nbtName.matchesValue(p_matchesChest_5_);
    }

    private boolean matchesDispenser(BlockPos p_matchesDispenser_1_, IBlockAccess p_matchesDispenser_2_) {
        EnumVariant customguiproperties$enumvariant;
        String s;
        TileEntity tileentity = p_matchesDispenser_2_.getTileEntity(p_matchesDispenser_1_);
        if (!(tileentity instanceof TileEntityDispenser)) {
            return false;
        }
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)tileentity;
        if (this.nbtName != null && !this.nbtName.matchesValue(s = tileentitydispenser.getName())) {
            return false;
        }
        return this.variants == null || Config.equalsOne((Object)(customguiproperties$enumvariant = this.getDispenserVariant(tileentitydispenser)), (Object[])this.variants);
    }

    private EnumVariant getDispenserVariant(TileEntityDispenser p_getDispenserVariant_1_) {
        return p_getDispenserVariant_1_ instanceof TileEntityDropper ? EnumVariant.DROPPER : EnumVariant.DISPENSER;
    }

    private boolean matchesShulker(BlockPos p_matchesShulker_1_, IBlockAccess p_matchesShulker_2_) {
        EnumDyeColor enumdyecolor;
        String s;
        TileEntity tileentity = p_matchesShulker_2_.getTileEntity(p_matchesShulker_1_);
        if (!(tileentity instanceof TileEntityShulkerBox)) {
            return false;
        }
        TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox)tileentity;
        if (this.nbtName != null && !this.nbtName.matchesValue(s = tileentityshulkerbox.getName())) {
            return false;
        }
        return this.colors == null || Config.equalsOne(enumdyecolor = tileentityshulkerbox.func_190592_s(), this.colors);
    }

    public boolean matchesEntity(EnumContainer p_matchesEntity_1_, Entity p_matchesEntity_2_, IBlockAccess p_matchesEntity_3_) {
        String s;
        if (!this.matchesGeneral(p_matchesEntity_1_, p_matchesEntity_2_.getPosition(), p_matchesEntity_3_)) {
            return false;
        }
        if (this.nbtName != null && !this.nbtName.matchesValue(s = p_matchesEntity_2_.getName())) {
            return false;
        }
        switch (p_matchesEntity_1_) {
            case HORSE: {
                return this.matchesHorse(p_matchesEntity_2_, p_matchesEntity_3_);
            }
            case VILLAGER: {
                return this.matchesVillager(p_matchesEntity_2_, p_matchesEntity_3_);
            }
        }
        return true;
    }

    private boolean matchesVillager(Entity p_matchesVillager_1_, IBlockAccess p_matchesVillager_2_) {
        if (!(p_matchesVillager_1_ instanceof EntityVillager)) {
            return false;
        }
        EntityVillager entityvillager = (EntityVillager)p_matchesVillager_1_;
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        entityvillager.writeToNBT(nbttagcompound);
        Integer integer = nbttagcompound.getInteger("Profession");
        Integer integer1 = nbttagcompound.getInteger("Career");
        if (integer != null && integer1 != null) {
            if (this.professions != null) {
                boolean flag = false;
                for (int i = 0; i < this.professions.length; ++i) {
                    VillagerProfession villagerprofession = this.professions[i];
                    if (!villagerprofession.matches(integer, integer1)) continue;
                    flag = true;
                    break;
                }
                if (!flag) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean matchesHorse(Entity p_matchesHorse_1_, IBlockAccess p_matchesHorse_2_) {
        EntityLlama entityllama;
        EnumDyeColor enumdyecolor;
        EnumVariant customguiproperties$enumvariant;
        if (!(p_matchesHorse_1_ instanceof AbstractHorse)) {
            return false;
        }
        AbstractHorse abstracthorse = (AbstractHorse)p_matchesHorse_1_;
        if (this.variants != null && !Config.equalsOne((Object)(customguiproperties$enumvariant = this.getHorseVariant(abstracthorse)), (Object[])this.variants)) {
            return false;
        }
        return this.colors == null || !(abstracthorse instanceof EntityLlama) || Config.equalsOne(enumdyecolor = (entityllama = (EntityLlama)abstracthorse).func_190704_dO(), this.colors);
    }

    private EnumVariant getHorseVariant(AbstractHorse p_getHorseVariant_1_) {
        if (p_getHorseVariant_1_ instanceof EntityHorse) {
            return EnumVariant.HORSE;
        }
        if (p_getHorseVariant_1_ instanceof EntityDonkey) {
            return EnumVariant.DONKEY;
        }
        if (p_getHorseVariant_1_ instanceof EntityMule) {
            return EnumVariant.MULE;
        }
        return p_getHorseVariant_1_ instanceof EntityLlama ? EnumVariant.LLAMA : null;
    }

    public EnumContainer getContainer() {
        return this.container;
    }

    public ResourceLocation getTextureLocation(ResourceLocation p_getTextureLocation_1_) {
        ResourceLocation resourcelocation = this.textureLocations.get(p_getTextureLocation_1_);
        return resourcelocation == null ? p_getTextureLocation_1_ : resourcelocation;
    }

    public String toString() {
        return "name: " + this.fileName + ", container: " + (Object)((Object)this.container) + ", textures: " + this.textureLocations;
    }

    private static enum EnumVariant {
        HORSE,
        DONKEY,
        MULE,
        LLAMA,
        DISPENSER,
        DROPPER;

    }

    public static enum EnumContainer {
        ANVIL,
        BEACON,
        BREWING_STAND,
        CHEST,
        CRAFTING,
        DISPENSER,
        ENCHANTMENT,
        FURNACE,
        HOPPER,
        HORSE,
        VILLAGER,
        SHULKER_BOX,
        CREATIVE,
        INVENTORY;

    }
}

