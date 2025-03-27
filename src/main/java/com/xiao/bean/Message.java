package com.xiao.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String wxId;
    private String message;
    private Boolean isExtreme;
    private Long time;
    private String groupId;
}