package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "osci_setting")
public class OsciSettingEntity extends BaseEntity {
    private String appName;

    private String version;

    private String techSupport;

    private String authorName;

    private String authorContact;
}
