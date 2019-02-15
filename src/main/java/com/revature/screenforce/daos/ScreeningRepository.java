package com.revature.screenforce.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.revature.screenforce.beans.Screening;


@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

}

