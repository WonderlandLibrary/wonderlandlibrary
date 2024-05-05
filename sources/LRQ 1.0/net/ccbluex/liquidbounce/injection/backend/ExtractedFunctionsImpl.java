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
    private static Field fastRenderField;
    public static final ExtractedFunctionsImpl INSTANCE;

    @Override
    public IBlock getBlockById(int id) {
        BlockImpl blockImpl;
        Block block = Block.func_149729_e((int)id);
        if (block != null) {
            Block block2 = block;
            boolean bl = false;
            boolean bl2 = false;
            Block p1 = block2;
            boolean bl3 = false;
            blockImpl = new BlockImpl(p1);
        } else {
            blockImpl = null;
        }
        return blockImpl;
    }

    @Override
    public int getIdFromBlock(IBlock block) {
        IBlock $this$unwrap$iv = block;
        boolean $i$f$unwrap = false;
        return Block.func_149682_b((Block)((BlockImpl)$this$unwrap$iv).getWrapped());
    }

    @Override
    public float getModifierForCreature(@Nullable IItemStack heldItem, IEnumCreatureAttribute creatureAttribute) {
        ItemStack itemStack;
        boolean $i$f$unwrap;
        Object $this$unwrap$iv;
        IItemStack iItemStack = heldItem;
        if (iItemStack != null) {
            $this$unwrap$iv = iItemStack;
            $i$f$unwrap = false;
            IItemStack iItemStack2 = $this$unwrap$iv;
            if (iItemStack2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.ItemStackImpl");
            }
            itemStack = ((ItemStackImpl)iItemStack2).getWrapped();
        } else {
            itemStack = null;
        }
        $this$unwrap$iv = creatureAttribute;
        ItemStack itemStack2 = itemStack;
        $i$f$unwrap = false;
        EnumCreatureAttribute enumCreatureAttribute = ((EnumCreatureAttributeImpl)$this$unwrap$iv).getWrapped();
        return EnchantmentHelper.func_152377_a((ItemStack)itemStack2, (EnumCreatureAttribute)enumCreatureAttribute);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public IItem getObjectFromItemRegistry(IResourceLocation res) {
        IItem iItem;
        void $this$unwrap$iv;
        IResourceLocation iResourceLocation = res;
        RegistryNamespaced registryNamespaced = Item.field_150901_e;
        boolean $i$f$unwrap = false;
        ResourceLocation resourceLocation = ((ResourceLocationImpl)$this$unwrap$iv).getWrapped();
        Item item = (Item)registryNamespaced.func_82594_a((Object)resourceLocation);
        if (item != null) {
            Item $this$wrap$iv = item;
            boolean $i$f$wrap = false;
            iItem = new ItemImpl<Item>($this$wrap$iv);
        } else {
            iItem = null;
        }
        return iItem;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void renderTileEntity(ITileEntity tileEntity, float partialTicks, int destroyStage) {
        void $this$unwrap$iv;
        ITileEntity iTileEntity = tileEntity;
        TileEntityRendererDispatcher tileEntityRendererDispatcher = TileEntityRendererDispatcher.field_147556_a;
        boolean $i$f$unwrap = false;
        TileEntity tileEntity2 = ((TileEntityImpl)$this$unwrap$iv).getWrapped();
        tileEntityRendererDispatcher.func_180546_a(tileEntity2, partialTicks, destroyStage);
    }

    @Override
    public IBlock getBlockFromName(String name) {
        IBlock iBlock;
        Block block = Block.func_149684_b((String)name);
        if (block != null) {
            Block $this$wrap$iv = block;
            boolean $i$f$wrap = false;
            iBlock = new BlockImpl($this$wrap$iv);
        } else {
            iBlock = null;
        }
        return iBlock;
    }

    @Override
    public IItem getItemByName(String name) {
        Object object = Items.class.getField(name).get(null);
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.Item");
        }
        Item $this$wrap$iv = (Item)object;
        boolean $i$f$wrap = false;
        return new ItemImpl<Item>($this$wrap$iv);
    }

    @Override
    public IEnchantment getEnchantmentByLocation(String location) {
        IEnchantment iEnchantment;
        Enchantment enchantment = Enchantment.func_180305_b((String)location);
        if (enchantment != null) {
            Enchantment $this$wrap$iv = enchantment;
            boolean $i$f$wrap = false;
            iEnchantment = new EnchantmentImpl($this$wrap$iv);
        } else {
            iEnchantment = null;
        }
        return iEnchantment;
    }

    @Override
    public IEnchantment getEnchantmentById(int enchantID) {
        IEnchantment iEnchantment;
        Enchantment enchantment = Enchantment.func_185262_c((int)enchantID);
        if (enchantment != null) {
            Enchantment $this$wrap$iv = enchantment;
            boolean $i$f$wrap = false;
            iEnchantment = new EnchantmentImpl($this$wrap$iv);
        } else {
            iEnchantment = null;
        }
        return iEnchantment;
    }

    @Override
    public Collection<IResourceLocation> getEnchantments() {
        return new WrappedCollection(Enchantment.field_185264_b.func_148742_b(), getEnchantments.1.INSTANCE, getEnchantments.2.INSTANCE);
    }

    @Override
    public Collection<IResourceLocation> getItemRegistryKeys() {
        return new WrappedCollection(Item.field_150901_e.func_148742_b(), getItemRegistryKeys.1.INSTANCE, getItemRegistryKeys.2.INSTANCE);
    }

    @Override
    public Collection<IResourceLocation> getBlockRegistryKeys() {
        return new WrappedCollection(Block.field_149771_c.func_148742_b(), getBlockRegistryKeys.1.INSTANCE, getBlockRegistryKeys.2.INSTANCE);
    }

    @Override
    public void disableStandardItemLighting() {
        RenderHelper.func_74518_a();
    }

    @Override
    public String formatI18n(String key, String ... values) {
        return I18n.func_135052_a((String)key, (Object[])new Object[]{values});
    }

    @Override
    public void sessionServiceJoinServer(GameProfile profile, String token, String sessionHash) {
        Minecraft.func_71410_x().func_152347_ac().joinServer(profile, token, sessionHash);
    }

    @Override
    public IPotion getPotionById(int potionID) {
        Potion potion = Potion.func_188412_a((int)potionID);
        if (potion == null) {
            Intrinsics.throwNpe();
        }
        Potion $this$wrap$iv = potion;
        boolean $i$f$wrap = false;
        return new PotionImpl($this$wrap$iv);
    }

    @Override
    public void enableStandardItemLighting() {
        RenderHelper.func_74519_b();
    }

    @Override
    public String scoreboardFormatPlayerName(@Nullable ITeam scorePlayerTeam, String playerName) {
        Team team;
        ITeam iTeam = scorePlayerTeam;
        if (iTeam != null) {
            ITeam $this$unwrap$iv = iTeam;
            boolean $i$f$unwrap = false;
            ITeam iTeam2 = $this$unwrap$iv;
            if (iTeam2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.TeamImpl");
            }
            team = ((TeamImpl)iTeam2).getWrapped();
        } else {
            team = null;
        }
        return ScorePlayerTeam.func_96667_a(team, (String)playerName);
    }

    @Override
    public void disableFastRender() {
        try {
            Field fastRenderer = fastRenderField;
            if (fastRenderer != null) {
                if (!fastRenderer.isAccessible()) {
                    fastRenderer.setAccessible(true);
                }
                fastRenderer.setBoolean(Minecraft.func_71410_x().field_71474_y, false);
            }
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
    }

    @Override
    public IIChatComponent jsonToComponent(String toString) {
        ITextComponent iTextComponent = ITextComponent.Serializer.func_150699_a((String)toString);
        if (iTextComponent == null) {
            Intrinsics.throwNpe();
        }
        ITextComponent $this$wrap$iv = iTextComponent;
        boolean $i$f$wrap = false;
        return new IChatComponentImpl($this$wrap$iv);
    }

    @Override
    public void setActiveTextureLightMapTexUnit() {
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
    }

    @Override
    public void setActiveTextureDefaultTexUnit() {
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
    }

    @Override
    public IItem getItemById(int id) {
        Item $this$wrap$iv = Item.func_150899_d((int)id);
        boolean $i$f$wrap = false;
        return new ItemImpl<Item>($this$wrap$iv);
    }

    @Override
    public int getIdFromItem(IItem sb) {
        IItem $this$unwrap$iv = sb;
        boolean $i$f$unwrap = false;
        return Item.func_150891_b(((ItemImpl)$this$unwrap$iv).getWrapped());
    }

    @Override
    public boolean canAddItemToSlot(ISlot slotIn, IItemStack stack, boolean stackSizeMatters) {
        Object $this$unwrap$iv = slotIn;
        boolean $i$f$unwrap = false;
        Slot slot = ((SlotImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = stack;
        Slot slot2 = slot;
        $i$f$unwrap = false;
        ItemStack itemStack = ((ItemStackImpl)$this$unwrap$iv).getWrapped();
        return Container.func_94527_a((Slot)slot2, (ItemStack)itemStack, (boolean)stackSizeMatters);
    }

    private ExtractedFunctionsImpl() {
    }

    static {
        ExtractedFunctionsImpl extractedFunctionsImpl;
        INSTANCE = extractedFunctionsImpl = new ExtractedFunctionsImpl();
        try {
            Field declaredField;
            fastRenderField = declaredField = GameSettings.class.getDeclaredField("ofFastRender");
            if (!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
            }
        }
        catch (NoSuchFieldException noSuchFieldException) {
            // empty catch block
        }
    }
}

