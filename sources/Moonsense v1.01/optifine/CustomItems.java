// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.entity.Entity;
import shadersmod.client.ShadersRender;
import shadersmod.client.Shaders;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.GlStateManager;
import java.util.HashSet;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemArmor;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.potion.Potion;
import java.util.LinkedHashMap;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import java.util.List;
import java.util.Set;
import java.util.Comparator;
import java.util.Arrays;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import java.util.Map;

public class CustomItems
{
    private static CustomItemProperties[][] itemProperties;
    private static CustomItemProperties[][] enchantmentProperties;
    private static Map mapPotionIds;
    private static ItemModelGenerator itemModelGenerator;
    private static boolean useGlint;
    public static final int MASK_POTION_SPLASH = 16384;
    public static final int MASK_POTION_NAME = 63;
    public static final String KEY_TEXTURE_OVERLAY = "texture.potion_overlay";
    public static final String KEY_TEXTURE_SPLASH = "texture.potion_bottle_splash";
    public static final String KEY_TEXTURE_DRINKABLE = "texture.potion_bottle_drinkable";
    public static final String DEFAULT_TEXTURE_OVERLAY = "items/potion_overlay";
    public static final String DEFAULT_TEXTURE_SPLASH = "items/potion_bottle_splash";
    public static final String DEFAULT_TEXTURE_DRINKABLE = "items/potion_bottle_drinkable";
    private static final int[] EMPTY_INT_ARRAY;
    private static final int[][] EMPTY_INT2_ARRAY;
    
    static {
        CustomItems.itemProperties = null;
        CustomItems.enchantmentProperties = null;
        CustomItems.mapPotionIds = null;
        CustomItems.itemModelGenerator = new ItemModelGenerator();
        CustomItems.useGlint = true;
        EMPTY_INT_ARRAY = new int[0];
        EMPTY_INT2_ARRAY = new int[0][];
    }
    
    public static void updateIcons(final TextureMap textureMap) {
        CustomItems.itemProperties = null;
        CustomItems.enchantmentProperties = null;
        CustomItems.useGlint = true;
        if (Config.isCustomItems()) {
            readCitProperties("mcpatcher/cit.properties");
            final IResourcePack[] rps = Config.getResourcePacks();
            for (int i = rps.length - 1; i >= 0; --i) {
                final IResourcePack rp = rps[i];
                updateIcons(textureMap, rp);
            }
            updateIcons(textureMap, Config.getDefaultResourcePack());
            if (CustomItems.itemProperties.length <= 0) {
                CustomItems.itemProperties = null;
            }
            if (CustomItems.enchantmentProperties.length <= 0) {
                CustomItems.enchantmentProperties = null;
            }
        }
    }
    
    private static void readCitProperties(final String fileName) {
        try {
            final ResourceLocation e = new ResourceLocation(fileName);
            final InputStream in = Config.getResourceStream(e);
            if (in == null) {
                return;
            }
            Config.dbg("CustomItems: Loading " + fileName);
            final Properties props = new Properties();
            props.load(in);
            in.close();
            CustomItems.useGlint = Config.parseBoolean(props.getProperty("useGlint"), true);
        }
        catch (FileNotFoundException var6) {}
        catch (IOException var5) {
            var5.printStackTrace();
        }
    }
    
