package viaversion.viaversion.api.rewriters;

@FunctionalInterface
public interface IdRewriteFunction {

    int rewrite(int id);
}