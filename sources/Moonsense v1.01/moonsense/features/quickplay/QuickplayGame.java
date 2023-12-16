// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.quickplay;

import moonsense.features.modules.QuickplayModule;
import moonsense.features.quickplay.ui.QuickplayMenu;
import java.util.Collection;
import java.util.List;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.init.Items;
import com.google.gson.JsonElement;
import moonsense.utils.StringUtils;
import java.util.LinkedHashMap;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Map;
import java.util.ArrayList;
import moonsense.features.quickplay.ui.QuickplayOption;

public class QuickplayGame implements QuickplayOption
{
    private String id;
    private String name;
    private ArrayList<String> aliases;
    private Map<String, QuickplayGameMode> modes;
    private URL imageURL;
    private BufferedImage image;
    private ItemStack item;
    
    public QuickplayGame(final JsonObject object) {
        this.id = object.get("unlocalizedName").getAsString();
        this.name = object.get("name").getAsString();
        (this.aliases = (ArrayList<String>)Lists.newArrayList()).add(this.name.replace(" ", ""));
        String alias1 = "";
        String[] split;
        for (int length = (split = this.name.split(" ")).length, i = 0; i < length; ++i) {
            final String s = split[i];
            alias1 = String.valueOf(alias1) + s.charAt(0);
        }
        this.aliases.add(alias1.toLowerCase());
        this.modes = new LinkedHashMap<String, QuickplayGameMode>();
        this.imageURL = StringUtils.sneakyParse(object.get("imageURL").getAsString());
        for (final JsonElement modeElement : object.get("modes").getAsJsonArray()) {
            final QuickplayGameMode mode = new QuickplayGameMode(this, modeElement.getAsJsonObject());
            this.modes.put(mode.getCommand(), mode);
        }
        Item itemType = null;
        final String id;
        switch (id = this.id) {
            case "skywars": {
                itemType = Items.ender_eye;
                break;
            }
            case "skyblock": {
                (this.item = new ItemStack(Items.skull)).setItemDamage(3);
                try {
                    this.item.setTagCompound(JsonToNBT.func_180713_a("{overrideMeta:1b,HideFlags:254,SkullOwner:{Id:\"e70f48d9-56b0-2023-b32e-f48bbdd063af\",hypixelPopulated:1b,Properties:{textures:[0:{Value:\"eyJ0aW1lc3RhbXAiOjE1NTkyMTU0MTY5MDksInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Q3Y2M2Njg3NDIzZDA1NzBkNTU2YWM1M2UwNjc2Y2I1NjNiYmRkOTcxN2NkODI2OWJkZWJlZDZmNmQ0ZTdiZjgifX19\"}]}}}"));
                }
                catch (NBTException ex) {}
                break;
            }
            case "arcade": {
                itemType = Items.slime_ball;
                break;
            }
            case "smashHeroes": {
                (this.item = new ItemStack(Items.skull)).setItemDamage(3);
                try {
                    this.item.setTagCompound(JsonToNBT.func_180713_a("{ench:[],overrideMeta:1b,HideFlags:254,SkullOwner:{Id:\"a83ccfb7-3672-281f-90da-0f78dcf95378\",hypixelPopulated:1b,Properties:{textures:[0:{Value:\"eyJ0aW1lc3RhbXAiOjE0NTIwNTgzNTA4MDcsInByb2ZpbGVJZCI6ImFhZDIzYTUwZWVkODQ3MTA5OWNmNjRiZThmZjM0ZWY0IiwicHJvZmlsZU5hbWUiOiIxUm9ndWUiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2QyOWE5ZjU3MjY3ZWQzNDJhMTNlM2FkM2EyNDBjNGM1YWY1YTFhMzZhYjJkZTBkNmMyYTMxYWYwZTNjZGRlIn19fQ==\"}]}}}"));
                }
                catch (NBTException ex2) {}
                break;
            }
            case "murder": {
                itemType = Items.bow;
                break;
            }
            case "tournament": {
                itemType = Items.blaze_powder;
                break;
            }
            case "thePit": {
                itemType = Item.getItemFromBlock(Blocks.dirt);
                break;
            }
            case "prototype": {
                itemType = Item.getItemFromBlock(Blocks.anvil);
                break;
            }
            case "mainLobby": {
                itemType = Items.oak_door;
                break;
            }
            case "bedwars": {
                itemType = Items.bed;
                break;
            }
            case "mw": {
                itemType = Item.getItemFromBlock(Blocks.soul_sand);
                break;
            }
            case "cvc": {
                itemType = Item.getItemFromBlock(Blocks.iron_bars);
                break;
            }
            case "tnt": {
                itemType = Item.getItemFromBlock(Blocks.tnt);
                break;
            }
            case "uhc": {
                itemType = Items.golden_apple;
                break;
            }
            case "blitz": {
                itemType = Items.diamond_sword;
                break;
            }
            case "duels": {
                itemType = Items.fishing_rod;
                break;
            }
            case "warlords": {
                itemType = Items.stone_axe;
                break;
            }
            case "classic": {
                itemType = Item.getItemFromBlock(Blocks.jukebox);
                break;
            }
            case "housing": {
                itemType = Items.dark_oak_door;
                break;
            }
            case "buildBattle": {
                itemType = Item.getItemFromBlock(Blocks.crafting_table);
                break;
            }
            default:
                break;
        }
        if (itemType != null) {
            this.item = new ItemStack(itemType);
        }
    }
    
    public QuickplayGameMode getMode(final String command) {
        return this.modes.get(command);
    }
    
    public BufferedImage getImage() {
        if (this.image == null) {
            try {
                Throwable t = null;
                try {
                    final InputStream in = this.imageURL.openStream();
                    try {
                        this.image = ImageIO.read(in);
                    }
                    finally {
                        if (in != null) {
                            in.close();
                        }
                    }
                }
                finally {
                    if (t == null) {
                        final Throwable exception;
                        t = exception;
                    }
                    else {
                        final Throwable exception;
                        if (t != exception) {
                            t.addSuppressed(exception);
                        }
                    }
                }
            }
            catch (IOException error) {
                throw new IllegalStateException(error);
            }
        }
        return this.image;
    }
    
    public List<QuickplayGameMode> getModes() {
        return new ArrayList<QuickplayGameMode>(this.modes.values());
    }
    
    public List<QuickplayOption> getModeOptions() {
        return new ArrayList<QuickplayOption>(this.modes.values());
    }
    
    @Override
    public String getText() {
        if (this.modes.size() == 1) {
            return this.name;
        }
        return String.valueOf(this.name) + " >";
    }
    
    @Override
    public void onClick(final QuickplayMenu palette, final QuickplayModule mod) {
        if (this.modes.size() == 1) {
            this.modes.values().stream().findFirst().get().onClick(palette, mod);
        }
        else {
            palette.selectGame(this);
        }
    }
    
    @Override
    public ItemStack getIcon() {
        return this.item;
    }
    
    public ItemStack getItem() {
        return this.item;
    }
    
    public String getId() {
        return this.id;
    }
    
    public URL getImageURL() {
        return this.imageURL;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ArrayList<String> getAliases() {
        return this.aliases;
    }
}
