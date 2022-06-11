package com.wangxiaohu.springgraphqldemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.reactivestreams.Publisher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsSubscription;
import com.wangxiaohu.springgraphqldemo.generated.types.Person;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@DgsComponent
public class PeopleDataFetcher {

    private final Map<String, Person> _people;

    private FluxSink<Person> _sink;

    public PeopleDataFetcher() {
        _people = new HashMap<>();
    }

    @DgsQuery
    public List<Person> getPeople() {
        return new ArrayList<>(_people.values());
    }

    @DgsMutation
    public Person createPerson(String name) {
        Integer id = _people.keySet().stream().map(Integer::parseInt).max(Integer::compare).orElse(0) + 1;

        Person person = new Person(id.toString(), name);
        _people.put(id.toString(), person);
        if (_sink != null) {
            _sink.next(person);
        }
        return person;
    }

    @DgsSubscription
    public Publisher<Person> personCreated() {
        return Flux.create(s -> {
            _sink = s;

            s.onDispose(() -> {
                _sink = null;
            });
        });
    }
}
