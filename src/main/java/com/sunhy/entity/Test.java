package com.sunhy.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Test implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    Integer id;
}
