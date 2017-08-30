package com.d3.yiqi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jivesoftware.smack.XMPPException;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.OfflineMessageManager;

import com.d3.yiqi.R;
import com.d3.yiqi.service.XmppApplication;
import com.d3.yiqi.service.XmppConnection;
import com.d3.yiqi.service.XmppService;
import com.d3.yiqi.util.DialogFactory;
import com.d3.yiqi.util.MyToast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("all")
public class LoginActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private Button mBtnRegister;
	private Button mBtnLogin;
	private EditText mAccounts, mPassword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loginpage);
		initView();
		mBtnRegister.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
	}

	public void initView() {
		mBtnRegister = (Button) findViewById(R.id.regist_btn);
		mBtnLogin = (Button) findViewById(R.id.login_btn);
		mAccounts = (EditText) findViewById(R.id.lgoin_accounts);
		mPassword = (EditText) findViewById(R.id.login_password);

	}

	/**
	 * �������¼�
	 */
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.regist_btn:
			register();
			break;
		case R.id.login_btn:
			submit();
			break;
		default:
			break;
		}
	}

	private void register() {
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);
	}

	/**
	 * �ύ�˺�������Ϣ��������
	 */
	private void submit() {
		String accounts = mAccounts.getText().toString();
		String password = mPassword.getText().toString();
		Log.e("xuzemin",accounts + password+"");
		if (!XmppConnection.isConnected()) {
			MyToast.showToast(getApplicationContext(), "���粻����");
		} else if (accounts.length() == 0 || password.length() == 0) {
			Toast.makeText(getApplicationContext(), "�ʺŻ����벻��Ϊ��",
					Toast.LENGTH_SHORT).show();
		} else {
			if (XmppService.login(accounts, password)) {
				Intent intent = new Intent();
				XmppApplication.user = accounts;
//				XmppApplication.dbHelper.getChatMsg();  //ֻ��ʾ���ʮ������
				intent.setClass(LoginActivity.this, FriendListActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "��½ʧ�ܣ��˺Ż����������",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}