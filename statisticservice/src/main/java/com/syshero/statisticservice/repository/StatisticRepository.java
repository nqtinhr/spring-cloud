package com.syshero.statisticservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syshero.statisticservice.entity.Statistic;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

}