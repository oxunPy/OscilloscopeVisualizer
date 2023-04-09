package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "osci_device")
public class OsciDeviceEntity extends BaseEntity {
    private String pcName;

    private String pcOwner;

    private String hdd;

    private String cpu;

    private String motherboard;
}
