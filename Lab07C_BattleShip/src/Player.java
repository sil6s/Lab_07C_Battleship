public class Player {
    private int totalHits;
    private int totalMisses;

    public Player() {
        totalHits = 0;
        totalMisses = 0;
    }

    public void recordHit() { totalHits++; }

    public void recordMiss() { totalMisses++; }

    public int getTotalHits() { return totalHits; }

    public int getTotalMisses() { return totalMisses; }
}
