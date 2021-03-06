package com.edu.nju.seckill;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.edu.nju.seckill.domain.User;
import com.edu.nju.seckill.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
class SeckillApplicationTests {
    @Resource
    RedisUtil redisUtil;

    @Autowired
    BCryptPasswordEncoder encoder;
    @Test
    void contextLoads() {
//        System.out.println((User) redisUtil.hget("user","13901586395"));
        User user= (User) redisUtil.hget("user","13901586395");
        System.out.println(user.getPassword());
    }
    @Test
    public void testRandomName(){
        System.out.println(UUID.randomUUID().toString().substring(0,8));

    }


    @Test
    public void testRedis(){
        User user=new User();
        for(int i=0;i<10;i++) {
            user.setName(i+"");
            user.setPassword("123123");
            user.setPhone("15651879552");
            redisUtil.hset("user",i+"", user);
        }

    }

    @Test
    public void testJwt(){
        //生成token
        Map<String,Object> header=new HashMap<String, Object>() ;
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        String token= JWT.create()
                .withHeader(header)// 设置头部信息 Header
                .withIssuer("Service")//设置 载荷 签名是有谁生成 例如 服务器
                .withSubject("login")//设置 载荷 签名的主题
                .withAudience("client")//设置 载荷 签名的观众 也可以理解谁接受签名的
                // .withNotBefore(new Date())//设置 载荷 定义在什么时间之前，该jwt都是不可用的.
                .withIssuedAt(new Date(System.currentTimeMillis())) //设置 载荷 生成签名的时间
                .withExpiresAt(new Date(System.currentTimeMillis()+60000))//设置 载荷 签名过期的时间
                .withClaim("user","123456")//设置自定义信息
                .withClaim("pass","123456")
                .sign(Algorithm.HMAC256("seckill"));//签名 Signature,加盐
        System.out.println(token);

        //验证token信息
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256("seckill"))
                .withIssuer("Service")//不添加 .withIssuer("SERVICE") 也是可以获取 JWTVerifier 。
                .build();
        DecodedJWT decodedJWT=jwtVerifier.verify(token);
        System.out.println("header "+decodedJWT.getHeader());
        System.out.println("payload "+decodedJWT.getPayload());
        System.out.println("Signature "+decodedJWT.getSignature());
        System.out.println("Subject "+decodedJWT.getSubject());
        System.out.println("Token "+decodedJWT.getToken());
        System.out.println("Algorithm "+decodedJWT.getAlgorithm());
        System.out.println("Audience "+decodedJWT.getAudience());
        System.out.println("Claims "+decodedJWT.getClaims());
        System.out.println("Claim "+decodedJWT.getClaim("user").asString());
        System.out.println("ContentType "+decodedJWT.getContentType());
        System.out.println("ExpiresAt "+decodedJWT.getExpiresAt());
        System.out.println("IssuedAt"+decodedJWT.getIssuedAt());


        System.out.println("-----------------------------------------");
        Map<String, Claim> map=decodedJWT.getClaims();
        for (Map.Entry<String, Claim> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().asString());
        }
        //

    }

    @Test
    public void testSecurity(){
        String pass="123456";
        String hash=encoder.encode(pass);
        System.out.println(encoder.matches(pass,hash));
    }

}
