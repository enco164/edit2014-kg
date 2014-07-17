package com.comtrade.activities;

import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comtrade.device.BeaconApkConfig;
import com.comtrade.ilserver.tasks.UserRegistrationTask;
import com.example.beaconapk.R;

public class RegisterActivity extends Activity {
	private EditText username, password, firstName, lastName;
	
	SharedPreferences pref;
	Editor editor;
	
	//public static final String SHARE_USERNAME="shareUsername",
			//SHARE_PASSWORD="sharePassword", SHARE_PREFERENCE_REGISTER="SharedPreferenceRegister";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		username=(EditText) findViewById(R.id.user);
		password=(EditText) findViewById(R.id.pass);
		firstName=(EditText) findViewById(R.id.f_name);
		lastName=(EditText) findViewById(R.id.l_name);
		
		//generisanje shared preference
				pref=getApplicationContext().getSharedPreferences(BeaconApkConfig.SHARE_PREFERENCE_REGISTER, 0);
				editor = pref.edit();
				
				ActionBar actionbar=getActionBar();
				actionbar.setDisplayHomeAsUpEnabled(true);
				
				Button button_Register=(Button) findViewById(R.id.buttonRegister);
				button_Register.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//  Auto-generated method stub				
						boolean provera = proveriRegisterPodatke(username.getText().toString(),
								password.getText().toString(),
								firstName.getText().toString(), 
								lastName.getText().toString());
						
						if(provera){
							// konekcija ka serveru i slanje podataka na server
							UserRegistrationTask userRegTas=new UserRegistrationTask();
							userRegTas.setUsername(username.getText().toString());
							userRegTas.setPassword(password.getText().toString());
							userRegTas.setFirstName(firstName.getText().toString());
							userRegTas.setSurname(lastName.getText().toString());
							
							//  shared preferences za URL
							userRegTas.execute(pref.getString(BeaconApkConfig.SHARE_URL,null)+"/rest/register",userRegTas.getUsername(),
									userRegTas.getPassword(), userRegTas.getFirstName(),
									userRegTas.getSurname());
							
							try {
								if(userRegTas.get()!=null){
									//uspesna registracija
									Toast.makeText(RegisterActivity.this, "Successfully registered!", Toast.LENGTH_LONG).show();
									//ubacivanje username u editor
									editor.putString(BeaconApkConfig.SHARE_USERNAME, username.getText().toString());
									editor.commit();
									//vracanje na login stranu
									finish();
								}
								else{
									Toast.makeText(RegisterActivity.this, "Unsuccessfully registered!", Toast.LENGTH_LONG).show();
									//neuspesna registracija
								}
							} catch (InterruptedException e) {
								//  Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								//  Auto-generated catch block
								e.printStackTrace();
							}			
							
						}else{
							//Poruka ukoliko nije ispostovano popunjavanje polja
							Toast.makeText(RegisterActivity.this, "You must fill in all fields!\n"
									+ "Space can't be included!\n"
									+ "Username must be at least 6 characters long\n"
									+ "Password must be at least 7 characters long!", Toast.LENGTH_LONG).show();
							//Fokus na prvo edit polje
							username.requestFocus();
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean proveriRegisterPodatke(String user, String pass, String f_name, String l_name){
		boolean rezultat;
		if(user!=null && user.length()>5 && !user.contains(" ") &&
				pass!=null && pass.length()>6 && !pass.contains(" ") && 
				f_name!=null && f_name.length()>0 && !f_name.contains(" ") &&
				l_name!=null && l_name.length()>0 && !l_name.contains(" ")){
			rezultat=true;
		}else{
			rezultat=false;
		}
		return rezultat;
	}
}