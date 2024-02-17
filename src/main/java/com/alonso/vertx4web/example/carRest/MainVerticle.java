package com.alonso.vertx4web.example.carRest;

import com.alonso.vertx4web.example.carRest.model.dto.CarRecord;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

public class MainVerticle extends AbstractVerticle {

  List<CarRecord> cars = new ArrayList<CarRecord>();

  @Override
  public void start(Promise<Void> onReady) {
    Router router = Router.router(vertx);

    router.get("/car/").handler(this::getAll); // Query Params: Integer pageIndex, Integer pageSize
    router.get("/car/:id").handler(this::getById);
    router.get("/car/name/:name").handler(this::getByName);
    router.get("/car/brand/:name").handler(this::getByBrandName);
    router.get("/car/price-range/").handler(this::getByPriceRange); // Query Params: Double startPrice, Double finalPrice
    router.post("/car").handler(this::postCar);
    router.put("/car/:id").handler(this::putCar);
    router.delete("/car/:id").handler(this::deleteById);

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8700)
      .onFailure(onReady::fail)
      .onSuccess(server -> onReady.complete());
  }

  // Query Params: Integer pageIndex, Integer pageSize
  private void getAll(RoutingContext routingContext) {
    MultiMap queryParams = routingContext.request().params();
    String pageIndexString = queryParams.get("pageIndex");
    String pageSizeString = queryParams.get("pageSize");
    Integer pageIndex = Integer.valueOf(pageIndexString);
    Integer pageSize = Integer.valueOf(pageSizeString);

    System.out.println("pageIndex: " + pageIndex + " pageSize: " + pageSize);

    routingContext.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(200)
      .end(Json.encodePrettily(pageIndex) + Json.encodePrettily(pageSize));
  }
  private void getById(RoutingContext routingContext) {
    routingContext.end(routingContext.pathParam("id"));
  }

  private void getByName(RoutingContext routingContext) {
    String carName = routingContext.pathParam("name");

    if(carName != null) {
      routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(Json.encodePrettily("Hello World"));
    } else {
      routingContext.response()
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end();
    }
  }

  private void getByBrandName(RoutingContext routingContext) {
    String brandName = routingContext.pathParam("name");

    if(brandName != null) {
      routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(Json.encodePrettily("Hello World"));
    } else {
      routingContext.response()
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end();
    }
  }

  // Query Params: Double startPrice, Double finalPrice
  private void getByPriceRange(RoutingContext routingContext) {

    MultiMap queryParams = routingContext.request().params();
    String startPriceString = queryParams.get("startPrice");
    String finalPriceString = queryParams.get("finalPrice");
    Double startPrice = Double.valueOf(startPriceString);
    Double finalPrice = Double.valueOf(finalPriceString);

    System.out.println("startPrice: " + startPrice + " finalPrice: " + finalPrice);

    //TODO return List of CARs within that Range or Empty List

    routingContext.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(200)
      .end("startPrice: " + startPrice + " finalPrice: " + finalPrice);
  }

  private void postCar(RoutingContext routingContext) {
    JsonObject requestJson = routingContext.getBodyAsJson();
    CarRecord requestCar = requestJson.mapTo(CarRecord.class);

    //TODO connect to DB save CAR
    routingContext.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(HttpResponseStatus.CREATED.code())
      .end(Json.encodePrettily(requestCar));
  }

  private void putCar(RoutingContext routingContext) {
    String carId = routingContext.pathParam("id");
    JsonObject requestJson = routingContext.getBodyAsJson();
    CarRecord requestCar = requestJson.mapTo(CarRecord.class);

    if(carId  != null) {
      //TODO connect to DB find car by id if exists UPDATE IT if not 404
      routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HttpResponseStatus.ACCEPTED.code())
        .end(Json.encodePrettily(requestCar));
    } else {
      routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end(carId + " not found in the System");
    }
  }

  private void deleteById(RoutingContext routingContext) {
    String carId = routingContext.pathParam("id");

    if(carId  != null) {
      //TODO connect to DB find car by id if exists delete if not 404
      routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
        .end(carId + " deleted sucessfully");
    } else {
      routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end(carId + " not found in the System");
    }
  }

}
