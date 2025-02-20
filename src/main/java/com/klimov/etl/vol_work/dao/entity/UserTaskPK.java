package com.klimov.etl.vol_work.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserTaskPK implements Serializable {

    @Column(name = "user_id")
    private String userId;
    @Column(name = "dag_id")
    private String dagId;

    public UserTaskPK() {
    }

    public UserTaskPK(String userId, String dagId) {
        this.userId = userId;
        this.dagId = dagId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDagId() {
        return dagId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserTaskPK)) return false;
        UserTaskPK that = (UserTaskPK) obj;

        return Objects.equals(this.userId, that.userId)
                && Objects.equals(this.dagId, that.dagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, dagId);
    }
}
