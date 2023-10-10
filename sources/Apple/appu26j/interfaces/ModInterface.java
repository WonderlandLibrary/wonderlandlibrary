package appu26j.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import appu26j.mods.Category;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModInterface
{
	public String name();
	public String description();
	public Category category();
	public float x() default 2.5F;
	public float y() default 2.5F;
	public float width() default 0;
	public float height() default 0;
}
