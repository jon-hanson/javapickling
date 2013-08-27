javapickling
============

**Java Pickling** is a Java framework for pickling objects into target pickled formats and vice versa (i.e. unpickling).
It was inspired by the [Pickling Combinators paper](http://research.microsoft.com/en-us/um/people/akenn/fun/picklercombinators.pdf), and by the [Scala Pickling project](https://github.com/scala/pickling).

The pickling is driven by the static types of values, however it will also handle objects where the static type is unknown. At present it does not provide automatic of pickling of custom classes, ergo pickling of those types must be implemented by the user. The custom picklers only need be defined once - the same pickler will be used regardless of the pickled format.

The design supports pickling into multiple pickled formats - JSON and byte[] implementations are provided.

See ByteIOPicklerTest.java and JsonPicklerTest.java for example usage, however once set up this illustrates the API usage:

    void test(House house) {

        // Create a pickler.
        Pickler<House, JsonNode> pickler = jsonPickler.object(House.class);

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

## Documentation

The framework is based around two interfaces, described in more detail in the following sections. The `Pickler` interface allows the serialisation of types to be expressed independently of a specific target format. The `PicklerCore` interface provides a means for serialisation into a specific format to be expressed as a set of `Pickler`s for the base types.

### Pickler

jon-hanson/javapickling/blob/master/src/main/java/org/javapickling/core/Pickler.java

    public interface Pickler<T, PF> {
        PF pickle(T t, PF target) throws IOException;
        T unpickle(PF source) throws IOException;
    }

`Pickler` is the primary interface in the framework. Classes which provide a pickling implementation for a class T implement `Pickler<T, PF>`. The `PF` type parameter represents the target type (such as JsonNode), and remains a type parameter for the Pickler impementation class.

### PicklerCore

jon-hanson/javapickling/blob/master/src/main/java/org/javapickling/core/PicklerCore.java

## History

(or why do we need another Java serialisation library/framework ?)

The library was born out the need for a Java serialisation framework that satisfied the following requirements:

1. Must be Java-based and able to serialise any Java type, including Generics and Enums.
1. Multiple target formats must be supportable, with byte arrays and JSON being the inital set of target formats.
1. Boilerplate serialisation code for custom classes is acceptable but should be minimal, and should not have to be repeated for each target format.
1. Serialisers should be composable - it should be possible to express serialisers for classes as being composed of the serislisers for the constituent fields.
1. Reflection use should be minimised.
1. Serialisation should be driven by the Java types - static type information should be used when possible. Java code generation from IDL or similar is not allowed. Everything should be as strongly-typed as possible.
1. Performance should be reasonable - on a par with Java's built-in Serialisation (excluding Java Serialisation's initial Reflection-based start-up cost).

This seemed to rule out most, if not all, of the existing frameworks and libraries. I was already familiar with Parser Combinator style frameworks, having previously written a simple one in F#, and wanted something similar for serialisers.

Around about this time the paper on the [Scala Pickling project](https://github.com/scala/pickling) came out. This looked very interesting. Unfortunately, being Scala-based ruled it out, having failed to convince my colleagues to adopt Scala. Also the paper and the website documentation was light on implementation details, but it did lead me to the [Pickling Combinators paper](http://research.microsoft.com/en-us/um/people/akenn/fun/picklercombinators.pdf), which, although aimed at functional languages, had some interesting ideas which at first glance might translate to Java. One weekend of coding later I had a working implementation.
