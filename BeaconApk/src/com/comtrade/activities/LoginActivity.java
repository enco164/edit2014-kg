package com.comtrade.activities;

import java.io.Console;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comtrade.device.BeaconApkConfig;
import com.comtrade.ilserver.tasks.ConnectTask;
import com.comtrade.ilserver.tasks.User;
import com.example.beaconapk.R;

public class LoginActivity extends Activity{
	
	Button btnLogin;
	Button btnRegister;
	EditText txtURL;
	EditText txtUsername;
	EditText txtPassword;
	SharedPreferences sharedPref;
	Editor sharedPrefEditor;
	public User userLogovan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		 

		btnLogin = (Button) findViewById(R.id.btn_login);
		btnRegister = (Button) findViewById(R.id.btn_create);
		txtUsername = (EditText) findViewById(R.id.txt_username);
		txtPassword = (EditText) findViewById(R.id.txt_password);
		txtURL = (EditText) findViewById(R.id.txt_url);
		
		sharedPref = getApplicationContext().getSharedPreferences(BeaconApkConfig.SHARE_PREFERENCE_REGISTER, 0);
		sharedPrefEditor = sharedPref.edit();
		
		
		btnLogin.setOnClickListener(new OnClickListener() {			

			@Override
			public void onClick(View v) {	
				
				// provera da li je url, username ili password prazan 
				if(txtURL.getText().toString().equals(null) || txtURL.getText().toString().equals(""))
					Toast.makeText(getApplicationContext(), "Niste uneli URL", Toast.LENGTH_SHORT).show();
				else if((txtUsername.getText().toString().equals("")) || (txtUsername.getText().toString().equals(null)))
					Toast.makeText(getApplicationContext(), "Niste unelu username", Toast.LENGTH_SHORT).show();
				/*else if((userEdit.getText().toString() != null && userEdit.length()<BeaconApkConfig.MIN_USER_LENGTH))
					Toast.makeText(getApplicationContext(), "Username nije dovoljno dugaak", Toast.LENGTH_SHORT).show();*/
				else if((txtPassword.getText().toString().equals("")) || (txtPassword.getText().toString().equals(null)))
					Toast.makeText(getApplicationContext(), "Niste uneli password", Toast.LENGTH_SHORT).show();
				/*else if((passEdit.getText().toString() != null && passEdit.length()<BeaconApkConfig.MIN_PASS_LENGTH))
					Toast.makeText(getApplicationContext(), "Password nije dovoljno dugaak", Toast.LENGTH_SHORT).show();*/
				else
				{
				// cuva podatke u shared preferences
					sharedPrefEditor.putString(BeaconApkConfig.SHARE_URL,txtURL.getText().toString());
					sharedPrefEditor.putString(BeaconApkConfig.SHARE_USERNAME, txtUsername.getText().toString());
					sharedPrefEditor.putString(BeaconApkConfig.SHARE_PASSWORD, txtPassword.getText().toString());
					sharedPrefEditor.apply();
					ConnectTask ct = new ConnectTask();
					ct.setUsername(txtUsername.getText().toString());
					ct.setPassword(txtPassword.getText().toString());
					ct.execute(txtURL.getText().toString()+"/rest/signin");
					try {
						String UserJSON = null;
						UserJSON = ct.get();
						Log.d("AAA", "" + UserJSON);
						if(UserJSON == null || UserJSON.equals("401")){
							Toast.makeText(getApplicationContext(), "Bad username or password", Toast.LENGTH_SHORT).show();
						}
						else if(UserJSON.equals("404")){
							Toast.makeText(getApplicationContext(), "Bad server", Toast.LENGTH_SHORT).show();
						}
						else{
							User userLogovan = User.fromJSON(UserJSON);
							ArrayList<String> roles = userLogovan.getRoles();
							
							if(roles.size() == 1){
								if(roles.get(0).toString().equals("client")){
									Intent i = new Intent(LoginActivity.this, GuideActivity.class);
									startActivity(i);
								}
								else{
									//ubaciti za monitoring activity 
									Intent i = new Intent(LoginActivity.this, SpacesActivity.class);
									startActivity(i);
								}
							}
							else{
								onUserLogin();
							}
						}
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			
			}
		});
		
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sharedPrefEditor.putString(BeaconApkConfig.SHARE_URL, txtURL.getText().toString());
				sharedPrefEditor.apply();
				
				Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(i);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		String tmp = sharedPref.getString(BeaconApkConfig.SHARE_URL, null);
		if (tmp != null) { txtURL.setText(tmp); }
		else { txtURL.setText(BeaconApkConfig.URI_ILS_DEFAULT); }
		
		tmp = sharedPref.getString(BeaconApkConfig.SHARE_USERNAME, null);
		if (tmp != null) { txtUsername.setText(tmp); }
		
		tmp=sharedPref.getString(BeaconApkConfig.SHARE_PASSWORD, null);
		if(tmp!=null){txtPassword.setText(tmp);}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.shared_preferences, menu);
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
	
	
	//funkcija za prikazivanje dialoga i odvodjenje na odgovarajuci activity
	public void onUserLogin() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder
				.setTitle("Role selection")
				.setMessage("Select whether to log in as a mobile or monitoring user: ")
				.setPositiveButton("Mobile user",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						//device.registerDevice();
						//TODO ovde ide akcija ukoliko se izabere obican korisnik
						//Toast.makeText(getApplicationContext(), "USER ROLA", Toast.LENGTH_LONG).show();
						Intent i = new Intent(LoginActivity.this, GuideActivity.class);
						i.putExtra("korisnik", userLogovan);
						startActivity(i);
					}
				})
				.setNegativeButton("Monitoring user",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						/*dialog.dismiss();
						finishActivity(0);*/
						//TODO ovde ide akcija ukoliko se izabere management rola
						Intent i = new Intent(LoginActivity.this, SpacesActivity.class);
						startActivity(i);
					}
				}).create();
		dialog.show();
	}

}