package com.example.demo.controllers;

/**
 * Created by albertchubakov on 05.03.2018.
 */
import com.auth0.SessionUtils;
import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.UserInfo;
import com.auth0.net.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings("unused")
@Controller
public class HomeController {



    @Value(value = "${com.auth0.domain}")
    private String domain;
    @Value(value = "${com.auth0.clientId}")
   private  String clientId;
    @Value(value = "${com.auth0.clientSecret}")
    private String clientSecret;


    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    protected String chat(final Map<String, Object> model, final HttpServletRequest req) {
         AuthAPI auth = new AuthAPI(domain,clientId,clientSecret);
        String accessToken = (String) SessionUtils.get(req, "accessToken");
        String idToken = (String) SessionUtils.get(req, "idToken");
        if (accessToken != null) {
            try {
                Request<UserInfo> request = auth.userInfo(accessToken);
                UserInfo info = request.execute();
                String loginwith = (String) info.getValues().get("sub");
                if(loginwith.contains("vkontakte")){
                    model.put("username", info.getValues().get("given_name"));
                } else
                    model.put("username", info.getValues().get("name"));

            } catch (Auth0Exception e) {
                e.printStackTrace();
            }

        } else if (idToken != null) {
           model.put("userId", idToken);
        }
        return "chat";
    }









}
