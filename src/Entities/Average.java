/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author mananmolhurt
 */
public class Average {
    
    private long code;
    private Level level;
    private int year_begin;
    private int year_end;
    private int month;
    private double percent;
    
    public Average() {
        
    }
    
    public Average(long code, Level level, int year_begin, int year_end, int month, double percent) {
        
        this.code = code;
        this.level = level;
        this.year_begin = year_begin;
        this.year_end = year_end;
        this.month = month;
        this.percent = percent;
    }

    /**
     * @return the code
     */
    public long getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(long code) {
        this.code = code;
    }

    /**
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * @return the year_begin
     */
    public int getYearBegin() {
        return year_begin;
    }

    /**
     * @param year_begin the year_begin to set
     */
    public void setYearBegin(int year_begin) {
        this.year_begin = year_begin;
    }

    /**
     * @return the year_end
     */
    public int getYearEnd() {
        return year_end;
    }

    /**
     * @param year_end the year_end to set
     */
    public void setYearEnd(int year_end) {
        this.year_end = year_end;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @return the percent
     */
    public double getPercent() {
        return percent;
    }

    /**
     * @param percent the percent to set
     */
    public void setPercent(double percent) {
        this.percent = percent;
    }
}
