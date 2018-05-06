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
public class Activity {
    
    private long code;
    private Average avg;
    private ActivityName name;
    private TeacherPerSubject teacher; 
    private boolean active;
    
    public Activity() {
        
    }
    
    public Activity(long code, Average avg, ActivityName name, TeacherPerSubject teacher, boolean active) {
        
        this.code = code;
        this.avg = avg;
        this.name = name;
        this.teacher = teacher;
        this.active = active;
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
     * @return the avg
     */
    public Average getAverage() {
        return avg;
    }

    /**
     * @param avg the avg to set
     */
    public void setAverage(Average avg) {
        this.avg = avg;
    }

    /**
     * @return the name
     */
    public ActivityName getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(ActivityName name) {
        this.name = name;
    }

    /**
     * @return the teacher
     */
    public TeacherPerSubject getTeacher() {
        return teacher;
    }

    /**
     * @param teacher the teacher to set
     */
    public void setTeacher(TeacherPerSubject teacher) {
        this.teacher = teacher;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
