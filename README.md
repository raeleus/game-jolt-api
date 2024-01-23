# Game Jolt API

A library that allows the user to access the gamejolt.com API with libGDX.

## About

[Game Jolt](https://gamejolt.com/learn) is a platform for hosting and sharing games. It includes an API that allows you to record high scores, save player data to the cloud, award trophies, and more. This library allows you to access this API from your libGDX game from all of the available backends. To use Game Jolt's API, the only requirement is that your game is hosted on the site. Your users do not have to have an account unless you want to take advantage of user specific data stores and high scores associated with their username and avatar. This is managed through game ID's, keys, usernames, and tokens. See the [Game Jolt API](https://gamejolt.com/game-api) docs for more details.

## How to use

The most simplistic use of the Game Jolt API is to submit a guest score. A utility method was made for this purpose:
```java
```
There are a number of utility methods like this available. However, the rest of the API uses a combination of requests and listeners to access the full array of options. Please see the [wiki](https://github.com/raeleus/game-jolt-api/wiki) for more details. An example of the Game Jolt API in action can be found [here](https://gamejolt.com/games/libgdx-gj-api-test/869827).

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
    implementation 'com.github.raeleus:gamejoltapi:0.0.1'
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
        implementation 'com.github.raeleus:gamejoltapi:0.0.1:sources'
    }
}
```

Add the following inherits line to your GdxDefinition.gwt.xml in the html project:  
`
<inherits name="com.ray3k.gamejoltapi" />
`
