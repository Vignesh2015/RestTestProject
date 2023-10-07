
import files.Payload;
import files.ReusableMethods;
import io.restassured.RestAssured;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Basics {

    //public static void main(String args[]){
    @Test
    public void testRest() throws IOException {

        RestAssured.baseURI = "https://rahulshettyacademy.com";

        /****Adding the New place*******/
        /*String response = given().log().all()
                .queryParam("key","qaclick123")
                .header("Content-Type","application/json").body(Payload.addPlace())
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200)
                .body("scope",equalTo("APP"))
                .header("Server",equalTo("Apache/2.4.41 (Ubuntu)")).extract()
                .response().asString();*/

        String response = given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get("E:\\Current Documents\\Desktop\\AddPlace.json"))))
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200)
                .body("scope", equalTo("APP"))
                .header("Server", equalTo("Apache/2.4.52 (Ubuntu)")).extract()
                .response().asString();

        JsonPath js = ReusableMethods.jsonResponse(response);
        String addPlace = js.getString("place_id");
        System.out.println("Added Place is " + addPlace);
        System.out.println("New Test added");

        /****Updating the New Place*******/

        String updatePlace = "70 Summer walk, Africa";
        String updatePlaceRes = given().log().all().
                queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\"" + addPlace + "\",\n" +
                        "\"address\":\"" + updatePlace + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}\n").when().put("/maps/api/place/update/json")
                .then().log().all().assertThat()
                .statusCode(200).body("msg", equalTo("Address successfully updated"))
                .extract().response().asString();

        JsonPath js1 = ReusableMethods.jsonResponse(updatePlaceRes);
        String updatePlaceresp = js1.getString("msg");
        System.out.println("Update Place is " + updatePlaceresp);
        Assert.assertEquals(updatePlaceresp, "Address successfully updated");

        /******Fetching the Place which got updated*******/

        String getPlaceResponse = given().log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", addPlace)
                .when().get("/maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();

        System.out.println("Get Place Response is " + getPlaceResponse.toString());

        JsonPath getPlaceJson = ReusableMethods.jsonResponse(getPlaceResponse);
        String getPlaceValue = getPlaceJson.getString("address");
        System.out.println("Fetched place is " + getPlaceValue);
        Assert.assertEquals(getPlaceValue, updatePlace);


    }
}
