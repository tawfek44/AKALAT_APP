package com.example.sharedprefrence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {
    boolean found=false;
    TextView signUp,tv;
    EditText UserName,Email,Password,Repassword;
    ImageView imageIcon;
    Button loginbtn;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imageIcon=(ImageView)findViewById(R.id.appIcon);
        signUp = (TextView)findViewById(R.id.signup_tv);
        tv = (TextView)findViewById(R.id.dontHaveAccount);
        Email=(EditText)findViewById(R.id.email_et);
        UserName=(EditText)findViewById(R.id.usrename_et);
        Password=(EditText)findViewById(R.id.password_et);
        Repassword=(EditText)findViewById(R.id.repassword_et);
        loginbtn=(Button)findViewById(R.id.log_btn);
    }

    public void onComponentsClick(View view){
        int id = view.getId();
        if(id== R.id.log_btn)
        {
            Log.i("tag","we just entered login btn !!!!");
            if(loginbtn.getText().toString().equals("SIGNUP"))
            {
                Log.d("tag","we just entered SignUp !!!!");
                if(TextUtils.isEmpty(UserName.getText().toString())||TextUtils.isEmpty(Email.getText().toString())
                        ||TextUtils.isEmpty(Password.getText().toString())||TextUtils.isEmpty(Repassword.getText().toString()))
                {
                    Toast.makeText(this,"All Fields Are Required !!",Toast.LENGTH_SHORT).show();
                    Log.d("tag","there is something empty!!!");
                }
                else if(!Password.getText().toString().equals(Repassword.getText().toString()))
                {
                    Log.d("tag","2 passwords Equal !!!");
                    Toast.makeText(this,"Passwords Fields Don't Match !!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.d("tag","We Entered!!!");
                    databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                    found = false;
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user user=new user(Email.getText().toString(),UserName.getText().toString(),Password.getText().toString());
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                user usr = snapshot.getValue(user.class);
                                if(usr!=null && !TextUtils.isEmpty(usr.getEmail()) && usr.getEmail().equals(Email.getText().toString())){
                                    found=true;
                                    Toast.makeText(loginActivity.this,"This Email Already Exist !!",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            if(!found){
                                String[] res = Email.getText().toString().split("@",2);
                                databaseReference.child(res[0]).setValue(user);
                                Intent intent = new Intent(loginActivity.this, CategoriesActivity.class);
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(loginActivity.this).edit();
                                editor.putString("user",res[0]);
                                editor.commit();
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
            else
            {
                found=false;
                if(TextUtils.isEmpty(Email.getText().toString())|| TextUtils.isEmpty(Password.getText().toString()))
                {
                    Toast.makeText(loginActivity.this,"All Fields Are Required !!",Toast.LENGTH_SHORT).show();
                    return;
                }
              databaseReference=FirebaseDatabase.getInstance().getReference("Users");
              databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      for(DataSnapshot snapshot:dataSnapshot.getChildren())
                      {
                          user usr=snapshot.getValue(user.class);

                          if(usr.getEmail().equals(Email.getText().toString())) {
                              if (usr.getPassword().equals(Password.getText().toString())) {
                                  Intent intent=new Intent(loginActivity.this, CategoriesActivity.class);
                                  startActivity(intent);
                                  finish();
                                  return;
                              }
                              else {
                                  Toast.makeText(loginActivity.this,"Password Is Not Correct !",Toast.LENGTH_SHORT).show();
                                  return;
                              }
                          }
                      }
                      Toast.makeText(loginActivity.this,"Email Is Not Exist !",Toast.LENGTH_SHORT).show();
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              });

            }
        }
        else if(id == R.id.signup_tv){
            if(signUp.getText().toString().equals("SignUp")) {
                UserName.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_0_n160));
                imageIcon.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_0_n15));
                Repassword.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_0_160));
                tv.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_0_200));
                signUp.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_0_200));
                loginbtn.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_0_120));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UserName.setY(UserName.getY() + (UserName.getHeight()*-1.6f));
                        loginbtn.setY(loginbtn.getY() + (loginbtn.getHeight()*1.2f));
                        Repassword.setY(Repassword.getY() + (Repassword.getHeight()*1.6f));
                        signUp.setY(signUp.getY() + (signUp.getHeight()*2.0f));
                        tv.setY(tv.getY() + (tv.getHeight()*2.0f));
                    }
                },1005);
                signUp.setText("LogIn");
                tv.setText("You Already Have An Account?");
                loginbtn.setText("SIGNUP");
            }
            else{
                UserName.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_0_160));
                imageIcon.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_0_15));
                Repassword.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_0_n160));
                tv.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_0_n200));
                signUp.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_0_n200));
                loginbtn.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_0_n120));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UserName.setY(UserName.getY() + (UserName.getHeight()*1.6f));
                        loginbtn.setY(loginbtn.getY() + (loginbtn.getHeight()*-1.2f));
                        Repassword.setY(Repassword.getY() + (Repassword.getHeight()*-1.6f));
                        signUp.setY(signUp.getY() + (signUp.getHeight()*-2.0f));
                        tv.setY(tv.getY() + (tv.getHeight()*-2.0f));
                    }
                },1010);
                signUp.setText("SignUp");
                tv.setText("You Don't Have An Account?");
                loginbtn.setText("LOGIN");
            }
        }


    }
}