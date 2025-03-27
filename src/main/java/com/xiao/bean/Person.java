package com.xiao.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private Long id;
    private String idCode;
    private String wxId;
    private Boolean isReal;
    private Boolean isImport;
    private List<String> wxIds;
    private String phone;
    private String address;
    private String name;
    private List<String> personType;
    private String city;
} 