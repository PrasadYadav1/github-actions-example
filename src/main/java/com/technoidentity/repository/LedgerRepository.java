package com.technoidentity.repository;

import com.technoidentity.entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LedgerRepository extends JpaRepository<Ledger, Long> {}
