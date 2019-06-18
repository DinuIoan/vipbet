package com.example.vipbet.signup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vipbet.MainActivity;
import com.example.vipbet.R;
import com.example.vipbet.database.FirebaseRepository;
import com.example.vipbet.login.EmailValidator;
import com.example.vipbet.login.LoginActivity;
import com.example.vipbet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameTextView;
    private EditText emailTextView;
    private EditText passwordTextView;
    private Button signUp;
    private TextView loginTextView;
    private LinearLayout signUpLinearLayout;
    private View mProgressView;
    private FirebaseAuth mAuth;
    private FirebaseRepository firebaseRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        firebaseRepository = new FirebaseRepository();
        nameTextView = findViewById(R.id.input_name);
        emailTextView = findViewById(R.id.input_email);
        passwordTextView = findViewById(R.id.input_password);
        signUp = findViewById(R.id.btn_signup);
        loginTextView = findViewById(R.id.link_login);
        signUpLinearLayout = findViewById(R.id.sign_up_view);
        mProgressView = findViewById(R.id.login_progress);

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        passwordTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });
    }

    private void attemptSignUp() {
        emailTextView.setError(null);
        passwordTextView.setError(null);

        String name = nameTextView.getText().toString();
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            nameTextView.setError(getString(R.string.error_field_required));
            focusView = nameTextView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordTextView.setError(getString(R.string.error_invalid_password_login));
            focusView = passwordTextView;
            cancel = true;
        }

        // Check for a valid email address.

        if (TextUtils.isEmpty(email)) {
            emailTextView.setError(getString(R.string.error_field_required));
            focusView = emailTextView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailTextView.setError(getString(R.string.error_invalid_email));
            focusView = emailTextView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password, mAuth);
//            mAuthTask.execute((Void) null);
            showProgress(true);
            signUp(name, email, password);
        }

    }

    private void signUp(final String name, final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("INFO", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mAuth.signInWithEmailAndPassword(email, password);
                            firebaseRepository.addUser(
                                    new User(user.getUid(),
                                            name,
                                            false));
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            showProgress(false);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("INFO", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            reloadActivity();
                        }

                        // ...
                    }
                });
    }

    private boolean isEmailValid(String email) {
        EmailValidator emailValidator = new EmailValidator();
        return emailValidator.validateEmail(email);
    }

    private boolean isPasswordValid(String password) {

        return password.length() >= 8; //&& password.matches("[A-Za-z0-9]");
    }

    private void reloadActivity() {
        finish();
        startActivity(getIntent());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            signUpLinearLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            signUpLinearLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    signUpLinearLayout.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            signUpLinearLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
