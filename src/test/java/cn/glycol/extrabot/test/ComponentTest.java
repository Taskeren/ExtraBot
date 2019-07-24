package cn.glycol.extrabot.test;


import cn.glycol.extrabot.component.Component;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ComponentTest {
    @Test
    public void parse(){
        System.out.println(Component.parseComponent("[CQ: , ]"));
        System.out.println(Component.parseComponent("Not CQ Code"));
        System.out.println(Component.parseComponent("[CQ:face,id=14]"));
        System.out.println(Component.parseComponent(" [CQ:image,file=1.jpg] "));
    }
    @Test
    public void parseMulti(){
        List<Component> components=Component.parseComponents(new ArrayList<>(Arrays.asList("before[CQ:face,id=14]after")));
        for (Component component:components){
            System.out.println(component);
        }
        System.out.println("///////////////");
        components=Component.parseComponents(new ArrayList<>(Arrays.asList("together as one!")));
        for (Component component:components){
            System.out.println(component);
        }
        System.out.println("///////////////");
        components=Component.parseComponents(new ArrayList<>(Arrays.asList(
                "[CQ:face,id=14]Before a component[CQ:face,id=14]After a component/At the end",
                "This String is in the array",
                "[CQ:face,id=14]"
        )));
        for (Component component:components){
            System.out.println(component);
        }
    }
    @Test
    public void speedTest(){
        String regex_new = "(\\[CQ:.*?,.*?\\])";
        Pattern pattern_new=Pattern.compile(regex_new);
        Pattern pattern_old=Pattern.compile("(\\[).*?(\\])");

        String s = "[CQ:face,id=14]";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            pattern_old.matcher(s);
            isCQCode(s);
        }
        System.out.println("old method:" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            pattern_new.matcher(s);
        }
        System.out.println("new method:" + (System.currentTimeMillis() - start));
    }

    private boolean isCQCode(String message){
        message = message.trim();
        return message.startsWith("[CQ:") && message.endsWith("]");
    }
}
