package lession12.provider;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginProviderBTTH {
    @DataProvider(name = "BTTH_Login")
    public Object[][] BTTH_Login_Data2(Method method) {
        // Read content from data file
        String dataFile = System.getProperty("user.dir") + File.separator + "src/test/java/lession12/data/loginData2.json";
        JSONObject jsonData = readContentFromJson2File(dataFile);

        // Convert to a map and parse to data list
        Map dataMap = jsonData.toMap();
        List jsonList = (List<HashMap>) dataMap.get(method.getName());

        // Add data to Objects
        Object[][] result  = new Object[jsonList.size()][1];
        for (int i = 0; i < jsonList.size(); i++) {
            result[i][0] = jsonList.get(i);
        }
        return result;
    }

    private JSONObject readContentFromJson2File(String filePath) {
        try {
            File file = new File(filePath);
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            return new JSONObject(content);
        } catch (Exception e) {
            System.out.println("Error reading : " + e.getMessage());
        }
        return null;
    }
}