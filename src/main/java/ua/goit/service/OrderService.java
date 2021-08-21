package ua.goit.service;

import com.google.gson.reflect.TypeToken;
import ua.goit.entity.ApiResponse;
import ua.goit.entity.Order;


import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class OrderService extends HttpService<Order> {
    private static final String CREATE_ORDER = "/store/order";
    private static final String READ_ORDER = "/store/order/";
    private static final String READ_ORDER_BY_STATUS = "/store/inventory";
    private static final String DELETE_ORDER = "/store/order/";

    public static Order createOrder(Order order) throws IOException, InterruptedException {
        HttpRequest request = HttpService.requestWithBody("POST",
                String.format("%s%s", HOST, CREATE_ORDER), order);
        HttpResponse<String> response = HttpService.getResponse(request);
        return GSON.fromJson(response.body(), Order.class);
    }

    public static Order getOrderById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpService.sendGet(String.format("%s%s%d", HOST, READ_ORDER, id));
        HttpResponse<String> response = HttpService.getResponse(request);
        return GSON.fromJson(response.body(), Order.class);
    }

    public static ApiResponse delete(int id) throws IOException, InterruptedException {
        HttpRequest request = sendDelete(String.format("%s%s%d", HOST, DELETE_ORDER, id));
        HttpResponse<String> response = HttpService.getResponse(request);
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    public static HashMap<String, Integer> getInventories() throws IOException, InterruptedException {
        HttpRequest request = HttpService.sendGet(String.format("%s%s", HOST, READ_ORDER_BY_STATUS));
        HttpResponse<String> response = HttpService.getResponse(request);
        return GSON.fromJson(response.body(), new TypeToken<HashMap<String, Integer>>() {
        }.getType());
    }

}
