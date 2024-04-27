package net.ccbluex.liquidbounce.api;

import com.mojang.authlib.GameProfile;
import java.util.Collection;
import kotlin.Metadata;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\b\n\n\u0000\n\b\n\b\n\n\n\u0000\n\n\b\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\bf\u000020J 020202\b0H&J\b\t0\nH&J\b0\nH&J\b\f0\nH&J)\r0202\n\b00\"0H&¢J020H&J020H&J\b00H&J020H&J02 0H&J!\b00H&J\"02#0H&J$02%0&H&J'0&20H&J(0&20H&J)\b00H&J*0+2\b,02-0.H&J/0&200H&J102230H&J405260H&J 70\n28092:0+2;0H&J<02\b=0>2?0H&J @0\n2A0B2C02D0H&J\bE0\nH&J\bF0\nH&¨G"}, d2={"Lnet/ccbluex/liquidbounce/api/IExtractedFunctions;", "", "canAddItemToSlot", "", "slotIn", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/ISlot;", "stack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "stackSizeMatters", "disableFastRender", "", "disableStandardItemLighting", "enableStandardItemLighting", "formatI18n", "", "key", "values", "", "(Ljava/lang/String;[Ljava/lang/String;)Ljava/lang/String;", "getBlockById", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "id", "", "getBlockFromName", "name", "getBlockRegistryKeys", "", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "getEnchantmentById", "Lnet/ccbluex/liquidbounce/api/minecraft/enchantments/IEnchantment;", "enchantID", "getEnchantmentByLocation", "location", "getEnchantments", "getIdFromBlock", "block", "getIdFromItem", "item", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "getItemById", "getItemByName", "getItemRegistryKeys", "getModifierForCreature", "", "heldItem", "creatureAttribute", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/IEnumCreatureAttribute;", "getObjectFromItemRegistry", "res", "getPotionById", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotion;", "potionID", "jsonToComponent", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "toString", "renderTileEntity", "tileEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/tileentity/ITileEntity;", "partialTicks", "destroyStage", "scoreboardFormatPlayerName", "scorePlayerTeam", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "playerName", "sessionServiceJoinServer", "profile", "Lcom/mojang/authlib/GameProfile;", "token", "sessionHash", "setActiveTextureDefaultTexUnit", "setActiveTextureLightMapTexUnit", "Pride"})
public interface IExtractedFunctions {
    @Nullable
    public IBlock getBlockById(int var1);

    public int getIdFromBlock(@NotNull IBlock var1);

    public float getModifierForCreature(@Nullable IItemStack var1, @NotNull IEnumCreatureAttribute var2);

    @Nullable
    public IItem getObjectFromItemRegistry(@NotNull IResourceLocation var1);

    public void renderTileEntity(@NotNull ITileEntity var1, float var2, int var3);

    @Nullable
    public IBlock getBlockFromName(@NotNull String var1);

    @Nullable
    public IItem getItemByName(@NotNull String var1);

    @Nullable
    public IEnchantment getEnchantmentByLocation(@NotNull String var1);

    @Nullable
    public IEnchantment getEnchantmentById(int var1);

    @NotNull
    public Collection<IResourceLocation> getEnchantments();

    @Nullable
    public IItem getItemById(int var1);

    @NotNull
    public Collection<IResourceLocation> getItemRegistryKeys();

    @NotNull
    public Collection<IResourceLocation> getBlockRegistryKeys();

    public void disableStandardItemLighting();

    @NotNull
    public String formatI18n(@NotNull String var1, String ... var2);

    public void sessionServiceJoinServer(@NotNull GameProfile var1, @NotNull String var2, @NotNull String var3);

    @NotNull
    public IPotion getPotionById(int var1);

    public void enableStandardItemLighting();

    @NotNull
    public String scoreboardFormatPlayerName(@Nullable ITeam var1, @NotNull String var2);

    public void disableFastRender();

    @NotNull
    public IIChatComponent jsonToComponent(@NotNull String var1);

    public void setActiveTextureLightMapTexUnit();

    public void setActiveTextureDefaultTexUnit();

    public boolean canAddItemToSlot(@NotNull ISlot var1, @NotNull IItemStack var2, boolean var3);

    @NotNull
    public Object getIdFromItem(@NotNull IItem var1);
}
