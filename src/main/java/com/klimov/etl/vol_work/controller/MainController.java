package com.klimov.etl.vol_work.controller;

import com.klimov.etl.vol_work.dto.exceptions.UnauthorizedException;
import com.klimov.etl.vol_work.entity.*;
import com.klimov.etl.vol_work.service.MainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static com.klimov.etl.vol_work.controller.util.Constants.*;

@SessionAttributes(names = {USER_TASK_ATTRIBUTE, SCREEN_STATE_ATTRIBUTE, LIST_RUN_TYPE_ATTRIBUTE})
@Controller
public class MainController {

    MainService mainService;

    public MainController(@Autowired MainService mainService) {
        this.mainService = mainService;
    }

    @ModelAttribute(USER_TASK_ATTRIBUTE)
    public UserTaskFromUI getDefaultUserTask() {
        return new UserTaskFromUI();
    }

    @ModelAttribute(SCREEN_STATE_ATTRIBUTE)
    public MainScreenStateUI getDefaultScreenState() {
        return new MainScreenStateUI();
    }

    @ModelAttribute(LIST_RUN_TYPE_ATTRIBUTE)
    public List<RunType> getRunTypeList() {
        return Arrays.stream(RunType.values()).toList();
    }

    @GetMapping(BASE_URL)
    public String loginStart(Model model) {

        model.addAttribute(CREDENTIALS_ATTRIBUTE, new CredentialsInfo());

        return "login-screen";
    }

    @RequestMapping(WAITING_SCREEN_URL)
    public String waitingScreen(@ModelAttribute(SCREEN_STATE_ATTRIBUTE) MainScreenStateUI screenState) {

        return "waiting-screen";
    }

    @RequestMapping(CHECK_ACCESS_URL)
    public String checkAccess(@ModelAttribute(CREDENTIALS_ATTRIBUTE) CredentialsInfo credentialsInfo,
                              @ModelAttribute(SCREEN_STATE_ATTRIBUTE) MainScreenStateUI screenState) {

        try {
            mainService.signIn(credentialsInfo.getLogin(), credentialsInfo.getPassword());
        } catch (UnauthorizedException e) {
            credentialsInfo.setAuthError(true);
            return "login-screen";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "redirect:/" + HOME_SCREEN_URL;
    }

    @GetMapping(HOME_SCREEN_URL)
    public String controlPanel(@ModelAttribute(SCREEN_STATE_ATTRIBUTE) MainScreenStateUI screenState) {

        updateScreenState(screenState);

        if (!screenState.isInitDone()) {
            return "redirect:/" + WAITING_SCREEN_URL;
        }

        return "main-screen";
    }

    @PostMapping(ADD_USER_TASK_URL)
    public String addFlow(@Valid @ModelAttribute(USER_TASK_ATTRIBUTE) UserTaskFromUI addingUserTask,
                          BindingResult bindingResult,
                          @ModelAttribute(SCREEN_STATE_ATTRIBUTE) MainScreenStateUI screenStateUI) {

        if (bindingResult.hasErrors()) {
            return "main-screen";
        }

        try {
            mainService.addTask(addingUserTask);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        addingUserTask.clear();

        return "redirect:/" + HOME_SCREEN_URL;
    }

    @PostMapping(SET_PAUSE_URL)
    public String setPause(@ModelAttribute(SCREEN_STATE_ATTRIBUTE) MainScreenStateUI screenState) {
        mainService.pauseStarts();
        return "redirect:/" + HOME_SCREEN_URL;
    }

    @PostMapping(SET_UNPAUSE_URL)
    public String setUnpause(@ModelAttribute(SCREEN_STATE_ATTRIBUTE) MainScreenStateUI screenState) {
        mainService.unpauseStarts();
        return "redirect:/" + HOME_SCREEN_URL;
    }

    @GetMapping("/deleteTask")
    public String deleteEmployee(@RequestParam("taskId") String taskId) {

        try {
            mainService.deleteTask(taskId);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/" + HOME_SCREEN_URL;
    }

    private void updateScreenState(MainScreenStateUI screenState) {

        MainScreenStateService newState = mainService.getUserState();

        screenState.setInitDone(newState.isInitDone());
        screenState.setDagObserveList(newState.getDagObserveList());
        screenState.setDagRunList(newState.getDagRunList());
        screenState.setPause(newState.isPause());
    }
}
