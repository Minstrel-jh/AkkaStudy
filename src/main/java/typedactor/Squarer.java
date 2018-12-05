package typedactor;

import akka.japi.Option;
import scala.concurrent.Future;

public interface Squarer {
    Future<Integer> square(int i); // non-blocking send-request-reply

    Option<Integer> squareNow(int i); // blocking send-request-reply

    int prepareData(int i); // blocking send-request-reply

}
