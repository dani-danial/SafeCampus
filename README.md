# 🛡️ SafeCampus

**SafeCampus** is a mobile safety reporting application designed for universities (specifically UiTM Melaka Kampus Jasin). It empowers students and staff to report incidents in real-time, view live safety announcements, and access emergency information.

This project integrates with a **Web Admin Dashboard** for managing reports and broadcasting news.

---

## 📱 Features

### 1. User Authentication
* **Secure Sign Up & Login:** Powered by **Firebase Authentication**.
* **Email Verification:** Ensures valid student/staff access.

### 2. Incident Reporting
* **Report Types:** Accident, Crime, Damaged Facilities, Suspicious Activity, Fire Hazard, and Others.
* **Location Tracking:**
    * **Auto-GPS:** Automatically detects the user's current location and time.
    * **Map Picker:** Integrated **OpenStreetMap (OSMDroid)** allowing users to manually pin incident locations.
* **Real-time Submission:** Reports are instantly saved to **Firebase Firestore**.

### 3. Live Announcements
* **Real-time Sync:** Students receive instant notifications and news from the Admin Dashboard without refreshing.
* **Categorized Alerts:** Visual indicators for "Emergency", "Alert", or "Info".

### 4. About & Credits
* Detailed list of the development team.
* Direct links to Mobile and Web repositories.

---

## 🛠️ Tech Stack

* **Language:** Java
* **IDE:** Android Studio Iguana/Jellyfish
* **Backend:** Google Firebase (Firestore Database & Authentication)
* **Maps:** OSMDroid (OpenStreetMap) & Google Location Services
* **UI:** Material Design Components

---

## 🚀 How to Run the App

### Prerequisites
* Android Studio installed.
* Java Development Kit (JDK) 1.8 or higher.

### Installation Steps

1.  **Clone the Repository**
    ```bash
    git clone [https://github.com/dani-danial/SafeCampus.git](https://github.com/dani-danial/SafeCampus.git)
    ```

2.  **Open in Android Studio**
    * File > Open > Select the `SafeCampus` folder.

3.  **Firebase Configuration (CRITICAL)**
    * This project requires a `google-services.json` file to connect to the database.
    * Obtain the file from the project lead (Danial) or your Firebase Console.
    * Place the file inside the `app/` folder:
        `SafeCampus/app/google-services.json`

4.  **Sync & Build**
    * Click "Sync Project with Gradle Files".
    * Run the app on an Emulator or Physical Device.

---

## 🔗 Related Repositories

This mobile app works in tandem with the **Admin Web Dashboard**:

* **📱 Mobile App Repo:** [github.com/dani-danial/SafeCampus](https://github.com/dani-danial/SafeCampus)
* **💻 Admin Web Repo:** [github.com/malsaaaa/SafeCampus](https://github.com/malsaaaa/SafeCampus)

---

## 👥 The Team (CS251)

**Mobile Application Team:**
1.  **Muhammad Danial Danny Bin Mazlan** (Lead Developer)
2.  Asfa Danish Bin Hezri
3.  Raisyah Sumaiyah Binti Muhamad Hakem
4.  Danial Hakim Bin Mohd Arif
5.  Nur Alisha Maisarah Binti Mohd Nizam

**Web Admin Team:**
1.  **Syed Danish Aslam Bin Syed Mohd Mahzan**

---

## 📄 License
This project is developed for the **CS251** course assignment.
© 2025 SafeCampus Group. All Rights Reserved.
