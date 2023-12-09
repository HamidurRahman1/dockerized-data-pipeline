package hrahman.ddp.hibernate.services;

import hrahman.ddp.hibernate.models.NycViolationAnalytics;
import hrahman.ddp.hibernate.repositories.NycViolationAnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NycViolationAnalyticsService {

    @Autowired
    private NycViolationAnalyticsRepository nycViolationAnalyticsRepository;

    public List<NycViolationAnalytics> getAll() {
        return nycViolationAnalyticsRepository.findAll();
    }

    public NycViolationAnalytics save(NycViolationAnalytics nycViolationAnalytics) {
        return nycViolationAnalyticsRepository.saveAndFlush(nycViolationAnalytics);
    }
}
