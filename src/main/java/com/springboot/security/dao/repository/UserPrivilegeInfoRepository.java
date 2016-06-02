package com.springboot.security.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.security.dao.entity.UserPrivilegeInfo;

@Repository
public interface UserPrivilegeInfoRepository extends JpaRepository<UserPrivilegeInfo, Long> {

    /**
     * where user_info_id = [userInfoId]
     * 
     * @param userInfoid
     * @return List<{@link UserPrivilegeInfo}>
     */
    List<UserPrivilegeInfo> findByUserInfoId(Long userInfoId);

}
