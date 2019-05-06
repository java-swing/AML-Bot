package com.thanhdc.GUI.Model;

import java.sql.Date;
import java.sql.Time;

public class Establish {
    private Date dateStart;
    private Date dateEnd;
    private Time timeStart;
    private Time timeEnd;
    private int intervalPeriod;
    private String saveFolderMap;

    public Establish(Date dateStart, Date dateEnd, Time timeStart,
                     Time timeEnd, int intervalPeriod, String saveFolderMap) {
        super();
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.intervalPeriod = intervalPeriod;
        this.saveFolderMap = saveFolderMap;
    }

    public Time getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Time timeStart) {
        this.timeStart = timeStart;
    }

    public Time getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Time timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getIntervalPeriod() {
        return intervalPeriod;
    }

    public void setIntervalPeriod(int intervalPeriod) {
        this.intervalPeriod = intervalPeriod;
    }

    public String getSaveFolderMap() {
        return saveFolderMap;
    }

    public void setSaveFolderMap(String saveFolderMap) {
        this.saveFolderMap = saveFolderMap;
    }

}
