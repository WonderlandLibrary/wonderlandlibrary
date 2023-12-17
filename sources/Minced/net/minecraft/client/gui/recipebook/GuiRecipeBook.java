// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.recipebook;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.util.Locale;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.client.util.RecipeBookClient;
import javax.annotation.Nullable;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Keyboard;
import com.google.common.collect.Lists;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.stats.RecipeBook;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.client.gui.GuiButtonToggle;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;

public class GuiRecipeBook extends Gui implements IRecipeUpdateListener
{
    protected static final ResourceLocation RECIPE_BOOK;
    private int xOffset;
    private int width;
    private int height;
    private final GhostRecipe ghostRecipe;
    private final List<GuiButtonRecipeTab> recipeTabs;
    private GuiButtonRecipeTab currentTab;
    private GuiButtonToggle toggleRecipesBtn;
    private InventoryCrafting craftingSlots;
    private Minecraft mc;
    private GuiTextField searchBar;
    private String lastSearch;
    private RecipeBook recipeBook;
    private final RecipeBookPage recipeBookPage;
    private RecipeItemHelper stackedContents;
    private int timesInventoryChanged;
    
    public GuiRecipeBook() {
        this.ghostRecipe = new GhostRecipe();
        this.recipeTabs = (List<GuiButtonRecipeTab>)Lists.newArrayList((Object[])new GuiButtonRecipeTab[] { new GuiButtonRecipeTab(0, CreativeTabs.SEARCH), new GuiButtonRecipeTab(0, CreativeTabs.TOOLS), new GuiButtonRecipeTab(0, CreativeTabs.BUILDING_BLOCKS), new GuiButtonRecipeTab(0, CreativeTabs.MISC), new GuiButtonRecipeTab(0, CreativeTabs.REDSTONE) });
        this.lastSearch = "";
        this.recipeBookPage = new RecipeBookPage();
        this.stackedContents = new RecipeItemHelper();
    }
    
    public void func_194303_a(final int p_194303_1_, final int p_194303_2_, final Minecraft p_194303_3_, final boolean p_194303_4_, final InventoryCrafting p_194303_5_) {
        this.mc = p_194303_3_;
        this.width = p_194303_1_;
        this.height = p_194303_2_;
        this.craftingSlots = p_194303_5_;
        this.recipeBook = Minecraft.player.getRecipeBook();
        this.timesInventoryChanged = Minecraft.player.inventory.getTimesChanged();
        (this.currentTab = this.recipeTabs.get(0)).setStateTriggered(true);
        if (this.isVisible()) {
            this.initVisuals(p_194303_4_, p_194303_5_);
        }
        Keyboard.enableRepeatEvents(true);
    }
    
    public void initVisuals(final boolean p_193014_1_, final InventoryCrafting p_193014_2_) {
        this.xOffset = (p_193014_1_ ? 0 : 86);
        final int i = (this.width - 147) / 2 - this.xOffset;
        final int j = (this.height - 166) / 2;
        this.stackedContents.clear();
        final Minecraft mc = this.mc;
        Minecraft.player.inventory.fillStackedContents(this.stackedContents, false);
        p_193014_2_.fillStackedContents(this.stackedContents);
        (this.searchBar = new GuiTextField(0, this.mc.fontRenderer, i + 25, j + 14, 80, this.mc.fontRenderer.FONT_HEIGHT + 5)).setMaxStringLength(50);
        this.searchBar.setEnableBackgroundDrawing(false);
        this.searchBar.setVisible(true);
        this.searchBar.setTextColor(16777215);
        this.recipeBookPage.init(this.mc, i, j);
        this.recipeBookPage.addListener(this);
        (this.toggleRecipesBtn = new GuiButtonToggle(0, i + 110, j + 12, 26, 16, this.recipeBook.isFilteringCraftable())).initTextureValues(152, 41, 28, 18, GuiRecipeBook.RECIPE_BOOK);
        this.updateCollections(false);
        this.updateTabs();
    }
    
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    public int updateScreenPosition(final boolean p_193011_1_, final int p_193011_2_, final int p_193011_3_) {
        int i;
        if (this.isVisible() && !p_193011_1_) {
            i = 177 + (p_193011_2_ - p_193011_3_ - 200) / 2;
        }
        else {
            i = (p_193011_2_ - p_193011_3_) / 2;
        }
        return i;
    }
    
    public void toggleVisibility() {
        this.setVisible(!this.isVisible());
    }
    
    public boolean isVisible() {
        return this.recipeBook.isGuiOpen();
    }
    
