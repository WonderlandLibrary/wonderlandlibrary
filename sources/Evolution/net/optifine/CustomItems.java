package net.optifine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.config.NbtTagValue;
import net.optifine.render.Blender;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;
import net.optifine.util.PropertiesOrdered;
import net.optifine.util.ResUtils;
import net.optifine.util.StrUtils;

public class CustomItems
{
    private static CustomItemProperties[][] itemProperties = (CustomItemProperties[][])null;
    private static CustomItemProperties[][] enchantmentProperties = (CustomItemProperties[][])null;
    private static Map mapPotionIds = null;
    private static ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
    private static boolean useGlint = true;
    private static boolean renderOffHand = false;
    public static final int MASK_POTION_SPLASH = 16384;
    public static final int MASK_POTION_NAME = 63;
    public static final int MASK_POTION_EXTENDED = 64;
    public static final String KEY_TEXTURE_OVERLAY = "texture.potion_overlay";
    public static final String KEY_TEXTURE_SPLASH = "texture.potion_bottle_splash";
    public static final String KEY_TEXTURE_DRINKABLE = "texture.potion_bottle_drinkable";
    public static final String DEFAULT_TEXTURE_OVERLAY = "items/potion_overlay";
    public static final String DEFAULT_TEXTURE_SPLASH = "items/potion_bottle_splash";
    public static final String DEFAULT_TEXTURE_DRINKABLE = "items/potion_bottle_drinkable";
    private static final int[][] EMPTY_INT2_ARRAY = new int[0][];
    private static final Map<String, Integer> mapPotionDamages = makeMapPotionDamages();
    private static final String TYPE_POTION_NORMAL = "normal";
    private static final String TYPE_POTION_SPLASH = "splash";
    private static final String TYPE_POTION_LINGER = "linger";

    public static void update()
    {
        itemProperties = (CustomItemProperties[][])null;
        enchantmentProperties = (CustomItemProperties[][])null;
        useGlint = true;

        if (Config.isCustomItems())
        {
            readCitProperties("mcpatcher/cit.properties");
            IResourcePack[] airesourcepack = Config.getResourcePacks();

            for (int i = airesourcepack.length - 1; i >= 0; --i)
            {
                IResourcePack iresourcepack = airesourcepack[i];
                update(iresourcepack);
            }

            update(Config.getDefaultResourcePack());

            if (itemProperties.length <= 0)
            {
                itemProperties = (CustomItemProperties[][])null;
            }

            if (enchantmentProperties.length <= 0)
            {
                enchantmentProperties = (CustomItemProperties[][])null;
            }
        }
    }

