package codes.karo.test.models;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private List<Tile> _tiles;

    public Row() {
        _tiles = new ArrayList<>();
    }

    public void add(Tile tile) {
        _tiles.add(tile);
    }

    public List<Tile> getTiles() {
        return _tiles;
    }
}
