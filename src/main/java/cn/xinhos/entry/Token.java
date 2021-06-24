package cn.xinhos.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
 * @ClassName: Token
 * @Description: 【实体类】JWT单点登录凭证信息
 * @author: xinhos
 * @data: 2021-06-16-11:41
 */
@NoArgsConstructor @AllArgsConstructor
@Data public class Token {
    private Long userId;
    private Boolean isAdmin;
    private Date issuedTime;
    private Date expireTime;
}
