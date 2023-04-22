package com.example.program.app.property;

import com.example.program.app.entity.base.BaseEntity;
import com.example.program.common.status.EntityStatus;
import com.sun.istack.NotNull;
import javafx.beans.property.*;

import java.util.Date;

public class BaseProperty {

    protected <E extends BaseEntity> void populateBase(E entity){
        this.setId(entity.getId());
        this.setStatus(entity.getStatus());
        this.setCreatedDate(entity.getCreated());
        this.setUpdatedDate(entity.getUpdated());
    }

    protected <E extends BaseEntity> E baseEntity(@NotNull E entity){
        entity.setId(getId());
        entity.setCreated(getCreatedDate());
        entity.setStatus(getStatus());
        entity.setUpdated(getUpdatedDate());
        return entity;
    }

    private LongProperty id = new SimpleLongProperty();

    private ObjectProperty<Date> createdDate = new SimpleObjectProperty<>();

    private ObjectProperty<Date> updatedDate = new SimpleObjectProperty<>();

    private ObjectProperty<EntityStatus> status = new SimpleObjectProperty<>(EntityStatus.ACTIVE);

    public Long getId() {
        return id.get() == 0 ? null : id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(Long id) {
        this.id.set(id == null ? 0 : id);
    }

    public Date getCreatedDate() {
        return createdDate.get();
    }

    public ObjectProperty<Date> createdDateProperty() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate.set(createdDate);
    }

    public Date getUpdatedDate() {
        return updatedDate.get();
    }

    public ObjectProperty<Date> updatedDateProperty() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate.set(updatedDate);
    }

    public EntityStatus getStatus() {
        return status.get();
    }

    public ObjectProperty<EntityStatus> statusProperty() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status.set(status);
    }
}
