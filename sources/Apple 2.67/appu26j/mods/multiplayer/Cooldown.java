package appu26j.mods.multiplayer;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventAttack2;
import appu26j.events.entity.EventTick;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;

@ModInterface(name = "1.9 Cooldown", description = "Brings back the 1.9 attack cooldown.", category = Category.MULTIPLAYER)
public class Cooldown extends Mod
{
	private int maximumAttackTicksLimit = 20, previousAttackTicks = 0, attackTicks = 0;
	private ItemStack previousHeldItem = null;
    private boolean attacking = false;
	
	public Cooldown()
	{
		this.addSetting(new Setting("Size", this, 0.5F, 1, 2, 0.25F));
        this.addSetting(new Setting("Cooldown Transparency", this, true));
        this.addSetting(new Setting("Hide subtitle (Fake cooldown by servers)", this, true));
	}
	
	@Subscribe
	public void onAttack(EventAttack2 e)
	{
		if (!this.mc.objectMouseOver.typeOfHit.equals(MovingObjectType.BLOCK) || this.mc.playerController.getCurrentGameType().isAdventure())
		{
		    this.attacking = true;
			this.attackTicks = 0;
		}
	}
	
	@Subscribe
	public void onTick(EventTick e)
	{
		this.previousAttackTicks = this.attackTicks;
		
		if (this.attackTicks < this.maximumAttackTicksLimit)
		{
			this.attackTicks++;
		}
		
		else
		{
            this.attacking = false;
		}
		
		if (this.mc.thePlayer.getHeldItem() != this.previousHeldItem)
		{
			this.attackTicks = 0;
			this.previousHeldItem = this.mc.thePlayer.getHeldItem();
		}
		
		if (this.mc.thePlayer.getHeldItem() != null)
		{
            Item item = mc.thePlayer.getHeldItem().getItem();
            
            if (item instanceof ItemSpade || item == Items.golden_axe || item == Items.diamond_axe || item == Items.wooden_hoe || item == Items.golden_hoe)
            {
            	this.maximumAttackTicksLimit = 20;
            }
            
            else if (item == Items.wooden_axe || item == Items.stone_axe)
            {
            	this.maximumAttackTicksLimit = 25;
            }
            
            else if (item instanceof ItemSword)
            {
            	this.maximumAttackTicksLimit = 12;
            }
            
            else if (item instanceof ItemPickaxe)
            {
            	this.maximumAttackTicksLimit = 17;
            }
            
            else if (item == Items.iron_axe)
            {
            	this.maximumAttackTicksLimit = 22;
            }
            
            else if (item == Items.stone_hoe)
            {
            	this.maximumAttackTicksLimit = 10;
            }
            
            else if (item == Items.iron_hoe)
            {
            	this.maximumAttackTicksLimit = 7;
            }
            
            else
            {
    			this.maximumAttackTicksLimit = 4;
            }
        }
		
		else
		{
			this.maximumAttackTicksLimit = 4;
		}
	}
	
	public void renderCooldown(int width, int height, float partialTicks)
	{
		float size = this.getSetting("Size").getSliderValue();
		GlStateManager.pushMatrix();
		GlStateManager.scale(size, size, size);
		
		if (this.attackTicks < this.maximumAttackTicksLimit)
		{
			int i = width / 2;
			int j = height / 2;
			i /= size;
			j /= size;
			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
            this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/attack_cooldown.png"));
            Gui.drawModalRectWithCustomSizedTexture(i - 8, j + 2, 0, 0, 16, 8, 16, 16);

            if (!this.getSetting("Cooldown Transparency").getCheckBoxValue())
            {
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            }

            GlStateManager.color(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
			this.scissor(i - 8, j + 6, i + 8 - (16 * (this.maximumAttackTicksLimit - this.getAttackTicks(partialTicks)) / this.maximumAttackTicksLimit), j + 12, size);
			this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/attack_cooldown.png"));
			Gui.drawModalRectWithCustomSizedTexture(i - 8, j + 2, 0, 4, 16, 16, 16, 16);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.disableBlend();
		}

		GlStateManager.popMatrix();
	}
	
	public float getAttackTicks(float partialTicks)
	{
		return this.previousAttackTicks + (this.attackTicks - this.previousAttackTicks) * partialTicks;
	}
	
	public float getMaximumAttackTicksLimit()
	{
		return this.maximumAttackTicksLimit;
	}
	
	public boolean isAttacking()
	{
	    return this.attacking;
	}
}
