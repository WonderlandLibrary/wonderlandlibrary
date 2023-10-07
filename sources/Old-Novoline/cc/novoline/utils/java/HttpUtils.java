package cc.novoline.utils.java;

import com.google.gson.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author xDelsy
 */
public final class HttpUtils {

	private static final JsonParser PARSER = new JsonParser();
	private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().serializeNulls().excludeFieldsWithoutExposeAnnotation()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

	/* constructors */
	private HttpUtils() {
		throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	/* methods */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T extends JsonElement> T parseConnectionInput(@NonNull HttpURLConnection connection,
																 @Nullable Consumer<? super HttpURLConnection> consumer,
																 @NonNull Class<T> elementClass) throws IOException {
		connection.connect();
		JsonElement element;

		try {
			if(connection.getResponseCode() < 200 || connection.getResponseCode() > 299) {
				if(consumer != null) consumer.accept(connection);
				return null;
			}

			element = PARSER.parse(new InputStreamReader(connection.getInputStream()));
		} finally {
			connection.disconnect();
		}

		if(!elementClass.isInstance(element)) {
			throw new RuntimeException("API schema has been changed!");
		}

		return (T) element;
	}

	@NonNull
	public static HttpURLConnection createConnection(String url) throws IOException {
		Checks.notBlank(url, "URL");

		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36 OPR/65.0.3467.62");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setConnectTimeout(15_000);
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);

		// Novoline.getLogger().info("Opening a connection to " + url);
		return connection;
	}

	//region Unimportant shit
	public static JsonParser getParser() {
		return PARSER;
	}

	public static Gson getGson() {
		return GSON;
	}
	//endregion

}
