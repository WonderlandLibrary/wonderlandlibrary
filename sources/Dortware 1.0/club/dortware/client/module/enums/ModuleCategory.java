package club.dortware.client.module.enums;

import org.apache.commons.lang3.StringUtils;

public enum ModuleCategory {
    COMBAT, MOVEMENT, PLAYER, MISC, RENDER, EXPLOIT, HIDE_ME;

    public String getName() {
        return StringUtils.capitalize(StringUtils.lowerCase(name()));
    }
}
