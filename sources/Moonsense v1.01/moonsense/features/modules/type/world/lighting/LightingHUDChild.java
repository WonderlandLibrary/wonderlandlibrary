// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.lighting;

import moonsense.enums.ModuleCategory;
import moonsense.config.utils.AnchorPoint;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.util.BlockPos;
import moonsense.ui.screen.settings.GuiHUDEditor;
import moonsense.features.SCModule;
import moonsense.features.SCAbstractRenderModule;

public class LightingHUDChild extends SCAbstractRenderModule
{
    public static LightingHUDChild INSTANCE;
    private final LightingModule module;
    private int width;
    private int height;
    
    public LightingHUDChild() {
        super("Lighting HUD", "");
        (LightingHUDChild.INSTANCE = this).setParentModule(LightingModule.INSTANCE);
        this.module = LightingModule.INSTANCE;
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
    
    @Override
    public void render(final float x, final float y) {
        if (this.getValue() == null) {
            return;
        }
        if (this.getValue() != null || (this.mc.currentScreen instanceof GuiHUDEditor && this.getDummyValue() != null)) {
            final BlockPos blockPos = new BlockPos(this.mc.func_175606_aa().posX, this.mc.func_175606_aa().getEntityBoundingBox().minY, this.mc.func_175606_aa().posZ);
            final Chunk chunk = this.mc.theWorld.getChunkFromBlockCoords(blockPos);
            String value;
            try {
                value = "Light: " + chunk.setLight(blockPos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, blockPos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, blockPos) + " block)";
            }
            catch (Exception e) {
                value = "Light: 12 (7 sky, 5 block)";
            }
            final String text = this.getFormat().replace("%value%", String.valueOf(value));
            this.width = this.mc.fontRendererObj.getStringWidth(text);
            this.height = this.mc.fontRendererObj.FONT_HEIGHT;
            if (this.module.lightingHUDBackground.getBoolean()) {
                this.width += this.module.lightingHUDBackgroundWidth.getInt() * 2;
                this.height += this.module.lightingHUDBackgroundHeight.getInt() * 2 - 2;
                this.renderBackground(x + this.width / 2.0f, y + this.height / 2.0f - this.mc.fontRendererObj.FONT_HEIGHT / 2.0f);
                final boolean isLowLight = chunk.setLight(blockPos, 0) < 8;
                this.drawCenteredString(text, x + this.width / 2.0f, y + this.height / 2.0f - (this.mc.fontRendererObj.FONT_HEIGHT - 2) / 2.0f, (this.module.lightingHUDColorNotifier.getBoolean() && isLowLight) ? this.module.lightingHUDNotifierColor.getColorObject() : this.module.lightingHUDTextColor.getColorObject(), this.module.lightingHUDTextShadow.getBoolean());
            }
            else {
                ++this.width;
                this.drawString(text, x + 1.0f, y + 1.0f, this.module.lightingHUDTextColor.getColorObject(), this.module.lightingHUDTextShadow.getBoolean());
            }
        }
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        this.render(x, y);
    }
    
    protected void renderBackground(final float x, final float y) {
        if (this.module.lightingHUDBorder.getBoolean()) {
            GuiUtils.drawRoundedOutline(x - this.width / 2, y - this.height / 2 + this.mc.fontRendererObj.FONT_HEIGHT / 2, x + this.width / 2, y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + this.height / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2, this.module.lightingHUDBackgroundRadius.getFloat(), this.module.lightingHUDBorderWidth.getFloat(), SCModule.getColor(this.module.lightingHUDBorderColor.getColorObject()));
        }
        GuiUtils.drawRoundedRect(x - this.width / 2, y - this.height / 2 + this.mc.fontRendererObj.FONT_HEIGHT / 2, x + this.width / 2, y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + this.height / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2, this.module.lightingHUDBackgroundRadius.getFloat(), SCModule.getColor(this.module.lightingHUDBackgroundColor.getColorObject()));
    }
    
    public String getFormat() {
        String bracketType = this.module.lightingHUDBrackets.getValue().get(this.module.lightingHUDBrackets.getInt() + 1);
        if (bracketType.equalsIgnoreCase("NONE")) {
            bracketType = "  ";
        }
        return String.valueOf(bracketType.charAt(0)) + "%value%" + bracketType.charAt(1);
    }
    
    public Object getValue() {
        final BlockPos blockPos = new BlockPos(this.mc.func_175606_aa().posX, this.mc.func_175606_aa().getEntityBoundingBox().minY, this.mc.func_175606_aa().posZ);
        final Chunk chunk = this.mc.theWorld.getChunkFromBlockCoords(blockPos);
        return "Light: " + chunk.setLight(blockPos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, blockPos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, blockPos) + " block)";
    }
    
    public Object getDummyValue() {
        return this.getValue();
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.TOP_CENTER;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.NEW;
    }
}
