package com.syshero.accountservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.syshero.accountservice.model.StatisticDTO;


@FeignClient(name = "statistic-service", fallback = StatisticServiceImpl.class)
public interface StatisticService {

    @PostMapping("/statistic")
    void add(@RequestBody StatisticDTO statisticDTO);
}

@Component
class StatisticServiceImpl implements StatisticService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void add(StatisticDTO statisticDTO) {
        // fallback
        logger.error("Statistic service is slow");
    }
}
