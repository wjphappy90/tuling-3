package com.tuling.config.indb;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by smlz on 2020/3/9.
 */
public class TestClass {

    public static void main(String[] args) throws IOException {

        Person person = new Person(1,"张三");
/*
        //jdk序列化
        JdkSerializationRedisSerializer jdk = new JdkSerializationRedisSerializer();
        byte[] personBytes = jdk.serialize(person);

        //反序列化
        Person person1 = (Person) jdk.deserialize(personBytes);*/

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Person.class);
        byte[] personBytes = jackson2JsonRedisSerializer.serialize(person);
        Person person1 = (Person) jackson2JsonRedisSerializer.deserialize(personBytes);
        System.out.println(person1);

    }
}

class Person implements Serializable{

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", sex='" + sex + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    private int age;

    private final String sex;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {

        return age;
    }

    public void setAge(int age) {
        this.age = age;

    }

    public Person() {
        this.sex = null;
        System.out.println("无参构造器");
    }

    public Person(int age ,String name) {
        this.age = age;
        this.name = name;
        this.sex = "aaaaa";
        System.out.println("有参构造器");
    }

}
