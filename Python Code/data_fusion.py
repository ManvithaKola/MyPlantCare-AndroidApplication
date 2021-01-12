from firebase import firebase
import pandas as pd
import json
from get_last_weather_report import get_weather

light = 'https://light-data-default-rtdb.firebaseio.com/light-data-default-rtdb.json'
firebase1 = firebase.FirebaseApplication(light, None)
getResults1 = firebase1.get('',None)
data1 = getResults1
#print(data1)
print(type(data1))
lightdata = [] 
for i in data1:
    lightdata.append("{"+str(i)+":"+str(data1[i])+"}")
    #print(data['Sensor_Data'][i]) 
print(lightdata[-1])

weatherdata = get_weather()
print(weatherdata['localtime'][0:13])

firebase = firebase.FirebaseApplication('https://datafusion-cc8fc-default-rtdb.firebaseio.com', None)

for i in data1.keys():
    fusion_data = {}
    if weatherdata['localtime'][0:13] == i[0:13]:
        #print(weatherdata['localtime'], weatherdata['temperature'],data1[i])
        #print(str("{"+"Time:"+str(weatherdata['localtime'])+",Humidity:"+ str(weatherdata['humidity'])+",Temperature:"+ str(weatherdata['temperature'])+",Light:"+ str(data1[i])+"}"))
        fusion_data['Humidity']=weatherdata['humidity']
        fusion_data['Temperature']=weatherdata['temperature']
        fusion_data['Light']=data1[i]
        fusion_data['Timestamp']=weatherdata['localtime']
        print(fusion_data)
        firebase.put('/FusionData','record',fusion_data)
