package Noice;

import Map.Utils.Vector;

class PermutationTable {
    private final static int INITIAL_SIZE = 10;
    private final static double EXPAND_INDEX = 1.5;
    private Vector[][] permutations;
    public PermutationTable(int initialSize) {
        this.permutations = new Vector[initialSize][initialSize];
        for (int i = 0; i < this.permutations.length; i++)
            for (int j = 0; j < this.permutations[i].length; j++)
                this.permutations[i][j] = Vector.getRandomVector();
    }
    public PermutationTable() {
        this(INITIAL_SIZE);
    }
    public Vector getPermutation(int x, int y) {
        if (isOutOfBound(x, y))
            expandArray(x, y);
        return this.permutations[y][x];
    }
    private boolean isOutOfBound(int x, int y) {
        return x < 0 || y < 0 || y >= this.permutations.length || x >= this.permutations[y].length;
    }
    private void expandArray(int width, int hight) {
        final int deltaWidth = width - this.permutations[0].length + 1;
        final int deltaHight = hight - this.permutations.length + 1;

        final int newHight = deltaHight <= 0 ? this.permutations.length : (int) ((this.permutations.length + deltaHight) * EXPAND_INDEX);
        final int newWidth = deltaWidth <= 0 ? this.permutations[0].length : (int) ((this.permutations[0].length + deltaWidth) * EXPAND_INDEX);
        final var newPermutations = new Vector[newHight][newWidth];

        for (int i = 0; i < this.permutations.length; i++)
            System.arraycopy(this.permutations[i], 0, newPermutations[i], 0, this.permutations[i].length);

        for (int i = 0; i < newHight; i++)
            for (int j = 0; j < newWidth; j++)
                newPermutations[i][j] = newPermutations[i][j] == null ? Vector.getRandomVector() : newPermutations[i][j];

        this.permutations = newPermutations;
    }


}
