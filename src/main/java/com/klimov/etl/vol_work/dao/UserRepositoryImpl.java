package com.klimov.etl.vol_work.dao;

import com.klimov.etl.vol_work.dao.entity.UserDBModel;
import com.klimov.etl.vol_work.dao.mapper.DBMapper;
import com.klimov.etl.vol_work.entity.User;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager entityManager;
    private final DBMapper mapper;

    public UserRepositoryImpl(@Autowired EntityManager entityManager, @Autowired DBMapper mapper) {
        this.entityManager = entityManager;
        this.mapper = mapper;
    }

    @Override
    public List<User> getAllUsers() {

        Session session = entityManager.unwrap(Session.class);
        List<UserDBModel> userModelList = session.createQuery("from UserDBModel ", UserDBModel.class).getResultList();

        List<User> userList = new ArrayList<>();
        for (UserDBModel model: userModelList) {
            userList.add(mapper.getUserFromDBModel(model));
        }

        return userList;
    }

    @Override
    public void saveUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public User getUser(String userId) {
        return entityManager.find(User.class, userId);
    }
}
