
import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;

public class JiraTest {
    
    public static void main(String args[]){

        RestAssured.baseURI = "http://localhost:8080";
        String expectedCommentsMessage = "Fifth comments";

        /*****Login Scenario****/
        SessionFilter session = new SessionFilter();

        String response = given().relaxedHTTPSValidation().log().all().header("Content-Type","application/json")
                        .body("{ \n" +
                                "    \"username\": \"VigneshRavichandran\", \n" +
                                "    \"password\": \"Password^321\" \n" +
                                "    \n" +
                                "    }").filter(session).when().post("/rest/auth/1/session")
                        .then().log().all().extract().response().asString();

       /* *//*****Create New Issue Scenario****//*
        String createIssueID = given().log().all().header("Content-Type","application/json")
                .body(" {\n" +
                        "     \"fields\": {\n" +
                        "        \"project\": \n" +
                        "        {\n" +
                        "            \"key\": \"TES\"\n" +
                        "        },\n" +
                        "        \"summary\": \"Debit card is a Invalid one\",\n" +
                        "        \"description\": \"Debit card is a  Invalid one\",\n" +
                        "         \"issuetype\": {\n" +
                        "            \"name\": \"Bug\"\n" +
                        "        }\n" +
                        "     }\n" +
                        " }").filter(session).when().post("/rest/api/2/issue/")
                .then().log().all().assertThat().statusCode(201).extract().response().asString();

        JsonPath js = ReusableMethods.jsonResponse(createIssueID);
        String newIssueID = js.getString("id");
        System.out.println("Newly created Issue id is "+newIssueID);*/

        /*********Adding comments to the existing issue*********/
       String commentsAddedText= given().log().all().header("Content-Type","application/json").pathParam("id",10500).
                body("{\n" +
                "    \"body\": \""+expectedCommentsMessage+"\",\n" +
                "    \"visibility\": {\n" +
                "        \"type\": \"role\",\n" +
                "        \"value\": \"Administrators\"\n" +
                "    }\n" +
                "}").filter(session).when().post("/rest/api/2/issue/{id}/comment").
                then().log().all().assertThat().statusCode(201).extract().response().asString();
        System.out.println("Comments added text is "+commentsAddedText);

        JsonPath addedComments = ReusableMethods.jsonResponse(commentsAddedText);
        String actualCommentsId = addedComments.getString("id");
        System.out.println("Newly created Issue id is "+actualCommentsId);


       /* *//*****Update the exising comments****//*
        given().log().all().header("Content-Type","application/json").pathParam("id",newIssueID)
                        .pathParam("id",newIssueID).filter(session)
                .body("{\n" +
                        "    \"body\": \"Comments updated from REST API automation code response to the existing ticket\",\n" +
                        "    \"visibility\": {\n" +
                        "        \"type\": \"role\",\n" +
                        "        \"value\": \"Administrators\"\n" +
                        "    }\n" +
                        "}")
                        .when().put("/rest/api/2/issue/{id}/comment/{id}")
                        .then().log().all().assertThat().statusCode(200);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        *//*****Add Attachments to the newly created JIRA ticket*******//*

        given().log().all().pathParam("id",newIssueID).
                header("X-Atlassian-Token","no-check").
        header("Content-Type","multipart/form-data").filter(session)
                .multiPart("file",new File("jiratest.txt"))
                .when().post("/rest/api/2/issue/{id}/attachments")
                .then().log().all().assertThat().statusCode(200);

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

        /*************Get the Issue details through GET Issue API*****************/
        String getIssueDetails = given().log().all().pathParam("id",10500).queryParam("fields","comment")
                .filter(session).when().get("/rest/api/2/issue/{id}")
                        .then().log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath getIssueID = ReusableMethods.jsonResponse(getIssueDetails);
        int commentsCount = getIssueID.getInt("fields.comment.comments.size()");
        System.out.println("No of comments displayed are "+commentsCount);

        for(int i=0; i < commentsCount; i++){

            String commentsID = getIssueID.get("fields.comment.comments["+i+"].id").toString();
            System.out.println(commentsID);
            if(commentsID.equalsIgnoreCase(actualCommentsId)){
                String commentMessage = getIssueID.get("fields.comment.comments["+i+"].body").toString();
                System.out.println(commentMessage);
                Assert.assertEquals(commentMessage,expectedCommentsMessage);
                break;
            }
        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /*****Deleting the issue which got created******//*
        given().log().all().pathParam("id",newIssueID).filter(session)
                .when().delete("/rest/api/2/issue/{id}")
                .then().log().all().assertThat().statusCode(204);*/

    }
}
