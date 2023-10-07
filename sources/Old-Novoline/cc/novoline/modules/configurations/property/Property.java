package cc.novoline.modules.configurations.property;

import org.jetbrains.annotations.Nullable;

/**
 * @author xDelsy
 */
public interface Property<Type> {

	@Nullable Type get();
	void set(@Nullable Type value);
}
