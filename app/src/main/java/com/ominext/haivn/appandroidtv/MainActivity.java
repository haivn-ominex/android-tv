/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ominext.haivn.appandroidtv;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;
import com.ominext.haivn.appandroidtv.api.ApiClient;
import com.ominext.haivn.appandroidtv.api.ApiResponse;
import com.ominext.haivn.appandroidtv.model.ListItem;
import com.ominext.haivn.appandroidtv.model.MyItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends Activity {
    private static List<MyItem> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*@Override
    protected void onDriveClientReady() {
        new MainActivity.RetrieveTokenTask().execute(googleSignInAccount.getEmail());
    }

    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        private final String TAG = RetrieveTokenTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {
            String accountName = params[0];
            //String scopes = "oauth2:https://www.googleapis.com/auth/plus.login";
            String scopes = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
            String token = null;

            String scope = "oauth2:" + Scopes.PROFILE;
            Account account = new Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), googleSignInAccount.getAccount(), scope);

                //token = GoogleAuthUtil.
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (UserRecoverableAuthException e) {
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listFiles("Bearer " + s);
            //listFiles("Bearer ya29.GmrpBptXQRnm40EgMdSDEE-lrHSWSBeQxVNlyO2SBpSW2K5C18yTNzSU_k5B1egkHgyIguWACMYAZ-tLeQp18Jj-Ai_JZyZ3p_YMNVALROQWYRUJTcu9cdyKw2m5zpx_VrlJSQDqp2rS9Cg1");
        }
    }

    *//**
     * Retrieves results for the next page. For the first run,
     * it retrieves results for the first page.
     *//*
    private void listFiles(String token) {
        ApiResponse apiService = ApiClient.getClient().create(ApiResponse.class);

        Call<ListItem> call = apiService.getListFile(token);
        call.enqueue(new Callback<ListItem>() {
            @Override
            public void onResponse(Call<ListItem> call, Response<ListItem> response) {
                if (response.code() == 200) {
                    List<MyItem> items = response.body().getItems();
                    list = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getTitle().contains(".jpg") || items.get(i).getTitle().contains(".jpeg")
                                || items.get(i).getTitle().contains(".png")
                                || items.get(i).getTitle().contains(".mp4")) {
                            list.add(items.get(i));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ListItem> call, Throwable t) {
            }
        });
    }*/

    public static List<MyItem> getList() {
        return list;
    }

    public void setList(List<MyItem> list) {
        this.list = list;
    }
}
