package edu.nwpu.market.dao;

import edu.nwpu.market.entity.NWPUMarketUser;
import edu.nwpu.market.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(NWPUMarketUser record);

    int insertSelective(NWPUMarketUser record);

    NWPUMarketUser selectByPrimaryKey(Long userId);

    NWPUMarketUser selectByLoginName(String loginName);

    NWPUMarketUser selectByLoginNameAndPasswd(@Param("loginName") String loginName, @Param("password") String password);

    int updateByPrimaryKeySelective(NWPUMarketUser record);

    int updateByPrimaryKey(NWPUMarketUser record);

    List<NWPUMarketUser> findMallUserList(PageQueryUtil pageUtil);

    int getTotalMallUsers(PageQueryUtil pageUtil);

    int lockUserBatch(@Param("ids") Integer[] ids, @Param("lockStatus") int lockStatus);
}