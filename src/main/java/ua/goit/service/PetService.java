package ua.goit.service;

import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import ua.goit.entity.ApiResponse;
import ua.goit.entity.Pet;
import ua.goit.entity.PetStatus;


import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class PetService extends HttpService<Pet> {
    private static final String CREATE_PET = "/pet";
    private static final String UPLOAD_IMAGE = "/uploadImage";
    private static final String UPDATE_PET = "/pet/";
    private static final String READ_PET = "/pet/";
    private static final String READ_PET_BY_STATUS = "/pet/findByStatus?status=";
    private static final String DELETE_PET = "/pet/";

    public static Pet createPet(Pet pet) throws IOException, InterruptedException {
        HttpRequest request = HttpService.requestWithBody("POST", String.format("%s%s", HOST, CREATE_PET), pet);
        HttpResponse<String> response = HttpService.getResponse(request);
        return GSON.fromJson(response.body(), Pet.class);
    }

    public static ApiResponse uploadImage(int id, String metaData, File image) throws IOException, InterruptedException {
        FileBody fileBody = new FileBody(image, ContentType.DEFAULT_BINARY);
        StringBody stringBody = new StringBody(metaData, ContentType.MULTIPART_FORM_DATA);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addPart("additionalMetadata", stringBody)
                .addPart("file", fileBody);
        HttpEntity build = builder.build();
        return sendMultipartEntity(String.format("%s%s%d%s", HOST, UPDATE_PET,
                id, UPLOAD_IMAGE), build);
    }

    public static ApiResponse updatePet(int id, Pet newPet) throws IOException, InterruptedException {
        String url = String.format("%s%s%d", HOST, UPDATE_PET, id);
        List<NameValuePair> form = new ArrayList<>();
        form.add(new BasicNameValuePair("name", newPet.getName()));
        form.add(new BasicNameValuePair("status", newPet.getStatus().name()));
        return sendPostEncoded(url, form);
    }

    public static Pet getPetById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpService.sendGet(String.format("%s%s%s", HOST, READ_PET, id));
        HttpResponse<String> response = HttpService.getResponse(request);
        return GSON.fromJson(response.body(), Pet.class);
    }

    public static List<Pet> getPetByStatus(PetStatus status) throws IOException, InterruptedException {
        HttpRequest request = HttpService.sendGet(String.format("%s%s%s", HOST, READ_PET_BY_STATUS,
                status.toString().toLowerCase()));
        HttpResponse<String> response = HttpService.getResponse(request);
        return GSON.fromJson(response.body(), new TypeToken<List<Pet>>() {
        }.getType());
    }

    public static ApiResponse delete(int id) throws IOException, InterruptedException {
        HttpRequest request = sendDelete(String.format("%s%s%d", HOST, DELETE_PET, id));
        HttpResponse<String> response = HttpService.getResponse(request);
        return GSON.fromJson(response.body(), ApiResponse.class);
    }
}
