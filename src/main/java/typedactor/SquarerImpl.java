package typedactor;

import akka.dispatch.Futures;
import akka.japi.Option;
import scala.concurrent.Future;

public class SquarerImpl implements Squarer{

    @Override
    public Future<Integer> square(int i) {
        return Futures.successful(prepareData(i));
    }

    @Override
    public Option<Integer> squareNow(int i) {
        return Option.some(prepareData(i));
    }

    @Override
    public int prepareData(int num) {
        int sum = 0;
        for (int i = 1; i <= num; i++) {
            sum += i;
            try {
                Thread.sleep(1000);
                System.out.println("sleep 1s...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return sum;
    }
}
