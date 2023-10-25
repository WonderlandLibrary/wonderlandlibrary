/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import baritone.api.BaritoneAPI;
import baritone.api.Settings;
import baritone.api.utils.BlockUtils;
import baritone.api.utils.TypeUtils;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class SettingsUtil {
    private static final Path SETTINGS_PATH = Minecraft.getMinecraft().gameDir.toPath().resolve("baritone").resolve("settings.txt");
    private static final Pattern SETTING_PATTERN = Pattern.compile("^(?<setting>[^ ]+) +(?<value>.+)");

    private static boolean isComment(String line) {
        return line.startsWith("#") || line.startsWith("//");
    }

    private static void forEachLine(Path file, Consumer<String> consumer) throws IOException {
        try (BufferedReader scan = Files.newBufferedReader(file);){
            String line;
            while ((line = scan.readLine()) != null) {
                if (line.isEmpty() || SettingsUtil.isComment(line)) continue;
                consumer.accept(line);
            }
        }
    }

    public static void readAndApply(Settings settings) {
        try {
            SettingsUtil.forEachLine(SETTINGS_PATH, line -> {
                Matcher matcher = SETTING_PATTERN.matcher((CharSequence)line);
                if (!matcher.matches()) {
                    System.out.println("Invalid syntax in setting file: " + line);
                    return;
                }
                String settingName = matcher.group("setting").toLowerCase();
                String settingValue = matcher.group("value");
                try {
                    SettingsUtil.parseAndApply(settings, settingName, settingValue);
                }
                catch (Exception ex) {
                    System.out.println("Unable to parse line " + line);
                    ex.printStackTrace();
                }
            });
        }
        catch (NoSuchFileException ignored) {
            System.out.println("Baritone settings file not found, resetting.");
        }
        catch (Exception ex) {
            System.out.println("Exception while reading Baritone settings, some settings may be reset to default values!");
            ex.printStackTrace();
        }
    }

    public static synchronized void save(Settings settings) {
        try (BufferedWriter out = Files.newBufferedWriter(SETTINGS_PATH, new OpenOption[0]);){
            for (Settings.Setting setting : SettingsUtil.modifiedSettings(settings)) {
                out.write(SettingsUtil.settingToString(setting) + "\n");
            }
        }
        catch (Exception ex) {
            System.out.println("Exception thrown while saving Baritone settings!");
            ex.printStackTrace();
        }
    }

    public static List<Settings.Setting> modifiedSettings(Settings settings) {
        ArrayList<Settings.Setting> modified = new ArrayList<Settings.Setting>();
        for (Settings.Setting<?> setting : settings.allSettings) {
            if (setting.value == null) {
                System.out.println("NULL SETTING?" + setting.getName());
                continue;
            }
            if (setting.getName().equals("logger") || setting.value == setting.defaultValue) continue;
            modified.add(setting);
        }
        return modified;
    }

    public static String settingTypeToString(Settings.Setting setting) {
        return setting.getType().getTypeName().replaceAll("(?:\\w+\\.)+(\\w+)", "$1");
    }

    public static <T> String settingValueToString(Settings.Setting<T> setting, T value) throws IllegalArgumentException {
        Parser io = Parser.getParser(setting.getType());
        if (io == null) {
            throw new IllegalStateException("Missing " + setting.getValueClass() + " " + setting.getName());
        }
        return io.toString(new ParserContext(setting), (Object)value);
    }

    public static String settingValueToString(Settings.Setting setting) throws IllegalArgumentException {
        return SettingsUtil.settingValueToString(setting, setting.value);
    }

    public static String settingDefaultToString(Settings.Setting setting) throws IllegalArgumentException {
        return SettingsUtil.settingValueToString(setting, setting.defaultValue);
    }

    public static String maybeCensor(int coord) {
        if (((Boolean)BaritoneAPI.getSettings().censorCoordinates.value).booleanValue()) {
            return "<censored>";
        }
        return Integer.toString(coord);
    }

    public static String settingToString(Settings.Setting setting) throws IllegalStateException {
        if (setting.getName().equals("logger")) {
            return "logger";
        }
        return setting.getName() + " " + SettingsUtil.settingValueToString(setting);
    }

    public static void parseAndApply(Settings settings, String settingName, String settingValue) throws IllegalStateException, NumberFormatException {
        Parser ioMethod;
        Object parsed;
        Settings.Setting<?> setting = settings.byLowerName.get(settingName);
        if (setting == null) {
            throw new IllegalStateException("No setting by that name");
        }
        Class<?> intendedType = setting.getValueClass();
        if (!intendedType.isInstance(parsed = (ioMethod = Parser.getParser(setting.getType())).parse(new ParserContext(setting), settingValue))) {
            throw new IllegalStateException(ioMethod + " parser returned incorrect type, expected " + intendedType + " got " + parsed + " which is " + parsed.getClass());
        }
        setting.value = parsed;
    }

    private static enum Parser implements ISettingParser
    {
        DOUBLE(Double.class, Double::parseDouble),
        BOOLEAN(Boolean.class, Boolean::parseBoolean),
        INTEGER(Integer.class, Integer::parseInt),
        FLOAT(Float.class, Float::parseFloat),
        LONG(Long.class, Long::parseLong),
        STRING(String.class, String::new),
        ENUMFACING(EnumFacing.class, EnumFacing::byName),
        COLOR(Color.class, str -> new Color(Integer.parseInt(str.split(",")[0]), Integer.parseInt(str.split(",")[1]), Integer.parseInt(str.split(",")[2])), color -> color.getRed() + "," + color.getGreen() + "," + color.getBlue()),
        VEC3I(Vec3i.class, str -> new Vec3i(Integer.parseInt(str.split(",")[0]), Integer.parseInt(str.split(",")[1]), Integer.parseInt(str.split(",")[2])), vec -> vec.getX() + "," + vec.getY() + "," + vec.getZ()),
        BLOCK(Block.class, str -> BlockUtils.stringToBlockRequired(str.trim()), BlockUtils::blockToString),
        ITEM(Item.class, str -> Item.getByNameOrId(str.trim()), item -> Item.REGISTRY.getNameForObject((Item)item).toString()),
        LIST{

            @Override
            public Object parse(ParserContext context, String raw) {
                Type type = ((ParameterizedType)context.getSetting().getType()).getActualTypeArguments()[0];
                Parser parser = Parser.getParser(type);
                return Stream.of(raw.split(",")).map(s -> parser.parse(context, (String)s)).collect(Collectors.toList());
            }

            @Override
            public String toString(ParserContext context, Object value) {
                Type type = ((ParameterizedType)context.getSetting().getType()).getActualTypeArguments()[0];
                Parser parser = Parser.getParser(type);
                return ((List)value).stream().map(o -> parser.toString(context, o)).collect(Collectors.joining(","));
            }

            @Override
            public boolean accepts(Type type) {
                return List.class.isAssignableFrom(TypeUtils.resolveBaseClass(type));
            }
        }
        ,
        MAPPING{

            @Override
            public Object parse(ParserContext context, String raw) {
                Type keyType = ((ParameterizedType)context.getSetting().getType()).getActualTypeArguments()[0];
                Type valueType = ((ParameterizedType)context.getSetting().getType()).getActualTypeArguments()[1];
                Parser keyParser = Parser.getParser(keyType);
                Parser valueParser = Parser.getParser(valueType);
                return Stream.of(raw.split(",(?=[^,]*->)")).map(s -> s.split("->")).collect(Collectors.toMap(s -> keyParser.parse(context, s[0]), s -> valueParser.parse(context, s[1])));
            }

            @Override
            public String toString(ParserContext context, Object value) {
                Type keyType = ((ParameterizedType)context.getSetting().getType()).getActualTypeArguments()[0];
                Type valueType = ((ParameterizedType)context.getSetting().getType()).getActualTypeArguments()[1];
                Parser keyParser = Parser.getParser(keyType);
                Parser valueParser = Parser.getParser(valueType);
                return ((Map)value).entrySet().stream().map(o -> keyParser.toString(context, o.getKey()) + "->" + valueParser.toString(context, o.getValue())).collect(Collectors.joining(","));
            }

            @Override
            public boolean accepts(Type type) {
                return Map.class.isAssignableFrom(TypeUtils.resolveBaseClass(type));
            }
        };

        private final Class<?> cla$$;
        private final Function<String, Object> parser;
        private final Function<Object, String> toString;

        private Parser() {
            this.cla$$ = null;
            this.parser = null;
            this.toString = null;
        }

        private <T> Parser(Class<T> cla$$, Function<String, T> parser) {
            this(cla$$, parser, Object::toString);
        }

        private <T> Parser(Class<T> cla$$, Function<String, T> parser, Function<T, String> toString) {
            this.cla$$ = cla$$;
            this.parser = parser::apply;
            this.toString = x -> (String)toString.apply(x);
        }

        public Object parse(ParserContext context, String raw) {
            Object parsed = this.parser.apply(raw);
            Objects.requireNonNull(parsed);
            return parsed;
        }

        public String toString(ParserContext context, Object value) {
            return this.toString.apply(value);
        }

        @Override
        public boolean accepts(Type type) {
            return type instanceof Class && this.cla$$.isAssignableFrom((Class)type);
        }

        public static Parser getParser(Type type) {
            return Stream.of(Parser.values()).filter(parser -> parser.accepts(type)).findFirst().orElse(null);
        }
    }

    private static class ParserContext {
        private final Settings.Setting<?> setting;

        private ParserContext(Settings.Setting<?> setting) {
            this.setting = setting;
        }

        private Settings.Setting<?> getSetting() {
            return this.setting;
        }
    }

    private static interface ISettingParser<T> {
        public T parse(ParserContext var1, String var2);

        public String toString(ParserContext var1, T var2);

        public boolean accepts(Type var1);
    }
}

