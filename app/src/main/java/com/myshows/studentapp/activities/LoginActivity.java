package com.myshows.studentapp.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.myshows.studentapp.BuildConfig;
import com.myshows.studentapp.R;
import com.myshows.studentapp.model.eventbus.LoginMsg;
import com.myshows.studentapp.model.eventbus.Message;
import com.myshows.studentapp.rest.RestClient;
import com.myshows.studentapp.rest.services.LoginService;
import com.myshows.studentapp.utils.Connectivity;
import com.myshows.studentapp.utils.EmailValidator;
import com.myshows.studentapp.utils.Utils;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class LoginActivity extends AppCompatActivity {

    private static final String BUNDLE_IS_LOADING = "BUNDLE_IS_LOADING";

    private boolean isLoading;
    private ProgressDialog dialog;

    private CallbackManager fbCallbackManager;

    @Bind(R.id.email)
    AutoCompleteTextView emailView;

    @Bind(R.id.password)
    EditText passwordView;

    @Bind(R.id.version_name)
    TextView versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        populateAutoComplete();
        setUpPasswordView();
        registerFbCallbackManager();
        updateValuesFromBundle(savedInstanceState);

        versionName.setText(BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onActivityResult(int request, int result, final Intent data) {
        if (fbCallbackManager.onActivityResult(request, result, data))
            return;
        if (VKSdk.onActivityResult(request, result, data, getVkCallback()))
            return;
        super.onActivityResult(request, result, data);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(LoginMsg msg) {
        hideLoadingDialog();
        if (msg.getMessage() == Message.OK) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Utils.makeSnackBar(
                    this,
                    msg.isSocial ? R.string.social_auth_error : R.string.auth_error,
                    Snackbar.LENGTH_SHORT
            );
        }
    }


    @MainThread
    private void showLoadingDialog() {
        dialog = ProgressDialog.show(this, "", getString(R.string.loading_msg), true);
        isLoading = true;
    }

    @MainThread
    private void hideLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
            isLoading = false;
            dialog = null;
        }
    }

    @MainThread
    private void populateAutoComplete() {
        Account[] accounts = AccountManager.get(this).getAccounts();
        if (accounts.length > 0) {
            ArrayList<String> emails = new ArrayList<>();
            for (Account account : accounts) {
                if (EmailValidator.isValid(account.name)) {
                    emails.add(account.name);
                }
            }

            if (emails.isEmpty()) return;

            emailView.setText(emails.get(0));

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, emails
            );
            emailView.setAdapter(adapter);
        }
    }

    @MainThread
    private void setUpPasswordView() {
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin(null);
                    return true;
                }
                return false;
            }
        });
    }

    private void registerFbCallbackManager() {
        fbCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(fbCallbackManager, getFacebookCallback());
    }

    @MainThread
    public void attemptLogin(@Nullable View view) {
        ((TextInputLayout) emailView.getParent()).setError(null);
        ((TextInputLayout) passwordView.getParent()).setError(null);

        String email = emailView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();

        boolean error = false;
        View focusView = null;

        if (password.isEmpty()) {
            ((TextInputLayout) passwordView.getParent())
                    .setError(getString(R.string.error_field_required));
            focusView = passwordView;
            error = true;
        }

        if (email.isEmpty()) {
            ((TextInputLayout) emailView.getParent())
                    .setError(getString(R.string.error_field_required));
            focusView = emailView;
            error = true;
        } else if (!EmailValidator.isValid(email)) {
            ((TextInputLayout) emailView.getParent())
                    .setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            error = true;
        }

        if (!Connectivity.isConnected(this)) {
            Utils.makeSnackBar(this, R.string.no_internet_connection, Snackbar.LENGTH_SHORT);
            return;
        }

        if (error) focusView.requestFocus();
        else {
            showLoadingDialog();
            RestClient.getInstance().login(email, Utils.toMD5(password, null));
        }
    }

    @MainThread
    public void socialBtnOnClick(View view) {
        switch (view.getId()) {
            case R.id.vk_btn:
                VKSdk.login(this);
                break;
            case R.id.fb_btn:
                if (fbCallbackManager == null) {
                    fbCallbackManager = CallbackManager.Factory.create();
                    LoginManager.getInstance()
                            .registerCallback(fbCallbackManager, getFacebookCallback());
                }
                LoginManager.getInstance().logInWithReadPermissions(
                        this, Arrays.asList("public_profile", "email"));
                break;
        }
    }

    private VKCallback<VKAccessToken> getVkCallback() {
        return new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                showLoadingDialog();
                RestClient.getInstance().loginSocial(LoginService.VK, res.accessToken, res.userId);
            }

            @Override
            public void onError(VKError error) {
                if (error.errorCode != VKError.VK_CANCELED) {
                    onEventMainThread(new LoginMsg(Message.AUTH_ERROR, true));
                }
            }
        };
    }

    private FacebookCallback<LoginResult> getFacebookCallback() {
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                RestClient.getInstance().loginSocial(
                        LoginService.FB, accessToken.getToken(), accessToken.getUserId());

                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showLoadingDialog();
                        }
                    });
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                onEventMainThread(new LoginMsg(Message.AUTH_ERROR, true));
            }
        };
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isLoading = savedInstanceState.getBoolean(BUNDLE_IS_LOADING, false);
            if (isLoading) showLoadingDialog();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_IS_LOADING, isLoading);
    }
}

