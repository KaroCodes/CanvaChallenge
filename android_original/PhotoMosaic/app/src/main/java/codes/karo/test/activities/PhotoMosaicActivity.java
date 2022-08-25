package codes.karo.test.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import codes.karo.test.R;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoMosaicActivity extends AppCompatActivity implements PhotoMosaicContract.View {
    private static final String IMAGE_TYPE = "image/*";
    private static final int PICK_IMAGE_REQUEST_CODE = 1;

    @BindView(R.id.image_placeholder) LinearLayout _imagePlaceholder;
    @BindView(R.id.status_message) TextView _statusMessage;

    private PhotoMosaicContract.Presenter _presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_mosaic_activity);
        ButterKnife.bind(this);

        _presenter = new PhotoMosaicPresenter(this);
    }

    @OnClick(R.id.select_image_button)
    public void selectImage() {
        _presenter.onSelectImageClicked();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST_CODE && data != null) {
            try {
                Uri uri = data.getData();
                Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                _presenter.onImageSelected(image);
            } catch (IOException e) {
                displayImageRetrievingError();
            }
        }
    }

    @Override
    public void startImagePicker() {
        Intent intent = new Intent();
        intent.setType(IMAGE_TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(
                intent, getString(R.string.select_picture_text)), PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    public void displayImageProcessingStartedMessage() {
        _statusMessage.setText(getString(R.string.image_processing_message));
        _statusMessage.setBackgroundColor(
                ResourcesCompat.getColor(getResources(), R.color.progressMessage, null));

    }

    @Override
    public void displayImageProcessingEndedMessage() {
        _statusMessage.setText(getString(R.string.image_processing_completed_message));
        _statusMessage.setBackgroundColor(
                ResourcesCompat.getColor(getResources(), R.color.completedMessage, null));
    }

    @Override
    public void displayImageProcessingErrorMessage() {
        _statusMessage.setText(getString(R.string.image_processing_error_message));
        _statusMessage.setBackgroundColor(
                ResourcesCompat.getColor(getResources(), R.color.errorMessage, null));
    }

    @Override
    public void clearImagePlaceholder() {
        _imagePlaceholder.removeAllViews();
    }

    @Override
    public void addImageRow(List<Bitmap> tiles) {
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);

        for (Bitmap tile : tiles) {
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(tile);

            row.addView(imageView);
        }

        _imagePlaceholder.addView(row);
    }

    @Override
    public int getImagePlaceholderWidth() {
        return _imagePlaceholder.getWidth();
    }

    @Override
    public int getImagePlaceholderHeight() {
        return _imagePlaceholder.getHeight();
    }

    private void displayImageRetrievingError() {
        _statusMessage.setText(getString(R.string.select_image_error_message));
        _statusMessage.setBackgroundColor(
                ResourcesCompat.getColor(getResources(), R.color.errorMessage, null));
    }
}