    private static void updateIcons(final TextureMap textureMap, final IResourcePack rp) {
        String[] names = ResUtils.collectFiles(rp, "mcpatcher/cit/", ".properties", null);
        final Map mapAutoProperties = makeAutoImageProperties(rp);
        if (mapAutoProperties.size() > 0) {
            final Set itemList = mapAutoProperties.keySet();
            final String[] enchantmentList = itemList.toArray(new String[itemList.size()]);
            names = (String[])Config.addObjectsToArray(names, enchantmentList);
        }
        Arrays.sort(names);
        final List var14 = makePropertyList(CustomItems.itemProperties);
        final List var15 = makePropertyList(CustomItems.enchantmentProperties);
        for (int comp = 0; comp < names.length; ++comp) {
            final String i = names[comp];
            Config.dbg("CustomItems: " + i);
            try {
                CustomItemProperties cips = null;
                if (mapAutoProperties.containsKey(i)) {
                    cips = mapAutoProperties.get(i);
                }
                if (cips == null) {
                    final ResourceLocation locFile = new ResourceLocation(i);
                    final InputStream in = rp.getInputStream(locFile);
                    if (in == null) {
                        Config.warn("CustomItems file not found: " + i);
                        continue;
                    }
                    final Properties props = new Properties();
                    props.load(in);
                    cips = new CustomItemProperties(props, i);
                }
                if (cips.isValid(i)) {
                    cips.updateIcons(textureMap);
                    addToItemList(cips, var14);
                    addToEnchantmentList(cips, var15);
                }
            }
            catch (FileNotFoundException var20) {
                Config.warn("CustomItems file not found: " + i);
            }
            catch (Exception var16) {
                var16.printStackTrace();
            }
        }
        CustomItems.itemProperties = propertyListToArray(var14);
        CustomItems.enchantmentProperties = propertyListToArray(var15);
        final Comparator var17 = getPropertiesComparator();
        for (int var18 = 0; var18 < CustomItems.itemProperties.length; ++var18) {
            final CustomItemProperties[] var19 = CustomItems.itemProperties[var18];
            if (var19 != null) {
                Arrays.sort(var19, var17);
            }
        }
        for (int var18 = 0; var18 < CustomItems.enchantmentProperties.length; ++var18) {
            final CustomItemProperties[] var19 = CustomItems.enchantmentProperties[var18];
            if (var19 != null) {
                Arrays.sort(var19, var17);
            }
        }
    }
    
    private static Comparator getPropertiesComparator() {
        final Comparator comp = new Comparator() {
            @Override
            public int compare(final Object o1, final Object o2) {
                final CustomItemProperties cip1 = (CustomItemProperties)o1;
                final CustomItemProperties cip2 = (CustomItemProperties)o2;
                return (cip1.layer != cip2.layer) ? (cip1.layer - cip2.layer) : ((cip1.weight != cip2.weight) ? (cip2.weight - cip1.weight) : (cip1.basePath.equals(cip2.basePath) ? cip1.name.compareTo(cip2.name) : cip1.basePath.compareTo(cip2.basePath)));
            }
        };
        return comp;
    }
    
    public static void updateModels() {
        if (CustomItems.itemProperties != null) {
            for (int id = 0; id < CustomItems.itemProperties.length; ++id) {
                final CustomItemProperties[] cips = CustomItems.itemProperties[id];
                if (cips != null) {
                    for (int c = 0; c < cips.length; ++c) {
                        final CustomItemProperties cip = cips[c];
                        if (cip != null && cip.type == 1) {
                            final TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
                            cip.updateModel(textureMap, CustomItems.itemModelGenerator);
                        }
                    }
                }
            }
        }
    }
    
    private static Map makeAutoImageProperties(final IResourcePack rp) {
        final HashMap map = new HashMap();
        map.putAll(makePotionImageProperties(rp, false));
        map.putAll(makePotionImageProperties(rp, true));
        return map;
    }
    
    private static Map makePotionImageProperties(final IResourcePack rp, final boolean splash) {
        final HashMap map = new HashMap();
        final String type = splash ? "splash/" : "normal/";
        final String[] prefixes = { "mcpatcher/cit/potion/" + type, "mcpatcher/cit/Potion/" + type };
        final String[] suffixes = { ".png" };
        final String[] names = ResUtils.collectFiles(rp, prefixes, suffixes);
        for (int i = 0; i < names.length; ++i) {
            final String path = names[i];
            final String name = StrUtils.removePrefixSuffix(path, prefixes, suffixes);
            final Properties props = makePotionProperties(name, splash, path);
            if (props != null) {
                final String pathProp = String.valueOf(StrUtils.removeSuffix(path, suffixes)) + ".properties";
                final CustomItemProperties cip = new CustomItemProperties(props, pathProp);
                map.put(pathProp, cip);
            }
        }
        return map;
    }
    
