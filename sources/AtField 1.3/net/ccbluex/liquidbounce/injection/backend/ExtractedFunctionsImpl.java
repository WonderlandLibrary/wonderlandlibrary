/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.EnumCreatureAttribute
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.Potion
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.registry.RegistryNamespaced
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.ITextComponent$Serializer
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import com.mojang.authlib.GameProfile;
import java.lang.reflect.Field;
import java.util.Collection;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.ccbluex.liquidbounce.api.minecraft.entity.IEnumCreatureAttribute;
import net.ccbluex.liquidbounce.api.minecraft.inventory.ISlot;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.api.minecraft.tileentity.ITileEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import net.ccbluex.liquidbounce.injection.backend.BlockImpl;
import net.ccbluex.liquidbounce.injection.backend.EnchantmentImpl;
import net.ccbluex.liquidbounce.injection.backend.EnumCreatureAttributeImpl;
import net.ccbluex.liquidbounce.injection.backend.ExtractedFunctionsImpl;
import net.ccbluex.liquidbounce.injection.backend.IChatComponentImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.ccbluex.liquidbounce.injection.backend.PotionImpl;
import net.ccbluex.liquidbounce.injection.backend.ResourceLocationImpl;
import net.ccbluex.liquidbounce.injection.backend.SlotImpl;
import net.ccbluex.liquidbounce.injection.backend.TeamImpl;
import net.ccbluex.liquidbounce.injection.backend.TileEntityImpl;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.Nullable;

