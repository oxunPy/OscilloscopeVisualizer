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

    private String code;            // code is the same with flag type file filename
}
