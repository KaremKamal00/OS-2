package diningphilosophers;

import java.util.concurrent.locks.ReentrantLock;

public class DiningServerImpl implements DiningServer {

    private static ReentrantLock LOCK;

    // different philosopher states
    enum State {
        THINKING, HUNGRY, EATING
    };

    // number of philosophers
    public static final int NUM_OF_PHILS = 5;

    // array to record each philosopher's state
    private volatile State[] state;

    public DiningServerImpl() {
        LOCK = new ReentrantLock();

        this.state = new State[NUM_OF_PHILS];

        // make intialization for all philosophers with thinking state
        for (int i = 0; i < NUM_OF_PHILS; i++) {
            this.state[i] = State.THINKING;
        }
    }

    // called by a philosopher when they wish to eat
    @Override
    public void takeForks(int pnum) {
        LOCK.lock();
        try {
            this.state[pnum] = State.HUNGRY;

            // checking to see if forks were available
            test(pnum);
            if (this.state[pnum] != State.EATING) {
                // wait
                SleepUtilities.nap();
            }

        } finally {
            LOCK.unlock();
        }
    }

    // called by a philosopher when they are finished eating
    @Override
    public void returnForks(int pnum) {

        LOCK.lock();
        try {
            this.state[pnum] = State.THINKING;

            // letting other philosphers know that forks are available for usage
            test((pnum + 1) % NUM_OF_PHILS);
            test((pnum + 4) % NUM_OF_PHILS);
        } finally {
            LOCK.unlock();
        }
    }

    // test if the right and left forks is available
    private void test(int pnum) {

        if ((this.state[pnum] == State.HUNGRY) &&
                (this.state[(pnum + 1) % NUM_OF_PHILS] != State.EATING) &&
                (this.state[(pnum + 4) % NUM_OF_PHILS] != State.EATING)) {
            this.state[pnum] = State.EATING;
        }
    }
}
