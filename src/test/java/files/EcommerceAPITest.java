package files;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.openqa.selenium.json.Json;
import org.testng.Assert;
import pojo_classes.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class EcommerceAPITest {

    public static void main(String args[]){

        RestAssured.baseURI = "https://rahulshettyacademy.com";

        System.out.println("**********Execution of Login Test cases******************");
        RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
                setContentType(ContentType.JSON).build();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserEmail("vignesh.ravimaha@gmail.com");
        loginRequest.setUserPassword("Password@123");

        RequestSpecification reqLogin = given().spec(reqSpec).body(loginRequest);

        ResponseSpecification respSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

        //LoginResponse loginResponse = new LoginResponse();
        LoginResponse response = reqLogin.when().post("/api/ecom/auth/login").then().spec(respSpec)
                .log().all().extract().response().as(LoginResponse.class);

        String tokenId = response.getToken();
        System.out.println("Token value is "+tokenId);
        String userId = response.getUserId();
        System.out.println("LoggedIn UserId is "+userId);
        System.out.println("Message displayed is "+response.getMessage());

        RequestSpecification  addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
                setContentType(ContentType.MULTIPART)
                        .addHeader("Authorization",tokenId).build();

        /**Add a product**/
        System.out.println("**********Execution of Add Product Test cases******************");

        String addProductReq = given().log().all().spec(addProductBaseReq).param("productName","Laptop")
                .param("productAddedBy",userId).param("productCategory","Electronics")
                .param("productSubCategory","Gadgets").param("productPrice","38500")
                .param("productDescription","Dell Inspiron")
                .param("productFor","All")
                .multiPart("productImage",new File("Laptop.jpg"))
                .when().post("/api/ecom/product/add-product").
                then().log().all().statusCode(201).extract().response().asString();

        JsonPath addProductExtract = ReusableMethods.jsonResponse(addProductReq);

        String productAddedDetails = addProductExtract.getString("productId");
        System.out.println("Product Added is "+productAddedDetails);

        /*Add to Cart Test cases*/
        System.out.println("**********Execution of Add to Cart Test cases******************");
        RequestSpecification addCartBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
                addHeader("Authorization",tokenId).setContentType(ContentType.JSON).build();

        AddCartProduct addCartProduct = new AddCartProduct();
        addCartProduct.set_id(productAddedDetails);
        addCartProduct.setProductName("Laptop");
        addCartProduct.setProductCategory("Electronics");
        addCartProduct.setProductSubCategory("Gadgets");
        addCartProduct.setProductPrice(38500);
        addCartProduct.setProductDescription("Dell Inspiron");
        addCartProduct.setProductImage("https://rahulshettyacademy.com/api/ecom/uploads/productImage_1691676238197.png");
        addCartProduct.setProductRating("0");
        addCartProduct.setProductTotalOrders("0");
        addCartProduct.setProductStatus(true);
        addCartProduct.setProductFor("All");
        addCartProduct.setProductAddedBy(userId);
        addCartProduct.set__v(0);

        AddCartProduct addCart = new AddCartProduct();
        addCart.set_id(userId);
        addCart.setProduct(addCartProduct);
        RequestSpecification addCartReqSpec = given().log().all().spec(addCartBaseReq).body(addCart);

        String addCartResp = addCartReqSpec.when().post("/api/ecom/user/add-to-cart")
                .then().log().all().extract().response().asString();

        JsonPath js1 = ReusableMethods.jsonResponse(addCartResp);
        String addCartResponse = js1.getString("message");
        Assert.assertEquals("Product Added To Cart",addCartResponse);

        /***************Create a Purchase order by adding the Product to cart*******************/
        System.out.println("**********Execution of Purchase order Test cases******************");

        RequestSpecification createOrderBaseSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
                addHeader("Authorization",tokenId).setContentType(ContentType.JSON).build();

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCountry("India");
        orderDetail.setProductOrderedId(productAddedDetails);

        List<OrderDetail> addmultipleOrders = new ArrayList<OrderDetail>();
        addmultipleOrders.add(orderDetail);

        Orders order = new Orders();
        order.setOrders(addmultipleOrders);

        RequestSpecification createOrderReqSpec = given().log().all().spec(createOrderBaseSpec).body(order);
        String createOrderRespSpec = createOrderReqSpec.when().post("/api/ecom/order/create-order")
                .then().log().all().extract().response().asString();

        JsonPath js = ReusableMethods.jsonResponse(createOrderRespSpec);
        String orderID = js.getString("orders");
        System.out.println("Purchase order details is "+orderID.toString());

       /* *//********View Purchased order details ***************//*

        String viewOrderDetails = given().urlEncodingEnabled(false).log().all().queryParam("id", orderID).
                header("Authorization",tokenId)
                .when().get("/api/ecom/order/get-orders-details")
                .then().log().all()
                .extract().response().asString();

         *//*RequestSpecification viewPurchasedBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
                 addHeader("Authorization",tokenId).addQueryParam("id",orderID).setContentType(ContentType.JSON).build();

         RequestSpecification viewPurchaseOrderReq = given().log().all().spec(viewPurchasedBaseReq);

         String viewOrderDetails = viewPurchaseOrderReq.when().get("/api/ecom/order/get-orders-details")
                 .then().log().all().extract().response().asString();*//*

         JsonPath viewPurchasedOrderDetails = ReusableMethods.jsonResponse(viewOrderDetails);

         String userIDDetails = viewPurchasedOrderDetails.getString("_id");
         System.out.println("User ID details is "+userIDDetails);
         String orderIDUser = viewPurchasedOrderDetails.get("orderById");
         System.out.println("User Order ID is"+orderIDUser);


        *//*System.out.println(addCartProduct.get_id()+"----->"+addCartProduct.getProductName()+"----->"+addCartProduct.getProductCategory()
        +"---->"+addCartProduct.getProductAddedBy()+"--->"+addCartProduct.getProductSubCategory());
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        viewOrderDetails.getProduct();*/

        /*****To delete a Product from the Product ID*****/
        System.out.println("**********Execution of Delete Product Test cases******************");
        RequestSpecification deleteProBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
                setContentType(ContentType.JSON).addHeader("Authorization",tokenId).build();

        RequestSpecification deleteProdCreateReq = given().log().all().spec(deleteProBaseReq).pathParam("productId",productAddedDetails);
        String deleteProdResp = deleteProdCreateReq.when().delete("/api/ecom/product/delete-product/{productId}").then()
                .log().all().extract().response().asString();

        JsonPath deleteProdID = ReusableMethods.jsonResponse(deleteProdResp);
        String deleteProdMessage = deleteProdID.getString("message");
        Assert.assertEquals("Product Deleted Successfully",deleteProdMessage);
    }
}
