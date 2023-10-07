package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.events.events.Render2DEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Locale;

public class Camera extends AbstractModule {

	@Property("mc-lock")
	BooleanProperty mcLock = PropertyFactory.createBoolean(true);
	@Property("view-clip")
	BooleanProperty view_clip = PropertyFactory.createBoolean(true);
	@Property("no-hurt-cam")
	BooleanProperty no_hurt_cam = PropertyFactory.createBoolean(true);
	@Property("motion-blur")
	BooleanProperty motion_blur = PropertyFactory.createBoolean(true);
	@Property("amount")
	private final IntProperty amount = PropertyFactory.createInt(1).minimum(1).maximum(10);


	private boolean released;

	public Camera(@NonNull ModuleManager novoline) {
		super(novoline, EnumModuleType.VISUALS, "Camera");
		Manager.put(new Setting("CAMERA_MC_LOCK", "Middle Click Lock", SettingType.CHECKBOX, this, mcLock));
		Manager.put(new Setting("CAMERA_VC", "View Clip", SettingType.CHECKBOX, this, view_clip));
		Manager.put(new Setting("CAMERA_NHC", "No Hurt Cam", SettingType.CHECKBOX, this, no_hurt_cam));
		Manager.put(new Setting("CAMERA_MB", "Motion Blur", SettingType.CHECKBOX, this, motion_blur));
		Manager.put(new Setting("CAMERA_BA", "Blur Amount", SettingType.SLIDER, this, amount, 1, motion_blur::get));
	}

	@Override
	public void onEnable() {
		if(motion_blur.get()) {
			mc.entityRenderer.theShaderGroup = null;

			if(mc.world != null) {
				try {
					mc.entityRenderer.loadCustomShader();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public JsonObject getJsonObject() {
		String JSON = "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}";
		double amount = 0.7 + this.amount.get() / 100.0 * 3.0 - 0.01;
		JsonParser parser = new JsonParser();
		return parser.parse(String.format(Locale.ENGLISH, JSON, amount, amount, amount)).getAsJsonObject();
	}

	@EventTarget
	public void on2D(Render2DEvent render2DEvent) {
		if(motion_blur.get() && mc.entityRenderer.theShaderGroup == null && mc.world != null) {
			try {
				mc.entityRenderer.loadCustomShader();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	@EventTarget
	public void onMotionUpdate(MotionUpdateEvent event){
		if(event.getState() == MotionUpdateEvent.State.POST){
			if(mcLock.get()) {
				if (Mouse.isButtonDown(2)) {
					mc.gameSettings.thirdPersonView = 1;
					released = false;
				} else {
					if (!released) {
						mc.gameSettings.thirdPersonView = 0;
						released = true;
					}
				}
			}
		}
	}

	public BooleanProperty getViewClip() {
		return view_clip;
	}

	public BooleanProperty getNoHurtCam() {
		return no_hurt_cam;
	}

	public BooleanProperty getMcLock() {
		return mcLock;
	}
}
