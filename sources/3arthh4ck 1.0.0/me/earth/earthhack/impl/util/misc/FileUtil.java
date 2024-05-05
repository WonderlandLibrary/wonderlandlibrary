package me.earth.earthhack.impl.util.misc;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class FileUtil
{
    public static Path getDirectory(Path parent, String...paths)
    {
        if (paths.length < 1)
        {
            return parent;
        }

        Path dir = lookupPath(parent, paths);
        createDirectory(dir);
        return dir;
    }

    public static Path lookupPath(Path root, String...paths)
    {
        return Paths.get(root.toString(), paths);
    }

    public static boolean createDirectory(File file)
    {
        boolean created = true;
        if (!file.exists())
        {
            created = file.mkdir();
        }

        return created;
    }

    public static void createDirectory(Path dir)
    {
        try
        {
            if (!Files.isDirectory(dir))
            {
                if (Files.exists(dir))
                {
                    Files.delete(dir);
                }

                Files.createDirectories(dir);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static List<String> readFile(String file)
    {
        try
        {
            Path path = Paths.get(file);
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

}
