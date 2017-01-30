package com.repleno.strangerlights;

import com.google.android.gms.maps.model.LatLng;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

public class TaskManager {
    private static TaskManager instance = null;
    private PubNub pubnub;
    private PNConfiguration pnC = new PNConfiguration();
    public String lightUpdate = "Auto";

    protected TaskManager(){
        pnC.setSubscribeKey("SUB_KEY");
        pnC.setPublishKey("PUB_KEY");
        pubnub = new PubNub(pnC);
    }

    public static TaskManager getInstance() {
        if(instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public void publishLocation(LatLng coord){
        String newMsg;
        if (lightUpdate.equals("Auto"))
            newMsg = lightUpdate;
        else
            newMsg = coord.toString();
        pubnub.publish()
                .message(newMsg)
                .channel("locations")
                .shouldStore(true)
                .usePOST(true)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (status.isError()) {
                            System.out.println("error happened while publishing: " + status.toString());
                        } else {
                            System.out.println("publish worked! timetoken: " + result.getTimetoken());
                        }
                    }
                });
    }

    public PubNub getPubnub(){
        return this.pubnub;
    }
}
