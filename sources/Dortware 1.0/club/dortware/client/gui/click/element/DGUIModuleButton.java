package club.dortware.client.gui.click.element;

import club.dortware.client.Client;
import club.dortware.client.gui.click.DortGUI;
import club.dortware.client.gui.click.Rect;
import club.dortware.client.module.Module;
import club.dortware.client.property.Property;
import club.dortware.client.property.impl.BooleanProperty;
import club.dortware.client.property.impl.DoubleProperty;
import club.dortware.client.property.impl.EnumProperty;
import club.dortware.client.property.impl.StringProperty;
import club.dortware.client.property.impl.interfaces.INameable;
import club.dortware.client.util.Util;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DGUIModuleButton implements Util {

	public Module module;
	private final ArrayList<Rect> rects = new ArrayList<>();
	private int index;
	private boolean dragging;
	public boolean listening;

	public DGUIModuleButton(Module module) {
		this.module = module;
	}

	public void keyPress(int kt) {
		this.module.setKeyBind(kt);
		this.listening = false;
	}

	public void render(int mx, int my, ScaledResolution sr, float x, float y, int index) {
		if (DortGUI.instance().selMod == this.module) {
			if (DortGUI.drawRect(new Rect(x - 3, y + 4, x + 10, y + 18), new Color(0x4e32a8).getRGB())
					.isMouseHovering(mx, my) && Mouse.isButtonDown(0)) {
				DortGUI.instance().selMod = null;
			}
			List<Property<?, ?>> properties = Client.INSTANCE.getPropertyManager().getSettingsByMod(module);
			if (!properties.isEmpty())
				for (Property option : properties) {
					if (option instanceof DoubleProperty) {
						DoubleProperty<?> numberOption = (DoubleProperty<?>) option;
						numberOption.getValue();
						int width = 0;
						String sname = numberOption.getName();
						String setstrg = sname.substring(0, 1).toUpperCase()
								+ sname.substring(1);
						String displaymax = "" + (double) Math.round(numberOption.getMax() * 100.0) / 100.0;
						double textx3 = x + width - (double) mc.fontRendererObj.getStringWidth(setstrg)
								- (double) mc.fontRendererObj.getStringWidth(displaymax) - 4.0;
						if (textx3 < x) {
							width += x - textx3 + 1.0;
						}
						String d = ((Double) numberOption.getValue()).toString();
						mc.fontRendererObj
								.drawString(
										numberOption.getName() + " ("
												+ d.substring(0,
														numberOption.isInteger() ? d.length() : d.indexOf(".") + 2)
												+ ")",
										x, this.index, -1);
						double percentBar = (numberOption.getValue() - numberOption.getMin())
								/ (numberOption.getMax() - numberOption.getMin());
						Gui.drawRect(x, this.index + 12.0F, x + width, this.index + 13.5F, -15724528);
						if (percentBar >= -9990.0) {
							dragging = DortGUI
									.drawRect(new Rect(x, this.index + 12.0, x + width, this.index + 14.5), -15724528)
									.isMouseHovering(mx, my) && Mouse.isButtonDown(0);
							DortGUI.drawRect(new Rect(x, this.index + 12.0, x + percentBar * width, this.index + 14.5),
									-1);
							DortGUI.drawRect(new Rect(x + percentBar * width - 1.0, this.index + 12.0,
									x + Math.min(percentBar * width, width), this.index + 14.5), -1);
						}
						if (this.dragging) {
							double diff = numberOption.getMax() - numberOption.getMin();
							double val = numberOption.getMin()
									+ MathHelper.clamp_double(((double) mx - x) / width, 0.0, 1.0) * diff;
							numberOption.setValue(val);
						}
					} else if (option instanceof StringProperty<?>) {
						StringProperty<?> stringOption = (StringProperty<?>) option;
						if (DortGUI
								.drawRect(new Rect(x, this.index + 8,
												x + mc.fontRendererObj.getStringWidth(stringOption.getValue()) + 25, this.index + 18),
										DortGUI.BACKGROUND_COLOR.brighter().brighter().getRGB())
								.isMouseHovering(mx, my) && Mouse.isButtonDown(0)) {
							int index1 = stringOption.getChoices().indexOf(stringOption.getValue()) + 1;
							if (index == -1) {
								stringOption.setValue(stringOption.getChoices().get(0));
							} else {
								try {
									stringOption.setValue(stringOption.getChoices().get(index1));
								} catch (Exception e) {
									stringOption.setValue(stringOption.getChoices().get(0));
								}
							}
							Mouse.destroy();
							try {
								Mouse.create();
							} catch (LWJGLException e) {
								e.printStackTrace();
							}
						}
						mc.fontRendererObj.drawString(stringOption.getValue(), x + 2, this.index + 9, -1);
						mc.fontRendererObj.drawString(">", x + mc.fontRendererObj.getStringWidth(stringOption.getValue()) + 19,
								this.index + 9.5F, -1);
						mc.fontRendererObj.drawString(stringOption.getName(), x, this.index, -1);
					}
					else if (option instanceof EnumProperty<?>) {
						EnumProperty<?> enumOption = (EnumProperty<?>) option;
						if (DortGUI
								.drawRect(new Rect(x, this.index + 8,
												x + mc.fontRendererObj.getStringWidth(enumOption.getValue().getName()) + 25, this.index + 18),
										DortGUI.BACKGROUND_COLOR.brighter().brighter().getRGB())
								.isMouseHovering(mx, my) && Mouse.isButtonDown(0)) {
							int index1 = enumOption.getValues().indexOf(enumOption.getValue()) + 1;
							if (index == -1) {
								enumOption.setValue(enumOption.getValues().get(0));
							} else {
								try {
									enumOption.setValue(enumOption.getValues().get(index1));
								} catch (Exception e) {
									enumOption.setValue(enumOption.getValues().get(0));
								}
							}
							Mouse.destroy();
							try {
								Mouse.create();
							} catch (LWJGLException e) {
								e.printStackTrace();
							}
						}
						mc.fontRendererObj.drawString(enumOption.getValue().getName(), x + 2, this.index + 9, -1);
						mc.fontRendererObj.drawString(">", x + mc.fontRendererObj.getStringWidth(enumOption.getValue().getName()) + 19,
								this.index + 9.5F, -1);
						mc.fontRendererObj.drawString(enumOption.getName(), x, this.index, -1);
					}
					else if (option instanceof BooleanProperty<?>){
						BooleanProperty<?> stringOption = (BooleanProperty<?>) option;
						mc.fontRendererObj.drawString(stringOption.getName(), x + 13, this.index + 10, -1);
						if (DortGUI.drawRect(new Rect(x - 2, y + this.index - 12, x + 10, y + this.index - 2),
								DortGUI.BACKGROUND_COLOR.darker().getRGB()).isMouseHovering(mx, my)) {
							if (Mouse.isButtonDown(0)) {
								stringOption.setValue(!stringOption.getValue());
								Mouse.destroy();
								try {
									Mouse.create();
								} catch (LWJGLException e) {
									e.printStackTrace();
								}
							}
						}
						if (stringOption.getValue()) {
							DortGUI.drawRect(new Rect(x - 2, y + this.index - 12, x + 10, y + this.index - 2), -1);
						}
					}
					this.index += 20;
				}
			if (DortGUI
					.drawRect(new Rect(x, this.index + 12, x + 40, this.index + 26),
							DortGUI.BACKGROUND_COLOR.brighter().getRGB())
					.isMouseHovering(mx, my) && Mouse.isButtonDown(0)) {
				listening = true;
			}
			mc.fontRendererObj.drawString("Key:", x, this.index + 4, -1);
			mc.fontRendererObj.drawString(
					(this.listening ? ">" : "") + Keyboard.getKeyName(module.getKeyBind()) + (this.listening ? "<" : ""),
					x + (this.listening ? 4 : 9), this.index + 15, -1);
			mc.fontRendererObj.drawString("<", x + 1, y + 8, -1);
		}
		this.index = (int) y + 21;
		if (DortGUI.instance().selMod == null) {
			this.rects.add(DortGUI.drawRect(new Rect(x - 3, y + index - 11, x + 100, y + index + 11),
					DortGUI.BACKGROUND_COLOR.brighter().getRGB()));
			for (Rect rect : this.rects) {
				if (rect.isMouseHovering(mx, my) && Mouse.isButtonDown(1)) {
					DortGUI.instance().selMod = this.module;
					Mouse.destroy();
					try {
						Mouse.create();
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
				}
			}
			this.rects.clear();
			mc.fontRendererObj.drawString(this.module.getModuleData().name(), x, y + index - 4, -1);
			this.rects.add(DortGUI.drawRect(new Rect(x + 70, y + index - 5, x + 98, y + index + 5),
					DortGUI.BACKGROUND_COLOR.getRGB()));
			if (module.isToggled()) {
				this.rects.add(DortGUI.drawRect(new Rect(x + 92, y + index - 3, x + 96, y + index + 3),
						!module.isToggled() ? Color.red.darker().getRGB() : Color.green.darker().getRGB()));
			} else {
				this.rects.add(DortGUI.drawRect(new Rect(x + 72, y + index - 3, x + 76, y + index + 3),
						!module.isToggled() ? Color.red.darker().getRGB() : Color.green.darker().getRGB()));
			}
			for (Rect rect : this.rects) {
				if (rect.isMouseHovering(mx, my) && Mouse.isButtonDown(0)) {
					module.toggle();
					Mouse.destroy();
					try {
						Mouse.create();
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
				}
			}
			this.rects.clear();
		}
	}

}
