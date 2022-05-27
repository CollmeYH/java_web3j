package com.example.java_web3j.pojo;

import com.example.java_web3j.utlis.contract.ReservesItem;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author wyh
 * @create: 2022-03-29 11:05
 * @Description : 类说明
 */
public class PancakePair extends Contract {

    private static final String BINARY = "";

    @Deprecated
    protected PancakePair(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PancakePair(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PancakePair(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PancakePair(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }


    @Deprecated
    public static PancakePair load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PancakePair(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PancakePair load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PancakePair(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PancakePair load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new PancakePair(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PancakePair load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PancakePair(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static final String FUNC_GETRESERVES = "getReserves";


    /**
     * 获取对配合约储备量信息
     * @return
     */
    public ReservesItem getReserves() {
        final Function function = new Function(
                FUNC_GETRESERVES,
                Collections.emptyList(),
                Arrays.asList(new TypeReference<ReservesItem>() {
                }));
        try {
            return executeRemoteCallSingleValueReturn(function, ReservesItem.class).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
