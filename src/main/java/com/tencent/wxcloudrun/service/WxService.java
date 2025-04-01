package com.tencent.wxcloudrun.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.tencent.wxcloudrun.dto.WxLoginInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WxService {
    private final WxMaService wxMaService;

    /**
     * 获取用户手机号和openid
     */
    public WxLoginInfo getPhoneNumberAndOpenid(String code, String phoneCode, String encryptedData, String iv) {
        try {
            // 使用登录code获取session_key和openid
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            String openid = sessionInfo.getOpenid();
            
            // 获取手机号
            WxMaPhoneNumberInfo phoneNumberInfo = wxMaService.getUserService().getPhoneNoInfo(phoneCode);
            String phoneNumber = phoneNumberInfo.getPhoneNumber();  
            
            // 返回手机号和openid
            WxLoginInfo loginInfo = new WxLoginInfo();
            loginInfo.setOpenid(openid);
            loginInfo.setPhone(phoneNumber);
            return loginInfo;
        } catch (Exception e) {
            log.error("获取微信信息失败: loginCode={}, phoneCode={}, error={}", code, phoneCode, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 仅获取openid
     */
    public String getOpenidFromCode(String code) {
        try {
            // 使用code获取openid
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            return sessionInfo.getOpenid();
        } catch (Exception e) {
            log.error("获取微信openid失败: code={}, error={}", code, e.getMessage(), e);
            return null;
        }
    }
}
