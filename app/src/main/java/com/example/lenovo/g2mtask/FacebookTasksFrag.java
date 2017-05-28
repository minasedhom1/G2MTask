package com.example.lenovo.g2mtask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


//LoginPage
public class FacebookTasksFrag extends Fragment {
    LoginButton loginButton;
    CallbackManager callbackManager;
    ImageView prof,share_image_view;
    Button select_img_btn,share_img_btn;
    ShareDialog shareDialog;
    Bitmap bitmap_to_share;
    TextView userName;
    public FacebookTasksFrag() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog=new ShareDialog(getActivity());
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getContext(),"Shared succeed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(),"Sharing cancelled",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(),"Sharing error",Toast.LENGTH_SHORT).show();

            } });
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_facebook_tasks, container, false);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        List<String> permissionNeeds = Arrays.asList("publish_actions"); //permession for sharing
        LoginManager.getInstance().logInWithPublishPermissions( getActivity(),permissionNeeds); //*****pass permession to LoginManager
        loginButton.setReadPermissions(Arrays.asList("public_profile"));
        // If using in a fragment
        loginButton.setFragment(this);
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("UserID", AccessToken.getCurrentAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
               Log.d("UserID","Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("UserID","Error");
            }

        });
        prof= (ImageView) view.findViewById(R.id.prof_img_view);
        userName= (TextView) view.findViewById(R.id.user_name_tv);

/*----------------------------------------------------------Tracks profile changes-----------------------------------------------------------------------*/
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            userName.setText(profile.getName());
            Picasso.with(getContext()).load(profile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(prof);
        } else {
            prof.setImageResource(R.mipmap.ic_launcher);
            userName.setText("");
        }
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    userName.setText(currentProfile.getName());
                    Picasso.with(getContext()).load(currentProfile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(prof);
                } else {
                    prof.setImageResource(R.mipmap.ic_launcher);
                    userName.setText("");
                }
            }
        };
        profileTracker.startTracking();

        share_image_view= (ImageView) view.findViewById(R.id.share_image_view);

        select_img_btn= (Button) view.findViewById(R.id.select_img_btn);
        select_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);

            }
        });

        share_img_btn= (Button) view.findViewById(R.id.share_img_btn);
        share_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( bitmap_to_share!=null) {
                    SharePhoto sharePhoto=new SharePhoto.Builder()
                        .setBitmap(bitmap_to_share)
                        .build();
                         SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto).build();

                    shareDialog.show(getActivity(),content);
            }
            else Toast.makeText(getContext(),"Please selet image first!",Toast.LENGTH_SHORT);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && resultCode == RESULT_OK && data.getData() != null) {

            Uri uri = data.getData();

            try {
               bitmap_to_share= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
               share_image_view.setImageBitmap(bitmap_to_share);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }
    }