    private static Properties makePotionProperties(final String name, final boolean splash, final String path) {
        if (StrUtils.endsWith(name, new String[] { "_n", "_s" })) {
            return null;
        }
        if (name.equals("empty") && !splash) {
            final int potionItemId = Item.getIdFromItem(Items.glass_bottle);
            final Properties var8 = new Properties();
            var8.put("type", "item");
            var8.put("items", new StringBuilder().append(potionItemId).toString());
            return var8;
        }
        final int potionItemId = Item.getIdFromItem(Items.potionitem);
        final int[] damages = getMapPotionIds().get(name);
        if (damages == null) {
            Config.warn("Potion not found for image: " + path);
            return null;
        }
        final StringBuffer bufDamage = new StringBuffer();
        for (int damageMask = 0; damageMask < damages.length; ++damageMask) {
            int props = damages[damageMask];
            if (splash) {
                props |= 0x4000;
            }
            if (damageMask > 0) {
                bufDamage.append(" ");
            }
            bufDamage.append(props);
        }
        final short var9 = 16447;
        final Properties var10 = new Properties();
        var10.put("type", "item");
        var10.put("items", new StringBuilder().append(potionItemId).toString());
        var10.put("damage", new StringBuilder().append(bufDamage.toString()).toString());
        var10.put("damageMask", new StringBuilder().append(var9).toString());
        if (splash) {
            var10.put("texture.potion_bottle_splash", name);
        }
        else {
            var10.put("texture.potion_bottle_drinkable", name);
        }
        return var10;
    }
    
    private static Map getMapPotionIds() {
        if (CustomItems.mapPotionIds == null) {
            (CustomItems.mapPotionIds = new LinkedHashMap()).put("water", new int[1]);
            CustomItems.mapPotionIds.put("awkward", new int[] { 16 });
            CustomItems.mapPotionIds.put("thick", new int[] { 32 });
            CustomItems.mapPotionIds.put("potent", new int[] { 48 });
            CustomItems.mapPotionIds.put("regeneration", getPotionIds(1));
            CustomItems.mapPotionIds.put("moveSpeed", getPotionIds(2));
            CustomItems.mapPotionIds.put("fireResistance", getPotionIds(3));
            CustomItems.mapPotionIds.put("poison", getPotionIds(4));
            CustomItems.mapPotionIds.put("heal", getPotionIds(5));
            CustomItems.mapPotionIds.put("nightVision", getPotionIds(6));
            CustomItems.mapPotionIds.put("clear", getPotionIds(7));
            CustomItems.mapPotionIds.put("bungling", getPotionIds(23));
            CustomItems.mapPotionIds.put("charming", getPotionIds(39));
            CustomItems.mapPotionIds.put("rank", getPotionIds(55));
            CustomItems.mapPotionIds.put("weakness", getPotionIds(8));
            CustomItems.mapPotionIds.put("damageBoost", getPotionIds(9));
            CustomItems.mapPotionIds.put("moveSlowdown", getPotionIds(10));
            CustomItems.mapPotionIds.put("diffuse", getPotionIds(11));
            CustomItems.mapPotionIds.put("smooth", getPotionIds(27));
            CustomItems.mapPotionIds.put("refined", getPotionIds(43));
            CustomItems.mapPotionIds.put("acrid", getPotionIds(59));
            CustomItems.mapPotionIds.put("harm", getPotionIds(12));
            CustomItems.mapPotionIds.put("waterBreathing", getPotionIds(13));
            CustomItems.mapPotionIds.put("invisibility", getPotionIds(14));
            CustomItems.mapPotionIds.put("thin", getPotionIds(15));
            CustomItems.mapPotionIds.put("debonair", getPotionIds(31));
            CustomItems.mapPotionIds.put("sparkling", getPotionIds(47));
            CustomItems.mapPotionIds.put("stinky", getPotionIds(63));
        }
        return CustomItems.mapPotionIds;
    }
    
    private static int[] getPotionIds(final int baseId) {
        return new int[] { baseId, baseId + 16, baseId + 32, baseId + 48 };
    }
    
