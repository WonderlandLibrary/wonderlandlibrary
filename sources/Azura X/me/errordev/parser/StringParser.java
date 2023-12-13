package me.errordev.parser;

public class StringParser {
    private final String[] in;

    public StringParser(String[] in) {
        this.in = in;
    }

    public int getLength() {
        return this.in.length;
    }

    public ParsedArgument get(String req) {
        for (String s : this.in) {
            if (!s.startsWith("-") || !s.substring(1).contains("=") || s.replace("-", "").split("=").length != 2 || !s.substring(1).split("=")[0].equalsIgnoreCase(req)) continue;
            return new ParsedArgument(s.replace("-", "").split("=")[1]);
        }
        boolean searchNextArgument = false;
        for (String s : this.in) {
            if (s.startsWith("-") && s.replace("-", "").equalsIgnoreCase(req)) {
                searchNextArgument = true;
                continue;
            }
            if (!searchNextArgument) continue;
            return new ParsedArgument(s);
        }
        return null;
    }

    public boolean has(String req) {
        return this.get(req) != null;
    }

    public boolean has(String ... req) {
        boolean current = false;
        int index = 0;
        for (String s : req) {
            if (index == 0 && this.has(s)) {
                current = true;
            }
            current = current && this.has(s);
            ++index;
        }
        return current;
    }
}
