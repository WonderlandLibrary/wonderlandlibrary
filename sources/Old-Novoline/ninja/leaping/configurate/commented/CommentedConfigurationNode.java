package ninja.leaping.configurate.commented;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A configuration node that can have a comment attached to it.
 */
public interface CommentedConfigurationNode extends ConfigurationNode {

    /**
     * Gets the current value for the comment.
     *
     * <p>If the comment contains multiple lines, the lines will be split by \n</p>
     *
     * @return The configuration's current comment
     */
    @NotNull Optional<String> getComment();

    /**
     * Sets the comment for this configuration node.
     *
     * @param comment The comment to set. Line breaks should be represented as LFs (\n)
     * @return this
     */
    @NotNull CommentedConfigurationNode setComment(@Nullable String comment);

    // Methods from superclass overridden to have correct return types
    @Nullable
    @Override
    CommentedConfigurationNode getParent();

    @NotNull
    @Override
    List<? extends CommentedConfigurationNode> getChildrenList();

    @NotNull
    @Override
    Map<Object, ? extends CommentedConfigurationNode> getChildrenMap();

    @NotNull
    @Override
    CommentedConfigurationNode setValue(@Nullable Object value);

    @NotNull
    @Override
    CommentedConfigurationNode mergeValuesFrom(@NotNull ConfigurationNode other);

    @NotNull
    @Override
    CommentedConfigurationNode getAppendedNode();

    @NotNull
    @Override
    CommentedConfigurationNode getNode(@NotNull Object... path);

    @NotNull
    @Override
    CommentedConfigurationNode copy();

}
