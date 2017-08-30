package com.d3.yiqi.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.d3.yiqi.R;
import com.d3.yiqi.adapter.FriendListAdapter;
import com.d3.yiqi.model.FriendInfo;
import com.d3.yiqi.model.GroupInfo;
import com.d3.yiqi.service.XmppApplication;
import com.d3.yiqi.service.XmppConnection;
import com.d3.yiqi.service.XmppService;
import com.d3.yiqi.util.TransUtils;
/**
 * �����б�
 */
@SuppressWarnings("all")
public class FriendListActivity extends Activity implements OnItemClickListener,OnItemLongClickListener{
	private String pUSERID;//��ǰ�û�
	private String pGROUPNAME;//��ǰ��
	private ListView listView;
	private List<FriendInfo> friendList;
	public static FriendListAdapter adapter;
	public static FriendListActivity friendListActivity;
	FriendInfo friendInfo;
	Roster roster = XmppConnection.getConnection().getRoster();
    XMPPConnection connection = XmppConnection.getConnection();
    private String fromUserJid = null;//����������û���userJid��������Ҫ����ʱ��Ϊ��
    private String toUserJid = null;//�յ�������û���userJid
	private TextView myStatusText = null;           //����״̬��
    private String myMood = null;
    private String friendMood = null;
    private NewMsgReceiver newMsgReceiver;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		friendListActivity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.friend_list);
//		NotiUtil.setNotiType(this,R.drawable.default_head, "��½�ɹ���");
		this.pUSERID = XmppApplication.user;
		//��ʾ�Լ�����
		TextView friend_list_myName = (TextView)findViewById(R.id.friend_list_myName);
		myStatusText = (TextView)findViewById(R.id.myStatusText);
		friend_list_myName.setText(pUSERID);
		listView = (ListView) findViewById(R.id.contact_list_view);
		registerForContextMenu(listView);
		try {		//���غ����б�
			loadFriend();
		} catch (Exception e) {	
			Toast.makeText(this, "������:"+e.getMessage(),0).show();
		}
		adapter = new FriendListAdapter(this,friendList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		//����Ϣ����
		newMsgReceiver = new NewMsgReceiver();
		registerReceiver(newMsgReceiver, new IntentFilter("newMsg"));
		
		roster.addRosterListener(new FriendListner());  

//        if(fromUserJid!=null){         //����Ҫ�������
//    		AlertDialog.Builder dialog=new AlertDialog.Builder(FriendListActivity.this);
//			initDialog(dialog);
//    	}         
	}
	
	public void loadFriend() {
		try {
			friendList = new ArrayList<FriendInfo>();
				Collection<RosterEntry> entries = roster.getEntries();
				for (RosterEntry entry : entries) {
//					if("both".equals(entry.getType().name())){//ֻ���˫�ߺ��� 
						friendInfo = new FriendInfo();
						friendInfo.setUsername(XmppService.getUsername(entry.getUser()));
						if(entry.getStatus() == null){
							friendMood ="Q�Ұɣ�����������ţ�";
						}
						friendInfo.setMood(friendMood);
						friendList.add(friendInfo);
						friendInfo = null;			
//					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		FriendInfo info = friendList.get(position);
		Intent intent = new Intent(this,ChatActivity.class);
		intent.putExtra("FRIENDID", info.getJid());
		startActivity(intent);
	}
	/**
	 * ����ɾ��
	*/
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int postion,
			long arg3) {
		final FriendInfo friendInfo = friendList.get(postion);
		LayoutInflater layoutInflater= LayoutInflater.from(this);
        View delFriendView = layoutInflater.inflate(R.layout.dialog_del_friend, null);
        TextView delname = (TextView)delFriendView.findViewById(R.id.delname);
        delname.setText(friendInfo.getJid());
        final CheckBox delCheckBox = (CheckBox)delFriendView.findViewById(R.id.delCheckBox);
        Dialog dialog =new AlertDialog.Builder(this)
        .setIcon(R.drawable.default_head)
        .setTitle("ɾ������")
        .setView(delFriendView)
        .setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
            	 XmppService.removeUser(roster, friendInfo.getJid());
				 if(delCheckBox.isChecked()){
					 XmppService.removeUser(roster, friendInfo.getJid());
				 }
				 Intent intent = new Intent();
      			 intent.setClass(FriendListActivity.this, FriendListActivity.class);
      			 startActivity(intent); 
            }
        })
        .setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                	dialog.cancel();
                }
        })
        .create();
        dialog.show();
		return true;
	}

	@Override
	protected void onResume() {
		adapter.notifyDataSetChanged();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		XmppConnection.closeConnection();
		Intent intent = new Intent(this, XmppService.class);
		stopService(intent);
		friendListActivity = null;
		unregisterReceiver(newMsgReceiver);
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_HOME);
		startActivity(i);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(Menu.NONE, Menu.FIRST + 1, 1,"ˢ���б�").setIcon(R.drawable.menu_refresh);
		menu.add(Menu.NONE, Menu.FIRST + 2, 1,"��������").setIcon(R.mipmap.menu_setting);
		menu.add(Menu.NONE, Menu.FIRST + 3, 1,"��Ӻ���").setIcon(R.mipmap.addfriends_icon_icon);
		menu.add(Menu.NONE, Menu.FIRST + 4, 1,"�˳���¼").setIcon(R.mipmap.menu_exit);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			loadFriend();
			Intent intent1 = new Intent();
			intent1.setClass(FriendListActivity.this, FriendListActivity.class);
			startActivity(intent1);
			break;
		case Menu.FIRST + 2:
			LayoutInflater layoutInflater= LayoutInflater.from(this);
            final View myMoodView = layoutInflater.inflate(R.layout.dialog_mood, null);           
            Dialog dialog =new AlertDialog.Builder(this)
            .setView(myMoodView)
            .setPositiveButton("����", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                	myMood = ((EditText)myMoodView.findViewById(R.id.myMood)).getText().toString().trim();
                    XmppService.changeStateMessage(connection, myMood);
                    myStatusText.setText(myMood);
                }
            })
            .setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                	dialog.cancel();
                 }
            })
            .create();
            dialog.show();
			break;
		case Menu.FIRST + 3:
			Intent intent11 = new Intent();
			intent11.setClass(FriendListActivity.this, FriendAddActivity.class);
			startActivity(intent11);
			break;
		case Menu.FIRST + 4:
            Intent exits = new Intent(Intent.ACTION_MAIN);		
            exits.addCategory(Intent.CATEGORY_HOME);
            exits.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(exits);
            System.exit(0);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

