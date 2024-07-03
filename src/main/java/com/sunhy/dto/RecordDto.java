package com.sunhy.dto;

import com.sun.source.doctree.SeeTree;
import com.sunhy.entity.Orders;
import lombok.Data;

@Data
public class RecordDto extends Orders {

    private String name; // 患者

    private String hospitalName;

    private String combo;

    private String deptName;
}
