package pojo_classes;

import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import pojo_classes.AddPlace;

public class SerializeTest {

    public static void main(String args[]){

        baseURI = "https://rahulshettyacademy.com";

        AddPlace addnewPlaces = new AddPlace();
        addnewPlaces.setAccuracy(60);
        addnewPlaces.setName("Frontline1 house");
        addnewPlaces.setPhone_number("\"(+91) 984 893 3937");
        addnewPlaces.setAddress("29, side12 layout, cohen 09");
        addnewPlaces.setWebsite("http://google.com");
        addnewPlaces.setLanguage("French-IN");

        Location ls = new Location();
        ls.setLat(-38.383495);
        ls.setLng(33.427366);
        addnewPlaces.setLocation(ls);

        List<String> addTypes = new ArrayList<String>();
        addTypes.add("shoe park1");
        addTypes.add("shop1");
        addnewPlaces.setTypes(addTypes);


        String addPlaceResponse = given().log().all().queryParam("key","qaclick123")
                .body(addnewPlaces).when().post("/maps/api/place/add/json").
                then().assertThat().statusCode(200).extract().response().asString();

        System.out.println(addPlaceResponse);

        JsonPath js = ReusableMethods.jsonResponse(addPlaceResponse);
        String placeId = js.getString("place_id");
        System.out.println("Add Place details is"+placeId);

    }
}
