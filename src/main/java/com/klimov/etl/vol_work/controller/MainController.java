package com.klimov.etl.vol_work.controller;

import com.klimov.etl.vol_work.dto.exceptions.UnauthorizedException;
import com.klimov.etl.vol_work.entity.*;
import com.klimov.etl.vol_work.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@SessionAttributes(names = {"addingUserTask", "screenState", "listRunType"})
@Controller
public class MainController {

    MainService mainService;

    public MainController(@Autowired MainService mainService) {
        this.mainService = mainService;
    }

    @ModelAttribute("addingUserTask")
    public UserTaskFromUI getDefaultUserTask() {
        return new UserTaskFromUI();
    }

    @ModelAttribute("screenState")
    public MainScreenState getDefaultScreenState() {
        return new MainScreenState();
    }

    @ModelAttribute("listRunType")
    public List<RunType> getRunTypeList() {
        return Arrays.stream(RunType.values()).toList();
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

        return "redirect:/control_panel";
    }

    @GetMapping("control_panel")
    public String controlPanel(@ModelAttribute("addingUserTask") UserTaskFromUI addingUserTask,
                               @ModelAttribute("screenState") MainScreenState screenState) {
        screenState = mainService.getUserState();
        return "main-screen";
    }

    @RequestMapping("/addFlow")
    public String addFlow(@ModelAttribute("addingUserTask") UserTaskFromUI addingUserTask) {

        try {
            mainService.addTask(addingUserTask);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }


        return "redirect:/control_panel";
    }

    @RequestMapping("/setPause")
    public String setPause(@ModelAttribute("screenState") MainScreenState screenState) {
        System.out.println("DONE setPause");
        screenState.setPause(true);
        return "redirect:/control_panel";
    }

    @RequestMapping("/setUnpause")
    public String setUnpause(@ModelAttribute("screenState") MainScreenState screenState) {
        System.out.println("DONE setUnpause");
        screenState.setPause(false);
        return "redirect:/control_panel";
    }
}
