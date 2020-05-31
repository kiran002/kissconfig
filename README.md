# KissConfig    
![build](https://github.com/kiran002/kissconfig/workflows/build/badge.svg)
[![codecov](https://codecov.io/gh/kiran002/kissconfig/branch/master/graph/badge.svg)](https://codecov.io/gh/kiran002/kissconfig)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.kiran002/kissconfig-core_2.12.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.kiran002%22%20AND%20a:%22kissconfig-core_2.12%22)


KissConfig (KISS + Config) is a pure scala library for reading TypeSafe configuration. modelled after the Keep it stupid simple principle.

## Usage

Kissconfig is currently available only for scala 2.12, but it is planned to add support for scala 2.11 and 2.13. 

### Import the dependency in your project

#### SBT
```
libraryDependencies += "com.github.kiran002" % "kissconfig-core_2.12" % "1.0.1"
```

#### Gradle

```
implementation 'com.github.kiran002:kissconfig-core_2.12:1.0.1'
```

#### Maven

```
<dependency>
  <groupId>com.github.kiran002</groupId>
  <artifactId>kissconfig_2.12</artifactId>
  <version>1.0.1</version>
</dependency>
```

### Why? 

Dealing with configurations is an every day task and it should be simple. KissConfig aims to make this a reality built on top of LightBend Config, Define a model (case class) for the properties you need and point KissConfig to your Configuration and let it take over the messy parts. 

KissConfig is a pure scala library, meaning first class support to all scala types and no need interop with Java types.

### How does it work?

KissConfig, just requires two inputs 

  1. the configuration and 
  2. the model you want to populate,
  
Using Scala reflection KissConfig inspects each field (type and name) in runtime, the field name is used the source of the configuration and type is used to determine how the configuration will be extracted.

Example

The following is a sample model for a Person,

```scala
case class Person(name:String,age:Int)
```
and this is how its corresponding configuration looks like
```hocon
{
name = "John"
age = 34
}
```

field | key | extraction method
------|------|-------------------
name | name | getString
age | age | getInteger


### Which types does KissConfig support? 

The idea behind KissConfig is not to support every type available, but only the types types that are most used in configurations. 

Having said that, KissConfig currently supports the following types

 1. Primitive types + Strings
 2. Collections (Lists,Maps)
 3. Custom case classes
 4. Optional Types

#### Config examples


Configuration can be simple assignments with Integers, Strings, Booleans

```hocon
myInt = 5
myString = "myString"
myBoolean = true
```

Configuration can be list of Strings, Integers, Booleans

   ```hocon
   lists = ["1", "2", "3"]
   ```

Configuration can be maps (key value pairs)

```hocon
map = {
  "key1" = "val1"
  "key2" = "val2"
}
```

Configurations can be nested types and the fields are populated based on the fields of the custom types
but config should contain config for all the fields, otherwise this would throw an exception in runtime
the configuration below could correspond to the following case class
```scala
case class PrimaryTypes(myInt: Int, myString: String, myBoolean: Boolean)
```
```hocon
pt = {
  myInt = 5
  myString = "myString"
  myBoolean = true
}
```


Configuration can also be custom types of Lists and Maps
```hocon
lm = {
  lists = ["1", "2", "3"]
  map = {
    "key1" = "val1"
    "key2" = "val2"
  }
}
```

you decide the naming convention of your properties
just provide how the resolution between fieldname --> configKey is done
the rest is taken care by KissConfig

KissConfig proivdes (camelCase to underscore) conversion out of the box
```hocon
underscore = {
  my_int = 5
  my_string = "myString"
  my_boolean = true
}
```

It also provides (underscore to camelCase) conversion
```hocon
camelcase = {
  myInt = 5
  myString = "myString"
  myBoolean = true
}
```

