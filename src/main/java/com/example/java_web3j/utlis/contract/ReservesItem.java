package com.example.java_web3j.utlis.contract;

import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.generated.Uint112;
import org.web3j.abi.datatypes.generated.Uint32;

import java.math.BigInteger;

/**
 * @author wyh
 * @create: 2022-03-15 11:26
 * @Description : 配对合约 getReserves方法返回结构体
 */
public class ReservesItem extends StaticStruct {
    public BigInteger _reserve0;

    public BigInteger _reserve1;

    public BigInteger _blockTimestampLast;

    public ReservesItem(BigInteger _reserve0, BigInteger _reserve1, BigInteger _blockTimestampLast) {
        super(
                new Uint112(_reserve0),
                new Uint112(_reserve1),
                new Uint32(_blockTimestampLast)
        );
        this._reserve0 = _reserve0;
        this._reserve1 = _reserve1;
        this._blockTimestampLast = _blockTimestampLast;
    }

    public ReservesItem(Uint112 _reserve0, Uint112 _reserve1, Uint32 _blockTimestampLast) {
        super(
                _reserve0,
                _reserve1,
                _blockTimestampLast
        );
        this._reserve0 = _reserve0.getValue();
        this._reserve1 = _reserve1.getValue();
        this._blockTimestampLast = _blockTimestampLast.getValue();
    }

    public BigInteger get_reserve0() {
        return _reserve0;
    }

    public BigInteger get_reserve1() {
        return _reserve1;
    }

    public BigInteger get_blockTimestampLast() {
        return _blockTimestampLast;
    }
}
