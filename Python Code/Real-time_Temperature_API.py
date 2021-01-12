import json
import csv
from firebase import firebase
from urllib.request import urlopen
from apscheduler.schedulers.background import BackgroundScheduler



def Merge(dict1, dict2):
    res = {**dict1, **dict2}
    return res



def push_to_firebase():
    from firebase import firebase
    #with urlopen("http://api.weatherstack.com/current?access_key=0188bf6b55296dc25f3041c49fac986a&query=Dublin") as url:
    with urlopen("http://api.weatherstack.com/historical?access_key=0188bf6b55296dc25f3041c49fac986a&query=Dublin&historical_date=2020-12-17&hourly=1") as url:
        fileobj = url.read()
    fileobj = fileobj.decode("utf-8")
    data = json.loads(fileobj)
    records = Merge(data["current"], data["location"])
    records = dict((k, records[k]) for k in ('localtime','is_day', 'name','temperature', 'humidity','precip','cloudcover','uv_index'))
    print(records)
    firebase = firebase.FirebaseApplication('https://realtime-weatherdata-default-rtdb.firebaseio.com', None)
    firebase.put('/Sensor_Data',records['localtime'],records)

sched = BackgroundScheduler(daemon=True)
sched.add_job(push_to_firebase,'interval',seconds=65)
sched.start()
#sched.shutdown()
