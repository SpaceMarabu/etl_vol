package com.klimov.etl.vol_work.controller;

import com.klimov.etl.vol_work.dto.exceptions.UnauthorizedException;
import com.klimov.etl.vol_work.entity.CredentialsInfo;
import com.klimov.etl.vol_work.entity.MainScreenState;
import com.klimov.etl.vol_work.entity.UserTask;
import com.klimov.etl.vol_work.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes(names = {"addingUserTask", "screenState"})
@Controller
public class MainController {

    MainService mainService;

    public MainController(@Autowired MainService mainService) {
        this.mainService = mainService;
    }

    @ModelAttribute("addingUserTask")
    public UserTask getDefaultUserTask() {
        return new UserTask();
    }

    @ModelAttribute("screenState")
    public MainScreenState getDefaultScreenState() {
        return new MainScreenState();
    }

    @GetMapping("/")
    public String loginStart(Model model) {

        model.addAttribute("credentials", new CredentialsInfo());

        return "login-screen";
    }

    @RequestMapping("/checkAccess")
    public String checkAccess(Model model, @ModelAttribute("credentials") CredentialsInfo credentialsInfo) {

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
    public String controlPanel(@ModelAttribute("addingUserTask") UserTask addingUserTask,
                               @ModelAttribute("screenState") MainScreenState screenState) {
        System.out.println("Is pause: " + screenState.isPause());
        return "main-screen";
    }

    @RequestMapping("/addFlow")
    public String addFlow(@ModelAttribute("addingUserTask") UserTask addingUserTask) {
        System.out.println("DONE");
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
