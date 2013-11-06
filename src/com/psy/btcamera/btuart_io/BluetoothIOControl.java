package com.psy.btcamera.btuart_io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.psy.btcamera.R;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothIOControl extends Activity {
	// Debugging
	private static final String TAG = "BluetoothIOControl";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	// Layout Views
	private TextView mTitle;
	// private ListView mConversationView;
	// private EditText mOutEditText;
	private EditText mBT11EditText, mBT21EditText, mBT31EditText,
			mBT41EditText, mBT51EditText, mBT61EditText, mBT71EditText,
			mBT81EditText;
	// private Button mSendButton;
	private Button mmBT11On, mBT21On, mBT31On, mBT41On, mBT51On, mBT61On,
			mBT71On, mBT81On, mBT91On, mBTA1On;
	private Button mmBT11Off, mBT21Off, mBT31Off, mBT41Off, mBT51Off, mBT61Off,
			mBT71Off, mBT81Off, mBT91Off, mBTA1Off;
	private Button btn_device, btn_disconnect, btn_connect;
	// Name of the connected device
	private String mConnectedDeviceName = null;
	private String address = null;

	// Array adapter for the conversation thread
	// private ArrayAdapter<String> mConversationArrayAdapter;
	// String buffer for outgoing messages
	// private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothIOControlService mChatService = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (D)
			Log.e(TAG, "+++ ON CREATE +++");

		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.hotlife_blucontrol);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		// Set up the custom title
		mTitle = (TextView) findViewById(R.id.title_left_text);
		mTitle.setText(R.string.app_name);
		mTitle = (TextView) findViewById(R.id.title_right_text);

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

	}

	private Object[] activities = { "BT1010", "BT1020", "BT1030", "BT1040",
			"BT1050", "BT1060", "BT1070", "BT1080" };

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			if (mChatService == null)
				setupChat();
		}

	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");
		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothIOControlService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}

	}

	private void setupChat() {
		Log.d(TAG, "setupChat()");

		mmBT11On = (Button) findViewById(R.id.mBT11On);
		mmBT11On.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1011";
				sendMessage(message + "\r\n");
			}
		});
		mmBT11Off = (Button) findViewById(R.id.mBT11Off);
		mmBT11Off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1010";
				sendMessage(message + "\r\n");
			}
		});
		mBT21On = (Button) findViewById(R.id.mBT21On);
		mBT21On.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1021";
				sendMessage(message + "\r\n");
			}
		});
		mBT21Off = (Button) findViewById(R.id.mBT21Off);
		mBT21Off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1020";
				sendMessage(message + "\r\n");
			}
		});
		mBT31On = (Button) findViewById(R.id.mBT31On);
		mBT31On.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1031";
				sendMessage(message + "\r\n");
			}
		});
		mBT31Off = (Button) findViewById(R.id.mBT31Off);
		mBT31Off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1030";
				sendMessage(message + "\r\n");
			}
		});
		mBT41On = (Button) findViewById(R.id.mBT41On);
		mBT41On.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1041";
				sendMessage(message + "\r\n");
			}
		});
		mBT41Off = (Button) findViewById(R.id.mBT41Off);
		mBT41Off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1040";
				sendMessage(message + "\r\n");
			}
		});
		mBT51On = (Button) findViewById(R.id.mBT51On);
		mBT51On.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1051";
				sendMessage(message + "\r\n");
			}
		});
		mBT51Off = (Button) findViewById(R.id.mBT51Off);
		mBT51Off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1050";
				sendMessage(message + "\r\n");
			}
		});
		mBT61On = (Button) findViewById(R.id.mBT61On);
		mBT61On.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1061";
				sendMessage(message + "\r\n");
			}
		});
		mBT61Off = (Button) findViewById(R.id.mBT61Off);
		mBT61Off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1060";
				sendMessage(message + "\r\n");
			}
		});
		mBT71On = (Button) findViewById(R.id.mBT71On);
		mBT71On.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1071";
				sendMessage(message + "\r\n");
			}
		});
		mBT71Off = (Button) findViewById(R.id.mBT71Off);
		mBT71Off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1070";
				sendMessage(message + "\r\n");
			}
		});
		mBT81On = (Button) findViewById(R.id.mBT81On);
		mBT81On.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1081";
				sendMessage(message + "\r\n");
			}
		});
		mBT81Off = (Button) findViewById(R.id.mBT81Off);
		mBT81Off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1080";
				sendMessage(message + "\r\n");
			}
		});
		mBT91On = (Button) findViewById(R.id.mBT91On);
		mBT91On.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1091";
				sendMessage(message + "\r\n");
			}
		});
		mBT91Off = (Button) findViewById(R.id.mBT91Off);
		mBT91Off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT1090";
				sendMessage(message + "\r\n");
			}
		});
		mBTA1On = (Button) findViewById(R.id.mBTA1On);
		mBTA1On.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT10A1";
				sendMessage(message + "\r\n");
			}
		});
		mBTA1Off = (Button) findViewById(R.id.mBTA1Off);
		mBTA1Off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				String message = "BT10A0";
				sendMessage(message + "\r\n");
			}
		});

		btn_device = (Button) findViewById(R.id.btn_device);
		btn_device.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				scanbt();
			}
		});

		btn_disconnect = (Button) findViewById(R.id.btn_disconnect);
		btn_disconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				if (mChatService.getState() == BluetoothIOControlService.STATE_CONNECTED) {
					mChatService.stop();
					mChatService.start();
				}
			}
		});
		btn_connect = (Button) findViewById(R.id.btn_connect);
		btn_connect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createNXTConnection();
			}
		});

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothIOControlService(this, mHandler);
	}

	private void createNXTConnection() {
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> bondedDevices = btAdapter.getBondedDevices();
		BluetoothDevice nxtDevice = null;

		for (BluetoothDevice bluetoothDevice : bondedDevices) {
			if (bluetoothDevice.getName().equals(mConnectedDeviceName)) {
				nxtDevice = bluetoothDevice;
				break;
			}
		}

		if (nxtDevice == null) {
			Toast toast = Toast.makeText(this, "No paired BT device found",
					Toast.LENGTH_SHORT);
			toast.show();
			return;
		}
		mChatService.connect(nxtDevice);

	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (D)
			Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		if (D)
			Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
	}

	private void resetport() {
		if (mChatService.getState() == BluetoothIOControlService.STATE_CONNECTED) {
			CharSequence[] list = new CharSequence[activities.length];
			for (int i = 0; i < list.length; i++) {
				// list[i] = (String)activities[i * 2];
				String message = (String) activities[i];
				sendMessage(message + "\r\n");
			}
		}
	}

	private void scanbt() {
		Intent serverIntent = new Intent(this, DeviceListActivity.class);
		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
	}

	private void ensureDiscoverable() {
		if (D)
			Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	private void hotlifeonly() {
		String hotlifecode = "001AFF";
		String[] checkcode = address.split(":");
		String data1 = checkcode[0] + checkcode[1] + checkcode[2];
		if (data1 != hotlifecode) {
			// Attempt to connect to the device
			mChatService.stop();
			mChatService.start();

		}
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothIOControlService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);
		}
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothIOControlService.STATE_CONNECTED:
					mTitle.setText(R.string.title_connected_to);
					mTitle.append(mConnectedDeviceName);
					resetport();
					// mConversationArrayAdapter.clear();
					break;
				case BluetoothIOControlService.STATE_CONNECTING:
					// Get the BLuetoothDevice object
					mTitle.setText(R.string.title_connecting);
					break;
				case BluetoothIOControlService.STATE_LISTEN:
				case BluetoothIOControlService.STATE_NONE:
					mTitle.setText(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras().getString(
						DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// Get the BLuetoothDevice object
				/*
				 * String[] hotlifecode = {"00","1A","FF"}; String[] checkcode =
				 * address.split(":"); String[] changcode = new String[3];
				 * changcode[0] = checkcode[0]; changcode[1] = checkcode[1];
				 * changcode[2] = checkcode[2];
				 * 
				 * // Attempt to connect to the device if(changcode[0] ==
				 * hotlifecode[0] && changcode[1] == hotlifecode[1] &&
				 * changcode[2] == hotlifecode[2]){ // Attempt to connect to the
				 * device mChatService.stop(); mChatService.start(); } else{
				 * 
				 * }
				 */
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				mChatService.connect(device);

			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scan:
			// Launch the DeviceListActivity to see devices and do scan
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		}
		return false;
	}

}