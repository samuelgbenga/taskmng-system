package com.squad22podA.task_mgt.repository;

import com.squad22podA.task_mgt.entity.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserModelRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByEmail(String email);

}
