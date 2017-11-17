package com.citi.cititransit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class UserSignInActivity extends AppCompatActivity {

    private EditText userEmail;
    private EditText userPassword;
    private Button loginButton;
    private TextView newUserTextView;
    private FirebaseAuth firebaseAuth;
    private AlertDialog regiAlert;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 9001;

    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);

        userEmail = (EditText)findViewById(R.id.loginEmail);
        userPassword = (EditText)findViewById(R.id.loginPassword);
        loginButton = (Button)findViewById(R.id.buttonLogin);
        newUserTextView = (TextView) findViewById(R.id.newUserSignUp);


        /**
         * GOOGLE SIGN IN
         * Configure sign-in to request the user's ID, email address, and basic
         * profile. ID and basic profile are included in DEFAULT_SIGN_IN.
         */

         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.credential))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
                        // be available.
                        Log.d(TAG, "onConnectionFailed:" + connectionResult);
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        firebaseAuth = FirebaseAuth.getInstance();

        //Button activities for user sign in page
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    loginUser(userEmail, userPassword);
            }
        });

        newUserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserSignInActivity.this, UserSignUpActivity.class));
            }
        });
    }

    /**
     * Sign in intent for google sign-in button
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                //TODO: CANNOT FUNCTION YET, NEED FIX
                Toast.makeText(UserSignInActivity.this, "Sign in failed",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(UserSignInActivity.this, MapsActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(UserSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void loginUser(EditText email, EditText password){
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        if(TextUtils.isEmpty(emailStr)){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(passwordStr)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(UserSignInActivity.this);
                            alertBuilder.setMessage("Sign In Successful")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            final Intent serviceIntent = new Intent(UserSignInActivity.this, NotificationService.class);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    startService(serviceIntent);
                                                }
                                            }, 5000);

                                            startActivity(new Intent(UserSignInActivity.this, MapsActivity.class));
                                        }
                                    });
                            regiAlert = alertBuilder.create();
                            regiAlert.show();
                        }else{
                            //todo: Need more work here
                            Toast.makeText(UserSignInActivity.this, "Something went wrong, try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }

}
