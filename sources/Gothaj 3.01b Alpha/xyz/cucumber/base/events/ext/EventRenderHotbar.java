package xyz.cucumber.base.events.ext;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import xyz.cucumber.base.events.Event;

public class EventRenderHotbar extends Event{

	private GuiIngame guiIngame;
	private float partialTicks;
	private ScaledResolution sr;

	public GuiIngame getGuiIngame() {
		return guiIngame;
	}

	public void setGuiIngame(GuiIngame guiIngame) {
		this.guiIngame = guiIngame;
	}

	public EventRenderHotbar(GuiIngame guiIngame, float partialTicks, ScaledResolution sr) {
		super();
		this.guiIngame = guiIngame;
		this.sr = sr;
		this.partialTicks =partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}

	public void setPartialTicks(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public ScaledResolution getScaledResolution() {
		return sr;
	}

	public void setScaledResolution(ScaledResolution sr) {
		this.sr = sr;
	}
	
}
