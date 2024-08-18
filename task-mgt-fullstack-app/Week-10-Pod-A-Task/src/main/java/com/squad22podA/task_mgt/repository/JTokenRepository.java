package com.squad22podA.task_mgt.repository;

import com.squad22podA.task_mgt.entity.model.JToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JTokenRepository extends JpaRepository<JToken, Long> {

    @Query(value = """
      select t from JToken t inner join UserModel u\s
      on t.userModel.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<JToken> findAllValidTokenByUser(Long id);

    Optional<JToken> findByToken(String token);
}
