package com.flybits.samples.context.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flybits.core.api.context.BasicData;
import com.flybits.core.api.context.plugins.activity.ActivityData;
import com.flybits.core.api.context.plugins.battery.BatteryLifeData;
import com.flybits.core.api.context.plugins.beacon.EddyStone;
import com.flybits.core.api.context.plugins.beacon.IBeacon;
import com.flybits.core.api.context.plugins.carrier.CarrierData;
import com.flybits.core.api.context.plugins.fitness.FitnessData;
import com.flybits.core.api.context.plugins.language.LanguageData;
import com.flybits.core.api.context.plugins.location.LocationData;
import com.flybits.core.api.context.plugins.network.NetworkData;
import com.flybits.samples.context.R;
import com.flybits.samples.context.utilities.TimeUtils;

import java.util.ArrayList;

public class ContextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ACTIVITY          = 0;
    private static final int TYPE_BATTERY           = 1;
    private static final int TYPE_BEACON_IBEACON    = 2;
    private static final int TYPE_BEACON_EDDYSTONE  = 3;
    private static final int TYPE_CARRIER           = 4;
    private static final int TYPE_FITNESS           = 5;
    private static final int TYPE_LANGUAGE          = 6;
    private static final int TYPE_LOCATION          = 7;
    private static final int TYPE_NETWORK           = 8;

    private Context mContext;
    private ArrayList<BasicData> mListOfContextData;

    public ContextAdapter(Context context, ArrayList<BasicData> listOfContextData) {
        mListOfContextData  = listOfContextData;
        mContext            = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int code) {

        View v = null;
        if (code == TYPE_ACTIVITY) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
            return new ViewContextActivity(v);
        }else if (code == TYPE_BATTERY) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_battery, parent, false);
            return new ViewContextBattery(v);
        }else if (code == TYPE_BEACON_EDDYSTONE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beacon_eddystone, parent, false);
            return new ViewContextEddystone(v);
        }else if (code == TYPE_BEACON_IBEACON) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beacon_ibeacon, parent, false);
            return new ViewContextiBeacon(v);
        }else if (code == TYPE_CARRIER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrier, parent, false);
            return new ViewContextCarrier(v);
        }else if (code == TYPE_FITNESS) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fitness, parent, false);
            return new ViewContextFitness(v);
        }else if (code == TYPE_LANGUAGE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
            return new ViewContextLanguage(v);
        }else if (code == TYPE_LOCATION) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
            return new ViewContextLocation(v);
        }else if (code == TYPE_NETWORK) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_network, parent, false);
            return new ViewContextNetwork(v);
        }

        return new ViewContextActivity(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewContextActivity) {

            BasicData<ActivityData> data    = mListOfContextData.get(position);

            ViewContextActivity holderActivity  = (ViewContextActivity) holder;
            holderActivity.txtStationary.setText(mContext.getString(R.string.txtActivityStationary, String.valueOf(data.value.stationary)));
            holderActivity.txtWalking.setText(mContext.getString(R.string.txtActivityWalking, String.valueOf(data.value.walking)));
            holderActivity.txtRunning.setText(mContext.getString(R.string.txtActivityRunning, String.valueOf(data.value.running)));
            holderActivity.txtRidingBike.setText(mContext.getString(R.string.txtActivityOnBike, String.valueOf(data.value.cycling)));
            holderActivity.txtDriving.setText(mContext.getString(R.string.txtActivityDriving, String.valueOf(data.value.driving)));
            holderActivity.txtUnknown.setText(mContext.getString(R.string.txtActivityUnknown, String.valueOf(data.value.unknown)));
            holderActivity.txtTimeTaken.setText(mContext.getString(R.string.txtUpdatedAt, TimeUtils.getTimeAsString(data.timestamp * 1000)));

        }else if (holder instanceof ViewContextBattery) {

            BasicData<BatteryLifeData> data    = mListOfContextData.get(position);

            ViewContextBattery holderActivity  = (ViewContextBattery) holder;
            holderActivity.txtIsCharging.setText(mContext.getString(R.string.txtBatteryIsCharging, String.valueOf(data.value.isCharging)));
            holderActivity.txtPercentage.setText(mContext.getString(R.string.txtBatteryPercentage, data.value.percentage));
            holderActivity.txtTimeTaken.setText(mContext.getString(R.string.txtUpdatedAt, TimeUtils.getTimeAsString(data.timestamp * 1000)));

        }else if (holder instanceof ViewContextCarrier) {

            BasicData<CarrierData> data    = mListOfContextData.get(position);

            ViewContextCarrier holderActivity  = (ViewContextCarrier) holder;
            holderActivity.txtMCC.setText(mContext.getString(R.string.txtCarrierMCC, data.value.mcc));
            holderActivity.txtMNC.setText(mContext.getString(R.string.txtCarrierMNC, data.value.mnc));
            holderActivity.txtTimeTaken.setText(mContext.getString(R.string.txtUpdatedAt, TimeUtils.getTimeAsString(data.timestamp * 1000)));

        }else if (holder instanceof ViewContextLanguage) {

            BasicData<LanguageData> data    = mListOfContextData.get(position);

            ViewContextLanguage holderActivity  = (ViewContextLanguage) holder;
            holderActivity.txtLanguage.setText(mContext.getString(R.string.txtLanguageCode, data.value.language));
            holderActivity.txtTimeTaken.setText(mContext.getString(R.string.txtUpdatedAt, TimeUtils.getTimeAsString(data.timestamp * 1000)));

        }else if (holder instanceof ViewContextNetwork) {

            BasicData<NetworkData> data    = mListOfContextData.get(position);

            ViewContextNetwork holderActivity  = (ViewContextNetwork) holder;
            holderActivity.txtNetworkIsConnected.setText(mContext.getString(R.string.txtNetworkIsConnected, String.valueOf(data.value.isConnected)));

            String ssid = (data.value.ssid == null)? "Not Connected To WiFi" : data.value.ssid;
            holderActivity.txtNetworkSSID.setText(mContext.getString(R.string.txtNetworkSSID, ssid));

            String connectionType;
            switch ((int)data.value.connectionType){
                case 1:
                    connectionType = "WiFi";
                    break;
                case 2:
                    connectionType = "2G";
                    break;
                case 3:
                    connectionType = "3G";
                    break;
                case 4:
                    connectionType = "4G";
                    break;
                case -1:
                    connectionType = "Not Connected To Internet";
                    break;
                default:
                    connectionType = "Unknown";
            }
            holderActivity.txtNetworkConnectionType.setText(mContext.getString(R.string.txtNetworkConnectionType, connectionType));
            holderActivity.txtTimeTaken.setText(mContext.getString(R.string.txtUpdatedAt, TimeUtils.getTimeAsString(data.timestamp * 1000)));

        }else if (holder instanceof ViewContextFitness) {

            BasicData<FitnessData> data    = mListOfContextData.get(position);

            ViewContextFitness holderActivity  = (ViewContextFitness) holder;
            holderActivity.txtFitnessSteps.setText(mContext.getString(R.string.txtFitnessSteps, data.value.steps));
            holderActivity.txtTimeTaken.setText(mContext.getString(R.string.txtUpdatedAt, TimeUtils.getTimeAsString(data.timestamp * 1000)));

        }else if (holder instanceof ViewContextLocation) {

            BasicData<LocationData> data    = mListOfContextData.get(position);

            ViewContextLocation holderActivity  = (ViewContextLocation) holder;
            holderActivity.txtLocationLatitude.setText(mContext.getString(R.string.txtLocationLatitude, data.value.lat));
            holderActivity.txtLocationLongitude.setText(mContext.getString(R.string.txtLocationLongitude, data.value.lng));
            holderActivity.txtLocationAltitude.setText(mContext.getString(R.string.txtLocationAltitude, data.value.altitude));
            holderActivity.txtLocationBearing.setText(mContext.getString(R.string.txtLocationBearing, data.value.bearing));
            holderActivity.txtTimeTaken.setText(mContext.getString(R.string.txtUpdatedAt, TimeUtils.getTimeAsString(data.timestamp * 1000)));

        }else if (holder instanceof ViewContextEddystone) {

            BasicData<EddyStone> data    = mListOfContextData.get(position);

            ViewContextEddystone holderActivity  = (ViewContextEddystone) holder;
            holderActivity.txtEddystoneInstance.setText(mContext.getString(R.string.txtBeaconNamespace, data.value.namespace));
            holderActivity.txtEddystoneNamespace.setText(mContext.getString(R.string.txtBeaconInstance, data.value.instance));
            holderActivity.txtTimeTaken.setText(mContext.getString(R.string.txtUpdatedAt, TimeUtils.getTimeAsString(data.timestamp * 1000)));

        }else if (holder instanceof ViewContextiBeacon) {

            BasicData<IBeacon> data    = mListOfContextData.get(position);

            ViewContextiBeacon holderActivity  = (ViewContextiBeacon) holder;
            holderActivity.txtiBeaconMajorID.setText(mContext.getString(R.string.txtBeaconMajorID, data.value.majorID));
            holderActivity.txtiBeaconMinorID.setText(mContext.getString(R.string.txtBeaconMinorID, data.value.minorID));
            holderActivity.txtiBeaconUUID.setText(mContext.getString(R.string.txtBeaconUUID, data.value.uuid));
            holderActivity.txtTimeTaken.setText(mContext.getString(R.string.txtUpdatedAt, TimeUtils.getTimeAsString(data.timestamp * 1000)));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mListOfContextData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mListOfContextData.get(position).value instanceof ActivityData){
            return TYPE_ACTIVITY;
        }else if (mListOfContextData.get(position).value instanceof BatteryLifeData){
            return TYPE_BATTERY;
        }else if (mListOfContextData.get(position).value instanceof IBeacon){
            return TYPE_BEACON_IBEACON;
        }else if (mListOfContextData.get(position).value instanceof EddyStone){
            return TYPE_BEACON_EDDYSTONE;
        }else if (mListOfContextData.get(position).value instanceof FitnessData){
            return TYPE_FITNESS;
        }else if (mListOfContextData.get(position).value instanceof LanguageData){
            return TYPE_LANGUAGE;
        }else if (mListOfContextData.get(position).value instanceof LocationData){
            return TYPE_LOCATION;
        }else if (mListOfContextData.get(position).value instanceof NetworkData){
            return TYPE_NETWORK;
        }else if (mListOfContextData.get(position).value instanceof CarrierData){
            return TYPE_CARRIER;
        }
        return TYPE_ACTIVITY;
    }

    public static class ViewContextActivity extends RecyclerView.ViewHolder {

        public TextView txtStationary;
        public TextView txtWalking;
        public TextView txtRunning;
        public TextView txtRidingBike;
        public TextView txtDriving;
        public TextView txtUnknown;
        public TextView txtTimeTaken;

        public ViewContextActivity(View v) {
            super(v);

            txtStationary       = (TextView) v.findViewById(R.id.activityStationary);
            txtWalking          = (TextView) v.findViewById(R.id.activityWalking);
            txtRunning          = (TextView) v.findViewById(R.id.activityRunning);
            txtRidingBike       = (TextView) v.findViewById(R.id.activityRidingBike);
            txtDriving          = (TextView) v.findViewById(R.id.activityDriving);
            txtUnknown          = (TextView) v.findViewById(R.id.activityUnknown);
            txtTimeTaken        = (TextView) v.findViewById(R.id.timeTaken);
        }
    }

    public static class ViewContextBattery extends RecyclerView.ViewHolder {

        public TextView txtIsCharging;
        public TextView txtPercentage;
        public TextView txtTimeTaken;

        public ViewContextBattery(View v) {
            super(v);

            txtIsCharging       = (TextView) v.findViewById(R.id.batteryIsCharging);
            txtPercentage       = (TextView) v.findViewById(R.id.batteryPercentage);
            txtTimeTaken        = (TextView) v.findViewById(R.id.timeTaken);
        }
    }

    public static class ViewContextCarrier extends RecyclerView.ViewHolder {

        public TextView txtMNC;
        public TextView txtMCC;
        public TextView txtTimeTaken;

        public ViewContextCarrier(View v) {
            super(v);

            txtMNC       = (TextView) v.findViewById(R.id.carrierMNC);
            txtMCC       = (TextView) v.findViewById(R.id.carrierMCC);
            txtTimeTaken        = (TextView) v.findViewById(R.id.timeTaken);
        }
    }

    public static class ViewContextLanguage extends RecyclerView.ViewHolder {

        public TextView txtLanguage;
        public TextView txtTimeTaken;

        public ViewContextLanguage(View v) {
            super(v);

            txtLanguage       = (TextView) v.findViewById(R.id.languageCode);
            txtTimeTaken        = (TextView) v.findViewById(R.id.timeTaken);
        }
    }

    public static class ViewContextNetwork extends RecyclerView.ViewHolder {

        public TextView txtNetworkIsConnected;
        public TextView txtNetworkSSID;
        public TextView txtNetworkConnectionType;
        public TextView txtTimeTaken;

        public ViewContextNetwork(View v) {
            super(v);

            txtNetworkIsConnected       = (TextView) v.findViewById(R.id.networkIsConnected);
            txtNetworkSSID              = (TextView) v.findViewById(R.id.networkSSID);
            txtNetworkConnectionType    = (TextView) v.findViewById(R.id.networkConnectionType);
            txtTimeTaken        = (TextView) v.findViewById(R.id.timeTaken);
        }
    }

    public static class ViewContextFitness extends RecyclerView.ViewHolder {

        public TextView txtFitnessSteps;
        public TextView txtTimeTaken;

        public ViewContextFitness(View v) {
            super(v);

            txtFitnessSteps       = (TextView) v.findViewById(R.id.fitnessSteps);
            txtTimeTaken        = (TextView) v.findViewById(R.id.timeTaken);
        }
    }

    public static class ViewContextLocation extends RecyclerView.ViewHolder {

        public TextView txtLocationLatitude;
        public TextView txtLocationLongitude;
        public TextView txtLocationAltitude;
        public TextView txtLocationBearing;
        public TextView txtTimeTaken;

        public ViewContextLocation(View v) {
            super(v);

            txtLocationLatitude       = (TextView) v.findViewById(R.id.locationLat);
            txtLocationLongitude      = (TextView) v.findViewById(R.id.locationLng);
            txtLocationAltitude       = (TextView) v.findViewById(R.id.locationAltitude);
            txtLocationBearing        = (TextView) v.findViewById(R.id.locationBearing);
            txtTimeTaken        = (TextView) v.findViewById(R.id.timeTaken);
        }
    }

    public static class ViewContextiBeacon extends RecyclerView.ViewHolder {

        public TextView txtiBeaconMajorID;
        public TextView txtiBeaconMinorID;
        public TextView txtiBeaconUUID;
        public TextView txtTimeTaken;

        public ViewContextiBeacon(View v) {
            super(v);

            txtiBeaconMajorID       = (TextView) v.findViewById(R.id.beaconMajorID);
            txtiBeaconMinorID       = (TextView) v.findViewById(R.id.beaconMinorID);
            txtiBeaconUUID          = (TextView) v.findViewById(R.id.beaconUUID);
            txtTimeTaken        = (TextView) v.findViewById(R.id.timeTaken);
        }
    }

    public static class ViewContextEddystone extends RecyclerView.ViewHolder {

        public TextView txtEddystoneNamespace;
        public TextView txtEddystoneInstance;
        public TextView txtTimeTaken;

        public ViewContextEddystone(View v) {
            super(v);

            txtEddystoneNamespace       = (TextView) v.findViewById(R.id.beaconNamespace);
            txtEddystoneInstance        = (TextView) v.findViewById(R.id.beaconInstance);
            txtTimeTaken        = (TextView) v.findViewById(R.id.timeTaken);
        }
    }
}