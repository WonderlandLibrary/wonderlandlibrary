package com.enjoytheban.module.modules.render.UI;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.enjoytheban.Client;
import com.enjoytheban.api.EventBus;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.misc.EventKey;
import com.enjoytheban.api.events.rendering.EventRender2D;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.api.value.Value;
import com.enjoytheban.management.Manager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.module.modules.render.HUD;
import com.enjoytheban.ui.font.CFontRenderer;
import com.enjoytheban.ui.font.FontLoaders;
import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.math.MathUtil;
import com.enjoytheban.utils.render.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * @author Purity a big gay tabgui that is really gay did i mention its gay
 */

public class TabUI implements Manager {

	// varibls
	private Section section = Section.TYPES;
	private ModuleType selectedType = ModuleType.values()[0];

	private Module selectedModule = null;
	private Value selectedValue = null;

	private int currentType = 0, currentModule = 0, currentValue = 0, height = 12, maxType, maxModule, maxValue;

	// Init method
	@Override
	public void init() {
		// call setselectedmodule method
		// this.setSelectedModule();

		for (ModuleType mt : ModuleType.values()) {
			if (this.maxType > Helper.mc.fontRendererObj.getStringWidth(mt.name().toUpperCase()) + 4) {
				continue;
			}
			this.maxType = Helper.mc.fontRendererObj.getStringWidth(mt.name().toUpperCase()) + 4;
		}

		for (Module m : Client.instance.getModuleManager().getModules()) {
			if (this.maxModule > Helper.mc.fontRendererObj.getStringWidth(m.getName().toUpperCase()) + 4) {
				continue;
			}
			this.maxModule = Helper.mc.fontRendererObj.getStringWidth(m.getName().toUpperCase()) + 4;
		}

		for (Module m : Client.instance.getModuleManager().getModules()) {
			if (m.getValues().isEmpty()) {
				continue;
			}

			for (Value val : m.getValues()) {
				if (this.maxValue > Helper.mc.fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + 4) {
					continue;
				}
				this.maxValue = Helper.mc.fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + 4;
			}
		}

		this.maxModule += 12;
		this.maxValue += 24;
		int highestWidth = 0;
		this.maxType = (this.maxType < this.maxModule ? this.maxModule : this.maxType);
		this.maxModule += this.maxType;
		this.maxValue += this.maxModule;

		// register the event
		EventBus.getInstance().register(this);
	}

	// Reset the value length
	private void resetValuesLength() {
		this.maxValue = 0;
		for (Value val : this.selectedModule.getValues()) {
			int off = val instanceof Option ? 6
					: Helper.mc.fontRendererObj.getStringWidth(String.format(" \2477%s", val.getValue().toString()))
							+ 6;
			if (this.maxValue > Helper.mc.fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + off) {
				continue;
			}
			this.maxValue = Helper.mc.fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + off;
		}
		this.maxValue += this.maxModule;
	}

