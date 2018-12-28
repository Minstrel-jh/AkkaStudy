package trycode.lambda;

import java.io.PrintStream;
import java.util.HashMap;

/**
 * 针对java中lambda写法的一些测试
 */
public class Demo {

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.test();
    }

    HashMap<Integer, IDeal> deals = new HashMap<>();

    public Demo() {

        /*
          常规写法
         */
        IDeal realDeal = new RealDeal();
        deals.put(1, realDeal);

        /*
          匿名类写法
         */
        deals.put(2, new IDeal() {
            @Override
            public int fun(int i) {
                return i;
            }
        });

        /*
          lambda写法
         */
        deals.put(3, (int i) -> i);

        /*
          java8 ::
          可以使用一个形式一样但没有实现指定接口的方法
         */
        deals.put(4, OtherDeal::sfun);

        OtherDeal otherDeal = new OtherDeal();
        deals.put(5, otherDeal::fun);
    }

    private void test() {
        PrintStream out = System.out;

        for (int i = 1; i <= deals.size(); i++) {
            out.println(deals.get(i).fun(i));
        }
    }
}

interface IDeal {
    int fun(int i);
}

class RealDeal implements IDeal {

    @Override
    public int fun(int i) {
        return i;
    }
}

class OtherDeal {
    static int sfun(int i) {
        return i;
    }

    int fun(int i) {
        return i;
    }
}
