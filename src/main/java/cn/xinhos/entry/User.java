package cn.xinhos.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor
@Data public class User {
    private Integer id;
    private String mail;
    private String phone;
    private String name;
    private String password;
    private String headerImg;
    private Date regData;
}
