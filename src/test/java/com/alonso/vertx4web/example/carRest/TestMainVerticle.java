package com.alonso.vertx4web.example.carRest;

import com.alonso.vertx4web.example.carRest.model.dto.CarRecord;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.hamcrest.Matchers;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEmptyString.emptyString;


import java.math.BigDecimal;
import java.util.Collections;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }
 /*

  @Test
  @Order(0)
  void testListAllCars() {
    //List all, should have all some of the cars the database has initially (import.sql)
    Response response = given()
      .when()
      .get("/car/")
      .then()
      .statusCode(HttpResponseStatus.OK.code())
      .contentType("application/json")
      .extract().response();
    assertThat(response.jsonPath().getList("name"), Matchers.hasItems("Diablo", "Enzo", "Uno Mille", "x1"));
  }

  @Test
  @Order(1)
  void testEntityNotFoundForDelete() {
    given()
      .when()
      .delete("/car/999")
      .then()
      .statusCode(HttpResponseStatus.NOT_FOUND.code())
      .body(emptyString());
  }

  @Test
  @Order(2)
  void testEntityNotFoundForUpdate() {
    CarRecord carRecord = new CarRecord("Fusion", "Ford", BigDecimal.valueOf(99000), "Hatchback");
    given()
      .when()
      .body(carRecord)
      .contentType("application/json")
      .put("/car/6767")
      .then()
      .statusCode(HttpResponseStatus.NOT_FOUND.code())
      .body(emptyString());
  }

  @Test
  @Order(3)
  void testEntityNotFoundForGetId() {
    given()
      .when()
      .contentType("application/json")
      .get("/car/6767")
      .then()
      .statusCode(HttpResponseStatus.NOT_FOUND.code())
      .body(emptyString());
  }

  @Test
  @Order(4)
  void testEntityNotFoundForGetName() {
    given()
      .when()
      .contentType("application/json")
      .get("/car/name/Onix")
      .then()
      .statusCode(HttpResponseStatus.NO_CONTENT.code())
      .body(emptyString());
  }

  @Test
  @Order(5)
  void testEntityFoundForGetName() {
    Response response = given()
      .when()
      .contentType("application/json")
      .get("/car/name/x5")
      .then()
      .statusCode(HttpResponseStatus.OK.code())
      .contentType("application/json")
      .extract().response();
    assertThat(response.jsonPath().get("name"), Matchers.equalTo("x5"));
    assertThat(response.jsonPath().get("brand"), Matchers.equalTo("BMW"));
  }

  @Test
  @Order(6)
  void testGetCarByBrandName() {
    // Given
    String carBrand = "Ferrari";

    // When/Then
    Response response = given()
      .when().get("/car/brand/{carBrand}", carBrand)
      .then()
      .statusCode(HttpResponseStatus.OK.code()).contentType("application/json")
      .extract().response();
    assertThat(response.jsonPath().getList("name"), Matchers.hasItems("Enzo", "F50"));
    assertThat(response.jsonPath().getList("brand"), Matchers.everyItem(Matchers.equalTo("Ferrari")));
  }

  @Test
  @Order(7)
  void testGetCarByBrandNameNonExistent() {
    // Given
    String carBrand = "TestNonExistent";

    // When/Then
    given()
      .when().get("/car/brand/{carBrand}", carBrand)
      .then()
      .statusCode(HttpResponseStatus.OK.code()).contentType("application/json")
      .body("", equalTo(Collections.emptyList()));
    ;
  }

  @Test
  @Order(8)
  void testUpdateCar() {
    // Given
    long carId = 1L;
    CarRecord carRecord = new CarRecord("Fusion", "Ford", BigDecimal.valueOf(70000), "Hatchback");

    // When/Then
    given().header("Content-type", "application/json")
      .and()
      .body(carRecord)
      .when().put("/car/{id}", carId)
      .then()
      .statusCode(HttpResponseStatus.ACCEPTED.code());
  }

  @Test
  @Order(9)
  void testDeleteCar() {
    // Given
    long carId = 1L;

    // When/Then
    given().header("Content-type", "application/json")
      .when().delete("/car/{id}", carId)
      .then()
      .statusCode(HttpResponseStatus.NO_CONTENT.code());
  }

  @Test
  @Order(10)
  void testPostCar() {
    // Given
    CarRecord carRecord = new CarRecord("Ka", "Ford", BigDecimal.valueOf(25000), "Mini Car");

    // When / Then
    given().header("Content-type", "application/json")
      .and()
      .body(carRecord)
      .when().post("/car")
      .then()
      .statusCode(HttpResponseStatus.CREATED.code());
  }

  @Test
  @Order(11)
  void testGetCarByRangeNotPossible() {
    // Given
    Double startPrice = 0.0;
    Double finalPrice = 1.0;

    // When/Then
    given()
      .when().get("/car/price-range/?startPrice={startPrice}&finalPrice={finalPrice}", startPrice, finalPrice)
      .then()
      .statusCode(HttpResponseStatus.OK.code())
      .body("", equalTo(Collections.emptyList()));
  }

  @Test
  @Order(12)
  void testGetCarByRange() {
    // Given
    Double startPrice = 9500.0;
    Double finalPrice = 110000.0;

    // When/Then
    Response response = given()
      .when().get("/car/price-range/?startPrice={startPrice}&finalPrice={finalPrice}", startPrice, finalPrice)
      .then()
      .statusCode(HttpResponseStatus.OK.code())
      .contentType("application/json")
      .extract().response();
    assertThat(response.jsonPath().getList("name"), Matchers.hasItems("Uno Mille"));
  }

  @Test
  @Order(13)
  void testGetCarById() {
    // Given
    Long carId = 3L;

    // When/Then
    Response response = given()
      .when().get("/car/{id}", carId)
      .then()
      .statusCode(HttpResponseStatus.OK.code()).contentType("application/json")
      .extract().response();
    assertThat(response.jsonPath().get("id").toString(), Matchers.equalTo(carId.toString()));
  }

  @Test
  @Order(14)
  void testListAllCarsPaged() {
    //List all, should have all some of the cars the database has initially (import.sql)
    Response response = given()
      .when()
      .get("/car/?pageIndex=0&pageSize=10")
      .then()
      .statusCode(HttpResponseStatus.OK.code())
      .contentType("application/json")
      .extract().response();
    assertThat(response.jsonPath().getList("name"), Matchers.hasItems("Uno Mille", "x1"));
  }

  @Test
  @Order(14)
  void testListAllCarsPagedWrongDefaultingToListAll() {
    //List all, with wrong page, so it will default to List all elements without paging
    Response response = given()
      .when()
      .get("/car/?pageIndex=5")
      .then()
      .statusCode(HttpResponseStatus.OK.code())
      .contentType("application/json")
      .extract().response();
    assertThat(response.jsonPath().getList("name"), Matchers.hasItems("Uno Mille", "x1"));
  }
*/
}