    private void setVisible(final boolean p_193006_1_) {
        this.recipeBook.setGuiOpen(p_193006_1_);
        if (!p_193006_1_) {
            this.recipeBookPage.setInvisible();
        }
        this.sendUpdateSettings();
    }
    
    public void slotClicked(@Nullable final Slot slotIn) {
        if (slotIn != null && slotIn.slotNumber <= 9) {
            this.ghostRecipe.clear();
            if (this.isVisible()) {
                this.updateStackedContents();
            }
        }
    }
    
    private void updateCollections(final boolean p_193003_1_) {
        final List<RecipeList> list = RecipeBookClient.RECIPES_BY_TAB.get(this.currentTab.getCategory());
        list.forEach(p_193944_1_ -> p_193944_1_.canCraft(this.stackedContents, this.craftingSlots.getWidth(), this.craftingSlots.getHeight(), this.recipeBook));
        final List<RecipeList> list2 = (List<RecipeList>)Lists.newArrayList((Iterable)list);
        list2.removeIf(p_193952_0_ -> !p_193952_0_.isNotEmpty());
        list2.removeIf(p_193953_0_ -> !p_193953_0_.containsValidRecipes());
        final String s = this.searchBar.getText();
        if (!s.isEmpty()) {
            final ObjectSet<RecipeList> objectset = (ObjectSet<RecipeList>)new ObjectLinkedOpenHashSet((Collection)this.mc.getSearchTree(SearchTreeManager.RECIPES).search(s.toLowerCase(Locale.ROOT)));
            list2.removeIf(p_193947_1_ -> !objectset.contains((Object)p_193947_1_));
        }
        if (this.recipeBook.isFilteringCraftable()) {
            list2.removeIf(p_193958_0_ -> !p_193958_0_.containsCraftableRecipes());
        }
        this.recipeBookPage.updateLists(list2, p_193003_1_);
    }
    
    private void updateTabs() {
        final int i = (this.width - 147) / 2 - this.xOffset - 30;
        final int j = (this.height - 166) / 2 + 3;
        final int k = 27;
        int l = 0;
        for (final GuiButtonRecipeTab guibuttonrecipetab : this.recipeTabs) {
            final CreativeTabs creativetabs = guibuttonrecipetab.getCategory();
            if (creativetabs == CreativeTabs.SEARCH) {
                guibuttonrecipetab.visible = true;
                guibuttonrecipetab.setPosition(i, j + 27 * l++);
            }
            else {
                if (!guibuttonrecipetab.updateVisibility()) {
                    continue;
                }
                guibuttonrecipetab.setPosition(i, j + 27 * l++);
                guibuttonrecipetab.startAnimation(this.mc);
            }
        }
    }
    
    public void tick() {
        if (this.isVisible()) {
            final int timesInventoryChanged = this.timesInventoryChanged;
            final Minecraft mc = this.mc;
            if (timesInventoryChanged != Minecraft.player.inventory.getTimesChanged()) {
                this.updateStackedContents();
                final Minecraft mc2 = this.mc;
                this.timesInventoryChanged = Minecraft.player.inventory.getTimesChanged();
            }
        }
    }
    
