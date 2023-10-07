import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import pojo_classes.Api;
import pojo_classes.GetCourses;
import pojo_classes.WebAutomation;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OAuth_Test {

    public static void main(String[] args){

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String[] expectedCourseTitle = {"Selenium Webdriver Java","Cypress","Protractor"};

        String URL = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0Adeu5BXyjvpuxUl6mh23JGSPNb31yFQsY1dWipDAjHA72YUg1AVfvMBG-iGPIPuw1FA-eg&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";

        String code1 = URL.split("&scope")[0];
        String code2 = URL.split("&scope")[1];
        System.out.println(code1+"-----"+code2);
        String finalCode = code1.split("code=")[1];
        System.out.println(finalCode);

       /* WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();*/
        /****Source code to generate the Access Token****/
        String accessTokenResponse = given().urlEncodingEnabled(false).log().all().queryParams("code",finalCode)
                        .queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                                .queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
                                        .queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
                                                .queryParams("grant_type","authorization_code")
                                                        .when().post("https://www.googleapis.com/oauth2/v4/token")
                        .then().log().all().extract().response().asString();

        JsonPath js = ReusableMethods.jsonResponse(accessTokenResponse);
        String accessToken  = js.getString("access_token");
        System.out.println("Generated Access Token value is "+accessToken);



        /****Source code to use the generated Access Token to get the list of pojo_classes.Courses****/
        GetCourses  gc1 = given().log().all().queryParam("access_token",accessToken).expect().defaultParser(Parser.JSON)
                .when().get("/getCourse.php").as(GetCourses.class);

        System.out.println("Instructor name is "+gc1.getInstructor());
        System.out.println("URL is "+gc1.getUrl());
        System.out.println("LinkedIn is "+gc1.getLinkedIn());
        System.out.println(gc1.getCourses().getApi().get(1).getCourseTitle());

//        int getApiSize = gc1.getCourses().getApi().size();
//        System.out.println("API Size is "+getApiSize);
        List<Api> getApiSize  = gc1.getCourses().getApi();
        for(int i=0; i< getApiSize.size(); i++){
           if(getApiSize.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")){
                System.out.println(getApiSize.get(i).getPrice());
            }
        }

        ArrayList<String> actualWebAutomationCourseTitle = new ArrayList<String>();
        List<WebAutomation> webAutomationCourses  = gc1.getCourses().getWebAutomation();
        for(int j=0; j < webAutomationCourses.size();j++){
            actualWebAutomationCourseTitle.add(webAutomationCourses.get(j).getCourseTitle());
        }

        System.out.println(actualWebAutomationCourseTitle);

        List<String> expectedWebAutomationCourseTitle = Arrays.asList(expectedCourseTitle);
        System.out.println("Expected course title is "+expectedWebAutomationCourseTitle);

        if(!expectedWebAutomationCourseTitle.equals(actualWebAutomationCourseTitle)){
            System.out.println("Expected and Actual course titles are not matched");
        }else {
            System.out.println("Expected and Actual course titles is matched");
        }

        /**Comparing the Expected and Actual Course title of WebAutomation courses***/
        Assert.assertTrue(expectedWebAutomationCourseTitle.equals(actualWebAutomationCourseTitle));

    }
}
