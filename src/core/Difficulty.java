package core;

public enum Difficulty {
    EASY(1,4,15),
    NORMAL(2,6,20),
    HARD(3,8,25);

    public final int enemySpeed;
    public final int bulletSpeed;
    public final int totalEnemies;

    Difficulty(int enemySpeed, int bulletSpeed, int totalEnemies){
        this.enemySpeed = enemySpeed;
        this.bulletSpeed = bulletSpeed;
        this.totalEnemies = totalEnemies;
    }
}
