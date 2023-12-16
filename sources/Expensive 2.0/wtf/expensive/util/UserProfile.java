package wtf.expensive.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dedinside
 * @since 25.06.2023
 */
@Getter
@Setter
@AllArgsConstructor
public class UserProfile {
    private final String name;
    private final int uid;
}
