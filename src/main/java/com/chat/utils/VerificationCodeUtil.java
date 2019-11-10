package com.chat.utils;

import com.chat.pojo.VerificationCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZhouHua
 * @date 2019/11/9
 */
public class VerificationCodeUtil {
    private static Map<String,VerificationCode> VERIFICATION_CODE_MAP = new ConcurrentHashMap<>();

    public static Map<String,VerificationCode> getVerificationCodeMap(){
        return VERIFICATION_CODE_MAP;
    }

    public static void setVerificationCode(String mobile,VerificationCode verificationCode){
        VERIFICATION_CODE_MAP.put(mobile,verificationCode);
    }

    public static void removeVerificationCode(String mobile){
        VERIFICATION_CODE_MAP.remove(mobile);
    }

    public static boolean authVerificationCode(String mobile,String code){
        VerificationCode verificationCode=VERIFICATION_CODE_MAP.get(mobile);
        if(verificationCode==null)return false;
        return StringUtils.equals(code,verificationCode.getCode());
    }
}
