package files;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DynamicJson {


    @Test(dataProvider = "BookData")
    public void addBook(String isbn,String aisle) throws InterruptedException {

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String addBookResponse = given().log().all().header("Content-Type", "application/json")
                .body(Payload.addBookPayload(isbn,aisle))
                .when().post("/Library/Addbook.php")
                .then().log().all().assertThat()
                .statusCode(200).extract()
                .response().asString();

        JsonPath addBookJsonRes = ReusableMethods.jsonResponse(addBookResponse);
        String addBookID = addBookJsonRes.getString("ID");
        System.out.println("Newly added book details is " + addBookID);
        Thread.sleep(10000);

       /* given().log().all().header("Content-Type", "application/json")
                .body(Payload.deleteBook(isbn,aisle))
                .when().post("Library/DeleteBook.php")
                .then().log().all().assertThat().statusCode(200);*/

        given().log().all().header("Content-Type", "application/json")
                .body(Payload.deleteBook(""+addBookID+""))
                .when().post("Library/DeleteBook.php")
                .then().log().all().assertThat().statusCode(200);

    }

    @DataProvider(name="BookData")
    public Object[][] getBooksData(){

        return new Object[][]{{"dsdduk","7564"},{"aserv","62098"},{"kjtuyj","76098"}};

    }

}

