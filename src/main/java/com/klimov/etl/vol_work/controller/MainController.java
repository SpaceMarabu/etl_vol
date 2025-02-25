package com.klimov.etl.vol_work.controller;

import com.klimov.etl.vol_work.dto.exceptions.UnauthorizedException;
import com.klimov.etl.vol_work.entity.*;
import com.klimov.etl.vol_work.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public MainScreenStateUI getDefaultScreenState() {
        return new MainScreenStateUI();
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

    @RequestMapping("waiting_screen")
    public String waitingScreen(@ModelAttribute("screenState") MainScreenStateUI screenState) {

        return "waiting-screen";

    }

    @RequestMapping("/checkAccess")
    public String checkAccess(@ModelAttribute("credentials") CredentialsInfo credentialsInfo,
                              @ModelAttribute("screenState") MainScreenStateUI screenState) {

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
    public String controlPanel(@ModelAttribute("screenState") MainScreenStateUI screenState) {

        updateScreenState(screenState);
        if (!screenState.isInitDone()) {
            return "redirect:/waiting_screen";
        }

        return "main-screen";
    }

    @PostMapping("/addFlow")
    public String addFlow(@ModelAttribute("addingUserTask") UserTaskFromUI addingUserTask) {

        try {
            mainService.addTask(addingUserTask);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/control_panel";
    }

    @PostMapping("/setPause")
    public String setPause(@ModelAttribute("screenState") MainScreenStateUI screenState) {
        mainService.pauseStarts();
        return "redirect:/control_panel";
    }

    @PostMapping("/setUnpause")
    public String setUnpause(@ModelAttribute("screenState") MainScreenStateUI screenState) {
        mainService.unpauseStarts();
        return "redirect:/control_panel";
    }

    private void updateScreenState(MainScreenStateUI screenState) {

        MainScreenStateService newState = mainService.getUserState();

        screenState.setInitDone(newState.isInitDone());
        screenState.setDagObserveList(newState.getDagObserveList());
        screenState.setDagRunList(newState.getDagRunList());
        screenState.setPause(newState.isPause());
    }
}
