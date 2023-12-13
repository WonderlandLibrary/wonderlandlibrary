package me.aidanmees.trivia.client.modules.Legit;

import java.lang.reflect.Method;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.client.tools.MathUtil;
import me.aidanmees.trivia.gui.custom.clickgui.CheckBtnSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;

public class AutoClicker extends Module {

	private long nextLeftUp;
	  private long nextLeftDown;
	  private long nextRightUp;
	  private long nextRightDown;
	  private long nextDrop;
	  private long nextExhaust;
	  private double dropRate;
	  private boolean dropping;
	  private Method guiScreenMethod;
	  private int i = 25000;

	public AutoClicker() {
		super("AutoClicker", Keyboard.KEY_NONE, Category.LEGIT,
				"Automatically clicks when holding down the attack button.");
	}

	@Override
	public void onToggle() {
		
		super.onToggle();
	}
	
	
	@Override
	public ModSetting[] getModSettings() {
		ModSetting slider2 = new SliderSetting<Number>("Min CPS", ClientSettings.AutoClickermin, 1.0, 20.0, 0.0, ValueFormat.DECIMAL);
		ModSetting slider3 = new SliderSetting<Number>("Max CPS", ClientSettings.AutoClickermax, 1.0, 20.0, 0.0, ValueFormat.DECIMAL);
		ModSetting slider1 = new SliderSetting<Number>("Min BlockHits PS", ClientSettings.MinBHPS, 0.1, 6.0, 0, ValueFormat.DECIMAL);
		ModSetting slider4 = new SliderSetting<Number>("Max BlockHits PS", ClientSettings.MaxBHPS, 0.1, 6.0, 0, ValueFormat.DECIMAL);
		ModSetting slider5 = new SliderSetting<Number>("JitterStrength", ClientSettings.JitterStrength, 0.1, 1, 0.0, ValueFormat.DECIMAL);
		
		CheckBtnSetting box2 = new CheckBtnSetting("JitterClick", "JitterClick");
		CheckBtnSetting box1 = new CheckBtnSetting("BlockHit", "BlockHitA");
		CheckBtnSetting box3 = new CheckBtnSetting("InvFill", "InvFill");
		
		
		return new ModSetting[] {  slider2, slider3 , slider4 ,slider1, slider5 , box1 , box2 ,box3 };
	}

	@Override
	public void onUpdate() {
		if (mc.theWorld != null &&mc.objectMouseOver.getMouseOverEntity() != null || Minecraft.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockAir) {
		
		boolean OnlyWeaponCheck;
	      if (mc.currentScreen == null) {
	            Mouse.poll();
	            if (Mouse.isButtonDown(0)) {
	                if (ClientSettings.JitterClick && MathUtil.random.nextDouble() > 0.65) {
	                    final float jitterStrength = (float)ClientSettings.JitterStrength * 0.5f;
	                    if (MathUtil.random.nextBoolean()) {
	                        final EntityPlayerSP field_71439_g = mc.thePlayer;
	                        field_71439_g.rotationYaw += MathUtil.random.nextFloat() * jitterStrength;
	                    }
	                    else {
	                        final EntityPlayerSP field_71439_g2 = mc.thePlayer;
	                        field_71439_g2.rotationYaw -= MathUtil.random.nextFloat() * jitterStrength;
	                    }
	                    if (MathUtil.random.nextBoolean()) {
	                        final EntityPlayerSP field_71439_g3 = mc.thePlayer;
	                        field_71439_g3.rotationPitch += MathUtil.random.nextFloat() * (jitterStrength * 0.75);
	                    }
	                    else {
	                        final EntityPlayerSP field_71439_g4 = mc.thePlayer;
	                        field_71439_g4.rotationPitch -= MathUtil.random.nextFloat() * (jitterStrength * 0.75);
	                    }
	                }
	                if (this.nextLeftDown > 0L && this.nextLeftUp > 0L) {
	                    if (System.currentTimeMillis() > this.nextLeftDown) {
	                        final int attackKeyBind = mc.gameSettings.keyBindAttack.getKeyCode();
	                        KeyBinding.setKeyBindState(attackKeyBind, true);
	                        KeyBinding.onTick(attackKeyBind);
	                      trivia.click();
	                      this.generateLeftDelay();
	                    }
	                    else if (System.currentTimeMillis() > this.nextLeftUp) {
	                        final int attackKeyBind = mc.gameSettings.keyBindAttack.getKeyCode();
	                        KeyBinding.setKeyBindState(attackKeyBind, false);
	                        trivia.click();
	                        this.generateLeftDelay();

	                    }
	                }
	                else {
	                    this.generateLeftDelay();
	                }
	                final boolean blockHit = ClientSettings.BlockHitA;
	                if (blockHit && Mouse.isButtonDown(1)) {
	                    if (this.nextRightDown > 0L && this.nextRightUp > 0L) {
	                        if (System.currentTimeMillis() > this.nextRightDown) {
	                            final int useItemKeyBind = mc.gameSettings.keyBindUseItem.getKeyCode();
	                            KeyBinding.setKeyBindState(useItemKeyBind, true);
	                            KeyBinding.onTick(useItemKeyBind);
	                           trivia.onRightClick();
	                            this.generateRightDelay();
	                        }
	                        else if (System.currentTimeMillis() > this.nextRightUp) {
	                            final int useItemKeyBind = mc.gameSettings.keyBindUseItem.getKeyCode();
	                            KeyBinding.setKeyBindState(useItemKeyBind, false);
	                            trivia.onRightClick();
	                        }
	                    }
	                    else {
	                        this.generateRightDelay();
	                    }
	                }
	                else {
	                    final long n = 0L;
	                    this.nextRightUp = n;
	                    this.nextRightDown = n;
	                }
	            }
	            else {
	                final long n2 = 0L;
	                this.nextRightUp = n2;
	                this.nextRightDown = n2;
	                this.nextLeftUp = n2;
	                this.nextLeftDown = n2;
	            }
	        }
	        else if (mc.currentScreen instanceof GuiInventory) {
	            if (Mouse.isButtonDown(0) && (Keyboard.isKeyDown(54) || Keyboard.isKeyDown(42))) {
	                final boolean inventoryFill = ClientSettings.InvFill;
	                if (!inventoryFill) {
	                    return;
	                }
	                if (this.nextLeftUp == 0L || this.nextLeftDown == 0L) {
	                    this.generateLeftDelay();
	                    return;
	                }
	                if (System.currentTimeMillis() > this.nextLeftDown) {
	                    this.generateLeftDelay();
	                    this.clickInventory(mc.currentScreen);
	                }
	            }
	            else {
	                final long n3 = 0L;
	                this.nextRightUp = n3;
	                this.nextRightDown = n3;
	                this.nextLeftUp = n3;
	                this.nextLeftDown = n3;
	            }
	        }
		}
		super.onUpdate();
	}

	

