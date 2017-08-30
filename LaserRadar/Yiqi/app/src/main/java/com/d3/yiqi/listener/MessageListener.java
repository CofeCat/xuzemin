package com.d3.yiqi.listener;

import java.io.IOException;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.d3.yiqi.R;
import com.d3.yiqi.model.Msg;
import com.d3.yiqi.model.OneFridenMessages;
import com.d3.yiqi.service.XmppApplication;
import com.d3.yiqi.service.XmppConnection;
import com.d3.yiqi.util.DateUtil;
import com.d3.yiqi.util.NotiUtil;

public class MessageListener implements PacketListener {

	OneFridenMessages mOneFridenMessages;

	@Override
	public void processPacket(Packet packet) {
		// �����ﴦ�����������յ�����Ϣ
		Message nowMessage = (Message) packet;
		Log.i("come",nowMessage.getFrom()+"  to"+nowMessage.getTo()+"   :  "+nowMessage.getBody());
		//ϵͳ��Ϣ
		if(nowMessage.getFrom().equals(XmppConnection.SERVER_NAME)){
		NotiUtil.setNotiType(XmppApplication.xmppApplication.getApplicationContext(),
				R.drawable.default_head, nowMessage.getFrom()+":  "+nowMessage.getBody());
		}	
		
		String friendUser = nowMessage.getFrom();
		if (friendUser.contains("/")) {
			friendUser = friendUser.substring(0, friendUser.indexOf("/"));
		}
		//��ȡ�ʹ˺��ѵĶԻ���Ϣ
		mOneFridenMessages = XmppApplication.AllFriendsMessageMapData
				.get(friendUser);

		if (mOneFridenMessages == null) {
			mOneFridenMessages = new OneFridenMessages();
			XmppApplication.AllFriendsMessageMapData.put(friendUser,
					mOneFridenMessages);
		}
		//����
		Msg nowMsg = new Msg(nowMessage.getFrom().substring(0,
				friendUser.indexOf("@")), nowMessage.getBody(),
				DateUtil.now_MM_dd_HH_mm_ss(), "IN");
		mOneFridenMessages.MessageList.add(nowMsg);
		//���浽sql
		XmppApplication.dbHelper.saveChatMsg(nowMsg,friendUser);
		mOneFridenMessages.NewMessageCount++;
		//���浽����share������������رճ���ʧ����Ϣ����
		XmppApplication.sharedPreferences.edit().putInt(friendUser+XmppApplication.user, 
				mOneFridenMessages.NewMessageCount).commit();
		//���͹㲥
		Intent messageIntent = new Intent(
				XmppApplication.XMPP_UP_MESSAGE_ACTION);

		messageIntent.setData(Uri.parse("xmpp://"
				+ friendUser.substring(0, friendUser.indexOf("@"))));

		XmppApplication.xmppApplication.sendBroadcast(messageIntent);
		XmppApplication.xmppApplication.sendBroadcast(new Intent(
				"newMsg"));
		
		// ��������
		MediaPlayer mPlayer  = MediaPlayer.create(XmppApplication.xmppApplication.getApplicationContext(),R.raw.msn );
		try {
			if (mPlayer != null) {
				mPlayer.stop();
			}
			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
