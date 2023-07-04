
# Dishpedia

An Android app that helps user to find new recipes or create their own


## Table of Contents

* [Demo](#Demo)
* [Screenshots](#Screenshots)
* [Features](#Features)
* [Tech Stack Used](#Tech-Stack)
* [Setup](#Setup)
* [Roadmap](#Roadmap)

## Demo

You can try the demo on a web browser by clicking [here](https://appetize.io/app/6phxn6r42pgmud7bbhxfjstyqy) or you can download the apk file [here](https://drive.google.com/file/d/1LQ4uZrR2lGsbpY3-KOn6UDi0svftxKcY/view?usp=sharing)


## Screenshots

| Screens                  | Screenshots                                                  |
|:-------------------------|:-------------------------------------------------------------|
| Home screen              | <img src="/ReadMe-Images/ss-1.png" width="350" height="630"> |
| Search screen            | <img src="/ReadMe-Images/ss-2.png" width="350" height="630"> |
| Recipe Info screen       | <img src="/ReadMe-Images/ss-3.png" width="350" height="630"> |
| My Recipes screen        | <img src="/ReadMe-Images/ss-4.png" width="350" height="630"> |
| Recipe entry/edit screen | <img src="/ReadMe-Images/ss-5.png" width="350" height="630"> |

## Features

- Search for recipes from the [Spoonacular](https://spoonacular.com/) database
- Get complete information of a recipe including its ingredients and procedure
- Search for recipes of a particular category
- Create your own recipes and save it offline
## Tech Stack

**UI:** Jetpack Compose

**Database:** SQLite

**Libraries:** Retrofit, Room, Coil, Accompanist


## Setup

To clone and run this application you'll need [Android Studio](https://developer.android.com/studio) installed on your computer.

Open Android Studio, and select 'Get from VCS'

![Setup-1](/ReadMe-Images/Setup-1.png)

Enter 'https://github.com/debesis27/Dishpedia.git' (without the quotes) in URL text field

![Setup-2](/ReadMe-Images/Setup-2.png)

Click Clone

![Setup-3](/ReadMe-Images/Setup-3.png)

Create a new account at [Spoonacular](https://spoonacular.com/registeremail) and enter your api key in [CommonUi.kt](https://github.com/debesis27/Dishpedia/blob/master/app/src/main/java/com/example/dishpedia/utils/Constants.kt)

![Setup-4](/ReadMe-Images/Setup-4.png)
## Roadmap

- Adding options to share recipe on Whatsapp, etc.

- Adding filters to search bar

- Adding voice search

- Adding photos for ingredients

- Improving Ingredients and Instructions screens

