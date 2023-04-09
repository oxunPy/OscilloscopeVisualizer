package com.example.program.app.entity;

import com.example.program.app.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "osci_log_history")
public class OsciLogHistory extends BaseEntity {
    private String exceptionMsg;

    @Column(name = "osci_user_id", nullable = true)
    private Long userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "osci_user_id", insertable = false, updatable = false)
    private OsciUserEntity osciUser;
}
