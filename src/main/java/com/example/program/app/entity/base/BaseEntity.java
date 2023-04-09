package com.example.program.app.entity.base;

import com.example.program.common.status.EntityStatus;
import com.vladmihalcea.hibernate.type.array.LongArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@TypeDefs({
        @TypeDef(name = "string-array", typeClass= StringArrayType.class),
        @TypeDef(name = "long-array", typeClass= LongArrayType.class),
        @TypeDef(name  = "json", typeClass= JsonType.class)
})
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CREATED")
    private Date created;

    @Column(name = "UPDATED")
    private Date updated;

    @Enumerated(EnumType.STRING)
    private EntityStatus status;
}
