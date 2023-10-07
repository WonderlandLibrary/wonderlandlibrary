package cc.novoline.modules.configurations.holder;

import cc.novoline.modules.AbstractModule;
import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author xDelsy
 */
public abstract class ModuleHolder<Module extends AbstractModule> implements Cloneable {

	/* fields */
	protected final String name;
	protected Module module;

	protected TypeToken<Module> typeToken;

	/* constructors */
	public ModuleHolder(@NotNull String name, @NotNull Module module) {
		this.name = name;
		this.module = module;
	}

	/* methods */

	//region Lombok
	public @NotNull String getName() {
		return name;
	}

	public @NotNull Module getModule() {
		return module;
	}

	@SuppressWarnings("unchecked")
	public @NotNull TypeToken<Module> getTypeToken() {
		return typeToken != null ? typeToken : (this.typeToken = (TypeToken<Module>) TypeToken.of(module.getClass()));
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return "ModuleHolder{" + "name='" + name + '\'' + ", module=" + module + '}';
	}
	//endregion
}
