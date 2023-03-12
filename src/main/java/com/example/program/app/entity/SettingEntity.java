package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "setting")
public class SettingEntity extends BaseEntity {
    private String appName;

    private String version;

    private String techSupport;

    private String authorName;

    private String authorContact;
}
