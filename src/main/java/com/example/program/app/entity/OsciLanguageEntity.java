package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "osci_language")
public class OsciLanguageEntity extends BaseEntity {

    private String name;

    private String code;

    @Column(name = "flag_file_id", nullable = true)
    private Long flagFileId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_file_id", insertable = false, updatable = false)
    private OsciFileEntity osciFile;
}
