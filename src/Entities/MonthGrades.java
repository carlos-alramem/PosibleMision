/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.List;

/**
 *
 * @author mananmolhurt
 */
public class MonthGrades {
    
    private List<Activity> activities;
    private List<Average> averages;
    private List<Student> students;
    private List<Grade> grades;
    
    public MonthGrades() {
        
    }
    
    public MonthGrades(List<Activity> activities) {
        
        this.activities = activities;
    }
    
    public MonthGrades(List<Activity> activities, List<Student> students) {
        
        this.activities = activities;
        this.students = students;
    }
    
    public MonthGrades(List<Activity> activities, List<Student> students, List<Grade> grades) {
        
        this.activities = activities;
        this.students = students;
        this.grades = grades;
    }
}
