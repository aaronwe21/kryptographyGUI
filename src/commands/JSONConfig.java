package commands;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONConfig {

    public void readJSONConfig(){
        String path = System.getProperty("user.dir") + "/src/Config.json";
        try {
            String contents = new String ((Files.readAllBytes(Paths.get(path))));
            JSONObject ob = new JSONObject(contents);
            JSONArray keys = ob.getJSONArray("key");
            for (int i = 0; i < keys.length(); i++){
                System.out.println(keys.get(i));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