    private static int getPotionNameDamage(final String name) {
        final String fullName = "potion." + name;
        final Potion[] effectPotions = Potion.potionTypes;
        for (int i = 0; i < effectPotions.length; ++i) {
            final Potion potion = effectPotions[i];
            if (potion != null) {
                final String potionName = potion.getName();
                if (fullName.equals(potionName)) {
                    return potion.getId();
                }
            }
        }
        return -1;
    }
    
    private static List makePropertyList(final CustomItemProperties[][] propsArr) {
        final ArrayList list = new ArrayList();
        if (propsArr != null) {
            for (int i = 0; i < propsArr.length; ++i) {
                final CustomItemProperties[] props = propsArr[i];
                ArrayList propList = null;
                if (props != null) {
                    propList = new ArrayList((Collection<? extends E>)Arrays.asList(props));
                }
                list.add(propList);
            }
        }
        return list;
    }
    
    private static CustomItemProperties[][] propertyListToArray(final List list) {
        final CustomItemProperties[][] propArr = new CustomItemProperties[list.size()][];
        for (int i = 0; i < list.size(); ++i) {
            final List subList = list.get(i);
            if (subList != null) {
                final CustomItemProperties[] subArr = subList.toArray(new CustomItemProperties[subList.size()]);
                Arrays.sort(subArr, new CustomItemsComparator());
                propArr[i] = subArr;
            }
        }
        return propArr;
    }
    
    private static void addToItemList(final CustomItemProperties cp, final List itemList) {
        if (cp.items != null) {
            for (int i = 0; i < cp.items.length; ++i) {
                final int itemId = cp.items[i];
                if (itemId <= 0) {
                    Config.warn("Invalid item ID: " + itemId);
                }
                else {
                    addToList(cp, itemList, itemId);
                }
            }
        }
    }
    
    private static void addToEnchantmentList(final CustomItemProperties cp, final List enchantmentList) {
        if (cp.type == 2 && cp.enchantmentIds != null) {
            for (int i = 0; i < 256; ++i) {
                if (cp.enchantmentIds.isInRange(i)) {
                    addToList(cp, enchantmentList, i);
                }
            }
        }
    }
    
    private static void addToList(final CustomItemProperties cp, final List list, final int id) {
        while (id >= list.size()) {
            list.add(null);
        }
        Object subList = list.get(id);
        if (subList == null) {
            subList = new ArrayList();
            list.set(id, subList);
        }
        ((List)subList).add(cp);
    }
    
    public static IBakedModel getCustomItemModel(final ItemStack itemStack, final IBakedModel model, final ModelResourceLocation modelLocation) {
        if (model.isAmbientOcclusionEnabled()) {
            return model;
        }
        if (CustomItems.itemProperties == null) {
            return model;
        }
        final CustomItemProperties props = getCustomItemProperties(itemStack, 1);
        return (props == null) ? model : props.getModel(modelLocation);
    }
    
    public static boolean bindCustomArmorTexture(final ItemStack itemStack, final int layer, final String overlay) {
        if (CustomItems.itemProperties == null) {
            return false;
        }
        final ResourceLocation loc = getCustomArmorLocation(itemStack, layer, overlay);
        if (loc == null) {
            return false;
        }
        Config.getTextureManager().bindTexture(loc);
        return true;
    }
    
    private static ResourceLocation getCustomArmorLocation(final ItemStack itemStack, final int layer, final String overlay) {
        final CustomItemProperties props = getCustomItemProperties(itemStack, 3);
        if (props == null) {
            return null;
        }
        if (props.mapTextureLocations == null) {
            return null;
        }
        final Item item = itemStack.getItem();
        if (!(item instanceof ItemArmor)) {
            return null;
        }
        final ItemArmor itemArmor = (ItemArmor)item;
        final String material = itemArmor.getArmorMaterial().func_179242_c();
        final StringBuffer sb = new StringBuffer();
        sb.append("texture.");
        sb.append(material);
        sb.append("_layer_");
        sb.append(layer);
        if (overlay != null) {
            sb.append("_");
            sb.append(overlay);
        }
        final String key = sb.toString();
        final ResourceLocation loc = props.mapTextureLocations.get(key);
        return loc;
    }
    
