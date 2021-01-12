from firebase import firebase
import pandas as pd
import json
weather = 'https://realtime-weatherdata-default-rtdb.firebaseio.com/realtime-weatherdata-default-rtdb.json'
firebase = firebase.FirebaseApplication(weather, None)
def get_weather():
    #weather = 'https://realtime-weatherdata-default-rtdb.firebaseio.com/realtime-weatherdata-default-rtdb.json'
    #firebase = firebase.FirebaseApplication(weather, None)
    getResults = firebase.get('/Sensor_Data',None)
    data = getResults
    weatherdata = [] 
    for i in data:
        weatherdata.append(data[i])
    return(weatherdata[-1])
