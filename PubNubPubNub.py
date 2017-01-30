import serial
srl = serial.Serial('COM5', 9600)
srl.stopbits = serial.STOPBITS_ONE
import struct

from geopy.distance import vincenty
DIVISION_COEFF = 30

LIGHTS = [(49.2583414,-123.1979885),
          (49.2592432,-123.1978987)]
DISTANCE_THRESHOLD = 0.5

#srl.open()



from pubnub.pnconfiguration import PNConfiguration
from pubnub.pubnub import PubNub
from pubnub.callbacks import SubscribeCallback
from pubnub.enums import PNOperationType, PNStatusCategory
from pubnub.pubnub import PubNub, SubscribeListener
 
pnconfig = PNConfiguration()
pnconfig.subscribe_key = "sub-c-1161e458-cca0-11e6-b045-02ee2ddab7fe"
pnconfig.publish_key = "pub-c-7c7d5b0d-8d9f-4e4e-95fa-cc182a00ed4b"
pnconfig.ssl = False
 
pubnub = PubNub(pnconfig)
pubnub.subscribe().channels('locations').execute()

class MySubscribeCallback(SubscribeCallback):
    def status(self, pubnub, status):
        pass
        if status.operation == PNOperationType.PNSubscribeOperation \
                or status.operation == PNOperationType.PNUnsubscribeOperation:
            if status.category == PNStatusCategory.PNConnectedCategory:
                pass
            elif status.category == PNStatusCategory.PNReconnectedCategory:
                pass
            elif status.category == PNStatusCategory.PNDisconnectedCategory:
                pass
            elif status.category == PNStatusCategory.PNUnexpectedDisconnectCategory:
                pass
            elif status.category == PNStatusCategory.PNAccessDeniedCategory:
                pass
            else:
                pass
        elif status.operation == PNOperationType.PNSubscribeOperation:
            if status.is_error():
                pass
            else:
                pass
        else:
            pass
 
    def presence(self, pubnub, presence):
        pass  # handle incoming presence data
 
    def message(self, pubnub, message):
        if (message.message == "ON"):
            for i in range(0,len(LIGHTS)):
                print("LIGHTS ON")
                srl.write(struct.pack('>B', i))
                srl.write(b'1')
                srl.write(b'\n')
                pubnub.publish().channel("light_state").message(str(i)+"1").should_store(True).use_post(True).async(publish_callback)

                
        elif (message.message == "OFF"):
            for i in range(0,len(LIGHTS)):
                print("LIGHTS OFF")
                srl.write(struct.pack('>B', i))
                srl.write(b'0')
                srl.write(b'\n')
                pubnub.publish().channel("light_state").message(str(i)+"0").should_store(True).use_post(True).async(publish_callback)

                
        else:
            for i in range(0,len(LIGHTS)):
                dist_from_light = (vincenty(LIGHTS[i], message.message).meters/DIVISION_COEFF)
                print("DISTANCE FROM LIGHT" + str(i) + ": "+ str(dist_from_light))
                if (dist_from_light < DISTANCE_THRESHOLD):
                    srl.write(struct.pack('>B', i))
                    srl.write(b'1')
                    srl.write(b'\n')
                    
                    pubnub.publish().channel("light_state").message(str(i)+"1").should_store(True).use_post(True).async(publish_callback)
                else:
                    srl.write(struct.pack('>B', i))
                    srl.write(b'0')
                    srl.write(b'\n')
                    
                    pubnub.publish().channel("light_state").message(str(i)+"0").should_store(True).use_post(True).async(publish_callback)
        pass  
        
pubnub.add_listener(MySubscribeCallback())
pubnub.add_listener(SubscribeListener())

def publish_callback(result, status):
    pass
 





