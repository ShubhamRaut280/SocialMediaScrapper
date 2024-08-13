package appiumtests.appiumtests;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    String instagramUsername ;
    String name;
    String imageurl ;

    public UserModel() {
    }

    public UserModel(String instagramUsername, String name, String imageurl) {
        this.instagramUsername = instagramUsername;
        this.name = name;
        this.imageurl = imageurl;
    }

    public String getInstagramUsername() {
        return instagramUsername;
    }

    public void setInstagramUsername(String instagramUsername) {
        this.instagramUsername = instagramUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public static void saveUserModelListAsJson(List<UserModel> userModels, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convert the list of UserModel objects to a JSON array string
            String jsonString = objectMapper.writeValueAsString(userModels);

            // Save the JSON array string to a file in the project directory
            File file = new File(System.getProperty("user.dir") + File.separator + fileName);
            objectMapper.writeValue(file, userModels);

            System.out.println("JSON array saved successfully at: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
