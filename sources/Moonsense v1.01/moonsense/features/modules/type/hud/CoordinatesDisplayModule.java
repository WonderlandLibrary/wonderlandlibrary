// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.Entity;
import moonsense.enums.SwitchEnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import moonsense.enums.BiomeType;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.BlockPos;
import java.awt.Color;
import moonsense.features.SCModule;
import net.minecraft.client.Minecraft;
import moonsense.settings.Setting;
import net.minecraft.client.gui.FontRenderer;
import moonsense.features.SCAbstractRenderModule;

public class CoordinatesDisplayModule extends SCAbstractRenderModule
{
    private int width;
    private int height;
    private FontRenderer font;
    private final Setting showBackground;
    private final Setting textShadow;
    private final Setting border;
    private final Setting borderThickness;
    private final Setting mode;
    private final Setting showC;
    private final Setting showAxisLabels;
    private final Setting showBiome;
    private final Setting showBiomeLabel;
    private final Setting dynamicBiomeColor;
    private final Setting showDirection;
    private final Setting showDirectionModification;
    private final Setting baseColor;
    private final Setting backgroundColor;
    private final Setting borderColor;
    private final Setting xLabelColor;
    private final Setting xValueColor;
    private final Setting yLabelColor;
    private final Setting yValueColor;
    private final Setting zLabelColor;
    private final Setting zValueColor;
    private final Setting cLabelColor;
    private final Setting cValueColor;
    private final Setting directionColor;
    private final Setting biomeColor;
    private final Setting biomeLabelColor;
    
