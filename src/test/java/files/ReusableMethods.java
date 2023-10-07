package files;

import io.restassured.path.json.JsonPath;

public class ReusableMethods {

    public static JsonPath jsonResponse(String JsonResponse){

        JsonPath js = new JsonPath(JsonResponse);
        return js;

    }
}
