package com.tianfang.message.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianfang.common.util.BeanUtils;
import com.tianfang.message.dao.NoticeUsersDao;
import com.tianfang.message.dto.NoticeDto;
import com.tianfang.message.dto.NoticeUsersDto;
import com.tianfang.message.pojo.NoticeUsers;


@Service
public class NoticeUsersServiceImpl implements INoticeUsersService {

	@Autowired
	private NoticeUsersDao noticeUsersDao;

	/**
	 * @author YIn
	 * @time:2016年2月3日 下午2:43:35
	 */
	@Override
	public int addNotice(NoticeUsersDto noticeUsersDto) {
		NoticeUsers noticeUsers = BeanUtils.createBeanByTarget(noticeUsersDto, NoticeUsers.class);
		return noticeUsersDao.insertSelective(noticeUsers);
	}

	/**
	 * @author YIn
	 * @time:2016年2月3日 下午5:26:18
	 */
	@Override
	public int releaseNotice(List<NoticeUsersDto> list) {

		return noticeUsersDao.releaseNotice(list);
	}


	public NoticeDto getLast(String userId){

		return noticeUsersDao.getLast(userId);
	}

	/**
	 * @author YIn
	 * @time:2016年3月28日 下午6:42:32
	 */
	@Override
	public int findMount(String id) {

		return noticeUsersDao.findMount(id);
	}
	/**
	 * @author YIn
	 * @time:2016年3月28日 上午11:08:35
	 */
	@Override
	public int findRead(String id) {
		return noticeUsersDao.findRead(id);
	}

	/**
	 * @author YIn
	 * @time:2016年3月29日 上午9:41:13
	 */
	@Override
	public int updateNotice(NoticeUsersDto noticeUsersDto) {
		NoticeUsers noticeUsers = BeanUtils.createBeanByTarget(noticeUsersDto, NoticeUsers.class);
		return noticeUsersDao.updateByPrimaryKeySelective(noticeUsers);
	}

	/**
	 * @author YIn
	 * @time:2016年3月29日 上午9:48:02
	 */
	@Override
	public List<NoticeUsersDto> findNoticeUsers(NoticeUsersDto noticeUsersDto) {
		NoticeUsers noticeUsers = BeanUtils.createBeanByTarget(noticeUsersDto, NoticeUsers.class);
		List<NoticeUsers> list = noticeUsersDao.selectByParameter(noticeUsers);
		List<NoticeUsersDto> dtoList = BeanUtils.createBeanListByTarget(list, NoticeUsersDto.class);
		return dtoList;
	}

}