package club.dortware.client.property.impl.interfaces;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.*;

public interface INameable {

    default String getName() {
        if (!shouldOverrideName()) {
            return name();
        }
        return capitalize(StringUtils.lowerCase(name().replace("_", " ")));
    }

    default boolean shouldOverrideName() {
        return true;
    }

    String name();

}
