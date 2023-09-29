package io.turntabl.project.exchangeclient;

import io.turntabl.project.exchangeclient.dtos.responsebodies.MarketData;
import io.turntabl.project.exchangeclient.dtos.requestbodies.CreateOrderRequestBody;
import io.turntabl.project.exchangeclient.dtos.requestbodies.UpdateOrderRequestBody;
import io.turntabl.project.exchangeclient.dtos.responsebodies.FindOrderResponseBody;
import io.turntabl.project.exchangeclient.dtos.responsebodies.OrderBookResponseBody;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

public interface ExchangeProxyInterface {

    @POST
    @Path("{api-key}/order")
    @Consumes(MediaType.APPLICATION_JSON)
    String createOrder(@PathParam("api-key") String apiKey,
                       CreateOrderRequestBody body);

    @GET
    @Path("{api-key}/order/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    FindOrderResponseBody getOrderById(@PathParam("api-key") String apiKey,
                                       @PathParam("id") String id);

    @DELETE
    @Path("{api-key}/order/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    boolean cancelOrderById(@PathParam("api-key") String apiKey,
                            @PathParam("id") String id);

    @PUT
    @Path("{api-key}/order/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    boolean updateOrderById(@PathParam("api-key") String apiKey,
                            @PathParam("id") String id,
                            UpdateOrderRequestBody body);

    @GET
    @Path("/pd")
    @Produces(MediaType.APPLICATION_JSON)
    List<MarketData> getMarketData();

    @GET
    @Path("/orderbook/{product}/open")
    @Produces(MediaType.APPLICATION_JSON)
    List<OrderBookResponseBody> getProductOpenOrders(@PathParam("product") String product);
}
