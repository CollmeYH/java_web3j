package com.example.java_web3j.utlis;

import com.example.java_web3j.pojo.PancakePair;
import com.example.java_web3j.pojo.PancakeRouter;
import com.example.java_web3j.utlis.contract.ERC721;
import com.example.java_web3j.utlis.contract.MarketItem;
import com.example.java_web3j.utlis.contract.ReservesItem;
import com.example.java_web3j.utlis.contract.Royalties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 调用erc721合约的方法
 */
public class DappWeb3jUtil {

    private static final Logger logger = LoggerFactory.getLogger(DappWeb3jUtil.class);

    private static Web3j web3j;

    public static void initWeb3j(String url) {
        web3j = Web3j.build(new HttpService(url));
    }

    private static final BigInteger gasPrice = Convert.toWei(String.valueOf(30), Convert.Unit.GWEI).toBigInteger();
    private static final BigInteger gasLimit = BigInteger.valueOf(10000000);


    /**
     * 根据tokenId获取erc721的url
     *
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
     *
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
     *
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
     *
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
     * 此处演示的是自定义 返回结构 也就是和智能合约结构体一一对应
     * 注意：web3j不推荐我们使用，我们用Java想读取智能合约数据的时候，极力推荐使用合约Event日志方式
     *
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


    /**
     * 发送基础币种
     *
     * @param to         发送给谁
     * @param value      数量 单位eth
     * @param privateKey 私钥
     * @return 交易id
     */
    public static String senEth(String to, BigDecimal value, String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        try {
            TransactionReceipt transactionReceipt = Transfer.sendFunds(
                    web3j, credentials, to,
                    value, Convert.Unit.ETHER).send();
            return transactionReceipt.getTransactionHash();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String approve(String address, BigInteger value, String privateKey, String contractAddr) {
        Credentials credentials = Credentials.create(privateKey);
        try {
            Function function = new Function(
                    "approve",
                    Arrays.asList(new Address(address), new Uint256(value)),
                    Collections.<TypeReference<?>>emptyList());

            String encodedFunction = FunctionEncoder.encode(function);

            //获取随机数nonce
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            //签名交易
            BigInteger customGasPrice = Convert.toWei(String.valueOf(30), Convert.Unit.GWEI).toBigInteger();
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce, customGasPrice, BigInteger.valueOf(10000000), contractAddr, encodedFunction);
            byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            //广播交易
            String hexSignMsg = Numeric.toHexString(signMessage);

            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexSignMsg).sendAsync().get();
            logger.info("addLiquidity==>{}", ethSendTransaction.getTransactionHash());
            return ethSendTransaction.getTransactionHash();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取基本数据
     * @param token 配对合约
     * @return
     */
    public static ReservesItem getReserves(String token) {
        TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
        PancakePair pancakePair = PancakePair.load(token, web3j, transactionManager, new DefaultGasProvider());
        try {
            return pancakePair.getReserves();
        } catch (Exception e) {
            logger.error("获取版权信息异常", e);
            return null;
        }
    }


    public static String addLiquidity(String privateKey, String contractAddr) {
        Credentials credentials = Credentials.create(privateKey);
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, 97);

        PancakeRouter router = PancakeRouter.load(contractAddr, web3j, transactionManager, new StaticGasProvider(gasPrice, gasLimit));
        RemoteFunctionCall<TransactionReceipt> transactionReceiptRemoteFunctionCall = router.addLiquidity(
                "0xd218CCF4cD88eb2B90076683B415a31750Bc46aa",
                "0xe38a71627E3289D8154da871F9ecF25Cd41EB483",
                new BigInteger("1000000000000000000"),
                new BigInteger("9700000000000000000"),
                BigInteger.ZERO,
                BigInteger.ZERO,
                "0xC5ba7F9959a76f6c6C89e4a7ED67Ba5127F48905",
                new BigInteger("1648542214")
        );
        TransactionReceipt send = null;
        try {
            send = transactionReceiptRemoteFunctionCall.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("addLiquidity==>{}", send.getTransactionHash());

        return send.getTransactionHash();
    }

    public static String swapExactTokensForTokens(String privateKey, String contractAddr) {
        Credentials credentials = Credentials.create(privateKey);
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, 97);

        PancakeRouter router = PancakeRouter.load(contractAddr, web3j, transactionManager, new StaticGasProvider(gasPrice, gasLimit));
        List<String> list = new ArrayList<>();
        list.add("0xe38a71627E3289D8154da871F9ecF25Cd41EB483");
        list.add("0xd218CCF4cD88eb2B90076683B415a31750Bc46aa");
        RemoteFunctionCall<TransactionReceipt> transactionReceiptRemoteFunctionCall = router.swapExactTokensForTokens(
                new BigInteger("20624748578800000000"),
                new BigInteger("0"),
                list,
                "0xC5ba7F9959a76f6c6C89e4a7ED67Ba5127F48905",
                new BigInteger("1648547882")
        );
        TransactionReceipt send = null;
        try {
            send = transactionReceiptRemoteFunctionCall.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("addLiquidity==>{}", send.getTransactionHash());

        return send.getTransactionHash();
    }
    


}
