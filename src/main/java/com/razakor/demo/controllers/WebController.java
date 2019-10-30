package com.razakor.demo.controllers;

import com.razakor.demo.documents.User;
import com.razakor.demo.services.MyUserDetailsService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class WebController {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final MyUserDetailsService myUserDetailsService;

    public WebController(OAuth2AuthorizedClientService authorizedClientService, MyUserDetailsService myUserDetailsService) {
        this.authorizedClientService = authorizedClientService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(OAuth2AuthenticationToken authentication, HttpServletRequest request) {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());

        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();

        System.out.println(client.getClientRegistration().getClientName());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                .getTokenValue());
        HttpEntity entity = new HttpEntity("", headers);
        ResponseEntity<Map> response = restTemplate
                .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
        Map userAttributes = response.getBody();

        if (client.getClientRegistration().getClientName().equals("Google")) {

            if(myUserDetailsService.isAlreadyExists(userAttributes.get("sub").toString())) {
                return "redirect:/user";
            }

            googleAuthorization(userAttributes, request);
            return "redirect:/registration";
        } else if (client.getClientRegistration().getClientName().equals("Facebook")) {

            if(myUserDetailsService.isAlreadyExists(userAttributes.get("id").toString())) {
                return "redirect:/user";
            }

            facebookAuthorization(userAttributes, request);
            return "redirect:/registration";
        }

        return "main";
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "<h1>Login failed</h1>";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "Friend");
        return "main";
    }

    @GetMapping("/user")
    public String user(Model model) {
        model.addAttribute("name", "user");
        return "main";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("name", "Admin");
        return "main";
    }

    @GetMapping("/registration")
    public String registration(Model model, HttpServletRequest request) {

        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            model.addAttribute("userEmail", user.getEmail());
            model.addAttribute("userName", user.getName());
            model.addAttribute("userSurname", user.getSurname());
        }

        return "registration";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("user") User user, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") != null) {
            user.setSub(((User) request.getSession().getAttribute("user")).getSub());
        }
        user.AddRole("ROLE_USER");
        myUserDetailsService.addUser(user);
        return "redirect:/user";
    }

    private void googleAuthorization(Map userAttributes, HttpServletRequest request) {

        String sub = userAttributes.get("sub").toString();
        String email = userAttributes.get("email").toString();
        String name = userAttributes.get("given_name").toString();
        String surname = userAttributes.get("family_name").toString();

        request.getSession().setAttribute("user", new User(sub, null, null, email, name, surname));
    }

    private void facebookAuthorization(Map userAttributes, HttpServletRequest request) {
        String sub = userAttributes.get("id").toString();
        String email = userAttributes.get("email").toString();
        String fullName = userAttributes.get("name").toString();

        String[] name = fullName.split(" ");

        request.getSession().setAttribute("user", new User(sub, null, null, email, name[0], name[1]));
    }
}