	 private void generateLeftDelay() {
	        final double minCPS = ClientSettings.AutoClickermin;
	        final double maxCPS = ClientSettings.AutoClickermax;
	        final double CPS = minCPS + MathUtil.random.nextDouble() * (maxCPS - minCPS);
	        long delay = (long)(int)Math.round(1700.0 / CPS);
	        if (System.currentTimeMillis() > this.nextDrop) {
	            if (!this.dropping && MathUtil.random.nextInt(100) >= 85) {
	                this.dropping = true;
	                this.dropRate = 1.1 + MathUtil.random.nextDouble() * 0.15;
	            }
	            else {
	                this.dropping = false;
	            }
	            this.nextDrop = System.currentTimeMillis() + 500L + MathUtil.random.nextInt(1500);
	        }
	        if (this.dropping) {
	            delay *= this.dropRate;
	        }
	        if (System.currentTimeMillis() > this.nextExhaust) {
	            if (MathUtil.random.nextInt(100) >= 80) {
	                delay += 50L + MathUtil.random.nextInt(150);
	            }
	            this.nextExhaust = System.currentTimeMillis() + 500L + MathUtil.random.nextInt(1500);
	        }
	        this.nextLeftDown = System.currentTimeMillis() + delay;
	        this.nextLeftUp = System.currentTimeMillis() + delay / 2L - MathUtil.random.nextInt(10);
	    }
	    
	    private void generateRightDelay() {
	        final double minCPS = ClientSettings.MinBHPS;
	        final double maxCPS = ClientSettings.MaxBHPS;
	        if (minCPS > maxCPS) {
	            return;
	        }
	        final double CPS = minCPS + MathUtil.random.nextDouble() * (maxCPS - minCPS);
	        final long delay = (long)(int)Math.round(1000.0 / CPS);
	        this.nextRightDown = System.currentTimeMillis() + delay;
	        this.nextRightUp = System.currentTimeMillis() + 20L + MathUtil.random.nextInt(30);
	    }
	    
	    private void clickInventory(final GuiScreen screen) {
	    	final int var1 = Mouse.getX() * screen.width / mc.displayWidth;
	        final int var2 = screen.height - Mouse.getY() * screen.height / mc.displayHeight - 1;
	        final int var3 = 0;
	        try {
	            this.guiScreenMethod.setAccessible(true);
	            this.guiScreenMethod.invoke(screen, var1, var2, var3);
	            this.guiScreenMethod.setAccessible(false);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public static boolean shouldClick() {
			return mc.gameSettings.keyBindAttack.pressed && trivia.getModuleByName("AutoClicker").isToggled();
		}

	    
	}