package com.d3.yiqi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;

import com.d3.yiqi.R;
import com.d3.yiqi.service.XmppConnection;
import com.d3.yiqi.service.XmppService;
import com.d3.yiqi.util.DialogFactory;

@SuppressWarnings("all")
public class RegisterActivity extends Activity implements OnClickListener {

	private Button mBtnRegister;
	private Button mRegBack;
	private EditText mEmailEt, mNameEt, mPasswdEt, mPasswdEt2, nameMCH;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.register);
		initView();
		mBtnRegister.setOnClickListener(this);
		mRegBack.setOnClickListener(this);

	}
	
	private void initView(){
		mBtnRegister = (Button) findViewById(R.id.register_btn);
		mRegBack = (Button) findViewById(R.id.reg_back_btn);
		nameMCH = (EditText) findViewById(R.id.reg_nameMCH);
		mEmailEt = (EditText) findViewById(R.id.reg_email);
		mNameEt = (EditText) findViewById(R.id.reg_name);
		mPasswdEt = (EditText) findViewById(R.id.reg_password);
		mPasswdEt2 = (EditText) findViewById(R.id.reg_password2);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_back_btn:
			login();
			break;
		case R.id.register_btn:
			registered();
			break;
		default:
			break;
		}
	}

	private void registered() {
		String accounts = mNameEt.getText().toString();
		String password = mPasswdEt.getText().toString();
		String password2 = mPasswdEt2.getText().toString();
		String email = mEmailEt.getText().toString();
		String nicheng = nameMCH.getText().toString();
		IQ result = null;
		//�ܷ�����
		if(!XmppConnection.isConnected()){
			Toast.makeText(getApplicationContext(), "������ά�������Ժ�",
					Toast.LENGTH_SHORT).show();
		}
		else{
			result =  XmppService.regist(accounts, password, email, nicheng);   //ע��
		//�������ж�
			if(accounts.length() == 0 || password.length() == 0) {
				Toast.makeText(getApplicationContext(), "�ף��ʺŻ����벻��Ϊ��Ŷ",
					Toast.LENGTH_SHORT).show();
			} 
			else if (!password.equals(password2)) {
				Toast.makeText(getApplicationContext(), "�������벻��ͬ��",
						Toast.LENGTH_SHORT).show();
			}
			else if (result == null) {
				Toast.makeText(getApplicationContext(), "������æ�����Ժ�",
						Toast.LENGTH_SHORT).show();
			}
			else if (result.getType() == IQ.Type.ERROR) {
				if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
					Toast.makeText(getApplicationContext(), "����˺��Ѿ�����",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "ע��ʧ��",
							Toast.LENGTH_SHORT).show();
				}
			} 
			else if (result.getType() == IQ.Type.RESULT) {   //ע��ɹ�ֱ�ӵ�¼
					Toast.makeText(getApplicationContext(), "��ϲ��ע��ɹ�",
							Toast.LENGTH_SHORT).show();
					if(XmppService.login(accounts, password))
					{
					Intent intent = new Intent();
					intent.putExtra("USERID", accounts);
					intent.setClass(RegisterActivity.this, FriendListActivity.class);
					startActivity(intent);
					}
				}
		}
	}

	private void login() {
		Intent intent = new Intent();
		intent.setClass(RegisterActivity.this, LoginActivity.class);
		startActivity(intent);
	}
}