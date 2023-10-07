package cc.novoline.gui.screen.alt.login;

import org.apache.commons.lang3.StringUtils;

/**
 * @author xDelsy
 */
public enum AltType {

    CRACKED,
    PREMIUM,
    ;

    private final String capitalized;

    AltType() {
        this.capitalized = StringUtils.capitalize(name().toLowerCase());
    }

    public String getCapitalized() {
        return this.capitalized;
    }

}
