javapickling
============

**Java Pickling** is a Java framework for pickling Java object and values into target pickled formats,
and unpickling the results back into the original objects and values.
"Pickling" essentially means serialisation.
The approach and design were inspired by the [Pickling Combinators paper](http://research.microsoft.com/en-us/um/people/akenn/fun/picklercombinators.pdf), and by the [Scala Pickling project](https://github.com/scala/pickling).

The pickling is driven by the static types of values,
however it will also handle objects where the static type is unknown.
At present it does not provide automatic of pickling of custom classes,
ergo pickling of those types must be implemented by the user.
The custom picklers only need be defined once - the same pickler will be used regardless of the pickled format.

The design supports pickling into multiple pickled formats - JSON, XML and byte[] implementations are provided.

See [ByteIOPicklerTest.java](http://github.com/jon-hanson/javapickling/blob/master/javapickling-core/src/test/java/org/javapickling/byteio/ByteIOPicklerTest.java)
and [JsonNodePicklerTest.java](http://github.com/jon-hanson/javapickling/blob/master/javapickling-json/src/test/java/org/javapickling/json/JsonNodePicklerTest.java) for example usage,
however once a PicklerCore set up this illustrates the API usage:

    void test(House house) {

        // Create a pickler.
        Pickler<House, JsonNode> pickler = jsonPickler.object_p(House.class);

        // Pickle a sample House object into a JsonNode.
        JsonNode node = pickler.pickle(house, null);

        // Unpickle the JsonNode back into a House.
        House house2 = pickler.unpickle(node);
    }

## Quick Start

1. Add the javapickling jar to your build.
1. For any custom classes you wish to pickle implement a `Pickler` class.
1. Either register your Pickler class with the `PicklerCore` implementation you're about to use, or use the `DefaultPickler` annotation to specify your Pickler.
1. Call `PicklerCore.pickle(obj, target)` to pickle your class into the target, and `obj = PicklerCore.unpickler(source)` to reconstitute a class from a source.

## Overview

The framework is based around two interfaces, described in more detail in the following sections.
The `Pickler` interface allows the pickling of types to be expressed, independently of a specific target format.
The `PicklerCore` interface provides a way for pickling into a specific format to be expressed as a set of `Pickler`s for the base types.

Since this is based on a combinator framework, picklers are composable.
In particular, a pickler for a class will be composed of picklers corresponding to the field types of that class.

### Pickler

[Source code](http://github.com/jon-hanson/javapickling/blob/master/javapickling-core/src/main/java/org/javapickling/core/Pickler.java)

    public interface Pickler<T, PF> {
        PF pickle(T t, PF target) throws IOException;
        T unpickle(PF source) throws IOException;
    }

A class which provides a pickling implementation for a class T implements `Pickler<T, PF>`.
The `PF` type parameter represents the target format type (such as JsonNode), and remains a type parameter for the Pickler impementation class.

Pickler implementations generally sub-class PicklerBase as this provides an implicit means of referencing the pickler methods in PicklerCore, such as string\_p() and integer\_p().
This means picklers can be expressed more concisely.

### PicklerCore

[Source code](http://github.com/jon-hanson/javapickling/blob/master/javapickling-core/src/main/java/org/javapickling/core/PicklerCore.java)

A class which provides an implementation of pickling to a specific format implements `PicklerCore<PF>`, where the PF type parameter specifies the target format. For example,

    public class JsonPicklerCore extends PicklerCoreBase<JsonNode> {
        // ...
    }

Implementations of `PicklerCore<PF>` provide `Pickler<T, PF>` implementations for the core types (primitives, collections and enums).
It also provides the tools required to facilitate implementing picklers for custom classes.

### Custom Class Pickling

"custom class" here means any class not directly supported by the framework.
Picklers for custom classes can take one of two forms.
If the class in question supports a direct conversion to a core type supported by the PicklerCore, such as String, then the pickler can delegate directly to the pickler for that type.
E.g.

    @DefaultPickler(MyTypePickler.class)
    public class MyType {
        public MyType(String s) {...}
        @Override public String toString() {...}
    }
    
    public class MyTypePickler<PF> extends PicklerBase<MyType, PF> {
        public MyTypePickler(PicklerCore<PF> core) {
            super(core);
        }
    
        @Override
        public PF pickle(MyType myType, PF target) throws IOException {
            return string_p().pickle(myType.toString(), target);
        }
    
        @Override
        public MyType unpickle(PF source) throws IOException {
            return new MyType(string_p().unpickle(source));
        }
    }

If the class to be pickled doesn't support conversion to and from a core type, then the more general approach is to express the pickler as a pickler for each field comprising the class. E.g.:

    @DefaultPickler(MyTypePickler.class)
    public class MyType {
        public final Integer id;
        public final String name;

        public MyType(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public class MyTypePickler<PF> extends PicklerBase<MyType, PF> {
        private final Field<Integer, PF> id = field("id", integer_p());
        private final Field<String, PF> name = field("name", string_p());

        public MyTypePickler(PicklerCore<PF> core) {
            super(core);
        }

        @Override
        public PF pickle(MyType myType, PF target) throws IOException {
            final FieldPickler<PF> mp = object_map().pickler(target);
            mp.field(id, myType.id);
            mp.field(name, myType.name);
            return mp.pickle(target);
        }

        @Override
        public MyType unpickle(PF source) throws IOException {
            final FieldUnpickler<PF> mu = object_map().unpickler(source);
            return new MyType(
                mu.field(id),
                mu.field(name));
        }
    }

A couple of things to note:
* The pickler for MyType is expressed in terms of the picklers for the field types which comprise MyType, namely Integer and String.
* The pickler is independent of the target format PF.

## Tutorial

In progress...

## History

(or why do we need another Java serialisation library/framework ?)

The library was born out the need for a Java serialisation framework that satisfied the following requirements:

1. Must be Java-based and able to serialise any Java type, including Collections, Enums, and all Generic types.
1. Multiple target formats must be supportable, with byte arrays and JSON being the inital set of target formats.
1. Boilerplate serialisation code for custom classes is acceptable but should be minimal, and should not have to be repeated for each target format.
1. Serialisers should be composable - it should be possible to express serialisers for classes as being composed of the serialisers for the constituent fields.
1. Reflection use should be minimised.
1. Serialisation should be driven by the Java types - static type information should be used when possible. Java code generation from IDL or similar is not allowed. Everything should be as strongly-typed as possible.
1. Performance should be reasonable - on a par with Java's built-in Serialisation (excluding Java Serialisation's initial Reflection-based start-up cost).

This seemed to rule out most, if not all, of the existing frameworks and libraries.
I was already familiar with Parser Combinator style frameworks, having previously written a simple one in F#, and wanted something similar for serialisers.

Around about this time the paper on the [Scala Pickling project](https://github.com/scala/pickling) came out.
This looked very interesting, uUnfortunately, being Scala-based ruled it out.
Also the paper and the website documentation was light on implementation details,
but it did lead me to the [Pickling Combinators paper](http://research.microsoft.com/en-us/um/people/akenn/fun/picklercombinators.pdf),
which, although aimed at functional languages, had some interesting ideas which at first glance might translate to Java.
One weekend of coding later I had a working implementation.
