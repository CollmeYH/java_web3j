package com.example.java_web3j.utlis;

import com.example.java_web3j.utlis.contract.ERC721;
import com.example.java_web3j.utlis.contract.MarketItem;
import com.example.java_web3j.utlis.contract.Royalties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 调用erc721合约的方法
 */
public class DappWeb3jUtil {

	private static final Logger logger = LoggerFactory.getLogger(DappWeb3jUtil.class);
	
	public static final String SUPPORT_ROYALTIES_CODE = "0xb7799584";
	
	private static Web3j web3j;
	
	public static void initWeb3j(String url) {
		web3j = Web3j.build(new HttpService(url));
	}

	/**
	 * 根据tokenId获取erc721的url
	 * @param token
	 * @param tokenId
	 * @return
	 */
	public static String getErc721Uri(String token, String tokenId) {
		TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
		ERC721 contract721 = ERC721.load(token, web3j, transactionManager, new DefaultGasProvider());
		try {
			return contract721.tokenURI(new BigInteger(tokenId)).sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {	
			logger.error("获取721uri异常", e);
			return null;
		}
	}

	/**
	 * 获取erc721的name
	 * @param token
	 * @return
	 */
	public static String getName(String token) {
		TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
		ERC721 contract721 = ERC721.load(token, web3j, transactionManager, new DefaultGasProvider());
		try {
			return contract721.name().sendAsync().get();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取erc721的getSymbol
	 * @param token
	 * @return
	 */
	public static String getSymbol(String token) {
		TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
		ERC721 contract721 = ERC721.load(token, web3j, transactionManager, new DefaultGasProvider());
		try {
			return contract721.symbol().sendAsync().get();
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 自定义方法，和合约方法对应
	 * @param token
	 * @param tokenId
	 * @return 此处演示的是 list<BigInteger>怎么返回
	 */
	public static List<BigInteger> getRoyalties(String token, String tokenId) {
		TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
		Royalties royalties = Royalties.load(token, web3j, transactionManager, new DefaultGasProvider());
		try {
			return royalties.getFeeBps(new BigInteger(tokenId)).sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error("获取版权信息异常", e);
			return null;
		}
	}

	/**
	 * 此处演示的是 传值需要 byte[]
	 * @param token
	 * @return
	 */
	public static Boolean isSupportRoyalties(String token) {
		TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
		Royalties royalties = Royalties.load(token, web3j, transactionManager, new DefaultGasProvider());
		try {
			return royalties.supportsInterface(Numeric.hexStringToByteArray(SUPPORT_ROYALTIES_CODE)).sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {	
			logger.error("获取版权信息异常", e);
			return false;
		}
	}

	/**
	 * 此处演示的是自定义 返回结构 也就是和智能合约结构体一一对应
	 * 注意：web3j不推荐我们使用，我们用Java想读取智能合约数据的时候，极力推荐使用合约Event日志方式
	 * @param token
	 * @return 返回List<自定义对象>
	 */
	public static List<MarketItem> getFetchMarketItems(String token) {
		TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
		Royalties royalties = Royalties.load(token, web3j, transactionManager, new DefaultGasProvider());
		try {
			List<MarketItem> fetchMarketItems = royalties.getFetchMarketItems();
			return fetchMarketItems;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static void main(String[] args) {
		Web3j web3j = Web3j.build(new HttpService("https://rinkeby-light.eth.linkpool.io/"));
		String token = "0xf4f06F21D179F37387b2d4d4321297A36f3E7f8c";
		TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
		Royalties royalties = Royalties.load(token, web3j, transactionManager, new DefaultGasProvider());
		try {
			List<MarketItem> fetchMarketItems = royalties.getFetchMarketItems();
			System.out.println(fetchMarketItems);

		} catch (Exception  e) {
			logger.error("获取版权信息异常", e);

		}
	}


}
