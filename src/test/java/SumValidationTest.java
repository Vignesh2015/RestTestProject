import files.Payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SumValidationTest {

    @Test
    public void sumOfCoursesTest(){

        int sumofAllCourses= 0;
        JsonPath js = new JsonPath(Payload.courseDetails());
        int noOfCoursesCount = js.getInt("courses.size()");

        for(int i=0; i< noOfCoursesCount; i++){

            int coursePrices = js.getInt("courses["+i+"].price");
            int courseCopies = js.getInt("courses["+i+"].copies");
            int totalPriceAmount = coursePrices * courseCopies;
            System.out.println("Total course price Amount is "+totalPriceAmount);
            sumofAllCourses = sumofAllCourses + totalPriceAmount;

        }
        System.out.println("Total Prcice Amount is "+sumofAllCourses);

        int coursePurchaseAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println("Course Purchase Amount is "+coursePurchaseAmount);

        Assert.assertEquals(coursePurchaseAmount,sumofAllCourses);

    }
}
