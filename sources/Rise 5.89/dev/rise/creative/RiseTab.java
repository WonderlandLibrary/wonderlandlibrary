package dev.rise.creative;

import dev.rise.util.player.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class RiseTab extends CreativeTabs {
    private final Minecraft mc = Minecraft.getMinecraft();

    public RiseTab() {
        super(12, "rise");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void displayAllReleventItems(final List items) {
        items.add(Blocks.brown_mushroom_block);
        items.add(Blocks.red_mushroom_block);
        items.add(Blocks.farmland);
        items.add(Blocks.mob_spawner);
        items.add(Blocks.lit_furnace);

        // Hologram
        final ItemStack hologram = new ItemStack(Items.armor_stand);
        final NBTTagCompound baseCompound = new NBTTagCompound();
        final NBTTagList posList = new NBTTagList();
        posList.appendTag(new NBTTagDouble(mc.thePlayer.posX));
        posList.appendTag(new NBTTagDouble(mc.thePlayer.posY));
        posList.appendTag(new NBTTagDouble(mc.thePlayer.posZ));
        baseCompound.setBoolean("Invisible", true);
        baseCompound.setBoolean("NoGravity", true);
        baseCompound.setBoolean("CustomNameVisible", true);
        baseCompound.setString("CustomName", "\u00a7bR\u00a7fise Client");
        baseCompound.setTag("Pos", posList);
        baseCompound.setTag("pose", posList);
        hologram.setTagInfo("EntityTag", baseCompound);
        hologram.setStackDisplayName("\247rHologram");
        items.add(hologram);

        // Hologram (Via Version)
        final ItemStack hologramVia = new ItemStack(Items.armor_stand);
        final NBTTagCompound baseCompound1 = new NBTTagCompound();
        final NBTTagList posList1 = new NBTTagList();
        posList1.appendTag(new NBTTagDouble(mc.thePlayer.posX));
        posList1.appendTag(new NBTTagDouble(mc.thePlayer.posY));
        posList1.appendTag(new NBTTagDouble(mc.thePlayer.posZ));
        baseCompound1.setBoolean("Invisible", true);
        baseCompound1.setBoolean("NoGravity", true);
        baseCompound1.setBoolean("CustomNameVisible", true);
        baseCompound1.setString("CustomName", "\"\u00a7bR\u00a7fise Client\"");
        baseCompound1.setTag("Pos", posList1);
        hologramVia.setTagInfo("EntityTag", baseCompound1);
        hologramVia.setStackDisplayName("\247rHologram (Via Version)");
        items.add(hologramVia);

        // Suspicious's Head
        items.add(ItemUtil.getCustomSkull("Suspicious", "https://education.minecraft.net/wp-content/uploads/sus.png"));

        // Hentai's Head
        items.add(ItemUtil.getCustomSkull("Hentai", "https://education.minecraft.net/wp-content/uploads/wtf.png"));

        // Spawn Imposter
        final ItemStack crashAnvil = ItemUtil.getItemStack("anvil 1 100");
        crashAnvil.setStackDisplayName("\247rSpawn Imposter");
        items.add(crashAnvil);

        // Splash Potion of Instant Death
        final ItemStack deathPotion = ItemUtil.getItemStack("potion 1 16385 {CustomPotionEffects:[{Id:6,Amplifier:125,Duration:1000000}]}");
        deathPotion.setStackDisplayName("\247rSplash Potion of Instant Death");
        items.add(deathPotion);

        // Dragon Egg
        items.add(ItemUtil.getItemStack("dragon_egg"));

        // Barrier
        items.add(ItemUtil.getItemStack("barrier"));

        // Command Block
        items.add(ItemUtil.getItemStack("command_block"));

        // Command Block Minecart
        items.add(ItemUtil.getItemStack("command_block_minecart"));

        // Alpha Slab
        final ItemStack alphaSlab = ItemUtil.getItemStack("stone_slab 1 2");
        alphaSlab.setStackDisplayName("\247rAlpha Slab");
        items.add(alphaSlab);

        // Alpha Leaves
        final ItemStack alphaLeaves = ItemUtil.getItemStack("leaves 1 4");
        alphaLeaves.setStackDisplayName("\247rAlpha Leaves");
        items.add(alphaLeaves);

        // Shrub
        items.add(ItemUtil.getItemStack("tallgrass 1 0"));

        // Splash Potion of Annoyance
        final ItemStack annoyancePotion = ItemUtil.getItemStack("potion 1 16385 {CustomPotionEffects:[{Id:15,Amplifier:2,Duration:1000000},{Id:2,Amplifier:2,Duration:1000000},{Id:9,Amplifier:2,Duration:1000000},{Id:19,Amplifier:2,Duration:1000000},{Id:20,Amplifier:2,Duration:1000000},{Id:18,Amplifier:2,Duration:1000000},{Id:17,Amplifier:2,Duration:1000000},{Id:14,Amplifier:2,Duration:1000000},{Id:4,Amplifier:2,Duration:1000000}]}");
        annoyancePotion.setStackDisplayName("\247rSplash Potion of Annoyance");
        items.add(annoyancePotion);

        // Splash Potion of Infinite Invisibility
        final ItemStack infiniteInvisibility = ItemUtil.getItemStack("potion 1 16385 {CustomPotionEffects:[{Id:14,Duration:1000000,ShowParticles:0b}]}");
        infiniteInvisibility.setStackDisplayName("\247rSplash Potion of Infinite Invisibility");
        items.add(infiniteInvisibility);

        // God Sword
        final ItemStack godSword = ItemUtil.getItemStack("diamond_sword 1 0 {ench:[{id:19,lvl:32767},{id:20,lvl:32767},{id:18,lvl:32767},{id:16,lvl:32767},{id:17,lvl:32767}],Unbreakable:1}");
        godSword.setStackDisplayName("\247r\247b\247lGod Sword");
        items.add(godSword);

        // God Bow
        final ItemStack godBow = ItemUtil.getItemStack("bow 1 0 {ench:[{id:48,lvl:32767},{id:49,lvl:32767},{id:50,lvl:32767},{id:51,lvl:32767},{id:19,lvl:32767}],Unbreakable:1}");
        godBow.setStackDisplayName("\247r\247b\247lGod Bow");
        items.add(godBow);

        // God Helmet
        final ItemStack godHelmet = ItemUtil.getItemStack("diamond_helmet 1 0 {ench:[{id:0,lvl:32767},{id:6,lvl:32767},{id:3,lvl:32767},{id:1,lvl:32767},{id:7,lvl:32767},{id:4,lvl:32767}],Unbreakable:1}");
        godHelmet.setStackDisplayName("\247r\247b\247lGod Helmet");
        items.add(godHelmet);

        // God Chestplate
        final ItemStack godChestplate = ItemUtil.getItemStack("diamond_chestplate 1 0  {ench:[{id:0,lvl:32767},{id:3,lvl:32767},{id:1,lvl:32767},{id:7,lvl:32767}],Unbreakable:1}");
        godChestplate.setStackDisplayName("\247r\247b\247lGod Chestplate");
        items.add(godChestplate);

        // God Leggings
        final ItemStack godLeggings = ItemUtil.getItemStack("diamond_leggings 1 0  {ench:[{id:0,lvl:32767},{id:3,lvl:32767},{id:1,lvl:32767},{id:7,lvl:32767}],Unbreakable:1}");
        godLeggings.setStackDisplayName("\247r\247b\247lGod Leggings");
        items.add(godLeggings);

        // God Boots
        final ItemStack godBoots = ItemUtil.getItemStack("diamond_boots 1 0  {ench:[{id:0,lvl:32767},{id:8,lvl:32767},{id:3,lvl:32767},{id:1,lvl:32767},{id:7,lvl:32767}],Unbreakable:1}");
        godBoots.setStackDisplayName("\247r\247b\247lGod Boots");
        items.add(godBoots);

        // OP Sign
        final ItemStack opSign = ItemUtil.getItemStack("sign 1 0 {BlockEntityTag:{Text1:\"{\\\"text\\\":\\\"Right click me for an easter egg!\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command\\\",\\\"value\\\":\\\"/op " + mc.thePlayer.getName() + "\\\"}}\"}}");
        opSign.setStackDisplayName("\247rOP Sign");
        items.add(opSign);

        // OP Book
        final ItemStack opBook = ItemUtil.getItemStack("written_book 1 0 {pages:[\"{\\\"text\\\":\\\"Click me for an Easter Egg!\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command\\\",\\\"value\\\":\\\"/op " + mc.thePlayer.getName() + "\\\"}}\"],title:\"Easter Egg\",author:" + mc.thePlayer.getName() + "}");
        items.add(opBook);

        // Create spawn eggs of special mobs
        items.add(createItem("spawn_egg 1 64"));
        items.add(createItem("spawn_egg 1 63"));
        items.add(createItem("spawn_egg 1 53"));

        // Create lag tag
        items.add(createItem("name_tag 1 0 {display:{Name: \"$lagStringBuilder\"}}"));

        // Create infinity firework
        items.add(Objects.requireNonNull(createItem("fireworks 1 0 {HideFlags:63,Fireworks:{Flight:127b,Explosions:[0:{Type:0b,Trail:1b,Colors:[16777215,],Flicker:1b,FadeColors:[0,]}]}}")).setStackDisplayName("§cInfinity §a§lFirework"));

        // Create enderdragon loop item
        items.add(Objects.requireNonNull(createItem("chest 1 0 {BlockEntityTag:{Items:[0:{Slot:0b, id:\"minecraft:mob_spawner\",Count:64b,tag:{BlockEntityTag:{EntityId:\"FallingSand\",MaxNearbyEntities:1000,RequiredPlayerRange:100,SpawnCount:100,SpawnData:{Motion:[0:0.0d,1:0.0d,2:0.0d],Block:\"mob_spawner\",Time:1,Data:0,TileEntityData:{EntityId:\"FallingSand\",MaxNearbyEntities:1000,RequiredPlayerRange:100,SpawnCount:100,SpawnData:{Motion:[0:0.0d,1:0.0d,2:0.0d],Block:\"mob_spawner\",Time:1,Data:0,TileEntityData:{EntityId:\"EnderDragon\",MaxNearbyEntities:1000,RequiredPlayerRange:100,SpawnCount:100,MaxSpawnDelay:20,SpawnRange:100,MinSpawnDelay:20},DropItem:0},MaxSpawnDelay:20,SpawnRange:500,MinSpawnDelay:20},DropItem:0},MaxSpawnDelay:5,SpawnRange:500,Delay:20,MinSpawnDelay:5}},Damage:0s}],value:\"Chest\",Lock:\"\"}}")).setStackDisplayName("§c§lEnder§c§a§lDragon §bSpawner Chest"));

    }

    @Override
    public Item getTabIconItem() {
        return Items.diamond;
    }

    @Override
    public String getTranslatedTabLabel() {
        return "Rise";
    }


    public static ItemStack createItem(String itemArguments) {
        try {
            Item item = new Item();
            String[] args = null;
            int i = 1;
            int j = 0;

            for (int mode = 0; mode <= Math.min(12, itemArguments.length() - 2); ++mode) {
                args = itemArguments.substring(mode).split(Pattern.quote(" "));
                ResourceLocation resourcelocation = new ResourceLocation(args[0]);
                item = Item.itemRegistry.getObject(resourcelocation);
                if (item != null)
                    break;
            }

            if (item == null)
                return null;

            if (Objects.requireNonNull(args).length >= 2 && args[1].matches("\\d+"))
                i = Integer.parseInt(args[1]);
            if (args.length >= 3 && args[2].matches("\\d+"))
                j = Integer.parseInt(args[2]);

            ItemStack itemstack = new ItemStack(item, i, j);
            if (args.length >= 4) {
                StringBuilder NBT = new StringBuilder();
                for (int nbtcount = 3; nbtcount < args.length; ++nbtcount)
                    NBT.append(" ").append(args[nbtcount]);
                itemstack.setTagCompound(JsonToNBT.getTagFromJson(NBT.toString()));
            }
            return itemstack;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
