package wtf.expensive.util.misc;

import java.io.*;

/**
 * @author Jefferson
 * @since 30/11/2022
 */

public class FileUtil {

    /**
     * Метод создает новый объект StringBuilder для хранения содержимого входного потока.
     * Затем метод создает объект BufferedReader, который читает данные из входного потока построчно,
     * используя InputStreamReader для преобразования потока байтов в символы.
     * @param inputStream
     * @return
     */
    public static String readInputStream(InputStream inputStream) {
        // Создание объекта StringBuilder для хранения результата чтения
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Создание объекта BufferedReader для чтения входного потока
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Чтение входного потока построчно и добавление каждой строки в объект StringBuilder
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (Exception e) {
            // Обработка исключений при чтении входного потока
            e.printStackTrace();
        }

        // Преобразование объекта StringBuilder в строку и возврат результата
        return stringBuilder.toString();
    }
}
