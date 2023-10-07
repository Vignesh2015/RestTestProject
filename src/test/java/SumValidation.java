import files.Payload;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

public class SumValidation {

    @Test
     public void sumOfCourses(){


        JsonPath js = new JsonPath(Payload.courseDetails());
        /*Print No of courses returned by API*/
        int noOfCoursesCount  = js.getInt("courses.size()");
        System.out.println("Total count of courses is "+noOfCoursesCount);

        /*Print Purchase Amount*/
        int totalCoursepurchaseAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println("Total Purchase amount of all the courses is "+totalCoursepurchaseAmount);

        /*Print Title of the first course*/
        String firstTitle = js.getString("courses[0].title");
        System.out.println(firstTitle);

        /*Print All course titles and their respective Prices*/
       for(int i = 0;i< noOfCoursesCount; i++){
           String courseTitles = js.get("courses["+i+"].title");
           System.out.println("Course titles within API is "+courseTitles);

           System.out.println(js.get("courses["+i+"].price").toString());

       }

       /*Print no of copies sold by RPA Course*/

        System.out.println("****************");
        for(int i= 0;i<  noOfCoursesCount; i++){
            String courseTitle = js.get("courses["+i+"].title");
            if(courseTitle.equalsIgnoreCase("RPA")){
                int courseCopies = js.get("courses["+i+"].copies");
                System.out.println("Total course copies "+courseCopies);
                break;
            }
        }




    }
}
