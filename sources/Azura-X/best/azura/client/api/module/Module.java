package best.azura.client.api.module;

import best.azura.client.impl.Client;
import best.azura.client.impl.ui.customhud.impl.ModuleListElement;
import net.minecraft.client.Minecraft;

public class Module {

	private final String name, description;
	private final Category category;
	private int keyBind;
	private String suffix = "";
	private boolean enabled = false;
	private final Module instance;

	// Module List animation
	public double animation = 0;

	public final Minecraft mc = Minecraft.getMinecraft();

	public Module() {
		this.instance = this;
		if (!this.getClass().isAnnotationPresent(ModuleInfo.class)) {
			category = null;
			name = null;
			description = null;
			return;
		}
		final ModuleInfo info = this.getClass().getAnnotation(ModuleInfo.class);
		this.name = info.name();
		this.description = info.description();
		this.keyBind = info.keyBind();
		this.category = info.category();
	}

	public Module(String name, String description, int keyBind) {
		this.name = name;
		this.description = description;
		this.category = Category.SCRIPTS;
		this.keyBind = keyBind;
		instance = this;
	}

	public String getName() {
		return name;
	}

	public void setKeyBind(int keyBind) {
		this.keyBind = keyBind;
	}

	public int getKeyBind() {
		return keyBind;
	}

	public Category getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		if (suffix == null || suffix.isEmpty()) return "";
		switch (ModuleListElement.suffixMode.getObject()) {
			case "None":
				return suffix = "";
			case "Dash":
				return "- " + suffix;
			case "Brackets":
				return "(" + suffix + ")";
		}
		return suffix;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getDisplayName() {
		final String suffix = getSuffix();
		return name + (suffix == null || suffix.isEmpty() ? "" : "§7 " + suffix);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			onEnable();
		} else {
			onDisable();
		}
	}

	public Module getInstance() {
		return instance;
	}

	public void onEnable() {
		Client.INSTANCE.getEventBus().register(instance);
	}

	public void onDisable() {
		Client.INSTANCE.getEventBus().unregister(instance);
	}

}
