package codes.karo.test.activities;

import android.graphics.Bitmap;

import java.util.List;

public interface PhotoMosaicContract {
    interface View {
        void startImagePicker();
        void displayImageProcessingStartedMessage();
        void displayImageProcessingEndedMessage();
        void displayImageProcessingErrorMessage();
        void clearImagePlaceholder();
        void addImageRow(List<Bitmap> tiles);
        int getImagePlaceholderWidth();
        int getImagePlaceholderHeight();
    }

    interface Presenter {
        void onSelectImageClicked();
        void onImageSelected(Bitmap image);
    }
}
