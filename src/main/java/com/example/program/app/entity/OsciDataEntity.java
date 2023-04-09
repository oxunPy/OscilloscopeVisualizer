package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "osci_data")
public class OsciDataEntity extends BaseEntity {

    @Column(name = "osci_tool_id", nullable = true)
    private Long osciToolId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "osci_tool_id", insertable = false, updatable = false)
    private OsciToolEntity osciTool;


    @Column(name = "file_id")
    private Long osciFileId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = true, insertable = false, updatable = false)
    private OsciFileEntity osciFile;

    @Column(name = "date", nullable = true)
    private Date date;

    @Column(columnDefinition = "varchar(255)")
    private String dataName;
}
