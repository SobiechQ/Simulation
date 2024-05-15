import org.jooq.lambda.Seq;

import java.util.stream.IntStream;

public class Test {
    static void main(String... args) {
        Seq.crossJoin(IntStream.rangeClosed(-1, 1).boxed(), IntStream.rangeClosed(-1, 1).boxed())
                        .forEach(System.out::println);
        System.out.println(args);
    }
}