/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package net.ccbluex.liquidbounce.tabs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.util.WrappedCreativeTabs;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;

public final class HeadsTab
extends WrappedCreativeTabs {
    private final ArrayList<IItemStack> heads = new ArrayList();

    private final void loadHeads() {
        try {
            ClientUtils.getLogger().info("Loading heads...");
            JsonElement headsConfiguration = new JsonParser().parse(HttpUtils.get("https://cloud.liquidbounce.net/LiquidBounce/heads.json"));
            if (!headsConfiguration.isJsonObject()) {
                return;
            }
            JsonObject headsConf = headsConfiguration.getAsJsonObject();
            if (headsConf.get("enabled").getAsBoolean()) {
                String url = headsConf.get("url").getAsString();
                ClientUtils.getLogger().info("Loading heads from " + url + "...");
                JsonElement headsElement = new JsonParser().parse(HttpUtils.get(url));
                if (!headsElement.isJsonObject()) {
                    ClientUtils.getLogger().error("Something is wrong, the heads json is not a JsonObject!");
                    return;
                }
                JsonObject headsObject = headsElement.getAsJsonObject();
                Iterator iterator = headsObject.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry;
                    Map.Entry entry2 = entry = (Map.Entry)iterator.next();
                    boolean bl = false;
                    JsonElement value = (JsonElement)entry2.getValue();
                    JsonObject headElement = value.getAsJsonObject();
                    this.heads.add(ItemUtils.createItem("skull 1 3 {display:{Name:\"" + headElement.get("name").getAsString() + "\"},SkullOwner:{Id:\"" + headElement.get("uuid").getAsString() + "\",Properties:{textures:[{Value:\"" + headElement.get("value").getAsString() + "\"}]}}}"));
                }
                ClientUtils.getLogger().info("Loaded " + this.heads.size() + " heads from HeadDB.");
            } else {
                ClientUtils.getLogger().info("Heads are disabled.");
            }
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("Error while reading heads.", (Throwable)e);
        }
    }

    @Override
    public void displayAllReleventItems(List<IItemStack> itemList) {
        itemList.addAll((Collection<IItemStack>)this.heads);
    }

    @Override
    public IItem getTabIconItem() {
        return WrapperImpl.INSTANCE.getClassProvider().getItemEnum(ItemType.SKULL);
    }

    @Override
    public String getTranslatedTabLabel() {
        return "Heads";
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }

    public HeadsTab() {
        super("Heads");
        this.getRepresentedType().setBackgroundImageName("item_search.png");
        this.loadHeads();
    }
}

