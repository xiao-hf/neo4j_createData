package com.xiao.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    private Long id;
    private String groupId;
    private String groupName;
    private String groupOwnerWxId;
    private String groupOwnerName;
    private String groupOwnerIdCode;
    private String groupOwnerPhone;
    private String groupOwnerAddress;
} 