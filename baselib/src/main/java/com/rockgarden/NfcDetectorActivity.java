package com.rockgarden;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;


/**
 * Abstract {@link Activity} for detecting incoming NFC messages.<br/><br/>
 * <p/>
 * - detects whether NFC is available (if device has NFC chip).<br/>
 * - detect whether NFC setting is on or off, and whether it changes from off to on or on to off.<br/>
 * - detect incoming data tags or beams.<br/>
 *
 * @author Thomas Rorvik Skjolberg
 *         Modify by rockgarden on 15/12/27.
 */
public abstract class NfcDetectorActivity extends Activity {

    /**
     * Broadcast Action: The state of the local NFC adapter has been
     * changed.
     * <p>For example, NFC has been turned on or off.
     * <p>Always contains the extra field
     */
    public static final String ACTION_ADAPTER_STATE_CHANGED = "android.nfc.action.ADAPTER_STATE_CHANGED";

    /**
     * Used as an int extra field in
     * intents to request the current power state. Possible values are:
     * {@link #STATE_OFF},
     * {@link #STATE_TURNING_ON},
     * {@link #STATE_ON},
     * {@link #STATE_TURNING_OFF},
     */
    public static final String EXTRA_ADAPTER_STATE = "android.nfc.extra.ADAPTER_STATE";

    public static final int STATE_OFF = 1;
    public static final int STATE_TURNING_ON = 2;
    public static final int STATE_ON = 3;
    public static final int STATE_TURNING_OFF = 4;

    private static final String ACTION_NFC_SETTINGS = "android.settings.NFC_SETTINGS";
    /**
     * this action seems never to be emitted, but is here for future use
     */
    private static final String ACTION_TAG_LEFT_FIELD = "android.nfc.action.TAG_LOST";

    private static final String TAG = NfcDetectorActivity.class.getName();

    private static IntentFilter nfcStateChangeIntentFilter = new IntentFilter(ACTION_ADAPTER_STATE_CHANGED);

    protected NfcAdapter nfcAdapter;
    protected IntentFilter[] writeTagFilters;
    protected PendingIntent nfcPendingIntent;

    protected boolean foreground = false;
    protected boolean intentProcessed = false;
    protected boolean nfcEnabled = false;

    protected BroadcastReceiver nfcStateChangeBroadcastReceiver;

    protected boolean detecting = false;

