import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class RobotTest {
    @Test
    void demoTestRobot() throws ExecutionException, InterruptedException {
        Robot r = new Robot("FF".toCharArray(), "Bob", false);
        OneRobotWalk o = new OneRobotWalk(r);
        assertArrayEquals(new int[]{0, 2}, o.getEndPoint());
    }

    @Test
    void demoMultipleRobotWalk() throws InterruptedException {
        Robot joe = new Robot("FRFLLFRF".toCharArray(), "Joe", true, 3);
        Robot bill = new Robot("FFFFFLF".toCharArray(), "Bill", true, 1);
        Robot jim = new Robot("LFRF".toCharArray(), "Jim", true, 2);
        List<Robot> arr = Arrays.asList(joe, bill, jim);
        MultipleRobotWalk m = new MultipleRobotWalk(arr);
        List<int[]> err = m.getEndPoints();
        err.forEach(e -> {
            System.out.println(Arrays.toString(e));
        });

        assertArrayEquals(new int[]{0, 2}, joe.coordinates);
        assertArrayEquals(new int[]{-1, 5}, bill.coordinates);
        assertArrayEquals(new int[]{-1, 1}, jim.coordinates);
    }
}
