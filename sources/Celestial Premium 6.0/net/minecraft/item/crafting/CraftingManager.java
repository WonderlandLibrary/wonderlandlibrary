/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCloning;
import net.minecraft.item.crafting.RecipeFireworks;
import net.minecraft.item.crafting.RecipeRepairItem;
import net.minecraft.item.crafting.RecipeTippedArrow;
import net.minecraft.item.crafting.RecipesArmorDyes;
import net.minecraft.item.crafting.RecipesBanners;
import net.minecraft.item.crafting.RecipesMapCloning;
import net.minecraft.item.crafting.RecipesMapExtending;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.item.crafting.ShieldRecipes;
import net.minecraft.item.crafting.ShulkerBoxRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CraftingManager {
    private static final Logger field_192422_a = LogManager.getLogger();
    private static int field_193381_c;
    public static final RegistryNamespaced<ResourceLocation, IRecipe> field_193380_a;

    public static boolean func_193377_a() {
        try {
            CraftingManager.func_193379_a("armordye", new RecipesArmorDyes());
            CraftingManager.func_193379_a("bookcloning", new RecipeBookCloning());
            CraftingManager.func_193379_a("mapcloning", new RecipesMapCloning());
            CraftingManager.func_193379_a("mapextending", new RecipesMapExtending());
            CraftingManager.func_193379_a("fireworks", new RecipeFireworks());
            CraftingManager.func_193379_a("repairitem", new RecipeRepairItem());
            CraftingManager.func_193379_a("tippedarrow", new RecipeTippedArrow());
            CraftingManager.func_193379_a("bannerduplicate", new RecipesBanners.RecipeDuplicatePattern());
            CraftingManager.func_193379_a("banneraddpattern", new RecipesBanners.RecipeAddPattern());
            CraftingManager.func_193379_a("shielddecoration", new ShieldRecipes.Decoration());
            CraftingManager.func_193379_a("shulkerboxcoloring", new ShulkerBoxRecipes.ShulkerBoxColoring());
            return CraftingManager.func_192420_c();
        }
        catch (Throwable var1) {
            return false;
        }
    }

    /*
     * Exception decompiling
     */
    private static boolean func_192420_c() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[TRYBLOCK]], but top level block is 4[TRYBLOCK]
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:429)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:478)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:728)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:806)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
         * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
         * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
         * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
         * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
         * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
         * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
         * org.benf.cfr.reader.Main.main(Main.java:49)
         */
        throw new IllegalStateException(Decompilation failed);
    }

    private static IRecipe func_193376_a(JsonObject p_193376_0_) {
        String s = JsonUtils.getString(p_193376_0_, "type");
        if ("crafting_shaped".equals(s)) {
            return ShapedRecipes.func_193362_a(p_193376_0_);
        }
        if ("crafting_shapeless".equals(s)) {
            return ShapelessRecipes.func_193363_a(p_193376_0_);
        }
        throw new JsonSyntaxException("Invalid or unsupported recipe type '" + s + "'");
    }

    public static void func_193379_a(String p_193379_0_, IRecipe p_193379_1_) {
        CraftingManager.func_193372_a(new ResourceLocation(p_193379_0_), p_193379_1_);
    }

    public static void func_193372_a(ResourceLocation p_193372_0_, IRecipe p_193372_1_) {
        if (field_193380_a.containsKey(p_193372_0_)) {
            throw new IllegalStateException("Duplicate recipe ignored with ID " + p_193372_0_);
        }
        field_193380_a.register(field_193381_c++, p_193372_0_, p_193372_1_);
    }

    public static ItemStack findMatchingRecipe(InventoryCrafting p_82787_0_, World craftMatrix) {
        for (IRecipe irecipe : field_193380_a) {
            if (!irecipe.matches(p_82787_0_, craftMatrix)) continue;
            return irecipe.getCraftingResult(p_82787_0_);
        }
        return ItemStack.field_190927_a;
    }

    @Nullable
    public static IRecipe func_192413_b(InventoryCrafting p_192413_0_, World p_192413_1_) {
        for (IRecipe irecipe : field_193380_a) {
            if (!irecipe.matches(p_192413_0_, p_192413_1_)) continue;
            return irecipe;
        }
        return null;
    }

    public static NonNullList<ItemStack> getRemainingItems(InventoryCrafting p_180303_0_, World craftMatrix) {
        for (IRecipe irecipe : field_193380_a) {
            if (!irecipe.matches(p_180303_0_, craftMatrix)) continue;
            return irecipe.getRemainingItems(p_180303_0_);
        }
        NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(p_180303_0_.getSizeInventory(), ItemStack.field_190927_a);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            nonnulllist.set(i, p_180303_0_.getStackInSlot(i));
        }
        return nonnulllist;
    }

    @Nullable
    public static IRecipe func_193373_a(ResourceLocation p_193373_0_) {
        return field_193380_a.getObject(p_193373_0_);
    }

    public static int func_193375_a(IRecipe p_193375_0_) {
        return field_193380_a.getIDForObject(p_193375_0_);
    }

    @Nullable
    public static IRecipe func_193374_a(int p_193374_0_) {
        return field_193380_a.getObjectById(p_193374_0_);
    }

    static {
        field_193380_a = new RegistryNamespaced();
    }
}

