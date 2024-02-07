![Game Jolt API Github](https://github.com/raeleus/game-jolt-api/assets/12948924/52d59e01-1579-4c63-8bc1-9885071fe61a)

# Game Jolt API

A library that allows the user to access the gamejolt.com API with libGDX.

## About

[Game Jolt](https://gamejolt.com/learn) is a platform for hosting and sharing games (desktop or html5). It includes an API that allows you to record high scores, save player data to the cloud, award trophies, and more. This library allows you to access this API from your libGDX game from all of the available backends. To use Game Jolt's API, the only requirement is that your game is hosted on the site. Your users do not have to have an account unless you want to take advantage of user specific data stores and high scores associated with their username and avatar. This is managed through game ID's, keys, usernames, and tokens. See the [Game Jolt API](https://gamejolt.com/game-api) docs for more details.

## How to use

The most simplistic use of the Game Jolt API is to submit a guest score. A utility method was made for this purpose:
```java
var gj = new GameJoltApi();

//You can find the gameID and key in your game's gamejolt settings under Game API >> API Settings.
String gameID = "869827";
//Care should be taken to keep your key secret. Do not publish your key to an open source repository.
String key = "78ac632c55945de5cb5f30b735246a8c";
String guest = "Some Player's Name";
long score = 2000L;

gj.addGuestScore(gameID, key, guest, score);
```
There are a number of utility methods like this available for casual use. In order to use the full power of the API, however, the rest of the library uses a combination of requests and listeners to access an array of options. Please see the [wiki](https://github.com/raeleus/game-jolt-api/wiki) for more details. An example of the Game Jolt API in action can be found [here](https://gamejolt.com/games/libgdx-gj-api-test/869827).

## How to Include Game Jolt API in your Project

Typical of most libGDX projects, Game Jolt API needs to be included as a Gradle dependency:

### Core Dependency
Add the following to your root build.gradle:
```groovy
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your core project:
```groovy
dependencies {
    ...
    implementation 'com.github.raeleus:game-jolt-api:0.0.2'
}
```

### HTML5 Dependency
Add the dependency to your html project of your root build.gradle if you want HTML5/GWT support:
```groovy
project(":html") {
    apply plugin: "gwt"
    apply plugin: "war"

    dependencies {
        ...
        implementation 'com.github.raeleus:game-jolt-api:0.0.2:sources'
    }
}
```

Add the following inherits line to your GdxDefinition.gwt.xml in the html project:  
`
<inherits name="com.github.raeleus.gamejoltapi" />
`
