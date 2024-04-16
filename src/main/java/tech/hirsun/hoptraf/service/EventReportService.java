package tech.hirsun.hoptraf.service;

import tech.hirsun.hoptraf.pojo.EventReport;
import tech.hirsun.hoptraf.pojo.PageBean;

public interface EventReportService {

    public PageBean page(Integer pageNum, Integer pageSize, String driverId, String keyword);

    public void removeAllReports();

    public void addReport(EventReport eventReport);
}
