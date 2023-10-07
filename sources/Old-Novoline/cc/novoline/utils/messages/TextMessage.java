package cc.novoline.utils.messages;

import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import static net.minecraft.util.EnumChatFormatting.RESET;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextMessage extends Message {

	private static final ChatStyle RESET_STYLE = new ChatStyle().setColor(RESET);

	/* fields */
	private final StringBuilder message;

	/* constructors */
	protected TextMessage(@Nullable String message, @Nullable EnumChatFormatting color) {
		this.message = message != null ? new StringBuilder(message) : new StringBuilder();
		setChatStyle(color != null ? new ChatStyle().setColor(color) : RESET_STYLE);
	}

	protected TextMessage(@Nullable String message) {
		this(message, null);
	}

	public static @NotNull TextMessage of(@Nullable String text) {
		return new TextMessage(text);
	}

	public static @NotNull TextMessage of(@Nullable String text, @Nullable EnumChatFormatting color) {
		return new TextMessage(text, color);
	}

	/* methods */
	public TextMessage prefix(@Nullable TextMessage prefix) {
		return prefix != null ? prefix.createCopy().appendSibling(this) : this;
	}

	public TextMessage suffix(@Nullable IChatComponent suffix) {
		if(suffix != null) siblings.add(suffix);
		return this;
	}

	public TextMessage newLine() {
		append("\n");
		return this;
	}

	public TextMessage append(@Nullable String s, @Nullable EnumChatFormatting color) {
		appendSibling(of(s, color));
		return this;
	}

	@Override
	public TextMessage appendText(@Nullable String s) {
		append(s, RESET);
		return this;
	}

	public TextMessage append(@Nullable String s) {
		appendText(s);
		return this;
	}

	public TextMessage append(IChatComponent component) {
		appendSibling(component);
		return this;
	}

	@Override
	public TextMessage appendSibling(IChatComponent component) {
		super.appendSibling(component);
		return this;
	}

	@Override
	public TextMessage createCopy() {
		TextMessage message = of(this.message.toString());
		message.setChatStyle(getChatStyle().createShallowCopy());

		for(IChatComponent sibling : getSiblings()) {
			message.appendSibling(sibling.createCopy());
		}

		return message;
	}

	@Override
	public String getUnformattedTextForChat() {
		return message.toString();
	}
}