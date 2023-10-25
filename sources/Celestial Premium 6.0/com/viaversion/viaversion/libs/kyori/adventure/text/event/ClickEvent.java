/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.viaversion.viaversion.libs.kyori.adventure.text.event;

import com.viaversion.viaversion.libs.kyori.adventure.text.format.StyleBuilderApplicable;
import com.viaversion.viaversion.libs.kyori.adventure.util.Index;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ClickEvent
implements Examinable,
StyleBuilderApplicable {
    private final Action action;
    private final String value;

    @NotNull
    public static ClickEvent openUrl(@NotNull String url) {
        return new ClickEvent(Action.OPEN_URL, url);
    }

    @NotNull
    public static ClickEvent openUrl(@NotNull URL url) {
        return ClickEvent.openUrl(url.toExternalForm());
    }

    @NotNull
    public static ClickEvent openFile(@NotNull String file) {
        return new ClickEvent(Action.OPEN_FILE, file);
    }

    @NotNull
    public static ClickEvent runCommand(@NotNull String command) {
        return new ClickEvent(Action.RUN_COMMAND, command);
    }

    @NotNull
    public static ClickEvent suggestCommand(@NotNull String command) {
        return new ClickEvent(Action.SUGGEST_COMMAND, command);
    }

    @NotNull
    public static ClickEvent changePage(@NotNull String page) {
        return new ClickEvent(Action.CHANGE_PAGE, page);
    }

    @NotNull
    public static ClickEvent changePage(int page) {
        return ClickEvent.changePage(String.valueOf(page));
    }

    @NotNull
    public static ClickEvent copyToClipboard(@NotNull String text) {
        return new ClickEvent(Action.COPY_TO_CLIPBOARD, text);
    }

    @NotNull
    public static ClickEvent clickEvent(@NotNull Action action, @NotNull String value) {
        return new ClickEvent(action, value);
    }

    private ClickEvent(@NotNull Action action, @NotNull String value) {
        this.action = Objects.requireNonNull(action, "action");
        this.value = Objects.requireNonNull(value, "value");
    }

    @NotNull
    public Action action() {
        return this.action;
    }

    @NotNull
    public String value() {
        return this.value;
    }

    @Override
    public void styleApply( @NotNull Style.Builder style) {
        style.clickEvent(this);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        ClickEvent that = (ClickEvent)other;
        return this.action == that.action && Objects.equals(this.value, that.value);
    }

    public int hashCode() {
        int result = this.action.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("action", (Object)this.action), ExaminableProperty.of("value", this.value));
    }

    public String toString() {
        return this.examine(StringExaminer.simpleEscaping());
    }

    public static final class Action
    extends Enum<Action> {
        public static final /* enum */ Action OPEN_URL = new Action("open_url", true);
        public static final /* enum */ Action OPEN_FILE = new Action("open_file", false);
        public static final /* enum */ Action RUN_COMMAND = new Action("run_command", true);
        public static final /* enum */ Action SUGGEST_COMMAND = new Action("suggest_command", true);
        public static final /* enum */ Action CHANGE_PAGE = new Action("change_page", true);
        public static final /* enum */ Action COPY_TO_CLIPBOARD = new Action("copy_to_clipboard", true);
        public static final Index<String, Action> NAMES;
        private final String name;
        private final boolean readable;
        private static final /* synthetic */ Action[] $VALUES;

        public static Action[] values() {
            return (Action[])$VALUES.clone();
        }

        public static Action valueOf(String name) {
            return Enum.valueOf(Action.class, name);
        }

        private Action(String name, boolean readable) {
            this.name = name;
            this.readable = readable;
        }

        public boolean readable() {
            return this.readable;
        }

        @NotNull
        public String toString() {
            return this.name;
        }

        private static /* synthetic */ Action[] $values() {
            return new Action[]{OPEN_URL, OPEN_FILE, RUN_COMMAND, SUGGEST_COMMAND, CHANGE_PAGE, COPY_TO_CLIPBOARD};
        }

        static {
            $VALUES = Action.$values();
            NAMES = Index.create(Action.class, constant -> constant.name);
        }
    }
}

