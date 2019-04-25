package com.booker.controllers;

import java.util.Base64;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booker.controllers.annotations.RestEndpoint;
import com.booker.services.users.UsersService;

@RestController
@CrossOrigin
@RestEndpoint
public class LoginController {

    @Autowired
    UsersService userService;

    @Autowired
    HttpSession session;

    @Autowired
    @Qualifier("passwordEncoder")
    private BCryptPasswordEncoder encoder;

    @PostMapping(value = "/login")
    public String login(@RequestParam("username") String userName,
	    @RequestParam("password") String password) {
	try {
	    userService.valiateUser(userName, password);
	    String basicAuthHeader = userName + ":" + password;
	    basicAuthHeader = "Basic " + Base64.getEncoder().encodeToString(basicAuthHeader.getBytes());
	    return basicAuthHeader;
	} catch (Exception e) {
	    return "";
	}
    }
}