    public CoordinatesDisplayModule() {
        super("Coordinates", "Displays your coordinates on the HUD.");
        this.font = Minecraft.getMinecraft().fontRendererObj;
        this.showBackground = new Setting(this, "Show Background").setDefault(true);
        this.textShadow = new Setting(this, "Text Shadow").setDefault(true);
        this.border = new Setting(this, "Border").setDefault(false);
        this.borderThickness = new Setting(this, "Border Thickness").setDefault(0.5f).setRange(0.5f, 3.0f, 0.1f);
        new Setting(this, "Render Options");
        this.mode = new Setting(this, "Render Mode").setDefault(0).setRange("Vertical", "Horizontal");
        this.showC = new Setting(this, "Show C (Active Renderers)").setDefault(false);
        this.showAxisLabels = new Setting(this, "Show Axis Labels").setDefault(true);
        this.showBiome = new Setting(this, "Show Biome").setDefault(true);
        this.showBiomeLabel = new Setting(this, "Show Biome Label").setDefault(true);
        this.dynamicBiomeColor = new Setting(this, "Dynamic Biome Color").setDefault(true);
        this.showDirection = new Setting(this, "Show Direction").setDefault(true);
        this.showDirectionModification = new Setting(this, "Show Direction Modification").setDefault(true);
        new Setting(this, "Color Options");
        this.baseColor = new Setting(this, "Base Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.backgroundColor = new Setting(this, "Background Color").setDefault(new Color(0, 0, 0, 75).getRGB(), 0);
        this.borderColor = new Setting(this, "Border Color").setDefault(new Color(0, 0, 0, 75).getRGB(), 0);
        this.xLabelColor = new Setting(this, "X Label Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.xValueColor = new Setting(this, "X Value Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.yLabelColor = new Setting(this, "Y Label Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.yValueColor = new Setting(this, "Y Value Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.zLabelColor = new Setting(this, "Z Label Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.zValueColor = new Setting(this, "Z Value Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.cLabelColor = new Setting(this, "C Label Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.cValueColor = new Setting(this, "C Value Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.directionColor = new Setting(this, "Direction Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.biomeColor = new Setting(this, "Biome Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.biomeLabelColor = new Setting(this, "Biome Label Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
    }
    
    @Override
    public int getWidth() {
        if (this.font == null) {
            this.font = Minecraft.getMinecraft().fontRendererObj;
        }
        final String mode = this.mode.getValue().get(this.mode.getInt() + 1);
        if (!mode.equals("Vertical")) {
            int len = 0;
            len += this.font.getStringWidth("X: " + (int)this.mc.thePlayer.posX + "  ");
            len += this.font.getStringWidth("Y: " + (int)this.mc.thePlayer.posY + "  ");
            len += this.font.getStringWidth("Z: " + (int)this.mc.thePlayer.posZ + "  ");
            if (this.showBiome.getBoolean()) {
                final BlockPos var1 = new BlockPos(this.mc.func_175606_aa().posX, this.mc.func_175606_aa().getEntityBoundingBox().minY, this.mc.func_175606_aa().posZ);
                if (this.mc.theWorld != null && this.mc.theWorld.isBlockLoaded(var1)) {
                    final Chunk var2 = this.mc.theWorld.getChunkFromBlockCoords(var1);
                    len += this.font.getStringWidth("Biome: " + var2.getBiome(var1, this.mc.theWorld.getWorldChunkManager()).biomeName) + 2;
                }
            }
            if (this.showC.getBoolean()) {
                final String[] debugString = this.mc.renderGlobal.getDebugInfoRenders().split("D:");
                final String cCounterString = debugString[0].substring(3);
                len += this.font.getStringWidth("C: " + cCounterString);
            }
            return len + 8;
        }
        final BlockPos var3 = new BlockPos(this.mc.func_175606_aa().posX, this.mc.func_175606_aa().getEntityBoundingBox().minY, this.mc.func_175606_aa().posZ);
        int biomeLength;
        if (this.mc.theWorld != null && this.mc.theWorld.isBlockLoaded(var3) && this.showBiome.getBoolean()) {
            final Chunk var4 = this.mc.theWorld.getChunkFromBlockCoords(var3);
            biomeLength = this.font.getStringWidth(String.valueOf(this.showBiomeLabel.getBoolean() ? "Biome: " : "") + var4.getBiome(var3, this.mc.theWorld.getWorldChunkManager()).biomeName) + 7;
        }
        else {
            biomeLength = 40;
        }
        int cCounterLength;
        if (this.showC.getBoolean()) {
            final String[] debugString2 = this.mc.renderGlobal.getDebugInfoRenders().split("D:");
            final String cCounterString2 = debugString2[0];
            cCounterLength = this.font.getStringWidth(cCounterString2);
        }
        else {
            cCounterLength = 0;
        }
        if (this.showDirection.getBoolean() || this.showDirectionModification.getBoolean()) {
            if (biomeLength > cCounterLength) {
                return biomeLength + 20;
            }
            if (cCounterLength > biomeLength) {
                return cCounterLength + 20;
            }
            return biomeLength + 20;
        }
        else {
            if (biomeLength > cCounterLength) {
                return biomeLength + 20 - this.font.getStringWidth("+Z") - 4;
            }
            if (cCounterLength > biomeLength) {
                return cCounterLength + 20 - this.font.getStringWidth("+Z") - 4;
            }
            return biomeLength + 20 - this.font.getStringWidth("+Z") - 4;
        }
    }
    
    @Override
    public int getHeight() {
        final String mode = this.mode.getValue().get(this.mode.getInt() + 1);
        if (mode.equals("Vertical")) {
            int h = 54;
            final BlockPos var1 = new BlockPos(this.mc.func_175606_aa().posX, this.mc.func_175606_aa().getEntityBoundingBox().minY, this.mc.func_175606_aa().posZ);
            if (this.mc.theWorld == null || !this.mc.theWorld.isBlockLoaded(var1)) {
                h -= this.font.FONT_HEIGHT;
            }
            if (this.font == null) {
                this.font = Minecraft.getMinecraft().fontRendererObj;
            }
            if (!this.showC.getBoolean()) {
                h -= this.font.FONT_HEIGHT;
            }
            if (!this.showBiome.getBoolean()) {
                h -= this.font.FONT_HEIGHT;
            }
            return h;
        }
        return this.font.FONT_HEIGHT + 8;
    }
    
    @Override
    public void render(final float x, final float y) {
        if (this.font == null) {
            this.font = Minecraft.getMinecraft().fontRendererObj;
        }
        final String mode = this.mode.getValue().get(this.mode.getInt() + 1);
        if (mode.equals("Vertical")) {
            int offset = 34;
            final BlockPos var1 = new BlockPos(this.mc.func_175606_aa().posX, this.mc.func_175606_aa().getEntityBoundingBox().minY, this.mc.func_175606_aa().posZ);
            if (this.showBackground.getBoolean()) {
                GuiUtils.drawRect(x, y, x + this.getWidth(), y + this.getHeight(), this.backgroundColor.getColorObject());
            }
            if (this.border.getBoolean()) {
                GuiUtils.drawRoundedOutline(x, y, x + this.getWidth(), y + this.getHeight(), 0.0f, this.borderThickness.getFloat(), this.borderColor.getColorObject());
            }
            final String dirFacing = this.getBlockFacing();
            if (this.showDirectionModification.getBoolean()) {
                if (dirFacing.equals("+Z") || dirFacing.equals("-Z")) {
                    this.drawString(dirFacing, x + this.getWidth() - 14.0f, y + 24.0f, this.directionColor.getColorObject(), this.textShadow.getBoolean());
                }
                if (dirFacing.equals("+X") || dirFacing.equals("-X")) {
                    this.drawString(dirFacing, x + this.getWidth() - 14.0f, y + 4.0f, this.directionColor.getColorObject(), this.textShadow.getBoolean());
                }
            }
            if (this.showAxisLabels.getBoolean()) {
                this.drawString("X: " + (int)this.mc.thePlayer.posX, x + 4.0f, y + 4.0f, this.xLabelColor.getColorObject(), this.textShadow.getBoolean());
                this.drawString(new StringBuilder().append((int)this.mc.thePlayer.posX).toString(), x + 4.0f + this.font.getStringWidth("X: "), y + 4.0f, this.xValueColor.getColorObject(), this.textShadow.getBoolean());
                this.drawString("Y: " + (int)this.mc.thePlayer.posY, x + 4.0f, y + 14.0f, this.yLabelColor.getColorObject(), this.textShadow.getBoolean());
                this.drawString(new StringBuilder().append((int)this.mc.thePlayer.posY).toString(), x + 4.0f + this.font.getStringWidth("Y: "), y + 14.0f, this.yValueColor.getColorObject(), this.textShadow.getBoolean());
                if (this.showDirection.getBoolean()) {
                    this.drawString(this.getFacing(), x + this.getWidth() - 8.0f, y + 14.0f, this.directionColor.getColorObject(), this.textShadow.getBoolean());
                }
                this.drawString("Z: " + (int)this.mc.thePlayer.posZ, x + 4.0f, y + 24.0f, this.zLabelColor.getColorObject(), this.textShadow.getBoolean());
                this.drawString(new StringBuilder().append((int)this.mc.thePlayer.posZ).toString(), x + 4.0f + this.font.getStringWidth("Z: "), y + 24.0f, this.zValueColor.getColorObject(), this.textShadow.getBoolean());
            }
            else {
                this.drawString(new StringBuilder().append((int)this.mc.thePlayer.posX).toString(), x + 4.0f, y + 4.0f, this.xValueColor.getColorObject(), this.textShadow.getBoolean());
                this.drawString(new StringBuilder().append((int)this.mc.thePlayer.posY).toString(), x + 4.0f, y + 14.0f, this.yValueColor.getColorObject(), this.textShadow.getBoolean());
                if (this.showDirection.getBoolean()) {
                    this.drawString(this.getFacing(), x + this.getWidth() - 8.0f, y + 14.0f, this.directionColor.getColorObject(), this.textShadow.getBoolean());
                }
                this.drawString(new StringBuilder().append((int)this.mc.thePlayer.posZ).toString(), x + 4.0f, y + 24.0f, this.zValueColor.getColorObject(), this.textShadow.getBoolean());
            }
            if (this.showBiome.getBoolean() && this.mc.theWorld != null && this.mc.theWorld.isBlockLoaded(var1)) {
                final BiomeGenBase var2 = (this.mc.theWorld != null) ? this.mc.theWorld.getChunkFromBlockCoords(var1).getBiome(var1, this.mc.theWorld.getWorldChunkManager()) : null;
                final BiomeType biomeType = BiomeType.getBiomeType(var2);
                final int biomeColor = this.dynamicBiomeColor.getBoolean() ? getBiomeColor(biomeType) : SCModule.getColor(this.biomeColor.getColorObject());
                if (this.showBiomeLabel.getBoolean()) {
                    this.drawString("Biome: ", x + 4.0f, y + offset, this.biomeLabelColor.getColorObject(), this.textShadow.getBoolean());
                    if (this.dynamicBiomeColor.getBoolean()) {
                        GuiUtils.drawString(biomeType.getBiomeName(), x + 2.0f + this.font.getStringWidth("Biome: "), y + offset, biomeColor, this.textShadow.getBoolean());
                    }
                    else {
                        this.drawString(biomeType.getBiomeName(), x + 2.0f + this.font.getStringWidth("Biome: "), y + offset, this.biomeColor.getColorObject(), this.textShadow.getBoolean());
                    }
                }
                else if (this.dynamicBiomeColor.getBoolean()) {
                    GuiUtils.drawString(biomeType.getBiomeName(), x + 4.0f, y + offset, biomeColor, this.textShadow.getBoolean());
                }
                else {
                    this.drawString(biomeType.getBiomeName(), x + 4.0f, y + offset, this.biomeColor.getColorObject(), this.textShadow.getBoolean());
                }
                offset += 10;
            }
            if (this.showC.getBoolean()) {
                final String[] debugString = this.mc.renderGlobal.getDebugInfoRenders().split("D:");
                final String cCounterString = debugString[0].substring(3);
                this.drawString("C: " + cCounterString, x + 4.0f, y + offset, this.cLabelColor.getColorObject(), this.textShadow.getBoolean());
            }
        }
        else {
            String s = "";
            s = String.valueOf(s) + "X: " + (int)this.mc.thePlayer.posX + "  ";
            s = String.valueOf(s) + "Y: " + (int)this.mc.thePlayer.posY + "  ";
            s = String.valueOf(s) + "Z: " + (int)this.mc.thePlayer.posZ + "  ";
            if (this.showBiome.getBoolean()) {
                final BlockPos var1 = new BlockPos(this.mc.func_175606_aa().posX, this.mc.func_175606_aa().getEntityBoundingBox().minY, this.mc.func_175606_aa().posZ);
                if (this.mc.theWorld != null && this.mc.theWorld.isBlockLoaded(var1)) {
                    final Chunk var3 = this.mc.theWorld.getChunkFromBlockCoords(var1);
                    s = String.valueOf(s) + "Biome: " + var3.getBiome(var1, this.mc.theWorld.getWorldChunkManager()).biomeName + "  ";
                }
            }
            if (this.showC.getBoolean()) {
                final String[] debugString2 = this.mc.renderGlobal.getDebugInfoRenders().split("D:");
                final String cCounterString2 = debugString2[0].substring(3);
                s = String.valueOf(s) + "C: " + cCounterString2;
            }
            if (this.showBackground.getBoolean()) {
                GuiUtils.drawRect(x, y, x + this.getWidth(), y + this.getHeight(), this.backgroundColor.getColorObject());
            }
            if (this.border.getBoolean()) {
                GuiUtils.drawRoundedOutline(x, y, x + this.getWidth(), y + this.getHeight(), 0.0f, this.borderThickness.getFloat(), this.borderColor.getColorObject());
            }
            GuiUtils.drawString(s, x + 4.0f, y + 4.5f, -1, this.textShadow.getBoolean());
        }
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        this.render(x, y);
    }
    
    public String getBlockFacing() {
        final Entity en = this.mc.func_175606_aa();
        final EnumFacing facing = en.func_174811_aO();
        final String def = "Invalid";
        switch (SwitchEnumFacing.field_178907_a[facing.ordinal()]) {
            case 1: {
                return "-Z";
            }
            case 2: {
                return "+Z";
            }
            case 3: {
                return "-X";
            }
            case 4: {
                return "+X";
            }
            default: {
                return def;
            }
        }
    }
    
    public String getFacing() {
        final Entity entity = this.mc.func_175606_aa();
        final String facing = entity.func_174811_aO().getName();
        if (facing.equals("north")) {
            return "N";
        }
        if (facing.equals("south")) {
            return "S";
        }
        if (facing.equals("east")) {
            return "E";
        }
        if (facing.equals("west")) {
            return "W";
        }
        return "?";
    }
    
    private static int getBiomeColor(final BiomeType var0) {
        switch (var0) {
            case FLOWER_FOREST: {
                return -32787;
            }
            case SUNFLOWER_PLAINS: {
                return -10240;
            }
            case OCEAN:
            case RIVER:
            case DEEP_OCEAN: {
                return -16752657;
            }
            case PLAINS: {
                return -10510539;
            }
            case DESERT:
            case BEACH:
            case DESERT_HILLS:
            case DESERT_LAKES: {
                return -1516367;
            }
            case MOUNTAINS:
            case MOUNTAIN_EDGE:
            case STONE_SHORE:
            case WOODED_MOUNTAINS:
            case GRAVELLY_MOUNTAINS:
            case MODIFIED_GRAVELLY_MOUNTAINS: {
                return -7368817;
            }
            case FOREST:
            case WOODED_HILLS: {
                return -11629531;
            }
            case TAIGA:
            case TAIGA_HILLS:
            case GIANT_TREE_TAIGA:
            case GIANT_TREE_TAIGA_HILLS:
            case TAIGA_MOUNTAINS:
            case GIANT_SPRUCE_TAIGA:
            case GIANT_SPRUCE_TAIGA_HILLS: {
                return -11637939;
            }
            case SWAMP:
            case SWAMP_HILLS: {
                return -12295900;
            }
            case NETHER_WASTES:
            case CRIMSON_FOREST: {
                return -7729393;
            }
            case SOUL_SAND_VALLEY: {
                return -10270148;
            }
            case WARPED_FOREST: {
                return -15758473;
            }
            case BASALT_DELTAS: {
                return -11118485;
            }
            case THE_END:
            case SMALL_END_ISLANDS:
            case END_MIDLANDS:
            case END_HIGHLANDS:
            case END_BARRENS:
            case THE_VOID: {
                return -9568091;
            }
            case FROZEN_OCEAN:
            case FROZEN_RIVER:
            case SNOWY_TUNDRA:
            case SNOWY_MOUNTAINS:
            case SNOWY_BEACH:
            case SNOWY_TAIGA:
            case SNOWY_TAIGA_HILLS:
            case COLD_OCEAN:
            case DEEP_COLD_OCEAN:
            case DEEP_FROZEN_OCEAN:
            case ICE_SPIKES:
            case SNOWY_TAIGA_MOUNTAINS: {
                return -6308109;
            }
            case MUSHROOM_FIELDS:
            case MUSHROOM_FIELD_SHORE: {
                return -9080700;
            }
            case JUNGLE:
            case JUNGLE_HILLS:
            case JUNGLE_EDGE:
            case MODIFIED_JUNGLE:
            case MODIFIED_JUNGLE_EDGE: {
                return -14913786;
            }
            case BIRCH_FOREST:
            case BIRCH_FOREST_HILLS:
            case TALL_BIRCH_FOREST:
            case TALL_BIRCH_HILLS: {
                return -3223858;
            }
            case DARK_FOREST:
            case DARK_FOREST_HILLS: {
                return -13145823;
            }
            case SAVANNA:
            case SAVANNA_PLATEAU:
            case SHATTERED_SAVANNA:
            case SHATTERED_SAVANNA_PLATEAU: {
                return -8225734;
            }
            case BADLANDS:
            case WOODED_BADLANDS_PLATEAU:
            case BADLANDS_PLATEAU:
            case ERODED_BADLANDS:
            case MODIFIED_WOODED_BADLANDS_PLATEAU:
            case MODIFIED_BADLANDS_PLATEAU: {
                return -5022683;
            }
            case WARM_OCEAN:
            case LUKEWARM_OCEAN:
            case DEEP_WARM_OCEAN:
            case DEEP_LUKEWARM_OCEAN: {
                return -16711727;
            }
            case BAMBOO_JUNGLE:
            case BAMBOO_JUNGLE_HILLS: {
                return -9925062;
            }
            default: {
                return -1118465;
            }
        }
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.TOP_LEFT;
    }
}
