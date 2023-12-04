package com.had0uken.sport_bot.utility;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
public class JsonGetter {

    static public String getLocalJson(String country_code) {
        StringBuilder content = new StringBuilder();

        String path="src/main/java/com/had0uken/sport_bot/temp/get_table_"+country_code+".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
    public String getJson(String path) {
        StringBuilder content = new StringBuilder();

        //src/ukraine.txt
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}