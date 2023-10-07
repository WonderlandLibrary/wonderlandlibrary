package cc.novoline.gui.screen.alt.repository.credential;

import cc.novoline.utils.java.Checks;
import com.thealtening.api.response.AccountDetails;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * @author xDelsy
 */
public class AlteningAltCredential extends AltCredential {

    @NonNull
    private final String name;
    @NonNull
    private final AccountDetails details;

    public AlteningAltCredential(@NonNull String login, @NonNull String name, @NonNull AccountDetails details) {
        super(login, null);
        this.name = Checks.notBlank(name, "name");
        this.details = details;
    }

    //region Lombok
    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public AccountDetails getDetails() {
        return this.details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlteningAltCredential)) return false;
        if (!super.equals(o)) return false;
        final AlteningAltCredential that = (AlteningAltCredential) o;
        return this.name.equals(that.name) && this.details.equals(that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.name, this.details);
    }
    //endregion

}