	// hereeee weeeee goooooooo
	@EventHandler
	private void renderTabGUI(EventRender2D e) {
		final CFontRenderer font = FontLoaders.kiona18;
		// if it isnt showing the shit in f3 && the hud module is enabled
		if (HUD.useFont) {
			if (!Helper.mc.gameSettings.showDebugInfo
					&& Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled()) {

				// vals
				int categoryY;
				int moduleY = categoryY = HUD.shouldMove ? 26 : this.height;
				int valueY = categoryY;

				// first rect
				RenderUtil.drawBorderedRect((float) 2.0, categoryY, this.maxType - 25,
						categoryY + 12 * ModuleType.values().length, 2, new Color(0, 0, 0, 130).getRGB(),
						new Color(0, 0, 0, 180).getRGB());

				// selector
				ModuleType[] moduleArray = ModuleType.values();
				int mA = moduleArray.length;
				int mA2 = 0;
				while (mA2 < mA) {
					ModuleType mt = moduleArray[mA2];
					if (this.selectedType == mt) {

						Gui.drawRect(2.5, categoryY + 0.5, this.maxType - 25.5,
								categoryY + Helper.mc.fontRendererObj.FONT_HEIGHT + 2.5,
								new Color(102, 172, 255).getRGB());
						moduleY = categoryY;
					}

					if (this.selectedType == mt) {
						font.drawStringWithShadow(mt.name(), 7, categoryY + 3, -1);
					} else {
						font.drawStringWithShadow(mt.name(), 5, categoryY + 3, new Color(180, 180, 180).getRGB());
					}
					categoryY += 12;
					++mA2;
				}

				if (this.section == Section.MODULES || this.section == Section.VALUES) {

					RenderUtil.drawBorderedRect(this.maxType - 20, moduleY, this.maxModule - 38,
							moduleY + 12
									* Client.instance.getModuleManager().getModulesInType(this.selectedType).size(),
							2, new Color(0, 0, 0, 130).getRGB(), new Color(0, 0, 0, 180).getRGB());

					for (Module m : Client.instance.getModuleManager().getModulesInType(this.selectedType)) {
						if (this.selectedModule == m) {
							Gui.drawRect(this.maxType - 19.5, moduleY + 0.5, this.maxModule - 38.5,
									moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT + 2.5,
									new Color(102, 172, 255).getRGB());
							valueY = moduleY;
						}

						if (this.selectedModule == m) {
							font.drawStringWithShadow(m.getName(), this.maxType - 15, moduleY + 3,
									m.isEnabled() ? -1 : 11184810);
						} else {
							font.drawStringWithShadow(m.getName(), this.maxType - 17, moduleY + 3,
									m.isEnabled() ? -1 : 11184810);
						}

						if (!m.getValues().isEmpty()) {
							Gui.drawRect(this.maxModule - 38, moduleY + 0.5, this.maxModule - 39,
									moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT + 2.5,
									new Color(153, 200, 255).getRGB());

							if (this.section == Section.VALUES && this.selectedModule == m) {
								RenderUtil.drawBorderedRect(this.maxModule - 32, valueY, this.maxValue - 25,
										valueY + 12 * this.selectedModule.getValues().size(), 2,
										new Color(10, 10, 10, 180).getRGB(), new Color(10, 10, 10, 180).getRGB());

								for (Value val : this.selectedModule.getValues()) {
									Gui.drawRect(this.maxModule - 31.5, valueY + 0.5, this.maxValue - 25.5,
											valueY + Helper.mc.fontRendererObj.FONT_HEIGHT + 2.5,
											this.selectedValue == val ? new Color(102, 172, 255).getRGB() : 0);
									if (val instanceof Option) {
										font.drawStringWithShadow(val.getDisplayName(),
												this.selectedValue == val ? this.maxModule - 27 : this.maxModule - 29,
												valueY + 3,
												(Boolean) val.getValue() != false ? new Color(153, 200, 255).getRGB()
														: 11184810);
									} else {
										String toRender = String.format("%s: \u00a77%s", val.getDisplayName(),
												val.getValue().toString());

										if (this.selectedValue == val) {

											font.drawStringWithShadow(toRender, this.maxModule - 27, valueY + 3, -1);
										} else {
											font.drawStringWithShadow(toRender, this.maxModule - 29, valueY + 3, -1);
										}
									}
									valueY += 12;
								}
							}
						}
						moduleY += 12;
					}
				}
			}
		} else {
			if (!Helper.mc.gameSettings.showDebugInfo
					&& Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled()) {

				// vals
				int categoryY;
				int moduleY = categoryY = HUD.shouldMove ? 26 : this.height;
				int valueY = categoryY;

				// first rect
				RenderUtil.drawBorderedRect((float) 2.0, categoryY, this.maxType - 25,
						categoryY + 12 * ModuleType.values().length, 2, new Color(0, 0, 0, 130).getRGB(),
						new Color(0, 0, 0, 180).getRGB());

				// selector
				ModuleType[] moduleArray = ModuleType.values();
				int mA = moduleArray.length;
				int mA2 = 0;
				while (mA2 < mA) {
					ModuleType mt = moduleArray[mA2];
					if (this.selectedType == mt) {

						Gui.drawRect(2.5, categoryY + 0.5, this.maxType - 25.5,
								categoryY + Helper.mc.fontRendererObj.FONT_HEIGHT + 2.5,
								new Color(102, 172, 255).getRGB());
						moduleY = categoryY;
					}

					if (this.selectedType == mt) {
						Helper.mc.fontRendererObj.drawStringWithShadow(mt.name(), 7, categoryY + 2, -1);
					} else {
						Helper.mc.fontRendererObj.drawStringWithShadow(mt.name(), 5, categoryY + 2,
								new Color(180, 180, 180).getRGB());
					}
					categoryY += 12;
					++mA2;
				}

				if (this.section == Section.MODULES || this.section == Section.VALUES) {

					RenderUtil.drawBorderedRect(this.maxType - 20, moduleY, this.maxModule - 38,
							moduleY + 12
									* Client.instance.getModuleManager().getModulesInType(this.selectedType).size(),
							2, new Color(0, 0, 0, 130).getRGB(), new Color(0, 0, 0, 180).getRGB());

					for (Module m : Client.instance.getModuleManager().getModulesInType(this.selectedType)) {
						if (this.selectedModule == m) {
							Gui.drawRect(this.maxType - 19.5, moduleY + 0.5, this.maxModule - 38.5,
									moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT + 2.5,
									new Color(102, 172, 255).getRGB());
							valueY = moduleY;
						}

						if (this.selectedModule == m) {
							Helper.mc.fontRendererObj.drawStringWithShadow(m.getName(), this.maxType - 15, moduleY + 2,
									m.isEnabled() ? -1 : 11184810);
						} else {
							Helper.mc.fontRendererObj.drawStringWithShadow(m.getName(), this.maxType - 17, moduleY + 2,
									m.isEnabled() ? -1 : 11184810);
						}

						if (!m.getValues().isEmpty()) {
							Gui.drawRect(this.maxModule - 38, moduleY + 0.5, this.maxModule - 39,
									moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT + 2.5,
									new Color(153, 200, 255).getRGB());

							if (this.section == Section.VALUES && this.selectedModule == m) {
								RenderUtil.drawBorderedRect(this.maxModule - 32, valueY, this.maxValue - 25,
										valueY + 12 * this.selectedModule.getValues().size(), 2,
										new Color(10, 10, 10, 180).getRGB(), new Color(10, 10, 10, 180).getRGB());

								for (Value val : this.selectedModule.getValues()) {
									Gui.drawRect(this.maxModule - 31.5, valueY + 0.5, this.maxValue - 25.5,
											valueY + Helper.mc.fontRendererObj.FONT_HEIGHT + 2.5,
											this.selectedValue == val ? new Color(102, 172, 255).getRGB() : 0);
									if (val instanceof Option) {
										Helper.mc.fontRendererObj.drawStringWithShadow(val.getDisplayName(),
												this.selectedValue == val ? this.maxModule - 27 : this.maxModule - 29,
												valueY + 2,
												(Boolean) val.getValue() != false ? new Color(153, 200, 255).getRGB()
														: 11184810);
									} else {
										String toRender = String.format("%s: \u00a77%s", val.getDisplayName(),
												val.getValue().toString());

										if (this.selectedValue == val) {

											Helper.mc.fontRendererObj.drawStringWithShadow(toRender,
													this.maxModule - 27, valueY + 2, -1);
										} else {
											Helper.mc.fontRendererObj.drawStringWithShadow(toRender,
													this.maxModule - 29, valueY + 2, -1);
										}
									}
									valueY += 12;
								}
							}
						}
						moduleY += 12;
					}
				}
			}
		}
	}