/*	*//**
	 * ���������ʾ��
	*//*
	public void initDialog(AlertDialog.Builder dialog){
		dialog.setTitle("��������")
	      .setIcon(R.drawable.log)
	      .setMessage("��"+fromUserJid+"�����㷢���������룬�Ƿ���ӶԷ�Ϊ����?")
	      .setPositiveButton("���", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {	
              	     dialog.cancel();//ȡ��������
              	     //������Ӻ�����ظ���Ϣ����������Ӧ��Ҳ����һ����������
              	     Presence subscription = new Presence(Presence.Type.subscribe);
                       subscription.setTo(fromUserJid);
                       XmppConnection.getConnection().sendPacket(subscription);
                       if(pGROUPNAME == null){
                      	 pGROUPNAME = "�ҵĺ���";
                       }
                       XmppService.addUser(roster, fromUserJid, XmppService.getUsername(fromUserJid));
                       Intent intent = new Intent();
                       intent.setClass(FriendListActivity.this, FriendListActivity.class);
                       startActivity(intent); 
                   }
                 })
	       .setNegativeButton("�ܾ�", new DialogInterface.OnClickListener() {
	                 public void onClick(DialogInterface dialog, int which) {			
	                	 XmppService.removeUser(roster, fromUserJid);
	                     dialog.cancel();//ȡ��������
	                 }
	               }).create().show();
	}
	*/
	
	class FriendListner implements  RosterListener{   
        @Override  
        //��������������Ϣ
        public void entriesAdded(Collection<String> invites) { 
        	for (Iterator iter = invites.iterator(); iter.hasNext();) {
        		  String fromUserJids = (String)iter.next();
        		  fromUserJid = fromUserJids;
        	}               	 
        	 if(fromUserJid!=null){
         	   Intent intent = new Intent();
         	   //ֱ�����
//         	   Presence subscription = new Presence(Presence.Type.subscribe);
//               subscription.setTo(fromUserJid);
//               XmppConnection.getConnection().sendPacket(subscription);
//               XmppService.addUser(roster, fromUserJid, XmppService.getUsername(fromUserJid));
      		   intent.setClass(FriendListActivity.this, FriendListActivity.class);
      		   startActivity(intent); 
     	   }     
        }    
        @Override  
        //��������ͬ�������Ϣ
        public void entriesUpdated(Collection<String> invites) {  
        	 /*  for (Iterator iter = invites.iterator(); iter.hasNext();) {
          		  String fromUserJids = (String)iter.next();
          	      toUserJid = fromUserJids;
            	} 
        	    if(toUserJid!=null){
        	    	XmppService.addUser(roster, toUserJid, XmppService.getUsername(toUserJid));
                    loadFriend();
        	    } */              	    
        } 
        @Override  
        //��������ɾ����Ϣ
        public void entriesDeleted(Collection<String> delFriends) { 
        	if(delFriends.size()>0){
        		loadFriend();
    	    }  
        } 
       @Override  
       //��������״̬�ı���Ϣ
        public void presenceChanged(Presence presence) {  
    	    friendMood = presence.getStatus();
    		loadFriend();
       }        
	}
	
	private class NewMsgReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// �յ��V���������ǵĽ���
			adapter.notifyDataSetChanged();
		}
	}
}
