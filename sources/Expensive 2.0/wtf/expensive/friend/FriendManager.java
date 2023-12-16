package wtf.expensive.friend;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dedinside
 * @since 09.06.2023
 */
public class FriendManager {
    private final List<Friend> friends = new ArrayList<>();
    public static final File file = new File(Minecraft.getInstance().gameDir, "\\expensive\\friends.exp");

    /**
     * Инициализирует FriendManager, создавая файл, если он не существует, или читает данные из файла, если он существует.
     *
     * @throws IOException если возникает ошибка при создании файла или чтении данных
     */
    public void init() throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        } else {
            System.out.println("Прочитал файл с друзьями");
            readFriends();
        }
    }

    /**
     * Добавляет друга в список друзей и обновляет файл.
     *
     * @param name имя друга
     */
    public void addFriend(String name) {
        friends.add(new Friend(name));
        updateFile();
    }

    /**
     * Проверяет, является ли указанное имя другом.
     *
     * @param friend имя друга
     * @return true, если имя присутствует в списке друзей, в противном случае - false
     */
    public boolean isFriend(String friend) {
        return friends.stream()
                .anyMatch(isFriend -> isFriend.getName().equals(friend));
    }

    /**
     * Удаляет друга из списка друзей и обновляет файл.
     *
     * @param name имя друга
     */
    public void removeFriend(String name) {
        friends.removeIf(friend -> friend.getName().equalsIgnoreCase(name));
        updateFile();
    }

    /**
     * Очищает список друзей и обновляет файл.
     */
    public void clearFriend() {
        friends.clear();
        updateFile();
    }

    /**
     * Возвращает список друзей.
     *
     * @return список друзей
     */
    public List<Friend> getFriends() {
        return this.friends;
    }

    /**
     * Обновляет файл, сохраняя список друзей.
     */
    public void updateFile() {
        try {
            StringBuilder builder = new StringBuilder();
            friends.forEach(friend -> builder.append(friend.getName()).append("\n"));
            Files.write(file.toPath(), builder.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Читает данные из файла и заполняет список друзей.
     */
    private void readFriends() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file.getAbsolutePath()))));
            String line;
            while ((line = reader.readLine()) != null) {
                friends.add(new Friend(line));
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}