package com.menor.easyfacebookconnect.samples;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.facebook.FacebookRequestError;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.menor.easyfacebookconnect.EasyActionListener;
import com.menor.easyfacebookconnect.EasyLoginListener;
import com.menor.easyfacebookconnect.ui.EasyFacebookFragmentActivity;

public class HomeActivity extends EasyFacebookFragmentActivity implements OnClickListener {

    private LinearLayout containerView;
    /**
     * Connect features
     */
    private Button facebookView;
    /**
     * Wall features
     */
    private TextView userView;
    private EditText statusWallView;
    private EditText statusPageView;

    private String link = "https://github.com/m3n0R/EasyFacebookConnect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setViews();
        checkStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStatus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.facebook_connect:
                connect();
                break;
            case R.id.facebook_wall_image_upload:
                uploadPhotoToWall();
                break;
            case R.id.facebook_wall_status_upload:
                uploadStatusToWall();
                break;
            case R.id.facebook_page_image_share:
                uploadImageToPage();
                break;
            case R.id.facebook_page_status_share:
                uploadStatusToPage();
                break;
            case R.id.facebook_page_url_image_share:
                uploadImageUrlToPage();
                break;
        }
    }

    private void setViews() {
        containerView = (LinearLayout) findViewById(R.id.session_container);
        /**
         * Connect features
         */
        facebookView = (Button) findViewById(R.id.facebook_connect);
        facebookView.setOnClickListener(this);
        userView = (TextView) findViewById(R.id.facebook_info);
        /**
         * Wall features
         */
        findViewById(R.id.facebook_wall_status_upload).setOnClickListener(this);
        findViewById(R.id.facebook_wall_image_upload).setOnClickListener(this);
        statusWallView = (EditText) findViewById(R.id.facebook_wall_status_edit);
        /**
         * Page features
         */
        findViewById(R.id.facebook_page_image_share).setOnClickListener(this);
        findViewById(R.id.facebook_page_status_share).setOnClickListener(this);
        findViewById(R.id.facebook_page_url_image_share).setOnClickListener(this);
        statusPageView = (EditText) findViewById(R.id.facebook_page_status_edit);
    }

    private void checkStatus() {
        if (isConnected()) {
            updateLoginUi();
        } else {
            udateLogoutUi();
        }
    }

    private void updateLoginUi() {
        userView.setText("Name: " + getUserFirstName() + "\n"
            + "Surname: " + getUserLastName() + "\n"
            + "Birth: " + getUserBirth() + "\n"
            + "Id: " + getUserId() + "\n"
            + "Username: " + getUserName() + "\n"
            + "City: " + getUserCity() + "\n"
            + "Email: " + getUserEmail() + "\n"
            + "Gender: " + getUserGender() + "\n"
            + "accessToken: " + getAccessToken() + "\n"
            + "appId: " + getApplicationId() + "\n");
        facebookView.setText(getString(R.string.logout));
        containerView.setVisibility(View.VISIBLE);
    }

    private void udateLogoutUi() {
        facebookView.setText(getString(R.string.login));
        containerView.setVisibility(View.GONE);
    }

    private void connect() {
        if (isConnected()) {
            disconnect();
            udateLogoutUi();
        } else {
            connect(new EasyLoginListener() {

                ProgressDialog progressDialog;

                @Override
                public void onStart() {
                    super.onStart();
                    progressDialog = new ProgressDialog(HomeActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage(getString(R.string.logging));
                    progressDialog.show();
                }

                @Override
                public void onSuccess(Response response) {
                    super.onSuccess(response);
                    Toast.makeText(HomeActivity.this, getString(R.string.login_successfully), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookRequestError error) {
                    super.onError(error);
                    Toast.makeText(HomeActivity.this, error.getErrorMessage(), Toast.LENGTH_LONG).show();
                }


                @Override
                public void onClosedLoginFailed(Session session, SessionState state, Exception exception) {
                    super.onClosedLoginFailed(session, state, exception);
                    Toast.makeText(HomeActivity.this, state.toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCreated(Session session, SessionState state, Exception exception) {
                    super.onCreated(session, state, exception);
                    Toast.makeText(HomeActivity.this, state.toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onClosed(Session session, SessionState state, Exception exception) {
                    super.onClosed(session, state, exception);
                    Toast.makeText(HomeActivity.this, state.toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    progressDialog.dismiss();
                    updateLoginUi();
                }

            });
        }
    }

    private void uploadPhotoToWall() {
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        postPhotoToWall(image, "Image description", new EasyActionListener() {

            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getString(R.string.uploading_image_wall));
                progressDialog.show();
            }

            @Override
            public void onSuccess(Response response) {
                super.onSuccess(response);
                Toast.makeText(HomeActivity.this, getString(R.string.upload_image_wall_success), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookRequestError error, Exception exception) {
                super.onError(error, exception);
                if (error != null) {
                    Toast.makeText(HomeActivity.this, error.getErrorMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HomeActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
                updateLoginUi();
            }
        });

    }

    private void uploadStatusToWall() {
        String status = statusWallView.getText().toString();
        postStatusToWall(status, new EasyActionListener() {

            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getString(R.string.uploading_status_wall));
                progressDialog.show();
            }

            @Override
            public void onSuccess(Response response) {
                super.onSuccess(response);
                Toast.makeText(HomeActivity.this, getString(R.string.upload_status_wall_success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookRequestError error, Exception exception) {
                super.onError(error, exception);
                if (error != null) {
                    Toast.makeText(HomeActivity.this, error.getErrorMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HomeActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
                updateLoginUi();
            }
        });
    }

    private void uploadStatusToPage() {
        String status = statusPageView.getText().toString();
        shareStatusToPage("me/feed", status, new EasyActionListener() {

            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getString(R.string.uploading_status_page));
                progressDialog.show();
            }

            @Override
            public void onSuccess(Response response) {
                super.onSuccess(response);
                Toast.makeText(HomeActivity.this, getString(R.string.upload_image_status_page_success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookRequestError error, Exception exception) {
                super.onError(error, exception);
                if (error != null) {
                    Toast.makeText(HomeActivity.this, error.getErrorMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HomeActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
                updateLoginUi();
            }

        });

    }

    private void uploadImageToPage() {
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        shareImageToPage("me/photos", image, "Image description", new EasyActionListener() {

            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getString(R.string.uploading_image_page));
                progressDialog.show();
            }

            @Override
            public void onSuccess(Response response) {
                super.onSuccess(response);
                Toast.makeText(HomeActivity.this, getString(R.string.upload_image_page_success), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookRequestError error, Exception exception) {
                super.onError(error, exception);
                if (error != null) {
                    Toast.makeText(HomeActivity.this, error.getErrorMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HomeActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
                updateLoginUi();
            }

        });
    }

    private void uploadImageUrlToPage() {
        String imageUrl = "http://sd.keepcalm-o-matic.co.uk/i/keep-calm-and-enjoy-android.png";
        shareImageUrlToPage("me/feed", imageUrl, link, "Image title", new EasyActionListener() {

            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getString(R.string.uploading_url_image_page));
                progressDialog.show();
            }

            @Override
            public void onSuccess(Response response) {
                super.onSuccess(response);
                Toast.makeText(HomeActivity.this, getString(R.string.upload_url_image_page_success), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookRequestError error, Exception exception) {
                super.onError(error, exception);
                if (error != null) {
                    Toast.makeText(HomeActivity.this, error.getErrorMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HomeActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
                updateLoginUi();
            }

        });


    }


}
