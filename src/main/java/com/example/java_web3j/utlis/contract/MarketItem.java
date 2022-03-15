package com.example.java_web3j.utlis.contract;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

import java.math.BigInteger;

/**
 * @author wyh
 * @create: 2022-03-15 11:26
 * @Description : 智能合约返回List<结构体> 必须用继承StaticStruct 如下实现
 */
public class MarketItem extends StaticStruct {
    public BigInteger itemId;

    public String nftContract;

    public BigInteger tokenId;

    public String seller;
    public String owner;
    public BigInteger price;
    public BigInteger sold;

    public MarketItem(BigInteger itemId, String nftContract, BigInteger tokenId, String seller, String owner, BigInteger price, BigInteger sold) {
        super(
                new Uint256(itemId),
                new Address(nftContract),
                new Uint256(tokenId),
                new Address(seller),
                new Address(owner),
                new Uint256(price),
                new Uint8(sold)
        );
        this.itemId = itemId;
        this.nftContract = nftContract;
        this.tokenId = tokenId;
        this.seller = seller;
        this.owner = owner;
        this.price = price;
        this.sold = sold;
    }

    public MarketItem(Uint256 itemId, Address nftContract, Uint256 tokenId, Address seller, Address owner, Uint256 price, Uint8 sold) {
        super(
                itemId,
                nftContract,
                tokenId,
                seller,
                owner,
                price,
                sold
        );
        this.itemId = itemId.getValue();
        this.nftContract = nftContract.getValue();
        this.tokenId = tokenId.getValue();
        this.seller = seller.getValue();
        this.owner = owner.getValue();
        this.price = price.getValue();
        this.sold = sold.getValue();
    }

}
