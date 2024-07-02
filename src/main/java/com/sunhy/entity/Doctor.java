package com.sunhy.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Doctor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long deptId;
}