    private static CustomItemProperties getCustomItemProperties(final ItemStack itemStack, final int type) {
        if (CustomItems.itemProperties == null) {
            return null;
        }
        if (itemStack == null) {
            return null;
        }
        final Item item = itemStack.getItem();
        final int itemId = Item.getIdFromItem(item);
        if (itemId >= 0 && itemId < CustomItems.itemProperties.length) {
            final CustomItemProperties[] cips = CustomItems.itemProperties[itemId];
            if (cips != null) {
                for (int i = 0; i < cips.length; ++i) {
                    final CustomItemProperties cip = cips[i];
                    if (cip.type == type && matchesProperties(cip, itemStack, null)) {
                        return cip;
                    }
                }
            }
        }
        return null;
    }
    
    private static boolean matchesProperties(final CustomItemProperties cip, final ItemStack itemStack, final int[][] enchantmentIdLevels) {
        final Item item = itemStack.getItem();
        if (cip.damage != null) {
            int idLevels = itemStack.getItemDamage();
            if (cip.damageMask != 0) {
                idLevels &= cip.damageMask;
            }
            if (cip.damagePercent) {
                final int nbt = item.getMaxDamage();
                idLevels = (int)(idLevels * 100 / (double)nbt);
            }
            if (!cip.damage.isInRange(idLevels)) {
                return false;
            }
        }
        if (cip.stackSize != null && !cip.stackSize.isInRange(itemStack.stackSize)) {
            return false;
        }
        int[][] var8 = enchantmentIdLevels;
        if (cip.enchantmentIds != null) {
            if (enchantmentIdLevels == null) {
                var8 = getEnchantmentIdLevels(itemStack);
            }
            boolean var9 = false;
            for (int i = 0; i < var8.length; ++i) {
                final int ntv = var8[i][0];
                if (cip.enchantmentIds.isInRange(ntv)) {
                    var9 = true;
                    break;
                }
            }
            if (!var9) {
                return false;
            }
        }
        if (cip.enchantmentLevels != null) {
            if (var8 == null) {
                var8 = getEnchantmentIdLevels(itemStack);
            }
            boolean var9 = false;
            for (int i = 0; i < var8.length; ++i) {
                final int ntv = var8[i][1];
                if (cip.enchantmentLevels.isInRange(ntv)) {
                    var9 = true;
                    break;
                }
            }
            if (!var9) {
                return false;
            }
        }
        if (cip.nbtTagValues != null) {
            final NBTTagCompound var10 = itemStack.getTagCompound();
            for (int i = 0; i < cip.nbtTagValues.length; ++i) {
                final NbtTagValue var11 = cip.nbtTagValues[i];
                if (!var11.matches(var10)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static int[][] getEnchantmentIdLevels(final ItemStack itemStack) {
        final Item item = itemStack.getItem();
        final NBTTagList nbt = (item == Items.enchanted_book) ? Items.enchanted_book.func_92110_g(itemStack) : itemStack.getEnchantmentTagList();
        if (nbt != null && nbt.tagCount() > 0) {
            final int[][] arr = new int[nbt.tagCount()][2];
            for (int i = 0; i < nbt.tagCount(); ++i) {
                final NBTTagCompound tag = nbt.getCompoundTagAt(i);
                final short id = tag.getShort("id");
                final short lvl = tag.getShort("lvl");
                arr[i][0] = id;
                arr[i][1] = lvl;
            }
            return arr;
        }
        return CustomItems.EMPTY_INT2_ARRAY;
    }
    
    public static boolean renderCustomEffect(final RenderItem renderItem, final ItemStack itemStack, final IBakedModel model) {
        if (CustomItems.enchantmentProperties == null) {
            return false;
        }
        if (itemStack == null) {
            return false;
        }
        final int[][] idLevels = getEnchantmentIdLevels(itemStack);
        if (idLevels.length <= 0) {
            return false;
        }
        HashSet layersRendered = null;
        boolean rendered = false;
        final TextureManager textureManager = Config.getTextureManager();
        for (int i = 0; i < idLevels.length; ++i) {
            final int id = idLevels[i][0];
            if (id >= 0 && id < CustomItems.enchantmentProperties.length) {
                final CustomItemProperties[] cips = CustomItems.enchantmentProperties[id];
                if (cips != null) {
                    for (int p = 0; p < cips.length; ++p) {
                        final CustomItemProperties cip = cips[p];
                        if (layersRendered == null) {
                            layersRendered = new HashSet();
                        }
                        if (layersRendered.add(id) && matchesProperties(cip, itemStack, idLevels) && cip.textureLocation != null) {
                            textureManager.bindTexture(cip.textureLocation);
                            final float width = cip.getTextureWidth(textureManager);
                            if (!rendered) {
                                rendered = true;
                                GlStateManager.depthMask(false);
                                GlStateManager.depthFunc(514);
                                GlStateManager.disableLighting();
                                GlStateManager.matrixMode(5890);
                            }
                            Blender.setupBlend(cip.blend, 1.0f);
                            GlStateManager.pushMatrix();
                            GlStateManager.scale(width / 2.0f, width / 2.0f, width / 2.0f);
                            final float offset = cip.speed * (Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
                            GlStateManager.translate(offset, 0.0f, 0.0f);
                            GlStateManager.rotate(cip.rotation, 0.0f, 0.0f, 1.0f);
                            renderItem.func_175035_a(model, -1);
                            GlStateManager.popMatrix();
                        }
                    }
                }
            }
        }
        if (rendered) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
            textureManager.bindTexture(TextureMap.locationBlocksTexture);
        }
        return rendered;
    }
    
    public static boolean renderCustomArmorEffect(final EntityLivingBase entity, final ItemStack itemStack, final ModelBase model, final float limbSwing, final float prevLimbSwing, final float partialTicks, final float timeLimbSwing, final float yaw, final float pitch, final float scale) {
        if (CustomItems.enchantmentProperties == null) {
            return false;
        }
        if (Config.isShaders() && Shaders.isShadowPass) {
            return false;
        }
        if (itemStack == null) {
            return false;
        }
        final int[][] idLevels = getEnchantmentIdLevels(itemStack);
        if (idLevels.length <= 0) {
            return false;
        }
        HashSet layersRendered = null;
        boolean rendered = false;
        final TextureManager textureManager = Config.getTextureManager();
        for (int i = 0; i < idLevels.length; ++i) {
            final int id = idLevels[i][0];
            if (id >= 0 && id < CustomItems.enchantmentProperties.length) {
                final CustomItemProperties[] cips = CustomItems.enchantmentProperties[id];
                if (cips != null) {
                    for (int p = 0; p < cips.length; ++p) {
                        final CustomItemProperties cip = cips[p];
                        if (layersRendered == null) {
                            layersRendered = new HashSet();
                        }
                        if (layersRendered.add(id) && matchesProperties(cip, itemStack, idLevels) && cip.textureLocation != null) {
                            textureManager.bindTexture(cip.textureLocation);
                            final float width = cip.getTextureWidth(textureManager);
                            if (!rendered) {
                                rendered = true;
                                if (Config.isShaders()) {
                                    ShadersRender.layerArmorBaseDrawEnchantedGlintBegin();
                                }
                                GlStateManager.enableBlend();
                                GlStateManager.depthFunc(514);
                                GlStateManager.depthMask(false);
                            }
                            Blender.setupBlend(cip.blend, 1.0f);
                            GlStateManager.disableLighting();
                            GlStateManager.matrixMode(5890);
                            GlStateManager.loadIdentity();
                            GlStateManager.rotate(cip.rotation, 0.0f, 0.0f, 1.0f);
                            final float texScale = width / 8.0f;
                            GlStateManager.scale(texScale, texScale / 2.0f, texScale);
                            final float offset = cip.speed * (Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
                            GlStateManager.translate(0.0f, offset, 0.0f);
                            GlStateManager.matrixMode(5888);
                            model.render(entity, limbSwing, prevLimbSwing, timeLimbSwing, yaw, pitch, scale);
                        }
                    }
                }
            }
        }
        if (rendered) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
            if (Config.isShaders()) {
                ShadersRender.layerArmorBaseDrawEnchantedGlintEnd();
            }
        }
        return rendered;
    }
    
    public static boolean isUseGlint() {
        return CustomItems.useGlint;
    }
}
