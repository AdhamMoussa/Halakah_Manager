package com.omarionapps.halaka.model;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Omar on 30-Apr-17.
 */
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinTable(name = "activity_teacher")
    @ManyToMany
    private Set<Activity> activity = new HashSet<>();
    @OneToMany(mappedBy = "teacher")
    private Set<Course> course = new HashSet<>();
    @OneToMany(mappedBy = "teacher")
    private Set<TeacherTrack> teacherTracks = new HashSet<>();
    @Column(name = "name", nullable = false)
    private String name;
    @Email
    private String email;
    private String tel;
    private String education;
    private String job;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "is_working", columnDefinition = "TINYINT")
    private boolean isWorking;
    private String comments;

    public Teacher(){}

    public Teacher(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Activity> getActivity() {
        return activity;
    }

    public void setActivity(Set<Activity> activity) {
        this.activity = activity;
    }

    public Set<Course> getCourse() {
        return course;
    }

    public void setCourse(Set<Course> course) {
        this.course = course;
    }

    public Set<TeacherTrack> getTeacherTracks() {
        return teacherTracks;
    }

    public void setTeacherTracks(Set<TeacherTrack> teacherTracks) {
        this.teacherTracks = teacherTracks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    @Override
    public String toString() {
        StringBuilder activities = new StringBuilder();
        for(Activity act : activity){
            activities.append(act.getName()).append(',');
        }
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", education='" + education + '\'' +
                ", comments='" + comments + '\'' +
                ", activities=[" + activities.toString() + "]}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;

        Teacher teacher = (Teacher) o;

        return getId() == teacher.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}