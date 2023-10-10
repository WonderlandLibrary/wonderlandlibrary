package today.flux.utility;

import lombok.Getter;
import lombok.Setter;
import today.flux.Flux;

public class SmoothAnimationTimer {
	@Getter @Setter
	public float target;
	
	@Getter @Setter
	public float speed = 0.3f;
	
	public SmoothAnimationTimer(float target) {
		this.target = target;
	}
	
	public SmoothAnimationTimer(float target, float speed) {
		this.target = target;
		this.speed = speed;
	}
	
	@Getter @Setter
	private float value = 0;
	
    public boolean update(boolean increment) {
    	this.value = AnimationUtils.getAnimationState(value, increment ? target : 0, (float) (Math.max(10, (Math.abs(this.value - (increment ? target : 0))) * 40) * speed));
    	return value == target;
    }
}
