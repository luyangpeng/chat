package com.chat.task;

import com.chat.pojo.VerificationCode;
import com.chat.utils.VerificationCodeUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 手机验证码相关的定时任务
 * @author ZhouHua
 * @date 2019/11/9
 */
@Component
public class VerificationCodeTask {

    @Scheduled(cron = "0 */1 * * * ?")
    private void configureTasks(){
        Date currDate=new Date();
        Map<String,VerificationCode> verificationCodeMap=VerificationCodeUtil.getVerificationCodeMap();
        Iterator<Map.Entry<String, VerificationCode>> entryIterator = verificationCodeMap.entrySet().iterator();
        while(entryIterator.hasNext()){
            VerificationCode verificationCode = entryIterator.next().getValue();
            if(currDate.compareTo(verificationCode.getExpireTime())<0){
                entryIterator.remove();
            }
        }
    }
}