    protected boolean nxpMifareClassic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nxpMifareClassic = hasMifareClassic();
        PackageManager pm = getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_NFC) && NfcAdapter.getDefaultAdapter(this) != null) {
            onNfcFeatureFound();
        } else {
            onNfcFeatureNotFound();
        }
    }

    private boolean hasMifareClassic() {
        return getPackageManager().hasSystemFeature("com.nxp.mifare");
    }

    /**
     * Notify that NFC is available
     */
    protected void onNfcFeatureFound() {
        initializeNfc();
        detectInitialNfcState();
    }

    /**
     * Initialize Nfc fields
     */
    protected void initializeNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter tagLost = new IntentFilter(ACTION_TAG_LEFT_FIELD);
        writeTagFilters = new IntentFilter[]{ndefDetected, tagDetected, techDetected, tagLost};
        nfcStateChangeBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final int state = intent.getIntExtra(EXTRA_ADAPTER_STATE, -1);
                if (state == STATE_OFF || state == STATE_ON) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (state == STATE_ON) {
                                if (detecting) {
                                    enableForeground();
                                }
                            }
                            detectNfcStateChanges();
                        }
                    });
                }
            }
        };
    }

    /**
     * Detect initial NFC state.
     */
    protected void detectInitialNfcState() {
        nfcEnabled = nfcAdapter.isEnabled();
        if (nfcEnabled) {
            Log.d(TAG, "NFC is enabled");
            onNfcStateEnabled();
        } else {
            Log.d(TAG, "NFC is disabled"); // change state in wireless settings
            onNfcStateDisabled();
        }
    }

    protected abstract void onNfcStateEnabled();

    protected abstract void onNfcStateDisabled();

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            // enable foreground mode if nfc is on and we have started detecting
            boolean enabled = nfcAdapter.isEnabled();
            if (enabled && detecting) {
                enableForeground();
            }
            detectNfcStateChanges();
            // for quicksettings
            startDetectingNfcStateChanges();
        }
        if (!intentProcessed) {
            intentProcessed = true;
            processIntent();
        }
    }

    /**
     * NFC setting changed since last check. For example, the user enabled NFC in the wireless settings.
     */
    protected abstract void onNfcStateChange(boolean enabled);

    /**
     * Detect changes in NFC settings - enabled/disabled
     */
    protected void detectNfcStateChanges() {
        boolean enabled = nfcAdapter.isEnabled();
        if (nfcEnabled != enabled) {
            onNfcStateChange(enabled);
            nfcEnabled = enabled;
        } else {
        }
    }

    public void startDetectingNfcStateChanges() {
        registerReceiver(nfcStateChangeBroadcastReceiver, nfcStateChangeIntentFilter);
    }

    public void stopDetectingNfcStateChanges() {
        unregisterReceiver(nfcStateChangeBroadcastReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            disableForeground();
            // for quicksettings
            stopDetectingNfcStateChanges();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        intentProcessed = false;
        setIntent(intent);
    }

    protected void enableForeground() {
        if (!foreground) {
            nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
            foreground = true;
        }
    }

    /**
     * Start detecting NDEF messages
     */
    protected void startDetecting() {
        if (!detecting) {
            enableForeground();
            detecting = true;
        }
    }

    /**
     * Stop detecting NDEF messages
     */
    protected void stopDetecting() {
        if (detecting) {
            disableForeground();
            detecting = false;
        }
    }

    protected void disableForeground() {
        if (foreground) {
            nfcAdapter.disableForegroundDispatch(this);
            foreground = false;
        }
    }

    /**
     * Process the current intent, looking for NFC-related actions
     */
    public void processIntent() {
        Intent intent = getIntent();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Log.d(TAG, "Process NDEF discovered action");

            onNfcIntentDetected(intent, NfcAdapter.ACTION_NDEF_DISCOVERED);
        } else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Log.d(TAG, "Process TAG discovered action");

            onNfcIntentDetected(intent, NfcAdapter.ACTION_TAG_DISCOVERED);
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            Log.d(TAG, "Process TECH discovered action");

            onNfcIntentDetected(intent, NfcAdapter.ACTION_TECH_DISCOVERED);
        } else if (ACTION_TAG_LEFT_FIELD.equals(intent.getAction())) {
            Log.d(TAG, "Process tag lost action");

            onNfcTagLost(intent); // NOTE: This seems not to work as expected
        } else {
            Log.d(TAG, "Ignore action " + intent.getAction());
        }
    }

    /**
     * Launch an activity for NFC (or wireless) settings, so that the user might enable or disable nfc
     */
    protected void startNfcSettingsActivity() {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            startActivity(new Intent(ACTION_NFC_SETTINGS));
        } else {
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    /**
     * Incoming NFC communication (in form of tag or beam) detected
     */
    protected abstract void onNfcIntentDetected(Intent intent, String action);

    /**
     * This device does not have NFC hardware
     */
    protected abstract void onNfcFeatureNotFound();

    public boolean isDetecting() {
        return detecting;
    }

    public void setDetecting(boolean detecting) {
        this.detecting = detecting;
    }

    protected abstract void onNfcTagLost(Intent intent);

    /**
     * Check to see if a tag is currently connected. This method synchronizes on the tag object.
     * Other methods accessing tag details should do so to.
     *
     * @return true if present
     * @deprecated should use broadcast intent instead (but it seems not to work)
     */
    public boolean isPresent(Tag tag) {
        String[] techList = tag.getTechList();
        for (String tech : techList) {
            TagTechnology tagTech = null;
            if (tech.equals(android.nfc.tech.MifareUltralight.class.getName())) {
                tagTech = MifareUltralight.get(tag);
            } else if (tech.equals(android.nfc.tech.NfcA.class.getName())) {
                tagTech = NfcA.get(tag);
            } else if (tech.equals(android.nfc.tech.NfcB.class.getName())) {
                tagTech = NfcB.get(tag);
            } else if (tech.equals(android.nfc.tech.NfcF.class.getName())) {
                tagTech = NfcF.get(tag);
            } else if (tech.equals(android.nfc.tech.NfcV.class.getName())) {
                tagTech = NfcV.get(tag);
            } else if (tech.equals(android.nfc.tech.IsoDep.class.getName())) {
                tagTech = IsoDep.get(tag);
            } else if (tech.equals(android.nfc.tech.MifareClassic.class.getName())) {
                tagTech = MifareClassic.get(tag);
            }
            if (tagTech != null) {
                synchronized (tag) {
                    try {
                        tagTech.connect();
                        return tagTech.isConnected();
                    } catch (IOException e) {
                        return false;
                    } finally {
                        try {
                            tagTech.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }

        }
        return false;
    }
}
