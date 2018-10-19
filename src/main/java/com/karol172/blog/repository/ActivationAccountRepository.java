package com.karol172.blog.repository;

import com.karol172.blog.model.ActivationAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivationAccountRepository extends JpaRepository<ActivationAccount, Long> {

    ActivationAccount findFirstByActivationCode (String activationCode);

}
