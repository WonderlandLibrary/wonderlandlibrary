package de.tired.base.module;

import de.tired.base.module.implementation.visual.Notifications;
import de.tired.util.animation.Animation;
import de.tired.base.annotations.ModuleAnnotation;
import de.tired.util.render.Translate;
import de.tired.base.event.EventManager;
import de.tired.base.interfaces.IHook;
import de.tired.util.render.notification.Notify;
import de.tired.util.render.notification.NotifyManager;
import de.tired.Tired;
import net.minecraft.client.gui.ScaledResolution;

public abstract class Module implements IHook {

	public final String name = getClass().getAnnotation(ModuleAnnotation.class).name();

	public final String clickGUIText = getClass().getAnnotation(ModuleAnnotation.class).clickG();

	public Translate translate;

	public float animation;

	public Animation slideAnimation = new Animation(), yAnimation = new Animation();

	public String desc = getClass().getAnnotation(ModuleAnnotation.class).desc();

	public int key = getClass().getAnnotation(ModuleAnnotation.class).key();

	public final ModuleCategory moduleCategory = getClass().getAnnotation(ModuleAnnotation.class).category();

	public final ScaledResolution sr = new ScaledResolution(MC);

	public Module() {
		this.translate = new Translate(0, 0);
		animation = 0;
	}

	public void setDesc(String desc) {

			this.desc = "";

	}

	public boolean state = false;

	public float posX;

	public abstract void onState();

	public abstract void onUndo();


	public void enableMod() {

		if (!state) {
			doEvent();
			setState(true);
			if (Tired.INSTANCE.moduleManager.findModuleByClass(Notifications.class).isState()) {
				NotifyManager.sendClientMessage("ModuleManager", "Toggled: " + name, Notify.NotificationType.SUCCESS);
			}
		}
	}

	public void disableModule() {
		if (Tired.INSTANCE.moduleManager.findModuleByClass(Notifications.class).isState()) {
			NotifyManager.sendClientMessage("ModuleManager", "Disabled: " + name, Notify.NotificationType.SUCCESS);
		}
		undoEvent();
		setState(false);
	}

	public void executeMod() {
		if (!state) {
			doEvent();
			setState(true);
			if (Tired.INSTANCE.moduleManager.findModuleByClass(Notifications.class).isState()) {
				NotifyManager.sendClientMessage("ModuleManager", "Toggled: " + name, Notify.NotificationType.SUCCESS);
			}
			return;
		}
		if (Tired.INSTANCE.moduleManager.findModuleByClass(Notifications.class).isState()) {
			NotifyManager.sendClientMessage("ModuleManager", "Disabled: " + name, Notify.NotificationType.SUCCESS);
		}
		undoEvent();
		setState(false);
	}

	public void unableModule() {
		if (state) {
			undoEvent();
			state = false;
			setState(false);
		}
	}


	public Translate getTranslate () {
		return translate;
	}

	public void setTranslate (Translate translate) {
		this.translate = translate;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public String getNameWithSuffix() {
		return name + " " + desc;
	}

	public int getKey() {
		return key;
	}

	public ModuleCategory getModuleCategory() {
		return moduleCategory;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public void doEvent() {
		EventManager.register(this);
		this.onState();
	}

	public void undoEvent() {
		EventManager.unregister(this);
		this.onUndo();
	}

}
