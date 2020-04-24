# KissConfig    
![build](https://github.com/kiran002/kissconfig/workflows/build/badge.svg)

KissConfig short for Keep it Simple Stupid Configuration, is a pure scala library for reading TypeSafe configuration

### Why?

Dealing with configurations is an every day task, KissConfig aims provide a scala friendly library.
That helps scala developers deal with configurations as easily as possible. 

### How does it work?

KissConfig uses Scala reflection to determine fields and their types in runtime and extracts configuration using these fields name

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
_however there is one assumption that the keys are always strings_

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

