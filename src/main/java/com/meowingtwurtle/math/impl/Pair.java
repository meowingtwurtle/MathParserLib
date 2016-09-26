package com.meowingtwurtle.math.impl;

public class Pair<R, S> {
    final R itemA;
    final S itemB;

    public Pair(R itemA, S itemB) {
        this.itemA = itemA;
        this.itemB = itemB;
    }

    public R getItemA() {
        return itemA;
    }

    public S getItemB() {
        return itemB;
    }
}
