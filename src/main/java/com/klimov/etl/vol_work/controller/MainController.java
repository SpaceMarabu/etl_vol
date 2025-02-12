package com.klimov.etl.vol_work.controller;

import com.klimov.etl.vol_work.dto.exceptions.UnauthorizedException;
import com.klimov.etl.vol_work.entity.CredentialsInfo;
import com.klimov.etl.vol_work.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    MainService mainService;

    public MainController(@Autowired MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/")
    public String loginStart(Model model) {

        model.addAttribute("credentials", new CredentialsInfo());

        return "login-screen";
    }

    @RequestMapping("/checkAccess")
    public String checkAccess(@ModelAttribute("credentials") CredentialsInfo credentialsInfo) {

        try {
            mainService.signIn(credentialsInfo.getLogin(), credentialsInfo.getPassword());
        } catch (UnauthorizedException e) {
            credentialsInfo.setAuthError(true);
            return "login-screen";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "main-screen";

    }
}
