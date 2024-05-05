package net.minecraft.crash;

static class CrashReportCategory.Entry {
    private final String key;
    private final String value;

    public CrashReportCategory.Entry(String key, Object value) {
        this.key = key;
        if (value == null) {
            this.value = "~~NULL~~";
        } else if (value instanceof Throwable) {
            Throwable throwable = (Throwable)value;
            this.value = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
        } else {
            this.value = value.toString();
        }
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}
