package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "osci_file")
public class OsciFileEntity extends BaseEntity {

    public enum FileType{
        OSCI_DATA,
        IMG,
    }

    @Column(name = "file_name")
    private String filename;

    @Column(name = "original_name")
    private String originalName;

    @Enumerated(EnumType.STRING)
    private FileType fileType;
}
