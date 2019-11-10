package com.chat.service;

import com.chat.mapper.UserInfoMapper;
import com.chat.pojo.VerificationCode;
import com.chat.pojo.po.UserInfo;
import com.chat.utils.RandomUtils;
import com.chat.utils.VerificationCodeUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Calendar;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;


    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return
     */
    public boolean queryUsernameIsExist(String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        UserInfo result = userInfoMapper.selectOne(userInfo);
        return result != null;
    }

    /**
     * 根据用户名和密码进行登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public UserInfo queryUserForLogin(String username, String password) {
        UserInfo criteria=new UserInfo();
        criteria.setMobile(username);
        criteria.setPassword(password);
        UserInfo userInfo=userInfoMapper.selectOne(criteria);
        if(userInfo==null){
            criteria=new UserInfo();
            criteria.setUsername(username);
            criteria.setPassword(password);
            userInfo=userInfoMapper.selectOne(criteria);
        }
        return userInfo;
    }

    /**
     * 根据ID查询用户
     * @param userId 用户ID
     * @return
     */
    public UserInfo queryUserById(Long userId) {
        return userInfoMapper.selectByPrimaryKey(userId);
    }

    /**
     * 修改用户信息
     * @param user 用户信息
     */
    public boolean updateUserInfo(UserInfo user) {
        return userInfoMapper.updateByPrimaryKeySelective(user)>0;
    }

    /**
     * 保存用户信息
     * @param user
     * @return
     */
    public boolean saveUser(UserInfo user) {
        return userInfoMapper.insert(user)>0;
    }

    /**
     * 根据unionid查询用户信息
     * @param unionid
     * @return
     */
    public UserInfo queryUserByUnionid(String unionid){
        UserInfo userInfo=new UserInfo();
        userInfo.setUnionid(unionid);
        return userInfoMapper.selectOne(userInfo);
    }

    /**
     * 根据openid查询用户信息
     * @param openid
     * @return
     */
    public UserInfo queryUserByOpenid(String openid){
        UserInfo userInfo=new UserInfo();
        userInfo.setOpenid(openid);
        return userInfoMapper.selectOne(userInfo);
    }

    /**
     * 根据手机号查询用户
     * @param mobile 手机号
     * @return
     */
    public UserInfo queryUserByMobile(String mobile) {
        UserInfo userInfo=new UserInfo();
        userInfo.setMobile(mobile);
        return userInfoMapper.selectOne(userInfo);
    }

    /**
     * 生成用户名
     * @return
     */
    public String generateUsername(){
        String username;
        do {
            username = RandomUtils.randomNumber(8);
        }while (queryUsernameIsExist(username));
        return username;
    }

    /**
     * 生成验证码
     * @return
     */
    public String generateVerificationCode(String mobile){
        String code = RandomUtils.randomNumber(6);
        VerificationCode verificationCode=new VerificationCode();
        verificationCode.setCode(code);
        verificationCode.setMobile(mobile);
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 5);
        verificationCode.setExpireTime(nowTime.getTime());
        VerificationCodeUtil.setVerificationCode(mobile,verificationCode);
        return code;
    }

    /**
     * 根据用户名或昵称搜索用户
     * @param keyword
     * @return
     */
    public List<UserInfo> queryUsersForSearch(String keyword){
        Example userExample = new Example(UserInfo.class);
        userExample.selectProperties("id","avatar","username","nickname");
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andLike("username","%"+keyword+"%");
        criteria.orLike("nickname","%"+keyword+"%");
        userExample.orderBy("id").desc();
        return userInfoMapper.selectByExampleAndRowBounds(userExample,new RowBounds(0,10));
    }

    /**
     * 赠送钻石
     * @param donorUser 赠送人
     * @param receiverUser 领取人
     * @param amount 赠送数量
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public synchronized boolean donateDiamond(UserInfo donorUser,UserInfo receiverUser,Integer amount){
        if(donorUser.getDiamond()<amount){
            return false;
        }
        receiverUser.setDiamond(receiverUser.getDiamond()+amount);
        donorUser.setDiamond(donorUser.getDiamond()-amount);
        int res1=userInfoMapper.updateByPrimaryKey(donorUser);
        int res2=userInfoMapper.updateByPrimaryKey(receiverUser);
        return res1>0&&res2>0;
    }
}
