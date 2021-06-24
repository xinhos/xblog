package cn.xinhos.util;

import cn.xinhos.entry.Token;
import cn.xinhos.entry.User;
import cn.xinhos.entry.dto.SiteInfoDto;
import cn.xinhos.service.PropertyService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/*
 * @ClassName: TokenHelper
 * @Description: token的一些工具
 * @author: xinhos
 * @data: 2021-06-15-11:15
 */
@Slf4j
@Component("tokenHelper")
public class TokenHelper {
    private Algorithm algorithm;
    private SiteInfoDto siteInfoDto;

    @Resource private PropertyService propertyService;

    @Resource public void setSiteInfoDto(PropertyService propertyService) {
        this.siteInfoDto = propertyService.getSiteInfo();
    }

    @Resource public void setAlgorithm(PropertyService propertyService) {
        this.algorithm = Algorithm.HMAC256(propertyService.getSiteInfo().getSecretKey());
    }

    /* 将所给的token字符串解析为一个token对象，若解析错误、token过期，则返回null */
    public Token parseToken(String tokenStr) {
        Token token = null;
        if (Objects.equals(tokenStr, "")) {
            return token;
        }

        try {
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("SERVICE").build();
            DecodedJWT jwt = verifier.verify(tokenStr);
            String issuer = jwt.getIssuer();
            if (Objects.equals(issuer, "SERVICE")) {
                Date expireTime = jwt.getExpiresAt();
                Date issuedTime = jwt.getIssuedAt();
                if (expireTime.getTime() - issuedTime.getTime() < 24 * 60 * 1000) {
                    token = new Token();
                    token.setUserId(jwt.getClaim("userId").asInt().longValue());
                    token.setIssuedTime(issuedTime);
                    token.setExpireTime(expireTime);
                    token.setIsAdmin(jwt.getClaim("isAdmin").asBoolean());
                }
            }
        } catch (JWTVerificationException e) {
            log.error(e.getMessage());
            return null;
        }

        return token;
    }

    /* 根据用户信息创建一个token字符串，采用对称加密（HMAC250） */
    public String createToken(User user, boolean isAdmin) {
        long currentTime = System.currentTimeMillis();
        long expireTime = currentTime + siteInfoDto.getKeepLoginNum() * 24 * 60 * 1000;
        SiteInfoDto siteInfoDto = propertyService.getSiteInfo();

        return JWT.create()
                .withIssuer(siteInfoDto.getSecretKey())
                .withIssuedAt(new Date(currentTime))
                .withExpiresAt(new Date(expireTime))
                .withClaim("userId", user.getId())
                .withClaim("isAdmin", isAdmin)
                .sign(algorithm);
    }
}
