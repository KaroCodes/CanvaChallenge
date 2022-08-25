package codes.karo.test.models;

public class Tile {
    public static int TILE_SIZE = 32;

    private int[] _pixels;

    public Tile(int[] pixels) {
        _pixels = pixels;
    }

    public int[] getPixels() {
        return _pixels;
    }
}
