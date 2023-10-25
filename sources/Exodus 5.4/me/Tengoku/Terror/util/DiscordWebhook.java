/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;

public class DiscordWebhook {
    private final String url;
    private String content;
    private List<EmbedObject> embeds = new ArrayList<EmbedObject>();
    private String username;
    private boolean tts;
    private String avatarUrl;

    public void setAvatarUrl(String string) {
        this.avatarUrl = string;
    }

    public void setTts(Boolean bl) {
        this.tts = bl;
    }

    public void setContent(String string) {
        this.content = string;
    }

    public DiscordWebhook(String string) {
        this.url = string;
    }

    public void setUsername(String string) {
        this.username = string;
    }

    public void execute() throws IOException {
        Serializable serializable;
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("content", this.content);
        jSONObject.put("username", this.username);
        jSONObject.put("avatar_url", this.avatarUrl);
        jSONObject.put("tts", this.tts);
        if (!this.embeds.isEmpty()) {
            serializable = new ArrayList();
            for (EmbedObject object2 : this.embeds) {
                Object object;
                Object object3;
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("title", object2.getTitle());
                jSONObject2.put("description", object2.getDescription());
                jSONObject2.put("url", object2.getUrl());
                if (object2.getColor() != null) {
                    object3 = object2.getColor();
                    int n = ((Color)object3).getRed();
                    n = (n << 8) + ((Color)object3).getGreen();
                    n = (n << 8) + ((Color)object3).getBlue();
                    jSONObject2.put("color", n);
                }
                object3 = object2.getFooter();
                EmbedObject.Image n = object2.getImage();
                EmbedObject.Thumbnail thumbnail = object2.getThumbnail();
                EmbedObject.Author author = object2.getAuthor();
                List<EmbedObject.Field> list = object2.getFields();
                if (object3 != null) {
                    object = new JSONObject();
                    ((JSONObject)object).put("text", ((EmbedObject.Footer)object3).getText());
                    ((JSONObject)object).put("icon_url", ((EmbedObject.Footer)object3).getIconUrl());
                    jSONObject2.put("footer", object);
                }
                if (n != null) {
                    object = new JSONObject();
                    ((JSONObject)object).put("url", n.getUrl());
                    jSONObject2.put("image", object);
                }
                if (thumbnail != null) {
                    object = new JSONObject();
                    ((JSONObject)object).put("url", thumbnail.getUrl());
                    jSONObject2.put("thumbnail", object);
                }
                if (author != null) {
                    object = new JSONObject();
                    ((JSONObject)object).put("name", author.getName());
                    ((JSONObject)object).put("url", author.getUrl());
                    ((JSONObject)object).put("icon_url", author.getIconUrl());
                    jSONObject2.put("author", object);
                }
                object = new ArrayList();
                for (EmbedObject.Field field : list) {
                    JSONObject jSONObject3 = new JSONObject();
                    jSONObject3.put("name", field.getName());
                    jSONObject3.put("value", field.getValue());
                    jSONObject3.put("inline", field.isInline());
                    object.add(jSONObject3);
                }
                jSONObject2.put("fields", object.toArray());
                serializable.add(jSONObject2);
            }
            jSONObject.put("embeds", serializable.toArray());
        }
        serializable = new URL(this.url);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)((URL)serializable).openConnection();
        httpsURLConnection.addRequestProperty("Content-Type", "application/json");
        httpsURLConnection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
        httpsURLConnection.setDoOutput(true);
        httpsURLConnection.setRequestMethod("POST");
        OutputStream outputStream = httpsURLConnection.getOutputStream();
        outputStream.write(jSONObject.toString().getBytes());
        outputStream.flush();
        outputStream.close();
        httpsURLConnection.getInputStream().close();
        httpsURLConnection.disconnect();
    }

    public void addEmbed(EmbedObject embedObject) {
        this.embeds.add(embedObject);
    }

    public static class EmbedObject {
        private Image image;
        private List<Field> fields = new ArrayList<Field>();
        private String url;
        private Color color;
        private String description;
        private Thumbnail thumbnail;
        private Footer footer;
        private String title;
        private Author author;

        public Footer getFooter() {
            return this.footer;
        }

        public EmbedObject setColor(Color color) {
            this.color = color;
            return this;
        }

        public EmbedObject setThumbnail(String string) {
            this.thumbnail = new Thumbnail(string);
            return this;
        }

        public EmbedObject setImage(String string) {
            this.image = new Image(string);
            return this;
        }

        public List<Field> getFields() {
            return this.fields;
        }

        public Image getImage() {
            return this.image;
        }

        public EmbedObject setFooter(String string, String string2) {
            this.footer = new Footer(string, string2);
            return this;
        }

        public String getTitle() {
            return this.title;
        }

        public EmbedObject addField(String string, String string2, boolean bl) {
            this.fields.add(new Field(string, string2, bl));
            return this;
        }

        public Thumbnail getThumbnail() {
            return this.thumbnail;
        }

        public EmbedObject setTitle(String string) {
            this.title = string;
            return this;
        }

        public Color getColor() {
            return this.color;
        }

        public EmbedObject setDescription(String string) {
            this.description = string;
            return this;
        }

        public EmbedObject setUrl(String string) {
            this.url = string;
            return this;
        }

        public EmbedObject setAuthor(String string, String string2, String string3) {
            this.author = new Author(string, string2, string3);
            return this;
        }

        public String getDescription() {
            return this.description;
        }

        public String getUrl() {
            return this.url;
        }

        public Author getAuthor() {
            return this.author;
        }

        private class Thumbnail {
            private String url;

            private Thumbnail(String string) {
                this.url = string;
            }

            private String getUrl() {
                return this.url;
            }
        }

        private class Image {
            private String url;

            private Image(String string) {
                this.url = string;
            }

            private String getUrl() {
                return this.url;
            }
        }

        private class Field {
            private String value;
            private String name;
            private boolean inline;

            private String getName() {
                return this.name;
            }

            private Field(String string, String string2, boolean bl) {
                this.name = string;
                this.value = string2;
                this.inline = bl;
            }

            private String getValue() {
                return this.value;
            }

            private boolean isInline() {
                return this.inline;
            }
        }

        private class Footer {
            private String text;
            private String iconUrl;

            private Footer(String string, String string2) {
                this.text = string;
                this.iconUrl = string2;
            }

            private String getText() {
                return this.text;
            }

            private String getIconUrl() {
                return this.iconUrl;
            }
        }

        private class Author {
            private String name;
            private String url;
            private String iconUrl;

            private String getName() {
                return this.name;
            }

            private String getUrl() {
                return this.url;
            }

            private Author(String string, String string2, String string3) {
                this.name = string;
                this.url = string2;
                this.iconUrl = string3;
            }

            private String getIconUrl() {
                return this.iconUrl;
            }
        }
    }

    private class JSONObject {
        private final HashMap<String, Object> map = new HashMap();

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            Set<Map.Entry<String, Object>> set = this.map.entrySet();
            stringBuilder.append("{");
            int n = 0;
            for (Map.Entry<String, Object> entry : set) {
                Object object = entry.getValue();
                stringBuilder.append(this.quote(entry.getKey())).append(":");
                if (object instanceof String) {
                    stringBuilder.append(this.quote(String.valueOf(object)));
                } else if (object instanceof Integer) {
                    stringBuilder.append(Integer.valueOf(String.valueOf(object)));
                } else if (object instanceof Boolean) {
                    stringBuilder.append(object);
                } else if (object instanceof JSONObject) {
                    stringBuilder.append(object.toString());
                } else if (object.getClass().isArray()) {
                    stringBuilder.append("[");
                    int n2 = Array.getLength(object);
                    int n3 = 0;
                    while (n3 < n2) {
                        stringBuilder.append(Array.get(object, n3).toString()).append(n3 != n2 - 1 ? "," : "");
                        ++n3;
                    }
                    stringBuilder.append("]");
                }
                stringBuilder.append(++n == set.size() ? "}" : ",");
            }
            return stringBuilder.toString();
        }

        void put(String string, Object object) {
            if (object != null) {
                this.map.put(string, object);
            }
        }

        private String quote(String string) {
            return "\"" + string + "\"";
        }

        private JSONObject() {
        }
    }
}

