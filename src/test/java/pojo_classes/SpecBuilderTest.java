package pojo_classes;

import files.ReusableMethods;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class SpecBuilderTest {

    public static void main(String args[]){

        //baseURI = "https://rahulshettyacademy.com";

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

        RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key","qaclick123").setContentType(ContentType.JSON).build();

        RequestSpecification requestSpec = given().log().all().spec(reqSpec)
                .body(addnewPlaces);

        ResponseSpecification respSpec = new ResponseSpecBuilder().expectContentType(ContentType.JSON).expectStatusCode(200).build();

        String responseSpec = requestSpec.when().post("/maps/api/place/add/json").
                then().assertThat().spec(respSpec).extract().response().asString();

        System.out.println(responseSpec);

        JsonPath js = ReusableMethods.jsonResponse(responseSpec);
        String placeId = js.getString("place_id");
        System.out.println("Add Place details is "+placeId);

    }
}
