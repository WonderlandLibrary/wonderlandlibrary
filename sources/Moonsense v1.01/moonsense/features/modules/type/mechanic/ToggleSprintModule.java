// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.mechanic;

import net.minecraft.potion.Potion;
import moonsense.config.ModuleConfig;
import java.text.DecimalFormat;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import moonsense.ui.screen.settings.GuiHUDEditor;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import moonsense.features.SCDefaultRenderModule;

public class ToggleSprintModule extends SCDefaultRenderModule
{
    private static ToggleSprintModule instance;
    private final Setting toggleSprint;
    private final Setting toggleSneak;
    private final Setting doubleTap;
    private final Setting keyHoldDelay;
    private final Setting flyBoost;
    private final Setting flyBoostAmount;
    private final Setting showHUD;
    private final Setting showRidingText;
    private final Setting showDescendingText;
    private String textToRender;
    private int width;
    private int height;
    
    public ToggleSprintModule() {
        super("Toggle Sprint", "Make the sprinting and sneaking keys toggleable instead of needing to be held down.", 16);
        this.textToRender = "";
        ToggleSprintModule.instance = this;
        this.settings.remove(this.brackets);
        new Setting(this, "General Options");
        this.toggleSprint = new Setting(this, "Toggle Sprint").setDefault(true);
        this.toggleSneak = new Setting(this, "Toggle Sneak").setDefault(true);
        this.doubleTap = new Setting(this, "Double Tap").setDefault(true);
        this.keyHoldDelay = new Setting(this, "Key Hold Delay").setDefault(1).setRange(1, 20, 1);
        new Setting(this, "Fly Boost Options");
        this.flyBoost = new Setting(this, "Fly Boost").setDefault(true);
        this.flyBoostAmount = new Setting(this, "Fly Boost Amount").setDefault(4).setRange(2, 8, 1);
        new Setting(this, "HUD Options");
        this.showHUD = new Setting(this, "Show HUD").setDefault(true);
        this.showRidingText = new Setting(this, "Show Riding Text").setDefault(true);
        this.showDescendingText = new Setting(this, "Show Decending Text").setDefault(true);
    }
    
    @Override
    public void render(final float x, final float y) {
        if (this.mc.currentScreen instanceof GuiHUDEditor) {
            this.renderDummy(x, y);
        }
        else {
            this.textToRender = this.mc.thePlayer.movementInput.getDisplayText();
            if (this.showHUD.getBoolean()) {
                this.calculateDimensionsAndRender(this.textToRender, x, y);
            }
            else {
                this.width = this.mc.fontRendererObj.getStringWidth(this.textToRender);
            }
        }
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        this.textToRender = this.mc.thePlayer.movementInput.getDisplayText();
        if (this.showHUD.getBoolean()) {
            this.calculateDimensionsAndRender("[Sneaking (Toggled)]", x, y);
        }
        else {
            this.width = this.mc.fontRendererObj.getStringWidth("[Sneaking (Toggled)]");
        }
    }
    
    @Override
    protected void renderBackground(final float x, final float y) {
        if (this.border.getBoolean()) {
            GuiUtils.drawRoundedOutline(x - this.width / 2, y - this.height / 2 + this.mc.fontRendererObj.FONT_HEIGHT / 2, x + this.width / 2, y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + this.height / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2, 0.0f, this.borderWidth.getFloat(), SCModule.getColor(this.borderColor.getColorObject()));
        }
        GuiUtils.drawRect(x - this.width / 2, y - this.height / 2 + this.mc.fontRendererObj.FONT_HEIGHT / 2, x + this.width / 2, y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + this.height / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2, SCModule.getColor(this.backgroundColor.getColorObject()));
    }
    
