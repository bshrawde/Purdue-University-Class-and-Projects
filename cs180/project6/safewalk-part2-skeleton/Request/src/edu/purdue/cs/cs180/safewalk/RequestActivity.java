package edu.purdue.cs.cs180.safewalk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import edu.purdue.cs.cs180.channel.ChannelException;
import edu.purdue.cs.cs180.channel.MessageListener;
import edu.purdue.cs.cs180.channel.TCPChannel;

public class RequestActivity extends Activity implements MessageListener {

	TCPChannel channel = null;
	Handler mHandler = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);

		// the submit button.
		final Button button = (Button) findViewById(R.id.submit_button);
		final Spinner locations = (Spinner) findViewById(R.id.locations_spinner);
		final TextView status = (TextView) findViewById(R.id.status_textview);

		//TODO: Add channel creation code here.
		try{
			channel = new TCPChannel(getResources().getString(R.string.host_name),Integer.parseInt((getResources().getString(R.string.port_number))));
		}catch(ChannelException ex){
			ex.printStackTrace();
			System.exit(1);
		}
		channel.setMessageListener(this);

		// A handler is needed since the message received is called from a
		// different Thread, and only the main thread can update the UI.
		// As a workaround, we create a handler in the main thread that displays
		// whatever it receives from the message received.
		mHandler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				Message safeWalkMessage = (Message) msg.obj;
				//TODO: handle the received message.
				if(safeWalkMessage.toString().equals("Searching:"))
				{
				status.setText(""+safeWalkMessage);	
			}
				else{
			    status.setText(""+safeWalkMessage);
				button.setEnabled(true);
				locations.setEnabled(true);
				}
			}
		};

		// The on click event.
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Spinner locations = (Spinner) findViewById(R.id.locations_spinner);
				String selectedItem = (String) locations.getSelectedItem();
				locations.setEnabled(false);
				button.setEnabled(false);
				//TODO: send a message to the Server.
				try{
					channel.sendMessage("Request:"+selectedItem);
				}catch(ChannelException ex){
					ex.printStackTrace();
					System.exit(1);
					
				}
			}
		});
	}

	@Override
	public void messageReceived(String message, int clientID) {
		// Create a handler message, and send it to the Main Thread.
		Message safeWalkMessage = new Message(message, clientID);
		android.os.Message msg = new android.os.Message();
		msg.obj = safeWalkMessage;
		mHandler.sendMessage(msg);
	}
	
	/**
	 * Close the application if sent to the background.
	 */
	@Override
	protected void onPause() {
	    super.onPause();
	    System.exit(0);
	}
}
