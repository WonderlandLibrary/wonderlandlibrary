package me.aquavit.liquidsense.utils.forge;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.item.ItemUtils;
import me.aquavit.liquidsense.utils.misc.HttpUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

@SideOnly(Side.CLIENT)
public class HeadsTab extends CreativeTabs {

    /**
     * Constructor of heads tab
     */
    public HeadsTab() {
        super("Heads");
        this.setBackgroundImageName("item_search.png");
        this.loadHeads();
    }

    // List of heads
    private final ArrayList<ItemStack> heads = new ArrayList<>();

    /**
     * @return searchbar status
     */
    @Override
    public boolean hasSearchBar() {
        return true;
    }

    /**
     * Return name of tab
     *
     * @return tab name
     */
    @Override
    public String getTranslatedTabLabel() {
        return "Heads";
    }

    /**
     * Return icon item of tab
     *
     * @return icon item
     */
    @Override
    public Item getTabIconItem() {
        return Items.skull;
    }

    /**
     * Add all items to tab
     *
     * @param itemList list of tab items
     */
    @Override
    public void displayAllReleventItems(List<ItemStack> itemList) {
        itemList.addAll(this.heads);
    }

    /**
     * Load all heads from the database
     */
    private void loadHeads() {
        ClientUtils.getLogger().info("Loading heads...");

        try {
            String headsConfigUrl = LiquidSense.CLIENT_RESOURCE + "heads.json";
            JsonObject headsConf = getJsonObject(headsConfigUrl);
            if (headsConf == null) {
                ClientUtils.getLogger().error("Failed to load heads configuration.");
                return;
            }

            if (!headsConf.has("enabled") || !headsConf.get("enabled").getAsBoolean()) {
                ClientUtils.getLogger().info("Heads are disabled.");
                return;
            }

            String headsUrl = headsConf.get("url").getAsString();
            JsonObject headsObject = getJsonObject(headsUrl);
            if (headsObject == null) {
                ClientUtils.getLogger().error("Failed to load heads from {}.", headsUrl);
                return;
            }

            for (Map.Entry<String, JsonElement> entry : headsObject.entrySet()) {
                JsonObject headObject = entry.getValue().getAsJsonObject();

                String name = headObject.get("name").getAsString();
                String uuid = headObject.get("uuid").getAsString();
                String value = headObject.get("value").getAsString();

                String skullData = String.format("skull 1 3 {display:{Name:\"%s\"},SkullOwner:{Id:\"%s\",Properties:{textures:[{Value:\"%s\"}]}}}", name, uuid, value);
                ItemStack skull = ItemUtils.createItem(skullData);

                if (skull != null) {
                    heads.add(skull);
                }
            }

            ClientUtils.getLogger().info("Loaded {} heads from HeadDB.", heads.size());
        } catch (Exception e) {
            ClientUtils.getLogger().error("Error while reading heads.", e);
        }
    }

    private JsonObject getJsonObject(String url) throws Exception {
        String json = HttpUtils.get(url);
        if (json == null) {
            return null;
        }

        JsonElement element = new JsonParser().parse(json);
        if (!element.isJsonObject()) {
            ClientUtils.getLogger().error("The response is not a JsonObject: {}", json);
            return null;
        }

        return element.getAsJsonObject();
    }

}
