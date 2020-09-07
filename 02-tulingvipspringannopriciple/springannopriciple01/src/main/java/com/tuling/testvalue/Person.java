package com.tuling.testvalue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class Person {

    @Value("司马")
    private String firstName;

    @Value("${person.isBoss}")
    private boolean isBoss;

    public void setBoss(boolean boss) {
        isBoss = boss;
    }

    @Value("${person.age}")
    private Integer age;

    @Value("${person.lastName}")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", isBoss=" + isBoss +
                ", age=" + age +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}