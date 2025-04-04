package com.klimov.etl.vol_work.dao;

import com.klimov.etl.vol_work.dao.entity.UserTaskDBModel;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
    public List<UserTaskDBModel> getAllUsersTasks() {

        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from UserTaskDBModel ", UserTaskDBModel.class).getResultList();

    }

    @Override
    public void saveUserTasks(List<UserTaskDBModel> userTasks) {

        for (UserTaskDBModel userTaskDBModel : userTasks) {
            entityManager.merge(userTaskDBModel);
        }

    }

    @Override
    public void deleteUserTask(String userId, String dagId) {

        Session session = entityManager.unwrap(Session.class);

        Query<UserTaskDBModel> query = session.createQuery("delete from UserTaskDBModel " +
                "where userTaskPK.userId =:userId " +
                "and userTaskPK.dagId = :dagId");
        query.setParameter("userId", userId);
        query.setParameter("dagId", dagId);
        query.executeUpdate();

    }

    @Override
    public List<UserTaskDBModel> getUserTasks(String userId) {

        Session session = entityManager.unwrap(Session.class);

        return session.createQuery("from UserTaskDBModel where id.userId = :user_id", UserTaskDBModel.class)
                .setParameter("user_id", (String) userId)
                .getResultList();
    }


}
