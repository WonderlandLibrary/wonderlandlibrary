package org.newdawn.slick.tiled;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TiledMap {
   private static boolean headless;
   protected int width;
   protected int height;
   protected int tileWidth;
   protected int tileHeight;
   protected String tilesLocation;
   protected Properties props;
   protected ArrayList tileSets;
   protected ArrayList layers;
   protected ArrayList objectGroups;
   private boolean loadTileSets;

   private static void setHeadless(boolean var0) {
      headless = var0;
   }

   public TiledMap(String var1) throws SlickException {
      this(var1, true);
   }

   public TiledMap(String var1, boolean var2) throws SlickException {
      this.tileSets = new ArrayList();
      this.layers = new ArrayList();
      this.objectGroups = new ArrayList();
      this.loadTileSets = true;
      this.loadTileSets = var2;
      var1 = var1.replace('\\', '/');
      this.load(ResourceLoader.getResourceAsStream(var1), var1.substring(0, var1.lastIndexOf("/")));
   }

   public TiledMap(String var1, String var2) throws SlickException {
      this.tileSets = new ArrayList();
      this.layers = new ArrayList();
      this.objectGroups = new ArrayList();
      this.loadTileSets = true;
      this.load(ResourceLoader.getResourceAsStream(var1), var2);
   }

   public TiledMap(InputStream var1) throws SlickException {
      this.tileSets = new ArrayList();
      this.layers = new ArrayList();
      this.objectGroups = new ArrayList();
      this.loadTileSets = true;
      this.load(var1, "");
   }

   public TiledMap(InputStream var1, String var2) throws SlickException {
      this.tileSets = new ArrayList();
      this.layers = new ArrayList();
      this.objectGroups = new ArrayList();
      this.loadTileSets = true;
      this.load(var1, var2);
   }

   public String getTilesLocation() {
      return this.tilesLocation;
   }

   public int getLayerIndex(String var1) {
      boolean var2 = false;

      for(int var3 = 0; var3 < this.layers.size(); ++var3) {
         Layer var4 = (Layer)this.layers.get(var3);
         if (var4.name.equals(var1)) {
            return var3;
         }
      }

      return -1;
   }

   public Image getTileImage(int var1, int var2, int var3) {
      Layer var4 = (Layer)this.layers.get(var3);
      int var5 = var4.data[var1][var2][0];
      if (var5 >= 0 && var5 < this.tileSets.size()) {
         TileSet var6 = (TileSet)this.tileSets.get(var5);
         int var7 = var6.getTileX(var4.data[var1][var2][1]);
         int var8 = var6.getTileY(var4.data[var1][var2][1]);
         return var6.tiles.getSprite(var7, var8);
      } else {
         return null;
      }
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getTileHeight() {
      return this.tileHeight;
   }

   public int getTileWidth() {
      return this.tileWidth;
   }

   public int getTileId(int var1, int var2, int var3) {
      Layer var4 = (Layer)this.layers.get(var3);
      return var4.getTileID(var1, var2);
   }

   public void setTileId(int var1, int var2, int var3, int var4) {
      Layer var5 = (Layer)this.layers.get(var3);
      var5.setTileID(var1, var2, var4);
   }

   public String getMapProperty(String var1, String var2) {
      return this.props == null ? var2 : this.props.getProperty(var1, var2);
   }

   public String getLayerProperty(int var1, String var2, String var3) {
      Layer var4 = (Layer)this.layers.get(var1);
      return var4 != null && var4.props != null ? var4.props.getProperty(var2, var3) : var3;
   }

   public String getTileProperty(int var1, String var2, String var3) {
      if (var1 == 0) {
         return var3;
      } else {
         TileSet var4 = this.findTileSet(var1);
         Properties var5 = var4.getProperties(var1);
         return var5 == null ? var3 : var5.getProperty(var2, var3);
      }
   }

   public void render(int var1, int var2) {
      this.render(var1, var2, 0, 0, this.width, this.height, false);
   }

   public void render(int var1, int var2, int var3) {
      this.render(var1, var2, 0, 0, this.getWidth(), this.getHeight(), var3, false);
   }

   public void render(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.render(var1, var2, var3, var4, var5, var6, false);
   }

   public void render(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
      Layer var9 = (Layer)this.layers.get(var7);

      for(int var10 = 0; var10 < var6; ++var10) {
         var9.render(var1, var2, var3, var4, var5, var10, var8, this.tileWidth, this.tileHeight);
      }

   }

   public void render(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      for(int var8 = 0; var8 < var6; ++var8) {
         for(int var9 = 0; var9 < this.layers.size(); ++var9) {
            Layer var10 = (Layer)this.layers.get(var9);
            var10.render(var1, var2, var3, var4, var5, var8, var7, this.tileWidth, this.tileHeight);
         }
      }

   }

   public int getLayerCount() {
      return this.layers.size();
   }

   private void load(InputStream var1, String var2) throws SlickException {
      this.tilesLocation = var2;

      try {
         DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();
         var3.setValidating(false);
         DocumentBuilder var4 = var3.newDocumentBuilder();
         var4.setEntityResolver(new EntityResolver(this) {
            private final TiledMap this$0;

            {
               this.this$0 = var1;
            }

            public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
               return new InputSource(new ByteArrayInputStream(new byte[0]));
            }
         });
         Document var5 = var4.parse(var1);
         Element var6 = var5.getDocumentElement();
         String var7 = var6.getAttribute("orientation");
         if (!var7.equals("orthogonal")) {
            throw new SlickException("Only orthogonal maps supported, found: " + var7);
         } else {
            this.width = Integer.parseInt(var6.getAttribute("width"));
            this.height = Integer.parseInt(var6.getAttribute("height"));
            this.tileWidth = Integer.parseInt(var6.getAttribute("tilewidth"));
            this.tileHeight = Integer.parseInt(var6.getAttribute("tileheight"));
            Element var8 = (Element)var6.getElementsByTagName("properties").item(0);
            NodeList var9;
            int var10;
            Element var11;
            if (var8 != null) {
               var9 = var8.getElementsByTagName("property");
               if (var9 != null) {
                  this.props = new Properties();

                  for(var10 = 0; var10 < var9.getLength(); ++var10) {
                     var11 = (Element)var9.item(var10);
                     String var12 = var11.getAttribute("name");
                     String var13 = var11.getAttribute("value");
                     this.props.setProperty(var12, var13);
                  }
               }
            }

            if (this.loadTileSets) {
               var9 = null;
               TileSet var16 = null;
               NodeList var17 = var6.getElementsByTagName("tileset");

               for(int var20 = 0; var20 < var17.getLength(); ++var20) {
                  Element var22 = (Element)var17.item(var20);
                  TileSet var15 = new TileSet(this, var22, !headless);
                  var15.index = var20;
                  if (var16 != null) {
                     var16.setLimit(var15.firstGID - 1);
                  }

                  var16 = var15;
                  this.tileSets.add(var15);
               }
            }

            var9 = var6.getElementsByTagName("layer");

            for(var10 = 0; var10 < var9.getLength(); ++var10) {
               var11 = (Element)var9.item(var10);
               Layer var21 = new Layer(this, var11);
               var21.index = var10;
               this.layers.add(var21);
            }

            NodeList var18 = var6.getElementsByTagName("objectgroup");

            for(int var19 = 0; var19 < var18.getLength(); ++var19) {
               Element var23 = (Element)var18.item(var19);
               TiledMap.ObjectGroup var24 = new TiledMap.ObjectGroup(this, var23);
               var24.index = var19;
               this.objectGroups.add(var24);
            }

         }
      } catch (Exception var14) {
         Log.error((Throwable)var14);
         throw new SlickException("Failed to parse tilemap", var14);
      }
   }

   public int getTileSetCount() {
      return this.tileSets.size();
   }

   public TileSet getTileSet(int var1) {
      return (TileSet)this.tileSets.get(var1);
   }

   public TileSet getTileSetByGID(int var1) {
      for(int var2 = 0; var2 < this.tileSets.size(); ++var2) {
         TileSet var3 = (TileSet)this.tileSets.get(var2);
         if (var3.contains(var1)) {
            return var3;
         }
      }

      return null;
   }

   public TileSet findTileSet(int var1) {
      for(int var2 = 0; var2 < this.tileSets.size(); ++var2) {
         TileSet var3 = (TileSet)this.tileSets.get(var2);
         if (var3.contains(var1)) {
            return var3;
         }
      }

      return null;
   }

   protected void renderedLine(int var1, int var2, int var3) {
   }

   public int getObjectGroupCount() {
      return this.objectGroups.size();
   }

   public int getObjectCount(int var1) {
      if (var1 >= 0 && var1 < this.objectGroups.size()) {
         TiledMap.ObjectGroup var2 = (TiledMap.ObjectGroup)this.objectGroups.get(var1);
         return var2.objects.size();
      } else {
         return -1;
      }
   }

   public String getObjectName(int var1, int var2) {
      if (var1 >= 0 && var1 < this.objectGroups.size()) {
         TiledMap.ObjectGroup var3 = (TiledMap.ObjectGroup)this.objectGroups.get(var1);
         if (var2 >= 0 && var2 < var3.objects.size()) {
            TiledMap.GroupObject var4 = (TiledMap.GroupObject)var3.objects.get(var2);
            return var4.name;
         }
      }

      return null;
   }

   public String getObjectType(int var1, int var2) {
      if (var1 >= 0 && var1 < this.objectGroups.size()) {
         TiledMap.ObjectGroup var3 = (TiledMap.ObjectGroup)this.objectGroups.get(var1);
         if (var2 >= 0 && var2 < var3.objects.size()) {
            TiledMap.GroupObject var4 = (TiledMap.GroupObject)var3.objects.get(var2);
            return var4.type;
         }
      }

      return null;
   }

   public int getObjectX(int var1, int var2) {
      if (var1 >= 0 && var1 < this.objectGroups.size()) {
         TiledMap.ObjectGroup var3 = (TiledMap.ObjectGroup)this.objectGroups.get(var1);
         if (var2 >= 0 && var2 < var3.objects.size()) {
            TiledMap.GroupObject var4 = (TiledMap.GroupObject)var3.objects.get(var2);
            return var4.x;
         }
      }

      return -1;
   }

   public int getObjectY(int var1, int var2) {
      if (var1 >= 0 && var1 < this.objectGroups.size()) {
         TiledMap.ObjectGroup var3 = (TiledMap.ObjectGroup)this.objectGroups.get(var1);
         if (var2 >= 0 && var2 < var3.objects.size()) {
            TiledMap.GroupObject var4 = (TiledMap.GroupObject)var3.objects.get(var2);
            return var4.y;
         }
      }

      return -1;
   }

   public int getObjectWidth(int var1, int var2) {
      if (var1 >= 0 && var1 < this.objectGroups.size()) {
         TiledMap.ObjectGroup var3 = (TiledMap.ObjectGroup)this.objectGroups.get(var1);
         if (var2 >= 0 && var2 < var3.objects.size()) {
            TiledMap.GroupObject var4 = (TiledMap.GroupObject)var3.objects.get(var2);
            return var4.width;
         }
      }

      return -1;
   }

   public int getObjectHeight(int var1, int var2) {
      if (var1 >= 0 && var1 < this.objectGroups.size()) {
         TiledMap.ObjectGroup var3 = (TiledMap.ObjectGroup)this.objectGroups.get(var1);
         if (var2 >= 0 && var2 < var3.objects.size()) {
            TiledMap.GroupObject var4 = (TiledMap.GroupObject)var3.objects.get(var2);
            return var4.height;
         }
      }

      return -1;
   }

   public String getObjectImage(int var1, int var2) {
      if (var1 >= 0 && var1 < this.objectGroups.size()) {
         TiledMap.ObjectGroup var3 = (TiledMap.ObjectGroup)this.objectGroups.get(var1);
         if (var2 >= 0 && var2 < var3.objects.size()) {
            TiledMap.GroupObject var4 = (TiledMap.GroupObject)var3.objects.get(var2);
            if (var4 == null) {
               return null;
            }

            return TiledMap.GroupObject.access$000(var4);
         }
      }

      return null;
   }

   public String getObjectProperty(int var1, int var2, String var3, String var4) {
      if (var1 >= 0 && var1 < this.objectGroups.size()) {
         TiledMap.ObjectGroup var5 = (TiledMap.ObjectGroup)this.objectGroups.get(var1);
         if (var2 >= 0 && var2 < var5.objects.size()) {
            TiledMap.GroupObject var6 = (TiledMap.GroupObject)var5.objects.get(var2);
            if (var6 == null) {
               return var4;
            }

            if (var6.props == null) {
               return var4;
            }

            return var6.props.getProperty(var3, var4);
         }
      }

      return var4;
   }

   protected class GroupObject {
      public int index;
      public String name;
      public String type;
      public int x;
      public int y;
      public int width;
      public int height;
      private String image;
      public Properties props;
      private final TiledMap this$0;

      public GroupObject(TiledMap var1, Element var2) throws SlickException {
         this.this$0 = var1;
         this.name = var2.getAttribute("name");
         this.type = var2.getAttribute("type");
         this.x = Integer.parseInt(var2.getAttribute("x"));
         this.y = Integer.parseInt(var2.getAttribute("y"));
         this.width = Integer.parseInt(var2.getAttribute("width"));
         this.height = Integer.parseInt(var2.getAttribute("height"));
         Element var3 = (Element)var2.getElementsByTagName("image").item(0);
         if (var3 != null) {
            this.image = var3.getAttribute("source");
         }

         Element var4 = (Element)var2.getElementsByTagName("properties").item(0);
         if (var4 != null) {
            NodeList var5 = var4.getElementsByTagName("property");
            if (var5 != null) {
               this.props = new Properties();

               for(int var6 = 0; var6 < var5.getLength(); ++var6) {
                  Element var7 = (Element)var5.item(var6);
                  String var8 = var7.getAttribute("name");
                  String var9 = var7.getAttribute("value");
                  this.props.setProperty(var8, var9);
               }
            }
         }

      }

      static String access$000(TiledMap.GroupObject var0) {
         return var0.image;
      }
   }

   protected class ObjectGroup {
      public int index;
      public String name;
      public ArrayList objects;
      public int width;
      public int height;
      public Properties props;
      private final TiledMap this$0;

      public ObjectGroup(TiledMap var1, Element var2) throws SlickException {
         this.this$0 = var1;
         this.name = var2.getAttribute("name");
         this.width = Integer.parseInt(var2.getAttribute("width"));
         this.height = Integer.parseInt(var2.getAttribute("height"));
         this.objects = new ArrayList();
         Element var3 = (Element)var2.getElementsByTagName("properties").item(0);
         NodeList var4;
         int var5;
         Element var6;
         if (var3 != null) {
            var4 = var3.getElementsByTagName("property");
            if (var4 != null) {
               this.props = new Properties();

               for(var5 = 0; var5 < var4.getLength(); ++var5) {
                  var6 = (Element)var4.item(var5);
                  String var7 = var6.getAttribute("name");
                  String var8 = var6.getAttribute("value");
                  this.props.setProperty(var7, var8);
               }
            }
         }

         var4 = var2.getElementsByTagName("object");

         for(var5 = 0; var5 < var4.getLength(); ++var5) {
            var6 = (Element)var4.item(var5);
            TiledMap.GroupObject var9 = var1.new GroupObject(var1, var6);
            var9.index = var5;
            this.objects.add(var9);
         }

      }
   }
}