    private static void readCitProperties(String fileName)
    {
        try
        {
            ResourceLocation resourcelocation = new ResourceLocation(fileName);
            InputStream inputstream = Config.getResourceStream(resourcelocation);

            if (inputstream == null)
            {
                return;
            }

            Config.dbg("CustomItems: Loading " + fileName);
            Properties properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            useGlint = Config.parseBoolean(properties.getProperty("useGlint"), true);
        }
        catch (FileNotFoundException var4)
        {
            return;
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    private static void update(IResourcePack rp)
    {
        String[] astring = ResUtils.collectFiles(rp, "mcpatcher/cit/", ".properties", (String[])null);
        Map map = makeAutoImageProperties(rp);

        if (map.size() > 0)
        {
            Set set = map.keySet();
            String[] astring1 = (String[])set.toArray(new String[set.size()]);
            astring = (String[])Config.addObjectsToArray(astring, astring1);
        }

        Arrays.sort((Object[])astring);
        List list = makePropertyList(itemProperties);
        List list1 = makePropertyList(enchantmentProperties);

        for (int i = 0; i < astring.length; ++i)
        {
            String s = astring[i];
            Config.dbg("CustomItems: " + s);

            try
            {
                CustomItemProperties customitemproperties = null;

                if (map.containsKey(s))
                {
                    customitemproperties = (CustomItemProperties)map.get(s);
                }

                if (customitemproperties == null)
                {
                    ResourceLocation resourcelocation = new ResourceLocation(s);
                    InputStream inputstream = rp.getInputStream(resourcelocation);

                    if (inputstream == null)
                    {
                        Config.warn("CustomItems file not found: " + s);
                        continue;
                    }

                    Properties properties = new PropertiesOrdered();
                    properties.load(inputstream);
                    inputstream.close();
                    customitemproperties = new CustomItemProperties(properties, s);
                }

                if (customitemproperties.isValid(s))
                {
                    addToItemList(customitemproperties, list);
                    addToEnchantmentList(customitemproperties, list1);
                }
            }
            catch (FileNotFoundException var11)
            {
                Config.warn("CustomItems file not found: " + s);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }

        itemProperties = propertyListToArray(list);
        enchantmentProperties = propertyListToArray(list1);
        Comparator comparator = getPropertiesComparator();

        for (int j = 0; j < itemProperties.length; ++j)
        {
            CustomItemProperties[] acustomitemproperties = itemProperties[j];

            if (acustomitemproperties != null)
            {
                Arrays.sort(acustomitemproperties, comparator);
            }
        }

        for (int k = 0; k < enchantmentProperties.length; ++k)
        {
            CustomItemProperties[] acustomitemproperties1 = enchantmentProperties[k];

            if (acustomitemproperties1 != null)
            {
                Arrays.sort(acustomitemproperties1, comparator);
            }
        }
    }

    private static Comparator getPropertiesComparator()
    {
        Comparator comparator = new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                CustomItemProperties customitemproperties = (CustomItemProperties)o1;
                CustomItemProperties customitemproperties1 = (CustomItemProperties)o2;

                if (customitemproperties.layer != customitemproperties1.layer)
                {
                    return customitemproperties.layer - customitemproperties1.layer;
                }
                else if (customitemproperties.weight != customitemproperties1.weight)
                {
                    return customitemproperties1.weight - customitemproperties.weight;
                }
                else
                {
                    return !customitemproperties.basePath.equals(customitemproperties1.basePath) ? customitemproperties.basePath.compareTo(customitemproperties1.basePath) : customitemproperties.name.compareTo(customitemproperties1.name);
                }
            }
        };
        return comparator;
    }

    public static void updateIcons(TextureMap textureMap)
    {
        for (CustomItemProperties customitemproperties : getAllProperties())
        {
            customitemproperties.updateIcons(textureMap);
        }
    }

    public static void loadModels(ModelBakery modelBakery)
    {
        for (CustomItemProperties customitemproperties : getAllProperties())
        {
            customitemproperties.loadModels(modelBakery);
        }
    }

    public static void updateModels()
    {
        for (CustomItemProperties customitemproperties : getAllProperties())
        {
            if (customitemproperties.type == 1)
            {
                TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
                customitemproperties.updateModelTexture(texturemap, itemModelGenerator);
                customitemproperties.updateModelsFull();
            }
        }
    }

    private static List<CustomItemProperties> getAllProperties()
    {
        List<CustomItemProperties> list = new ArrayList<CustomItemProperties>();
        addAll(itemProperties, list);
        addAll(enchantmentProperties, list);
        return list;
    }

    private static void addAll(CustomItemProperties[][] cipsArr, List<CustomItemProperties> list)
    {
        if (cipsArr != null)
        {
            for (int i = 0; i < cipsArr.length; ++i)
            {
                CustomItemProperties[] acustomitemproperties = cipsArr[i];

                if (acustomitemproperties != null)
                {
                    for (int j = 0; j < acustomitemproperties.length; ++j)
                    {
                        CustomItemProperties customitemproperties = acustomitemproperties[j];

                        if (customitemproperties != null)
                        {
                            list.add(customitemproperties);
                        }
                    }
                }
            }
        }
    }

    private static Map makeAutoImageProperties(IResourcePack rp)
    {
        Map map = new HashMap();
        map.putAll(makePotionImageProperties(rp, "normal", Item.getIdFromItem(Items.POTIONITEM)));
        map.putAll(makePotionImageProperties(rp, "splash", Item.getIdFromItem(Items.SPLASH_POTION)));
        map.putAll(makePotionImageProperties(rp, "linger", Item.getIdFromItem(Items.LINGERING_POTION)));
        return map;
    }

    private static Map makePotionImageProperties(IResourcePack rp, String type, int itemId)
    {
        Map map = new HashMap();
        String s = type + "/";
        String[] astring = new String[] {"mcpatcher/cit/potion/" + s, "mcpatcher/cit/Potion/" + s};
        String[] astring1 = new String[] {".png"};
        String[] astring2 = ResUtils.collectFiles(rp, astring, astring1);

        for (int i = 0; i < astring2.length; ++i)
        {
            String s1 = astring2[i];
            String name = StrUtils.removePrefixSuffix(s1, astring, astring1);
            Properties properties = makePotionProperties(name, type, itemId, s1);

            if (properties != null)
            {
                String s3 = StrUtils.removeSuffix(s1, astring1) + ".properties";
                CustomItemProperties customitemproperties = new CustomItemProperties(properties, s3);
                map.put(s3, customitemproperties);
            }
        }

        return map;
    }

    private static Properties makePotionProperties(String name, String type, int itemId, String path)
    {
        if (StrUtils.endsWith(name, new String[] {"_n", "_s"}))
        {
            return null;
        }
        else if (name.equals("empty") && type.equals("normal"))
        {
            itemId = Item.getIdFromItem(Items.GLASS_BOTTLE);
            Properties properties = new PropertiesOrdered();
            properties.put("type", "item");
            properties.put("items", "" + itemId);
            return properties;
        }
        else
        {
            int[] aint = (int[])getMapPotionIds().get(name);

            if (aint == null)
            {
                Config.warn("Potion not found for image: " + path);
                return null;
            }
            else
            {
                StringBuffer stringbuffer = new StringBuffer();

                for (int i = 0; i < aint.length; ++i)
                {
                    int j = aint[i];

                    if (type.equals("splash"))
                    {
                        j |= 16384;
                    }

                    if (i > 0)
                    {
                        stringbuffer.append(" ");
                    }

                    stringbuffer.append(j);
                }

                int k = 16447;

                if (name.equals("water") || name.equals("mundane"))
                {
                    k |= 64;
                }

                Properties properties1 = new PropertiesOrdered();
                properties1.put("type", "item");
                properties1.put("items", "" + itemId);
                properties1.put("damage", "" + stringbuffer.toString());
                properties1.put("damageMask", "" + k);

                if (type.equals("splash"))
                {
                    properties1.put("texture.potion_bottle_splash", name);
                }
                else
                {
                    properties1.put("texture.potion_bottle_drinkable", name);
                }

                return properties1;
            }
        }
    }

    private static Map getMapPotionIds()
    {
        if (mapPotionIds == null)
        {
            mapPotionIds = new LinkedHashMap();
            mapPotionIds.put("water", getPotionId(0, 0));
            mapPotionIds.put("awkward", getPotionId(0, 1));
            mapPotionIds.put("thick", getPotionId(0, 2));
            mapPotionIds.put("potent", getPotionId(0, 3));
            mapPotionIds.put("regeneration", getPotionIds(1));
            mapPotionIds.put("movespeed", getPotionIds(2));
            mapPotionIds.put("fireresistance", getPotionIds(3));
            mapPotionIds.put("poison", getPotionIds(4));
            mapPotionIds.put("heal", getPotionIds(5));
            mapPotionIds.put("nightvision", getPotionIds(6));
            mapPotionIds.put("clear", getPotionId(7, 0));
            mapPotionIds.put("bungling", getPotionId(7, 1));
            mapPotionIds.put("charming", getPotionId(7, 2));
            mapPotionIds.put("rank", getPotionId(7, 3));
            mapPotionIds.put("weakness", getPotionIds(8));
            mapPotionIds.put("damageboost", getPotionIds(9));
            mapPotionIds.put("moveslowdown", getPotionIds(10));
            mapPotionIds.put("leaping", getPotionIds(11));
            mapPotionIds.put("harm", getPotionIds(12));
            mapPotionIds.put("waterbreathing", getPotionIds(13));
            mapPotionIds.put("invisibility", getPotionIds(14));
            mapPotionIds.put("thin", getPotionId(15, 0));
            mapPotionIds.put("debonair", getPotionId(15, 1));
            mapPotionIds.put("sparkling", getPotionId(15, 2));
            mapPotionIds.put("stinky", getPotionId(15, 3));
            mapPotionIds.put("mundane", getPotionId(0, 4));
            mapPotionIds.put("speed", mapPotionIds.get("movespeed"));
            mapPotionIds.put("fire_resistance", mapPotionIds.get("fireresistance"));
            mapPotionIds.put("instant_health", mapPotionIds.get("heal"));
            mapPotionIds.put("night_vision", mapPotionIds.get("nightvision"));
            mapPotionIds.put("strength", mapPotionIds.get("damageboost"));
            mapPotionIds.put("slowness", mapPotionIds.get("moveslowdown"));
            mapPotionIds.put("instant_damage", mapPotionIds.get("harm"));
            mapPotionIds.put("water_breathing", mapPotionIds.get("waterbreathing"));
        }

        return mapPotionIds;
    }

    private static int[] getPotionIds(int baseId)
    {
        return new int[] {baseId, baseId + 16, baseId + 32, baseId + 48};
    }

    private static int[] getPotionId(int baseId, int subId)
    {
        return new int[] {baseId + subId * 16};
    }

    private static int getPotionNameDamage(String name)
    {
        String s = "effect." + name;

        for (ResourceLocation resourcelocation : Potion.REGISTRY.getKeys())
        {
            Potion potion = Potion.REGISTRY.getObject(resourcelocation);
            String s1 = potion.getName();

            if (s.equals(s1))
            {
                return Potion.getIdFromPotion(potion);
            }
        }

        return -1;
    }

    private static List makePropertyList(CustomItemProperties[][] propsArr)
    {
        List list = new ArrayList();

        if (propsArr != null)
        {
            for (int i = 0; i < propsArr.length; ++i)
            {
                CustomItemProperties[] acustomitemproperties = propsArr[i];
                List list1 = null;

                if (acustomitemproperties != null)
                {
                    list1 = new ArrayList(Arrays.asList(acustomitemproperties));
                }

                list.add(list1);
            }
        }

        return list;
    }

    private static CustomItemProperties[][] propertyListToArray(List list)
    {
        CustomItemProperties[][] acustomitemproperties = new CustomItemProperties[list.size()][];

        for (int i = 0; i < list.size(); ++i)
        {
            List subList = (List)list.get(i);

            if (subList != null)
            {
                CustomItemProperties[] acustomitemproperties1 = (CustomItemProperties[])subList.toArray(new CustomItemProperties[subList.size()]);
                Arrays.sort(acustomitemproperties1, new CustomItemsComparator());
                acustomitemproperties[i] = acustomitemproperties1;
            }
        }

        return acustomitemproperties;
    }

    private static void addToItemList(CustomItemProperties cp, List itemList)
    {
        if (cp.items != null)
        {
            for (int i = 0; i < cp.items.length; ++i)
            {
                int j = cp.items[i];

                if (j <= 0)
                {
                    Config.warn("Invalid item ID: " + j);
                }
                else
                {
                    addToList(cp, itemList, j);
                }
            }
        }
    }

    private static void addToEnchantmentList(CustomItemProperties cp, List enchantmentList)
    {
        if (cp.type == 2)
        {
            if (cp.enchantmentIds != null)
            {
                for (int i = 0; i < 256; ++i)
                {
                    if (cp.enchantmentIds.isInRange(i))
                    {
                        addToList(cp, enchantmentList, i);
                    }
                }
            }
        }
    }

    private static void addToList(CustomItemProperties cp, List list, int id)
    {
        while (id >= list.size()) {
            list.add(null);
        }
        ArrayList<CustomItemProperties> subList = (ArrayList<CustomItemProperties>)list.get(id);
        if (subList == null) {
            subList = new ArrayList<CustomItemProperties>();
            list.set(id, subList);
        }
        subList.add(cp);
    }

    public static IBakedModel getCustomItemModel(ItemStack itemStack, IBakedModel model, ResourceLocation modelLocation, boolean fullModel)
    {
        if (!fullModel && model.isGui3d())
        {
            return model;
        }
        else if (itemProperties == null)
        {
            return model;
        }
        else
        {
            CustomItemProperties customitemproperties = getCustomItemProperties(itemStack, 1);

            if (customitemproperties == null)
            {
                return model;
            }
            else
            {
                IBakedModel ibakedmodel = customitemproperties.getBakedModel(modelLocation, fullModel);
                return ibakedmodel != null ? ibakedmodel : model;
            }
        }
    }

    public static boolean bindCustomArmorTexture(ItemStack itemStack, EntityEquipmentSlot slot, String overlay)
    {
        if (itemProperties == null)
        {
            return false;
        }
        else
        {
            ResourceLocation resourcelocation = getCustomArmorLocation(itemStack, slot, overlay);

            if (resourcelocation == null)
            {
                return false;
            }
            else
            {
                Config.getTextureManager().bindTexture(resourcelocation);
                return true;
            }
        }
    }

    private static ResourceLocation getCustomArmorLocation(ItemStack itemStack, EntityEquipmentSlot slot, String overlay)
    {
        CustomItemProperties customitemproperties = getCustomItemProperties(itemStack, 3);

        if (customitemproperties == null)
        {
            return null;
        }
        else if (customitemproperties.mapTextureLocations == null)
        {
            return customitemproperties.textureLocation;
        }
        else
        {
            Item item = itemStack.getItem();

            if (!(item instanceof ItemArmor))
            {
                return null;
            }
            else
            {
                ItemArmor itemarmor = (ItemArmor)item;
                String s = itemarmor.getArmorMaterial().getName();
                int i = slot == EntityEquipmentSlot.LEGS ? 2 : 1;
                StringBuffer stringbuffer = new StringBuffer();
                stringbuffer.append("texture.");
                stringbuffer.append(s);
                stringbuffer.append("_layer_");
                stringbuffer.append(i);

                if (overlay != null)
                {
                    stringbuffer.append("_");
                    stringbuffer.append(overlay);
                }

                String s1 = stringbuffer.toString();
                ResourceLocation resourcelocation = (ResourceLocation)customitemproperties.mapTextureLocations.get(s1);
                return resourcelocation == null ? customitemproperties.textureLocation : resourcelocation;
            }
        }
    }

    public static ResourceLocation getCustomElytraTexture(ItemStack itemStack, ResourceLocation locElytra)
    {
        if (itemProperties == null)
        {
            return locElytra;
        }
        else
        {
            CustomItemProperties customitemproperties = getCustomItemProperties(itemStack, 4);

            if (customitemproperties == null)
            {
                return locElytra;
            }
            else
            {
                return customitemproperties.textureLocation == null ? locElytra : customitemproperties.textureLocation;
            }
        }
    }

    private static CustomItemProperties getCustomItemProperties(ItemStack itemStack, int type)
    {
        if (itemProperties == null)
        {
            return null;
        }
        else if (itemStack == null)
        {
            return null;
        }
        else
        {
            Item item = itemStack.getItem();
            int i = Item.getIdFromItem(item);

            if (i >= 0 && i < itemProperties.length)
            {
                CustomItemProperties[] acustomitemproperties = itemProperties[i];

                if (acustomitemproperties != null)
                {
                    for (int j = 0; j < acustomitemproperties.length; ++j)
                    {
                        CustomItemProperties customitemproperties = acustomitemproperties[j];

                        if (customitemproperties.type == type && matchesProperties(customitemproperties, itemStack, (int[][])null))
                        {
                            return customitemproperties;
                        }
                    }
                }
            }

            return null;
        }
    }

    private static boolean matchesProperties(CustomItemProperties cip, ItemStack itemStack, int[][] enchantmentIdLevels)
    {
        Item item = itemStack.getItem();

        if (cip.damage != null)
        {
            int i = getItemStackDamage(itemStack);

            if (i < 0)
            {
                return false;
            }

            if (cip.damageMask != 0)
            {
                i &= cip.damageMask;
            }

            if (cip.damagePercent)
            {
                int j = item.getMaxDamage();
                i = (int)((double)(i * 100) / (double)j);
            }

            if (!cip.damage.isInRange(i))
            {
                return false;
            }
        }

        if (cip.stackSize != null && !cip.stackSize.isInRange(itemStack.getCount()))
        {
            return false;
        }
        else
        {
            int[][] aint = enchantmentIdLevels;

            if (cip.enchantmentIds != null)
            {
                if (enchantmentIdLevels == null)
                {
                    aint = getEnchantmentIdLevels(itemStack);
                }

                boolean flag = false;

                for (int k = 0; k < aint.length; ++k)
                {
                    int l = aint[k][0];

                    if (cip.enchantmentIds.isInRange(l))
                    {
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    return false;
                }
            }

            if (cip.enchantmentLevels != null)
            {
                if (aint == null)
                {
                    aint = getEnchantmentIdLevels(itemStack);
                }

                boolean flag1 = false;

                for (int i1 = 0; i1 < aint.length; ++i1)
                {
                    int k1 = aint[i1][1];

                    if (cip.enchantmentLevels.isInRange(k1))
                    {
                        flag1 = true;
                        break;
                    }
                }

                if (!flag1)
                {
                    return false;
                }
            }

            if (cip.nbtTagValues != null)
            {
                NBTTagCompound nbttagcompound = itemStack.getTagCompound();

                for (int j1 = 0; j1 < cip.nbtTagValues.length; ++j1)
                {
                    NbtTagValue nbttagvalue = cip.nbtTagValues[j1];

                    if (!nbttagvalue.matches(nbttagcompound))
                    {
                        return false;
                    }
                }
            }

            if (cip.hand != 0)
            {
                if (cip.hand == 1 && renderOffHand)
                {
                    return false;
                }

                if (cip.hand == 2 && !renderOffHand)
                {
                    return false;
                }
            }

            return true;
        }
    }

    private static int getItemStackDamage(ItemStack itemStack)
    {
        Item item = itemStack.getItem();
        return item instanceof ItemPotion ? getPotionDamage(itemStack) : itemStack.getItemDamage();
    }

    private static int getPotionDamage(ItemStack itemStack)
    {
        NBTTagCompound nbttagcompound = itemStack.getTagCompound();

        if (nbttagcompound == null)
        {
            return 0;
        }
        else
        {
            String s = nbttagcompound.getString("Potion");

            if (s != null && !s.equals(""))
            {
                Integer integer = mapPotionDamages.get(s);

                if (integer == null)
                {
                    return -1;
                }
                else
                {
                    int i = integer.intValue();

                    if (itemStack.getItem() == Items.SPLASH_POTION)
                    {
                        i |= 16384;
                    }

                    return i;
                }
            }
            else
            {
                return 0;
            }
        }
    }

    private static Map<String, Integer> makeMapPotionDamages()
    {
        Map<String, Integer> map = new HashMap<String, Integer>();
        addPotion("water", 0, false, map);
        addPotion("awkward", 16, false, map);
        addPotion("thick", 32, false, map);
        addPotion("mundane", 64, false, map);
        addPotion("regeneration", 1, true, map);
        addPotion("swiftness", 2, true, map);
        addPotion("fire_resistance", 3, true, map);
        addPotion("poison", 4, true, map);
        addPotion("healing", 5, true, map);
        addPotion("night_vision", 6, true, map);
        addPotion("weakness", 8, true, map);
        addPotion("strength", 9, true, map);
        addPotion("slowness", 10, true, map);
        addPotion("leaping", 11, true, map);
        addPotion("harming", 12, true, map);
        addPotion("water_breathing", 13, true, map);
        addPotion("invisibility", 14, true, map);
        return map;
    }

    private static void addPotion(String name, int value, boolean extended, Map<String, Integer> map)
    {
        if (extended)
        {
            value |= 8192;
        }

        map.put("minecraft:" + name, Integer.valueOf(value));

        if (extended)
        {
            int i = value | 32;
            map.put("minecraft:strong_" + name, Integer.valueOf(i));
            int j = value | 64;
            map.put("minecraft:long_" + name, Integer.valueOf(j));
        }
    }

    private static int[][] getEnchantmentIdLevels(ItemStack itemStack)
    {
        Item item = itemStack.getItem();
        NBTTagList nbttaglist1;

        if (item == Items.ENCHANTED_BOOK)
        {
            ItemEnchantedBook itemenchantedbook = (ItemEnchantedBook)Items.ENCHANTED_BOOK;
            nbttaglist1 = ItemEnchantedBook.getEnchantments(itemStack);
        }
        else
        {
            nbttaglist1 = itemStack.getEnchantmentTagList();
        }

        NBTTagList nbttaglist = nbttaglist1;

        if (nbttaglist != null && nbttaglist.tagCount() > 0)
        {
            int[][] aint = new int[nbttaglist.tagCount()][2];

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getShort("id");
                int k = nbttagcompound.getShort("lvl");
                aint[i][0] = j;
                aint[i][1] = k;
            }

            return aint;
        }
        else
        {
            return EMPTY_INT2_ARRAY;
        }
    }

    public static boolean renderCustomEffect(RenderItem renderItem, ItemStack itemStack, IBakedModel model)
    {
        if (enchantmentProperties == null)
        {
            return false;
        }
        else if (itemStack == null)
        {
            return false;
        }
        else
        {
            int[][] aint = getEnchantmentIdLevels(itemStack);

            if (aint.length <= 0)
            {
                return false;
            }
            else
            {
                Set set = null;
                boolean flag = false;
                TextureManager texturemanager = Config.getTextureManager();

                for (int i = 0; i < aint.length; ++i)
                {
                    int j = aint[i][0];

                    if (j >= 0 && j < enchantmentProperties.length)
                    {
                        CustomItemProperties[] acustomitemproperties = enchantmentProperties[j];

                        if (acustomitemproperties != null)
                        {
                            for (int k = 0; k < acustomitemproperties.length; ++k)
                            {
                                CustomItemProperties customitemproperties = acustomitemproperties[k];

                                if (set == null)
                                {
                                    set = new HashSet();
                                }

                                if (set.add(Integer.valueOf(j)) && matchesProperties(customitemproperties, itemStack, aint) && customitemproperties.textureLocation != null)
                                {
                                    texturemanager.bindTexture(customitemproperties.textureLocation);
                                    float f = customitemproperties.getTextureWidth(texturemanager);

                                    if (!flag)
                                    {
                                        flag = true;
                                        GlStateManager.depthMask(false);
                                        GlStateManager.depthFunc(514);
                                        GlStateManager.disableLighting();
                                        GlStateManager.matrixMode(5890);
                                    }

                                    Blender.setupBlend(customitemproperties.blend, 1.0F);
                                    GlStateManager.pushMatrix();
                                    GlStateManager.scale(f / 2.0F, f / 2.0F, f / 2.0F);
                                    float f1 = customitemproperties.speed * (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
                                    GlStateManager.translate(f1, 0.0F, 0.0F);
                                    GlStateManager.rotate(customitemproperties.rotation, 0.0F, 0.0F, 1.0F);
                                    renderItem.renderModel(model, -1);
                                    GlStateManager.popMatrix();
                                }
                            }
                        }
                    }
                }

                if (flag)
                {
                    GlStateManager.enableAlpha();
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.matrixMode(5888);
                    GlStateManager.enableLighting();
                    GlStateManager.depthFunc(515);
                    GlStateManager.depthMask(true);
                    texturemanager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                }

                return flag;
            }
        }
    }

    public static boolean renderCustomArmorEffect(EntityLivingBase entity, ItemStack itemStack, ModelBase model, float limbSwing, float prevLimbSwing, float partialTicks, float timeLimbSwing, float yaw, float pitch, float scale)
    {
        if (enchantmentProperties == null)
        {
            return false;
        }
        else if (Config.isShaders() && Shaders.isShadowPass)
        {
            return false;
        }
        else if (itemStack == null)
        {
            return false;
        }
        else
        {
            int[][] aint = getEnchantmentIdLevels(itemStack);

            if (aint.length <= 0)
            {
                return false;
            }
            else
            {
                Set set = null;
                boolean flag = false;
                TextureManager texturemanager = Config.getTextureManager();

                for (int i = 0; i < aint.length; ++i)
                {
                    int j = aint[i][0];

                    if (j >= 0 && j < enchantmentProperties.length)
                    {
                        CustomItemProperties[] acustomitemproperties = enchantmentProperties[j];

                        if (acustomitemproperties != null)
                        {
                            for (int k = 0; k < acustomitemproperties.length; ++k)
                            {
                                CustomItemProperties customitemproperties = acustomitemproperties[k];

                                if (set == null)
                                {
                                    set = new HashSet();
                                }

                                if (set.add(Integer.valueOf(j)) && matchesProperties(customitemproperties, itemStack, aint) && customitemproperties.textureLocation != null)
                                {
                                    texturemanager.bindTexture(customitemproperties.textureLocation);
                                    float f = customitemproperties.getTextureWidth(texturemanager);

                                    if (!flag)
                                    {
                                        flag = true;

                                        if (Config.isShaders())
                                        {
                                            ShadersRender.renderEnchantedGlintBegin();
                                        }

                                        GlStateManager.enableBlend();
                                        GlStateManager.depthFunc(514);
                                        GlStateManager.depthMask(false);
                                    }

                                    Blender.setupBlend(customitemproperties.blend, 1.0F);
                                    GlStateManager.disableLighting();
                                    GlStateManager.matrixMode(5890);
                                    GlStateManager.loadIdentity();
                                    GlStateManager.rotate(customitemproperties.rotation, 0.0F, 0.0F, 1.0F);
                                    float f1 = f / 8.0F;
                                    GlStateManager.scale(f1, f1 / 2.0F, f1);
                                    float f2 = customitemproperties.speed * (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
                                    GlStateManager.translate(0.0F, f2, 0.0F);
                                    GlStateManager.matrixMode(5888);
                                    model.render(entity, limbSwing, prevLimbSwing, timeLimbSwing, yaw, pitch, scale);
                                }
                            }
                        }
                    }
                }

                if (flag)
                {
                    GlStateManager.enableAlpha();
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.loadIdentity();
                    GlStateManager.matrixMode(5888);
                    GlStateManager.enableLighting();
                    GlStateManager.depthMask(true);
                    GlStateManager.depthFunc(515);
                    GlStateManager.disableBlend();

                    if (Config.isShaders())
                    {
                        ShadersRender.renderEnchantedGlintEnd();
                    }
                }

                return flag;
            }
        }
    }

    public static boolean isUseGlint()
    {
        return useGlint;
    }

    public static void setRenderOffHand(boolean renderOffHand)
    {
        renderOffHand = renderOffHand;
    }
}
