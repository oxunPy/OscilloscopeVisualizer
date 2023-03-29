package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "osci_user")
public class OsciUserEntity extends BaseEntity {

    public enum UserType{
        ADMIN,
        SECRET,
        SIMPLE,
    }



    private String firstName;

    private String lastName;

    private String middleName;

    private String printableName;

    private String login;

    private String pass;

    private String info;

    private Boolean authSet;

    @Enumerated(EnumType.STRING)
    private UserType userType;
}
