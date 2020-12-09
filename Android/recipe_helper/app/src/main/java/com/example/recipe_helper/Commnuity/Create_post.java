package com.example.recipe_helper.Commnuity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_helper.Commnuity.Adapter.AddImageAdapter;
import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.HttpConnection.BaseResponse;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class Create_post extends Fragment implements AddImageAdapter.OnListItemSelectedInterface {

    private EditText tv_title;
    private EditText tv_content;
    private static final String TAG = "RHC";
    private InputMethodManager imm;
    private UserInfo user;
    private ImageButton imageAdd;

    private ArrayList<Bitmap> list = new ArrayList<>();
    private AddImageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_writing, container, false);
        view.setClickable(true);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setNavigationVisible(false);

        imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);

        user = ((MainActivity) getActivity()).user;

        tv_title = view.findViewById(R.id.post_enter_title);
        tv_content = view.findViewById(R.id.post_enter_content);
        imageAdd = view.findViewById(R.id.add_image);

        tv_title.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(tv_content.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(tv_title.getWindowToken(), 0);
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        list.clear();
        adapter = new AddImageAdapter(getContext(), list, this);
        adapter.setHasStableIds(true);
        RecyclerView recyclerView = view.findViewById(R.id.rv_image);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void uploadPost2(long id, String content, String title) {

        RequestBody IDBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id));
        RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"), content);
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody imageBody;
        ArrayList<MultipartBody.Part> images = new ArrayList<>();

        int idx = 0;
        for (Bitmap bitmap : list) {
            imageBody = RequestBody.create(MediaType.parse("text/plain"), getStringFromBitmap(bitmap));
            images.add(MultipartBody.Part.createFormData("images", String.valueOf(idx), imageBody));
            idx++;
        }

        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<BaseResponse> call = service.uploadPost2(images, IDBody, titleBody, contentBody);

        call.enqueue(new retrofit2.Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse result = response.body();
                    if (result.checkError(getContext()) != 0) {
                        Log.d(TAG, "ERROR");
                    }
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(getContext(), "연결실패", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.write_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        boolean status_1;
        boolean status_2;

        switch (item.getItemId()) {
            case android.R.id.home:
                //select back button
                getActivity().onBackPressed();
                return true;

            case R.id.item_post_check:
                String title = tv_title.getText().toString();
                String content = tv_content.getText().toString();

                status_1 = content.isEmpty();
                status_2 = title.isEmpty();

                if (status_1) {
                    tv_content.setError("내용을 입력해 주세요");
                }

                if (status_2) {
                    tv_title.setError("제목을 입력해 주세요");
                }

                if (!(status_1 || status_2)) {
                    uploadPost2(user.userID, content, title);

                    imm.hideSoftInputFromWindow(tv_content.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(tv_title.getWindowToken(), 0);
                    ((MainActivity) getActivity()).setNavigationVisible(true);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1 || resultCode != RESULT_OK) {
            return;
        }
        Uri dataUri = data.getData();
        String imgPath = getPath(getActivity().getApplicationContext(), dataUri);
        if (dataUri != null && imgPath != null) {
            try {
                InputStream in = getActivity().getContentResolver().openInputStream(dataUri);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                Bitmap image = BitmapFactory.decodeStream(in, null, options);
                in.close();
                //crop image
                if (image.getWidth() >= image.getHeight()) {
                    image = Bitmap.createBitmap(
                            image,
                            image.getWidth() / 2 - image.getHeight() / 2,
                            0,
                            image.getHeight(),
                            image.getHeight()
                    );
                } else {
                    image = Bitmap.createBitmap(
                            image,
                            0,
                            image.getHeight() / 2 - image.getWidth() / 2,
                            image.getWidth(),
                            image.getWidth()
                    );
                }

                image = Bitmap.createScaledBitmap(image, 480, 480, true);

                list.add(image);
                adapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    Toast.makeText(context, "Could not get file path. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    contentUri = MediaStore.Files.getContentUri("external");
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Override
    public void onItemSelected(View v, int position) {
        list.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imm.hideSoftInputFromWindow(tv_content.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(tv_title.getWindowToken(), 0);
        ((MainActivity) getActivity()).setNavigationVisible(true);
    }
}
