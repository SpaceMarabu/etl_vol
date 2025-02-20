package com.klimov.etl.vol_work.dao;

import com.klimov.etl.vol_work.dao.entity.UserDBModel;
import com.klimov.etl.vol_work.dao.entity.UserTaskDBModel;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager entityManager;

    public UserRepositoryImpl(@Autowired EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<UserDBModel> getAllUsers() {

        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from UserDBModel ", UserDBModel.class).getResultList();

    }

    @Override
    public void saveUser(UserDBModel user) {

//        UserDBModel userFromDb = getUser(user.getUserId());
//        userFromDb.setListTasks(user.getListTasks());

//        for (UserTaskDBModel userTaskDBModel : userFromDb.getListTasks()) {
//            entityManager.find(UserTaskDBModel.class, )
//        }
    }

    @Override
    public UserDBModel getUser(String userId) {
        return entityManager.find(UserDBModel.class, userId);
    }
}
