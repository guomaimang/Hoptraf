package tech.hirsun.hoptraf.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.hoptraf.dao.EventReportDao;
import tech.hirsun.hoptraf.pojo.EventReport;
import tech.hirsun.hoptraf.pojo.PageBean;
import tech.hirsun.hoptraf.service.EventReportService;

import java.util.List;

@Service
public class EventReportServiceImpl implements EventReportService {

    @Autowired
    private EventReportDao eventReportDao;

    @Override
    public PageBean page(Integer pageNum, Integer pageSize, String driverId, String keyword) {
        int count = eventReportDao.count(driverId, keyword);

        int start = (pageNum - 1) * pageSize;
        List<EventReport> list = eventReportDao.list(start, pageSize, driverId, keyword);
        return new PageBean(count, list, Math.floorDiv(count, pageSize) + 1, pageNum);
    }

    @Override
    public void removeAllReports() {
        eventReportDao.deleteAll();
    }

    @Override
    public void addReport(EventReport eventReport) {
        eventReportDao.insert(eventReport);
    }
}
