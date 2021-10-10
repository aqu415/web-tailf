package com.xx.log.ssh2.test;

import ch.ethz.ssh2.Connection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xx.log.common.pojo.dto.TestDTO;
import com.xx.log.ssh2.Ssh2Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class SS2Test {

    private ObjectMapper objectMapper = new ObjectMapper();


    @SneakyThrows
    @Test
    public void conTest() {
        Connection connection = Ssh2Utils.getConnect("kanan", "1", "192.168.60.128");
        log.info("connection:" + connection);
    }

    @Test
    public void upLoadTest() {
        Ssh2Utils.upLoad("/home/kanan/Desktop", "D:\\log\\tailf/spring.log", "kanan", "1", "192.168.60.128");
    }

    @Test
    public void cmdTest() {
        String res = Ssh2Utils.exeCmd("chmod u+x /home/kanan/tp2.txt", "kanan", "1", "192.168.60.128");
        log.info("cmdTest res:" + res);
    }

    @Test
    public void downLoadTest() {
        boolean res = Ssh2Utils.downLoad("/home/kanan/tp2.txt", "D:\\log", "kanan", "1", "192.168.60.128");
        log.info("download res:" + res);
    }

    @Test
    public void testJson() throws JsonProcessingException {
        TestDTO dto = new TestDTO();
        dto.setInfo("ww");
        String info = objectMapper.writeValueAsString(dto);
        System.out.println(info);
    }

}
