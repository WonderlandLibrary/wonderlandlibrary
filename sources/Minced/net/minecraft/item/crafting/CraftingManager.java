// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.NonNullList;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.util.Iterator;
import java.net.URI;
import java.net.URL;
import com.google.gson.Gson;
import java.nio.file.FileSystem;
import java.io.Closeable;
import java.net.URISyntaxException;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import com.google.gson.JsonParseException;
import java.io.Reader;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonObject;
import org.apache.commons.io.FilenameUtils;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.nio.file.Paths;
import com.google.gson.GsonBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.apache.logging.log4j.Logger;

public class CraftingManager
{
    private static final Logger LOGGER;
    private static int nextAvailableId;
    public static final RegistryNamespaced<ResourceLocation, IRecipe> REGISTRY;
    
    public static boolean init() {
        try {
            register("armordye", new RecipesArmorDyes());
            register("bookcloning", new RecipeBookCloning());
            register("mapcloning", new RecipesMapCloning());
            register("mapextending", new RecipesMapExtending());
            register("fireworks", new RecipeFireworks());
            register("repairitem", new RecipeRepairItem());
            register("tippedarrow", new RecipeTippedArrow());
            register("bannerduplicate", new RecipesBanners.RecipeDuplicatePattern());
            register("banneraddpattern", new RecipesBanners.RecipeAddPattern());
            register("shielddecoration", new ShieldRecipes.Decoration());
            register("shulkerboxcoloring", new ShulkerBoxRecipes.ShulkerBoxColoring());
            return parseJsonRecipes();
        }
        catch (Throwable var1) {
            return false;
        }
    }
    
    private static boolean parseJsonRecipes() {
        FileSystem filesystem = null;
        final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        boolean flag4 = false;
        try {
            final URL url = CraftingManager.class.getResource("/assets/.mcassetsroot");
            if (url != null) {
                final URI uri = url.toURI();
                Path path;
                if ("file".equals(uri.getScheme())) {
                    path = Paths.get(CraftingManager.class.getResource("/assets/minecraft/recipes").toURI());
                }
                else {
                    if (!"jar".equals(uri.getScheme())) {
                        CraftingManager.LOGGER.error("Unsupported scheme " + uri + " trying to list all recipes");
                        final boolean flag2 = false;
                        return flag2;
                    }
                    filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    path = filesystem.getPath("/assets/minecraft/recipes", new String[0]);
                }
                for (final Path path2 : Files.walk(path, new FileVisitOption[0])) {
                    if ("json".equals(FilenameUtils.getExtension(path2.toString()))) {
                        final Path path3 = path.relativize(path2);
                        final String s = FilenameUtils.removeExtension(path3.toString()).replaceAll("\\\\", "/");
                        final ResourceLocation resourcelocation = new ResourceLocation(s);
                        BufferedReader bufferedreader = null;
                        try {
                            bufferedreader = Files.newBufferedReader(path2);
                            register(s, parseRecipeJson(JsonUtils.fromJson(gson, bufferedreader, JsonObject.class)));
                        }
                        catch (JsonParseException jsonparseexception) {
                            CraftingManager.LOGGER.error("Parsing error loading recipe " + resourcelocation, (Throwable)jsonparseexception);
                            final boolean flag3 = false;
                            return flag3;
                        }
                        catch (IOException ioexception) {
                            CraftingManager.LOGGER.error("Couldn't read recipe " + resourcelocation + " from " + path2, (Throwable)ioexception);
                            final boolean flag3 = false;
                            return flag3;
                        }
                        finally {
                            IOUtils.closeQuietly((Reader)bufferedreader);
                        }
                    }
                }
                return true;
            }
            CraftingManager.LOGGER.error("Couldn't find .mcassetsroot");
            flag4 = false;
        }
        catch (IOException ex) {}
        catch (URISyntaxException urisyntaxexception) {
            CraftingManager.LOGGER.error("Couldn't get a list of all recipe files", (Throwable)urisyntaxexception);
            flag4 = false;
            return flag4;
        }
        finally {
            IOUtils.closeQuietly((Closeable)filesystem);
        }
        return flag4;
    }
    
    private static IRecipe parseRecipeJson(final JsonObject json) {
        final String s = JsonUtils.getString(json, "type");
        if ("crafting_shaped".equals(s)) {
            return ShapedRecipes.deserialize(json);
        }
        if ("crafting_shapeless".equals(s)) {
            return ShapelessRecipes.deserialize(json);
        }
        throw new JsonSyntaxException("Invalid or unsupported recipe type '" + s + "'");
    }
    
    public static void register(final String name, final IRecipe recipe) {
        register(new ResourceLocation(name), recipe);
    }
    
    public static void register(final ResourceLocation name, final IRecipe recipe) {
        if (CraftingManager.REGISTRY.containsKey(name)) {
            throw new IllegalStateException("Duplicate recipe ignored with ID " + name);
        }
        CraftingManager.REGISTRY.register(CraftingManager.nextAvailableId++, name, recipe);
    }
    
    public static ItemStack findMatchingResult(final InventoryCrafting craftMatrix, final World worldIn) {
        for (final IRecipe irecipe : CraftingManager.REGISTRY) {
            if (irecipe.matches(craftMatrix, worldIn)) {
                return irecipe.getCraftingResult(craftMatrix);
            }
        }
        return ItemStack.EMPTY;
    }
    
    @Nullable
    public static IRecipe findMatchingRecipe(final InventoryCrafting craftMatrix, final World worldIn) {
        for (final IRecipe irecipe : CraftingManager.REGISTRY) {
            if (irecipe.matches(craftMatrix, worldIn)) {
                return irecipe;
            }
        }
        return null;
    }
    
    public static NonNullList<ItemStack> getRemainingItems(final InventoryCrafting craftMatrix, final World worldIn) {
        for (final IRecipe irecipe : CraftingManager.REGISTRY) {
            if (irecipe.matches(craftMatrix, worldIn)) {
                return irecipe.getRemainingItems(craftMatrix);
            }
        }
        final NonNullList<ItemStack> nonnulllist = NonNullList.withSize(craftMatrix.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            nonnulllist.set(i, craftMatrix.getStackInSlot(i));
        }
        return nonnulllist;
    }
    
    @Nullable
    public static IRecipe getRecipe(final ResourceLocation name) {
        return CraftingManager.REGISTRY.getObject(name);
    }
    
    public static int getIDForRecipe(final IRecipe recipe) {
        return CraftingManager.REGISTRY.getIDForObject(recipe);
    }
    
    @Nullable
    public static IRecipe getRecipeById(final int id) {
        return CraftingManager.REGISTRY.getObjectById(id);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        REGISTRY = new RegistryNamespaced<ResourceLocation, IRecipe>();
    }
}
