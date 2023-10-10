package today.flux.module.implement.Render;

import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.FloatValue;

/**
 * Created by John on 2016/10/20.
 */
public class NoFov extends Module {
	public static FloatValue fov = new FloatValue("NoFov", "Fov", 1.0f, 0.1f, 1.5f, 0.01f);
	public NoFov() {
		super("NoFov", Category.Render, false);
	}

}
