package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;

import javax.persistence.Column;
import java.math.BigDecimal;

public class OsciDataEntity extends BaseEntity {

    @Column(name = "data")
    private BigDecimal value;
}
