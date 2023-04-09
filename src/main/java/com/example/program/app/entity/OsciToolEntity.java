package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "osci_tool")
public class OsciToolEntity extends BaseEntity {

    private String name;

    private String model;

    private String characteristics;

    private String info;

    @Column(name = "image_file_id", nullable = true)
    private Long imageFileId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_file_id", insertable = false, updatable = false)
    private OsciLanguageEntity osciLanguage;
}
