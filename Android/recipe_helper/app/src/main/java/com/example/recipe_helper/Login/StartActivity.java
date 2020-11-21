package com.example.recipe_helper.Login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.AgeRange;
import com.kakao.usermgmt.response.model.Gender;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;

public class StartActivity extends AppCompatActivity {
    private SessionCallback sessionCallback;
    private boolean autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);

        if (Session.getCurrentSession().checkAndImplicitOpen()) {
            Log.d("RHC", "Session Checked");
            autoLogin = true;
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            autoLogin = false;
            setContentView(R.layout.login);
            Log.d("RHC", "Session not checked");
        }
        getAppKeyHash();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(), "세션이 닫혔습니다. 다시 시도해 주세요: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {

                    long userID = result.getId();
                    UserAccount kakaoAccount = result.getKakaoAccount();

                    String nickName = kakaoAccount.getProfile().getNickname();
                    String email = kakaoAccount.getEmail();
                    AgeRange ageRange = kakaoAccount.getAgeRange();
                    Gender gender = kakaoAccount.getGender();
                    String profileImageUrl = kakaoAccount.getProfile().getProfileImageUrl();
                    AccessToken token = Session.getCurrentSession().getTokenInfo();
                    String accessToken = token.getAccessToken();
                    long expTime = token.accessTokenExpiresAt().getTime();

                    // 이메일
                    if (kakaoAccount == null) {
                        Toast.makeText(getBaseContext(), "아이디 정보를 제공해주셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String genderStr;
                    String ageStr;
                    // 성별 또는 나이 정보가 누락된 경우
                    if (gender == null || ageRange == null) {
                        ageStr = genderStr = "";
                    } else {
                        genderStr = kakaoAccount.getGender().getValue();
                        ageStr = kakaoAccount.getAgeRange().getValue();
                    }
                    UserInfo user = new UserInfo(userID, nickName, accessToken, email, profileImageUrl, genderStr, ageStr, expTime);

                    if (autoLogin) finish();
                    else replaceFragmentFull(new SignUp1(user));
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hash_key = new String(Base64.encode(md.digest(), 0));
                Log.d("HASH", "getAppKeyHash: " + hash_key);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    public void replaceFragmentFull(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
