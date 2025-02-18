package com.klimov.etl.vol_work.dao.mapper;

import com.klimov.etl.vol_work.dao.entity.UserDBModel;
import com.klimov.etl.vol_work.dao.entity.UserTaskDBModel;
import com.klimov.etl.vol_work.entity.RunType;
import com.klimov.etl.vol_work.entity.User;
import com.klimov.etl.vol_work.entity.UserTask;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DBMapper {

    public UserTask getUserTaskFromDBModel(UserTaskDBModel model) {

        List<String> listConf = Arrays.stream(model.getListConf().split(Util.LIST_SEPARATOR)).toList();
        RunType runType = RunType.valueOf(model.getRunType());
        UserTask userTask = new UserTask(
                model.getUserId(),
                model.getDagId(),
                runType,
                model.getTaskId(),
                listConf
        );

        userTask.setLastRunId(model.getLastRunId());

        return userTask;
    }

    public UserTaskDBModel getUserTaskDBModelFromEntity(UserTask entity) {

        String listConf = String.join(Util.LIST_SEPARATOR, entity.getListConf());
        String runType = entity.getRunType().name();

        return new UserTaskDBModel(
                entity.getUserId(),
                entity.getDagId(),
                runType,
                entity.getTaskId(),
                listConf,
                entity.getLastRunId()
        );
    }

    public User getUserFromDBModel(UserDBModel model) {

        List<UserTask> listUserTask = new ArrayList<>();
        for (UserTaskDBModel userTaskDBModel : model.getListTasks()) {
            listUserTask.add(getUserTaskFromDBModel(userTaskDBModel));
        }

        return new User(
                model.getUserId(),
                listUserTask
        );
    }

    public UserDBModel getUserDBModelFromEntity(User entity) {

        List<UserTaskDBModel> listUserTaskDBModel = new ArrayList<>();
        for (UserTask userTask : entity.getListTasks()) {
            listUserTaskDBModel.add(getUserTaskDBModelFromEntity(userTask));
        }

        return new UserDBModel(
                entity.getUserId(),
                listUserTaskDBModel
        );
    }
}
