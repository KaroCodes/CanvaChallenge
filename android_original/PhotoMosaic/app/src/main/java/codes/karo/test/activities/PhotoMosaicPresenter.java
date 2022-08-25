package codes.karo.test.activities;

import android.graphics.Bitmap;

import codes.karo.test.Utils;
import codes.karo.test.models.Row;
import codes.karo.test.models.Tile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PhotoMosaicPresenter implements PhotoMosaicContract.Presenter {
    private static final String TILE_URL = "http://192.168.1.111:8765/color/%d/%d/%s";
    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    private PhotoMosaicContract.View _view;

    private Observer<List<Bitmap>> _photoMosaicObserver;
    private Subscription _subscription;
    private Scheduler _scheduler;
    private int _tileOnScreenSize;


    public PhotoMosaicPresenter(PhotoMosaicContract.View view) {
        _view = view;
        _subscription = null;
        _scheduler = Schedulers.from(Executors.newFixedThreadPool(AVAILABLE_PROCESSORS));
        _photoMosaicObserver = new Observer<List<Bitmap>>() {
            @Override
            public void onCompleted() {
                _view.displayImageProcessingEndedMessage();
            }

            @Override
            public void onError(Throwable e) {
                _view.displayImageProcessingErrorMessage();
            }

            @Override
            public void onNext(List<Bitmap> tiles) {
                _view.addImageRow(tiles);
            }
        };
    }

    @Override
    public void onSelectImageClicked() {
        _view.startImagePicker();
    }

    @Override
    public void onImageSelected(Bitmap image) {
        unsubscribe(); // cancel currently processed image (if there is one)
        _view.clearImagePlaceholder();
        _view.displayImageProcessingStartedMessage();
        _tileOnScreenSize = getTileOnScreenSize(image.getWidth(), image.getHeight());
        subscribe(image);
    }

    private void subscribe(Bitmap image) {
        _subscription = getPhotoMosaicCreator(image)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_photoMosaicObserver);
    }

    private void unsubscribe() {
        if (_subscription != null && !_subscription.isUnsubscribed()) {
            _subscription.unsubscribe();
        }
    }

    @SuppressWarnings("unchecked")
    private Observable<List<Bitmap>> getPhotoMosaicCreator(Bitmap image) {
        // rows are processed one by one
        // each tile in the currently processed row runs its own io thread
        return Observable
                .fromCallable(() -> getPixelRows(image))
                .subscribeOn(Schedulers.computation())
                .concatMapEager(pixelRows -> Observable
                        .from(pixelRows)
                        .concatMapEager(row -> Observable
                                .from(row.getTiles())
                                .concatMapEager(tile -> getAverageImageFromPixels(tile.getPixels()))
                        .toList()));
    }

    private Observable getAverageImageFromPixels(int[] pixels) {
        return Observable
                .just(pixels)
                .observeOn(_scheduler)
                .map(Utils::getAveragePixelsColor)
                .map(this::getTileUrl)
                .map(url -> {
                    try {
                        return Utils.getBitmapFromURL(url);
                    } catch (IOException e) {
                        return Observable.error(e);
                    }
                });
    }

    private int getTileOnScreenSize(int imageWidth, int imageHeight) {
        final int tilesInRowCount = (int) Math.ceil((float) imageWidth / (float) Tile.TILE_SIZE);
        final int tilesInColumn = (int) Math.ceil((float) imageHeight / (float) Tile.TILE_SIZE);

        final int maxTileWidth = (int) Math.floor(_view.getImagePlaceholderWidth() / tilesInRowCount);
        final int maxTileHeight = (int) Math.floor(_view.getImagePlaceholderHeight() / tilesInColumn);

        return Math.min(maxTileWidth, maxTileHeight);
    }

    private List<Row> getPixelRows(Bitmap image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        List<Row> rows = new ArrayList<>();

        for (int y = 0; y < height; y+= Tile.TILE_SIZE) {
            Row row = new Row();
            int tileHeight = Math.min(Tile.TILE_SIZE, height - y);

            for (int x = 0; x < width; x += Tile.TILE_SIZE) {
                int tileWidth = Math.min(Tile.TILE_SIZE, width - x);
                int length = tileHeight * tileWidth;
                int[] tilePixels = new int[length];

                Bitmap tileBitmap = Bitmap.createBitmap(image, x, y, tileWidth, tileHeight);
                tileBitmap.getPixels(tilePixels, 0, tileWidth, 0, 0, tileWidth, tileHeight);

                row.add(new Tile(tilePixels));
            }

            rows.add(row);
        }

        return rows;
    }

    private String getTileUrl(int color) {
        return String.format(Locale.ENGLISH, TILE_URL,
                _tileOnScreenSize, _tileOnScreenSize, Utils.colorIntToHex(color));
    }
}