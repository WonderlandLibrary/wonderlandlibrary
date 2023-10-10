// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.utils.Sort;
import java.io.Closeable;
import com.badlogic.gdx.utils.ComparableTimSort;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import java.io.Reader;
import java.io.InputStreamReader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import java.io.IOException;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.BufferedReader;
import java.util.Comparator;

public final class TextureAtlas
{
    static final String[] tuple;
    static final Comparator<TextureAtlasData.Region> indexComparator;
    
    static String readValue(final BufferedReader reader) throws IOException {
        final String line;
        final int colon;
        if ((colon = (line = reader.readLine()).indexOf(58)) == -1) {
            throw new GdxRuntimeException("Invalid line: " + line);
        }
        return line.substring(colon + 1).trim();
    }
    
    static int readTuple(final BufferedReader reader) throws IOException {
        final String line;
        final int colon;
        if ((colon = (line = reader.readLine()).indexOf(58)) == -1) {
            throw new GdxRuntimeException("Invalid line: " + line);
        }
        int lastMatch;
        int i;
        int comma;
        for (lastMatch = colon + 1, i = 0; i < 3 && (comma = line.indexOf(44, lastMatch)) != -1; lastMatch = comma + 1, ++i) {
            TextureAtlas.tuple[i] = line.substring(lastMatch, comma).trim();
        }
        TextureAtlas.tuple[i] = line.substring(lastMatch).trim();
        return i + 1;
    }
    
    static {
        tuple = new String[4];
        indexComparator = new Comparator<TextureAtlasData.Region>() {};
    }
    
    public static final class TextureAtlasData
    {
        private Array<Page> pages;
        private Array<Region> regions;
        
        public TextureAtlasData(final FileHandle packFile, final FileHandle imagesDir, boolean reader) {
            this.pages = new Array<Page>();
            this.regions = new Array<Region>();
            reader = new BufferedReader(new InputStreamReader(packFile.read()), 64);
            try {
                Page page = null;
                String line;
                while ((line = ((BufferedReader)reader).readLine()) != null) {
                    if (line.trim().length() == 0) {
                        page = null;
                    }
                    else if (page == null) {
                        final FileHandle file = imagesDir.child(line);
                        float width = 0.0f;
                        float height = 0.0f;
                        if (TextureAtlas.readTuple((BufferedReader)reader) == 2) {
                            width = (float)Integer.parseInt(TextureAtlas.tuple[0]);
                            height = (float)Integer.parseInt(TextureAtlas.tuple[1]);
                            TextureAtlas.readTuple((BufferedReader)reader);
                        }
                        final Pixmap.Format format = Pixmap.Format.valueOf(TextureAtlas.tuple[0]);
                        TextureAtlas.readTuple((BufferedReader)reader);
                        final Texture.TextureFilter min = Texture.TextureFilter.valueOf(TextureAtlas.tuple[0]);
                        final Texture.TextureFilter max = Texture.TextureFilter.valueOf(TextureAtlas.tuple[1]);
                        final String direction = TextureAtlas.readValue((BufferedReader)reader);
                        Texture.TextureWrap repeatX = Texture.TextureWrap.ClampToEdge;
                        Texture.TextureWrap repeatY = Texture.TextureWrap.ClampToEdge;
                        if (direction.equals("x")) {
                            repeatX = Texture.TextureWrap.Repeat;
                        }
                        else if (direction.equals("y")) {
                            repeatY = Texture.TextureWrap.Repeat;
                        }
                        else if (direction.equals("xy")) {
                            repeatX = Texture.TextureWrap.Repeat;
                            repeatY = Texture.TextureWrap.Repeat;
                        }
                        page = new Page(file, width, height, min.isMipMap(), format, min, max, repeatX, repeatY);
                        this.pages.add(page);
                    }
                    else {
                        final String rotateValue;
                        if (!(rotateValue = TextureAtlas.readValue((BufferedReader)reader)).equalsIgnoreCase("true") && !rotateValue.equalsIgnoreCase("false")) {
                            Integer.valueOf(rotateValue);
                        }
                        TextureAtlas.readTuple((BufferedReader)reader);
                        final int left = Integer.parseInt(TextureAtlas.tuple[0]);
                        final int top = Integer.parseInt(TextureAtlas.tuple[1]);
                        TextureAtlas.readTuple((BufferedReader)reader);
                        Integer.parseInt(TextureAtlas.tuple[0]);
                        Integer.parseInt(TextureAtlas.tuple[1]);
                        final Region region;
                        (region = new Region()).page = page;
                        region.left = left;
                        region.top = top;
                        region.name = line;
                        if (TextureAtlas.readTuple((BufferedReader)reader) == 4) {
                            final int[] array = { Integer.parseInt(TextureAtlas.tuple[0]), Integer.parseInt(TextureAtlas.tuple[1]), Integer.parseInt(TextureAtlas.tuple[2]), Integer.parseInt(TextureAtlas.tuple[3]) };
                            if (TextureAtlas.readTuple((BufferedReader)reader) == 4) {
                                final int[] array2 = { Integer.parseInt(TextureAtlas.tuple[0]), Integer.parseInt(TextureAtlas.tuple[1]), Integer.parseInt(TextureAtlas.tuple[2]), Integer.parseInt(TextureAtlas.tuple[3]) };
                                TextureAtlas.readTuple((BufferedReader)reader);
                            }
                        }
                        Integer.parseInt(TextureAtlas.tuple[0]);
                        Integer.parseInt(TextureAtlas.tuple[1]);
                        TextureAtlas.readTuple((BufferedReader)reader);
                        Integer.parseInt(TextureAtlas.tuple[0]);
                        Integer.parseInt(TextureAtlas.tuple[1]);
                        region.index = Integer.parseInt(TextureAtlas.readValue((BufferedReader)reader));
                        this.regions.add(region);
                    }
                }
            }
            catch (Exception ex) {
                throw new GdxRuntimeException("Error reading pack file: " + packFile, ex);
            }
            finally {
                ComparableTimSort.closeQuietly((Closeable)reader);
            }
            final Array<Region> regions = this.regions;
            final Comparator<Region> indexComparator = TextureAtlas.indexComparator;
            final Array<Region> array3 = regions;
            Sort.instance().sort((T[])array3.items, (Comparator<? super T>)indexComparator, 0, array3.size);
        }
        
        public final Array<Page> getPages() {
            return this.pages;
        }
        
        public final Array<Region> getRegions() {
            return this.regions;
        }
        
        public static final class Page
        {
            public Page(final FileHandle handle, final float width, final float height, final boolean useMipMaps, final Pixmap.Format format, final Texture.TextureFilter minFilter, final Texture.TextureFilter magFilter, final Texture.TextureWrap uWrap, final Texture.TextureWrap vWrap) {
            }
        }
        
        public static final class Region
        {
            public Page page;
            public int index;
            public String name;
            public int left;
            public int top;
        }
    }
}
