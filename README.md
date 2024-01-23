# Game Jolt API

A library that allows the user to access the gamejolt.com API with libGDX.

## About

[Game Jolt](https://gamejolt.com/learn) is a platform for hosting and sharing games. It includes an API that allows you to record high scores, save player data to the cloud, award trophies

## How to Include Game Jolt API in your Project

Typical of most libGDX projects, Stripe requires the Gradle setup to be included your project.

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
    implementation 'com.github.raeleus.stripe:stripe:1.4.5'
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
        implementation 'com.github.raeleus.stripe:stripe:1.4.5:sources'
    }
}
```

Add the following inherits line to your GdxDefinition.gwt.xml in the html project:  
`
<inherits name="com.ray3k.stripe" />
`