public final class ExtractedFunctionsImpl
implements IExtractedFunctions {
    public static final ExtractedFunctionsImpl INSTANCE;
    private static Field fastRenderField;

    @Override
    public String scoreboardFormatPlayerName(@Nullable ITeam iTeam, String string) {
        Team team;
        ITeam iTeam2 = iTeam;
        if (iTeam2 != null) {
            ITeam iTeam3 = iTeam2;
            boolean bl = false;
            ITeam iTeam4 = iTeam3;
            if (iTeam4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.TeamImpl");
            }
            team = ((TeamImpl)iTeam4).getWrapped();
        } else {
            team = null;
        }
        return ScorePlayerTeam.func_96667_a(team, (String)string);
    }

    @Override
    public IBlock getBlockFromName(String string) {
        IBlock iBlock;
        Block block = Block.func_149684_b((String)string);
        if (block != null) {
            Block block2 = block;
            boolean bl = false;
            iBlock = new BlockImpl(block2);
        } else {
            iBlock = null;
        }
        return iBlock;
    }

    @Override
    public IItem getItemByName(String string) {
        Object object = Items.class.getField(string).get(null);
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.Item");
        }
        Item item = (Item)object;
        boolean bl = false;
        return new ItemImpl(item);
    }

    @Override
    public float getModifierForCreature(@Nullable IItemStack iItemStack, IEnumCreatureAttribute iEnumCreatureAttribute) {
        ItemStack itemStack;
        boolean bl;
        Object object;
        IItemStack iItemStack2 = iItemStack;
        if (iItemStack2 != null) {
            object = iItemStack2;
            bl = false;
            IItemStack iItemStack3 = object;
            if (iItemStack3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.ItemStackImpl");
            }
            itemStack = ((ItemStackImpl)iItemStack3).getWrapped();
        } else {
            itemStack = null;
        }
        object = iEnumCreatureAttribute;
        ItemStack itemStack2 = itemStack;
        bl = false;
        EnumCreatureAttribute enumCreatureAttribute = ((EnumCreatureAttributeImpl)object).getWrapped();
        return EnchantmentHelper.func_152377_a((ItemStack)itemStack2, (EnumCreatureAttribute)enumCreatureAttribute);
    }

    @Override
    public Collection getItemRegistryKeys() {
        return new WrappedCollection(Item.field_150901_e.func_148742_b(), getItemRegistryKeys.1.INSTANCE, getItemRegistryKeys.2.INSTANCE);
    }

    @Override
    public String formatI18n(String string, String ... stringArray) {
        return I18n.func_135052_a((String)string, (Object[])new Object[]{stringArray});
    }

    @Override
    public IPotion getPotionById(int n) {
        Potion potion = Potion.func_188412_a((int)n);
        if (potion == null) {
            Intrinsics.throwNpe();
        }
        Potion potion2 = potion;
        boolean bl = false;
        return new PotionImpl(potion2);
    }

    @Override
    public IIChatComponent jsonToComponent(String string) {
        ITextComponent iTextComponent = ITextComponent.Serializer.func_150699_a((String)string);
        if (iTextComponent == null) {
            Intrinsics.throwNpe();
        }
        ITextComponent iTextComponent2 = iTextComponent;
        boolean bl = false;
        return new IChatComponentImpl(iTextComponent2);
    }

    @Override
    public void setActiveTextureDefaultTexUnit() {
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
    }

    @Override
    public void renderTileEntity(ITileEntity iTileEntity, float f, int n) {
        ITileEntity iTileEntity2 = iTileEntity;
        TileEntityRendererDispatcher tileEntityRendererDispatcher = TileEntityRendererDispatcher.field_147556_a;
        boolean bl = false;
        TileEntity tileEntity = ((TileEntityImpl)iTileEntity2).getWrapped();
        tileEntityRendererDispatcher.func_180546_a(tileEntity, f, n);
    }

    private ExtractedFunctionsImpl() {
    }

    @Override
    public IItem getObjectFromItemRegistry(IResourceLocation iResourceLocation) {
        IItem iItem;
        IResourceLocation iResourceLocation2 = iResourceLocation;
        RegistryNamespaced registryNamespaced = Item.field_150901_e;
        boolean bl = false;
        ResourceLocation resourceLocation = ((ResourceLocationImpl)iResourceLocation2).getWrapped();
        Item item = (Item)registryNamespaced.func_82594_a((Object)resourceLocation);
        if (item != null) {
            iResourceLocation2 = item;
            bl = false;
            iItem = new ItemImpl((Item)iResourceLocation2);
        } else {
            iItem = null;
        }
        return iItem;
    }

    @Override
    public void enableStandardItemLighting() {
        RenderHelper.func_74519_b();
    }

    @Override
    public void sessionServiceJoinServer(GameProfile gameProfile, String string, String string2) {
        Minecraft.func_71410_x().func_152347_ac().joinServer(gameProfile, string, string2);
    }

    @Override
    public void disableStandardItemLighting() {
        RenderHelper.func_74518_a();
    }

    @Override
    public void setActiveTextureLightMapTexUnit() {
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
    }

    @Override
    public int getIdFromItem(IItem iItem) {
        IItem iItem2 = iItem;
        boolean bl = false;
        return Item.func_150891_b((Item)((ItemImpl)iItem2).getWrapped());
    }

    static {
        ExtractedFunctionsImpl extractedFunctionsImpl;
        INSTANCE = extractedFunctionsImpl = new ExtractedFunctionsImpl();
        try {
            Field field;
            fastRenderField = field = GameSettings.class.getDeclaredField("ofFastRender");
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
        }
        catch (NoSuchFieldException noSuchFieldException) {
            // empty catch block
        }
    }

    @Override
    public boolean canAddItemToSlot(ISlot iSlot, IItemStack iItemStack, boolean bl) {
        Object object = iSlot;
        boolean bl2 = false;
        Slot slot = ((SlotImpl)object).getWrapped();
        object = iItemStack;
        Slot slot2 = slot;
        bl2 = false;
        ItemStack itemStack = ((ItemStackImpl)object).getWrapped();
        return Container.func_94527_a((Slot)slot2, (ItemStack)itemStack, (boolean)bl);
    }

    @Override
    public Collection getEnchantments() {
        return new WrappedCollection(Enchantment.field_185264_b.func_148742_b(), getEnchantments.1.INSTANCE, getEnchantments.2.INSTANCE);
    }

    @Override
    public int getIdFromBlock(IBlock iBlock) {
        IBlock iBlock2 = iBlock;
        boolean bl = false;
        return Block.func_149682_b((Block)((BlockImpl)iBlock2).getWrapped());
    }

    @Override
    public IBlock getBlockById(int n) {
        BlockImpl blockImpl;
        Block block = Block.func_149729_e((int)n);
        if (block != null) {
            Block block2 = block;
            boolean bl = false;
            boolean bl2 = false;
            Block block3 = block2;
            boolean bl3 = false;
            blockImpl = new BlockImpl(block3);
        } else {
            blockImpl = null;
        }
        return blockImpl;
    }

    @Override
    public IEnchantment getEnchantmentById(int n) {
        IEnchantment iEnchantment;
        Enchantment enchantment = Enchantment.func_185262_c((int)n);
        if (enchantment != null) {
            Enchantment enchantment2 = enchantment;
            boolean bl = false;
            iEnchantment = new EnchantmentImpl(enchantment2);
        } else {
            iEnchantment = null;
        }
        return iEnchantment;
    }

    @Override
    public IEnchantment getEnchantmentByLocation(String string) {
        IEnchantment iEnchantment;
        Enchantment enchantment = Enchantment.func_180305_b((String)string);
        if (enchantment != null) {
            Enchantment enchantment2 = enchantment;
            boolean bl = false;
            iEnchantment = new EnchantmentImpl(enchantment2);
        } else {
            iEnchantment = null;
        }
        return iEnchantment;
    }

    @Override
    public IItem getItemById(int n) {
        Item item = Item.func_150899_d((int)n);
        boolean bl = false;
        return new ItemImpl(item);
    }

    @Override
    public Collection getBlockRegistryKeys() {
        return new WrappedCollection(Block.field_149771_c.func_148742_b(), getBlockRegistryKeys.1.INSTANCE, getBlockRegistryKeys.2.INSTANCE);
    }

    @Override
    public void disableFastRender() {
        try {
            Field field = fastRenderField;
            if (field != null) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.setBoolean(Minecraft.func_71410_x().field_71474_y, false);
            }
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
    }
}

