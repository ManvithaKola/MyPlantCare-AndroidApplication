## Python Code folder contains 4 python files:<br />

Real-time_Temperature_API.py : This program fetches the weather data using a REST API when run and uploads to real-time database(google firebase) <br />
data_fusion.py : This program collects data from both the sources(Android App and REST API) to integrate and push it to another real-time database <br />
send_email.py : Send email is called in the android app when the conditions are not met <br /><br />

## Steps:<br />
1. Download the MyLightApp and open it from android studio.<br />
2. Run the application on an android phone<br />
3. Run the time_Temperature_API.py from google colab after installing the dependency packages<br />
4. Run the data_fusion.py program to integrate data from both the sources<br />
