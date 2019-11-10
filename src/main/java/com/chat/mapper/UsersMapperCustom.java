package com.chat.mapper;

import java.util.List;

import com.chat.pojo.Users;
import com.chat.pojo.vo.FriendRequestVO;
import com.chat.pojo.vo.MyFriendsVO;
import com.chat.utils.MyMapper;

public interface UsersMapperCustom extends MyMapper<Users> {
	
	public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);
	
	public List<MyFriendsVO> queryMyFriends(String userId);
	
	public void batchUpdateMsgSigned(List<String> msgIdList);
	
}