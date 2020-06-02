# KissConfig    
![build](https://github.com/kiran002/kissconfig/workflows/build/badge.svg)
[![codecov](https://codecov.io/gh/kiran002/kissconfig/branch/master/graph/badge.svg)](https://codecov.io/gh/kiran002/kissconfig)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.kiran002/kissconfig-core_2.12.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.kiran002%22%20AND%20a:%22kissconfig-core_2.12%22)

## Intro

KissConfig ([KISS](https://en.wikipedia.org/wiki/KISS_principle) + Config) is a pure scala library for reading TypeSafe configuration. modelled after the Keep it stupid simple principle.

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

Field | Key | Extraction method
------|------|-------------------
name | name | getString
age | age | getInteger


### Which types does KissConfig support? 

The idea behind KissConfig is not to support every type available, but only the types types that are most used in configurations. KissConfig currently supports the following types,

 1. Primitive types + Strings
 2. Collections (Lists,Maps)
 3. Custom case classes
 4. Optional Types

_if you have a valid suggestion for some other types, please create an issue with the proper usecase and we can discuss it._

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
It is most often the case, that we want to follow different naming conventions in code and different naming conventions in Configuration. You should have the flexibility to chose the conventions that work the best for you, Using a resolution strategy you are able to define how a field name (in Scala) should be translated to its corresponding Key in the Configuration.

KissConfig provides two such conversions out of the box,

  1. camelCase to underscore 
      ```hocon
      underscore = {
        my_int = 5
        my_string = "myString"
        my_boolean = true
      }
      ```
    
  2. underscore to camelCase
        ```hocon
        camelcase = {
          myInt = 5
          myString = "myString"
          myBoolean = true
        }
        ```
## Usage

Kissconfig is currently available only for scala 2.12, but it is planned to add support for scala 2.11 and 2.13. 

### Import the dependency in your project

#### SBT
```
libraryDependencies += "com.github.kiran002" %% "kissconfig-core" % "1.0.2"
```

#### Gradle

```
for scala 2.11
implementation 'com.github.kiran002:kissconfig-core_2.11:1.0.2'

for scala 2.12
implementation 'com.github.kiran002:kissconfig-core_2.12:1.0.2'

for scala 2.13
implementation 'com.github.kiran002:kissconfig-core_2.13:1.0.2'
```

#### Maven

```
<dependency>
  <groupId>com.github.kiran002</groupId>
  <artifactId>kissconfig_<<scala_version>>/artifactId>
  <version>1.0.2</version>
</dependency>
```
## License

This project is licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)

## Contributing

If you find bugs while using KissConfig, please feel free to create an issue or a PR to patch the bug. If you would like to improve the software in any way (code, documentation, tests), please free to get in touch with mel


