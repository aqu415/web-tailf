package com.xx.log.controller;

import com.xx.log.common.pojo.dto.TestDTO;
import com.xx.log.common.pojo.group.GroupV1;
import com.xx.log.common.pojo.vo.TestVO;
import com.xx.log.common.util.MixUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {


    /**
     * test
     *
     * @param testDTO
     * @return
     */
    @PostMapping("/test")
    public TestVO test(@RequestBody @Validated({GroupV1.class}) TestDTO testDTO) {
        log.info("testDTO:{}", testDTO);
        return TestVO.builder().code(0).msg(MixUtil.toJsonString(testDTO)).build();
    }
}
