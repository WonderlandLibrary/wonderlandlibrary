/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
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

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0096\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0003H&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\nH&J\b\u0010\f\u001a\u00020\nH&J)\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0012\u0010\u0010\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u000e0\u0011\"\u00020\u000eH&\u00a2\u0006\u0002\u0010\u0012J\u0012\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0015\u001a\u00020\u0016H&J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0018\u001a\u00020\u000eH&J\u000e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aH&J\u0012\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\u0016H&J\u0012\u0010\u001f\u001a\u0004\u0018\u00010\u001d2\u0006\u0010 \u001a\u00020\u000eH&J\u000e\u0010!\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aH&J\u0010\u0010\"\u001a\u00020\u00162\u0006\u0010#\u001a\u00020\u0014H&J\u0010\u0010$\u001a\u00020\u00162\u0006\u0010%\u001a\u00020&H&J\u0012\u0010'\u001a\u0004\u0018\u00010&2\u0006\u0010\u0015\u001a\u00020\u0016H&J\u0012\u0010(\u001a\u0004\u0018\u00010&2\u0006\u0010\u0018\u001a\u00020\u000eH&J\u000e\u0010)\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aH&J\u001a\u0010*\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010\u00072\u0006\u0010-\u001a\u00020.H&J\u0012\u0010/\u001a\u0004\u0018\u00010&2\u0006\u00100\u001a\u00020\u001bH&J\u0010\u00101\u001a\u0002022\u0006\u00103\u001a\u00020\u0016H&J\u0010\u00104\u001a\u0002052\u0006\u00106\u001a\u00020\u000eH&J \u00107\u001a\u00020\n2\u0006\u00108\u001a\u0002092\u0006\u0010:\u001a\u00020+2\u0006\u0010;\u001a\u00020\u0016H&J\u001a\u0010<\u001a\u00020\u000e2\b\u0010=\u001a\u0004\u0018\u00010>2\u0006\u0010?\u001a\u00020\u000eH&J \u0010@\u001a\u00020\n2\u0006\u0010A\u001a\u00020B2\u0006\u0010C\u001a\u00020\u000e2\u0006\u0010D\u001a\u00020\u000eH&J\b\u0010E\u001a\u00020\nH&J\b\u0010F\u001a\u00020\nH&\u00a8\u0006G"}, d2={"Lnet/ccbluex/liquidbounce/api/IExtractedFunctions;", "", "canAddItemToSlot", "", "slotIn", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/ISlot;", "stack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "stackSizeMatters", "disableFastRender", "", "disableStandardItemLighting", "enableStandardItemLighting", "formatI18n", "", "key", "values", "", "(Ljava/lang/String;[Ljava/lang/String;)Ljava/lang/String;", "getBlockById", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "id", "", "getBlockFromName", "name", "getBlockRegistryKeys", "", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "getEnchantmentById", "Lnet/ccbluex/liquidbounce/api/minecraft/enchantments/IEnchantment;", "enchantID", "getEnchantmentByLocation", "location", "getEnchantments", "getIdFromBlock", "block", "getIdFromItem", "sb", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "getItemById", "getItemByName", "getItemRegistryKeys", "getModifierForCreature", "", "heldItem", "creatureAttribute", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/IEnumCreatureAttribute;", "getObjectFromItemRegistry", "res", "getPotionById", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotion;", "potionID", "jsonToComponent", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "toString", "renderTileEntity", "tileEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/tileentity/ITileEntity;", "partialTicks", "destroyStage", "scoreboardFormatPlayerName", "scorePlayerTeam", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "playerName", "sessionServiceJoinServer", "profile", "Lcom/mojang/authlib/GameProfile;", "token", "sessionHash", "setActiveTextureDefaultTexUnit", "setActiveTextureLightMapTexUnit", "LiKingSense"})
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

    @Nullable
    public IItem getItemById(int var1);

    public int getIdFromItem(@NotNull IItem var1);

    public boolean canAddItemToSlot(@NotNull ISlot var1, @NotNull IItemStack var2, boolean var3);
}

