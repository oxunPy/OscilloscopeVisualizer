package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "osci_file", uniqueConstraints = {@UniqueConstraint(name = "ID_FILENAME", columnNames = {"id", "file_name"}),
                                                @UniqueConstraint(name = "ID_FLAGCODE", columnNames = {"id", "flag_code"})})
public class OsciFileEntity extends BaseEntity {

    public enum FileType{
        OSCI_DATA,
        FLAG,
        IMG,
    }

    @Column(name = "file_name")
    private String filename;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "flag_code")
    private String flagCode;

    @Column(name = "data_size")
    private Double dataSize;

    @Column(name = "file_path")
    private String filePath;

    @Enumerated(EnumType.STRING)
    private FileType fileType;
}
