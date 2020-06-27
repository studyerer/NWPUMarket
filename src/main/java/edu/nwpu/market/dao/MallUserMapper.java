
package edu.nwpu.market.dao;

import edu.nwpu.market.entity.NWPUMallUser;
import edu.nwpu.market.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(NWPUMallUser record);

    int insertSelective(NWPUMallUser record);

    NWPUMallUser selectByPrimaryKey(Long userId);

    NWPUMallUser selectByLoginName(String loginName);

    NWPUMallUser selectByLoginNameAndPasswd(@Param("loginName") String loginName, @Param("password") String password);

    int updateByPrimaryKeySelective(NWPUMallUser record);

    int updateByPrimaryKey(NWPUMallUser record);

    List<NWPUMallUser> findMallUserList(PageQueryUtil pageUtil);

    int getTotalMallUsers(PageQueryUtil pageUtil);

    int lockUserBatch(@Param("ids") Integer[] ids, @Param("lockStatus") int lockStatus);
}