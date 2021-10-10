package com.xx.log.common.pojo.dto;

import com.xx.log.common.pojo.group.GroupV1;
import com.xx.log.common.pojo.group.GroupV2;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TestDTO {

    @NotBlank(message = "info 不能为空")
    @NotNull(message = "info 不能为null")
    @Size(min = 1, max = 4, groups = GroupV1.class)
    @Size(min = 6, max = 10, groups = GroupV2.class)
    private String info;

    private String usp;
}
