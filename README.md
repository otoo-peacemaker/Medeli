# Medeli

Rules
----------
1. Please fork or clone the project to your local machine
2. Create a branch with your name from the dev branch.
3. Do your assignment, push and create a pull request.


Project resources.
----------------------
* [UI/UX Designs](https://www.figma.com/file/AyHDaS14pMbJRusNVHyEHj/Taha-Pharmacy-preview-(Community)?node-id=1%3A9718&t=TI6PpYfKv4xYCAdD-0)
* [Run the app here](https://appetize.io/app/3o5cacis55xgbegsi7ib3tokle?device=pixel6&osVersion=12.0&scale=75)
* [Demo](https://www.loom.com/share/337b0c571ba748bd8312e02e8dbf9056)
* [APK file](https://drive.google.com/file/d/1TgS2YMke2-Q3o63sWUPyPy5sJN7JUEf-/view)
* [medeli postman api docs](https://guglex.postman.co/workspace/Team-Workspace~612614aa-b160-4228-84e0-679db7b5e36c/api/c7454719-05fe-47b8-99fd-a22024fcaf93)

Current Project features
-----------------
* Support dark and light theme
* Login and registration with firebase
* Reset password
* continue with google
* search pharmacy on map
* Home page with categorized product.
* Product page with list of products
* Search functionality on categorized product.
* Sharesheet(share pharmacy between apps)
* To be continue.....


Libraries and tools
-------------------
* Android studio
* Kotlin
* coroutines
* navigation component
* LiveData
* DI with dagger/hilt
* room
* firebase auth and cloud store


Architectural design
----------------------
MVVM

While using any architecture pattern can be a benefit in our project, with MVVM it’s very simple to separate the business logic of our app from the GUI.

We’ll be using 
* Firebase authentication with Google 
* and Cloud Firestore as a back-end. 

The following diagram depict how we are going about the architecture.

![1_5b-8CCT6MvQrWrep4aQUIw](https://user-images.githubusercontent.com/43262139/202090128-d644079b-b295-4ab5-9731-905abb60457b.png)

This diagram comes straight out from the Android Architecture Components, except the fact that we’ll not be using Room for local persistence since Firestore already has its own caching mechanism, nor or web service as a remote data source, we’ll be simply using Firebase. As you can see, we have a ViewModel and a LiveData and we need to make them work together using the Repository pattern. This solution is very efficient and it’s actually recommended by the Android team.
