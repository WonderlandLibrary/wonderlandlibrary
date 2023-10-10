package ravenweave.client.module.modules.combat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import ravenweave.client.event.impl.AttackEntityEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.*;
import ravenweave.client.utils.ReflectionUtils;
import ravenweave.client.utils.SoundUtils;
import ravenweave.client.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class LeftClicker extends Module {
    public static SliderSetting jitterLeft, hitSelectTick;
    public static TickSetting weaponOnly, breakBlocks;
    public static DoubleSliderSetting leftCPS;
    public static TickSetting inventoryFill, hitSelect;
    public static ComboSetting<SoundMode> soundMode;

    public static boolean autoClickerEnabled;
    private boolean leftDown;
    private long leftDownTime;
    private long leftUpTime;
    private long leftk;
    private long leftl;
    private double leftm;
    private boolean leftn;
    private boolean breakHeld;
    private boolean hitSelected;
    private Random rand;
    private Method playerMouseInput;
    public EntityLivingBase target;

    public LeftClicker() {
        super("Left Clicker", ModuleCategory.combat);

        this.registerSetting(new DescriptionSetting("Best with delay remover."));
        this.registerSetting(leftCPS = new DoubleSliderSetting("Left CPS", 9, 13, 1, 60, 0.5));
        this.registerSetting(jitterLeft = new SliderSetting("Jitter left", 0.0D, 0.0D, 3.0D, 0.1D));
        this.registerSetting(inventoryFill = new TickSetting("Inventory fill", false));
        this.registerSetting(weaponOnly = new TickSetting("Weapon only", false));
        this.registerSetting(breakBlocks = new TickSetting("Break blocks", false));
        this.registerSetting(hitSelect = new TickSetting("Hit Select", false));
        this.registerSetting(hitSelectTick = new SliderSetting("HitSelect Hurttick", 7, 1, 10, 1));
        this.registerSetting(soundMode = new ComboSetting<>("Click sound", SoundMode.NONE));

        try {
            this.playerMouseInput = ReflectionUtils.findMethod(GuiScreen.class, null,
                    "mouseClicked", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (this.playerMouseInput != null)
			this.playerMouseInput.setAccessible(true);
        autoClickerEnabled = false;
    }

    @Override
    public void onEnable() {
        if (this.playerMouseInput == null)
			this.disable();

        this.rand = new Random();
        autoClickerEnabled = true;
    }

    @Override
    public void onDisable() {
        this.leftDownTime = 0L;
        this.leftUpTime = 0L;
        autoClickerEnabled = false;
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent e) {
        if (e.target instanceof EntityLivingBase) {
            target = (EntityLivingBase) e.target;
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent ev) {
        if ((!Utils.Client.currentScreenMinecraft()
                && !(Minecraft.getMinecraft().currentScreen instanceof GuiInventory)
                && !(Minecraft.getMinecraft().currentScreen instanceof GuiChest)
        ) || shouldNotClick())
            return;

        click();
    }

    private boolean shouldNotClick() {
        if (!Mouse.isButtonDown(0))
			hitSelected = false;

        if (hitSelect.isToggled())
			if (hitSelected || ((mc.thePlayer.hurtTime != 0) && (mc.thePlayer.hurtTime > hitSelectTick.getInput())))
				hitSelected = true;
			else
				return true;
        return false;
    }

    private void click() {
        if ((mc.currentScreen != null) || !mc.inGameHasFocus) {
            doInventoryClick();
            return;
        }

        Mouse.poll();
        if (!Mouse.isButtonDown(0) && !leftDown) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
            Utils.Client.setMouseButtonState(0, false);
        }
        if (Mouse.isButtonDown(0) || leftDown) {
            if (weaponOnly.isToggled() && !Utils.Player.isPlayerHoldingWeapon())
				return;
            this.leftClickExecute(mc.gameSettings.keyBindAttack.getKeyCode());
        }
    }

    public void leftClickExecute(int key) {
        if (breakBlock())
            return;

        if (jitterLeft.getInput() > 0.0D) {
            double a = jitterLeft.getInput() * 0.45D;
            EntityPlayerSP entityPlayer;
            if (this.rand.nextBoolean()) {
                entityPlayer = mc.thePlayer;
                entityPlayer.rotationYaw = (float) ((double) entityPlayer.rotationYaw
                        + ((double) this.rand.nextFloat() * a));
            } else {
                entityPlayer = mc.thePlayer;
                entityPlayer.rotationYaw = (float) ((double) entityPlayer.rotationYaw
                        - ((double) this.rand.nextFloat() * a));
            }

            if (this.rand.nextBoolean()) {
                entityPlayer = mc.thePlayer;
                entityPlayer.rotationPitch = (float) ((double) entityPlayer.rotationPitch
                        + ((double) this.rand.nextFloat() * a * 0.45D));
            } else {
                entityPlayer = mc.thePlayer;
                entityPlayer.rotationPitch = (float) ((double) entityPlayer.rotationPitch
                        - ((double) this.rand.nextFloat() * a * 0.45D));
            }
        }

        if ((this.leftUpTime > 0L) && (this.leftDownTime > 0L)) {
            if ((System.currentTimeMillis() > this.leftUpTime) && leftDown) {
                if (soundMode.getMode() != SoundMode.NONE) {
                    if (SoundUtils.clip != null) SoundUtils.clip.stop();
                    SoundUtils.playSound(soundMode.getMode().name());
                }
                KeyBinding.setKeyBindState(key, true);
                KeyBinding.onTick(key);
                this.genLeftTimings();
                Utils.Client.setMouseButtonState(0, true);
                leftDown = false;
            } else if (System.currentTimeMillis() > this.leftDownTime) {
                KeyBinding.setKeyBindState(key, false);
                leftDown = true;
                Utils.Client.setMouseButtonState(0, false);
            }
        } else {
            this.genLeftTimings();
        }

    }

    public void genLeftTimings() {
        double clickSpeed = Utils.Client.ranModuleVal(leftCPS, this.rand) + (0.4D * this.rand.nextDouble());
        long delay = (int) Math.round(1000.0D / clickSpeed);
        if (System.currentTimeMillis() > this.leftk) {
            if (!this.leftn && (this.rand.nextInt(100) >= 85)) {
                this.leftn = true;
                this.leftm = 1.1D + (this.rand.nextDouble() * 0.15D);
            } else
				this.leftn = false;

            this.leftk = System.currentTimeMillis() + 500L + (long) this.rand.nextInt(1500);
        }

        if (this.leftn)
			delay = (long) ((double) delay * this.leftm);

        if (System.currentTimeMillis() > this.leftl) {
            if (this.rand.nextInt(100) >= 80)
				delay += 50L + (long) this.rand.nextInt(100);

            this.leftl = System.currentTimeMillis() + 500L + (long) this.rand.nextInt(1500);
        }

        this.leftUpTime = System.currentTimeMillis() + delay;
        this.leftDownTime = (System.currentTimeMillis() + (delay / 2L)) - (long) this.rand.nextInt(10);
    }

    private void inInvClick(GuiScreen guiScreen) {
        int mouseInGUIPosX = (Mouse.getX() * guiScreen.width) / mc.displayWidth;
        int mouseInGUIPosY = guiScreen.height - ((Mouse.getY() * guiScreen.height) / mc.displayHeight) - 1;

        try {
            this.playerMouseInput.invoke(guiScreen, mouseInGUIPosX, mouseInGUIPosY, 0);
        } catch (IllegalAccessException | InvocationTargetException ignored) {

        }

    }

    public boolean breakBlock() {
        if (breakBlocks.isToggled() && (mc.objectMouseOver != null)) {
            BlockPos p = mc.objectMouseOver.getBlockPos();

            if (p != null) {
                Block bl = mc.theWorld.getBlockState(p).getBlock();
                if ((bl != Blocks.air) && !(bl instanceof BlockLiquid)) {
                    if (!breakHeld) {
                        int e = mc.gameSettings.keyBindAttack.getKeyCode();
                        KeyBinding.setKeyBindState(e, true);
                        KeyBinding.onTick(e);
                        breakHeld = true;
                    }
                    return true;
                }
                if (breakHeld)
					breakHeld = false;
            }
        }
        return false;
    }

    public void doInventoryClick() {
        if (inventoryFill.isToggled()
                && ((mc.currentScreen instanceof GuiInventory) || (mc.currentScreen instanceof GuiChest)))
			if (!Mouse.isButtonDown(0) || (!Keyboard.isKeyDown(54) && !Keyboard.isKeyDown(42))) {
                this.leftDownTime = 0L;
                this.leftUpTime = 0L;
            } else if ((this.leftDownTime != 0L) && (this.leftUpTime != 0L)) {
                if (System.currentTimeMillis() > this.leftUpTime) {
                    this.genLeftTimings();
                    this.inInvClick(mc.currentScreen);
                }
            } else
				this.genLeftTimings();
    }

    public enum SoundMode {
        NONE, BASIC, CLICK, DOUBLE, G303, G502, GPRO, HP, MICROSOFT, OLD
    }
}
