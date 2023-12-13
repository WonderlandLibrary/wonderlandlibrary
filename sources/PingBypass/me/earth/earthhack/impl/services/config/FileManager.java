package me.earth.earthhack.impl.services.config;

import me.earth.earthhack.impl.util.misc.FileUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager
{
    private static final FileManager INSTANCE = new FileManager();

    private Path base;

    private FileManager() { /* This is a singleton. */ }

    public static FileManager getInstance()
    {
        return INSTANCE;
    }

    public void init()
    {
        base          = FileUtil.getDirectory(Paths.get(""), "earthhack");
    }

    public Path getBase()
    {
        return base;
    }

}
