package com.revature.screenforce.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.screenforce.beans.*;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Integer> {

}
