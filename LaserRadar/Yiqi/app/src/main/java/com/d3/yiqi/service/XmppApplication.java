package com.d3.yiqi.service;

import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.d3.yiqi.listener.ImgListener;
import com.d3.yiqi.listener.MessageInterceptor;
import com.d3.yiqi.listener.MessageListener;
import com.d3.yiqi.model.OneFridenMessages;
import com.d3.yiqi.util.DbHelper;

public class XmppApplication extends Application{
	public static XmppApplication xmppApplication;
	public static DbHelper dbHelper;
	public static SharedPreferences sharedPreferences;
	public static String user;
	public static final String XMPP_UP_MESSAGE_ACTION = "com.tarena.xmpp.chat.up.message.action";
	 // �������к��ѵ�����������Ϣ
 	public static ConcurrentHashMap<String, OneFridenMessages>AllFriendsMessageMapData;
	
 	@Override
	public void onCreate() {
		super.onCreate();
		xmppApplication = this;
		//�����¼
		AllFriendsMessageMapData = new ConcurrentHashMap<String, OneFridenMessages>();
		dbHelper = DbHelper.getInstance(getApplicationContext());
		//����Ϣ����
		sharedPreferences = getApplicationContext().getSharedPreferences("newMsgCount",
				Context.MODE_PRIVATE);
		initInterceptorAndListener();
	}
 	
	public void initInterceptorAndListener(){
		//��Ϣ��������
		MessageInterceptor mMessageInterceptor = new MessageInterceptor();
 		XmppConnection.getConnection().addPacketInterceptor(
				mMessageInterceptor,
				new PacketTypeFilter(Message.class));
 		//��Ϣ��������
 		MessageListener mMessageListener = new MessageListener();
		XmppConnection.getConnection().addPacketListener(mMessageListener,
				new PacketTypeFilter(Message.class));
		
		FileTransferManager fileTransferManager = new FileTransferManager(XmppConnection.getConnection());
		fileTransferManager.addFileTransferListener(new ImgListener());
 	}
}
