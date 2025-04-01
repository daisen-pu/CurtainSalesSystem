package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class PageDTO {
    private int page = 1;
    private int size = 10;
}
