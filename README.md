# Geb!
GEB (short for _Generative Event Bus_) is an event bus leveraging annotation processing to achieve maximum possible speed.

GEB is just a basic event bus in itself; the actual magician is the [processor](https://github.com/zaaarf/geb-processor).

## Installation
Just drop this and the annotation [processor](https://github.com/zaaarf/geb-processor) in your `build.gradle`, this is all on Maven Central!

```groovy
repositories {
	mavenCentral()
}

dependencies {
	implementation 'foo.zaaarf.geb:core:<version>'
	annotationProcessor 'foo.zaaarf.geb:processor:<version>'
}
```

### Versions before 0.5.0
Prior to that version, it used to be on a self-hosted maven under a different namespace.
I make no promises that this maven will remain running indefinitely.

```groovy
repositories {
	maven { url = 'https://maven.zaaarf.foo' }
}

dependencies {
	implementation 'ftbsc:geb:<version>'
	annotationProcessor 'ftbsc.geb:processor:<version>'
}
```


## Usage
Despite the unusual premise, GEB does not work much differently from other event buses.
You declare the bus, as follows:

```java
public static final GEB EVENT_BUS;
static {
	EVENT_BUS = new GEB();
	// optionally make it load the dispatchers right away
	EVENT_BUS.loadAndRegisterDispatchers(this.getClass().getClassLoader());
}
```

Noting that there's nothing stopping you from implementing `IBus` (the interface) in your own way.
Of course, it is not necessary to make it static or even a constant, I just like to do it that way in my projects.

To dispatch an event, create a new class implementing `IEvent` or `IEventCancelable` depending on your needs.
Note that `AbstractEventCancelable` exists to spare you some boilerplate.
You then simply pass that to the event bus wherever you want the event to happen:

```java
EVENT_BUS.handleEvent(new MyEvent());
```

As the docs note, `handleEvent` will return false or true, respectively, depending on whether your event was canceled or not.
Listening for the event is pretty straightforward, just annotate the method you want to turn into a listener:

```java
@Listen
public static void myEventListener(MyEvent event) {
	// your code to fire on event here!
}
```

Listeners have priorities (the higher it is, the earlier it's called), you can specify it in the annotation:
```java
@Listen(priority = 1)
```

Instance methods are also supported, provided that the instance is appropriately registered:

```java
EVENT_BUS.registerListener(myInstance);
```

Should be noted that the default `GEB` implementation of `IBus` currently assumes that these instances are singletons.
Only one listener per type should be registered at the same time (since `getClass()` is used for indexing).
This may change in the future.

### Sub-buses
As of `0.5.0`, support has been introduced for sub-buses, those being instances of `IBus` that can be registered under
another `IBus` to receive its events.

```java
EVENT_BUS.registerSubBus(someOtherBus, 1);
```

The integer represents priority, assuming that the parent bus always has a priority of 0.
This of course implies that listener priority is honored _internally_, but not _absolutely_.
It is an intrinsic limitation of how GEB works, and it will never be fixed.
That being said, you can work around it in a few ways, depending on the specific case.

## What's with the name?
"GEB Bus" kind of sounds like "Jeb Bush" and I think it's very funny. Please clap.