    private void updateStackedContents() {
        this.stackedContents.clear();
        final Minecraft mc = this.mc;
        Minecraft.player.inventory.fillStackedContents(this.stackedContents, false);
        this.craftingSlots.fillStackedContents(this.stackedContents);
        this.updateCollections(false);
    }
    
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.isVisible()) {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, 100.0f);
            this.mc.getTextureManager().bindTexture(GuiRecipeBook.RECIPE_BOOK);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final int i = (this.width - 147) / 2 - this.xOffset;
            final int j = (this.height - 166) / 2;
            this.drawTexturedModalRect(i, j, 1, 1, 147, 166);
            this.searchBar.drawTextBox();
            RenderHelper.disableStandardItemLighting();
            for (final GuiButtonRecipeTab guibuttonrecipetab : this.recipeTabs) {
                guibuttonrecipetab.drawButton(this.mc, mouseX, mouseY, partialTicks);
            }
            this.toggleRecipesBtn.drawButton(this.mc, mouseX, mouseY, partialTicks);
            this.recipeBookPage.render(i, j, mouseX, mouseY, partialTicks);
            GlStateManager.popMatrix();
        }
    }
    
    public void renderTooltip(final int p_191876_1_, final int p_191876_2_, final int p_191876_3_, final int p_191876_4_) {
        if (this.isVisible()) {
            this.recipeBookPage.renderTooltip(p_191876_3_, p_191876_4_);
            if (this.toggleRecipesBtn.isMouseOver()) {
                final String s1 = I18n.format(this.toggleRecipesBtn.isStateTriggered() ? "gui.recipebook.toggleRecipes.craftable" : "gui.recipebook.toggleRecipes.all", new Object[0]);
                if (this.mc.currentScreen != null) {
                    this.mc.currentScreen.drawHoveringText(s1, p_191876_3_, p_191876_4_);
                }
            }
            this.renderGhostRecipeTooltip(p_191876_1_, p_191876_2_, p_191876_3_, p_191876_4_);
        }
    }
    
    private void renderGhostRecipeTooltip(final int p_193015_1_, final int p_193015_2_, final int p_193015_3_, final int p_193015_4_) {
        ItemStack itemstack = null;
        for (int i = 0; i < this.ghostRecipe.size(); ++i) {
            final GhostRecipe.GhostIngredient ghostrecipe$ghostingredient = this.ghostRecipe.get(i);
            final int j = ghostrecipe$ghostingredient.getX() + p_193015_1_;
            final int k = ghostrecipe$ghostingredient.getY() + p_193015_2_;
            if (p_193015_3_ >= j && p_193015_4_ >= k && p_193015_3_ < j + 16 && p_193015_4_ < k + 16) {
                itemstack = ghostrecipe$ghostingredient.getItem();
            }
        }
        if (itemstack != null && this.mc.currentScreen != null) {
            this.mc.currentScreen.drawHoveringText(this.mc.currentScreen.getItemToolTip(itemstack), p_193015_3_, p_193015_4_);
        }
    }
    
    public void renderGhostRecipe(final int p_191864_1_, final int p_191864_2_, final boolean p_191864_3_, final float p_191864_4_) {
        this.ghostRecipe.render(this.mc, p_191864_1_, p_191864_2_, p_191864_3_, p_191864_4_);
    }
    
    public boolean mouseClicked(final int p_191862_1_, final int p_191862_2_, final int p_191862_3_) {
        if (this.isVisible()) {
            final Minecraft mc = this.mc;
            if (!Minecraft.player.isSpectator()) {
                if (this.recipeBookPage.mouseClicked(p_191862_1_, p_191862_2_, p_191862_3_, (this.width - 147) / 2 - this.xOffset, (this.height - 166) / 2, 147, 166)) {
                    final IRecipe irecipe = this.recipeBookPage.getLastClickedRecipe();
                    final RecipeList recipelist = this.recipeBookPage.getLastClickedRecipeList();
                    if (irecipe != null && recipelist != null) {
                        if (!recipelist.isCraftable(irecipe) && this.ghostRecipe.getRecipe() == irecipe) {
                            return false;
                        }
                        this.ghostRecipe.clear();
                        final PlayerControllerMP playerController = this.mc.playerController;
                        final Minecraft mc2 = this.mc;
                        final int windowId = Minecraft.player.openContainer.windowId;
                        final IRecipe p_194338_2_ = irecipe;
                        final boolean shiftKeyDown = GuiScreen.isShiftKeyDown();
                        final Minecraft mc3 = this.mc;
                        playerController.func_194338_a(windowId, p_194338_2_, shiftKeyDown, Minecraft.player);
                        if (!this.isOffsetNextToMainGUI() && p_191862_3_ == 0) {
                            this.setVisible(false);
                        }
                    }
                    return true;
                }
                if (p_191862_3_ != 0) {
                    return false;
                }
                if (this.searchBar.mouseClicked(p_191862_1_, p_191862_2_, p_191862_3_)) {
                    return true;
                }
                if (this.toggleRecipesBtn.mousePressed(this.mc, p_191862_1_, p_191862_2_)) {
                    final boolean flag = !this.recipeBook.isFilteringCraftable();
                    this.recipeBook.setFilteringCraftable(flag);
                    this.toggleRecipesBtn.setStateTriggered(flag);
                    this.toggleRecipesBtn.playPressSound(this.mc.getSoundHandler());
                    this.sendUpdateSettings();
                    this.updateCollections(false);
                    return true;
                }
                for (final GuiButtonRecipeTab guibuttonrecipetab : this.recipeTabs) {
                    if (guibuttonrecipetab.mousePressed(this.mc, p_191862_1_, p_191862_2_)) {
                        if (this.currentTab != guibuttonrecipetab) {
                            guibuttonrecipetab.playPressSound(this.mc.getSoundHandler());
                            this.currentTab.setStateTriggered(false);
                            (this.currentTab = guibuttonrecipetab).setStateTriggered(true);
                            this.updateCollections(true);
                        }
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }
    
    public boolean hasClickedOutside(final int p_193955_1_, final int p_193955_2_, final int p_193955_3_, final int p_193955_4_, final int p_193955_5_, final int p_193955_6_) {
        if (!this.isVisible()) {
            return true;
        }
        final boolean flag = p_193955_1_ < p_193955_3_ || p_193955_2_ < p_193955_4_ || p_193955_1_ >= p_193955_3_ + p_193955_5_ || p_193955_2_ >= p_193955_4_ + p_193955_6_;
        final boolean flag2 = p_193955_3_ - 147 < p_193955_1_ && p_193955_1_ < p_193955_3_ && p_193955_4_ < p_193955_2_ && p_193955_2_ < p_193955_4_ + p_193955_6_;
        return flag && !flag2 && !this.currentTab.mousePressed(this.mc, p_193955_1_, p_193955_2_);
    }
    
    public boolean keyPressed(final char typedChar, final int keycode) {
        if (this.isVisible()) {
            final Minecraft mc = this.mc;
            if (!Minecraft.player.isSpectator()) {
                if (keycode == 1 && !this.isOffsetNextToMainGUI()) {
                    this.setVisible(false);
                    return true;
                }
                if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat) && !this.searchBar.isFocused()) {
                    this.searchBar.setFocused(true);
                }
                else if (this.searchBar.textboxKeyTyped(typedChar, keycode)) {
                    final String s1 = this.searchBar.getText().toLowerCase(Locale.ROOT);
                    this.pirateRecipe(s1);
                    if (!s1.equals(this.lastSearch)) {
                        this.updateCollections(false);
                        this.lastSearch = s1;
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    
    private void pirateRecipe(final String text) {
        if ("excitedze".equals(text)) {
            final LanguageManager languagemanager = this.mc.getLanguageManager();
            final Language language = languagemanager.getLanguage("en_pt");
            if (languagemanager.getCurrentLanguage().compareTo(language) == 0) {
                return;
            }
            languagemanager.setCurrentLanguage(language);
            this.mc.gameSettings.language = language.getLanguageCode();
            this.mc.refreshResources();
            this.mc.fontRenderer.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.mc.gameSettings.forceUnicodeFont);
            this.mc.fontRenderer.setBidiFlag(languagemanager.isCurrentLanguageBidirectional());
            this.mc.gameSettings.saveOptions();
        }
    }
    
    private boolean isOffsetNextToMainGUI() {
        return this.xOffset == 86;
    }
    
    public void recipesUpdated() {
        this.updateTabs();
        if (this.isVisible()) {
            this.updateCollections(false);
        }
    }
    
    @Override
    public void recipesShown(final List<IRecipe> recipes) {
        for (final IRecipe irecipe : recipes) {
            final Minecraft mc = this.mc;
            Minecraft.player.removeRecipeHighlight(irecipe);
        }
    }
    
    public void setupGhostRecipe(final IRecipe p_193951_1_, final List<Slot> p_193951_2_) {
        final ItemStack itemstack = p_193951_1_.getRecipeOutput();
        this.ghostRecipe.setRecipe(p_193951_1_);
        this.ghostRecipe.addIngredient(Ingredient.fromStacks(itemstack), p_193951_2_.get(0).xPos, p_193951_2_.get(0).yPos);
        final int i = this.craftingSlots.getWidth();
        final int j = this.craftingSlots.getHeight();
        final int k = (p_193951_1_ instanceof ShapedRecipes) ? ((ShapedRecipes)p_193951_1_).getWidth() : i;
        int l = 1;
        final Iterator<Ingredient> iterator = p_193951_1_.getIngredients().iterator();
        for (int i2 = 0; i2 < j; ++i2) {
            for (int j2 = 0; j2 < k; ++j2) {
                if (!iterator.hasNext()) {
                    return;
                }
                final Ingredient ingredient = iterator.next();
                if (ingredient != Ingredient.EMPTY) {
                    final Slot slot = p_193951_2_.get(l);
                    this.ghostRecipe.addIngredient(ingredient, slot.xPos, slot.yPos);
                }
                ++l;
            }
            if (k < i) {
                l += i - k;
            }
        }
    }
    
    private void sendUpdateSettings() {
        if (this.mc.getConnection() != null) {
            this.mc.getConnection().sendPacket(new CPacketRecipeInfo(this.isVisible(), this.recipeBook.isFilteringCraftable()));
        }
    }
    
    static {
        RECIPE_BOOK = new ResourceLocation("textures/gui/recipe_book.png");
    }
}
