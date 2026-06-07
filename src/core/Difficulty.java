package core;

public enum Difficulty {
    EASY(1,4,15, 100),
    NORMAL(2,6,20, 200),
    HARD(3,8,25, 400);

    public final int enemySpeed;
    public final int bulletSpeed;
    public final int totalEnemies;
    public final int scorePerKill;

    Difficulty(int enemySpeed, int bulletSpeed, int totalEnemies, int scorePerKill){
        this.enemySpeed = enemySpeed;
        this.bulletSpeed = bulletSpeed;
        this.totalEnemies = totalEnemies;
        this.scorePerKill = scorePerKill;
    }
}
