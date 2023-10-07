package cc.novoline.gui.screen.alt.repository.credential;

import cc.novoline.utils.java.Checks;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author xDelsy
 */
public class AltCredential {

	private final String login;
	private final String password;

	public AltCredential(@NotNull String login, @Nullable String password) {
		Checks.notBlank(login, "login");

		this.login = login.trim();
		this.password = StringUtils.isNotBlank(password) ? password : null;
	}

	//region Lombok
	@NotNull
	public String getLogin() {
		return login;
	}

	@Nullable
	public String getPassword() {
		return password;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		AltCredential that = (AltCredential) o;
		return login.equals(that.login) && Objects.equals(password, that.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(login, password);
	}

	@Override
	public String toString() {
		return login + (password != null ? ":" + password : "");
	}
	//endregion

}
