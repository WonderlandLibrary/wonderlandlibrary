package summer.ui.clickuiutils;

/**
 * @author: AmirCC
 * 02:34 pm, 10/10/2020, Tuesday
 **/
public class AnimationUtil {
	private static float defaultSpeed = 0.125f;

	public static float moveUD(float current, float end, float minSpeed) {
		return moveUD(current, end, defaultSpeed, minSpeed);
	}
	public static float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
		float movement = (end - current) * smoothSpeed;

		if (movement > 0) {
			movement = Math.max(minSpeed, movement);
			movement = Math.min(end - current, movement);
		} else if (movement < 0) {
			movement = Math.min(-minSpeed, movement);
			movement = Math.max(end - current, movement);
		}

		return current + movement;
	}


}