	// honestly just gay shit for arrowkey actions document it urself tbh
	@EventHandler
	private void onKey(EventKey e) {
		if (!Helper.mc.gameSettings.showDebugInfo) {
			switch (e.getKey()) {
			case Keyboard.KEY_DOWN:
				switch (this.section) {
				case TYPES:
					this.currentType++;
					if (this.currentType > ModuleType.values().length - 1) {
						this.currentType = 0;
					}
					this.selectedType = ModuleType.values()[this.currentType];
					break;

				case MODULES:
					this.currentModule++;
					if (this.currentModule > Client.instance.getModuleManager().getModulesInType(this.selectedType)
							.size() - 1) {
						this.currentModule = 0;
					}
					this.selectedModule = Client.instance.getModuleManager().getModulesInType(this.selectedType)
							.get(this.currentModule);
					break;

				case VALUES:
					this.currentValue++;
					if (this.currentValue > this.selectedModule.getValues().size() - 1) {
						this.currentValue = 0;
					}
					this.selectedValue = this.selectedModule.getValues().get(this.currentValue);
					break;
				}
				break;

			case Keyboard.KEY_UP:
				switch (this.section) {
				case TYPES:
					this.currentType--;
					if (this.currentType < 0) {
						this.currentType = ModuleType.values().length - 1;
					}
					this.selectedType = ModuleType.values()[this.currentType];
					break;

				case MODULES:
					this.currentModule--;
					if (this.currentModule < 0) {
						this.currentModule = Client.instance.getModuleManager().getModulesInType(this.selectedType)
								.size() - 1;
					}
					this.selectedModule = Client.instance.getModuleManager().getModulesInType(this.selectedType)
							.get(this.currentModule);
					break;

				case VALUES:
					this.currentValue--;
					if (this.currentValue < 0) {
						this.currentValue = this.selectedModule.getValues().size() - 1;
					}
					this.selectedValue = this.selectedModule.getValues().get(this.currentValue);
					break;
				}
				break;

			case Keyboard.KEY_RIGHT:
				switch (this.section) {
				case TYPES:
					this.currentModule = 0;
					this.selectedModule = Client.instance.getModuleManager().getModulesInType(this.selectedType)
							.get(this.currentModule);
					this.section = Section.MODULES;
					break;

				case MODULES:
					if (!this.selectedModule.getValues().isEmpty()) {
						this.resetValuesLength();
						this.currentValue = 0;
						this.selectedValue = this.selectedModule.getValues().get(this.currentValue);
						this.section = Section.VALUES;
					}
					break;

				case VALUES:
					if (!Helper.onServer("enjoytheban")) {
						if (this.selectedValue instanceof Option) {
							this.selectedValue.setValue(!(boolean) this.selectedValue.getValue());
						} else if (this.selectedValue instanceof Numbers) {
							Numbers value = (Numbers) this.selectedValue;
							double inc = (double) value.getValue();
							inc += (double) value.getIncrement();
							inc = MathUtil.toDecimalLength(inc, 1);
							if (inc > (double) value.getMaximum()) {
								inc = (double) ((Numbers) this.selectedValue).getMinimum();
							}
							this.selectedValue.setValue(inc);
						} else if (this.selectedValue instanceof Mode) {
							Mode theme = (Mode) this.selectedValue;
							Enum current = (Enum) theme.getValue();
							int next = current.ordinal() + 1 >= theme.getModes().length ? 0 : current.ordinal() + 1;
							this.selectedValue.setValue(theme.getModes()[next]);
						}
						this.resetValuesLength();
					}
					break;
				}
				break;

			case Keyboard.KEY_RETURN:
				switch (this.section) {
				case TYPES:
					break;

				case MODULES:
					this.selectedModule.setEnabled(!this.selectedModule.isEnabled());
					break;

				case VALUES:
					this.section = Section.MODULES;
					break;
				}
				break;

			case Keyboard.KEY_LEFT:
				switch (this.section) {
				case TYPES:
					break;

				case MODULES:
					this.section = Section.TYPES;
					this.currentModule = 0;
					break;

				case VALUES:
					if (!Helper.onServer("enjoytheban")) {
						if (this.selectedValue instanceof Option) {
							this.selectedValue.setValue(!(boolean) this.selectedValue.getValue());
						}

						else if (this.selectedValue instanceof Numbers) {
							Numbers value = (Numbers) this.selectedValue;
							double inc = (double) value.getValue();
							inc -= (double) value.getIncrement();
							inc = MathUtil.toDecimalLength(inc, 1);
							if (inc < (double) value.getMinimum()) {
								inc = (double) ((Numbers) this.selectedValue).getMaximum();
							}
							this.selectedValue.setValue(inc);
						}

						else if (this.selectedValue instanceof Mode) {
							Mode theme = (Mode) this.selectedValue;
							Enum current = (Enum) theme.getValue();
							int next = current.ordinal() - 1 < 0 ? theme.getModes().length - 1 : current.ordinal() - 1;
							this.selectedValue.setValue(theme.getModes()[next]);
						}
						this.maxValue = 0;
						for (Value val : this.selectedModule.getValues()) {
							int off = val instanceof Option ? 6
									: Minecraft.getMinecraft().fontRendererObj
											.getStringWidth(String.format(" \2477%s", val.getValue().toString())) + 6;
							if (this.maxValue > Minecraft.getMinecraft().fontRendererObj
									.getStringWidth(val.getDisplayName().toUpperCase()) + off) {
								continue;
							}
							this.maxValue = Minecraft.getMinecraft().fontRendererObj
									.getStringWidth(val.getDisplayName().toUpperCase()) + off;
						}
						this.maxValue += this.maxModule;
					}
					break;
				}
				break;
			}
		}
	}

	// Enum for "sections"
	public enum Section {
		TYPES, MODULES, VALUES;
	}
}