package sample.hello2;

import java.util.Collections;
import java.util.List;

public final class Message {
    private final int age;
    private final List<String> list;

    Message(int age, List<String> list) {
        this.age = age;
        this.list = Collections.unmodifiableList(list);
    }

    public int getAge() {
        return age;
    }

    public List<String> getList() {
        return list;
    }
}
