### Contruction Management
<h2> 🚀 &nbsp;About The App</h2>
<p align="left">
The Construction Management App allows site workers, supervisors, and administrators to manage tasks, log work, check-in/out, track weather conditions, and communicate more efficiently.
It aims to replace traditional paper-based methods with a fully digital, easy-to-use solution, helping improve productivity, safety, and record-keeping on construction sites.
</p>
# 📖 Table of Contents

- [Technologies Used](#technologies-used)
- [Screenshots](#screenshots)
  - [Home Feed](#home-feed)
  - [Role-Based Login](#role-based-login)
  - [Logs Screen](#logs-screen)
  - [Settings Screen](#settings-screen)
  - [Weather Screen](#weather-screen)
- [Setup Instructions](#setup-instructions)


<h2> Technologies Used</h2>
<p>
A mobile application designed to simplify and modernise daily construction site operations.
Built with Android Studio, Jetpack Compose, Firebase, and Kotlin.
</p>
<p align="left">
<img src="https://github.com/devicons/devicon/blob/v2.16.0/icons/androidstudio/androidstudio-original.svg" alt="androidstudio" width="45" height="45"/>
<img src="https://github.com/devicons/devicon/blob/v2.16.0/icons/jetpackcompose/jetpackcompose-original.svg" alt="jetpackcompose" width="45" height="45"/>
<img src="https://github.com/devicons/devicon/blob/v2.16.0/icons/firebase/firebase-original.svg" alt="firebase" width="45" height="45"/>
<img src="https://github.com/devicons/devicon/blob/v2.16.0/icons/kotlin/kotlin-original.svg" alt="kotlin" width="45" height="45"/>
</p>
<p>
<ul>
<li>Android Studio (Jetpack Compose UI framework)
<li>Kotlin
<li>Firebase Authentication (Role-based login: Admin / Worker)
<li>Firebase Realtime Database (Data storage and sync)
<li>MVVM Architecture - StateFlow / ViewModel
<li>OpenWeatherMap API (Weather Forecast Integration)
<li>Offline Support / Syncing
<li>Custom Notifications via FCM
</ul>
</p>
<h2>Screenshots & Features</h2>
<h5>Home Feed</h5>
<p align="left"> The home feed for the workers comprises of an indication of how much progress the project is making, a check-in/check-out option, daily tasks given by the admin or manager, and a quote for motivation. </p>
<img src="https://github.com/user-attachments/assets/36cd4294-c452-4e59-960a-da66794bae6b"width="120"/>
<h5>Logs Screen</h5>
<p align="left">The log entry screen shows all of the previous logs that users have added. When adding a new log, the users will see the option to add a Title, Date, Time, Site Area, Description, and Media option. Users also have the option to update or delete the log if necessary. 
When the user clicks update, all of the existing information populates the row and then the user can change what is needed. </p>
<img src="https://github.com/user-attachments/assets/f18373ab-d598-4865-bc84-dce96e98fd9f"width="120"/>
<img src="https://github.com/user-attachments/assets/b764d93b-065b-4a12-bbc1-d39c89024917"width="120"/>
<img src="https://github.com/user-attachments/assets/7f09ed9c-6918-4026-99de-c2e1a99c086c"width="120"/>
<img src="https://github.com/user-attachments/assets/cee5179d-1cab-4bcb-8432-4f6d6d1ca36a"width="120"/>
<img src="https://github.com/user-attachments/assets/321f8f90-b01e-4ef2-ada2-731f41d2d4c7"width="120"/>
<h5>Role-based login</h5>
<p align="left"> Upon sign up you have to select your role within the company, and if you are an admin, you will have a different home feed from the workers which allows you to edit the project milestone progress and input daily tasks with a deletion option. 
  Using Firebase Cloud messaging the users will be notified every morning at 9:30am to inform them of the new tasks added.
  The home screen for admins does not have a check-in/check-out option as this role would typically be on a salary and therefore does not require this. </p>
<img src="https://github.com/user-attachments/assets/009999ee-a35a-4341-a0c2-80d2539c047d"width="120"/>
<img src="https://github.com/user-attachments/assets/864dc913-83f7-466d-96f2-9a0b9e6a53a8"width="120"/>
<img src="https://github.com/user-attachments/assets/007e5996-a520-4b5e-a5fb-b814a1f2e022"width="120"/>
<h5>Weather Screen</h5>
<p align="left"> I used Open Weather API that provides different query options I can include when calling the API. As you can see the queries that I included are daily and hourly weather so temperature and weather conditions. 
Open Weather also has an option to use their weather icons that is included with the temperature, and I used this to increase developer efficiency and avoid complicated coding to try and match the temperate with the exact icon.
If it is predicted to rain that day the row will be highlighted red, so it is clear.
 </p>
<img src="https://github.com/user-attachments/assets/d359f579-2869-43cd-a33f-5e0f8c0496ac"width="120"/>
<img src="https://github.com/user-attachments/assets/b757a788-3075-4483-81e3-d4c25f9a2246"width="120"/>
<h5>Settings Screen</h5>
<p align="left"> My settings screen consists of notifications which asks the user if they want to enable it. Once they do, a Firebase Cloud Messaging (FCM) token will be saved, and those users will receive notifications daily to review their tasks. Change language is now a stretch goal that will be outside of the projects scope. 
As you can see there is also an option for dark mode to increase user inclusivity and enhance user experience. Lastly, users can log out or permanently delete their account which will have a pop up to confirm if the user is sure that is what they want to do. 
This removes the users account from the database, and they will no longer be able to log back in after they click confirm. </p>
<img src="https://github.com/user-attachments/assets/72ffb531-75a0-45ac-8330-0a1c518247e9"width="120"/>
<img src="https://github.com/user-attachments/assets/161f6e9d-c5c7-4c1e-96d5-dba19dc4844e"width="120"/>
<h2>🔧 Setup Instructions </h2>
<p>Clone the repository:
<ul>
<li> 1. git clone https://github.com/joliciac/constructionmanagement.git
<li> 2. Open project in Android Studio
<li> 3. Add your google-services.json for Firebase configuration (Steps shown below)
<li> 4. Replace API keys where required (OpenWeatherMap API)
<li> 5. Build and run the project on emulator or physical device
</li></ul>
</p>
<p>Google-Services Json:
<ul>
<li> 6. Create a new project in [Firebase Console](https://console.firebase.google.com/)
<li> 7. Enable the following services:
    - Authentication (Email/Password)
    - Realtime Database
    - Storage (if applicable for media uploads)
<li> 8. Download the `google-services.json` file from Firebase Console
<li> 9. Place the file inside your Android project: app/google-services.json
<li> 10. Sync the project to apply changes
</li></ul>
</p>
