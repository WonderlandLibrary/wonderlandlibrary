/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  kotlin.Metadata
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
 *  net.minecraft.util.registry.RegistryNamespacedDefaultedByKey
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.ITextComponent$Serializer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import com.mojang.authlib.GameProfile;
import java.lang.reflect.Field;
import java.util.Collection;
import kotlin.Metadata;
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
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u009e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006H\u0016J\b\u0010\f\u001a\u00020\rH\u0016J\b\u0010\u000e\u001a\u00020\rH\u0016J\b\u0010\u000f\u001a\u00020\rH\u0016J)\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0012\u0010\u0013\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00110\u0014\"\u00020\u0011H\u0016\u00a2\u0006\u0002\u0010\u0015J\u0012\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0012\u0010\u001a\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u001b\u001a\u00020\u0011H\u0016J\u000e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dH\u0016J\u0012\u0010\u001f\u001a\u0004\u0018\u00010 2\u0006\u0010!\u001a\u00020\u0019H\u0016J\u0012\u0010\"\u001a\u0004\u0018\u00010 2\u0006\u0010#\u001a\u00020\u0011H\u0016J\u000e\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dH\u0016J\u0010\u0010%\u001a\u00020\u00192\u0006\u0010&\u001a\u00020\u0017H\u0016J\u0010\u0010'\u001a\u00020\u00192\u0006\u0010(\u001a\u00020)H\u0016J\u0012\u0010*\u001a\u0004\u0018\u00010)2\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0012\u0010+\u001a\u0004\u0018\u00010)2\u0006\u0010\u001b\u001a\u00020\u0011H\u0016J\u000e\u0010,\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dH\u0016J\u001a\u0010-\u001a\u00020.2\b\u0010/\u001a\u0004\u0018\u00010\n2\u0006\u00100\u001a\u000201H\u0016J\u0012\u00102\u001a\u0004\u0018\u00010)2\u0006\u00103\u001a\u00020\u001eH\u0016J\u0010\u00104\u001a\u0002052\u0006\u00106\u001a\u00020\u0019H\u0016J\u0010\u00107\u001a\u0002082\u0006\u00109\u001a\u00020\u0011H\u0016J \u0010:\u001a\u00020\r2\u0006\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020.2\u0006\u0010>\u001a\u00020\u0019H\u0016J\u001a\u0010?\u001a\u00020\u00112\b\u0010@\u001a\u0004\u0018\u00010A2\u0006\u0010B\u001a\u00020\u0011H\u0016J \u0010C\u001a\u00020\r2\u0006\u0010D\u001a\u00020E2\u0006\u0010F\u001a\u00020\u00112\u0006\u0010G\u001a\u00020\u0011H\u0016J\b\u0010H\u001a\u00020\rH\u0016J\b\u0010I\u001a\u00020\rH\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006J"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/ExtractedFunctionsImpl;", "Lnet/ccbluex/liquidbounce/api/IExtractedFunctions;", "()V", "fastRenderField", "Ljava/lang/reflect/Field;", "canAddItemToSlot", "", "slotIn", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/ISlot;", "stack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "stackSizeMatters", "disableFastRender", "", "disableStandardItemLighting", "enableStandardItemLighting", "formatI18n", "", "key", "values", "", "(Ljava/lang/String;[Ljava/lang/String;)Ljava/lang/String;", "getBlockById", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "id", "", "getBlockFromName", "name", "getBlockRegistryKeys", "", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "getEnchantmentById", "Lnet/ccbluex/liquidbounce/api/minecraft/enchantments/IEnchantment;", "enchantID", "getEnchantmentByLocation", "location", "getEnchantments", "getIdFromBlock", "block", "getIdFromItem", "sb", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "getItemById", "getItemByName", "getItemRegistryKeys", "getModifierForCreature", "", "heldItem", "creatureAttribute", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/IEnumCreatureAttribute;", "getObjectFromItemRegistry", "res", "getPotionById", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotion;", "potionID", "jsonToComponent", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "toString", "renderTileEntity", "tileEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/tileentity/ITileEntity;", "partialTicks", "destroyStage", "scoreboardFormatPlayerName", "scorePlayerTeam", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "playerName", "sessionServiceJoinServer", "profile", "Lcom/mojang/authlib/GameProfile;", "token", "sessionHash", "setActiveTextureDefaultTexUnit", "setActiveTextureLightMapTexUnit", "LiKingSense"})
public final class ExtractedFunctionsImpl
implements IExtractedFunctions {
    private static Field fastRenderField;
    public static final ExtractedFunctionsImpl INSTANCE;

    @Override
    @Nullable
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
    public int getIdFromBlock(@NotNull IBlock block) {
        Intrinsics.checkParameterIsNotNull((Object)block, (String)"block");
        IBlock $this$unwrap$iv = block;
        boolean $i$f$unwrap = false;
        return Block.func_149682_b((Block)((BlockImpl)$this$unwrap$iv).getWrapped());
    }

    @Override
    public float getModifierForCreature(@Nullable IItemStack heldItem, @NotNull IEnumCreatureAttribute creatureAttribute) {
        ItemStack itemStack;
        boolean $i$f$unwrap;
        Object $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)creatureAttribute, (String)"creatureAttribute");
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
    @Nullable
    public IItem getObjectFromItemRegistry(@NotNull IResourceLocation res) {
        IItem iItem;
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)res, (String)"res");
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
    public void renderTileEntity(@NotNull ITileEntity tileEntity, float partialTicks, int destroyStage) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)tileEntity, (String)"tileEntity");
        ITileEntity iTileEntity = tileEntity;
        TileEntityRendererDispatcher tileEntityRendererDispatcher = TileEntityRendererDispatcher.field_147556_a;
        boolean $i$f$unwrap = false;
        TileEntity tileEntity2 = ((TileEntityImpl)$this$unwrap$iv).getWrapped();
        tileEntityRendererDispatcher.func_180546_a(tileEntity2, partialTicks, destroyStage);
    }

    @Override
    @Nullable
    public IBlock getBlockFromName(@NotNull String name) {
        IBlock iBlock;
        Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
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
    @Nullable
    public IItem getItemByName(@NotNull String name) {
        Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
        Object object = Items.class.getField(name).get(null);
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.Item");
        }
        Item $this$wrap$iv = (Item)object;
        boolean $i$f$wrap = false;
        return new ItemImpl<Item>($this$wrap$iv);
    }

    @Override
    @Nullable
    public IEnchantment getEnchantmentByLocation(@NotNull String location) {
        IEnchantment iEnchantment;
        Intrinsics.checkParameterIsNotNull((Object)location, (String)"location");
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
    @Nullable
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
    @NotNull
    public Collection<IResourceLocation> getEnchantments() {
        RegistryNamespaced registryNamespaced = Enchantment.field_185264_b;
        Intrinsics.checkExpressionValueIsNotNull((Object)registryNamespaced, (String)"Enchantment.REGISTRY");
        return new WrappedCollection(registryNamespaced.func_148742_b(), getEnchantments.1.INSTANCE, getEnchantments.2.INSTANCE);
    }

    @Override
    @NotNull
    public Collection<IResourceLocation> getItemRegistryKeys() {
        RegistryNamespaced registryNamespaced = Item.field_150901_e;
        Intrinsics.checkExpressionValueIsNotNull((Object)registryNamespaced, (String)"Item.REGISTRY");
        return new WrappedCollection(registryNamespaced.func_148742_b(), getItemRegistryKeys.1.INSTANCE, getItemRegistryKeys.2.INSTANCE);
    }

    @Override
    @NotNull
    public Collection<IResourceLocation> getBlockRegistryKeys() {
        RegistryNamespacedDefaultedByKey registryNamespacedDefaultedByKey = Block.field_149771_c;
        Intrinsics.checkExpressionValueIsNotNull((Object)registryNamespacedDefaultedByKey, (String)"Block.REGISTRY");
        return new WrappedCollection(registryNamespacedDefaultedByKey.func_148742_b(), getBlockRegistryKeys.1.INSTANCE, getBlockRegistryKeys.2.INSTANCE);
    }

    @Override
    public void disableStandardItemLighting() {
        RenderHelper.func_74518_a();
    }

    @Override
    @NotNull
    public String formatI18n(@NotNull String key, String ... values) {
        Intrinsics.checkParameterIsNotNull((Object)key, (String)"key");
        Intrinsics.checkParameterIsNotNull((Object)values, (String)"values");
        String string = I18n.func_135052_a((String)key, (Object[])new Object[]{values});
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"I18n.format(key, values)");
        return string;
    }

    @Override
    public void sessionServiceJoinServer(@NotNull GameProfile profile, @NotNull String token, @NotNull String sessionHash) {
        Intrinsics.checkParameterIsNotNull((Object)profile, (String)"profile");
        Intrinsics.checkParameterIsNotNull((Object)token, (String)"token");
        Intrinsics.checkParameterIsNotNull((Object)sessionHash, (String)"sessionHash");
        Minecraft minecraft = Minecraft.func_71410_x();
        Intrinsics.checkExpressionValueIsNotNull((Object)minecraft, (String)"Minecraft.getMinecraft()");
        minecraft.func_152347_ac().joinServer(profile, token, sessionHash);
    }

    @Override
    @NotNull
    public IPotion getPotionById(int potionID) {
        Potion potion = Potion.func_188412_a((int)potionID);
        Intrinsics.checkExpressionValueIsNotNull((Object)potion, (String)"Potion.getPotionById(potionID)!!");
        Potion $this$wrap$iv = potion;
        boolean $i$f$wrap = false;
        return new PotionImpl($this$wrap$iv);
    }

    @Override
    public void enableStandardItemLighting() {
        RenderHelper.func_74519_b();
    }

    @Override
    @NotNull
    public String scoreboardFormatPlayerName(@Nullable ITeam scorePlayerTeam, @NotNull String playerName) {
        Team team;
        Intrinsics.checkParameterIsNotNull((Object)playerName, (String)"playerName");
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
        String string = ScorePlayerTeam.func_96667_a(team, (String)playerName);
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"ScorePlayerTeam.formatPl\u2026am?.unwrap(), playerName)");
        return string;
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
    @NotNull
    public IIChatComponent jsonToComponent(@NotNull String toString) {
        Intrinsics.checkParameterIsNotNull((Object)toString, (String)"toString");
        ITextComponent iTextComponent = ITextComponent.Serializer.func_150699_a((String)toString);
        Intrinsics.checkExpressionValueIsNotNull((Object)iTextComponent, (String)"ITextComponent.Serialize\u2026onToComponent(toString)!!");
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
    @Nullable
    public IItem getItemById(int id) {
        Item item = Item.func_150899_d((int)id);
        Intrinsics.checkExpressionValueIsNotNull((Object)item, (String)"Item.getItemById(id)");
        Item $this$wrap$iv = item;
        boolean $i$f$wrap = false;
        return new ItemImpl<Item>($this$wrap$iv);
    }

    @Override
    public int getIdFromItem(@NotNull IItem sb) {
        Intrinsics.checkParameterIsNotNull((Object)sb, (String)"sb");
        IItem $this$unwrap$iv = sb;
        boolean $i$f$unwrap = false;
        return Item.func_150891_b(((ItemImpl)$this$unwrap$iv).getWrapped());
    }

    @Override
    public boolean canAddItemToSlot(@NotNull ISlot slotIn, @NotNull IItemStack stack, boolean stackSizeMatters) {
        Intrinsics.checkParameterIsNotNull((Object)slotIn, (String)"slotIn");
        Intrinsics.checkParameterIsNotNull((Object)stack, (String)"stack");
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
            Field field = declaredField;
            Intrinsics.checkExpressionValueIsNotNull((Object)field, (String)"declaredField");
            if (!field.isAccessible()) {
                declaredField.setAccessible(true);
            }
        }
        catch (NoSuchFieldException noSuchFieldException) {
            // empty catch block
        }
    }
}

