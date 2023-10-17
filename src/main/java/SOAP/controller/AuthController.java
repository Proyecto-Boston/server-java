package SOAP.controller;

import SOAP.model.Response;
import SOAP.model.URLS;
import SOAP.model.User;
import org.json.JSONObject;

import java.net.http.HttpResponse;

public class AuthController {
    public static Response login(String email, String password){
        Response response = new Response();
        response.statusCode = 503;
        response.details = "El servicio actualmente no se encuentra disponible";

        String url = URLS.getAuthServerUrl() + "/login";
        String body = "{\"email\": \"" + email + "\", \"password\": \""+ password  + "\" }";

        try{
            HttpResponse<String> res = Request.post(url, body);
            if(res == null) return response;

            JSONObject resJSON = new JSONObject(res.body());

            response.statusCode = res.statusCode();
            if(response.statusCode == 202){
                response.json = "{\"token\": \""+ resJSON.getString("token_jwt") +"\"}";
                response.details = "Ingreso exitoso";
            }else if(response.statusCode == 400){
                response.details = resJSON.getString("message");
            }

            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static Response register(User user){
        Response response = new Response();
        response.statusCode = 503;
        response.details = "El servicio actualmente no se encuentra disponible";

        String url = URLS.getAuthServerUrl() + "/register";
        System.out.println(url);
        String body = "{\"email\": \"" + user.email + "\", \"password\": \""+ user.password  + "\" }";

        try{
            HttpResponse<String> res = Request.post(url, body);
            if(res == null) return response;

            JSONObject resJSON = new JSONObject(res.body());

            response.statusCode = res.statusCode();
            if(response.statusCode == 201){
                response.details = "Usuario registrado exitosamente";
            }else if(response.statusCode == 400){
                response.details = resJSON.getString("message");
            }
            return response;
        }   catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static Response verifySession(String token){
        Response response = new Response();
        response.statusCode = 503;
        response.details = "El servicio actualmente no se encuentra disponible";

        String url = URLS.getAuthServerUrl() + "/auth";
        String body = "{\"token_jwt\": \"" + token + "\"}";

        try{
            HttpResponse<String> res = Request.post(url, body);
            if(res == null) return response;

            JSONObject resJSON = new JSONObject(res.body());

            response.statusCode = res.statusCode();
            if(response.statusCode == 202){
                response.json = "{\"user_id\": " + resJSON.getInt("id_user")+ "}";
                response.details = "Token válido.";
            }else if(response.statusCode == 400){
                response.details = resJSON.getString("message");
            }
            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

}
