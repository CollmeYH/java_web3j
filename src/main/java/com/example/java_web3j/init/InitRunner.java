package com.example.java_web3j.init;


import com.example.java_web3j.utlis.DappEventUtils;
import com.example.java_web3j.utlis.DappWeb3jUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitRunner implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(InitRunner.class);

	private final static String CHAIN_API_URL = "https://rinkeby-light.eth.linkpool.io/";

	
	@Override
	public void run(String... args) throws Exception {
		logger.info("开始初始化数据");
		this.initWeb3j();
		logger.info("初始化完成...");
	}

	
	private void initWeb3j() {
		logger.info("初始化web3j");
		DappWeb3jUtil.initWeb3j(CHAIN_API_URL);
		DappEventUtils.initWeb3j(CHAIN_API_URL);
		logger.info("初始化web3j完成");
	}
}
