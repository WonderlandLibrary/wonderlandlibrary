// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import com.google.gson.JsonParseException;
import net.minecraft.item.crafting.IRecipe;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.crafting.CraftingManager;
import com.google.gson.JsonArray;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import java.util.Arrays;
import net.minecraft.entity.item.EntityItem;
import java.util.Iterator;
import net.minecraft.command.CommandResultStats;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.command.FunctionObject;
import net.minecraft.util.ResourceLocation;

public class AdvancementRewards
{
    public static final AdvancementRewards EMPTY;
    private final int experience;
    private final ResourceLocation[] loot;
    private final ResourceLocation[] recipes;
    private final FunctionObject.CacheableFunction function;
    
    public AdvancementRewards(final int experience, final ResourceLocation[] loot, final ResourceLocation[] recipes, final FunctionObject.CacheableFunction function) {
        this.experience = experience;
        this.loot = loot;
        this.recipes = recipes;
        this.function = function;
    }
    
    public void apply(final EntityPlayerMP player) {
        player.addExperience(this.experience);
        final LootContext lootcontext = new LootContext.Builder(player.getServerWorld()).withLootedEntity(player).build();
        boolean flag = false;
        for (final ResourceLocation resourcelocation : this.loot) {
            for (final ItemStack itemstack : player.world.getLootTableManager().getLootTableFromLocation(resourcelocation).generateLootForPools(player.getRNG(), lootcontext)) {
                if (player.addItemStackToInventory(itemstack)) {
                    player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    flag = true;
                }
                else {
                    final EntityItem entityitem = player.dropItem(itemstack, false);
                    if (entityitem == null) {
                        continue;
                    }
                    entityitem.setNoPickupDelay();
                    entityitem.setOwner(player.getName());
                }
            }
        }
        if (flag) {
            player.inventoryContainer.detectAndSendChanges();
        }
        if (this.recipes.length > 0) {
            player.unlockRecipes(this.recipes);
        }
        final MinecraftServer minecraftserver = player.server;
        final FunctionObject functionobject = this.function.get(minecraftserver.getFunctionManager());
        if (functionobject != null) {
            final ICommandSender icommandsender = new ICommandSender() {
                @Override
                public String getName() {
                    return player.getName();
                }
                
                @Override
                public ITextComponent getDisplayName() {
                    return player.getDisplayName();
                }
                
                @Override
                public void sendMessage(final ITextComponent component) {
                }
                
                @Override
                public boolean canUseCommand(final int permLevel, final String commandName) {
                    return permLevel <= 2;
                }
                
                @Override
                public BlockPos getPosition() {
                    return player.getPosition();
                }
                
                @Override
                public Vec3d getPositionVector() {
                    return player.getPositionVector();
                }
                
                @Override
                public World getEntityWorld() {
                    return player.world;
                }
                
                @Override
                public Entity getCommandSenderEntity() {
                    return player;
                }
                
                @Override
                public boolean sendCommandFeedback() {
                    return minecraftserver.worlds[0].getGameRules().getBoolean("commandBlockOutput");
                }
                
                @Override
                public void setCommandStat(final CommandResultStats.Type type, final int amount) {
                    player.setCommandStat(type, amount);
                }
                
                @Override
                public MinecraftServer getServer() {
                    return player.getServer();
                }
            };
            minecraftserver.getFunctionManager().execute(functionobject, icommandsender);
        }
    }
    
    @Override
    public String toString() {
        return "AdvancementRewards{experience=" + this.experience + ", loot=" + Arrays.toString(this.loot) + ", recipes=" + Arrays.toString(this.recipes) + ", function=" + this.function + '}';
    }
    
    static {
        EMPTY = new AdvancementRewards(0, new ResourceLocation[0], new ResourceLocation[0], FunctionObject.CacheableFunction.EMPTY);
    }
    
    public static class Deserializer implements JsonDeserializer<AdvancementRewards>
    {
        public AdvancementRewards deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "rewards");
            final int i = JsonUtils.getInt(jsonobject, "experience", 0);
            final JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "loot", new JsonArray());
            final ResourceLocation[] aresourcelocation = new ResourceLocation[jsonarray.size()];
            for (int j = 0; j < aresourcelocation.length; ++j) {
                aresourcelocation[j] = new ResourceLocation(JsonUtils.getString(jsonarray.get(j), "loot[" + j + "]"));
            }
            final JsonArray jsonarray2 = JsonUtils.getJsonArray(jsonobject, "recipes", new JsonArray());
            final ResourceLocation[] aresourcelocation2 = new ResourceLocation[jsonarray2.size()];
            for (int k = 0; k < aresourcelocation2.length; ++k) {
                aresourcelocation2[k] = new ResourceLocation(JsonUtils.getString(jsonarray2.get(k), "recipes[" + k + "]"));
                final IRecipe irecipe = CraftingManager.getRecipe(aresourcelocation2[k]);
                if (irecipe == null) {
                    throw new JsonSyntaxException("Unknown recipe '" + aresourcelocation2[k] + "'");
                }
            }
            FunctionObject.CacheableFunction functionobject$cacheablefunction;
            if (jsonobject.has("function")) {
                functionobject$cacheablefunction = new FunctionObject.CacheableFunction(new ResourceLocation(JsonUtils.getString(jsonobject, "function")));
            }
            else {
                functionobject$cacheablefunction = FunctionObject.CacheableFunction.EMPTY;
            }
            return new AdvancementRewards(i, aresourcelocation, aresourcelocation2, functionobject$cacheablefunction);
        }
    }
}
