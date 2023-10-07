package cc.novoline.utils.messages;

import com.google.common.collect.Iterators;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * @author xDelsy
 */
final class EmptyMessage extends Message {

    private static EmptyMessage INSTANCE;

    static EmptyMessage get() {
        if (INSTANCE == null) INSTANCE = new EmptyMessage();
        return INSTANCE;
    }

    @Override
    public IChatComponent appendSibling(IChatComponent component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IChatComponent appendText(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IChatComponent setChatStyle(ChatStyle style) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<IChatComponent> spliterator() {
        return Spliterators.emptySpliterator();
    }

    @Override
    @NonNull
    public Iterator<IChatComponent> iterator() {
        return Iterators.emptyIterator();
    }

    @Override
    public @NonNull String toString() {
        return "Message{}";
    }

}
