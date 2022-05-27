package com.example.java_web3j.pojo;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wyh
 * @create: 2022-03-29 11:05
 * @Description : 类说明
 */
public class PancakeRouter extends Contract {

    private static final String BINARY = "";

    @Deprecated
    protected PancakeRouter(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PancakeRouter(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PancakeRouter(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PancakeRouter(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }


    @Deprecated
    public static PancakeRouter load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PancakeRouter(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PancakeRouter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PancakeRouter(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PancakeRouter load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new PancakeRouter(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PancakeRouter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PancakeRouter(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static final String FUNC_ADDLIQUIDITY = "addLiquidity";
    public static final String FUNC_SWAPEXACTTOKENSFORTOKENS = "swapExactTokensForTokens";


    public RemoteFunctionCall<TransactionReceipt> addLiquidity(String tokenA, String tokenB, BigInteger amountADesired, BigInteger amountBDesired, BigInteger amountAMin, BigInteger amountBMin, String to, BigInteger deadline) {
        final Function function = new Function(
                FUNC_ADDLIQUIDITY,
                Arrays.<Type>asList(
                        new Address(tokenA),
                        new Address(tokenB),
                        new Uint(amountADesired),
                        new Uint(amountBDesired),
                        new Uint(amountAMin),
                        new Uint(amountBMin),
                        new Address(to),
                        new Uint(deadline)
                ),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> swapExactTokensForTokens(BigInteger amountIn, BigInteger amountOutMin, List<String> path, String to, BigInteger deadline) {
        List<Address> list = new ArrayList<>();
        list.add(new Address(path.get(0)));
        list.add(new Address(path.get(1)));

        final Function function = new Function(
                FUNC_SWAPEXACTTOKENSFORTOKENS,
                Arrays.<Type>asList(
                        new Uint(amountIn),
                        new Uint(amountOutMin),
                        new DynamicArray<Address>(Address.class,list),
                        new Address(to),
                        new Uint(deadline)
                ),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }



}
