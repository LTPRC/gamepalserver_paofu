package com.github.ltprc.gamepal.model;

public class ClassicalPosition {

    protected int sceneNo;
    protected int x;
    protected int y;

    public ClassicalPosition() {
    }

    public ClassicalPosition(int sceneNo, int x, int y) {
        super();
        this.sceneNo = sceneNo;
        this.x = x;
        this.y = y;
    }

    public int getSceneNo() {
        return sceneNo;
    }

    public void setSceneNo(int sceneNo) {
        this.sceneNo = sceneNo;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
