package dev.monsoon.ui.hud.mod.impl;

import java.awt.Color;

import dev.monsoon.Monsoon;
import dev.monsoon.ui.hud.mod.HudMod;
import dev.monsoon.util.render.DrawUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;

public class TargetHUD extends HudMod {
	
	EntityLivingBase target;
	int healthColor;
	double healthRect;


	public TargetHUD() {
		super("TargetHUD", 150, 150);
	}
	
	@Override
	public void draw() {
		renderTargetHud();
		
		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {

		super.renderDummy(mouseX, mouseY);
	}

	public void renderTargetHud() {
		EntityLivingBase target = Monsoon.killAura.target;

		if (Monsoon.killAura.target instanceof EntityLivingBase && !Monsoon.killAura.target.isDead && mc.thePlayer.getDistanceToEntity(Monsoon.killAura.target) < Monsoon.killAura.range.getValue() + 2 && Monsoon.killAura.isEnabled()) {

			Gui.drawRect(getX(), getY(), getX() + 130, getY() + 55, new Color(0, 0, 0, 170).getRGB());
			DrawUtil.setColor(new Color(255, 255, 255, 255).getRGB());
			mc.fontRendererObj.drawString(target.getName(), getX() + 5, getY() + 5, -1);


			DrawUtil.setColor(-1);
			GuiInventory.drawEntityOnScreen(getX() + 110, getY() + 50, 25, 40, -5, target);

			DrawUtil.setColor(new Color(0, 170, 255, 255).getRGB());
			mc.fontRendererObj.drawString((int) target.getHealth() + " \u2764", getX() + 5, getY() + 18, -1);

			drawEntityHealth();

		}
	}

	private int getWinColor() {
		EntityLivingBase currentTarget = (EntityLivingBase) Monsoon.killAura.target;
		if (currentTarget.getHealth() > mc.thePlayer.getHealth()) {
			return new Color(255, 0, 0, 255).getRGB();
		} else if (currentTarget.getHealth() == mc.thePlayer.getHealth() && currentTarget.getHealth() != 20 && mc.thePlayer.getHealth() != 20) {
			return new Color(255, 255, 0, 255).getRGB();
		} else if (currentTarget.getHealth() < mc.thePlayer.getHealth()) {
			return new Color(0, 255, 0, 255).getRGB();
		} else if (currentTarget.getHealth() == 0) {
			return new Color(0, 255, 0, 255).getRGB();
		}else if (mc.thePlayer.getHealth() == 0) {
			return new Color(255, 0, 0, 255).getRGB();
		} else if (currentTarget.getHealth() == 20 && mc.thePlayer.getHealth() == 20) {
			return new Color(0, 188, 255, 255).getRGB();
		}
		else {
			return new Color(0, 0, 255, 255).getRGB();
		}
	}

	private void drawEntityHealth() {
		EntityLivingBase target = (EntityLivingBase) Monsoon.killAura.target;

		if(target.getHealth() >= target.getMaxHealth()) {
			healthColor = new Color(0, 170, 255, 255).getRGB();
			healthRect = 85;
		} else if (target.getHealth() >= 19) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 81;
		} else if (target.getHealth() >= 18) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 77;
		} else if (target.getHealth() >= 17) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 73;
		} else if (target.getHealth() >= 16) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 69;
		} else if (target.getHealth() >= 15) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 65;
		} else if (target.getHealth() >= 14) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 61;
		} else if (target.getHealth() >= 13) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 57;
		} else if (target.getHealth() >= 11) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 53;
		} else if (target.getHealth() >= 10) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 49;
		} else if (target.getHealth() >= 9) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 45;
		} else if (target.getHealth() >= 8) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 41;
		} else if (target.getHealth() >= 7) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 37;
		} else if (target.getHealth() >= 6) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 33;
		} else if (target.getHealth() >= 5) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 29;
		} else if (target.getHealth() >= 4) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 25;
		} else if (target.getHealth() >= 3) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 21;
		} else if (target.getHealth() >= 2) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 17;
		} else if (target.getHealth() >= 1) {
			healthColor =new Color(0, 170, 255, 255).getRGB();
			healthRect = 10;
		}

		//Gui.drawRect(pos.getAbsoluteX() + 77, pos.getAbsoluteY(), pos.getAbsoluteX() + 79, pos.getAbsoluteY() + getHeight(), healthColor.getRGB());
		//154
		DrawUtil.setColor(new Color(0, 255, 255, 255).getRGB());
		Gui.drawRect(getX()  + 5, getY() + 35, getX() + 85, getY()+ 37, new Color(0, 0, 0, 255).getRGB());
		Gui.drawRect(getX()  + 5, getY() + 35, getX()  + healthRect, getY() + 37, new Color(0, 170, 255, 255).getRGB());

	}

	public int getWidth() {
		return 154;
	}

	public int getHeight() {
		return 52;
	}


}
