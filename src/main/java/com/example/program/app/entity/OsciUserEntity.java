package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "last_session")
    private Date lastSessionDate;


    @Column(name = "osci_lang_id", nullable = true)
    private Long languageId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "osci_lang_id", insertable = false, updatable = false)
    private OsciLanguageEntity osciLanguage;


    private String info;

    private Boolean authSet;

    @Enumerated(EnumType.STRING)
    private UserType userType;


    @Column(name = "img_file_id", nullable = true)
    private Long imageId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "img_file_id", insertable = false, updatable = false)
    private OsciFileEntity osciFile;
}
