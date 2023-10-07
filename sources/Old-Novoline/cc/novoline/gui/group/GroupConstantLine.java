package cc.novoline.gui.group;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author xDelsy
 */
public final class GroupConstantLine implements GroupLine<Object> {

    /* fields */
    @NonNull
    private final String text;

    /* constructors */
    private GroupConstantLine(@NonNull String text) {
        this.text = text;
    }

    /* methods */
    @NonNull
    static GroupConstantLine of(@NonNull String text) {
        return new GroupConstantLine(text);
    }

    //region Lombok
    @Override
    public String getText(Object o) {
        return this.text;
    }
    //endregion

}
