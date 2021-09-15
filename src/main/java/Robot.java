import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public Direction left() {
        return values()[(this.ordinal() - 1 + values().length) % values().length];
    }

    public Direction right() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}

class Robot implements Callable<int[]> {
    int[] coordinates;
    char[] instructions;
    String name;
    Direction d;
    int time;
    boolean needRest;
    int restInterval;

    public Robot(char[] instructions, String name, boolean needRest, int restInterval) {
        this.instructions = instructions;
        this.name = name;
        this.needRest = needRest;
        this.restInterval = restInterval;

        this.coordinates = new int[2];
        d = Direction.NORTH;
        time = 1;
    }

    public Robot(char[] instructions, String name, boolean needRest) {
        this(instructions, name, needRest, 0);
    }

    void readInstruction(char c) throws InterruptedException {
        switch (c) {
            case 'F':
                move();
                System.out.printf("%ds %s : Moves F \n", time, name);
                break;
            case 'L':
                d = d.left();
                System.out.printf("%ds %s : Turns L \n", time, name);
                break;
            case 'R':
                d = d.right();
                System.out.printf("%ds %s : Turns R \n", time, name);
                break;
            default:
        }

        if (needRest) {
            Thread.sleep(restInterval * 100L);
        }
        time += restInterval;
    }

    void move() {
        switch (d) {
            case NORTH:
                coordinates[1]++;
                break;
            case EAST:
                coordinates[0]++;
                break;
            case SOUTH:
                coordinates[1]--;
                break;
            case WEST:
                coordinates[0]--;
                break;
        }
    }

    @Override
    public int[] call() throws Exception {
        assert coordinates[0] == 0;
        assert coordinates[1] == 0;

        for (char c : instructions) {
            readInstruction(c);
        }
        return coordinates;
    }
}

class OneRobotWalk {
    Robot r;

    public OneRobotWalk(Robot r) {
        this.r = r;
    }

    public int[] getEndPoint() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Callable<int[]> task = r;
        Future<int[]> future = executor.submit(task);
        return future.get();
    }
}

class MultipleRobotWalk {
    List<Robot> arr;

    public MultipleRobotWalk(List<Robot> arr) {
        this.arr = arr;
    }

    public List<int[]> getEndPoints() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<int[]>> futures = executor.invokeAll(arr);
        return futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return new int[2];
                })
                .collect(Collectors.toList());
    }
}
