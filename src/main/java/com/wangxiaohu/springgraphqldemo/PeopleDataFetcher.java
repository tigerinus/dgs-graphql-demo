package com.wangxiaohu.springgraphqldemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.wangxiaohu.springgraphqldemo.generated.types.Person;

@DgsComponent
public class PeopleDataFetcher {

    private final Map<String, Person> people;

    public PeopleDataFetcher() {
        this.people = new HashMap<>();
    }

    @DgsQuery
    public List<Person> getPeople() {
        return new ArrayList<>(people.values());
    }

    @DgsMutation
    public Person createPerson(String name) {
        Integer id = people.keySet().stream().map(Integer::parseInt).max(Integer::compare).orElse(0) + 1;

        Person person = new Person(id.toString(), name);
        people.put(id.toString(), person);
        return person;
    }
}
