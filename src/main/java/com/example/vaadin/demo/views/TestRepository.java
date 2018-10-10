package com.example.vaadin.demo.views;

import org.springframework.stereotype.Repository;
import sun.rmi.server.WeakClassHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TestRepository {

    private List<Test> list = new ArrayList<>();

    public void addTest(Test test){
        List<Test> byId = findById(test.getId());
        if(!byId.isEmpty()){
           list.remove(byId);
        }
        list.add(test);
    }

    public List<Test> getTests(){
        return list;
    }

    public void removeTest(Test test){
        list.remove(test);
    }


    public List<Test> findById(String id) {
        return list.stream().filter(t -> t.getId().equals(id)).collect(Collectors.toList());
    }

    public List<Test> findData1StartWith(String s){
        return list.stream().filter(t -> t.getData1().startsWith(s)).collect(Collectors.toList());
    }
}
