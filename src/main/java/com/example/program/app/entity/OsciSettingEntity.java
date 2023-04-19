package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import com.ziclix.python.sql.Fetch;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @Column(name = "default_language_id", nullable = true)
    private Long languageId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_language_id", insertable = false, updatable = false)
    private OsciLanguageEntity osciLanguage;
}
