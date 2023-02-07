package com.example.stock.util;

import com.example.stock.entity.User;
import com.example.stock.vo.UserSDK;

public class UserSDKMapper {
    public static UserSDK userToUserSDK(User user){
        UserSDK userSDK = new UserSDK();
        userSDK.setId(user.getId());
        userSDK.setNickname(user.getNickname());
        userSDK.setRegisterDate(user.getRegisterDate());
        userSDK.setLastLoginDate(user.getLastLoginDate());
        userSDK.setLoginCount(user.getLoginCount());
        return userSDK;
    }
}
