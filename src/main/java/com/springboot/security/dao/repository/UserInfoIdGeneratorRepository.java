package com.springboot.security.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.security.dao.entity.UserInfoIdGenerator;

@Repository
public interface UserInfoIdGeneratorRepository extends JpaRepository<UserInfoIdGenerator, String> {

}
