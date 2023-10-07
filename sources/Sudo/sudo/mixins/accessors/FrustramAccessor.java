package sudo.mixins.accessors;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Vector4f;

@Mixin(Frustum.class)
public interface FrustramAccessor {
	
	@Accessor
	public abstract Vector4f[] getHomogeneousCoordinates();
	
	@Accessor
	public abstract void setHomogeneousCoordinates(Vector4f[] vector4f);
	
	@Accessor
	public abstract double getX();
	
	@Accessor
	public abstract void setX(double x);
	
	@Accessor
	public abstract double getY();
	
	@Accessor
	public abstract void setY(double y);
	
	@Accessor
	public abstract double getZ();
	
	@Accessor
	public abstract void setZ(double z);
}