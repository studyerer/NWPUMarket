package edu.nwpu.market.controller.vo;

import org.omg.PortableServer._ServantActivatorStub;

import java.io.Serializable;

public class NWPUMarketUserVO implements Serializable {

    private Long userId;

    private String nickName;

    private String loginName;

    private String introduceSign;

    private String address;

    private int shopCartItemCount;

    /*test*/
    
    public NWPUMarketUserVO(Long _userId,String _nickName,String _loginName,
                            String _introduceSign,String _address,int _shopCartItemCount){
        userId = _userId;
        nickName = _nickName;
        loginName = _loginName;
        introduceSign = _introduceSign;
        address = _address;
        shopCartItemCount = _shopCartItemCount;

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getIntroduceSign() {
        return introduceSign;
    }

    public void setIntroduceSign(String introduceSign) {
        this.introduceSign = introduceSign;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getShopCartItemCount() {
        return shopCartItemCount;
    }

    public void setShopCartItemCount(int shopCartItemCount) {
        this.shopCartItemCount = shopCartItemCount;
    }
}
