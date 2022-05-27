package com.example.java_web3j;


import com.example.java_web3j.utlis.DappWeb3jUtil;
import com.example.java_web3j.utlis.contract.ReservesItem;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class JavaWeb3jApplicationTests {


    @Test
    void contextLoads() {
//        DappWeb3jUtil.approve("0x8f4bf43401feaca2ec8e6f308a3b6c3e55d392a9",new BigInteger("100"),"a9b2c5e6a335e8fed98f8c0020c5c1cf81b420d4c3c4772f1f8a241ab6db5947","0xc31101B42b7bfC3290bf6deAa90924fe8d310156");

        ReservesItem reserves = DappWeb3jUtil.getReserves("0xaB185475ea38e5ECD697B3f9E24ED8bF57EedFD3");
        System.out.println(reserves._reserve0);
    }

}