    private void calculateDimensionsAndRender(final String text, final float x, final float y) {
        if (text.isEmpty()) {
            return;
        }
        this.width = this.mc.fontRendererObj.getStringWidth(text);
        this.height = this.mc.fontRendererObj.FONT_HEIGHT;
        if (this.background.getBoolean()) {
            this.width += this.backgroundWidth.getInt() * 2;
            this.height += this.backgroundHeight.getInt() * 2 - 2;
            this.renderBackground(x + this.width / 2.0f, y + this.height / 2.0f - this.mc.fontRendererObj.FONT_HEIGHT / 2.0f);
            this.drawCenteredString(text, x + this.width / 2.0f, y + this.height / 2.0f - (this.mc.fontRendererObj.FONT_HEIGHT - 2) / 2.0f, this.textColor.getColorObject(), this.textShadow.getBoolean());
        }
        else {
            ++this.width;
            this.drawString(text, x + 1.0f, y + 1.0f, this.textColor.getColorObject(), this.textShadow.getBoolean());
        }
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
    public Object getValue() {
        return null;
    }
    
    public static ToggleSprintModule getInstance() {
        return ToggleSprintModule.instance;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.MECHANIC;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.TOP_RIGHT;
    }
    
    public static final class ClientMovementInput extends MovementInput
    {
        private boolean sprint;
        private GameSettings gameSettings;
        private int sneakWasPressed;
        private int sprintWasPressed;
        private EntityPlayerSP player;
        private float originalFlySpeed;
        private float boostedFlySpeed;
        private Minecraft mc;
        private static final DecimalFormat df;
        
        static {
            df = new DecimalFormat("#.0");
        }
        
        public ClientMovementInput(final GameSettings gameSettings) {
            this.sprint = false;
            this.sneakWasPressed = 0;
            this.sprintWasPressed = 0;
            this.originalFlySpeed = -1.0f;
            this.boostedFlySpeed = 0.0f;
            this.gameSettings = gameSettings;
            this.mc = Minecraft.getMinecraft();
        }
        
        @Override
        public void updatePlayerMoveState() {
            this.player = this.mc.thePlayer;
            this.moveStrafe = 0.0f;
            this.moveForward = 0.0f;
            final int keyHoldTicks = ToggleSprintModule.instance.keyHoldDelay.getInt();
            final float flyBoostFactor = (float)ToggleSprintModule.instance.flyBoostAmount.getInt();
            final boolean flyBoost = ToggleSprintModule.instance.flyBoost.getBoolean();
            if (this.gameSettings.keyBindForward.getIsKeyPressed()) {
                ++this.moveForward;
            }
            if (this.gameSettings.keyBindBack.getIsKeyPressed()) {
                --this.moveForward;
            }
            if (this.gameSettings.keyBindLeft.getIsKeyPressed()) {
                ++this.moveStrafe;
            }
            if (this.gameSettings.keyBindRight.getIsKeyPressed()) {
                --this.moveStrafe;
            }
            this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
            if (ModuleConfig.INSTANCE.isEnabled(ToggleSprintModule.instance)) {
                if (this.gameSettings.keyBindSneak.getIsKeyPressed()) {
                    if (this.sneakWasPressed == 0) {
                        if (this.sneak) {
                            this.sneakWasPressed = -1;
                        }
                        else if (this.player.isRiding() || this.player.capabilities.isFlying) {
                            this.sneakWasPressed = keyHoldTicks + 1;
                        }
                        else {
                            this.sneakWasPressed = 1;
                        }
                        this.sneak = !this.sneak;
                    }
                    else if (this.sneakWasPressed > 0) {
                        ++this.sneakWasPressed;
                    }
                }
                else {
                    if (keyHoldTicks > 0 && this.sneakWasPressed > keyHoldTicks) {
                        this.sneak = false;
                    }
                    this.sneakWasPressed = 0;
                }
            }
            else {
                this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
            }
            if (this.sneak) {
                this.moveStrafe *= 0.3f;
                this.moveForward *= 0.3f;
            }
            if (ModuleConfig.INSTANCE.isEnabled(ToggleSprintModule.instance)) {
                if (this.gameSettings.keyBindSprint.getIsKeyPressed()) {
                    if (this.sprintWasPressed == 0) {
                        if (this.sprint) {
                            this.sprintWasPressed = -1;
                        }
                        else if (this.player.capabilities.isFlying) {
                            this.sprintWasPressed = keyHoldTicks + 1;
                        }
                        else {
                            this.sprintWasPressed = 1;
                        }
                        this.sprint = !this.sprint;
                    }
                    else if (this.sprintWasPressed > 0) {
                        ++this.sprintWasPressed;
                    }
                }
                else {
                    if (keyHoldTicks > 0 && this.sprintWasPressed > keyHoldTicks) {
                        this.sprint = false;
                    }
                    this.sprintWasPressed = 0;
                }
            }
            else {
                this.sprint = false;
            }
            if (this.sprint && this.moveForward == 1.0f && this.player.onGround && !this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness)) {
                this.player.setSprinting(true);
            }
            if (flyBoost && this.player.capabilities.isCreativeMode && this.player.capabilities.isFlying && this.mc.func_175606_aa() == this.player && this.sprint) {
                if (this.originalFlySpeed < 0.0f || this.player.capabilities.getFlySpeed() != this.boostedFlySpeed) {
                    this.originalFlySpeed = this.player.capabilities.getFlySpeed();
                }
                this.boostedFlySpeed = this.originalFlySpeed * flyBoostFactor;
                this.player.capabilities.setFlySpeed(this.boostedFlySpeed);
                if (this.sneak) {
                    final EntityPlayerSP player = this.player;
                    player.motionY -= 0.15 * (flyBoostFactor - 1.0f);
                }
                if (this.jump) {
                    final EntityPlayerSP player2 = this.player;
                    player2.motionY += 0.15 * (flyBoostFactor - 1.0f);
                }
            }
            else {
                if (this.player.capabilities.getFlySpeed() == this.boostedFlySpeed) {
                    this.player.capabilities.setFlySpeed(this.originalFlySpeed);
                }
                this.originalFlySpeed = -1.0f;
            }
        }
        
        public String getDisplayText() {
            String displayText = "";
            final boolean isFlying = this.mc.thePlayer.capabilities.isFlying;
            final boolean isRiding = this.mc.thePlayer.isRiding();
            final boolean isHoldingSneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
            final boolean isHoldingSprint = this.gameSettings.keyBindSprint.getIsKeyPressed();
            if (isFlying) {
                if (this.originalFlySpeed > 0.0f) {
                    displayText = String.valueOf(displayText) + "[Flying (" + ClientMovementInput.df.format(this.boostedFlySpeed / this.originalFlySpeed) + "x Boost)]  ";
                }
                else {
                    displayText = String.valueOf(displayText) + "[Flying]  ";
                }
            }
            if (ToggleSprintModule.instance.showRidingText.getBoolean() && isRiding) {
                displayText = String.valueOf(displayText) + "[Riding]  ";
            }
            if (this.sneak) {
                if (ToggleSprintModule.instance.showDescendingText.getBoolean() && isFlying) {
                    displayText = String.valueOf(displayText) + "[Descending]  ";
                }
                else if (isHoldingSneak) {
                    displayText = String.valueOf(displayText) + "[Sneaking (Key Held)]  ";
                }
                else if (ToggleSprintModule.instance.toggleSneak.getBoolean()) {
                    displayText = String.valueOf(displayText) + "[Sneaking (Toggled)]  ";
                }
                else {
                    this.sneak = false;
                }
            }
            else if (this.sprint && !isFlying && !isRiding) {
                if (isHoldingSprint) {
                    displayText = String.valueOf(displayText) + "[Sprinting (Key Held)]  ";
                }
                else if (ToggleSprintModule.instance.toggleSprint.getBoolean()) {
                    displayText = String.valueOf(displayText) + "[Sprinting (Toggled)]  ";
                }
                else {
                    this.sprint = false;
                }
            }
            return displayText.trim();
        }
    }
}
