package com.tianfang.user.dao;

import java.util.List;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tianfang.common.constants.DataStatus;
import com.tianfang.common.model.PageQuery;
import com.tianfang.common.mybatis.MyBatisBaseDao;
import com.tianfang.common.util.BeanUtils;
import com.tianfang.common.util.StringUtils;
import com.tianfang.user.dto.MemoDto;
import com.tianfang.user.mapper.MemoMapper;
import com.tianfang.user.pojo.Memo;
import com.tianfang.user.pojo.MemoExample;

@Repository
public class MemoDao extends MyBatisBaseDao<Memo> {
	
	@Getter
	@Autowired
	private MemoMapper mapper;
	
	public List<MemoDto> findMemoByParam(MemoDto dto){
		return findMemoByParam(dto, null);
	}
	
	public List<MemoDto> findMemoByParam(MemoDto dto, PageQuery query) {
		MemoExample example = new MemoExample();
		MemoExample.Criteria criteria = example.createCriteria();
        assemblyParams(dto, criteria);
        if(null != query){
        	example.setOrderByClause("create_time desc limit "+query.getStartNum() +"," + query.getPageSize());
		}
        List<Memo> results = mapper.selectByExample(example);        
		return BeanUtils.createBeanListByTarget(results, MemoDto.class);
	}
	
	public int countMemoByParam(MemoDto dto){
		MemoExample example = new MemoExample();
		MemoExample.Criteria criteria = example.createCriteria();
        assemblyParams(dto, criteria);
		return mapper.countByExample(example);
	}
	
	/**
	 * 组装查询参数
	 * @param params
	 * @param criteria
	 * @author xiang_wang
	 * 2016年1月12日下午4:51:12
	 */
	private void assemblyParams(MemoDto params, MemoExample.Criteria criteria) {
		if (null != params) {
        	if (StringUtils.isNotBlank(params.getId())){
        		criteria.andIdEqualTo(params.getId().trim());
        	}
        	if (StringUtils.isNotBlank(params.getUserId())){
        		criteria.andUserIdEqualTo(params.getUserId().trim());
        	}
        	if (StringUtils.isNotBlank(params.getUserName())){
        		criteria.andUserNameLike("%"+params.getUserName()+"%");
        	}
        }
		criteria.andStatEqualTo(DataStatus.ENABLED);
	}
}