package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "osci_file", uniqueConstraints = {@UniqueConstraint(name = "id", columnNames = {"id", "fileName"})})
public class OsciFileEntity extends BaseEntity {

    public enum FileType{
        OSCI_DATA,
        FLAG,
        IMG,
    }

    @Column(name = "file_name", unique = true)
    private String filename;

    private String originalName;

    private String dataSize;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private FileType fileType;
}
