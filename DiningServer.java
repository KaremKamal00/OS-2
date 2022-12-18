package diningphilosophers;

public interface DiningServer {
    // called by a philosopher when he wishies to eat
    public void takeForks(int philNumber);

    // called by a philosopher when he is finished eatign
    public void returnForks(int philNumber);
}
